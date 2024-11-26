import com.android.build.gradle.internal.utils.findKaptOrKspConfigurationsForVariant

plugins {
    //alias(libs.plugins.android.application)
    //alias(libs.plugins.google.gms.google.services)
    kotlin("android")
    //alias(libs.plugins.kotlin.ksp)
    //kotlin("ksp")
    //id("com.android.application")
    //id("com.google.devtools.ksp")
    id("com.android.application")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.smartknock"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.smartknock"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.annotation.jvm)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //implementation(project.dependencies.platform("com.google.firebase:firebase-database:21.0.0"))
    implementation(libs.firebase.auth)
    implementation(libs.glide)
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-messaging")
    // ksp(libs.glide.ksp)
}