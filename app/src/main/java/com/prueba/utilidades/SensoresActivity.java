package com.prueba.utilidades;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SensoresActivity extends Activity{

	SensorManager manager;
	Sensor sensor;
	ListView lista;
	List<Sensor> listaSensores;
	Context contexto;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_sensores);
		lista=(ListView) findViewById(R.id.lista);
		manager=(SensorManager) getSystemService(SENSOR_SERVICE);
		listaSensores=manager.getSensorList(Sensor.TYPE_ALL);
		String[] sensores=new String[listaSensores.size()];
		for (int i = 0; i < sensores.length; i++) {
			sensores[i]=listaSensores.get(i).getName()+" "+listaSensores.get(i).getMaximumRange();
		}		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sensores); 
		lista.setAdapter(adapter);
		lista.setOnItemClickListener(listener);
		contexto=this;
	}
	
	
	/**
	 * Registra los eventos de toque sobre los elementos de la lista
	 */
	OnItemClickListener listener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
				Intent intent=new Intent(contexto, AcelerometroActivity.class);
				intent.putExtra("tipoSensor", listaSensores.get(arg2).getType());
				startActivity(intent);
//			Toast.makeText(getApplicationContext(),"Hola", Toast.LENGTH_LONG).show();
		}
	};

}