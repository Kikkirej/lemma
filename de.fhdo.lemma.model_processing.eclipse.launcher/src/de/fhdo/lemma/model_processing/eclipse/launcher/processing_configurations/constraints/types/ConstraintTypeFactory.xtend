package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types

class ConstraintTypeFactory {
    static val CREATED_TYPES = <ConstraintTypeIdentifier, AbstractConstraintType>newHashMap
    static def fromIdentifier(String identifierString) {
        if (identifierString.nullOrEmpty)
            return null

        try {
            return fromIdentifier(ConstraintTypeIdentifier.valueOf(identifierString))
        } catch (IllegalArgumentException ex) {
            throw unsupportedIdentifierException(identifierString)
        }
    }

    static def fromIdentifier(ConstraintTypeIdentifier identifier) {
        if (identifier === null)
            return null

        val existingType = CREATED_TYPES.get(identifier)
        if (existingType !== null)
            return existingType

        val createdType = switch (identifier) {
            case ConstraintTypeIdentifier.FILENAME_REGEX:
                new FilenameRegexConstraintType()
            case ConstraintTypeIdentifier.MODEL_KIND:
                new ModelKindConstraintType()
            default:
                throw unsupportedIdentifierException(identifier.toString)
        }

        CREATED_TYPES.put(identifier, createdType)
        return createdType
    }

    private static def unsupportedIdentifierException(String identifierString) {
        return new IllegalArgumentException("Creation of constraint type with identifier " +
            '''"«identifierString»" not supported''')
    }
}