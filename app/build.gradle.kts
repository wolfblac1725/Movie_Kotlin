@file:Suppress("UnstableApiUsage")
import com.android.build.api.dsl.ApplicationBuildType
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}


android {
    namespace = "com.erik.canseco.movies"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.erik.canseco.movies"
        minSdk = 26
        targetSdk = 37
        versionCode = 10
        versionName = "3.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            configureProps(this)

            // ESTA LÍNEA permite correr release en el emulador con la firma de debug
            signingConfig = signingConfigs.getByName("debug")
        }

        debug {
            // También configuramos las props para debug si las necesitas
            configureProps(this)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
fun configureProps( buildType: ApplicationBuildType){
    val data = Properties()
    data.load(FileInputStream(file("../config/data.properties")))
    setBuildPropsByEnv( data, buildType)

}
fun setBuildPropsByEnv(
    props: Properties,
    buildType: ApplicationBuildType,
) {
    props.forEach { (key, value) ->
        buildType.buildConfigField("String", key.toString(), value.toString())
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.google.fonts)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization)

    implementation(libs.androidx.room)
    ksp(libs.androidx.room.compile)
    implementation(libs.androidx.room.paging)

    implementation(libs.google.hilt.android)
    ksp(libs.google.hilt.compile)
    ksp(libs.androidx.hilt.compile)
    implementation(libs.androidx.hilt.navigation)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    implementation(libs.androidx.material.icons)
    implementation(libs.paging.compose)
    implementation(libs.paging.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}