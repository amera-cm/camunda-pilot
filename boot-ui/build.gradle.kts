plugins {
    id("cmtbuild.spring-boot-conventions")
    alias(libs.plugins.vaadin.plugin)
}

dependencies {
    implementation("com.vaadin:vaadin-spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(platform(libs.vaadin.bom))

    developmentOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

application {
    mainClass.set("io.cmt.camunda_pilot.boot.BootAppUi")
}
