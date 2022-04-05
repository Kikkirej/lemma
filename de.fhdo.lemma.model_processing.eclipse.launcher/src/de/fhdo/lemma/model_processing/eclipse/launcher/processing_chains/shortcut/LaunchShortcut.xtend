package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.shortcut

import org.eclipse.jface.viewers.ISelection
import org.eclipse.ui.IEditorPart
import org.eclipse.core.resources.IFile
import org.eclipse.debug.core.DebugPlugin
import org.eclipse.debug.ui.ILaunchShortcut2
import static de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants.PROCESSING_CHAIN_LAUNCH_CONFIGURATION_TYPE
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import org.eclipse.jface.dialogs.IDialogConstants
import org.eclipse.jface.window.Window
import org.eclipse.debug.core.ILaunchManager
import org.eclipse.debug.ui.DebugUITools
import java.util.Map
import org.eclipse.debug.core.ILaunchConfiguration
import org.eclipse.jface.dialogs.MessageDialog
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.ProcessingChain

class LaunchShortcut implements ILaunchShortcut2 {
    static val LAUNCH_MANAGER = DebugPlugin.^default.launchManager

    override getLaunchConfigurations(ISelection selection) {
        val file = getSelectedFile(selection)
        return if (file !== null)
                file.launchConfigurations
            else
                null
    }

    private def getLaunchConfigurations(IFile file) {
        return LAUNCH_MANAGER
            .getLaunchConfigurations(PROCESSING_CHAIN_LAUNCH_CONFIGURATION_TYPE)
            .filter[
                it.mappedResources !== null &&
                it.mappedResources.exists[resource |
                    resource instanceof IFile && resource.fullPath == file.fullPath
                ]
            ]
    }

    override getLaunchableResource(ISelection selection) {
        return null
    }

    override launch(ISelection selection, String mode) {
        getSelectedFile(selection).launch(mode)
    }

    private def launch(IFile file, String mode) {
        if (file === null)
            return

        val dialog = new LaunchConfigurationChainCreationDialog(
            getActiveShell(),
            getProcessingConfigurationLaunchConfigurations(),
            new ProcessingChainWithLaunchConfigurationName(),
            [processingChain, availableLaunchConfigurations |
                storeAndRun(processingChain, availableLaunchConfigurations)],
            [processingChain, availableLaunchConfigurations |
                store(processingChain, availableLaunchConfigurations)]
        )
        dialog.open
    }

    private def storeAndRun(ProcessingChainWithLaunchConfigurationName completedProcessingChain,
        Map<String, ILaunchConfiguration> availableLaunchConfigurations) {
            DebugUITools.launch(completedProcessingChain.store(availableLaunchConfigurations),
                ILaunchManager.RUN_MODE)
        }

    private def store(ProcessingChainWithLaunchConfigurationName completedProcessingChain,
        Map<String, ILaunchConfiguration> availableLaunchConfigurations) {
        try {
            completedProcessingChain.validate(availableLaunchConfigurations)
        } catch(IllegalArgumentException ex) {
            MessageDialog.openError(getActiveShell(), "Erroneous processing chain", "Processing " +
                '''chain is erroneous: «ex.message». Execution not possible.''')
            throw ex
        }

        val newLaunchConfigurationWorkingCopy = PROCESSING_CHAIN_LAUNCH_CONFIGURATION_TYPE
            .newInstance(null, completedProcessingChain.launchConfigurationName)
        ProcessingChain.setProcessingChainAsAttribute(newLaunchConfigurationWorkingCopy,
            completedProcessingChain)
        return newLaunchConfigurationWorkingCopy
            .doSave(ILaunchConfiguration.UPDATE_PROTOTYPE_CHILDREN)
    }

    override getLaunchConfigurations(IEditorPart editor) {
        val file = getEditedFile(editor)
        return if (file !== null)
                file.launchConfigurations
            else
                null
    }

    override getLaunchableResource(IEditorPart editorpart) {
        return null
    }

    override launch(IEditorPart editor, String mode) {
        getEditedFile(editor).launch(mode)
    }
}