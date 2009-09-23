
package com.siga.Utilidades;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.beans.AdmLenguajesAdm;

/**
 * @author daniel.campos
 *
 */

public class UtilidadesMultidioma 
{
	static public String getDatoMaestroIdioma (String key, UsrBean user) 
	{
		AdmLenguajesAdm a = new AdmLenguajesAdm(user);
		Vector v;
		Hashtable codigos = new Hashtable();
		try {
		    codigos.put(new Integer(1),key);
		    codigos.put(new Integer(2),user.getLanguage());
            v = a.findBind("SELECT F_SIGA_GETRECURSO(:1,:2) AS VALOR FROM DUAL",codigos).getAll();
        } catch (ClsExceptions e) {
            e.printStackTrace();
            return key;
        }
        if (v!=null&&v.size()>0) {
            Row ht = (Row) v.get(0);
		    return (String)ht.getValue("VALOR");
		}
		return key;
	}

	static public String getCampoMultidioma (String campo, String idioma) 
	{
		if ((campo == null) || campo.equals("")) return "";
		if (idioma == null) idioma = new String ("1");

		String alias = campo;
		int i = campo.indexOf(".");
		if (i >= 0) {
			alias = campo.substring(i+1);
		}
		return " F_SIGA_GETRECURSO (" + campo + "," + idioma + ") " + alias + " ";
	}

	static public String getCampoMultidiomaSimple (String campo, String idioma) 
	{
		if ((campo == null) || campo.equals("")) return "";
		if (idioma == null) idioma = new String ("1");

		return " F_SIGA_GETRECURSO (" + campo + "," + idioma + ")  ";
	}
}
