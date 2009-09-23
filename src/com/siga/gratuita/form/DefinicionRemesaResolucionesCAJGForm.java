package com.siga.gratuita.form;

import org.apache.struts.upload.FormFile;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CajgRemesaResolucionBean;
import com.siga.general.MasterForm;

public class DefinicionRemesaResolucionesCAJGForm extends MasterForm {
	
	private FormFile file;
	
	/**
	 * @return the file
	 */
	public FormFile getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(FormFile file) {
		this.file = file;
	}
	
	/**
	 * @return the idResolucion
	 */
	public String getIdRemesaResolucion() {
		return UtilidadesHash.getString(this.datos, CajgRemesaResolucionBean.C_IDREMESARESOLUCION);
	}
	/**
	 * @param idResolucion the idResolucion to set
	 */
	public void setIdRemesaResolucion(String idRemesaResolucion) {
		this.datos.put(CajgRemesaResolucionBean.C_IDREMESARESOLUCION, idRemesaResolucion);
	}
	/**
	 * @return the idInstitucion
	 */
	public String getIdInstitucion() {
		return UtilidadesHash.getString(this.datos, CajgRemesaResolucionBean.C_IDINSTITUCION);
	}
	/**
	 * @param idInstitucion the idInstitucion to set
	 */
	public void setIdInstitucion(String idInstitucion) {
		this.datos.put(CajgRemesaResolucionBean.C_IDINSTITUCION, idInstitucion);
	}
	/**
	 * @return the prefijo
	 */
	public String getPrefijo() {
		return UtilidadesHash.getString(this.datos, CajgRemesaResolucionBean.C_PREFIJO);
	}
	/**
	 * @param prefijo the prefijo to set
	 */
	public void setPrefijo(String prefijo) {
		this.datos.put(CajgRemesaResolucionBean.C_PREFIJO, prefijo);
	}
	/**
	 * @return the numero
	 */
	public String getNumero() {
		return UtilidadesHash.getString(this.datos, CajgRemesaResolucionBean.C_NUMERO);
	}
	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.datos.put(CajgRemesaResolucionBean.C_NUMERO, numero);
	}
	/**
	 * @return the sufijo
	 */
	public String getSufijo() {
		return UtilidadesHash.getString(this.datos, CajgRemesaResolucionBean.C_SUFIJO);
	}
	/**
	 * @param sufijo the sufijo to set
	 */
	public void setSufijo(String sufijo) {
		this.datos.put(CajgRemesaResolucionBean.C_SUFIJO, sufijo);
	}
	/**
	 * @return the fechaResolucion
	 */
	public String getFechaResolucion() {
		return UtilidadesHash.getString(this.datos, CajgRemesaResolucionBean.C_FECHARESOLUCION);
	}
	/**
	 * @param fechaResolucion the fechaResolucion to set
	 */
	public void setFechaResolucion(String fechaResolucion) {
		this.datos.put(CajgRemesaResolucionBean.C_FECHARESOLUCION, fechaResolucion);
	}
	/**
	 * @return the fechaResolucionHasta
	 */
	public String getFechaResolucionHasta() {
		return UtilidadesHash.getString(this.datos, CajgRemesaResolucionBean.C_FECHARESOLUCIONHASTA);
	}
	/**
	 * @param fechaResolucionHasta the fechaResolucionHasta to set
	 */
	public void setFechaResolucionHasta(String fechaResolucionHasta) {
		this.datos.put(CajgRemesaResolucionBean.C_FECHARESOLUCIONHASTA, fechaResolucionHasta);
	}
	
	/**
	 * @return the NombreFichero
	 */
	public String getNombreFichero() {
		return UtilidadesHash.getString(this.datos, CajgRemesaResolucionBean.C_NOMBREFICHERO);
	}
	/**
	 * @param NombreFichero the NombreFichero to set
	 */
	public void setNombreFichero(String nombreFichero) {
		this.datos.put(CajgRemesaResolucionBean.C_NOMBREFICHERO, nombreFichero);
	}
	
	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return UtilidadesHash.getString(this.datos, CajgRemesaResolucionBean.C_OBSERVACIONES);
	}
	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.datos.put(CajgRemesaResolucionBean.C_OBSERVACIONES, observaciones);
	}
	
	/**
	 * @return the fechaCarga
	 */
	public String getFechaCarga() {
		return UtilidadesHash.getString(this.datos, CajgRemesaResolucionBean.C_FECHACARGA);
	}
	/**
	 * @param fechaCarga the fechaCarga to set
	 */
	public void setFechaCarga(String fechaCarga) {
		this.datos.put(CajgRemesaResolucionBean.C_FECHACARGA, fechaCarga);
	}
	/**
	 * @return the fechaCargaHasta
	 */
	public String getFechaCargaHasta() {
		return UtilidadesHash.getString(this.datos, CajgRemesaResolucionBean.c_FECHACARGAHASTA);
	}
	/**
	 * @param fechaCargaHasta the fechaCargaHasta to set
	 */
	public void setFechaCargaHasta(String fechaCargaHasta) {
		this.datos.put(CajgRemesaResolucionBean.c_FECHACARGAHASTA, fechaCargaHasta);
	}
	
	/**
	 * @return the logGenerado
	 */
	public String getLogGenerado() {
		return UtilidadesHash.getString(this.datos, CajgRemesaResolucionBean.C_LOGGENERADO);
	}
	/**
	 * @param logGenerado the logGenerado to set
	 */
	public void setLogGenerado(String logGenerado) {
		this.datos.put(CajgRemesaResolucionBean.C_LOGGENERADO, logGenerado);
	}
}