package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types;

import java.util.Collections;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class SourceModelArgumentType extends AbstractArgumentTypeWithEnumValueSelection<SourceModelKind> {
  public static final ArgumentTypeIdentifier IDENTIFIER = ArgumentTypeIdentifier.SOURCE_MODEL;
  
  public SourceModelArgumentType() {
    super(SourceModelArgumentType.IDENTIFIER, "Source model");
  }
  
  @Override
  public Map<SourceModelKind, String> getValidLiteralStringValues() {
    Pair<SourceModelKind, String> _mappedTo = Pair.<SourceModelKind, String>of(SourceModelKind.SELECTED_FILE, "Selected model file");
    return Collections.<SourceModelKind, String>unmodifiableMap(CollectionLiterals.<SourceModelKind, String>newHashMap(_mappedTo));
  }
}
