package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ArgumentTypeFactory;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.RawStringArgumentType;

@SuppressWarnings("all")
public class ConstantParameterArgumentKind extends AbstractArgumentKind {
  public static final ArgumentKindIdentifier IDENTIFIER = ArgumentKindIdentifier.CONSTANT_PARAMETER;
  
  public ConstantParameterArgumentKind() {
    super(ConstantParameterArgumentKind.IDENTIFIER, "Constant Parameter", 
      ArgumentTypeFactory.fromIdentifier(RawStringArgumentType.IDENTIFIER));
  }
}
