package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.zuul

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.AbstractCodeGeneratorTemplate
import org.eclipse.swt.widgets.Shell
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import org.eclipse.emf.ecore.EObject
import java.util.Map
import de.fhdo.lemma.operation.OperationModel
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*

abstract class AbstractZuulGeneratorTemplate extends AbstractCodeGeneratorTemplate {
    new(Shell parentShell, String name, IProject project, IFile file) {
        super(parentShell, name, project, file)
    }

    final override isApplicable(EObject modelRoot, Map<String, String> technologyNamePerAlias) {
        val technologyReferences = (modelRoot as OperationModel).infrastructureNodes
            .map[it.technologies]
            .flatten

        // Convert import paths to absolute paths as otherwise the below call to
        // TechnologyReference.getTechnology() won't be able to resolve the cross-reference to the
        // imported technology
        makeImportPathsAbsolute(modelRoot, file)

        return  technologyReferences.exists[
            val alias = it.name
            val technologyName = technologyNamePerAlias.get(alias)
            "zuul".equalsIgnoreCase(technologyName)
        ]
    }
}