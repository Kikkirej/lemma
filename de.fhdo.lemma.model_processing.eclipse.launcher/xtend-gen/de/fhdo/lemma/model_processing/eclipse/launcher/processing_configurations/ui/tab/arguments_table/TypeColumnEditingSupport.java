package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.arguments_table;

import com.google.common.base.Objects;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentTypeWithEnumValueSelection;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportWithAliasArgumentType;
import java.util.EventObject;
import java.util.List;
import java.util.function.Function;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class TypeColumnEditingSupport extends ObservableValueEditingSupport<Argument, AbstractArgumentType, String> {
  public static class EditorActivationListener extends ColumnViewerEditorActivationListener {
    private final ArgumentsTable argumentsTable;
    
    private AbstractArgumentType currentArgumentType;
    
    public EditorActivationListener(final ArgumentsTable argumentsTable) {
      this.argumentsTable = argumentsTable;
    }
    
    @Override
    public void beforeEditorActivated(final ColumnViewerEditorActivationEvent event) {
      this.currentArgumentType = this.getArgumentType(event);
    }
    
    private AbstractArgumentType getArgumentType(final EventObject event) {
      Object _source = event.getSource();
      final ViewerCell cell = ((ViewerCell) _source);
      Object _element = cell.getElement();
      return ((Argument) _element).getType();
    }
    
    @Override
    public void afterEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {
      AbstractArgumentType _argumentType = this.getArgumentType(event);
      boolean _equals = Objects.equal(_argumentType, this.currentArgumentType);
      if (_equals) {
        return;
      }
      Object _source = event.getSource();
      final ViewerCell cell = ((ViewerCell) _source);
      Object _element = cell.getElement();
      final Argument argument = ((Argument) _element);
      argument.setParameter("");
      final AbstractArgumentType argumentType = argument.getType();
      boolean _matched = false;
      if (argumentType instanceof AbstractArgumentTypeWithEnumValueSelection) {
        _matched=true;
        final List<String> items = ((AbstractArgumentTypeWithEnumValueSelection<?>)argumentType).getStringValuesSorted();
        boolean _isEmpty = items.isEmpty();
        boolean _not = (!_isEmpty);
        if (_not) {
          argument.setValue(items.get(0));
        }
      }
      if (!_matched) {
        if (argumentType instanceof IntermediateModelOfImportWithAliasArgumentType) {
          _matched=true;
          try {
            final ProcessingConfiguration configuration = this.argumentsTable.getCurrentConfiguration();
            final Function1<Pair<Class<? extends EObject>, Utils.ImportInfo>, String> _function = (Pair<Class<? extends EObject>, Utils.ImportInfo> it) -> {
              return it.getValue().getAlias();
            };
            final List<String> importAliases = IterableExtensions.<String>sort(ListExtensions.<Pair<Class<? extends EObject>, Utils.ImportInfo>, String>map(((IntermediateModelOfImportWithAliasArgumentType)argumentType).parseTransformableImportedModelsOfSourceModelFile(configuration), _function));
            boolean _isEmpty = importAliases.isEmpty();
            boolean _not = (!_isEmpty);
            if (_not) {
              argument.setValue(importAliases.get(0));
            }
          } catch (final Throwable _t) {
            if (_t instanceof IllegalArgumentException) {
              argument.setValue("");
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
        }
      }
      if (!_matched) {
        argument.setValue("");
      }
      this.argumentsTable.getViewer().update(argument, null);
    }
    
    @Override
    public void afterEditorActivated(final ColumnViewerEditorActivationEvent event) {
    }
    
    @Override
    public void beforeEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {
    }
  }
  
  private final ArgumentsTable argumentsTable;
  
  private final DataBindingContext dataBindingContext;
  
  private List<AbstractArgumentType> supportedArgumentTypes;
  
  private List<String> supportedArgumentTypesNames;
  
  public TypeColumnEditingSupport(final ArgumentsTable argumentsTable, final DataBindingContext dataBindingContext) {
    super(argumentsTable.getViewer(), dataBindingContext);
    this.argumentsTable = argumentsTable;
    this.dataBindingContext = dataBindingContext;
    ColumnViewerEditor _columnViewerEditor = argumentsTable.getViewer().getColumnViewerEditor();
    TypeColumnEditingSupport.EditorActivationListener _editorActivationListener = new TypeColumnEditingSupport.EditorActivationListener(argumentsTable);
    _columnViewerEditor.addEditorActivationListener(_editorActivationListener);
  }
  
  @Override
  protected boolean canEdit(final Object element) {
    return this.argumentsTable.getEnabled();
  }
  
  @Override
  protected CellEditor getCellEditor(final Object element) {
    final Function1<AbstractArgumentType, String> _function = (AbstractArgumentType it) -> {
      return it.getName();
    };
    this.supportedArgumentTypes = IterableExtensions.<AbstractArgumentType, String>sortBy(((Argument) element).getKind().getSupportedArgumentTypes(), _function);
    final Function1<AbstractArgumentType, String> _function_1 = (AbstractArgumentType it) -> {
      return it.getName();
    };
    this.supportedArgumentTypesNames = ListExtensions.<AbstractArgumentType, String>map(this.supportedArgumentTypes, _function_1);
    Table _table = this.argumentsTable.getViewer().getTable();
    return new ComboBoxCellEditor(_table, ((String[])Conversions.unwrapArray(this.supportedArgumentTypesNames, String.class)), 
      (SWT.DROP_DOWN | SWT.READ_ONLY));
  }
  
  @Override
  protected IObservableValue<String> doCreateCellEditorObservable(final CellEditor editor) {
    Control _control = editor.getControl();
    return WidgetProperties.ccomboSelection().observe(((CCombo) _control));
  }
  
  @Override
  protected IObservableValue<AbstractArgumentType> doCreateElementObservable(final Argument argument, final ViewerCell cell) {
    return BeanProperties.<Argument, AbstractArgumentType>value(Argument.class, "type", null).observe(argument);
  }
  
  @Override
  protected Binding createBinding(final IObservableValue<String> target, final IObservableValue<AbstractArgumentType> model) {
    final UpdateValueStrategy<AbstractArgumentType, String> modelToTargetConverter = new UpdateValueStrategy<AbstractArgumentType, String>();
    final Function<AbstractArgumentType, String> _function = (AbstractArgumentType it) -> {
      return it.getName();
    };
    modelToTargetConverter.setConverter(IConverter.<AbstractArgumentType, String>create(_function));
    final UpdateValueStrategy<String, AbstractArgumentType> targetToModelConverter = new UpdateValueStrategy<String, AbstractArgumentType>();
    final Function<Object, AbstractArgumentType> _function_1 = (Object it) -> {
      return this.supportedArgumentTypes.get(this.supportedArgumentTypesNames.indexOf(it));
    };
    targetToModelConverter.setConverter(IConverter.<Object, AbstractArgumentType>create(_function_1));
    return this.dataBindingContext.<String, AbstractArgumentType>bindValue(target, model, targetToModelConverter, modelToTargetConverter);
  }
}
