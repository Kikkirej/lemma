package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator

import org.eclipse.swt.widgets.Shell
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportWithAliasArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.ProcessingConfigurationWithLaunchConfigurationName
import org.eclipse.emf.ecore.EObject
import java.util.Map

class TypeFocusedMappingModelJavaBaseGeneratorTemplate extends JavaBaseGeneratorTemplate {
    new(Shell parentShell, IProject project, IFile file) {
        super(parentShell, "Type-focused mapping model: Java microservice generation", project,
            file)
    }

    override getJavaBaseGeneratorCompletionDialog() {
        return new TypeFocusedMappingModelTemplateCompletionDialog(parentShell, project, file)
    }

    override isApplicable(EObject modelRoot, Map<String, String> technologyNamePerAlias) {
        return true
    }

    private static class TypeFocusedMappingModelTemplateCompletionDialog
        extends TemplateCompletionDialog {
        var Argument alternativeIntermediateServiceModelArgument

        protected new(Shell parentShell, IProject project, IFile file) {
            super(parentShell, project, file)
        }

        override createDialogArea(Composite parent) {
            val area = super.createDialogArea(parent)
            parent.shell.size = new Point(DEFAULT_WIDTH, 630)
            return area
        }

        override addAdditionalControlsBeforeGenletsGroup(Composite parent) {
            parent.addAlternativeIntermediateServiceModel
        }

        private def addAlternativeIntermediateServiceModel(Composite parent) {
            new Label(parent, SWT.NULL).text = "Service model:"
            val comboWrapper = new IntermediateModelOfImportWithAliasArgumentTypeComboWrapper(
                parent,
                SWT.DROP_DOWN.bitwiseOr(SWT.READ_ONLY),
                configurationTemplate,
                JavaBaseGeneratorParameters.ALTERNATIVE_INTERMEDIATE_SERVICE_MODEL_PARAMETER
            )

            val layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)
            layoutData.horizontalSpan = 2
            comboWrapper.layoutData = layoutData

            bindWithValidationDecorationSupport(
                comboWrapper.combo,
                Argument,
                "value",
                comboWrapper.argument,
                [
                    (comboWrapper.argument.type as IntermediateModelOfImportWithAliasArgumentType)
                        .checkValidValue(configurationTemplate, it)
                ]
            )

            alternativeIntermediateServiceModelArgument = comboWrapper.argument
            alternativeIntermediateServiceModelArgument.addPropertyChangeListener(this)
        }

        override completeProcessingConfigurationTemplate(
            ProcessingConfigurationWithLaunchConfigurationName templateToComplete
        ) {
            super.completeProcessingConfigurationTemplate(templateToComplete)
            templateToComplete.arguments.add(alternativeIntermediateServiceModelArgument)
        }

        override close() {
            alternativeIntermediateServiceModelArgument.removePropertyChangeListener(this)
            super.close()
        }
    }
}