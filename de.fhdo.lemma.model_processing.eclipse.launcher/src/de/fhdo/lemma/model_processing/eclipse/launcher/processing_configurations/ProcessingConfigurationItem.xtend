package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations

import javax.xml.stream.XMLStreamWriter
import org.w3c.dom.Element
import de.fhdo.lemma.model_processing.eclipse.launcher.ModelObjectWithPropertyChangeSupport

abstract class ProcessingConfigurationItem extends ModelObjectWithPropertyChangeSupport
    implements Cloneable {
    final def serializeToXmlInternal(XMLStreamWriter writer) {
        //ensureValidState()
        serializeToXml(writer)
    }

    /*def void ensureValidState() {
        // NOOP
    }*/

    abstract def void serializeToXml(XMLStreamWriter writer)

    final def deserializeFromXmlInternal(Element element) {
        deserializeFromXml(element)
        //ensureValidState()
    }

    abstract def void deserializeFromXml(Element element)

    def convertToUserRepresentation() {
        // NOOP
    }

    def convertToInternalRepresentation() {
        // NOOP
    }
}