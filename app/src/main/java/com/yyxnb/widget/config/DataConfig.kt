package com.yyxnb.widget.config

object DataConfig {

    val data = ArrayList<String>()
        get() {
            field.clear()
            for (i in 0..19) {
                field.add("----- 第 $i 条数据 -----")
            }
            return field
        }

    val dataMain = ArrayList<String>()
        get() {
            field.clear()
            field.add("----- title -----")
            field.add("----- 网络请求 -----")
            field.add("----- fragment -----")
            field.add("----- adapter -----")
            field.add("----- 自定义 behavior -----")
            field.add("----- 标签 -----")
            field.add("----- 弹框 -----")
            return field
        }

    val fragmentList = ArrayList<String>()
        get() {
            field.clear()
            field.add("----- fragment 懒加载 s/h -----")
            field.add("----- fragment 懒加载 vp -----")
            field.add("----- fragment ForResult -----")
            return field
        }

    val networkList = ArrayList<String>()
        get() {
            field.clear()
            field.add("----- 列表 -----")
            field.add("----- paging -----")
            return field
        }

    val dataUrl = ArrayList<String>()
        get() {
            field.clear()
            for (i in 0..19) {
                field.add("http://img0.imgtn.bdimg.com/it/u=4073821464,3431246218&fm=26&gp=0.jpg")
            }
            return field
        }

}