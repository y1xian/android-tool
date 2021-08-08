
-dontwarn com.yyxnb.oh.aop.**
-keep class com.yyxnb.oh.aop.** {*;}

-keep @com.yyxnb.oh.aop.annotation.* class * {*;}
-keep class * {
    @com.yyxnb.oh.aop.annotation.* <fields>;
}
-keepclassmembers class * {
    @com.yyxnb.oh.aop.annotation.* <methods>;
}