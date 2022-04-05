package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types;

import com.google.common.base.Objects;
import java.util.HashMap;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class ArgumentTypeFactory {
  private static final HashMap<ArgumentTypeIdentifier, AbstractArgumentType> CREATED_TYPES = CollectionLiterals.<ArgumentTypeIdentifier, AbstractArgumentType>newHashMap();
  
  public static AbstractArgumentType fromIdentifier(final String identifierString) {
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(identifierString);
    if (_isNullOrEmpty) {
      return null;
    }
    try {
      return ArgumentTypeFactory.fromIdentifier(ArgumentTypeIdentifier.valueOf(identifierString));
    } catch (final Throwable _t) {
      if (_t instanceof IllegalArgumentException) {
        throw ArgumentTypeFactory.unsupportedIdentifierException(identifierString);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public static AbstractArgumentType fromIdentifier(final ArgumentTypeIdentifier identifier) {
    if ((identifier == null)) {
      return null;
    }
    final AbstractArgumentType existingType = ArgumentTypeFactory.CREATED_TYPES.get(identifier);
    if ((existingType != null)) {
      return existingType;
    }
    AbstractArgumentType _switchResult = null;
    boolean _matched = false;
    if (Objects.equal(identifier, FileArgumentType.IDENTIFIER)) {
      _matched=true;
      _switchResult = new FileArgumentType();
    }
    if (!_matched) {
      if (Objects.equal(identifier, FolderArgumentType.IDENTIFIER)) {
        _matched=true;
        _switchResult = new FolderArgumentType();
      }
    }
    if (!_matched) {
      if (Objects.equal(identifier, IntermediateModelArgumentType.IDENTIFIER)) {
        _matched=true;
        _switchResult = new IntermediateModelArgumentType();
      }
    }
    if (!_matched) {
      if (Objects.equal(identifier, IntermediateModelOfImportArgumentType.IDENTIFIER)) {
        _matched=true;
        _switchResult = new IntermediateModelOfImportArgumentType();
      }
    }
    if (!_matched) {
      if (Objects.equal(identifier, IntermediateModelsOfAllImportsArgumentType.IDENTIFIER)) {
        _matched=true;
        _switchResult = new IntermediateModelsOfAllImportsArgumentType();
      }
    }
    if (!_matched) {
      if (Objects.equal(identifier, IntermediateModelOfImportWithAliasArgumentType.IDENTIFIER)) {
        _matched=true;
        _switchResult = new IntermediateModelOfImportWithAliasArgumentType();
      }
    }
    if (!_matched) {
      if (Objects.equal(identifier, RawStringArgumentType.IDENTIFIER)) {
        _matched=true;
        _switchResult = new RawStringArgumentType();
      }
    }
    if (!_matched) {
      if (Objects.equal(identifier, SourceModelArgumentType.IDENTIFIER)) {
        _matched=true;
        _switchResult = new SourceModelArgumentType();
      }
    }
    if (!_matched) {
      if (Objects.equal(identifier, StringPairArgumentType.IDENTIFIER)) {
        _matched=true;
        _switchResult = new StringPairArgumentType();
      }
    }
    if (!_matched) {
      throw ArgumentTypeFactory.unsupportedIdentifierException(identifier.toString());
    }
    final AbstractArgumentType createdType = _switchResult;
    ArgumentTypeFactory.CREATED_TYPES.put(identifier, createdType);
    return createdType;
  }
  
  private static IllegalArgumentException unsupportedIdentifierException(final String identifierString) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    _builder.append(identifierString);
    _builder.append("\" not supported");
    String _plus = ("Creation of argument type with identifier " + _builder);
    return new IllegalArgumentException(_plus);
  }
}
