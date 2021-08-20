
-dontwarn com.yyxnb.what.aop.**
-keep class com.yyxnb.what.aop.** {*;}

-keep @com.yyxnb.what.aop.annotation.* class * {*;}
-keep class * {
    @com.yyxnb.what.aop.annotation.* <fields>;
}
-keepclassmembers class * {
    @com.yyxnb.what.aop.annotation.* <methods>;
}