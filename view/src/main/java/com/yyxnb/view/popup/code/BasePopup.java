package com.yyxnb.view.popup.code;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.yyxnb.view.popup.animator.EmptyAnimator;
import com.yyxnb.view.popup.animator.PopupAnimator;
import com.yyxnb.view.popup.animator.ScaleAlphaAnimator;
import com.yyxnb.view.popup.animator.ScrollScaleAnimator;
import com.yyxnb.view.popup.animator.ShadowBgAnimator;
import com.yyxnb.view.popup.animator.TranslateAlphaAnimator;
import com.yyxnb.view.popup.animator.TranslateAnimator;
import com.yyxnb.view.utils.KeyboardUtils;
import com.yyxnb.view.popup.NavigationBarObserver;
import com.yyxnb.view.popup.interfaces.OnNavigationBarListener;
import com.yyxnb.view.popup.PopupInfo;
import com.yyxnb.view.popup.interfaces.PopupStatus;
import com.yyxnb.view.popup.Popup;
import com.yyxnb.view.popup.PopupUtils;

import java.util.ArrayList;
import java.util.Stack;

import static com.yyxnb.view.popup.animator.PopupAnimation.NoAnimation;


/**
 * Description: 弹窗基类
 */
public abstract class BasePopup extends FrameLayout implements OnNavigationBarListener {
    private static Stack<BasePopup> stack = new Stack<>(); //静态存储所有弹窗对象
    public PopupInfo popupInfo = new PopupInfo();
    protected PopupAnimator popupContentAnimator;
    protected ShadowBgAnimator shadowBgAnimator;
    private int touchSlop;
    public PopupStatus popupStatus = PopupStatus.Dismiss;
    private boolean isCreated = false;
    private AppCompatActivity activity;

    public AppCompatActivity getActivity() {
        return activity;
    }

    public BasePopup(@NonNull Context context) {
        super(context);
        activity = (AppCompatActivity) context;
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        shadowBgAnimator = new ShadowBgAnimator(this);
        //  添加Popup窗体内容View
        View contentView = LayoutInflater.from(context).inflate(initPopupLayoutId(), this, false);
        // 事先隐藏，等测量完毕恢复，避免View影子跳动现象。
        contentView.setAlpha(0);
        addView(contentView);
    }

    public BasePopup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePopup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 执行初始化
     */
    protected void init() {
        if (popupStatus == PopupStatus.Showing) {
            return;
        }
        popupStatus = PopupStatus.Showing;
        NavigationBarObserver.getInstance().register(getContext());
        NavigationBarObserver.getInstance().addOnNavigationBarListener(this);

        //1. 初始化Popup
        if (!isCreated) {
            initPopupContent();
        }
        //apply size dynamic
        if (!(this instanceof FullScreenPopup)) {
            PopupUtils.setWidthHeight(getTargetSizeView(),
                    (getMaxWidth() != 0 && getPopupWidth() > getMaxWidth()) ? getMaxWidth() : getPopupWidth(),
                    (getMaxHeight() != 0 && getPopupHeight() > getMaxHeight()) ? getMaxHeight() : getPopupHeight()
            );
        }
        if (!isCreated) {
            isCreated = true;
            onCreate();
            if (popupInfo.popupCallback != null) {
                popupInfo.popupCallback.onCreated();
            }
        }
        postDelayed(() -> {
            // 如果有导航栏，则不能覆盖导航栏，判断各种屏幕方向
            applySize(false);
            getPopupContentView().setAlpha(1f);

            //2. 收集动画执行器
            collectAnimator();

            if (popupInfo.popupCallback != null) {
                popupInfo.popupCallback.beforeShow();
            }

            //3. 执行动画
            doShowAnimation();

            doAfterShow();

            //目前全屏弹窗快速弹出输入法有问题，暂时用这个方案
            if (!(BasePopup.this instanceof FullScreenPopup)) {
                focusAndProcessBackPress();
            }
        }, 50);

    }

    private boolean hasMoveUp = false;

    private void collectAnimator() {
        if (popupContentAnimator == null) {
            // 优先使用自定义的动画器
            if (popupInfo.customAnimator != null) {
                popupContentAnimator = popupInfo.customAnimator;
                popupContentAnimator.targetView = getPopupContentView();
            } else {
                // 根据PopupInfo的popupAnimation字段来生成对应的动画执行器，如果popupAnimation字段为null，则返回null
                popupContentAnimator = genAnimatorByPopupType();
                if (popupContentAnimator == null) {
                    popupContentAnimator = getPopupAnimator();
                }
            }

            //3. 初始化动画执行器
            shadowBgAnimator.initAnimator();
            if (popupContentAnimator != null) {
                popupContentAnimator.initAnimator();
            }
        }
    }

    @Override
    public void onNavigationBarChange(boolean show) {
        if (!show) {
            applyFull();
        } else {
            applySize(true);
        }
    }

    protected void applyFull() {
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.topMargin = 0;
        params.leftMargin = 0;
        params.bottomMargin = 0;
        params.rightMargin = 0;
        setLayoutParams(params);
    }

    protected void applySize(boolean isShowNavBar) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        int rotation = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        boolean isNavBarShown = isShowNavBar || PopupUtils.isNavBarVisible(getContext());
        if (rotation == 0) {
            params.leftMargin = 0;
            params.rightMargin = 0;
            params.bottomMargin = isNavBarShown ? PopupUtils.getNavBarHeight() : 0;
        } else if (rotation == 1) {
            params.bottomMargin = 0;
            params.rightMargin = isNavBarShown ? PopupUtils.getNavBarHeight() : 0;
            params.leftMargin = 0;
        } else if (rotation == 3) {
            params.bottomMargin = 0;
            params.leftMargin = 0;
            params.rightMargin = isNavBarShown ? PopupUtils.getNavBarHeight() : 0;
        }
        setLayoutParams(params);
    }

    public BasePopup show() {
        if (getParent() != null) {
            return this;
        }
        popupInfo.decorView = (ViewGroup) activity.getWindow().getDecorView();
        KeyboardUtils.registerSoftInputChangedListener(activity, this, height -> {
            if (height == 0) { // 说明对话框隐藏
                PopupUtils.moveDown(BasePopup.this);
                hasMoveUp = false;
            } else {
                //when show keyboard, move up
                PopupUtils.moveUpToKeyboard(height, BasePopup.this);
                hasMoveUp = true;
            }
        });
        // 1. add PopupView to its decorView after measured.
        popupInfo.decorView.post(() -> {
            if (getParent() != null) {
                ((ViewGroup) getParent()).removeView(BasePopup.this);
            }
            popupInfo.decorView.addView(BasePopup.this, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));

            //2. do init，game start.
            init();
        });
        return this;
    }

    protected void doAfterShow() {
        removeCallbacks(doAfterShowTask);
        postDelayed(doAfterShowTask, getAnimationDuration());
    }

    private Runnable doAfterShowTask = () -> {
        popupStatus = PopupStatus.Show;
        onShow();
        if (BasePopup.this instanceof FullScreenPopup) {
            focusAndProcessBackPress();
        }
        if (popupInfo != null && popupInfo.popupCallback != null) {
            popupInfo.popupCallback.onShow();
        }
        if (PopupUtils.getDecorViewInvisibleHeight(activity) > 0 && !hasMoveUp) {
            PopupUtils.moveUpToKeyboard(PopupUtils.getDecorViewInvisibleHeight(activity), BasePopup.this);
        }
    };

    private ShowSoftInputTask showSoftInputTask;

    public void focusAndProcessBackPress() {
        if (popupInfo.isRequestFocus) {
            setFocusableInTouchMode(true);
            requestFocus();
            if (!stack.contains(this)) {
                stack.push(this);
            }
        }
        // 此处焦点可能被内容的EditText抢走，也需要给EditText也设置返回按下监听
        setOnKeyListener(new BackPressListener());
        if (!popupInfo.autoFocusEditText) {
            showSoftInput(this);
        }

        //let all EditText can process back pressed.
        ArrayList<EditText> list = new ArrayList<>();
        PopupUtils.findAllEditText(list, (ViewGroup) getPopupContentView());
        for (int i = 0; i < list.size(); i++) {
            final EditText et = list.get(i);
            et.setOnKeyListener(new BackPressListener());
            if (i == 0 && popupInfo.autoFocusEditText) {
                et.setFocusable(true);
                et.setFocusableInTouchMode(true);
                et.requestFocus();
                showSoftInput(et);
            }
        }
    }

    protected void showSoftInput(View focusView) {
        if (popupInfo.autoOpenSoftInput) {
            if (showSoftInputTask == null) {
                showSoftInputTask = new ShowSoftInputTask(focusView);
            } else {
                removeCallbacks(showSoftInputTask);
            }
            postDelayed(showSoftInputTask, 10);
        }
    }

    protected void dismissOrHideSoftInput() {
        if (KeyboardUtils.sDecorViewInvisibleHeightPre == 0) {
            dismiss();
        } else {
            KeyboardUtils.hideSoftInput(BasePopup.this);
        }
    }

    class ShowSoftInputTask implements Runnable {
        View focusView;
        boolean isDone = false;

        public ShowSoftInputTask(View focusView) {
            this.focusView = focusView;
        }

        @Override
        public void run() {
            if (focusView != null && !isDone) {
                isDone = true;
                KeyboardUtils.showSoftInput(focusView);
            }
        }
    }

    class BackPressListener implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                if (popupInfo.isDismissOnBackPressed &&
                        (popupInfo.popupCallback == null || !popupInfo.popupCallback.onBackPressed())) {
                    dismissOrHideSoftInput();
                }
                return true;
            }
            return false;
        }
    }

    /**
     * 根据PopupInfo的popupAnimation字段来生成对应的内置的动画执行器
     */
    protected PopupAnimator genAnimatorByPopupType() {
        if (popupInfo == null || popupInfo.popupAnimation == null) {
            return null;
        }
        switch (popupInfo.popupAnimation) {
            case ScaleAlphaFromCenter:
            case ScaleAlphaFromLeftTop:
            case ScaleAlphaFromRightTop:
            case ScaleAlphaFromLeftBottom:
            case ScaleAlphaFromRightBottom:
                return new ScaleAlphaAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case TranslateAlphaFromLeft:
            case TranslateAlphaFromTop:
            case TranslateAlphaFromRight:
            case TranslateAlphaFromBottom:
                return new TranslateAlphaAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case TranslateFromLeft:
            case TranslateFromTop:
            case TranslateFromRight:
            case TranslateFromBottom:
                return new TranslateAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case ScrollAlphaFromLeft:
            case ScrollAlphaFromLeftTop:
            case ScrollAlphaFromTop:
            case ScrollAlphaFromRightTop:
            case ScrollAlphaFromRight:
            case ScrollAlphaFromRightBottom:
            case ScrollAlphaFromBottom:
            case ScrollAlphaFromLeftBottom:
                return new ScrollScaleAnimator(getPopupContentView(), popupInfo.popupAnimation);

            case NoAnimation:
                return new EmptyAnimator();
            default:
                break;
        }
        return null;
    }

    protected abstract int initPopupLayoutId();

    /**
     * 如果你自己继承BasePopupView来做，这个不用实现
     */
    protected int initLayoutResId() {
        return -1;
    }

    /**
     * 获取PopupAnimator，用于每种类型的PopupView自定义自己的动画器
     */
    protected PopupAnimator getPopupAnimator() {
        return null;
    }

    /**
     * 请使用onCreate，主要给弹窗内部用，不要去重写。
     */
    protected void initPopupContent() {
    }

    /**
     * do init.
     */
    protected void onCreate() {
    }

    /**
     * 执行显示动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    protected void doShowAnimation() {
        if (popupInfo.hasShadowBg) {
            shadowBgAnimator.isZeroDuration = (popupInfo.popupAnimation == NoAnimation);
            shadowBgAnimator.animateShow();
        }
        if (popupContentAnimator != null) {
            popupContentAnimator.animateShow();
        }
    }

    /**
     * 执行消失动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    protected void doDismissAnimation() {
        if (popupInfo.hasShadowBg) {
            shadowBgAnimator.animateDismiss();
        }
        if (popupContentAnimator != null) {
            popupContentAnimator.animateDismiss();
        }
    }

    /**
     * 获取内容View，本质上PopupView显示的内容都在这个View内部。
     * 而且我们对PopupView执行的动画，也是对它执行的动画
     */
    public View getPopupContentView() {
        return getChildAt(0);
    }

    public View getPopupImplView() {
        return ((ViewGroup) getPopupContentView()).getChildAt(0);
    }

    public int getAnimationDuration() {
        return popupInfo.popupAnimation == NoAnimation ? 10 : Popup.getAnimationDuration();
    }

    /**
     * 弹窗的最大宽度，一般用来限制布局宽度为wrap或者match时的最大宽度
     */
    protected int getMaxWidth() {
        return 0;
    }

    /**
     * 弹窗的最大高度，一般用来限制布局高度为wrap或者match时的最大宽度
     */
    protected int getMaxHeight() {
        return popupInfo.maxHeight;
    }

    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     */
    protected int getPopupWidth() {
        return 0;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     */
    protected int getPopupHeight() {
        return 0;
    }

    protected View getTargetSizeView() {
        return getPopupContentView();
    }

    /**
     * 消失
     */
    public void dismiss() {
        if (popupStatus == PopupStatus.Dismissing) {
            return;
        }
        popupStatus = PopupStatus.Dismissing;
        if (popupInfo.autoOpenSoftInput) {
            KeyboardUtils.hideSoftInput(this);
        }
        clearFocus();
        doDismissAnimation();
        doAfterDismiss();
    }

    public void delayDismiss(long delay) {
        if (delay < 0) {
            delay = 0;
        }
        postDelayed(this::dismiss, delay);
    }

    public void delayDismissWith(long delay, Runnable runnable) {
        this.dismissWithRunnable = runnable;
        delayDismiss(delay);
    }

    protected void doAfterDismiss() {
        if (popupInfo.autoOpenSoftInput) {
            KeyboardUtils.hideSoftInput(this);
        }
        removeCallbacks(doAfterDismissTask);
        postDelayed(doAfterDismissTask, getAnimationDuration());
    }

    private Runnable doAfterDismissTask = new Runnable() {
        @Override
        public void run() {
            onDismiss();
            if (popupInfo != null && popupInfo.popupCallback != null) {
                popupInfo.popupCallback.onDismiss();
            }
            if (dismissWithRunnable != null) {
                dismissWithRunnable.run();
                dismissWithRunnable = null;//no cache, avoid some bad edge effect.
            }
            popupStatus = PopupStatus.Dismiss;
            NavigationBarObserver.getInstance().removeOnNavigationBarListener(BasePopup.this);

            if (!stack.isEmpty()) {
                stack.pop();
            }
            if (popupInfo != null && popupInfo.isRequestFocus) {
                if (!stack.isEmpty()) {
                    stack.get(stack.size() - 1).focusAndProcessBackPress();
                } else {
                    // 让根布局拿焦点，避免布局内RecyclerView类似布局获取焦点导致布局滚动
                    View needFocusView = activity.findViewById(android.R.id.content);
                    needFocusView.setFocusable(true);
                    needFocusView.setFocusableInTouchMode(true);
                }
            }

            // 移除弹窗，GameOver
            if (popupInfo.decorView != null) {
                popupInfo.decorView.removeView(BasePopup.this);
                KeyboardUtils.removeLayoutChangeListener(popupInfo.decorView, BasePopup.this);
            }
        }
    };

    Runnable dismissWithRunnable;

    public void dismissWith(Runnable runnable) {
        this.dismissWithRunnable = runnable;
        dismiss();
    }

    public boolean isShow() {
        return popupStatus != PopupStatus.Dismiss;
    }

    public boolean isDismiss() {
        return popupStatus == PopupStatus.Dismiss;
    }

    public void toggle() {
        if (isShow()) {
            dismiss();
        } else {
            show();
        }
    }

    /**
     * 消失动画执行完毕后执行
     */
    protected void onDismiss() {
    }

    /**
     * 显示动画执行完毕后执行
     */
    protected void onShow() {
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stack.clear();
        removeCallbacks(doAfterShowTask);
        removeCallbacks(doAfterDismissTask);
        KeyboardUtils.removeLayoutChangeListener(popupInfo.decorView, BasePopup.this);
        if (showSoftInputTask != null) {
            removeCallbacks(showSoftInputTask);
        }
        popupStatus = PopupStatus.Dismiss;
        showSoftInputTask = null;
        hasMoveUp = false;
    }

    private float x, y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果自己接触到了点击，并且不在PopupContentView范围内点击，则进行判断是否是点击事件,如果是，则dismiss
        Rect rect = new Rect();
        getPopupContentView().getGlobalVisibleRect(rect);
        if (!PopupUtils.isInRect(event.getX(), event.getY(), rect)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float dx = event.getX() - x;
                    float dy = event.getY() - y;
                    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    if (distance < touchSlop && popupInfo.isDismissOnTouchOutside) {
                        dismiss();
                    }
                    x = 0;
                    y = 0;
                    break;
                default:
                    break;
            }
        }
        return true;
    }


}
