package com.prueba.utilidades.conexion;

import java.util.Set;
import java.util.Vector;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class ListaBluetoothRemotos extends ListActivity 
{
	public static String EXTRA_DEVICE_ADDRESS = "device_address";//Identificador para los extras que se cargan en el intent de resultado
	public static final int REQUEST_ENABLE_BT=0; //código de solicitud de encendido bluetooth
    private BluetoothAdapter mBtAdapter;

    private Vector<String> nombres = new Vector<String>();  //se guardan los dispositivos encontrados
    private Vector<String> direcciones = new Vector<String>();  //se guardan los dispositivos encontrados
    private ArrayAdapter<String> adapterDispositivos;
    private Set<BluetoothDevice> pairedDevices;
    
    /**
     * Métodos del activity
     */
    @Override
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //verifica la característica barra de progreso en lña barra de título
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        // resultado cancelado en caso de salir
        setResult(Activity.RESULT_CANCELED);
        // detecta el adaptador bluetooth local
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBtAdapter!=null){
        	nombres.add("Buscar nuevos"); //encabeza la lista de dispositivos la opción de buscar nuevos
        	direcciones.add(null);        //para mantener una correlación entre los dos vectores

        	adapterDispositivos = new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1, nombres);
        
        	// obtiene un set de dispositivos bluetooth almacenados si el adaptador Bluetooth esta encendido
        	pairedDevices = mBtAdapter.getBondedDevices();
        	setListAdapter(adapterDispositivos);

        	// si hay dispositivos almacenados añade cada uno al adaptador de la lista
        	if (pairedDevices.size() > 0) 
        	{
        		for (BluetoothDevice device : pairedDevices) 
        		{
        			adapterDispositivos.add(device.getName() + "\n" + device.getAddress());
        			direcciones.add(device.getAddress());
        		}
        	} 
        
        	// Registra el broadcast para las acciones de descubrimiento 
        	this.registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        	// Registra broadcast para cuando la busqueda ha finalizado
        	this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));        
        	this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        }else{
        	Toast.makeText(this, "El dispositivo no soporta Bluetooth", Toast.LENGTH_LONG).show();
        	finish();
        }
    }
    
    @Override
	protected void onStart()
    {
    	super.onStart();
    	if (!mBtAdapter.isEnabled()) {
    	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    	}
    }
    
    @Override
	protected void onPause()
    {
    	super.onPause();
    }
    
    @Override
	protected void onResume()
    {
    	super.onResume();
    }
    
    @Override
	protected void onStop()
    {
    	super.onStop();
    }
    @Override
	protected void onDestroy() 
    {
        super.onDestroy();
        if(mBtAdapter!=null){
        	// se asegura de no estar en proceso de descubrimiento
        	if (mBtAdapter.isDiscovering()) {
        		mBtAdapter.cancelDiscovery();
        	}
        	// quita el registro al broadcast
        	this.unregisterReceiver(mReceiver);
        }
    }

    /**
     * listener de eventos en la seleccion de elementos de la lista
     */
    @Override
	protected void onListItemClick (ListView l, View v, int position, long id)
    {
    	if(position==0)
    	{
    		setProgressBarIndeterminateVisibility(true); //muestra barra de progreso en la barra de titulo
    		if(mBtAdapter.isDiscovering())
    		{
    			mBtAdapter.cancelDiscovery();
    			System.out.println("Cancelado");
    		}
    		else
    		{
    			
    			adapterDispositivos.clear();
    			adapterDispositivos.add("Buscar nuevos");
    			direcciones.removeAllElements();
    			direcciones.add(null);
    			mBtAdapter.startDiscovery(); //inicia la busqueda de dispositivos
    			System.out.println("Buscando...");
    		}
    	}
    	else
    	{
    		String address=direcciones.elementAt(position);
            // crea el resultado e incluye la dirección bluetooth 
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // configura el resultado y finaliza
            setResult(Activity.RESULT_OK, intent);
            finish();
    	}
    }
    
    /**
     * Se reciben los eventos derivados de la búsqueda de dispositivos bluetooth en el área
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
		public void onReceive(Context context, Intent intent) 
        {
            String accion = intent.getAction();

            // Se encuentra un dispositivo
            if (BluetoothDevice.ACTION_FOUND.equals(accion)) {
                // obtiene la dirección bluetooth desde el intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                adapterDispositivos.add(device.getName());
                direcciones.add(device.getAddress());
            } 
            // cuando la búsqueda ha finalizado cambia el título del Activity
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(accion)) 
            {
            	System.out.println("finalizada la busqueda");
                setProgressBarIndeterminateVisibility(false);
                if(direcciones.size()==1)
                {
                }
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(accion))
            	System.out.println("Iniciando busqueda");
        }
    };
    
    /**
     * Maneja el resultado de la solicitud de encendido del adaptador bluetooth si es OK lo enciende
     * en caso contrario termina la actividad.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (resultCode) {
		case RESULT_OK:
			Toast.makeText(this, "Bluetooth encendido", Toast.LENGTH_LONG).show();
			break;
		case RESULT_CANCELED:
			Toast.makeText(this, "No se ha ensendido el Bluetooth", Toast.LENGTH_LONG).show();
			finish();
			break;
		default:
			break;
		}
    };
}
