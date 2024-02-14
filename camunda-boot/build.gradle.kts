plugins {
    id("cmtbuild.spring-boot-conventions")
    alias(libs.plugins.vaadin.plugin)
}

dependencies {
    implementation("com.vaadin:vaadin-spring-boot-starter")
    implementation("com.zaxxer:HikariCP")
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(libs.camunda.keycloak.plugin)
    implementation(libs.camunda.springboot.starter)
    implementation(libs.camunda.springboot.starter.webapp)
    implementation(platform(libs.vaadin.bom))

    developmentOnly("org.springframework.boot:spring-boot-devtools")

}

application {
    mainClass.set("io.cmt.camunda_pilot.camunda.boot.CamundaBootApp")
}
