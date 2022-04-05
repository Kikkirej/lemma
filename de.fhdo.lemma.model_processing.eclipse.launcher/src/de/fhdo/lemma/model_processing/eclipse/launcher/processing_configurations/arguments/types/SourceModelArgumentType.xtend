package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types

class SourceModelArgumentType extends AbstractArgumentTypeWithEnumValueSelection<SourceModelKind> {
    public static val IDENTIFIER = ArgumentTypeIdentifier.SOURCE_MODEL

    new() {
        super(IDENTIFIER, "Source model")
    }

    override getValidLiteralStringValues() {
        return #{
            SourceModelKind.SELECTED_FILE -> "Selected model file"
        }
    }
}