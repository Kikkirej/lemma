package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types

class ArgumentTypeFactory {
    static val CREATED_TYPES = <ArgumentTypeIdentifier, AbstractArgumentType>newHashMap

    static def fromIdentifier(String identifierString) {
        if (identifierString.nullOrEmpty)
            return null

        try {
            return fromIdentifier(ArgumentTypeIdentifier.valueOf(identifierString))
        } catch (IllegalArgumentException ex) {
            throw unsupportedIdentifierException(identifierString)
        }
    }

    static def fromIdentifier(ArgumentTypeIdentifier identifier) {
        if (identifier === null)
            return null

        val existingType = CREATED_TYPES.get(identifier)
        if (existingType !== null)
            return existingType

        val createdType = switch (identifier) {
            case FileArgumentType.IDENTIFIER:
                new FileArgumentType()
            case FolderArgumentType.IDENTIFIER:
                new FolderArgumentType()
            case IntermediateModelArgumentType.IDENTIFIER:
                new IntermediateModelArgumentType()
            case IntermediateModelOfImportArgumentType.IDENTIFIER:
                new IntermediateModelOfImportArgumentType()
            case IntermediateModelsOfAllImportsArgumentType.IDENTIFIER:
                new IntermediateModelsOfAllImportsArgumentType()
            case IntermediateModelOfImportWithAliasArgumentType.IDENTIFIER:
                new IntermediateModelOfImportWithAliasArgumentType()
            case RawStringArgumentType.IDENTIFIER:
                new RawStringArgumentType()
            case SourceModelArgumentType.IDENTIFIER:
                new SourceModelArgumentType()
            case StringPairArgumentType.IDENTIFIER:
                new StringPairArgumentType()
            default:
                throw unsupportedIdentifierException(identifier.toString)
        }

        CREATED_TYPES.put(identifier, createdType)
        return createdType
    }

    private static def unsupportedIdentifierException(String identifierString) {
        return new IllegalArgumentException("Creation of argument type with identifier " +
            '''"«identifierString»" not supported''')
    }
}