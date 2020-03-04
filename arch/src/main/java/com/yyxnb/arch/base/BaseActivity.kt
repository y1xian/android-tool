package com.yyxnb.arch.base

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.github.anzewei.parallaxbacklayout.ParallaxBack
import com.github.anzewei.parallaxbacklayout.ParallaxHelper
import com.github.anzewei.parallaxbacklayout.widget.ParallaxBackLayout.EDGE_MODE_DEFAULT
import com.github.anzewei.parallaxbacklayout.widget.ParallaxBackLayout.EDGE_MODE_FULL
import com.yyxnb.arch.ContainerActivity
import com.yyxnb.arch.annotations.BarStyle
import com.yyxnb.arch.annotations.SwipeStyle
import com.yyxnb.arch.common.ArchConfig
import com.yyxnb.arch.delegate.ActivityDelegate
import com.yyxnb.arch.interfaces.IActivity
import com.yyxnb.arch.delegate.LifecycleDelegate
import com.yyxnb.arch.utils.FragmentManagerUtils
import com.yyxnb.utils.MainThreadUtils
import com.yyxnb.utils.StatusBarUtils
import com.yyxnb.utils.ext.hideKeyBoard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import me.jessyan.autosize.AutoSizeCompat
import kotlin.math.abs


/**
 * Description: BaseActivity
 *
 * @author : yyx
 * @date : 2018/6/10
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@ParallaxBack(edgeMode = ParallaxBack.EdgeMode.EDGE)
abstract class BaseActivity : AppCompatActivity(), IActivity, CoroutineScope by MainScope() {

    protected val TAG: String = javaClass.canonicalName

    protected lateinit var mContext: Context

    private val mActivityDelegate by lazy { ActivityDelegate(this) }
    private val lifecycleDelegate by lazy { LifecycleDelegate(this) }

    private var statusBarTranslucent = ArchConfig.statusBarTranslucent
    private var fitsSystemWindows = ArchConfig.fitsSystemWindows
    private var statusBarHidden = ArchConfig.statusBarHidden
    private var statusBarDarkTheme = ArchConfig.statusBarStyle
    private var navigationBarDarkTheme = ArchConfig.navigationBarStyle
    private var swipeBack = ArchConfig.swipeBack

    val hasId: Int
        get() = abs(TAG.hashCode())

    override fun getBaseDelegate(): ActivityDelegate? = mActivityDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = this
        // 在界面未初始化之前调用的初始化窗口
        initWindows()
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(Java8Observer(TAG))

        initAttributes()
        setContentView(initLayoutResId())

        initView(savedInstanceState)
    }

    //加载注解设置
    private fun initAttributes() {
        MainThreadUtils.post(Runnable {
            if (!isSubPage()) {
                setStatusBarTranslucent(statusBarTranslucent, fitsSystemWindows)
                setStatusBarStyle(statusBarDarkTheme)
                setNavigationBarStyle(navigationBarDarkTheme)
                setStatusBarHidden(statusBarHidden)
            }
        })
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDestroy() {
        super.onDestroy()
        // 取消协程
        if (isActive) {
            cancel()
        }
    }

    /**
     * 初始化根布局
     */
    @LayoutRes
    abstract override fun initLayoutResId(): Int

    override fun getResources(): Resources {
        //需要升级到 v1.1.2 及以上版本才能使用 AutoSizeCompat
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())//如果没有自定义需求用这个方法
//        AutoSizeCompat.autoConvertDensity(super.getResources(), 667f, false)//如果有自定义需求就用这个方法
        return super.getResources()
    }

    //侧滑
    override fun setSwipeBack(@SwipeStyle mSwipeBack: Int) {
        val layout = ParallaxHelper.getParallaxBackLayout(this, true)
        when (mSwipeBack) {
            SwipeStyle.Full -> {
                ParallaxHelper.enableParallaxBack(this)
                layout.setEdgeMode(EDGE_MODE_FULL) //全屏滑动
            }
            SwipeStyle.NONE -> {
                ParallaxHelper.disableParallaxBack(this)
            }
            SwipeStyle.Edge -> {
                ParallaxHelper.enableParallaxBack(this)
                layout.setEdgeMode(EDGE_MODE_DEFAULT) //边缘滑动
            }
        }
    }

    //开启沉浸式
    fun setStatusBarTranslucent(translucent: Boolean, fitsSystemWindows: Boolean) {
        StatusBarUtils.setStatusBarTranslucent(window, translucent, fitsSystemWindows)
    }

    //状态栏颜色
    fun setStatusBarColor(color: Int) {
        var mColor = color
        //判断是否Color类下
        if (mColor > 0) {
            mColor = resources.getColor(mColor)
        }
        StatusBarUtils.setStatusBarColor(window, mColor)
    }

    //状态栏字体
    fun setStatusBarStyle(barStyle: Int) {
        StatusBarUtils.setStatusBarStyle(window, barStyle == BarStyle.DarkContent)
    }

    //隐藏状态栏
    fun setStatusBarHidden(hidden: Boolean) {
        StatusBarUtils.setStatusBarHidden(window, hidden)
    }

    //底部栏颜色
    fun setNavigationBarColor(color: Int) {
        StatusBarUtils.setNavigationBarColor(window, color)
    }

    //底部栏字体
    fun setNavigationBarStyle(barStyle: Int) {
        StatusBarUtils.setNavigationBarStyle(window, barStyle == BarStyle.DarkContent)
    }

    //隐藏虚拟导航栏
    fun setNavigationBarHidden(hidden: Boolean) {
        StatusBarUtils.setNavigationBarHidden(window, hidden)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        FragmentManagerUtils.apply {
            if (count > 1) {
                //当前传值
                val current = currentFragment() as BaseFragment
                //上一个页面需接收的
                val before = beforeFragment()
                //将回调的传入到fragment中去
                before.onActivityResult(current.requestCode, current.resultCode, current.result)
            }
        }
        if (fragments.isNotEmpty()) {
            ActivityCompat.finishAfterTransition(this)
        } else {
            super.onBackPressed()
        }
    }

    @JvmOverloads
    fun <T : BaseFragment> startFragment(targetFragment: T, requestCode: Int = 0) {
        scheduleTaskAtStarted(Runnable {
            val intent = Intent(this, ContainerActivity::class.java)
            val bundle = targetFragment.initArguments()
            bundle.putInt(ArchConfig.REQUEST_CODE, requestCode)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(ArchConfig.FRAGMENT, targetFragment.javaClass.canonicalName)
            intent.putExtra(ArchConfig.BUNDLE, bundle)
            startActivityForResult(intent, requestCode)
        })
    }

    fun setRootFragment(fragment: BaseFragment, containerId: Int = com.yyxnb.arch.R.id.content) {
        scheduleTaskAtStarted(Runnable {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(containerId, fragment, fragment.sceneId)
            transaction.addToBackStack(fragment.sceneId)
            transaction.commitAllowingStateLoss()
        })
    }

    @JvmOverloads
    protected fun scheduleTaskAtStarted(runnable: Runnable, interval: Long = 1L) {
        lifecycleDelegate.scheduleTaskAtStarted(runnable, interval)
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATED_IDENTITY_EQUALS")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //把操作放在用户点击的时候
        if (event.action === MotionEvent.ACTION_DOWN) {
            val v = currentFocus      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (mActivityDelegate.isShouldHideKeyboard(v, event)) { //判断用户点击的是否是输入框以外的区域
                //收起键盘
                v.hideKeyBoard()
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 是否子页面
     */
    open fun isSubPage(): Boolean {
        return false
    }

}
