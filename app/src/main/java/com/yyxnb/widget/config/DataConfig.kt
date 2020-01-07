package com.yyxnb.widget.config

import com.yyxnb.widget.bean.TestData
import kotlin.random.Random

object DataConfig {

    val data = ArrayList<String>()
        get() {
            field.clear()
            for (i in 0..9) {
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
            field.add("----- 上传 -----")
            field.add("----- 下载 -----")
            field.add("----- 列表 无状态 -----")
            return field
        }

    val adapterList = ArrayList<String>()
        get() {
            field.clear()
            field.add("----- 头尾 侧滑-----")
            field.add("----- 多Item -----")
            return field
        }

    val dataUrl = ArrayList<String>()
        get() {
            field.clear()
            for (i in 0..9) {
                field.add("http://img0.imgtn.bdimg.com/it/u=4073821464,3431246218&fm=26&gp=0.jpg")
            }
            return field
        }

    val dataTestData = ArrayList<TestData>()
        get() {
            field.clear()
            for (i in 0..2) {
                field.add(TestData(i," - ${Random.nextInt(99)} - "))
            }
            return field
        }

    val dataTestData2 = ArrayList<TestData>()
        get() {
            field.clear()
            for (i in 0..2) {
                field.add(TestData(i,"第 $i 条"))
            }
            return field
        }

}