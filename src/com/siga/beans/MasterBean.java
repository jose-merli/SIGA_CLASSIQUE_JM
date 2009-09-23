/*
 * Created on 19-oct-2004
 *
 */
package com.siga.beans;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * @author daniel.campos
 *
 * MasterBean
 * Clase maestra que representa el registro de una tabla
 *
 * Tiene tantos metodos set y get por cada uno de los campos de la tabla 
 */
public abstract class MasterBean implements Serializable{

	protected Integer	usuMod;
	protected String	fechaMod = "";
	private Hashtable originalHash=null;

	static public final String C_FECHAMODIFICACION 	= "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION 	= "USUMODIFICACION";

	protected MasterBean ( ) {
//		this.setFechaMod();
//		this.setUsuMod();
	}

	public void setOriginalHash(Hashtable hash) {
		originalHash=(Hashtable)hash.clone();
	}
	
	public Hashtable getOriginalHash() {
		return originalHash;
	}
	
	public void setUsuMod (Integer id) 	{ this.usuMod = id; }
	public void setFechaMod (String f) 	{ this.fechaMod = f; }

	public Integer getUsuMod 			() 	{ return this.usuMod; }
	public String getFechaMod 			() 	{ return this.fechaMod; }

/*	// Ahora
	Hashtable datosBean = new Hashtable();

	static public final String C_FECHAMODIFICACION 	= "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION 	= "USUMODIFICACION";

	protected MasterBean ( ) {
		this.setFechaMod("Sysdate");
		this.setUsuMod(-1);
	}

	public void setUsuMod (int id) 		{ this.datosBean.put(MasterBean.C_USUMODIFICACION, String.valueOf(id)); }
	public void setFechaMod (String f) 	{ this.datosBean.put(MasterBean.C_FECHAMODIFICACION, f); }
	
	public int getUsuMod 			() 	{ return this.datosBean.get(MasterBean.C_USUMODIFICACION); }
	public String getFechaMod 		() 	{ return (String)this.datosBean.get(MasterBean.C_FECHAMODIFICACION); }
*/
}
