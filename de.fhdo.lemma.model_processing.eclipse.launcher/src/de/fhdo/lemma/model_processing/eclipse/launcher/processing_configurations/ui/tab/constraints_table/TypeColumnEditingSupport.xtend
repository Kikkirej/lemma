package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.constraints_table

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.Constraint
import org.eclipse.jface.viewers.ComboBoxCellEditor
import org.eclipse.jface.databinding.swt.typed.WidgetProperties
import org.eclipse.swt.custom.CCombo
import org.eclipse.core.databinding.UpdateValueStrategy
import org.eclipse.core.databinding.conversion.IConverter
import java.util.List
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.AbstractConstraintType
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport
import org.eclipse.jface.viewers.CellEditor
import org.eclipse.jface.viewers.ViewerCell
import org.eclipse.core.databinding.beans.typed.BeanProperties
import org.eclipse.core.databinding.observable.value.IObservableValue
import org.eclipse.swt.SWT
import java.util.EventObject
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent
import org.eclipse.core.databinding.DataBindingContext

class TypeColumnEditingSupport
    extends ObservableValueEditingSupport<Constraint, AbstractConstraintType, String> {
    val ConstraintsTable constraintsTable
    DataBindingContext dataBindingContext
    List<AbstractConstraintType> supportedConstraintTypes
    List<String> supportedConstraintTypesNames

    new(ConstraintsTable constraintsTable, DataBindingContext dataBindingContext,
        List<AbstractConstraintType> supportedConstraintTypes) {
        super(constraintsTable.viewer, dataBindingContext)
        this.constraintsTable = constraintsTable
        this.dataBindingContext = dataBindingContext
        this.supportedConstraintTypes = supportedConstraintTypes.sortBy[name]
        supportedConstraintTypesNames = supportedConstraintTypes.map[name]

        constraintsTable.viewer.columnViewerEditor.addEditorActivationListener(
            new EditorActivationListener(constraintsTable)
        )
    }

    static class EditorActivationListener extends ColumnViewerEditorActivationListener {
        val ConstraintsTable constraintsTable
        var AbstractConstraintType currentConstraintType

        new(ConstraintsTable constraintsTable) {
            this.constraintsTable = constraintsTable
        }

        override beforeEditorActivated(ColumnViewerEditorActivationEvent event) {
            currentConstraintType = event.constraintType
        }

        private def getConstraintType(EventObject event) {
            val cell = event.source as ViewerCell
            return (cell.element as Constraint).type
        }

        override afterEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
            if (event.constraintType == currentConstraintType)
                return

            // Reset constraint value due to selection of another constraint type
            val cell = event.source as ViewerCell
            val constraint = cell.element as Constraint
            constraint.value = ""
            constraintsTable.viewer.update(constraint, null)
        }

        override afterEditorActivated(ColumnViewerEditorActivationEvent event) {
            // NOOP
        }

        override beforeEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
            // NOOP
        }
    }

    override protected canEdit(Object element) {
        constraintsTable.enabled
    }

    override protected getCellEditor(Object element) {
        return new ComboBoxCellEditor(constraintsTable.viewer.table, supportedConstraintTypesNames,
            SWT.READ_ONLY)
    }

    override protected doCreateCellEditorObservable(CellEditor editor) {
        return WidgetProperties.ccomboSelection.observe(editor.control as CCombo)
    }

    override protected doCreateElementObservable(Constraint constraint, ViewerCell cell) {
        return BeanProperties.value(Constraint, "type", null).observe(constraint)
    }

    override protected createBinding(IObservableValue<String> target,
        IObservableValue<AbstractConstraintType> model) {
        val modelToTargetConverter = new UpdateValueStrategy<AbstractConstraintType, String>()
        modelToTargetConverter.converter = IConverter.create([name])

        val targetToModelConverter = new UpdateValueStrategy<String, AbstractConstraintType>()
        targetToModelConverter.converter = IConverter.create([
            supportedConstraintTypes.get(supportedConstraintTypesNames.indexOf(it))
        ])

        return dataBindingContext.bindValue(target, model, targetToModelConverter,
            modelToTargetConverter)
    }
}