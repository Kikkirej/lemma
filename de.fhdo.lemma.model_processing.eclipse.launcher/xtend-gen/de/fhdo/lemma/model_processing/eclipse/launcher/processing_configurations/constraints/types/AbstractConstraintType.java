package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types;

import com.google.common.base.Objects;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public abstract class AbstractConstraintType {
  @Accessors(AccessorType.PUBLIC_GETTER)
  private final ConstraintTypeIdentifier identifier;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private final String name;
  
  @Override
  public boolean equals(final Object o) {
    boolean _xifexpression = false;
    if ((o == this)) {
      _xifexpression = true;
    } else {
      boolean _xifexpression_1 = false;
      if ((!(o instanceof AbstractConstraintType))) {
        _xifexpression_1 = false;
      } else {
        _xifexpression_1 = Objects.equal(this.identifier, ((AbstractConstraintType) o).identifier);
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public AbstractConstraintType(final ConstraintTypeIdentifier identifier, final String name) {
    if ((identifier == null)) {
      throw new IllegalArgumentException("Constraint type identifier must not be null");
    } else {
      boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(name);
      if (_isNullOrEmpty) {
        throw new IllegalArgumentException("Constraint type name must not be null or empty");
      }
    }
    this.identifier = identifier;
    this.name = name;
  }
  
  public abstract void checkValidValue(final String value);
  
  public void checkValidValueInUserRepresentation(final String value) {
    this.checkValidValue(value);
  }
  
  public String convertValueToUserRepresentation(final String value) {
    return value;
  }
  
  public String convertValueToInternalRepresentation(final String value) {
    return value;
  }
  
  @Pure
  public ConstraintTypeIdentifier getIdentifier() {
    return this.identifier;
  }
  
  @Pure
  public String getName() {
    return this.name;
  }
}
