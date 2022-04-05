package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types;

import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public abstract class AbstractConstraintTypeWithEnumValue<T extends Enum<?>> extends AbstractConstraintType {
  private final String enumClassName;
  
  private final Map<String, T> literalNamesToLiterals;
  
  private final BiMap<String, String> validLiteralNamesAndStringValues;
  
  private final List<String> stringValuesSorted;
  
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
        String _name = this.getName();
        String _name_1 = ((AbstractConstraintType) o).getName();
        _xifexpression_1 = Objects.equal(_name, _name_1);
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public AbstractConstraintTypeWithEnumValue(final ConstraintTypeIdentifier identifier, final String name) {
    super(identifier, name);
    final Map<T, String> validLiteralStringValues = this.getValidLiteralStringValues();
    boolean _isEmpty = validLiteralStringValues.isEmpty();
    if (_isEmpty) {
      throw new IllegalArgumentException("No string values given");
    }
    this.enumClassName = IterableExtensions.<T>toList(validLiteralStringValues.keySet()).get(0).getClass().getSimpleName();
    int _size = validLiteralStringValues.values().size();
    int _size_1 = IterableExtensions.<String>toSet(validLiteralStringValues.values()).size();
    boolean _notEquals = (_size != _size_1);
    if (_notEquals) {
      throw new IllegalArgumentException(("Duplicate string values for enum " + 
        this.enumClassName));
    }
    final Function1<T, String> _function = (T it) -> {
      return it.name();
    };
    final Function1<T, T> _function_1 = (T it) -> {
      return it;
    };
    this.literalNamesToLiterals = IterableExtensions.<T, String, T>toMap(validLiteralStringValues.keySet(), _function, _function_1);
    final Function1<T, String> _function_2 = (T it) -> {
      return it.name();
    };
    final Function1<T, String> _function_3 = (T it) -> {
      return validLiteralStringValues.get(it);
    };
    this.validLiteralNamesAndStringValues = HashBiMap.<String, String>create(
      IterableExtensions.<T, String, String>toMap(validLiteralStringValues.keySet(), _function_2, _function_3));
    this.stringValuesSorted = IterableExtensions.<String>sort(validLiteralStringValues.values());
  }
  
  public abstract Map<T, String> getValidLiteralStringValues();
  
  /**
   * final def literalNameToLiteral(String literalName) {
   * return literalNamesToLiterals.get(literalName)
   * }
   */
  public final String literalNameToStringValue(final String literalName) {
    return this.validLiteralNamesAndStringValues.get(literalName);
  }
  
  public final String stringValueToLiteralName(final String stringValue) {
    return this.validLiteralNamesAndStringValues.inverse().get(stringValue);
  }
  
  @Override
  public final void checkValidValue(final String value) {
    boolean _contains = this.validLiteralNamesAndStringValues.keySet().contains(value);
    boolean _not = (!_contains);
    if (_not) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Invalid literal name \"");
      _builder.append(value);
      _builder.append("\" for enum ");
      String _plus = (_builder.toString() + 
        this.enumClassName);
      throw new IllegalArgumentException(_plus);
    }
  }
  
  @Override
  public final void checkValidValueInUserRepresentation(final String value) {
    boolean _contains = this.validLiteralNamesAndStringValues.inverse().keySet().contains(value);
    boolean _not = (!_contains);
    if (_not) {
      String _join = IterableExtensions.join(this.stringValuesSorted, ", ");
      String _plus = ("Please select a value from the following: " + _join);
      throw new IllegalArgumentException(_plus);
    }
  }
  
  /**
   * final def getLiteralNameAtSortIndex(int index) {
   * return stringValueToLiteralName(stringValuesSorted.get(index))
   * }
   */
  public final List<String> getStringValuesSorted() {
    return Collections.<String>unmodifiableList(this.stringValuesSorted);
  }
  
  @Override
  public final String convertValueToInternalRepresentation(final String value) {
    return this.stringValueToLiteralName(value);
  }
  
  @Override
  public final String convertValueToUserRepresentation(final String value) {
    return this.literalNameToStringValue(value);
  }
}
