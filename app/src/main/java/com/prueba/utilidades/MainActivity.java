package com.prueba.utilidades;


import java.util.ArrayList;
import com.prueba.utilidades.conexion.ListaBluetoothRemotos;
import com.prueba.utilidades.conexion.RedWifiInfoActivity;
import com.prueba.utilidades.conexion.TelefoniaLocalizacionActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	ListView lista;
	ArrayList<Item> items;
	Context contexto;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contexto=this;
		setContentView(R.layout.main_layout);
		lista=(ListView)findViewById(R.id.listView1);
		lista.setOnItemClickListener(onItemClicklistener);
		items=new ArrayList<Item>();
		items.add(new Item(R.drawable.bt,R.string.bluetooth,null));
		items.add(new Item(R.drawable.chronometer,R.string.cron_metro,null));
		items.add(new Item(R.drawable.sensors,R.string.sensores,null));
		items.add(new Item(R.drawable.wifi,R.string.red,null));
		items.add(new Item(R.drawable.location,R.string.localizaci_n_y_telefon_a,null));
		items.add(new Item(R.drawable.clima,R.string.informaci_n_del_clima,null));
		lista.setAdapter(new ItemAdapter(this, items));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

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
	
	OnItemClickListener onItemClicklistener=new OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int position,
				long id) {
			switch (position) {
			case 0:
				startActivity(new Intent(getApplicationContext(),ListaBluetoothRemotos.class));
				break;
			case 1:
				startActivity(new Intent(getApplicationContext(),CronometroActivity.class));
				break;
			case 2:
				startActivity(new Intent(getApplicationContext(),SensoresActivity.class));
				break;
			case 3:
				startActivity(new Intent(getApplicationContext(),RedWifiInfoActivity.class));
				break;
			case 4:
				startActivity(new Intent(getApplicationContext(),TelefoniaLocalizacionActivity.class));
				break;
			case 5:
				startActivity(new Intent(getApplicationContext(),ServicioClienteActivity.class));
				break;

			default:
				break;
			}
		}
	};

}
