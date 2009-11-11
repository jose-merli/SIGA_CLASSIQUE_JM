//VERSIONES
//ruben.fernandez : 20/04/2005 Creacion
//

package com.siga.facturacionSJCS.form;

import com.siga.general.MasterForm;

public class MantenimientoPrevisionesForm extends MasterForm {

	private String nombreB = 		"NOMBREB";
	private String hitosB = 		"HITOSB";
	private String fechaDesdeB = 	"FECHADESDEB";
	private String fechaHastaB = 	"FECHAHASTAB";
	private String nombre = 		"NOMBRE";
	private String hitos = 			"HITOS";
	private String fechaDesde = 	"FECHADESDE";
	private String fechaHasta = 	"FECHAHASTA";
	private String partida = 		"PARTIDA";
	private String importe = 		"IMPORTE";
	private String idPrevision = 	"IDPREVISION";
	private String idFacturacion = 	"IDFACTURACION";
	private String idInstitucion = 	"IDINSTITUCION";
	
	
	/**
	 * @return Returns the idFacturacion.
	 */
	public String getIdFacturacion() {
		return (String)datos.get(idFacturacion);
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setIdInstitucion(String valor) {
		this.datos.put(idInstitucion , valor);
	}

	
	/**
	 * @return Returns the idFacturacion.
	 */
	public String getIdInstitucion() {
		return (String)datos.get(idInstitucion);
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setIdFacturacion(String valor) {
		this.datos.put(idFacturacion , valor);
	}
	
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return (String)datos.get(nombre);
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String valor) {
		this.datos.put(nombre , valor);
	}
	
	/**
	 * @return Returns the fechaDesde.
	 */
	public String getFechaDesde() {
		return (String)datos.get(fechaDesde);
	}
	/**
	 * @param fechaDesde The fechaDesde to set.
	 */
	public void setFechaDesde(String valor) {
		this.datos.put(fechaDesde , valor);
	}
	/**
	 * @return (String)datos.get(Returns the fechaHasta.
	 */
	public String getFechaHasta() {
		return (String)datos.get(fechaHasta);
	}
	/**
	 * @param fechaHasta The fechaHasta to set.
	 */
	public void setFechaHasta(String valor) {
		this.datos.put(fechaHasta , valor);
	}
	/**
	 * @return (String)datos.get(Returns the hitos.
	 */
	public String getHitos() {
		return (String)datos.get(hitos);
	}
	/**
	 * @param hitos The hitos to set.
	 */
	public void setHitos(String valor) {
		this.datos.put(hitos  , valor);
	}
	/**
	 * @return (String)datos.get(Returns the idPrevision.
	 */
	public String getIdPrevision() {
		return (String)datos.get(idPrevision);
	}
	/**
	 * @param idPrevision The idPrevision to set.
	 */
	public void setIdPrevision(String valor) {
		this.datos.put(idPrevision  , valor);
	}
	/**
	 * @return (String)datos.get(Returns the importe.
	 */
	public String getImporte() {
		return (String)datos.get(importe);
	}
	/**
	 * @param importe The importe to set.
	 */
	public void setImporte(String valor) {
		this.datos.put(importe  , valor);
	}
	/**
	 * @return (String)datos.get(Returns the partida.
	 */
	public String getPartida() {
		return (String)datos.get(partida);
	}
	/**
	 * @param partida The partida to set.
	 */
	public void setPartida(String valor) {
		try{
			this.datos.put(partida ,valor.substring(0,valor.indexOf(",")));
		}catch(Exception e){
			this.datos.put(partida ,valor);
		}
	}

	/**
	 * @return Returns the nombre.
	 */
	public String getNombreB() {
		return (String)datos.get(nombreB);
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombreB(String valor) {
		this.datos.put(nombreB , valor);
	}
	
	/**
	 * @return Returns the fechaDesde.
	 */
	public String getFechaDesdeB() {
		return (String)datos.get(fechaDesdeB);
	}
	/**
	 * @param fechaDesde The fechaDesde to set.
	 */
	public void setFechaDesdeB(String valor) {
		this.datos.put(fechaDesdeB , valor);
	}
	/**
	 * @return (String)datos.get(Returns the fechaHasta.
	 */
	public String getFechaHastaB() {
		return (String)datos.get(fechaHastaB);
	}
	/**
	 * @param fechaHasta The fechaHasta to set.
	 */
	public void setFechaHastaB(String valor) {
		this.datos.put(fechaHastaB , valor);
	}
	/**
	 * @return (String)datos.get(Returns the hitos.
	 */
	public String getHitosB() {
		return (String)datos.get(hitosB);
	}
	/**
	 * @param hitos The hitos to set.
	 */
	public void setHitosB(String valor) {
		this.datos.put(hitosB  , valor);
	}
}