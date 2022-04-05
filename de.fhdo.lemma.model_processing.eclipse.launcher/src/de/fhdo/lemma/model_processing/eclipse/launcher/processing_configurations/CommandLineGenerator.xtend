package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations

import java.util.List
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.SourceModelArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelsOfAllImportsArgumentType
import de.fhdo.lemma.eclipse.ui.ProgrammaticIntermediateModelTransformation
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractIntermediateModelArgumentTypeWithEnumValue
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ImportedIntermediateModelKind
import de.fhdo.lemma.eclipse.ui.ServiceModelTransformationStrategy
import de.fhdo.lemma.eclipse.ui.OperationModelTransformationStrategy
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FileArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FolderArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AllImportedIntermediateModelKinds
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.RawStringArgumentType
import org.eclipse.core.resources.ResourcesPlugin
import org.apache.commons.io.FilenameUtils
import de.fhdo.lemma.eclipse.ui.ModelFile
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.StringPairArgumentType
import java.io.File
import org.eclipse.core.resources.IFile
import java.util.concurrent.TimeUnit
import org.apache.commons.io.output.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import org.eclipse.xtend.lib.annotations.Accessors
import java.io.IOException
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportWithAliasArgumentType

class CommandLineGenerator {
    static val NO_VALUE_STRING = "<NO VALUE GIVEN>"

    val ProcessingConfiguration configuration
    val ProgrammaticIntermediateModelTransformation silentTransformation
    val String domainModelFileTypeId
    val String serviceModelFileTypeId
    val String operationModelFileTypeId
    val collectedWarnings = <String>newArrayList

    new(ProcessingConfiguration configuration) {
        this.configuration = configuration
        silentTransformation = getSilentTransformationFor(configuration)
        domainModelFileTypeId = ServiceModelTransformationStrategy.DATA_MODEL_FILE_TYPE_ID
        operationModelFileTypeId = OperationModelTransformationStrategy.OPERATION_MODEL_FILE_TYPE_ID

        val transformationStrategy = silentTransformation?.strategy
        serviceModelFileTypeId = if (transformationStrategy !== null) {
                switch (transformationStrategy) {
                    ServiceModelTransformationStrategy:
                        ServiceModelTransformationStrategy.SERVICE_MODEL_FILE_TYPE_ID
                    OperationModelTransformationStrategy:
                        OperationModelTransformationStrategy.SERVICE_MODEL_FILE_TYPE_ID
                    default:
                        throw new IllegalArgumentException("Service model file type identifier " +
                            "not determinable for intermediate transformation strategy " +
                            transformationStrategy.class.simpleName)
                }
            } else
                null
    }

    private def getSilentTransformationFor(ProcessingConfiguration configuration) {
        val intermediateTransformatioRequired = configuration.arguments.exists[
            AbstractIntermediateModelArgumentTypeWithEnumValue.isAssignableFrom(it.type.class)
        ]

        if (!intermediateTransformatioRequired)
            return null
        else if (configuration.sourceModelFile === null)
            throw new IllegalArgumentException("Processing configuration specifies arguments " +
                "requiring intermediate model representations but no source model file for " +
                "intermediate transformations was given")

        return new ProgrammaticIntermediateModelTransformation(configuration.sourceModelFile)
    }

    def toPrintableCommandLine(CoherentCommandLineParts coherentCommandLineParts,
        String delimiter) {
        return coherentCommandLineParts.mergeParts.map[it.join(" ")].join(delimiter)
    }

    def toExecutableCommandLine(CoherentCommandLineParts coherentCommandLineParts) {
        // We again remove the quotes of parameter values with spaces because Java's ProcessBuilder
        // expects unquoted commandline options
        return coherentCommandLineParts.mergeAndFlattenParts.map[it.removeSurroundingQuotes].toList
    }

    private def removeSurroundingQuotes(String s) {
        return if (s.startsWith("\"") && s.endsWith("\""))
                s.substring(1, s.length-1)
            else
                s
    }

    def Pair<CoherentCommandLineParts, List<String>> generateCoherentCommandLineParts() {
        collectedWarnings.clear
        val commandLineParts = new CoherentCommandLineParts()

        val processorExecutablePath = if (!configuration.processorExecutablePath.nullOrEmpty)
                configuration.processorExecutablePath.quoteIfContainsSpaces
            // For all processor executable types besides "raw executables", we expect a processor
            // executable path
            else if (configuration.processorExecutableType !=
                ProcessorExecutableType.RAW_EXECUTABLE)
                "<NO PROCESSOR EXECUTABLE PATH SPECIFIED>"
            else
                ""

        val executionCommandParts = configuration.generateExecutionCommandParts()
        commandLineParts.addExecutionCommandPart(executionCommandParts.key)
        commandLineParts.addExecutionCommandParts(executionCommandParts.value)
        commandLineParts.addExecutionCommandPart(processorExecutablePath)

        val argumentValues = determineArgumentValues()
        configuration.arguments.forEach[argument |
            val argumentValuePairs = argumentValues.get(argument)
            if (argumentValuePairs !== null) {
                val argumentParameterParts = argumentValuePairs.unfoldWithoutNullValues()
                commandLineParts.addArgumentParameterParts(argumentParameterParts)
            }
        ]

        return commandLineParts -> collectedWarnings
    }

    static class CoherentCommandLineParts {
        val executionCommandParts = <List<String>>newArrayList
        val argumentParameters = <List<String>>newArrayList

        protected def addExecutionCommandPart(String part) {
            addExecutionCommandPart(#[part])
        }

        protected def addExecutionCommandPart(List<String> part) {
            executionCommandParts.add(part)
        }

        protected def addExecutionCommandParts(List<List<String>> parts) {
            executionCommandParts.addAll(parts)
        }

        protected def addArgumentParameterParts(List<List<String>> parts) {
            argumentParameters.addAll(parts)
        }

        protected def mergeParts() {
            val result = <List<String>>newArrayList
            result.addAll(executionCommandParts)
            result.addAll(argumentParameters)
            return result.unmodifiableView
        }

        protected def mergeAndFlattenParts() {
            return mergeParts.flatten.toList.unmodifiableView
        }
    }

    private static class CoherentParameterValuePairs {
        val coherentPairs = <Pair<String, String>>newArrayList

        new() {
            // NOOP
        }

        new(Argument argument) {
            add(argument)
        }

        private def getForCommandLine(String parameter) {
           return parameter ?: "<NO PARAMETER GIVEN>"
        }

        new(Argument argument, String value) {
            add(argument, value)
        }

        def add(Argument argument) {
            add(argument, null)
        }

        def add(Argument argument, String value) {
            add(argument.parameter, value)
        }

        def add(String parameter, String value) {
            coherentPairs.add(parameter.forCommandLine -> value)
        }

        def unfoldWithoutNullValues() {
            return coherentPairs.map[
                it.value !== null ? #[it.key, it.value] : #[it.key]
            ]
        }
    }

    private def Pair<List<String>, List<List<String>>> generateExecutionCommandParts(
        ProcessingConfiguration configuration
    ) {
        val command = configuration.processorBasicExecutionCommand
            ?.trim
            ?.quoteIfContainsSpaces
        if (command === null)
            return #["<NO BASIC EXECUTION COMMAND SPECIFIED>"] -> emptyList

        return switch(configuration.processorExecutableType) {
            case LOCAL_DOCKER_IMAGE: configuration.generateDockerExecutionCommandParts(command)
            case LOCAL_JAVA_PROGRAM: newArrayList(
                command,
                // Prevent illegal reflective access warnings until LEMMA is fully compatible with
                // the Java Platform Module System
                "--add-opens",
                "java.base/java.lang=ALL-UNNAMED",
                "-jar"
            ) -> emptyList
            case RAW_EXECUTABLE: newArrayList(command) -> emptyList
        }
    }

    private def Pair<List<String>, List<List<String>>> generateDockerExecutionCommandParts(
        ProcessingConfiguration configuration,
        String command
    ) {
        val commandParts = newArrayList(command, "run", "-i")
        val additionalCommandParameters = <List<String>>newArrayList()

        try {
            val userId = getUserInfo("id -u", "User ID")
            val groupId = getUserInfo("id -g", "User group ID")
            additionalCommandParameters.add(#["-u", '''«userId»:«groupId»'''])
        } catch(UserInfoNotDeterminable ex) {
            var message = ex.message
            if (!message.endsWith(".")) message = message + "."
            collectedWarnings.add('''«message» Docker container will run as "root".''')
        }

        val projectPath = configuration.sourceModelFile.project.location.makeAbsolute.toString
        val volumePaths = newHashSet(projectPath)
        configuration.arguments.forEach[
            if (it.type.identifier == FolderArgumentType.IDENTIFIER)
                volumePaths.add(it.value)
            else if (it.type.identifier == FileArgumentType.IDENTIFIER) {
                val absolutePath = new File(it.value).absolutePath
                volumePaths.add(FilenameUtils.getFullPathNoEndSeparator(absolutePath))
            }
        ]
        volumePaths.forEach[additionalCommandParameters.addAsDockerVolumeParameter(it)]

        return commandParts -> additionalCommandParameters
    }

    private def addAsDockerVolumeParameter(List<List<String>> parameters, String path) {
        var folder = path
        val fobj = new File(folder)
        if (!fobj.directory)
            folder = FilenameUtils.getFullPathNoEndSeparator(fobj.absolutePath)

        val volumeParameterParts = newArrayList("-v")
        volumeParameterParts.add('''«folder»:«folder»''')
        parameters.add(volumeParameterParts)
    }

    private def String getUserInfo(String command, String printablePart) {
        val userInfoResult = try {
            executeShellCommandBlocking(command, 50, 4)
        } catch (IOException ex) {
            throw new UserInfoNotDeterminable('''«printablePart» not determinable: «ex.message»''')
        }

        val userInfo = userInfoResult.value.trim
        if (userInfoResult.key != 0)
            throw new UserInfoNotDeterminable('''«printablePart» not determinable. Execution ''' +
                '''of "«command»" returned with exit code «userInfoResult.key»: ''' +
                userInfo)

        return userInfo
    }

    private static class UserInfoNotDeterminable extends Exception {
        new(String message) {
            super(message)
        }
    }

    private def quoteIfContainsSpaces(String s) {
        return if (s.nullOrEmpty)
                NO_VALUE_STRING
            else if (s.contains(" "))
                '''"«s»"'''
            else
                s
    }

    private def determineArgumentValues() {
        val argumentValues = <Argument, CoherentParameterValuePairs>newHashMap

        configuration.arguments.forEach[argument |
            val argumentValuePairs = switch(argument.type) {
                FileArgumentType |
                FolderArgumentType:
                    argument.generateFileSystemArgumentValue
                IntermediateModelArgumentType:
                    argument.generateIntermediateModelArgumentValue
                IntermediateModelOfImportArgumentType:
                    argument.generateIntermediateModelOfImportArgumentValue
                IntermediateModelsOfAllImportsArgumentType:
                    argument.generateIntermediateModelsOfAllImportsArgumentValues
                IntermediateModelOfImportWithAliasArgumentType:
                    argument.generateIntermediateModelOfImportWithAliasArgumentValue
                RawStringArgumentType:
                    argument.generateRawStringArgumentValue
                SourceModelArgumentType:
                    argument.generateSourceModelArgumentValue
                StringPairArgumentType:
                    argument.generateStringPairArgumentValue
                default: throw new IllegalArgumentException("Value determination for argument " +
                    '''type «argument.type.name» not supported''')
            }

            argumentValues.put(argument, argumentValuePairs)
        ]

        return argumentValues
    }

    private def generateFileSystemArgumentValue(Argument argument) {
        return new CoherentParameterValuePairs(argument,
            argument.value?.quoteIfContainsSpaces?.valueForCommandLine)
    }

    private def getValueForCommandLine(String value) {
        return if (!value.nullOrEmpty) value else NO_VALUE_STRING
    }

    private def generateIntermediateModelArgumentValue(Argument argument) {
        val value = silentTransformation.rootModelFile.absoluteTransformationTargetPath
            .quoteIfContainsSpaces
        return new CoherentParameterValuePairs(argument, value)
    }

    private def getAbsoluteTransformationTargetPath(ModelFile modelFile) {
        val transformationTargetPath = modelFile.transformationTargetPath
        if (transformationTargetPath === null)
            return null

        val projectFolder = modelFile?.file?.project?.location?.makeAbsolute?.toFile
        val parentFolder = projectFolder?.parent
        return if (parentFolder !== null)
                FilenameUtils.separatorsToSystem('''«parentFolder»«transformationTargetPath»''')
            else
                null
    }

    private def generateIntermediateModelOfImportArgumentValue(Argument argument) {
        val modelKind = ImportedIntermediateModelKind.valueOf(argument.value)

        val firstImportedModel = switch (modelKind) {
            case FIRST_DOMAIN_MODEL: getFirstRootModelChildOfType(domainModelFileTypeId)
            case FIRST_OPERATION_MODEL: getFirstRootModelChildOfType(operationModelFileTypeId)
            case FIRST_SERVICE_MODEL: getFirstRootModelChildOfType(serviceModelFileTypeId)
            default: throw new IllegalArgumentException("Retrieving the first imported model of " +
                '''kind «modelKind» is not supported''')
        }

        return if (firstImportedModel !== null) {
                val value = firstImportedModel
                    .absoluteTransformationTargetPath
                    .quoteIfContainsSpaces
                new CoherentParameterValuePairs(argument, value)
            } else
                null
    }

    private def getFirstRootModelChildOfType(String modelFileTypeId) {
        return silentTransformation.rootModelFile.children
            .findFirst[fileTypeDescription.fileType == modelFileTypeId]
    }

    private def generateIntermediateModelOfImportWithAliasArgumentValue(Argument argument) {
        val importedModel = silentTransformation.rootModelFile.children
            .findFirst[importAlias == argument.value]

        return if (importedModel !== null) {
                val value = importedModel.absoluteTransformationTargetPath.quoteIfContainsSpaces
                new CoherentParameterValuePairs(argument, value)
            } else
                null
    }

    private def generateIntermediateModelsOfAllImportsArgumentValues(Argument argument) {
        val modelKind = AllImportedIntermediateModelKinds.valueOf(argument.value)

        val importedModels = switch (modelKind) {
            case ALL_DOMAIN_MODELS: getRootModelChildrenOfType(domainModelFileTypeId)
            case ALL_OPERATION_MODELS: getRootModelChildrenOfType(operationModelFileTypeId)
            case ALL_SERVICE_MODELS: getRootModelChildrenOfType(serviceModelFileTypeId)
            default: throw new IllegalArgumentException("Retrieving all imported models of kind " +
                '''«modelKind» is not supported''')
        }

        val argumentValuePairs = new CoherentParameterValuePairs()
        importedModels.forEach[
            val path = absoluteTransformationTargetPath.quoteIfContainsSpaces
            argumentValuePairs.add(argument, path)
        ]
        return argumentValuePairs
    }

    private def getRootModelChildrenOfType(String modelFileTypeId) {
        return silentTransformation.rootModelFile.children
            .filter[fileTypeDescription.fileType == modelFileTypeId]
    }

    private def generateRawStringArgumentValue(Argument argument) {
        return new CoherentParameterValuePairs(argument)
    }

    private def generateSourceModelArgumentValue(Argument argument) {
        val absoluteSourceModelPath = configuration.sourceModelFile?.rawLocation?.makeAbsolute
            ?.toString?.quoteIfContainsSpaces
        return new CoherentParameterValuePairs(argument, absoluteSourceModelPath)
    }

    private def generateStringPairArgumentValue(Argument argument) {
        return new CoherentParameterValuePairs(argument,
            argument.value?.quoteIfContainsSpaces?.valueForCommandLine)
    }
}