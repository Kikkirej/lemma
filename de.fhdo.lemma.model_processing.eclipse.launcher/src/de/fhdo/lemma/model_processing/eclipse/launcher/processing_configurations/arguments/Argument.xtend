package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments

import org.eclipse.xtend.lib.annotations.Accessors

import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import javax.xml.stream.XMLStreamWriter
import org.w3c.dom.Element
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfigurationItem
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.AbstractArgumentKind
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.ArgumentKindFactory
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ArgumentTypeFactory
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration

class Argument extends ProcessingConfigurationItem {
    public static val XML_ARGUMENT_ELEMENT = "argument"
    static val XML_ARGUMENT_ATTR_KIND = "kind"
    static val XML_ARGUMENT_ATTR_TYPE = "type"
    static val XML_PARAMETER_ELEMENT = "parameter"
    static val XML_VALUE_ELEMENT = "value"

    @Accessors(PUBLIC_GETTER)
    var AbstractArgumentKind kind

    def setKind(AbstractArgumentKind kind) {
        firePropertyChange("kind", this.kind, this.kind = kind)
    }

    @Accessors(PUBLIC_GETTER)
    var AbstractArgumentType type

    def setType(AbstractArgumentType type) {
        firePropertyChange("type", this.type, this.type = type)
    }

    @Accessors(PUBLIC_GETTER)
    var String parameter

    def setParameter(String parameter) {
        firePropertyChange("parameter", this.parameter, this.parameter = parameter)
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
        else if (!(o instanceof Argument))
            false
        else {
            val otherArg = o as Argument
            kind == otherArg.kind &&
                type == otherArg.type &&
                parameter == otherArg.parameter &&
                value == otherArg.value
        }
    }*/

    new() {
        // NOOP
    }

    new(AbstractArgumentKind kind, AbstractArgumentType type, String parameter, String value) {
        this.kind = kind
        this.type = type
        this.parameter = parameter
        this.value = value
    }

    static def newArgument() {
        return new ArgumentBuilder(new Argument())
    }

    static def newArgument(ProcessingConfiguration processingConfiguration) {
        return new ArgumentBuilder(new Argument(), processingConfiguration)
    }

    override clone() {
        return super.clone()
    }

    def validateInUserRepresentation(ProcessingConfiguration configuration) {
        validKind()
        validType()
        validParameter()
        validValueInUserRepresentation(configuration)
    }

    override serializeToXml(XMLStreamWriter writer) {
        writer.writeStartElement(XML_ARGUMENT_ELEMENT)
        writer.writeAttribute(XML_ARGUMENT_ATTR_KIND, kind?.identifier.toString ?: "")
        writer.writeAttribute(XML_ARGUMENT_ATTR_TYPE, type?.identifier.toString ?: "")
        writer.writeArgumentInformationToXml
        writer.writeEndElement()
    }

    def validKind() {
        notNull(kind, "Processing argument kind must not be empty")
    }

    def validType() {
        notNull(type, "Processing argument type must not be empty")
        kind.checkValidArgumentType(type)
    }

    def validParameter() {
        parameter.validParameterValue()
    }

    def validParameterValue(String value) {
        notNullOrEmpty(value, "Processing argument parameter must not be empty")
    }

    def validValueInUserRepresentation(ProcessingConfiguration configuration) {
        // In case the argument can only have one possible value, we assume the argument's value to
        // always be exactly this one value, which implicitly is valid in the context of the
        // argument type
        //if (!hasOnlyOnePossibleValue(type))
        //type.checkValidValueInternal(value)
        type.checkValidValueInUserRepresentation(configuration, value)
    }

    private static def hasOnlyOnePossibleValue(AbstractArgumentType type) {
        //return type.requiresValue && type.possibleValues.size == 1
    }

    private def writeArgumentInformationToXml(XMLStreamWriter writer) {
        writer.writeStartElement(XML_PARAMETER_ELEMENT)
        writer.writeCharacters(parameter ?: "")
        writer.writeEndElement()

        writer.writeStartElement(XML_VALUE_ELEMENT)
        //if (hasOnlyOnePossibleValue(type))
            // In case the argument can only have one possible value, we assume the argument's value
            // to always be exactly this one value and thus don't write it to XML
            //writer.writeCharacters("")
        //else
            writer.writeCharacters(value ?: "")
        writer.writeEndElement()
    }

    override deserializeFromXml(Element element) {
        kind = ArgumentKindFactory.fromIdentifier(element.getAttribute(XML_ARGUMENT_ATTR_KIND))
        type = ArgumentTypeFactory.fromIdentifier(element.getAttribute(XML_ARGUMENT_ATTR_TYPE))
        parameter = findChildElementWithTag(element, XML_PARAMETER_ELEMENT)?.textContent
        value = findChildElementWithTag(element, XML_VALUE_ELEMENT)?.textContent
    }

    override convertToUserRepresentation() {
        value = type?.convertValueToUserRepresentation(value) ?: ""
    }

    override convertToInternalRepresentation() {
        value = type?.convertValueToInternalRepresentation(value) ?: ""
    }
}