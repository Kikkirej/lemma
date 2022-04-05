package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.shortcut

import org.eclipse.xtend.lib.annotations.Accessors
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import org.eclipse.debug.core.ILaunchConfiguration
import java.util.Map
import org.eclipse.debug.core.DebugPlugin
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.ProcessingChain

class ProcessingChainWithLaunchConfigurationName extends ProcessingChain implements Cloneable {
    static val LAUNCH_MANAGER = DebugPlugin.^default.launchManager

    @Accessors(PUBLIC_GETTER)
    var String launchConfigurationName

    def setLaunchConfigurationName(String launchConfigurationName) {
        firePropertyChange("launchConfigurationName", this.launchConfigurationName,
            this.launchConfigurationName = launchConfigurationName)
    }

    override validate(Map<String, ILaunchConfiguration> availableLaunchConfigurations) {
        super.validate(availableLaunchConfigurations)
        validLaunchConfigurationName(launchConfigurationName)
    }

    static def validLaunchConfigurationName(String launchConfigurationName) {
        notNullOrEmpty(launchConfigurationName, "Launch configuration name must not be empty")
        if (LAUNCH_MANAGER.isExistingLaunchConfigurationName(launchConfigurationName))
            throw new IllegalArgumentException("Launch configuration already exists")
    }

    override clone() {
        super.clone
    }
}