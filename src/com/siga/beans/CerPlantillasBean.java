package com.siga.beans;

public class CerPlantillasBean extends MasterBean
{
	/* Variables */
	private Integer idInstitucion;
	private Integer idTipoProducto;
	private Integer idProducto;
	private Integer idProductoInstitucion;
	private Integer idPlantilla;
	private String descripcion;
	private String porDefecto;

	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOPRODUCTO = "IDTIPOPRODUCTO";
	static public final String C_IDPRODUCTO = "IDPRODUCTO";
	static public final String C_IDPRODUCTOINSTITUCION = "IDPRODUCTOINSTITUCION";
	static public final String C_IDPLANTILLA = "IDPLANTILLA";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_PORDEFECTO = "PORDEFECTO";
	
	static public final String T_NOMBRETABLA = "CER_PLANTILLAS";
	
	// Métodos SET
    public Integer getIdInstitucion()
    {
        return idInstitucion;
    }
    
    public void setIdInstitucion(Integer idInstitucion)
    {
        this.idInstitucion = idInstitucion;
    }
    
    public Integer getIdTipoProducto()
    {
        return idTipoProducto;
    }
    
    public void setIdTipoProducto(Integer idTipoProducto)
    {
        this.idTipoProducto = idTipoProducto;
    }

    public Integer getIdProducto()
    {
        return idProducto;
    }
    
    public void setIdProducto(Integer idProducto)
    {
        this.idProducto = idProducto;
    }

    public Integer getIdProductoInstitucion()
    {
        return idProductoInstitucion;
    }
    
    public void setIdProductoInstitucion(Integer idProductoInstitucion)
    {
        this.idProductoInstitucion = idProductoInstitucion;
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
}