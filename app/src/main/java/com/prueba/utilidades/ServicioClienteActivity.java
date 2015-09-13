package com.prueba.utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ServicioClienteActivity extends Activity {

	TextView tv_temp;
	TextView tv_temp_max;
	TextView tv_temp_min;
	TextView tv_presion;
	ProgressDialog dialogoProgreso;
	
	Messenger messengerServicio;
	Handler activityHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ServicioClima.PUBLICAR_DATOS:
				Log.i("RESPUESTA","datos publicados");
				tv_temp.setText(msg.getData().getDouble("temp")+"");
				tv_temp_max.setText(msg.getData().getDouble("temp_max")+"");
				tv_temp_min.setText(msg.getData().getDouble("temp_min")+"");
				tv_presion.setText(msg.getData().getDouble("pressure")+"");
				break;
			default:
				break;
			}
		}
	};
	Messenger messengerActivity=new Messenger(activityHandler);

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_servicio_cliente);
		dialogoProgreso=new ProgressDialog(this);
		dialogoProgreso.setMessage("Progreso de conexión");
		dialogoProgreso.setTitle("PROGRESO");
		tv_presion=(TextView) findViewById(R.id.tv_presion);
		tv_temp=(TextView) findViewById(R.id.tv_temp);
		tv_temp_max=(TextView) findViewById(R.id.tv_temp_max);
		tv_temp_min=(TextView) findViewById(R.id.tv_temp_min);		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Intent intent=getIntent();
		if(intent.getBooleanExtra("desdeNotificacion", false)){
			Intent intent1=new Intent(getApplicationContext(), ServicioClima.class);
			bindService(intent1, conexion, Context.BIND_AUTO_CREATE);
		}
	}
	
	public void accionConectar(View v){
		WifiManager wifiManager=(WifiManager) getSystemService(WIFI_SERVICE);
		ConnectivityManager conectividadManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo informacion=conectividadManager.getActiveNetworkInfo();
		if((wifiManager.isWifiEnabled())&&(State.CONNECTED.equals(informacion.getState()))){
			TareaConexion tarea=new TareaConexion();
			tarea.execute("");
			Intent intent=new Intent(getApplicationContext(), ServicioClima.class);
			startService(intent);
			bindService(intent, conexion, Context.BIND_AUTO_CREATE);			
		}else{
			Toast.makeText(this, "No hay conexion de red", Toast.LENGTH_LONG).show();
		}
	}
	
	public void accionDesconectar(View v){
		try {
			messengerServicio.send(Message.obtain(null, ServicioClima.FINALIZAR_SERVICIO));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	ServiceConnection conexion=new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Message mensaje=Message.obtain(null, ServicioClima.REGISTRAR_CLIENTE);
			mensaje.replyTo=messengerActivity;
			messengerServicio=new Messenger(service);
			try {
				messengerServicio.send(mensaje);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public class TareaConexion extends AsyncTask<String, Integer, Void>{

		@Override
		protected Void doInBackground(String... params) {
			solicitarClima();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialogoProgreso.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogoProgreso.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			dialogoProgreso.setProgress(values[0]);
		}
	}
	
	public void solicitarClima(){
		String ruta="http://api.openweathermap.org/data/2.5/weather?q=Caracas";
		try {
			URL url=new URL(ruta);
			try {
				URLConnection conexion=url.openConnection();
				InputStream entrada=conexion.getInputStream();
				InputStreamReader reader=new InputStreamReader(entrada);
				char buffer[]=new char[1024];
				int leidos=reader.read(buffer);
				String respuesta=new String(buffer, 0, leidos);
				Log.i("RESPUESTA",respuesta);
				try {
					JSONObject objeto=new JSONObject(respuesta);
					String coordenadas=objeto.getString("coord");
					Log.i("COORDENADAS",coordenadas);
					Iterator iterador=objeto.keys();
					while(iterador.hasNext()){
						String clave=iterador.next().toString();
						String valor=objeto.getString(clave);
						Log.i("CONTENIDO",clave+" : "+valor);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(messengerServicio!=null){			
			Message msg=new Message();
			msg.what=ServicioClima.DESVINCULAR_CLIENTE;
			try {
				messengerServicio.send(msg);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			unbindService(conexion);
		}
		super.onBackPressed();
	}
	
}
