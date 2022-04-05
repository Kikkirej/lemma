package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.arguments_table

import org.eclipse.jface.viewers.TableViewer
import org.eclipse.swt.widgets.Composite
import java.util.Set
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.SWT
import org.eclipse.jface.viewers.TableViewerColumn
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.layout.RowLayout
import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils
import org.eclipse.jface.resource.LocalResourceManager
import org.eclipse.jface.resource.JFaceResources
import org.eclipse.xtend.lib.annotations.Accessors
import java.util.List
import org.eclipse.jface.viewers.IStructuredContentProvider
import org.eclipse.jface.viewers.ColumnLabelProvider
import org.eclipse.jface.fieldassist.FieldDecorationRegistry
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport
import java.util.function.Consumer
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.AbstractArgumentKind
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.ArgumentKindFactory
import org.eclipse.jface.viewers.ViewerCell
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FileArgumentType
import org.eclipse.swt.widgets.TableItem
import org.eclipse.swt.custom.TableEditor
import org.eclipse.jface.viewers.ColumnViewer
import org.eclipse.jface.viewers.ViewerColumn
import org.eclipse.swt.widgets.FileDialog
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType
import java.util.Map
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FolderArgumentType
import org.eclipse.swt.widgets.DirectoryDialog
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.SingleValuedParameterArgumentKind
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Arguments
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration
import java.beans.PropertyChangeListener
import org.eclipse.core.databinding.DataBindingContext

class ArgumentsTable {
    static val DEFAULT_ARGUMENT_KIND = ArgumentKindFactory
        .fromIdentifier(SingleValuedParameterArgumentKind.IDENTIFIER)
    static val RESOURCE_MANAGER = new LocalResourceManager(JFaceResources.getResources())
    static val NEW_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, ArgumentsTable, "add.gif")
    static val REMOVE_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, ArgumentsTable,
        "remove.png")
    static val MOVE_UP_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, ArgumentsTable,
        "moveUp.png")
    static val MOVE_DOWN_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, ArgumentsTable,
        "moveDown.png")
    static val INPUT_SUPPORT_BUTTONS = <Argument, Button>newHashMap

    val Map<Class<? extends AbstractArgumentType>, Consumer<Argument>>
        TYPE_SPECIFIC_ARGUMENT_VALUE_INPUT_SUPPORT

    val Composite parent
    @Accessors(PUBLIC_GETTER)
    val TableViewer viewer
    var Button newButton
    var Button removeButton
    var Button moveUpButton
    var Button moveDownButton
    val List<AbstractArgumentKind> supportedArgumentKinds
    val dataBindingContext = new DataBindingContext()
    @Accessors(PUBLIC_GETTER)
    var enabled = true
    //var List<Argument> currentArguments
    @Accessors(PUBLIC_GETTER)
    var ProcessingConfiguration currentConfiguration
    var Arguments currentArguments

    new(Composite parent, Set<AbstractArgumentKind> supportedArgumentKinds) {
        if (supportedArgumentKinds.nullOrEmpty)
            throw new IllegalArgumentException("Set of supported argument kinds must not be empty")

        this.parent = parent
        this.supportedArgumentKinds = supportedArgumentKinds.toList.sortBy[name]

        viewer = new TableViewer(parent)
        ColumnViewerToolTipSupport.enableFor(viewer)
        viewer.contentProvider = new IStructuredContentProvider() {
            override getElements(Object inputElement) {
                return currentArguments.get
            }
        }
        setViewerLayout()

        createKindColumn()
        createTypeColumn()
        createParameterColumn()
        createValueColumn()

        TYPE_SPECIFIC_ARGUMENT_VALUE_INPUT_SUPPORT = #{
            FileArgumentType -> new Consumer<Argument> {
                override accept(Argument argument) {
                    val fileDialog = new FileDialog(viewer.control.display.activeShell, SWT.OPEN)
                    fileDialog.text = "Select a file"
                    val selectedFile = fileDialog.open
                    if (selectedFile !== null)
                        argument.value = selectedFile
                }
            },
            FolderArgumentType -> new Consumer<Argument> {
                override accept(Argument argument) {
                    val folderDialog = new DirectoryDialog(viewer.control.display.activeShell,
                        SWT.OPEN)
                    folderDialog.text = "Select a folder"
                    val selectedFolder = folderDialog.open
                    if (selectedFolder !== null)
                        argument.value = selectedFolder
                }
            }
        }
        createInputSupportColumn()

        createButtonRow()
    }

    private def setViewerLayout() {
        viewer.table.headerVisible = true
        viewer.table.linesVisible = true
        viewer.table.layoutData = new GridData(GridData.FILL, GridData.FILL, true, true)
    }

    private def createKindColumn() {
        val kindColumn = new TableViewerColumn(viewer, SWT.NONE)
        kindColumn.column.width = 150
        kindColumn.column.text = "Kind"
        kindColumn.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                return (element as Argument).kind.name
            }
        }
        kindColumn.editingSupport = new KindColumnEditingSupport(this, dataBindingContext,
            supportedArgumentKinds)
    }

    private def createTypeColumn() {
        val typeColumn = new TableViewerColumn(viewer, SWT.NONE)
        typeColumn.column.width = 200
        typeColumn.column.text = "Type"
        typeColumn.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                return (element as Argument).type.name
            }
        }
        typeColumn.editingSupport = new TypeColumnEditingSupport(this, dataBindingContext)
    }

    private def createParameterColumn() {
        val parameterColumn = new TableViewerColumn(viewer, SWT.NONE)
        parameterColumn.column.width = 200
        parameterColumn.column.text = "Parameter"
        parameterColumn.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                return (element as Argument).parameter
            }

            override getImage(Object element) {
                return try {
                    (element as Argument).validParameter()
                    null
                } catch (IllegalArgumentException ex) {
                    FieldDecorationRegistry.^default
                        .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).image
                }
            }

            override getToolTipText(Object element) {
                return try {
                    (element as Argument).validParameter()
                    null
                } catch (IllegalArgumentException ex) {
                    ex.message
                }
            }
        }
        parameterColumn.editingSupport = new ParameterColumnEditingSupport(this, dataBindingContext)
    }

    private def createValueColumn() {
        val valueColumn = new TableViewerColumn(viewer, SWT.NONE)
        valueColumn.column.width = 830
        valueColumn.column.text = "Value"
        valueColumn.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                return (element as Argument).value
            }

            override getImage(Object element) {
                return try {
                    val argument = element as Argument
                    argument.type.checkValidValueInUserRepresentation(currentConfiguration,
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
                    argument.type.checkValidValueInUserRepresentation(currentConfiguration,
                        argument.value)
                    null
                } catch (IllegalArgumentException ex) {
                    ex.message
                }
            }
        }
        valueColumn.editingSupport = new ValueColumnEditingSupport(this, dataBindingContext)
    }

    private def createInputSupportColumn() {
        val inputSupportColumn = new TableViewerColumn(viewer, SWT.NONE)
        inputSupportColumn.column.width = 20
        inputSupportColumn.labelProvider = new ColumnLabelProvider() {
            override update(ViewerCell cell) {
                val argument = cell.element as Argument
                INPUT_SUPPORT_BUTTONS.get(argument)?.dispose
                val inputSupportButton = INPUT_SUPPORT_BUTTONS.putInputSupportButtonIfProvided(
                    viewer.control as Composite,
                    argument
                )

                val item = cell.item as TableItem
                val editor = new TableEditor(item.parent)
                editor.grabHorizontal  = true
                editor.grabVertical = true
                editor.setEditor(inputSupportButton, item, cell.columnIndex)
                editor.layout()
            }

            override dispose(ColumnViewer viewer, ViewerColumn column) {
                INPUT_SUPPORT_BUTTONS.values.forEach[dispose]
                super.dispose()
            }
        }
    }

    private def putInputSupportButtonIfProvided(
        Map<Argument, Button> buttonsPerArgument,
        Composite parent,
        Argument argument
    ) {
        val inputSupport = TYPE_SPECIFIC_ARGUMENT_VALUE_INPUT_SUPPORT.get(argument.type.class)
        if (inputSupport === null) {
            return null
        }

        val inputSupportButton = new Button(parent, SWT.NONE)
        inputSupportButton.layoutData = new GridData(SWT.CENTER, SWT.FILL, false, true)
        inputSupportButton.image = FieldDecorationRegistry.^default
            .getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).image
        inputSupportButton.addListener(SWT.Selection, [
            inputSupport.accept(argument)
            viewer.update(argument, null)
        ])
        buttonsPerArgument.put(argument, inputSupportButton)
        return inputSupportButton
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
            if (viewer.input === null) {
                return
            }

            val argument = new Argument(
                DEFAULT_ARGUMENT_KIND,
                DEFAULT_ARGUMENT_KIND.firstSupportedArgumentType,
                "",
                ""
            )
            addArgument(argument)
            // Trigger EditorActivationEvent on all argument columns and thus getCellEditor() in
            // the argument's EditingSupport. As a result, the data bindings between columns' inputs
            // and argument attributes are established, thereby validating the yet empty inputs.
            // Consequently, the bindings receive possible error states, which are recognizable when
            // the user hits the "Apply" button, even immediately after she added the new argument.
            // More precisely, triggering the event doesn't require the user to first enter input
            // fields before the cell is recognized as erroneous by the established bindings.
            // Even more, triggering the event makes editing argument attributes more convenient as
            // the cell editor is active immediately for input receipt.
            viewer.editElement(argument, 1)
        ])
    }

    def containsArgument(Argument argument) {
        /*return currentArguments.exists[
            kind == argument.kind &&
            type == argument.type &&
            parameter == argument.parameter &&
            value == argument.value
        ]*/
        return currentArguments.contains(argument)
    }

    def updateArguments(Class<?> argumentTypeClass) {
        val tableArguments = currentArguments.getManagedArgumentRepresentations(argumentTypeClass)
        tableArguments.forEach[viewer.update(it, null)]
    }

    def addArgument(Argument argument) {
        /*addArgument(argument, currentArguments.size)
        viewer.input = currentArguments*/
        currentArguments.add(argument)
        syncInput()
    }

    private def syncInput() {
        viewer.input = currentArguments.get()
    }

    def addArgument(Argument argument, int index) {
        currentArguments.add(index, argument)
        syncInput()
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
                selectedArguments.forEach[INPUT_SUPPORT_BUTTONS.remove(it)?.dispose()]
                syncInput()
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
                //moveUpInPlace(currentArguments, selectedArgument as Argument)
                //viewer.input = currentArguments
                currentArguments.moveUp(selectedArgument)
                syncInput()
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
                //moveDownInPlace(currentArguments, selectedArgument as Argument)
                //viewer.input = currentArguments
                currentArguments.moveDown(selectedArgument)
                syncInput()
            }
        ])
    }

    /*def setEnabled(boolean enabled) {
        this.enabled = enabled
        newButton.enabled = enabled
        removeButton.enabled = enabled
        moveUpButton.enabled = enabled
        moveDownButton.enabled = enabled
    }*/

    def setInput(ProcessingConfiguration configuration) {
        currentConfiguration = configuration
        currentArguments = new Arguments(currentConfiguration.arguments ?: newArrayList)
        //viewer.input = arguments
        syncInput()
    }

    def void addPropertyChangeListener(PropertyChangeListener listener) {
        currentArguments?.addPropertyChangeListener(listener)
    }

    def void removePropertyChangeListener(PropertyChangeListener listener) {
        currentArguments?.removePropertyChangeListener(listener)
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