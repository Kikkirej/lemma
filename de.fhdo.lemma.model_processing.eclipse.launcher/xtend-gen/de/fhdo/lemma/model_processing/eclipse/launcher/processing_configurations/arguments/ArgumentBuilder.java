package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.ArgumentKindFactory;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.ConstantParameterArgumentKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.MultiValuedParameterArgumentKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.SingleValuedParameterArgumentKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AllImportedIntermediateModelKinds;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ArgumentTypeFactory;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FileArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FolderArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ImportedIntermediateModelKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportWithAliasArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelsOfAllImportsArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.RawStringArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.SourceModelArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.SourceModelKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.StringPairArgumentType;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class ArgumentBuilder {
  public static class ParameterWithoutValueBuilder {
    private final Argument argument;
    
    private final ProcessingConfiguration processingConfiguration;
    
    public ParameterWithoutValueBuilder(final Argument argument, final ProcessingConfiguration processingConfiguration) {
      this.argument = argument;
      this.processingConfiguration = processingConfiguration;
    }
    
    public Argument parameter(final String parameter) {
      this.argument.setParameter(parameter);
      if ((this.processingConfiguration != null)) {
        ArgumentBuilder.checkValidity(this.argument, this.processingConfiguration);
      }
      return this.argument;
    }
  }
  
  public static class MissingProcessingConfigurationException extends Exception {
    public MissingProcessingConfigurationException(final String message) {
      super(message);
    }
  }
  
  public static class ParameterBuilder {
    private final Argument argument;
    
    private final ProcessingConfiguration processingConfiguration;
    
    public ParameterBuilder(final Argument argument, final ProcessingConfiguration processingConfiguration) {
      this.argument = argument;
      this.processingConfiguration = processingConfiguration;
    }
    
    public ArgumentBuilder.ValueBuilder parameter(final String parameter) {
      this.argument.setParameter(parameter);
      return new ArgumentBuilder.ValueBuilder(this.argument, this.processingConfiguration);
    }
  }
  
  public static class ValueBuilder {
    private final Argument argument;
    
    private final ProcessingConfiguration processingConfiguration;
    
    public ValueBuilder(final Argument argument, final ProcessingConfiguration processingConfiguration) {
      this.argument = argument;
      this.processingConfiguration = processingConfiguration;
    }
    
    public Argument value(final String value) {
      this.argument.setValue(value);
      if ((this.processingConfiguration != null)) {
        ArgumentBuilder.checkValidity(this.argument, this.processingConfiguration);
      }
      return this.argument;
    }
  }
  
  public static class SingleValuedTypeBuilder {
    private final Argument argument;
    
    private final ProcessingConfiguration processingConfiguration;
    
    public SingleValuedTypeBuilder(final Argument argument, final ProcessingConfiguration processingConfiguration) {
      this.argument = argument;
      this.processingConfiguration = processingConfiguration;
    }
    
    public ArgumentBuilder.ParameterBuilder file() {
      this.argument.setType(ArgumentTypeFactory.fromIdentifier(FileArgumentType.IDENTIFIER));
      return new ArgumentBuilder.ParameterBuilder(this.argument, this.processingConfiguration);
    }
    
    public ArgumentBuilder.ParameterBuilder folder() {
      this.argument.setType(ArgumentTypeFactory.fromIdentifier(FolderArgumentType.IDENTIFIER));
      return new ArgumentBuilder.ParameterBuilder(this.argument, this.processingConfiguration);
    }
    
    public ArgumentBuilder.ParameterWithoutValueBuilder intermediateModel() {
      AbstractArgumentType _fromIdentifier = ArgumentTypeFactory.fromIdentifier(IntermediateModelArgumentType.IDENTIFIER);
      final IntermediateModelArgumentType argumentType = ((IntermediateModelArgumentType) _fromIdentifier);
      this.argument.setType(argumentType);
      this.argument.setValue(argumentType.getValidLiteralStringValues().get(IntermediateModelKind.SELECTED_FILE));
      return new ArgumentBuilder.ParameterWithoutValueBuilder(this.argument, this.processingConfiguration);
    }
    
    public ArgumentBuilder.ParameterWithoutValueBuilder intermdiateModelOfImport(final ImportedIntermediateModelKind modelKind) {
      AbstractArgumentType _fromIdentifier = ArgumentTypeFactory.fromIdentifier(IntermediateModelOfImportArgumentType.IDENTIFIER);
      final IntermediateModelOfImportArgumentType argumentType = ((IntermediateModelOfImportArgumentType) _fromIdentifier);
      this.argument.setType(argumentType);
      this.argument.setValue(argumentType.getValidLiteralStringValues().get(modelKind));
      return new ArgumentBuilder.ParameterWithoutValueBuilder(this.argument, this.processingConfiguration);
    }
    
    public ArgumentBuilder.ParameterWithoutValueBuilder intermediateModelOfImportWithAlias(final String alias) {
      AbstractArgumentType _fromIdentifier = ArgumentTypeFactory.fromIdentifier(IntermediateModelOfImportWithAliasArgumentType.IDENTIFIER);
      final IntermediateModelOfImportWithAliasArgumentType argumentType = ((IntermediateModelOfImportWithAliasArgumentType) _fromIdentifier);
      this.argument.setType(argumentType);
      this.argument.setValue(alias);
      return new ArgumentBuilder.ParameterWithoutValueBuilder(this.argument, this.processingConfiguration);
    }
    
    public ArgumentBuilder.ParameterWithoutValueBuilder sourceModel() {
      AbstractArgumentType _fromIdentifier = ArgumentTypeFactory.fromIdentifier(SourceModelArgumentType.IDENTIFIER);
      final SourceModelArgumentType argumentType = ((SourceModelArgumentType) _fromIdentifier);
      this.argument.setType(argumentType);
      this.argument.setValue(argumentType.getValidLiteralStringValues().get(SourceModelKind.SELECTED_FILE));
      return new ArgumentBuilder.ParameterWithoutValueBuilder(this.argument, this.processingConfiguration);
    }
    
    public ArgumentBuilder.ParameterBuilder stringPair() {
      this.argument.setType(ArgumentTypeFactory.fromIdentifier(StringPairArgumentType.IDENTIFIER));
      return new ArgumentBuilder.ParameterBuilder(this.argument, this.processingConfiguration);
    }
  }
  
  private final Argument argument;
  
  private ProcessingConfiguration processingConfiguration;
  
  public ArgumentBuilder(final Argument argument) {
    this.argument = argument;
  }
  
  public ArgumentBuilder(final Argument argument, final ProcessingConfiguration processingConfiguration) {
    this(argument);
    this.processingConfiguration = processingConfiguration;
  }
  
  public ArgumentBuilder.ParameterWithoutValueBuilder constant() {
    this.argument.setKind(ArgumentKindFactory.fromIdentifier(ConstantParameterArgumentKind.IDENTIFIER));
    this.argument.setType(ArgumentTypeFactory.fromIdentifier(RawStringArgumentType.IDENTIFIER));
    return new ArgumentBuilder.ParameterWithoutValueBuilder(this.argument, this.processingConfiguration);
  }
  
  private static void checkValidity(final Argument argument, final ProcessingConfiguration processingConfiguration) {
    try {
      if ((processingConfiguration == null)) {
        throw new ArgumentBuilder.MissingProcessingConfigurationException(("Processing configuration must not " + 
          "be null for argument validity check"));
      }
      argument.getKind().checkValidArgumentType(argument.getType());
      argument.getType().checkValidValueInUserRepresentation(processingConfiguration, argument.getValue());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public ArgumentBuilder.ParameterWithoutValueBuilder multiValued(final AllImportedIntermediateModelKinds modelKinds) {
    this.argument.setKind(ArgumentKindFactory.fromIdentifier(MultiValuedParameterArgumentKind.IDENTIFIER));
    AbstractArgumentType _fromIdentifier = ArgumentTypeFactory.fromIdentifier(IntermediateModelsOfAllImportsArgumentType.IDENTIFIER);
    final IntermediateModelsOfAllImportsArgumentType argumentType = ((IntermediateModelsOfAllImportsArgumentType) _fromIdentifier);
    this.argument.setType(argumentType);
    this.argument.setValue(argumentType.getValidLiteralStringValues().get(modelKinds));
    return new ArgumentBuilder.ParameterWithoutValueBuilder(this.argument, this.processingConfiguration);
  }
  
  public ArgumentBuilder.SingleValuedTypeBuilder singleValued() {
    this.argument.setKind(ArgumentKindFactory.fromIdentifier(SingleValuedParameterArgumentKind.IDENTIFIER));
    return new ArgumentBuilder.SingleValuedTypeBuilder(this.argument, this.processingConfiguration);
  }
}
