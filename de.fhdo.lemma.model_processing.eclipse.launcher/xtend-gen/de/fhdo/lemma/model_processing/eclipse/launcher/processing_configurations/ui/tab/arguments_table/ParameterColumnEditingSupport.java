package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.arguments_table;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;

@SuppressWarnings("all")
public class ParameterColumnEditingSupport extends ObservableValueEditingSupport<Argument, String, String> {
  private final ArgumentsTable argumentsTable;
  
  private Argument currentArgument;
  
  private DataBindingContext dataBindingContext;
  
  public ParameterColumnEditingSupport(final ArgumentsTable argumentsTable, final DataBindingContext dataBindingContext) {
    super(argumentsTable.getViewer(), dataBindingContext);
    this.argumentsTable = argumentsTable;
    this.dataBindingContext = dataBindingContext;
    ColumnViewerEditor _columnViewerEditor = argumentsTable.getViewer().getColumnViewerEditor();
    _columnViewerEditor.addEditorActivationListener(
      new ColumnViewerEditorActivationListener() {
        @Override
        public void afterEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {
          Object _source = event.getSource();
          final ViewerCell cell = ((ViewerCell) _source);
          argumentsTable.getViewer().update(cell.getElement(), null);
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
    return this.argumentsTable.getEnabled();
  }
  
  @Override
  protected CellEditor getCellEditor(final Object element) {
    Table _table = this.argumentsTable.getViewer().getTable();
    return new TextCellEditor(_table);
  }
  
  @Override
  protected IObservableValue<String> doCreateCellEditorObservable(final CellEditor editor) {
    return WidgetProperties.<Control>text(SWT.Modify).observe(editor.getControl());
  }
  
  @Override
  protected IObservableValue<String> doCreateElementObservable(final Argument argument, final ViewerCell cell) {
    this.currentArgument = argument;
    return BeanProperties.<Argument, String>value(Argument.class, "parameter", null).observe(argument);
  }
  
  @Override
  protected Binding createBinding(final IObservableValue<String> target, final IObservableValue<String> model) {
    final Object updateStrategy = null;
    UpdateValueStrategy<String, String> _updateValueStrategy = new UpdateValueStrategy<String, String>(UpdateValueStrategy.POLICY_UPDATE);
    return this.dataBindingContext.<String, String>bindValue(target, model, _updateValueStrategy, null);
  }
}
