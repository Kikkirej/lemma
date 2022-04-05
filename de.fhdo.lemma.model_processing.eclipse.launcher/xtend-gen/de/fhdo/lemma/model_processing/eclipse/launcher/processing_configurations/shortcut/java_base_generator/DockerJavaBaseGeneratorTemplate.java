package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.AbstractCodeGeneratorCompletionDialog;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.ProcessingConfigurationWithLaunchConfigurationName;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class DockerJavaBaseGeneratorTemplate extends AbstractJavaBaseGeneratorTemplate {
  public static class TemplateCompletionDialog extends AbstractJavaBaseGeneratorCompletionDialog {
    protected TemplateCompletionDialog(final Shell parentShell, final IProject project, final IFile file) {
      super(parentShell, project, file);
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
      this.setTitle("Generate Java Microservices From LEMMA Models Using Docker");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Use this dialog to configure and run LEMMA\'s ");
      _builder.append(this.generatorLongName);
      _builder.append(" in a ");
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("Docker container on the selected ");
      String _printableModelFileType = this.printableModelFileType();
      _builder_1.append(_printableModelFileType);
      _builder_1.append(" model");
      String _plus = (_builder.toString() + _builder_1);
      this.setMessage(_plus);
    }
    
    @Override
    public Control createDialogArea(final Composite parent) {
      final Control area = super.createDialogArea(parent);
      Shell _shell = parent.getShell();
      Point _point = new Point(AbstractCodeGeneratorCompletionDialog.DEFAULT_WIDTH, 340);
      _shell.setSize(_point);
      return area;
    }
  }
  
  public DockerJavaBaseGeneratorTemplate(final Shell parentShell, final IProject project, final IFile file) {
    this(parentShell, "Docker-based Java microservice generation", project, file);
  }
  
  public DockerJavaBaseGeneratorTemplate(final Shell parentShell, final String name, final IProject project, final IFile file) {
    super(parentShell, name, project, file);
  }
  
  @Override
  public ProcessorExecutableType getProcessorExecutableType() {
    return ProcessorExecutableType.LOCAL_DOCKER_IMAGE;
  }
  
  @Override
  public ProcessingConfigurationWithLaunchConfigurationName extendInitializedProcessingConfigurationTemplate(final ProcessingConfigurationWithLaunchConfigurationName configuration) {
    final ProcessingConfigurationWithLaunchConfigurationName initializedConfiguration = super.extendInitializedProcessingConfigurationTemplate(configuration);
    initializedConfiguration.setProcessorExecutablePath(JavaBaseGeneratorParameters.DEFAULT_DOCKER_IMAGE_NAME);
    final Consumer<JavaBaseGeneratorParameters.GenletType> _function = (JavaBaseGeneratorParameters.GenletType it) -> {
      initializedConfiguration.getArguments().add(this.buildGenletArgument(it));
    };
    ((List<JavaBaseGeneratorParameters.GenletType>)Conversions.doWrapArray(JavaBaseGeneratorParameters.GenletType.values())).forEach(_function);
    return configuration;
  }
  
  private Argument buildGenletArgument(final JavaBaseGeneratorParameters.GenletType genletType) {
    return Argument.newArgument().singleValued().stringPair().parameter(JavaBaseGeneratorParameters.GENLET_PARAMETER).value(JavaBaseGeneratorParameters.instance().getDockerGenletFilePath(genletType));
  }
  
  @Override
  public AbstractJavaBaseGeneratorCompletionDialog getJavaBaseGeneratorCompletionDialog() {
    return new DockerJavaBaseGeneratorTemplate.TemplateCompletionDialog(this.parentShell, this.project, this.file);
  }
}
