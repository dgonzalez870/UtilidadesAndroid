package com.prueba.utilidades;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String accion=intent.getAction();
		if(accion.equals(Intent.ACTION_BOOT_COMPLETED)){
			context.startService(new Intent(context,ServicioClima.class));
		}
	}

}
