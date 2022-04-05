package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration
import de.fhdo.lemma.eclipse.ui.ProgrammaticIntermediateModelTransformation

abstract class AbstractIntermediateModelArgumentTypeWithEnumValue<T extends Enum<?>>
    extends AbstractArgumentTypeWithEnumValueSelection<T> {

    new(ArgumentTypeIdentifier identifier, String name) {
        super(identifier, name)
    }

    override checkValidValueInUserRepresentation(ProcessingConfiguration configuration,
        String value) {
        super.checkValidValueInUserRepresentation(configuration, value)
        val sourceModelFile = configuration.sourceModelFile
        // Might not have a source model when user selected a new project and the source model
        // input field got cleared
        if (sourceModelFile === null) {
            return
        }

        if (!ProgrammaticIntermediateModelTransformation.supportsTranformation(sourceModelFile))
            throw new IllegalArgumentException("Intermediate transformation of model files of " +
                '''kind *.«sourceModelFile.fileExtension» is not supported''')
    }
}
