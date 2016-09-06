/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creaci�n
 */

/**
 * Tiene tantos metodos set y get por cada uno de los campos de la tabla 
 */

package com.siga.facturacion.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.FacPlantillaFacturacionBean;
import com.siga.beans.FacSerieFacturacionBean;
import com.siga.general.MasterForm;

public class DatosGeneralesForm extends MasterForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5641929929812522853L;
	private String idPlantillaMail, idTipoPlantillaMail, idSerieFacturacionPrevia, fechaBaja;
	private String[] formaPagoAutom�tica;

	// Metodos Set (Formulario (*.jsp))
	public void setIdInstitucion (Integer id) {
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDINSTITUCION, id);
 	}
	
	public void setIdSerieFacturacion (Long id) { 
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION, id);
  	}
	
	public void setIdPlantilla (Integer id) { 
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDPLANTILLA, id);
 	}

	/*public void setIdTipoPlantillaMail (String id) { 
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDTIPOPLANTILLAMAIL, id);
 	}
	
	public void setIdPlantillaMail (String id) { 
		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDPLANTILLAMAIL, id);
 	}*/
	
	public void setPlantilla (String dato) {
 		UtilidadesHash.set(datos, FacPlantillaFacturacionBean.C_DESCRIPCION, dato);
 	}
	
 	public void setNombreAbreviado (String dato) {
 		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_NOMBREABREVIADO, dato.toUpperCase());
 	}
 	
 	public void setIdNombreDescargaPDF (String dato) {
 		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDNOMBREDESCARGAPDF, dato.toUpperCase());
 	}
 	
 	public void setDescripcion (String dato) { 
 		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_DESCRIPCION, dato);
 	}
 	
 	public void setObservaciones (String dato) { 
 		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_OBSERVACIONES, dato);
 	}
 	
 	public void setIdContador (String dato) { 
 		UtilidadesHash.set(datos, "form_idContador", dato);
 	}
 	
 	public void setPrefijo (String dato) { 
 		UtilidadesHash.set(datos, "form_prefijo", dato);
 	}
 	
 	public void setContador (String dato) { 
 		UtilidadesHash.set(datos, "form_contador", dato);
 	}
 	
 	public void setSufijo (String dato) { 
 		UtilidadesHash.set(datos, "form_sufijo", dato);
 	}
 	
 	public void setConfigurarContador (String dato) { 
 		UtilidadesHash.set(datos, "form_configurarContador", dato);
 	}

 	public void setContadorExistente (String dato) { 
 		UtilidadesHash.set(datos, "contadorExistente", dato);
 	}

 	public void setPrefijo_nuevo (String dato) { 
 		UtilidadesHash.set(datos, "form_prefijo_nuevo", dato);
 	}
 	
 	public void setContador_nuevo (String dato) { 
 		UtilidadesHash.set(datos, "form_contador_nuevo", dato);
 	}
 	
 	public void setSufijo_nuevo (String dato) { 
 		UtilidadesHash.set(datos, "form_sufijo_nuevo", dato);
 	}
 	

 	// Metodos Get 1 por campo Formulario (*.jsp)
 	public Integer getIdInstitucion	() {
 		return UtilidadesHash.getInteger(datos, FacSerieFacturacionBean.C_IDINSTITUCION);
 	}
 	
 	public Long getIdSerieFacturacion	() {
 		return UtilidadesHash.getLong(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION);
 	}
 	
 	public Integer getIdPlantilla	() {
 		return UtilidadesHash.getInteger(datos, FacSerieFacturacionBean.C_IDPLANTILLA);	
 	}
 	
 	/* public String getIdTipoPlantillaMail	() {
 		return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_IDTIPOPLANTILLAMAIL);	
 	}
 	 	
 	 public String getIdPlantillaMail	() {
 		return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_IDPLANTILLAMAIL);	
 	}*/
 	
 	public String getPlantilla	() 	{ 
 		return UtilidadesHash.getString(datos, FacPlantillaFacturacionBean.C_DESCRIPCION);
 	}
 	
 	public String getNombreAbreviado	() 	{ 
 		return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_NOMBREABREVIADO);
 	}
 	
	public String getIdNombreDescargaPDF	() 	{ 
 		return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_IDNOMBREDESCARGAPDF);
 	}
 	
 	public String getDescripcion	() 	{ 
 		return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_DESCRIPCION);
 	}
 	
 	public String getObservaciones	() 	{ 
 		return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_OBSERVACIONES);
 	}
 	
 	public void setTipoSerie (String d) {
 		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_TIPOSERIE, d);
 	}
 	
 	public String getTipoSerie () { 
 		return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_TIPOSERIE);
 	}
 	
 	public void setGenerarPDF	(String d) 	{
 		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_GENERARPDF, d);
 	}
 	
 	public String getGenerarPDF	() 	{ 
 		return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_GENERARPDF);
 	}
 	
 	public void setEnvioFacturas	(String d) 	{
 		UtilidadesHash.set(datos, FacSerieFacturacionBean.C_ENVIOFACTURA, d);
 	}
 	
 	public String getEnvioFacturas	() 	{ 
 		return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_ENVIOFACTURA);
 	}

 	public String getIdContador () { 
 		return UtilidadesHash.getString(datos, "form_idContador");
 	}
 	
 	public String getPrefijo () { 
 		return UtilidadesHash.getString(datos, "form_prefijo");
 	}
 	
 	public String getContador () { 
 		return UtilidadesHash.getString(datos, "form_contador");
 	}
 	
 	public String getSufijo () { 
 		return UtilidadesHash.getString(datos, "form_sufijo");
 	}
 	
 	public String getConfigurarContador () { 
 		return UtilidadesHash.getString(datos, "form_configurarContador");
 	}

 	public String getContadorExistente () { 
 		return UtilidadesHash.getString(datos, "contadorExistente");
 	}

 	public String getPrefijo_nuevo () { 
 		return UtilidadesHash.getString(datos, "form_prefijo_nuevo");
 	}
 	
 	public String getContador_nuevo () { 
 		return UtilidadesHash.getString(datos, "form_contador_nuevo");
 	}
 	
 	public String getSufijo_nuevo () { 
 		return UtilidadesHash.getString(datos, "form_sufijo_nuevo");
 	}

	public String getIdPlantillaMail() {
		return idPlantillaMail;
	}

	public void setIdPlantillaMail(String idPlantillaMail) {
		this.idPlantillaMail = idPlantillaMail;
	}

	public String getIdTipoPlantillaMail() {
		return idTipoPlantillaMail;
	}

	public void setIdTipoPlantillaMail(String idTipoPlantillaMail) {
		this.idTipoPlantillaMail = idTipoPlantillaMail;
	}

	public String[] getFormaPagoAutom�tica() {

		return this.formaPagoAutom�tica;
	}

	public void setFormaPagoAutom�tica(String[] formaPagoAutom�tica) {
		this.formaPagoAutom�tica = formaPagoAutom�tica;
	}

	public String getIdSerieFacturacionPrevia() {
		return idSerieFacturacionPrevia;
	}

	public void setIdSerieFacturacionPrevia(String idSerieFacturacionPrevia) {
		this.idSerieFacturacionPrevia = idSerieFacturacionPrevia;
	}

	public String getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
}
