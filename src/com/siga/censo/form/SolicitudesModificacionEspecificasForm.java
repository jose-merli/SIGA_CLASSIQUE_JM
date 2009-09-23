/*
 * VERSIONES:
 * 
 * miguel.villegas - 24-01-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario para la edicion de solicitudes <br/>
 * de modificacion especificas
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.censo.form;

import com.siga.general.MasterForm;



public class SolicitudesModificacionEspecificasForm extends MasterForm{
		
	// Variables busqueda
	
	private String fechaDesde="";
	private String fechaHasta="";		
	private String solicitudes="";	
	private String solicitudesTipoModif="";	
	private String tipoModificacion="";
	private String estadoSolicitud="";	
	private String idPersona="";
	private String idInstitucion="";
	private String textoModificacion="";
	
	
	
	// Metodos set

	// Fomulario listado pendientes
	
	public void setSolicitudes(String solic){
		this.solicitudes=solic;
	}	
	
	public void setSolicitudesTipoModif(String solic){
		this.solicitudesTipoModif=solic;
	}
	
	// Formularios Busqueda
	
	public void setFechaDesde(String f){
		this.fechaDesde=f;
	} 	

	public void setFechaHasta(String f){
		this.fechaHasta=f;
	}	
	
	public void setTipoModifEspec(String id){
		this.tipoModificacion=id;
	}	

	public void setTextoModificacion(String texto){
		this.textoModificacion=texto;
	}
	
	public void setEstadoSolicitudModif(String id){
		this.estadoSolicitud=id;
	}	
		 				
	public void setIdPersona(String id){
		this.idPersona=id;
	}
	
	public void setIdInstitucion(String id){
		this.idInstitucion=id;		
	}	
	
	// Metodos get	
	
	// Fomulario listado pendientes
	
	public String getTextoModificacion(){
		return this.textoModificacion;
	}
	
	public String getSolicitudes(){
		return this.solicitudes;
	}
	
	public String getSolicitudesTipoModif(){
		return this.solicitudesTipoModif;
	}

	public String getFechaDesde(){
		return this.fechaDesde;
	} 	

	public String getFechaHasta(){
		return this.fechaHasta;
	}	

	public String getTipoModifEspec(){
		return this.tipoModificacion;
	}	

	public String getEstadoSolicitudModif(){
		return this.estadoSolicitud;
	}		
		
	public String getIdPersona(){
		return this.idPersona;
	}
	
	public String getIdInstitucion(){
		return this.idInstitucion;
	}
	
	public String[] getPeticiones(){		
		return this.solicitudes.split("%");
		
	}	
	public String[] getPeticionesTipoModif(){		
		return this.solicitudesTipoModif.split("%");
		
	}	
	
	
}