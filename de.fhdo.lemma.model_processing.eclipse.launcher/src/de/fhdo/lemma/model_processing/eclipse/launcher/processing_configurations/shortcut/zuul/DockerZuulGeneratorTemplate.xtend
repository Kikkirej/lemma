package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.zuul

import org.eclipse.swt.widgets.Shell
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.AbstractCodeGeneratorCompletionDialog
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.ProcessingConfigurationWithLaunchConfigurationName

class DockerZuulGeneratorTemplate extends AbstractZuulGeneratorTemplate {
    new(Shell parentShell, IProject project, IFile file) {
        this(parentShell, "Docker-based Zuul artifact generation", project, file)
    }

    new(Shell parentShell, String name, IProject project, IFile file) {
        super(parentShell, name, project, file)
    }

    override getProcessorExecutableType() {
        return ProcessorExecutableType.LOCAL_DOCKER_IMAGE
    }

    override extendInitializedProcessingConfigurationTemplate(
        ProcessingConfigurationWithLaunchConfigurationName configuration
    ) {
        val initializedConfiguration = super
            .extendInitializedProcessingConfigurationTemplate(configuration)

        initializedConfiguration.processorExecutablePath
            = ZuulGeneratorConstants.DEFAULT_DOCKER_IMAGE_NAME

        return configuration
    }

    override getCodeGeneratorCompletionDialog() {
        return new TemplateCompletionDialog(parentShell, project, file)
    }

    static class TemplateCompletionDialog extends AbstractCodeGeneratorCompletionDialog {
        protected new(Shell parentShell, IProject project, IFile file) {
            super(parentShell, project, file, ZuulGeneratorConstants.GENERATOR_LONG_NAME,
                ZuulGeneratorConstants.GENERATOR_SHORT_NAME)
        }

        override getLaunchConfigurationNameAddendum() {
            return "Docker"
        }

        override getProcessorExecutableLabelTextAddendum() {
            return "Docker image"
        }

        override create() {
            super.create()
            title = "Generate Zuul Artifacts From LEMMA Models Using Docker"
            message = '''Use this dialog to configure and run LEMMA's «generatorLongName» in a ''' +
                "Docker container on the selected operation model"
        }
    }
}