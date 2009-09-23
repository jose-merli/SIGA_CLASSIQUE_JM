package com.siga.administracion.form;

import com.siga.general.MasterForm;

public class SIGAListadoPerfilRolForm extends MasterForm
{
//    private String modo="";
    private String modal="";
    private String descripcion="";
    private String idInstitucion="";
    private String idPerfil="";
    private String idRol="";
    private String grupoPorDefecto="N";
    
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
    
    public String getDescripcion()
    {
    	return descripcion;
    }
    
    public void setDescripcion(String descripcion)
    {
    	this.descripcion = descripcion;
    }

    public String getIdInstitucion() 
    {
        return idInstitucion;
    }
    
    public void setIdInstitucion(String idInstitucion) 
    {
        this.idInstitucion = idInstitucion;
    }
    
    public String getIdPerfil() 
    {
        return idPerfil;
    }
    
    public void setIdPerfil(String idPerfil) 
    {
        this.idPerfil = idPerfil;
    }
    
    public String getIdRol() 
    {
        return idRol;
    }
    
    public void setIdRol(String idRol) 
    {
        this.idRol = idRol;
    }
    
    public String getGrupoPorDefecto() 
    {
        return grupoPorDefecto;
    }
    
    public void setGrupoPorDefecto(String grupoPorDefecto) 
    {
        this.grupoPorDefecto = grupoPorDefecto;
    }
}