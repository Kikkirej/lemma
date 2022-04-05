package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration

class StringPairArgumentType extends AbstractArgumentType {
    public static val IDENTIFIER = ArgumentTypeIdentifier.STRING_PAIR

    new() {
        super(IDENTIFIER, "String pair")
    }

    override checkValidValue(ProcessingConfiguration processingConfiguration, String value) {
        if (value.nullOrEmpty)
            throw new IllegalArgumentException("Please specify a value")
    }
}