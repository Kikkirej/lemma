package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut

import org.eclipse.jface.dialogs.Dialog
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Composite
import org.eclipse.jface.dialogs.IDialogConstants
import org.eclipse.swt.widgets.Group
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.widgets.Text
import org.eclipse.swt.dnd.Clipboard
import org.eclipse.swt.dnd.TextTransfer
import org.eclipse.swt.dnd.Transfer
import org.eclipse.ui.PlatformUI
import org.osgi.framework.FrameworkUtil

// Custom implementation of internal Eclipse dialog
// org.eclipse.debug.internal.ui.launchConfigurations.ShowCommandLineDialog
class ShowCommandLineDialog extends Dialog {
    val String commandLine
    var Text commandLineTextField

    new(Shell parentShell, String commandLine) {
        super(parentShell)
        shellStyle = SWT.RESIZE.bitwiseOr(shellStyle)
        this.commandLine = commandLine
    }

    override configureShell(Shell newShell) {
        super.configureShell(newShell)
        newShell.text = "Command Line"
    }

    override createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, "Close", true)
        createButton(parent, IDialogConstants.OK_ID, "Copy && Close", true)
    }

    override createDialogArea(Composite parent) {
        val container = super.createDialogArea(parent) as Composite

        val group = new Group(container, SWT.NONE)
        val topLayout = new GridLayout()
        group.layout = topLayout
        var gridData = new GridData(GridData.FILL_BOTH)
        gridData.heightHint = convertHeightInCharsToPixels(20)
        gridData.widthHint = convertWidthInCharsToPixels(90)
        group.layoutData = gridData

        commandLineTextField = new Text(
            group,
            SWT.MULTI.bitwiseOr(SWT.WRAP).bitwiseOr(SWT.BORDER).bitwiseOr(SWT.V_SCROLL)
        )
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = convertHeightInCharsToPixels(10);
        gridData.widthHint = convertWidthInCharsToPixels(60);
        commandLineTextField.layoutData = gridData
        commandLineTextField.text = commandLine
        commandLineTextField.editable = false

        return container
    }

    override buttonPressed(int buttonId) {
        if (buttonId !== IDialogConstants.OK_ID) {
            super.buttonPressed(buttonId)
            return
        }

        val clipboard = new Clipboard(null)
        try {
            val textTransfer = TextTransfer.instance
            val Transfer[] transfers = #[textTransfer]
            val Object[] data = #[commandLineTextField.text]
            clipboard.setContents(data, transfers)
        } finally {
            clipboard.dispose()
        }
    }

    override getDialogBoundsSettings() {
        val bundle = FrameworkUtil.getBundle(ShowCommandLineDialog)
        val settings = PlatformUI.getDialogSettingsProvider(bundle).dialogSettings
        return settings.getSection(getDialogSettingsSectionName())
            ?: settings.addNewSection(getDialogSettingsSectionName())
    }

    protected def String getDialogSettingsSectionName() {
        return "SHOW_COMMAND_LINE_DIALOG"
    }
}