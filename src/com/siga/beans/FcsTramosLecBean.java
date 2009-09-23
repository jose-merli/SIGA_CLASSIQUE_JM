//VERSIONES:
//ruben.fernandez 31-03-2005 creacion

package com.siga.beans;

/**
* Implementa las operaciones sobre el bean de la tabla FCS_TRAMOS_LEC
* 
* @author AtosOrigin
* @since 31/03/2005 
*/

public class FcsTramosLecBean extends MasterBean{
	
	/**
	 *  Variables 
	 * 
	 * */ 
	
	private Integer		idTramoLec;
	private Integer		minimosMi;
	private Integer 	maximosMi;
	private Integer 	retencion;
	
	/**
	 *  Nombre de Tabla
	 * */
	
	static public String T_NOMBRETABLA = "FCS_TRAMOS_LEC";
	
	
	/**
	 * Nombre de campos de la tabla*/
	
	static public final String 	C_IDTRAMOLEC = 			"IDTRAMOLEC";
	static public final String 	C_MINIMOSMI = 			"MINIMOSMI";
	static public final String 	C_MAXIMOSMI = 			"MAXIMOSMI";
	static public final String 	C_RETENCION =			"RETENCION";
	
	
	/**
	 * @return Returns the idTramoLec.
	 */
	public Integer getIdTramoLec() {
		return idTramoLec;
	}
	/**
	 * @param idTramoLec The idTramoLec to set.
	 */
	public void setIdTramoLec(Integer idTramoLec) {
		this.idTramoLec = idTramoLec;
	}
	/**
	 * @return Returns the maximosMi.
	 */
	public Integer getMaximosMi() {
		return maximosMi;
	}
	/**
	 * @param maximosMi The maximosMi to set.
	 */
	public void setMaximosMi(Integer maximosMi) {
		this.maximosMi = maximosMi;
	}
	/**
	 * @return Returns the minimosMi.
	 */
	public Integer getMinimosMi() {
		return minimosMi;
	}
	/**
	 * @param minimosMi The minimosMi to set.
	 */
	public void setMinimosMi(Integer minimosMi) {
		this.minimosMi = minimosMi;
	}
	/**
	 * @return Returns the retencion.
	 */
	public Integer getRetencion() {
		return retencion;
	}
	/**
	 * @param retencion The retencion to set.
	 */
	public void setRetencion(Integer retencion) {
		this.retencion = retencion;
	}
}