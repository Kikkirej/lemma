package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types

import org.eclipse.xtend.lib.annotations.Accessors
import java.util.List
import java.util.Collections
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration

abstract class AbstractArgumentType {
    @Accessors(PUBLIC_GETTER)
    val ArgumentTypeIdentifier identifier

    @Accessors(PUBLIC_GETTER)
    val String name

    val List<String> possibleValues

    def getPossibleValues() {
        return possibleValues.unmodifiableView
    }

    override equals(Object o) {
        return if (o === this)
            true
        else if (!(o instanceof AbstractArgumentType))
            false
        else
            name == (o as AbstractArgumentType).name
    }

    new(ArgumentTypeIdentifier identifier, String name) {
        this(identifier, name, emptyList)
    }

    new(ArgumentTypeIdentifier identifier, String name, List<String> possibleValues) {
        if (identifier === null)
            throw new IllegalArgumentException("Argument type identifier must not be null")
        else if (name.nullOrEmpty)
            throw new IllegalArgumentException("Argument type name must not be null or empty")
        else if (possibleValues === null)
            throw new IllegalArgumentException("List of possible values must not be null")

        this.identifier = identifier
        this.name = name
        this.possibleValues = newArrayList(possibleValues)
        Collections.sort(this.possibleValues)
    }

    /*final def checkValidValueInternal(String value) {
        if (!possibleValues.isEmpty && !possibleValues.contains(value))
            throw new IllegalArgumentException('''«value» is an invalid value for the argument ''' +
                '''type (expected values: «possibleValues.join(", ")»)''')
        else
            checkValidValue(value)
    }*/

    abstract def void checkValidValue(ProcessingConfiguration processingConfiguration, String value)

    /*final def defaultValue() {
        return if (!possibleValues.empty)
                possibleValues.get(0)
            else
                ""
    }*/

    def void checkValidValueInUserRepresentation(ProcessingConfiguration configuration,
        String value) {
        checkValidValue(configuration, value)
    }

    def convertValueToUserRepresentation(String value) {
        return value
    }

    def convertValueToInternalRepresentation(String value) {
        return value
    }

    /*def String serializeValueToXml(String value) {
        return value
    }*/
}