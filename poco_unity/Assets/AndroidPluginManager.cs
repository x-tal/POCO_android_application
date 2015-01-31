using UnityEngine;
using System.Collections;

public class AndroidPluginManager : MonoBehaviour {

	static AndroidPluginManager _instance;
	private AndroidJavaObject curActivity;
	private AndroidJavaClass firstPluginJc;

	public string original_label_str;
	public string label_str;
	public string data;

	AndroidPluginManager(){
		original_label_str = "<size=32>Test check <color=red>Time.realtimeSinceStartup</color></size>";
		label_str = original_label_str;
	}

	public static AndroidPluginManager GetInstance(){
		if (_instance == null) {
			_instance = new GameObject("AndroidPluginManager").AddComponent<AndroidPluginManager>();
		}

		return _instance;
	}

	void Awake(){
		AndroidJavaClass jc = new AndroidJavaClass ("com.unity3d.player.UnityPlayer");
		curActivity = jc.GetStatic<AndroidJavaObject> ("currentActivity");
	}

	public void CallPopup(){
		curActivity.Call ("callPopup", "It works!!!");
	}

	public void CallAndroid(string strMsg){
		curActivity.Call ("callAndroid", strMsg);
	}

	public void CallDialog(){
		curActivity.Call ("callDialog", "POCO");
	}

	public void ConnectBluetooth() {
		curActivity.Call ("connectAndroid", "Hi");
	}

	public void receivedData(string strMsg){
		this.data = strMsg;
	}

	public string getData() {
		return this.data;
	}

	public void setLabel(string strMsg){
		label_str = strMsg;
		label_str = "<size=32>" + label_str + "</size>";
	}
}