package com.siga.beans;

import java.util.StringTokenizer;

public class AdmPerfilBean extends MasterBean
{
	/* Variables */
	private String idPerfil;
	private String descripcion;
	private Integer nivelPerfil;
	private String idInstitucion;
	private String roles;

	/* Nombre campos de la tabla */
	static public final String C_IDPERFIL = "IDPERFIL";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_NIVELPERFIL = "NIVELPERFIL";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_ROLES = "ROLES";
	
	static public final String F_ROLES = "F_SIGA_ROLES_PERFIL(" + C_IDINSTITUCION + " , " + C_IDPERFIL + ") AS " + C_ROLES;

	static public final String T_NOMBRETABLA = "ADM_PERFIL";
	
	// Métodos SET
	public void setIdPerfil (String idPerfil)
	{
		this.idPerfil = idPerfil;
	}
	
	public void setDescripcion (String descripcion)
	{
		this.descripcion = descripcion;
	}

	public void setNivelPerfil (Integer nivelPerfil)
	{
		this.nivelPerfil = nivelPerfil;
	}

	public void setIdInstitucion (String idInstitucion)
	{
		this.idInstitucion = idInstitucion;
	}

    public void setRoles(String roles)
    {
        this.roles=roles;
    }

	// Métodos GET
	public String getIdPerfil ()
	{
		return idPerfil;
	}
	
	public String getDescripcion ()
	{
		return descripcion;
	}

	public Integer getNivelPerfil ()
	{
		return nivelPerfil;
	}

	public String getIdInstitucion ()
	{
		return idInstitucion;
	}

    public String getRoles()
    {
        return roles;
    }
    
    public String getIdsRoles()
    {
        if (roles==null || roles.trim().equals(""))
        {
            return roles;
        }
        
        String roles2 = "";
        String rolAux = "";
        
        StringTokenizer st = new StringTokenizer(roles, "*");
        
        while (st.hasMoreTokens())
        {
            rolAux = st.nextToken();
            rolAux = rolAux.trim();
            
            roles2 += rolAux.substring(0, rolAux.indexOf("-")) + ",";
            roles2 = roles2.trim();
        }
        
        return roles2;
    }
    
    public String getDescRoles()
    {
        if (roles==null || roles.trim().equals(""))
        {
            return roles;
        }
        
        String roles2 = "";
        String rolAux = "";
        
        StringTokenizer st = new StringTokenizer(roles, "*");
        
        while (st.hasMoreTokens())
        {
            rolAux = st.nextToken();
            rolAux = rolAux.trim();
            
            roles2 += rolAux.substring(rolAux.indexOf("-")+1) + ",";
            roles2 = roles2.trim();
        }
        
        return roles2;
    }
}
