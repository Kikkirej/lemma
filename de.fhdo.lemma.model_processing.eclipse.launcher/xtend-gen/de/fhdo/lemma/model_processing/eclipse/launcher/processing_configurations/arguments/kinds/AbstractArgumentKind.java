package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds;

import com.google.common.base.Objects;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public abstract class AbstractArgumentKind {
  @Accessors(AccessorType.PUBLIC_GETTER)
  private final ArgumentKindIdentifier identifier;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private final String name;
  
  private final List<AbstractArgumentType> supportedArgumentTypes;
  
  public List<AbstractArgumentType> getSupportedArgumentTypes() {
    return Collections.<AbstractArgumentType>unmodifiableList(this.supportedArgumentTypes);
  }
  
  @Override
  public boolean equals(final Object o) {
    boolean _xifexpression = false;
    if ((o == this)) {
      _xifexpression = true;
    } else {
      boolean _xifexpression_1 = false;
      if ((!(o instanceof AbstractArgumentKind))) {
        _xifexpression_1 = false;
      } else {
        _xifexpression_1 = Objects.equal(this.identifier, ((AbstractArgumentKind) o).identifier);
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public AbstractArgumentKind(final ArgumentKindIdentifier identifier, final String name, final AbstractArgumentType... supportedArgumentTypes) {
    if ((identifier == null)) {
      throw new IllegalArgumentException("Argument kind identifier must not be null");
    } else {
      boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(name);
      if (_isNullOrEmpty) {
        throw new IllegalArgumentException("Argument kind name must not be null or empty");
      } else {
        boolean _isEmpty = ((List<AbstractArgumentType>)Conversions.doWrapArray(supportedArgumentTypes)).isEmpty();
        if (_isEmpty) {
          throw new IllegalArgumentException("Argument types must not be empty");
        }
      }
    }
    this.identifier = identifier;
    this.name = name;
    this.supportedArgumentTypes = ((List<AbstractArgumentType>)Conversions.doWrapArray(supportedArgumentTypes));
  }
  
  public final void checkValidArgumentType(final AbstractArgumentType type) {
    boolean _contains = this.supportedArgumentTypes.contains(type);
    boolean _not = (!_contains);
    if (_not) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Argument kind \"");
      _builder.append(this.name);
      _builder.append("\" does not support ");
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("argument type \"");
      String _name = type.getName();
      _builder_1.append(_name);
      _builder_1.append("\"");
      String _plus = (_builder.toString() + _builder_1);
      throw new IllegalArgumentException(_plus);
    }
  }
  
  public AbstractArgumentType firstSupportedArgumentType() {
    final Function1<AbstractArgumentType, String> _function = (AbstractArgumentType it) -> {
      return this.name;
    };
    return IterableExtensions.<AbstractArgumentType, String>sortBy(this.supportedArgumentTypes, _function).get(0);
  }
  
  @Pure
  public ArgumentKindIdentifier getIdentifier() {
    return this.identifier;
  }
  
  @Pure
  public String getName() {
    return this.name;
  }
}
