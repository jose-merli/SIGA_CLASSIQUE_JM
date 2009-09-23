/*
 * Created on 06-Marzo-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author PDM
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScsBeneficiarioSOJBean extends MasterBean {

	/* Variables */	
	private String 	descripcion;
	private Integer idInstitucion, idPersona, idTipoConoce, idTipoGrupoLab;
	private Integer nVecesSOJ ;
	private String solicitaJG,solicitaInfoSOJ;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "SCS_BENEFICIARIOSOJ";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION		= "IDINSTITUCION";
	static public final String C_IDPERSONA	= "IDPERSONA";
	static public final String C_IDTIPOCONOCE	= "IDTIPOCONOCE";
	static public final String C_IDTIPOGRUPOLAB	= "IDTIPOGRUPOLAB";
	static public final String C_SOLICITAJG	= "SOLICITAJG";
	static public final String C_SOLICITAINFOJG	= "SOLICITAINFOJG";
	static public final String C_NUMVECESSOJ	= "NUMVECESSOJ";
	

	
	
	
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
	 * @return Returns the idPersona.
	 */
	public Integer getIdPersona() {
		return idPersona;
	}
	/**
	 * @param idPersona The idPersona to set.
	 */
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	/**
	 * @return Returns the idTipoConoce.
	 */
	public Integer getIdTipoConoce() {
		return idTipoConoce;
	}
	/**
	 * @param idTipoConoce The idTipoConoce to set.
	 */
	public void setIdTipoConoce(Integer idTipoConoce) {
		this.idTipoConoce = idTipoConoce;
	}
	/**
	 * @return Returns the idTipoGrupoLab.
	 */
	public Integer getIdTipoGrupoLab() {
		return idTipoGrupoLab;
	}
	/**
	 * @param idTipoGrupoLab The idTipoGrupoLab to set.
	 */
	public void setIdTipoGrupoLab(Integer idTipoGrupoLab) {
		this.idTipoGrupoLab = idTipoGrupoLab;
	}
	
	/**
	 * @return Returns the nVecesSOJ.
	 */
	public Integer getNVecesSOJ() {
		return nVecesSOJ;
	}
	/**
	 * @param vecesSOJ The nVecesSOJ to set.
	 */
	public void setNVecesSOJ(Integer vecesSOJ) {
		nVecesSOJ = vecesSOJ;
	}
	
	/**
	 * @return Returns the solicitaInfoSOJ.
	 */
	public String getSolicitaInfoSOJ() {
		return solicitaInfoSOJ;
	}
	/**
	 * @param solicitaInfoSOJ The solicitaInfoSOJ to set.
	 */
	public void setSolicitaInfoSOJ(String solicitaInfoSOJ) {
		this.solicitaInfoSOJ = solicitaInfoSOJ;
	}
	/**
	 * @return Returns the solicitaJG.
	 */
	public String getSolicitaJG() {
		return solicitaJG;
	}
	/**
	 * @param solicitaJG The solicitaJG to set.
	 */
	public void setSolicitaJG(String solicitaJG) {
		this.solicitaJG = solicitaJG;
	}
}
