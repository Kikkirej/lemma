package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.constraints_table;

import com.google.common.base.Objects;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.Constraint;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.AbstractConstraintType;
import java.util.EventObject;
import java.util.List;
import java.util.function.Function;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
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
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class TypeColumnEditingSupport extends ObservableValueEditingSupport<Constraint, AbstractConstraintType, String> {
  public static class EditorActivationListener extends ColumnViewerEditorActivationListener {
    private final ConstraintsTable constraintsTable;
    
    private AbstractConstraintType currentConstraintType;
    
    public EditorActivationListener(final ConstraintsTable constraintsTable) {
      this.constraintsTable = constraintsTable;
    }
    
    @Override
    public void beforeEditorActivated(final ColumnViewerEditorActivationEvent event) {
      this.currentConstraintType = this.getConstraintType(event);
    }
    
    private AbstractConstraintType getConstraintType(final EventObject event) {
      Object _source = event.getSource();
      final ViewerCell cell = ((ViewerCell) _source);
      Object _element = cell.getElement();
      return ((Constraint) _element).getType();
    }
    
    @Override
    public void afterEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {
      AbstractConstraintType _constraintType = this.getConstraintType(event);
      boolean _equals = Objects.equal(_constraintType, this.currentConstraintType);
      if (_equals) {
        return;
      }
      Object _source = event.getSource();
      final ViewerCell cell = ((ViewerCell) _source);
      Object _element = cell.getElement();
      final Constraint constraint = ((Constraint) _element);
      constraint.setValue("");
      this.constraintsTable.getViewer().update(constraint, null);
    }
    
    @Override
    public void afterEditorActivated(final ColumnViewerEditorActivationEvent event) {
    }
    
    @Override
    public void beforeEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {
    }
  }
  
  private final ConstraintsTable constraintsTable;
  
  private DataBindingContext dataBindingContext;
  
  private List<AbstractConstraintType> supportedConstraintTypes;
  
  private List<String> supportedConstraintTypesNames;
  
  public TypeColumnEditingSupport(final ConstraintsTable constraintsTable, final DataBindingContext dataBindingContext, final List<AbstractConstraintType> supportedConstraintTypes) {
    super(constraintsTable.getViewer(), dataBindingContext);
    this.constraintsTable = constraintsTable;
    this.dataBindingContext = dataBindingContext;
    final Function1<AbstractConstraintType, String> _function = (AbstractConstraintType it) -> {
      return it.getName();
    };
    this.supportedConstraintTypes = IterableExtensions.<AbstractConstraintType, String>sortBy(supportedConstraintTypes, _function);
    final Function1<AbstractConstraintType, String> _function_1 = (AbstractConstraintType it) -> {
      return it.getName();
    };
    this.supportedConstraintTypesNames = ListExtensions.<AbstractConstraintType, String>map(supportedConstraintTypes, _function_1);
    ColumnViewerEditor _columnViewerEditor = constraintsTable.getViewer().getColumnViewerEditor();
    TypeColumnEditingSupport.EditorActivationListener _editorActivationListener = new TypeColumnEditingSupport.EditorActivationListener(constraintsTable);
    _columnViewerEditor.addEditorActivationListener(_editorActivationListener);
  }
  
  @Override
  protected boolean canEdit(final Object element) {
    return this.constraintsTable.getEnabled();
  }
  
  @Override
  protected CellEditor getCellEditor(final Object element) {
    Table _table = this.constraintsTable.getViewer().getTable();
    return new ComboBoxCellEditor(_table, ((String[])Conversions.unwrapArray(this.supportedConstraintTypesNames, String.class)), 
      SWT.READ_ONLY);
  }
  
  @Override
  protected IObservableValue<String> doCreateCellEditorObservable(final CellEditor editor) {
    Control _control = editor.getControl();
    return WidgetProperties.ccomboSelection().observe(((CCombo) _control));
  }
  
  @Override
  protected IObservableValue<AbstractConstraintType> doCreateElementObservable(final Constraint constraint, final ViewerCell cell) {
    return BeanProperties.<Constraint, AbstractConstraintType>value(Constraint.class, "type", null).observe(constraint);
  }
  
  @Override
  protected Binding createBinding(final IObservableValue<String> target, final IObservableValue<AbstractConstraintType> model) {
    final UpdateValueStrategy<AbstractConstraintType, String> modelToTargetConverter = new UpdateValueStrategy<AbstractConstraintType, String>();
    final Function<AbstractConstraintType, String> _function = (AbstractConstraintType it) -> {
      return it.getName();
    };
    modelToTargetConverter.setConverter(IConverter.<AbstractConstraintType, String>create(_function));
    final UpdateValueStrategy<String, AbstractConstraintType> targetToModelConverter = new UpdateValueStrategy<String, AbstractConstraintType>();
    final Function<Object, AbstractConstraintType> _function_1 = (Object it) -> {
      return this.supportedConstraintTypes.get(this.supportedConstraintTypesNames.indexOf(it));
    };
    targetToModelConverter.setConverter(IConverter.<Object, AbstractConstraintType>create(_function_1));
    return this.dataBindingContext.<String, AbstractConstraintType>bindValue(target, model, targetToModelConverter, modelToTargetConverter);
  }
}
