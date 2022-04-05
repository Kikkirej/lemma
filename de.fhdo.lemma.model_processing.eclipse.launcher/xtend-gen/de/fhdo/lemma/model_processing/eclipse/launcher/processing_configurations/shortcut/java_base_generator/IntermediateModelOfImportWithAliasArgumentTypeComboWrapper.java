package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator;

import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportWithAliasArgumentType;
import java.util.Collections;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class IntermediateModelOfImportWithAliasArgumentTypeComboWrapper {
  private final ProcessingConfiguration processingConfiguration;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private final Argument argument;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private final Combo combo;
  
  public IntermediateModelOfImportWithAliasArgumentTypeComboWrapper(final Composite parent, final int style, final ProcessingConfiguration processingConfiguration, final String parameterName) {
    this(parent, style, processingConfiguration, parameterName, Collections.<Class<? extends EObject>>unmodifiableList(CollectionLiterals.<Class<? extends EObject>>newArrayList()));
  }
  
  public IntermediateModelOfImportWithAliasArgumentTypeComboWrapper(final Composite parent, final int style, final ProcessingConfiguration processingConfiguration, final String parameterName, final List<Class<? extends EObject>> filteredModelTypes) {
    Combo _combo = new Combo(parent, style);
    this.combo = _combo;
    this.processingConfiguration = processingConfiguration;
    this.argument = Argument.newArgument().singleValued().intermediateModelOfImportWithAlias("").parameter(parameterName);
    this.addItems(filteredModelTypes);
  }
  
  private void addItems(final List<Class<? extends EObject>> filteredModelTypes) {
    try {
      AbstractArgumentType _type = this.argument.getType();
      final Function1<Pair<Class<? extends EObject>, Utils.ImportInfo>, Boolean> _function = (Pair<Class<? extends EObject>, Utils.ImportInfo> it) -> {
        return Boolean.valueOf((filteredModelTypes.isEmpty() || filteredModelTypes.contains(it.getKey())));
      };
      final Function1<Pair<Class<? extends EObject>, Utils.ImportInfo>, String> _function_1 = (Pair<Class<? extends EObject>, Utils.ImportInfo> it) -> {
        return it.getValue().getAlias();
      };
      final Iterable<String> importAliases = IterableExtensions.<Pair<Class<? extends EObject>, Utils.ImportInfo>, String>map(IterableExtensions.<Pair<Class<? extends EObject>, Utils.ImportInfo>>filter(((IntermediateModelOfImportWithAliasArgumentType) _type).parseTransformableImportedModelsOfSourceModelFile(this.processingConfiguration), _function), _function_1);
      this.combo.setItems(((String[])Conversions.unwrapArray(importAliases, String.class)));
      boolean _isEmpty = IterableExtensions.isEmpty(importAliases);
      boolean _not = (!_isEmpty);
      if (_not) {
        this.argument.setValue(((String[])Conversions.unwrapArray(importAliases, String.class))[0]);
        this.combo.setText(this.argument.getValue());
      }
    } catch (final Throwable _t) {
      if (_t instanceof IllegalArgumentException) {
        final IllegalArgumentException ex = (IllegalArgumentException)_t;
        final ControlDecoration decoration = new ControlDecoration(this.combo, (SWT.TOP | SWT.LEFT));
        decoration.setDescriptionText(ex.getMessage());
        decoration.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
        decoration.show();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public void setLayoutData(final Object layoutData) {
    this.combo.setLayoutData(layoutData);
  }
  
  @Pure
  public Argument getArgument() {
    return this.argument;
  }
  
  @Pure
  public Combo getCombo() {
    return this.combo;
  }
}
