package com.prueba.utilidades.conexion;

public class ItemTexto {

	private String id_dato,valor_dato;
    public ItemTexto() {
        super();
    }
 
    public ItemTexto(String id_dato, String valor_dato) {
    	super();
    	this.id_dato=id_dato;
    	this.valor_dato=valor_dato;
    }
 
    public String getIdDato() {
        return id_dato;
    }
 
    public String getValorDato() {
        return valor_dato;
    }
     
}
