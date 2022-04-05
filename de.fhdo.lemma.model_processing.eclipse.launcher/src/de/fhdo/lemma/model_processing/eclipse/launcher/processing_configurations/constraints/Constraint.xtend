package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints

import org.eclipse.xtend.lib.annotations.Accessors
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import javax.xml.stream.XMLStreamWriter
import org.w3c.dom.Element
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.ConstraintTypeFactory
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfigurationItem
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.AbstractConstraintType

class Constraint extends ProcessingConfigurationItem {
    public static val XML_CONSTRAINT_ELEMENT = "constraint"
    static val XML_CONSTRAINT_ATTR_TYPE = "type"
    static val XML_VALUE_ELEMENT = "value"

    @Accessors(PUBLIC_GETTER)
    var AbstractConstraintType type

    def setType(AbstractConstraintType type) {
        firePropertyChange("type", this.type, this.type = type)
    }

    @Accessors(PUBLIC_GETTER)
    var String value

    def setValue(String value) {
        firePropertyChange("value", this.value, this.value = value)
    }

    // TODO: Remove? When implemented as below, TableViewer fails to update correct cell
    /*override equals(Object o) {
        return if (o === this)
            true
        else if (!(o instanceof Constraint))
            false
        else {
            val otherConstraint = o as Constraint
            type == otherConstraint.type && value == otherConstraint.value
        }
    }*/

    override clone() {
        return super.clone()
    }

    // TODO Probably not needed
    /*override ensureValidState() {
        validType()
        validValue()
    }*/

    override serializeToXml(XMLStreamWriter writer) {
        writer.writeStartElement(XML_CONSTRAINT_ELEMENT)
        writer.writeAttribute(XML_CONSTRAINT_ATTR_TYPE, type?.identifier.toString ?: "")
        writer.writeConstraintInformationToXml
        writer.writeEndElement()
    }

    private def validType() {
        notNull(type, "Processing constraint type must not be empty")
    }

    private def validValue() {
        type.checkValidValue(value)
    }

    private def writeConstraintInformationToXml(XMLStreamWriter writer) {
        writer.writeStartElement(XML_VALUE_ELEMENT)
        writer.writeCharacters(value ?: "")
        writer.writeEndElement()
    }

    override deserializeFromXml(Element element) {
        type = ConstraintTypeFactory.fromIdentifier(element.getAttribute(XML_CONSTRAINT_ATTR_TYPE))
        value = findChildElementWithTag(element, XML_VALUE_ELEMENT)?.textContent
    }

    override convertToUserRepresentation() {
        value = type?.convertValueToUserRepresentation(value) ?: ""
    }

    override convertToInternalRepresentation() {
        value = type?.convertValueToInternalRepresentation(value) ?: ""
    }
}