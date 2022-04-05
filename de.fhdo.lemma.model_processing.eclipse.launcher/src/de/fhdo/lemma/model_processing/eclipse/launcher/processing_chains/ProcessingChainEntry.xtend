package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains

import de.fhdo.lemma.model_processing.eclipse.launcher.ModelObjectWithPropertyChangeSupport
import org.eclipse.xtend.lib.annotations.Accessors
import javax.xml.stream.XMLStreamWriter
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import org.w3c.dom.Element
import static de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants.*
import java.util.Map
import org.eclipse.debug.core.ILaunchConfiguration

class ProcessingChainEntry extends ModelObjectWithPropertyChangeSupport implements Cloneable {
    public static val XML_PROCESSING_CHAIN_ENTRY = "processingChainEntry"
    static val XML_LAUNCH_CONFIGURATION_NAME_ATTR = "launchConfigurationName"
    static val XML_PREVIOUS_EXIT_VALUE_COMPARATOR_ATTR = "previousExitValueComparator"
    static val XML_PREVIOUS_EXIT_VALUE_ATTR = "previousExitValue"

    @Accessors
    var ProcessingChain chain

    @Accessors(PUBLIC_GETTER)
    var String launchConfigurationName

    def setLaunchConfigurationName(String launchConfigurationName) {
        firePropertyChange("launchConfigurationName", this.launchConfigurationName,
            this.launchConfigurationName = launchConfigurationName)
    }

    @Accessors(PUBLIC_GETTER)
    var PreviousExitValueComparator.Comparator previousExitValueComparator

    def setPreviousExitValueComparator(
        PreviousExitValueComparator.Comparator previousExitValueComparator
    ) {
        firePropertyChange("previousExitValueComparator", this.previousExitValueComparator,
            this.previousExitValueComparator = previousExitValueComparator)
    }

    @Accessors(PUBLIC_GETTER)
    var Integer previousExitValue

    def setPreviousExitValue(Integer previousExitValue) {
        firePropertyChange("previousExitValue", this.previousExitValue,
            this.previousExitValue = previousExitValue)
    }

    new() {
        // NOOP
    }

    new(
        String launchConfigurationName,
        ProcessingChain chain,
        PreviousExitValueComparator.Comparator previousExitValueComparator,
        int previousExitValue
    ) {
        this.launchConfigurationName = launchConfigurationName
        this.chain = chain
        this.previousExitValueComparator = previousExitValueComparator
        this.previousExitValue = previousExitValue
    }

    override clone() {
        return super.clone()
    }

    /*def validate() {
        validLaunchConfigurationName()
        validPreviousExitValueComparator()
        validPreviousExitValue()
    }*/

    def validLaunchConfigurationName(
        Map<String, ILaunchConfiguration> availableLaunchConfigurations
    ) {
        validLaunchConfigurationName(launchConfigurationName, availableLaunchConfigurations)
    }

    static def validLaunchConfigurationName(String launchConfigurationName,
        Map<String, ILaunchConfiguration> availableLaunchConfigurations) {
        notNullOrEmpty(launchConfigurationName, "Launch configuration name must not be empty")

        val existsLaunchConfiguration = availableLaunchConfigurations.keySet
            .contains(launchConfigurationName)
        if (!existsLaunchConfiguration)
            throw new IllegalArgumentException("Launch configuration with name " +
                '''«launchConfigurationName» does not exist or does not concern execution of ''' +
                "a LEMMA model processor")
    }

    def validPreviousExitValueComparator() {
        validPreviousExitValueComparator(this, previousExitValueComparator)
    }

    static def validPreviousExitValueComparator(
        ProcessingChainEntry entry,
        PreviousExitValueComparator.Comparator previousExitValueComparator
    ) {
        if (entry.editable)
            notNull(previousExitValueComparator, "Exit value comparator must not be empty")
    }

    def validPreviousExitValue() {
        validPreviousExitValue(this, previousExitValue)
    }

    static def validPreviousExitValue(ProcessingChainEntry entry, Integer previousExitValue) {
        if (!entry.editable) {
            return
        }

        notNull(previousExitValue, "Exit value must not be empty")

        if (previousExitValue < 0)
            throw new IllegalArgumentException("Exit value must be greater or equal zero")
        else if (entry.previousExitValueComparator == PreviousExitValueComparator.Comparator.LOWER)
            throw new IllegalArgumentException("Exit value comparison result must be greater or " +
                "equal zero")
    }

    def validate(Map<String, ILaunchConfiguration> availableLaunchConfigurations) {
        validLaunchConfigurationName(availableLaunchConfigurations)
        validPreviousExitValueComparator()
        validPreviousExitValue()
    }

    def isEditable() {
        val isFirstEntry = chain.entries.indexOf(this) == 0
        return !isFirstEntry
    }

    def void serializeToXml(XMLStreamWriter writer) {
        writer.writeStartElement(XML_PROCESSING_CHAIN_ENTRY)
        writer.writeAttribute(XML_LAUNCH_CONFIGURATION_NAME_ATTR, launchConfigurationName ?: "")
        writer.writeAttribute(XML_PREVIOUS_EXIT_VALUE_COMPARATOR_ATTR,
            previousExitValueComparator?.toString ?: "")
        writer.writeAttribute(XML_PREVIOUS_EXIT_VALUE_ATTR, previousExitValue?.toString ?: "")
        writer.writeEndElement()
    }

    def deserializeFromXml(Element element) {
        launchConfigurationName = element.getAttribute(XML_LAUNCH_CONFIGURATION_NAME_ATTR)

        val previousExitValueComparatorXmlValue = element
            .getAttribute(XML_PREVIOUS_EXIT_VALUE_COMPARATOR_ATTR)
        previousExitValueComparator = if (!previousExitValueComparatorXmlValue.nullOrEmpty)
                PreviousExitValueComparator.Comparator.valueOf(previousExitValueComparatorXmlValue)
            else
                null

        val previousExitValueXmlValue = element.getAttribute(XML_PREVIOUS_EXIT_VALUE_ATTR)
        previousExitValue = if (!previousExitValueXmlValue.nullOrEmpty)
                Integer.valueOf(previousExitValueXmlValue)
            else
                null
    }
}