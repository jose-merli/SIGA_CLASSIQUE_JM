// VERSIONES:
// raul.ggonzalez 11-03-2005 creacion

package com.siga.beans;

/**
 * Implementa las operaciones sobre el bean de la tabla SCS_GRUPOFACTURACION
 * 
 * @author AtosOrigin
 * @since 09/03/2005 
 */
 
public class FcsFactGrupoFactHitoBean extends MasterBean{
	
	/**
	 *  Variables 
	 * */ 
	
	private Integer		idInstitucion;
	private Integer		idFacturacion;
	private Integer 	idGrupoFacturacion;
	private Integer 	idHitoGeneral;
	
		
	/**
	 *  Nombre de Tabla
	 * */
	
	static public String T_NOMBRETABLA = "FCS_FACT_GRUPOFACT_HITO";
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDFACTURACION = 			"IDFACTURACION";
	static public final String 	C_IDGRUPOFACTURACION = 		"IDGRUPOFACTURACION";
	static public final String 	C_IDHITOGENERAL = 		"IDHITOGENERAL";
	
		
		
	
	/**
	 * @return Returns the idFacturacion.
	 */
	public Integer getIdFacturacion() {
		return idFacturacion;
	}
	/**
	 * @param idFacturacion The idFacturacion to set.
	 */
	public void setIdFacturacion(Integer idFacturacion) {
		this.idFacturacion = idFacturacion;
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