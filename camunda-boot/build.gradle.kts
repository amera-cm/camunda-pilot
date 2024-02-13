plugins {
    id("cmtbuild.spring-boot-conventions")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("com.h2database:h2")
    implementation("org.postgresql:postgresql")
    implementation("com.zaxxer:HikariCP")
    implementation(libs.camunda.springboot.starter)
    implementation(libs.camunda.springboot.starter.webapp)
    implementation(libs.camunda.keycloak.plugin)
}

application {
    mainClass.set("io.cmt.camunda_pilot.camunda.boot.CamundaBootApp")
}
