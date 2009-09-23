package com.siga.envios.form;

import com.siga.general.MasterForm;
import org.apache.struts.upload.*;

public class SIGAPlantillasEnviosPlantillasForm extends MasterForm
{
//    private String modo="";
    private String plantilla="";
    private String editable="";
    private String idInstitucion="";
    private String idTipoEnvio="";
    private String idPlantillaEnvios="";
    private String idPlantilla="";
    private String descripcion="";
    private String idTipoEnvios="";
    private String porDefecto="";
    private FormFile theFile;
    
    private String descripcionPlantilla="";

//    public String getModo() 
//    {
//        return modo;
//    }
//    
//    public void setModo(String modo) 
//    {
//        this.modo = modo;
//    }
    
    public String getPlantilla()
    {
    	return plantilla;
    }
    
    public void setPlantilla(String plantilla)
    {
    	this.plantilla=plantilla;
    }

    public String getEditable()
    {
    	return editable;
    }
    
    public void setEditable(String editable)
    {
    	this.editable=editable;
    }

    public String getIdInstitucion()
    {
    	return idInstitucion;
    }
    
    public void setIdInstitucion(String idInstitucion)
    {
    	this.idInstitucion=idInstitucion;
    }

    public String getIdTipoEnvios()
    {
    	return idTipoEnvios;
    }
    
    public void setIdTipoEnvios(String idTipoEnvios)
    {
    	this.idTipoEnvios=idTipoEnvios;
    }

    public String getIdTipoEnvio()
    {
    	return idTipoEnvio;
    }
    
    public void setIdTipoEnvio(String idTipoEnvio)
    {
    	this.idTipoEnvio=idTipoEnvio;
    }

    public String getIdPlantillaEnvios()
    {
    	return idPlantillaEnvios;
    }
    
    public void setIdPlantillaEnvios(String idPlantillaEnvios)
    {
    	this.idPlantillaEnvios=idPlantillaEnvios;
    }

    public String getIdPlantilla()
    {
    	return idPlantilla;
    }
    
    public void setIdPlantilla(String idPlantilla)
    {
    	this.idPlantilla=idPlantilla;
    }

    public String getDescripcion()
    {
    	return descripcion;
    }
    
    public void setDescripcion(String descripcion)
    {
    	this.descripcion=descripcion;
    }

    public String getPorDefecto()
    {
    	return porDefecto;
    }
    
    public void setPorDefecto(String porDefecto)
    {
    	this.porDefecto=porDefecto;
    }

    public FormFile getTheFile() 
    {
        return theFile;
    }

    public void setTheFile(FormFile theFile) 
    {
        this.theFile = theFile;
    }

    public String getDescripcionPlantilla()
    {
    	return descripcionPlantilla;
    }
    
    public void setDescripcionPlantilla(String descripcionPlantilla)
    {
    	this.descripcionPlantilla=descripcionPlantilla;
    }
}