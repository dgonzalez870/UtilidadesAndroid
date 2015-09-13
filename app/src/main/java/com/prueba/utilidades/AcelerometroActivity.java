package com.prueba.utilidades;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.prueba.utilidades.vistas.Grafico;

public class AcelerometroActivity extends Activity {

	Context contexto;
	static final int N_MUESTRAS = 50; // Se predefine para que opere a 50
										// muestras por segundo
	static final int PERIODO = 1000000 / N_MUESTRAS; // define el período de
														// muestreo en micro
														// segundos
	int nMuestras = 5; // array de muestras guardadas
						// antes de ser graficadas
	SensorManager manager;
	Sensor sensor;
	Grafico grafico;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contexto=this;
		setContentView(R.layout.layout_grafico);
		grafico = (Grafico) findViewById(R.id.grafico1);
		grafico.setColorSenal(Color.RED);
		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// crea una instancia del sensor acelerometro
		sensor = manager.getDefaultSensor(getIntent().getIntExtra("tipoSensor",
				Sensor.TYPE_ACCELEROMETER));
		grafico.setRango(sensor.getMaximumRange()/2);
		// los datos de aceleración se reciben a través de un listener que debe
		// ser reistrado
		manager.registerListener(listener, sensor, PERIODO);
	}

	/**
	 * Listener que recibe las notificaciones de cambios de datos en los
	 * sensores, los datos de aceleración no son solicitados directamente, deben
	 * ser procesados a través del listener
	 */
	SensorEventListener listener = new SensorEventListener() {
		
		int iMuestras = 0; //indice de las muestras tomadas antes de ser graficadas
		float[] muestrasX = new float[nMuestras];
		float[] muestrasY = new float[nMuestras];
		float[] muestrasZ = new float[nMuestras];
		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				float acel_x = event.values[0];
				float acel_y = event.values[1];
				float acel_z = event.values[2];
				muestrasX[iMuestras] = acel_x;
				muestrasY[iMuestras] = acel_y;
				muestrasZ[iMuestras] = acel_z;
				iMuestras++;
				if (iMuestras == 5) {
					iMuestras = 0;
					//manda a graficar los datos
					grafico.actualizarGrafico(muestrasX);
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};

	public void onBackPressed() {
		// elimina el vinculo con el listener para dejar de recibir
		// notificaciones
		manager.unregisterListener(listener,sensor);
		super.onBackPressed();
	};
		
}
