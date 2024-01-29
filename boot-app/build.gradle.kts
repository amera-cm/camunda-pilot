plugins {
    id("cmtbuild.spring-boot-conventions")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(libs.keycloak.adminClient)
    implementation(libs.keycloak.core)
    implementation(libs.keycloak.policyEnforcer)
}

application {
    mainClass.set("io.cmt.camunda_pilot.App")
}
