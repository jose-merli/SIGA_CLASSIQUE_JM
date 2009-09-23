/*
 * Created on Jan 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.form;


import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExpedientesForm extends MasterForm{
		
	private String idPersona=""; 	
	private String idInstitucion="";
	private String idInstitucionTipoExpediente=""; 	
	private String idTipoExpediente="";
	private String numeroExpediente=""; 	
	private String fechaExpediente="";
	private String motivo="";
	private String nombre="";
	private String numero="";
	
	// Metodos set	 	
	
	public void setNombre(String nombre){
		this.nombre=nombre;
	}

	public void setNumero(String numero){
		this.numero=numero;
	}
	
	public void setidPersona(String id){
		this.idPersona=id;
	}

	public void setidInstitucion(String id){
		this.idInstitucion=id;
	}
				
	public void setidInstitucionTipoExpediente(String id){
		this.idInstitucionTipoExpediente=id;
	}

	public void setidTipoExpediente(String id){
		this.idTipoExpediente=id;
	}

	public void setNumeroExpediente(String num){
		this.numeroExpediente=num;
	}

	public void setFechaExpediente(String fecha){
		this.fechaExpediente=fecha;
	}

	public void setMotivo(String mot){
		this.motivo=mot;
	}
	
	// Metodos get	
	
	public String getNombre(){
		return this.nombre;
	}

	public String getNumero(){
		return this.numero;
	}
	
	public String getidPersona(){
		return this.idPersona;
	}

	public String getidInstitucion(){
		return this.idInstitucion;
	}
				
	public String getidInstitucionTipoExpediente(){
		return this.idInstitucionTipoExpediente;
	}

	public String getidTipoExpediente(){
		return this.idTipoExpediente;
	}

	public String getNumeroExpediente(){
		return this.numeroExpediente;
	}

	public String getFechaExpediente(){
		return this.fechaExpediente;
	}

	public String getMotivo(){
		return this.motivo;
	}		
	
}

