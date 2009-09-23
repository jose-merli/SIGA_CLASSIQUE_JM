/*
 * VERSIONES:
 * 
 * miguel.villegas - 04-01-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario para la edicion de solicitudes de modificacion <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.censo.form;

import com.siga.general.MasterForm;
import com.siga.beans.*;


public class SolicitudesModificacionForm extends MasterForm{
		
	// Variables busqueda
	
	private String fechaDesde="";
	private String fechaHasta="";		
	private String solicitudes="";		
	
	// Metodos set

	// Fomulario listado pendientes
	
	public void setSolicitudes(String solic){
		this.solicitudes=solic;
	}	
	
	// Formularios Busqueda
	
	public void setFechaDesde(String f){
		this.fechaDesde=f;
	} 	

	public void setFechaHasta(String f){
		this.fechaHasta=f;
	}	
	
	// Formulario SolicitudTextoLibre	
	
	public void setCmbTipoModificacion(String id){
		datos.put(CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION ,id);
	} 	
	
	public void setDescripcion(String desc){
		datos.put(CenSolicitudesModificacionBean.C_DESCRIPCION ,desc);
	}
	
	public void setEstadoSolicitudModif(String id){
		datos.put(CenSolicitudesModificacionBean.C_IDESTADOSOLIC ,id);
	}	
	
	public void setIdPersona(String id){
		datos.put(CenSolicitudesModificacionBean.C_IDPERSONA ,id);
	}
	
	public void setIdInstitucion(String id){
		datos.put(CenSolicitudesModificacionBean.C_IDINSTITUCION ,id);
	}	
	
	// Metodos get	
	
	// Fomulario listado pendientes
	
	public String getSolicitudes(){
		return this.solicitudes;
	}
	
	// Formularios Busqueda
	
	public String getFechaDesde(){
		return this.fechaDesde;
	} 	

	public String getFechaHasta(){
		return this.fechaHasta;
	}	
	
	// Formulario SolicitudTextoLibre
	
	public String getCmbTipoModificacion(){
		return (String)datos.get(CenSolicitudesModificacionBean.C_IDTIPOMODIFICACION);
	}

	public String getDescripcion(){
		return (String)datos.get(CenSolicitudesModificacionBean.C_DESCRIPCION);
	}
	
	public String getEstadoSolicitudModif(){
		return (String)datos.get(CenSolicitudesModificacionBean.C_IDESTADOSOLIC);
	}	
	
	public String getIdPersona(){
		return (String)datos.get(CenSolicitudesModificacionBean.C_IDPERSONA);
	}
	
	public String getIdInstitucion(){
		return (String)datos.get(CenSolicitudesModificacionBean.C_IDINSTITUCION);
	}
	
	public String[] getPeticiones(){		
		return this.solicitudes.split("%");
		
	}	
	
	
}