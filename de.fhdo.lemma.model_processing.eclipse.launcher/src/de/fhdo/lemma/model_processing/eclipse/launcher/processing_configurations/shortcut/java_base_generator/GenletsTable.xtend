package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator

import org.eclipse.swt.widgets.Composite
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport
import org.eclipse.jface.viewers.IStructuredContentProvider
import org.eclipse.swt.layout.GridData
import org.eclipse.jface.viewers.TableViewerColumn
import org.eclipse.swt.SWT
import org.eclipse.jface.viewers.ColumnLabelProvider
import org.eclipse.jface.fieldassist.FieldDecorationRegistry
import org.eclipse.jface.viewers.ViewerCell
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.FileDialog
import org.eclipse.swt.widgets.TableItem
import org.eclipse.swt.custom.TableEditor
import org.eclipse.jface.viewers.ColumnViewer
import org.eclipse.jface.viewers.ViewerColumn
import org.eclipse.swt.layout.RowLayout
import org.eclipse.jface.resource.LocalResourceManager
import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils
import org.eclipse.jface.resource.JFaceResources
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport
import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.jface.viewers.CellEditor
import org.eclipse.jface.viewers.TextCellEditor
import org.eclipse.jface.databinding.swt.typed.WidgetProperties
import org.eclipse.core.databinding.beans.typed.BeanProperties
import org.eclipse.core.databinding.observable.value.IObservableValue
import org.eclipse.core.databinding.UpdateValueStrategy
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FileArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Arguments
import java.beans.PropertyChangeListener
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration

class GenletsTable {
    static val RESOURCE_MANAGER = new LocalResourceManager(JFaceResources.getResources())
    static val NEW_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, GenletsTable, "add.gif")
    static val REMOVE_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, GenletsTable, "remove.png")
    static val MOVE_UP_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, GenletsTable,
        "moveUp.png")
    static val MOVE_DOWN_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, GenletsTable,
        "moveDown.png")
    static val PATH_SELECTION_BUTTONS = <Argument, Button>newHashMap

    val Composite parent
    val dataBindingContext = new DataBindingContext()
    val TableViewer viewer
    var Button newButton
    var Button removeButton
    var Button moveUpButton
    var Button moveDownButton
    val Arguments currentArguments = new Arguments(newArrayList)
    val ProcessingConfiguration processingConfiguration

    new(Composite parent, ProcessingConfiguration processingConfiguration) {
        this.parent = parent
        this.processingConfiguration = processingConfiguration

        viewer = new TableViewer(parent)
        ColumnViewerToolTipSupport.enableFor(viewer)
        viewer.contentProvider = new IStructuredContentProvider() {
            override getElements(Object inputElement) {
                return currentArguments.get
            }
        }
        setViewerLayout()

        createPathColumn()
        createPathSelectionColumn()
        createButtonRow()
    }

    private def setViewerLayout() {
        viewer.table.headerVisible = true
        viewer.table.linesVisible = true
        viewer.table.layoutData = new GridData(GridData.FILL, GridData.FILL, true, true)
    }

    private def createPathColumn() {
        val pathColumn = new TableViewerColumn(viewer, SWT.NONE)
        pathColumn.column.width = 730
        pathColumn.column.text = "Genlet"
        pathColumn.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                return (element as Argument).value
            }

            override getImage(Object element) {
                return try {
                    val argument = element as Argument
                    (argument.type as FileArgumentType).checkValidValue(processingConfiguration,
                        argument.value)
                    null
                } catch (IllegalArgumentException ex) {
                    FieldDecorationRegistry.^default
                        .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).image
                }
            }

            override getToolTipText(Object element) {
                return try {
                    val argument = element as Argument
                    (argument.type as FileArgumentType).checkValidValue(processingConfiguration,
                        argument.value)
                    null
                } catch (IllegalArgumentException ex) {
                    ex.message
                }
            }
        }
        pathColumn.editingSupport = new PathColumnEditingSupport(this, dataBindingContext)
    }

    private static class PathColumnEditingSupport
        extends ObservableValueEditingSupport<Argument, String, String> {
        val GenletsTable genletsTable
        DataBindingContext dataBindingContext

        new(GenletsTable genletsTable, DataBindingContext dataBindingContext) {
            super(genletsTable.viewer, dataBindingContext)
            this.genletsTable = genletsTable
            this.dataBindingContext = dataBindingContext
        }

        override protected getCellEditor(Object element) {
            new TextCellEditor(genletsTable.viewer.table)
        }

        override protected doCreateCellEditorObservable(CellEditor cellEditor) {
            WidgetProperties.text(SWT.Modify).observe(cellEditor.control)
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

    private def createPathSelectionColumn() {
        val pathSelectionColumn = new TableViewerColumn(viewer, SWT.NONE)
        pathSelectionColumn.column.width = 20
        pathSelectionColumn.labelProvider = new ColumnLabelProvider() {
            override update(ViewerCell cell) {
                val argument = cell.element as Argument
                PATH_SELECTION_BUTTONS.get(argument)?.dispose

                val pathSelectionButton = new Button(viewer.control as Composite, SWT.NONE)
                pathSelectionButton.layoutData = new GridData(SWT.CENTER, SWT.FILL, false, true)
                pathSelectionButton.image = FieldDecorationRegistry.^default
                    .getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).image
                pathSelectionButton.addListener(SWT.Selection, [
                    val selectedPath = pathSelectionDialog()
                    if (selectedPath !== null) {
                        argument.value = selectedPath
                        viewer.update(argument, null)
                    }
                ])
                PATH_SELECTION_BUTTONS.put(argument, pathSelectionButton)

                val item = cell.item as TableItem
                val editor = new TableEditor(item.parent)
                editor.grabHorizontal  = true
                editor.grabVertical = true
                editor.setEditor(pathSelectionButton, item, cell.columnIndex)
                editor.layout()
            }

            override dispose(ColumnViewer viewer, ViewerColumn column) {
                PATH_SELECTION_BUTTONS.values.forEach[dispose]
                super.dispose()
            }
        }
    }

    private def pathSelectionDialog() {
        val dialog = new FileDialog(parent.shell, SWT.OPEN)
        dialog.text = "Select a Genlet"
        dialog.filterExtensions = #["*.jar"]
        return dialog.open
    }

    private def createButtonRow() {
        val buttonRow = new Composite(parent, SWT.NONE)
        buttonRow.layout = new RowLayout
        createNewButton(buttonRow)
        createRemoveButton(buttonRow)
        createMoveUpButton(buttonRow)
        createMoveDownButton(buttonRow)
    }

    private def createNewButton(Composite parent) {
        newButton = new Button(parent, SWT.PUSH)
        newButton.image = NEW_IMAGE
        newButton.addListener(SWT.Selection, [
            if (viewer.input === null)
                viewer.input = <Argument>newArrayList

            val newArgument = Argument.newArgument
                .singleValued
                .file
                .parameter(JavaBaseGeneratorParameters.GENLET_PARAMETER)
                .value("")
            currentArguments.add(newArgument)
            viewer.input = currentArguments.get

            // Open Genlet path selection dialog for convenience
            val selectedPath = pathSelectionDialog()
            if (selectedPath !== null) {
                newArgument.value = selectedPath
                viewer.update(newArgument, null)
            }
        ])
    }

    private def createRemoveButton(Composite parent) {
        removeButton = new Button(parent, SWT.PUSH)
        removeButton.image = REMOVE_IMAGE
        removeButton.addListener(SWT.Selection, [
            if (viewer.input === null) {
                return
            }

            val selectedArguments = rowSelectionOrError(viewer)
            if (selectedArguments !== null) {
                currentArguments.removeAll(selectedArguments.map[it as Argument].toList)
                selectedArguments.forEach[PATH_SELECTION_BUTTONS.remove(it)?.dispose()]
                viewer.input = currentArguments.get
            }
        ])
    }

    private def createMoveUpButton(Composite parent) {
        moveUpButton = new Button(parent, SWT.PUSH)
        moveUpButton.image = MOVE_UP_IMAGE
        moveUpButton.addListener(SWT.Selection, [
            if (viewer.input === null) {
                return
            }

            val selectedArgument = singleRowSelectionOrError(viewer)?.firstElement as Argument
            if (selectedArgument !== null) {
                currentArguments.moveUp(selectedArgument)
                viewer.input = currentArguments.get
            }
        ])
    }

    private def createMoveDownButton(Composite parent) {
        moveDownButton = new Button(parent, SWT.PUSH)
        moveDownButton.image = MOVE_DOWN_IMAGE
        moveDownButton.addListener(SWT.Selection, [
            if (viewer.input === null) {
                return
            }

            val selectedArgument = singleRowSelectionOrError(viewer)?.firstElement as Argument
            if (selectedArgument !== null) {
                currentArguments.moveDown(selectedArgument)
                viewer.input = currentArguments.get
            }
        ])
    }

    def getGenletArguments() {
        return currentArguments.get
    }

    def void addPropertyChangeListener(PropertyChangeListener listener) {
        currentArguments.addPropertyChangeListener(listener)
    }

    def void removePropertyChangeListener(PropertyChangeListener listener) {
        currentArguments.removePropertyChangeListener(listener)
    }

    def dispose() {
        dataBindingContext.dispose
        newButton.dispose
        removeButton.dispose
        moveUpButton.dispose
        moveDownButton.dispose
        viewer.control.dispose
        parent.dispose
    }
}