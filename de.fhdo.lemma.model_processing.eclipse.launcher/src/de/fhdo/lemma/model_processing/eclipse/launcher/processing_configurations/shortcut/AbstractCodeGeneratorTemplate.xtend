package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut

import org.eclipse.swt.widgets.Shell
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import static de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants.SUPPORTED_DEFAULT_BASIC_PROCESSOR_EXECUTION_COMMANDS
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType

abstract class AbstractCodeGeneratorTemplate extends AbstractLaunchConfigurationTemplate {
    protected val Shell parentShell
    protected val IProject project
    protected val IFile file
    val ProcessorExecutableType processorExecutableType
    var Argument targetFolderArgument

    new(Shell parentShell, String name, IProject project, IFile file) {
        super(parentShell, name, project, file)
        this.parentShell = parentShell
        this.project = project
        this.file = file
        processorExecutableType = getProcessorExecutableType()
    }

    abstract def ProcessorExecutableType getProcessorExecutableType()

    override extendInitializedProcessingConfigurationTemplate(
        ProcessingConfigurationWithLaunchConfigurationName configuration
    ) {
        configuration.processorExecutableType = processorExecutableType
        configuration.processorBasicExecutionCommand
            = SUPPORTED_DEFAULT_BASIC_PROCESSOR_EXECUTION_COMMANDS.get(processorExecutableType)

        val arguments = configuration.arguments
        arguments.add(Argument.newArgument.singleValued.sourceModel.parameter("-s"))
        arguments.add(Argument.newArgument.singleValued.intermediateModel.parameter("-i"))
        targetFolderArgument = Argument.newArgument.singleValued.folder.parameter("-t").value("")
        arguments.add(targetFolderArgument)

        return configuration
    }

    final override getCompletionDialog() {
        val dialog = getCodeGeneratorCompletionDialog()
        dialog.processorExecutableType = processorExecutableType
        dialog.targetFolderArgument = targetFolderArgument
        return dialog
    }

    abstract def AbstractCodeGeneratorCompletionDialog getCodeGeneratorCompletionDialog()
}