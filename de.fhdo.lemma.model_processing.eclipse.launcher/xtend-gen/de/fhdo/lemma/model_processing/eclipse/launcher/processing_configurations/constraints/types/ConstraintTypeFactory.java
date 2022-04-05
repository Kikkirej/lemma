package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types;

import java.util.HashMap;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class ConstraintTypeFactory {
  private static final HashMap<ConstraintTypeIdentifier, AbstractConstraintType> CREATED_TYPES = CollectionLiterals.<ConstraintTypeIdentifier, AbstractConstraintType>newHashMap();
  
  public static AbstractConstraintType fromIdentifier(final String identifierString) {
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(identifierString);
    if (_isNullOrEmpty) {
      return null;
    }
    try {
      return ConstraintTypeFactory.fromIdentifier(ConstraintTypeIdentifier.valueOf(identifierString));
    } catch (final Throwable _t) {
      if (_t instanceof IllegalArgumentException) {
        throw ConstraintTypeFactory.unsupportedIdentifierException(identifierString);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public static AbstractConstraintType fromIdentifier(final ConstraintTypeIdentifier identifier) {
    if ((identifier == null)) {
      return null;
    }
    final AbstractConstraintType existingType = ConstraintTypeFactory.CREATED_TYPES.get(identifier);
    if ((existingType != null)) {
      return existingType;
    }
    AbstractConstraintType _switchResult = null;
    if (identifier != null) {
      switch (identifier) {
        case FILENAME_REGEX:
          _switchResult = new FilenameRegexConstraintType();
          break;
        case MODEL_KIND:
          _switchResult = new ModelKindConstraintType();
          break;
        default:
          throw ConstraintTypeFactory.unsupportedIdentifierException(identifier.toString());
      }
    } else {
      throw ConstraintTypeFactory.unsupportedIdentifierException(identifier.toString());
    }
    final AbstractConstraintType createdType = _switchResult;
    ConstraintTypeFactory.CREATED_TYPES.put(identifier, createdType);
    return createdType;
  }
  
  private static IllegalArgumentException unsupportedIdentifierException(final String identifierString) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    _builder.append(identifierString);
    _builder.append("\" not supported");
    String _plus = ("Creation of constraint type with identifier " + _builder);
    return new IllegalArgumentException(_plus);
  }
}
