package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import org.eclipse.xtend.lib.annotations.Accessors

class JavaBaseGeneratorParameters {
    static val DOCKER_GENLETS_BASEPATH = "/home/genlets"
    enum GenletType {
        DDD,
        SPRING_CLOUD,
        SPRING_CLOUD_CQRS,
        SPRING_CLOUD_DOMAIN_EVENTS,
        SPRING_CLOUD_KAFKA
    }
    static val DOCKER_GENLET_FILES = #{
        GenletType.DDD -> "de.fhdo.lemma.model_processing.code_generation.ddd.jar",
        GenletType.SPRING_CLOUD -> "de.fhdo.lemma.model_processing.code_generation.springcloud.jar",
        GenletType.SPRING_CLOUD_CQRS
            -> "de.fhdo.lemma.model_processing.code_generation.springcloud.cqrs.jar",
        GenletType.SPRING_CLOUD_DOMAIN_EVENTS
            -> "de.fhdo.lemma.model_processing.code_generation.springcloud.domain_events.jar",
        GenletType.SPRING_CLOUD_KAFKA
            -> "de.fhdo.lemma.model_processing.code_generation.springcloud.kafka.jar"
    }
    public static val DEFAULT_DOCKER_IMAGE_NAME = "lemma/java_generator:latest"

    public static val GENERATION_SERIALIZER_PARAMETER = "code_generation_serializer"
    public static val GENLET_PARAMETER = "--genlet"
    public static val ALTERNATIVE_INTERMEDIATE_SERVICE_MODEL_PARAMETER
        = "--alternative_intermediate_service_model"

    static var JavaBaseGeneratorParameters instance

    val BiMap<String, String> availableCodeGenerationSerializers = HashBiMap.create()
    @Accessors(PUBLIC_GETTER)
    val String defaultGenerationSerializer

    private new() {
        availableCodeGenerationSerializers.put("extended-generation-gap", "Extended Generation Gap")
        availableCodeGenerationSerializers.put("generation-gap", "Generation Gap")
        availableCodeGenerationSerializers.put("plain", "Plain Generation")
        defaultGenerationSerializer = availableCodeGenerationSerializers.keySet.get(0)
    }

    static def instance() {
        if (instance === null)
            instance = new JavaBaseGeneratorParameters()
        return instance
    }

    def getSerializerLabels() {
        return availableCodeGenerationSerializers.values.sortBy[it].toList
    }

    def getSerializerLabel(String serializerParameterName) {
        return availableCodeGenerationSerializers.get(serializerParameterName)
    }

    def defaultSerializerLabel() {
        return availableCodeGenerationSerializers.get(defaultGenerationSerializer)
    }

    def getSerializerParameterName(String serializerLabel) {
        return availableCodeGenerationSerializers.inverse.get(serializerLabel)
    }

    def getDockerGenletFilePath(GenletType genletType) {
        return '''«DOCKER_GENLETS_BASEPATH»/«DOCKER_GENLET_FILES.get(genletType)»'''
    }
}