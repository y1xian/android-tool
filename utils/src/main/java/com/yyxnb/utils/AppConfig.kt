package com.yyxnb.utils


import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import java.io.Serializable
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType

/**
 * 初始化相关
 */
object AppConfig : Serializable {

    private lateinit var mWeakReferenceContext: WeakReference<Context>

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    fun init(application: Application) {
        mApp = application
        SPUtils.init(application)
        mWeakReferenceContext = WeakReference(application.applicationContext)
    }

    var mApp: Application? = null

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    val context: Context
        get() {
            if (null != mWeakReferenceContext) {
                return mWeakReferenceContext.get()!!.applicationContext
            }
            throw NullPointerException("u should init first")
        }

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
            Log.d("---", str)
        }
    }

    /**
     * 判断App是否是Debug版本
     */
    val isDebug: Boolean
        get() {
            if (TextUtils.isEmpty(mWeakReferenceContext.get()!!.packageName)) {
                return false
            }
            try {
                val pm = mWeakReferenceContext.get()!!.packageManager
                val ai = pm.getApplicationInfo(mWeakReferenceContext.get()!!.packageName, 0)
                return ai != null && ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                return false
            }

        }

    /**
     * 返回实例的泛型类型
     */
    fun <T> getNewInstance(t: Any?, i: Int = 0): T? {
        t?.let {
            try {
                return ((it.javaClass
                        .genericSuperclass as ParameterizedType)
                        .actualTypeArguments[i] as Class<T>)
                        .newInstance()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: ClassCastException) {
                e.printStackTrace()
            }
        }
        return null

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

}