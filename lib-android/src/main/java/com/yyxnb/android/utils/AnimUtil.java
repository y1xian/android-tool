package com.yyxnb.android.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 动画工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/4/4
 */
public class AnimUtil {

	/**
	 * 默认动画持续时间
	 */
	public static final long DEFAULT_ANIMATION_DURATION = 300;

	/**
	 * 抖动动画，左右抖动
	 *
	 * @param context 上下文
	 * @param v       要执行动画的view
	 */
	public static void shake(Context context, View v) {
		//移动方向
		Animation shake = new TranslateAnimation(0, 10, 0, 0);
		//执行总时间
		shake.setDuration(DEFAULT_ANIMATION_DURATION);
		//循环次数
		shake.setInterpolator(new CycleInterpolator(7));
		v.startAnimation(shake);
	}

	/**
	 * 缩放动画，按下时缩放，抬起时恢复
	 *
	 * @param v        要执行动画的view
	 * @param event    触摸事件
	 * @param listener 点击事件
	 * @return 触摸结果
	 */
	public static boolean setClickAnim(View v, MotionEvent event, View.OnClickListener listener) {
		float start = 1.0f;
		float end = 0.95f;
		Animation scaleAnimation = new ScaleAnimation(start, end, start, end,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		Animation endAnimation = new ScaleAnimation(end, start, end, start,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(200);
		scaleAnimation.setFillAfter(true);
		endAnimation.setDuration(200);
		endAnimation.setFillAfter(true);
		switch (event.getAction()) {
			// 按下时调用
			case MotionEvent.ACTION_DOWN:
				v.startAnimation(scaleAnimation);
				v.invalidate();
				break;
			// 抬起时调用
			case MotionEvent.ACTION_UP:
				v.startAnimation(endAnimation);
				v.invalidate();
				if (listener != null) {
					listener.onClick(v);
				}
				break;
			// 滑动出去不会调用action_up,调用action_cancel
			case MotionEvent.ACTION_CANCEL:
				v.startAnimation(endAnimation);
				v.invalidate();
				break;
			default:
				break;
		}
		// 不返回true，Action_up就响应不了
		return true;
	}
}
