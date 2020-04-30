package com.yyxnb.arch.delegate

import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.yyxnb.arch.ContainerActivity
import com.yyxnb.arch.annotations.BarStyle
import com.yyxnb.arch.annotations.BindFragment
import com.yyxnb.arch.annotations.BindViewModel
import com.yyxnb.arch.annotations.SwipeStyle
import com.yyxnb.arch.base.BaseFragment
import com.yyxnb.arch.common.ArchConfig
import com.yyxnb.arch.ext.bus
import com.yyxnb.arch.interfaces.IActivity
import com.yyxnb.arch.interfaces.IFragment
import com.yyxnb.arch.livedata.ViewModelFactory
import com.yyxnb.arch.utils.FragmentManagerUtils
import com.yyxnb.utils.MainThreadUtils
import com.yyxnb.utils.StatusBarUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import java.lang.reflect.Field

class FragmentDelegate(private val iFragment: IFragment) : CoroutineScope by MainScope() {

    lateinit var mActivity: AppCompatActivity
    var iActivity: IActivity? = null
    val mFragment: Fragment = iFragment as Fragment
    var mRootView: View? = null

    val mLazyDelegate by lazy { FragmentLazyDelegate(mFragment) }

    var layoutRes = 0
    var statusBarTranslucent = ArchConfig.statusBarTranslucent
    var fitsSystemWindows = ArchConfig.fitsSystemWindows
    var statusBarHidden = ArchConfig.statusBarHidden
    var statusBarColor = ArchConfig.statusBarColor
    var statusBarDarkTheme = ArchConfig.statusBarStyle
    var swipeBack = SwipeStyle.Edge
    var subPage = false
    var group = -1
    var needLogin = false

    fun onAttach(activity: AppCompatActivity) {
        mActivity = activity
        if (activity is IActivity) {
            iActivity = activity
        }
    }

    fun onCreate(savedInstanceState: Bundle?) {
        mLazyDelegate.onCreate(savedInstanceState)
        FragmentManagerUtils.pushFragment(mFragment)
    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initAttributes()
        if (null == mRootView) {
            mRootView = inflater.inflate(if (layoutRes == 0) iFragment.initLayoutResId() else layoutRes, container, false)
        } else {
            //  二次加载删除上一个子view
            val viewGroup = mRootView?.parent as ViewGroup
            viewGroup.removeView(mRootView)
        }
        mRootView!!.setOnTouchListener { _, event ->
            mActivity.onTouchEvent(event)
            return@setOnTouchListener false
        }
        return mRootView
    }

    fun onActivityCreated(savedInstanceState: Bundle?) {
        mLazyDelegate.onActivityCreated(savedInstanceState, subPage)
    }

    fun setUserVisibleHint(isVisibleToUser: Boolean) {
        mLazyDelegate.setUserVisibleHint(isVisibleToUser)
    }

    fun onHiddenChanged(hidden: Boolean) {
        mLazyDelegate.onHiddenChanged(hidden)
    }

    fun onConfigurationChanged(newConfig: Configuration) {
        mLazyDelegate.onConfigurationChanged(newConfig)
    }

    fun onResume() {
        mLazyDelegate.onResume()
    }

    fun onPause() {
        mLazyDelegate.onPause()
    }

    fun onDestroy() {
        mLazyDelegate.onDestroy()
    }

    fun onDestroyView() {
        // 取消协程
        if (isActive) {
            cancel()
        }
        FragmentManagerUtils.killFragment(mFragment)
    }

    /**
     * 加载注解设置
     */
    fun initAttributes() {
        MainThreadUtils.post(Runnable {
            iFragment.javaClass.getAnnotation(BindFragment::class.java)?.let {
                layoutRes = it.layoutRes
                fitsSystemWindows = it.fitsSystemWindows
                statusBarTranslucent = it.statusBarTranslucent
                swipeBack = it.swipeBack
                subPage = it.subPage
                group = it.group
                if (it.statusBarStyle != BarStyle.NONE) {
                    statusBarDarkTheme = it.statusBarStyle
                }
                if (it.statusBarColor != 0) {
                    statusBarColor = it.statusBarColor
                }
                needLogin = it.needLogin
                // 如果需要登录，并且处于未登录状态下，发送通知
                if (needLogin && !ArchConfig.needLogin) {
                    ArchConfig.NEED_LOGIN.bus(needLogin)
                }
            }
            if (!subPage) {
                setNeedsStatusBarAppearanceUpdate()
            }
        })
    }

    /**
     * 获得成员变量
     */
    fun initDeclaredFields() {
        MainThreadUtils.post(Runnable {
            iFragment.javaClass.declaredFields
                    .filter { it.isAnnotationPresent(BindViewModel::class.java) }
                    .forEach { field ->
                        field.apply {
                            // 允许修改反射属性
                            isAccessible = true
                            /**
                             *  根据 @BindViewModel 注解, 查找注解标示的变量（ViewModel）
                             *  并且 创建 ViewModel 实例, 注入到变量中
                             */
                            getAnnotation(BindViewModel::class.java)?.let {
                                //向对象的这个Field属性设置新值value
                                set(iFragment, getViewModel(field, it.isActivity))
                            }
                        }
                    }
        })
    }

    /**
     * 更新状态栏样式
     */
    fun setNeedsStatusBarAppearanceUpdate() {

        if (subPage) return
        // 侧滑返回
        iActivity?.setSwipeBack(swipeBack)

        // 隐藏
        val hidden = statusBarHidden
        StatusBarUtils.setStatusBarHidden(getWindow(), hidden)

        // 文字颜色
        val statusBarStyle = statusBarDarkTheme
        StatusBarUtils.setStatusBarStyle(getWindow(), statusBarStyle == BarStyle.DarkContent)

        // 隐藏 or 不留空间 则透明
        if (hidden || !fitsSystemWindows) {
            StatusBarUtils.setStatusBarColor(getWindow(), Color.TRANSPARENT)
        } else {
            var statusBarColor = statusBarColor

            //不为深色
            var shouldAdjustForWhiteStatusBar = !StatusBarUtils.isBlackColor(statusBarColor, 176)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                shouldAdjustForWhiteStatusBar = shouldAdjustForWhiteStatusBar && statusBarStyle == BarStyle.LightContent
            }
            //如果状态栏处于白色且状态栏文字也处于白色，避免看不见
            if (shouldAdjustForWhiteStatusBar) {
                statusBarColor = ArchConfig.shouldAdjustForWhiteStatusBar
            }

            StatusBarUtils.setStatusBarColor(getWindow(), statusBarColor)
        }
        StatusBarUtils.setStatusBarTranslucent(getWindow(), statusBarTranslucent, fitsSystemWindows)
    }

    fun getWindow(): Window {
        return mActivity.window
    }

    @Suppress("UNCHECKED_CAST")
    private fun getViewModel(field: Field, activity: Boolean): ViewModel {
        return if (activity) {
            ViewModelFactory.createViewModel(mActivity, field)
        } else {
            ViewModelFactory.createViewModel(mFragment, field)
        }
    }

    fun initArguments(): Bundle {
        var args = mFragment.arguments
        if (args == null) {
            args = Bundle()
            mFragment.arguments = args
        }
        return args
    }

    fun <T : BaseFragment> startFragment(targetFragment: T, requestCode: Int = 0) {
        val bundle = initArguments()
        val intent = Intent(mActivity, ContainerActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(ArchConfig.FRAGMENT, targetFragment.javaClass.canonicalName)
        bundle.putInt(ArchConfig.REQUEST_CODE, requestCode)
        intent.putExtra(ArchConfig.BUNDLE, bundle)
        mActivity.startActivityForResult(intent, requestCode)
    }

}