package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types;

import java.util.Collections;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class IntermediateModelArgumentType extends AbstractIntermediateModelArgumentTypeWithEnumValue<IntermediateModelKind> {
  public static final ArgumentTypeIdentifier IDENTIFIER = ArgumentTypeIdentifier.INTERMEDIATE_MODEL;
  
  public IntermediateModelArgumentType() {
    super(IntermediateModelArgumentType.IDENTIFIER, "Intermediate model");
  }
  
  @Override
  public Map<IntermediateModelKind, String> getValidLiteralStringValues() {
    Pair<IntermediateModelKind, String> _mappedTo = Pair.<IntermediateModelKind, String>of(IntermediateModelKind.SELECTED_FILE, ("Intermediate representation of selected " + 
      "model file"));
    return Collections.<IntermediateModelKind, String>unmodifiableMap(CollectionLiterals.<IntermediateModelKind, String>newHashMap(_mappedTo));
  }
}
