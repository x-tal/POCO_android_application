using UnityEngine;
using System.Collections;

public class testGUI : MonoBehaviour
{
	carlObject m_carl = null;

	void Start() {
		GameObject carl = GameObject.Find ("carl");
		m_carl = carl.GetComponent<carlObject> ();
	}

	void Update() {

	}

	void OnGUI(){
		GUI.Label (new Rect (128, 128, 1024, 128), AndroidPluginManager.GetInstance().label_str);

		if (GUI.Button (new Rect (128, 256, 256, 256), "<size=64>Swing Spine</size>") == true) {
			// AndroidPluginManager.GetInstance().CallPopup();
			// AndroidPluginManager.GetInstance().CallAndroid(Time.realtimeSinceStartup.ToString());
			
			this.m_carl.toggleSwing ();
		}

		if(GUI.Button (new Rect (512, 256, 256, 256), "<size=64>Sit</size>") == true) {
			// AndroidPluginManager.GetInstance().setLabel(AndroidPluginManager.GetInstance().original_label_str);
			// GameObject tempObject = GameObject.Find("carl");
			// tempObject.transform.Rotate(90, 0, 0);

			this.m_carl.toggleSit ();
		}

		if (GUI.Button (new Rect (768, 256, 256, 256), "<size=64>Bluetooth</size>") == true) {
			AndroidPluginManager.GetInstance().ConnectBluetooth();
		}
	}
}

