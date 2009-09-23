//VERSIONES
//ruben.fernandez : 04/04/2005 Creacion
//

package com.siga.facturacionSJCS.form;


import com.siga.general.MasterForm;

public class DatosDetallePagoForm extends MasterForm {

	private String idPersona 	   = "IDPERSONA";
	private String idPago 		   = "IDPAGOSJG";
	private String idInstitucion   = "IDINSTITUCION";
	private String importeTotal    = "IMPORTETOTAL";
	private String tipo 		   = "TIPO";
	private String ncolegiado 	   = "NCOLEGIADO";
	private String nombreColegiado = "NOMBRECOLEGIADO";
	private String importeIrpf	   = "IMPORTEIRPF";
	private String porcentajeIrpf  = "PORCENTAJEIRPF";
	private String estadoPago  	   = "ESTADOPAGO";
	private String accion	  	   = "ACCION";
	private String modoOriginal	   = "MODOORIGINAL";
	
	
	/**
	 * @return Returns the idInstitucion.
	 */
	public String getNcolegiado() {
		return (String)this.datos.get(ncolegiado);
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setNcolegiado(String valor) {
		this.datos.put(ncolegiado ,valor);
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public String getNombreColegiado() {
		return (String)this.datos.get(nombreColegiado);
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setNombreColegiado(String valor) {
		this.datos.put(nombreColegiado ,valor);
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public String getIdInstitucion() {
		return (String)this.datos.get(idInstitucion);
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(String valor) {
		this.datos.put(idInstitucion ,valor);
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public String getTipo() {
		return (String)this.datos.get(tipo);
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setTipo(String valor) {
		this.datos.put(tipo,valor);
	}
	/**
	 * @return Returns the idPago.
	 */
	public String getIdPago() {
		return (String)this.datos.get(idPago);
	}
	/**
	 * @param idPago The idPago to set.
	 */
	public void setIdPago(String valor) {
		this.datos.put(idPago,valor);
	}
	/**
	 * @return Returns the idPersona.
	 */
	public String getIdPersona() {
		return (String)this.datos.get(idPersona);
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(String valor) {
		this.datos.put(idPersona,valor);
	}
	/**
	 * @return Returns the importeTotal.
	 */
	public String getImporteTotal() {
		return (String)this.datos.get(importeTotal);
	}
	/**
	 * @param importeTotal The importeTotal to set.
	 */
	public void setImporteTotal(String valor) {
		this.datos.put(importeTotal,valor);
	}
	
	/**
	 * @return Returns the importeIrpf.
	 */
	public String getImporteIrpf() {
		return (String)this.datos.get(importeIrpf);
	}
	/**
	 * @param importeIrpf The importeIrpf to set.
	 */
	public void setImporteIrpf(String valor) {
		this.datos.put(importeIrpf,valor);		
	}
	

	/**
	 * @param dato
	 */
	public void 	setTablaDatosDinamicosDtablaDatos1(String dato)	{
		super.setTablaDatosDinamicosD(dato);
	}
	
	/**
	 * @param dato
	 */
	public void 	setTablaDatosDinamicosDtablaDatos2(String dato)	{
		super.setTablaDatosDinamicosD(dato);
	}
	
	/**
	 * @param dato
	 */
	public void 	setTablaDatosDinamicosDtablaDatos3(String dato)	{
		super.setTablaDatosDinamicosD(dato);
	}
	
	/**
	 * @param dato
	 */
	public void 	setTablaDatosDinamicosDtablaDatos4(String dato)	{
		super.setTablaDatosDinamicosD(dato);
	}
	
	/**
	 * @param dato
	 */
	public void 	setTablaDatosDinamicosDtablaDatos5(String dato)	{
		super.setTablaDatosDinamicosD(dato);
	}
	
	/**
	 * @param dato
	 */
	public void 	setTablaDatosDinamicosDtablaDatos6(String dato)	{
		super.setTablaDatosDinamicosD(dato);
	}
	
	/**
	 * @param dato
	 */
	public void 	setTablaDatosDinamicosDtablaDatos7(String dato)	{
		super.setTablaDatosDinamicosD(dato);
	}
	
	/**
	 * @param dato
	 */
	public void 	setTablaDatosDinamicosDtablaDatos8(String dato)	{
		super.setTablaDatosDinamicosD(dato);
	}
	

	/**
	 * @return Returns the porcentajeIrpf.
	 */
	public String getPorcentajeIrpf() {
		return (String)this.datos.get(porcentajeIrpf);
	}
	/**
	 * @param porcentajeIrpf The porcentajeIrpf to set.
	 */
	public void setPorcentajeIrpf(String valor) {
		this.datos.put(porcentajeIrpf,valor);				
	}
	
	/**
	 * @return Returns the estadoPago.
	 */
	public String getEstadoPago() {
		return (String)this.datos.get(estadoPago);
	}
	/**
	 * @param estadoPago The estadoPago to set.
	 */
	public void setEstadoPago(String valor) {
		this.datos.put(estadoPago,valor);				
	}
	
	/**
	 * @return Returns the modoOriginal.
	 */
	public String getModoOriginal() {
		return (String)this.datos.get(modoOriginal);
	}
	/**
	 * @param modoOriginal The modoOriginal to set.
	 */
	public void setModoOriginal(String valor) {
		this.datos.put(modoOriginal,valor);				
	}

}