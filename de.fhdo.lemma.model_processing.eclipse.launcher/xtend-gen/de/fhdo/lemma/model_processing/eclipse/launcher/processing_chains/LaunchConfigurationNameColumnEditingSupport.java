package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains;

import java.util.List;
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
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class LaunchConfigurationNameColumnEditingSupport extends ObservableValueEditingSupport<ProcessingChainEntry, String, String> {
  private TableViewer tableViewer;
  
  private DataBindingContext dataBindingContext;
  
  private final List<String> launchConfigurationNames;
  
  public LaunchConfigurationNameColumnEditingSupport(final TableViewer tableViewer, final DataBindingContext dataBindingContext, final List<String> launchConfigurationNames) {
    super(tableViewer, dataBindingContext);
    this.tableViewer = tableViewer;
    this.dataBindingContext = dataBindingContext;
    this.launchConfigurationNames = launchConfigurationNames;
    ColumnViewerEditor _columnViewerEditor = tableViewer.getColumnViewerEditor();
    _columnViewerEditor.addEditorActivationListener(
      new ColumnViewerEditorActivationListener() {
        @Override
        public void afterEditorDeactivated(final ColumnViewerEditorDeactivationEvent event) {
          Object _source = event.getSource();
          final ViewerCell cell = ((ViewerCell) _source);
          tableViewer.update(cell.getElement(), null);
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
  protected CellEditor getCellEditor(final Object element) {
    Table _table = this.tableViewer.getTable();
    return new ComboBoxCellEditor(_table, ((String[])Conversions.unwrapArray(this.launchConfigurationNames, String.class)), 
      (SWT.DROP_DOWN | SWT.READ_ONLY));
  }
  
  @Override
  protected IObservableValue<String> doCreateCellEditorObservable(final CellEditor editor) {
    Control _control = editor.getControl();
    return WidgetProperties.ccomboSelection().observe(((CCombo) _control));
  }
  
  @Override
  protected IObservableValue<String> doCreateElementObservable(final ProcessingChainEntry entry, final ViewerCell cell) {
    return BeanProperties.<ProcessingChainEntry, String>value(ProcessingChainEntry.class, "launchConfigurationName", null).observe(entry);
  }
  
  @Override
  protected Binding createBinding(final IObservableValue<String> target, final IObservableValue<String> model) {
    UpdateValueStrategy<String, String> _updateValueStrategy = new UpdateValueStrategy<String, String>(UpdateValueStrategy.POLICY_UPDATE);
    return this.dataBindingContext.<String, String>bindValue(target, model, _updateValueStrategy, null);
  }
}
