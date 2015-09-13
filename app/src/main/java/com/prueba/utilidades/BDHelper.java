package com.prueba.utilidades;

import android.database.SQLException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BDHelper extends SQLiteOpenHelper{

	public static final String NOMBRE_BD="REGISTROS";
	public static final String NOMBRE_TABLA_REGISTROS="TREGISTROS";
	private final String CREAR_TABLA_TR="CREATE TABLE TREGISTROS(ID INTEGER PRIMARY KEY AUTOINCREMENT, PERIODO TEXT)";
	private final String CREAR_TABLA_USUARIOS="CREATE TABLE TUSUARIOS(ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE TEXT)";
	public BDHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREAR_TABLA_TR);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.beginTransaction();
		try{
			db.execSQL(CREAR_TABLA_USUARIOS);
			db.setTransactionSuccessful();
		}catch(SQLException e){
			Log.e("DBHelper",e.getMessage().toString());
		}
		finally{
			db.endTransaction();
		}
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		db.beginTransaction();
		try{
			super.onDowngrade(db, oldVersion, newVersion);
			db.execSQL("DROP TABLE TUSUARIOS");
			db.setTransactionSuccessful();
		}catch(SQLException e){
			Log.e("DBHelper",e.getMessage().toString());
		}
		finally{
			db.endTransaction();
		}
	}
}
