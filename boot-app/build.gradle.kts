plugins {
    id("cmtbuild.spring-boot-conventions")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
}

application {
    mainClass.set("io.cmt.camunda_pilot.App")
}
