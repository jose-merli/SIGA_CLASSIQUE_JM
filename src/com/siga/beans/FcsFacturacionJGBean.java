// VERSIONES:
// raul.ggonzalez 08-03-2005 creacion
//
package com.siga.beans;


/**
 * Bean de la tabla FCS_FACTURACIONJG
 * 
 * @author AtosOrigin 08-03-2005
 * 
 */
public class FcsFacturacionJGBean extends MasterBean {
	
	/* Variables */
	private Integer idInstitucion, idFacturacion, idFacturacion_regulariza;
	private Double importeTotal, importeOficio, importeGuardia, importeSOJ, importeEJG;
	
	private String 	nombre, fechaDesde, fechaHasta, regularizacion, prevision, nombreFisico;

	/* Nombre tabla */
	static public String T_NOMBRETABLA 					= "FCS_FACTURACIONJG";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 			= "IDINSTITUCION";
	static public final String C_IDFACTURACION			= "IDFACTURACION";
	static public final String C_FECHADESDE				= "FECHADESDE";
	static public final String C_FECHAHASTA				= "FECHAHASTA";
	static public final String C_IMPORTETOTAL			= "IMPORTETOTAL";
	static public final String C_IMPORTEOFICIO			= "IMPORTEOFICIO";				
	static public final String C_IMPORTEGUARDIA			= "IMPORTEGUARDIA";					
	static public final String C_IMPORTESOJ				= "IMPORTESOJ";			
	static public final String C_IMPORTEEJG				= "IMPORTEEJG";
	static public final String C_PREVISION			    = "PREVISION";
	static public final String C_NOMBRE					= "NOMBRE";
	static public final String C_REGULARIZACION			= "REGULARIZACION";
	static public final String C_IDFACTURACION_REGULARIZA	= "IDFACTURACION_REGULARIZA";
	static public final String C_NOMBREFISICO			= "NOMBREFISICO";
	
	
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
	 * @return Returns the idFacturacion_regulariza.
	 */
	public Integer getIdFacturacion_regulariza() {
		return idFacturacion_regulariza;
	}
	/**
	 * @param idFacturacion_regulariza The idFacturacion_regulariza to set.
	 */
	public void setIdFacturacion_regulariza(Integer idFacturacion_regulariza) {
		this.idFacturacion_regulariza = idFacturacion_regulariza;
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
	/**
	 * @return Returns the importeTotal.
	 */
	public Double getImporteTotal() {
		return importeTotal;
	}
	/**
	 * @param importeTotal The importeTotal to set.
	 */
	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
	}
	/**
	 * @return Returns the nombre.
	 */
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
	 * @return Returns the prevision.
	 */
	public String getPrevision() {
		return prevision;
	}
	/**
	 * @param prevision The prevision to set.
	 */
	public void setPrevision(String prevision) {
		this.prevision = prevision;
	}
	/**
	 * @return Returns the regularizacion.
	 */
	public String getRegularizacion() {
		return regularizacion;
	}
	/**
	 * @param regularizacion The regularizacion to set.
	 */
	public void setRegularizacion(String regularizacion) {
		this.regularizacion = regularizacion;
	}
	
	public String getNombreFisico() {
		return nombreFisico;
	}
	public void setNombreFisico(String nombreFisico) {
		this.nombreFisico = nombreFisico;
	}
}

