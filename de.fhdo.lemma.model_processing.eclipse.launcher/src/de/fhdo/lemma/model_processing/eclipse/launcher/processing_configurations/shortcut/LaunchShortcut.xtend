package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut

import org.eclipse.jface.viewers.ISelection
import org.eclipse.ui.IEditorPart
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.core.runtime.Platform
import org.eclipse.core.resources.IFile
import org.eclipse.ui.part.FileEditorInput
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.jface.dialogs.TitleAreaDialog
import org.eclipse.swt.widgets.Shell
import java.util.List
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.layout.GridData
import org.eclipse.jface.dialogs.IDialogConstants
import org.eclipse.core.resources.IProject
import org.eclipse.debug.core.DebugPlugin
import org.eclipse.debug.ui.ILaunchShortcut2
import static de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants.PROCESSING_CONFIGURATION_LAUNCH_CONFIGURATION_TYPE
import org.eclipse.debug.ui.DebugUITools
import org.eclipse.debug.core.ILaunchManager
import org.eclipse.debug.core.ILaunchConfiguration
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator.JavaBaseGeneratorTemplate
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator.DockerJavaBaseGeneratorTemplate
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator.DockerTypeFocusedMappingModelJavaBaseGeneratorTemplate
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator.TypeFocusedMappingModelJavaBaseGeneratorTemplate
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.container_base_generator.ContainerBaseGeneratorTemplate
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.container_base_generator.DockerContainerBaseGeneratorTemplate
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils
import de.fhdo.lemma.service.ServiceModel
import de.fhdo.lemma.service.ImportType
import de.fhdo.lemma.utils.LemmaUtils
import de.fhdo.lemma.operation.OperationModel
import de.fhdo.lemma.technology.mapping.TechnologyMapping
import java.util.Map
import org.eclipse.emf.ecore.EObject
import de.fhdo.lemma.technology.Technology
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.mariadb.MariaDBGeneratorTemplate
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.mongodb.MongoDBGeneratorTemplate
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.mariadb.DockerMariaDBGeneratorTemplate
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.mongodb.DockerMongoDBGeneratorTemplate
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.eureka.EurekaGeneratorTemplate
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.eureka.DockerEurekaGeneratorTemplate
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.zuul.ZuulGeneratorTemplate
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.zuul.DockerZuulGeneratorTemplate
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration

class LaunchShortcut implements ILaunchShortcut2 {
    static val LAUNCH_MANAGER = DebugPlugin.^default.launchManager
    static val SUPPORTED_LAUNCH_CONFIGURATION_TEMPLATES = #{
        "mapping" -> <Class<? extends AbstractLaunchConfigurationTemplate>>newArrayList(
                DockerJavaBaseGeneratorTemplate,
                DockerTypeFocusedMappingModelJavaBaseGeneratorTemplate,
                JavaBaseGeneratorTemplate,
                TypeFocusedMappingModelJavaBaseGeneratorTemplate
            ),
        "operation" -> <Class<? extends AbstractLaunchConfigurationTemplate>>newArrayList(
                ContainerBaseGeneratorTemplate,
                EurekaGeneratorTemplate,
                MariaDBGeneratorTemplate,
                MongoDBGeneratorTemplate,
                ZuulGeneratorTemplate,
                DockerContainerBaseGeneratorTemplate,
                DockerEurekaGeneratorTemplate,
                DockerMariaDBGeneratorTemplate,
                DockerMongoDBGeneratorTemplate,
                DockerZuulGeneratorTemplate
            ),
        "services" -> <Class<? extends AbstractLaunchConfigurationTemplate>>newArrayList(
                JavaBaseGeneratorTemplate,
                DockerJavaBaseGeneratorTemplate
            )
    }

    override getLaunchConfigurations(ISelection selection) {
        val file = getSelectedFile(selection)
        return if (file !== null)
                file.launchConfigurations
            else
                null
    }

    private def getLaunchConfigurations(IFile file) {
        return LAUNCH_MANAGER
            .getLaunchConfigurations(PROCESSING_CONFIGURATION_LAUNCH_CONFIGURATION_TYPE)
            .filter[
                it.mappedResources !== null &&
                it.mappedResources.exists[resource |
                    resource instanceof IFile && resource.fullPath == file.fullPath
                ]
            ]
    }

    override getLaunchableResource(ISelection selection) {
        return null
    }

    override launch(ISelection selection, String mode) {
        getSelectedFile(selection).launch(mode)
    }

    private def launch(IFile file, String mode) {
        if (file === null)
            return

        /*val launchConfigurations = file.launchConfigurations
        if (!launchConfigurations.empty) {
            launchConfigurations.get(0).launch(mode, new NullProgressMonitor())
            return
        }*/

        val project = file.projectOrError
        if (project === null)
            return

        val templates = file.launchConfigurationTemplates
        if (templates.empty) {
            MessageDialog.openInformation(getActiveShell(), "Quick launch not supported", "Quick " +
                '''launch for model files with extension "«file.rawLocation.fileExtension»" ''' +
                "currently not supported")
            return
        }

        new TemplateSelectionDialog(getActiveShell(), project, file, templates).open
    }

    private def getProjectOrError(IFile file) {
        return if (file.project !== null)
                file.project
            else {
                MessageDialog.openError(getActiveShell(), "Project not determinable", "Could not " +
                    '''determine project for model file "«file.rawLocation.makeAbsolute»". ''' +
                    "Model files must be part of a project in the current workspace to process " +
                    "them.")
                null
            }
    }

    private def getLaunchConfigurationTemplates(IFile file) {
        return SUPPORTED_LAUNCH_CONFIGURATION_TEMPLATES.get(file.rawLocation.fileExtension) ?: #[]
    }

    override getLaunchConfigurations(IEditorPart editor) {
        val file = getEditedFile(editor)
        return if (file !== null)
                file.launchConfigurations
            else
                null
    }

    override getLaunchableResource(IEditorPart editorpart) {
        return null
    }

    override launch(IEditorPart editor, String mode) {
        getEditedFile(editor).launch(mode)
    }

    private static class TemplateSelectionDialog extends TitleAreaDialog {
        val IProject project
        val IFile file
        val List<? extends AbstractLaunchConfigurationTemplate> allTemplates
        var List<? extends AbstractLaunchConfigurationTemplate> applicableTemplates
        var List<? extends AbstractLaunchConfigurationTemplate> currentTemplates
        var org.eclipse.swt.widgets.List templateSelectionList
        var Button continueButton

        new(
            Shell parentShell,
            IProject project,
            IFile file,
            List<Class<? extends AbstractLaunchConfigurationTemplate>> templateClasses
        ) {
            super(parentShell)
            helpAvailable = false

            this.project = project
            this.file = file
            allTemplates = templateClasses.map[it.instantiate].sortBy[it.name]
            currentTemplates = allTemplates
        }

        private def instantiate(
            Class<? extends AbstractLaunchConfigurationTemplate> templateClass
        ) {
            val constructor = templateClass.constructors.findFirst[
                it.parameterCount == 3 &&
                it.parameterTypes.get(0) == Shell &&
                it.parameterTypes.get(1) == IProject &&
                it.parameterTypes.get(2) == IFile
            ]

            if (constructor === null)
                throw new IllegalArgumentException("Launch configuration template of type " +
                    '''«templateClass.simpleName» not instantiable. Required constructor with ''' +
                    '''parameters of types «Shell.simpleName», «IProject.simpleName» and ''' +
                    '''«IFile.simpleName» not found.''')

            return constructor.newInstance(shell, project, file)
                as AbstractLaunchConfigurationTemplate
        }

        override create() {
            super.create()
            title = "Select quick launch configuration"
            message = "Please select a quick launch configuration from the list below"
        }

        override createDialogArea(Composite parent) {
            val area = super.createDialogArea(parent) as Composite
            val container = new Composite(area, SWT.NONE);
            container.layoutData = new GridData(SWT.FILL, SWT.FILL, true, true)
            container.layout = new GridLayout(1, false)

            container.addTemplateSelectionList
            container.addFilterNonApplicableTemplatesCheckbox

            return area
        }

        private def addTemplateSelectionList(Composite parent) {
            /*val container = new Composite(parent, SWT.NONE)
            container.layout = new GridLayout(1, false)
            container.layoutData = new GridData(SWT.FILL, SWT.FILL, true, true)*/

            templateSelectionList = new org.eclipse.swt.widgets.List(parent,
                SWT.BORDER.bitwiseOr(SWT.H_SCROLL).bitwiseOr(SWT.V_SCROLL))
            templateSelectionList.layoutData = new GridData(SWT.FILL, SWT.FILL, true, true)
            updateTemplateSelectionList()
        }

        private def updateTemplateSelectionList() {
            templateSelectionList.removeAll
            currentTemplates.forEach[templateSelectionList.add(it.name)]
        }

        private def addFilterNonApplicableTemplatesCheckbox(Composite parent) {
            val checkbox = new Button(parent, SWT.CHECK)
            checkbox.text = "Show only applicable templates"
            checkbox.addSelectionListener(new SelectionAdapter() {
                override widgetSelected(SelectionEvent event) {
                    val button = event.source as Button
                    if (button.selection) {
                        if (applicableTemplates === null)
                            applicableTemplates = getApplicableTemplates().toList

                        currentTemplates = applicableTemplates
                    } else
                        currentTemplates = allTemplates

                    updateTemplateSelectionList()
                    updateContinueButton()
                }
            })
        }

        private def getApplicableTemplates() {
            val modelResource = LemmaUiUtils.loadXtextResource(file)
            if (modelResource.contents.empty)
                return emptyList

            val modelRoot = modelResource.contents.get(0)
            val absoluteModelFilePath = file.rawLocation.makeAbsolute.toString

            return allTemplates.filter[
                it.isApplicable(
                    modelRoot,
                    modelRoot.getAbsoluteTechnologyModelImportPathPerAlias(absoluteModelFilePath)
                        .technologyNamePerAlias
            )]
        }

        private def getAbsoluteTechnologyModelImportPathPerAlias(EObject modelRoot,
            String absoluteModelPath) {
            val imports = try {
                modelRoot.imports
            } catch (IllegalArgumentException ex) {
                return emptyMap
            }

            return imports
                .filter[it.importType == ImportType.TECHNOLOGY]
                .toMap(
                    [it.name],
                    [LemmaUtils.convertToAbsolutePath(it.importURI, absoluteModelPath)]
                )
        }

        private dispatch def getImports(ServiceModel modelRoot) {
            return modelRoot.imports
        }

        private dispatch def getImports(TechnologyMapping modelRoot) {
            return modelRoot.imports
        }

        private dispatch def getImports(OperationModel modelRoot) {
            return modelRoot.imports
        }

        private dispatch def getImports(EObject modelRoot) {
            throw new IllegalArgumentException('''Model type «modelRoot.class.simpleName» does ''' +
                '''not support retrieval of imported models''')
        }

        private def getTechnologyNamePerAlias(
            Map<String, String> technologyModelImportPathPerAlias
        ) {
            val resultMap = <String, String>newHashMap

            for (entry : technologyModelImportPathPerAlias.entrySet) {
                val alias = entry.key
                val importPath = entry.value

                val technologyModelResource = LemmaUiUtils.loadXtextResource(importPath)
                val resourceContents = technologyModelResource.contents

                val technologyName = if (!resourceContents.empty &&
                    resourceContents.get(0) instanceof Technology)
                        (resourceContents.get(0) as Technology).name
                    else
                        null

                resultMap.put(alias, technologyName)
            }

            return resultMap
        }

        private def updateContinueButton() {
            continueButton.enabled = !currentTemplates.empty
        }

        override createButtonsForButtonBar(Composite parent) {
            continueButton = createButton(parent, IDialogConstants.OK_ID, "Continue", true)
            updateContinueButton()

            createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false)
        }

        override okPressed() {
            if (templateSelectionList.selectionIndex == -1) {
                MessageDialog.openError(getActiveShell(), "No configuration selected", "Please " +
                    "select a quick launch configuration from the list")
                return
            }

            val launchConfigurationTemplate = currentTemplates
                .get(templateSelectionList.selectionIndex)
            var processingConfigurationTemplate = initializeProcessingConfigurationTemplate()
            processingConfigurationTemplate = launchConfigurationTemplate
                .extendInitializedProcessingConfigurationTemplate(processingConfigurationTemplate)
            val completionDialog = launchConfigurationTemplate.completionDialog
            completionDialog.setConfigurationTemplate(processingConfigurationTemplate)
            completionDialog.storeCallback = [it.store]
            completionDialog.runCallback = [it.storeAndRun]

            super.okPressed
            completionDialog.open()
        }

        private def initializeProcessingConfigurationTemplate() {
            val configuration = new ProcessingConfigurationWithLaunchConfigurationName
            configuration.sourceModelProjectName = project.name
            configuration.sourceModelFilePath = file.projectRelativePath.toString
            return configuration
        }

        private def storeAndRun(
            ProcessingConfigurationWithLaunchConfigurationName completedProcessingConfiguration
        ) {
            DebugUITools.launch(completedProcessingConfiguration.store, ILaunchManager.RUN_MODE)
        }

        private def store(
            ProcessingConfigurationWithLaunchConfigurationName completedProcessingConfiguration
        ) {
            try {
                completedProcessingConfiguration.validateInUserRepresentation
            } catch(IllegalArgumentException ex) {
                MessageDialog.openError(getActiveShell(), "Erroneous processing configuration",
                    '''Processing configuration is erroneous: «ex.message». Execution not ''' +
                    "possible.")
                throw ex
            }

            val newLaunchConfigurationWorkingCopy
                = PROCESSING_CONFIGURATION_LAUNCH_CONFIGURATION_TYPE.newInstance(null,
                    completedProcessingConfiguration.launchConfigurationName)
            completedProcessingConfiguration.convertToInternalRepresentation()
            ProcessingConfiguration.setProcessingConfigurationAsAttribute(
                newLaunchConfigurationWorkingCopy,
                completedProcessingConfiguration
            )
            return newLaunchConfigurationWorkingCopy
                .doSave(ILaunchConfiguration.UPDATE_PROTOTYPE_CHILDREN)
        }

        override isResizable() {
            return true
        }
    }
}