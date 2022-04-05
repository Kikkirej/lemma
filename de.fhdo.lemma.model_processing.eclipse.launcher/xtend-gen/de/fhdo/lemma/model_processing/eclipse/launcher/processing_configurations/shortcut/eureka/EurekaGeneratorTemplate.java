package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.eureka;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.AbstractCodeGeneratorCompletionDialog;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class EurekaGeneratorTemplate extends AbstractEurekaGeneratorTemplate {
  public static class TemplateCompletionDialog extends AbstractCodeGeneratorCompletionDialog {
    protected TemplateCompletionDialog(final Shell parentShell, final IProject project, final IFile file) {
      super(parentShell, project, file, EurekaGeneratorConstants.GENERATOR_LONG_NAME, 
        EurekaGeneratorConstants.GENERATOR_SHORT_NAME);
    }
    
    @Override
    public String getProcessorExecutableLabelTextAddendum() {
      return "path";
    }
    
    @Override
    public void create() {
      super.create();
      this.setTitle("Generate Eureka Artifacts From LEMMA Models");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Use this dialog to configure and run LEMMA\'s ");
      _builder.append(this.generatorLongName);
      _builder.append(" on ");
      String _plus = (_builder.toString() + 
        "the selected operation model");
      this.setMessage(_plus);
    }
  }
  
  public EurekaGeneratorTemplate(final Shell parentShell, final IProject project, final IFile file) {
    this(parentShell, "Eureka artifact generation", project, file);
  }
  
  public EurekaGeneratorTemplate(final Shell parentShell, final String name, final IProject project, final IFile file) {
    super(parentShell, name, project, file);
  }
  
  @Override
  public ProcessorExecutableType getProcessorExecutableType() {
    return ProcessorExecutableType.LOCAL_JAVA_PROGRAM;
  }
  
  @Override
  public AbstractCodeGeneratorCompletionDialog getCodeGeneratorCompletionDialog() {
    return new EurekaGeneratorTemplate.TemplateCompletionDialog(this.parentShell, this.project, this.file);
  }
}
