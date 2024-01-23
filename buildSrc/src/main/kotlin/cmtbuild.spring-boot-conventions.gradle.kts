plugins {
    application
    id("cmtbuild.java-conventions")
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
}

configurations {
    all {
        exclude(
                group = "org.springframework.boot",
                module = "spring-boot-starter-logging"
        )
    }
}
