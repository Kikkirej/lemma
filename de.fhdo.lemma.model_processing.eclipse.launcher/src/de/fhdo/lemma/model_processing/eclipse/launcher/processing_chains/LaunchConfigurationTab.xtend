package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains

import org.eclipse.debug.ui.AbstractLaunchConfigurationTab
import org.eclipse.debug.core.ILaunchConfiguration
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.jface.fieldassist.FieldDecorationRegistry
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import org.eclipse.core.databinding.DataBindingContext
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.arguments_table.ArgumentsTable
import static de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants.*
import org.eclipse.swt.widgets.Button
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeEvent
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.viewers.IStructuredContentProvider
import org.eclipse.jface.viewers.TableViewerColumn
import org.eclipse.jface.viewers.ColumnLabelProvider
import org.eclipse.jface.resource.LocalResourceManager
import org.eclipse.jface.resource.JFaceResources
import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils
import org.eclipse.swt.layout.RowLayout

class LaunchConfigurationTab extends AbstractLaunchConfigurationTab
    implements PropertyChangeListener {
    static val RESOURCE_MANAGER = new LocalResourceManager(JFaceResources.getResources())
    static val NEW_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, LaunchConfigurationTab,
        "add.gif")
    static val REMOVE_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, ArgumentsTable,
        "remove.png")
    static val MOVE_UP_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, ArgumentsTable,
        "moveUp.png")
    static val MOVE_DOWN_IMAGE = LemmaUiUtils.createImage(RESOURCE_MANAGER, ArgumentsTable,
        "moveDown.png")

    val dataBindingContext = new DataBindingContext()
    val availableLaunchConfigurations = getProcessingConfigurationLaunchConfigurations()
    var Composite mainComposite

    var ProcessingChain originalChain
    var ProcessingChain currentChain
    var TableViewer tableViewer
    var Button newButton
    var Button removeButton
    var Button moveUpButton
    var Button moveDownButton

    override createControl(Composite parent) {
        mainComposite = new Composite(parent, SWT.NULL)
        mainComposite.layoutData = new GridData(SWT.FILL, SWT.FILL, true, true)
        mainComposite.layout = new GridLayout(1, false)
        setControl(mainComposite)
        createTable(mainComposite)
        createButtonRow(mainComposite)
    }

    private def createTable(Composite parent) {
        tableViewer = new TableViewer(parent)
        tableViewer.contentProvider = new IStructuredContentProvider() {
            override getElements(Object inputElement) {
                return currentChain.entries
            }
        }
        tableViewer.table.headerVisible = true
        tableViewer.table.linesVisible = true
        tableViewer.table.layoutData = new GridData(GridData.FILL, GridData.FILL, true, true)
        createLaunchConfigurationColumn()
        createExitValueComparatorColumn()
        createExitValueColumn()
    }

    private def createLaunchConfigurationColumn() {
        val column = new TableViewerColumn(tableViewer, SWT.NONE)
        column.column.width = 500
        column.column.text = "LEMMA Model Processing Configuration"
        column.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                return (element as ProcessingChainEntry).launchConfigurationName
            }

            override getImage(Object element) {
                return try {
                    (element as ProcessingChainEntry)
                        .validLaunchConfigurationName(availableLaunchConfigurations)
                    null
                } catch (IllegalArgumentException ex) {
                    FieldDecorationRegistry.^default
                        .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).image
                }
            }

            override getToolTipText(Object element) {
                return try {
                    (element as ProcessingChainEntry)
                        .validLaunchConfigurationName(availableLaunchConfigurations)
                    null
                } catch (IllegalArgumentException ex) {
                    ex.message
                }
            }
        }
        column.editingSupport = new LaunchConfigurationNameColumnEditingSupport(
            tableViewer,
            dataBindingContext,
            availableLaunchConfigurations.keySet.sortBy[it]
        )
    }

    private def createExitValueComparatorColumn() {
        val column = new TableViewerColumn(tableViewer, SWT.NONE)
        column.column.width = 210
        column.column.text = "Previous Exit Value Comparator"
        column.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                val comparator = (element as ProcessingChainEntry).previousExitValueComparator
                return PreviousExitValueComparator.getUserRepresentation(comparator)
            }

            override getImage(Object element) {
                return try {
                    (element as ProcessingChainEntry).validPreviousExitValueComparator
                    null
                } catch (IllegalArgumentException ex) {
                    FieldDecorationRegistry.^default
                        .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).image
                }
            }

            override getToolTipText(Object element) {
                return try {
                    (element as ProcessingChainEntry).validPreviousExitValueComparator
                    null
                } catch (IllegalArgumentException ex) {
                    ex.message
                }
            }
        }
        column.editingSupport = new PreviousExitValueComparatorColumnEditingSupport(tableViewer,
            dataBindingContext)
    }

    private def createExitValueColumn() {
        val column = new TableViewerColumn(tableViewer, SWT.NONE)
        column.column.width = 250
        column.column.text = "Expected Exit Value from the Execution of the Previous Configuration"
        column.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                return (element as ProcessingChainEntry).previousExitValue?.toString ?: ""
            }

            override getImage(Object element) {
                return try {
                    (element as ProcessingChainEntry).validPreviousExitValue
                    null
                } catch (IllegalArgumentException ex) {
                    FieldDecorationRegistry.^default
                        .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).image
                }
            }

            override getToolTipText(Object element) {
                return try {
                    (element as ProcessingChainEntry).validPreviousExitValue
                    null
                } catch (IllegalArgumentException ex) {
                    ex.message
                }
            }
        }
        column.editingSupport = new PreviousExitValueColumnEditingSupport(tableViewer,
            dataBindingContext)
    }

    private def createButtonRow(Composite parent) {
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
            if (tableViewer.input === null) {
                return
            }

            val entry = new ProcessingChainEntry(
                "",
                currentChain,
                PreviousExitValueComparator.Comparator.EQUAL,
                0
            )
            addEntry(entry)
            // Trigger EditorActivationEvent on all columns and thus getCellEditor() in the
            // argument's EditingSupport. As a result, the data bindings between columns' inputs and
            // argument attributes are established, thereby validating the yet empty inputs.
            // Consequently, the bindings receive possible error states, which are recognizable when
            // the user hits the "Apply" button, even immediately after she added the new row. More
            // precisely, triggering the event doesn't require the user to first enter input fields
            // before the cell is recognized as erroneous by the established bindings. Even more,
            // triggering the event makes editing row columns more convenient as the cell editor is
            // active immediately for input receipt.
            tableViewer.editElement(entry, 0)
        ])
    }

    private def addEntry(ProcessingChainEntry entry) {
        currentChain.addEntry(entry)
        syncInput()
    }

    private def clearIrrelevantFieldsIfFirstEntryInChain(ProcessingChainEntry entry) {
        if (currentChain.entries.indexOf(entry) == 0) {
            // For the first entry in the chain there is no previous entry. Thus, clear fields
            // pointing to previous entries.
            entry.previousExitValueComparator = null
            entry.previousExitValue = null
        }
    }

    private def syncInput() {
        if (!currentChain.entries.empty)
            currentChain.entries.get(0).clearIrrelevantFieldsIfFirstEntryInChain
        tableViewer.input = currentChain.entries
    }

    private def createRemoveButton(Composite parent) {
        removeButton = new Button(parent, SWT.PUSH)
        removeButton.image = REMOVE_IMAGE
        removeButton.addListener(SWT.Selection, [
            if (tableViewer.input === null) {
                return
            }

            val selectedEntries = rowSelectionOrError(tableViewer)
            if (selectedEntries !== null) {
                currentChain.removeAll(selectedEntries.map[it as ProcessingChainEntry].toList)
                syncInput()
            }
        ])
    }

    private def createMoveUpButton(Composite parent) {
        moveUpButton = new Button(parent, SWT.PUSH)
        moveUpButton.image = MOVE_UP_IMAGE
        moveUpButton.addListener(SWT.Selection, [
            if (tableViewer.input === null) {
                return
            }

            val selectedEntry = singleRowSelectionOrError(tableViewer)?.firstElement
            if (selectedEntry !== null) {
                currentChain.moveUp(selectedEntry as ProcessingChainEntry)
                syncInput()
            }
        ])
    }

    private def createMoveDownButton(Composite parent) {
        moveDownButton = new Button(parent, SWT.PUSH)
        moveDownButton.image = MOVE_DOWN_IMAGE
        moveDownButton.addListener(SWT.Selection, [
            if (tableViewer.input === null) {
                return
            }

            val selectedEntry = singleRowSelectionOrError(tableViewer)?.firstElement
            if (selectedEntry !== null) {
                //moveDownInPlace(currentArguments, selectedArgument as Argument)
                //viewer.input = currentArguments
                currentChain.moveDown(selectedEntry as ProcessingChainEntry)
                syncInput()
            }
        ])
    }

    override initializeFrom(ILaunchConfiguration launchConfiguration) {
        val chain = ProcessingChain.deserializeFrom(launchConfiguration) ?: new ProcessingChain
        setCurrentChain(chain)
        syncInput()
    }

    private def setCurrentChain(ProcessingChain chain) {
        removeCurrentConfigurationBindings()
        currentChain?.removePropertyChangeListener(this)

        originalChain = chain
        currentChain = chain.clone() as ProcessingChain

        //originalChain.convertToUserRepresentation()
        //currentChain.convertToUserRepresentation()

        currentChain.addPropertyChangeListener(this)
    }

    private def removeCurrentConfigurationBindings() {
        dataBindingContext.bindings.forEach[
            dispose
            dataBindingContext.removeBinding(it)
        ]
    }

    override isValid(ILaunchConfiguration configuration) {
        return try {
            currentChain.validate(availableLaunchConfigurations)
            true
        } catch (IllegalArgumentException ex) {
            false
        }
    }

    override getErrorMessage() {
        return try {
            currentChain.validate(availableLaunchConfigurations, true)
            null
        } catch (IllegalArgumentException ex) {
            ex.message
        }
    }

    override isDirty() {
        return originalChain != currentChain
    }

    override performApply(ILaunchConfigurationWorkingCopy launchConfiguration) {
        ProcessingChain.setProcessingChainAsAttribute(launchConfiguration, currentChain)
    }

    override propertyChange(PropertyChangeEvent event) {
        updateLaunchConfigurationDialog()
    }

    override getName() {
        return "Chained LEMMA Model Processing Configurations"
    }

    override getImage() {
        return COMMON_LAUNCH_CONFIGURATION_TAB_IMAGE
    }

    override setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        // NOOP
    }

    override dispose() {
        dataBindingContext.dispose
        super.dispose
    }
}