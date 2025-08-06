# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#-keep class de.geosphere.speechplaning.data.model.** { *; }
#-keepclassmembers class de.geosphere.speechplaning.data.model.** {
#    <init>(...);
#}
#
#-keep class com.google.firebase.** { *; }
#-keep interface com.google.firebase.** { *; }
#-keepattributes Signature
#-keepattributes *Annotation*
#
#-keep enum de.geosphere.speechplaning.data.** { *; }
#
#-keep class org.koin.** { *; }
#
#-keep class kotlin.** { *; }
#-keep class kotlin.jvm.** { *; }
#-keep class kotlin.reflect.** { *; }
##
##-keep class com.google.gson.** { *; }
##-keep class retrofit2.** { *; }
##-keep class okhttp3.** { *; }
##-keep class * implements java.io.Serializable {
##    *;
##}
##-keep class * implements android.os.Parcelable {
##    public static final android.os.Parcelable$Creator *;
##}
