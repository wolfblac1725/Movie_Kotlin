# Gson rules
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Keep DTOs in the response package
-keep class com.erik.canseco.movies.data.remote.response.** { *; }
-keepclassmembers class com.erik.canseco.movies.data.remote.response.** { *; }
