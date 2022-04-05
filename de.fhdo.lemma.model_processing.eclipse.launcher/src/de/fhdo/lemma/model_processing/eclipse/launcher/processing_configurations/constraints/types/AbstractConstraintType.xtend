package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types

import org.eclipse.xtend.lib.annotations.Accessors

abstract class AbstractConstraintType {
    @Accessors(PUBLIC_GETTER)
    val ConstraintTypeIdentifier identifier

    @Accessors(PUBLIC_GETTER)
    val String name

    override equals(Object o) {
        return if (o === this)
            true
        else if (!(o instanceof AbstractConstraintType))
            false
        else
            identifier == (o as AbstractConstraintType).identifier
    }

    new(ConstraintTypeIdentifier identifier, String name) {
        if (identifier === null)
            throw new IllegalArgumentException("Constraint type identifier must not be null")
        else if (name.nullOrEmpty)
            throw new IllegalArgumentException("Constraint type name must not be null or empty")

        this.identifier = identifier
        this.name = name
    }

    abstract def void checkValidValue(String value)

    def void checkValidValueInUserRepresentation(String value) {
        checkValidValue(value)
    }

    def convertValueToUserRepresentation(String value) {
        return value
    }

    def convertValueToInternalRepresentation(String value) {
        return value
    }
}
