package de.fhdo.lemma.model_processing.eclipse.launcher;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

@SuppressWarnings("all")
public class ModelObjectWithPropertyChangeSupport {
  private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
  
  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    this.changeSupport.addPropertyChangeListener(listener);
  }
  
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    this.changeSupport.removePropertyChangeListener(listener);
  }
  
  protected void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
    this.changeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }
  
  protected PropertyChangeListener[] getPropertyChangeListeners() {
    return this.changeSupport.getPropertyChangeListeners();
  }
}
