package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration
import de.fhdo.lemma.eclipse.ui.ProgrammaticIntermediateModelTransformation
import de.fhdo.lemma.eclipse.ui.ServiceModelTransformationStrategy
import de.fhdo.lemma.eclipse.ui.OperationModelTransformationStrategy

class IntermediateModelsOfAllImportsArgumentType
    extends AbstractIntermediateModelArgumentTypeWithEnumValue<AllImportedIntermediateModelKinds> {
    public static val IDENTIFIER = ArgumentTypeIdentifier.INTERMEDIATE_MODELS_OF_ALL_IMPORTS

    new() {
        super(IDENTIFIER, "Intermediate models of all imports")
    }

    override getValidLiteralStringValues() {
        return #{
            AllImportedIntermediateModelKinds.ALL_DOMAIN_MODELS
                -> "Intermediate representations of all imported data models",
            AllImportedIntermediateModelKinds.ALL_OPERATION_MODELS
                -> "Intermediate representations of all imported operation models",
            AllImportedIntermediateModelKinds.ALL_SERVICE_MODELS
                -> "Intermediate representations of all imported service models"
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

        val intermediateModelKind = AllImportedIntermediateModelKinds
            .valueOf(stringValueToLiteralName(value))
        val importedModelFileExtensions = switch(intermediateModelKind) {
            case ALL_DOMAIN_MODELS:
                ServiceModelTransformationStrategy.DATA_MODEL_FILE_EXTENSIONS
            case ALL_OPERATION_MODELS:
                OperationModelTransformationStrategy.OPERATION_MODEL_FILE_EXTENSIONS
            case ALL_SERVICE_MODELS:
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