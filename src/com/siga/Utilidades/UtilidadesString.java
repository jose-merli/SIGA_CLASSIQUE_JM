/*
 * Created on 11-nov-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.Utilidades;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.MaskFormatter;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.util.KeyValue;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.AdmLenguajesBean;
import com.siga.general.SIGAException;
import com.siga.tlds.TagSelect;



/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * Modificada el 22/04/2005 por david.sanchez para incluir tratarImporte().
 */
public class UtilidadesString {
	
    static private Hashtable<String, Properties> idiomas = null;
	//static private ResourceBundle bundle = null;
    //static private String lengact="none";
    private static List<String> DIGITOS_COMIENZO_MOVIL = Arrays.asList("6","7");
    private static final String CONSTANTE_TRUE ="TRUE";
    private static final String CONSTANTE_T ="T";
    private static final String CONSTANTE_YES ="YES";
    private static final String CONSTANTE_Y ="Y";
    private static final String CONSTANTE_SI ="SI";
    private static final String CONSTANTE_S ="S";
    private static final String CONSTANTE_SI_TILDE ="S�";
    private static final String CONSTANTE_ON ="ON";
    private static final String CONSTANTE_1 ="1";
    
    
    private static Pattern patternDigitos = Pattern.compile("^[0-9]*$");
	
	static public String parseLike (String cadena) throws ClsExceptions {
		try {
			cadena=cadena.replace('*','%');
			cadena=cadena.replace('?','_');
			cadena=cadena.replaceAll("\'","\'\''");
			return cadena.toUpperCase();
		}
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al transformar la clausula like.");
		}
	}

	static public String getMensajeIdiomaCombo (UsrBean usrbean, String key) 
	{
		if (usrbean!=null) {
			return getMensajeIdioma(usrbean.getLanguage(),key);
		} else {
			return key;
		}
	}

	static public String getMensajeIdioma (UsrBean usrbean, String key) 
	{
		if (usrbean==null) {
		    return getMensajeIdioma("1",key);
		} else {
		    return getMensajeIdioma(usrbean.getLanguage(),key);
		}
	}

	static public String getMensajeIdioma (String idioma, String key)	{
		String idi = idioma;
		// si no hay idioma lo hago en castellano
		if (idi.equals("")) {
		    idi="1";
		}
		// si no estan creados los cargo
		if (idiomas==null) {
		    recargarFicherosIdiomasEnCaliente(); 
		}
		Properties prop = (Properties)idiomas.get(idi); 
		if (prop == null)	{
			// modificado por raul.ggonzalez 15-12-2004
			// para que si no encuantra la clave o el bundle 
			// devuelva el literal tal cual
			// return "";
			return key;
		}
		String aux = prop.getProperty(key,key);
		if (aux.equals(key)) {
		    return aux;
		} else {
		    return caracteresJavascript(aux);
		}
	}
	
	/*
	static public String getMensajeIdiomaExt (String idioma, String key) {
	    UsrBean usr = new UsrBean(new Integer(ClsConstants.USUMODIFICACION_AUTOMATICO).toString(),idioma);
		AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
		Vector v;
        try {
            v = a.select("WHERE UPPER("+AdmLenguajesBean.C_CODIGOEXT+")='"+idioma.toUpperCase()+"'");
            if (v!=null && v.size()==1) {
    		    AdmLenguajesBean len = (AdmLenguajesBean) v.get(0);
    		    return getMensajeIdioma(len.getIdLenguaje(), key);
    		}
        } catch (ClsExceptions e) {
        }
        return key;
	}
	*/
	
	/**
	 * Funcion que obtiene el mensaje despues de reemplazar los argumentos que recibe
	 * @param idioma
	 * @param msg
	 * @param datos
	 * 
	 * Ejemplo Entrada: "El campo {0} es incorrecto" - "Telefono"
	 *         Salida:  "El campo Telefono es incorrecto"
	 * @return
	 */
	public static String getMensajeIdioma(UsrBean us, String msg, String[] datos)
	{
	    return getMensajeIdioma(us.getLanguage(),msg, datos);
	}

    public static String getMensajeIdioma(String idioma, String msg, String[] datos)
	{
		String mensaje = UtilidadesString.getMensajeIdioma(idioma, msg);
		for (int i = 0; i < datos.length; i++){
		    String ele = "{" + i + "}";
		    int pos = mensaje.indexOf(ele);
		    if (pos < 0) continue;
		    mensaje = mensaje.substring(0, pos) + UtilidadesString.getMensajeIdioma(idioma, datos[i]) + mensaje.substring(pos + ele.length());
		    i--;
		}
		return mensaje;		
	}

	public static void recargarFicherosIdiomasEnCaliente()
	{
		if (idiomas==null) {
		    idiomas = new Hashtable<String, Properties>();
		} 
		else {
		    idiomas.clear();
		}
		
		UsrBean usu = new UsrBean();
		usu.setLanguage("1");
		AdmLenguajesAdm a = new AdmLenguajesAdm(usu);
		Vector<AdmLenguajesBean> v;
		FileInputStream fis = null;
        try {
            v = a.select();
            for (int i=0;v!=null && i<v.size();i++) {
    			AdmLenguajesBean b = (AdmLenguajesBean) v.get(i);
    			Properties aux = new Properties();
    			try {
    				StringBuilder strBld = new StringBuilder();
    				strBld.append(SIGAReferences.getDirectoryReference(SIGAReferences.RESOURCE_FILES.RECURSOS_DIR))
    						.append("/ApplicationResources_")
    						.append(b.getCodigoExt().toLowerCase())
    						.append(".properties");
    			    fis = new FileInputStream(strBld.toString());
                    aux.load(fis);
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                UtilidadesString.idiomas.put(b.getIdLenguaje(),aux);	
    		}			
        } catch (ClsExceptions e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception eee) {}
        }
        ClsLogging.writeFileLog("Ficheros de idiomas recargados en caliente.",8);
	}
	
	static public boolean stringToBoolean (String dato) {
		if (dato != null) {
			dato = dato.toLowerCase();
			if (dato.equalsIgnoreCase(CONSTANTE_TRUE) || dato.equalsIgnoreCase(CONSTANTE_T) ||  
				dato.equalsIgnoreCase(CONSTANTE_YES)  || dato.equalsIgnoreCase(CONSTANTE_Y) ||
				dato.equalsIgnoreCase(CONSTANTE_SI)   || dato.equalsIgnoreCase(CONSTANTE_S) ||
				dato.equalsIgnoreCase(CONSTANTE_SI_TILDE)   || dato.equalsIgnoreCase(CONSTANTE_1) ||
				dato.equalsIgnoreCase(CONSTANTE_ON)) {
				return true;
			}
		}
		return false;
	}
	
	static public String[] split(String cadena, String comodin){

		String[] sCadena = null;
		if ((cadena == null) || cadena.equals(""))
			return sCadena;
		
		StringTokenizer tokens = new StringTokenizer(cadena, comodin);
		int nCadena = tokens.countTokens();
	    sCadena = new String[nCadena];
	    int i=0;
	    while(tokens.hasMoreTokens()){
	       sCadena[i]=tokens.nextToken().trim().toUpperCase();	      
	       i++;
	    }
		return sCadena;
	}

	/**
	 * Funcion que devuelve un array de cadenas, partiendo de una cadena de caracteres que se separara
	 * por la cadena especificada, el array de cadenas de caracteres se devolveran seg�n se envien, es decir
	 * sin pasarlas a may�sculas o a min�sculas
	 * @param cadena. Cadena de Caracteres completa
	 * @param comodin. Separador de la cadena en diferentes cadenas
	 * 
	 * Ejemplo Entrada: "1#El Campo#Obtenido"
	 *         Salida:  1
	 *                  El Campo
	 *                  Obtenido
	 * @return Array de cadenas
	 */	
	
	static public String[] splitIgual(String cadena, String comodin){

		String[] sCadena = null;
		if ((cadena == null) || cadena.equals(""))
			return sCadena;
		
		StringTokenizer tokens = new StringTokenizer(cadena, comodin);
		int nCadena = tokens.countTokens();
	    sCadena = new String[nCadena];
	    int i=0;
	    while(tokens.hasMoreTokens()){
	       sCadena[i]=tokens.nextToken().trim();	      
	       i++;
	    }
		return sCadena;
	}	
	
	static public String[] splitNormal(String cadena, String comodin){

		String[] sCadena = null;
		if ((cadena == null) || cadena.equals(""))
			return sCadena;
		
		StringTokenizer tokens = new StringTokenizer(cadena, comodin);
		int nCadena = tokens.countTokens();
	    sCadena = new String[nCadena];
	    int i=0;
	    while(tokens.hasMoreTokens()){
	       sCadena[i]=tokens.nextToken();	      
	       i++;
	    }
		return sCadena;
	}

	/**
	  *  Da formato HTML a un texto
	  *  @param <B>sCadena</B> La cadena a transformar
	  *  @return La nueva cadena con formato HTML
	  */

	  public static String entidades(String sCadena)
	  {
	    String sSalida="";

	    if (sCadena==null) return sSalida;

	    sSalida = sCadena.replaceAll("&", "&amp;");
	    sSalida = sSalida.replaceAll("�", "&aacute;");
	    sSalida = sSalida.replaceAll("�", "&Aacute;");
	    sSalida = sSalida.replaceAll("�", "&eacute;");
	    sSalida = sSalida.replaceAll("�", "&Eacute;");
	    sSalida = sSalida.replaceAll("�", "&iacute;");
	    sSalida = sSalida.replaceAll("�", "&Iacute;");
	    sSalida = sSalida.replaceAll("�", "&oacute;");
	    sSalida = sSalida.replaceAll("�", "&Oacute;");
	    sSalida = sSalida.replaceAll("�", "&uacute;");
	    sSalida = sSalida.replaceAll("�", "&Uacute;");
	    sSalida = sSalida.replaceAll("�", "&ntilde;");
	    sSalida = sSalida.replaceAll("�", "&Ntilde;");
	    sSalida = sSalida.replaceAll("<", "&lt;");
	    sSalida = sSalida.replaceAll(">", "&gt;");
	    sSalida = sSalida.replaceAll("\"", "&quot;");
	    sSalida = sSalida.replaceAll("�", "&iexcl;");
	    sSalida = sSalida.replaceAll("�", "&acute;");
	    sSalida = sSalida.replaceAll("`", "&acute;");
	    sSalida = sSalida.replaceAll("�", "&iquest;");
	    sSalida = sSalida.replaceAll("�", "&uml;");
	    sSalida = sSalida.replaceAll("�", "&Agrave;");
	    sSalida = sSalida.replaceAll("�", "&agrave;");
	    sSalida = sSalida.replaceAll("�", "&Acirc;");
	    sSalida = sSalida.replaceAll("�", "&acirc;");
	    sSalida = sSalida.replaceAll("�", "&Auml;");
	    sSalida = sSalida.replaceAll("�", "&auml;");
	    sSalida = sSalida.replaceAll("�", "&Egrave;");
	    sSalida = sSalida.replaceAll("�", "&Ecirc;");
	    sSalida = sSalida.replaceAll("�", "&Euml;");
	    sSalida = sSalida.replaceAll("�", "&egrave;");
	    sSalida = sSalida.replaceAll("�", "&ecirc;");
	    sSalida = sSalida.replaceAll("�", "&euml;");
	    sSalida = sSalida.replaceAll("�", "&Igrave;");
	    sSalida = sSalida.replaceAll("�", "&Icirc;");
	    sSalida = sSalida.replaceAll("�", "&Iuml;");
	    sSalida = sSalida.replaceAll("�", "&igrave;");
	    sSalida = sSalida.replaceAll("�", "&icirc;");
	    sSalida = sSalida.replaceAll("�", "&iuml;");
	    sSalida = sSalida.replaceAll("�", "&Ograve;");
	    sSalida = sSalida.replaceAll("�", "&Ocirc;");
	    sSalida = sSalida.replaceAll("�", "&Ouml;");
	    sSalida = sSalida.replaceAll("�", "&ograve;");
	    sSalida = sSalida.replaceAll("�", "&ocirc;");
	    sSalida = sSalida.replaceAll("�", "&ouml;");
	    sSalida = sSalida.replaceAll("�", "&Ugrave;");
	    sSalida = sSalida.replaceAll("�", "&Ucirc;");
	    sSalida = sSalida.replaceAll("�", "&Uuml;");
	    sSalida = sSalida.replaceAll("�", "&ugrave;");
	    sSalida = sSalida.replaceAll("�", "&ucirc;");
	    sSalida = sSalida.replaceAll("�", "&uuml;");
	    sSalida = sSalida.replaceAll("�", "&yacute;");
	    sSalida = sSalida.replaceAll("�", "&Yacute;");
	    sSalida = sSalida.replaceAll("�", "&yuml;");
	    sSalida = sSalida.replaceAll("�", "&ccedil;");
	    sSalida = sSalida.replaceAll("�", "&Ccedil;");
	    //sSalida = sSalida.replaceAll("'", "&apos;");
	    sSalida = sSalida.replaceAll("\r\n", "<BR>");
	    sSalida = sSalida.replaceAll("\n\r", "<BR>");
	    sSalida = sSalida.replaceAll("\n", "<BR>");
	    return sSalida;
	  }

	  public static String sustituirParaExcell(String sCadena)
	  {
	    String sSalida="";

	    if (sCadena==null) return sSalida;
	    sSalida = sCadena.replaceAll("\t", " ");
	    sSalida = sSalida.replaceAll("\n\r", " ");
	    sSalida = sSalida.replaceAll("\n", " ");
	    sSalida = sSalida.replaceAll("\r", " ");
	    //sSalida = sSalida.replaceAll("�", "\u20ac");
	    return sSalida;
	  }

	  /**
	   * Sustituye comillas simples por apostrofo para javascript. El problema catal�n.
	   * @param sCadena a traducir
	   * @return
	   */
	  
	  private static String caracteresJavascript(String sCadena)
	  {
	    if (sCadena==null) return "";

	    sCadena = sCadena.replaceAll("'", "�");
	    sCadena = sCadena.replaceAll("\"", "�");

	    return sCadena;
	  }
	  
	  
	  /**
		  *  Da formato ISO-8859-1 a un texto
		  *  @param <B>sCadena</B> La cadena a transformar
		  *  @return La nueva cadena con formato ISO-8859-1
		  */

		  public static String old_formato_ISO_8859_1(String sCadena)
		  {
		    String sSalida=sCadena;

		    if (sCadena==null) return sSalida;

		    sSalida = sSalida.replaceAll("&", "&amp;");
		    
		    sSalida = sSalida.replaceAll("�", "&#225;");
		    sSalida = sSalida.replaceAll("�", "&#193;");
		    sSalida = sSalida.replaceAll("�", "&#233;");
		    sSalida = sSalida.replaceAll("�", "&#201;");
		    sSalida = sSalida.replaceAll("�", "&#237;");
		    sSalida = sSalida.replaceAll("�", "&#205;");
		    sSalida = sSalida.replaceAll("�", "&#243;");
		    sSalida = sSalida.replaceAll("�", "&#211;");
		    sSalida = sSalida.replaceAll("�", "&#250;");
		    sSalida = sSalida.replaceAll("�", "&#218;");
		    sSalida = sSalida.replaceAll("nh", "&#241;");
		    sSalida = sSalida.replaceAll("�", "&#209;");
		    sSalida = sSalida.replaceAll("�", "&#161;");
		    sSalida = sSalida.replaceAll("�", "&#180;");
		    sSalida = sSalida.replaceAll("�", "&#191;");
		    sSalida = sSalida.replaceAll("�", "&#168;");
		    sSalida = sSalida.replaceAll("�", "&#192;");
		    sSalida = sSalida.replaceAll("�", "&#224;");
		    sSalida = sSalida.replaceAll("�", "&#194;");
		    sSalida = sSalida.replaceAll("�", "&#226;");
		    sSalida = sSalida.replaceAll("�", "&#196;");
		    sSalida = sSalida.replaceAll("�", "&#228;");
		    sSalida = sSalida.replaceAll("�", "&#200;");
		    sSalida = sSalida.replaceAll("�", "&#202;");
		    sSalida = sSalida.replaceAll("�", "&#203;");
		    sSalida = sSalida.replaceAll("�", "&#232;");
		    sSalida = sSalida.replaceAll("�", "&#234;");
		    sSalida = sSalida.replaceAll("�", "&#235;");
		    sSalida = sSalida.replaceAll("�", "&#204;");
		    sSalida = sSalida.replaceAll("�", "&#206;");
		    sSalida = sSalida.replaceAll("�", "&#207;");
		    sSalida = sSalida.replaceAll("�", "&#236;");
		    sSalida = sSalida.replaceAll("�", "&#238;");
		    sSalida = sSalida.replaceAll("�", "&#239;");		    
		    sSalida = sSalida.replaceAll("�", "&#210;");
		    sSalida = sSalida.replaceAll("�", "&#212;");
		    sSalida = sSalida.replaceAll("�", "&#214;");
		    sSalida = sSalida.replaceAll("�", "&#242;");
		    sSalida = sSalida.replaceAll("�", "&#244;");
		    sSalida = sSalida.replaceAll("�", "&#246;");
		    sSalida = sSalida.replaceAll("�", "&#217;");
		    sSalida = sSalida.replaceAll("�", "&#219;");
		    sSalida = sSalida.replaceAll("�", "&#220;");
		    sSalida = sSalida.replaceAll("�", "&#249;");
		    sSalida = sSalida.replaceAll("�", "&#251;");
		    sSalida = sSalida.replaceAll("�", "&#252;");
		    sSalida = sSalida.replaceAll("�", "&#253;");
		    sSalida = sSalida.replaceAll("�", "&#221;");
		    sSalida = sSalida.replaceAll("�", "&#255;");
		    sSalida = sSalida.replaceAll("�", "&#231;");
		    sSalida = sSalida.replaceAll("�", "&#199;");
		    sSalida = sSalida.replaceAll("�", "&#8364;");
		    sSalida = sSalida.replaceAll("�", "&#186;");
		    sSalida = sSalida.replaceAll("<", "&#60;");
		    sSalida = sSalida.replaceAll(">", "&#62;");

		    return sSalida;
		  }
	  
		  public static String formato_ISO_8859_1(String s) {
		    StringBuffer buf = new StringBuffer("");
		    char[] mChars = s.toCharArray();
		    for (int i=0;i<mChars.length;i++){
		        int code = mChars[i];
		        if(code>47 && code<58){//digito
		        	buf.append(mChars[i]);
		        }else if(code>64 && code<91){//mayuscula
		        	buf.append(mChars[i]);
		        }else if(code>96 && code<123){//minuscula
		        	buf.append(mChars[i]);
		        }else if(code==128){//excepcion del euro
		        	//LMS 08/08/2006
		        	//En lugar de utilizar el formato decimal, se usa el hexadecimal para definir el s�mbolo del euro.
		        	//buf.append("&#x20AC;");
		        	buf.append("&#8364;");
		        }else {//resto de caracteres
		        	if(code!=0)
		        		buf.append("&#x"+Integer.toHexString(code)+";");
		        }
		    }
		    return buf.toString();
		  }
  
	
	  /**
		  *  Da formato ISO-8859-1 a un texto
		  *  @param <B>sCadena</B> La cadena a transformar
		  *  @return La nueva cadena con formato ISO-8859-1
		  */

		  public static String old_formato_ISO_8859_1_SinAmpersand(String sCadena)
		  {
		    String sSalida=sCadena;

		    if (sCadena==null) return sSalida;

		    sSalida = sSalida.replaceAll("�", "&#225;");
		    sSalida = sSalida.replaceAll("�", "&#193;");
		    sSalida = sSalida.replaceAll("�", "&#233;");
		    sSalida = sSalida.replaceAll("�", "&#201;");
		    sSalida = sSalida.replaceAll("�", "&#237;");
		    sSalida = sSalida.replaceAll("�", "&#205;");
		    sSalida = sSalida.replaceAll("�", "&#243;");
		    sSalida = sSalida.replaceAll("�", "&#211;");
		    sSalida = sSalida.replaceAll("�", "&#250;");
		    sSalida = sSalida.replaceAll("�", "&#218;");
		    sSalida = sSalida.replaceAll("nh", "&#241;");
		    sSalida = sSalida.replaceAll("�", "&#209;");
		    sSalida = sSalida.replaceAll("�", "&#161;");
		    sSalida = sSalida.replaceAll("�", "&#180;");
		    sSalida = sSalida.replaceAll("�", "&#191;");
		    sSalida = sSalida.replaceAll("�", "&#168;");
		    sSalida = sSalida.replaceAll("�", "&#192;");
		    sSalida = sSalida.replaceAll("�", "&#224;");
		    sSalida = sSalida.replaceAll("�", "&#194;");
		    sSalida = sSalida.replaceAll("�", "&#226;");
		    sSalida = sSalida.replaceAll("�", "&#196;");
		    sSalida = sSalida.replaceAll("�", "&#228;");
		    sSalida = sSalida.replaceAll("�", "&#200;");
		    sSalida = sSalida.replaceAll("�", "&#202;");
		    sSalida = sSalida.replaceAll("�", "&#203;");
		    sSalida = sSalida.replaceAll("�", "&#232;");
		    sSalida = sSalida.replaceAll("�", "&#234;");
		    sSalida = sSalida.replaceAll("�", "&#235;");
		    sSalida = sSalida.replaceAll("�", "&#204;");
		    sSalida = sSalida.replaceAll("�", "&#206;");
		    sSalida = sSalida.replaceAll("�", "&#207;");
		    sSalida = sSalida.replaceAll("�", "&#236;");
		    sSalida = sSalida.replaceAll("�", "&#238;");
		    sSalida = sSalida.replaceAll("�", "&#239;");		    
		    sSalida = sSalida.replaceAll("�", "&#210;");
		    sSalida = sSalida.replaceAll("�", "&#212;");
		    sSalida = sSalida.replaceAll("�", "&#214;");
		    sSalida = sSalida.replaceAll("�", "&#242;");
		    sSalida = sSalida.replaceAll("�", "&#244;");
		    sSalida = sSalida.replaceAll("�", "&#246;");
		    sSalida = sSalida.replaceAll("�", "&#217;");
		    sSalida = sSalida.replaceAll("�", "&#219;");
		    sSalida = sSalida.replaceAll("�", "&#220;");
		    sSalida = sSalida.replaceAll("�", "&#249;");
		    sSalida = sSalida.replaceAll("�", "&#251;");
		    sSalida = sSalida.replaceAll("�", "&#252;");
		    sSalida = sSalida.replaceAll("�", "&#253;");
		    sSalida = sSalida.replaceAll("�", "&#221;");
		    sSalida = sSalida.replaceAll("�", "&#255;");
		    sSalida = sSalida.replaceAll("�", "&#231;");
		    sSalida = sSalida.replaceAll("�", "&#199;");
		    sSalida = sSalida.replaceAll("�", "&#8364;");
		    sSalida = sSalida.replaceAll("�", "&#186;");
		    sSalida = sSalida.replaceAll("<", "&#60;");
		    sSalida = sSalida.replaceAll(">", "&#62;");

		    return sSalida;
		  }
	  

		/**
		  *  Da formato HTML a un texto con las entidades m�nimas
		  *  @param <B>sCadena</B> La cadena a transformar
		  *  @return La nueva cadena con formato HTML
		  */
	  public static String entidadesMinimas(String sCadena)
	  {
	    String sSalida="";

	    if (sCadena==null) return sSalida;

	    sSalida = sCadena.replaceAll("&", "&amp;");
	    sSalida = sSalida.replaceAll("<", "&lt;");
	    sSalida = sSalida.replaceAll(">", "&gt;");
	    sSalida = sSalida.replaceAll("\"", "&quot;");
	    //sSalida = sSalida.replaceAll("'", "&apos;");

	    return sSalida;
	  }
		/**
		  *  Controla los nulos y los caracteres HTML a mostrar
		  *  @param <B>sCadena</B> La cadena a transformar
		  *  @return La nueva cadena con formato HTML
		  */
		static public String mostrarDatoJSP (Object o) {
			String a = "";
			if (o!=null) {
				a = o.toString();
			}
			if (a.trim().equals(""))
				return "&nbsp";
			else 
				return entidades(a);
		}
		/**
		 * corta la cadena a la longitud deseada y le concatena (...)
		 * @param o
		 * @param longitud
		 * @return
		 */
		static public String mostrarDatoJSP (Object o,int longitud) {
			String a = "";
			if (o!=null) {
				
				a = o.toString();
				if(a.length()>longitud){
					a = a.substring(0,longitud);
					a += " (...)";
				}
				
			}
			return mostrarDatoJSP(a);
			
		}

		/**
		  *  Solamente controla los nulos y devuelve blanco en tal caso 
		  *  @param <B>sCadena</B> La cadena a transformar
		  *  @return La nueva cadena con formato HTML
		  */
		static public String controlNulos (Object o) {
			String a = " ";
			if (o!=null) {
				a = o.toString();
			}
			return a;
		}

		/**
		  *  Cambia comillas dobles por su modificador 
		  *  @param <B>sCadena</B> La cadena a transformar
		  *  @return La nueva cadena con formato HTML
		  */
		static public String cambiarDoblesComillas (Object o) {
			String a = " ";
			if (o!=null) {
				a = (String) o;
				a = a.replaceAll("\"","\\\\\"");
				// a = a.replaceAll("\"","\\\\%22");
			}
			return a;
		}
		
		/**
		  *  Cambia la coma por almohadilla 
		  *  @param <B>sCadena</B> La cadena a transformar
		  *  @return La nueva cadena
		  */
		static public String comaToAnd (String S) {
			
			if (S!=null) {
				S = S.replaceAll(",","&");				
			}
			return S;
		}
		
		/**
		  *  Cambia la almohadilla por coma
		  *  @param <B>sCadena</B> La cadena a transformar
		  *  @return La nueva cadena
		  */
		static public String andToComa (String S) {
			
			if (S!=null) {
				S = S.replaceAll("&",",");				
			}
			return S;
		}
		
		/**
		  *  Permite crear mensajes personalizados 
		  *  @param <B>msg</B> nombre del recurso que va a ser personalizado
		  *  @param <B>datos</B> array que contiene los par�metros que van a ser sustituidos en el mensaje
		  *  @param <B>idioma</B> idioma del usuario.
		  *  @return el nuevo mensaje 
		  */
		public static String getMensaje(String msg, String[] datos, String idioma){
			String mensaje = UtilidadesString.getMensajeIdioma(idioma, msg);
			String cadInicial;
			String cadFinal;
			int pos;
			
			for(int i = 0; i < datos.length; i++){
				pos = mensaje.indexOf("{");
				if(pos != -1){
					cadInicial = mensaje.substring(0,pos);
					pos = mensaje.indexOf("}");
					if(pos != -1){
						cadFinal = mensaje.substring(pos+1,mensaje.length());
						mensaje = cadInicial.concat(datos[i].concat(cadFinal));					
					}				
				}				
			}
			return mensaje;		
		}

		public static String quitarEspaciosAcentos(String s) throws ClsExceptions {
			return quitarAcentos(replaceAllIgnoreCase(s, " ", "").toUpperCase()); 
		}
		
		/**
		  *  formatea un dato a una longitud rellenando por la izquierda a ceros 
		  *  o por la derecha a blancos en funcion de si es numerico
		  *  @param <B>datoOrig</B> dato a formatear
		  *  @param <B>longitud</B> longitud final del formateo
		  *  @param <B>numerico</B> Si es numerico pone ceros a la izquierda y si no blancos a la derecha
		  *  @return La cadena formateada 
		  */
		public static String formatea (Object datoOrig, int longitud, boolean numerico) throws ClsExceptions {
			String salida= "";
			if (datoOrig==null) {
				if (numerico) {
					salida = relleno("0",longitud);  
				} else {
					salida = relleno(" ",longitud);
				}
			} else {
				String dato = datoOrig.toString();
				if (dato.length()==0) {
					if (numerico) {
						salida = relleno("0",longitud);  
					} else {
						salida = relleno(" ",longitud);
					}
				} else
				if (dato.length()>longitud) {
					// mayor
					if (numerico) {
						salida = dato.substring(dato.length() - longitud, dato.length());  
					} else {
						salida = dato.substring(0, longitud);
					}
				} else
				if (dato.length()<longitud) {
					// menor
					if (numerico) {
						salida = relleno("0",longitud - dato.length()) + dato;  
					} else {
						salida = dato + relleno(" ",longitud - dato.length());
					}
				} else {
					// es igual
					salida = dato;
				}
			}	
			
			return salida;	
		}

		/**
		  *  Crea un string de longitud x relleno del caracter indicado 
		  *  @param <B>caracter</B> caracter a iterar
		  *  @param <B>longitus</B> longitud final de la cadena
		  *  @return La cadena rellena 
		  */
		public static String relleno (String caracter, int longitud) throws ClsExceptions {
			String salida= "";
			
			for (int i=0;i<longitud;i++) {
				salida += caracter;
			}
			
			return salida;	
		}

		/**
		 * Devuelve un String con el importe sin decimales ni punto si su parte decimal es 0.
		 * En caso de no ser 0 devuelve el String correspondiente al Double.
		 * @param importe
		 * @return
		 */
		public static String tratarImporte(Double importe){
			String salida=null, parteEntera=null, parteDecimal;
			
		    salida = importe.toString();
			parteEntera = salida.substring(0,salida.indexOf('.'));
			parteDecimal = salida.substring(salida.indexOf('.')+1);

			//Si su parte decimal es 0 devuelvo la parte entera, sino lo devuelvo tal cual:
			if (Double.valueOf(parteDecimal).equals(Double.valueOf("0")))
				salida = parteEntera;
			
			return salida;
		}
		
		/**
		 * Devuelve un String con el importe empleando '.' como separadors de miles y ',' para decimales
		 * @param importe - importe entrada
		 * @return String - resultado de la operacion
		 */
		public static String formatoImporte(String importe){
			
			String parteEntera="";
			String parteDecimal="";
			String resultado="";
			String signo="";
			
			if (!importe.equalsIgnoreCase("")){
				// Tenemos que controlar el signo para evitar que pinte -.123,01
				if(importe.startsWith("-")){
					signo="-";
					importe= importe.substring(1);
				}
				if (importe.indexOf('.')!=-1){
					parteEntera = importe.substring(0,importe.indexOf('.'));
					if (parteEntera.equals(""))
						parteEntera = "0";
					
					parteDecimal = importe.substring(importe.indexOf('.')+1);
					if(parteDecimal.length()==1)
						parteDecimal+="0";
				}
				else{
					parteEntera=importe;
					parteDecimal="00";
				}

				int i=0;

				while (i<parteEntera.length()){
					if (((parteEntera.length()-(i+1))%3==0)&&(parteEntera.length()-(i+1)!=0)){
						resultado+=parteEntera.charAt(i)+".";
					}
					else{
						resultado+=parteEntera.charAt(i);
					}
					i++;
				}				
				resultado+=","+parteDecimal;				
			}

			resultado=signo+resultado;
			
			return resultado;
		}
		
		/**
		 * Devuelve un String con el importe empleando '.' como separadors de miles y ',' para decimales
		 * En este caso siempre se rellenan los dos decimales
		 * @param importe - importe entrada
		 * @return String - resultado de la operacion
		 */
		public static String formatoImporte(double importe){
			DecimalFormat df= new DecimalFormat("###,###,###,###,###,###,###,##0.00");
			String result=df.format(importe);
			return result;
		}
		
		/**
		  *  Metodo reemplazaParametros
		  *  Reemplaza las propiedades, delimitadas por dos marcas, por sus respectivos valores en un texto
		  * @param <B>texto</B> El texto a inspeccionar
		  * @param <B>marca</B> Marca delimitadora
		  * @param <B>properties</B> Conjunto de propiedades con sus valores
		  * @return String con los valores reemplazados
		  */
		public static String reemplazaParametros(String texto, String marca, Hashtable<String,String> properties){
			
			StringBuffer finalText = new StringBuffer();
			int dif=marca.length();
			String prop;
			int pos1 = 0;
			int pos2 = 0;
			int index = 0;
			if(texto==null) texto="";
			//while parameters in the string
			while ((pos1 = texto.indexOf(marca,index)) != -1) {
				// search the parameter betwen the control characters
				// and replace by its value
				pos2 = texto.indexOf(marca,pos1+dif);
				prop = texto.substring(pos1+dif,pos2);
				
				finalText.append(texto.substring(index,pos1));
				String propValue =(String)properties.get(prop.toUpperCase());
				if (propValue != null) {
					propValue=UtilidadesString.formato_ISO_8859_1(propValue);
					finalText.append(propValue);
				}
				// searching on from the last control character
				index = pos2+dif;
			}
			
			finalText.append(texto.substring(index,texto.length()));
			return finalText.toString();
		}
		
		/**
		 *  Metodo reemplazaEntreMarcasCon
		 *  Reemplaza por un texto nuevo un literal entre dos marcas incluyendo las marcas
		 * @param <B>texto</B> El texto a inspeccionar
		 * @param <B>marcaInicial</B> Marca inicial
		 * @param <B>marcaFinal</B> Marca final
		 * @param <B>insTexto</B> Texto que se inserta
		 * @return String con la subcaneda reemplazada
		 */
		public static String reemplazaEntreMarcasCon(String texto, String marcaInicial, String marcaFinal, String insTexto){
			String retorno=null;
			int inicio=texto.indexOf(marcaInicial);
			int fin=texto.indexOf(marcaFinal);
			
			if(inicio!=-1 && fin!=-1){
				retorno=texto.substring(0,inicio)+insTexto+texto.substring(fin+marcaFinal.length());
			}else{
				retorno=texto;
			}
			return retorno;
		} 
		
		
		/**
		 *  Metodo encuentraEntreMarcas
		 *  Obtiene un literal entre dos marcas sin incluirlas
		 * @param <B>texto</B> El texto a inspeccionar
		 * @param <B>marcaInicial</B> Marca inicial
		 * @param <B>marcaFinal</B> Marca final
		 * @return String con la subcaneda obtenida
		 */
		public static String encuentraEntreMarcas(String texto, String marcaInicial, String marcaFinal){
			int inicio=texto.indexOf(marcaInicial);
			int fin=texto.indexOf(marcaFinal);
			String retorno=null;
			if(inicio!=-1 && fin!=-1){
				retorno=texto.substring(inicio+marcaInicial.length(),fin);
			}
			return retorno;
		} 
		
		/**
		 * Obtiene el contenido de un fichero
		 * @param file Objeto fichero
		 * @return contenido del fichero
		 * @throws ClsExceptions
		 */
		public static String getFileContent(File file)throws ClsExceptions{
			String content =null;
		
			if (!file.exists()){
				throw new ClsExceptions("El fichero "+file.getName()+" no existe");
			}
			InputStream is= null;
			try {
				is=new FileInputStream(file);
				int nBytes = is.available();
				byte buffer[] = new byte[nBytes];
				is.read(buffer, 0, nBytes);
				content = new String(buffer);
				is.close();
				return content;
			} catch (IOException e) {
			    try {
			        is.close();
			    } catch (Exception eee) {}
				throw new ClsExceptions(e,"facturacion.nuevoFichero.literal.errorLectura");			    
			}
		}
		
		public static String getFileContent(File file,String charsetName)throws ClsExceptions{
			String content =null;
		
			if (!file.exists()){
				throw new ClsExceptions("El fichero "+file.getName()+" no existe");
			}
			InputStream is= null;
			try {
				is=new FileInputStream(file);
				int nBytes = is.available();
				byte buffer[] = new byte[nBytes];
				is.read(buffer, 0, nBytes);
				content = new String(buffer,charsetName);
				is.close();
				return content;
			} catch (IOException e) {
			    try {
			        is.close();
			    } catch (Exception eee) {}
				throw new ClsExceptions(e,"facturacion.nuevoFichero.literal.errorLectura");			    
			}
		}
		
		/**
		 * Establece el contenido de un fichero
		 * @param file Objeto fichero
		 * @param content contenido del fichero
		 * @throws SIGAException
		 */	
		public static void setFileContent(File file, String content) throws SIGAException{
			try {
				StringReader is= new StringReader(content);
				OutputStream os = new FileOutputStream(file);
				int car;
				while ((car=is.read())!=-1){
					os.write(car);
				}
//				int nBytes = is.available();
//				byte buffer[] = new byte[nBytes];
//				is.read(buffer, 0, nBytes);
//				os.write(buffer, 0, nBytes);
				os.flush();
				os.close();
				is.close();
				
			}catch (IOException e) {
				throw new SIGAException("facturacion.nuevoFichero.literal.errorInsercion",e);			    
			}
		}
		
		// formatea un dato a una longitud rellenando por la izquierda a ceros 
		// o por la derecha a blancos en funcion de si es numerico para el impreso 190
		// Ademas pone todo en mayusculas y quita los acentos
		public static String formateaFicheros (Object datoOrig, int longitud, boolean numerico) throws ClsExceptions {
			String salida= "";
			if (datoOrig==null) {
				if (numerico) {
					salida = relleno("0",longitud);  
				} else {
					salida = relleno(" ",longitud);
				}
			} else {
				String dato = datoOrig.toString();
				if (dato.length()==0) {
					if (numerico) {
						salida = relleno("0",longitud);  
					} else {
						salida = relleno(" ",longitud);
					}
				} else
				if (dato.length()>longitud) {
					// mayor
					if (numerico) {
						salida = dato.substring(dato.length() - longitud, dato.length());  
					} else {
						salida = dato.substring(0, longitud);
					}
				} else
				if (dato.length()<longitud) {
					// menor
					if (numerico) {
						salida = relleno("0",longitud - dato.length()) + dato;  
					} else {
						salida = dato + relleno(" ",longitud - dato.length());
					}
				} else {
					// es igual
					salida = dato;
				}
			}	
			
			salida = salida.toUpperCase();
			salida = quitarAcentos(salida);
			return salida;	
		}

		// quita los acentos
		public static String quitarAcentos (String cadena) throws ClsExceptions {
			
			cadena = cadena.replaceAll("�","A");
			cadena = cadena.replaceAll("�","E");
			cadena = cadena.replaceAll("�","I");
			cadena = cadena.replaceAll("�","O");
			cadena = cadena.replaceAll("�","U");
			
			cadena = cadena.replaceAll("�","A");
			cadena = cadena.replaceAll("�","E");
			cadena = cadena.replaceAll("�","I");
			cadena = cadena.replaceAll("�","O");
			cadena = cadena.replaceAll("�","U");
			
			cadena = cadena.replaceAll("�","A");
			cadena = cadena.replaceAll("�","E");
			cadena = cadena.replaceAll("�","I");
			cadena = cadena.replaceAll("�","O");
			cadena = cadena.replaceAll("�","U");
			
			cadena = cadena.replaceAll("�","A");
			cadena = cadena.replaceAll("�","E");
			cadena = cadena.replaceAll("�","I");
			cadena = cadena.replaceAll("�","O");
			cadena = cadena.replaceAll("�","U");
			
			return cadena;	
		}

		public static String formatoFecha(String fechain, String formatin, String formatout) throws Exception {
			String fechaout ="";
			if(fechain!=null && !fechain.equalsIgnoreCase("")){
				SimpleDateFormat sdf = new SimpleDateFormat(formatin);
				Date fe = sdf.parse(fechain);
				sdf = new SimpleDateFormat(formatout);
				fechaout = sdf.format(fe);
			}
			return fechaout;
		}
		
		public static String formatoFecha(Date fecha, String formatout) throws ClsExceptions {
			String fechaout ="";
			
			SimpleDateFormat sdf = new SimpleDateFormat(formatout);
			fechaout = sdf.format(fecha);
			
			return fechaout;
		}
		
		public static String formatoFecha(Date fecha, String formatout, String defaultValue)  {
			try{
				return formatoFecha(fecha,formatout);
			}
			catch(Exception e){
				return null;
			}
		}
				
	/**
	 * Devuelve una fecha escrita en el idioma indicado
	 * @param fecha La fecha que queremos recibir escrita
	 * @param formatoIn El formato de la fecha de entrada (dd/mm/yyyy)
	 * @param lang El identificador del idioma en el que devuelve la fecha
	 * @return la fecha escrita
	 * @throws ClsExceptions
	 */ 
	static public String getFechaEscrita(String fecha, String formatoIn, String lang) throws ClsExceptions {
		String sFecha= fecha;
		try {
			Locale loc= new Locale(lang);
			SimpleDateFormat sdf1 = new SimpleDateFormat(formatoIn); 
			SimpleDateFormat sdf2 = new SimpleDateFormat("d 'de' MMMM 'de' yyyy",loc); 
			Date date = sdf1.parse(sFecha);
			sFecha = sdf2.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sFecha;
	}
	
	/** 
	 * Devuelve el timestamp de la hora actual yyyyMMddHHmmss
	 *  
	 * @return
	 * @throws ClsExceptions
	 */
	static public String getTimeStamp() throws ClsExceptions {
		return getTimeStamp("");
	}
	static public String getTimeStamp(String formato) throws ClsExceptions {
		String val = "";
		if(formato==null || formato.equalsIgnoreCase("")){
			formato="yyyyMMddHHmmss";
		}
		try {
		    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(formato);
		    java.util.Date d = new Date();
			val = sdf.format(d);
		} catch (Exception e) {
			val="";
		}
		return val;
	}
		
		// vector con el signo, la parte entera y la parte decimal (dos posiciones decimales) para el impreso 190
		public static Vector<String> desdoblarDouble (Double valor) throws ClsExceptions {
			Vector<String> salida= new Vector<String>();
			
			String signo = " ";
			
			if (valor == null) {
				salida.add("");
				salida.add("");
				salida.add("");
			} else {
				String sValor = null;
				
				if (valor.doubleValue()<0) {
					sValor = String.format("%.2f",valor).replace(",", ".");
					// le quitamos el signo
					sValor = sValor.substring(1,sValor.length());
					signo = "N";
				} else {
					sValor = String.format("%.2f",valor).replace(",", ".");
				}
					
				salida.add(signo);

				int pos = sValor.indexOf(".");
				if (pos!=-1) {
					// tiene punto
					salida.add(sValor.substring(0,pos));
					salida.add(sValor.substring(pos+1,sValor.length()));
					
				} else {
					// NO tiene punto
					salida.add(sValor);
					salida.add("");
				}
			}
			
			return salida;	
		}

		public static String LTrim (String s) 
		{
			return LTrim (s, null);
		}

		public static String LTrim (String s, String cadenaASub) 
		{
			if (cadenaASub == null) cadenaASub = new String (" ");
	        for (int j = 0; j < s.length(); j++) {
	        	if(s.startsWith(cadenaASub))
	        	{
	        		s = s.substring(cadenaASub.length());
	        		j=0;
	        	}
	        	else break;
	        }
	        return s;
		}

		
		public static String obtenerIPServidor (HttpServletRequest req) throws ClsExceptions {
			String salida = "NO ENCONTRADO";
			try {
				
				// saltamos el frontend
				String fe = ""; 
				fe = req.getHeader("WL-Proxy-Client-IP");
				if (fe!=null && !fe.trim().equals("")) {
					// existe front end y aqui esta la IP original
					salida = fe;
				} else {
					// No existe front end
					// saltamos el proxy
					fe = req.getHeader("HTTP_X_FORWARDED_FOR");
					if (fe!=null && !fe.trim().equals("")) {
						// existe proxy y aqui esta la IP original
						salida = fe;
					} else {
						// No existe el proxy
						fe = req.getRemoteAddr();
						salida = fe;
					}
				}
				
				return salida;
				
			} catch (Exception e) {
				throw new ClsExceptions(e,"Error en obtenerIPServidor");
			}
		}

    public static String escape(String s) {
		return StringUtils.escape(s); 
    }

    public static String replaceFirstIgnoreCase (String texto, String clave, String valor)
    {
    	if(texto!=null && clave!=null && valor!=null){
	    	String t = texto.toUpperCase();
	    	int ini = t.indexOf(clave.toUpperCase());
	    	if (ini < 0) 
	    		return texto;
	    	
	    	t = texto.substring(0, ini) + valor + texto.substring(ini + clave.length());
	    	return t;
    	}else{
    		ClsLogging.writeFileLogWithoutSession("replaceFirstIgnoreCase.returntexto. Devolvemos espacio");
    		return "";
    	}
    }
    
    private static int replaceFirstIgnoreCase (String texto[], String clave, String valor, int posIni)
    {
    	if (texto==null || texto.length < 0 ||clave ==null||valor ==null){ 
    		ClsLogging.writeFileLogWithoutSession("replaceFirstIgnoreCase.returnmenosuno");
    		return -1;
    	}
    	
    	String t = texto[0].toUpperCase();
    	int ini = t.indexOf(clave.toUpperCase(), posIni);
    	if (ini < 0) 
    		return -1;
    	
    	t = texto[0].substring(0, ini) + valor + texto[0].substring(ini + clave.length());
    	texto[0] = t;
    	return ini + valor.length();
    }
    
    public static String replaceAllIgnoreCase (String texto, String clave, String valor) 
    {
		String t[] = {texto}; 
    	int i = 0;
		while(true){
			i = UtilidadesString.replaceFirstIgnoreCase(t, clave, valor, i);
    		if (i < 0) 
    			return t[0];
    	} 
    }

//    public static String replaceAllIgnoreCase (String texto, String clave, String valor) 
//    {
//    	if (valor.toUpperCase().indexOf(clave.toUpperCase()) > 0) 
//    		return texto;
//    	
//    	while (true) {
//    		String t = UtilidadesString.replaceFirstIgnoreCase(texto, clave, valor);
//    		if (texto.equals(t)) 
//    			return texto;
//    		texto = t;
//    	}
//    }
    
    /**
     * Funcion que oculta los primeros numeros de la cuenta corriente.
     * Solo deja visible las 4 ultimos cifras de la cuenta.
     * In: 0123456789 ->  Out: ******6789
     */
    public static String mostrarNumeroCuentaConAsteriscos (String numeroCuenta) 
    {
    	try	{
    		// Verficamos si el numero de cuenta es completo banco-oficina-dc-numeroCuenta
    		String aux = "";    		
    		int i = numeroCuenta.lastIndexOf("-");
    		if (i > 0) {
    			aux = numeroCuenta.substring(0, i+1);
    			numeroCuenta = numeroCuenta.substring(i+1);
    		}
    		numeroCuenta = numeroCuenta.trim();
    		numeroCuenta = numeroCuenta.substring(numeroCuenta.length()-4);
    		while (numeroCuenta.length() < 10) {
    			numeroCuenta = "*" + numeroCuenta;
    		}
    		return aux + numeroCuenta;
    	}
    	catch (Exception e) {
    		return "";
		}
    }
    
    /**
     * Funcion que oculta los primeros numeros de la cuenta corriente.
     * Solo deja visible las 4 ultimos cifras de la cuenta.
     * In: 0123456789 ->  Out: ******6789
     */
    public static String mostrarIBANConAsteriscos (String iban) {
    	try	{
    		// Verficamos si el iban tiene espacios
    		iban = iban.trim().replaceAll(" ", "");    		
    		String ibanInicio = iban.substring(0,8);
    		String ibanFin = iban.substring(iban.length()-4);
    		String asteriscos = "";
    		int longitudIbanMedio = iban.substring(8,iban.length()-4).length();
    		
    		for(int i = 0; i < longitudIbanMedio; i++) {
    			asteriscos = "*" + asteriscos;
    		}
    		return mostrarDatoMascara(ibanInicio + asteriscos + ibanFin,"AA AA AAAA **** **** **** **** **** **** **");
    	}
    	catch (Exception e) {
    		return "";
		}
    }    
    
    /**
     * 
     * @param nombreFichero Debe ser unicamente el nombre del fichero 'fichero.txt' sin ruta
     * @return
     */
    public static String validarNombreFichero (String nombreFichero) 
    {
    	char caracter = '_';
    	nombreFichero = nombreFichero.replace('\\', caracter);
    	nombreFichero = nombreFichero.replace('/', caracter);
    	nombreFichero = nombreFichero.replace(':', caracter);
    	nombreFichero = nombreFichero.replace('*', caracter);
    	nombreFichero = nombreFichero.replace('?', caracter);
    	nombreFichero = nombreFichero.replace('\"', caracter);
    	nombreFichero = nombreFichero.replace('<', caracter);
    	nombreFichero = nombreFichero.replace('>', caracter);
    	nombreFichero = nombreFichero.replace('|', caracter);
    	return nombreFichero;
    }
    
    /**
     * Convierte un nombre a texto valido para el modelo 190
     * 
     * @param nombre String del que se quieren eliminar caracteres no validos para el modelo 190
     * @return
     */
    public static String validarModelo190 (String nombre) 
    {
    	// Primero tenemos que convertir a validos aquellos caracteres que queramos conservar
    	nombre = replaceAllIgnoreCase(nombre, "�", "A");
    	nombre = replaceAllIgnoreCase(nombre, "�", "O");
    	   	
    	/*
    	nombre = replaceAllIgnoreCase(nombre, "�", "");
    	nombre = replaceAllIgnoreCase(nombre, "`", "");
    	nombre = replaceAllIgnoreCase(nombre, "^", "");

    	nombre = replaceAllIgnoreCase(nombre, "@", "");
    	nombre = replaceAllIgnoreCase(nombre, "#", "");
    	nombre = replaceAllIgnoreCase(nombre, "!", "");
    	nombre = replaceAllIgnoreCase(nombre, "!", "");
    	nombre = replaceAllIgnoreCase(nombre, "�", "");
    	*/
    	
    	// Caracteres validos segun la aplicacion Informativas Modelo 190 de AEAT-> QWERTYUIOP ASDFGHJKLѴ� ZXCVBNM,.- 
    	String caracteresValidos = "QWERTYUIOP ASDFGHJKLѴ� ZXCVBNM,.- ";
    	String nombreValido = "";
    	
    	for (int i=0; i<nombre.length(); i++){
    		// Si el caracter del nombre esta entre los validos
    		if( caracteresValidos.indexOf(nombre.charAt(i)) >= 0){
    			nombreValido += nombre.charAt(i);
    		}else{
    			nombreValido += " ";
    		}
    	}
    	
    	// Finalmente quitamos espacios dobles y triples (ocurre al quitar ciertos caracteres)
    	nombre = replaceAllIgnoreCase(nombre, "  ", " ");
    	nombre = replaceAllIgnoreCase(nombre, "  ", " ");

    	return nombreValido;
    }
    
	public static String validarModelo34 (String nombre)
    {
       
        // Caracteres validos segun la aplicacion Informativas Modelo 190 de AEAT-> QWERTYUIOP ASDFGHJKLѴ� ZXCVBNM,.-
        String caracteresValidos = "0123456789QWERTYUIOP ASDFGHJKLѴ� ZXCVBNM,.- �";
        String nombreValido = "";
       
        for (int i=0; i<nombre.length(); i++){
            // Si el caracter del nombre esta entre los validos
            if( caracteresValidos.indexOf(nombre.charAt(i)) >= 0){
                nombreValido += nombre.charAt(i);
            }else{
                nombreValido += " ";
            }
        }
       
        // Finalmente quitamos espacios dobles y triples (ocurre al quitar ciertos caracteres)
        nombre = replaceAllIgnoreCase(nombre, "  ", " ");
        nombre = replaceAllIgnoreCase(nombre, "  ", " ");

        return nombreValido;
    }
		
		/**
		 *  Metodo reemplazarTextoEntreMarca
		 *  convierte a mayuscula el texto que este entre la marca.
		 *  y devuelve todo el texto que recibio pero con los textos que estan entre la marca convertida a mayuscula.
		 * @param <B>texto</B> El texto a inspeccionar
		 * @param <B>marca</B> Marca que se define para buscar texto entre la marca		
		 * @return String con la cadena obtenida tras cambiar todos los textos que esten entre la marca y ponerlos a mayuscula.
		 */
		
	public static String reemplazarTextoEntreMarca(String textoreemplazar,String marca){
        if(textoreemplazar==null ||textoreemplazar.length()==0)
        	return "";
        String texto = textoreemplazar.replaceAll(AppConstants.MARCAS_ETIQUETAS_REEMPLAZO_TEXTO_ANTIGUO, marca);
        
        String textoAux = texto;
        String cadena = "";
        
        boolean flag = true;
        Vector vIndices = new Vector();
        while (flag) {
            int beginIndex = textoAux.indexOf(marca)+2;
            if(beginIndex==-1){
                flag= false;
                continue;
            }
            cadena = textoAux.substring(beginIndex);
            int endIndex = cadena.indexOf(marca);
            if(endIndex==-1){
                flag= false;
                continue;
            }
            int[] indices = {beginIndex,beginIndex+endIndex};
            vIndices.add(indices);
            textoAux = textoAux.substring(beginIndex+endIndex+2);
            beginIndex = textoAux.indexOf(marca);
            if(beginIndex==-1)
                flag= false;
        }
        int indiceInicial = 0;
        int indiceFin = 0;
        textoAux = texto;
        String cadenaAntes="";
        String cadenaDespues="";
        for (int i = 0; i < vIndices.size(); i++) {
            int[]indices = (int[]) vIndices.get(i);
            indiceInicial += indices[0];
            indiceFin += indices[1];
            cadenaAntes = textoAux.substring(0,indiceInicial);
            cadenaDespues = textoAux.substring(indiceFin);
            String cadenaReemplazo = textoAux.substring(indiceInicial,indiceFin).toUpperCase();
            indiceInicial=indiceFin+2;
            indiceFin=indiceFin+2;
            textoAux = cadenaAntes+cadenaReemplazo+cadenaDespues;
       }
        return textoAux;
        
    }
    
	//Valida una cadena para que s�lo tenga caracteres AlfaNum�ricos y guiones
	public static boolean validarAlfaNumericoYGuiones(String textorValidar){
		Pattern p = Pattern.compile("^[\\w,\\-,_]+$");
        Matcher m = p.matcher(textorValidar);
        return m.find();       
	}
	
	/**
	 * Valida un n�mero de m�vil para enviar sms por el servicio ECOS
	 * El n�mero debe de tener el formato (+xx)6xxxxxxxx o (+xx)7xxxxxxxx
	 * @param numeroTelefono
	 * @return
	 */
	public static boolean esNumMovilECOS(String numeroTelefono){
		boolean bEsNumMovilECOS = false;
		if (numeroTelefono != null && numeroTelefono.length() >= 9){
			String numeroSinPrefijo = "";
			if (numeroTelefono.startsWith("(+34)"))
				numeroSinPrefijo = numeroTelefono.substring(5);
			else
				numeroSinPrefijo = getNumeroConPrefijoECOS(numeroTelefono).substring(5);
			if (numeroSinPrefijo.length() == 9 && sonDigitos(numeroSinPrefijo) && 
					DIGITOS_COMIENZO_MOVIL.contains(Character.toString(numeroSinPrefijo.charAt(0))))
				bEsNumMovilECOS = true;
		}
		return bEsNumMovilECOS;
	}		
	
	public static boolean sonDigitos(String numero){
		if (numero == null)
			return false;
		else
			return patternDigitos.matcher(numero).find();
	}
	
	/**
     * Devuelve un numero de tel�fono con el prefijo de Espa�a entre parentesis (+34).
     * Si ya tiene prefijo y no es el de espa�a (contiene el + pero no 
     * seguido de 34/034/0034) se obviar� el + y se tomar� como un n�mero sin prefijo.
     * @param numero
     * @return
     */
	public static String getNumeroConPrefijoECOS(String numero) {
    	String prefijo = "+34";
    	if (numero.startsWith("+")){
    		numero = numero.substring(1);
    		if (numero.startsWith("34"))
    			numero = numero.substring(2);
    		else if (numero.startsWith("034"))
    			numero = numero.substring(3);
    		else if (numero.startsWith("0034"))
    			numero = numero.substring(4);
    	}
    	numero = "("+prefijo+")"+numero;
    	ClsLogging.writeFileLog("NUMERO DE TEL�FONO CON PREFIJO:" + numero,10);
    	return numero;
    }
	
	public static String ReemplazaIdioma (String consulta, HttpSession session, String parametroIdioma) {
		try {
			String idioma = "";
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
			try {
				idioma = "" + usrbean.getLanguage();
			}
			catch (Exception e) {
				idioma = "1"; // Por defecto espa�ol para la pantalla inicial de los 3 combos (es temporal)
			}
			consulta = consulta.replaceAll("(?i)" + parametroIdioma , idioma);
		}
		catch (Exception e) {}
		return consulta;
	}
	
	public static String ReemplazaParametros (String nombreParametro,String consulta, String dato[]) {
		for (int i = 0; i < dato.length; i++) {
			if ((dato[i] == null) || (dato[i].trim().equalsIgnoreCase(""))){
				break;
			}
			consulta = consulta.replaceFirst("(?i)"+nombreParametro, dato[i]);
		}
		return consulta;
	}
	
	 /**
	  * Method to convert map into json format
	  * @param map with data to be converted into json
	  * @return json string
	 * @throws IOException 
	  */	
	public static String createTagSelectDataJson(List<KeyValue> keyValuesList) throws IOException{
		HashMap<String, List<KeyValue>> hmOptions = new HashMap<String, List<KeyValue>>();
		hmOptions.put(TagSelect.DATA_JSON_OPTION_KEY, keyValuesList);
		return UtilidadesString.createJsonString(hmOptions, null, false);
	}
	
	 public static String createJsonString(Map jsonMap) throws IOException {
		 return UtilidadesString.createJsonString(jsonMap, null, false);
	 }
	 
	 public static String createJsonString(Map jsonMap, UsrBean usr, boolean translateValue) throws IOException {
		 String jsonString = "";
		 if (jsonMap != null && jsonMap.size() > 0){
			 try{
				if (translateValue && usr != null){
					 Iterator iteraKeys = jsonMap.keySet().iterator();
					 while (iteraKeys.hasNext()){
						 Object key = iteraKeys.next();
						 Object value = jsonMap.get(key);
						 try{
							 if (value instanceof String || !StringUtils.isEmpty(value.toString())){
								 jsonMap.put(key, UtilidadesString.getMensajeIdioma(usr, value.toString()));
							 }
						 } catch (Exception e){
							 
						 }
					 }
				}
				Writer writer = new StringWriter();
				JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(writer);
				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(jsonGenerator, jsonMap);
				jsonGenerator.close();
				jsonString = writer.toString();
			 } catch (Exception e){
				 ClsLogging.writeFileLog("Error al crear JSON string desde MAP",10);
			 }
		 }
		 return jsonString;
	 }
	 
	 public static String createJsonString(String jsonKey, String jsonValue) throws IOException {
		 String jsonString = "";
		 try{
			 if (jsonKey == null){
				 jsonKey = "";
			 }
			 if (!"".equals(jsonKey)){
				 jsonString = "{\""+jsonKey.toLowerCase()+"\":\""+jsonValue+"\"}";
			 }
		 } catch (Exception e){
			 ClsLogging.writeFileLog("Error al crear JSON con key: "+jsonKey+ " y value: "+jsonValue,10);
		 }
		 return jsonString;
	 }
	 
	 /**
	  * Method to convert json string into map
	  * @param jsonString with valid json format
	  * @return jsonMap HashMap
	  */
	 public static HashMap createHashMap(String jsonString) throws IOException {
		 return UtilidadesString.createHashMap(jsonString, HashMap.class);
	 }
	 
	 public static HashMap createHashMap(String jsonString, Class mapClass) throws IOException {
		 HashMap jsonMap = new HashMap<Object, Object>();
		 try{
			 jsonMap = (HashMap) new ObjectMapper().readValue(jsonString, mapClass);
		 } catch (Exception e){
			 ClsLogging.writeFileLog("Error al crear HASHMAP desde JSON STRING",10);
		 }
		 return jsonMap;
	 }
	 public static HashMap createJsonMap(String jsonString) throws Exception {
		 return UtilidadesString.createJson(jsonString, HashMap.class);
	 }
	 public static HashMap createJson(String jsonString, Class mapClass) throws Exception {
		 HashMap jsonMap = new HashMap<Object, Object>();
		 try{
			 jsonMap = (HashMap) new ObjectMapper().readValue(jsonString, mapClass);
		 } catch (Exception e){
			 ClsLogging.writeFileLog("Error al crear HASHMAP desde JSON STRING",10);
			 throw e;
		 }
		 return jsonMap;
	 }
	 public static Object createHashMap(String jsonString, TypeReference typeReference) throws IOException {
		 Object result= null;
		 try{
			 result = new ObjectMapper().readValue(jsonString, typeReference);
		 } catch (Exception e){
			 ClsLogging.writeFileLog("Error al crear HASHMAP desde JSON STRING",10);
		 }
		 return result;
	 }
	 
	/**
	 * Aplica una mascara del IBAN
	 * @param o
	 * @param longitud
	 * @return
	 */
	 public static String mostrarDatoMascara (Object o,String mask) {
		 String a = "";
		 try {
			 if (o!=null) {
				 MaskFormatter mf = new MaskFormatter(mask);
				 mf.setValueContainsLiteralCharacters(false);
				 a = mf.valueToString(o.toString());
			 }	
		 } catch (ParseException e) {
			 ClsLogging.writeFileLog("Error al utilizar la mascara",10);
			 return "";
		 }
		
		 return mostrarDatoJSP(a);
	 }
	/**
	 * Valida el campo url/ruta de cualquier formulario.
	 * @param url valor del campo del formulario,
	 * @return boolean En funci�n del resultado, retorna true si se cumple o false si la comprobaci�n es incorrecta
	 */
	public static boolean isValidURL(String url) {  
	
	    URL u = null;
	
	    try {  
	        u = new URL(url);  
	    } catch (MalformedURLException e) {  
	        return false;  
	    }
	
	    try {  
	        u.toURI();  
	    } catch (URISyntaxException e) {  
	        return false;  
	    }  
	
	    return true;  
	} 
	public static String getPrimeraMayuscula(String cadena){
		if(cadena!=null)
			return cadena.substring(0,1).toUpperCase() + cadena.substring(1).toLowerCase(); 
		else 
			return "";
		
	}
		
	
	/**
	 * Funci�n que elimina acentos y caracteres especiales de una cadena de texto.
	 * @param input
	 * @return cadena de texto limpia de acentos y caracteres especiales.
	 */
	public static String eliminarAcentosYCaracteresEspeciales(String input) {
		// Cadena de caracteres original a sustituir.
		String original = "��������������u�������������������!\"#$%&'()*+-,./:;<=>?@[\\]^_`{|}~";	

		// Cadena de caracteres ASCII que reemplazar�n los originales.
		String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC                                ";
		String output = input;
		for (int i = 0; i < original.length(); i++) {
			// Reemplazamos los caracteres especiales.
			output = output.replace(original.charAt(i), ascii.charAt(i));
		}// for i
		return output;
	}
	
	/**
	 * Funci�n que escapa las comillas simples para querys de bbdd.
	 * @param input
	 * @return cadena de texto escapa comillas.
	 */
	public static String escapaCaracteres(String input) {
		String output = "";
		if(input != null){
			output = input.replaceAll("'", "''");
		}		
		return output;
	}
}
