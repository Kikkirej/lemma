package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.RawStringArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ArgumentTypeFactory

class ConstantParameterArgumentKind extends AbstractArgumentKind {
    public static val IDENTIFIER = ArgumentKindIdentifier.CONSTANT_PARAMETER

    new() {
        super(IDENTIFIER, "Constant Parameter",
            ArgumentTypeFactory.fromIdentifier(RawStringArgumentType.IDENTIFIER))
    }
}