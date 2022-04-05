package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.shortcut

import org.eclipse.swt.widgets.Shell
import java.util.List
import org.eclipse.debug.core.ILaunchConfiguration
import org.eclipse.swt.widgets.Composite
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.viewers.IStructuredContentProvider
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.SWT
import org.eclipse.jface.viewers.TableViewerColumn
import org.eclipse.jface.viewers.ColumnLabelProvider
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.jface.window.Window
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.widgets.Group
import org.eclipse.jface.resource.LocalResourceManager
import org.eclipse.jface.resource.JFaceResources
import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils
import org.eclipse.swt.widgets.Button
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.swt.layout.RowLayout

import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import org.eclipse.jface.dialogs.IDialogConstants
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Text
import org.eclipse.jface.fieldassist.ControlDecoration
import org.eclipse.jface.dialogs.TitleAreaDialog
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeEvent
import java.util.Map
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.PreviousExitValueComparator
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.ProcessingChainEntry
import org.eclipse.core.databinding.DataBindingContext
import static de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants.*

class LaunchConfigurationChainCreationDialog extends TitleAreaDialog
    implements PropertyChangeListener {
    static val RESOURCE_MANAGER = new LocalResourceManager(JFaceResources.getResources())
    static val MOVE_DOWN_IMAGE = LemmaUiUtils.createImage(
        RESOURCE_MANAGER,
        LaunchConfigurationChainCreationDialog,
        "moveDown.png"
    )
    static val MOVE_RIGHT_IMAGE = LemmaUiUtils.createImage(
        RESOURCE_MANAGER,
        LaunchConfigurationChainCreationDialog,
        "moveRight.png"
    )
    static val MOVE_UP_IMAGE = LemmaUiUtils.createImage(
        RESOURCE_MANAGER,
        LaunchConfigurationChainCreationDialog,
        "moveUp.png"
    )
    static val REMOVE_IMAGE = LemmaUiUtils.createImage(
        RESOURCE_MANAGER,
        LaunchConfigurationChainCreationDialog,
        "remove.png"
    )

    val Map<String, ILaunchConfiguration> availableLaunchConfigurations
    val ProcessingChainWithLaunchConfigurationName chainTemplate

    var TableViewer launchConfigurationsTable
    var TableViewer chainTable
    var Button storeButton
    var Button runButton

    val dataBindingContext = new DataBindingContext()
    val currentDecorations = <ControlDecoration>newArrayList
    val (ProcessingChainWithLaunchConfigurationName, Map<String, ILaunchConfiguration>)=>void
        runCallback
    val (ProcessingChainWithLaunchConfigurationName, Map<String, ILaunchConfiguration>)=>void
        storeCallback

    new(
        Shell parentShell,
        Map<String, ILaunchConfiguration> availableLaunchConfigurations,
        ProcessingChainWithLaunchConfigurationName chainTemplate,
        (ProcessingChainWithLaunchConfigurationName, Map<String, ILaunchConfiguration>)=>void
            runCallback,
        (ProcessingChainWithLaunchConfigurationName, Map<String, ILaunchConfiguration>)=>void
            storeCallback
    ) {
        super(parentShell)
        helpAvailable = false
        this.availableLaunchConfigurations = availableLaunchConfigurations
        this.chainTemplate = chainTemplate
        this.runCallback = runCallback
        this.storeCallback = storeCallback
        chainTemplate.launchConfigurationName = LAUNCH_MANAGER
            .generateLaunchConfigurationName("LEMMA_Model_Processing_Chain")
        chainTemplate.addPropertyChangeListener(this)
    }

    override create() {
        super.create()
        title = "Create and Run a LEMMA Model Processing Chain"
        message = "Use this dialog to assemble a LEMMA Model Processing Chain that executes " +
            "launch configurations for LEMMA Model Processors in a given order"
    }

    override createDialogArea(Composite parent) {
        val container = super.createDialogArea(parent) as Composite
        container.layout = new GridLayout(3, false)
        container.layoutData = new GridData(SWT.FILL, SWT.FILL, true, true)

        createConfigurationNameTextField(container)
        createApplicableLaunchConfigurationsTable(container)
        createMoveButton(container)
        createChainTableAndButtons(container)

        return container
    }

    private def createConfigurationNameTextField(Composite parent) {
        val container = new Composite(parent, SWT.NULL)
        var layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)
        layoutData.horizontalSpan = 3
        container.layoutData = layoutData
        container.layout = new GridLayout(3, false)

        new Label(container, SWT.NULL).text = "Launch configuration name:"

        val textField = new Text(container, SWT.BORDER)
        layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)
        layoutData.horizontalSpan = 2
        textField.layoutData = layoutData

        val bindingAndDecoration = bindWithValidationDecorationSupport(
            textField,
            dataBindingContext,
            ProcessingChainWithLaunchConfigurationName,
            "launchConfigurationName",
            chainTemplate,
            [
                ProcessingChainWithLaunchConfigurationName
                    .validLaunchConfigurationName(it)
            ]
        )

        currentDecorations.add(bindingAndDecoration.value)
    }

    private def createApplicableLaunchConfigurationsTable(Composite parent) {
        val group = new Group(parent, SWT.SHADOW_ETCHED_IN)
        group.text = "Applicable Launch Configurations"
        group.layout = new GridLayout(1, false)
        group.layoutData = new GridData(GridData.FILL, GridData.FILL, true, true)

        launchConfigurationsTable = new TableViewer(group)
        launchConfigurationsTable.contentProvider = new IStructuredContentProvider() {
            override getElements(Object inputElement) {
                return availableLaunchConfigurations.keySet.sort
            }
        }
        launchConfigurationsTable.table.headerVisible = true
        launchConfigurationsTable.table.linesVisible = true
        launchConfigurationsTable.table.layoutData = new GridData(GridData.FILL, GridData.FILL,
            true, true)

        val column = new TableViewerColumn(launchConfigurationsTable, SWT.NONE)
        column.column.width = 300
        column.column.text = "LEMMA Model Processor Launch Configuration"
        column.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                return element as String
            }
        }

        launchConfigurationsTable.input = availableLaunchConfigurations
    }

    private def createMoveButton(Composite parent) {
        val container = new Composite(parent, SWT.NONE)
        container.layout = new GridLayout(1, false)
        container.layoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false)

        val button = new Button(container, SWT.PUSH)
        button.image = MOVE_RIGHT_IMAGE
        button.addListener(SWT.Selection, [
            if (launchConfigurationsTable.input === null) {
                return
            }

            val selection = launchConfigurationsTable.selection as IStructuredSelection
            if (selection.empty) {
                MessageDialog.openError(shell, "No Launch Configurations Selected", "Please " +
                    "select one or more entries from the list of available LEMMA Model Processor " +
                    "launch configurations")
                return
            }

            selection.forEach[chainTemplate.addEntry(new ProcessingChainEntry(
                (it as String),
                chainTemplate,
                PreviousExitValueComparator.Comparator.EQUAL,
                0
            ))]
            syncChainTable()
        ])
    }

    private def syncChainTable() {
        chainTable.input = chainTemplate.entries
    }

    private def createChainTableAndButtons(Composite parent) {
        val group = new Group(parent, SWT.SHADOW_ETCHED_IN)
        group.text = "Launch Configuration Chain Entries"
        group.layout = new GridLayout(1, false)
        group.layoutData = new GridData(GridData.FILL, GridData.FILL, true, true)

        chainTable = new TableViewer(group)
        chainTable.contentProvider = new IStructuredContentProvider() {
            override getElements(Object element) {
                return element as List<ProcessingChainEntry>
            }
        }
        chainTable.table.headerVisible = true
        chainTable.table.linesVisible = true
        chainTable.table.layoutData = new GridData(GridData.FILL, GridData.FILL, true, true)

        val column = new TableViewerColumn(chainTable, SWT.NONE)
        column.column.width = 300
        column.column.text = "Launch Configuration Chain Entry"
        column.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                return (element as ProcessingChainEntry).launchConfigurationName
            }
        }

        createChainTableButtons(group)
    }

    private def createChainTableButtons(Composite parent) {
        val buttonRow = new Composite(parent, SWT.NONE)
        buttonRow.layout = new RowLayout

        createRemoveButton(buttonRow)
        createMoveUpButton(buttonRow)
        createMoveDownButton(buttonRow)
    }

    private def createRemoveButton(Composite parent) {
        val button = new Button(parent, SWT.PUSH)
        button.image = REMOVE_IMAGE
        button.addListener(SWT.Selection, [
            if (chainTable.input === null) {
                return
            }

            val selectedEntries = rowSelectionOrError(chainTable)
            if (selectedEntries !== null) {
                chainTemplate.removeAll(selectedEntries.map[it as ProcessingChainEntry].toList)
                syncChainTable()
            }
        ])
    }

    private def createMoveUpButton(Composite parent) {
        val button = new Button(parent, SWT.PUSH)
        button.image = MOVE_UP_IMAGE
        button.addListener(SWT.Selection, [
            if (chainTable.input === null) {
                return
            }

            val selectedEntry = singleRowSelectionOrError(chainTable)?.firstElement
            if (selectedEntry !== null) {
                chainTemplate.moveUp(selectedEntry as ProcessingChainEntry)
                syncChainTable()
            }
        ])
    }

    private def createMoveDownButton(Composite parent) {
        val button = new Button(parent, SWT.PUSH)
        button.image = MOVE_DOWN_IMAGE
        button.addListener(SWT.Selection, [
            if (chainTable.input === null) {
                return
            }

            val selectedEntry = singleRowSelectionOrError(chainTable)?.firstElement
            if (selectedEntry !== null) {
                chainTemplate.moveDown(selectedEntry as ProcessingChainEntry)
                syncChainTable()
            }
        ])
    }

    override createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false)
        storeButton = createButton(parent, IDialogConstants.FINISH_ID, "Store Without Running",
            false)
        runButton = createButton(parent, IDialogConstants.OK_ID, "Run", true)

        updateDialogFromChainValidity()
    }

    override propertyChange(PropertyChangeEvent event) {
        updateDialogFromChainValidity()
    }

    private def updateDialogFromChainValidity() {
        val chainIsValid = chainTemplate.isValid()
        storeButton.enabled = chainIsValid
        runButton.enabled = chainIsValid
    }

    private def isValid(ProcessingChainWithLaunchConfigurationName chain) {
        return try {
            chain.validate(availableLaunchConfigurations)
            true
        } catch (IllegalArgumentException ex) {
            false
        }
    }

    override open() {
        if (availableLaunchConfigurations.empty) {
            MessageDialog.openError(shell, "No Launch Configurations", "No LEMMA Model Processor " +
                "launch configurations found.")
            return Window.CANCEL
        }

        return super.open()
    }

    override buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.FINISH_ID)
            storePressed()
        else
            super.buttonPressed(buttonId)
    }

    private def storePressed() {
        storeCallback.apply(chainTemplate, availableLaunchConfigurations)
        super.okPressed
    }

    override okPressed() {
        /*if (chainTemplate.entries.empty) {
            MessageDialog.openError(shell, "Empty Launch Configuration Chain", "Please add at " +
                "least one LEMMA Model Processor launch configuration to the chain.")
            return
        }*/

        runCallback.apply(chainTemplate, availableLaunchConfigurations)
        super.okPressed()
    }

    override close() {
        dataBindingContext.dispose()
        currentDecorations.forEach[it.dispose]
        chainTemplate.removePropertyChangeListener(this)
        super.close()
    }

    override isResizable() {
        return true
    }

    override getInitialSize() {
        return new Point(950, 500)
    }
}