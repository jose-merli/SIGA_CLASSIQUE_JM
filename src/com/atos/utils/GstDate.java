/**
 * <p>Title: GstDate </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import java.util.Vector;

/*
Letter  Date or Time Component  Presentation  Examples
G  Era designator  Text  AD
y  Year  Year  1996; 96
M  Month in year  Month  July; Jul; 07
w  Week in year  Number  27
W  Week in month  Number  2
D  Day in year  Number  189
d  Day in month  Number  10
F  Day of week in month  Number  2
E  Day in week  Text  Tuesday; Tue
a  Am/pm marker  Text  PM
H  Hour in day (0-23)  Number  0
k  Hour in day (1-24)  Number  24
K  Hour in am/pm (0-11)  Number  0
h  Hour in am/pm (1-12)  Number  12
m  Minute in hour  Number  30
s  Second in minute  Number  55
S  Millisecond  Number  978
z  Time zone  General time zone  Pacific Standard Time; PST; GMT-08:00
Z  Time zone  RFC 822 time zone  -0800
*/

/***************/
/*

format and date  Result

"yyyy.MM.dd G 'at' HH:mm:ss z"  2001.07.04 AD at 12:08:56 PDT
"EEE, MMM d, ''yy"      		Wed, Jul 4, '01
"h:mm a"  						12:08 PM
"hh 'o''clock' a, zzzz"  		12 o'clock PM, Pacific Daylight Time
"K:mm a, z"  					0:08 PM, PDT
"yyyyy.MMMMM.dd GGG hh:mm aaa"  02001.July.04 AD 12:08 PM
"EEE, d MMM yyyy HH:mm:ss Z"  	Wed, 4 Jul 2001 12:08:56 -0700
"yyMMddHHmmssZ"  				010704120856-0700


*/

public class GstDate {

  public GstDate() {
  }

  public Date parseStringToDate(String date, String format, Locale locale)throws ClsExceptions{
    Date dat=new Date();
    SimpleDateFormat sdf = new SimpleDateFormat(format,locale);
    try {
      dat = sdf.parse(date);
    }
    catch (Exception e) {
    	ClsExceptions exc=new ClsExceptions(e,"error en GstDate     :"+e.toString());
    	exc.setErrorCode("DATEFORMAT");
    	throw exc;
    }
    return dat;
  }

  public String parseDateToString(Date date, String format, Locale locale)throws ClsExceptions{
    String dat="";
    SimpleDateFormat sdf = new SimpleDateFormat(format,locale);
    try {
      dat = sdf.format(date);
    }
    catch (Exception e) {
    	ClsExceptions exc=new ClsExceptions(e,"error en GstDate     :"+e.toString());
    	exc.setErrorCode("DATEFORMAT");
    	throw exc;
    }
    return dat;
  }

  public static String getFormatedDateShort(String lang, String date) throws ClsExceptions
  {
    String dat="";
    SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
    Date datFormat = null;

    if (date!=null && !date.trim().equals("")){
      try {
      	
// DCG      	date = GstDate.getFechaLenguaje(lang, date);
        datFormat = sdf.parse(date.trim());
      } catch (ParseException ex) {
      	ClsExceptions exc=new ClsExceptions(ex,"THIS DATE "+date+" IS BAD FORMATED");
      	exc.setErrorCode("DATEFORMAT");
        throw exc;
      }

      if (lang.equalsIgnoreCase("EN")) {
      	sdf.applyPattern(ClsConstants.DATE_FORMAT_SHORT_ENGLISH);
      }
      else {
      	sdf.applyPattern(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
      }
      dat=sdf.format(datFormat);
    }
    return dat;
  }

  public static String getFormatedDateShort(String lang, Object odate) throws ClsExceptions
  {
  	String dat="";
  	String date="";
    SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
    Date datFormat = null;
    if (odate!=null) {
    	date = (String) odate;	
	    if (!date.trim().equals("")){
	      try {
	      	
	// DCG      	date = GstDate.getFechaLenguaje(lang, date);
	        datFormat = sdf.parse(date.trim());
	      } catch (ParseException ex) {
	      	ClsExceptions exc=new ClsExceptions(ex,"THIS DATE "+date+" IS BAD FORMATED");
	      	exc.setErrorCode("DATEFORMAT");
	        throw exc;	      	
	      }
	
	      if (lang.equalsIgnoreCase("EN")) {
	      	sdf.applyPattern(ClsConstants.DATE_FORMAT_SHORT_ENGLISH);
	      }
	      else {
	      	sdf.applyPattern(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
	      }
	      dat=sdf.format(datFormat);
	    }
    }
    return dat;
  }


  public static String getFormatedDateLong(String lang, String date) throws ClsExceptions
  {
    String dat="";
    SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
    Date datFormat = null;

    if (date!=null && !date.equals("")){
      try {
        datFormat = sdf.parse(date);
      } catch (ParseException ex) {
      	ClsExceptions exc=new ClsExceptions(ex,"THIS DATE "+date+" IS BAD FORMATED");
      	exc.setErrorCode("DATEFORMAT");
        throw exc;      	
      }

      if (lang.equalsIgnoreCase("EN")) {
        sdf.applyPattern(ClsConstants.DATE_FORMAT_LONG_ENGLISH);
      }
      else {
        sdf.applyPattern(ClsConstants.DATE_FORMAT_LONG_SPANISH);
      }
      dat=sdf.format(datFormat);
    }
    return dat;
  }

  public static String getFormatedDateMedium(String lang, String date) throws ClsExceptions
  {
    String dat="";
    SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
    Date datFormat = null;

    if (date!=null && !date.equals("")){
      try {
        datFormat = sdf.parse(date);
      }
      catch (ParseException ex) {
      	ClsExceptions exc=new ClsExceptions(ex,"THIS DATE "+date+" IS BAD FORMATED");
      	exc.setErrorCode("DATEFORMAT");
        throw exc;      	
      }

      if (lang.equalsIgnoreCase("EN")) {
        sdf.applyPattern(ClsConstants.DATE_FORMAT_MEDIUM_ENGLISH);
      }
      else {
        sdf.applyPattern(ClsConstants.DATE_FORMAT_MEDIUM_SPANISH);
      }

      dat=sdf.format(datFormat);
    }
    return dat;
  }

  public static String getApplicationFormatDate(String lang, String date) throws ClsExceptions
  {
	if(date==null || date.length()==0)return null;  
    String dat="";
    Date oDate = null;
    SimpleDateFormat sdf;
    sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_LONG_ENGLISH);
    if(date.length()==11 && date.substring(2,3).equals(".")){
    	String DATE_FORMAT_LOGBOOK = "dd.MMM.yyyy";
        if (lang.equalsIgnoreCase("EN")) {
        	oDate = new GstDate().parseStringToDate(date,DATE_FORMAT_LOGBOOK,Locale.ENGLISH);
        }
        else {
//Espanhol  	oDate = new GstDate().parseStringToDate(date,DATE_FORMAT_LOGBOOK,Locale.ENGLISH);
        }
    }
    else	  
    	if(date.length()==20 && date.substring(4,5).equals("-")){
    		String DATE_FORMAT_LOGBOOK = "dd.MMM.yyyy";
            if (lang.equalsIgnoreCase("EN")) {
            	oDate=new GstDate().parseStringToDate(date,DATE_FORMAT_LOGBOOK,Locale.ENGLISH);
            }
            else {
//Espanhol      	oDate=new GstDate().parseStringToDate(date,DATE_FORMAT_LOGBOOK,Locale.ENGLISH);
            }
    	}
    	else{
	      if(date.length()> 11 ){
	        //LONG
            if (lang.equalsIgnoreCase("EN")) {
            	sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_LONG_ENGLISH);
            }
            else {
            	sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_LONG_SPANISH);
            }
	      }
	      else{
	        //SHORT
            if (lang.equalsIgnoreCase("EN")) {
            	sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_ENGLISH);
            }
            else {
            	sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
            }
	      }

	      try {
	      	oDate = sdf.parse(date);
	      }
	      catch (ParseException ex) {
	      	ClsExceptions exc=new ClsExceptions(ex,"THIS DATE "+date+" IS BAD FORMATED");
	      	exc.setErrorCode("DATEFORMAT");
	        throw exc;	      	
	      }
    	}
    if (oDate == null) {
	 sdf.applyPattern(ClsConstants.DATE_FORMAT_JAVA);
	  try {
	    oDate = sdf.parse(date);
	  } 
	  catch (ParseException ex) {
      	ClsExceptions exc=new ClsExceptions(ex,"THIS DATE "+date+" IS BAD FORMATED");
      	exc.setErrorCode("DATEFORMAT");
        throw exc;	  	
	  }
	}
	    
   sdf.applyPattern(ClsConstants.DATE_FORMAT_JAVA);
   dat=sdf.format(oDate);
   return dat;
 }
  
  
  


/*
 public String parseDatetoString(Date date, String format, Locale locale)throws ClsExceptions{
   String dat="";
   SimpleDateFormat sdf = new SimpleDateFormat(format,locale);
   try {
     sdf.format(date,new FieldPosition(0));
//			  Date d = sdf.parse(date, new ParsePosition(0));
   }
   catch (Exception e) {
      throw new ClsExceptions("error en GstDate     :"+e.toString());
   }
   return dat;
  }
*/

  
  // Daniel Campos
  public static String getFechaLenguaje (String lenguaje, String date) throws ClsExceptions 
  {
	SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA); // Ingles
	Date datFormat = null;
	
	if (date!=null &&!date.equals("")){
		try {
			datFormat = sdf.parse(date);
			if (!lenguaje.equalsIgnoreCase("en")) {
				sdf.applyPattern(ClsConstants.DATE_FORMAT_LONG_SPANISH);
			}
			return sdf.format(datFormat);
		} 
		catch (ParseException ex) {
			// La fecha esta en espanhol
			sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_LONG_SPANISH);
			try {
				datFormat = sdf.parse(date);
				if (lenguaje.equalsIgnoreCase("en")) {
					sdf.applyPattern(ClsConstants.DATE_FORMAT_LONG_ENGLISH);
				}
				return sdf.format(datFormat);
			}
			catch (Exception e) {
				if (date.indexOf(":") > 0){
			      	ClsExceptions exc=new ClsExceptions(ex,"Formato de fecha no reconocido");
			      	exc.setErrorCode("DATEFORMAT");
			        throw exc;					
				}
				else {
					return GstDate.getFechaLenguaje(lenguaje, date+" 00:00:00");
				}
			}
		}
	}
	return date;
  }
  
 /** Funcion dateBetween0and24h (String nombreColumna, String fecha)<br>
   * Devuelve un String que contiene una condición AND para buscar registros cuya 
   * fecha se encuentra entre las 0h y las 24h del día (dd/mm/yyyy) que indica <b>fecha</b>.<br>
   * Ejemplo: fecha = 03/01/2005, return = AND NOMBRECOLUMNA >= 2005/01/03 00:00:00 AND NOMBRECOLUMNA <= 2005/01/03 23:59:59
   * @param nombreColumna Nombre de la columna a filtrar
   * @param fecha Día en formato dd/mm/yyyy
   * @return String con condición AND
   * @throws ClsExceptions 
   **/
	public static String dateBetween0and24h(String nombreColumna, String fecha) throws ClsExceptions{	
		String result = "";	
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA); 
		
		try {
			String fechaIni = GstDate.getApplicationFormatDate("",fecha);
			Date d=sdf.parse(fechaIni);
			d.setTime(d.getTime()+86399000);
			String fechaFin = (sdf.format(d));
			
			result = nombreColumna +" >= TO_DATE('" + fechaIni + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
			result += " AND "+ nombreColumna +" <= TO_DATE('" + fechaFin + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
						
		} catch (Exception e) {
	      	ClsExceptions exc=new ClsExceptions(e,"Formato de fecha no reconocido");
	      	exc.setErrorCode("DATEFORMAT");
	        throw exc;
		}
	
		
		return result;
	}

	public static String dateSuma24hFormatoJava(String fecha) throws ClsExceptions{	
		String result = "";	
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA); 
		
		try {
			String fechaIni = GstDate.getApplicationFormatDate("",fecha);
			Date d=sdf.parse(fechaIni);
			d.setTime(d.getTime()+86399000);
			result = (sdf.format(d));
			
		} catch (Exception e) {
	      	ClsExceptions exc=new ClsExceptions(e,"Formato de fecha no reconocido");
	      	exc.setErrorCode("DATEFORMAT");
	        throw exc;
		}
	
		
		return result;
	}

	public static String dateSumaDiasJava(String fecha,Integer dias) throws ClsExceptions{	
		String result = "";	
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA); 
		
		try {
			String fechaIni = fecha;
			Date d=sdf.parse(fechaIni);
			d.setTime(d.getTime()+86399000*dias.intValue());
			result = (sdf.format(d));
			
		} catch (Exception e) {
	      	ClsExceptions exc=new ClsExceptions(e,"Formato de fecha no reconocido");
	      	exc.setErrorCode("DATEFORMAT");
	        throw exc;
		}
	
		
		return result;
	}

	public static String dateFormatoJava(String fecha) throws ClsExceptions{	
		String result = "";	
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA); 
		
		try {
			String fechaIni = GstDate.getApplicationFormatDate("",fecha);
			Date d=sdf.parse(fechaIni);
			result = (sdf.format(d));
			
		} catch (Exception e) {
	      	ClsExceptions exc=new ClsExceptions(e,"Formato de fecha no reconocido");
	      	exc.setErrorCode("DATEFORMAT");
	        throw exc;
		}
	
		
		return result;
	}

	public static String getYear(Date fecha) throws ClsExceptions{	
		String result = "";	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); 
		
		try {
			result = (sdf.format(fecha));
			
		} catch (Exception e) {
	      	ClsExceptions exc=new ClsExceptions(e,"Formato de fecha no reconocido");
	      	exc.setErrorCode("DATEFORMAT");
	        throw exc;
		}
		
		return result;
	}

	/**
	 * 
	 * @param nombreColumna
	 * @param fechaDesde en formato YYYY/MM/DD HH:mm:ss
	 * @param fechaHasta en formato YYYY/MM/DD HH:mm:ss
	 * @return String con la sentencia " nombrecampo>=fechaDesde and nombrecampo<=fechaHasta "
	 * @throws ClsExceptions
	 */
	public static String dateBetweenDesdeAndHasta(String nombreColumna, String fechaDesde, String fechaHasta) throws ClsExceptions{	
		String result = "";	
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA); 
		
		try {
			String fechaIni = fechaDesde;
			String fechaFin = fechaHasta;
			if (fechaFin!=null && !fechaFin.trim().equals("")) {
				Date d=sdf.parse(fechaFin);
				d.setTime(d.getTime()+86399000);
				fechaFin = (sdf.format(d));
			}
			boolean existedesde = false;
			
			if (fechaIni!=null && !fechaIni.trim().equals("")){
				result = "trunc("+nombreColumna +") >= trunc(TO_DATE('" + fechaIni + "', '" + ClsConstants.DATE_FORMAT_SQL + "')) ";
				existedesde = true;
			}
			
			if (fechaFin!=null && !fechaFin.trim().equals("")) {
				if (existedesde) {
					result+= " AND "; 
				}
				result += "trunc("+nombreColumna +") <= trunc(TO_DATE('" + fechaFin + "', '" + ClsConstants.DATE_FORMAT_SQL + "')) ";
			}
						
		} catch (Exception e) {
	      	ClsExceptions exc=new ClsExceptions(e,"Formato de fecha no reconocido");
	      	exc.setErrorCode("DATEFORMAT");
	        throw exc;			
		}
	
		
		return result;
	}

	public static Vector dateBetweenDesdeAndHastaBind(String nombreColumna, String fechaDesde, String fechaHasta, int contador, Hashtable codigos) throws ClsExceptions{	
		Vector resultV = new Vector ();
	    String result = "";	
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA); 
		
		try {
			String fechaIni = fechaDesde;
			String fechaFin = fechaHasta;
			if (fechaFin!=null && !fechaFin.trim().equals("")) {
				Date d=sdf.parse(fechaFin);
				d.setTime(d.getTime()+86399000);
				fechaFin = (sdf.format(d));
			}
			boolean existedesde = false;
			
			if (fechaIni!=null && !fechaIni.trim().equals("")){
			    contador ++;
		        codigos.put(new Integer(contador),fechaIni);
		        result = "trunc("+nombreColumna +") >= trunc(TO_DATE(:"+contador+", '" + ClsConstants.DATE_FORMAT_SQL + "')) ";
				existedesde = true;
			}
			
			if (fechaFin!=null && !fechaFin.trim().equals("")) {
				if (existedesde) {
					result+= " AND "; 
				}
			    contador ++;
		        codigos.put(new Integer(contador),fechaFin);
				result += "trunc("+nombreColumna +") <= trunc(TO_DATE(:"+contador+", '" + ClsConstants.DATE_FORMAT_SQL + "')) ";
			}
						
		} catch (Exception e) {
	      	ClsExceptions exc=new ClsExceptions(e,"Formato de fecha no reconocido");
	      	exc.setErrorCode("DATEFORMAT");
	        throw exc;			
		}
	
		resultV.add(new Integer(contador));
		resultV.add(result);
		
		return resultV;
	}


	public static String dateMasUnDia(String nombreColumna, String fecha) throws ClsExceptions{	
		String result = "";	
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SQL); 
		
		try {
			Date d=sdf.parse(fecha);
			d.setTime(d.getTime()+86399000);
			String fechaFin = (sdf.format(d));
			
			result = nombreColumna +" <= TO_DATE('" + fechaFin + "', '" + ClsConstants.DATE_FORMAT_SQL + "') ";
						
		} catch (Exception e) {
	      	ClsExceptions exc=new ClsExceptions(e,"Formato de fecha no reconocido");
	      	exc.setErrorCode("DATEFORMAT");
	        throw exc;			
		}
	
		
		return result;
	}

	
    public static int compararFechas(String sFecha1, String sFecha2)
    {
       SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
       try {
       		Date fecha1 = sdf.parse(sFecha1);
	       	Date fecha2 = sdf.parse(sFecha2);
	       	return fecha1.compareTo(fecha2);
       }
       catch (ParseException e) {
       	return -1;
       }
    }
 
    public static Date convertirFecha(String sFecha) throws Exception
    {
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
       Date fecha = null;
       fecha = sdf.parse(sFecha);
       return fecha;
    }
 
    public static Date convertirFechaHora(String sFecha) throws Exception
    {
       SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
       Date fecha = null;
       fecha = sdf.parse(sFecha);
       return fecha;
    }
 
    public static String convertirFecha(Date fecha) throws Exception
    {
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
       String sFecha = null;
       sFecha = sdf.format(fecha);
       return sFecha;
    }
 
    public static String convertirFechaHora(Date fecha) throws Exception
    {
       SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
       String sFecha = null;
       sFecha = sdf.format(fecha);
       return sFecha;
    }
 
    
    public static String quitaHora(String sFecha)
    {
       try {
       		return (sFecha.substring(0,sFecha.indexOf(" "))+ " 00:00:00");
       }
       catch (Exception e) {
       	return sFecha;
       }
    }
    public static String getFormatedDateMedium(String lang, Object odate) throws ClsExceptions
    {
  	  String dat="";
  	  	String date="";
  	    SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
  	    Date datFormat = null;
  	    if (odate!=null) {
  	    	date = (String) odate;	
  		    if (!date.trim().equals("")){
  		      try {
  		      	
  		// DCG      	date = GstDate.getFechaLenguaje(lang, date);
  		        datFormat = sdf.parse(date.trim());
  		      } catch (ParseException ex) {
  		      	ClsExceptions exc=new ClsExceptions(ex,"THIS DATE "+date+" IS BAD FORMATED");
  		      	exc.setErrorCode("DATEFORMAT");
  		        throw exc;	      	
  		      }
  		
  		      if (lang.equalsIgnoreCase("EN")) {
  		      	sdf.applyPattern(ClsConstants.DATE_FORMAT_MEDIUM_ENGLISH);
  		      }
  		      else {
  		      	sdf.applyPattern(ClsConstants.DATE_FORMAT_MEDIUM_SPANISH);
  		      }
  		      dat=sdf.format(datFormat);
  		    }
  	    }
  	    return dat;
    }

  
}