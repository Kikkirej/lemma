package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.shortcut;

import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.ProcessingChain;
import java.util.Map;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class ProcessingChainWithLaunchConfigurationName extends ProcessingChain implements Cloneable {
  private static final ILaunchManager LAUNCH_MANAGER = DebugPlugin.getDefault().getLaunchManager();
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private String launchConfigurationName;
  
  public void setLaunchConfigurationName(final String launchConfigurationName) {
    this.firePropertyChange("launchConfigurationName", this.launchConfigurationName, 
      this.launchConfigurationName = launchConfigurationName);
  }
  
  @Override
  public void validate(final Map<String, ILaunchConfiguration> availableLaunchConfigurations) {
    super.validate(availableLaunchConfigurations);
    ProcessingChainWithLaunchConfigurationName.validLaunchConfigurationName(this.launchConfigurationName);
  }
  
  public static void validLaunchConfigurationName(final String launchConfigurationName) {
    try {
      Utils.notNullOrEmpty(launchConfigurationName, "Launch configuration name must not be empty");
      boolean _isExistingLaunchConfigurationName = ProcessingChainWithLaunchConfigurationName.LAUNCH_MANAGER.isExistingLaunchConfigurationName(launchConfigurationName);
      if (_isExistingLaunchConfigurationName) {
        throw new IllegalArgumentException("Launch configuration already exists");
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  public Object clone() {
    return super.clone();
  }
  
  @Pure
  public String getLaunchConfigurationName() {
    return this.launchConfigurationName;
  }
}
