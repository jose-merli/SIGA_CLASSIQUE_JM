/*
 * Creada el 31 de Marzo de 2005.
 * @author david.sanchezp
 *
 */
package com.siga.beans;

import java.io.Serializable;

//Este bean sirve para implementar cada fila de Criterios de Pago con todos sus campos.
public class CriteriosPagosBean implements Serializable{
    
	//Variables
	private String idGrupoFacturacion = null;
	private String idHitoGeneral = null;
	private String idPagosJG = null;
	private String grupoFacturacion = null;	
	private String hitoGeneral = null;
	private String checkCriterio = null;
	
	
	/**
	 * @return Returns the grupoFacturacion.
	 */
	public String getGrupoFacturacion() {
		return grupoFacturacion;
	}
	/**
	 * @param grupoFacturacion The grupoFacturacion to set.
	 */
	public void setGrupoFacturacion(String grupoFacturacion) {
		this.grupoFacturacion = grupoFacturacion;
	}
	/**
	 * @return Returns the hitoGeneral.
	 */
	public String getHitoGeneral() {
		return hitoGeneral;
	}
	/**
	 * @param hitoGeneral The hitoGeneral to set.
	 */
	public void setHitoGeneral(String hitoGeneral) {
		this.hitoGeneral = hitoGeneral;
	}
	/**
	 * @return Returns the idGrupoFacturacion.
	 */
	public String getIdGrupoFacturacion() {
		return idGrupoFacturacion;
	}
	/**
	 * @param idGrupoFacturacion The idGrupoFacturacion to set.
	 */
	public void setIdGrupoFacturacion(String idGrupoFacturacion) {
		this.idGrupoFacturacion = idGrupoFacturacion;
	}
	/**
	 * @return Returns the idHitoGeneral.
	 */
	public String getIdHitoGeneral() {
		return idHitoGeneral;
	}
	/**
	 * @param idHitoGeneral The idHitoGeneral to set.
	 */
	public void setIdHitoGeneral(String idHitoGeneral) {
		this.idHitoGeneral = idHitoGeneral;
	}
	/**
	 * @return Returns the checkCriterio.
	 */
	public String getCheckCriterio() {
		return checkCriterio;
	}
	/**
	 * @param checkCriterio The checkCriterio to set.
	 */
	public void setCheckCriterio(String checkCriterio) {
		this.checkCriterio = checkCriterio;
	}
	/**
	 * @return Returns the idPagosJG.
	 */
	public String getIdPagosJG() {
		return idPagosJG;
	}
	/**
	 * @param idPagosJG The idPagosJG to set.
	 */
	public void setIdPagosJG(String idPagosJG) {
		this.idPagosJG = idPagosJG;
	}
}
