using UnityEngine;
using System.Collections;

public class carlObject : MonoBehaviour {

	public static carlObject _instance;

	bool isSit = false;
	bool isSwing = false;
	
	// Use this for initialization
	void Start () {
		_instance = this;
		this.isSit = false;

		GameObject red_light = GameObject.Find ("Directional light");
		Light red = red_light.GetComponent<Light> ();
		red.intensity = 0;
	}
	
	// Update is called once per frame
	void Update () {
		if (this.isSwing == true) {
			GameObject carl_spine = GameObject.Find ("Spine");
			carl_spine.transform.localEulerAngles = new Vector3 (60 * Mathf.Sin (Time.realtimeSinceStartup), 1, 60 * Mathf.Cos (Time.realtimeSinceStartup));
		// carl_spine.transform.Rotate (60*Mathf.Sin(Time.realtimeSinceStartup), 0, Mathf.Cos(Time.realtimeSinceStartup));
		} else {
			GameObject carl_spine = GameObject.Find ("Spine2");

			string data = AndroidPluginManager.GetInstance().getData();
			if (data != null) {
				string[] result = data.Split(' ');

				float resultZ = System.Convert.ToSingle(result[0]);
				float resultX = System.Convert.ToSingle(result[1]) * -1;
				float resultY = System.Convert.ToSingle(result[2]);

				carl_spine.transform.localEulerAngles = new Vector3 (resultX, resultY, resultZ);
			}
	
			GameObject red_light = GameObject.Find ("Directional light");
			Light red = red_light.GetComponent<Light> ();
			if (isWrongAngle(carl_spine)) {
				red.intensity = 10;
			} else {
				red.intensity = 0;
			}
		}
	}

	private bool isWrongAngle (GameObject go) {
		if ((go.transform.localEulerAngles.x > 15 && go.transform.localEulerAngles.x < 355) || (go.transform.localEulerAngles.y > 15 && go.transform.localEulerAngles.y < 355) || (go.transform.localEulerAngles.z > 15 && go.transform.localEulerAngles.z < 355)) {
			return true;
		} else {
			return false;
		}
	}

	public void toggleSwing () {
		if (this.isSwing == false) {
			this.isSwing = true;
			if (this.isSit == true) {
				this.SitUp ();
			}
		}
		else {
			this.isSwing = false;
		}
	}

	public void toggleSwing (string str) {
		this.toggleSwing ();
	}

	public void toggleSit () {
		if (this.isSit == false) {
			this.SitDown ();
		}
		else {
			this.SitUp ();
		}
	}

	private void SitDown () {
		this.isSit = true;

		GameObject left_up_leg, right_up_leg, left_leg, right_leg;
		left_up_leg = GameObject.Find ("LeftUpLeg");
		right_up_leg = GameObject.Find ("RightUpLeg");
		left_leg = GameObject.Find ("LeftLeg");
		right_leg = GameObject.Find ("RightLeg");

		left_up_leg.transform.Rotate (275, 0, 0);
		left_leg.transform.Rotate (85, 0, 0);
		right_up_leg.transform.Rotate (275, 0, 0);
		right_leg.transform.Rotate (85, 0, 0);

		this.transform.position = new Vector3 (0, -4, -3);
	}

	private void SitUp () {
		this.isSit = false;

		GameObject left_up_leg, right_up_leg, left_leg, right_leg;
		left_up_leg = GameObject.Find ("LeftUpLeg");
		right_up_leg = GameObject.Find ("RightUpLeg");
		left_leg = GameObject.Find ("LeftLeg");
		right_leg = GameObject.Find ("RightLeg");
		
		left_up_leg.transform.Rotate (85, 0, 0);
		left_leg.transform.Rotate (275, 0, 0);
		right_up_leg.transform.Rotate (85, 0, 0);
		right_leg.transform.Rotate (275, 0, 0);

		this.transform.position = new Vector3 (0, -3, -3);
	}
}
