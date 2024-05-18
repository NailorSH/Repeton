import java.util.Properties

plugins {
    kotlin("kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
}

// Считывание значений из local.properties
val secretsProperties = Properties().apply {
    val secretsPropertiesFile = rootProject.file("secrets.properties")
    if (secretsPropertiesFile.exists()) {
        secretsPropertiesFile.inputStream().use { fis -> load(fis) }
    }
}

val clientId: String = secretsProperties.getProperty("clientId", "")
val clientSecret: String = secretsProperties.getProperty("clientSecret", "")

android {
    namespace = "com.nailorsh.repeton"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nailorsh.repeton"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Добавление значений в Manifest Placeholders.
        addManifestPlaceholders(
            mapOf(
                "VKIDRedirectHost" to "vk.com", // обычно vk.com
                "VKIDRedirectScheme" to "vk$clientId", // обычно vk{ID приложения}
                "VKIDClientID" to clientId,
                "VKIDClientSecret" to clientSecret
            )
        )
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

val daggerVersion = "2.51"
val vkIdSdkVersion = "1.3.2"

dependencies {
    // For VK ID SDK
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation("com.vk.id:onetap-compose:${vkIdSdkVersion}")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:$daggerVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:hilt-compiler:$daggerVersion")

    implementation("androidx.compose.material:material-icons-core:1.6.4")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.02"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.7") //либа для навигации
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.02"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.ui:ui-viewbinding:1.6.3")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")

    // Coil
    implementation("io.coil-kt:coil-compose:2.6.0")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}