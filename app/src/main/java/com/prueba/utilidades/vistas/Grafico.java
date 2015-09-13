package com.prueba.utilidades.vistas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

public class Grafico extends View 
{
	private Paint paint;
	private float eMin=-15f;
	private float eMax=15f;
	private String titulo="default";
	private Señal señal;
	private Grilla grilla;

	
	public Grafico(Context context, AttributeSet ats) 
	{
		super(context,ats);
		paint=new Paint();
		señal=new Señal();
		grilla=new Grilla();	
	}
	public void setRango(float rango){
		eMin=-rango;
		eMax=rango;
	}
	public void actualizarGrafico(float[] arreglo)
	{
		señal.actualizaSeñal(arreglo);
		invalidate();
	}
	public void setColorSenal(int color){
		señal.setColorSenal(color);
	}
		
	public void setTitulo(String titulo)
	{
		this.titulo=titulo;
	}
		
	public void setColorGrilla(int color){
		grilla.SetGrillaColor(color);
	}
	@Override
	protected void onMeasure(int widthSpec, int heightSpec )
	{
		int parentHeight=MeasureSpec.getSize(heightSpec);
		int parentWidth=MeasureSpec.getSize(widthSpec);
		setMeasuredDimension(parentWidth, parentHeight);
	}

	int nSegundos=10;
	int tamanoFuente=30;
	int ancho, alto, lado;
	int borde=0;
	int ancho_l, alto_l; //separacion entre las lineas de la grilla
	int totalPixeles_an, totalPixeles_al, y_offSet;
	@Override
	protected void onDraw(Canvas g)
	{
		alto=this.getHeight();
		ancho=this.getWidth();
		
		borde=2*tamanoFuente;
		totalPixeles_an=ancho-2*borde;
		ancho_l=totalPixeles_an/nSegundos;
		totalPixeles_al=alto-2*borde;
		alto_l=totalPixeles_al/4;
		grilla.dibujarGrilla(g,paint);		
		señal.dibujarSeñal(g,paint);
	}
	
	class Grilla
	{
		int color=Color.BLACK;
		public Grilla()
		{
		}
		public void SetGrillaColor(int color){
			this.color=color;
		}
		
		public void dibujarGrilla(Canvas c, Paint p)
		{
			p.setColor(this.color);
			p.setTextSize(tamanoFuente);
			p.setTextAlign(Align.CENTER);
			p.setStrokeWidth(1.5f);
			for(int i=0;i<=4;i++)
			{
				c.drawLine(0+borde+1.5f*tamanoFuente,borde+i*alto_l,ancho-borde+1.5f*tamanoFuente,borde+i*alto_l, p); //linea horizontal
				c.drawText(""+(eMax-i*eMax/2), 1.5f*tamanoFuente,borde+i*alto_l,p);
			}			
			for(int i=0;i<=nSegundos;i++)
			{
				c.drawLine(borde+i*ancho_l+1.5f*tamanoFuente,0+borde,borde+i*ancho_l+1.5f*tamanoFuente,alto-borde,p); //linea vertical
				c.drawText(""+(10*i/100.0), borde+i*ancho_l+1.5f*tamanoFuente, alto-tamanoFuente, p);
			}			
		}
	}
	class Señal
	{
		private int nMuestras=50;
		private float[] y=new float[nMuestras];	
		private int color;
		public Señal()
		{
			
		}
		
		public void setColorSenal(int color){
			this.color=color;
		}
		
		public void actualizaSeñal(float[] muestras){
			//corre los datos en el arreglo
			for (int i = 0; i < y.length-muestras.length; i++)
			{
				y[i]=y[muestras.length+i];  
			}
			//carga el nuevo bloque de datos al final del arreglo
			int bloque=nMuestras-muestras.length;
			for (int j = 0; j < muestras.length; j++)
			{
				y[bloque+j]=muestras[j];
			}
		}
		
		public void dibujarSeñal(Canvas c, Paint p)
		{
			float dx=totalPixeles_an/nMuestras;
			float x1=borde+tamanoFuente,x2=x1+dx,y1,y2;
			p.setColor(this.color);
			p.setStrokeWidth(3.5f);
			for (int i = 0; i <nMuestras-1; i++) 
			{
				y1=borde+(totalPixeles_al*(1-(float)(1.0*(y[i]-eMin)/(eMax-eMin))));
				y2=borde+(totalPixeles_al*(1-(float)(1.0*(y[i+1]-eMin)/(eMax-eMin))));
				x1+=dx;
				x2+=dx;
				c.drawLine(x1, y1, x2, y2,p);
			}
		}
	}
}
