package com.Poco.Poco;

import java.util.ArrayList;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class AdminActivity extends Activity implements OnClickListener {

	private AlertDialog planDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setWeightSum(0.2f);
		
		Button bt1 = new Button(this);
		Button bt2 = new Button(this);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.1f);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.1f);
		bt1.setText("오차 범위 설정");
		bt2.setText("기타 관리자 설정");
		bt1.setTag("AdminActivity_bt1");
		bt2.setTag("AdminActivity_bt2");
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		
		//FrameLayout fm = new FrameLayout(this);
		//LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.8f);
		
		ll.addView(bt1, 0, lp1);
		ll.addView(bt2, 0, lp2);
		//ll.addView(fm, 0, lp3);
		
		this.setContentView(ll);
	}

	@Override
	public void onClick(View v) {
		String gettedTag = v.getTag().toString();
		if (gettedTag.equals("AdminActivity_bt1")) {
			this.planDialog = this.createPlanDialog();
			this.planDialog.show();
		} else if (gettedTag.equals("AdminActivity_bt2")) {
			return;
		}
		
	}
	
	private AlertDialog createPlanDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("목표 자세 정도를 선택해주세요.");
		// ArrayAdapter<String> la = new ArrayAdapter<String>();
		
		ArrayList<String> al = new ArrayList<String>();
		al.add("빡시게!(20%)");
		al.add("넉넉하게(15%)");
		al.add("사람답게(10%)");
		al.add("원치않아여(5%)");
		
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, al);
	
		ab.setAdapter(aa, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d("OK", Integer.toString(which));
				UnityPlayer.UnitySendMessage("AndroidPluginManager", "setWrongRange", Integer.toString(which));
			}
		});
		ab.setNegativeButton("Cancel", null);
		
		return ab.create();
	}
}