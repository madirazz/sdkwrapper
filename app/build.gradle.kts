plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.trulioosdkwrapper"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
        buildConfig = true  // Add this line
    }


    defaultConfig {
        buildConfigField("String", "TRULIOO_API_KEY", "eyJraWQiOiJiNzU4MmQwYi0zMGU1LTQzZGEtYjNhNC1mZWVhOGVmNmQyZjEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI5MmZjMDI3Yy1jZjE1LTQ1NWEtOTFkZi1mODY4ZGU4ZDdiNGQiLCJhdWQiOiJjb20udHJ1bGlvby5jdXN0b21lciIsInJldGVudGlvbl9wb2xpY3kiOiI3Nzc2MDAwIiwidXNlIjoibGljZW5zZSIsImlzcyI6Imh0dHBzOi8vdmVyaWZpY2F0aW9uLnRydWxpb28uY29tIiwic291cmNlIjoiY3VzdG9tZXIiLCJleHAiOjE3OTkzMDg4MDAsImlhdCI6MTczNjI5Mjg3M30.RZQfDp6fnOD3iCfvuifWdr0xc1RenLnzMmjRWA1A5hTetyP38MiTUZzLIeqoW0-9tu0oKxX3JTe6JIHw3KtlKygQq5bdb0ibaWJDyheQMnZGGkaqIBkQTKXdBwliWW3bwe16wrvPZVbWEVemeXFPUUS6-XODvx8bmJqEMFys2X7KxUMGQy5xhR15wvmV2-j1NWiqL-ay35Imcr_eOrPvKZ5yiYYja751VU-uRRXUY009zY1VFDY-xvFdYxIl-0r-_XmUuxXZ10_S1geJZlufMc476vp-zuXQHHjEQ6fq29b00MpFPhqdhm4Avr3uYp_7uX5cKmIC7XSuGXXQa1_9iBjeYKDdXxoUOW7k9FP_CV_oMNFNApgJKQjKBjy6VuaBCt9f_MaxASMSAkkGTt2HNvrIOCsnedPxsBbwbt9_IYkaYPWJL9d2z5cuWPykpSWFhW2Uf6qradmQXJ-pXpSPlrmc3bQx2L5HoWwvgRMtE0-MMxjhYrCm2kJ2PvxVHj3N")
        applicationId = "com.example.trulioosdkwrapper"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Trulioo SDK
    implementation("com.trulioo:docv:2.+")

    // OkHttp for API calls
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // Android core dependencies
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.startup:startup-runtime:1.1.1")

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}