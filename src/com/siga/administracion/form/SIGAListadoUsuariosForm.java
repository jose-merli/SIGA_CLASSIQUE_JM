package com.siga.administracion.form;

import com.siga.general.MasterForm;

public class SIGAListadoUsuariosForm extends MasterForm
{
//    private String modo="";
    private String modal="";
    private String activoConsulta="";
    private String idUsuario="";
    private String idInstitucion="";
    private String descripcion="";
    private String idLenguaje="";
    private String NIF="";
    private String activo="N";
    private String fechaAlta="";
    private String codigoExt="";
    
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
    
    public String getIdInstitucion() 
    {
        return idInstitucion;
    }
    
    public void setIdInstitucion(String idInstitucion) 
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
    
    public String getIdUsuario() 
    {
        return idUsuario;
    }
    
    public void setIdUsuario(String idUsuario) 
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
    public String getActivoConsulta() 
    {
        return activoConsulta;
    }
    
    public void setActivoConsulta(String activoConsulta) 
    {
        this.activoConsulta = activoConsulta;
    }

	public String getCodigoExt() {
		return codigoExt;
	}

	public void setCodigoExt(String codigoExt) {
		this.codigoExt = codigoExt;
	}
    
}