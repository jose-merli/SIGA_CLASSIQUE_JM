package com.siga.beans;

public class EnvCamposPlantillaBean extends MasterBean
{
	/* Variables */
	private Integer idInstitucion;
	private Integer idTipoEnvios;
	private Integer idPlantillaEnvios;
	private Integer idCampo;
	private String tipoCampo;
	private Integer idFormato;
	private String valor;

	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOENVIOS = "IDTIPOENVIOS";
	static public final String C_IDPLANTILLAENVIOS = "IDPLANTILLAENVIOS";
	static public final String C_IDCAMPO = "IDCAMPO";
	static public final String C_TIPOCAMPO = "TIPOCAMPO";
	static public final String C_IDFORMATO = "IDFORMATO";
	static public final String C_VALOR = "VALOR";
	
	static public final String T_NOMBRETABLA = "ENV_CAMPOSPLANTILLA";
	
	// Métodos SET
    public Integer getIdInstitucion()
    {
        return idInstitucion;
    }
    
    public void setIdInstitucion(Integer idInstitucion)
    {
        this.idInstitucion = idInstitucion;
    }
    
    public Integer getIdTipoEnvios()
    {
        return idTipoEnvios;
    }
    
    public void setIdTipoEnvios(Integer idTipoEnvios)
    {
        this.idTipoEnvios = idTipoEnvios;
    }

    public Integer getIdPlantillaEnvios()
    {
        return idPlantillaEnvios;
    }
    
    public void setIdPlantillaEnvios(Integer idPlantillaEnvios)
    {
        this.idPlantillaEnvios = idPlantillaEnvios;
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