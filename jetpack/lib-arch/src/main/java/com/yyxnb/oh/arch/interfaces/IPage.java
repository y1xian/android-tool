package com.yyxnb.oh.arch.interfaces;

import android.content.Intent;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.yyxnb.oh.arch.page.PageException;

public interface IPage {

    /**
     * Return the host object of this fragment. May return {@link android.app.Activity} if the fragment
     * is has no parent fragment . otherwise , return {@link Fragment} .
     */
    @NonNull
    Object getPageHost();

    /**
     * Adds and show a fragment into parent's containerView and hide other fragments that is placed in the
     * containerView.
     *
     * @param target the fragment that will be showed.
     */
    void startFragment(@NonNull Fragment target);

    /**
     * Adds and shows a fragment for which you would like a result when it closed.
     * When this fragment is exists,your onFragmentResult() method will be called with the given requestCode.
     * The onFragmentResult() method might not be called in the class that called this method, onFragmentResult() method
     * will be called in the class that have the fragment's container.
     *
     * @param target      The fragment to be started.
     * @param requestCode If >= 0,this code will be returned in onFragmentResult() method when fragment exits.
     */
    void startFragmentForResult(@NonNull Fragment target, int requestCode);

    /**
     * Shows a fragment and remove the others which is contained in the containerView.
     * if the fragment is not added in the stack,then add first,
     * if the fragment is already exist,then throw {@link PageException}.
     * <p>
     * The fragment added by this method is not pushed in the stack.if the method {@link #onBackPressed()} is
     * called, this fragment has none operation.
     *
     * @param target          the fragment to be showed.
     * @param containerViewId the fragment's container view's id.
     */
    void replaceFragment(@NonNull Fragment target, @IdRes int containerViewId);

    void replaceFragment(@NonNull String fragmentName, @IdRes int containerViewId);

    /**
     * Returns the resume status of Activity/Fragment.
     *
     * @return is or not resumed.
     */
    boolean isResumed();

    /**
     * Call this to set the result that your fragment will return to its
     * caller.
     *
     * @param resultCode The result code to propagate back to the originating
     *                   fragment, often RESULT_CANCELED or RESULT_OK
     * @param result     The data to propagate back to the originating fragment.
     * @see PageHelper#RESULT_OK
     * @see PageHelper#RESULT_CANCELED
     */
    void setResult(int resultCode, Intent result);

}
