package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains

import de.fhdo.lemma.model_processing.eclipse.launcher.ModelObjectWithPropertyChangeSupport
import org.eclipse.xtend.lib.annotations.Accessors
import javax.xml.stream.XMLStreamWriter
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import org.w3c.dom.Element
import org.eclipse.debug.core.ILaunchConfiguration
import static de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants.*
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy
import java.io.ByteArrayOutputStream
import javax.xml.stream.XMLOutputFactory
import java.nio.charset.StandardCharsets
import java.util.List
import java.beans.PropertyChangeListener
import java.util.Map

class ProcessingChain extends ModelObjectWithPropertyChangeSupport implements Cloneable {
    static val XML_CHAIN_ELEMENT = "processingChain"
    static val XML_CHAIN_ENTRIES_ELEMENT = "entries"

    @Accessors(PUBLIC_GETTER)
    var List<ProcessingChainEntry> entries = newArrayList

    override equals(Object o) {
        return if (o === this)
            true
        else if (!(o instanceof ProcessingChain))
            false
        else {
            val otherConfig = o as ProcessingChain
            equalLists(entries, otherConfig.entries)
        }
    }

    override clone() {
        val clone = super.clone() as ProcessingChain
        clone.entries = newArrayList(entries.map[clone() as ProcessingChainEntry])
        clone.entries.forEach[it.chain = clone]
        return clone
    }

    // Property change listeners are transitive, i.e., they exist on the ProcessingChain object
    // itself and also all managed entries being part of the ProcessingChain object. Listeners thus
    // get called not only when the ProcessingChain object changes but also when a managed entry
    // changes.
    override addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener)
        entries.forEach[it.addPropertyChangeListener(listener)]
    }

    override removePropertyChangeListener(PropertyChangeListener listener) {
        super.removePropertyChangeListener(listener)
        entries.forEach[it.removePropertyChangeListener(listener)]
    }

    def addEntry(ProcessingChainEntry entry) {
        // Make all previously registered property change listeners aware of the new attribute
        propertyChangeListeners.forEach[entry.addPropertyChangeListener(it)]

        firePropertyChange("entries", flatCopy(entries), entries = addInPlace(entries, entry))
    }

    def moveUp(ProcessingChainEntry entry) {
        firePropertyChange("entries", flatCopy(entries), entries = moveUpInPlace(entries, entry))
    }

    def moveDown(ProcessingChainEntry entry) {
        firePropertyChange("entries", flatCopy(entries), entries = moveDownInPlace(entries, entry))
    }

    def remove(ProcessingChainEntry entry) {
        propertyChangeListeners.forEach[entry.removePropertyChangeListener(it)]
        firePropertyChange("entries", flatCopy(entries), entries = removeInPlace(entries, entry))
    }

    def removeAll(List<ProcessingChainEntry> entriesToRemove) {
        propertyChangeListeners.forEach[propertyChangeListener |
            entriesToRemove.forEach[removePropertyChangeListener(propertyChangeListener)]
        ]
        firePropertyChange("entries", flatCopy(entries),
            entries = removeAllInPlace(entries, entriesToRemove))
    }

    def void serializeToXml(XMLStreamWriter writer) {
        writer.writeStartElement(XML_CHAIN_ELEMENT)

        writer.writeStartElement(XML_CHAIN_ENTRIES_ELEMENT)
        entries.forEach[it.serializeToXml(writer)]
        writer.writeEndElement()

        writer.writeEndElement()
    }

    /*def validate() {
        if (entries.empty)
            throw new IllegalArgumentException("Model processing chain must comprise at least " +
                "one element")

        entries.forEach[it.validate()]
    }*/

    def validate(Map<String, ILaunchConfiguration> availableLaunchConfigurations) {
        validate(availableLaunchConfigurations, false)
    }

    def validate(Map<String, ILaunchConfiguration> availableLaunchConfigurations,
        boolean disableEntryValidation) {
        if (entries.empty)
            throw new IllegalArgumentException("Model processing chain must comprise at least " +
                "one element")

        if (!disableEntryValidation)
            entries.forEach[it.validate(availableLaunchConfigurations)]
    }

    static def deserializeFrom(ILaunchConfiguration launchConfiguration) {
        val xml = launchConfiguration.getAttribute(PROCESSING_CHAIN_ATTRIBUTE, "")
        if (xml.empty)
            return null

        return deserializeFromXml(xml)
    }

    static def deserializeFromXml(String xml) {
        val chainXmlRoot = getRootElementWithTag(parseXmlString(xml), XML_CHAIN_ELEMENT)
        if (chainXmlRoot === null)
            throw new IllegalArgumentException("Error during deserialization of processing " +
                '''chain: Root XML element «XML_CHAIN_ELEMENT» not found''')

        val chain = new ProcessingChain()
        chain.entries = initializeEntriesFromXml(chainXmlRoot, chain)
        return chain
    }

    private static def initializeEntriesFromXml(Element chainXmlRoot, ProcessingChain chain) {
        val entries = <ProcessingChainEntry>newArrayList
        val entriesParentElement = findChildElementWithTag(chainXmlRoot, XML_CHAIN_ENTRIES_ELEMENT)
        val entryElements = entriesParentElement
            .getElementsByTagName(ProcessingChainEntry.XML_PROCESSING_CHAIN_ENTRY)
        for (n : 0..<entryElements.length) {
            val entry = new ProcessingChainEntry
            entry.deserializeFromXml(entryElements.item(n) as Element)
            entry.chain = chain
            entries.add(entry)
        }
        return entries
    }

    static def setProcessingChainAsAttribute(ILaunchConfigurationWorkingCopy launchConfiguration,
        ProcessingChain processingChain) {
        val chainResources = processingChain.entriesLaunchConfigurations
            .map[it.mappedResources.toList]
            .flatten
            .toSet
        launchConfiguration.mappedResources = chainResources

        val out = new ByteArrayOutputStream
        val factory = XMLOutputFactory.newInstance()
        val writer = factory.createXMLStreamWriter(out)
        processingChain.serializeToXml(writer)
        launchConfiguration.setAttribute(PROCESSING_CHAIN_ATTRIBUTE,
            out.toString(StandardCharsets.UTF_8))
        out.close()
        writer.close()
    }

    def getEntriesLaunchConfigurations() {
        return entries.map[
            getProcessingConfigurationLaunchConfigurations.get(it.launchConfigurationName)
        ].filterNull
    }
}