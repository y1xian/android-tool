package com.yyxnb.widget

import android.Manifest
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yyxnb.arch.base.BaseActivity
import com.yyxnb.view.image.ImageWatcher
import com.yyxnb.view.image.ImageWatcherHelper
import com.yyxnb.view.rv.MultiItemTypeAdapter
import com.yyxnb.widget.adapter.MainListAdapter
import com.yyxnb.widget.config.DataConfig
import com.yyxnb.widget.fragments.NetWorkFragment
import kotlinx.android.synthetic.main.activity_main.*
import com.yyxnb.view.permission.FanPermissionUtils
import com.yyxnb.view.permission.FanPermissionListener
import com.yyxnb.view.popup.Popup
import com.yyxnb.view.popup.interfaces.OnSelectListener
import com.yyxnb.widget.utils.GlideSimpleLoader


class MainActivity : BaseActivity() {

    private val mAdapter by lazy { MainListAdapter() }

    private var iwHelper: ImageWatcherHelper? = null

    override fun initLayoutResId(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

        initPermission()

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter

        mAdapter.setDataItems(DataConfig.dataUrl)

        iwHelper = ImageWatcherHelper.with(this@MainActivity, GlideSimpleLoader());

        mAdapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
//                startFragment(NetWorkFragment.newInstance())

//                val loadingPopup = Popup.Builder(this@MainActivity).asLoading()
//                loadingPopup.show()

//                Popup.Builder(this@MainActivity)
//                        .asBottomList("", arrayOf("条目1", "条目2", "条目3", "条目4", "条目5"), null
//                                //                                null, 0,R.mipmap.icon_pay_check,
//                        ) { position, text ->
//                            //                                        ToastUtil.show("click " + text);
//                        }
//                        .show()


                iwHelper?.showString(DataConfig.dataUrl,0)

            }

            override fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean {
                return false
            }

        })

    }

    private fun initPermission() {

        FanPermissionUtils.with(this@MainActivity)
                //添加所有你需要申请的权限
                .addPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
//                .addPermissions(Manifest.permission.CALL_PHONE)
                .addPermissions(Manifest.permission.ACCESS_WIFI_STATE)
//                .addPermissions(Manifest.permission.CAMERA)
                //添加权限申请回调监听 如果申请失败 会返回已申请成功的权限列表，用户拒绝的权限列表和用户点击了不再提醒的永久拒绝的权限列表
                .setPermissionsCheckListener(object : FanPermissionListener {
                    override fun permissionRequestSuccess() {
                        //所有权限授权成功才会回调这里
                    }

                    override fun permissionRequestFail(grantedPermissions: Array<String>, deniedPermissions: Array<String>, forceDeniedPermissions: Array<String>) {
                        //当有权限没有被授权就会回调这里
                        //会返回已申请成功的权限列表（grantedPermissions）
                        //用户拒绝的权限列表（deniedPermissions）
                        //用户点击了不再提醒的永久拒绝的权限列表（forceDeniedPermissions）
                    }
                })
                //生成配置
                .createConfig()
                //配置是否强制用户授权才可以使用，当设置为true的时候，如果用户拒绝授权，会一直弹出授权框让用户授权
                .setForceAllPermissionsGranted(false)
                //配置当用户点击了不再提示的时候，会弹窗指引用户去设置页面授权，这个参数是弹窗里面的提示内容
                .setForceDeniedPermissionTips("请前往设置->应用->【" + FanPermissionUtils.getAppName(this@MainActivity) + "】->权限中打开相关权限，否则功能无法正常运行！")
                //构建配置并生效
                .buildConfig()
                //开始授权
                .startCheckPermission()
    }
}
