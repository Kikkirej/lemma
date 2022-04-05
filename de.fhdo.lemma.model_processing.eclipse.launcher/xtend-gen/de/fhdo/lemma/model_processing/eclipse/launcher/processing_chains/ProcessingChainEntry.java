package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains;

import com.google.common.base.Objects;
import de.fhdo.lemma.model_processing.eclipse.launcher.ModelObjectWithPropertyChangeSupport;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.PreviousExitValueComparator;
import java.util.Map;
import javax.xml.stream.XMLStreamWriter;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.w3c.dom.Element;

@SuppressWarnings("all")
public class ProcessingChainEntry extends ModelObjectWithPropertyChangeSupport implements Cloneable {
  public static final String XML_PROCESSING_CHAIN_ENTRY = "processingChainEntry";
  
  private static final String XML_LAUNCH_CONFIGURATION_NAME_ATTR = "launchConfigurationName";
  
  private static final String XML_PREVIOUS_EXIT_VALUE_COMPARATOR_ATTR = "previousExitValueComparator";
  
  private static final String XML_PREVIOUS_EXIT_VALUE_ATTR = "previousExitValue";
  
  @Accessors
  private ProcessingChain chain;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private String launchConfigurationName;
  
  public void setLaunchConfigurationName(final String launchConfigurationName) {
    this.firePropertyChange("launchConfigurationName", this.launchConfigurationName, 
      this.launchConfigurationName = launchConfigurationName);
  }
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private PreviousExitValueComparator.Comparator previousExitValueComparator;
  
  public void setPreviousExitValueComparator(final PreviousExitValueComparator.Comparator previousExitValueComparator) {
    this.firePropertyChange("previousExitValueComparator", this.previousExitValueComparator, 
      this.previousExitValueComparator = previousExitValueComparator);
  }
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private Integer previousExitValue;
  
  public void setPreviousExitValue(final Integer previousExitValue) {
    this.firePropertyChange("previousExitValue", this.previousExitValue, 
      this.previousExitValue = previousExitValue);
  }
  
  public ProcessingChainEntry() {
  }
  
  public ProcessingChainEntry(final String launchConfigurationName, final ProcessingChain chain, final PreviousExitValueComparator.Comparator previousExitValueComparator, final int previousExitValue) {
    this.launchConfigurationName = launchConfigurationName;
    this.chain = chain;
    this.previousExitValueComparator = previousExitValueComparator;
    this.previousExitValue = Integer.valueOf(previousExitValue);
  }
  
  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * def validate() {
   * validLaunchConfigurationName()
   * validPreviousExitValueComparator()
   * validPreviousExitValue()
   * }
   */
  public void validLaunchConfigurationName(final Map<String, ILaunchConfiguration> availableLaunchConfigurations) {
    ProcessingChainEntry.validLaunchConfigurationName(this.launchConfigurationName, availableLaunchConfigurations);
  }
  
  public static void validLaunchConfigurationName(final String launchConfigurationName, final Map<String, ILaunchConfiguration> availableLaunchConfigurations) {
    Utils.notNullOrEmpty(launchConfigurationName, "Launch configuration name must not be empty");
    final boolean existsLaunchConfiguration = availableLaunchConfigurations.keySet().contains(launchConfigurationName);
    if ((!existsLaunchConfiguration)) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(launchConfigurationName);
      _builder.append(" does not exist or does not concern execution of ");
      String _plus = ("Launch configuration with name " + _builder);
      String _plus_1 = (_plus + 
        "a LEMMA model processor");
      throw new IllegalArgumentException(_plus_1);
    }
  }
  
  public void validPreviousExitValueComparator() {
    ProcessingChainEntry.validPreviousExitValueComparator(this, this.previousExitValueComparator);
  }
  
  public static void validPreviousExitValueComparator(final ProcessingChainEntry entry, final PreviousExitValueComparator.Comparator previousExitValueComparator) {
    boolean _isEditable = entry.isEditable();
    if (_isEditable) {
      Utils.notNull(previousExitValueComparator, "Exit value comparator must not be empty");
    }
  }
  
  public void validPreviousExitValue() {
    ProcessingChainEntry.validPreviousExitValue(this, this.previousExitValue);
  }
  
  public static void validPreviousExitValue(final ProcessingChainEntry entry, final Integer previousExitValue) {
    boolean _isEditable = entry.isEditable();
    boolean _not = (!_isEditable);
    if (_not) {
      return;
    }
    Utils.notNull(previousExitValue, "Exit value must not be empty");
    if (((previousExitValue).intValue() < 0)) {
      throw new IllegalArgumentException("Exit value must be greater or equal zero");
    } else {
      boolean _equals = Objects.equal(entry.previousExitValueComparator, PreviousExitValueComparator.Comparator.LOWER);
      if (_equals) {
        throw new IllegalArgumentException(("Exit value comparison result must be greater or " + 
          "equal zero"));
      }
    }
  }
  
  public void validate(final Map<String, ILaunchConfiguration> availableLaunchConfigurations) {
    this.validLaunchConfigurationName(availableLaunchConfigurations);
    this.validPreviousExitValueComparator();
    this.validPreviousExitValue();
  }
  
  public boolean isEditable() {
    int _indexOf = this.chain.getEntries().indexOf(this);
    final boolean isFirstEntry = (_indexOf == 0);
    return (!isFirstEntry);
  }
  
  public void serializeToXml(final XMLStreamWriter writer) {
    try {
      writer.writeStartElement(ProcessingChainEntry.XML_PROCESSING_CHAIN_ENTRY);
      String _elvis = null;
      if (this.launchConfigurationName != null) {
        _elvis = this.launchConfigurationName;
      } else {
        _elvis = "";
      }
      writer.writeAttribute(ProcessingChainEntry.XML_LAUNCH_CONFIGURATION_NAME_ATTR, _elvis);
      String _elvis_1 = null;
      String _string = null;
      if (this.previousExitValueComparator!=null) {
        _string=this.previousExitValueComparator.toString();
      }
      if (_string != null) {
        _elvis_1 = _string;
      } else {
        _elvis_1 = "";
      }
      writer.writeAttribute(ProcessingChainEntry.XML_PREVIOUS_EXIT_VALUE_COMPARATOR_ATTR, _elvis_1);
      String _elvis_2 = null;
      String _string_1 = null;
      if (this.previousExitValue!=null) {
        _string_1=this.previousExitValue.toString();
      }
      if (_string_1 != null) {
        _elvis_2 = _string_1;
      } else {
        _elvis_2 = "";
      }
      writer.writeAttribute(ProcessingChainEntry.XML_PREVIOUS_EXIT_VALUE_ATTR, _elvis_2);
      writer.writeEndElement();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public Integer deserializeFromXml(final Element element) {
    Integer _xblockexpression = null;
    {
      this.launchConfigurationName = element.getAttribute(ProcessingChainEntry.XML_LAUNCH_CONFIGURATION_NAME_ATTR);
      final String previousExitValueComparatorXmlValue = element.getAttribute(ProcessingChainEntry.XML_PREVIOUS_EXIT_VALUE_COMPARATOR_ATTR);
      PreviousExitValueComparator.Comparator _xifexpression = null;
      boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(previousExitValueComparatorXmlValue);
      boolean _not = (!_isNullOrEmpty);
      if (_not) {
        _xifexpression = PreviousExitValueComparator.Comparator.valueOf(previousExitValueComparatorXmlValue);
      } else {
        _xifexpression = null;
      }
      this.previousExitValueComparator = _xifexpression;
      final String previousExitValueXmlValue = element.getAttribute(ProcessingChainEntry.XML_PREVIOUS_EXIT_VALUE_ATTR);
      Integer _xifexpression_1 = null;
      boolean _isNullOrEmpty_1 = StringExtensions.isNullOrEmpty(previousExitValueXmlValue);
      boolean _not_1 = (!_isNullOrEmpty_1);
      if (_not_1) {
        _xifexpression_1 = Integer.valueOf(previousExitValueXmlValue);
      } else {
        _xifexpression_1 = null;
      }
      _xblockexpression = this.previousExitValue = _xifexpression_1;
    }
    return _xblockexpression;
  }
  
  @Pure
  public ProcessingChain getChain() {
    return this.chain;
  }
  
  public void setChain(final ProcessingChain chain) {
    this.chain = chain;
  }
  
  @Pure
  public String getLaunchConfigurationName() {
    return this.launchConfigurationName;
  }
  
  @Pure
  public PreviousExitValueComparator.Comparator getPreviousExitValueComparator() {
    return this.previousExitValueComparator;
  }
  
  @Pure
  public Integer getPreviousExitValue() {
    return this.previousExitValue;
  }
}
