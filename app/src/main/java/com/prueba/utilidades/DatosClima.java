package com.prueba.utilidades;

public class DatosClima {

	private Main main;
	private Coordenadas coord;
	
	
	
	public Main getMain() {
		return main;
	}

	public Coordenadas getCoord() {
		return coord;
	}

	public class Main{
		private double temp;
		private double temp_min;
		private double temp_max;
		private double pressure;
		private double humidity;
		public double getTemp() {
			return temp;
		}
		public double getTemp_min() {
			return temp_min;
		}
		public double getTemp_max() {
			return temp_max;
		}
		public double getPressure() {
			return pressure;
		}
		public double getHumidity() {
			return humidity;
		}
	}
	
	public class Coordenadas{
		private double lat;
		private double lon;
		public double getLat() {
			return lat;
		}
		public double getLon() {
			return lon;
		}
		
	}
}
