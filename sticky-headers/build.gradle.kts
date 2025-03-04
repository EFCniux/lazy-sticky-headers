import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.jetbrainsCompose)
    //id("module.publication")
    alias(libs.plugins.dokka)
    alias(libs.plugins.nexusPlugin)
}

version = "0.1.0-alpha04"

kotlin {
    jvm(name = "desktop")
    androidTarget {
        publishLibraryVariants("release")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.foundation)
                api(compose.runtime)
                //api(compose.material3)
                //api(compose.material)
                //put your multiplatform dependencies here
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }

    //explicitApi()
}

android {
    namespace = "me.gingerninja.lazy"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    buildFeatures {
        buildConfig = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint {
        abortOnError = false
    }
}

/*tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xexplicit-api=strict")
    }
}*/

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(libs.versions.jvmTarget.get())
    }
}

tasks.withType<JavaCompile>().configureEach {
    this.targetCompatibility = libs.versions.jvmTarget.get()
    this.sourceCompatibility = libs.versions.jvmTarget.get()
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01)

    coordinates("me.gingerninja.lazy", "sticky-headers", version.toString())

    pom {
        name = "Lazy Sticky Headers"
        description = "Lazy Sticky Headers for Compose Multiplatform"
        inceptionYear = "2024"
        url = "https://github.com/gregkorossy/lazy-sticky-headers/"

        licenses {
            license {
                name = "The Apache Software License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "gregkorossy"
                name = "Gergely Kőrössy"
                url = "https://github.com/gregkorossy/"
            }
        }
        scm {
            url = "https://github.com/gregkorossy/lazy-sticky-headers/"
            connection = "scm:git:git://github.com/gregkorossy/lazy-sticky-headers.git"
            developerConnection = "scm:git:ssh://git@github.com/gregkorossy/lazy-sticky-headers.git"
        }
        issueManagement {
            system = "GitHub"
            url = "https://github.com/gregkorossy/lazy-sticky-headers/issues"
        }
        ciManagement {
            system = "GitHub Actions"
            url = "https://github.com/gregkorossy/lazy-sticky-headers/actions"
        }
    }

    // signAllPublications()
}

/*kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

composeCompiler {
    enableStrongSkippingMode = true
}*/

/* ℹ️ Interesting commands:

- publish to local maven: ./gradlew publishToMavenLocal --no-configuration-cache
- API dump: ./gradlew :sticky-headers:apiDump
- API check: ./gradlew :sticky-headers:apiCheck
- check project: ./gradlew :sticky-headers:check
- Dokka: ./gradlew sticky-headers:dokkaHtml --no-configuration-cache

 */