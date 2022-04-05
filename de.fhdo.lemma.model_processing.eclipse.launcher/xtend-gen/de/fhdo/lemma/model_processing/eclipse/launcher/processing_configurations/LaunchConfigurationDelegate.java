package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations;

import com.google.common.base.Objects;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractIntermediateModelArgumentTypeWithEnumValue;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.Constraint;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.AbstractConstraintType;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class LaunchConfigurationDelegate implements ILaunchConfigurationDelegate {
  public static final String CONSOLE_NAME = "LEMMA Model Processor Run";
  
  public static final String DISABLE_CONSOLE_CLEARING_LAUNCH_ATTRIBUTE = (("lemma" + 
    LaunchConfigurationDelegate.class.getName()) + "DisableConsoleClearing");
  
  private static final String INDENT = "  ";
  
  private MessageConsoleStream infoStream;
  
  private MessageConsoleStream errorStream;
  
  private String CURRENT_HEADLINE;
  
  @Override
  public String showCommandLine(final ILaunchConfiguration launchConfiguration, final String mode, final ILaunch launch, final IProgressMonitor monitor) {
    final ProcessingConfiguration configuration = ProcessingConfiguration.deserializeFrom(launchConfiguration);
    if ((configuration == null)) {
      return null;
    }
    String _xtrycatchfinallyexpression = null;
    try {
      String _xblockexpression = null;
      {
        final CommandLineGenerator generator = new CommandLineGenerator(configuration);
        final Pair<CommandLineGenerator.CoherentCommandLineParts, List<String>> partsAndWarnings = generator.generateCoherentCommandLineParts();
        final Consumer<String> _function = (String it) -> {
          MessageDialog.openWarning(Utils.getActiveShell(), "Warning during commandline generation", it);
        };
        partsAndWarnings.getValue().forEach(_function);
        _xblockexpression = generator.toPrintableCommandLine(partsAndWarnings.getKey(), "\n\t");
      }
      _xtrycatchfinallyexpression = _xblockexpression;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception ex = (Exception)_t;
        Object _xblockexpression_1 = null;
        {
          this.showCommandLineGenerationError(ex);
          _xblockexpression_1 = null;
        }
        _xtrycatchfinallyexpression = ((String)_xblockexpression_1);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return _xtrycatchfinallyexpression;
  }
  
  private void showCommandLineGenerationError(final Exception ex) {
    String _xifexpression = null;
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(ex.getMessage());
    boolean _not = (!_isNullOrEmpty);
    if (_not) {
      _xifexpression = ex.getMessage();
    } else {
      StringConcatenation _builder = new StringConcatenation();
      String _simpleName = ex.getClass().getSimpleName();
      _builder.append(_simpleName);
      _builder.append(" occurred");
      _xifexpression = _builder.toString();
    }
    String message = _xifexpression;
    MessageDialog.openError(Utils.getActiveShell(), "Error during commandline generation", message);
  }
  
  @Override
  public void launch(final ILaunchConfiguration launchConfiguration, final String mode, final ILaunch launch, final IProgressMonitor monitor) {
    try {
      final ProcessingConfiguration configuration = ProcessingConfiguration.deserializeFrom(launchConfiguration);
      if ((configuration == null)) {
        Status _status = new Status(IStatus.ERROR, LaunchConfigurationDelegate.class, 
          "LEMMA model processor configuration not readable. Launch aborted.");
        throw new CoreException(_status);
      }
      try {
        configuration.convertToUserRepresentation();
        configuration.validateInUserRepresentation();
      } catch (final Throwable _t) {
        if (_t instanceof IllegalArgumentException) {
          final IllegalArgumentException ex = (IllegalArgumentException)_t;
          String _message = ex.getMessage();
          String _plus = ("Invalid LEMMA model processor configuration:\n" + _message);
          String _plus_1 = (_plus + "\n\nLaunch ");
          String _plus_2 = (_plus_1 + 
            "aborted.");
          Status _status_1 = new Status(IStatus.ERROR, LaunchConfigurationDelegate.class, _plus_2);
          throw new CoreException(_status_1);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      final MessageConsole console = Utils.getAndRevealConsole(LaunchConfigurationDelegate.CONSOLE_NAME);
      final boolean disableConsoleClearing = launchConfiguration.getAttribute(LaunchConfigurationDelegate.DISABLE_CONSOLE_CLEARING_LAUNCH_ATTRIBUTE, false);
      if ((!disableConsoleClearing)) {
        console.clearConsole();
      }
      this.infoStream = console.newMessageStream();
      this.errorStream = Utils.newErrorMessageStream(console);
      boolean _erroneousModels = this.erroneousModels(configuration.getSourceModelFile());
      if (_erroneousModels) {
        this.closeStreams();
        return;
      }
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("\"");
      String _name = launchConfiguration.getName();
      _builder.append(_name);
      _builder.append("\"");
      String _plus = ("Executing LEMMA model processor configuration " + _builder);
      this.CURRENT_HEADLINE = _plus;
      this.infoStream.println(this.CURRENT_HEADLINE);
      final SubMonitor subMonitor = SubMonitor.convert(monitor, 1);
      boolean _intermediateTransformationRequired = this.intermediateTransformationRequired(configuration);
      if (_intermediateTransformationRequired) {
        this.printlnSep(this.infoStream);
        final Procedure0 _function = () -> {
          this.runModelProcessor(configuration, launch, subMonitor);
        };
        final Procedure0 _function_1 = () -> {
          this.handleTransformationErrorsOrCancelation();
        };
        new IntermediateTransformationExecutor(configuration, 
          this.infoStream, 
          this.errorStream, _function, _function_1).run();
      } else {
        this.runModelProcessor(configuration, launch, subMonitor);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private boolean erroneousModels(final IFile sourceModelFile) {
    final String absoluteSourceModelPath = sourceModelFile.getRawLocation().makeAbsolute().toString();
    final HashMap<String, List<String>> erroneousModels = this.transitiveModelValidation(absoluteSourceModelPath);
    List<String> _elvis = null;
    List<String> _get = erroneousModels.get(absoluteSourceModelPath);
    if (_get != null) {
      _elvis = _get;
    } else {
      _elvis = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList());
    }
    final List<String> modelErrors = _elvis;
    final Function1<Map.Entry<String, List<String>>, Boolean> _function = (Map.Entry<String, List<String>> it) -> {
      String _key = it.getKey();
      return Boolean.valueOf((!Objects.equal(_key, absoluteSourceModelPath)));
    };
    final Iterable<Map.Entry<String, List<String>>> importedModelErrors = IterableExtensions.<Map.Entry<String, List<String>>>filter(erroneousModels.entrySet(), _function);
    boolean _isEmpty = modelErrors.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      String _join = IterableExtensions.join(modelErrors, "\n\t");
      String _plus = ("Model contains errors:\n\t" + _join);
      this.errorStream.println(_plus);
      this.errorStream.println();
    }
    boolean _isEmpty_1 = IterableExtensions.isEmpty(importedModelErrors);
    boolean _not_1 = (!_isEmpty_1);
    if (_not_1) {
      this.errorStream.println("Imported models contain errors:");
      final Consumer<Map.Entry<String, List<String>>> _function_1 = (Map.Entry<String, List<String>> it) -> {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("\t");
        _builder.append("- ");
        String _key = it.getKey();
        _builder.append(_key);
        _builder.append(":");
        this.errorStream.println(_builder.toString());
        final Consumer<String> _function_2 = (String it_1) -> {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("\t\t");
          _builder_1.append("- ");
          _builder_1.append(it_1);
          this.errorStream.println(_builder_1.toString());
        };
        it.getValue().forEach(_function_2);
      };
      importedModelErrors.forEach(_function_1);
      this.errorStream.println();
    }
    boolean _isEmpty_2 = erroneousModels.isEmpty();
    boolean _not_2 = (!_isEmpty_2);
    if (_not_2) {
      this.errorStream.println("Model processor execution aborted");
    }
    boolean _isEmpty_3 = erroneousModels.isEmpty();
    return (!_isEmpty_3);
  }
  
  private HashMap<String, List<String>> transitiveModelValidation(final String rootModelFilepath) {
    throw new Error("Unresolved compilation problems:"
      + "\nType mismatch: cannot convert from String to IFile");
  }
  
  private List<String> toErrorMessages(final List<Issue> issues) {
    final Function1<Issue, Boolean> _function = (Issue it) -> {
      Severity _severity = it.getSeverity();
      return Boolean.valueOf(Objects.equal(_severity, Severity.ERROR));
    };
    final Function1<Issue, String> _function_1 = (Issue it) -> {
      StringConcatenation _builder = new StringConcatenation();
      Integer _lineNumber = it.getLineNumber();
      _builder.append(_lineNumber);
      _builder.append(":");
      Integer _column = it.getColumn();
      _builder.append(_column);
      _builder.append(": ");
      String _message = it.getMessage();
      _builder.append(_message);
      return _builder.toString();
    };
    return IterableExtensions.<String>toList(IterableExtensions.<Issue, String>map(IterableExtensions.<Issue>filter(issues, _function), _function_1));
  }
  
  private Set<String> getAbsolutePathsOfImportedModels(final XtextResource resource, final String absoluteBasepath) {
    boolean _isEmpty = resource.getContents().isEmpty();
    if (_isEmpty) {
      return Collections.<String>unmodifiableSet(CollectionLiterals.<String>newHashSet());
    }
    final EObject modelRoot = resource.getContents().get(0);
    Utils.makeImportPathsAbsoluteFromBasefilePath(modelRoot, absoluteBasepath);
    final Function1<Pair<? extends Class<? extends EObject>, Utils.ImportInfo>, String> _function = (Pair<? extends Class<? extends EObject>, Utils.ImportInfo> it) -> {
      return it.getValue().getImportUri();
    };
    return IterableExtensions.<String>toSet(ListExtensions.map(Utils.typedImports(modelRoot), _function));
  }
  
  private void printlnSep(final MessageConsoleStream stream) {
    Utils.printlnSep(stream, this.CURRENT_HEADLINE.length());
  }
  
  private void closeStreams() {
    try {
      this.infoStream.close();
      this.errorStream.close();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private void handleTransformationErrorsOrCancelation() {
    this.errorStream.println("\nModel processor execution aborted");
    this.closeStreams();
  }
  
  private void runModelProcessor(final ProcessingConfiguration configuration, final ILaunch launch, final IProgressMonitor monitor) {
    this.printlnSep(this.infoStream);
    final File workdir = configuration.getSourceModelFile().getRawLocation().makeAbsolute().toFile().getParentFile();
    final CommandLineGenerator commandLineGenerator = new CommandLineGenerator(configuration);
    CommandLineGenerator.CoherentCommandLineParts commandLineParts = null;
    try {
      final Pair<CommandLineGenerator.CoherentCommandLineParts, List<String>> partsAndWarnings = commandLineGenerator.generateCoherentCommandLineParts();
      List<String> _value = partsAndWarnings.getValue();
      for (final String warning : _value) {
        {
          String warningMessage = warning;
          boolean _endsWith = warningMessage.endsWith(".");
          boolean _not = (!_endsWith);
          if (_not) {
            warningMessage = (warningMessage + ".");
          }
          boolean _endsWith_1 = warningMessage.endsWith(" ");
          boolean _not_1 = (!_endsWith_1);
          if (_not_1) {
            warningMessage = (warningMessage + " ");
          }
          warningMessage = (warningMessage + "Do you want to continue?");
          final boolean continue_ = this.askForContinuationAfterWarning(("Warning during commandline " + 
            "generation"), warningMessage);
          if ((!continue_)) {
            this.handleTransformationErrorsOrCancelation();
            return;
          }
        }
      }
      commandLineParts = partsAndWarnings.getKey();
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception ex = (Exception)_t;
        final Runnable _function = () -> {
          this.showCommandLineGenerationError(ex);
        };
        Utils.getWorkbenchDisplay().syncExec(_function);
        this.handleTransformationErrorsOrCancelation();
        return;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    this.infoStream.println("Running model processor:");
    String _repeat = LaunchConfigurationDelegate.INDENT.repeat(2);
    String _plus = ("\n" + _repeat);
    final String printableCommandLine = commandLineGenerator.toPrintableCommandLine(commandLineParts, _plus);
    Utils.printlnIndent(this.infoStream, printableCommandLine);
    this.infoStream.println();
    try {
      final List<String> commandline = commandLineGenerator.toExecutableCommandLine(commandLineParts);
      final Process process = DebugPlugin.exec(((String[])Conversions.unwrapArray(commandline, String.class)), workdir);
      process.getInputStream().transferTo(this.infoStream);
      process.getErrorStream().transferTo(this.errorStream);
      final IProcess debugProcess = DebugPlugin.newProcess(launch, process, "");
      Utils.getAndRevealConsole(LaunchConfigurationDelegate.CONSOLE_NAME);
      final Runnable processObserver = new Runnable() {
        @Override
        public void run() {
          boolean processDestroyed = false;
          while ((process.isAlive() && (!processDestroyed))) {
            try {
              process.waitFor(250, TimeUnit.MILLISECONDS);
            } catch (final Throwable _t) {
              if (_t instanceof InterruptedException) {
                process.destroyForcibly();
                processDestroyed = true;
              } else {
                throw Exceptions.sneakyThrow(_t);
              }
            }
          }
          int _exitValue = process.exitValue();
          boolean _equals = (_exitValue == 0);
          if (_equals) {
            LaunchConfigurationDelegate.this.infoStream.println("Model processor execution finished");
          } else {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("(exit code: ");
            int _exitValue_1 = process.exitValue();
            _builder.append(_exitValue_1);
            _builder.append(")");
            String _plus = ("\nModel processor execution finished with errors " + _builder);
            LaunchConfigurationDelegate.this.errorStream.println(_plus);
          }
          LaunchConfigurationDelegate.this.closeStreams();
          monitor.worked(1);
          monitor.done();
        }
      };
      new Thread(processObserver).start();
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception ex = (Exception)_t;
        this.infoStream.println();
        this.errorStream.println(ex.getMessage());
        this.closeStreams();
        monitor.done();
        return;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  private boolean askForContinuationAfterWarning(final String title, final String message) {
    abstract class __LaunchConfigurationDelegate_2 implements Runnable {
      boolean continuationConfirmed;
    }
    
    final __LaunchConfigurationDelegate_2 continuationDialogRunnable = new __LaunchConfigurationDelegate_2() {
      @Override
      public void run() {
        Shell _activeShell = Utils.getActiveShell();
        final MessageDialog dialog = new MessageDialog(_activeShell, title, null, message, 
          MessageDialog.WARNING, new String[] { "Continue", "Cancel" }, 0);
        int _open = dialog.open();
        boolean _equals = (_open == 0);
        this.continuationConfirmed = _equals;
      }
    };
    Utils.getWorkbenchDisplay().syncExec(continuationDialogRunnable);
    return continuationDialogRunnable.continuationConfirmed;
  }
  
  private boolean intermediateTransformationRequired(final ProcessingConfiguration configuration) {
    final Function1<Argument, Boolean> _function = (Argument it) -> {
      AbstractArgumentType _type = it.getType();
      return Boolean.valueOf((_type instanceof AbstractIntermediateModelArgumentTypeWithEnumValue));
    };
    return IterableExtensions.<Argument>exists(configuration.getArguments(), _function);
  }
  
  /**
   * private def constraintsPreview(ProcessingConfiguration configuration) {
   * val constraintsPerType = configuration.constraints.groupBy[it.type.class]
   * if (constraintsPerType.empty)
   * return ""
   * 
   * val previewStrings = newArrayList(
   * constraintsPerType.previewForConstraintsOfType(
   * ModelKindConstraintType,
   * "LEMMA models of kind",
   * "LEMMA models of one of the kinds"
   * ),
   * constraintsPerType.previewForConstraintsOfType(
   * FilenameRegexConstraintType,
   * "Files matching pattern",
   * "Files matching one of the patterns"
   * )
   * )
   * 
   * previewStrings.removeAll("")
   * return previewStrings.map['''«"\t"»- «it»'''].join("\n\tAND\n")
   * }
   */
  private String previewForConstraintsOfType(final Map<Class<? extends AbstractConstraintType>, List<Constraint>> constraintsPerType, final Class<? extends AbstractConstraintType> type, final String singularLabel, final String pluralLabel) {
    final List<Constraint> constraints = constraintsPerType.get(type);
    if ((constraints == null)) {
      return "";
    }
    final StringBuffer sb = new StringBuffer();
    int _size = constraints.size();
    boolean _equals = (_size == 1);
    if (_equals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(singularLabel);
      _builder.append(": ");
      sb.append(_builder);
    } else {
      int _size_1 = constraints.size();
      boolean _greaterThan = (_size_1 > 1);
      if (_greaterThan) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(pluralLabel);
        _builder_1.append(": ");
        sb.append(_builder_1);
      }
    }
    final Function1<Constraint, String> _function = (Constraint it) -> {
      return it.getValue();
    };
    sb.append(IterableExtensions.join(ListExtensions.<Constraint, String>map(constraints, _function), ", "));
    return sb.toString();
  }
}
