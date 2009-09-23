package com.siga.gratuita.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * Clase action form del caso de uso BUSCAR CLIENTE
 * @author raul.ggonzalez 14-12-2004
 */
public class BusquedaClientesFiltrosForm extends MasterForm {
	
	// Metodos Set (Formulario (*.jsp))
	public void setSustituta (String dato) { 
		UtilidadesHash.set(this.datos,"Sustituta", dato);
	}
	
	public void setIdInstitucion (Integer dato) { 
		UtilidadesHash.set(this.datos,"IdInstitucion", dato);
	}
	
	public void setIdPersona (Long dato) { 
		UtilidadesHash.set(this.datos,"IdPersona", dato);
	}
	
	public void setNumeroColegiado (String dato) { 
		UtilidadesHash.set(this.datos,"NumeroColegiado", dato);
	}
	
	public void setNif (String dato) { 
		UtilidadesHash.set(this.datos,"Nif", dato);
	}
	
	public void setNombrePersona (String dato) { 
		UtilidadesHash.set(this.datos,"NombrePersona", dato);
	}
	
	public void setApellido1 (String dato) { 
		UtilidadesHash.set(this.datos,"Apellido1", dato);
	}
	
	public void setApellido2 (String dato) { 
		UtilidadesHash.set(this.datos,"Apellido2", dato);
	}
	
	public void setConcepto (String dato) { 
		UtilidadesHash.set(this.datos,"Concepto", dato);
	}
	
	public void setOperacion (String dato) { 
		UtilidadesHash.set(this.datos,"Operacion", dato);
	}
	
	public void setIdTurno (String dato) { 
		UtilidadesHash.set(this.datos,"IdTurno", dato);
	}
	
	public void setIdGuardia (String dato) { 
		UtilidadesHash.set(this.datos,"IdGuardia", dato);
	}
	
	public void setFecha (String dato) { 
		UtilidadesHash.set(this.datos,"Fecha", dato);
	}
	
	public void setIdFiltro (String dato) { 
		UtilidadesHash.set(this.datos,"IdFiltro", dato);
	}
	
	public void setIdDireccion (Long dato) { 
		UtilidadesHash.set(this.datos,"IdDireccion", dato);
	}
	
	public void setTelefono1 (String dato) { 
		UtilidadesHash.set(this.datos,"Telefono1", dato);
	}
	
	public void setTelefono2 (String dato) { 
		UtilidadesHash.set(this.datos,"Telefono2", dato);
	}
	
	public void setMovil (String dato) { 
		UtilidadesHash.set(this.datos,"Movil", dato);
	}
	
	public void setFax1 (String dato) { 
		UtilidadesHash.set(this.datos,"Fax1", dato);
	}
	
	public void setFax2 (String dato) { 
		UtilidadesHash.set(this.datos,"Fax2", dato);
	}
	
	
	// Metodos Get 1 por campo Formulario (*.jsp)
	public String getSustituta	() 	{ 
		return UtilidadesHash.getString(this.datos, "Sustituta");		
	}	
	
	public Integer getIdInstitucion	() 	{ 
		return UtilidadesHash.getInteger(this.datos, "IdInstitucion");		
	}
	
	public Long getIdPersona	() 	{ 
		return UtilidadesHash.getLong(this.datos, "IdPersona");		
	}
	
	public String getNumeroColegiado	() 	{ 
		return UtilidadesHash.getString(this.datos, "NumeroColegiado");		
	}
	
	public String getNif	() 	{ 
		return UtilidadesHash.getString(this.datos, "Nif");		
	}
	
	public String getNombrePersona	() 	{ 
		return UtilidadesHash.getString(this.datos, "NombrePersona");		
	}
	
	public String getApellido1	() 	{ 
		return UtilidadesHash.getString(this.datos, "Apellido1");		
	}
	
	public String getApellido2	() 	{ 
		return UtilidadesHash.getString(this.datos, "Apellido2");		
	}
	
	public String getConcepto	() 	{ 
		return UtilidadesHash.getString(this.datos, "Concepto");		
	}
	
	public String getOperacion	() 	{ 
		return UtilidadesHash.getString(this.datos, "Operacion");		
	}
	
	public String getIdTurno	() 	{ 
		return UtilidadesHash.getString(this.datos, "IdTurno");		
	}
	
	public String getIdGuardia	() 	{ 
		return UtilidadesHash.getString(this.datos, "IdGuardia");		
	}
	
	public String getFecha	() 	{ 
		return UtilidadesHash.getString(this.datos, "Fecha");		
	}
	
	public String getIdFiltro	() 	{ 
		return UtilidadesHash.getString(this.datos, "IdFiltro");		
	}
	
	public Long getIdDireccion	() 	{ 
		return UtilidadesHash.getLong(this.datos, "IdDireccion");		
	}
	
	public String getTelefono1	() 	{ 
		return UtilidadesHash.getString(this.datos, "Telefono1");		
	}
	
	public String getTelefono2	() 	{ 
		return UtilidadesHash.getString(this.datos, "Telefono2");		
	}
	
	public String getMovil() 	{ 
		return UtilidadesHash.getString(this.datos, "Movil");		
	}
	
	public String getFax1() 	{ 
		return UtilidadesHash.getString(this.datos, "Fax1");		
	}
	
	public String getFax2() 	{ 
		return UtilidadesHash.getString(this.datos, "Fax2");		
	}
	
	
	// OTRAS FUNCIONES 
	
	
	/**
	 * Metodo que resetea el formulario
	 * @param  mapping - Mapeo de los struts
	 * @param  request - objeto llamada HTTP 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		try {
			// resetea el formulario
			super.reset(mapping, request);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}