
-dontwarn com.yyxnb.what.paging.**
-keep class com.yyxnb.what.paging.** {*;}
-keep public class * extends com.yyxnb.what.paging.base.MultiItemTypePagedAdapter

# paging
-dontwarn android.arch.paging.**
-keep class android.arch.paging.** {*;}