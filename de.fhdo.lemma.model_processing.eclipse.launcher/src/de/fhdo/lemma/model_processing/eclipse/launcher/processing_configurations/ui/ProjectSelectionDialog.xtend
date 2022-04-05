package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui

import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.widgets.Composite

import org.eclipse.ui.dialogs.ResourceListSelectionDialog
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.resources.IResource

/**
 * Dialog to select files from an Eclipse project.
 *
 * @author <a href="mailto:florian.rademacher@fh-dortmund.de">Florian Rademacher</a>
 */
class ProjectSelectionDialog extends ResourceListSelectionDialog {
    var initializePattern = true

    new(Shell parentShell) {
        super(parentShell, ResourcesPlugin.workspace.root, IResource.PROJECT)
    }

    override createDialogArea(Composite parent) {
        val dialogArea = super.createDialogArea(parent)
        refresh(false)
        return dialogArea
    }

    override adjustPattern() {
        return if (initializePattern) {
                initializePattern = false
                "*"
            } else
                super.adjustPattern()
    }

    def getSelectedProject() {
        return if (result !== null && !result.empty)
                result.get(0) as IProject
            else
                null
    }
}