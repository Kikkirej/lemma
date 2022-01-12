package de.fhdo.lemma.service.openapi.test

import static org.junit.Assert.assertTrue

import org.slf4j.Logger
import org.junit.Before
import org.slf4j.LoggerFactory
import de.fhdo.lemma.service.openapi.LemmaDataSubGenerator
import org.junit.Test
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.parser.core.models.ParseOptions
import java.io.File
import de.fhdo.lemma.service.openapi.LemmaTechnologySubGenerator
import de.fhdo.lemma.service.openapi.LemmaServiceSubGenerator

class GeneratorsTest {
    Logger logger;
    val localSchema = new File("test-schemas/openapi.json").toURI.toString
    LemmaDataSubGenerator dataGenerator
    LemmaTechnologySubGenerator technologyGenerator
    LemmaServiceSubGenerator serviceGenerator
    OpenAPI openAPI

    @Before
    def void setup() throws Exception {
        logger = LoggerFactory.getLogger(ValidationTest)
        // Retrieval of parsed openapi
        val parseOptions = new ParseOptions()
        parseOptions.setResolve(true)
        parseOptions.setFlatten(true)
        val result = new OpenAPIParser().readLocation(localSchema, null, parseOptions)
        openAPI = result.openAPI
    }

    @Test
    def void dataTest() throws Exception {
        logger.info("Starting generation of LEMMA Data Model...")
        dataGenerator= new LemmaDataSubGenerator(openAPI, System.getProperty("user.dir")+
          "/test-model-gen/", "test.data")
        dataGenerator.generate
        assertTrue(new File(System.getProperty("user.dir")+"/model-gen/test.data").exists)
    }
    @Test
    def void technologyTest() throws Exception {
        logger.info("Starting generation of LEMMA Technology Model...")
        technologyGenerator= new LemmaTechnologySubGenerator(openAPI, System.getProperty("user.dir")+
          "/test-model-gen/", "test.technology")
        technologyGenerator.generate
        assertTrue(new File(System.getProperty("user.dir")+"/model-gen/test.technology").exists)
    }

    @Test
    def void serviceTest() throws Exception {
        logger.info("Starting generation of LEMMA Service Model...")
        dataGenerator= new LemmaDataSubGenerator(openAPI, System.getProperty("user.dir")+
          "/test-model-gen/", "test.data")
        val dataModel = "test.data" -> dataGenerator.generate

        technologyGenerator= new LemmaTechnologySubGenerator(openAPI, System.getProperty("user.dir")+
          "/test-model-gen/", "test.technology")
        val techModel = "test.technology" -> technologyGenerator.generate

        serviceGenerator = new LemmaServiceSubGenerator(openAPI, dataModel, techModel,
            System.getProperty("user.dir")+"/test-model-gen/", "test.service")
        serviceGenerator.generate("test")

        assertTrue(new File(System.getProperty("user.dir")+"/model-gen/test.service").exists)
    }
}