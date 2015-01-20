package com.Poco.Poco;

import com.unity3d.player.*;

import android.app.AlertDialog;
import android.app.NativeActivity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class UnityPlayerNativeActivity extends NativeActivity implements Callback
{
	private Handler handler = null;
	private AlertDialog mDialog = null;
	protected UnityPlayer mUnityPlayer;		// don't change the name of this variable; referenced from native code
	
	private AlertDialog createDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("title");
		ab.setMessage("message");
		ab.setCancelable(false);
		
		ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				
			}
		});
		
		return ab.create();
	}
	
	public void callPopup(String strMsg){
		Log.d("Unity_POCO", strMsg);
		this.handler.sendEmptyMessage(0);
	}
	
	public void callAndroid(String strMsg)
	{
		UnityPlayer.UnitySendMessage("AndroidPluginManager", "setLabel", "Startup Time : "+strMsg);
	}
	
	// Setup activity layout
	@Override protected void onCreate (Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		getWindow().takeSurface(null);
		setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
		getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

		mUnityPlayer = new UnityPlayer(this);
		if (mUnityPlayer.getSettings ().getBoolean ("hide_status_bar", true))
			getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
			                       WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(mUnityPlayer);
		mUnityPlayer.requestFocus();
		
		this.handler = new Handler(this);
	}

	// Quit Unity
	@Override protected void onDestroy ()
	{
		mUnityPlayer.quit();
		super.onDestroy();
	}

	// Pause Unity
	@Override protected void onPause()
	{
		super.onPause();
		mUnityPlayer.pause();
	}

	// Resume Unity
	@Override protected void onResume()
	{
		super.onResume();
		mUnityPlayer.resume();
	}

	// This ensures the layout will be correct.
	@Override public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mUnityPlayer.configurationChanged(newConfig);
	}

	// Notify Unity of the focus change.
	@Override public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		mUnityPlayer.windowFocusChanged(hasFocus);
	}

	// For some reason the multiple keyevent type is not supported by the ndk.
	// Force event injection by overriding dispatchKeyEvent().
	@Override public boolean dispatchKeyEvent(KeyEvent event)
	{
		if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
			return mUnityPlayer.injectEvent(event);
		return super.dispatchKeyEvent(event);
	}

	// Pass any events not handled by (unfocused) views straight to UnityPlayer
	@Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
	@Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
	@Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
	/*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == 0) {
			this.mDialog = this.createDialog();
			this.mDialog.show();
		}
		
		return false;
	}
}
