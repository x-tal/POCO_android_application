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
		super.onCreate(savedInstanceState);

//		getWindow().takeSurface(null);
//		getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

		setContentView(R.layout.main);
		setTitle("POCO:Wearable posture fix solution");
		
		this.mTabhost = this.getTabHost();
		
		TabSpec tabSpec1 = this.mTabhost.newTabSpec("Unity Frame Tab");
		tabSpec1.setIndicator("나의 자세");
		tabSpec1.setContent(new Intent(this, UnityFrameActivity.class));
		this.mTabhost.addTab(tabSpec1);
		
		TabSpec tabSpec2 = this.mTabhost.newTabSpec("Unity Frame Tab2");
		tabSpec2.setIndicator("Tab2");
		tabSpec2.setContent(new Intent(this, OtherActivity.class));
		this.mTabhost.addTab(tabSpec2);
		
		this.mTabhost.setCurrentTab(0);
		
		this.handler = new Handler(this);
		
		// Create Bluetooth service
//		if(this.btService == null) {
//			Log.d("OK", "Create Bluetooth Service");
//			this.btService = new BluetoothService(this, this.mHandler);
//		}
//		
//		this.btThread = new AcceptThread(this.btService.pocoDevice());
	}
	

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
