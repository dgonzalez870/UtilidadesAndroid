package com.prueba.utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class ServicioClima extends IntentService{

	public static final int REGISTRAR_CLIENTE=0;
	public static final int DESVINCULAR_CLIENTE=1;
	public static final int PUBLICAR_DATOS=2;
	public static final int FINALIZAR_SERVICIO=3;
	
	private boolean finalizar=false;
	Messenger messengerActivity;
	Handler servicioHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
//			super.handleMessage(msg);
			switch (msg.what) {
			case REGISTRAR_CLIENTE:
				Log.i("SERVICIO","Se ha registrado el cliente");
				messengerActivity=msg.replyTo;
				break;
			case DESVINCULAR_CLIENTE:
				messengerActivity=null;
				Log.i("SERVICIO","Se ha desvinculado el cliente");
				break;
			case FINALIZAR_SERVICIO:
				Log.i("SERVICIO", "Finalizar servicio");
				finalizar=true;
				break;
			default:
				break;
			}
		}
		
	};
	Messenger messengerServicio=new Messenger(servicioHandler);
	
	ServicioClienteActivity activity=new ServicioClienteActivity();
	public ServicioClima() {
		super("com.prueba.utilidades.ServicioClima");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		//for (int i = 0; i < 20; i++) {
		while(!finalizar){
			
			DatosClima datosClima=solicitarClima();
			Message message=Message.obtain(null, PUBLICAR_DATOS);
			Bundle data=new Bundle();
			data.putDouble("temp",datosClima.getMain().getTemp());
			data.putDouble("temp_min",datosClima.getMain().getTemp_max());
			data.putDouble("temp_max",datosClima.getMain().getTemp_min());
			data.putDouble("pressure",datosClima.getMain().getPressure());
			message.setData(data);
			if(messengerActivity!=null){
				
			try {
				messengerActivity.send(message);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}else{
				notificar();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Destruyendo el servicio", Toast.LENGTH_LONG).show();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Toast.makeText(this, "Iniciando servicioClima", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
//		return super.onBind(intent);
		return messengerServicio.getBinder();
	}
	
	public DatosClima solicitarClima(){
		String ruta="http://api.openweathermap.org/data/2.5/weather?q=Caracas";
		Gson gson=new Gson();
		DatosClima datosClima=null;
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
				datosClima=gson.fromJson(respuesta, DatosClima.class);
				return datosClima;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return datosClima;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return datosClima;
		}
	}
public void notificar(){
	NotificationCompat.Builder mBuilder =
	        new NotificationCompat.Builder(this)
	        .setSmallIcon(android.R.drawable.ic_menu_help)
	        .setContentTitle("Informacion del Clima")
	        .setContentText("Hay nueva informacion del Clima").setAutoCancel(true);
	// Creates an explicit intent for an Activity in your app
	Intent resultIntent = new Intent(this, ServicioClienteActivity.class);
	resultIntent.putExtra("desdeNotificacion", true);
	// The stack builder object will contain an artificial back stack for the
	// started Activity.
	// This ensures that navigating backward from the Activity leads out of
	// your application to the Home screen.
	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
	// Adds the back stack for the Intent (but not the Intent itself)
	stackBuilder.addParentStack(ServicioClienteActivity.class);
	// Adds the Intent that starts the Activity to the top of the stack
	stackBuilder.addNextIntent(resultIntent);
	PendingIntent resultPendingIntent =
	        stackBuilder.getPendingIntent(
	            0,
	            PendingIntent.FLAG_UPDATE_CURRENT
	        );
	mBuilder.setContentIntent(resultPendingIntent);
	NotificationManager mNotificationManager =
	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	// mId allows you to update the notification later on.
	mNotificationManager.notify(0, mBuilder.build());	
}
	
}
