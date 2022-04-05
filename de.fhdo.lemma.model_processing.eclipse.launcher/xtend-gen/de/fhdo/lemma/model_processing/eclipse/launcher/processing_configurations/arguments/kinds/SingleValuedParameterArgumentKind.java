package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ArgumentTypeFactory;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FileArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FolderArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportWithAliasArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.SourceModelArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.StringPairArgumentType;

@SuppressWarnings("all")
public class SingleValuedParameterArgumentKind extends AbstractArgumentKind {
  public static final ArgumentKindIdentifier IDENTIFIER = ArgumentKindIdentifier.SINGLE_VALUED_PARAMETER;
  
  public SingleValuedParameterArgumentKind() {
    super(
      SingleValuedParameterArgumentKind.IDENTIFIER, 
      "Single-Valued Parameter", 
      ArgumentTypeFactory.fromIdentifier(FileArgumentType.IDENTIFIER), 
      ArgumentTypeFactory.fromIdentifier(FolderArgumentType.IDENTIFIER), 
      ArgumentTypeFactory.fromIdentifier(IntermediateModelArgumentType.IDENTIFIER), 
      ArgumentTypeFactory.fromIdentifier(IntermediateModelOfImportArgumentType.IDENTIFIER), 
      ArgumentTypeFactory.fromIdentifier(IntermediateModelOfImportWithAliasArgumentType.IDENTIFIER), 
      ArgumentTypeFactory.fromIdentifier(SourceModelArgumentType.IDENTIFIER), 
      ArgumentTypeFactory.fromIdentifier(StringPairArgumentType.IDENTIFIER));
  }
}
