package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains;

import java.util.Set;
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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class PreviousExitValueComparatorColumnEditingSupport extends ObservableValueEditingSupport<ProcessingChainEntry, PreviousExitValueComparator.Comparator, String> {
  private TableViewer tableViewer;
  
  private DataBindingContext dataBindingContext;
  
  public PreviousExitValueComparatorColumnEditingSupport(final TableViewer tableViewer, final DataBindingContext dataBindingContext) {
    super(tableViewer, dataBindingContext);
    this.tableViewer = tableViewer;
    this.dataBindingContext = dataBindingContext;
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
    Set<String> _userRepresentationValues = PreviousExitValueComparator.getUserRepresentationValues();
    return new ComboBoxCellEditor(_table, ((String[])Conversions.unwrapArray(_userRepresentationValues, String.class)), 
      (SWT.DROP_DOWN | SWT.READ_ONLY));
  }
  
  @Override
  protected IObservableValue<String> doCreateCellEditorObservable(final CellEditor editor) {
    Control _control = editor.getControl();
    return WidgetProperties.ccomboSelection().observe(((CCombo) _control));
  }
  
  @Override
  protected IObservableValue<PreviousExitValueComparator.Comparator> doCreateElementObservable(final ProcessingChainEntry entry, final ViewerCell cell) {
    return BeanProperties.<ProcessingChainEntry, PreviousExitValueComparator.Comparator>value(ProcessingChainEntry.class, "previousExitValueComparator", null).observe(entry);
  }
  
  @Override
  protected Binding createBinding(final IObservableValue<String> target, final IObservableValue<PreviousExitValueComparator.Comparator> model) {
    final UpdateValueStrategy<PreviousExitValueComparator.Comparator, String> modelToTargetConverter = new UpdateValueStrategy<PreviousExitValueComparator.Comparator, String>();
    final Function<PreviousExitValueComparator.Comparator, String> _function = (PreviousExitValueComparator.Comparator it) -> {
      return PreviousExitValueComparator.getUserRepresentation(it);
    };
    modelToTargetConverter.setConverter(IConverter.<PreviousExitValueComparator.Comparator, String>create(_function));
    final UpdateValueStrategy<String, PreviousExitValueComparator.Comparator> targetToModelConverter = new UpdateValueStrategy<String, PreviousExitValueComparator.Comparator>();
    final Function<String, PreviousExitValueComparator.Comparator> _function_1 = (String it) -> {
      return PreviousExitValueComparator.getInternalRepresentation(it);
    };
    targetToModelConverter.setConverter(IConverter.<String, PreviousExitValueComparator.Comparator>create(_function_1));
    return this.dataBindingContext.<String, PreviousExitValueComparator.Comparator>bindValue(target, model, targetToModelConverter, modelToTargetConverter);
  }
  
  @Override
  protected boolean canEdit(final Object element) {
    return ((ProcessingChainEntry) element).isEditable();
  }
}
