package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut;

import de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument;
import java.util.ArrayList;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("all")
public abstract class AbstractCodeGeneratorTemplate extends AbstractLaunchConfigurationTemplate {
  protected final Shell parentShell;
  
  protected final IProject project;
  
  protected final IFile file;
  
  private final ProcessorExecutableType processorExecutableType;
  
  private Argument targetFolderArgument;
  
  public AbstractCodeGeneratorTemplate(final Shell parentShell, final String name, final IProject project, final IFile file) {
    super(parentShell, name, project, file);
    this.parentShell = parentShell;
    this.project = project;
    this.file = file;
    this.processorExecutableType = this.getProcessorExecutableType();
  }
  
  public abstract ProcessorExecutableType getProcessorExecutableType();
  
  @Override
  public ProcessingConfigurationWithLaunchConfigurationName extendInitializedProcessingConfigurationTemplate(final ProcessingConfigurationWithLaunchConfigurationName configuration) {
    configuration.setProcessorExecutableType(this.processorExecutableType);
    configuration.setProcessorBasicExecutionCommand(LaunchConfigurationConstants.SUPPORTED_DEFAULT_BASIC_PROCESSOR_EXECUTION_COMMANDS.get(this.processorExecutableType));
    final ArrayList<Argument> arguments = configuration.getArguments();
    arguments.add(Argument.newArgument().singleValued().sourceModel().parameter("-s"));
    arguments.add(Argument.newArgument().singleValued().intermediateModel().parameter("-i"));
    this.targetFolderArgument = Argument.newArgument().singleValued().folder().parameter("-t").value("");
    arguments.add(this.targetFolderArgument);
    return configuration;
  }
  
  @Override
  public final AbstractLaunchConfigurationTemplate.AbstractTemplateCompletionDialog getCompletionDialog() {
    final AbstractCodeGeneratorCompletionDialog dialog = this.getCodeGeneratorCompletionDialog();
    dialog.setProcessorExecutableType(this.processorExecutableType);
    dialog.setTargetFolderArgument(this.targetFolderArgument);
    return dialog;
  }
  
  public abstract AbstractCodeGeneratorCompletionDialog getCodeGeneratorCompletionDialog();
}
