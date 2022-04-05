package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations

import org.eclipse.debug.ui.AbstractLaunchConfigurationTab
import org.eclipse.debug.core.ILaunchConfiguration
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Combo
import org.eclipse.swt.events.SelectionListener
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.widgets.Text
import org.eclipse.jface.fieldassist.ControlDecoration
import org.eclipse.jface.fieldassist.FieldDecorationRegistry
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.core.databinding.Binding
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport
import java.util.function.Supplier
import org.eclipse.jface.databinding.swt.typed.WidgetProperties
import org.eclipse.core.databinding.beans.typed.BeanProperties
import org.eclipse.core.databinding.UpdateValueStrategy
import org.eclipse.core.databinding.validation.ValidationStatus
import org.eclipse.core.databinding.conversion.IConverter
import org.eclipse.debug.ui.DebugUITools
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.ConstraintTypeFactory
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.ArgumentKindFactory
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.ConstantParameterArgumentKind
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.MultiValuedParameterArgumentKind
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.SingleValuedParameterArgumentKind
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.ConstraintTypeIdentifier
import org.eclipse.swt.widgets.Group
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.constraints_table.ConstraintsTable
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.arguments_table.ArgumentsTable
import org.eclipse.core.runtime.IStatus
import static de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants.*
import java.io.ByteArrayOutputStream
import javax.xml.stream.XMLOutputFactory
import java.nio.charset.StandardCharsets
import org.eclipse.swt.widgets.Button
import org.eclipse.ui.dialogs.ResourceListSelectionDialog
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.resources.IResource
import org.eclipse.swt.widgets.Shell
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.ui.model.WorkbenchLabelProvider
import org.eclipse.jface.window.Window
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ArgumentTypeFactory
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.SourceModelArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.SourceModelKind
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeEvent
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractIntermediateModelArgumentTypeWithEnumValue
import de.fhdo.lemma.eclipse.ui.ProgrammaticIntermediateModelTransformation
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.ProjectSelectionDialog
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.ProjectFileSelectionDialog
import org.eclipse.jface.dialogs.MessageDialog

class LaunchConfigurationTab extends AbstractLaunchConfigurationTab
    implements PropertyChangeListener {
    static val DEFAULT_PROCESSOR_EXECUTABLE_TYPE = ProcessorExecutableType.LOCAL_JAVA_PROGRAM
    /*static val DEFAULT_SOURCE_MODEL_FILE_ARGUMENT_TYPE = ArgumentTypeFactory
        .fromIdentifier(SourceModelArgumentType.IDENTIFIER) as SourceModelArgumentType
    static val DEFAULT_SOURCE_MODEL_FILE_ARGUMENT_VALUE = DEFAULT_SOURCE_MODEL_FILE_ARGUMENT_TYPE
        .getValidLiteralStringValues().get(SourceModelKind.SELECTED_FILE)
    static val DEFAULT_SOURCE_MODEL_FILE_ARGUMENT = new Argument(
        ArgumentKindFactory.fromIdentifier(SingleValuedParameterArgumentKind.IDENTIFIER),
        DEFAULT_SOURCE_MODEL_FILE_ARGUMENT_TYPE,
        "-s",
        DEFAULT_SOURCE_MODEL_FILE_ARGUMENT_VALUE
    )*/
    static val DEFAULT_SOURCE_MODEL_FILE_ARGUMENT = Argument.newArgument
        .singleValued
        .sourceModel
        .parameter("-s")

    static val SUPPORTED_CONSTRAINT_TYPES = #{
        ConstraintTypeFactory.fromIdentifier(ConstraintTypeIdentifier.FILENAME_REGEX),
        ConstraintTypeFactory.fromIdentifier(ConstraintTypeIdentifier.MODEL_KIND)
    }

    static val SUPPORTED_ARGUMENT_KINDS = #{
        ArgumentKindFactory.fromIdentifier(ConstantParameterArgumentKind.IDENTIFIER),
        ArgumentKindFactory.fromIdentifier(MultiValuedParameterArgumentKind.IDENTIFIER),
        ArgumentKindFactory.fromIdentifier(SingleValuedParameterArgumentKind.IDENTIFIER)
    }

    val CONFIGURATION_BINDERS
        = <Supplier<ControlDecoration>>newArrayList(
            //[bindConfigurationName],
            [bindSourceModelProjectName],
            [bindSourceModelFilePath],
            [bindProcessorExecutableType],
            [bindProcessorBasicExecutionCommand],
            [bindProcessorExecutablePath]
        )

    val dataBindingContext = new DataBindingContext()
    val currentDecorations = <ControlDecoration>newArrayList

    //var Text configurationName
    var Composite mainComposite
    var Text sourceModelProjectName
    var Text sourceModelFilePath
    var Combo processorExecutableType
    var Text processorBasicExecutionCommand
    var Text processorExecutablePath
    var ControlDecoration processorExecutablePathDecorator
    // TODO probably not necessary
    //var ConstraintsTable constraintsTable
    var ArgumentsTable argumentsTable

    var initializationDone = false
    var sourceModelArgumentWasAddedOnce = false
    var ProcessingConfiguration originalConfiguration
    var ProcessingConfiguration currentConfiguration
    var ProcessorExecutableType previousProcessorExecutableType

    override createControl(Composite parent) {
        // TODO
        /*try {
            configurations
                = PropertiesAccess.loadOrCreateProcessingConfigurations(element as IProject)
            // In case a previous user interaction exited unexpectedly, make sure that the editable
            // and unedited configurations are synchronized
            configurations.resetEditInformation()
            configurations.convertEditableConfigurationsToUserRepresentation()

            setConfigurationsSelectionFromConfigurations()
            prepareInputFields()
        } catch (ProcessingConfigurationDeserializationException ex) {
            MessageDialog.openError(shell, "Error during configuration deserialization", ex.message)
        }*/

        mainComposite = new Composite(parent, SWT.NULL)
        mainComposite.layoutData = new GridData(SWT.FILL, SWT.FILL, true, true)
        mainComposite.layout = new GridLayout(1, false)
        setControl(mainComposite)

        // TODO Probably not necessary
        //addConfigurationsSelection(composite)
        addResourceInputs(mainComposite)
        addProcessorInputs(mainComposite)
        // TODO Probably not necessary
        //addConstraintsTable(mainComposite)
        addArgumentsTable(mainComposite)
    }

    private def addResourceInputs(Composite parent) {
        val resourceInputsGrid = new Composite(parent, SWT.NULL)
        resourceInputsGrid.layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)
        resourceInputsGrid.layout = new GridLayout(3, false)

        addSourceModelProject(resourceInputsGrid)
        addSourceModelFile(resourceInputsGrid)
    }

    private def addSourceModelProject(Composite parent) {
        new Label(parent, SWT.NULL).text = "Source model project:"

        sourceModelProjectName = new Text(parent, SWT.BORDER)
        sourceModelProjectName.layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)

        val projectSelectionButton = new Button(parent, SWT.PUSH)
        projectSelectionButton.text = "Browse..."
        projectSelectionButton.addListener(SWT.Selection,
            [handleSourceModelProjectSelection(parent.shell)])
    }

    private def handleSourceModelProjectSelection(Shell shell) {
        var IProject selectedProject
        val dialog = new ProjectSelectionDialog(shell)
        if (dialog.open() != Window.CANCEL) {
            selectedProject = dialog.selectedProject
            sourceModelProjectName.text = selectedProject.name
        }

        // Don't clear source model file field if it already contains a file that exists in the
        // selected project
        try {
            ProcessingConfiguration.validSourceModelFilePath(selectedProject?.name,
                sourceModelFilePath.text)
        } catch (IllegalArgumentException ex) {
            sourceModelFilePath.text = ""
        }
    }

    private def addSourceModelFile(Composite parent) {
        new Label(parent, SWT.NULL).text = "Source model file:"

        sourceModelFilePath = new Text(parent, SWT.BORDER)
        sourceModelFilePath.layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)

        val fileSelectionButton = new Button(parent, SWT.PUSH)
        fileSelectionButton.text = "Browse..."
        fileSelectionButton.addListener(SWT.Selection,
            [handleSourceModelFileSelection(parent.shell)])
    }

    private def handleSourceModelFileSelection(Shell shell) {
        val selectedProject = findProjectInCurrentWorkspace(sourceModelProjectName.text)
        var IFile selectedFile
        if (selectedProject !== null) {
            val dialog = new ProjectFileSelectionDialog(shell, selectedProject)
            if (dialog.open() != Window.CANCEL)
                selectedFile = dialog.selectedFile
        } else {
            val dialog = new ResourceListSelectionDialog(shell, ResourcesPlugin.workspace.root,
                IResource.FILE)
            if (dialog.open() != Window.CANCEL && !dialog.result.empty)
                selectedFile = dialog.result.get(0) as IFile
        }

        if (selectedFile !== null) {
            // If not project was specified before, use that of the selected file. Note that the
            // assignment to the "text" property of the project text field must happen before that
            // for the file text field as otherwise the binding validation of the file text field is
            // likely to report a non-existing file because the model has not been updated with the
            // new project name, yet.
            if (selectedProject === null)
                sourceModelProjectName.text = selectedFile.project.name

            sourceModelFilePath.text = selectedFile.projectRelativePath.toString
        }
    }

    private def addProcessorInputs(Composite parent) {
        // TODO
        /*val separator = new Label(parent,
            SWT.SEPARATOR.bitwiseOr(SWT.SHADOW_OUT).bitwiseOr(SWT.HORIZONTAL))
        separator.layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false)*/

        val inputGroup = new Group(parent, SWT.SHADOW_ETCHED_IN)
        inputGroup.text = "Processor Information"
        inputGroup.layout = new GridLayout(2, false)
        inputGroup.layoutData = new GridData(GridData.FILL, GridData.BEGINNING, true, false)

        // TODO probably not necessary
        /*new Label(detailsGrid, SWT.NULL).text = "Configuration Name:"
        configurationName = new Text(detailsGrid, SWT.BORDER)
        configurationName.layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)*/

        addTypeOfProcessorExecutable(inputGroup)
        addPathToProcessorExecutable(inputGroup)
        addBasicProcessorExecutionCommand(inputGroup)
    }

    private def addTypeOfProcessorExecutable(Composite parent) {
        new Label(parent, SWT.NULL).text = "Type of processor executable:"
        processorExecutableType = new Combo(parent, SWT.DROP_DOWN.bitwiseOr(SWT.READ_ONLY))
        processorExecutableType.layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)
        ProcessorExecutableType.values
            .map[ProcessorExecutableTypesInformationManager.label(it)]
            .forEach[processorExecutableType.add(it)]
        processorExecutableType.addSelectionListener(new SelectionListener() {
            override widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e)
            }

            override widgetSelected(SelectionEvent e) {
                toggleProcessorExecutablePathDecorator()
                if (!resetBasicProcessorExecutionCommandOnExecutableTypeSwitch()) {
                    // Dirty hack to programmatically trigger the validation of the
                    // processorBasicExecutionCommand field in case its value was not reset to a
                    // default processor execution command before. The validation triggering allows
                    // validators for processor execution commands to take the executable type into
                    // account. For example, processing configurations with a Docker executable type
                    // require the specification of a processor execution command.
                    // This call cannot happen in propertyChange() as triggerValidation() changes
                    // the value of the text field which again triggers propertyChange() resulting
                    // in an infinite chain of propertyChange() calls.
                    triggerValidation(processorBasicExecutionCommand)
                }
            }
        })
    }

    private def addPathToProcessorExecutable(Composite parent) {
        new Label(parent, SWT.NULL).text = "Path to processor executable:"
        processorExecutablePath = new Text(parent, SWT.BORDER)
        val layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)
        layoutData.widthHint = 50
        processorExecutablePath.layoutData = layoutData

        processorExecutablePathDecorator = new ControlDecoration(processorExecutablePath,
            SWT.TOP.bitwiseOr(SWT.RIGHT))
        processorExecutablePathDecorator.descriptionText = "Click to receive input support for " +
            "the selected type of the processor executable"
        processorExecutablePathDecorator.image = FieldDecorationRegistry.^default
            .getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).image
        processorExecutablePathDecorator.addSelectionListener(new SelectionListener() {
            override widgetDefaultSelected(SelectionEvent e) {
                widgetDefaultSelected(e)
            }

            override widgetSelected(SelectionEvent e) {
                val selectedInput = try {
                    ProcessorExecutableTypesInformationManager
                    .inputSupportFunction(currentConfiguration.processorExecutableType)
                    ?.apply(shell, currentConfiguration)
                } catch (IllegalArgumentException ex) {
                    MessageDialog.openError(shell, "Input Support Error", ex.message)
                    null
                }

                if (selectedInput !== null)
                    processorExecutablePath.text = selectedInput
            }
        })
    }

    private def addBasicProcessorExecutionCommand(Composite parent) {
        new Label(parent, SWT.NULL).text = "Basic processor execution command:"
        processorBasicExecutionCommand = new Text(parent, SWT.BORDER)
        processorBasicExecutionCommand.layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)
        val executionCommandHint = new ControlDecoration(processorBasicExecutionCommand,
            SWT.TOP.bitwiseOr(SWT.RIGHT))
        executionCommandHint.descriptionText = "The basic execution command to invoke the " +
            '''processor executable, e.g., "java" or "docker"'''
        executionCommandHint.image = FieldDecorationRegistry.^default
            .getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).image
    }

    override initializeFrom(ILaunchConfiguration launchConfiguration) {
        val configuration = ProcessingConfiguration.deserializeFrom(launchConfiguration)
            ?: prepareNewProcessingConfigurationInstance()

        setCurrentConfiguration(configuration)
        toggleProcessorExecutablePathDecorator()

        initializationDone = true
    }

    private def prepareNewProcessingConfigurationInstance() {
        return new ProcessingConfiguration(
            "",
            "",
            DEFAULT_PROCESSOR_EXECUTABLE_TYPE,
            "",
            SUPPORTED_DEFAULT_BASIC_PROCESSOR_EXECUTION_COMMANDS
                .get(DEFAULT_PROCESSOR_EXECUTABLE_TYPE)
        )
    }

    private def setCurrentConfiguration(ProcessingConfiguration configuration) {
        removeCurrentConfigurationBindings()
        currentConfiguration?.removePropertyChangeListener(this)

        originalConfiguration = configuration
        currentConfiguration = configuration.clone() as ProcessingConfiguration

        originalConfiguration.convertToUserRepresentation()
        currentConfiguration.convertToUserRepresentation()

        // TODO probably not necessary
        //constraintsTable.input = currentConfiguration?.constraints
        argumentsTable.removePropertyChangeListener(this)
        argumentsTable.input = currentConfiguration
        argumentsTable.addPropertyChangeListener(this)

        establishCurrentConfigurationBindings()
        currentConfiguration.addPropertyChangeListener(this)
    }

    private def removeCurrentConfigurationBindings() {
        dataBindingContext.bindings.forEach[
            dispose
            dataBindingContext.removeBinding(it)
        ]

        currentDecorations.forEach[it.dispose]
        currentDecorations.clear
    }

    private def establishCurrentConfigurationBindings() {
        CONFIGURATION_BINDERS.forEach[binder |
            val decoration = binder.get()
            if (decoration !== null)
                currentDecorations.add(decoration)
        ]
    }

    override propertyChange(PropertyChangeEvent event) {
        switch(event.propertyName) {
            case "sourceModelProjectName",
            case "sourceModelFilePath": {
                addSourceModelFileArgumentIfAllowed()
                // Update all intermediate model table items to render, e.g., errors of lacking
                // transformation support
                argumentsTable.updateArguments(AbstractIntermediateModelArgumentTypeWithEnumValue)
            }
            case "processorBasicExecutionCommand":
                triggerValidation(processorExecutablePath)
        }

        updateLaunchConfigurationDialog()
    }

    private def addSourceModelFileArgumentIfAllowed() {
        if (!initializationDone ||
            sourceModelArgumentWasAddedOnce ||
            argumentsTable.containsArgument(DEFAULT_SOURCE_MODEL_FILE_ARGUMENT)) {
            return
        }

        try {
            ProcessingConfiguration.validSourceModelFilePath(
                currentConfiguration.sourceModelProjectName,
                currentConfiguration.sourceModelFilePath
            )
        } catch (IllegalArgumentException ex) {
            return
        }

        if (ProgrammaticIntermediateModelTransformation
            .supportsTranformation(currentConfiguration.sourceModelFile)) {
            argumentsTable.addArgument(DEFAULT_SOURCE_MODEL_FILE_ARGUMENT, 0)
            sourceModelArgumentWasAddedOnce = true
        }
    }

    // TODO should be handled by Eclipse
    /*
    private def bindConfigurationName() {
        val target = WidgetProperties.text(SWT.Modify).observe(configurationName)
        val model = BeanProperties.value(ProcessingConfiguration, "name")
            .observe(currentConfiguration)
        //val updateStrategy = new UpdateValueStrategy<String, String>()
        /*updateStrategy.afterGetValidator = [value |
            return try {
                ProcessingConfiguration.validName(value)
                ValidationStatus.ok()
            } catch (IllegalArgumentException ex)
                ValidationStatus.error(ex.message)

            val existingConfiguration = configurations.get(value)
            val duplicateConfigurationName = existingConfiguration !== null &&
                existingConfiguration !== originalConfiguration
            return if (duplicateConfigurationName)
                ValidationStatus.error("Another configuration with this name already exists")
            else
                ValidationStatus.ok()
        ]
        //val bindValue = DATA_BINDING_CONTEXT.bindValue(target, model, updateStrategy, null)
        val bindValue = DATA_BINDING_CONTEXT.bindValue(target, model, null, null)
        val decoration = ControlDecorationSupport.create(bindValue, SWT.TOP.bitwiseOr(SWT.LEFT))
        return bindValue -> decoration
    }*/

    private def ControlDecoration bindSourceModelProjectName() {
        return sourceModelProjectName.bindWithValidationDecorationSupport(
            ProcessingConfiguration,
            "sourceModelProjectName",
            currentConfiguration,
            [ProcessingConfiguration.validSourceModelProjectName(it)]
        )
    }

    private def <T> bindWithValidationDecorationSupport(
        Text field,
        Class<T> beanClass,
        String propertyName,
        T source,
        (String)=>void validationProcedure
    ) {
        return bindWithValidationDecorationSupport(
            field,
            dataBindingContext,
            beanClass,
            propertyName,
            source,
            validationProcedure
        ).value
    }

    private def ControlDecoration bindSourceModelFilePath() {
        return sourceModelFilePath.bindWithValidationDecorationSupport(
            ProcessingConfiguration,
            "sourceModelFilePath",
            currentConfiguration,
            [
                ProcessingConfiguration.validSourceModelFilePath(
                    currentConfiguration.sourceModelProjectName,
                    it
                )
            ]
        )
    }

    private def ControlDecoration bindProcessorExecutableType() {
        val target = WidgetProperties.comboSelection.observe(processorExecutableType)
        val model = BeanProperties.value(ProcessingConfiguration, "processorExecutableType",
            ProcessorExecutableType).observe(currentConfiguration)

        val modelToTargetConverter = new UpdateValueStrategy<ProcessorExecutableType, String>()
        modelToTargetConverter.converter = IConverter.create(
            [execType | ProcessorExecutableTypesInformationManager.label(execType)]
        )

        val targetToModelConverter = new UpdateValueStrategy<String, ProcessorExecutableType>()
        targetToModelConverter.converter = IConverter.create(
            [
                label |
                // Make previous executable type accessible to
                // resetBasicProcessorExecutionCommandOnExecutableTypeSwitch() for determining if
                // the basic execution command shall be reset to a default value
                previousProcessorExecutableType = currentConfiguration.processorExecutableType
                ProcessorExecutableTypesInformationManager.literal(label)
            ]
        )

        dataBindingContext.bindValue(target, model, targetToModelConverter, modelToTargetConverter)
        return null
    }

    private def ControlDecoration bindProcessorBasicExecutionCommand() {
        return processorBasicExecutionCommand.bindWithValidationDecorationSupport(
            ProcessingConfiguration,
            "processorBasicExecutionCommand",
            currentConfiguration,
            [
                ProcessingConfiguration.validProcessorBasicExecutionCommand(
                    currentConfiguration.processorExecutableType,
                    it
                )
            ]
        )
    }

    private def ControlDecoration bindProcessorExecutablePath() {
        return processorExecutablePath.bindWithValidationDecorationSupport(
            ProcessingConfiguration,
            "processorExecutablePath",
            currentConfiguration,
            [
                ProcessingConfiguration.validProcessorExecutablePath(
                    currentConfiguration.processorExecutableType,
                    currentConfiguration.processorBasicExecutionCommand,
                    it
                )
            ]
        )
    }

    private def prepareInputFields() {
        // TODO probably not necessary
        /*configurationEditButton.enabled = configurationsSelection.selectionIndex > -1
        configurationRemoveButton.enabled = configurationsSelection.selectionIndex > -1
        configurationName.enabled = currentConfiguration !== null
        processorExecutableType.enabled = currentConfiguration !== null
        processorExecutablePath.enabled = currentConfiguration !== null*/
        //toggleProcessorExecutablePathDecorator()
        // TODO
        /*constraintsTable.enabled = currentConfiguration !== null
        argumentsTable.enabled = currentConfiguration !== null*/

        // At the first call of prepareInputFields() during the createContents() callback, the
        // preview button will be null because PreferencePage invokes the contributeButtons()
        // callback after createContents() in PreferencePage.createControl()
        // TODO
        //previewButton?.setEnabled(currentConfiguration !== null)
    }

    private def toggleProcessorExecutablePathDecorator() {
        val executableTypeHasInputSupport = ProcessorExecutableTypesInformationManager
            .hasInputSupport(currentConfiguration.processorExecutableType)
        if (executableTypeHasInputSupport)
            processorExecutablePathDecorator.show()
        else
            processorExecutablePathDecorator.hide()
    }

    private def resetBasicProcessorExecutionCommandOnExecutableTypeSwitch() {
        val previousTypeDefaultCommand = SUPPORTED_DEFAULT_BASIC_PROCESSOR_EXECUTION_COMMANDS
            .get(previousProcessorExecutableType)
        val newTypeDefaultCommand = SUPPORTED_DEFAULT_BASIC_PROCESSOR_EXECUTION_COMMANDS
            .get(currentConfiguration.processorExecutableType)
        if (previousTypeDefaultCommand === null || newTypeDefaultCommand === null) {
            return false
        }

        // Don't overwrite the value in the basic execution command field if the user did not change
        // the default command for the previous processor executable type. In case the user already
        // entered a valid command but for the wrong processor executable type, we don't want her to
        // having to enter the execution command again.
        return
            if (previousTypeDefaultCommand == currentConfiguration.processorBasicExecutionCommand)
                currentConfiguration.setDefaultBasicProcessorExecutionCommand()
            else
                false
    }

    private def setDefaultBasicProcessorExecutionCommand(ProcessingConfiguration configuration) {
        val defaultCommand = SUPPORTED_DEFAULT_BASIC_PROCESSOR_EXECUTION_COMMANDS
            .get(configuration.processorExecutableType)
        if (defaultCommand === null)
            return false

        configuration.processorBasicExecutionCommand = defaultCommand
        return true
    }

    private def addConstraintsTable(Composite parent) {
        val tableGroup = new Group(parent, SWT.SHADOW_ETCHED_IN)
        tableGroup.text = "Constraints"
        tableGroup.layout = new GridLayout(1, false)

        val constraintLinkageHint = new ControlDecoration(tableGroup, SWT.TOP.bitwiseOr(SWT.RIGHT))
        constraintLinkageHint.descriptionText = "Target elements must adhere to at least one " +
            "constraint of each type for the configuration to be invokable on them"
        constraintLinkageHint.image = FieldDecorationRegistry.^default
            .getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).image

        val tableGroupData = new GridData()
        tableGroupData.grabExcessHorizontalSpace = true
        tableGroupData.grabExcessVerticalSpace = true
        tableGroupData.horizontalAlignment = GridData.FILL
        tableGroupData.verticalAlignment = GridData.FILL
        tableGroup.layoutData = tableGroupData

        // TODO probably not necessary
        /*constraintsTable = new ConstraintsTable(
            tableGroup,
            SUPPORTED_CONSTRAINT_TYPES,
            [newBinding | CURRENT_CONFIGURATION_BINDINGS.add(newBinding)],
            [removedBinding |
                removedBinding.dispose
                CURRENT_CONFIGURATION_BINDINGS.remove(removedBinding)
            ]
        )*/
    }

    private def addArgumentsTable(Composite parent) {
        val tableGroup = new Group(parent, SWT.SHADOW_ETCHED_IN)
        tableGroup.text = "Arguments"
        tableGroup.layout = new GridLayout(1, false)
        tableGroup.layoutData = new GridData(GridData.FILL, GridData.FILL, true, true)

        argumentsTable = new ArgumentsTable(tableGroup, SUPPORTED_ARGUMENT_KINDS)
    }

    override isValid(ILaunchConfiguration configuration) {
        return try {
            currentConfiguration.validateInUserRepresentation
            true
        } catch (IllegalArgumentException ex) {
            false
        }
    }

    /*override OkToLeaveTab() {
        return !erroneousInput()
    }*/

    /*override canSave() {
        return !erroneousInput()
    }*/

    override isDirty() {
        return originalConfiguration != currentConfiguration
    }

    private def erroneousInput() {
        //CURRENT_CONFIGURATION_BINDINGS.exists[validationStatus.value.severity == IStatus.ERROR] ||
        // We need to check elements, which are edited in tables, separately, because their bindings
        // get disposed by ObservableValueEditingSupport when cell editors are closed
    }

    // TODO probably not necessary
    /*private def erroneousConstraintInputs() {
        currentConfiguration.constraints !== null &&
        currentConfiguration.constraints.exists[
            try {
                type.checkValidValueInUserRepresentation(value)
                false
            } catch (IllegalArgumentException ex) {
                true
            }
        ]
    }*/

    override performApply(ILaunchConfigurationWorkingCopy launchConfiguration) {
        currentConfiguration.convertToInternalRepresentation()
        ProcessingConfiguration.setProcessingConfigurationAsAttribute(launchConfiguration,
            currentConfiguration)
        currentConfiguration.convertToUserRepresentation()
    }

    override getName() {
        return COMMON_LAUNCH_CONFIGURATION_TAB_NAME
    }

    override getImage() {
        return COMMON_LAUNCH_CONFIGURATION_TAB_IMAGE
    }

    override setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        // NOOP
    }

    override dispose() {
        argumentsTable.dispose
        dataBindingContext.dispose
        currentDecorations.forEach[it.dispose]
        super.dispose()
    }
}