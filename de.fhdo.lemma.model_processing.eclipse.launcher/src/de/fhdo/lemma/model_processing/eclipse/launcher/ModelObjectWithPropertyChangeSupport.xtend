package de.fhdo.lemma.model_processing.eclipse.launcher

import java.beans.PropertyChangeSupport
import java.beans.PropertyChangeListener

class ModelObjectWithPropertyChangeSupport {
    val changeSupport = new PropertyChangeSupport(this)

    def void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener)
    }

    def void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener)
    }

    protected def void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue)
    }

    protected def getPropertyChangeListeners() {
        return changeSupport.getPropertyChangeListeners()
    }
}