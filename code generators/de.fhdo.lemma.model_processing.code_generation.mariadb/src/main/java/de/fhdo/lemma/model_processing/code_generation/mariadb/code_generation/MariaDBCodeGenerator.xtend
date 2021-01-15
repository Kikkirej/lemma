package de.fhdo.lemma.model_processing.code_generation.mariadb.code_generation

import de.fhdo.lemma.model_processing.annotations.CodeGenerationModule
import de.fhdo.lemma.model_processing.builtin_phases.code_generation.AbstractCodeGenerationModule
import de.fhdo.lemma.operation.intermediate.IntermediatePackage
import de.fhdo.lemma.model_processing.languages.LanguageDescription
import de.fhdo.lemma.operation.intermediate.IntermediateOperationModel
import java.io.File
import org.jetbrains.annotations.NotNull
import de.fhdo.lemma.operation.intermediate.IntermediateContainer
import de.fhdo.lemma.model_processing.code_generation.container_base.file.property.OpenedPropertyFiles
import java.nio.file.Paths
import de.fhdo.lemma.operation.intermediate.OperationMicroserviceReference
import de.fhdo.lemma.model_processing.code_generation.container_base.util.Util
import de.fhdo.lemma.model_processing.code_generation.container_base.file.property.SortableProperties
import java.io.ByteArrayInputStream
import de.fhdo.lemma.model_processing.code_generation.container_base.file.property.PropertyFile
import de.fhdo.lemma.model_processing.code_generation.mariadb.template.MariaDBTemplate
import de.fhdo.lemma.operation.intermediate.IntermediateOperationNode
import de.fhdo.lemma.model_processing.code_generation.container_base.file.docker.OpenedDockerComposeFile
import de.fhdo.lemma.operation.intermediate.IntermediateInfrastructureNode

/**
 * Main class of the MariaDB code generation module of the container base code generator.
 *
 * @author <a href="mailto:philip.wizenty@fh-dortmund.de">Philip Wizenty</a>
 */
@CodeGenerationModule(name="mariadb")
class MariaDBCodeGenerator extends AbstractCodeGenerationModule {
    var IntermediateOperationModel model
    val content = <String, String> newHashMap
    public static val MARIADB_TECHNOLOGY = "mariadb.mariadb"


    /**
     * This method is responsible for handling the instance of the intermediate operation model.
     * The corresponding functions are called depending on whether the operation node is an instance
     * of an intermediate container or infrastructure node.
     */
    override execute(String[] phaseArguments, String[] moduleArguments) {
        // Receive the intermediate operation model
        model = intermediateModelResource.contents.get(0) as IntermediateOperationModel

        // Create service-specific MariaDB configurations
        model.containers.forEach[
            val usesMariaDB = it.dependsOnNodes.exists[ it.qualifiedTechnologyName.toLowerCase
                == MARIADB_TECHNOLOGY]

            if (usesMariaDB)
                createContainerSpecificConfiguration(it)
        ]

        // Create MariaDB components, e.g., docker-compose parts and Kubernetes deployments
        model.infrastructureNodes.forEach[node |
            val usesMariaDBTechnology =
                node.qualifiedInfrastructureTechnologyName.toLowerCase == MARIADB_TECHNOLOGY

            if (usesMariaDBTechnology) {
                createMariaDbKubernetesDeployment(node)
                createDockerComposeDeployment(node)
            }
        ]

        // Create application.properties files
        OpenedPropertyFiles.instance.propertyFiles.forEach[
            content.put(it.filePath, it.buildPropertyFile)
        ]

        return withCharset(content, "UTF-8");
    }

    /**
     * Receive the language description for the intermediate operation model package.
     */
    @NotNull
    override getLanguageDescription() {
        new LanguageDescription(IntermediatePackage.eINSTANCE)
    }

    /**
     * Create the container-specific configuration for each deployed Spring-based microservice.
     */
    private def createContainerSpecificConfiguration(IntermediateContainer container) {
        container.deployedServices.forEach[service | createServiceSpecificConfiguration(service)]
    }

    /**
     * Create the service-specific MariaDB configuration in the application.properties file.
     */
    private def createServiceSpecificConfiguration(OperationMicroserviceReference service) {
        val serviceFilePath = Util.buildPathFromQualifiedName(service.qualifiedName)

        // FIXME: This statement only supports Java or Kotlin microservices with the Spring Framework
        val path = Paths.get('''«targetFolder»«File.separator»«serviceFilePath»«File.separator»''' +
            '''/src/main/resources/application.properties''').toString

        /*
         * Load existing PropertyFile from the file path and merge it with a potentially existing
         * PropertyFile.
         */
        val loadedFile = OpenedPropertyFiles.instance.openPropertyFile(path)
        val mergedLoadedyFile = OpenedPropertyFiles.instance.mergePropertyFile(loadedFile)
        OpenedPropertyFiles.instance.add(mergedLoadedyFile)

        /*
         * Create a PropertyFile based on the intermediate operation model configuration and merge
         * it with a potentially existing one.
         */
        val serviceConfig = service.node.getEffectiveConfigurationValues(service)
        val mariaDBConfig = MariaDBTemplate::getPropertiesForMariaDBServiceConfig(serviceConfig)
        val properties = new SortableProperties
        properties.load(new ByteArrayInputStream(mariaDBConfig.getBytes()))
        val propertyFile = new PropertyFile(path, properties)
        val mergedFile = OpenedPropertyFiles.instance.mergePropertyFile(propertyFile)
        OpenedPropertyFiles.instance.add(mergedFile)
    }

    /**
     * Create an IntermediateOperationNode-specific Kubernetes deployment file.
     */
    private def createMariaDbKubernetesDeployment(IntermediateOperationNode node) {
        val kubernetes = MariaDBTemplate::getKubernetesDeploymentForMariaDb(node)
        val path = '''«targetFolder»«File.separator»«node.name»«File.separator»''' +
            '''«node.name.toLowerCase»-deployment.yaml'''

        content.put(path, kubernetes)
    }

    /**
     * Create an IntermediateOperationNode-specific Docker-Compose deployment file.
     */
    private def createDockerComposeDeployment(IntermediateInfrastructureNode node) {
        /*
         * Set targetFolder for docker-compose.yml file and open an existing or create a new
         * docker-compose file
         */
        OpenedDockerComposeFile.instance.init(targetFolder)
        OpenedDockerComposeFile.instance.openExistingDockerComposeFile

        // Build Docker-Compose part based on a operation aspect or node configuration
        val dockerComposeAspect = node.aspects.findFirst[aspect | aspect.name == "ComposePart"]
        if (dockerComposeAspect === null)
            OpenedDockerComposeFile.instance.addOrReplaceDockerComposePart(node.name,
                MariaDBTemplate::getDockerComposeForMariaDB(node))
        else
            OpenedDockerComposeFile.instance.addOrReplaceDockerComposePart(node.name,
                dockerComposeAspect.propertyValues?.get(0).value)

        // Write Docker-Compose file to the file path
        val filePath = '''«targetFolder»«File.separator»docker-compose.yml'''
        content.put(filePath, OpenedDockerComposeFile.instance.toString)
    }
}