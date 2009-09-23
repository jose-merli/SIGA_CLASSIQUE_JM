package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla cen_tiposCVSubtipo1
 * 
 * @author pdm
 * @since 05/10/2007
 */

public class CenTiposCVSubtipo1Bean extends MasterBean{
	
	/**
	 *  Variables */ 
	
	
	private String	codigoExt,descripcion;
	private Integer idTipoCV,idTipoCVSubtipo1,idInstitucion;
	
	/**
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "CEN_TIPOSCVSUBTIPO1";
	
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String	C_IDTIPOCV         = 	"IDTIPOCV";
	static public final String 	C_IDTIPOCVSUBTIPO1 = 	"IDTIPOCVSUBTIPO1";
	static public final String C_IDINSTITUCION	   = "IDINSTITUCION";
    static public final String C_DESCRIPCION	   = "DESCRIPCION";
	static public final String C_CODIGOEXT		   = "CODIGOEXT  ";
	
		
	
	/**
	 * @return Returns the codigoExt.
	 */
	public String getCodigoExt() {
		return codigoExt;
	}
	/**
	 * @param codigoExt The codigoExt to set.
	 */
	public void setCodigoExt(String codigoExt) {
		this.codigoExt = codigoExt;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	 * @return Returns the idTipoCV.
	 */
	public Integer getIdTipoCV() {
		return idTipoCV;
	}
	/**
	 * @param idTipoCV The idTipoCV to set.
	 */
	public void setIdTipoCV(Integer idTipoCV) {
		this.idTipoCV = idTipoCV;
	}
	/**
	 * @return Returns the idTipoCVSubtipo1.
	 */
	public Integer getIdTipoCVSubtipo1() {
		return idTipoCVSubtipo1;
	}
	/**
	 * @param idTipoCVSubtipo1 The idTipoCVSubtipo1 to set.
	 */
	public void setIdTipoCVSubtipo1(Integer idTipoCVSubtipo1) {
		this.idTipoCVSubtipo1 = idTipoCVSubtipo1;
	}
}