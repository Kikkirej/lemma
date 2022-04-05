package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator

import org.eclipse.swt.widgets.Shell
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.ProcessingConfigurationWithLaunchConfigurationName
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator.JavaBaseGeneratorParameters.GenletType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.graphics.Point

class DockerJavaBaseGeneratorTemplate extends AbstractJavaBaseGeneratorTemplate {

    new(Shell parentShell, IProject project, IFile file) {
        this(parentShell, "Docker-based Java microservice generation", project, file)
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
            = JavaBaseGeneratorParameters.DEFAULT_DOCKER_IMAGE_NAME

        GenletType.values.forEach[initializedConfiguration.arguments.add(it.buildGenletArgument)]

        return configuration
    }

    private def buildGenletArgument(GenletType genletType) {
         return Argument.newArgument
            .singleValued
            .stringPair
            .parameter(JavaBaseGeneratorParameters.GENLET_PARAMETER)
            .value(JavaBaseGeneratorParameters.instance.getDockerGenletFilePath(genletType))
    }

    override getJavaBaseGeneratorCompletionDialog() {
        return new TemplateCompletionDialog(parentShell, project, file)
    }

    static class TemplateCompletionDialog extends AbstractJavaBaseGeneratorCompletionDialog {
        protected new(Shell parentShell, IProject project, IFile file) {
            super(parentShell, project, file)
        }

        override getLaunchConfigurationNameAddendum() {
            return "Docker"
        }

        override getProcessorExecutableLabelTextAddendum() {
            return "Docker image"
        }

        override create() {
            super.create()
            title = "Generate Java Microservices From LEMMA Models Using Docker"
            message = '''Use this dialog to configure and run LEMMA's «generatorLongName» in a ''' +
                '''Docker container on the selected «printableModelFileType» model'''
        }

        override createDialogArea(Composite parent) {
            val area = super.createDialogArea(parent)
            parent.shell.size = new Point(DEFAULT_WIDTH, 340)
            return area
        }
    }
}