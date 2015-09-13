package com.prueba.utilidades.conexion;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ListView;

import com.prueba.utilidades.R;

public class RedWifiInfoActivity extends Activity{
	
//	Maneaja la conectividad y la informacion de la red, requiere la activación de permisos en el manifiesto 
	ConnectivityManager conectividadManager;
	//informacion de la red wifi requiere permiso en el manifiesto
	WifiManager wifiManager;
	//informacion de la red telefónica
	TelephonyManager telephonyManager;
ListView listaWifi;	
//	TextView tvInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_info_red);
		listaWifi=(ListView) findViewById(R.id.listViewWifi);
//		tvInfo=(TextView) findViewById(R.id.tvInfo);
		conectividadManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo informacion=conectividadManager.getActiveNetworkInfo();
		State estado=informacion.getState();
		ArrayList<ItemTexto> items= new ArrayList<ItemTexto>();
		if(State.DISCONNECTED.equals(estado))
			items.add(new ItemTexto("Conexión","Desconectado"));
		else if(State.CONNECTED.equals(estado))
			items.add(new ItemTexto("Conexión","Conectado"));
//		proporciona el nombre de la red
//		tvInfo.append(informacion.getExtraInfo()+"\n");
		items.add(new ItemTexto("Información",informacion.getExtraInfo()));
		int tipoRed=informacion.getType();
		switch (tipoRed) {
		case ConnectivityManager.TYPE_BLUETOOTH:
			items.add(new ItemTexto("Tipo de red","Red Bluetooth"));
			break;
		case ConnectivityManager.TYPE_ETHERNET:
			items.add(new ItemTexto("Tipo de red","Red Ethernet"));
			break;
		case ConnectivityManager.TYPE_WIFI:
			items.add(new ItemTexto("Tipo de red","Red wi-fi"));
			break;
		default:
			break;
		}

		DetailedState estadoDetallado= informacion.getDetailedState();
		DetailedState[] detalles=DetailedState.values();
		wifiManager=(WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo infoWifi=wifiManager.getConnectionInfo();
		int direccionIp=infoWifi.getIpAddress();
		items.add(new ItemTexto("Dirección IP",direccionIp+""));
		int velocidad=infoWifi.getLinkSpeed();
		items.add(new ItemTexto("Velocidad",velocidad+""));
		String bssid=infoWifi.getBSSID();
		String ssid=infoWifi.getSSID();
		String macadd=infoWifi.getMacAddress();
		int id=infoWifi.getNetworkId();
		//potencia de la senal en dBm
		int rssi=infoWifi.getRssi();
		items.add(new ItemTexto("BSSID",bssid));
		items.add(new ItemTexto("SSID",ssid));
		items.add(new ItemTexto("RSSI",rssi+""));
		items.add(new ItemTexto("Dirección de máquina",macadd));
		items.add(new ItemTexto("ID",""+id));
		listaWifi.setAdapter(new ItemsTextoAdapter(getApplicationContext(), items));

	}
	
	
}
