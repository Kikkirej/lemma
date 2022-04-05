package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints;

import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfigurationItem;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.AbstractConstraintType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.ConstraintTypeFactory;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.ConstraintTypeIdentifier;
import javax.xml.stream.XMLStreamWriter;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.w3c.dom.Element;

@SuppressWarnings("all")
public class Constraint extends ProcessingConfigurationItem {
  public static final String XML_CONSTRAINT_ELEMENT = "constraint";
  
  private static final String XML_CONSTRAINT_ATTR_TYPE = "type";
  
  private static final String XML_VALUE_ELEMENT = "value";
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private AbstractConstraintType type;
  
  public void setType(final AbstractConstraintType type) {
    this.firePropertyChange("type", this.type, this.type = type);
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
   * else if (!(o instanceof Constraint))
   * false
   * else {
   * val otherConstraint = o as Constraint
   * type == otherConstraint.type && value == otherConstraint.value
   * }
   * }
   */
  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * override ensureValidState() {
   * validType()
   * validValue()
   * }
   */
  @Override
  public void serializeToXml(final XMLStreamWriter writer) {
    try {
      writer.writeStartElement(Constraint.XML_CONSTRAINT_ELEMENT);
      String _elvis = null;
      ConstraintTypeIdentifier _identifier = null;
      if (this.type!=null) {
        _identifier=this.type.getIdentifier();
      }
      String _string = _identifier.toString();
      if (_string != null) {
        _elvis = _string;
      } else {
        _elvis = "";
      }
      writer.writeAttribute(Constraint.XML_CONSTRAINT_ATTR_TYPE, _elvis);
      this.writeConstraintInformationToXml(writer);
      writer.writeEndElement();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private void validType() {
    Utils.notNull(this.type, "Processing constraint type must not be empty");
  }
  
  private void validValue() {
    this.type.checkValidValue(this.value);
  }
  
  private void writeConstraintInformationToXml(final XMLStreamWriter writer) {
    try {
      writer.writeStartElement(Constraint.XML_VALUE_ELEMENT);
      String _elvis = null;
      if (this.value != null) {
        _elvis = this.value;
      } else {
        _elvis = "";
      }
      writer.writeCharacters(_elvis);
      writer.writeEndElement();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  public void deserializeFromXml(final Element element) {
    this.type = ConstraintTypeFactory.fromIdentifier(element.getAttribute(Constraint.XML_CONSTRAINT_ATTR_TYPE));
    Element _findChildElementWithTag = Utils.findChildElementWithTag(element, Constraint.XML_VALUE_ELEMENT);
    String _textContent = null;
    if (_findChildElementWithTag!=null) {
      _textContent=_findChildElementWithTag.getTextContent();
    }
    this.value = _textContent;
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
  public AbstractConstraintType getType() {
    return this.type;
  }
  
  @Pure
  public String getValue() {
    return this.value;
  }
}
