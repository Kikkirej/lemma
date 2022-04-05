package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup
import org.eclipse.debug.ui.ILaunchConfigurationDialog

class LaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {
    override createTabs(ILaunchConfigurationDialog dialog, String mode) {
        setTabs(#[new LaunchConfigurationTab])
    }
}