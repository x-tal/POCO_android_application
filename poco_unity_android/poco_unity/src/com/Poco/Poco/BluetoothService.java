package com.Poco.Poco;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class BluetoothService {
	private BluetoothAdapter btAdapter;
	private Activity mActivity;
	private Handler mHandler;
	private static final int REQUEST_ENABLE_BT = 2;

	public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	public BluetoothService(Activity ma, Handler mh) {
		this.mActivity = ma;
		this.mHandler = mh;
		
		this.btAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	public BluetoothServerSocket getListenUsingRfcommWithServiceRecord() {
		BluetoothServerSocket returnSock = null;
		try {
			returnSock = this.btAdapter.listenUsingRfcommWithServiceRecord("POCO", MY_UUID);
 		} catch (IOException e) {
 			Log.d("OK", "getListenUsingRfcommWithServiceRecord failed");
		}
		return returnSock;
	}

	public boolean getDeviceState() {
		if(this.btAdapter == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void enableBluetooth() {
		if(this.btAdapter.isEnabled() == false) {
			Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			this.mActivity.startActivityForResult(i, REQUEST_ENABLE_BT);
		}
	}
	
	public Set<BluetoothDevice> getBondedDevices() {
		return this.btAdapter.getBondedDevices();
	}
	
	public BluetoothDevice pocoDevice() {
		Set<BluetoothDevice> pairedDevices = this.getBondedDevices();
		if(pairedDevices.size() > 0) {
			for(BluetoothDevice device : pairedDevices) {
				Log.d("OK", device.getName() + ", " + device.getAddress());
				
				// Temply. 
				if(device.getName().equals("yeop")) {
					Log.d("OK", "SUCCESS to yeop");
					return device;
				}
			}
		}
		return null;
	}
	
	public BluetoothAdapter getBTAdapter() {
		return this.btAdapter;
	}

}
