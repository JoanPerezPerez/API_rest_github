plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.api_rest_git"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.api_rest_git"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //Retrofitcore
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    // Gson Converter for Retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
}

