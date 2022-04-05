package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration
import org.eclipse.xtend.lib.annotations.Accessors
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import org.eclipse.debug.core.DebugPlugin

class ProcessingConfigurationWithLaunchConfigurationName extends ProcessingConfiguration
    implements Cloneable {
    static val LAUNCH_MANAGER = DebugPlugin.^default.launchManager

    @Accessors(PUBLIC_GETTER)
    var String launchConfigurationName

    def setLaunchConfigurationName(String launchConfigurationName) {
        firePropertyChange("launchConfigurationName", this.launchConfigurationName,
            this.launchConfigurationName = launchConfigurationName)
    }

    override validateInUserRepresentation() {
        validLaunchConfigurationName(launchConfigurationName)
        super.validateInUserRepresentation()
    }

    static def validLaunchConfigurationName(String launchConfigurationName) {
        notNullOrEmpty(launchConfigurationName, "Launch configuration name must not be empty")
        if (LAUNCH_MANAGER.isExistingLaunchConfigurationName(launchConfigurationName))
            throw new IllegalArgumentException("Launch configuration already exists")
    }

    override clone() {
        return super.clone()
    }
}