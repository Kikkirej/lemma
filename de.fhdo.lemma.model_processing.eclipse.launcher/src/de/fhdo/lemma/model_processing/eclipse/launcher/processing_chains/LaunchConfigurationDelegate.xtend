package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains

import org.eclipse.debug.core.model.ILaunchConfigurationDelegate
import org.eclipse.debug.core.ILaunchConfiguration
import org.eclipse.debug.core.ILaunch
import org.eclipse.core.runtime.IProgressMonitor
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.CoreException

class LaunchConfigurationDelegate implements ILaunchConfigurationDelegate {
    override launch(ILaunchConfiguration launchConfiguration, String mode, ILaunch launch,
        IProgressMonitor monitor) {
        val chain = ProcessingChain.deserializeFrom(launchConfiguration)
        if (chain === null)
            throw new CoreException(new Status(IStatus.ERROR, LaunchConfigurationDelegate,
                "LEMMA model processing chain not readable. Launch aborted."))

        val processingLaunchConfigurations = getProcessingConfigurationLaunchConfigurations()
        try {
            chain.validate(processingLaunchConfigurations)
        } catch (IllegalArgumentException ex) {
            throw new CoreException(new Status(IStatus.ERROR, LaunchConfigurationDelegate,
                "Invalid LEMMA model processing chain:\n" + ex.message + "\n\nLaunch aborted."))
        }

        /*val console = getAndRevealConsole(CONSOLE_NAME)
        console.clearConsole
        infoStream = console.newMessageStream
        errorStream = console.newErrorMessageStream*/

        /*val launchExecutor = new Runnable() {
            val LAUNCHES_IDENTIFIER = UUID.randomUUID.toString
            var int currentEntryIndex
            var ILaunchesListener2 launchListener

            override run() {
                launchListener = new ILaunchesListener2 {
                    override launchesTerminated(ILaunch[] launches) {
                        val matchingLaunches = launches
                            .filter[it.getAttribute("yolobert") == LAUNCHES_IDENTIFIER]
                        if (matchingLaunches.empty)
                            return
                        else if (matchingLaunches.size > 1) {
                            doneLaunches()
                            throw new IllegalStateException("There should be only one launch " +
                                "for an entry in a LEMMA Model Processing Chain with launches " +
                                '''identifier «LAUNCHES_IDENTIFIER»''')
                        } else if (currentEntryIndex == chain.entries.size - 1) {
                            doneLaunches()
                            return
                        }

                        // The launch configuration delegate for LEMMA model processor runs always
                        // associates exactly one process to fired launches
                        val launchProcess = matchingLaunches.get(0).processes.get(0)

                        monitor.worked(1)
                        currentEntryIndex++
                        chain.launch(currentEntryIndex)
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

                LAUNCH_MANAGER.addLaunchListener(launchListener)
                currentEntryIndex = 0
                // There must be at least one entry in the chain as otherwise it wouldn't be valid
                // and thus launchable
                chain.launch(currentEntryIndex)
            }

            private def doneLaunches() {
                monitor.done
                LAUNCH_MANAGER.removeLaunchListener(launchListener)
            }

            private def launch(ProcessingChain chain, int entryIndex) {
                val launchConfigurationName = chain.entries.get(entryIndex).launchConfigurationName
                val processingLaunchConfiguration = processingLaunchConfigurations
                    .get(launchConfigurationName)
                val launch = DebugUITools.buildAndLaunch(
                    processingLaunchConfiguration,
                    ILaunchManager.RUN_MODE,
                    SubMonitor.convert(monitor, 1)
                )
                launch.setAttribute("yolobert", LAUNCHES_IDENTIFIER)
            }
        }
        new Thread(launchExecutor).start()*/
        new ProcessingChainLaunchesExecutor(launchConfiguration.name, chain,
            processingLaunchConfigurations, monitor).start()
    }
}