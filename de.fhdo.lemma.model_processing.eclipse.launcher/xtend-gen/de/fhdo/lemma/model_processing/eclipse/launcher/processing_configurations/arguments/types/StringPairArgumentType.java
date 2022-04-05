package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class StringPairArgumentType extends AbstractArgumentType {
  public static final ArgumentTypeIdentifier IDENTIFIER = ArgumentTypeIdentifier.STRING_PAIR;
  
  public StringPairArgumentType() {
    super(StringPairArgumentType.IDENTIFIER, "String pair");
  }
  
  @Override
  public void checkValidValue(final ProcessingConfiguration processingConfiguration, final String value) {
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(value);
    if (_isNullOrEmpty) {
      throw new IllegalArgumentException("Please specify a value");
    }
  }
}
