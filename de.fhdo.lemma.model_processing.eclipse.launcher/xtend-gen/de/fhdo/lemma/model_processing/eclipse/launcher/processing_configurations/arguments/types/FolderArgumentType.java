package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class FolderArgumentType extends AbstractArgumentType {
  public static final ArgumentTypeIdentifier IDENTIFIER = ArgumentTypeIdentifier.FOLDER;
  
  public FolderArgumentType() {
    super(FolderArgumentType.IDENTIFIER, "Folder");
  }
  
  @Override
  public void checkValidValue(final ProcessingConfiguration processingConfiguration, final String value) {
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(value);
    if (_isNullOrEmpty) {
      throw new IllegalArgumentException("Please select a folder");
    } else {
      boolean _isDirectory = Files.isDirectory(Paths.get(value));
      boolean _not = (!_isDirectory);
      if (_not) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Folder \"");
        _builder.append(value);
        _builder.append("\" does not exist");
        throw new IllegalArgumentException(_builder.toString());
      }
    }
  }
}
