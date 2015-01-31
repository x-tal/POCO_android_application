package com.Poco.Poco;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class OtherActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout ll = new LinearLayout(this);
		
		this.setContentView(ll);
	}
}