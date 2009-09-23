/*
 * Created on Dec 27, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;


public class ExpCamposValorBean extends MasterBean {
    
	//Variables
    private Integer idInstitucion;
	private Integer idInstitucion_TipoExpediente;
	private Integer idTipoExpediente;
	private Integer idCampo;
	private Integer numeroExpediente;
	private Integer anioExpediente;
	private Integer idPestanaConf;
	private Integer idCampoConf;
	private String valor;
	
	
	// Nombre campos de la tabla 
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDINSTITUCION_TIPOEXPEDIENTE = "IDINSTITUCION_TIPOEXPEDIENTE";
	static public final String C_IDTIPOEXPEDIENTE = "IDTIPOEXPEDIENTE";
	static public final String C_IDCAMPO = "IDCAMPO";
	static public final String C_NUMEROEXPEDIENTE = "NUMEROEXPEDIENTE";
	static public final String C_ANIOEXPEDIENTE = "ANIOEXPEDIENTE";
	static public final String C_IDPESTANACONF = "IDPESTANACONF";
	static public final String C_IDCAMPOCONF="IDCAMPOCONF";
	static public final String C_VALOR = "VALOR";
	
	static public final String T_NOMBRETABLA = "EXP_CAMPOSVALOR";
	
	
    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(Integer valor) {
        this.idInstitucion = valor;
    }

    public Integer getIdInstitucion_TipoExpediente() {
        return idInstitucion_TipoExpediente;
    }
    public void setIdInstitucion_TipoExpediente(Integer valor) {
        this.idInstitucion_TipoExpediente = valor;
    }

    public Integer getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(Integer valor) {
        this.idTipoExpediente = valor;
    }

    public Integer getIdCampo() {
        return idCampo;
    }
    public void setIdCampo(Integer valor) {
        this.idCampo = valor;
    }

    public Integer getNumeroExpediente() {
        return numeroExpediente;
    }
    public void setNumeroExpediente(Integer valor) {
        this.numeroExpediente = valor;
    }

    public Integer getAnioExpediente() {
        return anioExpediente;
    }
    public void setAnioExpediente(Integer valor) {
        this.anioExpediente = valor;
    }

    public Integer getIdPestanaConf() {
        return idPestanaConf;
    }
    public void setIdPestanaConf(Integer valor) {
        this.idPestanaConf = valor;
    }

    public Integer getIdCampoConf() {
        return idCampoConf;
    }
    public void setIdCampoConf(Integer valor) {
        this.idCampoConf = valor;
    }

    public String getValor() {
        return valor;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }

}
