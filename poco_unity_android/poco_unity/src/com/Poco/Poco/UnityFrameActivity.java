package com.Poco.Poco;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class UnityFrameActivity extends Activity {
	
	protected UnityPlayer mUnityPlayer;		// don't change the name of this variable; referenced from native code
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
		mUnityPlayer = new UnityPlayer(this);
//		if (mUnityPlayer.getSettings ().getBoolean ("hide_status_bar", true))
//			getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
//			                       WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//		FrameLayout layout = (FrameLayout) findViewById(R.id.tab_1_frame);
//		LayoutParams lp = new LayoutParams (LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
//		layout.addView(mUnityPlayer, 0, lp);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setWeightSum(10);
//		LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) ll.getLayoutParams();
//		llp.gravity = Gravity.BOTTOM;
		
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams (LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 9);
		ll.addView(mUnityPlayer, 0, lp1);
		
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams (LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1);
		Button b1 = new Button(this);
		b1.setText("Button");
		
		ll.addView(b1, 0, lp2);
		
		this.setContentView(ll);
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