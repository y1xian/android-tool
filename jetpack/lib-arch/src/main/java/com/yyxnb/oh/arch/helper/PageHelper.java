package com.yyxnb.oh.arch.helper;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yyxnb.oh.arch.annotation.BindPage;
import com.yyxnb.oh.arch.interfaces.IPage;
import com.yyxnb.oh.arch.page.PageDelegate;
import com.yyxnb.oh.arch.page.PageException;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/04/27
 * 描    述：Activity/Fragment 启动页面帮助类
 * ================================================
 */
public final class PageHelper {

    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;

    private static final String TAG = "----------Page------------->";
    private final SparseArray<IPage> sparseArray;

    private static volatile PageHelper mInstance = null;

    private PageHelper() {
        sparseArray = new SparseArray<>();
    }

    public static PageHelper getInstance() {
        if (null == mInstance) {
            synchronized (PageHelper.class) {
                if (null == mInstance) {
                    mInstance = new PageHelper();
                }
            }
        }
        return mInstance;
    }

    /**
     * 必要实现 {@link BindPage}
     * {@link Fragment}/{@link FragmentActivity}
     */
    @NonNull
    public static IPage getPage(Object page) {
        if (!(page instanceof FragmentActivity) && !(page instanceof Fragment)) {
            throw new PageException(
                    "BindPage Annotation class can only used on Activity or Fragment");
        }
         Class<?> clazz = page.getClass();
         BindPage bindPage = clazz.getAnnotation(BindPage.class);
        if (bindPage == null) {
            throw new PageException("Can not find BindPage annotation.please add BindPage annotation for the class " +
                    page.getClass().getName());
        }
        int code = System.identityHashCode(page);
        IPage iPage = getInstance().sparseArray.get(code);
        if (null == iPage) {
            throw new PageException(
                    "UnKnown error " + page + " is not added into iPage");
        }
        return iPage;
    }

    /**
     * Returns the Rigger object or puts the page to page list.
     */
    private PageDelegate createPage(Object page) {
        int code = System.identityHashCode(page);
        IPage iPage = sparseArray.get(code);
        if (null == iPage) {
            iPage = PageDelegate.create(page);
            sparseArray.put(code, iPage);
            return (PageDelegate) iPage;
        }
        return (PageDelegate) iPage;
    }

    /**
     * Remove a page object from caches.
     *
     * @param page Activity/Fragment
     * @return the result of this process.
     */
    private boolean removePage(Object page) {
        int code = System.identityHashCode(page);
        if (sparseArray.indexOfKey(code) < 0) {
            return false;
        }
        sparseArray.remove(code);
        return true;
    }

    // -----------------------------------------------------------------------

    /**
     * {@link Fragment#onAttach(Context)}
     */
    public void onAttach(Object object, Context context) {
        createPage(object).onAttach(context);
    }

    /**
     * {@link FragmentActivity#onCreate(Bundle)}/{@link Fragment#onCreate(Bundle)}
     */
    public void onCreate(Object object, Bundle savedInstanceState) {
        createPage(object).onCreate(savedInstanceState);
    }

    /**
     * {@link Fragment#onViewCreated(View, Bundle)}
     */
    public void onViewCreated(Object object, View view, @Nullable Bundle savedInstanceState) {
        createPage(object).onViewCreated(view, savedInstanceState);
    }

    /**
     * {@link Fragment#onViewCreated(View, Bundle)}
     */
    public void onActivityCreated(Object object, @Nullable Bundle savedInstanceState) {
        createPage(object).onActivityCreated(savedInstanceState);
    }

    /**
     * {@link Fragment#onResume()}
     */
    public void onResume(Object object) {
        createPage(object).onResume();
    }

    /**
     * {@link FragmentActivity#onPause()}
     */
    public void onPause(Object object) {
        createPage(object).onPause();
    }

    /**
     * {@link FragmentActivity#onSaveInstanceState(Bundle)}/{@link Fragment#onSaveInstanceState(Bundle)}
     */
    public void onSaveInstanceState(Object object, Bundle outState) {
        createPage(object).onSaveInstanceState(outState);
    }

    /**
     * {@link FragmentActivity#onDestroy()}/{@link Fragment#onDestroy()}
     */
    public void onDestroy(Object object) {
        createPage(object).onDestroy();
        removePage(object);
    }

}
