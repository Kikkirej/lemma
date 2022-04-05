package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

@SuppressWarnings("all")
public class LaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {
  @Override
  public void createTabs(final ILaunchConfigurationDialog dialog, final String mode) {
    LaunchConfigurationTab _launchConfigurationTab = new LaunchConfigurationTab();
    this.setTabs(new ILaunchConfigurationTab[] { _launchConfigurationTab });
  }
}
