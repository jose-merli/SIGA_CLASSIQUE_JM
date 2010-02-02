
package com.siga.beans;
/**
 * 
 * @author jorgeta
 *
 */

public class ScsParentescoBean extends MasterBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6071319360435820291L;
	
	private Integer idParentesco;
	private String 	descripcion;
	private String codigoExt;
	private String bloqueado;
	private Integer idInstitucion;
	
//	 Nombre tabla 
	static public String T_NOMBRETABLA = "SCS_PARENTESCO";
	
//	 Nombre campos de la tabla 
	static public final String C_IDPARENTESCO		= "IDPARENTESCO";
	static public final String C_DESCRIPCION				= "DESCRIPCION";
	static public final String C_CODIGOEXT					= "CODIGOEXT";
	static public final String C_BLOQUEADO					= "BLOQUEADO";
	static public final String C_IDINSTITUCION				= "IDINSTITUCION";
	public Integer getIdParentesco() {
		return idParentesco;
	}
	public void setIdParentesco(Integer idParentesco) {
		this.idParentesco = idParentesco;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCodigoExt() {
		return codigoExt;
	}
	public void setCodigoExt(String codigoExt) {
		this.codigoExt = codigoExt;
	}
	public String getBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	
}
