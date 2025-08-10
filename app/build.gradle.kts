plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.dagger.hilt)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.kangpismanapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kangpismanapp"
        minSdk = 24
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true

    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.glide)

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")
    implementation ("androidx.fragment:fragment-ktx:1.8.2")

    // ML Kit Barcode Scanning
    implementation ("com.google.mlkit:barcode-scanning:17.2.0")

    // CameraX untuk mempermudah manajemen kamera
    implementation ("androidx.camera:camera-core:1.3.4")
    implementation ("androidx.camera:camera-camera2:1.3.4")
    implementation ("androidx.camera:camera-lifecycle:1.3.4")
    implementation ("androidx.camera:camera-view:1.3.4")

    // Google Sign-In
    implementation ("com.google.android.gms:play-services-auth:21.2.0")


    // OpenStreetMap Library (osmdroid)
    implementation ("org.osmdroid:osmdroid-android:6.1.18")

    // Dependensi untuk PreferenceManager (dibutuhkan oleh osmdroid)
    implementation ("androidx.preference:preference-ktx:1.2.1")

    // Google Play Services untuk mendapatkan lokasi pengguna
    implementation ("com.google.android.gms:play-services-location:21.3.0")

    // QR Code Generator (ZXing)
    implementation ("com.google.zxing:core:3.5.1")

    implementation ("com.google.guava:guava:33.0.0-android")

}