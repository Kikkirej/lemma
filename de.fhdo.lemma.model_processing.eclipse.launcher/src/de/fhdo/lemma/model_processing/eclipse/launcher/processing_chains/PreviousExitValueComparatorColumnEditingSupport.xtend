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
import org.eclipse.core.databinding.UpdateValueStrategy
import org.eclipse.core.databinding.conversion.IConverter
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent

class PreviousExitValueComparatorColumnEditingSupport
    extends ObservableValueEditingSupport<
        ProcessingChainEntry, PreviousExitValueComparator.Comparator, String
    > {
    var TableViewer tableViewer
    var DataBindingContext dataBindingContext

    new(TableViewer tableViewer, DataBindingContext dataBindingContext) {
        super(tableViewer, dataBindingContext)
        this.tableViewer = tableViewer
        this.dataBindingContext = dataBindingContext

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
        return new ComboBoxCellEditor(
            tableViewer.table,
            PreviousExitValueComparator.userRepresentationValues,
            SWT.DROP_DOWN.bitwiseOr(SWT.READ_ONLY)
        )
    }

    override protected doCreateCellEditorObservable(CellEditor editor) {
        return WidgetProperties.ccomboSelection.observe(editor.control as CCombo)
    }

    override protected doCreateElementObservable(ProcessingChainEntry entry, ViewerCell cell) {
        return BeanProperties.value(ProcessingChainEntry, "previousExitValueComparator", null)
            .observe(entry)
    }

    override protected createBinding(IObservableValue<String> target,
        IObservableValue<PreviousExitValueComparator.Comparator> model) {
        val modelToTargetConverter
            = new UpdateValueStrategy<PreviousExitValueComparator.Comparator, String>()
        modelToTargetConverter.converter = IConverter.create([
            PreviousExitValueComparator.getUserRepresentation(it)
        ])

        val targetToModelConverter
            = new UpdateValueStrategy<String, PreviousExitValueComparator.Comparator>()
        targetToModelConverter.converter = IConverter.create([
            PreviousExitValueComparator.getInternalRepresentation(it)
        ])

        return dataBindingContext.bindValue(target, model, targetToModelConverter,
            modelToTargetConverter)
    }

    override protected canEdit(Object element) {
        return (element as ProcessingChainEntry).editable
    }
}