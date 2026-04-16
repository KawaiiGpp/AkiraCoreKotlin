import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.papermc.paperweight.userdev.ReobfArtifactConfiguration

plugins {
    `maven-publish`
    kotlin("jvm") version "2.1.20"
    id("com.gradleup.shadow") version "8.3.10"
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "com.akira"
version = "1.4.0"

val serverFolder = "D:/Server/PaperDebug"
val pluginFolder = "$serverFolder/plugins"

java { withSourcesJar() }

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    testImplementation(kotlin("test"))
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("shadow")
    dependencies { exclude(dependency("org.jetbrains:annotations")) }
    relocate("kotlin", "com.akira.shadow.kotlin")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

tasks.register<Copy>("deployPlugin") {
    dependsOn("shadowJar")

    from(tasks.named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
    into(pluginFolder)
    rename { "${project.name}.jar" }
}

tasks.build { dependsOn("deployPlugin") }

tasks.named("publishToMavenLocal") { finalizedBy("deployPlugin") }

tasks.withType<ProcessResources> {
    inputs.property("checkVersion", project.version)
    filesMatching("plugin.yml") { expand("version" to project.version) }
}

tasks.withType<Test> { useJUnitPlatform() }

kotlin { jvmToolchain(21) }

paperweight { reobfArtifactConfiguration = ReobfArtifactConfiguration.MOJANG_PRODUCTION }