package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.ArgumentKindFactory
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.ConstantParameterArgumentKind
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.RawStringArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ArgumentTypeFactory
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.MultiValuedParameterArgumentKind
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelsOfAllImportsArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.SingleValuedParameterArgumentKind
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FileArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.ArgumentBuilder.ParameterBuilder
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FolderArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelKind
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ImportedIntermediateModelKind
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AllImportedIntermediateModelKinds
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.SourceModelArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.SourceModelKind
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.StringPairArgumentType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportWithAliasArgumentType

class ArgumentBuilder {
    val Argument argument
    var ProcessingConfiguration processingConfiguration

    new(Argument argument) {
        this.argument = argument
    }

    new(Argument argument, ProcessingConfiguration processingConfiguration) {
        this(argument)
        this.processingConfiguration = processingConfiguration
    }

    def constant() {
        argument.kind = ArgumentKindFactory.fromIdentifier(ConstantParameterArgumentKind.IDENTIFIER)
        argument.type = ArgumentTypeFactory.fromIdentifier(RawStringArgumentType.IDENTIFIER)
        return new ParameterWithoutValueBuilder(argument, processingConfiguration)
    }

    static class ParameterWithoutValueBuilder {
        val Argument argument
        val ProcessingConfiguration processingConfiguration

        new(Argument argument, ProcessingConfiguration processingConfiguration) {
           this.argument = argument
           this.processingConfiguration = processingConfiguration
        }

        def parameter(String parameter) {
            argument.parameter = parameter
            if (processingConfiguration !== null)
                argument.checkValidity(processingConfiguration)
            return argument
        }
    }

    private static def checkValidity(Argument argument,
        ProcessingConfiguration processingConfiguration) {
        if (processingConfiguration === null)
            throw new MissingProcessingConfigurationException("Processing configuration must not " +
                "be null for argument validity check")
        argument.kind.checkValidArgumentType(argument.type)
        argument.type.checkValidValueInUserRepresentation(processingConfiguration, argument.value)
    }

    static class MissingProcessingConfigurationException extends Exception {
        new (String message) {
            super(message)
        }
    }

    def multiValued(AllImportedIntermediateModelKinds modelKinds) {
        argument.kind = ArgumentKindFactory
            .fromIdentifier(MultiValuedParameterArgumentKind.IDENTIFIER)
        val argumentType = ArgumentTypeFactory
            .fromIdentifier(IntermediateModelsOfAllImportsArgumentType.IDENTIFIER)
            as IntermediateModelsOfAllImportsArgumentType
        argument.type = argumentType
        argument.value = argumentType.validLiteralStringValues.get(modelKinds)
        return new ParameterWithoutValueBuilder(argument, processingConfiguration)
    }

    static class ParameterBuilder {
        val Argument argument
        val ProcessingConfiguration processingConfiguration

        new(Argument argument, ProcessingConfiguration processingConfiguration) {
           this.argument = argument
           this.processingConfiguration = processingConfiguration
        }

        def parameter(String parameter) {
            argument.parameter = parameter
            return new ValueBuilder(argument, processingConfiguration)
        }
    }

    static class ValueBuilder {
        val Argument argument
        val ProcessingConfiguration processingConfiguration

        new(Argument argument, ProcessingConfiguration processingConfiguration) {
           this.argument = argument
           this.processingConfiguration = processingConfiguration
        }

        def value(String value) {
            argument.value = value
            if (processingConfiguration !== null)
                argument.checkValidity(processingConfiguration)
            return argument
        }
    }

    def singleValued() {
        argument.kind = ArgumentKindFactory
            .fromIdentifier(SingleValuedParameterArgumentKind.IDENTIFIER)
        return new SingleValuedTypeBuilder(argument, processingConfiguration)
    }

    static class SingleValuedTypeBuilder {
        val Argument argument
        val ProcessingConfiguration processingConfiguration

        new(Argument argument, ProcessingConfiguration processingConfiguration) {
           this.argument = argument
           this.processingConfiguration = processingConfiguration
        }

        def file() {
            argument.type = ArgumentTypeFactory.fromIdentifier(FileArgumentType.IDENTIFIER)
            return new ParameterBuilder(argument, processingConfiguration)
        }

        def folder() {
            argument.type = ArgumentTypeFactory.fromIdentifier(FolderArgumentType.IDENTIFIER)
            return new ParameterBuilder(argument, processingConfiguration)
        }

        def intermediateModel() {
            val argumentType = ArgumentTypeFactory
                .fromIdentifier(IntermediateModelArgumentType.IDENTIFIER)
                as IntermediateModelArgumentType
            argument.type = argumentType
            argument.value = argumentType
                .validLiteralStringValues
                .get(IntermediateModelKind.SELECTED_FILE)
            return new ParameterWithoutValueBuilder(argument, processingConfiguration)
        }

        def intermdiateModelOfImport(ImportedIntermediateModelKind modelKind) {
            val argumentType = ArgumentTypeFactory
                .fromIdentifier(IntermediateModelOfImportArgumentType.IDENTIFIER)
                as IntermediateModelOfImportArgumentType
            argument.type = argumentType
            argument.value = argumentType
                .validLiteralStringValues
                .get(modelKind)
            return new ParameterWithoutValueBuilder(argument, processingConfiguration)
        }

        def intermediateModelOfImportWithAlias(String alias) {
            val argumentType = ArgumentTypeFactory
                .fromIdentifier(IntermediateModelOfImportWithAliasArgumentType.IDENTIFIER)
                as IntermediateModelOfImportWithAliasArgumentType
            argument.type = argumentType
            argument.value = alias
            return new ParameterWithoutValueBuilder(argument, processingConfiguration)
        }

        def sourceModel() {
            val argumentType = ArgumentTypeFactory
                .fromIdentifier(SourceModelArgumentType.IDENTIFIER)
                as SourceModelArgumentType
            argument.type = argumentType
            argument.value = argumentType
                .validLiteralStringValues
                .get(SourceModelKind.SELECTED_FILE)
            return new ParameterWithoutValueBuilder(argument, processingConfiguration)
        }

        def stringPair() {
            argument.type = ArgumentTypeFactory.fromIdentifier(StringPairArgumentType.IDENTIFIER)
            return new ParameterBuilder(argument, processingConfiguration)
        }
    }
}