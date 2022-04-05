package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.shortcut;

import com.google.common.base.Objects;
import de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.ProcessingChain;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut2;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class LaunchShortcut implements ILaunchShortcut2 {
  private static final ILaunchManager LAUNCH_MANAGER = DebugPlugin.getDefault().getLaunchManager();
  
  @Override
  public ILaunchConfiguration[] getLaunchConfigurations(final ISelection selection) {
    final IFile file = Utils.getSelectedFile(selection);
    Iterable<ILaunchConfiguration> _xifexpression = null;
    if ((file != null)) {
      _xifexpression = this.getLaunchConfigurations(file);
    } else {
      _xifexpression = null;
    }
    return ((ILaunchConfiguration[])Conversions.unwrapArray(_xifexpression, ILaunchConfiguration.class));
  }
  
  private Iterable<ILaunchConfiguration> getLaunchConfigurations(final IFile file) {
    try {
      final Function1<ILaunchConfiguration, Boolean> _function = (ILaunchConfiguration it) -> {
        try {
          return Boolean.valueOf(((it.getMappedResources() != null) && 
            IterableExtensions.<IResource>exists(((Iterable<IResource>)Conversions.doWrapArray(it.getMappedResources())), ((Function1<IResource, Boolean>) (IResource resource) -> {
              return Boolean.valueOf(((resource instanceof IFile) && Objects.equal(resource.getFullPath(), file.getFullPath())));
            }))));
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      };
      return IterableExtensions.<ILaunchConfiguration>filter(((Iterable<ILaunchConfiguration>)Conversions.doWrapArray(LaunchShortcut.LAUNCH_MANAGER.getLaunchConfigurations(LaunchConfigurationConstants.PROCESSING_CHAIN_LAUNCH_CONFIGURATION_TYPE))), _function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  public IResource getLaunchableResource(final ISelection selection) {
    return null;
  }
  
  @Override
  public void launch(final ISelection selection, final String mode) {
    this.launch(Utils.getSelectedFile(selection), mode);
  }
  
  private void launch(final IFile file, final String mode) {
    if ((file == null)) {
      return;
    }
    Shell _activeShell = Utils.getActiveShell();
    Map<String, ILaunchConfiguration> _processingConfigurationLaunchConfigurations = Utils.getProcessingConfigurationLaunchConfigurations();
    ProcessingChainWithLaunchConfigurationName _processingChainWithLaunchConfigurationName = new ProcessingChainWithLaunchConfigurationName();
    final Procedure2<ProcessingChainWithLaunchConfigurationName, Map<String, ILaunchConfiguration>> _function = (ProcessingChainWithLaunchConfigurationName processingChain, Map<String, ILaunchConfiguration> availableLaunchConfigurations) -> {
      this.storeAndRun(processingChain, availableLaunchConfigurations);
    };
    final Procedure2<ProcessingChainWithLaunchConfigurationName, Map<String, ILaunchConfiguration>> _function_1 = (ProcessingChainWithLaunchConfigurationName processingChain, Map<String, ILaunchConfiguration> availableLaunchConfigurations) -> {
      this.store(processingChain, availableLaunchConfigurations);
    };
    final LaunchConfigurationChainCreationDialog dialog = new LaunchConfigurationChainCreationDialog(_activeShell, _processingConfigurationLaunchConfigurations, _processingChainWithLaunchConfigurationName, _function, _function_1);
    dialog.open();
  }
  
  private void storeAndRun(final ProcessingChainWithLaunchConfigurationName completedProcessingChain, final Map<String, ILaunchConfiguration> availableLaunchConfigurations) {
    DebugUITools.launch(this.store(completedProcessingChain, availableLaunchConfigurations), 
      ILaunchManager.RUN_MODE);
  }
  
  private ILaunchConfiguration store(final ProcessingChainWithLaunchConfigurationName completedProcessingChain, final Map<String, ILaunchConfiguration> availableLaunchConfigurations) {
    try {
      try {
        completedProcessingChain.validate(availableLaunchConfigurations);
      } catch (final Throwable _t) {
        if (_t instanceof IllegalArgumentException) {
          final IllegalArgumentException ex = (IllegalArgumentException)_t;
          Shell _activeShell = Utils.getActiveShell();
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("chain is erroneous: ");
          String _message = ex.getMessage();
          _builder.append(_message);
          _builder.append(". Execution not possible.");
          String _plus = ("Processing " + _builder);
          MessageDialog.openError(_activeShell, "Erroneous processing chain", _plus);
          throw ex;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      final ILaunchConfigurationWorkingCopy newLaunchConfigurationWorkingCopy = LaunchConfigurationConstants.PROCESSING_CHAIN_LAUNCH_CONFIGURATION_TYPE.newInstance(null, completedProcessingChain.getLaunchConfigurationName());
      ProcessingChain.setProcessingChainAsAttribute(newLaunchConfigurationWorkingCopy, completedProcessingChain);
      return newLaunchConfigurationWorkingCopy.doSave(ILaunchConfiguration.UPDATE_PROTOTYPE_CHILDREN);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  public ILaunchConfiguration[] getLaunchConfigurations(final IEditorPart editor) {
    final IFile file = Utils.getEditedFile(editor);
    Iterable<ILaunchConfiguration> _xifexpression = null;
    if ((file != null)) {
      _xifexpression = this.getLaunchConfigurations(file);
    } else {
      _xifexpression = null;
    }
    return ((ILaunchConfiguration[])Conversions.unwrapArray(_xifexpression, ILaunchConfiguration.class));
  }
  
  @Override
  public IResource getLaunchableResource(final IEditorPart editorpart) {
    return null;
  }
  
  @Override
  public void launch(final IEditorPart editor, final String mode) {
    this.launch(Utils.getEditedFile(editor), mode);
  }
}
