plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.lfstudios"
version = "1.0.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    implementation("net.kyori:adventure-text-minimessage:4.17.0")
    implementation("com.zaxxer:HikariCP:5.1.0")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
    shadowJar {
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
}
