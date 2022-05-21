package com.yyxnb.android.utils;

import static android.hardware.Camera.Parameters.FLASH_MODE_OFF;
import static android.hardware.Camera.Parameters.FLASH_MODE_TORCH;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import com.yyxnb.android.Oh;

import java.io.IOException;

/**
 * 闪光灯
 * <p>
 * {@link #isFlashlightEnable} : 判断设备是否支持闪光灯
 * {@link #isFlashlightOn}     : 判断闪光灯是否打开
 * {@link #setFlashlightStatus}: 设置闪光灯状态
 * {@link #destroy}            : 销毁
 *
 * @author yyx
 */
public final class FlashlightUtil {

	private static Camera mCamera;
	private static SurfaceTexture mSurfaceTexture;

	private FlashlightUtil() {
	}

	/**
	 * 判断设备是否支持闪光灯.
	 *
	 * @return {@code true}: yes<br>{@code false}: no
	 */
	public static boolean isFlashlightEnable(Context context) {
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}

	/**
	 * 判断闪光灯是否打开.
	 *
	 * @return {@code true}: yes<br>{@code false}: no
	 */
	public static boolean isFlashlightOn() {
		if (!init()) {
			return false;
		}
		Camera.Parameters parameters = mCamera.getParameters();
		return FLASH_MODE_TORCH.equals(parameters.getFlashMode());
	}

	/**
	 * 设置闪光灯状态.
	 *
	 * @param isOn True to turn on the flashlight, false otherwise.
	 */
	public static void setFlashlightStatus(final boolean isOn) {
		if (!init()) {
			return;
		}
		final Camera.Parameters parameters = mCamera.getParameters();
		if (isOn) {
			if (!FLASH_MODE_TORCH.equals(parameters.getFlashMode())) {
				try {
					mCamera.setPreviewTexture(mSurfaceTexture);
					mCamera.startPreview();
					parameters.setFlashMode(FLASH_MODE_TORCH);
					mCamera.setParameters(parameters);
				} catch (IOException e) {
					Oh.log().e(e.getMessage());
				}
			}
		} else {
			if (!FLASH_MODE_OFF.equals(parameters.getFlashMode())) {
				parameters.setFlashMode(FLASH_MODE_OFF);
				mCamera.setParameters(parameters);
			}
		}
	}

	/**
	 * destroy.
	 */
	public static void destroy() {
		if (mCamera == null) {
			return;
		}
		mCamera.release();
		mSurfaceTexture = null;
		mCamera = null;
	}

	private static boolean init() {
		if (mCamera == null) {
			try {
				mCamera = Camera.open(0);
				mSurfaceTexture = new SurfaceTexture(0);
			} catch (Throwable t) {
				Oh.log().e("init failed: ", t);
				return false;
			}
		}
		if (mCamera == null) {
			Oh.log().e("init failed.");
			return false;
		}
		return true;
	}
}