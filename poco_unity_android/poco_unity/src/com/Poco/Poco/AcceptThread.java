package com.Poco.Poco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import com.unity3d.player.UnityPlayer;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class AcceptThread extends Thread {
	private final BluetoothSocket mmSocket;
	private InputStream mmInStream;
	
	public AcceptThread(BluetoothDevice device) {
		BluetoothSocket tempSock = null;
		try {
			tempSock = device.createRfcommSocketToServiceRecord(BluetoothService.MY_UUID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.mmSocket = tempSock;
		// tempSock = btService.getListenUsingRfcommWithServiceRecord();
		
		if(this.mmSocket != null) {
			Log.d("OK", "AcceptThread create");
			try {
				this.mmInStream = this.mmSocket.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void run() {
		try {
			this.mmSocket.connect();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Log.d("OK", "AcceptThread connect success!");
		
		Log.d("OK", "AcceptThread goto while");
		while(true) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(this.mmInStream));
				String inputLine;
				while((inputLine = br.readLine()) != null) {
					String message = inputLine;
					Log.d("OK", message);
					UnityPlayer.UnitySendMessage("AndroidPluginManager", "receivedData", message);
				}
			} catch (IOException e) {
				// Bluetooth data 못받아 올 시의 임시 처리.
				e.printStackTrace();
				try {
//					if (this.mmSocket.isConnected() == true) {
//						this.mmSocket.close();
//					}
					this.mmSocket.connect();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void cancel() {
//		try {
//			this.mmServerSocket.close();
//		} catch(IOException e) {
//			Log.d("OK", "AcceptThread cancel exception");
//		}
	}
}
