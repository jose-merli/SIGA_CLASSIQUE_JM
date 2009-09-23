package com.siga.beans;

public class AdmUsuarioEfectivoBean extends MasterBean
{
	/* Variables */
	private Integer idUsuario;
	private Integer idInstitucion;
	private String idRol;
	private String numSerie;

	/* Nombre campos de la tabla */
	static public final String C_IDUSUARIO = "IDUSUARIO";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDROL = "IDROL";
	static public final String C_NUMSERIE = "NUMSERIE";
	
	static public final String T_NOMBRETABLA = "ADM_USUARIO_EFECTIVO";
	
	// Métodos SET
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

    public String getIdRol() 
    {
        return idRol;
    }
    
    public void setIdRol(String idRol) 
    {
        this.idRol = idRol;
    }
    
    public String getNumSerie() 
    {
        return numSerie;
    }
    
    public void setNumSerie(String numSerie) 
    {
        this.numSerie = numSerie;
    }
}