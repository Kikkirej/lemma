package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations;

import com.google.common.collect.Iterables;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.Constraint;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.FilenameRegexConstraintType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.LemmaModelKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.ModelKindConstraintType;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class ConstraintsMatcher {
  private final Set<String> validModelKindExtensions;
  
  private final Set<Pattern> validFilenamePatterns;
  
  public ConstraintsMatcher(final List<Constraint> constraints) {
    final Function1<Constraint, Boolean> _function = (Constraint it) -> {
      return Boolean.valueOf(((it.getType() instanceof ModelKindConstraintType) && (!StringExtensions.isNullOrEmpty(it.getValue()))));
    };
    final Function1<Constraint, List<String>> _function_1 = (Constraint it) -> {
      return ModelKindConstraintType.getFileExtensions(LemmaModelKind.valueOf(it.getValue()));
    };
    this.validModelKindExtensions = IterableExtensions.<String>toSet(Iterables.<String>concat(IterableExtensions.<Constraint, List<String>>map(IterableExtensions.<Constraint>filter(constraints, _function), _function_1)));
    final Function1<Constraint, Boolean> _function_2 = (Constraint it) -> {
      return Boolean.valueOf(((it.getType() instanceof FilenameRegexConstraintType) && (!StringExtensions.isNullOrEmpty(it.getValue()))));
    };
    final Function1<Constraint, String> _function_3 = (Constraint it) -> {
      return it.getValue();
    };
    final Function1<String, Pattern> _function_4 = (String it) -> {
      return Pattern.compile(it);
    };
    this.validFilenamePatterns = IterableExtensions.<Pattern>toSet(IterableExtensions.<String, Pattern>map(IterableExtensions.<String>toSet(IterableExtensions.<Constraint, String>map(IterableExtensions.<Constraint>filter(constraints, _function_2), _function_3)), _function_4));
  }
  
  public boolean matches(final String filename) {
    final boolean validExtension = (this.validModelKindExtensions.isEmpty() || 
      this.validModelKindExtensions.contains(FilenameUtils.getExtension(filename)));
    final boolean validFilenamePattern = (this.validFilenamePatterns.isEmpty() || 
      IterableExtensions.<Pattern>exists(this.validFilenamePatterns, ((Function1<Pattern, Boolean>) (Pattern it) -> {
        return Boolean.valueOf(it.matcher(filename).matches());
      })));
    return (validExtension && validFilenamePattern);
  }
}
