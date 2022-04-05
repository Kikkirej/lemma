package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.arguments_table

import org.eclipse.jface.databinding.swt.typed.WidgetProperties
import org.eclipse.jface.viewers.TextCellEditor
import org.eclipse.swt.SWT

import org.eclipse.jface.viewers.ComboBoxCellEditor
import org.eclipse.swt.custom.CCombo
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport
import org.eclipse.jface.viewers.CellEditor
import org.eclipse.jface.viewers.ViewerCell
import org.eclipse.core.databinding.observable.value.IObservableValue
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent
import org.eclipse.core.databinding.beans.typed.BeanProperties
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FileArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FolderArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.RawStringArgumentType
import org.eclipse.core.databinding.UpdateValueStrategy
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.StringPairArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportWithAliasArgumentType
import org.eclipse.jface.fieldassist.ControlDecoration
import org.eclipse.jface.fieldassist.FieldDecorationRegistry
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentTypeWithEnumValueSelection
import org.eclipse.core.databinding.DataBindingContext

class ValueColumnEditingSupport
    extends ObservableValueEditingSupport<Argument, String, String> {
    val ArgumentsTable argumentsTable
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
        val argument = element as Argument
        val argumentType = argument.type
        return switch (argumentType) {
            FileArgumentType,
            FolderArgumentType,
            StringPairArgumentType:
                new TextCellEditor(argumentsTable.viewer.table)

            AbstractArgumentTypeWithEnumValueSelection<?>:
                new ComboBoxCellEditor(argumentsTable.viewer.table, argumentType.stringValuesSorted,
                    SWT.DROP_DOWN.bitwiseOr(SWT.READ_ONLY))

            IntermediateModelOfImportWithAliasArgumentType: {
                try {
                    val configuration = argumentsTable.currentConfiguration
                    val importAliases = argumentType
                        .parseTransformableImportedModelsOfSourceModelFile(configuration)
                        .map[it.value.alias]
                        .sort
                    new ComboBoxCellEditor(argumentsTable.viewer.table, importAliases,
                        SWT.DROP_DOWN.bitwiseOr(SWT.READ_ONLY))
                } catch (IllegalArgumentException ex) {
                    val editor = new ComboBoxCellEditor(argumentsTable.viewer.table, #[],
                        SWT.DROP_DOWN.bitwiseOr(SWT.READ_ONLY))
                    val decoration = new ControlDecoration(editor.control,
                        SWT.TOP.bitwiseOr(SWT.LEFT))
                    decoration.descriptionText = ex.message
                    decoration.image = FieldDecorationRegistry
                        .^default
                        .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
                        .image
                    decoration.show()
                    editor
                }
            }

            RawStringArgumentType:
                null
        }
    }

    override protected doCreateCellEditorObservable(CellEditor editor) {
        return switch (editor) {
            TextCellEditor: WidgetProperties.text(SWT.Modify).observe(editor.control)
            ComboBoxCellEditor: WidgetProperties.ccomboSelection.observe(editor.control as CCombo)
        }
    }

    override protected doCreateElementObservable(Argument argument, ViewerCell cell) {
        return BeanProperties.value(Argument, "value", null).observe(argument)
    }

    override protected createBinding(IObservableValue<String> target,
        IObservableValue<String> model) {
        return dataBindingContext.bindValue(target, model,
            new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), null)
    }
}