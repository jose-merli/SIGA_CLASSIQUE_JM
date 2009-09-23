package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla FCS_PAGO_GRUPOFACT_HITO
 * 
 * @author david.sanchezp
 * @since 31/03/2005 
 */
public class FcsPagoGrupoFactHitoBean extends MasterBean{
	
	/**
	 *  Variables 
	 * */ 
	
	private Integer		idInstitucion;
	private Integer		idPagosJG;
	private Integer 	idGrupoFacturacion;
	private Integer 	idHitoGeneral;
	
		
	/**
	 *  Nombre de Tabla
	 * */
	
	static public String T_NOMBRETABLA = "FCS_PAGO_GRUPOFACT_HITO";
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_IDINSTITUCION = 		"IDINSTITUCION";
	static public final String 	C_IDPAGOSJG 	= 		"IDPAGOSJG";
	static public final String 	C_IDGRUPOFACTURACION = 	"IDGRUPOFACTURACION";
	static public final String 	C_IDHITOGENERAL = 		"IDHITOGENERAL";
	
	/**
	 * @return Returns the idPagosJG.
	 */
	public Integer getIdPagosJG() {
		return idPagosJG;
	}
	/**
	 * @param idPagosJG The idPagosJG to set.
	 */
	public void setIdPagosJG(Integer idPagosJG) {
		this.idPagosJG = idPagosJG;
	}
	/**
	 * @return Returns the idGrupoFacturacion.
	 */
	public Integer getIdGrupoFacturacion() {
		return idGrupoFacturacion;
	}
	/**
	 * @param idGrupoFacturacion The idGrupoFacturacion to set.
	 */
	public void setIdGrupoFacturacion(Integer idGrupoFacturacion) {
		this.idGrupoFacturacion = idGrupoFacturacion;
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
	 * @return Returns the idHitoGeneral
	 */
	public Integer getIdHitoGeneral() {
		return idHitoGeneral;
	}
	/**
	 * @param idInstitucion The idIHitoGeneral to set.
	 */
	public void setIdHitoGeneral(Integer idHitoGeneral) {
		this.idHitoGeneral = idHitoGeneral;
	}
}