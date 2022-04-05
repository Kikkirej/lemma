package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains

import java.util.UUID
import org.eclipse.debug.core.ILaunchesListener2
import org.eclipse.debug.core.ILaunch
import static de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants.*
import org.eclipse.core.runtime.SubMonitor
import org.eclipse.debug.core.ILaunchConfiguration
import java.util.Map
import org.eclipse.debug.ui.DebugUITools
import org.eclipse.debug.core.ILaunchManager
import org.eclipse.core.runtime.IProgressMonitor
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import static de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.LaunchConfigurationDelegate.*
import org.eclipse.ui.console.MessageConsole
import org.eclipse.ui.console.MessageConsoleStream
import org.eclipse.debug.core.DebugPlugin
import org.eclipse.debug.internal.ui.DebugUIPlugin

class ProcessingChainLaunchesExecutor extends Thread implements ILaunchesListener2 {
    static val LAUNCHES_IDENTIFIER_ATTRIBUTE_NAME = "lemma" +
        typeof(ProcessingChainLaunchesExecutor).name + "LaunchesIdentifier"

    val String chainName
    val ProcessingChain chain
    val Map<String, ILaunchConfiguration> processingLaunchConfigurations
    val IProgressMonitor monitor
    val String launchesIdentifier
    var int currentEntryIndex
    var MessageConsole console
    var MessageConsoleStream infoStream

    new(
        String chainName,
        ProcessingChain chain,
        Map<String, ILaunchConfiguration> processingLaunchConfigurations,
        IProgressMonitor monitor
    ) {
        this.chainName = chainName
        this.chain = chain
        this.processingLaunchConfigurations = processingLaunchConfigurations
        this.monitor = monitor
        launchesIdentifier = UUID.randomUUID.toString
    }

    override void run() {
        LAUNCH_MANAGER.addLaunchListener(this)
        currentEntryIndex = 0
        console = getAndRevealConsole(CONSOLE_NAME)
        infoStream = console.newMessageStream

        // There must be at least one entry in the chain as otherwise it wouldn't be valid
        // and thus launchable
        chain.launch(currentEntryIndex)
    }

    override launchesTerminated(ILaunch[] launches) {
        val matchingLaunches = launches.filter[
            it.getAttribute(LAUNCHES_IDENTIFIER_ATTRIBUTE_NAME) == launchesIdentifier
        ]
        if (matchingLaunches.empty)
            return
        else if (matchingLaunches.size > 1) {
            doneLaunches()
            throw new IllegalStateException("There should be only one launch for an entry in a " +
                '''LEMMA Model Processing Chain with launches identifier «launchesIdentifier»''')
        } else if (
            monitor.canceled ||
            currentEntryIndex == chain.entries.size - 1 ||
            !continueWithNextEntry(matchingLaunches.get(0))
        ) {
            doneLaunches()
            return
        }

        monitor.worked(1)
        currentEntryIndex++
        chain.launch(currentEntryIndex)
    }

    private def doneLaunches() {
        infoStream.close()
        monitor.done
        LAUNCH_MANAGER.removeLaunchListener(this)
    }

    private def continueWithNextEntry(ILaunch launch) {
        // The launch configuration delegate for LEMMA model processor runs always associates
        // exactly one process to fired launches
        val exitValue = launch.processes.get(0).exitValue
        val nextEntry = chain.entries.get(currentEntryIndex+1)
        val expectedExitValue = nextEntry.previousExitValue
        val exitValComparator = nextEntry.previousExitValueComparator
        if (PreviousExitValueComparator.matches(exitValue, expectedExitValue, exitValComparator))
            return true

        val errorStream = newErrorMessageStream(console)
        val entryName = chain.entries.get(currentEntryIndex).launchConfigurationName
        val nextEntryName = nextEntry.launchConfigurationName
        val printableComparator = PreviousExitValueComparator
            .getUserRepresentation(exitValComparator)
        errorStream.println(
            "\n" + '''Execution of LEMMA model processing chain "«chainName»" aborted. Launch ''' +
            '''configuration "«entryName»" returned with unexpected exit value «exitValue». ''' +
            '''The following configuration "«nextEntryName»" requires however an exit value of ''' +
            '''«printableComparator» «expectedExitValue».'''
        )
        errorStream.close()
        return false
    }

    private def launch(ProcessingChain chain, int entryIndex) {
        console.clearConsole
        infoStream.println('''Executing LEMMA model processing chain "«chainName»": Entry ''' +
            '''«entryIndex+1» of «chain.entries.size»''' + "\n")

        val launchConfigName = chain.entries.get(entryIndex).launchConfigurationName
        val processingLaunchConfiguration = processingLaunchConfigurations.get(launchConfigName)
            .getWorkingCopy
        // We need to hang the attribute for disabling console clearing by the launch delegate for
        // LEMMA model processor executions to the launch configuration itself as buildAndLaunch
        // blocks until the invocation of launch() in the launch delegate. Note that this attribute
        // is temporarily because we don't save the working copy of the configuration.
        processingLaunchConfiguration.setAttribute(DISABLE_CONSOLE_CLEARING_LAUNCH_ATTRIBUTE, true)
        val launch = DebugUITools.buildAndLaunch(
            processingLaunchConfiguration,
            ILaunchManager.RUN_MODE,
            SubMonitor.convert(monitor, 1)
        )
        launch.setAttribute(LAUNCHES_IDENTIFIER_ATTRIBUTE_NAME, launchesIdentifier)
    }

    override launchesAdded(ILaunch[] launches) {
        // NOOP
    }

    override launchesChanged(ILaunch[] launches) {
        // NOOP
    }

    override launchesRemoved(ILaunch[] launches) {
        // NOOP
    }

}