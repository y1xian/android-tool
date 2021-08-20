
# arch
-dontwarn com.yyxnb.what.arch.**
-keep class com.yyxnb.what.arch.** {*;}

-keep @com.yyxnb.what.arch.annotation.* class * {*;}
-keep class * {
    @com.yyxnb.what.arch.annotation.* <fields>;
}
-keepclassmembers class * {
    @com.yyxnb.what.arch.annotation.* <methods>;
}