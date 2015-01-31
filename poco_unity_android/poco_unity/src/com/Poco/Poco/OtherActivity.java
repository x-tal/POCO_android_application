package com.Poco.Poco;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OtherActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout ll = new LinearLayout(this);
		
		TextView label = new TextView(this);
		label.setText("Tab 2 임.");
		ll.addView(label);
		
		this.setContentView(ll);
	}
}