plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
//    alias(libs.plugins.dagger.hilt.plugin)
}

android {
    namespace = "com.xvantage.rental"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.xvantage.rental"
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

    flavorDimensions += "environment"

    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationId = "com.xvantage.rental.dev"
            versionCode = 1
            versionName = "1.0"
            resValue("string", "folder", "Rental")

            buildConfigField("String", "SERVER_USER", "\"https://beta.helllo.com/csm/api/\"")
            buildConfigField("String", "SERVER_IMAGE", "\"https://beta.hello.com/media/images/user/\"")

            buildConfigField("String", "URL_TERMS", "\"https://www.hello.com/terms-of-service/\"")
            buildConfigField("boolean", "IS_DEBUG", "true")
        }

        create("live") {
            dimension = "environment"
            applicationId = "com.xvantage.rental"
            versionCode = 1
            versionName = "1.0"

            resValue("string", "folder", "Rental")

            buildConfigField("String", "SERVER_USER", "\"https://beta.helllo.com/csm/api/\"")
            buildConfigField("String", "SERVER_IMAGE", "\"https://beta.hello.com/media/images/user/\"")

            buildConfigField("String", "URL_TERMS", "\"https://www.hello.com/terms-of-service/\"")
            buildConfigField("boolean", "IS_DEBUG", "false")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }

    dataBinding {
        enable = true
    }
    viewBinding {
        enable = true
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.tools.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.ssp.android)
    implementation(libs.sdp.android)


    implementation(libs.jwtdecode)
    implementation(libs.java.jwt)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)

    implementation(libs.glide)
    implementation(libs.overlap.avatar)

    implementation(libs.dagger.hilt)
//    implementation(libs.hilt.compiler)
//    implementation(libs.snackbar.android)


    implementation(libs.lifecycle.viewmodel.ktx)

}