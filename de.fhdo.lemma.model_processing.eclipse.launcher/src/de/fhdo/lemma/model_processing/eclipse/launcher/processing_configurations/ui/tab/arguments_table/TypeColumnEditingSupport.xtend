package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.arguments_table

import org.eclipse.jface.viewers.ComboBoxCellEditor
import org.eclipse.jface.databinding.swt.typed.WidgetProperties
import org.eclipse.swt.custom.CCombo
import org.eclipse.core.databinding.UpdateValueStrategy
import org.eclipse.core.databinding.conversion.IConverter
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport
import org.eclipse.jface.viewers.CellEditor
import org.eclipse.jface.viewers.ViewerCell
import org.eclipse.core.databinding.beans.typed.BeanProperties
import org.eclipse.core.databinding.observable.value.IObservableValue
import org.eclipse.swt.SWT
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType
import java.util.List
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent
import java.util.EventObject
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentTypeWithEnumValueSelection
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportWithAliasArgumentType
import org.eclipse.core.databinding.DataBindingContext

class TypeColumnEditingSupport
    extends ObservableValueEditingSupport<Argument, AbstractArgumentType, String> {
    val ArgumentsTable argumentsTable
    val DataBindingContext dataBindingContext
    var List<AbstractArgumentType> supportedArgumentTypes
    var List<String> supportedArgumentTypesNames

    new(ArgumentsTable argumentsTable, DataBindingContext dataBindingContext) {
        super(argumentsTable.viewer, dataBindingContext)
        this.argumentsTable = argumentsTable
        this.dataBindingContext = dataBindingContext

        argumentsTable.viewer.columnViewerEditor.addEditorActivationListener(
            new EditorActivationListener(argumentsTable)
        )
    }

    static class EditorActivationListener extends ColumnViewerEditorActivationListener {
        val ArgumentsTable argumentsTable
        var AbstractArgumentType currentArgumentType

        new(ArgumentsTable argumentsTable) {
            this.argumentsTable = argumentsTable
        }

        override beforeEditorActivated(ColumnViewerEditorActivationEvent event) {
            currentArgumentType = event.argumentType
        }

        private def getArgumentType(EventObject event) {
            val cell = event.source as ViewerCell
            return (cell.element as Argument).type
        }

        override afterEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
            if (event.argumentType == currentArgumentType)
                return

            // Reset argument parameter and value due to selection of another argument type
            val cell = event.source as ViewerCell
            val argument = cell.element as Argument
            argument.parameter = ""

            val argumentType = argument.type
            switch(argumentType) {
                AbstractArgumentTypeWithEnumValueSelection<?>: {
                    val items = argumentType.stringValuesSorted
                    if (!items.empty)
                        argument.value = items.get(0)
                }

                IntermediateModelOfImportWithAliasArgumentType: {
                    try {
                        val configuration = argumentsTable.currentConfiguration
                        val importAliases = argumentType
                            .parseTransformableImportedModelsOfSourceModelFile(configuration)
                            .map[it.value.alias]
                            .sort
                        if (!importAliases.empty)
                            argument.value = importAliases.get(0)
                    } catch (IllegalArgumentException ex) {
                        argument.value = ""
                    }
                }

                default:
                    argument.value = ""
            }

            argumentsTable.viewer.update(argument, null)
        }

        override afterEditorActivated(ColumnViewerEditorActivationEvent event) {
            // NOOP
        }

        override beforeEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
            // NOOP
        }
    }

    override protected canEdit(Object element) {
        argumentsTable.enabled
    }

    override protected getCellEditor(Object element) {
        supportedArgumentTypes = (element as Argument).kind.supportedArgumentTypes.sortBy[name]
        supportedArgumentTypesNames = supportedArgumentTypes.map[name]
        return new ComboBoxCellEditor(argumentsTable.viewer.table, supportedArgumentTypesNames,
            SWT.DROP_DOWN.bitwiseOr(SWT.READ_ONLY))
    }

    override protected doCreateCellEditorObservable(CellEditor editor) {
        return WidgetProperties.ccomboSelection.observe(editor.control as CCombo)
    }

    override protected doCreateElementObservable(Argument argument, ViewerCell cell) {
        return BeanProperties.value(Argument, "type", null).observe(argument)
    }

    override protected createBinding(IObservableValue<String> target,
        IObservableValue<AbstractArgumentType> model) {
        val modelToTargetConverter = new UpdateValueStrategy<AbstractArgumentType, String>()
        modelToTargetConverter.converter = IConverter.create([name])

        val targetToModelConverter = new UpdateValueStrategy<String, AbstractArgumentType>()
        targetToModelConverter.converter = IConverter.create([
            supportedArgumentTypes.get(supportedArgumentTypesNames.indexOf(it))
        ])

        return dataBindingContext.bindValue(target, model, targetToModelConverter,
            modelToTargetConverter)
    }
}