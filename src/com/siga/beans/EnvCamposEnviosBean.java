package com.siga.beans;

public class EnvCamposEnviosBean extends MasterBean
{
	/* Variables */
	private Integer idInstitucion;
	private Integer idEnvio;
	private Integer idCampo;
	private String tipoCampo;
	private Integer idFormato;
	private String valor;

	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDENVIO = "IDENVIO";
	static public final String C_IDCAMPO = "IDCAMPO";
	static public final String C_TIPOCAMPO = "TIPOCAMPO";
	static public final String C_IDFORMATO = "IDFORMATO";
	static public final String C_VALOR = "VALOR";
	
	static public final String T_NOMBRETABLA = "ENV_CAMPOSENVIOS";
	
	// Métodos SET
    public Integer getIdInstitucion()
    {
        return idInstitucion;
    }
    
    public void setIdInstitucion(Integer idInstitucion)
    {
        this.idInstitucion = idInstitucion;
    }
    
    public Integer getIdEnvio()
    {
        return idEnvio;
    }
    
    public void setIdEnvio(Integer idEnvio)
    {
        this.idEnvio = idEnvio;
    }

    public Integer getIdCampo()
    {
        return idCampo;
    }
    
    public void setIdCampo(Integer idCampo)
    {
        this.idCampo = idCampo;
    }

    public String getTipoCampo()
    {
        return tipoCampo;
    }
    
    public void setTipoCampo(String tipoCampo)
    {
        this.tipoCampo = tipoCampo;
    }

    public Integer getIdFormato()
    {
        return idFormato;
    }
    
    public void setIdFormato(Integer idFormato)
    {
        this.idFormato = idFormato;
    }

    public String getValor()
    {
        return valor;
    }
    
    public void setValor(String valor)
    {
        this.valor = valor;
    }
}