package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.AbstractLaunchConfigurationTemplate.AbstractTemplateCompletionDialog
import org.eclipse.swt.widgets.Shell
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import org.eclipse.debug.core.DebugPlugin
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Text
import org.eclipse.swt.widgets.Button
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.ProcessingConfigurationWithLaunchConfigurationName
import org.eclipse.swt.widgets.DirectoryDialog
import org.eclipse.jface.dialogs.MessageDialog
import java.beans.PropertyChangeEvent
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.StringUtils
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableTypesInformationManager

abstract class AbstractCodeGeneratorCompletionDialog extends AbstractTemplateCompletionDialog {
    protected static val DEFAULT_WIDTH = 800
    static val DEFAULT_SIZE = new Point(DEFAULT_WIDTH, 300)
    static val LAUNCH_MANAGER = DebugPlugin.^default.launchManager

    var ProcessorExecutableType processorExecutableType
    var Argument targetFolderArgument
    protected var Text processorExecutablePath
    protected var Text processorBasicExecutionCommand
    protected var Text targetFolder
    protected val String generatorLongName
    protected val String generatorShortName

    protected new(Shell parentShell, IProject project, IFile file, String generatorLongName,
        String generatorShortName) {
        super(parentShell, project, file)
        this.generatorLongName = generatorLongName
        this.generatorShortName = generatorShortName
    }

    final protected def setProcessorExecutableType(
        ProcessorExecutableType processorExecutableType
    ) {
        this.processorExecutableType = processorExecutableType
    }

    final protected def setTargetFolderArgument(Argument targetFolderArgument) {
        this.targetFolderArgument = targetFolderArgument
        // Let argument participate in configuration validation in
        // AbstractTemplateCompletionDialog.propertyChange()
        this.targetFolderArgument.addPropertyChangeListener(this)
    }

    override create() {
        super.create()
        configurationTemplate.launchConfigurationName = LAUNCH_MANAGER
            .generateLaunchConfigurationName(getLaunchConfigurationNamePrefix(file))
    }

    final def getLaunchConfigurationNamePrefix(IFile file) {
        val fileBasename = FilenameUtils.getBaseName(file.name)
        val fileExtension = file.fileExtension ?: ""
        var addendum = getLaunchConfigurationNameAddendum()
        if (!addendum.empty)
            addendum = StringUtils.replace(addendum, " ", "_") + "_"

        return '''Run_«generatorShortName»_«addendum»«fileBasename»_«fileExtension»'''
    }

    def getLaunchConfigurationNameAddendum() {
        return ""
    }

    override createDialogArea(Composite parent) {
        val area = super.createDialogArea(parent) as Composite
        val container = new Composite(area, SWT.NONE)
        container.layoutData = new GridData(SWT.FILL, SWT.FILL, true, true)
        container.layout = new GridLayout(3, false)

        container.addConfigurationNameTextField(2)
        container.addPathToProcessorExecutable
        container.addProcessorBasicExecutionCommand
        container.addTargetFolder
        container.addAdditionalControlsToDialog

        parent.shell.size = DEFAULT_SIZE

        return area
    }

    def void addAdditionalControlsToDialog(Composite parent) {
        // NOOP
    }

    private def addPathToProcessorExecutable(Composite parent) {
        new Label(parent, SWT.NULL).text = '''«getProcessorExecutableLabelText()»:'''

        processorExecutablePath = new Text(parent, SWT.BORDER)
        processorExecutablePath.layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)

        val processorExecutablePathSelectionButton = new Button(parent, SWT.PUSH)
        processorExecutablePathSelectionButton.text = "Browse..."
        processorExecutablePathSelectionButton.addListener(SWT.Selection, [
            val selectedExecutablePath = try {
                ProcessorExecutableTypesInformationManager
                    .inputSupportFunction(processorExecutableType)
                    .apply(shell, configurationTemplate)
            } catch (IllegalArgumentException ex) {
                MessageDialog.openError(shell, "Input Support Error", ex.message)
                null
            }

            if (selectedExecutablePath !== null)
                processorExecutablePath.text = selectedExecutablePath
        ])

        bindWithValidationDecorationSupport(
            processorExecutablePath,
            ProcessingConfigurationWithLaunchConfigurationName,
            "processorExecutablePath",
            configurationTemplate,
            [
                ProcessingConfigurationWithLaunchConfigurationName.validProcessorExecutablePath(
                    configurationTemplate.processorExecutableType,
                    configurationTemplate.processorBasicExecutionCommand,
                    it
                )
            ]
        )
    }

    private def String getProcessorExecutableLabelText() {
        var labelAddendum = getProcessorExecutableLabelTextAddendum()
        if (!labelAddendum.empty)
            labelAddendum = " " + labelAddendum
        return generatorLongName + labelAddendum
    }

    def String getProcessorExecutableLabelTextAddendum() {
        return ""
    }

    private def addProcessorBasicExecutionCommand(Composite parent) {
        new Label(parent, SWT.NULL).text = "Basic processor execution command:"
        processorBasicExecutionCommand = new Text(parent, SWT.BORDER)
        val layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)
        layoutData.horizontalSpan = 2
        processorBasicExecutionCommand.layoutData = layoutData

        bindWithValidationDecorationSupport(
            processorBasicExecutionCommand,
            ProcessingConfigurationWithLaunchConfigurationName,
            "processorBasicExecutionCommand",
            configurationTemplate,
            [
                ProcessingConfigurationWithLaunchConfigurationName
                    .validProcessorBasicExecutionCommand(
                        configurationTemplate.processorExecutableType,
                        it
                    )
            ]
        )
    }

    private def addTargetFolder(Composite parent) {
        new Label(parent, SWT.NULL).text = "Generation target folder:"
        targetFolder = new Text(parent, SWT.BORDER)
        targetFolder.layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)

        val folderSelectionButton = new Button(parent, SWT.PUSH)
        folderSelectionButton.text = "Browse..."
        folderSelectionButton.addListener(SWT.Selection, [
            val directoryDialog = new DirectoryDialog(shell, SWT.OPEN)
            directoryDialog.text = "Select target folder for generated artifacts"
            directoryDialog.filterPath = configurationTemplate.sourceModelFile.project
                .location.makeAbsolute.toString

            val selectedFolder = directoryDialog.open
            if (selectedFolder !== null)
                targetFolder.text = selectedFolder
        ])

        bindWithValidationDecorationSupport(
            targetFolder,
            Argument,
            "value",
            targetFolderArgument,
            [
                targetFolderArgument.type.checkValidValueInUserRepresentation(configurationTemplate,
                    it)
            ]
        )
    }

    override reactToPropertyChange(PropertyChangeEvent event) {
        if (event.propertyName == "processorBasicExecutionCommand")
            triggerValidation(processorExecutablePath)
    }

    override close() {
        targetFolderArgument.removePropertyChangeListener(this)
        super.close()
    }
}