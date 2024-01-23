plugins {
    `java-library`
    `maven-publish`
    id("cmtbuild.java-conventions")
}

group = "io.cmt"

publishing {
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
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}
