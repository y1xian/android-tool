package com.yyxnb.arch.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.yyxnb.arch.common.ArchConfig
import com.yyxnb.arch.delegate.FragmentDelegate
import com.yyxnb.arch.ext.bus
import com.yyxnb.arch.ext.busObserve
import com.yyxnb.arch.delegate.LifecycleDelegate
import com.yyxnb.arch.interfaces.IFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import java.util.*
import kotlin.math.abs

/**
 * Description: 处理懒加载、状态栏 BaseFragment
 *
 * @author : yyx
 * @date ：2016/10
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
abstract class BaseFragment : Fragment(), IFragment, CoroutineScope by MainScope() {

    protected lateinit var mActivity: AppCompatActivity
    protected lateinit var mContext: Context

    protected val TAG: String = javaClass.canonicalName

    protected var mRootView: View? = null
    private val lifecycleDelegate by lazy { LifecycleDelegate(this) }
    private val mFragmentDelegate by lazy { FragmentDelegate(this) }

    private var group = mFragmentDelegate.group

    val sceneId: String
        get() = UUID.randomUUID().toString()

    val hasId: Int
        get() = abs(TAG.hashCode())

    fun <B : ViewDataBinding> getBinding(): B? {
        DataBindingUtil.bind<B>(mRootView!!)
        return DataBindingUtil.getBinding(mRootView!!)
    }

    init {
        lifecycle.addObserver(Java8Observer(TAG))
    }

    override fun getBaseDelegate(): FragmentDelegate? = mFragmentDelegate

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = context as AppCompatActivity
        mFragmentDelegate.onAttach(mActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFragmentDelegate.onCreate(savedInstanceState)
        val bundle = initArguments()
        if (bundle.size() > 0) {
            initVariables(bundle)
        }
    }

    fun initArguments(): Bundle {
        return mFragmentDelegate.initArguments()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mRootView = mFragmentDelegate.onCreateView(inflater, container, savedInstanceState)
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mFragmentDelegate.onActivityCreated(savedInstanceState)
        //当设备旋转时，fragment会随托管activity一起销毁并重建。
//        retainInstance = true
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mFragmentDelegate.setUserVisibleHint(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mFragmentDelegate.onHiddenChanged(hidden)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mFragmentDelegate.onConfigurationChanged(newConfig)
    }

    override fun onResume() {
        super.onResume()
        mFragmentDelegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        mFragmentDelegate.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mFragmentDelegate.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mFragmentDelegate.onDestroyView()
        // 取消协程
        if (isActive) {
            cancel()
        }
        mRootView = null
    }

    /**
     * 被ViewPager移出的Fragment 下次显示时会从getArguments()中重新获取数据
     * 所以若需要刷新被移除Fragment内的数据需要重新put数据
     */
    open fun initVariables(bundle: Bundle) {
        setRequest(bundle.getInt(ArchConfig.REQUEST_CODE, 0))
    }

    /**
     * 初始化根布局
     */
    @LayoutRes
    override fun initLayoutResId(): Int = 0

    /**
     * 初始化控件
     */
    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化复杂数据 懒加载
     */
    override fun initViewData() {
        mFragmentDelegate.initDeclaredFields()
        ArchConfig.FRAGMENT_FINISH.busObserve<Int>(this) { i ->
            // 层级 i越小级别越低
            // 关闭级别 -1以上 i以下的页面
            if (i >= group && group != -1 && !mFragmentDelegate.subPage) {
                finish()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> findViewById(@IdRes resId: Int): T {
        return mRootView!!.findViewById<View>(resId) as T
    }

    /**
     * 返回.
     */
    fun finish() {
        mActivity.onBackPressed()
    }

    /**
     * 关掉所有的页面，可给页面设置层级
     */
    @JvmOverloads
    fun finishAll(lv: Int = 0) {
        group = 0
        ArchConfig.FRAGMENT_FINISH.bus(lv)
    }

    // ------ lifecycle arch -------

    @JvmOverloads
    fun scheduleTaskAtStarted(runnable: Runnable, interval: Long = 1L) {
        lifecycleDelegate.scheduleTaskAtStarted(runnable, interval)
    }

    fun getWindow(): Window {
        return mActivity.window
    }

    /**
     * 跳转 activity.
     *
     * @param clazz 目标activity class.
     * @param <T>   [Activity].
    </T> */
    protected fun <T : Activity> startActivity(clazz: Class<T>) {
        startActivity(Intent(mActivity, clazz))
    }

    /**
     * 跳转 fragment.
     *
     * @param targetFragment 目标fragment.
     * @param requestCode    请求码.
     * @param T          [BaseFragment].
     */
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @JvmOverloads
    fun <T : BaseFragment> startFragment(targetFragment: T, requestCode: Int = 0) {
        onHiddenChanged(true)
        mFragmentDelegate.startFragment(targetFragment, requestCode)
    }

    var requestCode = 0
    var resultCode = 0
    var result: Intent? = null

    fun setRequest(requestCode: Int) {
        this.requestCode = requestCode
    }

    @JvmOverloads
    fun setResult(resultCode: Int, result: Intent? = null) {
        this.resultCode = resultCode
        this.result = result
    }

}
