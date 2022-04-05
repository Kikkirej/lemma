package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;

@SuppressWarnings("all")
public class RawStringArgumentType extends AbstractArgumentType {
  public static final ArgumentTypeIdentifier IDENTIFIER = ArgumentTypeIdentifier.RAW_STRING;
  
  public RawStringArgumentType() {
    super(RawStringArgumentType.IDENTIFIER, "Raw string");
  }
  
  @Override
  public void checkValidValue(final ProcessingConfiguration processingConfiguration, final String value) {
  }
}
