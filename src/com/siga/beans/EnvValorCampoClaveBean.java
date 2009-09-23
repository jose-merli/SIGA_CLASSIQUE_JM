/*
 * Created on Mar 24, 2009
 * @author jtacosta
 *
 */
package com.siga.beans;

public class EnvValorCampoClaveBean extends EnvDestProgramInformesBean {
    
	//Variables
    

private Long idValor;
private String idTipoInforme;
private String clave;
private String campo ;
private String valor ;

	static public final String C_IDVALOR = "IDVALOR";
	static public final String C_IDTIPOINFORME = "IDTIPOINFORME";
	static public final String C_CLAVE = "CLAVE";
	static public final String C_CAMPO = "CAMPO";
	static public final String C_VALOR = "VALOR";
	
	
	static public final String T_NOMBRETABLA = "ENV_VALORCAMPOCLAVE";

	


	public Long getIdValor() {
		return idValor;
	}


	public void setIdValor(Long idValor) {
		this.idValor = idValor;
	}


	public String getIdTipoInforme() {
		return idTipoInforme;
	}


	public void setIdTipoInforme(String idTipoInforme) {
		this.idTipoInforme = idTipoInforme;
	}


	public String getClave() {
		return clave;
	}


	public void setClave(String clave) {
		this.clave = clave;
	}




	public String getCampo() {
		return campo;
	}


	public void setCampo(String campo) {
		this.campo = campo;
	}


	public String getValor() {
		return valor;
	}


	public void setValor(String valor) {
		this.valor = valor;
	}
	

	
	


}
