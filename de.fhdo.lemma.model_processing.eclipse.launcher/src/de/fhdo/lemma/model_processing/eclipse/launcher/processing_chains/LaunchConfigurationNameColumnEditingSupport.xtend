package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains

import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport
import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.viewers.ComboBoxCellEditor
import org.eclipse.swt.SWT
import org.eclipse.jface.viewers.CellEditor
import org.eclipse.swt.custom.CCombo
import org.eclipse.jface.databinding.swt.typed.WidgetProperties
import org.eclipse.jface.viewers.ViewerCell
import org.eclipse.core.databinding.beans.typed.BeanProperties
import org.eclipse.core.databinding.observable.value.IObservableValue
import java.util.List
import org.eclipse.core.databinding.UpdateValueStrategy
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent

class LaunchConfigurationNameColumnEditingSupport
    extends ObservableValueEditingSupport<ProcessingChainEntry, String, String> {
    var TableViewer tableViewer
    var DataBindingContext dataBindingContext
    val List<String> launchConfigurationNames

    new(TableViewer tableViewer, DataBindingContext dataBindingContext,
        List<String> launchConfigurationNames) {
        super(tableViewer, dataBindingContext)
        this.tableViewer = tableViewer
        this.dataBindingContext = dataBindingContext
        this.launchConfigurationNames = launchConfigurationNames

        tableViewer.columnViewerEditor.addEditorActivationListener(
            new ColumnViewerEditorActivationListener() {
                override afterEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
                    val cell = event.source as ViewerCell
                    // Call update() on element as otherwise a new value won't be displayed in the
                    // column
                    tableViewer.update(cell.element, null)
                }

                override afterEditorActivated(ColumnViewerEditorActivationEvent event) {
                    // NOOP
                }

                override beforeEditorActivated(ColumnViewerEditorActivationEvent event) {
                    // NOOP
                }

                override beforeEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
                    // NOOP
                }
            }
        )
    }

    override protected getCellEditor(Object element) {
        return new ComboBoxCellEditor(tableViewer.table, launchConfigurationNames,
            SWT.DROP_DOWN.bitwiseOr(SWT.READ_ONLY))
    }

    override protected doCreateCellEditorObservable(CellEditor editor) {
        return WidgetProperties.ccomboSelection.observe(editor.control as CCombo)
    }

    override protected doCreateElementObservable(ProcessingChainEntry entry, ViewerCell cell) {
        return BeanProperties.value(ProcessingChainEntry, "launchConfigurationName", null)
            .observe(entry)
    }

    override protected createBinding(IObservableValue<String> target,
        IObservableValue<String> model) {
        return dataBindingContext.bindValue(target, model,
            new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), null)
    }
}