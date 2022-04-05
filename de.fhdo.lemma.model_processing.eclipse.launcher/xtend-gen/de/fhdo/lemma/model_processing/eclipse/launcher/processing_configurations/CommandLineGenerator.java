package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fhdo.lemma.eclipse.ui.AbstractUiModelTransformationStrategy;
import de.fhdo.lemma.eclipse.ui.ModelFile;
import de.fhdo.lemma.eclipse.ui.OperationModelTransformationStrategy;
import de.fhdo.lemma.eclipse.ui.ProgrammaticIntermediateModelTransformation;
import de.fhdo.lemma.eclipse.ui.ServiceModelTransformationStrategy;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractIntermediateModelArgumentTypeWithEnumValue;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AllImportedIntermediateModelKinds;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ArgumentTypeIdentifier;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FileArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FolderArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.ImportedIntermediateModelKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportWithAliasArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelsOfAllImportsArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.RawStringArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.SourceModelArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.StringPairArgumentType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class CommandLineGenerator {
  public static class CoherentCommandLineParts {
    private final ArrayList<List<String>> executionCommandParts = CollectionLiterals.<List<String>>newArrayList();
    
    private final ArrayList<List<String>> argumentParameters = CollectionLiterals.<List<String>>newArrayList();
    
    protected boolean addExecutionCommandPart(final String part) {
      return this.addExecutionCommandPart(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(part)));
    }
    
    protected boolean addExecutionCommandPart(final List<String> part) {
      return this.executionCommandParts.add(part);
    }
    
    protected boolean addExecutionCommandParts(final List<List<String>> parts) {
      return this.executionCommandParts.addAll(parts);
    }
    
    protected boolean addArgumentParameterParts(final List<List<String>> parts) {
      return this.argumentParameters.addAll(parts);
    }
    
    protected List<List<String>> mergeParts() {
      final ArrayList<List<String>> result = CollectionLiterals.<List<String>>newArrayList();
      result.addAll(this.executionCommandParts);
      result.addAll(this.argumentParameters);
      return Collections.<List<String>>unmodifiableList(result);
    }
    
    protected List<String> mergeAndFlattenParts() {
      return Collections.<String>unmodifiableList(IterableExtensions.<String>toList(Iterables.<String>concat(this.mergeParts())));
    }
  }
  
  private static class CoherentParameterValuePairs {
    private final ArrayList<Pair<String, String>> coherentPairs = CollectionLiterals.<Pair<String, String>>newArrayList();
    
    public CoherentParameterValuePairs() {
    }
    
    public CoherentParameterValuePairs(final Argument argument) {
      this.add(argument);
    }
    
    private String getForCommandLine(final String parameter) {
      String _elvis = null;
      if (parameter != null) {
        _elvis = parameter;
      } else {
        _elvis = "<NO PARAMETER GIVEN>";
      }
      return _elvis;
    }
    
    public CoherentParameterValuePairs(final Argument argument, final String value) {
      this.add(argument, value);
    }
    
    public boolean add(final Argument argument) {
      return this.add(argument, null);
    }
    
    public boolean add(final Argument argument, final String value) {
      return this.add(argument.getParameter(), value);
    }
    
    public boolean add(final String parameter, final String value) {
      String _forCommandLine = this.getForCommandLine(parameter);
      Pair<String, String> _mappedTo = Pair.<String, String>of(_forCommandLine, value);
      return this.coherentPairs.add(_mappedTo);
    }
    
    public List<List<String>> unfoldWithoutNullValues() {
      final Function1<Pair<String, String>, List<String>> _function = (Pair<String, String> it) -> {
        List<String> _xifexpression = null;
        String _value = it.getValue();
        boolean _tripleNotEquals = (_value != null);
        if (_tripleNotEquals) {
          String _key = it.getKey();
          String _value_1 = it.getValue();
          _xifexpression = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_key, _value_1));
        } else {
          String _key_1 = it.getKey();
          _xifexpression = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_key_1));
        }
        return _xifexpression;
      };
      return ListExtensions.<Pair<String, String>, List<String>>map(this.coherentPairs, _function);
    }
  }
  
  private static class UserInfoNotDeterminable extends Exception {
    public UserInfoNotDeterminable(final String message) {
      super(message);
    }
  }
  
  private static final String NO_VALUE_STRING = "<NO VALUE GIVEN>";
  
  private final ProcessingConfiguration configuration;
  
  private final ProgrammaticIntermediateModelTransformation silentTransformation;
  
  private final String domainModelFileTypeId;
  
  private final String serviceModelFileTypeId;
  
  private final String operationModelFileTypeId;
  
  private final ArrayList<String> collectedWarnings = CollectionLiterals.<String>newArrayList();
  
  public CommandLineGenerator(final ProcessingConfiguration configuration) {
    this.configuration = configuration;
    this.silentTransformation = this.getSilentTransformationFor(configuration);
    this.domainModelFileTypeId = ServiceModelTransformationStrategy.DATA_MODEL_FILE_TYPE_ID;
    this.operationModelFileTypeId = OperationModelTransformationStrategy.OPERATION_MODEL_FILE_TYPE_ID;
    AbstractUiModelTransformationStrategy _strategy = null;
    if (this.silentTransformation!=null) {
      _strategy=this.silentTransformation.getStrategy();
    }
    final AbstractUiModelTransformationStrategy transformationStrategy = _strategy;
    String _xifexpression = null;
    if ((transformationStrategy != null)) {
      String _switchResult = null;
      boolean _matched = false;
      if (transformationStrategy instanceof ServiceModelTransformationStrategy) {
        _matched=true;
        _switchResult = ServiceModelTransformationStrategy.SERVICE_MODEL_FILE_TYPE_ID;
      }
      if (!_matched) {
        if (transformationStrategy instanceof OperationModelTransformationStrategy) {
          _matched=true;
          _switchResult = OperationModelTransformationStrategy.SERVICE_MODEL_FILE_TYPE_ID;
        }
      }
      if (!_matched) {
        String _simpleName = transformationStrategy.getClass().getSimpleName();
        String _plus = (("Service model file type identifier " + 
          "not determinable for intermediate transformation strategy ") + _simpleName);
        throw new IllegalArgumentException(_plus);
      }
      _xifexpression = _switchResult;
    } else {
      _xifexpression = null;
    }
    this.serviceModelFileTypeId = _xifexpression;
  }
  
  private ProgrammaticIntermediateModelTransformation getSilentTransformationFor(final ProcessingConfiguration configuration) {
    final Function1<Argument, Boolean> _function = (Argument it) -> {
      return Boolean.valueOf(AbstractIntermediateModelArgumentTypeWithEnumValue.class.isAssignableFrom(it.getType().getClass()));
    };
    final boolean intermediateTransformatioRequired = IterableExtensions.<Argument>exists(configuration.getArguments(), _function);
    if ((!intermediateTransformatioRequired)) {
      return null;
    } else {
      IFile _sourceModelFile = configuration.getSourceModelFile();
      boolean _tripleEquals = (_sourceModelFile == null);
      if (_tripleEquals) {
        throw new IllegalArgumentException((("Processing configuration specifies arguments " + 
          "requiring intermediate model representations but no source model file for ") + 
          "intermediate transformations was given"));
      }
    }
    IFile _sourceModelFile_1 = configuration.getSourceModelFile();
    return new ProgrammaticIntermediateModelTransformation(_sourceModelFile_1);
  }
  
  public String toPrintableCommandLine(final CommandLineGenerator.CoherentCommandLineParts coherentCommandLineParts, final String delimiter) {
    final Function1<List<String>, String> _function = (List<String> it) -> {
      return IterableExtensions.join(it, " ");
    };
    return IterableExtensions.join(ListExtensions.<List<String>, String>map(coherentCommandLineParts.mergeParts(), _function), delimiter);
  }
  
  public List<String> toExecutableCommandLine(final CommandLineGenerator.CoherentCommandLineParts coherentCommandLineParts) {
    final Function1<String, String> _function = (String it) -> {
      return this.removeSurroundingQuotes(it);
    };
    return IterableExtensions.<String>toList(ListExtensions.<String, String>map(coherentCommandLineParts.mergeAndFlattenParts(), _function));
  }
  
  private String removeSurroundingQuotes(final String s) {
    String _xifexpression = null;
    if ((s.startsWith("\"") && s.endsWith("\""))) {
      int _length = s.length();
      int _minus = (_length - 1);
      _xifexpression = s.substring(1, _minus);
    } else {
      _xifexpression = s;
    }
    return _xifexpression;
  }
  
  public Pair<CommandLineGenerator.CoherentCommandLineParts, List<String>> generateCoherentCommandLineParts() {
    this.collectedWarnings.clear();
    final CommandLineGenerator.CoherentCommandLineParts commandLineParts = new CommandLineGenerator.CoherentCommandLineParts();
    String _xifexpression = null;
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(this.configuration.getProcessorExecutablePath());
    boolean _not = (!_isNullOrEmpty);
    if (_not) {
      _xifexpression = this.quoteIfContainsSpaces(this.configuration.getProcessorExecutablePath());
    } else {
      String _xifexpression_1 = null;
      ProcessorExecutableType _processorExecutableType = this.configuration.getProcessorExecutableType();
      boolean _notEquals = (!Objects.equal(_processorExecutableType, 
        ProcessorExecutableType.RAW_EXECUTABLE));
      if (_notEquals) {
        _xifexpression_1 = "<NO PROCESSOR EXECUTABLE PATH SPECIFIED>";
      } else {
        _xifexpression_1 = "";
      }
      _xifexpression = _xifexpression_1;
    }
    final String processorExecutablePath = _xifexpression;
    final Pair<List<String>, List<List<String>>> executionCommandParts = this.generateExecutionCommandParts(this.configuration);
    commandLineParts.addExecutionCommandPart(executionCommandParts.getKey());
    commandLineParts.addExecutionCommandParts(executionCommandParts.getValue());
    commandLineParts.addExecutionCommandPart(processorExecutablePath);
    final HashMap<Argument, CommandLineGenerator.CoherentParameterValuePairs> argumentValues = this.determineArgumentValues();
    final Consumer<Argument> _function = (Argument argument) -> {
      final CommandLineGenerator.CoherentParameterValuePairs argumentValuePairs = argumentValues.get(argument);
      if ((argumentValuePairs != null)) {
        final List<List<String>> argumentParameterParts = argumentValuePairs.unfoldWithoutNullValues();
        commandLineParts.addArgumentParameterParts(argumentParameterParts);
      }
    };
    this.configuration.getArguments().forEach(_function);
    return Pair.<CommandLineGenerator.CoherentCommandLineParts, List<String>>of(commandLineParts, this.collectedWarnings);
  }
  
  private Pair<List<String>, List<List<String>>> generateExecutionCommandParts(final ProcessingConfiguration configuration) {
    String _processorBasicExecutionCommand = configuration.getProcessorBasicExecutionCommand();
    String _trim = null;
    if (_processorBasicExecutionCommand!=null) {
      _trim=_processorBasicExecutionCommand.trim();
    }
    String _quoteIfContainsSpaces = null;
    if (_trim!=null) {
      _quoteIfContainsSpaces=this.quoteIfContainsSpaces(_trim);
    }
    final String command = _quoteIfContainsSpaces;
    if ((command == null)) {
      List<List<String>> _emptyList = CollectionLiterals.<List<String>>emptyList();
      return Pair.<List<String>, List<List<String>>>of(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("<NO BASIC EXECUTION COMMAND SPECIFIED>")), _emptyList);
    }
    Pair<List<String>, List<List<String>>> _switchResult = null;
    ProcessorExecutableType _processorExecutableType = configuration.getProcessorExecutableType();
    if (_processorExecutableType != null) {
      switch (_processorExecutableType) {
        case LOCAL_DOCKER_IMAGE:
          _switchResult = this.generateDockerExecutionCommandParts(configuration, command);
          break;
        case LOCAL_JAVA_PROGRAM:
          ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(command, 
            "--add-opens", 
            "java.base/java.lang=ALL-UNNAMED", 
            "-jar");
          List<List<String>> _emptyList_1 = CollectionLiterals.<List<String>>emptyList();
          _switchResult = Pair.<List<String>, List<List<String>>>of(_newArrayList, _emptyList_1);
          break;
        case RAW_EXECUTABLE:
          ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(command);
          List<List<String>> _emptyList_2 = CollectionLiterals.<List<String>>emptyList();
          _switchResult = Pair.<List<String>, List<List<String>>>of(_newArrayList_1, _emptyList_2);
          break;
        default:
          break;
      }
    }
    return _switchResult;
  }
  
  private Pair<List<String>, List<List<String>>> generateDockerExecutionCommandParts(final ProcessingConfiguration configuration, final String command) {
    final ArrayList<String> commandParts = CollectionLiterals.<String>newArrayList(command, "run", "-i");
    final ArrayList<List<String>> additionalCommandParameters = CollectionLiterals.<List<String>>newArrayList();
    try {
      final String userId = this.getUserInfo("id -u", "User ID");
      final String groupId = this.getUserInfo("id -g", "User group ID");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(userId);
      _builder.append(":");
      _builder.append(groupId);
      additionalCommandParameters.add(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("-u", _builder.toString())));
    } catch (final Throwable _t) {
      if (_t instanceof CommandLineGenerator.UserInfoNotDeterminable) {
        final CommandLineGenerator.UserInfoNotDeterminable ex = (CommandLineGenerator.UserInfoNotDeterminable)_t;
        String message = ex.getMessage();
        boolean _endsWith = message.endsWith(".");
        boolean _not = (!_endsWith);
        if (_not) {
          message = (message + ".");
        }
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(message);
        _builder_1.append(" Docker container will run as \"root\".");
        this.collectedWarnings.add(_builder_1.toString());
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    final String projectPath = configuration.getSourceModelFile().getProject().getLocation().makeAbsolute().toString();
    final HashSet<String> volumePaths = CollectionLiterals.<String>newHashSet(projectPath);
    final Consumer<Argument> _function = (Argument it) -> {
      ArgumentTypeIdentifier _identifier = it.getType().getIdentifier();
      boolean _equals = Objects.equal(_identifier, FolderArgumentType.IDENTIFIER);
      if (_equals) {
        volumePaths.add(it.getValue());
      } else {
        ArgumentTypeIdentifier _identifier_1 = it.getType().getIdentifier();
        boolean _equals_1 = Objects.equal(_identifier_1, FileArgumentType.IDENTIFIER);
        if (_equals_1) {
          String _value = it.getValue();
          final String absolutePath = new File(_value).getAbsolutePath();
          volumePaths.add(FilenameUtils.getFullPathNoEndSeparator(absolutePath));
        }
      }
    };
    configuration.getArguments().forEach(_function);
    final Consumer<String> _function_1 = (String it) -> {
      this.addAsDockerVolumeParameter(additionalCommandParameters, it);
    };
    volumePaths.forEach(_function_1);
    return Pair.<List<String>, List<List<String>>>of(commandParts, additionalCommandParameters);
  }
  
  private boolean addAsDockerVolumeParameter(final List<List<String>> parameters, final String path) {
    boolean _xblockexpression = false;
    {
      String folder = path;
      final File fobj = new File(folder);
      boolean _isDirectory = fobj.isDirectory();
      boolean _not = (!_isDirectory);
      if (_not) {
        folder = FilenameUtils.getFullPathNoEndSeparator(fobj.getAbsolutePath());
      }
      final ArrayList<String> volumeParameterParts = CollectionLiterals.<String>newArrayList("-v");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(folder);
      _builder.append(":");
      _builder.append(folder);
      volumeParameterParts.add(_builder.toString());
      _xblockexpression = parameters.add(volumeParameterParts);
    }
    return _xblockexpression;
  }
  
  private String getUserInfo(final String command, final String printablePart) {
    try {
      Pair<Integer, String> _xtrycatchfinallyexpression = null;
      try {
        _xtrycatchfinallyexpression = Utils.executeShellCommandBlocking(command, 50, 4);
      } catch (final Throwable _t) {
        if (_t instanceof IOException) {
          final IOException ex = (IOException)_t;
          StringConcatenation _builder = new StringConcatenation();
          _builder.append(printablePart);
          _builder.append(" not determinable: ");
          String _message = ex.getMessage();
          _builder.append(_message);
          throw new CommandLineGenerator.UserInfoNotDeterminable(_builder.toString());
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      final Pair<Integer, String> userInfoResult = _xtrycatchfinallyexpression;
      final String userInfo = userInfoResult.getValue().trim();
      Integer _key = userInfoResult.getKey();
      boolean _notEquals = ((_key).intValue() != 0);
      if (_notEquals) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(printablePart);
        _builder.append(" not determinable. Execution ");
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("of \"");
        _builder_1.append(command);
        _builder_1.append("\" returned with exit code ");
        Integer _key_1 = userInfoResult.getKey();
        _builder_1.append(_key_1);
        _builder_1.append(": ");
        String _plus = (_builder.toString() + _builder_1);
        String _plus_1 = (_plus + userInfo);
        throw new CommandLineGenerator.UserInfoNotDeterminable(_plus_1);
      }
      return userInfo;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private String quoteIfContainsSpaces(final String s) {
    String _xifexpression = null;
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(s);
    if (_isNullOrEmpty) {
      _xifexpression = CommandLineGenerator.NO_VALUE_STRING;
    } else {
      String _xifexpression_1 = null;
      boolean _contains = s.contains(" ");
      if (_contains) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("\"");
        _builder.append(s);
        _builder.append("\"");
        _xifexpression_1 = _builder.toString();
      } else {
        _xifexpression_1 = s;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  private HashMap<Argument, CommandLineGenerator.CoherentParameterValuePairs> determineArgumentValues() {
    final HashMap<Argument, CommandLineGenerator.CoherentParameterValuePairs> argumentValues = CollectionLiterals.<Argument, CommandLineGenerator.CoherentParameterValuePairs>newHashMap();
    final Consumer<Argument> _function = (Argument argument) -> {
      CommandLineGenerator.CoherentParameterValuePairs _switchResult = null;
      AbstractArgumentType _type = argument.getType();
      boolean _matched = false;
      if (_type instanceof FileArgumentType || _type instanceof FolderArgumentType) {
        _matched=true;
        _switchResult = this.generateFileSystemArgumentValue(argument);
      }
      if (!_matched) {
        if (_type instanceof IntermediateModelArgumentType) {
          _matched=true;
          _switchResult = this.generateIntermediateModelArgumentValue(argument);
        }
      }
      if (!_matched) {
        if (_type instanceof IntermediateModelOfImportArgumentType) {
          _matched=true;
          _switchResult = this.generateIntermediateModelOfImportArgumentValue(argument);
        }
      }
      if (!_matched) {
        if (_type instanceof IntermediateModelsOfAllImportsArgumentType) {
          _matched=true;
          _switchResult = this.generateIntermediateModelsOfAllImportsArgumentValues(argument);
        }
      }
      if (!_matched) {
        if (_type instanceof IntermediateModelOfImportWithAliasArgumentType) {
          _matched=true;
          _switchResult = this.generateIntermediateModelOfImportWithAliasArgumentValue(argument);
        }
      }
      if (!_matched) {
        if (_type instanceof RawStringArgumentType) {
          _matched=true;
          _switchResult = this.generateRawStringArgumentValue(argument);
        }
      }
      if (!_matched) {
        if (_type instanceof SourceModelArgumentType) {
          _matched=true;
          _switchResult = this.generateSourceModelArgumentValue(argument);
        }
      }
      if (!_matched) {
        if (_type instanceof StringPairArgumentType) {
          _matched=true;
          _switchResult = this.generateStringPairArgumentValue(argument);
        }
      }
      if (!_matched) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("type ");
        String _name = argument.getType().getName();
        _builder.append(_name);
        _builder.append(" not supported");
        String _plus = ("Value determination for argument " + _builder);
        throw new IllegalArgumentException(_plus);
      }
      final CommandLineGenerator.CoherentParameterValuePairs argumentValuePairs = _switchResult;
      argumentValues.put(argument, argumentValuePairs);
    };
    this.configuration.getArguments().forEach(_function);
    return argumentValues;
  }
  
  private CommandLineGenerator.CoherentParameterValuePairs generateFileSystemArgumentValue(final Argument argument) {
    String _value = argument.getValue();
    String _quoteIfContainsSpaces = null;
    if (_value!=null) {
      _quoteIfContainsSpaces=this.quoteIfContainsSpaces(_value);
    }
    String _valueForCommandLine = null;
    if (_quoteIfContainsSpaces!=null) {
      _valueForCommandLine=this.getValueForCommandLine(_quoteIfContainsSpaces);
    }
    return new CommandLineGenerator.CoherentParameterValuePairs(argument, _valueForCommandLine);
  }
  
  private String getValueForCommandLine(final String value) {
    String _xifexpression = null;
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(value);
    boolean _not = (!_isNullOrEmpty);
    if (_not) {
      _xifexpression = value;
    } else {
      _xifexpression = CommandLineGenerator.NO_VALUE_STRING;
    }
    return _xifexpression;
  }
  
  private CommandLineGenerator.CoherentParameterValuePairs generateIntermediateModelArgumentValue(final Argument argument) {
    final String value = this.quoteIfContainsSpaces(this.getAbsoluteTransformationTargetPath(this.silentTransformation.getRootModelFile()));
    return new CommandLineGenerator.CoherentParameterValuePairs(argument, value);
  }
  
  private String getAbsoluteTransformationTargetPath(final ModelFile modelFile) {
    final String transformationTargetPath = modelFile.getTransformationTargetPath();
    if ((transformationTargetPath == null)) {
      return null;
    }
    IFile _file = null;
    if (modelFile!=null) {
      _file=modelFile.getFile();
    }
    IProject _project = null;
    if (_file!=null) {
      _project=_file.getProject();
    }
    IPath _location = null;
    if (_project!=null) {
      _location=_project.getLocation();
    }
    IPath _makeAbsolute = null;
    if (_location!=null) {
      _makeAbsolute=_location.makeAbsolute();
    }
    File _file_1 = null;
    if (_makeAbsolute!=null) {
      _file_1=_makeAbsolute.toFile();
    }
    final File projectFolder = _file_1;
    String _parent = null;
    if (projectFolder!=null) {
      _parent=projectFolder.getParent();
    }
    final String parentFolder = _parent;
    String _xifexpression = null;
    if ((parentFolder != null)) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(parentFolder);
      _builder.append(transformationTargetPath);
      _xifexpression = FilenameUtils.separatorsToSystem(_builder.toString());
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  private CommandLineGenerator.CoherentParameterValuePairs generateIntermediateModelOfImportArgumentValue(final Argument argument) {
    final ImportedIntermediateModelKind modelKind = ImportedIntermediateModelKind.valueOf(argument.getValue());
    ModelFile _switchResult = null;
    if (modelKind != null) {
      switch (modelKind) {
        case FIRST_DOMAIN_MODEL:
          _switchResult = this.getFirstRootModelChildOfType(this.domainModelFileTypeId);
          break;
        case FIRST_OPERATION_MODEL:
          _switchResult = this.getFirstRootModelChildOfType(this.operationModelFileTypeId);
          break;
        case FIRST_SERVICE_MODEL:
          _switchResult = this.getFirstRootModelChildOfType(this.serviceModelFileTypeId);
          break;
        default:
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("kind ");
          _builder.append(modelKind);
          _builder.append(" is not supported");
          String _plus = ("Retrieving the first imported model of " + _builder);
          throw new IllegalArgumentException(_plus);
      }
    } else {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("kind ");
      _builder.append(modelKind);
      _builder.append(" is not supported");
      String _plus = ("Retrieving the first imported model of " + _builder);
      throw new IllegalArgumentException(_plus);
    }
    final ModelFile firstImportedModel = _switchResult;
    CommandLineGenerator.CoherentParameterValuePairs _xifexpression = null;
    if ((firstImportedModel != null)) {
      CommandLineGenerator.CoherentParameterValuePairs _xblockexpression = null;
      {
        final String value = this.quoteIfContainsSpaces(this.getAbsoluteTransformationTargetPath(firstImportedModel));
        _xblockexpression = new CommandLineGenerator.CoherentParameterValuePairs(argument, value);
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  private ModelFile getFirstRootModelChildOfType(final String modelFileTypeId) {
    final Function1<ModelFile, Boolean> _function = (ModelFile it) -> {
      String _fileType = it.getFileTypeDescription().getFileType();
      return Boolean.valueOf(Objects.equal(_fileType, modelFileTypeId));
    };
    return IterableExtensions.<ModelFile>findFirst(this.silentTransformation.getRootModelFile().getChildren(), _function);
  }
  
  private CommandLineGenerator.CoherentParameterValuePairs generateIntermediateModelOfImportWithAliasArgumentValue(final Argument argument) {
    final Function1<ModelFile, Boolean> _function = (ModelFile it) -> {
      String _importAlias = it.getImportAlias();
      String _value = argument.getValue();
      return Boolean.valueOf(Objects.equal(_importAlias, _value));
    };
    final ModelFile importedModel = IterableExtensions.<ModelFile>findFirst(this.silentTransformation.getRootModelFile().getChildren(), _function);
    CommandLineGenerator.CoherentParameterValuePairs _xifexpression = null;
    if ((importedModel != null)) {
      CommandLineGenerator.CoherentParameterValuePairs _xblockexpression = null;
      {
        final String value = this.quoteIfContainsSpaces(this.getAbsoluteTransformationTargetPath(importedModel));
        _xblockexpression = new CommandLineGenerator.CoherentParameterValuePairs(argument, value);
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  private CommandLineGenerator.CoherentParameterValuePairs generateIntermediateModelsOfAllImportsArgumentValues(final Argument argument) {
    final AllImportedIntermediateModelKinds modelKind = AllImportedIntermediateModelKinds.valueOf(argument.getValue());
    Iterable<ModelFile> _switchResult = null;
    if (modelKind != null) {
      switch (modelKind) {
        case ALL_DOMAIN_MODELS:
          _switchResult = this.getRootModelChildrenOfType(this.domainModelFileTypeId);
          break;
        case ALL_OPERATION_MODELS:
          _switchResult = this.getRootModelChildrenOfType(this.operationModelFileTypeId);
          break;
        case ALL_SERVICE_MODELS:
          _switchResult = this.getRootModelChildrenOfType(this.serviceModelFileTypeId);
          break;
        default:
          StringConcatenation _builder = new StringConcatenation();
          _builder.append(modelKind);
          _builder.append(" is not supported");
          String _plus = ("Retrieving all imported models of kind " + _builder);
          throw new IllegalArgumentException(_plus);
      }
    } else {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(modelKind);
      _builder.append(" is not supported");
      String _plus = ("Retrieving all imported models of kind " + _builder);
      throw new IllegalArgumentException(_plus);
    }
    final Iterable<ModelFile> importedModels = _switchResult;
    final CommandLineGenerator.CoherentParameterValuePairs argumentValuePairs = new CommandLineGenerator.CoherentParameterValuePairs();
    final Consumer<ModelFile> _function = (ModelFile it) -> {
      final String path = this.quoteIfContainsSpaces(this.getAbsoluteTransformationTargetPath(it));
      argumentValuePairs.add(argument, path);
    };
    importedModels.forEach(_function);
    return argumentValuePairs;
  }
  
  private Iterable<ModelFile> getRootModelChildrenOfType(final String modelFileTypeId) {
    final Function1<ModelFile, Boolean> _function = (ModelFile it) -> {
      String _fileType = it.getFileTypeDescription().getFileType();
      return Boolean.valueOf(Objects.equal(_fileType, modelFileTypeId));
    };
    return IterableExtensions.<ModelFile>filter(this.silentTransformation.getRootModelFile().getChildren(), _function);
  }
  
  private CommandLineGenerator.CoherentParameterValuePairs generateRawStringArgumentValue(final Argument argument) {
    return new CommandLineGenerator.CoherentParameterValuePairs(argument);
  }
  
  private CommandLineGenerator.CoherentParameterValuePairs generateSourceModelArgumentValue(final Argument argument) {
    IFile _sourceModelFile = this.configuration.getSourceModelFile();
    IPath _rawLocation = null;
    if (_sourceModelFile!=null) {
      _rawLocation=_sourceModelFile.getRawLocation();
    }
    IPath _makeAbsolute = null;
    if (_rawLocation!=null) {
      _makeAbsolute=_rawLocation.makeAbsolute();
    }
    String _string = null;
    if (_makeAbsolute!=null) {
      _string=_makeAbsolute.toString();
    }
    String _quoteIfContainsSpaces = null;
    if (_string!=null) {
      _quoteIfContainsSpaces=this.quoteIfContainsSpaces(_string);
    }
    final String absoluteSourceModelPath = _quoteIfContainsSpaces;
    return new CommandLineGenerator.CoherentParameterValuePairs(argument, absoluteSourceModelPath);
  }
  
  private CommandLineGenerator.CoherentParameterValuePairs generateStringPairArgumentValue(final Argument argument) {
    String _value = argument.getValue();
    String _quoteIfContainsSpaces = null;
    if (_value!=null) {
      _quoteIfContainsSpaces=this.quoteIfContainsSpaces(_value);
    }
    String _valueForCommandLine = null;
    if (_quoteIfContainsSpaces!=null) {
      _valueForCommandLine=this.getValueForCommandLine(_quoteIfContainsSpaces);
    }
    return new CommandLineGenerator.CoherentParameterValuePairs(argument, _valueForCommandLine);
  }
}
