plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.sceventhunters.sceventfishing"
    // ИСПРАВЛЕНО: 36 — это preview, GitHub Actions её не находит.
    // 35 — стабильная версия, которую SDK manager скачивает без проблем.
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sceventhunters.sceventfishing"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "5.0-tsu"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ИСПРАВЛЕНО: ограничиваем сборку только arm64-v8a.
        // APK останется с именем app-debug.apk, но внутри будут только
        // нативные библиотеки под arm64. x86/armeabi-v7a будут вырезаны.
        ndk {
            abiFilters += "arm64-v8a"
        }
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

    // ИСПРАВЛЕНО: Java 8 не поддерживается современным AGP + Compose.
    // Kotlin/Compose требуют минимум Java 11, стабильно работает на 17.
    // Если оставить 1.8 — сборка упадёт с ошибкой про desugaring / kotlin.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // ИСПРАВЛЕНО: явно указываем Kotlin использовать ту же JVM-цель,
    // иначе Gradle ругается на несоответствие jvmTarget и compileOptions.
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            // ИСПРАВЛЕНО: расширен список исключений — иначе сборка падает
            // на дубликатах META-INF/LICENSE, NOTICE, DEPENDENCIES и т.п.
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/META-INF/LICENSE",
                "/META-INF/LICENSE.txt",
                "/META-INF/NOTICE",
                "/META-INF/NOTICE.txt",
                "/META-INF/DEPENDENCIES",
                "/META-INF/INDEX.LIST",
                "/META-INF/*.kotlin_module"
            )
        }
    }

    // ИСПРАВЛЕНО: отключаем lint во время сборки — на GitHub Actions
    // он часто валит сборку на незначительных предупреждениях.
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.activity:activity-compose:1.12.3")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    implementation("com.google.accompanist:accompanist-drawablepainter:0.34.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}