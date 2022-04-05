package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator

import org.eclipse.swt.widgets.Shell
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.ProcessingConfigurationWithLaunchConfigurationName
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.AbstractCodeGeneratorTemplate
import org.eclipse.emf.ecore.EObject
import java.util.Map
import de.fhdo.lemma.service.ServiceModel
import de.fhdo.lemma.technology.mapping.TechnologyMapping
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*

abstract class AbstractJavaBaseGeneratorTemplate extends AbstractCodeGeneratorTemplate {
    var Argument generationSerializerArgument

    new(Shell parentShell, String name, IProject project, IFile file) {
        super(parentShell, name, project, file)
    }

    override extendInitializedProcessingConfigurationTemplate(
        ProcessingConfigurationWithLaunchConfigurationName configuration
    ) {
        super.extendInitializedProcessingConfigurationTemplate(configuration)

        val arguments = configuration.arguments
        /*arguments.add(
            Argument.newArgument.singleValued.stringPair.parameter("code_generation").value("main")
        )*/
        generationSerializerArgument = Argument.newArgument
            .singleValued
            .stringPair
            .parameter('''--«JavaBaseGeneratorParameters.GENERATION_SERIALIZER_PARAMETER»''')
            .value(JavaBaseGeneratorParameters.instance.defaultGenerationSerializer)
        configuration.arguments.add(generationSerializerArgument)

        return configuration
    }

    final override getCodeGeneratorCompletionDialog() {
        val dialog = getJavaBaseGeneratorCompletionDialog()
        dialog.generationSerializerArgument = generationSerializerArgument
        return dialog
    }

    abstract def AbstractJavaBaseGeneratorCompletionDialog getJavaBaseGeneratorCompletionDialog()

    override isApplicable(EObject modelRoot, Map<String, String> technologyNamePerAlias) {
        val technologyReferences = switch (modelRoot) {
            ServiceModel: modelRoot.microservices.map[it.technologyReferences].flatten
            TechnologyMapping: modelRoot.serviceMappings.map[it.technologyReferences].flatten
            default: emptyList
        }

        // Convert import paths to absolute paths as otherwise the below call to
        // TechnologyReference.getTechnology() won't be able to resolve the cross-reference to the
        // imported technology
        makeImportPathsAbsolute(modelRoot, file)

        return technologyReferences.exists[
            val alias = it.technology.name
            val technologyName = technologyNamePerAlias.get(alias)
            "java".equalsIgnoreCase(technologyName)
        ]
    }
}