package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types;

import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class ModelKindConstraintType extends AbstractConstraintTypeWithEnumValue<LemmaModelKind> {
  public static final String NAME = "LEMMA Model Kind";
  
  public static final ConstraintTypeIdentifier IDENTIFIER = ConstraintTypeIdentifier.MODEL_KIND;
  
  private static final Map<LemmaModelKind, List<String>> FILE_EXTENSIONS = Collections.<LemmaModelKind, List<String>>unmodifiableMap(CollectionLiterals.<LemmaModelKind, List<String>>newHashMap(Pair.<LemmaModelKind, List<String>>of(LemmaModelKind.DOMAIN, LemmaUiUtils.getFileExtensions(LemmaUiUtils.DATA_DSL_EDITOR_ID)), Pair.<LemmaModelKind, List<String>>of(LemmaModelKind.MAPPING, LemmaUiUtils.getFileExtensions(LemmaUiUtils.MAPPING_DSL_EDITOR_ID)), Pair.<LemmaModelKind, List<String>>of(LemmaModelKind.OPERATION, LemmaUiUtils.getFileExtensions(LemmaUiUtils.OPERATION_DSL_EDITOR_ID)), Pair.<LemmaModelKind, List<String>>of(LemmaModelKind.SERVICE, LemmaUiUtils.getFileExtensions(LemmaUiUtils.SERVICE_DSL_EDITOR_ID)), Pair.<LemmaModelKind, List<String>>of(LemmaModelKind.TECHNOLOGY, LemmaUiUtils.getFileExtensions(LemmaUiUtils.TECHNOLOGY_DSL_EDITOR_ID))));
  
  public static List<String> getFileExtensions(final LemmaModelKind modelKind) {
    final List<String> extensions = ModelKindConstraintType.FILE_EXTENSIONS.get(modelKind);
    boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(extensions);
    if (_isNullOrEmpty) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Model kind ");
      _builder.append(modelKind);
      _builder.append(" does not have file ");
      String _plus = (_builder.toString() + 
        "extensions associated");
      throw new IllegalArgumentException(_plus);
    }
    return extensions;
  }
  
  public LemmaModelKind stringValueToModelKind(final String stringValue) {
    final String literalName = this.stringValueToLiteralName(stringValue);
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(literalName);
    if (_isNullOrEmpty) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("\"");
      _builder.append(stringValue);
      _builder.append("\"");
      String _plus = ("No model kind found for string value " + _builder);
      throw new IllegalArgumentException(_plus);
    }
    return LemmaModelKind.valueOf(literalName);
  }
  
  public ModelKindConstraintType() {
    super(ModelKindConstraintType.IDENTIFIER, ModelKindConstraintType.NAME);
  }
  
  @Override
  public Map<LemmaModelKind, String> getValidLiteralStringValues() {
    final Function1<LemmaModelKind, LemmaModelKind> _function = (LemmaModelKind it) -> {
      return it;
    };
    final Function1<LemmaModelKind, String> _function_1 = (LemmaModelKind it) -> {
      return StringExtensions.toFirstUpper(it.name().toLowerCase());
    };
    return IterableExtensions.<LemmaModelKind, LemmaModelKind, String>toMap(((Iterable<? extends LemmaModelKind>)Conversions.doWrapArray(LemmaModelKind.values())), _function, _function_1);
  }
}
