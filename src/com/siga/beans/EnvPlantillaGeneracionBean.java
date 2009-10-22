package com.siga.beans;

public class EnvPlantillaGeneracionBean extends MasterBean
{
	/* Variables */
	private Integer idInstitucion;
	private Integer idTipoEnvios;
	private Integer idPlantillaEnvios;
	private Integer idPlantilla;
	private String descripcion;
	private String porDefecto;
	private String tipoArchivo;

	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOENVIOS = "IDTIPOENVIOS";
	static public final String C_IDPLANTILLAENVIOS = "IDPLANTILLAENVIOS";
	static public final String C_IDPLANTILLA = "IDPLANTILLA";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_PORDEFECTO = "PORDEFECTO";
	static public final String C_TIPOARCHIVO = "TIPOARCHIVO";
	
	static public final String T_NOMBRETABLA = "ENV_PLANTILLAGENERACION";
	
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

    public Integer getIdPlantilla()
    {
        return idPlantilla;
    }
    
    public void setIdPlantilla(Integer idPlantilla)
    {
        this.idPlantilla = idPlantilla;
    }

    public String getDescripcion()
    {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }
    
    public String getPorDefecto()
    {
        return porDefecto;
    }
    
    public void setPorDefecto(String porDefecto)
    {
        this.porDefecto = porDefecto;
    }

	public String getTipoArchivo() {
		return tipoArchivo;
	}

	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}
}