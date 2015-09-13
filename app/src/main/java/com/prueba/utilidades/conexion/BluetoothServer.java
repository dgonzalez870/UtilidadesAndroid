package com.prueba.utilidades.conexion;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class BluetoothServer {
	private UUID uuid=UUID.fromString("2fbf5ef6-e4d9-46d0-99bf-bf3fc60569a5"); 
	public static final String NOMBRE_SERVICIO="Mi sevicio";
	BluetoothAdapter mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
	AcceptThread hiloServer;
	BluetoothServerSocket mmServerSocket;
	private class AcceptThread extends Thread {
	 
	    public AcceptThread() {
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NOMBRE_SERVICIO, uuid);
	        } catch (IOException e) { }
	        mmServerSocket = tmp;
	    }
	 
	    public void run() {
	        BluetoothSocket socket = null;
	        // Keep listening until exception occurs or a socket is returned
	        while (true) {
	            try {
	                socket = mmServerSocket.accept();
	            } catch (IOException e) {
	                break;
	            }
	            // If a connection was accepted
	            if (socket != null) {
	                // Do work to manage the connection (in a separate thread)
//	                manageConnectedSocket(socket);
	                try {
						mmServerSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                break;
	            }
	        }
	    }
	    
	}
	public void iniciarServer(){
		hiloServer=new AcceptThread();
		hiloServer.start();
	}
	
	/** Will cancel the listening socket, and cause the thread to finish */
	public void cancel() {
		try {
			mmServerSocket.close();
		} catch (IOException e) { }
	}
	
}
