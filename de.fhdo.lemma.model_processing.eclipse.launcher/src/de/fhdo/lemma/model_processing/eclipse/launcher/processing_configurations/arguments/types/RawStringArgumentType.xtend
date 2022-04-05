package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration

class RawStringArgumentType extends AbstractArgumentType {
    public static val IDENTIFIER = ArgumentTypeIdentifier.RAW_STRING

    new() {
        super(IDENTIFIER, "Raw string")
    }

    override checkValidValue(ProcessingConfiguration processingConfiguration, String value) {
        // NOOP
    }
}