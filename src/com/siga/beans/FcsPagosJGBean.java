//VERSIONES:
//Creacion: david.sanchezp 18-03-2005
// jose.barrientos -  23-02-2009 - Añadidos los campos domicilio, poblacion y motivo
//
package com.siga.beans;

import com.siga.Utilidades.AjaxXMLBuilderAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderNameAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderValueAnnotation;

/**
* Bean de la tabla FCS_PAGOSJG
* 
* @author david.sanchezp 
* 
*/
@AjaxXMLBuilderAnnotation 
public class FcsPagosJGBean extends MasterBean {
	
	/* Variables */
	private Integer idInstitucion, idPagosJG, idFacturacion, idpropSEPA, idpropOtros, idsufijo;
	private Double importeRepartir, importePagado, importeOficio = null, importeGuardia = null, importeSOJ = null, importeEJG = null, importeMinimo = null; 
	private String 	nombre=null, abreviatura=null, fechaDesde=null, fechaHasta=null, criterioPagoTurno=null, contabilizado=null, concepto=null, bancosCodigo=null;
	

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FCS_PAGOSJG";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_IDPAGOSJG 			= "IDPAGOSJG";
	static public final String C_IDFACTURACION 		= "IDFACTURACION";
	static public final String C_NOMBRE				= "NOMBRE";
	static public final String C_ABREVIATURA		= "ABREVIATURA";
	static public final String C_FECHADESDE			= "FECHADESDE";
	static public final String C_FECHAHASTA			= "FECHAHASTA";
	static public final String C_CRITERIOPAGOTURNO	= "CRITERIOPAGOTURNO";
	static public final String C_IMPORTEREPARTIR	= "IMPORTEREPARTIR";
	static public final String C_IMPORTEPAGADO		= "IMPORTEPAGADO";
	static public final String C_IMPORTEOFICIO		= "IMPORTEOFICIO";			
	static public final String C_IMPORTEGUARDIA		= "IMPORTEGUARDIA";	
	static public final String C_IMPORTESOJ			= "IMPORTESOJ";
	static public final String C_IMPORTEEJG			= "IMPORTEEJG";
	static public final String C_IMPORTEMINIMO		= "IMPORTEMINIMO";
	static public final String C_CONTABILIZADO		= "CONTABILIZADO";
	static public final String C_CONCEPTO			= "CONCEPTO";
	static public final String C_BANCOS_CODIGO		= "BANCOS_CODIGO";
	static public final String C_IDPROPSEPA	   		= "IDPROPSEPA";
	static public final String C_IDPROPOTROS		= "IDPROPOTROS";
	static public final String C_IDSUFIJO			= "IDSUFIJO";

	public String getContabilizado() {
		return contabilizado;
	}
	public void setContabilizado(String contabilizado) {
		this.contabilizado = contabilizado;
	}	
	
	/**
	 * @return Returns the abreviatura.
	 */
	public String getAbreviatura() {
		return abreviatura;
	}
	/**
	 * @param abreviatura The abreviatura to set.
	 */
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	/**
	 * @return Returns the criterioPagoTurno.
	 */
	public String getCriterioPagoTurno() {
		return criterioPagoTurno;
	}
	/**
	 * @param criterioPagoTurno The criterioPagoTurno to set.
	 */
	public void setCriterioPagoTurno(String criterioPagoTurno) {
		this.criterioPagoTurno = criterioPagoTurno;
	}
	/**
	 * @return Returns the fechaDesde.
	 */
	public String getFechaDesde() {
		return fechaDesde;
	}
	/**
	 * @param fechaDesde The fechaDesde to set.
	 */
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	/**
	 * @return Returns the fechaHasta.
	 */
	public String getFechaHasta() {
		return fechaHasta;
	}
	/**
	 * @param fechaHasta The fechaHasta to set.
	 */
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	/**
	 * @return Returns the idFacturacion.
	 */
	public Integer getIdFacturacion() {
		return idFacturacion;
	}
	/**
	 * @param idFacturacion The idFacturacion to set.
	 */
	public void setIdFacturacion(Integer idFacturacion) {
		this.idFacturacion = idFacturacion;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the idPagosJG.
	 */
	@AjaxXMLBuilderValueAnnotation(isCData=false)
	public Integer getIdPagosJG() {
		return idPagosJG;
	}
	/**
	 * @param idPagosJG The idPagosJG to set.
	 */
	public void setIdPagosJG(Integer idPagosJG) {
		this.idPagosJG = idPagosJG;
	}
	/**
	 * @return Returns the importeRepartir.
	 */
	public Double getImporteRepartir() {
		return importeRepartir;
	}
	/**
	 * @param importeRepartir The importeRepartir to set.
	 */
	public void setImporteRepartir(Double importeRepartir) {
		this.importeRepartir = importeRepartir;
	}
	/**
	 * @return Returns the nombre.
	 */
	@AjaxXMLBuilderNameAnnotation
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return Returns the importePagado.
	 */
	public Double getImportePagado() {
		return importePagado;
	}
	/**
	 * @param importePagado The importePagado to set.
	 */
	public void setImportePagado(Double importePagado) {
		this.importePagado = importePagado;
	}
	/**
	 * @return Returns the importeEJG.
	 */
	public Double getImporteEJG() {
		return importeEJG;
	}
	/**
	 * @param importeEJG The importeEJG to set.
	 */
	public void setImporteEJG(Double importeEJG) {
		this.importeEJG = importeEJG;
	}
	/**
	 * @return Returns the importeGuardia.
	 */
	public Double getImporteGuardia() {
		return importeGuardia;
	}
	/**
	 * @param importeGuardia The importeGuardia to set.
	 */
	public void setImporteGuardia(Double importeGuardia) {
		this.importeGuardia = importeGuardia;
	}
	/**
	 * @return Returns the importeMinimo.
	 */
	public Double getImporteMinimo() {
		return importeMinimo;
	}
	/**
	 * @param importeMinimo The importeMinimo to set.
	 */
	public void setImporteMinimo(Double importeMinimo) {
		this.importeMinimo = importeMinimo;
	}
	/**
	 * @return Returns the importeOficio.
	 */
	public Double getImporteOficio() {
		return importeOficio;
	}
	/**
	 * @param importeOficio The importeOficio to set.
	 */
	public void setImporteOficio(Double importeOficio) {
		this.importeOficio = importeOficio;
	}
	/**
	 * @return Returns the importeSOJ.
	 */
	public Double getImporteSOJ() {
		return importeSOJ;
	}
	/**
	 * @param importeSOJ The importeSOJ to set.
	 */
	public void setImporteSOJ(Double importeSOJ) {
		this.importeSOJ = importeSOJ;
	}
	public static String getC_CONTABILIZADO() {
		return C_CONTABILIZADO;
	}
	
	public String getConcepto() {
		return concepto;
	}
	
	public void setConcepto(String concepto){
		this.concepto = concepto;
	}
	
	public String getBancosCodigo() {
		return bancosCodigo;
	}
	
	public void setBancosCodigo(String bancosCodigo){
		this.bancosCodigo = bancosCodigo;
	}
	
	
	public Integer getIdpropSEPA() {
		return idpropSEPA;
	}
	
	public void setIdpropSEPA(Integer idpropSEPA) {
		this.idpropSEPA = idpropSEPA;
	}
	
	public Integer getIdpropOtros() {
		return idpropOtros;
	}
	
	public void setIdpropOtros(Integer idpropOtros) {
		this.idpropOtros = idpropOtros;
	}
	
	public Integer getIdsufijo() {
		return idsufijo;
	}
	
	public void setIdsufijo(Integer idsufijo) {
		this.idsufijo = idsufijo;
	}
}
