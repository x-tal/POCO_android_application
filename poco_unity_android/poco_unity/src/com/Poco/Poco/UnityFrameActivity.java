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

public class UnityFrameActivity extends Activity implements OnClickListener {
	
	protected UnityPlayer mUnityPlayer;		// don't change the name of this variable; referenced from native code
	
	private AlertDialog wrongMessageDialog = null;
	private AlertDialog startupDialog = null;
	
	@Override
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
		ll2.setWeightSum(5);
		
		Button btn[] = new Button[5];
		for(int i = 0; i < 5; i++) {
			btn[i] = new Button(this);
			String text;
			switch(i) {
			case 0: text = "레츠 파티"; break;
			case 1: text = "오차 범위 설정"; break;
			case 2: text = "목표 자세 설정"; break;
			case 3: text = "부위 설정"; break;
			case 4: text = "디바이스 연결"; break;
			default : text = "뭐여 이건"; break;
			}
			btn[i].setText(text);
			btn[i].setOnClickListener(this);
			btn[i].setTag(i);
			LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
			ll2.addView(btn[i], 0, llp1);
		}
		
		ll.addView(ll2, 0, lp2);
		
		this.setContentView(ll);
	}
	
	// I think it is bad method.
	public void onClick(View v) {
		switch((Integer)v.getTag()){
		case 0: Log.d("OK", "Let's Party"); break;
		case 1: 
			Log.d("OK", "목표 자세.");
			break;
		case 2: 
			Log.d("OK", "오차 범우.");
			break;
		case 3: 
			this.startupDialog = this.createStarupDialog();
			this.startupDialog.show();
			break;
		case 4: 
			UnityPlayerNativeActivity._instance.connectAndroid("이건 똥이야 이히히히");
			break;
		default: break;
		}
	}
	
	private AlertDialog createWrongDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("title");
		ab.setMessage("message");
		ab.setCancelable(true);
		
//		ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				if (wrongMessageDialog != null) {
//					wrongMessageDialog.dismiss();
//				}
//				
//			}
//		});
		
		return ab.create();
	}
	
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