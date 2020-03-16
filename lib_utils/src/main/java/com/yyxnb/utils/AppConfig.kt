package com.yyxnb.utils


import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import com.yyxnb.utils.log.LogUtils
import java.io.Serializable
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

/**
 * 初始化相关
 */
@Suppress("UNCHECKED_CAST")
object AppConfig : Serializable {

    val app: Application = AppGlobals.getApplication()

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    val context: Context = app.applicationContext

    //debug下吐司
    fun debugToast(str: String) {
        if (isDebug) {
            ToastUtils.normal(str)
        }
    }

    //正常吐司
    fun toast(str: String) {
        ToastUtils.normal(str)
    }

    //debug下打印
    fun debugLog(str: String) {
        if (isDebug) {
            LogUtils.w(str)
        }
    }

    /**
     * 判断App是否是Debug版本
     */
    val isDebug: Boolean
        get() {
            if (TextUtils.isEmpty(app.packageName)) {
                return false
            }
            return try {
                val pm = app.packageManager
                val ai = pm.getApplicationInfo(app.packageName, 0)
                ai != null && ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                false
            }

        }

    /**
     * 返回实例的泛型类型
     */
    fun <T> getInstance(t: Any?, i: Int = 0): T? {
        t?.let {
            try {
                return (it.javaClass
                        .genericSuperclass as ParameterizedType)
                        .actualTypeArguments[i] as T
            } catch (e: ClassCastException) {
                e.printStackTrace()
            }

        }
        return null

    }

    fun <T> getClass(t: Any): Class<T> {
        // 通过反射 获取当前类的父类的泛型 (T) 对应 Class类
        return (t.javaClass.genericSuperclass as ParameterizedType)
                .actualTypeArguments[0]
                as Class<T>
    }

    fun <T> getFiledClazz(field: Field) = field.genericType as Class<T>
}