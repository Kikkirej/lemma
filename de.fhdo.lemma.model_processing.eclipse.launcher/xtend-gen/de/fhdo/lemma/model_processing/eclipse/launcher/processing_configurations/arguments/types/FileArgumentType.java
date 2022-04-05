package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types;

import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class FileArgumentType extends AbstractArgumentType {
  public static final ArgumentTypeIdentifier IDENTIFIER = ArgumentTypeIdentifier.FILE;
  
  public FileArgumentType() {
    super(FileArgumentType.IDENTIFIER, "File");
  }
  
  @Override
  public void checkValidValue(final ProcessingConfiguration processingConfiguration, final String value) {
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(value);
    if (_isNullOrEmpty) {
      throw new IllegalArgumentException("Please select a file");
    } else {
      boolean _isRegularFile = Files.isRegularFile(Paths.get(value));
      boolean _not = (!_isRegularFile);
      if (_not) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("File \"");
        _builder.append(value);
        _builder.append("\" does not exist");
        throw new IllegalArgumentException(_builder.toString());
      }
    }
  }
}
