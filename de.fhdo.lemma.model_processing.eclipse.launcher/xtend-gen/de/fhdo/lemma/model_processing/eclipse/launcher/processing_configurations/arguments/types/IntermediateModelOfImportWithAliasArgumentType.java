package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types;

import com.google.common.base.Objects;
import de.fhdo.lemma.data.DataModel;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import de.fhdo.lemma.operation.OperationModel;
import de.fhdo.lemma.service.ServiceModel;
import java.util.Collections;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class IntermediateModelOfImportWithAliasArgumentType extends AbstractArgumentType {
  public static final ArgumentTypeIdentifier IDENTIFIER = ArgumentTypeIdentifier.INTERMEDIATE_MODEL_WITH_IMPORT_ALIAS;
  
  private static final List<? extends Class<? extends EObject>> transformableModelTypes = Collections.<Class<? extends EObject>>unmodifiableList(CollectionLiterals.<Class<? extends EObject>>newArrayList(DataModel.class, ServiceModel.class, OperationModel.class));
  
  public IntermediateModelOfImportWithAliasArgumentType() {
    super(IntermediateModelOfImportWithAliasArgumentType.IDENTIFIER, "Intermediate model of import with alias");
  }
  
  @Override
  public void checkValidValue(final ProcessingConfiguration processingConfiguration, final String value) {
    final List<Pair<Class<? extends EObject>, Utils.ImportInfo>> imports = this.parseTransformableImportedModelsOfSourceModelFile(processingConfiguration);
    final Function1<Pair<Class<? extends EObject>, Utils.ImportInfo>, Boolean> _function = (Pair<Class<? extends EObject>, Utils.ImportInfo> it) -> {
      String _alias = it.getValue().getAlias();
      return Boolean.valueOf(Objects.equal(_alias, value));
    };
    boolean _exists = IterableExtensions.<Pair<Class<? extends EObject>, Utils.ImportInfo>>exists(imports, _function);
    boolean _not = (!_exists);
    if (_not) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("\"");
      _builder.append(value);
      _builder.append("\"");
      String _plus = ("Source model does not import model with alias " + _builder);
      throw new IllegalArgumentException(_plus);
    }
  }
  
  public List<Pair<Class<? extends EObject>, Utils.ImportInfo>> parseTransformableImportedModelsOfSourceModelFile(final ProcessingConfiguration processingConfiguration) {
    final IFile sourceModelFile = processingConfiguration.getSourceModelFile();
    if ((sourceModelFile == null)) {
      throw new IllegalArgumentException("Please specify an existing source model file");
    }
    final Function1<Pair<Class<? extends EObject>, Utils.ImportInfo>, Boolean> _function = (Pair<Class<? extends EObject>, Utils.ImportInfo> it) -> {
      return Boolean.valueOf(IntermediateModelOfImportWithAliasArgumentType.transformableModelTypes.contains(it.getKey()));
    };
    final List<Pair<Class<? extends EObject>, Utils.ImportInfo>> imports = IterableExtensions.<Pair<Class<? extends EObject>, Utils.ImportInfo>>toList(IterableExtensions.<Pair<Class<? extends EObject>, Utils.ImportInfo>>filter(Utils.parseImports(sourceModelFile), _function));
    boolean _isEmpty = imports.isEmpty();
    if (_isEmpty) {
      throw new IllegalArgumentException("Source model does not import other models");
    }
    return imports;
  }
}
