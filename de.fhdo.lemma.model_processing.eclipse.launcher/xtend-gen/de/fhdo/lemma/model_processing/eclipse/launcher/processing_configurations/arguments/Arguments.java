package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments;

import com.google.common.base.Objects;
import de.fhdo.lemma.model_processing.eclipse.launcher.ModelObjectWithPropertyChangeSupport;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class Arguments extends ModelObjectWithPropertyChangeSupport {
  private List<Argument> arguments = CollectionLiterals.<Argument>newArrayList();
  
  public Arguments(final List<Argument> arguments) {
    this.firePropertyChange("arguments", Utils.<Argument>flatCopy(this.arguments), this.arguments = arguments);
  }
  
  /**
   * private def getUserClone(Argument argument) {
   * val clone = argument.clone as Argument
   * clone.convertToUserRepresentation()
   * return clone
   * }
   */
  @Override
  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    super.addPropertyChangeListener(listener);
    final Consumer<Argument> _function = (Argument it) -> {
      it.addPropertyChangeListener(listener);
    };
    this.arguments.forEach(_function);
  }
  
  @Override
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    super.removePropertyChangeListener(listener);
    final Consumer<Argument> _function = (Argument it) -> {
      it.removePropertyChangeListener(listener);
    };
    this.arguments.forEach(_function);
  }
  
  public void add(final Argument argument) {
    final Consumer<PropertyChangeListener> _function = (PropertyChangeListener it) -> {
      argument.addPropertyChangeListener(it);
    };
    ((List<PropertyChangeListener>)Conversions.doWrapArray(this.getPropertyChangeListeners())).forEach(_function);
    this.add(this.arguments.size(), argument);
  }
  
  public void add(final int index, final Argument argument) {
    this.firePropertyChange("arguments", Utils.<Argument>flatCopy(this.arguments), 
      this.arguments = Utils.<Argument>addInPlace(this.arguments, argument));
  }
  
  public void moveUp(final Argument argument) {
    this.firePropertyChange("arguments", Utils.<Argument>flatCopy(this.arguments), 
      this.arguments = Utils.<Argument>moveUpInPlace(this.arguments, argument));
  }
  
  public void moveDown(final Argument argument) {
    this.firePropertyChange("arguments", Utils.<Argument>flatCopy(this.arguments), 
      this.arguments = Utils.<Argument>moveDownInPlace(this.arguments, argument));
  }
  
  public void remove(final Argument argument) {
    final Consumer<PropertyChangeListener> _function = (PropertyChangeListener it) -> {
      argument.removePropertyChangeListener(it);
    };
    ((List<PropertyChangeListener>)Conversions.doWrapArray(this.getPropertyChangeListeners())).forEach(_function);
    this.firePropertyChange("arguments", Utils.<Argument>flatCopy(this.arguments), 
      this.arguments = Utils.<Argument>removeInPlace(this.arguments, argument));
  }
  
  public void removeAll(final List<Argument> argumentsToRemove) {
    final Consumer<PropertyChangeListener> _function = (PropertyChangeListener propertyChangeListener) -> {
      final Consumer<Argument> _function_1 = (Argument it) -> {
        it.removePropertyChangeListener(propertyChangeListener);
      };
      argumentsToRemove.forEach(_function_1);
    };
    ((List<PropertyChangeListener>)Conversions.doWrapArray(this.getPropertyChangeListeners())).forEach(_function);
    this.firePropertyChange("arguments", Utils.<Argument>flatCopy(this.arguments), 
      this.arguments = Utils.<Argument>removeAllInPlace(this.arguments, argumentsToRemove));
  }
  
  public boolean contains(final Argument argument) {
    final Function1<Argument, Boolean> _function = (Argument it) -> {
      return Boolean.valueOf((((Objects.equal(it.getKind(), argument.getKind()) && 
        Objects.equal(it.getType(), argument.getType())) && 
        Objects.equal(it.getParameter(), argument.getParameter())) && 
        Objects.equal(it.getValue(), argument.getValue())));
    };
    return IterableExtensions.<Argument>exists(this.arguments, _function);
  }
  
  public List<Argument> getManagedArgumentRepresentations(final Class<?> argumentTypeClass) {
    final Function1<Argument, Boolean> _function = (Argument it) -> {
      return Boolean.valueOf(argumentTypeClass.isAssignableFrom(it.getType().getClass()));
    };
    return Collections.<Argument>unmodifiableList(IterableExtensions.<Argument>toList(IterableExtensions.<Argument>filter(this.arguments, _function)));
  }
  
  public List<Argument> get() {
    return Collections.<Argument>unmodifiableList(this.arguments);
  }
}
