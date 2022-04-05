package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ArgumentTypeFactory;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelsOfAllImportsArgumentType;

@SuppressWarnings("all")
public class MultiValuedParameterArgumentKind extends AbstractArgumentKind {
  public static final ArgumentKindIdentifier IDENTIFIER = ArgumentKindIdentifier.MULTI_VALUED_PARAMETER;
  
  public MultiValuedParameterArgumentKind() {
    super(MultiValuedParameterArgumentKind.IDENTIFIER, "Multi-Valued Parameter", 
      ArgumentTypeFactory.fromIdentifier(IntermediateModelsOfAllImportsArgumentType.IDENTIFIER));
  }
}
