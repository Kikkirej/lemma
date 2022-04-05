package de.fhdo.lemma.model_processing.eclipse.launcher

import org.eclipse.ui.plugin.AbstractUIPlugin
import org.osgi.framework.BundleContext
import org.eclipse.swt.widgets.FileDialog
import org.eclipse.swt.SWT
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.DockerImageSelectionDialog
import org.eclipse.jface.window.Window
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableTypesInformationManager
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableTypeInformation
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType

class Activator extends AbstractUIPlugin {
    override start(BundleContext context) {
        super.start(context)

        initializeExecutableTypesInformation()
    }

    private def initializeExecutableTypesInformation() {
        ProcessorExecutableTypesInformationManager.register(
            new ProcessorExecutableTypeInformation(
                ProcessorExecutableType.LOCAL_DOCKER_IMAGE,
                "Local Docker Image",
                "local Docker image",
                [shell, processingConfiguration |
                    val dialog = new DockerImageSelectionDialog(shell,
                        processingConfiguration.processorBasicExecutionCommand)
                    return if (dialog.open == Window.OK)
                            dialog.selectedImage
                        else
                            null
                ]
            ),
            new ProcessorExecutableTypeInformation(
                ProcessorExecutableType.LOCAL_JAVA_PROGRAM,
                "Local Java Program",
                "local Java program",
                [shell, _ |
                    val dialog = new FileDialog(shell, SWT.OPEN)
                    dialog.text = "Select local Java program"
                    dialog.filterExtensions = #["*.jar"]
                    dialog.open
                ]
            ),
            new ProcessorExecutableTypeInformation(
                ProcessorExecutableType.RAW_EXECUTABLE,
                "Raw executable",
                "raw executable",
                null
            )
        )
    }
}