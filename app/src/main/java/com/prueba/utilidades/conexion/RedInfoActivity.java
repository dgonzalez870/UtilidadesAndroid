package com.prueba.utilidades.conexion;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prueba.utilidades.R;

public class RedInfoActivity extends Activity {

	public static final String MCC_VENEZUELA = "734";
	public static final String MNC_MOVISTAR = "04";
	public static final String MNC_DIGITEL = "02";
	public static final String MNC_MOVILNET = "06";
	private ListView lista;
	
	
	public class EstadoTelefonoListener extends PhoneStateListener {

		@Override
		public void onCellLocationChanged(CellLocation location) {
			// TODO Auto-generated method stub
			super.onCellLocationChanged(location);
		}

		@Override
		public void onServiceStateChanged(ServiceState serviceState) {
			// TODO Auto-generated method stub
			super.onServiceStateChanged(serviceState);
		}

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			// TODO Auto-generated method stub
			super.onSignalStrengthsChanged(signalStrength);
			Log.i("RED_INFO", "Nivel de señal: " + signalStrength.getCdmaDbm());
			View vista = lista.getChildAt(3);
			TextView tv_dato = (TextView) vista
					.findViewById(R.id.text_value_dato);
			tv_dato.setText(signalStrength.getGsmSignalStrength());
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// tipo de red
		int tipo=manager.getNetworkType();
		Log.i("RED_INFO", "Tipo de red: " + tipo);
		String tipoDeRed = "default";
		if (TelephonyManager.NETWORK_TYPE_1xRTT==tipo)
			tipoDeRed = "1xRTT";
		else if (tipo == TelephonyManager.NETWORK_TYPE_CDMA)
			tipoDeRed = "CDMS";
		else if (tipo== TelephonyManager.NETWORK_TYPE_EDGE)
			tipoDeRed = "EDGE";
		else if (tipo == TelephonyManager.NETWORK_TYPE_GPRS)
			tipoDeRed = "GPRS";
		else if (tipo == TelephonyManager.NETWORK_TYPE_UMTS)
			tipoDeRed = "UMTS";
		else if (tipo == TelephonyManager.NETWORK_TYPE_EVDO_0)
			tipoDeRed = "EVDO_0";
		else if (tipo == TelephonyManager.NETWORK_TYPE_EVDO_A)
			tipoDeRed = "EVDO_A";
		else if (tipo == TelephonyManager.NETWORK_TYPE_EVDO_B)
			tipoDeRed = "EVDO_B";
		else if (tipo == TelephonyManager.NETWORK_TYPE_HSDPA)
			tipoDeRed = "HSDPA";
		else if (tipo == TelephonyManager.NETWORK_TYPE_HSPA)
			tipoDeRed = "HSPA";
		else if (tipo == TelephonyManager.NETWORK_TYPE_HSUPA)
			tipoDeRed = "HSUPA";
		else if (tipo == TelephonyManager.NETWORK_TYPE_IDEN)
			tipoDeRed = "IDEN";
		else if (tipo == TelephonyManager.NETWORK_TYPE_UNKNOWN)
			tipoDeRed = "UNKNOWN";

		EstadoTelefonoListener listener = new EstadoTelefonoListener();
		// verifica el estado de la SIM para las opciones de la RED
		if (manager.getSimState() == TelephonyManager.SIM_STATE_READY) {
			List<NeighboringCellInfo> cellinfo = manager
					.getNeighboringCellInfo();
			Log.i("RED_INFO", "Información de la red " + cellinfo.size());
			if (cellinfo.size() != 0) {
				ListIterator<NeighboringCellInfo> iterador = cellinfo
						.listIterator();
				while (iterador.hasNext()) {
					NeighboringCellInfo info = iterador.next();
					// identificador de la red
					int cid = info.getCid();
					// LAC GSM
					int lac = info.getLac();
					// tipo de red
					int tipoRed = info.getNetworkType();
					// rssi(potencia de la se�al)
					int rssi = info.getRssi();
					Log.i("RED_INFO", " " + "CID " + cid);
					Log.i("RED_INFO", " " + "LAC " + lac);
					Log.i("RED_INFO", " " + "TIPO " + tipoRed);
					Log.i("RED_INFO", " " + "RSSI " + rssi);
				}
			}

			ArrayList<ItemTexto> items = new ArrayList<ItemTexto>();
			// Returns the MCC+MNC (mobile country code + mobile network code)
			// of the provider of the SIM
			String operador = manager.getSimOperator().substring(3, 5);
			String pais = manager.getSimOperator().substring(0, 3);
			// Identifica el país de la red
//			if (pais.equals(MCC_VENEZUELA))
			if (MCC_VENEZUELA.equals(pais))
				pais = "Venezuela";
			else
				pais = "default";

			if (MNC_DIGITEL.equals(operador))
				operador = "DIGITEL";
			else if (MNC_MOVISTAR.equals(operador))
				operador = "MOVISTAR";
			else if (MNC_MOVILNET.equals(operador))
				operador = "MOVILNET";

			items.add(new ItemTexto("Pa�s", pais));
			items.add(new ItemTexto("Operador de la Red", operador));
			items.add(new ItemTexto("Tipo de Red", tipoDeRed));
			items.add(new ItemTexto("Nivel de se�al", "0.0"));
			items.add(new ItemTexto("Latitud","0.0"));
			items.add(new ItemTexto("Longitud","0.0"));
			ItemsTextoAdapter adapter=new ItemsTextoAdapter(this,items);  
			setContentView(R.layout.layout_main);
			lista = (ListView) findViewById(R.id.lista_actividades);
			
			lista.setAdapter(adapter);
			manager.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		} else {
			Toast.makeText(getApplicationContext(),
					"Se ha presentado un error en la Tarjeta SIM",
					Toast.LENGTH_LONG).show();
		}
		/**
		 * Información de localización
		 */
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, false);
		Toast.makeText(this, provider, Toast.LENGTH_LONG).show();
		locationManager.requestLocationUpdates(
				provider, 0, 0,
				listenerLocalizacion);
	}

	LocationListener listenerLocalizacion = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Log.i("RED_INFO", "Se ha habilitado el proveedor de localización");

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.i("RED_INFO", "Localización: " + location.getLatitude() + " "
					+ location.getLongitude() + " " + location.getSpeed());
			View vista1 = lista.getChildAt(5);
			TextView tv_dato = (TextView) vista1
					.findViewById(R.id.text_value_dato);
			tv_dato.setText(location.getLongitude()+"");
			View vista2 = lista.getChildAt(4);
			TextView tv_dato2 = (TextView) vista2
					.findViewById(R.id.text_value_dato);
			tv_dato2.setText(location.getLongitude()+"");
		}
	};

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
