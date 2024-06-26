@file:Suppress("UNUSED_EXPRESSION")

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.manager.helo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.manager.helo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // glider
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //RxJava
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation ("io.reactivex.rxjava3:rxjava:3.0.0")
    implementation ("io.reactivex:rxjava:1.1.6")
    implementation ("io.reactivex:rxandroid:1.2.1")
    implementation ("com.squareup.retrofit2:adapter-rxjava:2.1.0")
    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")
    implementation ("com.squareup.retrofit:retrofit:2.0.0-beta2")
    implementation ("com.squareup.retrofit:converter-gson:2.0.0-beta2")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.12.0")
    //badge
    implementation ("com.nex3z:notification-badge:1.0.4")
    //even bus
    implementation("org.greenrobot:eventbus:3.3.1")
    //paper
    implementation ("io.github.pilgr:paperdb:2.7.2")
    //gson
    implementation ("com.google.code.gson:gson:2.10.1")
    //lottie
    implementation("com.airbnb.android:lottie:6.4.0")
    //neumorphism
    implementation ("com.github.fornewid:neumorphism:0.3.2")
    //multiDex
    implementation ("androidx.multidex:multidex:2.0.1")
    //imagePicker
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    implementation ("com.google.android.gms:play-services-safetynet:18.0.1")

}