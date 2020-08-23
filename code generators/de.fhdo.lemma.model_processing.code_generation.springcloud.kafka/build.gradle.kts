plugins {
    kotlin("jvm") version "1.3.72"
    maven
}

group = "de.fhdo.lemma.model_processing.code_generation.springcloud.kafka"

repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
}

buildscript {
    extra.set("jansiVersion", "1.17.1")
    extra.set("javaBaseGeneratorVersion", version)
    extra.set("javaParserVersion", "3.14.10")
    extra.set("lemmaEclipsePluginsVersion", version)
    extra.set("lsp4jVersion", "0.9.0")
    extra.set("modelProcessingVersion", version)
    extra.set("picocliVersion", "3.9.3")
    extra.set("xmlBuilderVersion", "1.5.2")
}

dependencies {
    val jansiVersion: String by rootProject.extra
    val javaBaseGeneratorVersion: String by rootProject.extra
    val javaParserVersion: String by rootProject.extra
    val lemmaEclipsePluginsVersion: String by rootProject.extra
    val lsp4jVersion: String by rootProject.extra
    val modelProcessingVersion: String by rootProject.extra
    val picocliVersion: String by rootProject.extra
    val xmlBuilderVersion: String by rootProject.extra

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("com.github.javaparser:javaparser-core:$javaParserVersion")
    implementation("de.fhdo.lemma:de.fhdo.lemma.typechecking:$lemmaEclipsePluginsVersion")
    implementation("de.fhdo.lemma.data.datadsl:de.fhdo.lemma.data.datadsl:$lemmaEclipsePluginsVersion")
    implementation("de.fhdo.lemma.data.datadsl:de.fhdo.lemma.data.datadsl.metamodel:$lemmaEclipsePluginsVersion")
    implementation("de.fhdo.lemma.intermediate:de.fhdo.lemma.data.intermediate.metamodel:$lemmaEclipsePluginsVersion")
    implementation("de.fhdo.lemma.intermediate:de.fhdo.lemma.service.intermediate.metamodel:" +
        lemmaEclipsePluginsVersion)
    implementation("de.fhdo.lemma.live_validation:de.fhdo.lemma.live_validation:$lemmaEclipsePluginsVersion")
    implementation("de.fhdo.lemma.model_processing:de.fhdo.lemma.model_processing:$modelProcessingVersion")
    implementation("de.fhdo.lemma.model_processing.code_generation.java_base:" +
        "de.fhdo.lemma.model_processing.code_generation.java_base:$javaBaseGeneratorVersion")
    implementation("de.fhdo.lemma.model_processing.utils:de.fhdo.lemma.model_processing.utils:$modelProcessingVersion")
    implementation("de.fhdo.lemma.servicedsl:de.fhdo.lemma.servicedsl:$lemmaEclipsePluginsVersion")
    implementation("de.fhdo.lemma.technology.mappingdsl:de.fhdo.lemma.technology.mappingdsl:" +
            lemmaEclipsePluginsVersion)
    implementation("de.fhdo.lemma.technology.mappingdsl:" +
            "de.fhdo.lemma.technology.mappingdsl.metamodel:$lemmaEclipsePluginsVersion")
    implementation("de.fhdo.lemma.technology.technologydsl:de.fhdo.lemma.technology.technologydsl:" +
        lemmaEclipsePluginsVersion)
    implementation("de.fhdo.lemma.technology.technologydsl:de.fhdo.lemma.technology.technologydsl.metamodel:" +
        lemmaEclipsePluginsVersion)
    implementation("info.picocli:picocli:$picocliVersion")
    implementation("org.fusesource.jansi:jansi:$jansiVersion")
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j:$lsp4jVersion")
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j.jsonrpc:$lsp4jVersion")
    implementation("org.redundent:kotlin-xml-builder:$xmlBuilderVersion")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}

/**
 * standalone task to create a standalone runnable JAR
 */
val standalone = task("standalone", type = Jar::class) {
    archiveClassifier.set("standalone")

    // Build fat JAR
    from(configurations.compileClasspath.get().filter{ it.exists() }.map { if (it.isDirectory) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)

    manifest {
        attributes("Main-Class" to "de.fhdo.lemma.model_processing.code_generation.springcloud.kafka.KafkaGeneratorKt")

        // Prevent security exception from JAR verifier
        exclude("META-INF/*.DSA", "META-INF/*.RSA", "META-INF/*.SF")
    }
}