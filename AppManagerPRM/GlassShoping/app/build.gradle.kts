plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.manager.glassshoping"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.manager.glassshoping"
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
        dataBinding = true
        compose = true
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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //glider
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // RxAndroid để dùng AndroidSchedulers.mainThread()
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")

    //RxJava
    implementation("io.reactivex.rxjava3:rxjava:3.0.0")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")
    //notification-badge
    implementation("com.nex3z:notification-badge:1.0.4")
    //evenbus
    implementation("org.greenrobot:eventbus:3.3.1")
    //paper
    implementation("io.github.pilgr:paperdb:2.7.2")
    //gson
    implementation("com.google.code.gson:gson:2.13.1")
    //lottie
    implementation("com.airbnb.android:lottie:6.6.6")
    //neu
    implementation("com.github.fornewid:neumorphism:0.3.0")
    //imagepicker
    implementation("com.github.dhaval2404:imagepicker:2.1")
}