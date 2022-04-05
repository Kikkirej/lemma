package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains

import com.google.common.collect.HashBiMap
import org.eclipse.xtend.lib.annotations.Accessors

class PreviousExitValueComparator {
    static val COMPARATORS = #{
        Comparator.LOWER -> new ComparatorInfo("<",
            [actualExitValue, expectedExitValue | actualExitValue < expectedExitValue]),
        Comparator.LOWER_OR_EQUAL -> new ComparatorInfo("<=",
            [actualExitValue, expectedExitValue | actualExitValue <= expectedExitValue]),
        Comparator.EQUAL -> new ComparatorInfo("==",
            [actualExitValue, expectedExitValue | actualExitValue == expectedExitValue]),
        Comparator.GREATER -> new ComparatorInfo(">",
            [actualExitValue, expectedExitValue | actualExitValue > expectedExitValue]),
        Comparator.GREATER_OR_EQUAL -> new ComparatorInfo(">=",
            [actualExitValue, expectedExitValue | actualExitValue >= expectedExitValue])
    }

    static class ComparatorInfo {
        @Accessors(PROTECTED_GETTER)
        val String userRepresentation

        @Accessors(PROTECTED_GETTER)
        val (int, int)=>boolean comparatorFunction

        new (String userRepresentation, (int, int)=>boolean comparatorFunction) {
            this.userRepresentation = userRepresentation
            this.comparatorFunction = comparatorFunction
        }
    }

    static val COMPARATOR_USER_REPRESENTATIONS
        = HashBiMap.create(COMPARATORS.mapValues[it.userRepresentation])

    enum Comparator {
        LOWER,
        LOWER_OR_EQUAL,
        EQUAL,
        GREATER,
        GREATER_OR_EQUAL
    }

    static def getUserRepresentationValues() {
        return COMPARATOR_USER_REPRESENTATIONS.values
    }

    static def getUserRepresentation(Comparator comparator) {
        // Possible for first entry in a chain
        if (comparator === null)
            return ""

        val userRepresentation = COMPARATOR_USER_REPRESENTATIONS.get(comparator)
        if (userRepresentation === null)
            throw new IllegalArgumentException('''Invalid exit value comparator "«comparator»"''')

        return userRepresentation
    }

    static def getInternalRepresentation(String userRepresentation) {
        // Possible for first entry in a chain
        if (userRepresentation.nullOrEmpty)
            return null

        val comparator = COMPARATOR_USER_REPRESENTATIONS.inverse.get(userRepresentation)
        if (comparator === null)
            throw new IllegalArgumentException("Exit value comparator with user representation " +
                '''"«userRepresentation»" does not exist''')

        return comparator
    }

    static def matches(int actualExitValue, int expectedExitValue, Comparator comparator) {
        val comparatorInfo = COMPARATORS.get(comparator)
        if (comparatorInfo === null)
            throw new IllegalArgumentException('''Invalid exit value comparator "«comparator»"''')

        return comparatorInfo.comparatorFunction.apply(actualExitValue, expectedExitValue)
    }
}