package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.constraints_table

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.Constraint
import org.eclipse.jface.databinding.swt.typed.WidgetProperties
import org.eclipse.core.databinding.UpdateValueStrategy
import org.eclipse.jface.viewers.TextCellEditor
import org.eclipse.swt.SWT
import org.eclipse.jface.viewers.ComboBoxCellEditor
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.AbstractConstraintTypeWithEnumValue
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.AbstractConstraintType
import org.eclipse.swt.custom.CCombo
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport
import org.eclipse.jface.viewers.CellEditor
import org.eclipse.jface.viewers.ViewerCell
import org.eclipse.core.databinding.observable.value.IObservableValue
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent
import org.eclipse.core.databinding.beans.typed.BeanProperties
import org.eclipse.core.databinding.DataBindingContext

class ValueColumnEditingSupport
    extends ObservableValueEditingSupport<Constraint, String, String> {
    val ConstraintsTable constraintsTable
    DataBindingContext dataBindingContext

    new(ConstraintsTable constraintsTable,
        DataBindingContext dataBindingContext) {
        super(constraintsTable.viewer, dataBindingContext)
        this.constraintsTable = constraintsTable
        this.dataBindingContext = dataBindingContext

        constraintsTable.viewer.columnViewerEditor.addEditorActivationListener(
            new ColumnViewerEditorActivationListener() {
                override afterEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
                    val cell = event.source as ViewerCell
                    // Call update() on element as otherwise a new value won't be displayed in the
                    // column
                    constraintsTable.viewer.update(cell.element, null)
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
        constraintsTable.enabled
    }

    override protected getCellEditor(Object element) {
        val constraint = element as Constraint
        val constraintType = constraint.type
        return switch (constraintType) {
            AbstractConstraintTypeWithEnumValue<? extends Enum<?>>:
                new ComboBoxCellEditor(constraintsTable.viewer.table,
                    constraintType.stringValuesSorted, SWT.READ_ONLY)
            AbstractConstraintType:
                new TextCellEditor(constraintsTable.viewer.table)
        }
    }

    override protected doCreateCellEditorObservable(CellEditor editor) {
        return switch editor {
            TextCellEditor: WidgetProperties.text(SWT.Modify).observe(editor.control)
            ComboBoxCellEditor: WidgetProperties.ccomboSelection.observe(editor.control as CCombo)
        }
    }

    override protected doCreateElementObservable(Constraint constraint, ViewerCell cell) {
        return BeanProperties.value(Constraint, "value", null).observe(constraint)
    }

    override protected createBinding(IObservableValue<String> target,
        IObservableValue<String> model) {
        return dataBindingContext.bindValue(target, model,
            new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), null)
    }
}