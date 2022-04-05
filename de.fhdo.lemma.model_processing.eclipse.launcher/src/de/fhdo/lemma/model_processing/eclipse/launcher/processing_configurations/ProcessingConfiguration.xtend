package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations

import org.eclipse.xtend.lib.annotations.Accessors
import java.util.List
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import javax.xml.stream.XMLStreamWriter

import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import org.w3c.dom.Element
import java.util.function.Supplier
import de.fhdo.lemma.model_processing.eclipse.launcher.ModelObjectWithPropertyChangeSupport
import java.nio.file.Files
import java.nio.file.Paths
import java.io.IOException
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy
import java.io.ByteArrayOutputStream
import javax.xml.stream.XMLOutputFactory
import java.nio.charset.StandardCharsets
import org.eclipse.debug.core.ILaunchConfiguration
import static de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants.*

class ProcessingConfiguration extends ModelObjectWithPropertyChangeSupport implements Cloneable {
    static val XML_CONFIGURATION_ELEMENT = "processingConfiguration"
    // TODO probably not necessary
    //public static val XML_CONFIGURATION_ATTR_NAME = "name"
    static val XML_CONFIGURATION_ATTR_SOURCE_MODEL_PROJECT_NAME = "sourceModelProjectName"
    static val XML_CONFIGURATION_ATTR_SOURCE_MODEL_FILE_PATH = "sourceModelFilePath"
    static val XML_PROCESSOR_ELEMENT = "processor"
    static val XML_PROCESSOR_TYPE_ELEMENT = "type"
    static val XML_PROCESSOR_BASIC_EXECUTION_COMMAND_ELEMENT = "basicExecutionCommand"
    static val XML_PROCESSOR_PATH_ELEMENT = "path"
    static val XML_ARGUMENTS_ELEMENT = "arguments"
    // TODO probably not necessary
    //static val XML_CONSTRAINTS_ELEMENT = "constraints"

    // TODO probably not necessary
    /*@Accessors(PUBLIC_GETTER)
    var String name

    def setName(String name) {
        firePropertyChange("name", this.name, this.name = name)
    }*/

    @Accessors(PUBLIC_GETTER)
    var String sourceModelProjectName

    def setSourceModelProjectName(String sourceModelProjectName) {
        firePropertyChange("sourceModelProjectName", this.sourceModelProjectName,
            this.sourceModelProjectName = sourceModelProjectName)
    }

    @Accessors(PUBLIC_GETTER)
    var String sourceModelFilePath

    def setSourceModelFilePath(String sourceModelFilePath) {
        firePropertyChange("sourceModelFilePath", this.sourceModelFilePath,
            this.sourceModelFilePath = sourceModelFilePath)
    }

    @Accessors
    var ProcessorExecutableType processorExecutableType

    def setProcessorExecutableType(ProcessorExecutableType processorExecutableType) {
        firePropertyChange("processorExecutableType", this.processorExecutableType,
            this.processorExecutableType = processorExecutableType)
    }

    @Accessors(PUBLIC_GETTER)
    var String processorBasicExecutionCommand

    def setProcessorBasicExecutionCommand(String processorBasicExecutionCommand) {
        firePropertyChange("processorBasicExecutionCommand", this.processorBasicExecutionCommand,
            this.processorBasicExecutionCommand = processorBasicExecutionCommand)
    }

    @Accessors
    var String processorExecutablePath

    def setProcessorExecutablePath(String processorExecutablePath) {
        firePropertyChange("processorExecutablePath", this.processorExecutablePath,
            this.processorExecutablePath = processorExecutablePath)
    }

    new() {
        // NOOP
    }

    new(
        String sourceModelProjectName,
        String sourceModelFilePath,
        ProcessorExecutableType processorExecutableType,
        String processorExecutablePath,
        String processorBasicExecutionCommand
    ) {
        this.sourceModelProjectName = sourceModelProjectName
        this.sourceModelFilePath = sourceModelFilePath
        this.processorExecutableType = processorExecutableType
        this.processorExecutablePath = processorExecutablePath
        this.processorBasicExecutionCommand = processorBasicExecutionCommand
    }

    @Accessors(PUBLIC_GETTER)
    var arguments = <Argument>newArrayList

    // TODO probably not necessary
    /*@Accessors(PUBLIC_GETTER)
    var constraints = <Constraint>newArrayList*/

    override equals(Object o) {
        return if (o === this)
            true
        else if (!(o instanceof ProcessingConfiguration))
            false
        else {
            val otherConfig = o as ProcessingConfiguration
            //name == otherConfig.name &&
                sourceModelProjectName == otherConfig.sourceModelProjectName &&
                sourceModelFilePath == otherConfig.sourceModelFilePath &&
                processorExecutableType == otherConfig.processorExecutableType &&
                processorBasicExecutionCommand == otherConfig.processorBasicExecutionCommand &&
                processorExecutablePath == otherConfig.processorExecutablePath &&
                equalLists(arguments, otherConfig.arguments)// &&
                //equalLists(constraints, otherConfig.constraints)
        }
    }

    override clone() {
        val clone = super.clone() as ProcessingConfiguration
        clone.arguments = newArrayList(arguments.map[clone() as Argument])
        //clone.constraints = newArrayList(constraints.map[clone() as Constraint])
        return clone
    }

    // Derived during (de)serialization of configuration, and thus not considered by equals() or
    // clone()
    // TODO probably not necessary
    /* @Accessors(PUBLIC_GETTER)
    var ConstraintsMatcher constraintsMatcher = null*/

    def void serializeToXml(XMLStreamWriter writer) {
        //ensureValidState()

        writer.writeStartElement(XML_CONFIGURATION_ELEMENT)
        // TODO probably not necessary
        //writer.writeAttribute(XML_CONFIGURATION_ATTR_NAME, name)
        writer.writeAttribute(XML_CONFIGURATION_ATTR_SOURCE_MODEL_PROJECT_NAME,
            sourceModelProjectName)
        writer.writeAttribute(XML_CONFIGURATION_ATTR_SOURCE_MODEL_FILE_PATH, sourceModelFilePath)
        writer.writeProcessorInformationToXml
        arguments.serializeToXml(writer, XML_ARGUMENTS_ELEMENT)
        //constraints.serializeToXml(writer, XML_CONSTRAINTS_ELEMENT)
        //constraintsMatcher = new ConstraintsMatcher(constraints)
        writer.writeEndElement()
    }

    def convertToInternalRepresentation() {
        arguments.forEach[it.convertToInternalRepresentation()]
        // TODO probably not necessary
        //constraints.forEach[it.convertToInternalRepresentation()]
    }

    def getSourceModelFile() {
        try {
            validSourceModelProjectName(sourceModelProjectName)
            validSourceModelFilePath(sourceModelProjectName, sourceModelFilePath)
        } catch (IllegalArgumentException ex) {
            return null
        }

        return findFileInWorkspaceProject(sourceModelProjectName, sourceModelFilePath)
    }

    def validateInUserRepresentation() {
        // TODO probably not necessary
        //validName(name)
        validSourceModelProjectName(sourceModelProjectName)
        validSourceModelFilePath(sourceModelProjectName, sourceModelFilePath)
        validProcessorExecutableType(processorExecutableType)
        validProcessorBasicExecutionCommand(processorExecutableType, processorBasicExecutionCommand)
        validProcessorExecutablePath(processorExecutableType, processorBasicExecutionCommand,
            processorExecutablePath)
        validArgumentsInUserRepresentation(this, arguments)
    }

    // TODO probably not necessary
    /*static def validName(String name) {
        notNullOrEmpty(name, "Processing configuration name must not be empty")
    }*/

    static def validSourceModelProjectName(String sourceModelProjectName) {
        notNullOrEmpty(sourceModelProjectName, "Processing configuration must specify a source " +
            "model project")
        if (findProjectInCurrentWorkspace(sourceModelProjectName) === null)
            throw new IllegalArgumentException("Source model project does not exist in workspace")
    }

    static def validSourceModelFilePath(String sourceModelProjectName, String sourceModelFilePath) {
        validSourceModelProjectName(sourceModelProjectName)
        notNullOrEmpty(sourceModelFilePath, "Processing configuration must specify the path to a " +
            "source model file relative to a source model project path")

        notNull(
            findFileInWorkspaceProject(sourceModelProjectName, sourceModelFilePath),
            "Source model file does not exist in source model project"
        )
    }

    static def validProcessorExecutableType(ProcessorExecutableType processorExecutableType) {
        notNull(processorExecutableType, "Processing configuration must specify type of " +
            "processor executable")
    }

    static def validProcessorBasicExecutionCommand(ProcessorExecutableType processorExecutableType,
        String processorExecutionCommand) {
        notNull(processorExecutionCommand, "Processing configuration must specify a basic " +
            "execution command")
        if (processorExecutableType == ProcessorExecutableType.LOCAL_DOCKER_IMAGE)
            notEmpty(processorExecutionCommand, "Docker-based processing configurations must " +
                "specify a basic execution command")
    }

    static def validProcessorExecutablePath(
        ProcessorExecutableType processorExecutableType,
        String processorExecutionCommand,
        String processorExecutablePath
    ) {
        notNullOrEmpty(processorExecutablePath, "Processing configuration must specify path to " +
            "processor executable")
        if (processorExecutableType == ProcessorExecutableType.LOCAL_JAVA_PROGRAM)
            processorExecutablePath.validJavaProcessorExecutablePath
        else if (processorExecutableType == ProcessorExecutableType.LOCAL_DOCKER_IMAGE)
            processorExecutablePath.validDockerProcessorExecutablePath(processorExecutionCommand)
    }

    private static def validJavaProcessorExecutablePath(String processorExecutablePath) {
        if (!Files.isRegularFile(Paths.get(processorExecutablePath)))
            throw new IllegalArgumentException("Executable processor file does not exist")
    }

    private static def validDockerProcessorExecutablePath(String processorExecutablePath,
        String processorExecutionCommand) {
        notEmpty(processorExecutablePath, "Processing configuration must specify Docker image name")
        notNullOrEmpty(processorExecutionCommand, "Validity of Docker image not determinable " +
            "because no processor executable is specified")

        val imageValidationCommand = '''«processorExecutionCommand» inspect ''' +
            processorExecutablePath
        try {
            val commandResult = executeShellCommandBlocking(imageValidationCommand, 50, 4)
            if (commandResult.key == 1)
                throw new IllegalArgumentException("Image does not exist")
            else if (commandResult.key > 1)
                throw new IllegalArgumentException("Validity of Docker image not determinable. " +
                    '''«imageValidationCommand» returned with exit code «commandResult.key»: ''' +
                    commandResult.value)
        } catch (IOException ex) {
            throw new IllegalArgumentException("Validity of Docker image not determinable: " +
                '''Invalid validation command "«imageValidationCommand»"''')
        }
    }

    static def validArgumentsInUserRepresentation(ProcessingConfiguration configuration,
        List<Argument> arguments) {
        arguments?.forEach[it.validateInUserRepresentation(configuration)]
    }

    private def writeProcessorInformationToXml(XMLStreamWriter writer) {
        writer.writeStartElement(XML_PROCESSOR_ELEMENT)

        writer.writeStartElement(XML_PROCESSOR_TYPE_ELEMENT)
        writer.writeCharacters(processorExecutableType.name ?: "")
        writer.writeEndElement()

        writer.writeStartElement(XML_PROCESSOR_BASIC_EXECUTION_COMMAND_ELEMENT)
        writer.writeCharacters(processorBasicExecutionCommand ?: "")
        writer.writeEndElement()

        writer.writeStartElement(XML_PROCESSOR_PATH_ELEMENT)
        writer.writeCharacters(processorExecutablePath ?: "")
        writer.writeEndElement()

        writer.writeEndElement()
    }

    private def serializeToXml(
        List<? extends ProcessingConfigurationItem> items,
        XMLStreamWriter writer,
        String startElementTag
    ) {
        writer.writeStartElement(startElementTag)
        items.forEach[serializeToXmlInternal(writer)]
        writer.writeEndElement()
    }

    // TODO probably not necessary
    /*def matchesConstraints(String filename) {
        return constraintsMatcher.matches(filename)
    }*/

    static def deserializeFromXml(String xml) {
        val configurationRoot = getRootElementWithTag(parseXmlString(xml),
            XML_CONFIGURATION_ELEMENT)
        if (configurationRoot === null)
            throw new IllegalArgumentException("Error during deserialization of processing " +
                '''configuration: Root XML element «XML_CONFIGURATION_ELEMENT» not found''')

        val sourceModelProjectName = configurationRoot
            .getAttribute(XML_CONFIGURATION_ATTR_SOURCE_MODEL_PROJECT_NAME)
        val sourceModelFilePath = configurationRoot
            .getAttribute(XML_CONFIGURATION_ATTR_SOURCE_MODEL_FILE_PATH)
        val processorInformation = configurationRoot.processorInformation
        val configuration = new ProcessingConfiguration(
            sourceModelProjectName,
            sourceModelFilePath,
            processorInformation.processorExecutableType,
            processorInformation.processorExecutablePath,
            processorInformation.processorBasicExecutionCommand
        )

        //val configuration = new ProcessingConfiguration()
        // TODO probably not necessary
        //configuration.name = element.getAttribute(XML_CONFIGURATION_ATTR_NAME)
        /*configuration.sourceModelProjectName = configurationRoot
            .getAttribute(XML_CONFIGURATION_ATTR_SOURCE_MODEL_PROJECT_NAME)
        configuration.sourceModelFilePath = configurationRoot
            .getAttribute(XML_CONFIGURATION_ATTR_SOURCE_MODEL_FILE_PATH)
        configuration.addProcessorInformationFromXml(configurationRoot)*/
        configuration.arguments = initializeItemsFromXml([new Argument()], configurationRoot,
            XML_ARGUMENTS_ELEMENT, Argument.XML_ARGUMENT_ELEMENT)
        // TODO probably not necessary
        /*configuration.constraints = initializeItemsFromXml([new Constraint()], configurationRoot,
            XML_CONSTRAINTS_ELEMENT, Constraint.XML_CONSTRAINT_ELEMENT)*/

        //configuration.ensureValidState()

        // TODO probably not necessary
        //configuration.constraintsMatcher = new ConstraintsMatcher(configuration.constraints)

        return configuration
    }

    def convertToUserRepresentation() {
        arguments.forEach[it.convertToUserRepresentation()]
        //constraints.forEach[it.convertToUserRepresentation()]
    }

    private static def getProcessorInformation(Element element) {
        val processorElement = findChildElementWithTag(element, XML_PROCESSOR_ELEMENT)
        if (processorElement === null)
            return null

        val executableTypeElement = findChildElementWithTag(processorElement,
            XML_PROCESSOR_TYPE_ELEMENT)
        val processorExecutableType = try {
                ProcessorExecutableType.valueOf(executableTypeElement.textContent)
            } catch (Exception ex) {
                null
            }

        val processorBasicExecutionCommand = findChildElementWithTag(processorElement,
            XML_PROCESSOR_BASIC_EXECUTION_COMMAND_ELEMENT)?.textContent

        val processorExecutablePath = findChildElementWithTag(processorElement,
            XML_PROCESSOR_PATH_ELEMENT)?.textContent

        return new ProcessorInformation(processorExecutableType, processorBasicExecutionCommand,
            processorExecutablePath)
    }

    private static class ProcessorInformation {
        @Accessors(PUBLIC_GETTER)
        val ProcessorExecutableType processorExecutableType
        @Accessors(PUBLIC_GETTER)
        val String processorBasicExecutionCommand
        @Accessors(PUBLIC_GETTER)
        val String processorExecutablePath

        new (ProcessorExecutableType processorExecutableType, String processorBasicExecutionCommand,
            String processorExecutablePath) {
            this.processorExecutableType = processorExecutableType
            this.processorBasicExecutionCommand = processorBasicExecutionCommand
            this.processorExecutablePath = processorExecutablePath
        }
    }

    private static def <T extends ProcessingConfigurationItem> initializeItemsFromXml(
        Supplier<T> constructItemInstance,
        Element element,
        String elementParentTag,
        String elementTag
    ) {
        val items = <T>newArrayList
        val parentElement = findChildElementWithTag(element, elementParentTag)
        val elements = parentElement.getElementsByTagName(elementTag)
        for (n : 0..<elements.length) {
            val item = constructItemInstance.get()
            item.deserializeFromXmlInternal(elements.item(n) as Element)
            items.add(item)
        }
        return items
    }

    static def setProcessingConfigurationAsAttribute(
        ILaunchConfigurationWorkingCopy launchConfiguration,
        ProcessingConfiguration processingConfiguration
    ) {
        launchConfiguration.mappedResources = #[processingConfiguration.sourceModelFile]

        val out = new ByteArrayOutputStream
        val factory = XMLOutputFactory.newInstance()
        val writer = factory.createXMLStreamWriter(out)
        processingConfiguration.serializeToXml(writer)
        launchConfiguration.setAttribute(PROCESSING_CONFIGURATION_ATTRIBUTE,
            out.toString(StandardCharsets.UTF_8))
        out.close()
        writer.close()
    }

    static def deserializeFrom(ILaunchConfiguration launchConfiguration) {
        val xml = launchConfiguration.getAttribute(PROCESSING_CONFIGURATION_ATTRIBUTE, "")
        if (xml.empty)
            return null

        return deserializeFromXml(xml)
    }
}