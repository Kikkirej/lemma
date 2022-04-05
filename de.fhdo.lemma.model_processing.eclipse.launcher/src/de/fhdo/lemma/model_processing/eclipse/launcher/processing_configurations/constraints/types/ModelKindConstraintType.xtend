package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types

import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils

class ModelKindConstraintType extends AbstractConstraintTypeWithEnumValue<LemmaModelKind> {
    public static val NAME = "LEMMA Model Kind"
    public static val IDENTIFIER = ConstraintTypeIdentifier.MODEL_KIND
    static val FILE_EXTENSIONS = #{
        LemmaModelKind.DOMAIN -> LemmaUiUtils.getFileExtensions(LemmaUiUtils.DATA_DSL_EDITOR_ID),
        LemmaModelKind.MAPPING
            -> LemmaUiUtils.getFileExtensions(LemmaUiUtils.MAPPING_DSL_EDITOR_ID),
        LemmaModelKind.OPERATION
            -> LemmaUiUtils.getFileExtensions(LemmaUiUtils.OPERATION_DSL_EDITOR_ID),
        LemmaModelKind.SERVICE
            -> LemmaUiUtils.getFileExtensions(LemmaUiUtils.SERVICE_DSL_EDITOR_ID),
        LemmaModelKind.TECHNOLOGY
            -> LemmaUiUtils.getFileExtensions(LemmaUiUtils.TECHNOLOGY_DSL_EDITOR_ID)
    }

    static def getFileExtensions(LemmaModelKind modelKind) {
        val extensions = FILE_EXTENSIONS.get(modelKind)
        if (extensions.nullOrEmpty)
            throw new IllegalArgumentException('''Model kind «modelKind» does not have file ''' +
                "extensions associated")

        return extensions
    }

    def stringValueToModelKind(String stringValue) {
        val literalName = stringValueToLiteralName(stringValue)
        if (literalName.nullOrEmpty)
            throw new IllegalArgumentException("No model kind found for string value " +
                '''"«stringValue»"''')

        return LemmaModelKind.valueOf(literalName)
    }

    new() {
        super(IDENTIFIER, NAME)
    }

    override getValidLiteralStringValues() {
        return LemmaModelKind.values.toMap([it], [it.name.toLowerCase.toFirstUpper])
    }
}