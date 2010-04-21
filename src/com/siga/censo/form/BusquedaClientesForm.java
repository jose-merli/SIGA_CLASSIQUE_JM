package com.siga.censo.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * Clase action form del caso de uso BUSCAR CLIENTE
 * @author raul.ggonzalez 14-12-2004
 * @version 30/01/2006 (david.sanchezp): modificacion de las busquedas por el tipo
 */
 public class BusquedaClientesForm extends MasterForm {
 	
	// Metodos Set (Formulario (*.jsp))

	// BLOQUE PARA EL FORMULARIO DE BUSQUEDA SIMPLE 

 	
 
	String fechaNacimientoDesde="", fechaNacimientoHasta="", fechaIncorporacionDesde="", fechaIncorporacionHasta="",fechaAltaHasta="",fechaAltaDesde="";
 	private String solBusqueda="", codigo = "";
 	String permitirAniadirNuevo= "N";
 	String idPersona=  null;
 	String idInstitucion;
 	private String verFichaLetrado;

	

 	
 	/**
	 * Almacena en la Hashtable el tipo para el tagBusquedaPersona 
	 * 
	 * @param  
	 * @return void 
	 */
	public void setTipoBus				(String tipoBus)				{ this.datos.put("TIPOBUS", tipoBus);						}
 	/**
	 * Almacena en la Hashtable el numeronif para el tagBusquedaPersona 
	 * 
	 * @param  
	 * @return void 
	 */
	public void setNumeroNif				(String numeroNif)				{ this.datos.put("NUMERONIF", numeroNif);						}
	/**
	 * Recupera de la Hashtable el tipo para el tagBusquedaPersona 
	 * @return String 
	 */
	public String getTipoBus() { 
		return (this.datos.get("TIPOBUS")!=null)?this.datos.get("TIPOBUS").toString():"";				
	}
	
	/**
	 * Recupera de la Hashtable el numeronif para el tagBusquedaPersona 
	 * @return String
	 */
	public String  getNumeroNif					()							{ return this.datos.get("NUMERONIF").toString();	}
 	
 	
 	public void setTipo (String dato) { 
 		UtilidadesHash.set(this.datos,"Tipo", dato);
 	} 	
 	
 	public void setNombreInstitucion (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"NombreInstitucion", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public String  getChkBusqueda(){
 		return UtilidadesHash.getString(this.datos, "ChkBusqueda");
	 }
	 public void setChkBusqueda(String valor){
	 	UtilidadesHash.set(this.datos, "ChkBusqueda", valor);
	 }
	 public String  getValorCheck(){
 		return UtilidadesHash.getString(this.datos, "ValorCheck");
	 }
	 public void setValorCheck(String valor){
	 	UtilidadesHash.set(this.datos, "ValorCheck", valor);
	 }
 	public void setNumeroColegiado (String dato) { 
 		try {
 			// lo meto en una clave auxiliar para luego tratarlo
 			// puesto que va a ir a un bean o a otro en funcion de otro valor
 			UtilidadesHash.set(this.datos,"NumeroColegiado", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setTipoCliente (String dato) { 
 		try {
 			// lo meto en una clave auxiliar para luego tratarlo
 			// puesto que va a ir a un bean o a otro en funcion de otro valor
 			UtilidadesHash.set(this.datos,"TipoCliente", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setNif (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Nif", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setNombrePersona (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"NombrePersona", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setApellido1 (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Apellido1", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

 	public void setApellido2 (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Apellido2", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}

	// BLOQUE PARA EL FORMULARIO DE BUSQUEDA AVANZADA 

 	public void setGrupoClientes (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"GrupoClientes", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setFechaNacimiento (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"FechaNacimiento", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	
 	public void setSexo (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Sexo", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public void setTipoApunte (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"TipoApunte", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public void setIdTipoCVSubtipo1 (String dato) {
 		try{
 			UtilidadesHash.set(this.datos,"IdTipoCVSubtipo1", dato);
 		} catch (Exception e) {
 			// escribamos la traza de momento
 			e.printStackTrace();
 		}
 		
 		
 	}
 	public void setIdTipoCVSubtipo2 (String dato) {
 		try{
 			UtilidadesHash.set(this.datos,"IdTipoCVSubtipo2", dato);
 		} catch (Exception e) {
 			// escribamos la traza de momento
 			e.printStackTrace();
 		}
 		
 		
 	}
 	public void setTipoColegiacion (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"TipoColegiacion", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public void setComision (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Comision", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public void setDomicilio (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Domicilio", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setCodigoPostal (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"CodigoPostal", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setTelefono (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Telefono", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setFax (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Fax", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setCorreo (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Correo", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setEjerciente (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Ejerciente", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setFechaIncorporacion (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"FechaIncorporacion", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setResidente (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Residente", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	
 	public void setComunitario (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Comunitario", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setFechaAlta (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"FechaAlta", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setConcepto (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Concepto", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setColegiado (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Colegiado", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	
 	public void setAvanzada (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Avanzada", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
	

	// Metodos Get 1 por campo Formulario (*.jsp)
 	
	// BLOQUE PARA EL FORMULARIO DE BUSQUEDA SIMPLE 

 	public String getTipo () 	{ 
 		return UtilidadesHash.getString(this.datos, "Tipo");		
 	}
 	
 	public String getNombreInstitucion	() 	{ 
 		return UtilidadesHash.getString(this.datos, "NombreInstitucion");		
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

	// BLOQUE PARA EL FORMULARIO DE BUSQUEDA AVANZADA 

 	public String getGrupoClientes	() 	{ 
 		return UtilidadesHash.getString(this.datos, "GrupoClientes");		
 	}

 	public String getFechaNacimiento	() 	{ 
 		return UtilidadesHash.getString(this.datos, "FechaNacimiento");		
 	}
 	public String getFechaNacimientoHasta() {
		
 		return UtilidadesHash.getString(this.datos, "FechaNacimientoHasta");	
	}
	public void setFechaNacimientoHasta(String fechaNacimientoHasta) {
		
		try {
 			UtilidadesHash.set(this.datos,"FechaNacimientoHasta", fechaNacimientoHasta);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
	}
	public String getFechaNacimientoDesde() {
		
		return UtilidadesHash.getString(this.datos, "FechaNacimientoDesde");
	}
	public void setFechaNacimientoDesde(String fechaNacimientoDesde) {
		
		try {
 			UtilidadesHash.set(this.datos,"FechaNacimientoDesde", fechaNacimientoDesde);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
	}
	
	public String getFechaIncorporacionDesde	() 	{ 
		return UtilidadesHash.getString(this.datos, "FechaIncorporacionDesde");
 	}
	public void setFechaIncorporacionDesde(String fechaIncorporacionDesde) {
		try {
 			UtilidadesHash.set(this.datos,"FechaIncorporacionDesde", fechaIncorporacionDesde);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
	}
	
	public String getFechaAltaDesde	() 	{ 
 		return fechaAltaDesde;	
 	}
	public void setFechaAltaDesde(String fechaAltaDesde) {
		this.fechaAltaDesde = fechaAltaDesde;
	}
	public String getFechaAltaHasta	() 	{ 
 		return fechaAltaHasta;	
 	}
	public void setFechaAltaHasta(String fechaAltaHasta) {
		this.fechaAltaHasta = fechaAltaHasta;
	}
	public String getFechaIncorporacionHasta	() 	{ 
 		return fechaIncorporacionHasta;	
 	}
	public void setFechaIncorporacionHasta(String fechaIncorporacionHasta) {
		this.fechaIncorporacionHasta = fechaIncorporacionHasta;
	}
 	public String getSexo	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Sexo");		
 	}

 	public String getDomicilio	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Domicilio");		
 	}

 	public String getCodigoPostal	() 	{ 
 		return UtilidadesHash.getString(this.datos, "CodigoPostal");		
 	}

 	public String getTelefono	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Telefono");		
 	}

 	public String getFax	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Fax");		
 	}

 	public String getCorreo	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Correo");		
 	}

 	public String getEjerciente	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Ejerciente");		
 	}

 	public String getFechaIncorporacion	() 	{ 
 		return UtilidadesHash.getString(this.datos, "FechaIncorporacion");		
 	}

 	public String getResidente	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Residente");		
 	}
 	
 	public String getComunitario	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Comunitario");		
 	}

 	public String getFechaAlta	() 	{ 
 		return UtilidadesHash.getString(this.datos, "FechaAlta");		
 	}

 	public String getConcepto	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Concepto");		
 	}

 	public String getColegiado	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Colegiado");		
 	}

 	public String getAvanzada	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Avanzada");		
 	}
 	
 	public String getTipoApunte	() 	{ 
 		return UtilidadesHash.getString(this.datos, "TipoApunte");		
 	}
 	public String getIdTipoCVSubtipo1 () {
 		return UtilidadesHash.getString(this.datos, "IdTipoCVSubtipo1"); 
 	}
 	public String getIdTipoCVSubtipo2 () {
 		return UtilidadesHash.getString(this.datos, "IdTipoCVSubtipo2");
 	}

 	public String getComision	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Comision");		
 	}
 	
 	public void setLetrado (String dato) { 
 		try {
 			UtilidadesHash.set(this.datos,"Letrado", dato);
 		} catch (Exception e) {
 			// escribimos la traza de momento
 			e.printStackTrace();
 		}
 	}
 	public String getLetrado	() 	{ 
 		return UtilidadesHash.getString(this.datos, "Letrado");		
 	}
 	public String getTipoColegiacion	() 	{ 
 		return UtilidadesHash.getString(this.datos, "TipoColegiacion");		
 	}

	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
			String colegiadoAux = this.getColegiado();
			super.reset(mapping, request);
			this.setColegiado(colegiadoAux);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getPermitirAniadirNuevo() {
		return permitirAniadirNuevo;
	}
	public void setPermitirAniadirNuevo(String permitirAniadirNuevo) {
		this.permitirAniadirNuevo = permitirAniadirNuevo;
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
	
	public String getVerFichaLetrado(){
		return this.verFichaLetrado;
	}
	public void setVerFichaLetrado(String verFichaLetrado){
		this.verFichaLetrado = verFichaLetrado;
	}
}