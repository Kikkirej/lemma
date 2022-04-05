package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class FilenameRegexConstraintType extends AbstractConstraintType {
  public static final String NAME = "Filename Regex";
  
  public static final ConstraintTypeIdentifier IDENTIFIER = ConstraintTypeIdentifier.FILENAME_REGEX;
  
  public FilenameRegexConstraintType() {
    super(FilenameRegexConstraintType.IDENTIFIER, FilenameRegexConstraintType.NAME);
  }
  
  @Override
  public void checkValidValue(final String value) {
    try {
      Pattern.compile(value);
    } catch (final Throwable _t) {
      if (_t instanceof PatternSyntaxException) {
        final PatternSyntaxException ex = (PatternSyntaxException)_t;
        throw new IllegalArgumentException(ex);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
}
