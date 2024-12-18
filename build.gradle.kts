// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.kapt) apply false
    alias(libs.plugins.dagger.hilt) apply false
    id("androidx.room") version "2.6.0" apply false
}