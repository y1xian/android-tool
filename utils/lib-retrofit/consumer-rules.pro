
#
-dontwarn com.yyxnb.what.retrofit.**
-keep class com.yyxnb.what.retrofit.** {*;}

# Retrofit
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepattributes Signature
-keepattributes Exceptions
-dontwarn javax.annotation.**

# paging
-dontwarn android.arch.paging.**
-keep class android.arch.paging.** {*;}