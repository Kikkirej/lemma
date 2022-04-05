package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration
import de.fhdo.lemma.eclipse.ui.ProgrammaticIntermediateModelTransformation
import de.fhdo.lemma.eclipse.ui.ServiceModelTransformationStrategy
import de.fhdo.lemma.eclipse.ui.OperationModelTransformationStrategy

class IntermediateModelOfImportArgumentType
    extends AbstractIntermediateModelArgumentTypeWithEnumValue<ImportedIntermediateModelKind> {
    public static val IDENTIFIER = ArgumentTypeIdentifier.INTERMEDIATE_MODEL_OF_IMPORT

    new() {
        super(IDENTIFIER, "Intermediate model of import")
    }

    override getValidLiteralStringValues() {
        return #{
            ImportedIntermediateModelKind.FIRST_DOMAIN_MODEL
                -> "Intermediate representation of first imported data model",
            ImportedIntermediateModelKind.FIRST_OPERATION_MODEL
                -> "Intermediate representation of first imported operation model",
            ImportedIntermediateModelKind.FIRST_SERVICE_MODEL
                -> "Intermediate representation of first imported service model"
        }
    }

    override checkValidValueInUserRepresentation(ProcessingConfiguration configuration,
        String value) {
        super.checkValidValueInUserRepresentation(configuration, value)

        // Might not have a source model when user selected a new project and the source model
        // input field got cleared
        val sourceModelFileExtension = configuration.sourceModelFile?.fileExtension
        if (sourceModelFileExtension === null)
            return

        val transformableFileExtensions = ProgrammaticIntermediateModelTransformation
            .getTransformationStrategy(sourceModelFileExtension)
            .modelFileTypeDescriptions
            .values
            .map[extensions]
            .flatten

        val intermediateModelKind = ImportedIntermediateModelKind
            .valueOf(stringValueToLiteralName(value))
        val importedModelFileExtensions = switch(intermediateModelKind) {
            case FIRST_DOMAIN_MODEL:
                ServiceModelTransformationStrategy.DATA_MODEL_FILE_EXTENSIONS
            case FIRST_OPERATION_MODEL:
                OperationModelTransformationStrategy.OPERATION_MODEL_FILE_EXTENSIONS
            case FIRST_SERVICE_MODEL:
                ServiceModelTransformationStrategy.SERVICE_MODEL_FILE_EXTENSIONS
        }

        if (transformableFileExtensions.exists[importedModelFileExtensions.contains(it)])
            return

        val kindHint = if (importedModelFileExtensions.size == 1)
                '''kind *.«importedModelFileExtensions.get(0)»'''
            else
                "kinds " + importedModelFileExtensions.map['''*.«it»'''].join(", ")
        throw new IllegalArgumentException("Source model intermediate transformation does not " +
            '''support transformation of imported model files of «kindHint»''')
    }
}