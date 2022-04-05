package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class ProcessorExecutableTypeInformation {
  @Accessors(AccessorType.PUBLIC_GETTER)
  private ProcessorExecutableType processorExecutableType;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private String label;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private String printableInSentenceString;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private Function2<? super Shell, ? super ProcessingConfiguration, ? extends String> inputSupportFunction;
  
  public ProcessorExecutableTypeInformation(final ProcessorExecutableType processorExecutableType, final String label, final String printableInSentenceString, final Function2<? super Shell, ? super ProcessingConfiguration, ? extends String> inputSupportFunction) {
    this.processorExecutableType = processorExecutableType;
    this.label = label;
    this.printableInSentenceString = printableInSentenceString;
    this.inputSupportFunction = inputSupportFunction;
  }
  
  @Pure
  public ProcessorExecutableType getProcessorExecutableType() {
    return this.processorExecutableType;
  }
  
  @Pure
  public String getLabel() {
    return this.label;
  }
  
  @Pure
  public String getPrintableInSentenceString() {
    return this.printableInSentenceString;
  }
  
  @Pure
  public Function2<? super Shell, ? super ProcessingConfiguration, ? extends String> getInputSupportFunction() {
    return this.inputSupportFunction;
  }
}
