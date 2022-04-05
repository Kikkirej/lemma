package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut

import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.swt.widgets.Shell
import org.eclipse.jface.dialogs.TitleAreaDialog
import org.eclipse.swt.widgets.Composite
import org.eclipse.jface.dialogs.IDialogConstants
import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.swt.widgets.Text

import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport
import org.eclipse.core.databinding.Binding
import org.eclipse.core.runtime.IStatus
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeEvent
import org.eclipse.swt.widgets.Button
import org.eclipse.jface.fieldassist.ControlDecoration
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.CommandLineGenerator
import org.eclipse.swt.widgets.Control
import java.util.Map
import org.eclipse.emf.ecore.EObject

abstract class AbstractLaunchConfigurationTemplate {
    val protected Shell parentShell
    val protected IProject project
    val protected IFile file

    @Accessors(PUBLIC_GETTER)
    val String name

    new(Shell parentShell, String name, IProject project, IFile file) {
        this.name = name
        this.parentShell = parentShell
        this.project = project
        this.file = file
    }

    def ProcessingConfigurationWithLaunchConfigurationName
    extendInitializedProcessingConfigurationTemplate(
        ProcessingConfigurationWithLaunchConfigurationName initializedConfiguration
    ) {
        // NOOP
    }

    abstract def Boolean isApplicable(EObject modelRoot, Map<String, String> technologyNamePerAlias)

    abstract def AbstractTemplateCompletionDialog getCompletionDialog()

    static abstract class AbstractTemplateCompletionDialog extends TitleAreaDialog
        implements PropertyChangeListener {
        protected val dataBindingContext = new DataBindingContext()
        protected val bindings = <Binding>newArrayList
        val decorations = <ControlDecoration>newArrayList
        var (ProcessingConfigurationWithLaunchConfigurationName)=>void runCallback
        var (ProcessingConfigurationWithLaunchConfigurationName)=>void storeCallback
        protected val IProject project
        protected val IFile file
        var Button showCommandLineButton
        var Button storeButton
        var Button runButton

        @Accessors(PROTECTED_GETTER)
        protected var ProcessingConfigurationWithLaunchConfigurationName configurationTemplate

        protected new(Shell parentShell, IProject project, IFile file) {
            super(parentShell)
            this.project = project
            this.file = file
            helpAvailable = false
        }

        final def setConfigurationTemplate(
            ProcessingConfigurationWithLaunchConfigurationName configurationTemplate
        ) {
            this.configurationTemplate = configurationTemplate
            this.configurationTemplate.addPropertyChangeListener(this)
        }

        final override propertyChange(PropertyChangeEvent event) {
            val completedConfiguration = configurationTemplate.clone
                as ProcessingConfigurationWithLaunchConfigurationName
            completeProcessingConfigurationTemplate(completedConfiguration)
            val configurationIsValid = completedConfiguration.isValid()
            showCommandLineButton.enabled = configurationIsValid
            storeButton.enabled = configurationIsValid
            runButton.enabled = configurationIsValid
            event.reactToPropertyChange
        }

        def void reactToPropertyChange(PropertyChangeEvent evt) {
            // NOOP
        }

        private def isValid(ProcessingConfigurationWithLaunchConfigurationName configuration) {
            return try {
                configuration.validateInUserRepresentation
                true
            } catch (IllegalArgumentException ex) {
                false
            }
        }

        final def setStoreCallback(
            (ProcessingConfigurationWithLaunchConfigurationName)=>void callback
        ) {
            this.storeCallback = callback
        }

        final def setRunCallback(
            (ProcessingConfigurationWithLaunchConfigurationName)=>void callback
        ) {
            this.runCallback = callback
        }

        override createButtonsForButtonBar(Composite parent) {
            createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false)
            showCommandLineButton = createButton(parent, IDialogConstants.DETAILS_ID,
                "Show Command Line", false)
            storeButton = createButton(parent, IDialogConstants.FINISH_ID, "Store Without Running",
                false)
            runButton = createButton(parent, IDialogConstants.OK_ID, "Run", true)
        }

        override buttonPressed(int buttonId) {
            switch (buttonId) {
                case IDialogConstants.DETAILS_ID: showCommandLinePressed()
                case IDialogConstants.FINISH_ID: storePressed()
                default: super.buttonPressed(buttonId)
            }
        }

        private def showCommandLinePressed() {
            // The commandline generator operates on a completed clone of the current configuration
            // template. This approach ensures that the configuration is complete but also that the
            // current configuration, which the user may want to adapt again, remains in its
            // current state.
            val completedTemplate = configurationTemplate.clone()
                as ProcessingConfigurationWithLaunchConfigurationName
            completeProcessingConfigurationTemplate(completedTemplate)
            val commandLineGenerator = new CommandLineGenerator(completedTemplate)

            val commandLineParts = try {
                    val partsAndWarnings = commandLineGenerator.generateCoherentCommandLineParts()
                    partsAndWarnings.value.forEach[
                        MessageDialog.openWarning(shell, "Warning during commandline generation",
                            it)
                    ]
                    partsAndWarnings.key
                } catch (Exception ex) {
                    MessageDialog.openError(shell, "Error during commandline generation",
                        ex.message)
                    return
                }

            val commandLineDialog = new ShowCommandLineDialog(shell,
                commandLineGenerator.toPrintableCommandLine(commandLineParts, "\n\t"))
            commandLineDialog.open()
        }

        private def storePressed() {
            try {
                completeProcessingConfigurationTemplate(configurationTemplate)
                storeCallback.apply(configurationTemplate)
                super.okPressed()
            } catch(Exception ex) {
                // NOOP
            }
        }

        override okPressed() {
            try {
                completeProcessingConfigurationTemplate(configurationTemplate)
                runCallback.apply(configurationTemplate)
                super.okPressed()
            } catch(Exception ex) {
                // NOOP
            }
        }

        def void completeProcessingConfigurationTemplate(
            ProcessingConfigurationWithLaunchConfigurationName templateToComplete
        ) {
            // NOOP
        }

        final protected def addConfigurationNameTextField(Composite parent, int columnSpan) {
            new Label(parent, SWT.NULL).text = "Launch configuration name:"

            val nameTextField = new Text(parent, SWT.BORDER)
            val layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)
            if (columnSpan > 0)
                layoutData.horizontalSpan = columnSpan
            nameTextField.layoutData = layoutData

            bindWithValidationDecorationSupport(
                nameTextField,
                ProcessingConfigurationWithLaunchConfigurationName,
                "launchConfigurationName",
                configurationTemplate,
                [
                    ProcessingConfigurationWithLaunchConfigurationName
                        .validLaunchConfigurationName(it)
                ]
            )
        }

        final protected def <T> void bindWithValidationDecorationSupport(
            Control control,
            Class<T> beanClass,
            String propertyName,
            T source,
            (String)=>void validationProcedure
        ) {
            val bindingAndDecoration = bindWithValidationDecorationSupport(
                control,
                dataBindingContext,
                beanClass,
                propertyName,
                source,
                validationProcedure
            )
            bindings.add(bindingAndDecoration.key)
            decorations.add(bindingAndDecoration.value)
        }

        override close() {
            bindings.forEach[it.dispose]
            bindings.clear
            dataBindingContext.dispose()
            decorations.forEach[it.dispose]
            decorations.clear
            configurationTemplate.removePropertyChangeListener(this)
            super.close()
        }

        override isResizable() {
            return true
        }
    }
}