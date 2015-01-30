using UnityEngine;
using System.Collections;

public class carlObject : MonoBehaviour {

	bool isSit = false;
	bool isSwing = false;

	// Use this for initialization
	void Start () {
		this.isSit = false;
	}
	
	// Update is called once per frame
	void Update () {
		if (this.isSwing == true) {
			GameObject carl_spine = GameObject.Find ("Spine");
			carl_spine.transform.localEulerAngles = new Vector3 (60 * Mathf.Sin (Time.realtimeSinceStartup), 1, 60 * Mathf.Cos (Time.realtimeSinceStartup));
		// carl_spine.transform.Rotate (60*Mathf.Sin(Time.realtimeSinceStartup), 0, Mathf.Cos(Time.realtimeSinceStartup));
		} else {
			string data = AndroidPluginManager.GetInstance().getData();
			string[] result = data.Split(' ');

			float resultZ = System.Convert.ToSingle(result[0]);
			float resultX = System.Convert.ToSingle(result[1]) * -1;
			float resultY = System.Convert.ToSingle(result[2]);

			GameObject carl_spine = GameObject.Find ("Spine2");
			carl_spine.transform.localEulerAngles = new Vector3 (resultX, resultY, resultZ);
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
