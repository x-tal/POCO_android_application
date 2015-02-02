package com.Poco.Poco;

import java.util.ArrayList;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class UnityFrameActivity extends Activity implements OnClickListener, Callback  {
	
	protected UnityPlayer mUnityPlayer;		// don't change the name of this variable; referenced from native code
	
	// private AlertDialog wrongMessageDialog = null;
	private AlertDialog startupDialog = null;
	private AlertDialog postureDialog = null;
	private AlertDialog messageDialog = null;
	
	public Handler mHandler = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mUnityPlayer = new UnityPlayer(this);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setWeightSum(5);
		
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
		ll.addView(mUnityPlayer, 0, lp1);
		
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 4);
		LinearLayout ll2 = new LinearLayout(this);
		ll2.setOrientation(LinearLayout.VERTICAL);
		ll2.setWeightSum(3);
		
		Button btn[] = new Button[3];
		for(int i = 0; i < 3; i++) {
			btn[i] = new Button(this);
			String text;
			switch(i) {
			//case 0: text = "레츠 파티"; break;
			//case 1: text = "오차 범위 설정"; break;
			case 0: text = "목표 자세 설정"; break;
			case 1: text = "POCO 사용 위치"; break;
			case 2: text = "POCO 연결"; break;
			default : text = "뭐여 이건"; break;
			}
			btn[i].setText(text);
			btn[i].setOnClickListener(this);
			btn[i].setTag(i);
			LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
			ll2.addView(btn[i], 0, llp1);
		}
		
		ll.addView(ll2, 0, lp2);
		
		this.mHandler = new Handler(this);
		
		this.setContentView(ll);
		this.mUnityPlayer.requestFocus();
	}
	
	// I think it is bad method.
	public void onClick(View v) {
		switch((Integer)v.getTag()){
		case -1: 
			UnityPlayer.UnitySendMessage("AndroidPluginManager", "setSwing", "Yeaaaa");
			break;
		case 0:
			this.postureDialog = this.createPostureDialog();
			this.postureDialog.show();
			break;
		case 1: 
			this.startupDialog = this.createStarupDialog();
			this.startupDialog.show();
			break;
		case 2: 
			UnityPlayerNativeActivity._instance.connectAndroid("POCO Connect");
			break;
		case 3: 
			// pass
			break;
		default: break;
		}
	}
	
	private AlertDialog createStarupDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		String array[] = {"목", "가슴", "팔", "골반", "다리"};
		
		ab.setTitle("POCO 사용위치를 선택해주세요.");	
		ab.setMultiChoiceItems(array, null, null);
		ab.setPositiveButton("OK", null);
		ab.setNegativeButton("Cancel", null);
		
		return ab.create();
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == 0) {
			this.startMessageDialog((String)msg.obj);
		}
		
		return false;
	}
	
	public void callMessageDialog(String strMsg){
		Message msg = new Message();
		msg.obj = strMsg;
		msg.what = 0;
		this.mHandler.sendMessage(msg);
	}
	
	private void startMessageDialog(String strMsg){
		Log.d("OK", strMsg);
		String data[] = strMsg.split(",");
		
		this.messageDialog = this.createMessageDialog(data);
		this.messageDialog.show();
	}

	private AlertDialog createMessageDialog(String data[]) {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("문제점");
		
		Float float_data[] = new Float[3];
		float_data[0] = Float.valueOf(data[0]);
		float_data[1] = Float.valueOf(data[1]);
		float_data[2] = Float.valueOf(data[2]);
		
		String xAxisDef, yAxisDef, zAxisDef;
		if (float_data[0]-180.0f < 0) {
			xAxisDef = "앞";
		} else {
			xAxisDef = "뒤";
			float_data[0] -= 360.0f;
			float_data[0] *= -1;
		}
		if (float_data[1]-180.0f < 0) {
			yAxisDef = "우";			
		} else {
			yAxisDef = "좌";
			float_data[1] -= 360.0f;
			float_data[1] *= -1;
		}
		if (float_data[2]-180.0f < 0) {
			zAxisDef = "왼쪽";
		} else {
			zAxisDef = "오른쪽";
			float_data[2] -= 360.0f;
			float_data[2] *= -1;
		}
		
		ab.setMessage(xAxisDef+"쪽으로 "+float_data[0].toString()+"도, "+yAxisDef+"측으로 "+float_data[1].toString()+"도, "+zAxisDef+"로 "+float_data[2].toString()+"도 틀어졌습니다.");
		ab.setPositiveButton("OK", null);
		
		return ab.create();
	}
	
	private AlertDialog createPostureDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("무슨 자세를 원하세요??");
		// ArrayAdapter<String> la = new ArrayAdapter<String>();
		
		ArrayList<String> al = new ArrayList<String>();
		al.add("요가");
		al.add("헬스");
		al.add("정상자세");
		al.add("국민체조");
		al.add("안알랴쥼");
		
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, al);
		
		ab.setAdapter(aa, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("OK", Integer.toString(which));
			}
		});
		ab.setNegativeButton("Cancel", null);
		
		return ab.create();
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

}