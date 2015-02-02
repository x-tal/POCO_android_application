package com.Poco.Poco;

import java.util.ArrayList;

import com.unity3d.player.UnityPlayer;

import android.annotation.SuppressLint;
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
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Space;

public class AdminActivity extends Activity implements OnClickListener {

	private AlertDialog planDialog = null;
	
	// @SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_admin);
		
		Button bt1 = (Button)findViewById(R.id.admin_btn1);
		Button bt2 = (Button)findViewById(R.id.admin_btn2);
		
		bt1.setText("오차 범위 설정");
		bt2.setText("기타 관리자 설정");
		bt1.setTag("AdminActivity_bt1");
		bt2.setTag("AdminActivity_bt2");
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
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
		
		ArrayList<String> al = new ArrayList<String>();
		al.add("5°");
		al.add("10°");
		al.add("15°");
		al.add("20°");
		
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, al);
	
		ab.setAdapter(aa, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("OK", Integer.toString(which));
				UnityPlayer.UnitySendMessage("AndroidPluginManager", "setWrongRange", Integer.toString(which));
			}
		});
		ab.setNegativeButton("Cancel", null);
		
		return ab.create();
	}
}