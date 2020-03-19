package com.yyxnb.widget.config

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yyxnb.utils.AppConfig.toast
import com.yyxnb.widget.R
import com.yyxnb.widget.bean.MainBean
import com.yyxnb.widget.bean.TestData
import kotlin.collections.set
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

    val dataMain2 = ArrayList<String>()
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

    val dataMain = ArrayList<MainBean>()
        get() {
            field.clear()
            field.add(MainBean(1145905694, "----- title -----", "home/f/title"))
            field.add(MainBean(135884164, "----- 网络请求 -----", "home/f/http"))
            field.add(MainBean(1231750126, "----- fragment -----", "home/f/fragment"))
            field.add(MainBean(474114952, "----- adapter -----", "home/f/adapter"))
            field.add(MainBean(288680808, "----- 自定义 behavior -----", "home/f/behavior"))
            field.add(MainBean(1260165028, "----- 标签 -----", "home/f/tag"))
            field.add(MainBean(819827592, "----- 弹框 -----", "home/f/popup"))
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
            field.add("----- 上传 -----")
            field.add("----- 下载 -----")
            return field
        }

    val adapterList = ArrayList<String>()
        get() {
            field.clear()
            field.add("----- 头尾 侧滑-----")
            field.add("----- 多Item -----")
            field.add("----- 状态视图 -----")
            field.add("----- Paging -----")
            return field
        }

    val dialogList = ArrayList<String>()
        get() {
            field.clear()
            field.add("loading")
            field.add("提示")
            field.add("输入框")
            field.add("中间列表")
            field.add("中间列表 带选中")
            field.add("底部列表")
            field.add("底部列表 带选中")
            field.add("全屏")
            field.add("底部弹框 注册")
            field.add("评论列表")
            field.add("底部 + vp")
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
                field.add(TestData(Random.nextInt(999), " - ${Random.nextInt(999)} - "))
            }
            return field
        }

    val dataTestData2 = ArrayList<TestData>()
        get() {
            field.clear()
            for (i in 0..2) {
                field.add(TestData(i, "第 $i 条"))
            }
            return field
        }

    fun createView(mContext: Context, text: String): TextView? {
        val textView = TextView(mContext)
        textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
        //        textView.setPadding(80, 80, 80, 80);
        textView.gravity = Gravity.CENTER
        textView.setTextColor(Color.WHITE)
        when (java.util.Random().nextInt(4)) {
            0 -> textView.setBackgroundResource(R.color.red)
            1 -> textView.setBackgroundResource(R.color.yellow)
            2 -> textView.setBackgroundResource(R.color.blue)
            else -> textView.setBackgroundResource(R.color.black)
        }
        textView.text = text
        textView.setOnClickListener { v: View? -> toast(text) }
        return textView
    }

    @JvmStatic
    fun main(args: Array<String>) {

        val json = "{\n" +
                "  \"main/f/fragment\": {\n" +
                "    \"id\": 1231750126,\n" +
                "    \"needLogin\": false,\n" +
                "    \"asStarter\": false,\n" +
                "    \"pageUrl\": \"main/f/fragment\",\n" +
                "    \"className\": \"com.yyxnb.widget.fragments.lazy.FragmentListFragment\",\n" +
                "    \"isFragment\": true\n" +
                "  },\n" +
                "  \"main/tabs/other\": {\n" +
                "    \"id\": 1183122074,\n" +
                "    \"needLogin\": true,\n" +
                "    \"asStarter\": false,\n" +
                "    \"pageUrl\": \"main/tabs/other\",\n" +
                "    \"className\": \"com.yyxnb.widget.fragments.OtherFragment\",\n" +
                "    \"isFragment\": true\n" +
                "  }}"

        val map = hashMapOf<String, String>()
        map["main/1"] = "1"
        map["main/2"] = "2"
        map["main/3"] = "3"
        map["user/1"] = "11"
        map["user/2"] = "22"
        map["user/3"] = "33"

//        getDestConfig()

        val sp = "main"

        val w  = 480
        val h  = 854
        val v  = w/h
        val v2 = w  + h

        val w1 = 1080
        val h1 = 2220
        val v1 : Float = (w1 / h1).toFloat()


        println("Hello World! $v  $v1  $v2")

//        map.forEach { entry ->
//
//            if (entry.key.contains(sp)) {
//                println(entry.key + entry.value)
//            }
//        }


//        val v :HashMap<String,Destination>() = Gson().fromJson<HashMap<String,Destination>()>(json,object : Type {})

//        val a = JSON.parseObject<HashMap<String, Destination>>(json, object : TypeReference<HashMap<String, Destination>>() {})


//        a.forEach{
//            entry ->
//            println(entry.key + entry.value)
//        }
//        a.forEach { t: String ->
//            if (t.contains(sp)) {
//                println(t)
//            }
//        }
    }

}