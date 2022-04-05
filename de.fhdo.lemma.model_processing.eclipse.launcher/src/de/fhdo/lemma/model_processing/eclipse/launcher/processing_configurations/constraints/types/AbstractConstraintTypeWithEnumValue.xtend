package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types

import java.util.Map
import java.util.List
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap

abstract class AbstractConstraintTypeWithEnumValue<T extends Enum<?>>
    extends AbstractConstraintType {
    val String enumClassName
    val Map<String, T> literalNamesToLiterals
    val BiMap<String, String> validLiteralNamesAndStringValues
    val List<String> stringValuesSorted

    override equals(Object o) {
        return if (o === this)
            true
        else if (!(o instanceof AbstractConstraintType))
            false
        else
            name == (o as AbstractConstraintType).name
    }

    new(ConstraintTypeIdentifier identifier, String name) {
        super(identifier, name)

        val validLiteralStringValues = getValidLiteralStringValues()
        if (validLiteralStringValues.empty)
            throw new IllegalArgumentException("No string values given")

        enumClassName = validLiteralStringValues.keySet.toList.get(0).class.simpleName

        if (validLiteralStringValues.values.size != validLiteralStringValues.values.toSet.size)
            throw new IllegalArgumentException("Duplicate string values for enum " +
                enumClassName)

        literalNamesToLiterals = validLiteralStringValues.keySet.toMap([it.name], [it])
        validLiteralNamesAndStringValues = HashBiMap.create(
            validLiteralStringValues.keySet.toMap([it.name], [validLiteralStringValues.get(it)])
        )
        stringValuesSorted = validLiteralStringValues.values.sort
    }

    abstract def Map<T, String> getValidLiteralStringValues()

    /*final def literalNameToLiteral(String literalName) {
        return literalNamesToLiterals.get(literalName)
    }*/

    final def literalNameToStringValue(String literalName) {
        return validLiteralNamesAndStringValues.get(literalName)
    }

    final def stringValueToLiteralName(String stringValue) {
        return validLiteralNamesAndStringValues.inverse().get(stringValue)
    }

    final override checkValidValue(String value) {
        if (!validLiteralNamesAndStringValues.keySet.contains(value))
            throw new IllegalArgumentException('''Invalid literal name "«value»" for enum ''' +
                enumClassName)
    }

    final override checkValidValueInUserRepresentation(String value) {
        if (!validLiteralNamesAndStringValues.inverse().keySet.contains(value))
            throw new IllegalArgumentException("Please select a value from the following: " +
                stringValuesSorted.join(", "))
    }

    /*final def getSortIndexOfStringValue(String stringValue) {
        return stringValuesSorted.indexOf(stringValue)
    }*/

    /*final def getStringValueAtSortIndex(int index) {
        return stringValuesSorted.get(index)
    }*/

    /*final def getSortIndexOfLiteralName(String literalName) {
        return stringValuesSorted.indexOf(literalNameToStringValue(literalName))
    }*/

    /*final def getLiteralNameAtSortIndex(int index) {
        return stringValueToLiteralName(stringValuesSorted.get(index))
    }*/

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
