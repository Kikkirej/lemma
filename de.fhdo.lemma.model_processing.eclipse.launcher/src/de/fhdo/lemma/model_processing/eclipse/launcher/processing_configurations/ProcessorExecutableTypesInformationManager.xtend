package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap

class ProcessorExecutableTypesInformationManager {
    static val MANAGED_INFO
        = <ProcessorExecutableType, ProcessorExecutableTypeInformation>newHashMap
    static val BiMap<ProcessorExecutableType, String> LABEL_VIEW = HashBiMap.create()

    static def register(ProcessorExecutableTypeInformation... info) {
        info.forEach[
            MANAGED_INFO.put(it.processorExecutableType, it)
            LABEL_VIEW.put(it.processorExecutableType, it.label)
        ]
    }

    static def label(ProcessorExecutableType literal) {
        val label = LABEL_VIEW.get(literal)
        return if (label !== null)
                label
            else
                throw new IllegalArgumentException("Unsupported processor executable type: " +
                    literal)
    }

    static def literal(String label) {
        val literal = LABEL_VIEW.inverse().get(label)
        return if (literal !== null)
                literal
            else
                throw new IllegalArgumentException("Unsupported processor executable type " +
                    '''label: «label»''')
    }

    static def printableInSentenceString(ProcessorExecutableType literal) {
        val info = MANAGED_INFO.get(literal)
        return info?.printableInSentenceString
    }

    static def hasInputSupport(ProcessorExecutableType literal) {
        val info = MANAGED_INFO.get(literal)
        return info !== null && info.inputSupportFunction !== null
    }

    static def inputSupportFunction(ProcessorExecutableType literal) {
        val info = MANAGED_INFO.get(literal)
        if (info === null)
            throw new IllegalArgumentException("Unsupported processor executable type: " +
                literal)
        return info.inputSupportFunction
    }
}