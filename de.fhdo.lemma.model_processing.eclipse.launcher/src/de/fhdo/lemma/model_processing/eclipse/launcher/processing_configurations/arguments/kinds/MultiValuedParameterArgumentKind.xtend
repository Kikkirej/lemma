package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelsOfAllImportsArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ArgumentTypeFactory

class MultiValuedParameterArgumentKind extends AbstractArgumentKind {
    public static val IDENTIFIER = ArgumentKindIdentifier.MULTI_VALUED_PARAMETER

    new() {
        super(IDENTIFIER, "Multi-Valued Parameter",
            ArgumentTypeFactory.fromIdentifier(IntermediateModelsOfAllImportsArgumentType.IDENTIFIER))
    }
}