package com.yyxnb.widget

import android.Manifest
import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yyxnb.arch.base.BaseActivity
import com.yyxnb.http.upload.UploadRetrofit
import com.yyxnb.utils.interfaces.OnCancelListener
import com.yyxnb.utils.interfaces.OnConfirmListener
import com.yyxnb.utils.log.LogUtils
import com.yyxnb.utils.permission.FanPermissionListener
import com.yyxnb.utils.permission.FanPermissionUtils
import com.yyxnb.view.popup.Popup
import com.yyxnb.view.rv.ItemDecoration
import com.yyxnb.view.rv.MultiItemTypeAdapter
import com.yyxnb.widget.adapter.MainListAdapter
import com.yyxnb.widget.config.DataConfig
import com.yyxnb.widget.fragments.BehaviorFragment
import com.yyxnb.widget.fragments.TagFragment
import com.yyxnb.widget.fragments.TitleFragment
import com.yyxnb.widget.fragments.adapter.AdapterListFragment
import com.yyxnb.widget.fragments.lazy.FragmentListFragment
import com.yyxnb.widget.fragments.network.NetWorkListFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val mAdapter by lazy { MainListAdapter() }

    override fun initLayoutResId(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

        initPermission()
        setStatusBarColor(Color.TRANSPARENT)

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)
        val decoration = ItemDecoration(mContext)
        decoration.setDividerColor(resources.getColor(R.color.red))
        decoration.setPaddingLeft(16)
        mRecyclerView.addItemDecoration(decoration)
        mRecyclerView.adapter = mAdapter

        mAdapter.setDataItems(DataConfig.dataMain)

        mAdapter.setOnItemClickListener(object : MultiItemTypeAdapter.SimpleOnItemClickListener() {
            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {

                setMenu(position)

//                val loadingPopup = Popup.Builder(this@MainActivity).asLoading()
//                loadingPopup.show()

//                Popup.Builder(this@MainActivity)
//                        .asBottomList("", arrayOf("条目1", "条目2", "条目3", "条目4", "条目5"), null
//                                //                                null, 0,R.mipmap.icon_pay_check,
//                        ) { position, text ->
//                            //                                        ToastUtil.show("click " + text);
//                        }
//                        .show()


            }

        })

    }

    private fun setMenu(position: Int) {

        when (position) {
            0 -> startFragment(TitleFragment.newInstance())
            1 -> startFragment(NetWorkListFragment.newInstance())
            2 -> startFragment(FragmentListFragment.newInstance())
            3 -> startFragment(AdapterListFragment.newInstance())
            4 -> startFragment(BehaviorFragment())
            5 -> startFragment(TagFragment.newInstance())
            else -> {
//                val loadingPopup = Popup.Builder(this@MainActivity).asLoading()
//                loadingPopup.show()

                val url = "/storage/emulated/0/MagazineUnlock/magazine-unlock-05-2.3.2862-98D64FEA5AEA01B9532F258EE5AF1980.jpg"

                val map = mutableMapOf("uid" to "74", "token" to "5afee96b6156db5efee003f174640667")
                val f = mutableListOf<String>()
                f.add(url)

                UploadRetrofit.uploadImgsWithParams("http://www.51duanshiping.com/api/public/?service=Video.setVideoThumb",
                        "file",map,f).observe(this, Observer {
                    t ->
                    t?.let {
                        LogUtils.w("uuuu  ${it.string()}")
                    }

                })


//                DownloadRetrofit.downloadFile("http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4", "download.mp4")
//                        .observe(this, Observer { t ->
//                            t?.let {
//                                LogUtils.w("dddd  已下 ${it.bytesRead} -进度 ${it.progress}  是否完成 ${it.done} - 路径 ${it.filePath}")
//                            }
//                        })

                Popup.Builder(mContext)
                        .asConfirm("1","您确定要退出吗？", OnConfirmListener { }, OnCancelListener { })
                        .bindLayout(R.layout.popup_tip_confirm)
                        .show()

//                HttpHelper.get().url("http://www.mocky.io/v2/5dd6271933000041d5f38453").execute(object :ICallBack{
//                    override fun onSuccess(result: String) {
//                        LogUtils.w(result)
//                    }
//
//                    override fun onFailure(result: String) {
//                        LogUtils.e(result)
//                    }
//
//                })
//                val map = hashMapOf("service" to "Video.getVideo", "uid" to "298463", "videoid" to "134")
//                HttpHelper.post().url("http://ce27p.cn/api/public/").params(map).execute(object : ICallBack {
//                    override fun onSuccess(result: String) {
//                        LogUtils.w(result)
//                    }
//
//                    override fun onFailure(result: String) {
//                        LogUtils.e(result)
//                    }
//
//                })
            }
        }

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
