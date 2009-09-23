package com.siga.beans;

/**
 * Bean de la tabla SCS_JUZGADOPROCEDIMIENTO
 * 
 * @author david.sanchezp
 * @since 08/02/2006
 */
public class ScsJuzgadoProcedimientoBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer	idInstitucion, idInstitucionJuzgado, idJuzgado;
	private String	idProcedimiento=null;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_JUZGADOPROCEDIMIENTO";
	
	
	
	/*Nombre de campos de la tabla*/

	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDPROCEDIMIENTO = 		"IDPROCEDIMIENTO";
	static public final String 	C_IDINSTITUCION_JUZG = 		"IDINSTITUCION_JUZG";
	static public final String 	C_IDJUZGADO = 				"IDJUZGADO";
	
		
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
	 * @return Returns the idInstitucionJuzgado.
	 */
	public Integer getIdInstitucionJuzgado() {
		return idInstitucionJuzgado;
	}
	/**
	 * @param idInstitucionJuzgado The idInstitucionJuzgado to set.
	 */
	public void setIdInstitucionJuzgado(Integer idInstitucionJuzgado) {
		this.idInstitucionJuzgado = idInstitucionJuzgado;
	}
	/**
	 * @return Returns the idJuzgado.
	 */
	public Integer getIdJuzgado() {
		return idJuzgado;
	}
	/**
	 * @param idJuzgado The idJuzgado to set.
	 */
	public void setIdJuzgado(Integer idJuzgado) {
		this.idJuzgado = idJuzgado;
	}
	/**
	 * @return Returns the idProcedimiento.
	 */
	public String getIdProcedimiento() {
		return idProcedimiento;
	}
	/**
	 * @param idProcedimiento The idProcedimiento to set.
	 */
	public void setIdProcedimiento(String idProcedimiento) {
		this.idProcedimiento = idProcedimiento;
	}
}