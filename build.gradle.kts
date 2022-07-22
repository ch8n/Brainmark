buildscript {

    val kotlinVersion = "1.6.10"
    //https://cashapp.github.io/sqldelight/
    val sqlDelightVersion = "1.5.3"
    //https://github.com/yshrsmz/BuildKonfig
    val buildKonfigVersion = "0.11.0"
    //https://github.com/icerockdev/moko-resources#features
    val mokoResourceVersion = "0.20.1"

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:$buildKonfigVersion")
        classpath("dev.icerock.moko:resources-generator:$mokoResourceVersion")
    }
}


group = "dev.ch8n"
version = "1.0"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}