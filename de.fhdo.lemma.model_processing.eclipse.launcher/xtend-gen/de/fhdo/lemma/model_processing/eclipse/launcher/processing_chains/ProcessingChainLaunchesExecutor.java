package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains;

import com.google.common.base.Objects;
import de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.LaunchConfigurationDelegate;
import java.util.Map;
import java.util.UUID;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ProcessingChainLaunchesExecutor extends Thread implements ILaunchesListener2 {
  private static final String LAUNCHES_IDENTIFIER_ATTRIBUTE_NAME = (("lemma" + 
    ProcessingChainLaunchesExecutor.class.getName()) + "LaunchesIdentifier");
  
  private final String chainName;
  
  private final ProcessingChain chain;
  
  private final Map<String, ILaunchConfiguration> processingLaunchConfigurations;
  
  private final IProgressMonitor monitor;
  
  private final String launchesIdentifier;
  
  private int currentEntryIndex;
  
  private MessageConsole console;
  
  private MessageConsoleStream infoStream;
  
  public ProcessingChainLaunchesExecutor(final String chainName, final ProcessingChain chain, final Map<String, ILaunchConfiguration> processingLaunchConfigurations, final IProgressMonitor monitor) {
    this.chainName = chainName;
    this.chain = chain;
    this.processingLaunchConfigurations = processingLaunchConfigurations;
    this.monitor = monitor;
    this.launchesIdentifier = UUID.randomUUID().toString();
  }
  
  @Override
  public void run() {
    LaunchConfigurationConstants.LAUNCH_MANAGER.addLaunchListener(this);
    this.currentEntryIndex = 0;
    this.console = Utils.getAndRevealConsole(LaunchConfigurationDelegate.CONSOLE_NAME);
    this.infoStream = this.console.newMessageStream();
    this.launch(this.chain, this.currentEntryIndex);
  }
  
  @Override
  public void launchesTerminated(final ILaunch[] launches) {
    final Function1<ILaunch, Boolean> _function = (ILaunch it) -> {
      String _attribute = it.getAttribute(ProcessingChainLaunchesExecutor.LAUNCHES_IDENTIFIER_ATTRIBUTE_NAME);
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
        if (((this.monitor.isCanceled() || 
          (this.currentEntryIndex == (this.chain.getEntries().size() - 1))) || 
          (!this.continueWithNextEntry(((ILaunch[])Conversions.unwrapArray(matchingLaunches, ILaunch.class))[0])))) {
          this.doneLaunches();
          return;
        }
      }
    }
    this.monitor.worked(1);
    this.currentEntryIndex++;
    this.launch(this.chain, this.currentEntryIndex);
  }
  
  private void doneLaunches() {
    try {
      this.infoStream.close();
      this.monitor.done();
      LaunchConfigurationConstants.LAUNCH_MANAGER.removeLaunchListener(this);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private boolean continueWithNextEntry(final ILaunch launch) {
    try {
      final int exitValue = (launch.getProcesses()[0]).getExitValue();
      final ProcessingChainEntry nextEntry = this.chain.getEntries().get((this.currentEntryIndex + 1));
      final Integer expectedExitValue = nextEntry.getPreviousExitValue();
      final PreviousExitValueComparator.Comparator exitValComparator = nextEntry.getPreviousExitValueComparator();
      Boolean _matches = PreviousExitValueComparator.matches(exitValue, (expectedExitValue).intValue(), exitValComparator);
      if ((_matches).booleanValue()) {
        return true;
      }
      final MessageConsoleStream errorStream = Utils.newErrorMessageStream(this.console);
      final String entryName = this.chain.getEntries().get(this.currentEntryIndex).getLaunchConfigurationName();
      final String nextEntryName = nextEntry.getLaunchConfigurationName();
      final String printableComparator = PreviousExitValueComparator.getUserRepresentation(exitValComparator);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Execution of LEMMA model processing chain \"");
      _builder.append(this.chainName);
      _builder.append("\" aborted. Launch ");
      String _plus = ("\n" + _builder);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("configuration \"");
      _builder_1.append(entryName);
      _builder_1.append("\" returned with unexpected exit value ");
      _builder_1.append(exitValue);
      _builder_1.append(". ");
      String _plus_1 = (_plus + _builder_1);
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("The following configuration \"");
      _builder_2.append(nextEntryName);
      _builder_2.append("\" requires however an exit value of ");
      String _plus_2 = (_plus_1 + _builder_2);
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append(printableComparator);
      _builder_3.append(" ");
      _builder_3.append(expectedExitValue);
      _builder_3.append(".");
      String _plus_3 = (_plus_2 + _builder_3);
      errorStream.println(_plus_3);
      errorStream.close();
      return false;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private void launch(final ProcessingChain chain, final int entryIndex) {
    try {
      this.console.clearConsole();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Executing LEMMA model processing chain \"");
      _builder.append(this.chainName);
      _builder.append("\": Entry ");
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append((entryIndex + 1));
      _builder_1.append(" of ");
      int _size = chain.getEntries().size();
      _builder_1.append(_size);
      String _plus = (_builder.toString() + _builder_1);
      String _plus_1 = (_plus + "\n");
      this.infoStream.println(_plus_1);
      final String launchConfigName = chain.getEntries().get(entryIndex).getLaunchConfigurationName();
      final ILaunchConfigurationWorkingCopy processingLaunchConfiguration = this.processingLaunchConfigurations.get(launchConfigName).getWorkingCopy();
      processingLaunchConfiguration.setAttribute(LaunchConfigurationDelegate.DISABLE_CONSOLE_CLEARING_LAUNCH_ATTRIBUTE, true);
      final ILaunch launch = DebugUITools.buildAndLaunch(processingLaunchConfiguration, 
        ILaunchManager.RUN_MODE, 
        SubMonitor.convert(this.monitor, 1));
      launch.setAttribute(ProcessingChainLaunchesExecutor.LAUNCHES_IDENTIFIER_ATTRIBUTE_NAME, this.launchesIdentifier);
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
