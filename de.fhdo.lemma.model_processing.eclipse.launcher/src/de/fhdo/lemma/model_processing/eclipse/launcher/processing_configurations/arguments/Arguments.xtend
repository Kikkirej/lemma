package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments

import de.fhdo.lemma.model_processing.eclipse.launcher.ModelObjectWithPropertyChangeSupport
import java.util.List
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.AbstractArgumentKind
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType
import java.beans.PropertyChangeListener

class Arguments extends ModelObjectWithPropertyChangeSupport {
    var List<Argument> arguments = newArrayList

    new(List<Argument> arguments) {
        //firePropertyChange("arguments", this.arguments, this.arguments = arguments.map[userClone])
        firePropertyChange("arguments", flatCopy(this.arguments), this.arguments = arguments)
    }

    /*private def getUserClone(Argument argument) {
        val clone = argument.clone as Argument
        clone.convertToUserRepresentation()
        return clone
    }*/

    // Property change listeners are transitive, i.e., they exist on the Arguments object itself and
    // also all managed arguments being part of the Arguments object. Listener thus get called not
    // only when the Arguments object changes but also when a managed argument changes.
    override addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener)
        arguments.forEach[it.addPropertyChangeListener(listener)]
    }

    override removePropertyChangeListener(PropertyChangeListener listener) {
        super.removePropertyChangeListener(listener)
        arguments.forEach[it.removePropertyChangeListener(listener)]
    }

    def add(Argument argument) {
        // Make all previously registered property change listeners aware of the new attribute
        propertyChangeListeners.forEach[argument.addPropertyChangeListener(it)]

        add(arguments.size, argument)
    }

    def add(int index, Argument argument) {
        //arguments.add(index, argument.userClone)
        firePropertyChange("arguments", flatCopy(arguments),
            arguments = addInPlace(arguments, argument))
    }

    def moveUp(Argument argument) {
        firePropertyChange("arguments", flatCopy(arguments),
            arguments = moveUpInPlace(arguments, argument))
    }

    def moveDown(Argument argument) {
        firePropertyChange("arguments", flatCopy(arguments),
            arguments = moveDownInPlace(arguments, argument))
    }

    def remove(Argument argument) {
        propertyChangeListeners.forEach[argument.removePropertyChangeListener(it)]
        firePropertyChange("arguments", flatCopy(arguments),
            arguments = removeInPlace(arguments, argument))
    }

    def removeAll(List<Argument> argumentsToRemove) {
        propertyChangeListeners.forEach[propertyChangeListener |
            argumentsToRemove.forEach[it.removePropertyChangeListener(propertyChangeListener)]
        ]
        firePropertyChange("arguments", flatCopy(arguments),
            arguments = removeAllInPlace(arguments, argumentsToRemove))
    }

    def contains(Argument argument) {
        /*val comparableClone = argument.userClone
        return arguments.exists[
            kind == comparableClone.kind &&
            type == comparableClone.type &&
            parameter == comparableClone.parameter &&
            value == comparableClone.value
        ]*/
        return arguments.exists[
            kind == argument.kind &&
            type == argument.type &&
            parameter == argument.parameter &&
            value == argument.value
        ]
    }

    def getManagedArgumentRepresentations(Class<?> argumentTypeClass) {
        return arguments
            .filter[argumentTypeClass.isAssignableFrom(it.type.class)]
            .toList()
            .unmodifiableView
    }

    def get() {
        return arguments.unmodifiableView
    }
}