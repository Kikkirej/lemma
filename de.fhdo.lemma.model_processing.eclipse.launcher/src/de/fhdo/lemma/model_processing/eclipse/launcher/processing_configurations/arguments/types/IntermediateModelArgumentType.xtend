package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types

class IntermediateModelArgumentType
    extends AbstractIntermediateModelArgumentTypeWithEnumValue<IntermediateModelKind> {
    public static val IDENTIFIER = ArgumentTypeIdentifier.INTERMEDIATE_MODEL

    new() {
        super(IDENTIFIER, "Intermediate model")
    }

    override getValidLiteralStringValues() {
        return #{
            IntermediateModelKind.SELECTED_FILE -> "Intermediate representation of selected " +
                "model file"
        }
    }
}