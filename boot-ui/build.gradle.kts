plugins {
    id("cmtbuild.spring-boot-conventions")
    alias(libs.plugins.vaadin.plugin)
}

dependencies {
    implementation("com.vaadin:vaadin-spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation(platform(libs.vaadin.bom))

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

application {
    mainClass.set("io.cmt.camunda_pilot.boot.BootAppUi")
}
