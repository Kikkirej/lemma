package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types

import java.nio.file.Files
import java.nio.file.Paths
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration

class FileArgumentType extends AbstractArgumentType {
    public static val IDENTIFIER = ArgumentTypeIdentifier.FILE

    new() {
        super(IDENTIFIER, "File")
    }

    override checkValidValue(ProcessingConfiguration processingConfiguration, String value) {
        if (value.nullOrEmpty)
            throw new IllegalArgumentException("Please select a file")
        else if (!Files.isRegularFile(Paths.get(value)))
            throw new IllegalArgumentException('''File "«value»" does not exist''')
    }
}