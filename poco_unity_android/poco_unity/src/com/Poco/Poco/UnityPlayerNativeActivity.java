package com.Poco.Poco;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import com.unity3d.player.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NativeActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class UnityPlayerNativeActivity extends NativeActivity implements Callback
{
	private static final int REQUEST_ENABLE_BT = 2;
	private Handler handler = null;
	private AlertDialog wrongMessageDialog = null;
	private AlertDialog startupDialog = null;
	private BluetoothService btService = null;
	private AcceptThread btThread = null;
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.sendMessage(msg);
		}
	};
	
	protected UnityPlayer mUnityPlayer;		// don't change the name of this variable; referenced from native code
	
	private AlertDialog createWrongDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("title");
		ab.setMessage("message");
		ab.setCancelable(false);
		
		ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (wrongMessageDialog != null) {
					wrongMessageDialog.dismiss();
				}
				
			}
		});
		
		return ab.create();
	}
	
	@SuppressLint("NewApi")
	private AlertDialog createStarupDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("잘못된 부위를 알랴주세요.");
		// ArrayAdapter<String> la = new ArrayAdapter<String>();
		
		ArrayList<String> al = new ArrayList<String>();
		al.add("목");
		al.add("가슴");
		al.add("팔");
		al.add("골반");
		al.add("다리");
		
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, al);
		
		ab.setAdapter(aa, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d("OK", "안알랴쥼");
			}
		});
		
		return ab.create();
	}
	
	public void callPopup(String strMsg){
		Log.d("Unity_POCO", strMsg);
		this.handler.sendEmptyMessage(0);
	}
	
	public void callDialog(String strMsg){
		Log.d("OK", "callDialog()");
		this.handler.sendEmptyMessage(1);
	}
	
	public void callAndroid(String strMsg)
	{
		UnityPlayer.UnitySendMessage("AndroidPluginManager", "setLabel", "Startup Time : "+strMsg);
	}
	
	public void connectAndroid(String strMsg) {
		if(this.btService.getDeviceState() == false) {
			Log.d("OK", "Service Fail and Enable");
			this.btService.enableBluetooth();
		} else {
			Log.d("OK", "Click button and success.");
			btThread.start();
		}
	}
	
	// Setup activity layout
	@Override protected void onCreate (Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		getWindow().takeSurface(null);
		getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

		setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
		mUnityPlayer = new UnityPlayer(this);
		if (mUnityPlayer.getSettings ().getBoolean ("hide_status_bar", true))
			getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
			                       WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(mUnityPlayer);
		mUnityPlayer.requestFocus();
		
		this.handler = new Handler(this);
		
		// Create Bluetooth service
		if(this.btService == null) {
			Log.d("OK", "Create Bluetooth Service");
			this.btService = new BluetoothService(this, this.mHandler);
		}
		
		this.btThread = new AcceptThread(this.btService.pocoDevice());
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
			this.wrongMessageDialog = this.createWrongDialog();
			this.wrongMessageDialog.show();
		} else if (msg.what == 1) {
			this.startupDialog = this.createStarupDialog();
			this.startupDialog.show();
		}
		
		return false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode) {
		case REQUEST_ENABLE_BT:
			if(resultCode == Activity.RESULT_OK) {
				Log.d("OK", "OK");
			}
			break;
		}
	}
}
