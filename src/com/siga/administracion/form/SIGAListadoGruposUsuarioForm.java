package com.siga.administracion.form;

import com.siga.general.MasterForm;

public class SIGAListadoGruposUsuarioForm extends MasterForm
{
//    private String modo="";
    private String modal="";
    private String descripcion="";
    private String roles="";
    private String rolesAntiguos="";
    private String perfil="";
    
//    public String getModo() 
//    {
//        return modo;
//    }
//    
//    public void setModo(String modo) 
//    {
//        this.modo = modo;
//    }

    public String getModal() 
    {
        return modal;
    }
    
    public void setModal(String modal) 
    {
        this.modal = modal;
    }

    public void setDescripcion(String descripcion) 
    {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() 
    {
        return descripcion;
    }
    
    public void setRoles(String roles) 
    {
        this.roles = roles;
    }

    public String getRoles() 
    {
        return roles;
    }

    public void setRolesAntiguos(String rolesAntiguos) 
    {
        this.rolesAntiguos = rolesAntiguos;
    }

    public String getRolesAntiguos() 
    {
        return rolesAntiguos;
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