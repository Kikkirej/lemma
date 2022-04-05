package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.constraints_table;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.Constraint;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.AbstractConstraintType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.AbstractConstraintTypeWithEnumValue;
import java.util.List;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class ValueColumnEditingSupport extends ObservableValueEditingSupport<Constraint, String, String> {
  private final ConstraintsTable constraintsTable;
  
  private DataBindingContext dataBindingContext;
  
  public ValueColumnEditingSupport(final ConstraintsTable constraintsTable, final DataBindingContext dataBindingContext) {
    super(constraintsTable.getViewer(), dataBindingContext);
    this.constraintsTable = constraintsTable;
    this.dataBindingContext = dataBindingContext;
    ColumnViewerEditor _columnViewerEditor = constraintsTable.getViewer().getColumnViewerEditor();
    _columnViewerEditor.addEditorActivationListener(
      new ColumnViewerEditorActivationListener() {
        @Override
        public void afterEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {
          Object _source = event.getSource();
          final ViewerCell cell = ((ViewerCell) _source);
          constraintsTable.getViewer().update(cell.getElement(), null);
        }
        
        @Override
        public void afterEditorActivated(final ColumnViewerEditorActivationEvent event) {
        }
        
        @Override
        public void beforeEditorActivated(final ColumnViewerEditorActivationEvent event) {
        }
        
        @Override
        public void beforeEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {
        }
      });
  }
  
  @Override
  protected boolean canEdit(final Object element) {
    return this.constraintsTable.getEnabled();
  }
  
  @Override
  protected CellEditor getCellEditor(final Object element) {
    final Constraint constraint = ((Constraint) element);
    final AbstractConstraintType constraintType = constraint.getType();
    CellEditor _switchResult = null;
    boolean _matched = false;
    if (constraintType instanceof AbstractConstraintTypeWithEnumValue) {
      _matched=true;
      Table _table = this.constraintsTable.getViewer().getTable();
      List<String> _stringValuesSorted = ((AbstractConstraintTypeWithEnumValue<? extends Enum<?>>)constraintType).getStringValuesSorted();
      _switchResult = new ComboBoxCellEditor(_table, ((String[])Conversions.unwrapArray(_stringValuesSorted, String.class)), SWT.READ_ONLY);
    }
    if (!_matched) {
      if (constraintType instanceof AbstractConstraintType) {
        _matched=true;
        Table _table = this.constraintsTable.getViewer().getTable();
        _switchResult = new TextCellEditor(_table);
      }
    }
    return _switchResult;
  }
  
  @Override
  protected IObservableValue<String> doCreateCellEditorObservable(final CellEditor editor) {
    ISWTObservableValue<String> _switchResult = null;
    boolean _matched = false;
    if (editor instanceof TextCellEditor) {
      _matched=true;
      _switchResult = WidgetProperties.<Control>text(SWT.Modify).observe(((TextCellEditor)editor).getControl());
    }
    if (!_matched) {
      if (editor instanceof ComboBoxCellEditor) {
        _matched=true;
        Control _control = ((ComboBoxCellEditor)editor).getControl();
        _switchResult = WidgetProperties.ccomboSelection().observe(((CCombo) _control));
      }
    }
    return _switchResult;
  }
  
  @Override
  protected IObservableValue<String> doCreateElementObservable(final Constraint constraint, final ViewerCell cell) {
    return BeanProperties.<Constraint, String>value(Constraint.class, "value", null).observe(constraint);
  }
  
  @Override
  protected Binding createBinding(final IObservableValue<String> target, final IObservableValue<String> model) {
    UpdateValueStrategy<String, String> _updateValueStrategy = new UpdateValueStrategy<String, String>(UpdateValueStrategy.POLICY_UPDATE);
    return this.dataBindingContext.<String, String>bindValue(target, model, _updateValueStrategy, null);
  }
}
