package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.eureka;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.AbstractCodeGeneratorCompletionDialog;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.ProcessingConfigurationWithLaunchConfigurationName;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class DockerEurekaGeneratorTemplate extends AbstractEurekaGeneratorTemplate {
  public static class TemplateCompletionDialog extends AbstractCodeGeneratorCompletionDialog {
    protected TemplateCompletionDialog(final Shell parentShell, final IProject project, final IFile file) {
      super(parentShell, project, file, EurekaGeneratorConstants.GENERATOR_LONG_NAME, 
        EurekaGeneratorConstants.GENERATOR_SHORT_NAME);
    }
    
    @Override
    public String getLaunchConfigurationNameAddendum() {
      return "Docker";
    }
    
    @Override
    public String getProcessorExecutableLabelTextAddendum() {
      return "Docker image";
    }
    
    @Override
    public void create() {
      super.create();
      this.setTitle("Generate Eureka Artifacts From LEMMA Models Using Docker");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Use this dialog to configure and run LEMMA\'s ");
      _builder.append(this.generatorLongName);
      _builder.append(" in a ");
      String _plus = (_builder.toString() + 
        "Docker container on the selected operation model");
      this.setMessage(_plus);
    }
  }
  
  public DockerEurekaGeneratorTemplate(final Shell parentShell, final IProject project, final IFile file) {
    this(parentShell, "Docker-based Eureka artifact generation", project, file);
  }
  
  public DockerEurekaGeneratorTemplate(final Shell parentShell, final String name, final IProject project, final IFile file) {
    super(parentShell, name, project, file);
  }
  
  @Override
  public ProcessorExecutableType getProcessorExecutableType() {
    return ProcessorExecutableType.LOCAL_DOCKER_IMAGE;
  }
  
  @Override
  public ProcessingConfigurationWithLaunchConfigurationName extendInitializedProcessingConfigurationTemplate(final ProcessingConfigurationWithLaunchConfigurationName configuration) {
    final ProcessingConfigurationWithLaunchConfigurationName initializedConfiguration = super.extendInitializedProcessingConfigurationTemplate(configuration);
    initializedConfiguration.setProcessorExecutablePath(EurekaGeneratorConstants.DEFAULT_DOCKER_IMAGE_NAME);
    return configuration;
  }
  
  @Override
  public AbstractCodeGeneratorCompletionDialog getCodeGeneratorCompletionDialog() {
    return new DockerEurekaGeneratorTemplate.TemplateCompletionDialog(this.parentShell, this.project, this.file);
  }
}
