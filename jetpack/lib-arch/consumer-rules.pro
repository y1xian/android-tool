
# arch
-dontwarn com.yyxnb.oh.arch.**
-keep class com.yyxnb.oh.arch.** {*;}

-keep @com.yyxnb.oh.arch.annotation.* class * {*;}
-keep class * {
    @com.yyxnb.oh.arch.annotation.* <fields>;
}
-keepclassmembers class * {
    @com.yyxnb.oh.arch.annotation.* <methods>;
}