package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations;

import de.fhdo.lemma.model_processing.eclipse.launcher.ModelObjectWithPropertyChangeSupport;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Element;

@SuppressWarnings("all")
public abstract class ProcessingConfigurationItem extends ModelObjectWithPropertyChangeSupport implements Cloneable {
  public final void serializeToXmlInternal(final XMLStreamWriter writer) {
    this.serializeToXml(writer);
  }
  
  /**
   * def void ensureValidState() {
   * // NOOP
   * }
   */
  public abstract void serializeToXml(final XMLStreamWriter writer);
  
  public final void deserializeFromXmlInternal(final Element element) {
    this.deserializeFromXml(element);
  }
  
  public abstract void deserializeFromXml(final Element element);
  
  public Object convertToUserRepresentation() {
    return null;
  }
  
  public Object convertToInternalRepresentation() {
    return null;
  }
}
