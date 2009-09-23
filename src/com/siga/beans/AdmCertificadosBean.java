package com.siga.beans;

public class AdmCertificadosBean extends MasterBean
{
	/* Variables */
	private Integer idUsuario;
	private Integer idInstitucion;
	private String numSerie;
	private String revocacion;
	private String fechaCad;
	private String NIF;
	private String rol;
	private String roles;

	/* Nombre campos de la tabla */
	static public final String C_IDUSUARIO = "IDUSUARIO";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_NUMSERIE = "NUMSERIE";
	static public final String C_REVOCACION = "REVOCACION";
	static public final String C_FECHACAD = "FECHACAD";
	static public final String C_NIF = "NIF";
	static public final String C_ROL = "ROL";
	static public final String C_IDS_ROLES = "IDSROLES";
	
	static public final String F_ROLES = "F_SIGA_ROLES_CERTIFICADO(" + C_NUMSERIE + " , " + C_IDINSTITUCION + " , "+C_IDUSUARIO+") AS " + C_IDS_ROLES;

	static public final String T_NOMBRETABLA = "ADM_CERTIFICADOS";
	
	// Métodos SET
    public String getRevocacion() 
    {
        return revocacion;
    }
    
    public void setRevocacion(String revocacion) 
    {
        this.revocacion = revocacion;
    }
    
    public String getNumSerie() 
    {
        return numSerie;
    }
    
    public void setNumSerie(String numSerie) 
    {
        this.numSerie = numSerie;
    }
    
    public String getFechaCad() 
    {
        return fechaCad;
    }
    
    public void setFechaCad(String fechaCad) 
    {
        this.fechaCad = fechaCad;
    }
    
    public Integer getIdInstitucion() 
    {
        return idInstitucion;
    }
    
    public void setIdInstitucion(Integer idInstitucion) 
    {
        this.idInstitucion = idInstitucion;
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
    
    public void setNIF(String NIF)
    {
        this.NIF = NIF;
    }

    public String getRol() 
    {
        return rol;
    }
    
    public void setRol(String rol)
    {
        this.rol = rol;
    }

    public String getRoles()
    {
        return roles;
    }
    
    public void setRoles(String roles)
    {
        this.roles = roles;
    }    
}