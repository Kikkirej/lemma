package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations;

import com.google.common.base.Objects;
import de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants;
import de.fhdo.lemma.model_processing.eclipse.launcher.ModelObjectWithPropertyChangeSupport;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("all")
public class ProcessingConfiguration extends ModelObjectWithPropertyChangeSupport implements Cloneable {
  private static class ProcessorInformation {
    @Accessors(AccessorType.PUBLIC_GETTER)
    private final ProcessorExecutableType processorExecutableType;
    
    @Accessors(AccessorType.PUBLIC_GETTER)
    private final String processorBasicExecutionCommand;
    
    @Accessors(AccessorType.PUBLIC_GETTER)
    private final String processorExecutablePath;
    
    public ProcessorInformation(final ProcessorExecutableType processorExecutableType, final String processorBasicExecutionCommand, final String processorExecutablePath) {
      this.processorExecutableType = processorExecutableType;
      this.processorBasicExecutionCommand = processorBasicExecutionCommand;
      this.processorExecutablePath = processorExecutablePath;
    }
    
    @Pure
    public ProcessorExecutableType getProcessorExecutableType() {
      return this.processorExecutableType;
    }
    
    @Pure
    public String getProcessorBasicExecutionCommand() {
      return this.processorBasicExecutionCommand;
    }
    
    @Pure
    public String getProcessorExecutablePath() {
      return this.processorExecutablePath;
    }
  }
  
  private static final String XML_CONFIGURATION_ELEMENT = "processingConfiguration";
  
  private static final String XML_CONFIGURATION_ATTR_SOURCE_MODEL_PROJECT_NAME = "sourceModelProjectName";
  
  private static final String XML_CONFIGURATION_ATTR_SOURCE_MODEL_FILE_PATH = "sourceModelFilePath";
  
  private static final String XML_PROCESSOR_ELEMENT = "processor";
  
  private static final String XML_PROCESSOR_TYPE_ELEMENT = "type";
  
  private static final String XML_PROCESSOR_BASIC_EXECUTION_COMMAND_ELEMENT = "basicExecutionCommand";
  
  private static final String XML_PROCESSOR_PATH_ELEMENT = "path";
  
  private static final String XML_ARGUMENTS_ELEMENT = "arguments";
  
  /**
   * @Accessors(PUBLIC_GETTER)
   * var String name
   * 
   * def setName(String name) {
   * firePropertyChange("name", this.name, this.name = name)
   * }
   */
  @Accessors(AccessorType.PUBLIC_GETTER)
  private String sourceModelProjectName;
  
  public void setSourceModelProjectName(final String sourceModelProjectName) {
    this.firePropertyChange("sourceModelProjectName", this.sourceModelProjectName, 
      this.sourceModelProjectName = sourceModelProjectName);
  }
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private String sourceModelFilePath;
  
  public void setSourceModelFilePath(final String sourceModelFilePath) {
    this.firePropertyChange("sourceModelFilePath", this.sourceModelFilePath, 
      this.sourceModelFilePath = sourceModelFilePath);
  }
  
  @Accessors
  private ProcessorExecutableType processorExecutableType;
  
  public void setProcessorExecutableType(final ProcessorExecutableType processorExecutableType) {
    this.firePropertyChange("processorExecutableType", this.processorExecutableType, 
      this.processorExecutableType = processorExecutableType);
  }
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private String processorBasicExecutionCommand;
  
  public void setProcessorBasicExecutionCommand(final String processorBasicExecutionCommand) {
    this.firePropertyChange("processorBasicExecutionCommand", this.processorBasicExecutionCommand, 
      this.processorBasicExecutionCommand = processorBasicExecutionCommand);
  }
  
  @Accessors
  private String processorExecutablePath;
  
  public void setProcessorExecutablePath(final String processorExecutablePath) {
    this.firePropertyChange("processorExecutablePath", this.processorExecutablePath, 
      this.processorExecutablePath = processorExecutablePath);
  }
  
  public ProcessingConfiguration() {
  }
  
  public ProcessingConfiguration(final String sourceModelProjectName, final String sourceModelFilePath, final ProcessorExecutableType processorExecutableType, final String processorExecutablePath, final String processorBasicExecutionCommand) {
    this.sourceModelProjectName = sourceModelProjectName;
    this.sourceModelFilePath = sourceModelFilePath;
    this.processorExecutableType = processorExecutableType;
    this.processorExecutablePath = processorExecutablePath;
    this.processorBasicExecutionCommand = processorBasicExecutionCommand;
  }
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private ArrayList<Argument> arguments = CollectionLiterals.<Argument>newArrayList();
  
  /**
   * @Accessors(PUBLIC_GETTER)
   * var constraints = <Constraint>newArrayList
   */
  @Override
  public boolean equals(final Object o) {
    boolean _xifexpression = false;
    if ((o == this)) {
      _xifexpression = true;
    } else {
      boolean _xifexpression_1 = false;
      if ((!(o instanceof ProcessingConfiguration))) {
        _xifexpression_1 = false;
      } else {
        boolean _xblockexpression = false;
        {
          final ProcessingConfiguration otherConfig = ((ProcessingConfiguration) o);
          _xblockexpression = (((((Objects.equal(this.sourceModelProjectName, otherConfig.sourceModelProjectName) && 
            Objects.equal(this.sourceModelFilePath, otherConfig.sourceModelFilePath)) && 
            Objects.equal(this.processorExecutableType, otherConfig.processorExecutableType)) && 
            Objects.equal(this.processorBasicExecutionCommand, otherConfig.processorBasicExecutionCommand)) && 
            Objects.equal(this.processorExecutablePath, otherConfig.processorExecutablePath)) && 
            Utils.<Argument>equalLists(this.arguments, otherConfig.arguments));
        }
        _xifexpression_1 = _xblockexpression;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  @Override
  public Object clone() {
    try {
      Object _clone = super.clone();
      final ProcessingConfiguration clone = ((ProcessingConfiguration) _clone);
      final Function1<Argument, Argument> _function = (Argument it) -> {
        Object _clone_1 = it.clone();
        return ((Argument) _clone_1);
      };
      clone.arguments = CollectionLiterals.<Argument>newArrayList(((Argument[])Conversions.unwrapArray(ListExtensions.<Argument, Argument>map(this.arguments, _function), Argument.class)));
      return clone;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * @Accessors(PUBLIC_GETTER)
   * var ConstraintsMatcher constraintsMatcher = null
   */
  public void serializeToXml(final XMLStreamWriter writer) {
    try {
      writer.writeStartElement(ProcessingConfiguration.XML_CONFIGURATION_ELEMENT);
      writer.writeAttribute(ProcessingConfiguration.XML_CONFIGURATION_ATTR_SOURCE_MODEL_PROJECT_NAME, 
        this.sourceModelProjectName);
      writer.writeAttribute(ProcessingConfiguration.XML_CONFIGURATION_ATTR_SOURCE_MODEL_FILE_PATH, this.sourceModelFilePath);
      this.writeProcessorInformationToXml(writer);
      this.serializeToXml(this.arguments, writer, ProcessingConfiguration.XML_ARGUMENTS_ELEMENT);
      writer.writeEndElement();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void convertToInternalRepresentation() {
    final Consumer<Argument> _function = (Argument it) -> {
      it.convertToInternalRepresentation();
    };
    this.arguments.forEach(_function);
  }
  
  public IFile getSourceModelFile() {
    try {
      ProcessingConfiguration.validSourceModelProjectName(this.sourceModelProjectName);
      ProcessingConfiguration.validSourceModelFilePath(this.sourceModelProjectName, this.sourceModelFilePath);
    } catch (final Throwable _t) {
      if (_t instanceof IllegalArgumentException) {
        return null;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return Utils.findFileInWorkspaceProject(this.sourceModelProjectName, this.sourceModelFilePath);
  }
  
  public void validateInUserRepresentation() {
    ProcessingConfiguration.validSourceModelProjectName(this.sourceModelProjectName);
    ProcessingConfiguration.validSourceModelFilePath(this.sourceModelProjectName, this.sourceModelFilePath);
    ProcessingConfiguration.validProcessorExecutableType(this.processorExecutableType);
    ProcessingConfiguration.validProcessorBasicExecutionCommand(this.processorExecutableType, this.processorBasicExecutionCommand);
    ProcessingConfiguration.validProcessorExecutablePath(this.processorExecutableType, this.processorBasicExecutionCommand, 
      this.processorExecutablePath);
    ProcessingConfiguration.validArgumentsInUserRepresentation(this, this.arguments);
  }
  
  /**
   * static def validName(String name) {
   * notNullOrEmpty(name, "Processing configuration name must not be empty")
   * }
   */
  public static void validSourceModelProjectName(final String sourceModelProjectName) {
    Utils.notNullOrEmpty(sourceModelProjectName, ("Processing configuration must specify a source " + 
      "model project"));
    IProject _findProjectInCurrentWorkspace = Utils.findProjectInCurrentWorkspace(sourceModelProjectName);
    boolean _tripleEquals = (_findProjectInCurrentWorkspace == null);
    if (_tripleEquals) {
      throw new IllegalArgumentException("Source model project does not exist in workspace");
    }
  }
  
  public static void validSourceModelFilePath(final String sourceModelProjectName, final String sourceModelFilePath) {
    ProcessingConfiguration.validSourceModelProjectName(sourceModelProjectName);
    Utils.notNullOrEmpty(sourceModelFilePath, ("Processing configuration must specify the path to a " + 
      "source model file relative to a source model project path"));
    Utils.notNull(
      Utils.findFileInWorkspaceProject(sourceModelProjectName, sourceModelFilePath), 
      "Source model file does not exist in source model project");
  }
  
  public static void validProcessorExecutableType(final ProcessorExecutableType processorExecutableType) {
    Utils.notNull(processorExecutableType, ("Processing configuration must specify type of " + 
      "processor executable"));
  }
  
  public static void validProcessorBasicExecutionCommand(final ProcessorExecutableType processorExecutableType, final String processorExecutionCommand) {
    Utils.notNull(processorExecutionCommand, ("Processing configuration must specify a basic " + 
      "execution command"));
    boolean _equals = Objects.equal(processorExecutableType, ProcessorExecutableType.LOCAL_DOCKER_IMAGE);
    if (_equals) {
      Utils.notEmpty(processorExecutionCommand, ("Docker-based processing configurations must " + 
        "specify a basic execution command"));
    }
  }
  
  public static void validProcessorExecutablePath(final ProcessorExecutableType processorExecutableType, final String processorExecutionCommand, final String processorExecutablePath) {
    Utils.notNullOrEmpty(processorExecutablePath, ("Processing configuration must specify path to " + 
      "processor executable"));
    boolean _equals = Objects.equal(processorExecutableType, ProcessorExecutableType.LOCAL_JAVA_PROGRAM);
    if (_equals) {
      ProcessingConfiguration.validJavaProcessorExecutablePath(processorExecutablePath);
    } else {
      boolean _equals_1 = Objects.equal(processorExecutableType, ProcessorExecutableType.LOCAL_DOCKER_IMAGE);
      if (_equals_1) {
        ProcessingConfiguration.validDockerProcessorExecutablePath(processorExecutablePath, processorExecutionCommand);
      }
    }
  }
  
  private static void validJavaProcessorExecutablePath(final String processorExecutablePath) {
    boolean _isRegularFile = Files.isRegularFile(Paths.get(processorExecutablePath));
    boolean _not = (!_isRegularFile);
    if (_not) {
      throw new IllegalArgumentException("Executable processor file does not exist");
    }
  }
  
  private static void validDockerProcessorExecutablePath(final String processorExecutablePath, final String processorExecutionCommand) {
    Utils.notEmpty(processorExecutablePath, "Processing configuration must specify Docker image name");
    Utils.notNullOrEmpty(processorExecutionCommand, ("Validity of Docker image not determinable " + 
      "because no processor executable is specified"));
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(processorExecutionCommand);
    _builder.append(" inspect ");
    final String imageValidationCommand = (_builder.toString() + processorExecutablePath);
    try {
      final Pair<Integer, String> commandResult = Utils.executeShellCommandBlocking(imageValidationCommand, 50, 4);
      Integer _key = commandResult.getKey();
      boolean _equals = ((_key).intValue() == 1);
      if (_equals) {
        throw new IllegalArgumentException("Image does not exist");
      } else {
        Integer _key_1 = commandResult.getKey();
        boolean _greaterThan = ((_key_1).intValue() > 1);
        if (_greaterThan) {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append(imageValidationCommand);
          _builder_1.append(" returned with exit code ");
          Integer _key_2 = commandResult.getKey();
          _builder_1.append(_key_2);
          _builder_1.append(": ");
          String _plus = ("Validity of Docker image not determinable. " + _builder_1);
          String _value = commandResult.getValue();
          String _plus_1 = (_plus + _value);
          throw new IllegalArgumentException(_plus_1);
        }
      }
    } catch (final Throwable _t) {
      if (_t instanceof IOException) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("Invalid validation command \"");
        _builder_2.append(imageValidationCommand);
        _builder_2.append("\"");
        String _plus_2 = ("Validity of Docker image not determinable: " + _builder_2);
        throw new IllegalArgumentException(_plus_2);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public static void validArgumentsInUserRepresentation(final ProcessingConfiguration configuration, final List<Argument> arguments) {
    if (arguments!=null) {
      final Consumer<Argument> _function = (Argument it) -> {
        it.validateInUserRepresentation(configuration);
      };
      arguments.forEach(_function);
    }
  }
  
  private void writeProcessorInformationToXml(final XMLStreamWriter writer) {
    try {
      writer.writeStartElement(ProcessingConfiguration.XML_PROCESSOR_ELEMENT);
      writer.writeStartElement(ProcessingConfiguration.XML_PROCESSOR_TYPE_ELEMENT);
      String _elvis = null;
      String _name = this.processorExecutableType.name();
      if (_name != null) {
        _elvis = _name;
      } else {
        _elvis = "";
      }
      writer.writeCharacters(_elvis);
      writer.writeEndElement();
      writer.writeStartElement(ProcessingConfiguration.XML_PROCESSOR_BASIC_EXECUTION_COMMAND_ELEMENT);
      String _elvis_1 = null;
      if (this.processorBasicExecutionCommand != null) {
        _elvis_1 = this.processorBasicExecutionCommand;
      } else {
        _elvis_1 = "";
      }
      writer.writeCharacters(_elvis_1);
      writer.writeEndElement();
      writer.writeStartElement(ProcessingConfiguration.XML_PROCESSOR_PATH_ELEMENT);
      String _elvis_2 = null;
      if (this.processorExecutablePath != null) {
        _elvis_2 = this.processorExecutablePath;
      } else {
        _elvis_2 = "";
      }
      writer.writeCharacters(_elvis_2);
      writer.writeEndElement();
      writer.writeEndElement();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private void serializeToXml(final List<? extends ProcessingConfigurationItem> items, final XMLStreamWriter writer, final String startElementTag) {
    try {
      writer.writeStartElement(startElementTag);
      final Consumer<ProcessingConfigurationItem> _function = (ProcessingConfigurationItem it) -> {
        it.serializeToXmlInternal(writer);
      };
      items.forEach(_function);
      writer.writeEndElement();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * def matchesConstraints(String filename) {
   * return constraintsMatcher.matches(filename)
   * }
   */
  public static ProcessingConfiguration deserializeFromXml(final String xml) {
    final Element configurationRoot = Utils.getRootElementWithTag(Utils.parseXmlString(xml), 
      ProcessingConfiguration.XML_CONFIGURATION_ELEMENT);
    if ((configurationRoot == null)) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("configuration: Root XML element ");
      _builder.append(ProcessingConfiguration.XML_CONFIGURATION_ELEMENT);
      _builder.append(" not found");
      String _plus = ("Error during deserialization of processing " + _builder);
      throw new IllegalArgumentException(_plus);
    }
    final String sourceModelProjectName = configurationRoot.getAttribute(ProcessingConfiguration.XML_CONFIGURATION_ATTR_SOURCE_MODEL_PROJECT_NAME);
    final String sourceModelFilePath = configurationRoot.getAttribute(ProcessingConfiguration.XML_CONFIGURATION_ATTR_SOURCE_MODEL_FILE_PATH);
    final ProcessingConfiguration.ProcessorInformation processorInformation = ProcessingConfiguration.getProcessorInformation(configurationRoot);
    final ProcessingConfiguration configuration = new ProcessingConfiguration(sourceModelProjectName, sourceModelFilePath, 
      processorInformation.processorExecutableType, 
      processorInformation.processorExecutablePath, 
      processorInformation.processorBasicExecutionCommand);
    final Supplier<Argument> _function = () -> {
      return new Argument();
    };
    configuration.arguments = ProcessingConfiguration.<Argument>initializeItemsFromXml(_function, configurationRoot, 
      ProcessingConfiguration.XML_ARGUMENTS_ELEMENT, Argument.XML_ARGUMENT_ELEMENT);
    return configuration;
  }
  
  public void convertToUserRepresentation() {
    final Consumer<Argument> _function = (Argument it) -> {
      it.convertToUserRepresentation();
    };
    this.arguments.forEach(_function);
  }
  
  private static ProcessingConfiguration.ProcessorInformation getProcessorInformation(final Element element) {
    final Element processorElement = Utils.findChildElementWithTag(element, ProcessingConfiguration.XML_PROCESSOR_ELEMENT);
    if ((processorElement == null)) {
      return null;
    }
    final Element executableTypeElement = Utils.findChildElementWithTag(processorElement, 
      ProcessingConfiguration.XML_PROCESSOR_TYPE_ELEMENT);
    ProcessorExecutableType _xtrycatchfinallyexpression = null;
    try {
      _xtrycatchfinallyexpression = ProcessorExecutableType.valueOf(executableTypeElement.getTextContent());
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        _xtrycatchfinallyexpression = null;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    final ProcessorExecutableType processorExecutableType = _xtrycatchfinallyexpression;
    Element _findChildElementWithTag = Utils.findChildElementWithTag(processorElement, 
      ProcessingConfiguration.XML_PROCESSOR_BASIC_EXECUTION_COMMAND_ELEMENT);
    String _textContent = null;
    if (_findChildElementWithTag!=null) {
      _textContent=_findChildElementWithTag.getTextContent();
    }
    final String processorBasicExecutionCommand = _textContent;
    Element _findChildElementWithTag_1 = Utils.findChildElementWithTag(processorElement, 
      ProcessingConfiguration.XML_PROCESSOR_PATH_ELEMENT);
    String _textContent_1 = null;
    if (_findChildElementWithTag_1!=null) {
      _textContent_1=_findChildElementWithTag_1.getTextContent();
    }
    final String processorExecutablePath = _textContent_1;
    return new ProcessingConfiguration.ProcessorInformation(processorExecutableType, processorBasicExecutionCommand, processorExecutablePath);
  }
  
  private static <T extends ProcessingConfigurationItem> ArrayList<T> initializeItemsFromXml(final Supplier<T> constructItemInstance, final Element element, final String elementParentTag, final String elementTag) {
    final ArrayList<T> items = CollectionLiterals.<T>newArrayList();
    final Element parentElement = Utils.findChildElementWithTag(element, elementParentTag);
    final NodeList elements = parentElement.getElementsByTagName(elementTag);
    int _length = elements.getLength();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer n : _doubleDotLessThan) {
      {
        final T item = constructItemInstance.get();
        Node _item = elements.item((n).intValue());
        item.deserializeFromXmlInternal(((Element) _item));
        items.add(item);
      }
    }
    return items;
  }
  
  public static void setProcessingConfigurationAsAttribute(final ILaunchConfigurationWorkingCopy launchConfiguration, final ProcessingConfiguration processingConfiguration) {
    try {
      IFile _sourceModelFile = processingConfiguration.getSourceModelFile();
      launchConfiguration.setMappedResources(new IResource[] { _sourceModelFile });
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      final XMLOutputFactory factory = XMLOutputFactory.newInstance();
      final XMLStreamWriter writer = factory.createXMLStreamWriter(out);
      processingConfiguration.serializeToXml(writer);
      launchConfiguration.setAttribute(LaunchConfigurationConstants.PROCESSING_CONFIGURATION_ATTRIBUTE, 
        out.toString(StandardCharsets.UTF_8));
      out.close();
      writer.close();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static ProcessingConfiguration deserializeFrom(final ILaunchConfiguration launchConfiguration) {
    try {
      final String xml = launchConfiguration.getAttribute(LaunchConfigurationConstants.PROCESSING_CONFIGURATION_ATTRIBUTE, "");
      boolean _isEmpty = xml.isEmpty();
      if (_isEmpty) {
        return null;
      }
      return ProcessingConfiguration.deserializeFromXml(xml);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Pure
  public String getSourceModelProjectName() {
    return this.sourceModelProjectName;
  }
  
  @Pure
  public String getSourceModelFilePath() {
    return this.sourceModelFilePath;
  }
  
  @Pure
  public ProcessorExecutableType getProcessorExecutableType() {
    return this.processorExecutableType;
  }
  
  @Pure
  public String getProcessorBasicExecutionCommand() {
    return this.processorBasicExecutionCommand;
  }
  
  @Pure
  public String getProcessorExecutablePath() {
    return this.processorExecutablePath;
  }
  
  @Pure
  public ArrayList<Argument> getArguments() {
    return this.arguments;
  }
}
