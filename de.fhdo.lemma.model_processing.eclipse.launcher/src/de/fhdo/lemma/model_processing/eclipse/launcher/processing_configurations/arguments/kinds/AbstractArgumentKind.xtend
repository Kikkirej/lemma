package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds

import org.eclipse.xtend.lib.annotations.Accessors
import java.util.List
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType

abstract class AbstractArgumentKind {
    @Accessors(PUBLIC_GETTER)
    val ArgumentKindIdentifier identifier

    @Accessors(PUBLIC_GETTER)
    val String name

    val List<AbstractArgumentType> supportedArgumentTypes

    def getSupportedArgumentTypes() {
        return supportedArgumentTypes.unmodifiableView
    }

    override equals(Object o) {
        return if (o === this)
            true
        else if (!(o instanceof AbstractArgumentKind))
            false
        else
            identifier == (o as AbstractArgumentKind).identifier
    }

    new(ArgumentKindIdentifier identifier, String name,
        AbstractArgumentType... supportedArgumentTypes) {
        if (identifier === null)
            throw new IllegalArgumentException("Argument kind identifier must not be null")
        else if (name.nullOrEmpty)
            throw new IllegalArgumentException("Argument kind name must not be null or empty")
        else if (supportedArgumentTypes.empty)
            throw new IllegalArgumentException("Argument types must not be empty")

        this.identifier = identifier
        this.name = name
        this.supportedArgumentTypes = supportedArgumentTypes
    }

    final def checkValidArgumentType(AbstractArgumentType type) {
        if (!supportedArgumentTypes.contains(type))
            throw new IllegalArgumentException('''Argument kind "«name»" does not support ''' +
                '''argument type "«type.name»"''')
    }

    def firstSupportedArgumentType() {
        return supportedArgumentTypes.sortBy[name].get(0)
    }
}