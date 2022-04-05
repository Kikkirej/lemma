package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types

import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

class FilenameRegexConstraintType extends AbstractConstraintType {
    public static val NAME = "Filename Regex"
    public static val IDENTIFIER = ConstraintTypeIdentifier.FILENAME_REGEX

    new() {
        super(IDENTIFIER, NAME)
    }

    override checkValidValue(String value) {
        try {
            Pattern.compile(value)
        } catch (PatternSyntaxException ex) {
            throw new IllegalArgumentException(ex)
        }
    }
}
