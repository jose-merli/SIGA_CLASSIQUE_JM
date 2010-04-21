/*
 * VERSIONES:
 * 
 * miguel.villegas - 28-12-2004 - Creacion
 *	
 */

/**
 * Recoge y establece los campos del formulario de mantenimiento de los datos colegiales generales <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.censo.form;

import com.atos.utils.GstDate;
import com.siga.general.MasterForm;
import com.siga.beans.*;
import com.siga.censo.vos.ColegiadoVO;
import com.siga.comun.vos.Vo;
	

public class DatosColegiacionForm extends MasterForm
{
	String idPersona, idInstitucion,nombre,numero;
	
	
	
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
	// Formulario Consulta Datos Estado	
			
	public void setMotivo(String mot){
		datos.put(CenEstadoActividadPersonaBean.C_MOTIVO,mot);
	}	
	
	public void setFechaEstado(String fecha){
		try{
			fecha = GstDate.getApplicationFormatDate("",fecha);
			if (fecha!=null){
			 datos.put(CenEstadoActividadPersonaBean.C_FECHAESTADO,fecha);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public void setIdCodigo(String cod){
		datos.put(CenEstadoActividadPersonaBean.C_IDCODIGO,cod);
	}	
	
	// Metodos get	
	public String getMotivo(){
		return (String)datos.get(CenEstadoActividadPersonaBean.C_MOTIVO);
	}

	public String getFechaEstado(){
		return (String)datos.get(CenEstadoActividadPersonaBean.C_FECHAESTADO);
	}	
	
	public String getIdCodigo(){
		return (String)datos.get(CenEstadoActividadPersonaBean.C_IDCODIGO);
	}
	
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return Returns the numero.
	 */
	public String getNumero() {
		return numero;
	}
	/**
	 * @param numero The numero to set.
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getId() {
		return getIdPersona() + Vo.PK_SEPARATOR + getIdInstitucion();
	}

}