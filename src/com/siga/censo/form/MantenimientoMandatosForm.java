package com.siga.censo.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * Clase action form del caso de uso BUSCAR CLIENTE
 * @author raul.ggonzalez 14-12-2004
 * @version 30/01/2006 (david.sanchezp): modificacion de las busquedas por el tipo
 */
 public class MantenimientoMandatosForm extends MasterForm {	

	
	private String fechaFirma="";
	private String lugarFirma="";
	private String idPersona;
	private String idInstitucion;
	private FormFile fichero;
	
 	public void setFichero(FormFile valor){
 		this.fichero = valor;
 	}
 	public FormFile getFichero	() 	{ 
 		return this.fichero;		
 	}
 	
 	public void setTipoColegiacion (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos, "TipoColegiacion", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	}
 	public String getNumeroColegiado	() 	{ 
 		return UtilidadesHash.getString(this.datos, "NumeroColegiado");		
 	}

 	public String getTipoCliente () 	{ 
 		return UtilidadesHash.getString(this.datos, "TipoCliente");		
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	
	// Getters
	public String getChkPendientesFirmar(){
 		return UtilidadesHash.getString(this.datos, "ChkPendientesFirmar");
	}	
	
	// Setters	
	public void setApellido1 (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos, "Apellido1", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	}
	public void setApellido2 (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos, "Apellido2", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	}
	public void setChkPendientesFirmar(String valor){
	 	try {
 			UtilidadesHash.set(this.datos, "ChkPendientesFirmar", valor);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
	}
	public void setNif (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos, "Nif", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	}	 
	public void setNombreInstitucion (String dato) { 
		try {
 			UtilidadesHash.set(this.datos, "NombreInstitucion", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	}	 
	public void setNombrePersona (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos, "NombrePersona", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	}
 	public void setNumeroColegiado (String dato) {
 		try {
 			UtilidadesHash.set(this.datos,"NumeroColegiado", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}		
 	}	 
 	public void setTipo (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos, "Tipo", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	} 	 	
 	public void setTipoCliente (String dato) {
 		try {
 			UtilidadesHash.set(this.datos,"TipoCliente", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	} 	
	
	public String  getFechaFirma() { 
		return UtilidadesHash.getString(this.datos, "FECHAFIRMA");
	}
	public void setFechaFirma (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos, "FECHAFIRMA", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
	}
	
	public String  getLugarFirma () { 
		return UtilidadesHash.getString(this.datos, "LUGARFIRMA");
	}
	public void setLugarFirma (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos, "LUGARFIRMA", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
	}
	
	public String  getFechaFirmaInicio() { 
		return UtilidadesHash.getString(this.datos, "FECHAFIRMAINICIO");
	}
	public void setFechaFirmaInicio (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos, "FECHAFIRMAINICIO", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
	}
	
	public String  getFechaFirmaFin () { 
		return UtilidadesHash.getString(this.datos, "FECHAFIRMAFIN");
	}
	public void setFechaFirmaFin (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos, "FECHAFIRMAFIN", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
	}
	
	public String  getFechaModInicio() { 
		return UtilidadesHash.getString(this.datos, "FECHAMODINICIO");
	}
	public void setFechaModInicio (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos, "FECHAMODINICIO", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
	}
	
	public String  getValorCheck() { 
		return UtilidadesHash.getString(this.datos, "VALORCHECK");
	}
	public void setValorCheck (String dato) { 
		try {
			UtilidadesHash.set(this.datos, "VALORCHECK", dato);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String  getFechaModFin () { 
		return UtilidadesHash.getString(this.datos, "FECHAMODFIN");
	}
	public void setFechaModFin (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos, "FECHAMODFIN", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
	}
	
	public String  getTipoMandato () { 
		return UtilidadesHash.getString(this.datos, "TIPOMANDATO");
	}
	public void setTipoMandato (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos, "TIPOMANDATO", dato);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
	}

	public String  getTipoColegiado() { 
		return UtilidadesHash.getString(this.datos, "TIPOCOLEGIADO");
	}
	public void setTipoColegiado(String dato) { 
		try {
			UtilidadesHash.set(this.datos, "TIPOCOLEGIADO", dato);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String  getTipoNoColegiado() { 
		return UtilidadesHash.getString(this.datos, "TIPONOCOLEGIADO");
	}
	public void setTipoNoColegiado(String dato) { 
		try {
			UtilidadesHash.set(this.datos, "TIPONOCOLEGIADO", dato);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}