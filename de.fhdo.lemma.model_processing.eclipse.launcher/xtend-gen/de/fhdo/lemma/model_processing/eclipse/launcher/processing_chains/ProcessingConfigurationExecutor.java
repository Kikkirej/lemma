package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains;

import com.google.common.base.Objects;
import de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants;
import java.util.Map;
import java.util.UUID;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ProcessingConfigurationExecutor extends Thread implements ILaunchesListener2 {
  private static final String LAUNCHES_IDENTIFIER_ATTRIBUTE_NAME = (("lemma" + 
    ProcessingConfigurationExecutor.class.getName()) + "LaunchesIdentifier");
  
  private final ProcessingChain chain;
  
  private final Map<String, ILaunchConfiguration> processingLaunchConfigurations;
  
  private final IProgressMonitor monitor;
  
  private final String launchesIdentifier;
  
  private int currentEntryIndex;
  
  public ProcessingConfigurationExecutor(final ProcessingChain chain, final Map<String, ILaunchConfiguration> processingLaunchConfigurations, final IProgressMonitor monitor) {
    this.chain = chain;
    this.processingLaunchConfigurations = processingLaunchConfigurations;
    this.monitor = monitor;
    this.launchesIdentifier = UUID.randomUUID().toString();
  }
  
  @Override
  public void run() {
    LaunchConfigurationConstants.LAUNCH_MANAGER.addLaunchListener(this);
    this.currentEntryIndex = 0;
    this.launch(this.chain, this.currentEntryIndex);
  }
  
  @Override
  public void launchesTerminated(final ILaunch[] launches) {
    final Function1<ILaunch, Boolean> _function = (ILaunch it) -> {
      String _attribute = it.getAttribute(ProcessingConfigurationExecutor.LAUNCHES_IDENTIFIER_ATTRIBUTE_NAME);
      return Boolean.valueOf(Objects.equal(_attribute, this.launchesIdentifier));
    };
    final Iterable<ILaunch> matchingLaunches = IterableExtensions.<ILaunch>filter(((Iterable<ILaunch>)Conversions.doWrapArray(launches)), _function);
    boolean _isEmpty = IterableExtensions.isEmpty(matchingLaunches);
    if (_isEmpty) {
      return;
    } else {
      int _size = IterableExtensions.size(matchingLaunches);
      boolean _greaterThan = (_size > 1);
      if (_greaterThan) {
        this.doneLaunches();
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("LEMMA Model Processing Chain with launches identifier ");
        _builder.append(this.launchesIdentifier);
        String _plus = ("There should be only one launch for an entry in a " + _builder);
        throw new IllegalStateException(_plus);
      } else {
        int _size_1 = this.chain.getEntries().size();
        int _minus = (_size_1 - 1);
        boolean _equals = (this.currentEntryIndex == _minus);
        if (_equals) {
          this.doneLaunches();
          return;
        }
      }
    }
    final IProcess launchProcess = (((ILaunch[])Conversions.unwrapArray(matchingLaunches, ILaunch.class))[0]).getProcesses()[0];
    this.monitor.worked(1);
    this.currentEntryIndex++;
    this.launch(this.chain, this.currentEntryIndex);
  }
  
  private void doneLaunches() {
    this.monitor.done();
    LaunchConfigurationConstants.LAUNCH_MANAGER.removeLaunchListener(this);
  }
  
  private void launch(final ProcessingChain chain, final int entryIndex) {
    try {
      final String launchConfigName = chain.getEntries().get(entryIndex).getLaunchConfigurationName();
      final ILaunchConfiguration processingLaunchConfiguration = this.processingLaunchConfigurations.get(launchConfigName);
      final ILaunch launch = DebugUITools.buildAndLaunch(processingLaunchConfiguration, 
        ILaunchManager.RUN_MODE, 
        SubMonitor.convert(this.monitor, 1));
      launch.setAttribute(ProcessingConfigurationExecutor.LAUNCHES_IDENTIFIER_ATTRIBUTE_NAME, this.launchesIdentifier);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  public void launchesAdded(final ILaunch[] launches) {
  }
  
  @Override
  public void launchesChanged(final ILaunch[] launches) {
  }
  
  @Override
  public void launchesRemoved(final ILaunch[] launches) {
  }
}
