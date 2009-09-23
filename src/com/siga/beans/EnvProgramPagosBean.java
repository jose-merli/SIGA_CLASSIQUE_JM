/*
 * Created on Mar 24, 2009
 * @author jtacosta
 *
 */
package com.siga.beans;

public class EnvProgramPagosBean extends EnvProgramBean {
    
	//Variables
    
	private Integer idPago;
	private Long idPersona;
	
	
	static public final String C_IDPAGO = "IDPAGO";
	static public final String T_NOMBRETABLA = "ENV_PROGRAMPAGOS";
	static public final String C_IDPERSONA = "IDPERSONA";
	public Long getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Integer getIdPago() {
		return idPago;
	}

	public void setIdPago(Integer idPago) {
		this.idPago = idPago;
	}

}
