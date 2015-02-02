using UnityEngine;
using System.Collections;

public class AndroidPluginManager : MonoBehaviour {

	static AndroidPluginManager _instance;
	private AndroidJavaObject curActivity;
	private AndroidJavaObject unityFrameActivity;

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

//		AndroidJavaClass jc2 = new AndroidJavaClass ("com.Poco.Poco.UnityFrameActivity");
//		this.unityFrameActivity = jc2.GetStatic<AndroidJavaObject> ("getInstance");
	}

	public void CallMessageDialog(float m_x, float m_y, float m_z){
		string message = m_x.ToString () + "," + m_y.ToString () + "," + m_z.ToString ();
		curActivity.Call ("callMessageDialog", message);
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

	public void setSwing(string strMsg) {
		carlObject._instance.toggleSwing ();
	}

	public void setWrongRange(string strMsg) {
		if (strMsg.Equals ("0")) {
			carlObject._instance.setWrongRange(5);
		} else if (strMsg.Equals ("1")) {
			carlObject._instance.setWrongRange(10);
		} else if (strMsg.Equals ("2")) {
			carlObject._instance.setWrongRange(15);
		} else if (strMsg.Equals ("3")) {
			carlObject._instance.setWrongRange(20);
		}
	}

	public void setLabel(string strMsg){
		label_str = strMsg;
		label_str = "<size=32>" + label_str + "</size>";
	}
}