package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations

import org.eclipse.debug.core.model.ILaunchConfigurationDelegate
import org.eclipse.debug.core.ILaunchConfiguration
import org.eclipse.debug.core.ILaunch
import org.eclipse.core.runtime.IProgressMonitor
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.CommandLineGenerator
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.AbstractConstraintType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.Constraint
import java.util.List
import java.util.Map
import org.eclipse.ui.console.ConsolePlugin
import org.eclipse.ui.console.MessageConsole
import org.eclipse.core.runtime.IStatus
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration
import org.eclipse.ui.console.MessageConsoleStream
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractIntermediateModelArgumentTypeWithEnumValue
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.SubMonitor
import java.util.concurrent.TimeUnit
import org.eclipse.core.runtime.CoreException
import org.eclipse.jface.dialogs.MessageDialog
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.CommandLineGenerator.CoherentCommandLineParts
import org.eclipse.ui.PlatformUI
import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils
import java.util.ArrayDeque
import org.eclipse.xtext.diagnostics.Severity
import org.eclipse.xtext.resource.XtextResource
import de.fhdo.lemma.data.DataModel
import org.eclipse.xtext.validation.Issue
import org.eclipse.core.resources.IFile
import static de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants.*
import org.eclipse.debug.core.DebugPlugin
import org.eclipse.debug.core.IStreamListener
import org.eclipse.debug.core.model.IStreamMonitor

class LaunchConfigurationDelegate implements ILaunchConfigurationDelegate {
    public static val CONSOLE_NAME = "LEMMA Model Processor Run"
    public static val DISABLE_CONSOLE_CLEARING_LAUNCH_ATTRIBUTE = "lemma" +
        typeof(LaunchConfigurationDelegate).name + "DisableConsoleClearing"
    static val INDENT = "  "

    //var ProcessingConfiguration configuration
    var MessageConsoleStream infoStream
    var MessageConsoleStream errorStream
    var String CURRENT_HEADLINE

    override showCommandLine(ILaunchConfiguration launchConfiguration, String mode, ILaunch launch,
        IProgressMonitor monitor) {
        val configuration = ProcessingConfiguration.deserializeFrom(launchConfiguration)
        if (configuration === null)
            return null

        return try {
            val generator = new CommandLineGenerator(configuration)
            val partsAndWarnings = generator.generateCoherentCommandLineParts()
            partsAndWarnings.value.forEach[
                MessageDialog.openWarning(getActiveShell(), "Warning during commandline generation",
                    it)
            ]
            generator.toPrintableCommandLine(partsAndWarnings.key, "\n\t")
        } catch (Exception ex) {
            ex.showCommandLineGenerationError
            null
        }

        // TODO probably not necessary
        /*val constraintsPreview = constraintsPreview(configuration)
        if (!constraintsPreview.empty) {
            sb.append("\n\nThe command will be executable on target elements that adhere to " +
                "the following constraints:\n")
            sb.append(constraintsPreview)
        }*/
    }

    private def showCommandLineGenerationError(Exception ex) {
        var message = if (!ex.message.nullOrEmpty)
                ex.message
            else
                '''«ex.class.simpleName» occurred'''

        MessageDialog.openError(getActiveShell(), "Error during commandline generation", message)
    }

    override launch(ILaunchConfiguration launchConfiguration, String mode, ILaunch launch,
        IProgressMonitor monitor) {
        val configuration = ProcessingConfiguration.deserializeFrom(launchConfiguration)
        if (configuration === null)
            throw new CoreException(new Status(IStatus.ERROR, LaunchConfigurationDelegate,
                "LEMMA model processor configuration not readable. Launch aborted."))

        try {
            configuration.convertToUserRepresentation
            configuration.validateInUserRepresentation
        } catch (IllegalArgumentException ex) {
            throw new CoreException(new Status(IStatus.ERROR, LaunchConfigurationDelegate,
                "Invalid LEMMA model processor configuration:\n" + ex.message + "\n\nLaunch " +
                "aborted."
            ))
        }

        val console = getAndRevealConsole(CONSOLE_NAME)
        val disableConsoleClearing = launchConfiguration.getAttribute(DISABLE_CONSOLE_CLEARING_LAUNCH_ATTRIBUTE, false)
        if (!disableConsoleClearing)
            console.clearConsole
        infoStream = console.newMessageStream
        errorStream = newErrorMessageStream(console)

        if (configuration.sourceModelFile.erroneousModels()) {
            closeStreams()
            return
        }

        CURRENT_HEADLINE = "Executing LEMMA model processor configuration " +
            '''"«launchConfiguration.name»"'''

        infoStream.println(CURRENT_HEADLINE)

        val subMonitor = SubMonitor.convert(monitor, 1)
        if (configuration.intermediateTransformationRequired) {
            infoStream.printlnSep
            new IntermediateTransformationExecutor(
                configuration,
                infoStream,
                errorStream,
                [configuration.runModelProcessor(launch, subMonitor)],
                [handleTransformationErrorsOrCancelation]
            ).run()
        } else
            configuration.runModelProcessor(launch, subMonitor)
    }

    private def erroneousModels(IFile sourceModelFile) {
        val absoluteSourceModelPath = sourceModelFile.rawLocation.makeAbsolute.toString
        val erroneousModels = absoluteSourceModelPath.transitiveModelValidation()
        val modelErrors = erroneousModels.get(absoluteSourceModelPath) ?: #[]
        val importedModelErrors = erroneousModels.entrySet.filter[it.key != absoluteSourceModelPath]

        if (!modelErrors.empty) {
            errorStream.println("Model contains errors:\n\t" + modelErrors.join("\n\t"))
            errorStream.println()
        }

        if (!importedModelErrors.empty) {
            errorStream.println("Imported models contain errors:")
            importedModelErrors.forEach[
                errorStream.println('''«"\t"»- «it.key»:''')
                it.value.forEach[
                    errorStream.println('''«"\t\t"»- «it»''')
                ]
            ]
            errorStream.println()
        }

        if (!erroneousModels.empty)
            errorStream.println("Model processor execution aborted")

        return !erroneousModels.empty
    }

    private def transitiveModelValidation(String rootModelFilepath) {
        val erroneousModels = <String, List<String>>newHashMap

        val modelsToValidate = new ArrayDeque<String>
        modelsToValidate.push(rootModelFilepath)
        val validatedModels = <String>newHashSet

        while(!modelsToValidate.empty) {
            val modelPath = modelsToValidate.pop

            if (!validatedModels.contains(modelPath)) {
                var XtextResource xtextResource = null
                try {
                    xtextResource = LemmaUiUtils.loadXtextResource(modelPath)

                    // This will throw an IndexOutOfBoundsException if the model is empty, which is
                    // fine because none of LEMMA's model kinds may be empty
                    val modelRoot = xtextResource.contents.get(0)
                    makeImportPathsAbsoluteFromBasefilePath(modelRoot, rootModelFilepath)

                    val errorMessages = LemmaUiUtils.validateXtextResource(xtextResource)
                        .toErrorMessages
                    if (!errorMessages.empty)
                        erroneousModels.put(modelPath, errorMessages)
                } catch (Exception ex) {
                    erroneousModels.put(modelPath, #[ex.message])
                }

                val importedModelsPaths = try {
                    xtextResource?.getAbsolutePathsOfImportedModels(modelPath)
                } catch (Exception ex) {
                    erroneousModels.put(modelPath, #[ex.message])
                    null
                }

                importedModelsPaths?.forEach[
                    if (!validatedModels.contains(it))
                        modelsToValidate.add(it)
                ]
            }
        }

        return erroneousModels
    }

    private def toErrorMessages(List<Issue> issues) {
        return issues.filter[it.severity == Severity.ERROR].map[
            '''«it.lineNumber»:«it.column»: «it.message»'''
        ].toList
    }

    private def getAbsolutePathsOfImportedModels(XtextResource resource, String absoluteBasepath) {
        if (resource.contents.empty)
            return #{}

        val modelRoot = resource.contents.get(0)
        makeImportPathsAbsoluteFromBasefilePath(modelRoot, absoluteBasepath)
        return typedImports(modelRoot).map[it.value.importUri].toSet
    }

    private def printlnSep(MessageConsoleStream stream) {
        printlnSep(stream, CURRENT_HEADLINE.length)
    }

    private def void closeStreams() {
        infoStream.close()
        errorStream.close()
    }

    private def handleTransformationErrorsOrCancelation() {
        errorStream.println("\nModel processor execution aborted")
        closeStreams()
    }

    private def runModelProcessor(ProcessingConfiguration configuration, ILaunch launch,
        IProgressMonitor monitor) {
        infoStream.printlnSep

        val workdir = configuration.sourceModelFile.rawLocation.makeAbsolute.toFile.parentFile
        val commandLineGenerator = new CommandLineGenerator(configuration)

        var CoherentCommandLineParts commandLineParts = null
        try {
            val partsAndWarnings = commandLineGenerator.generateCoherentCommandLineParts()
            for (warning : partsAndWarnings.value) {
                var warningMessage = warning
                if (!warningMessage.endsWith(".")) warningMessage = warningMessage + "."
                if (!warningMessage.endsWith(" ")) warningMessage = warningMessage + " "
                warningMessage = warningMessage + "Do you want to continue?"
                val continue = askForContinuationAfterWarning("Warning during commandline " +
                    "generation", warningMessage)
                if (!continue) {
                    handleTransformationErrorsOrCancelation()
                    return
                }
            }

            commandLineParts = partsAndWarnings.key
        } catch (Exception ex) {
            getWorkbenchDisplay().syncExec([ex.showCommandLineGenerationError])
            handleTransformationErrorsOrCancelation()
            return
        }

        infoStream.println("Running model processor:")
        val printableCommandLine = commandLineGenerator.toPrintableCommandLine(commandLineParts,
            "\n" + INDENT.repeat(2))
        printlnIndent(infoStream, printableCommandLine)
        infoStream.println()

        try {
            val commandline = commandLineGenerator.toExecutableCommandLine(commandLineParts)
            /*val process = new ProcessBuilder(commandline).directory(workdir).start()
            process.inputStream.transferTo(infoStream)
            process.errorStream.transferTo(errorStream)*/

            /*val processObserver = new Runnable() {
                override run() {
                    var processDestroyed = false
                    while(process.alive && !processDestroyed) {
                        try {
                            process.waitFor(250, TimeUnit.MILLISECONDS)
                        } catch (InterruptedException ex) {
                            process.destroyForcibly
                            processDestroyed = true
                        }
                    }
                    if (process.exitValue == 0)
                        infoStream.println("Model processor execution finished")
                    else
                        errorStream.println("Model processor execution finished with errors " +
                            '''(exit code: «process.exitValue»)''')
                    closeStreams()
                    monitor.worked(1)
                    monitor.done()
                }
            }
            new Thread(processObserver).start()*/

            val process = DebugPlugin.exec(commandline, workdir)
            process.inputStream.transferTo(infoStream)
            process.errorStream.transferTo(errorStream)
            val debugProcess = DebugPlugin.newProcess(launch, process, "")
            // Reveal console again because DebugPlugin reveals its own one
            getAndRevealConsole(CONSOLE_NAME)
            /*debugProcess.streamsProxy.outputStreamMonitor.addListener(new IStreamListener() {
                override streamAppended(String text, IStreamMonitor monitor) {
                    infoStream.print(text)
                }
            })
            debugProcess.streamsProxy.errorStreamMonitor.addListener(new IStreamListener() {
                override streamAppended(String text, IStreamMonitor monitor) {
                    errorStream.print(text)
                }
            })*/
            val processObserver = new Runnable() {
                override run() {
                    var processDestroyed = false
                    while(process.alive && !processDestroyed) {
                        try {
                            process.waitFor(250, TimeUnit.MILLISECONDS)
                        } catch (InterruptedException ex) {
                            process.destroyForcibly
                            processDestroyed = true
                        }
                    }
                    if (process.exitValue == 0)
                        infoStream.println("Model processor execution finished")
                    else
                        errorStream.println("\nModel processor execution finished with errors " +
                            '''(exit code: «process.exitValue»)''')
                    closeStreams()
                    monitor.worked(1)
                    monitor.done()
                }
            }
            new Thread(processObserver).start()
        } catch(Exception ex) {
            infoStream.println()
            errorStream.println(ex.message)
            closeStreams()
            monitor.done()
            return
        }
        /*val eclipseProcessWrapper = DebugPlugin.newProcess(launch, systemProcess,
            "LEMMA model processor")
        eclipseProcessWrapper.streamsProxy.outputStreamMonitor.addListener(
            new IStreamListener() {
                override streamAppended(String text, IStreamMonitor monitor) {
                    infoStream.println(text)
                }
            }
        )
        eclipseProcessWrapper.streamsProxy.errorStreamMonitor.addListener(
            new IStreamListener() {
                override streamAppended(String text, IStreamMonitor monitor) {
                    errorStream.println(text)
                }
            }
        )*/
    }

    private def askForContinuationAfterWarning(String title, String message) {
        val continuationDialogRunnable = new Runnable() {
            var boolean continuationConfirmed

            override run() {
                val dialog = new MessageDialog(getActiveShell(), title, null, message,
                    MessageDialog.WARNING, #["Continue", "Cancel"], 0)
                continuationConfirmed = dialog.open() == 0
            }
        }

        getWorkbenchDisplay().syncExec(continuationDialogRunnable)
        return continuationDialogRunnable.continuationConfirmed
    }

    private def intermediateTransformationRequired(ProcessingConfiguration configuration) {
        return configuration.arguments
            .exists[type instanceof AbstractIntermediateModelArgumentTypeWithEnumValue]
    }

    // TODO probably not necessary
    /*private def constraintsPreview(ProcessingConfiguration configuration) {
        val constraintsPerType = configuration.constraints.groupBy[it.type.class]
        if (constraintsPerType.empty)
            return ""

        val previewStrings = newArrayList(
            constraintsPerType.previewForConstraintsOfType(
                ModelKindConstraintType,
                "LEMMA models of kind",
                "LEMMA models of one of the kinds"
            ),
            constraintsPerType.previewForConstraintsOfType(
                FilenameRegexConstraintType,
                "Files matching pattern",
                "Files matching one of the patterns"
            )
        )

        previewStrings.removeAll("")
        return previewStrings.map['''«"\t"»- «it»'''].join("\n\tAND\n")
    }*/

    private def previewForConstraintsOfType(
        Map<Class<? extends AbstractConstraintType>, List<Constraint>> constraintsPerType,
        Class<? extends AbstractConstraintType> type,
        String singularLabel,
        String pluralLabel
    ) {
        val constraints = constraintsPerType.get(type)
        if (constraints === null)
            return ""

        val sb = new StringBuffer
        if (constraints.size == 1)
            sb.append('''«singularLabel»: ''')
        else if (constraints.size > 1)
            sb.append('''«pluralLabel»: ''')

        sb.append(constraints.map[it.value].join(", "))
        return sb.toString()
    }
}