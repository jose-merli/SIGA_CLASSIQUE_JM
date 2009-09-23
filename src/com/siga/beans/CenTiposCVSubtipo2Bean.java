package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_PRECIOHITO
 * 
 * @author ruben.fernandez
 * @since 6/12/2004
 */

public class CenTiposCVSubtipo2Bean extends MasterBean{
	
	/**
	 *  Variables */ 
	
	private String	codigoExt,descripcion;
	private Integer idTipoCV,idTipoCVSubtipo2,idInstitucion;
	
	/**
	 *  Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "CEN_TIPOSCVSUBTIPO2";
	
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String	C_IDTIPOCV         = 	"IDTIPOCV";
	static public final String 	C_IDTIPOCVSUBTIPO2 = 	"IDTIPOCVSUBTIPO2";
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
	public Integer getIdTipoCVSubtipo2() {
		return idTipoCVSubtipo2;
	}
	/**
	 * @param idTipoCVSubtipo1 The idTipoCVSubtipo1 to set.
	 */
	public void setIdTipoCVSubtipo2(Integer idTipoCVSubtipo2) {
		this.idTipoCVSubtipo2 = idTipoCVSubtipo2;
	}
}