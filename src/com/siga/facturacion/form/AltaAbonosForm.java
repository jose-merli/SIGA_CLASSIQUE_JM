/*
 * VERSIONES:
 * 
 * miguel.villegas - 08-03-2005 - Creacion
 *	
 */

/**
 * Clase que recoge y establece los campos del formulario de alta de abonos <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */

package com.siga.facturacion.form;

import com.siga.general.MasterForm;



public class AltaAbonosForm extends MasterForm{
	

	private String idAbono="";
	private String idInstitucion="";
	private String idFactura="";
	private String numFactura="";
	private String fecha="";
	private String fechaFactura="";
	private String idPersona="";
	private String motivos="";
	
	// Metodos set
	
	// Formulario Alta Abono	
	
	public void setIdAbono(String id){
		this.idAbono=id;
	}
	
	public void setIdInstitucion(String id){
		this.idInstitucion=id;
	}
	
	public void setIdFactura(String id){
		this.idFactura=id;		
	}
	
	public void setNumFactura(String id){
		this.numFactura=id;		
	}
	
	public void setFecha(String fecha){
		this.fecha=fecha;
	}
	
	public void setFechaFactura(String fecha){
		this.fechaFactura=fecha;
	}
	
	public void setIdPersona(String id){
		this.idPersona=id;		
	}
	
	public void setMotivos(String motivo){
		this.motivos=motivo;		
	}
			
	// Metodos get	
	
	// Formulario Alta Abono
	
	public String getIdAbono(){
		return this.idAbono;
	}
	
	public String getIdInstitucion(){
		return this.idInstitucion;
	}
	
	public String getIdFactura(){
		return this.idFactura;		
	}
	
	public String getNumFactura(){
		return this.numFactura;		
	}
	
	public String getFecha(){
		return this.fecha;
	}
	
	public String getFechaFactura(){
		return this.fechaFactura;
	}
	
	public String getIdPersona(){
		return this.idPersona;		
	}
		
	public String getMotivos(){
		return this.motivos;		
	}
				
}