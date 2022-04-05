package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations;

import com.google.common.base.Predicate;
import de.fhdo.lemma.eclipse.ui.ModelFile;
import de.fhdo.lemma.eclipse.ui.ProgrammaticIntermediateModelTransformation;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;

@SuppressWarnings("all")
public class IntermediateTransformationExecutor {
  private final ProcessingConfiguration configuration;
  
  private final MessageConsoleStream infoStream;
  
  private final MessageConsoleStream errorStream;
  
  private final Procedure0 successfulCallback;
  
  private final Procedure0 errorOrCancelCallback;
  
  private IProgressMonitor monitor;
  
  private boolean stopTransformations;
  
  public IntermediateTransformationExecutor(final ProcessingConfiguration configuration, final MessageConsoleStream infoStream, final MessageConsoleStream errorStream, final Procedure0 successfulCallback, final Procedure0 errorOrCancelCallback) {
    this.infoStream = infoStream;
    this.errorStream = errorStream;
    this.configuration = configuration;
    this.successfulCallback = successfulCallback;
    this.errorOrCancelCallback = errorOrCancelCallback;
  }
  
  public void run() {
    final Job job = new Job("LEMMA intermediate transformation") {
      @Override
      public IStatus run(final IProgressMonitor monitor) {
        final SubMonitor subMonitor = SubMonitor.convert(monitor, 1);
        IntermediateTransformationExecutor.this.monitor = subMonitor;
        IFile _sourceModelFile = IntermediateTransformationExecutor.this.configuration.getSourceModelFile();
        final Predicate<ModelFile> _function = (ModelFile it) -> {
          return IntermediateTransformationExecutor.this.nextIntermediateTransformation(it);
        };
        final Predicate<ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationException> _function_1 = (ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationException it) -> {
          return IntermediateTransformationExecutor.this.intermediateTransformationException(it);
        };
        final Predicate<List<ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationResult>> _function_2 = (List<ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationResult> it) -> {
          return IntermediateTransformationExecutor.this.transformationSuccessful(it);
        };
        final Function2<List<ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationResult>, List<ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationException>, Boolean> _function_3 = (List<ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationResult> results, List<ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationException> exceptions) -> {
          return Boolean.valueOf(IntermediateTransformationExecutor.this.transformationsFinished(results, exceptions));
        };
        new ProgrammaticIntermediateModelTransformation(_sourceModelFile).run(
          "PROCESSING_CONFIGURATION_EXECUTION", 
          null, 
          null, 
          true, _function, _function_1, _function_2, _function_3);
        IStatus _xifexpression = null;
        boolean _isCanceled = subMonitor.isCanceled();
        boolean _not = (!_isCanceled);
        if (_not) {
          _xifexpression = Status.OK_STATUS;
        } else {
          _xifexpression = Status.CANCEL_STATUS;
        }
        return _xifexpression;
      }
    };
    job.schedule();
  }
  
  private boolean nextIntermediateTransformation(final ModelFile modelFile) {
    boolean _stopTransformations = this.stopTransformations();
    if (_stopTransformations) {
      return false;
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    IPath _fullPath = modelFile.getFile().getProject().getFullPath();
    _builder.append(_fullPath);
    _builder.append("/");
    IPath _projectRelativePath = modelFile.getFile().getProjectRelativePath();
    _builder.append(_projectRelativePath);
    _builder.append("\"");
    String _plus = ("Running intermediate transformation of model file " + _builder);
    this.infoStream.println(_plus);
    boolean _canceledByUser = this.canceledByUser();
    return (!_canceledByUser);
  }
  
  private boolean stopTransformations() {
    if (this.stopTransformations) {
      return true;
    } else {
      boolean _canceledByUser = this.canceledByUser();
      if (_canceledByUser) {
        this.stopTransformations = true;
        return true;
      } else {
        return false;
      }
    }
  }
  
  private boolean canceledByUser() {
    boolean _isCanceled = this.monitor.isCanceled();
    if (_isCanceled) {
      this.errorStream.println("Transformations aborted by user");
      this.errorOrCancelCallback.apply();
    }
    return this.monitor.isCanceled();
  }
  
  private boolean intermediateTransformationException(final ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationException ex) {
    boolean _stopTransformations = this.stopTransformations();
    boolean _not = (!_stopTransformations);
    if (_not) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Error: ");
      String _message = ex.getMessage();
      _builder.append(_message);
      String _plus = ("\t" + _builder);
      String _plus_1 = (_plus + "\n\tTransformations aborted.");
      this.errorStream.println(_plus_1);
      this.stopTransformations = true;
    }
    this.monitor.worked(1);
    this.errorOrCancelCallback.apply();
    return false;
  }
  
  private boolean transformationSuccessful(final List<ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationResult> results) {
    boolean _stopTransformations = this.stopTransformations();
    if (_stopTransformations) {
      return false;
    }
    Utils.printlnIndent(this.infoStream, "Successful.");
    this.infoStream.println();
    boolean _canceledByUser = this.canceledByUser();
    return (!_canceledByUser);
  }
  
  private Set<String> getUniqueOutputPaths(final List<ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationResult> results) {
    final Function1<ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationResult, String> _function = (ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationResult it) -> {
      return it.getResult().getOutputModel().getOutputPath();
    };
    return IterableExtensions.<String>toSet(ListExtensions.<ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationResult, String>map(results, _function));
  }
  
  private boolean transformationsFinished(final List<ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationResult> allResults, final List<ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationException> allExceptions) {
    boolean _stopTransformations = this.stopTransformations();
    if (_stopTransformations) {
      return false;
    }
    final int resultCount = this.getUniqueOutputPaths(allResults).size();
    String _xifexpression = null;
    if ((resultCount == 1)) {
      _xifexpression = "result file";
    } else {
      _xifexpression = "result files";
    }
    final String resultsSuffix = _xifexpression;
    final int errorCount = allExceptions.size();
    String _xifexpression_1 = null;
    if ((errorCount == 1)) {
      _xifexpression_1 = "error";
    } else {
      _xifexpression_1 = "errors";
    }
    final String errorsSuffix = _xifexpression_1;
    boolean _isEmpty = allExceptions.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("(");
      _builder.append(errorCount);
      _builder.append(" ");
      _builder.append(errorsSuffix);
      _builder.append(", ");
      _builder.append(resultCount);
      _builder.append(" ");
      _builder.append(resultsSuffix);
      _builder.append(" from ");
      String _plus = ("Transformations finished " + _builder);
      String _plus_1 = (_plus + 
        "successful transformations)");
      this.infoStream.println(_plus_1);
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("(");
      _builder_1.append(resultCount);
      _builder_1.append(" ");
      _builder_1.append(resultsSuffix);
      _builder_1.append(")");
      String _plus_2 = ("All transformations finished successfully " + _builder_1);
      this.infoStream.println(_plus_2);
    }
    boolean _isEmpty_1 = allResults.isEmpty();
    boolean _not_1 = (!_isEmpty_1);
    if (_not_1) {
      Utils.printlnIndent(this.infoStream, "Resulting model files:");
      final Consumer<String> _function = (String it) -> {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("- ");
        _builder_2.append(it);
        Utils.printlnIndent(this.infoStream, _builder_2.toString(), 2);
      };
      IterableExtensions.<String>sort(this.getUniqueOutputPaths(allResults)).forEach(_function);
    }
    if ((allExceptions.isEmpty() && (!this.canceledByUser()))) {
      this.successfulCallback.apply();
    }
    this.monitor.worked(1);
    return true;
  }
}
