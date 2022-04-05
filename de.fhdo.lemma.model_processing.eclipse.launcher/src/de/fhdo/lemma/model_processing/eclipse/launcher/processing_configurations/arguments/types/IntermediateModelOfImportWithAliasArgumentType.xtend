package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import de.fhdo.lemma.data.DataModel
import de.fhdo.lemma.service.ServiceModel
import de.fhdo.lemma.operation.OperationModel

class IntermediateModelOfImportWithAliasArgumentType extends AbstractArgumentType {
    public static val IDENTIFIER = ArgumentTypeIdentifier.INTERMEDIATE_MODEL_WITH_IMPORT_ALIAS
    static val transformableModelTypes = #[DataModel, ServiceModel, OperationModel]

    new() {
        super(IDENTIFIER, "Intermediate model of import with alias")
    }

    override checkValidValue(ProcessingConfiguration processingConfiguration, String value) {
        val imports = processingConfiguration.parseTransformableImportedModelsOfSourceModelFile
        if (!imports.exists[it.value.alias == value])
            throw new IllegalArgumentException("Source model does not import model with alias " +
                '''"«value»"''')
    }

    def parseTransformableImportedModelsOfSourceModelFile(
        ProcessingConfiguration processingConfiguration
    ) {
        val sourceModelFile = processingConfiguration.sourceModelFile
        if (sourceModelFile === null)
            throw new IllegalArgumentException("Please specify an existing source model file")

        val imports = parseImports(sourceModelFile)
            .filter[transformableModelTypes.contains(it.key)]
            .toList
        if (imports.empty)
            throw new IllegalArgumentException("Source model does not import other models")

        return imports
    }
}