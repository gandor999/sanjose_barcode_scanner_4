import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.com.intellij.openapi.vfs.StandardFileSystems.jar

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
//    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.example.san_jose_barcode_scanner_4"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("org.postgresql:postgresql:42.5.0")
    implementation("io.github.cdimascio:dotenv-java:3.2.0")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "san_jose_barcode_scanner_4"
            packageVersion = "2.0.0"
        }
    }

//    tasks.jar {
//        manifest {
//            attributes ["Main-Class"] = application.mainClass
//        }
//    }

    tasks.shadowJar {
        archiveBaseName.set(application.nativeDistributions.packageName)
        archiveVersion.set(application.nativeDistributions.packageVersion)
        manifest {
            attributes["Main-Class"] = application.mainClass
        }
    }
}


