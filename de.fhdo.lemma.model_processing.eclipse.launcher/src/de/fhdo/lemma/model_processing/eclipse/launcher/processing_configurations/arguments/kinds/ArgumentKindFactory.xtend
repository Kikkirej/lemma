package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds

class ArgumentKindFactory {
    static val CREATED_KINDS = <ArgumentKindIdentifier, AbstractArgumentKind>newHashMap

    static def fromIdentifier(String identifierString) {
        if (identifierString.nullOrEmpty)
            return null

        try {
            return fromIdentifier(ArgumentKindIdentifier.valueOf(identifierString))
        } catch (IllegalArgumentException ex) {
            throw unsupportedIdentifierException(identifierString)
        }
    }

    static def fromIdentifier(ArgumentKindIdentifier identifier) {
        if (identifier === null)
            return null

        val existingKind = CREATED_KINDS.get(identifier)
        if (existingKind !== null)
            return existingKind

        val createdType = switch (identifier) {
            case ConstantParameterArgumentKind.IDENTIFIER:
                new ConstantParameterArgumentKind()
            case MultiValuedParameterArgumentKind.IDENTIFIER:
                new MultiValuedParameterArgumentKind()
            case SingleValuedParameterArgumentKind.IDENTIFIER:
                new SingleValuedParameterArgumentKind()
            default:
                throw unsupportedIdentifierException(identifier.toString)
        }

        CREATED_KINDS.put(identifier, createdType)
        return createdType
    }

    private static def unsupportedIdentifierException(String identifierString) {
        return new IllegalArgumentException("Creation of argument kind with identifier " +
            '''"«identifierString»" not supported''')
    }
}