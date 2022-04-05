package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.mongodb

import org.eclipse.swt.widgets.Shell
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.AbstractCodeGeneratorCompletionDialog
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.ProcessingConfigurationWithLaunchConfigurationName

class DockerMongoDBGeneratorTemplate extends AbstractMongoDBGeneratorTemplate {
    new(Shell parentShell, IProject project, IFile file) {
        this(parentShell, "Docker-based MongoDB artifact generation", project, file)
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
            = MongoDBGeneratorConstants.DEFAULT_DOCKER_IMAGE_NAME

        return configuration
    }

    override getCodeGeneratorCompletionDialog() {
        return new TemplateCompletionDialog(parentShell, project, file)
    }

    static class TemplateCompletionDialog extends AbstractCodeGeneratorCompletionDialog {
        protected new(Shell parentShell, IProject project, IFile file) {
            super(parentShell, project, file, MongoDBGeneratorConstants.GENERATOR_LONG_NAME,
                MongoDBGeneratorConstants.GENERATOR_SHORT_NAME)
        }

        override getLaunchConfigurationNameAddendum() {
            return "Docker"
        }

        override getProcessorExecutableLabelTextAddendum() {
            return "Docker image"
        }

        override create() {
            super.create()
            title = "Generate MongoDB Artifacts From LEMMA Models Using Docker"
            message = '''Use this dialog to configure and run LEMMA's «generatorLongName» in a ''' +
                "Docker container on the selected operation model"
        }
    }
}