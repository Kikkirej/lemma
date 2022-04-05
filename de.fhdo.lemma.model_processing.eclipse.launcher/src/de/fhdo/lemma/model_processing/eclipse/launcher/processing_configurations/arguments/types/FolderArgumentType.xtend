package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types

import java.nio.file.Files
import java.nio.file.Paths
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration

class FolderArgumentType extends AbstractArgumentType {
    public static val IDENTIFIER = ArgumentTypeIdentifier.FOLDER

    new() {
        super(IDENTIFIER, "Folder")
    }

    override checkValidValue(ProcessingConfiguration processingConfiguration, String value) {
        if (value.nullOrEmpty)
            throw new IllegalArgumentException("Please select a folder")
        else if (!Files.isDirectory(Paths.get(value)))
            throw new IllegalArgumentException('''Folder "«value»" does not exist''')
    }
}