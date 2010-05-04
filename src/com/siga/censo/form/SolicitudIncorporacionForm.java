/*
 * Created on 10-sep-2004
 *
 */

package com.siga.censo.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import com.siga.Utilidades.*;
import com.siga.beans.*;
import com.siga.general.MasterForm;
import com.atos.utils.*;

/**
 * @author daniel.campos
 * @version: modificado por david.sanchezp el 27/12/2005 para incluir el campo sexo
 */
 public class SolicitudIncorporacionForm extends MasterForm {
 	
	// Metodos Set (Formulario (*.jsp))
 	public void setSexo (String dato) { 
 		this.datos.put(CenSolicitudIncorporacionBean.C_SEXO, dato);
 	}
 	public void setTipoSolicitud (Integer dato) { 
 		this.datos.put(CenSolicitudIncorporacionBean.C_IDTIPOSOLICITUD, String.valueOf(dato));
 	}
 	public void setFechaSolicitud (String dato) { 
		try {
			dato = GstDate.getApplicationFormatDate("",dato);
	 		this.datos.put(CenSolicitudIncorporacionBean.C_FECHASOLICITUD, dato);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
 	}
 	public void setTipoColegiacion (Integer dato) { 
 		this.datos.put(CenSolicitudIncorporacionBean.C_IDTIPOCOLEGIACION, String.valueOf(dato)); 		
 	}
	public void setTipoDon (Integer dato) { 
		this.datos.put(CenSolicitudIncorporacionBean.C_IDTRATAMIENTO, String.valueOf(dato));	
	}
	public void setTipoIdentificacion(Integer dato) { 
		this.datos.put(CenSolicitudIncorporacionBean.C_IDTIPOIDENTIFICACION, String.valueOf(dato));
	}
	public void setNIFCIF (String dato) { 
		this.datos.put(CenSolicitudIncorporacionBean.C_NUMEROIDENTIFICADOR, dato.toUpperCase());
	}
	public void setNombre(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_NOMBRE, dato);
	}
	public void setApellido1(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_APELLIDO1, dato);
	}
	public void setApellido2(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_APELLIDO2, dato);
	}
	public void setFechaNacimiento(String dato) 	{ 
		try {
			dato = GstDate.getApplicationFormatDate("",dato);
			this.datos.put(CenSolicitudIncorporacionBean.C_FECHANACIMIENTO, dato);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setFechaEstadoColegial(String dato) 	{ 
		try {
			if (!dato.trim().equals("")) 
			dato = GstDate.getApplicationFormatDate("",dato);
			this.datos.put(CenSolicitudIncorporacionBean.C_FECHAESTADOCOLEGIAL, dato);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setNatural(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_NATURALDE, dato);
	}
	public void setEstadoCivil(Integer dato) 		{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_IDESTADOCIVIL, String.valueOf(dato));
	}
	public void setDomicilio(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_DOMICILIO, dato);
	}
	public void setPoblacion(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_IDPOBLACION, dato);
	}
	public void setPoblacionExt(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_POBLACIONEXTRANJERA, dato);
	}
	public void setCP(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_CODIGOPOSTAL, dato);
	}
	public void setProvincia(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_IDPROVINCIA, dato);
	}
	public void setTelefono1(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_TELEFONO1, dato);
	}
	public void setTelefono2(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_TELEFONO2, dato);
	}
	public void setTelefono3(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_MOVIL, dato);
	}
	public void setFax1(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_FAX1, dato);
	}
	public void setFax2(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_FAX2, dato);
	}
	public void setMail(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_CORREOELECTRONICO, dato);
	}
	public void setObservaciones(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_OBSERVACIONES, dato);
	}
	public void setPais(String dato) 	{ 
		this.datos.put(CenSolicitudIncorporacionBean.C_IDPAIS, dato);
	}
	
	// Metodos Get 1 por campo Formulario (*.jsp)
 	public Integer getTipoSolicitud		() 	{ 
 		return UtilidadesHash.getInteger(this.datos, CenSolicitudIncorporacionBean.C_IDTIPOSOLICITUD);		
 	}
 	public String getFechaSolicitud	() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_FECHASOLICITUD);
 	}
 	public Integer getTipoColegiacion	() 	{ 
 		return UtilidadesHash.getInteger(this.datos, CenSolicitudIncorporacionBean.C_IDTIPOCOLEGIACION);
 	}
	public Integer getTipoDon			() 	{ 
 		return UtilidadesHash.getInteger(this.datos, CenSolicitudIncorporacionBean.C_IDTRATAMIENTO);
	}
	public Integer getTipoIdentificacion()	{ 
 		return UtilidadesHash.getInteger(this.datos, CenSolicitudIncorporacionBean.C_IDTIPOIDENTIFICACION);
	}
	public String getNIFCIF			() 	{ 
 		return String.valueOf(this.datos.get(CenSolicitudIncorporacionBean.C_NUMEROIDENTIFICADOR)).trim();
 	}
	public String getNombre			() 	{ 
 		return String.valueOf(this.datos.get(CenSolicitudIncorporacionBean.C_NOMBRE)).trim();
 	}
	public String getApellido1		() 	{ 
 		return String.valueOf(this.datos.get(CenSolicitudIncorporacionBean.C_APELLIDO1)).trim();
 	}
	public String getApellido2		() 	{ 
 		return String.valueOf(this.datos.get(CenSolicitudIncorporacionBean.C_APELLIDO2)).trim();
 	}
	public String getFechaNacimiento() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_FECHANACIMIENTO);
 	}
	public String getFechaEstadoColegial() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_FECHAESTADOCOLEGIAL);
 	}
	public String getNatural		() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_NATURALDE);
 	}
	public Integer getEstadoCivil		() 	{ 
 		return UtilidadesHash.getInteger(this.datos, CenSolicitudIncorporacionBean.C_IDESTADOCIVIL);
	}
	public String getDomicilio		() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_DOMICILIO);
 	}
	public String getPoblacion		() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_IDPOBLACION);
 	}
	public String getPoblacionExt		() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_POBLACIONEXTRANJERA);
 	}
	public String getCP				() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_CODIGOPOSTAL);
 	}
	public String getProvincia		() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_IDPROVINCIA);
 	}
	public String getTelefono1		() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_TELEFONO1);
 	}
	public String getTelefono2		() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_TELEFONO2);
 	}
	public String getTelefono3		() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_MOVIL);
 	}
	public String getFax1			() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_FAX1);
 	}
	public String getFax2			() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_FAX2);
 	}
	public String getMail			() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_CORREOELECTRONICO);
 	}
	public String getObservaciones	() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_OBSERVACIONES);
 	}
	public String getPais	() 	{ 
 		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_IDPAIS);
 	}

	public boolean getResidente() {
		return UtilidadesHash.getBoolean(this.datos, CenSolicitudIncorporacionBean.C_RESIDENTE);
	}
	
	// Ventana de validacion  /////////////////////////////////////////////
	public void setClave	(String dato) 	{	
		UtilidadesHash.set(this.datos, CenSolicitudIncorporacionBean.C_IDSOLICITUD, dato);	
	}
	public String getClave	() 	{	
		return UtilidadesHash.getString(this.datos, CenSolicitudIncorporacionBean.C_IDSOLICITUD);	
	}
	///////////////////////////////////////////////////////////////////////
	
	
	// Ventana de busqueda  ///////////////////////////////////////////////
 	public void setBuscarTipoSolicitud (Integer dato) { 
 		UtilidadesHash.set(datos, "Buscar_" + CenSolicitudIncorporacionBean.C_IDTIPOSOLICITUD, dato);
 	}
 	public void setBuscarEstadoSolicitud (Integer dato) { 
 		UtilidadesHash.set(datos, "Buscar_" + CenSolicitudIncorporacionBean.C_IDESTADO, dato);
 	}
 	public void setBuscarVerAlarmas (Boolean dato) { 
 		UtilidadesHash.set(datos, "Buscar_VerAlarmas", dato);
 	}
 	public void setBuscarFechaDesde (String dato) { 
 		try {
 			if (!dato.trim().equals("")) 
 				dato = GstDate.getApplicationFormatDate("",dato);
 			UtilidadesHash.set(datos, "Buscar_FechaDesde", dato);
 		}
 		catch (Exception e) {
 			e.printStackTrace();			
 		}
 	}
 	public void setBuscarFechaHasta (String dato) { 
 		try {
 			if (!dato.trim().equals("")) 
 				dato = GstDate.getApplicationFormatDate("",dato);
 			UtilidadesHash.set(datos, "Buscar_FechaHasta", dato);
 		}
 		catch (Exception e) {
 			e.printStackTrace();			
 		}
	}
 	public void setBuscarModoAnteriorBusqueda (Boolean dato) { 
 		UtilidadesHash.set(datos, "Buscar_ModoAnteriorBusqueda", dato);
	}
 	

 	public Integer getBuscarTipoSolicitud () { 
 		return UtilidadesHash.getInteger(datos, "Buscar_" + CenSolicitudIncorporacionBean.C_IDTIPOSOLICITUD);
 	}
 	public Integer getBuscarEstadoSolicitud () { 
 		return UtilidadesHash.getInteger(datos, "Buscar_" + CenSolicitudIncorporacionBean.C_IDESTADO);
 	}
 	public Boolean getBuscarVerAlarmas () { 
 		return UtilidadesHash.getBoolean(datos, "Buscar_VerAlarmas");
 	}
 	public String getBuscarFechaDesde () { 
 		return UtilidadesHash.getString(datos, "Buscar_FechaDesde");
 	}
 	public String getBuscarFechaHasta () { 
 		return UtilidadesHash.getString(datos, "Buscar_FechaHasta");
	}
 	public Boolean getBuscarModoAnteriorBusqueda () {
 		return UtilidadesHash.getBoolean(datos, "Buscar_ModoAnteriorBusqueda");
	}
 	public String getSexo () {
 		return UtilidadesHash.getString(datos, CenSolicitudIncorporacionBean.C_SEXO);
	}
 	///////////////////////////////////////////////////////////////////////

 	// Ventana de Edicion /////////////////////////////////////////////////
 	public void setEditarIdSolicitud (Long dato) {
 		UtilidadesHash.set(datos, "Editar_" + CenSolicitudIncorporacionBean.C_IDSOLICITUD, dato);
 	}
 	public void setEditarEstadoSolicitud (Integer dato) {
 		UtilidadesHash.set(datos, "Editar_" + CenSolicitudIncorporacionBean.C_IDESTADO, dato);
 	}
 	
 	public Long getEditarIdSolicitud () {
 		return UtilidadesHash.getLong(datos, "Editar_" + CenSolicitudIncorporacionBean.C_IDSOLICITUD);
 	}
 	public Integer getEditarEstadoSolicitud () {
 		return UtilidadesHash.getInteger(datos, "Editar_" + CenSolicitudIncorporacionBean.C_IDESTADO);
 	}

	///////////////////////////////////////////////////////////////////////
	
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		try {
			super.reset(mapping, request);
			this.setBuscarVerAlarmas(new Boolean (false));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Para establecer si vamos a modal o no:
	public void setEsModal(String dato) { 
		this.datos.put("ESMODAL", dato);
	}
	public String getEsModal() { 
 		return (String)this.datos.get("ESMODAL");
 	}
	

	public void setTipoModalidadDocumentacion(Integer dato) { 
		UtilidadesHash.set(this.datos, "TIPO_MODALIDAD_DOCUMENTACION", dato);
	}
	public Integer getTipoModalidadDocumentacion() { 
 		return UtilidadesHash.getInteger(this.datos, "TIPO_MODALIDAD_DOCUMENTACION");		
 	}
	
	public String getNumeroColegiado(){
		return (String)this.datos.get(CenSolicitudIncorporacionBean.C_NCOLEGIADO);
	}
	public void setNumeroColegiado(String dato){
		UtilidadesHash.set(this.datos, CenSolicitudIncorporacionBean.C_NCOLEGIADO, dato);
	}
	
	
	//////////////////////////////// Metemos los datos bancarios para no obligar a hacerlo luego
	
	public void setCuentaAbono(Boolean dato) {
		UtilidadesHash.set(this.datos, "CUENTA_ABONO", dato);
	}
	
	public void setCuentaCargo(Boolean dato) {
		UtilidadesHash.set(this.datos, "CUENTA_CARGO", dato);
	}
	
	public void setTitular(String titular) {	
		this.datos.put(CenCuentasBancariasBean.C_TITULAR, titular);
	}
	
	public void setAbonoCargo(Boolean abonoCargo) {		
		UtilidadesHash.set(this.datos, CenCuentasBancariasBean.C_ABONOCARGO, abonoCargo); 
	}
	
	public void setAbonoSJCS(Boolean abonoSJCS) {
		UtilidadesHash.set(this.datos, CenCuentasBancariasBean.C_ABONOSJCS, abonoSJCS);
	}
	
	public void setCuentaContable(String cuentaContable) {
		this.datos.put(CenCuentasBancariasBean.C_CUENTACONTABLE, cuentaContable);
	}
	
	public void setCbo_Codigo(String cbo_Codigo) {
		this.datos.put(CenCuentasBancariasBean.C_CBO_CODIGO, cbo_Codigo);
	}
	
	public void setCodigoSucursal(String codigoSucursal) {
		this.datos.put(CenCuentasBancariasBean.C_CODIGOSUCURSAL, codigoSucursal);
	}
	
	public void setDigitoControl(String digitoControl) {
		this.datos.put(CenCuentasBancariasBean.C_DIGITOCONTROL, digitoControl);
	}
	
	public void setNumeroCuenta(String numeroCuenta) {
		this.datos.put(CenCuentasBancariasBean.C_NUMEROCUENTA, numeroCuenta);
	}
	
	public void setResidente(boolean residente) {
		this.datos.put(CenSolicitudIncorporacionBean.C_RESIDENTE, residente);
	}
	
	

	
//	metodos get de los campos del formulario
	public Boolean getCuentaAbono() {
		return UtilidadesHash.getBoolean(this.datos, "CUENTA_ABONO");
	}
	
	public Boolean getCuentaCargo() {
		return UtilidadesHash.getBoolean(this.datos, "CUENTA_CARGO");
	}
	
	public String getTitular() {
		return (String)this.datos.get(CenCuentasBancariasBean.C_TITULAR);
	}
	
	public Boolean getAbonoCargo() {
		return UtilidadesHash.getBoolean(this.datos, CenCuentasBancariasBean.C_ABONOCARGO);
	}	

	public Boolean getAbonoSJCS() {
		return UtilidadesHash.getBoolean(this.datos, CenCuentasBancariasBean.C_ABONOSJCS);
	}
			
	public String getCuentaContable() {
		return (String)this.datos.get(CenCuentasBancariasBean.C_CUENTACONTABLE);
	}
	
	public String getCbo_Codigo() {
		return (String)this.datos.get(CenCuentasBancariasBean.C_CBO_CODIGO);
	}	
	
	public String getCodigoSucursal() {
		return (String)this.datos.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL);
	}
	
	public String getDigitoControl() {
		return (String)this.datos.get(CenCuentasBancariasBean.C_DIGITOCONTROL);
	}
	
	public String getNumeroCuenta() {
		return (String)this.datos.get(CenCuentasBancariasBean.C_NUMEROCUENTA);
	}

	
	
}