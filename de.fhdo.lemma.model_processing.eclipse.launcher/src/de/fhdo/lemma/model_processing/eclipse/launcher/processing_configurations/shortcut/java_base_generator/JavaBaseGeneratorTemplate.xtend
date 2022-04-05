package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator

import org.eclipse.swt.widgets.Shell
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.widgets.Group
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.layout.GridData
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.ProcessingConfigurationWithLaunchConfigurationName

class JavaBaseGeneratorTemplate extends AbstractJavaBaseGeneratorTemplate {
    new(Shell parentShell, IProject project, IFile file) {
        this(parentShell, "Java microservice generation", project, file)
    }

    new(Shell parentShell, String name, IProject project, IFile file) {
        super(parentShell, name, project, file)
    }

    override getProcessorExecutableType() {
        return ProcessorExecutableType.LOCAL_JAVA_PROGRAM
    }

    override getJavaBaseGeneratorCompletionDialog() {
        return new TemplateCompletionDialog(parentShell, project, file)
    }

    static class TemplateCompletionDialog extends AbstractJavaBaseGeneratorCompletionDialog {
        GenletsTable genletsTable

        protected new(Shell parentShell, IProject project, IFile file) {
            super(parentShell, project, file)
        }

        override getProcessorExecutableLabelTextAddendum() {
            return "path"
        }

        override create() {
            super.create()
            title = "Generate Java Microservices From LEMMA Models"
            message = '''Use this dialog to configure and run LEMMA's «generatorLongName» on ''' +
                '''the selected «printableModelFileType» model'''
        }

        override createDialogArea(Composite parent) {
            val area = super.createDialogArea(parent)
            parent.shell.size = new Point(DEFAULT_WIDTH, 600)
            return area
        }

        final override addAdditionalControlsToDialog(Composite parent) {
            super.addAdditionalControlsToDialog(parent)

            parent.addAdditionalControlsBeforeGenletsGroup

            val genletsTableGroup = new Group(parent, SWT.SHADOW_ETCHED_IN)
            genletsTableGroup.text = "Genlets"
            genletsTableGroup.layout = new GridLayout(1, false)
            val layoutData = new GridData(GridData.FILL, GridData.FILL, true, true)
            genletsTableGroup.layoutData = layoutData
            layoutData.horizontalSpan = 3

            genletsTable = new GenletsTable(genletsTableGroup, configurationTemplate)
            genletsTable.addPropertyChangeListener(this)

            parent.addAdditionalControlsAfterGenletsGroup
        }

        def void addAdditionalControlsBeforeGenletsGroup(Composite parent) {
            // NOOP
        }

        def void addAdditionalControlsAfterGenletsGroup(Composite parent) {
            // NOOP
        }

        override completeProcessingConfigurationTemplate(
            ProcessingConfigurationWithLaunchConfigurationName templateToComplete
        ) {
            templateToComplete.arguments.addAll(genletsTable.genletArguments)
        }

        override close() {
            genletsTable.removePropertyChangeListener(this)
            genletsTable.dispose()
            super.close()
        }
    }
}