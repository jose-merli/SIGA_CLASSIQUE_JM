/*
 * Created on 11-nov-2004
 *	Revision 2-2-2005 nuria.rgonzalez Float y Double 
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.Utilidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UtilidadesHash {

	// Metodos sobre las tablas Hash
	static public String getString (Hashtable hash, String key) { // throws ClsExceptions {
		try {
			return (String) hash.get(key);
		}
		catch (Exception e) {
			return null;
		}
	}
	static public ArrayList getArrayList (Hashtable hash, String key) { // throws ClsExceptions {
		try {
			return (ArrayList) hash.get(key);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	static public HashMap getHashMap (Hashtable hash, String key) { // throws ClsExceptions {
		try {
			return (HashMap) hash.get(key);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	

	static public Integer getInteger (Hashtable hash, String key){ // throws ClsExceptions {
		try {
			return Integer.valueOf((String)hash.get(key));
		}
		catch (Exception e) {
			return null;
		}
	}
	
	static public Date getDate (Hashtable hash, String key){ // throws ClsExceptions {
		try {
			return (Date)hash.get(key);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	static public Float getFloat (Hashtable hash, String key){// throws ClsExceptions {
		try {
			return Float.valueOf((String)hash.get(key));
		}
		catch (Exception e) {
			return null;
		}
	}

	static public Double getDouble (Hashtable hash, String key){// throws ClsExceptions {
		try {
			return Double.valueOf(((String)hash.get(key)).replaceAll(",","."));
		}
		catch (Exception e) {
			return null;
		}
	}
	
	static public Long getLong (Hashtable hash, String key){// throws ClsExceptions {
		try {
			return Long.valueOf((String)hash.get(key));
		}
		catch (Exception e) {
			return null;
		}
	}

	static public Boolean getBoolean (Hashtable hash, String key) { // throws ClsExceptions {
		try {
			Boolean b = (Boolean) hash.get(key);
			if (b == null) {
				return new Boolean (false);
			}
			return b;
		}
		catch (Exception e) {
			return new Boolean (false);
		}
	}

	static public void set (Hashtable hash, String key, String valor) {
		try {
			if (key==null || valor==null) return;
			hash.put (key, valor);
		}
		catch (Exception e) { 	
		    return;
		}
	}

	static public void set (Hashtable hash, String key, Integer valor){
		try {
			if (key==null || valor==null) return;
			hash.put (key, String.valueOf(valor));
		}
		catch (Exception e) { 	
		    return;
		}
	}
	static public void set (Hashtable hash, String key, ArrayList valor){
		try {
			if (key==null || valor==null) return;
			hash.put (key, valor);
		}
		catch (Exception e) { 	
		    return;
		}
	}
	static public void set (Hashtable hash, String key, HashMap valor){
		try {
			if (key==null || valor==null) return;
			hash.put (key, valor);
		}
		catch (Exception e) { 	
		    return;
		}
	}

	static public void set (Hashtable hash, String key, Long valor){
		try {
			if (key==null || valor==null) return;
			hash.put (key, String.valueOf(valor));
		}
		catch (Exception e) {
		    return;
		}
	}
	
	static public void set (Hashtable hash, String key, Float valor){
		try {
			if (key==null || valor==null) return;
			hash.put (key, String.valueOf(valor));
		}
		catch (Exception e) {
		    return;
		}
	}
	
	static public void set (Hashtable hash, String key, Double valor){
		try {
			if (key==null || valor==null) return;
			hash.put (key, String.valueOf(valor));
		}
		catch (Exception e) {
		    return;
		}
	}

	static public void set (Hashtable hash, String key, Boolean valor) {
		try {
			if (key==null || valor==null) return;
			hash.put (key, valor);
		}
		catch (Exception e) { 	
		    return;
		}
	}

	static public void set (Hashtable hash, String key, Date valor) {
		try {
			if (key==null || valor==null) return;
			hash.put (key, valor);
		}
		catch (Exception e) { 	
		    return;
		}
	}

// RGG 07-04-2006 estos set sirven para luego poder comparar los hash creados desde un form, que siempre crean entrada 
// aunque sea nulo , de tal modo que la comparacion del update funcione
	
	static public void setForCompare (Hashtable hash, String key, String valor) {
		try {
			if (valor==null) 
				hash.put (key, "");
			else
				hash.put (key, valor);
		}
		catch (Exception e) { 	
		    return;
		}
	}

	static public void setForCompare (Hashtable hash, String key, Integer valor){
		try {
			if (valor==null) 
				hash.put (key, "");
			else
				hash.put (key, valor.toString());
		}
		catch (Exception e) { 	
		    return;
		}
	}

	static public void setForCompare (Hashtable hash, String key, Long valor){
		try {
			if (valor==null) 
				hash.put (key, "");
			else
				hash.put (key, valor.toString());
		}
		catch (Exception e) {
		    return;
		}
	}
	
	static public void setForCompare (Hashtable hash, String key, Float valor){
		try {
			if (valor==null) 
				hash.put (key, "");
			else
				hash.put (key, valor.toString());
		}
		catch (Exception e) {
		    return;
		}
	}
	
	static public void setForCompare (Hashtable hash, String key, Double valor){
		try {
			if (valor==null) 
				hash.put (key, "");
			else
				hash.put (key, valor.toString());
		}
		catch (Exception e) {
		    return;
		}
	}

	static public void setForCompare (Hashtable hash, String key, Boolean valor) {
		try {
			if (valor==null) 
				hash.put (key, "");
			else
				hash.put (key, valor.toString());
		}
		catch (Exception e) { 	
		    return;
		}
	}

	static public String getShortDate(Hashtable ht,String key) {
		String f=getString(ht,key);
		if(f==null){
			f="";
		}else{
			try {
				f=GstDate.getFormatedDateShort("",f);
			} catch (ClsExceptions e) {
				f="";
			}
		}
		return f;
	}
	
	static public void setShortDate(Hashtable ht,String key, String dato) {
		String f=dato;
		if(f==null){
			f="";
		}else{
			try {
				f=GstDate.getApplicationFormatDate("",f);
			} catch (ClsExceptions e) {
				f="";
			}
		}
		set(ht,key,f);
	}
	
	static public Vector eliminaRepetidosUnaClave (Vector entrada, String campo) {
		Vector salida = new Vector();
		if (entrada!=null) {
			Object valorAnt = null;
			for (int i=0;i<entrada.size();i++) {
				Hashtable ht = (Hashtable) entrada.get(i);
				Object valor = ht.get(campo);
				if (!valor.equals(valorAnt)) {
					salida.add(ht);
				}
				valorAnt = valor;
			}
		} else {
			salida = null;
		}
		return salida;
	}
}
