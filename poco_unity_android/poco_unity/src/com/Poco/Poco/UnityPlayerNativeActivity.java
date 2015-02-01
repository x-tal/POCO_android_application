package com.Poco.Poco;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import com.unity3d.player.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NativeActivity;
import android.app.TabActivity;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class UnityPlayerNativeActivity extends TabActivity implements Callback
{
	private static final int REQUEST_ENABLE_BT = 2;
	private TabHost mTabhost;
	private Handler handler = null;
	private BluetoothService btService = null;
	private AcceptThread btThread = null;

	public static UnityPlayerNativeActivity _instance;
	public static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.sendMessage(msg);
		}
	};

	
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
		UnityPlayer.UnitySendMessage("carlObject", "toggleSwing", "Yeaaaa");
	}
	
	
	public void connectAndroid(String strMsg) {
		if(btService.getDeviceState() == false) {
			Log.d("OK", "Service Fail and Enable");
			btService.enableBluetooth();
		} else {
			Log.d("OK", "Click button and success.");
			btThread.start();
		}
	}
	
	// Setup activity layout
	@Override protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

//		getWindow().takeSurface(null);
//		getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

		setContentView(R.layout.main);
		setTitle("POCO: Wearable posture fix solution");
		
		this.mTabhost = this.getTabHost();
		
		TabSpec tabSpec1 = this.mTabhost.newTabSpec("Unity Frame Tab");
		tabSpec1.setIndicator("나의 자세");
		tabSpec1.setContent(new Intent(this, UnityFrameActivity.class));
		this.mTabhost.addTab(tabSpec1);
		
		TabSpec tabSpec2 = this.mTabhost.newTabSpec("Unity Frame Tab2");
		tabSpec2.setIndicator("Hot line");
		tabSpec2.setContent(new Intent(this, OtherActivity.class));
		this.mTabhost.addTab(tabSpec2);
		
		TabSpec tabSpec3 = this.mTabhost.newTabSpec("Unity Frame Tab3");
		tabSpec3.setIndicator("관리자");
		tabSpec3.setContent(new Intent(this, AdminActivity.class));
		this.mTabhost.addTab(tabSpec3);
		
		this.mTabhost.setCurrentTab(0);
		
		this.handler = new Handler(this);
		
		// Create Bluetooth service
		if(this.btService == null) {
			Log.d("OK", "Create Bluetooth Service");
			this.btService = new BluetoothService(this, this.mHandler);
		}
		
		this.btThread = new AcceptThread(this.btService.pocoDevice());
		
		// 임시적으로 만든 인스턴스. 수정 필요! <--- 진짜로 똥임!
		_instance = this;
	}
	

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
//		if (msg.what == 0) {
//			this.wrongMessageDialog = this.createWrongDialog();
//			this.wrongMessageDialog.show();
//		} else if (msg.what == 1) {
//			this.startupDialog = this.createStarupDialog();
//			this.startupDialog.show();
//		} else if (msg.what == 2) {
//			this.connectAndroid("in android");
//		}
		
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
