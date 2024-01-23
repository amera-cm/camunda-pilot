import com.github.spotbugs.snom.SpotBugsTask
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    java
    jacoco
    checkstyle
    id("cmtbuild.commons")
    id("com.github.spotbugs")
    id("io.spring.dependency-management")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

repositories {
    maven {
        name = providers.systemProperty("cmt.mvn.name").getOrElse(System.getenv("CMT_MVN_NAME"))
        url = uri(providers.systemProperty("cmt.mvn.url").getOrElse(System.getenv("CMT_MVN_URL")))
        credentials {
            username = providers.systemProperty("cmt.mvn.username").getOrElse(System.getenv("CMT_MVN_USERNAME"))
            password = providers.systemProperty("cmt.mvn.password").getOrElse(System.getenv("CMT_MVN_PASSWORD"))
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    implementation(libs.cmt.commons)
    implementation(libs.spotbugs.annotations)
    compileOnly(libs.lombok)

    annotationProcessor(libs.lombok)

    "testImplementation"("org.hamcrest:hamcrest-library")
    "testImplementation"("org.junit.jupiter:junit-jupiter-api")
    "testImplementation"("org.mockito:mockito-core")
    "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine")
    testImplementation(libs.lombok)

    testAnnotationProcessor(libs.lombok)


    checkstyle(libs.checkstyle)
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<Checkstyle>().configureEach {
    maxWarnings = 0
}

tasks.withType<SpotBugsTask>().configureEach {
    reports {
        create("html") {
            enabled = true
        }
        create("xml") {
            enabled = true
        }
    }
}

tasks.withType<JacocoReport> {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
    }
}

tasks.withType<Javadoc> {
    isFailOnError = false
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

spotbugs {
    excludeFilter.set(file("../config/spotbugs/spotbugs-exclude.xml"))
}
