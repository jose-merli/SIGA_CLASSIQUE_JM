package com.siga.administracion.form;

import com.siga.general.MasterForm;

public class SIGAListadoCertificadosForm extends MasterForm
{
//    private String modo="";
    private String modal="";
    private String revocacionConsulta="";
    private String idUsuario="";
    private String idInstitucion="";
    private String descripcion="";
    private String revocacion="N";
    private String fechaAlta="";
    
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

    public String getRevocacion() 
    {
        return revocacion;
    }
    
    public void setRevocacion(String revocacion) 
    {
        this.revocacion = revocacion;
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
    
    public String getIdInstitucion() 
    {
        return idInstitucion;
    }
    
    public void setIdInstitucion(String idInstitucion) 
    {
        this.idInstitucion = idInstitucion;
    }
    
    public String getIdUsuario() 
    {
        return idUsuario;
    }
    
    public void setIdUsuario(String idUsuario) 
    {
        this.idUsuario = idUsuario;
    }
    
    public String getRevocacionConsulta() 
    {
        return revocacionConsulta;
    }
    
    public void setRevocacionConsulta(String revocacionConsulta) 
    {
        this.revocacionConsulta = revocacionConsulta;
    }
}