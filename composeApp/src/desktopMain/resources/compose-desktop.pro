-ignorewarn
-keepclassmembers enum * {
   public static **[] values();
   public static ** valueOf(java.lang.String);
}
-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }