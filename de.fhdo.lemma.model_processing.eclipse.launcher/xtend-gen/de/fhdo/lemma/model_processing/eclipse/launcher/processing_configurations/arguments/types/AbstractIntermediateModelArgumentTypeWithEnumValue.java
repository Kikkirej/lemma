package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types;

import de.fhdo.lemma.eclipse.ui.ProgrammaticIntermediateModelTransformation;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public abstract class AbstractIntermediateModelArgumentTypeWithEnumValue<T extends Enum<?>> extends AbstractArgumentTypeWithEnumValueSelection<T> {
  public AbstractIntermediateModelArgumentTypeWithEnumValue(final ArgumentTypeIdentifier identifier, final String name) {
    super(identifier, name);
  }
  
  @Override
  public void checkValidValueInUserRepresentation(final ProcessingConfiguration configuration, final String value) {
    super.checkValidValueInUserRepresentation(configuration, value);
    final IFile sourceModelFile = configuration.getSourceModelFile();
    if ((sourceModelFile == null)) {
      return;
    }
    boolean _supportsTranformation = ProgrammaticIntermediateModelTransformation.supportsTranformation(sourceModelFile);
    boolean _not = (!_supportsTranformation);
    if (_not) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("kind *.");
      String _fileExtension = sourceModelFile.getFileExtension();
      _builder.append(_fileExtension);
      _builder.append(" is not supported");
      String _plus = ("Intermediate transformation of model files of " + _builder);
      throw new IllegalArgumentException(_plus);
    }
  }
}
