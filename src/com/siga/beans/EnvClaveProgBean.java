/*
 * Created on Ag 17, 2009
 * @author jtacosta
 *
 */
package com.siga.beans;

public class EnvClaveProgBean extends MasterBean{
    
	//Variables
    


private String idTipoInforme;
private String clave;
private String tipo;

	static public final String C_IDTIPOINFORME = "IDTIPOINFORME";
	static public final String C_CLAVE = "CLAVE";
	static public final String C_TIPO = "TIPO";
	
	
	static public final String T_NOMBRETABLA = "ENV_CLAVEPROG";


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


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	

	
	


}
