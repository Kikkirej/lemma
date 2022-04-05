package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations

import java.util.List
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.Constraint
import java.util.regex.Pattern
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.ModelKindConstraintType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.LemmaModelKind
import java.util.Set
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.FilenameRegexConstraintType
import org.apache.commons.io.FilenameUtils

// TODO probably not necessary
class ConstraintsMatcher {
    val Set<String> validModelKindExtensions
    val Set<Pattern> validFilenamePatterns

    new(List<Constraint> constraints) {
        validModelKindExtensions = constraints
            .filter[type instanceof ModelKindConstraintType && !value.nullOrEmpty]
            .map[ModelKindConstraintType.getFileExtensions(LemmaModelKind.valueOf(value))]
            .flatten
            .toSet

        validFilenamePatterns = constraints
            .filter[type instanceof FilenameRegexConstraintType && !value.nullOrEmpty]
            .map[value]
            // Remove possible duplicate pattern strings because the Pattern class doesn't support
            // effective comparison of Pattern objects by their defining pattern strings
            .toSet
            .map[Pattern.compile(it)]
            .toSet
    }

    def matches(String filename) {
        val validExtension = validModelKindExtensions.empty ||
            validModelKindExtensions.contains(FilenameUtils.getExtension(filename))

        val validFilenamePattern = validFilenamePatterns.empty ||
            validFilenamePatterns.exists[matcher(filename).matches()]

        return validExtension && validFilenamePattern
    }
}