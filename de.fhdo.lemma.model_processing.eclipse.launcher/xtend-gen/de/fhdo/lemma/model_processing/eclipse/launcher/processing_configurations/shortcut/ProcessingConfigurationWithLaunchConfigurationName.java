package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut;

import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class ProcessingConfigurationWithLaunchConfigurationName extends ProcessingConfiguration implements Cloneable {
  private static final ILaunchManager LAUNCH_MANAGER = DebugPlugin.getDefault().getLaunchManager();
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private String launchConfigurationName;
  
  public void setLaunchConfigurationName(final String launchConfigurationName) {
    this.firePropertyChange("launchConfigurationName", this.launchConfigurationName, 
      this.launchConfigurationName = launchConfigurationName);
  }
  
  @Override
  public void validateInUserRepresentation() {
    ProcessingConfigurationWithLaunchConfigurationName.validLaunchConfigurationName(this.launchConfigurationName);
    super.validateInUserRepresentation();
  }
  
  public static void validLaunchConfigurationName(final String launchConfigurationName) {
    try {
      Utils.notNullOrEmpty(launchConfigurationName, "Launch configuration name must not be empty");
      boolean _isExistingLaunchConfigurationName = ProcessingConfigurationWithLaunchConfigurationName.LAUNCH_MANAGER.isExistingLaunchConfigurationName(launchConfigurationName);
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
