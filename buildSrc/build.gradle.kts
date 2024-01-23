plugins {
    `kotlin-dsl`
}

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

dependencies {
    implementation(libs.spotbugs.plugin.lib)
    implementation(libs.spring.plugin.dependencies.lib)
    implementation(libs.spring.plugin.boot.lib)

    // https://stackoverflow.com/questions/67795324/gradle7-version-catalog-how-to-use-it-with-buildsrc
    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
