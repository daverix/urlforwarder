plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "net.daverix.urlforward"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 33
        versionCode = 12
        versionName = "0.5.0"
        testInstrumentationRunner = "net.daverix.urlforward.UrlForwarderJunitRunner"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
        }
    }
    lint {
        abortOnError = false
    }

    kotlinOptions {
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
        resources {
            // for JNA and JNA-platform
            excludes += listOf("META-INF/AL2.0", "META-INF/LGPL2.1")
        }
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(libs.versions.jdk.map(JavaLanguageVersion::of))
    }
}

dependencies {
    // kotlin
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.coroutines.android)

    // dagger
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)

    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.compiler)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.navigation.compose)

    // compose
    implementation(platform(libs.compose.bom))

    // android studio preview
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    // material
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material.icons.extended)

    // ui test
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.compose.ui.test.manifest)
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.core)

    // unit test
    testImplementation(libs.junit.junit)
    testImplementation(libs.truth)
}

kapt {
    correctErrorTypes = true
}
