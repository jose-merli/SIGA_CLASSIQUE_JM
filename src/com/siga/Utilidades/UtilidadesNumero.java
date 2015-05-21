/*
 * Created on 09-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.Utilidades;

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

	static public double redondea (double numero, int precision) {
		if (numero<0)
			return -1 * Math.round(-1 * numero * Math.pow(10, precision)) / Math.pow(10, precision);
		else
			return Math.round(numero * Math.pow(10, precision)) / Math.pow(10, precision);
	}
	 
	static public float redondea (float numero, int precision) {
	    double d = numero;
	    return (float) redondea(d, precision);
	}
	
	static public String redondea (String numero, int precision) {
	    double d =  Double.parseDouble(numero);
	    return String.valueOf(redondea(d,precision));
	}
		

	static public String formato (String numero)
	{
	   return formatear(numero,true);
	}
	
	static public String formatoCartaPago (String numero)
	{
	   return formatear(numero,true);
	}
	/**
	 * 
	 * los miles el punto, los decimale las coma,
	 * 103.652,35(ciento tres mil seiscientos cincuenta y dos con treinta y cinco)
	 * 
	 * @param numero
	 * @return
	 */
	static public String formatoCartas(double numero)
	{
		
		DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');  //Separador de decimales.
		dfs.setGroupingSeparator('.'); //Separador de miles.
		df.setMinimumFractionDigits(2); //M�nimo 2 decimales.
		df.setMaximumFractionDigits(2); //M�ximo 2 decimales.
		df.setGroupingUsed(true); //Muestra el separador de miles en funci�n del par�metro.
		df.setDecimalFormatSymbols(dfs);
		return df.format(numero);
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
	//M�todo que devuelve un String con un n�mero formateado siempre con 2 decimales,
	//y con separador de miles cuando as� se le especifique.
	//Es privado para obligar a usar los otros m�todos que a su vez llaman a este.
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
	
			df.setMinimumFractionDigits(2); //M�nimo 2 decimales.
			df.setMaximumFractionDigits(2); //M�ximo 2 decimales.
			
			df.setGroupingUsed(bMiles); //Muestra el separador de miles en funci�n del par�metro.
			
			df.setDecimalFormatSymbols(dfs);
			
			return df.format(dNumero); //Formatea el n�mero.
		}
		
		catch(Exception e)
		{
			return sNumero; //En caso de error se devuelve el n�mero tal cual, sin formatear.
		}
	}
	static public double getDouble (String numero) 
	{
		numero = UtilidadesString.replaceAllIgnoreCase(numero, ".", "");
		numero = UtilidadesString.replaceAllIgnoreCase(numero, ",", ".");
		return Double.parseDouble(numero);
	}
	static public BigDecimal getBigDecimal (String numero) 
	{
		numero = UtilidadesString.replaceAllIgnoreCase(numero, ".", "");
		numero = UtilidadesString.replaceAllIgnoreCase(numero, ",", ".");
		return new BigDecimal(numero);
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

	public static Integer parseInt(String nColegiado) {
		try{
			return Integer.parseInt(nColegiado);
		}
		catch(Exception e){
			return null;
		}
	}

}
