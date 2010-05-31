/*
 * Created on Mar 6, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.gratuita.form;
import java.util.List;

import com.siga.beans.ScsTelefonosPersonaJGBean;

/**
 * @author A203486
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DefinirTelefonosJGForm extends com.siga.general.MasterForm {

	String idPersona=null; 
	String numeroTelefonoJG=null;
	String nombreTelefonoJG=null;
	String accion=null; 
	String idTelefono=null;
	String idInstitucion=null;
	String preferenteSms=null;
	
	
	
	/**
	 * @return Returns the preferenteSms.
	 */
	public String getPreferenteSms() {
		return preferenteSms;
	}
	/**
	 * @param preferenteSms The preferenteSms to set.
	 */
	public void setPreferenteSms(String preferenteSms) {
		this.preferenteSms = preferenteSms;
	}
	/**
	 * @return Returns the accion.
	 */
	public String getAccion() {
		return accion;
	}
	/**
	 * @param accion The accion to set.
	 */
	public void setAccion(String accion) {
		this.accion = accion;
	}
	/**
	 * @return Returns the descripcionTelefonoJG.
	 */
	public String getNombreTelefonoJG() {
		return nombreTelefonoJG;
	}
	/**
	 * @param descripcionTelefonoJG The descripcionTelefonoJG to set.
	 */
	public void setNombreTelefonoJG(String descripcionTelefonoJG) {
		this.nombreTelefonoJG = descripcionTelefonoJG;
	}
	/**
	 * @return Returns the idPersona.
	 */
	public String getIdPersona() {
		return idPersona;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @return Returns the telefonoJG.
	 */
	public String getNumeroTelefonoJG() {
		return numeroTelefonoJG;
	}
	/**
	 * @param telefonoJG The telefonoJG to set.
	 */
	public void setNumeroTelefonoJG(String telefonoJG) {
		this.numeroTelefonoJG = telefonoJG;
	}
	/**
	 * @return Returns the idTelefono.
	 */
	public String getIdTelefono() {
		return idTelefono;
	}
	/**
	 * @param idTelefono The idTelefono to set.
	 */
	public void setIdTelefono(String idTelefono) {
		this.idTelefono = idTelefono;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public String getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	
	
	
	List<ScsTelefonosPersonaJGBean> telefonos; 
	public List<ScsTelefonosPersonaJGBean> getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(List<ScsTelefonosPersonaJGBean> telefonos) {
		this.telefonos = telefonos;
	}

}
