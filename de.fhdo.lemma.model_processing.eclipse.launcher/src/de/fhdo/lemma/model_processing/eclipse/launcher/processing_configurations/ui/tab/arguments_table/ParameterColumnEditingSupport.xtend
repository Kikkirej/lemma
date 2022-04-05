package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.arguments_table

import org.eclipse.jface.databinding.swt.typed.WidgetProperties
import org.eclipse.core.databinding.UpdateValueStrategy
import org.eclipse.jface.viewers.TextCellEditor
import org.eclipse.swt.SWT
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport
import org.eclipse.jface.viewers.CellEditor
import org.eclipse.jface.viewers.ViewerCell
import org.eclipse.core.databinding.observable.value.IObservableValue
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent
import org.eclipse.core.databinding.beans.typed.BeanProperties
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import org.eclipse.core.databinding.DataBindingContext

class ParameterColumnEditingSupport
    extends ObservableValueEditingSupport<Argument, String, String> {
    val ArgumentsTable argumentsTable
    var Argument currentArgument
    DataBindingContext dataBindingContext

    new(ArgumentsTable argumentsTable, DataBindingContext dataBindingContext) {
        super(argumentsTable.viewer, dataBindingContext)
        this.argumentsTable = argumentsTable
        this.dataBindingContext = dataBindingContext

        argumentsTable.viewer.columnViewerEditor.addEditorActivationListener(
            new ColumnViewerEditorActivationListener() {
                override afterEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
                    val cell = event.source as ViewerCell
                    // Call update() on element as otherwise a new value won't be displayed in the
                    // column
                    argumentsTable.viewer.update(cell.element, null)
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

    override protected canEdit(Object element) {
        argumentsTable.enabled
    }

    override protected getCellEditor(Object element) {
        return new TextCellEditor(argumentsTable.viewer.table)
    }

    override protected doCreateCellEditorObservable(CellEditor editor) {
        return WidgetProperties.text(SWT.Modify).observe(editor.control)
    }

    override protected doCreateElementObservable(Argument argument, ViewerCell cell) {
        currentArgument = argument
        return BeanProperties.value(Argument, "parameter", null).observe(argument)
    }

    override protected createBinding(IObservableValue<String> target,
        IObservableValue<String> model) {

        val updateStrategy = null /*new UpdateValueStrategy<String, String>()
        updateStrategy.afterGetValidator = [value |
            return try {
                currentArgument.validParameterValue(value)
                ValidationStatus.ok()
            } catch (IllegalArgumentException ex)
                ValidationStatus.error(ex.message)
        ]*/

        return dataBindingContext.bindValue(target, model,
            new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), null)
    }
}