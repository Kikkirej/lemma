package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains;

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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class PreviousExitValueColumnEditingSupport extends ObservableValueEditingSupport<ProcessingChainEntry, Integer, String> {
  private TableViewer tableViewer;
  
  private DataBindingContext dataBindingContext;
  
  public PreviousExitValueColumnEditingSupport(final TableViewer tableViewer, final DataBindingContext dataBindingContext) {
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
    return new TextCellEditor(_table);
  }
  
  @Override
  protected IObservableValue<String> doCreateCellEditorObservable(final CellEditor editor) {
    return WidgetProperties.<Control>text(SWT.Modify).observe(editor.getControl());
  }
  
  @Override
  protected IObservableValue<Integer> doCreateElementObservable(final ProcessingChainEntry entry, final ViewerCell cell) {
    return BeanProperties.<ProcessingChainEntry, Integer>value(ProcessingChainEntry.class, "previousExitValue", null).observe(entry);
  }
  
  @Override
  protected Binding createBinding(final IObservableValue<String> target, final IObservableValue<Integer> model) {
    final UpdateValueStrategy<Integer, String> modelToTargetConverter = new UpdateValueStrategy<Integer, String>();
    final Function<Integer, String> _function = (Integer it) -> {
      String _elvis = null;
      String _string = null;
      if (it!=null) {
        _string=it.toString();
      }
      if (_string != null) {
        _elvis = _string;
      } else {
        _elvis = "";
      }
      return _elvis;
    };
    modelToTargetConverter.setConverter(IConverter.<Integer, String>create(_function));
    final UpdateValueStrategy<String, Integer> targetToModelConverter = new UpdateValueStrategy<String, Integer>();
    final Function<String, Integer> _function_1 = (String it) -> {
      Integer _xtrycatchfinallyexpression = null;
      try {
        _xtrycatchfinallyexpression = Integer.valueOf(it);
      } catch (final Throwable _t) {
        if (_t instanceof NumberFormatException) {
          _xtrycatchfinallyexpression = null;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      return _xtrycatchfinallyexpression;
    };
    targetToModelConverter.setConverter(IConverter.<String, Integer>create(_function_1));
    return this.dataBindingContext.<String, Integer>bindValue(target, model, targetToModelConverter, modelToTargetConverter);
  }
  
  @Override
  protected boolean canEdit(final Object element) {
    return ((ProcessingChainEntry) element).isEditable();
  }
}
