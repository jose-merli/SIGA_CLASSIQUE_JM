package com.siga.beans;

public class CerProducInstiCampCertifBean extends MasterBean
{
	/* Variables */
	private Integer idInstitucion;
	private Integer idTipoProducto;
	private Integer idProducto;
	private Integer idProductoInstitucion;
	private Integer idCampoCertificado;
	private Integer idFormato;
	private String tipoCampo;
	private String valor;

	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOPRODUCTO = "IDTIPOPRODUCTO";
	static public final String C_IDPRODUCTO = "IDPRODUCTO";
	static public final String C_IDPRODUCTOINSTITUCION = "IDPRODUCTOINSTITUCION";
	static public final String C_IDCAMPOCERTIFICADO = "IDCAMPOCERTIFICADO";
	static public final String C_IDFORMATO = "IDFORMATO";
	static public final String C_TIPOCAMPO = "TIPOCAMPO";
	static public final String C_VALOR = "VALOR";
	
	static public final String T_NOMBRETABLA = "CER_PRODUCINSTICAMPCERTIF";
	
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

    public Integer getIdCampoCertificado()
    {
        return idCampoCertificado;
    }
    
    public void setIdCampoCertificado(Integer idCampoCertificado)
    {
        this.idCampoCertificado = idCampoCertificado;
    }

    public Integer getIdFormato()
    {
        return idFormato;
    }
    
    public void setIdFormato(Integer idFormato)
    {
        this.idFormato = idFormato;
    }

    public String getTipoCampo()
    {
        return tipoCampo;
    }
    
    public void setTipoCampo(String tipoCampo)
    {
        this.tipoCampo = tipoCampo;
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