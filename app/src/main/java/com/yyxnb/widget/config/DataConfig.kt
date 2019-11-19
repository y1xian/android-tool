package com.yyxnb.widget.config

object DataConfig {

    val data = ArrayList<String>()
        get() {
            for (i in 0..19) {
                field.add("----- 第 $i 条数据 -----")
            }
            return field
        }

    val dataUrl = ArrayList<String>()
        get() {
            for (i in 0..19) {
                field.add("http://img0.imgtn.bdimg.com/it/u=4073821464,3431246218&fm=26&gp=0.jpg")
            }
            return field
        }

}