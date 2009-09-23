package com.siga.beans;

public class AdmUsuariosPerfilBean extends MasterBean
{
	/* Variables */
	private Integer usuario;
	private Integer institucion;
	private String  perfil;

	/* Nombre campos de la tabla */
	static public final String C_IDUSUARIO = "IDUSUARIO";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDPERFIL = "IDPERFIL";
	
	static public final String T_NOMBRETABLA = "ADM_USUARIOS_PERFIL";
	
	// Métodos SET
    public Integer getUsuario()
    {
        return usuario;
    }
    
    public void setUsuario(Integer usuario)
    {
        this.usuario = usuario;
    }
    
    public Integer getInstitucion()
    {
        return institucion;
    }
    
    public void setInstitucion(Integer institucion)
    {
        this.institucion = institucion;
    }
    
    public String getPerfil()
    {
        return perfil;
    }
    
    public void setPerfil(String perfil)
    {
        this.perfil = perfil;
    }
}
