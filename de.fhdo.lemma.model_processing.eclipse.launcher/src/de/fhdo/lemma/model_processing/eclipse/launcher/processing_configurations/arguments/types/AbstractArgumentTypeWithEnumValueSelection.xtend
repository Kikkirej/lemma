package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types

import java.util.Map
import java.util.List
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration

abstract class AbstractArgumentTypeWithEnumValueSelection<T extends Enum<?>>
    extends AbstractArgumentType {
    val String enumClassName
    val BiMap<String, String> validLiteralNamesAndStringValues
    val List<String> stringValuesSorted

    override equals(Object o) {
        return if (o === this)
            true
        else if (!(o instanceof AbstractArgumentType))
            false
        else
            name == (o as AbstractArgumentType).name
    }

    new(ArgumentTypeIdentifier identifier, String name) {
        super(identifier, name)

        val validLiteralStringValues = getValidLiteralStringValues()
        if (validLiteralStringValues.empty)
            throw new IllegalArgumentException("No string values given")

        enumClassName = validLiteralStringValues.keySet.toList.get(0).class.simpleName

        if (validLiteralStringValues.values.size != validLiteralStringValues.values.toSet.size)
            throw new IllegalArgumentException("Duplicate string values for enum " +
                enumClassName)

        validLiteralNamesAndStringValues = HashBiMap.create(
            validLiteralStringValues.keySet.toMap([it.name], [validLiteralStringValues.get(it)])
        )
        stringValuesSorted = validLiteralStringValues.values.sort
    }

    abstract def Map<T, String> getValidLiteralStringValues()

    final def literalNameToStringValue(String literalName) {
        return validLiteralNamesAndStringValues.get(literalName)
    }

    final def stringValueToLiteralName(String stringValue) {
        return validLiteralNamesAndStringValues.inverse().get(stringValue)
    }

    final override checkValidValue(ProcessingConfiguration processingConfiguration, String value) {
        if (!validLiteralNamesAndStringValues.keySet.contains(value))
            throw new IllegalArgumentException('''Invalid literal name "«value»" for enum ''' +
                enumClassName)
    }

    override checkValidValueInUserRepresentation(ProcessingConfiguration configuration,
        String value) {
        if (!validLiteralNamesAndStringValues.inverse().keySet.contains(value))
            throw new IllegalArgumentException("Please select a value from the following: " +
                stringValuesSorted.join(", "))
    }

    final def getStringValuesSorted() {
        return stringValuesSorted.unmodifiableView
    }

    final override convertValueToInternalRepresentation(String value) {
        return stringValueToLiteralName(value)
    }

    final override convertValueToUserRepresentation(String value) {
        return literalNameToStringValue(value)
    }
}
