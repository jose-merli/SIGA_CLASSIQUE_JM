package com.siga.censo.form;


import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * Clase action form del caso de uso ANTICIPOS CLIENTE
 * @author ivan.arias 28-11-2008
 * 
 */
 public class AnticiposClienteForm extends MasterForm {
	 
	 private String incluirRegistrosConBajaLogica;
	 
 	
	// Metodos Set (Formulario (*.jsp))

	// BLOQUE PARA EL FORMULARIO DE BUSQUEDA SIMPLE 
 		
 	public void setIdInstitucion(String valor) { 
 		this.datos.put("IdInstitucion", valor);
 	}
	public void setIdPersona(String valor) { 
		this.datos.put("IdPersona", valor);
	}
	public void setIdAnticipo(String valor) { 
		this.datos.put("IdAnticipo", valor);						
	}
	public void setFecha(String valor) { 
		this.datos.put("Fecha", valor);						
	}
	public void setImporteAnticipado(String valor) { 
		this.datos.put("ImporteAnticipado", valor);						
	}
	public void setImporteRestante(String valor) { 
		this.datos.put("ImporteRestante", valor);						
	}
	public void setImporteUsado(String valor) { 
		this.datos.put("ImporteUsado", valor);					
	}
	public void setDescripcion(String valor) { 
		this.datos.put("Descripcion", valor);						
	}
 	
	public void setAccion (String valor) { 
		this.datos.put("Accion", valor);						
	}
	
 	public void setCategoriaServicio(String valor){
 		this.datos.put("CategoriaServicio", valor);
 	}
 	
 	public void setTipoServicio(String valor){
 		this.datos.put("TipoServicio", valor);
 	}
 	
 	public void setServiciosSeleccionados(String valor){
 		this.datos.put("ServiciosSeleccionados", valor);
 	}
	
 	public void setNombreServicio(String valor){
 		this.datos.put("NombreServicio", valor);
 	}
	
 	public void setCtaContable(String valor){
 		this.datos.put("CtaContable", valor);
 	}
 	
 	public void setIncluirRegistrosConBajaLogica(String valor) {
		this.incluirRegistrosConBajaLogica = valor;
	}
	
 	public String getIdInstitucion	() 	{ 
 		return UtilidadesHash.getString(this.datos, "IdInstitucion");		
 	}
 	public String getIdPersona	() 	{ 
 		return UtilidadesHash.getString(this.datos, "IdPersona");		
 	}
 	public String getIdAnticipo	() 	{ 
 		return UtilidadesHash.getString(this.datos, "IdAnticipo");		
 	}
 	public String getFecha	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Fecha");		
 	}
 	public String getImporteAnticipado	() 	{ 
 		return UtilidadesHash.getString(this.datos, "ImporteAnticipado");		
 	}
 	public String getImporteRestante	() 	{ 
 		return UtilidadesHash.getString(this.datos, "ImporteRestante");		
 	}
 	public String getImporteUsado	() 	{ 
 		return UtilidadesHash.getString(this.datos, "ImporteUsado");		
 	}
 	public String getDescripcion	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Descripcion");		
 	}
 	
 	public String getCategoriaServicio(){
 		return UtilidadesHash.getString(this.datos, "CategoriaServicio");
 	}
 	public String getTipoServicio(){
 		return UtilidadesHash.getString(this.datos, "TipoServicio");
 	}
 	public String getNombreServicio(){
 		return UtilidadesHash.getString(this.datos, "NombreServicio");
 	}
 	public String getCtaContable(){
 		return UtilidadesHash.getString(this.datos, "CtaContable");
 	}

 	public String getAccion	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Accion");		
 	}

 	public String getServiciosSeleccionados	() 	{ 
 		return UtilidadesHash.getString(this.datos, "ServiciosSeleccionados");		
 	}
 	
 	public String getIncluirRegistrosConBajaLogica() {
		return this.incluirRegistrosConBajaLogica;
	}
	
 	

}