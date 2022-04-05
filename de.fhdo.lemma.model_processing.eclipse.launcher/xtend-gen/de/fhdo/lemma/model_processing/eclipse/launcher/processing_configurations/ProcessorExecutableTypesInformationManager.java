package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function2;

@SuppressWarnings("all")
public class ProcessorExecutableTypesInformationManager {
  private static final HashMap<ProcessorExecutableType, ProcessorExecutableTypeInformation> MANAGED_INFO = CollectionLiterals.<ProcessorExecutableType, ProcessorExecutableTypeInformation>newHashMap();
  
  private static final BiMap<ProcessorExecutableType, String> LABEL_VIEW = HashBiMap.<ProcessorExecutableType, String>create();
  
  public static void register(final ProcessorExecutableTypeInformation... info) {
    final Consumer<ProcessorExecutableTypeInformation> _function = (ProcessorExecutableTypeInformation it) -> {
      ProcessorExecutableTypesInformationManager.MANAGED_INFO.put(it.getProcessorExecutableType(), it);
      ProcessorExecutableTypesInformationManager.LABEL_VIEW.put(it.getProcessorExecutableType(), it.getLabel());
    };
    ((List<ProcessorExecutableTypeInformation>)Conversions.doWrapArray(info)).forEach(_function);
  }
  
  public static String label(final ProcessorExecutableType literal) {
    final String label = ProcessorExecutableTypesInformationManager.LABEL_VIEW.get(literal);
    String _xifexpression = null;
    if ((label != null)) {
      _xifexpression = label;
    } else {
      throw new IllegalArgumentException(("Unsupported processor executable type: " + literal));
    }
    return _xifexpression;
  }
  
  public static ProcessorExecutableType literal(final String label) {
    final ProcessorExecutableType literal = ProcessorExecutableTypesInformationManager.LABEL_VIEW.inverse().get(label);
    ProcessorExecutableType _xifexpression = null;
    if ((literal != null)) {
      _xifexpression = literal;
    } else {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("label: ");
      _builder.append(label);
      String _plus = ("Unsupported processor executable type " + _builder);
      throw new IllegalArgumentException(_plus);
    }
    return _xifexpression;
  }
  
  public static String printableInSentenceString(final ProcessorExecutableType literal) {
    final ProcessorExecutableTypeInformation info = ProcessorExecutableTypesInformationManager.MANAGED_INFO.get(literal);
    String _printableInSentenceString = null;
    if (info!=null) {
      _printableInSentenceString=info.getPrintableInSentenceString();
    }
    return _printableInSentenceString;
  }
  
  public static boolean hasInputSupport(final ProcessorExecutableType literal) {
    final ProcessorExecutableTypeInformation info = ProcessorExecutableTypesInformationManager.MANAGED_INFO.get(literal);
    return ((info != null) && (info.getInputSupportFunction() != null));
  }
  
  public static Function2<? super Shell, ? super ProcessingConfiguration, ? extends String> inputSupportFunction(final ProcessorExecutableType literal) {
    final ProcessorExecutableTypeInformation info = ProcessorExecutableTypesInformationManager.MANAGED_INFO.get(literal);
    if ((info == null)) {
      throw new IllegalArgumentException(("Unsupported processor executable type: " + literal));
    }
    return info.getInputSupportFunction();
  }
}
