//VERSIONES
//ruben.fernandez : 10/03/2005 Creacion
//

package com.siga.facturacionSJCS.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

public class DatosGeneralesFacturacionForm extends MasterForm {

	private String nombre = "NOMBRE";
	private String estado = "ESTADO";
	private String fechaInicio = "FECHADESDE";
	private String fechaFin = "FECHAHASTA";
	private String fechaEstado = "FECHAESTADO";
	private String partidaP = "IDPARTIDAPRESUPUESTARIA";
	private String idFacturacion = "IDFACTURACION";
	private String importe = "IMPORTE";
	private String grupoF = "GRUPOF";
	private String hito = "HITO";
	private String turnos = "TURNOS";
	private String guardias = "GUARDIAS";
	private String ejg = "EJG";
	private String soj = "SOJ";
	private String idInstitucion = "IDINSTITUCION";
	private String idEstado=null;
	
	
	/**
	 * @return Returns the idInstitucion.
	 */
	public String getIdInstitucion() {
		return (String)this.datos.get(idInstitucion);
	}
	public String getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
	/**
	 * @param ejg The idInstitucion to set.
	 */
	public void setIdInstitucion(String valor) {
		this.datos.put(idInstitucion, valor);
	}
	/**
	 * @return Returns the ejg.
	 */
	public String getEjg() {
		return (String)this.datos.get(ejg);
	}
	/**
	 * @param ejg The ejg to set.
	 */
	public void setEjg(String valor) {
		this.datos.put(ejg, valor);
	}
	/**
	 * @return Returns the fecha estado.
	 */
	public String getFechaEstado() {
		return (String)this.datos.get(fechaEstado);
	}
	/**
	 * @param ejg The fecha estado to set.
	 */
	public void setFechaEstado(String valor) {
		this.datos.put(fechaEstado, valor);
	}
	/**
	 * @return Returns the ejg.
	 */
	public String getIdFacturacion() {
		return (String)this.datos.get(idFacturacion);
	}
	/**
	 * @param ejg The ejg to set.
	 */
	public void setIdFacturacion(String valor) {
		this.datos.put(idFacturacion, valor);
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return (String)this.datos.get(estado);
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String valor) {
		this.datos.put(estado ,valor);
	}
	/**
	 * @return Returns the fechaFin.
	 */
	public String getFechaFin() {
		return (String)this.datos.get(fechaFin);
	}
	/**
	 * @param fechaFin The fechaFin to set.
	 */
	public void setFechaFin(String valor) {
		this.datos.put(fechaFin,valor);
	}
	/**
	 * @return Returns the fechaInicio.
	 */
	public String getFechaInicio() {
		return (String)this.datos.get(fechaInicio);
	}
	/**
	 * @param fechaInicio The fechaInicio to set.
	 */
	public void setFechaInicio(String valor) {
		this.datos.put(fechaInicio ,valor);
	}
	/**
	 * @return Returns the gruposF.
	 */
	public String getGrupoF() {
		return (String)this.datos.get(grupoF);
	}
	/**
	 * @param gruposF The gruposF to set.
	 */
	public void setGrupoF(String valor) {
		this.datos.put(grupoF,valor);
	}
	/**
	 * @return Returns the gruposF.
	 */
	public String getHito() {
		return (String)this.datos.get(hito);
	}
	/**
	 * @param gruposF The gruposF to set.
	 */
	public void setHito(String valor) {
		this.datos.put(hito,valor);
	}
	/**
	 * @return Returns the guardias.
	 */
	public String getGuardias() {
		return (String)this.datos.get(guardias);
	}
	/**
	 * @param guardias The guardias to set.
	 */
	public void setGuardias(String valor) {
		this.datos.put(guardias,valor);
	}
	/**
	 * @return Returns the importe.
	 */
	public String getImporte() {
		return (String)this.datos.get(importe);
	}
	/**
	 * @param importe The importe to set.
	 */
	public void setImporte(String valor) {
		this.datos.put(importe,valor);
	}
	/**
	 * @return Returns the partidaP.
	 */
	public String getPartidaP() {
		return (String)this.datos.get(partidaP);
	}
	/**
	 * @param partidaP The partidaP to set.
	 */
	public void setPartidaP(String valor) {
		try{
			this.datos.put(partidaP ,valor.substring(0,valor.indexOf(",")));
		}catch(Exception e){
			this.datos.put(partidaP ,valor);
		}
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return (String)this.datos.get(nombre);
	}
	/**
	 * @param serie The nombre to set.
	 */
	public void setNombre(String valor) {
		this.datos.put(nombre, valor);
	}
	/**
	 * @return Returns the soj.
	 */
	public String getSoj() {
		return (String)this.datos.get(soj);
	}
	/**
	 * @param soj The soj to set.
	 */
	public void setSoj(String valor) {
		this.datos.put(soj, valor);
	}
	/**
	 * @return Returns the turnos.
	 */
	public String getTurnos() {
		return (String)this.datos.get(turnos);
	}
	/**
	 * @param turnos The turnos to set.
	 */
	public void setTurnos(String valor) {
		this.datos.put(turnos ,valor);
	}
	
	/**
	 * @return Returns the idFacturacionDownload.
	 */
	public Integer getIdFacturacionDownload() {
		return UtilidadesHash.getInteger(datos, "idFacturacionDonwload");
	}
	/**
	 * @param idFacturacionDownload The idFacturacionDownload to set.
	 */
	public void setIdFacturacionDownload(Integer idFacturacionDownload) {
		UtilidadesHash.set(datos, "idFacturacionDonwload", idFacturacionDownload);
	}
	
	/**
	 * @return Returns the idFacturacionDownload.
	 */
	public Integer getIdFacturacionFinDownload() {
		return UtilidadesHash.getInteger(datos, "idFacturacionFinDonwload");
	}
	/**
	 * @param idFacturacionFinDownload The idFacturacionFinDownload to set.
	 */
	public void setIdFacturacionFinDownload(Integer idFacturacionFinDownload) {
		UtilidadesHash.set(datos, "idFacturacionFinDonwload", idFacturacionFinDownload);
	}
	/**
	 * @return Returns the idFacturacionIniDownload.
	 */
	public Integer getIdFacturacionIniDownload() {
		return UtilidadesHash.getInteger(datos, "idFacturacionIniDonwload");
	}
	/**
	 * @param idFacturacionIniDownload The idFacturacionIniDownload to set.
	 */
	public void setIdFacturacionIniDownload(Integer idFacturacionIniDownload) {
		UtilidadesHash.set(datos, "idFacturacionIniDonwload", idFacturacionIniDownload);
	}
	
	/**
	 * @return Returns the idInstitucionDownload.
	 */
	public Integer getIdInstitucionDownload() {
		return UtilidadesHash.getInteger(datos, "idInstitucionDonwload");
	}
	/**
	 * @param idInstitucionDownload The idInstitucionDownload to set.
	 */
	public void setIdInstitucionDownload(Integer idInstitucionDownload) {
		 UtilidadesHash.set(datos, "idInstitucionDonwload", idInstitucionDownload);
	}
	/**
	 * 
	 * @return
	 */
	public String getIdPagoDownload() {
		return UtilidadesHash.getString(datos, "idPagoDonwload");
	}
	/**
	 * 
	 * @param id
	 */
	public void setIdPagoDownload(String id) {
		 UtilidadesHash.set(datos, "idPagoDonwload", id);
	}
	/**
	 * 
	 * @return
	 */
	public String getIdPersonaDownload() {
		return UtilidadesHash.getString(datos, "idPersonaDonwload");
	}
	/**
	 * @param id
	 */
	public void setIdPersonaDownload(String id) {
		 UtilidadesHash.set(datos, "idPersonaDonwload", id);
	}
	/**
	 * @return Returns the tipoDownload.
	 */
	public String getTipoDownload() {
		 return UtilidadesHash.getString(datos, "idTipoDonwload");
	}
	/**
	 * @param tipoDownload The tipoDownload to set.
	 */
	public void setTipoDownload(String tipoDownload) {
		 UtilidadesHash.set(datos, "idTipoDonwload", tipoDownload);
	}
	
	/**
	 * @return Returns the idioma.
	 */
	public String getIdiomaDownload() {
		 return UtilidadesHash.getString(datos, "idiomaDownload");
	}
	/**
	 * @param idioma The idioma to set.
	 */
	public void setIdiomaDownload(String idiomaDownload) {
		 UtilidadesHash.set(datos, "idiomaDownload", idiomaDownload);
	}

}