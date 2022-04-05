package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds;

import com.google.common.base.Objects;
import java.util.HashMap;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class ArgumentKindFactory {
  private static final HashMap<ArgumentKindIdentifier, AbstractArgumentKind> CREATED_KINDS = CollectionLiterals.<ArgumentKindIdentifier, AbstractArgumentKind>newHashMap();
  
  public static AbstractArgumentKind fromIdentifier(final String identifierString) {
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(identifierString);
    if (_isNullOrEmpty) {
      return null;
    }
    try {
      return ArgumentKindFactory.fromIdentifier(ArgumentKindIdentifier.valueOf(identifierString));
    } catch (final Throwable _t) {
      if (_t instanceof IllegalArgumentException) {
        throw ArgumentKindFactory.unsupportedIdentifierException(identifierString);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public static AbstractArgumentKind fromIdentifier(final ArgumentKindIdentifier identifier) {
    if ((identifier == null)) {
      return null;
    }
    final AbstractArgumentKind existingKind = ArgumentKindFactory.CREATED_KINDS.get(identifier);
    if ((existingKind != null)) {
      return existingKind;
    }
    AbstractArgumentKind _switchResult = null;
    boolean _matched = false;
    if (Objects.equal(identifier, ConstantParameterArgumentKind.IDENTIFIER)) {
      _matched=true;
      _switchResult = new ConstantParameterArgumentKind();
    }
    if (!_matched) {
      if (Objects.equal(identifier, MultiValuedParameterArgumentKind.IDENTIFIER)) {
        _matched=true;
        _switchResult = new MultiValuedParameterArgumentKind();
      }
    }
    if (!_matched) {
      if (Objects.equal(identifier, SingleValuedParameterArgumentKind.IDENTIFIER)) {
        _matched=true;
        _switchResult = new SingleValuedParameterArgumentKind();
      }
    }
    if (!_matched) {
      throw ArgumentKindFactory.unsupportedIdentifierException(identifier.toString());
    }
    final AbstractArgumentKind createdType = _switchResult;
    ArgumentKindFactory.CREATED_KINDS.put(identifier, createdType);
    return createdType;
  }
  
  private static IllegalArgumentException unsupportedIdentifierException(final String identifierString) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    _builder.append(identifierString);
    _builder.append("\" not supported");
    String _plus = ("Creation of argument kind with identifier " + _builder);
    return new IllegalArgumentException(_plus);
  }
}
