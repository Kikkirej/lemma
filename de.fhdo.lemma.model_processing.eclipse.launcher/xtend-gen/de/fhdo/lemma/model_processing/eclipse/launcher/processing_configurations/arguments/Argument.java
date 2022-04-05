package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments;

import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfigurationItem;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.AbstractArgumentKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.ArgumentKindFactory;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.ArgumentKindIdentifier;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ArgumentTypeFactory;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ArgumentTypeIdentifier;
import javax.xml.stream.XMLStreamWriter;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.w3c.dom.Element;

@SuppressWarnings("all")
public class Argument extends ProcessingConfigurationItem {
  public static final String XML_ARGUMENT_ELEMENT = "argument";
  
  private static final String XML_ARGUMENT_ATTR_KIND = "kind";
  
  private static final String XML_ARGUMENT_ATTR_TYPE = "type";
  
  private static final String XML_PARAMETER_ELEMENT = "parameter";
  
  private static final String XML_VALUE_ELEMENT = "value";
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private AbstractArgumentKind kind;
  
  public void setKind(final AbstractArgumentKind kind) {
    this.firePropertyChange("kind", this.kind, this.kind = kind);
  }
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private AbstractArgumentType type;
  
  public void setType(final AbstractArgumentType type) {
    this.firePropertyChange("type", this.type, this.type = type);
  }
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private String parameter;
  
  public void setParameter(final String parameter) {
    this.firePropertyChange("parameter", this.parameter, this.parameter = parameter);
  }
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private String value;
  
  public void setValue(final String value) {
    this.firePropertyChange("value", this.value, this.value = value);
  }
  
  /**
   * override equals(Object o) {
   * return if (o === this)
   * true
   * else if (!(o instanceof Argument))
   * false
   * else {
   * val otherArg = o as Argument
   * kind == otherArg.kind &&
   * type == otherArg.type &&
   * parameter == otherArg.parameter &&
   * value == otherArg.value
   * }
   * }
   */
  public Argument() {
  }
  
  public Argument(final AbstractArgumentKind kind, final AbstractArgumentType type, final String parameter, final String value) {
    this.kind = kind;
    this.type = type;
    this.parameter = parameter;
    this.value = value;
  }
  
  public static ArgumentBuilder newArgument() {
    Argument _argument = new Argument();
    return new ArgumentBuilder(_argument);
  }
  
  public static ArgumentBuilder newArgument(final ProcessingConfiguration processingConfiguration) {
    Argument _argument = new Argument();
    return new ArgumentBuilder(_argument, processingConfiguration);
  }
  
  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void validateInUserRepresentation(final ProcessingConfiguration configuration) {
    this.validKind();
    this.validType();
    this.validParameter();
    this.validValueInUserRepresentation(configuration);
  }
  
  @Override
  public void serializeToXml(final XMLStreamWriter writer) {
    try {
      writer.writeStartElement(Argument.XML_ARGUMENT_ELEMENT);
      String _elvis = null;
      ArgumentKindIdentifier _identifier = null;
      if (this.kind!=null) {
        _identifier=this.kind.getIdentifier();
      }
      String _string = _identifier.toString();
      if (_string != null) {
        _elvis = _string;
      } else {
        _elvis = "";
      }
      writer.writeAttribute(Argument.XML_ARGUMENT_ATTR_KIND, _elvis);
      String _elvis_1 = null;
      ArgumentTypeIdentifier _identifier_1 = null;
      if (this.type!=null) {
        _identifier_1=this.type.getIdentifier();
      }
      String _string_1 = _identifier_1.toString();
      if (_string_1 != null) {
        _elvis_1 = _string_1;
      } else {
        _elvis_1 = "";
      }
      writer.writeAttribute(Argument.XML_ARGUMENT_ATTR_TYPE, _elvis_1);
      this.writeArgumentInformationToXml(writer);
      writer.writeEndElement();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void validKind() {
    Utils.notNull(this.kind, "Processing argument kind must not be empty");
  }
  
  public void validType() {
    Utils.notNull(this.type, "Processing argument type must not be empty");
    this.kind.checkValidArgumentType(this.type);
  }
  
  public void validParameter() {
    this.validParameterValue(this.parameter);
  }
  
  public void validParameterValue(final String value) {
    Utils.notNullOrEmpty(value, "Processing argument parameter must not be empty");
  }
  
  public void validValueInUserRepresentation(final ProcessingConfiguration configuration) {
    this.type.checkValidValueInUserRepresentation(configuration, this.value);
  }
  
  private static Object hasOnlyOnePossibleValue(final AbstractArgumentType type) {
    return null;
  }
  
  private void writeArgumentInformationToXml(final XMLStreamWriter writer) {
    try {
      writer.writeStartElement(Argument.XML_PARAMETER_ELEMENT);
      String _elvis = null;
      if (this.parameter != null) {
        _elvis = this.parameter;
      } else {
        _elvis = "";
      }
      writer.writeCharacters(_elvis);
      writer.writeEndElement();
      writer.writeStartElement(Argument.XML_VALUE_ELEMENT);
      String _elvis_1 = null;
      if (this.value != null) {
        _elvis_1 = this.value;
      } else {
        _elvis_1 = "";
      }
      writer.writeCharacters(_elvis_1);
      writer.writeEndElement();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  public void deserializeFromXml(final Element element) {
    this.kind = ArgumentKindFactory.fromIdentifier(element.getAttribute(Argument.XML_ARGUMENT_ATTR_KIND));
    this.type = ArgumentTypeFactory.fromIdentifier(element.getAttribute(Argument.XML_ARGUMENT_ATTR_TYPE));
    Element _findChildElementWithTag = Utils.findChildElementWithTag(element, Argument.XML_PARAMETER_ELEMENT);
    String _textContent = null;
    if (_findChildElementWithTag!=null) {
      _textContent=_findChildElementWithTag.getTextContent();
    }
    this.parameter = _textContent;
    Element _findChildElementWithTag_1 = Utils.findChildElementWithTag(element, Argument.XML_VALUE_ELEMENT);
    String _textContent_1 = null;
    if (_findChildElementWithTag_1!=null) {
      _textContent_1=_findChildElementWithTag_1.getTextContent();
    }
    this.value = _textContent_1;
  }
  
  @Override
  public Object convertToUserRepresentation() {
    String _elvis = null;
    String _convertValueToUserRepresentation = null;
    if (this.type!=null) {
      _convertValueToUserRepresentation=this.type.convertValueToUserRepresentation(this.value);
    }
    if (_convertValueToUserRepresentation != null) {
      _elvis = _convertValueToUserRepresentation;
    } else {
      _elvis = "";
    }
    return this.value = _elvis;
  }
  
  @Override
  public Object convertToInternalRepresentation() {
    String _elvis = null;
    String _convertValueToInternalRepresentation = null;
    if (this.type!=null) {
      _convertValueToInternalRepresentation=this.type.convertValueToInternalRepresentation(this.value);
    }
    if (_convertValueToInternalRepresentation != null) {
      _elvis = _convertValueToInternalRepresentation;
    } else {
      _elvis = "";
    }
    return this.value = _elvis;
  }
  
  @Pure
  public AbstractArgumentKind getKind() {
    return this.kind;
  }
  
  @Pure
  public AbstractArgumentType getType() {
    return this.type;
  }
  
  @Pure
  public String getParameter() {
    return this.parameter;
  }
  
  @Pure
  public String getValue() {
    return this.value;
  }
}
