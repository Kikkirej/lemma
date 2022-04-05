package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations

import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.swt.widgets.Shell
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration

class ProcessorExecutableTypeInformation {
    @Accessors(PUBLIC_GETTER)
    ProcessorExecutableType processorExecutableType

    @Accessors(PUBLIC_GETTER)
    String label

    @Accessors(PUBLIC_GETTER)
    String printableInSentenceString

    @Accessors(PUBLIC_GETTER)
    (Shell, ProcessingConfiguration)=>String inputSupportFunction

    new(
        ProcessorExecutableType processorExecutableType,
        String label,
        String printableInSentenceString,
        (Shell, ProcessingConfiguration)=>String inputSupportFunction
    ) {
        this.processorExecutableType = processorExecutableType
        this.label = label
        this.printableInSentenceString = printableInSentenceString
        this.inputSupportFunction = inputSupportFunction
    }
}