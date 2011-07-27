package com.siga.beans;

public class GenTablasMaestrasBean extends MasterBean
{
	/* Variables */
	private String idTablaMaestra;
	private String idCampoCodigo;
	private String idTablaTraduccion;
	private String idCampoDescripcion;
	private String pathAccion;
	private String aliasTabla;
	private String flagBorradoLogico;
	private String flagUsaLenguaje;
	private String longitudCodigo;
	private String longitudDescripcion;
	private String idRecurso;
	private String idLenguaje;
	private String tipoCodigo;
	private String local;
	private Integer aceptabaja;

	/* Nombre campos de la tabla */
	static public final String C_IDTABLAMAESTRA = "IDTABLAMAESTRA";
	static public final String C_IDCAMPOCODIGO = "IDCAMPOCODIGO";
	static public final String C_IDTABLATRADUCCION = "IDTABLATRADUCCION";
	static public final String C_IDCAMPODESCRIPCION = "IDCAMPODESCRIPCION";
	static public final String C_PATH_ACCION = "PATHACCION";
	static public final String C_ALIAS_TABLA = "ALIASTABLA";
	static public final String C_FLAG_BORRADO_LOGICO = "FLAGBORRADOLOGICO";
	static public final String C_FLAG_USA_LENGUAJE = "FLAGUSALENGUAJE";
	static public final String C_LONGITUD_CODIGO = "LONGITUDCODIGO";
	static public final String C_LONGITUD_DESCRIPCION = "LONGITUDDESCRIPCION";
	static public final String C_IDRECURSO = "IDRECURSO";
	static public final String C_IDLENGUAJE = "IDLENGUAJE";
	static public final String C_TIPO_CODIGO = "TIPOCODIGO";
	static public final String C_LOCAL = "LOCAL";
	static public final String C_IDCAMPOBLOQUEADO = "IDCAMPODESCRIPCION";
	static public final String C_ACEPTABAJA = "ACEPTABAJA";
	
	// RGG Cambio para codigo ext
	private String idCampoCodigoExt;
	private String longitudCodigoExt;
	private String tipoCodigoExt;
	static public final String C_IDCAMPOCODIGOEXT = "IDCAMPOCODIGOEXT";
	static public final String C_LONGITUDCODIGOEXT = "LONGITUDCODIGOEXT";
	static public final String C_TIPOCODIGOEXT = "TIPOCODIGOEXT";
    public String getIdCampoCodigoExt()
    {
        return idCampoCodigoExt;
    }
    public String getLongitudCodigoExt()
    {
        return longitudCodigoExt;
    }
    public String getTipoCodigoExt()
    {
        return tipoCodigoExt;
    }
    public void setIdCampoCodigoExt(String val )
    {
        idCampoCodigoExt=val;
    }
    public void setLongitudCodigoExt(String val )
    {
        longitudCodigoExt=val;
    }
    public void setTipoCodigoExt(String val )
    {
        tipoCodigoExt=val;
    }
	// fin Cambio para codigo ext


	static public final String T_NOMBRETABLA = "GEN_TABLAS_MAESTRAS";
	
	// Métodos SET
    public String getIdTablaMaestra()
    {
        return idTablaMaestra;
    }
    
    public void setIdTablaMaestra(String idTablaMaestra) 
    {
        this.idTablaMaestra = idTablaMaestra;
    }
    
    public String getIdCampoCodigo()
    {
        return idCampoCodigo;
    }
    
    public void setIdCampoCodigo(String idCampoCodigo) 
    {
        this.idCampoCodigo = idCampoCodigo;
    }
    
    public String getIdTablaTraduccion() 
    {
        return idTablaTraduccion;
    }
    
    public void setIdTablaTraduccion(String idTablaTraduccion) 
    {
        this.idTablaTraduccion = idTablaTraduccion;
    }
    
    public String getIdCampoDescripcion()
    {
        return idCampoDescripcion;
    }
    
    public void setIdCampoDescripcion(String idCampoDescripcion) 
    {
        this.idCampoDescripcion = idCampoDescripcion;
    }
    
    public String getPathAccion() 
    {
        return pathAccion;
    }
    
    public void setPathAccion(String pathAccion)
    {
        this.pathAccion = pathAccion;
    }

    public String getAliasTabla() 
    {
        return aliasTabla;
    }
    
    public void setAliasTabla(String aliasTabla)
    {
        this.aliasTabla = aliasTabla;
    }
    
    public String getFlagBorradoLogico() 
    {
        return flagBorradoLogico;
    }
    
    public void setFlagBorradoLogico(String flagBorradoLogico)
    {
        this.flagBorradoLogico = flagBorradoLogico;
    }
    
    public String getFlagUsaLenguaje()
    {
        return flagUsaLenguaje;
    }
    
    public void setFlagUsaLenguaje(String flagUsaLenguaje)
    {
        this.flagUsaLenguaje = flagUsaLenguaje;
    }

    public String getLongitudCodigo()
    {
        return longitudCodigo;
    }
    
    public void setLongitudCodigo(String longitudCodigo)
    {
        this.longitudCodigo = longitudCodigo;
    }

    public String getLongitudDescripcion()
    {
        return longitudDescripcion;
    }
    
    public void setLongitudDescripcion(String longitudDescripcion)
    {
        this.longitudDescripcion = longitudDescripcion;
    }

    public String getIdRecurso()
    {
        return idRecurso;
    }
    
    public void setIdRecurso(String idRecurso)
    {
        this.idRecurso = idRecurso;
    }

    public String getIdLenguaje()
    {
        return idLenguaje;
    }
    
    public void setIdLenguaje(String idLenguaje)
    {
        this.idLenguaje = idLenguaje;
    }

    public String getTipoCodigo()
    {
        return tipoCodigo;
    }
    
    public void setTipoCodigo(String tipoCodigo)
    {
        this.tipoCodigo = tipoCodigo;
    }

    public String getLocal()
    {
        return local;
    }
    
    public void setLocal(String local)
    {
        this.local = local;
    }
	public Integer getAceptabaja() {
		return aceptabaja;
	}
	public void setAceptabaja(Integer aceptabaja) {
		this.aceptabaja = aceptabaja;
	}
    
    
}
