package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportWithAliasArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.AbstractCodeGeneratorCompletionDialog;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.ProcessingConfigurationWithLaunchConfigurationName;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DockerTypeFocusedMappingModelJavaBaseGeneratorTemplate extends DockerJavaBaseGeneratorTemplate {
  private static class TypeFocusedMappingModelTemplateCompletionDialog extends DockerJavaBaseGeneratorTemplate.TemplateCompletionDialog {
    private Argument alternativeIntermediateServiceModelArgument;
    
    protected TypeFocusedMappingModelTemplateCompletionDialog(final Shell parentShell, final IProject project, final IFile file) {
      super(parentShell, project, file);
    }
    
    @Override
    public Control createDialogArea(final Composite parent) {
      final Control area = super.createDialogArea(parent);
      Shell _shell = parent.getShell();
      Point _point = new Point(AbstractCodeGeneratorCompletionDialog.DEFAULT_WIDTH, 380);
      _shell.setSize(_point);
      return area;
    }
    
    @Override
    public void addAdditionalControlsToDialog(final Composite parent) {
      super.addAdditionalControlsToDialog(parent);
      this.addAlternativeIntermediateServiceModel(parent);
    }
    
    private void addAlternativeIntermediateServiceModel(final Composite parent) {
      Label _label = new Label(parent, SWT.NULL);
      _label.setText("Service model:");
      final IntermediateModelOfImportWithAliasArgumentTypeComboWrapper comboWrapper = new IntermediateModelOfImportWithAliasArgumentTypeComboWrapper(parent, 
        (SWT.DROP_DOWN | SWT.READ_ONLY), 
        this.configurationTemplate, 
        JavaBaseGeneratorParameters.ALTERNATIVE_INTERMEDIATE_SERVICE_MODEL_PARAMETER);
      final GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
      layoutData.horizontalSpan = 2;
      comboWrapper.setLayoutData(layoutData);
      final Procedure1<String> _function = (String it) -> {
        AbstractArgumentType _type = comboWrapper.getArgument().getType();
        ((IntermediateModelOfImportWithAliasArgumentType) _type).checkValidValue(this.configurationTemplate, it);
      };
      this.<Argument>bindWithValidationDecorationSupport(
        comboWrapper.getCombo(), 
        Argument.class, 
        "value", 
        comboWrapper.getArgument(), _function);
      this.alternativeIntermediateServiceModelArgument = comboWrapper.getArgument();
      this.alternativeIntermediateServiceModelArgument.addPropertyChangeListener(this);
    }
    
    @Override
    public void completeProcessingConfigurationTemplate(final ProcessingConfigurationWithLaunchConfigurationName templateToComplete) {
      templateToComplete.getArguments().add(this.alternativeIntermediateServiceModelArgument);
    }
    
    @Override
    public boolean close() {
      boolean _xblockexpression = false;
      {
        this.alternativeIntermediateServiceModelArgument.removePropertyChangeListener(this);
        _xblockexpression = super.close();
      }
      return _xblockexpression;
    }
  }
  
  public DockerTypeFocusedMappingModelJavaBaseGeneratorTemplate(final Shell parentShell, final IProject project, final IFile file) {
    super(parentShell, "Type-focused mapping model: Docker-based Java microservice generation", project, file);
  }
  
  @Override
  public AbstractJavaBaseGeneratorCompletionDialog getJavaBaseGeneratorCompletionDialog() {
    return new DockerTypeFocusedMappingModelJavaBaseGeneratorTemplate.TypeFocusedMappingModelTemplateCompletionDialog(this.parentShell, this.project, this.file);
  }
  
  @Override
  public Boolean isApplicable(final EObject modelRoot, final Map<String, String> technologyNamePerAlias) {
    return Boolean.valueOf(true);
  }
}
