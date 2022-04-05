package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types;

import com.google.common.base.Objects;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public abstract class AbstractArgumentType {
  @Accessors(AccessorType.PUBLIC_GETTER)
  private final ArgumentTypeIdentifier identifier;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private final String name;
  
  private final List<String> possibleValues;
  
  public List<String> getPossibleValues() {
    return Collections.<String>unmodifiableList(this.possibleValues);
  }
  
  @Override
  public boolean equals(final Object o) {
    boolean _xifexpression = false;
    if ((o == this)) {
      _xifexpression = true;
    } else {
      boolean _xifexpression_1 = false;
      if ((!(o instanceof AbstractArgumentType))) {
        _xifexpression_1 = false;
      } else {
        _xifexpression_1 = Objects.equal(this.name, ((AbstractArgumentType) o).name);
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public AbstractArgumentType(final ArgumentTypeIdentifier identifier, final String name) {
    this(identifier, name, CollectionLiterals.<String>emptyList());
  }
  
  public AbstractArgumentType(final ArgumentTypeIdentifier identifier, final String name, final List<String> possibleValues) {
    if ((identifier == null)) {
      throw new IllegalArgumentException("Argument type identifier must not be null");
    } else {
      boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(name);
      if (_isNullOrEmpty) {
        throw new IllegalArgumentException("Argument type name must not be null or empty");
      } else {
        if ((possibleValues == null)) {
          throw new IllegalArgumentException("List of possible values must not be null");
        }
      }
    }
    this.identifier = identifier;
    this.name = name;
    this.possibleValues = CollectionLiterals.<String>newArrayList(((String[])Conversions.unwrapArray(possibleValues, String.class)));
    Collections.<String>sort(this.possibleValues);
  }
  
  /**
   * final def checkValidValueInternal(String value) {
   * if (!possibleValues.isEmpty && !possibleValues.contains(value))
   * throw new IllegalArgumentException('''«value» is an invalid value for the argument ''' +
   * '''type (expected values: «possibleValues.join(", ")»)''')
   * else
   * checkValidValue(value)
   * }
   */
  public abstract void checkValidValue(final ProcessingConfiguration processingConfiguration, final String value);
  
  /**
   * final def defaultValue() {
   * return if (!possibleValues.empty)
   * possibleValues.get(0)
   * else
   * ""
   * }
   */
  public void checkValidValueInUserRepresentation(final ProcessingConfiguration configuration, final String value) {
    this.checkValidValue(configuration, value);
  }
  
  public String convertValueToUserRepresentation(final String value) {
    return value;
  }
  
  public String convertValueToInternalRepresentation(final String value) {
    return value;
  }
  
  @Pure
  public ArgumentTypeIdentifier getIdentifier() {
    return this.identifier;
  }
  
  @Pure
  public String getName() {
    return this.name;
  }
}
