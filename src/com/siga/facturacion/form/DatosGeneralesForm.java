/*
 * VERSIONES:
 * yolanda.garcia - 15-11-2004 - Creación
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
	private String[] formaPagoAutomática;

	// Metodos Set (Formulario (*.jsp))
	public void setIdInstitucion (Integer id) { UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDINSTITUCION, id);	}
	public void setIdSerieFacturacion (Long id) { UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION, id); }
	public void setIdPlantilla (Integer id) { UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDPLANTILLA, id); }
	public void setPlantilla (String dato) { UtilidadesHash.set(datos, FacPlantillaFacturacionBean.C_DESCRIPCION, dato); }
 	public void setNombreAbreviado (String dato) { UtilidadesHash.set(datos, FacSerieFacturacionBean.C_NOMBREABREVIADO, dato.toUpperCase()); }
 	public void setIdNombreDescargaPDF (String dato) { UtilidadesHash.set(datos, FacSerieFacturacionBean.C_IDNOMBREDESCARGAPDF, dato.toUpperCase()); }
 	public void setDescripcion (String dato) {  UtilidadesHash.set(datos, FacSerieFacturacionBean.C_DESCRIPCION, dato); }
 	public void setObservaciones (String dato) {  UtilidadesHash.set(datos, FacSerieFacturacionBean.C_OBSERVACIONES, dato); }
 	public void setTipoSerie (String d) { UtilidadesHash.set(datos, FacSerieFacturacionBean.C_TIPOSERIE, d); }
 	public void setGenerarPDF	(String d) 	{ UtilidadesHash.set(datos, FacSerieFacturacionBean.C_GENERARPDF, d); }
 	public void setTraspasoFacturas(String d) { UtilidadesHash.set(datos, FacSerieFacturacionBean.C_TRASPASOFACTURAS, d); }
 	public void setPlantillaTraspasoFacturas(String d) { UtilidadesHash.set(datos, FacSerieFacturacionBean.C_TRASPASOPLANTILLA, d); }
 	public void setPlantillaTraspasoAuditoria(String d) { UtilidadesHash.set(datos, FacSerieFacturacionBean.C_TRASPASOCODAUDITORIADEF, d); }
 	public void setEnvioFacturas	(String d) 	{ UtilidadesHash.set(datos, FacSerieFacturacionBean.C_ENVIOFACTURA, d); }
	public void setIdPlantillaMail(String idPlantillaMail) { this.idPlantillaMail = idPlantillaMail; }
	public void setIdTipoPlantillaMail(String idTipoPlantillaMail) { this.idTipoPlantillaMail = idTipoPlantillaMail; } 
	public void setFormaPagoAutomática(String[] formaPagoAutomática) { this.formaPagoAutomática = formaPagoAutomática; }
	public void setIdSerieFacturacionPrevia(String idSerieFacturacionPrevia) { this.idSerieFacturacionPrevia = idSerieFacturacionPrevia; } 
	public void setFechaBaja(String fechaBaja) { this.fechaBaja = fechaBaja; }

 	public void setIdContador (String dato) {  UtilidadesHash.set(datos, "form_idContador", dato); }
 	public void setPrefijo (String dato) {  UtilidadesHash.set(datos, "form_prefijo", dato); }
 	public void setContador (String dato) {  UtilidadesHash.set(datos, "form_contador", dato); }
 	public void setSufijo (String dato) {  UtilidadesHash.set(datos, "form_sufijo", dato); }
 	public void setConfigurarContador (String dato) {  UtilidadesHash.set(datos, "form_configurarContador", dato); }
 	public void setContadorExistente (String dato) {  UtilidadesHash.set(datos, "contadorExistente", dato); }
 	public void setPrefijo_nuevo (String dato) {  UtilidadesHash.set(datos, "form_prefijo_nuevo", dato); }
 	public void setContador_nuevo (String dato) {  UtilidadesHash.set(datos, "form_contador_nuevo", dato); }
 	public void setSufijo_nuevo (String dato) {  UtilidadesHash.set(datos, "form_sufijo_nuevo", dato); }
 	public void setIdContadorAbonos (String dato) {  UtilidadesHash.set(datos, "form_idContadorAbonos", dato); }
 	public void setPrefijoAbonos (String dato) {  UtilidadesHash.set(datos, "form_prefijoAbonos", dato); }
 	public void setContadorAbonos (String dato) {  UtilidadesHash.set(datos, "form_contadorAbonos", dato); }
 	public void setSufijoAbonos (String dato) {  UtilidadesHash.set(datos, "form_sufijoAbonos", dato); }
 	public void setConfigurarContadorAbonos (String dato) {  UtilidadesHash.set(datos, "form_configurarContadorAbonos", dato); }
 	public void setContadorExistenteAbonos (String dato) {  UtilidadesHash.set(datos, "contadorExistenteAbonos", dato); }
 	public void setPrefijo_nuevo_abonos (String dato) {  UtilidadesHash.set(datos, "form_prefijo_nuevo_abonos", dato); }
 	public void setContador_nuevo_abonos (String dato) {  UtilidadesHash.set(datos, "form_contador_nuevo_abonos", dato); }
 	public void setSufijo_nuevo_abonos (String dato) {  UtilidadesHash.set(datos, "form_sufijo_nuevo_abonos", dato); }


 	// Metodos Get 1 por campo Formulario (*.jsp)
 	public Integer getIdInstitucion	() { return UtilidadesHash.getInteger(datos, FacSerieFacturacionBean.C_IDINSTITUCION); }
 	public Long getIdSerieFacturacion	() { return UtilidadesHash.getLong(datos, FacSerieFacturacionBean.C_IDSERIEFACTURACION); }
 	public Integer getIdPlantilla	() { return UtilidadesHash.getInteger(datos, FacSerieFacturacionBean.C_IDPLANTILLA);	 }
 	public String getPlantilla	() 	{  return UtilidadesHash.getString(datos, FacPlantillaFacturacionBean.C_DESCRIPCION); }
 	public String getNombreAbreviado	() 	{  return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_NOMBREABREVIADO); }
	public String getIdNombreDescargaPDF	() 	{  return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_IDNOMBREDESCARGAPDF); }
 	public String getDescripcion	() 	{  return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_DESCRIPCION); }
 	public String getObservaciones	() 	{  return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_OBSERVACIONES); }
 	public String getTipoSerie () {  return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_TIPOSERIE); }
 	public String getGenerarPDF	() 	{  return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_GENERARPDF); }
 	public String getTraspasoFacturas() { return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_TRASPASOFACTURAS); }
 	public String getPlantillaTraspasoFacturas() { return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_TRASPASOPLANTILLA); }
 	public String getPlantillaTraspasoAuditoria() { return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_TRASPASOCODAUDITORIADEF); }
 	public String getEnvioFacturas	() 	{  return UtilidadesHash.getString(datos, FacSerieFacturacionBean.C_ENVIOFACTURA); }
	public String getIdPlantillaMail() { return idPlantillaMail; }
	public String getIdTipoPlantillaMail() { return idTipoPlantillaMail; }
	public String[] getFormaPagoAutomática() { return this.formaPagoAutomática; } 
	public String getIdSerieFacturacionPrevia() { return idSerieFacturacionPrevia; } 
	public String getFechaBaja() { return fechaBaja; }

 	public String getIdContador () {  return UtilidadesHash.getString(datos, "form_idContador"); }
 	public String getPrefijo () {  return UtilidadesHash.getString(datos, "form_prefijo"); }
 	public String getContador () {  return UtilidadesHash.getString(datos, "form_contador"); }
 	public String getSufijo () {  return UtilidadesHash.getString(datos, "form_sufijo"); }
 	public String getConfigurarContador () {  return UtilidadesHash.getString(datos, "form_configurarContador"); }
 	public String getContadorExistente () {  return UtilidadesHash.getString(datos, "contadorExistente"); }
 	public String getPrefijo_nuevo () {  return UtilidadesHash.getString(datos, "form_prefijo_nuevo"); }
 	public String getContador_nuevo () {  return UtilidadesHash.getString(datos, "form_contador_nuevo"); }
 	public String getSufijo_nuevo () {  return UtilidadesHash.getString(datos, "form_sufijo_nuevo"); }
 	public String getIdContadorAbonos () {  return UtilidadesHash.getString(datos, "form_idContadorAbonos"); }
 	public String getPrefijoAbonos () {  return UtilidadesHash.getString(datos, "form_prefijoAbonos"); }
 	public String getContadorAbonos () {  return UtilidadesHash.getString(datos, "form_contadorAbonos"); }
 	public String getSufijoAbonos () {  return UtilidadesHash.getString(datos, "form_sufijoAbonos"); }
 	public String getConfigurarContadorAbonos () {  return UtilidadesHash.getString(datos, "form_configurarContadorAbonos"); }
 	public String getContadorExistenteAbonos () {  return UtilidadesHash.getString(datos, "contadorExistenteAbonos"); }
 	public String getPrefijo_nuevo_abonos () {  return UtilidadesHash.getString(datos, "form_prefijo_nuevo_abonos"); }
 	public String getContador_nuevo_abonos () {  return UtilidadesHash.getString(datos, "form_contador_nuevo_abonos"); }
 	public String getSufijo_nuevo_abonos () {  return UtilidadesHash.getString(datos, "form_sufijo_nuevo_abonos"); }

}
