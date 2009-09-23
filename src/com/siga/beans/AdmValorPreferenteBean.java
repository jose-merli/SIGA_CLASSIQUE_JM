package com.siga.beans;

/**
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AdmValorPreferenteBean extends MasterBean {
    
	//Variables
	private Integer idInstitucion;
	private Integer idBoton;
	private String campo;
	private String valor;
	
	
	// Nombre campos de la tabla 
	static public final String C_IDBOTON = "IDBOTON";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_CAMPO = "CAMPO";
	static public final String C_VALOR = "VALOR";
	
	static public final String T_NOMBRETABLA = "ADM_VALORPREFERENTE";
	

    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(Integer valor) {
        this.idInstitucion = valor;
    }
    public Integer getIdBoton() {
        return idBoton;
    }
    public void setIdBoton(Integer valor) {
        this.idBoton = valor;
    }
    public String getCampo() {
        return campo;
    }
    public void setCampo(String valor) {
        this.campo = valor;
    }
    public String getValor() {
        return valor;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }
}
