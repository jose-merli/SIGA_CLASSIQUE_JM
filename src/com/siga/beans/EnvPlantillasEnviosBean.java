package com.siga.beans;

public class EnvPlantillasEnviosBean extends MasterBean
{
	/* Variables */
	private Integer idInstitucion;
	private Integer idTipoEnvios;
	private Integer idPlantillaEnvios;
	private String nombre;

	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOENVIOS = "IDTIPOENVIOS";
	static public final String C_IDPLANTILLAENVIOS = "IDPLANTILLAENVIOS";
	static public final String C_NOMBRE = "NOMBRE";
	
	static public final String T_NOMBRETABLA = "ENV_PLANTILLASENVIOS";
	
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

    public String getNombre()
    {
        return nombre;
    }
    
    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }
}