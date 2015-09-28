/*
 * Created on 09-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.Utilidades;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
		
		if (Double.isNaN(numero)) // Contolo NaN 
			return 0.0;
		
		// Calcula el signo
		BigDecimal bdSigno = new BigDecimal("1");
		if (numero<0) {
			bdSigno = new BigDecimal("-1");
		}
				
		// Calcula la precision
		BigDecimal bdPrecision = new BigDecimal("1");
		for (int i=0; i<precision; i++) {
			bdPrecision = bdPrecision.multiply(new BigDecimal("10"));
		}
		
		BigDecimal bCalculo = BigDecimal.valueOf(numero); // Conversion double to BigDecimal
		
		bCalculo = bCalculo.multiply(bdSigno); // Control inicial del signo
		
		bCalculo = bCalculo.multiply(bdPrecision); // Pone la parte decimal dentro de la precision como entero
		
		bCalculo = bCalculo.add(new BigDecimal("0.5")); // Sumo 0.5
		
		RoundingMode RM = RoundingMode.DOWN;
		bCalculo = bCalculo.setScale(0, RM); // Obtengo la parte entera		
		//bCalculo = BigDecimal.valueOf(bCalculo.intValue()); 
		
		bCalculo = bCalculo.divide(bdPrecision); // Vuelvo a poner la parte decimal
		
		bCalculo = bCalculo.multiply(bdSigno); // Control final del signo
		
		return bCalculo.doubleValue(); 
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
		df.setMinimumFractionDigits(2); //Mínimo 2 decimales.
		df.setMaximumFractionDigits(2); //Máximo 2 decimales.
		df.setGroupingUsed(true); //Muestra el separador de miles en función del parámetro.
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
