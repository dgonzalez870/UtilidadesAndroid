package com.prueba.utilidades;

import java.util.Currency;
import java.util.Vector;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.Toast;


public class CronometroActivity extends Activity {

	Vector<String> registros=new Vector<String>();
	ArrayAdapter<String> adapter;
	Button btnIniciar;
	Chronometer cronometro;
	ListView listaRegistros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cronometro);
        cronometro=(Chronometer)findViewById(R.id.chronometer1);
        btnIniciar=(Button) findViewById(R.id.btn_iniciar);
        listaRegistros=(ListView) findViewById(R.id.listView1);
		adapter= new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_item_lista, registros);
		listaRegistros.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	BDHelper helper=new BDHelper(getApplicationContext(), BDHelper.NOMBRE_BD, null, 2);
    	SQLiteDatabase bd=helper.getReadableDatabase();
    	String[] columnas=new String[]{"ID","PERIODO"};
    	Cursor cursor=bd.query(BDHelper.NOMBRE_TABLA_REGISTROS, columnas, "ID > ?", new String[]{"10"}, null, null, null);
    	if(cursor.moveToFirst()){
    		int indice=0;
    		String[] reg_bd=new String[cursor.getCount()];
    		do{
    			reg_bd[indice]=cursor.getString(cursor.getColumnIndex("PERIODO"));
    			adapter.add(reg_bd[indice]);
    			indice++;
    		}while(cursor.moveToNext());
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cronometro, menu);
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	cronometro.stop();
    	finish();
    }
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    }

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    boolean iniciado=false;
    public void controlCronometro(View v){
    	if(!iniciado){
    		iniciado=true;
    		//actualiza la referencia para que inicie de cero
    		cronometro.start();
    		cronometro.setBase(SystemClock.elapsedRealtime());
    		btnIniciar.setText("Detener");
    	}else{
    		cronometro.stop();
    		iniciado=false;
    		btnIniciar.setText("Iniciar");
    		String valor=cronometro.getText().toString();
    		adapter.add(valor);
    		guardarRegistro(valor);
    	}
    }
    
    public void guardarRegistro(String valor){
    	BDHelper helper=new BDHelper(getApplicationContext(), BDHelper.NOMBRE_BD, null, 1);
    	SQLiteDatabase bd=helper.getWritableDatabase();
    	ContentValues contenido=new ContentValues();
    	contenido.put("PERIODO", valor);
    	if(bd.insert(BDHelper.NOMBRE_TABLA_REGISTROS, null, contenido)==-1){
    		Toast.makeText(getApplicationContext(), "Error al insertar valor", Toast.LENGTH_LONG).show();
    	}
    	bd.close();
    }
}
