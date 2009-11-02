/*
 * Created on 09-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.Utilidades;

import java.lang.Math;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UtilidadesNumero {

	static public double redondea (double numero, int precision)
	{
	    double aux = Math.pow(10, precision);
	    double entero = Math.round(numero * aux);
// RGG cambio para redondeo correcto	    return entero/aux;
	    return Math.rint(entero)/aux;
	}
	 
	static public float redondea (float numero, int precision) 
	{
	    double d = numero;
	    return (float) UtilidadesNumero.redondea(d, precision);
	}
	
	static public String redondea (String numero, int precision)
	{
	    double d =  Double.parseDouble(numero);
	    return String.valueOf(UtilidadesNumero.redondea(d,precision));
	}
		

	static public String formato (String numero)
	{
	   return formatear(numero,true);
	}
	
	static public String formatoCartaPago (String numero)
	{
	   return formatear(numero,true);
	}

	static public String formatoCampo (String numero)
	{
	   return formatear(numero,false);
	}

	static public String formato (double numero)
	{
	   return formatear(""+numero,true);
	}

	static public String formatoCampo (double numero)
	{
	   return formatear(""+numero,false);
	}

	static public String formato (float numero)
	{
	   return formatear(""+numero,true);
	}

	static public String formatoCampo (float numero)
	{
	   return formatear(""+numero,false);
	}

	//LMS 30/08/2006
	//Método que devuelve un String con un número formateado siempre con 2 decimales,
	//y con separador de miles cuando así se le especifique.
	//Es privado para obligar a usar los otros métodos que a su vez llaman a este.
	private static String formatear(String sNumero, boolean bMiles)
	{
		try
		{
			String sNumeroAux = sNumero.replaceAll(",",".");
			
			Double dNumero = new Double(sNumeroAux);
			
			DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			
			dfs.setDecimalSeparator(',');  //Separador de decimales.
			dfs.setGroupingSeparator('.'); //Separador de miles.
	
			df.setMinimumFractionDigits(2); //Mínimo 2 decimales.
			df.setMaximumFractionDigits(2); //Máximo 2 decimales.
			
			df.setGroupingUsed(bMiles); //Muestra el separador de miles en función del parámetro.
			
			df.setDecimalFormatSymbols(dfs);
			
			return df.format(dNumero); //Formatea el número.
		}
		
		catch(Exception e)
		{
			return sNumero; //En caso de error se devuelve el número tal cual, sin formatear.
		}
	}
	static public double getDouble (String numero) 
	{
		numero = UtilidadesString.replaceAllIgnoreCase(numero, ".", "");
		numero = UtilidadesString.replaceAllIgnoreCase(numero, ",", ".");
		return Double.parseDouble(numero);
	}
	
	/**
	 * Redondea el numero <code>d</code> a <code>dec</code> decimales.
	 * @param d
	 * @param dec
	 * @return
	 */
	public static double round(String d, int dec){
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(dec,BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

}
