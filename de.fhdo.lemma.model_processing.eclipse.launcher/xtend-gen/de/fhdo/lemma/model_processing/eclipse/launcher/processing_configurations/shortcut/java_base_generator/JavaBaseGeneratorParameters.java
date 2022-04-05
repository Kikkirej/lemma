package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class JavaBaseGeneratorParameters {
  public enum GenletType {
    DDD,
    
    SPRING_CLOUD,
    
    SPRING_CLOUD_CQRS,
    
    SPRING_CLOUD_DOMAIN_EVENTS,
    
    SPRING_CLOUD_KAFKA;
  }
  
  private static final String DOCKER_GENLETS_BASEPATH = "/home/genlets";
  
  private static final Map<JavaBaseGeneratorParameters.GenletType, String> DOCKER_GENLET_FILES = Collections.<JavaBaseGeneratorParameters.GenletType, String>unmodifiableMap(CollectionLiterals.<JavaBaseGeneratorParameters.GenletType, String>newHashMap(Pair.<JavaBaseGeneratorParameters.GenletType, String>of(JavaBaseGeneratorParameters.GenletType.DDD, "de.fhdo.lemma.model_processing.code_generation.ddd.jar"), Pair.<JavaBaseGeneratorParameters.GenletType, String>of(JavaBaseGeneratorParameters.GenletType.SPRING_CLOUD, "de.fhdo.lemma.model_processing.code_generation.springcloud.jar"), Pair.<JavaBaseGeneratorParameters.GenletType, String>of(JavaBaseGeneratorParameters.GenletType.SPRING_CLOUD_CQRS, "de.fhdo.lemma.model_processing.code_generation.springcloud.cqrs.jar"), Pair.<JavaBaseGeneratorParameters.GenletType, String>of(JavaBaseGeneratorParameters.GenletType.SPRING_CLOUD_DOMAIN_EVENTS, "de.fhdo.lemma.model_processing.code_generation.springcloud.domain_events.jar"), Pair.<JavaBaseGeneratorParameters.GenletType, String>of(JavaBaseGeneratorParameters.GenletType.SPRING_CLOUD_KAFKA, "de.fhdo.lemma.model_processing.code_generation.springcloud.kafka.jar")));
  
  public static final String DEFAULT_DOCKER_IMAGE_NAME = "lemma/java_generator:latest";
  
  public static final String GENERATION_SERIALIZER_PARAMETER = "code_generation_serializer";
  
  public static final String GENLET_PARAMETER = "--genlet";
  
  public static final String ALTERNATIVE_INTERMEDIATE_SERVICE_MODEL_PARAMETER = "--alternative_intermediate_service_model";
  
  private static JavaBaseGeneratorParameters instance;
  
  private final BiMap<String, String> availableCodeGenerationSerializers = HashBiMap.<String, String>create();
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private final String defaultGenerationSerializer;
  
  private JavaBaseGeneratorParameters() {
    this.availableCodeGenerationSerializers.put("extended-generation-gap", "Extended Generation Gap");
    this.availableCodeGenerationSerializers.put("generation-gap", "Generation Gap");
    this.availableCodeGenerationSerializers.put("plain", "Plain Generation");
    this.defaultGenerationSerializer = ((String[])Conversions.unwrapArray(this.availableCodeGenerationSerializers.keySet(), String.class))[0];
  }
  
  public static JavaBaseGeneratorParameters instance() {
    if ((JavaBaseGeneratorParameters.instance == null)) {
      JavaBaseGeneratorParameters _javaBaseGeneratorParameters = new JavaBaseGeneratorParameters();
      JavaBaseGeneratorParameters.instance = _javaBaseGeneratorParameters;
    }
    return JavaBaseGeneratorParameters.instance;
  }
  
  public List<String> getSerializerLabels() {
    final Function1<String, String> _function = (String it) -> {
      return it;
    };
    return IterableExtensions.<String>toList(IterableExtensions.<String, String>sortBy(this.availableCodeGenerationSerializers.values(), _function));
  }
  
  public String getSerializerLabel(final String serializerParameterName) {
    return this.availableCodeGenerationSerializers.get(serializerParameterName);
  }
  
  public String defaultSerializerLabel() {
    return this.availableCodeGenerationSerializers.get(this.defaultGenerationSerializer);
  }
  
  public String getSerializerParameterName(final String serializerLabel) {
    return this.availableCodeGenerationSerializers.inverse().get(serializerLabel);
  }
  
  public String getDockerGenletFilePath(final JavaBaseGeneratorParameters.GenletType genletType) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(JavaBaseGeneratorParameters.DOCKER_GENLETS_BASEPATH);
    _builder.append("/");
    String _get = JavaBaseGeneratorParameters.DOCKER_GENLET_FILES.get(genletType);
    _builder.append(_get);
    return _builder.toString();
  }
  
  @Pure
  public String getDefaultGenerationSerializer() {
    return this.defaultGenerationSerializer;
  }
}
