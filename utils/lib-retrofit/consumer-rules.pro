
#
-dontwarn com.yyxnb.oh.retrofit.**
-keep class com.yyxnb.oh.retrofit.** {*;}

# Retrofit
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepattributes Signature
-keepattributes Exceptions
-dontwarn javax.annotation.**

# paging
-dontwarn android.arch.paging.**
-keep class android.arch.paging.** {*;}