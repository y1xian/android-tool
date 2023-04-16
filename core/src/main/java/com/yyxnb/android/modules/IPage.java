package com.yyxnb.android.modules;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

/**
 * 启动页面
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/9
 */
public interface IPage extends IModule {

	/**
	 * 启动Fragment，通过容器加载
	 *
	 * @param fragment Fragment
	 */
	void startFragment(Fragment fragment);

	/**
	 * 启动Fragment，通过容器加载
	 *
	 * @param fragment Fragment
	 * @param bundle   Bundle
	 */
	void startFragment(Fragment fragment, Bundle bundle);

	/**
	 * 启动Fragment，通过容器加载
	 *
	 * @param fragment    Fragment
	 * @param bundle      Bundle
	 * @param requestCode requestCode
	 */
	void startFragmentForResult(Fragment fragment, Bundle bundle, int requestCode);

}
