package com.yyxnb.android.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.yyxnb.android.core.utils.ActivityUtil;
import com.yyxnb.android.modules.IPage;
import com.yyxnb.android.secure.app.intent.IntentUtils;
import com.yyxnb.android.secure.app.intent.SafeBundle;
import com.yyxnb.android.utils.LogUtil;

/**
 * FragmentPageImpl
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/13
 */
public class FragmentPageImpl implements IPage {

    private static final String TAG = FragmentPageImpl.class.getSimpleName();

    /**
     * 启动Fragment，通过容器加载
     *
     * @param fragment Fragment
     */
    @Override
    public void startFragment(Fragment fragment) {
        startFragment(fragment, null);
    }

    /**
     * 启动Fragment，通过容器加载
     *
     * @param fragment Fragment
     * @param bundle   Bundle
     */
    @Override
    public void startFragment(Fragment fragment, Bundle bundle) {
        startFragmentForResult(fragment, bundle, 0);
    }

    /**
     * 启动Fragment，通过容器加载
     *
     * @param fragment    Fragment
     * @param bundle      Bundle
     * @param requestCode requestCode
     */
    @Override
    public void startFragmentForResult(Fragment fragment, Bundle bundle, int requestCode) {
        try {
            final Activity activity = ActivityUtil.getTopActivity();
            Intent intent = new Intent(activity, ContainerActivity.class);
            Bundle iBundle = new SafeBundle(bundle).getBundle();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ArgumentKeys.FRAGMENT, fragment.getClass().getCanonicalName());
            intent.putExtra(ArgumentKeys.BUNDLE, iBundle);
            boolean forResultStatic = IntentUtils.safeStartActivityForResultStatic(activity, intent, requestCode, iBundle);
            if (!forResultStatic) {
                activity.startActivityForResult(intent, requestCode, bundle);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
    }
}
