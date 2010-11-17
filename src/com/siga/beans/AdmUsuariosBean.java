package com.siga.beans;

public class AdmUsuariosBean extends MasterBean
{
	/* Variables */
	private Integer idUsuario;
	private String descripcion;
	private String idLenguaje;
	private Integer idInstitucion;
	private String NIF;
	private String activo;
	private String fechaAlta;
	private String grupos;
	private String codigoExt;

	/* Nombre campos de la tabla */
	static public final String C_IDUSUARIO = "IDUSUARIO";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_IDLENGUAJE = "IDLENGUAJE";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_NIF = "NIF";
	static public final String C_ACTIVO = "ACTIVO";
	static public final String C_FECHA_ALTA = "FECHAALTA";
	static public final String C_IDS_GRUPOS = "IDSGRUPOS";
	static public final String C_CODIGOEXT = "CODIGOEXT";
	
	static public final String F_GRUPOS = "F_SIGA_ROLES_USUARIO(" + C_IDINSTITUCION + " , " + C_IDUSUARIO + ") AS " + C_IDS_GRUPOS;

	static public final String T_NOMBRETABLA = "ADM_USUARIOS";
	
	// Métodos SET
    public String getActivo() 
    {
        return activo;
    }
    
    public void setActivo(String activo) 
    {
        this.activo = activo;
    }
    
    public String getDescripcion() 
    {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) 
    {
        this.descripcion = descripcion;
    }
    
    public String getFechaAlta() 
    {
        return fechaAlta;
    }
    
    public void setFechaAlta(String fechaAlta) 
    {
        this.fechaAlta = fechaAlta;
    }
    
    public Integer getIdInstitucion() 
    {
        return idInstitucion;
    }
    
    public void setIdInstitucion(Integer idInstitucion) 
    {
        this.idInstitucion = idInstitucion;
    }
    
    public String getIdLenguaje() 
    {
        return idLenguaje;
    }
    
    public void setIdLenguaje(String idLenguaje) 
    {
        this.idLenguaje = idLenguaje;
    }

    public Integer getIdUsuario() 
    {
        return idUsuario;
    }
    
    public void setIdUsuario(Integer idUsuario) 
    {
        this.idUsuario = idUsuario;
    }
    
    public String getNIF() 
    {
        return NIF;
    }
    
    public void setNIF(String nif) 
    {
        NIF = nif;
    }
    
    public String getGrupos()
    {
        return grupos;
    }
    
    public void setGrupos(String grupos)
    {
        this.grupos=grupos;
    }

	public String getCodigoExt() {
		return codigoExt;
	}

	public void setCodigoExt(String codigoExt) {
		this.codigoExt = codigoExt;
	}    
    
    
}
