package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types;

import com.google.common.collect.Iterables;
import de.fhdo.lemma.eclipse.ui.ModelFileTypeDescription;
import de.fhdo.lemma.eclipse.ui.OperationModelTransformationStrategy;
import de.fhdo.lemma.eclipse.ui.ProgrammaticIntermediateModelTransformation;
import de.fhdo.lemma.eclipse.ui.ServiceModelTransformationStrategy;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class IntermediateModelsOfAllImportsArgumentType extends AbstractIntermediateModelArgumentTypeWithEnumValue<AllImportedIntermediateModelKinds> {
  public static final ArgumentTypeIdentifier IDENTIFIER = ArgumentTypeIdentifier.INTERMEDIATE_MODELS_OF_ALL_IMPORTS;
  
  public IntermediateModelsOfAllImportsArgumentType() {
    super(IntermediateModelsOfAllImportsArgumentType.IDENTIFIER, "Intermediate models of all imports");
  }
  
  @Override
  public Map<AllImportedIntermediateModelKinds, String> getValidLiteralStringValues() {
    Pair<AllImportedIntermediateModelKinds, String> _mappedTo = Pair.<AllImportedIntermediateModelKinds, String>of(AllImportedIntermediateModelKinds.ALL_DOMAIN_MODELS, "Intermediate representations of all imported data models");
    Pair<AllImportedIntermediateModelKinds, String> _mappedTo_1 = Pair.<AllImportedIntermediateModelKinds, String>of(AllImportedIntermediateModelKinds.ALL_OPERATION_MODELS, "Intermediate representations of all imported operation models");
    Pair<AllImportedIntermediateModelKinds, String> _mappedTo_2 = Pair.<AllImportedIntermediateModelKinds, String>of(AllImportedIntermediateModelKinds.ALL_SERVICE_MODELS, "Intermediate representations of all imported service models");
    return Collections.<AllImportedIntermediateModelKinds, String>unmodifiableMap(CollectionLiterals.<AllImportedIntermediateModelKinds, String>newHashMap(_mappedTo, _mappedTo_1, _mappedTo_2));
  }
  
  @Override
  public void checkValidValueInUserRepresentation(final ProcessingConfiguration configuration, final String value) {
    super.checkValidValueInUserRepresentation(configuration, value);
    IFile _sourceModelFile = configuration.getSourceModelFile();
    String _fileExtension = null;
    if (_sourceModelFile!=null) {
      _fileExtension=_sourceModelFile.getFileExtension();
    }
    final String sourceModelFileExtension = _fileExtension;
    if ((sourceModelFileExtension == null)) {
      return;
    }
    final Function1<ModelFileTypeDescription, List<String>> _function = (ModelFileTypeDescription it) -> {
      return it.getExtensions();
    };
    final Iterable<String> transformableFileExtensions = Iterables.<String>concat(IterableExtensions.<ModelFileTypeDescription, List<String>>map(ProgrammaticIntermediateModelTransformation.getTransformationStrategy(sourceModelFileExtension).getModelFileTypeDescriptions().values(), _function));
    final AllImportedIntermediateModelKinds intermediateModelKind = AllImportedIntermediateModelKinds.valueOf(this.stringValueToLiteralName(value));
    List<String> _switchResult = null;
    if (intermediateModelKind != null) {
      switch (intermediateModelKind) {
        case ALL_DOMAIN_MODELS:
          _switchResult = ServiceModelTransformationStrategy.DATA_MODEL_FILE_EXTENSIONS;
          break;
        case ALL_OPERATION_MODELS:
          _switchResult = OperationModelTransformationStrategy.OPERATION_MODEL_FILE_EXTENSIONS;
          break;
        case ALL_SERVICE_MODELS:
          _switchResult = ServiceModelTransformationStrategy.SERVICE_MODEL_FILE_EXTENSIONS;
          break;
        default:
          break;
      }
    }
    final List<String> importedModelFileExtensions = _switchResult;
    final Function1<String, Boolean> _function_1 = (String it) -> {
      return Boolean.valueOf(importedModelFileExtensions.contains(it));
    };
    boolean _exists = IterableExtensions.<String>exists(transformableFileExtensions, _function_1);
    if (_exists) {
      return;
    }
    String _xifexpression = null;
    int _size = importedModelFileExtensions.size();
    boolean _equals = (_size == 1);
    if (_equals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("kind *.");
      String _get = importedModelFileExtensions.get(0);
      _builder.append(_get);
      _xifexpression = _builder.toString();
    } else {
      final Function1<String, String> _function_2 = (String it) -> {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("*.");
        _builder_1.append(it);
        return _builder_1.toString();
      };
      String _join = IterableExtensions.join(ListExtensions.<String, String>map(importedModelFileExtensions, _function_2), ", ");
      _xifexpression = ("kinds " + _join);
    }
    final String kindHint = _xifexpression;
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("support transformation of imported model files of ");
    _builder_1.append(kindHint);
    String _plus = ("Source model intermediate transformation does not " + _builder_1);
    throw new IllegalArgumentException(_plus);
  }
}
