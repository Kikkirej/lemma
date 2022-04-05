package de.fhdo.lemma.model_processing.eclipse.launcher;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableTypeInformation;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableTypesInformationManager;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.DockerImageSelectionDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.osgi.framework.BundleContext;

@SuppressWarnings("all")
public class Activator extends AbstractUIPlugin {
  @Override
  public void start(final BundleContext context) {
    try {
      super.start(context);
      this.initializeExecutableTypesInformation();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private void initializeExecutableTypesInformation() {
    final Function2<Shell, ProcessingConfiguration, String> _function = (Shell shell, ProcessingConfiguration processingConfiguration) -> {
      String _processorBasicExecutionCommand = processingConfiguration.getProcessorBasicExecutionCommand();
      final DockerImageSelectionDialog dialog = new DockerImageSelectionDialog(shell, _processorBasicExecutionCommand);
      String _xifexpression = null;
      int _open = dialog.open();
      boolean _equals = (_open == Window.OK);
      if (_equals) {
        _xifexpression = dialog.getSelectedImage();
      } else {
        _xifexpression = null;
      }
      return _xifexpression;
    };
    ProcessorExecutableTypeInformation _processorExecutableTypeInformation = new ProcessorExecutableTypeInformation(
      ProcessorExecutableType.LOCAL_DOCKER_IMAGE, 
      "Local Docker Image", 
      "local Docker image", _function);
    final Function2<Shell, ProcessingConfiguration, String> _function_1 = (Shell shell, ProcessingConfiguration __) -> {
      String _xblockexpression = null;
      {
        final FileDialog dialog = new FileDialog(shell, SWT.OPEN);
        dialog.setText("Select local Java program");
        dialog.setFilterExtensions(new String[] { "*.jar" });
        _xblockexpression = dialog.open();
      }
      return _xblockexpression;
    };
    ProcessorExecutableTypeInformation _processorExecutableTypeInformation_1 = new ProcessorExecutableTypeInformation(
      ProcessorExecutableType.LOCAL_JAVA_PROGRAM, 
      "Local Java Program", 
      "local Java program", _function_1);
    ProcessorExecutableTypeInformation _processorExecutableTypeInformation_2 = new ProcessorExecutableTypeInformation(
      ProcessorExecutableType.RAW_EXECUTABLE, 
      "Raw executable", 
      "raw executable", 
      null);
    ProcessorExecutableTypesInformationManager.register(_processorExecutableTypeInformation, _processorExecutableTypeInformation_1, _processorExecutableTypeInformation_2);
  }
}
