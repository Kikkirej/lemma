package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.constraints_table

import org.eclipse.jface.viewers.TableViewer
import org.eclipse.swt.widgets.Composite
import java.util.Set
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.SWT
import org.eclipse.jface.viewers.TableViewerColumn
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.layout.RowLayout
import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils
import org.eclipse.jface.resource.LocalResourceManager
import org.eclipse.jface.resource.JFaceResources
import org.eclipse.xtend.lib.annotations.Accessors
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.Constraint
import java.util.List
import org.eclipse.jface.viewers.IStructuredContentProvider
import org.eclipse.jface.viewers.ColumnLabelProvider
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.ConstraintTypeFactory
import org.eclipse.jface.fieldassist.FieldDecorationRegistry
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport
import org.eclipse.core.databinding.Binding
import java.util.function.Consumer
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.dialogs.MessageDialog
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.AbstractConstraintType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.ConstraintTypeIdentifier
import org.eclipse.core.databinding.DataBindingContext

class ConstraintsTable {
    static val DEFAULT_CONSTRAINT_TYPE = ConstraintTypeFactory
        .fromIdentifier(ConstraintTypeIdentifier.MODEL_KIND)
    static val RESOURCE_MANAGER = new LocalResourceManager(JFaceResources.getResources())
    static val NEW_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, ConstraintsTable, "add.gif")
    static val REMOVE_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, ConstraintsTable,
        "remove.png")

    val Composite parent
    @Accessors(PUBLIC_GETTER)
    val TableViewer viewer
    var Button newButton
    var Button removeButton
    val List<AbstractConstraintType> supportedConstraintTypes
    val DataBindingContext dataBindingContext = new DataBindingContext()
    @Accessors(PUBLIC_GETTER)
    var enabled = true
    var List<Constraint> currentConstraints

    new(Composite parent, Set<AbstractConstraintType> supportedConstraintTypes,
        Consumer<Binding> newBindingCallback, Consumer<Binding> removedBindingCallback) {
        if (supportedConstraintTypes.nullOrEmpty)
            throw new IllegalArgumentException("Set of supported constraint types must not be " +
                "empty")

        this.supportedConstraintTypes = supportedConstraintTypes.toList.sortBy[name]
        this.parent = new Composite(parent, SWT.NONE)
        setParentLayout()

        viewer = new TableViewer(this.parent)
        ColumnViewerToolTipSupport.enableFor(viewer)
        viewer.contentProvider = new IStructuredContentProvider() {
            override getElements(Object inputElement) {
                return (inputElement as List<Constraint>).toArray
            }
        }
        setViewerLayout()
        createTypeColumn()
        createValueColumn()

        createButtonRow()
    }

    private def setParentLayout() {
        parent.layout = new GridLayout(1, false)

        val parentData = new GridData()
        parentData.grabExcessHorizontalSpace = true
        parentData.grabExcessVerticalSpace = true
        parentData.horizontalAlignment = GridData.FILL
        parentData.verticalAlignment = GridData.FILL
        parent.layoutData = parentData
    }

    private def setViewerLayout() {
        viewer.table.headerVisible = true
        viewer.table.linesVisible = true

        val layoutData = new GridData()
        layoutData.grabExcessHorizontalSpace = true
        layoutData.horizontalAlignment= GridData.FILL
        layoutData.grabExcessVerticalSpace = true
        layoutData.verticalAlignment = GridData.FILL
        viewer.table.layoutData = layoutData
    }

    private def createTypeColumn() {
        val typeColumn = new TableViewerColumn(viewer, SWT.NONE)
        typeColumn.column.width = 150
        typeColumn.column.text = "Type"
        typeColumn.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                return (element as Constraint).type.name
            }
        }
        typeColumn.editingSupport = new TypeColumnEditingSupport(this, dataBindingContext,
            supportedConstraintTypes)
    }

    private def createValueColumn() {
        val valueColumn = new TableViewerColumn(viewer, SWT.NONE)
        valueColumn.column.width = 300
        valueColumn.column.text = "Value"
        valueColumn.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                return (element as Constraint).value
            }

            override getImage(Object element) {
                return try {
                    val constraint = element as Constraint
                    constraint.type.checkValidValueInUserRepresentation(constraint.value)
                    null
                } catch (IllegalArgumentException ex) {
                    FieldDecorationRegistry.^default
                        .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).image
                }
            }

            override getToolTipText(Object element) {
                return try {
                    val constraint = element as Constraint
                    constraint.type.checkValidValueInUserRepresentation(constraint.value)
                    null
                } catch (IllegalArgumentException ex) {
                    ex.message
                }
            }
        }
        valueColumn.editingSupport = new ValueColumnEditingSupport(this, dataBindingContext)
    }

    private def createButtonRow() {
        val buttonRow = new Composite(parent, SWT.NONE)
        buttonRow.layout = new RowLayout
        createNewButton(buttonRow)
        createRemoveButton(buttonRow)
    }

    private def createNewButton(Composite parent) {
        newButton = new Button(parent, SWT.PUSH)
        newButton.image = NEW_IMAGE
        newButton.addListener(SWT.Selection, [
            if (viewer.input === null) {
                return
            }

            val constraint = new Constraint
            constraint.type = DEFAULT_CONSTRAINT_TYPE
            constraint.value = ""
            currentConstraints.add(constraint)
            viewer.input = currentConstraints
            viewer.refresh()
            // Trigger EditorActivationEvent on constraint value column and thus getCellEditor() in
            // the column's EditingSupport. As a result, the data binding between column input and
            // constraint value is established, thereby validating the yet empty input.
            // Consequently, the binding receives an error status, which is recognizable when the
            // user hits the "Apply" button, even immediately after she added the new constraint.
            // More precisely, triggering the event doesn't require the user to first enter the
            // input field before the cell is recognized as erroneous by the established binding.
            // Even more, triggering the event makes editing the constraint value more convenient as
            // the cell editor is active immediately for input receipt.
            viewer.editElement(constraint, 1)
        ])
    }

    private def createRemoveButton(Composite parent) {
        removeButton = new Button(parent, SWT.PUSH)
        removeButton.image = REMOVE_IMAGE
        removeButton.addListener(SWT.Selection, [
            if (viewer.input === null) {
                return
            }

            val selection = viewer.selection as IStructuredSelection
            if (selection.empty) {
                MessageDialog.openError(viewer.control.shell, "No row selected", "Please select " +
                    "a constraint row for removal")
                return
            } else if (selection.length > 1) {
                MessageDialog.openError(viewer.control.shell, "Too many rows selected", "Please " +
                    "select only a single constraint row for removal")
                return
            }

            currentConstraints.remove(selection.firstElement)
            viewer.input = currentConstraints
        ])
    }

    def setEnabled(boolean enabled) {
        this.enabled = enabled
        newButton.enabled = enabled
        removeButton.enabled = enabled
    }

    def setInput(List<Constraint> constraints) {
        currentConstraints = constraints
        viewer.input = constraints
    }
}