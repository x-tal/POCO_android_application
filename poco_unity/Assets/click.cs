using UnityEngine;
using System.Collections;

public class click : MonoBehaviour {

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

	void OnMouseDown () {
		GameObject red_light = GameObject.Find ("Directional light");
		Light red = red_light.GetComponent<Light> ();
		if (red.intensity != 0) {
			GameObject carl_spine = GameObject.Find ("Spine2");
			float m_x = carl_spine.transform.localEulerAngles.x;
			float m_y = carl_spine.transform.localEulerAngles.y;
			float m_z = carl_spine.transform.localEulerAngles.z;
			
			AndroidPluginManager.GetInstance().CallMessageDialog(m_x, m_y, m_z);
		}
	}
}
