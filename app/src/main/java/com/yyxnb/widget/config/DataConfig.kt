package com.yyxnb.widget.config

object DataConfig {

    val data = ArrayList<String>()
        get() {
            for (i in 0..19) {
                field.add("第 $i 条数据")
            }
            return field
        }

}