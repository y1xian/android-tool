
-dontwarn com.yyxnb.oh.adapter.**
-keep class com.yyxnb.oh.adapter.** {*;}
-keep interface com.yyxnb.oh.adapter.** {*;}
-keep public class * extends com.yyxnb.oh.adapter.base.MultiItemTypeAdapter
-keepclassmembers class **$** extends com.yyxnb.oh.adapter.base.BaseViewHolder{*;}

-keep class java.util.** {*;}
-keep interface java.util.** {*;}




#-keep class com.chad.library.adapter.** {
#*;
#}
#-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
#-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
#-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
#     <init>(...);
#}