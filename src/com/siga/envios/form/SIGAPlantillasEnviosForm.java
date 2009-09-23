package com.siga.envios.form;

import com.siga.general.MasterForm;

public class SIGAPlantillasEnviosForm extends MasterForm
{
//    private String modo="";
    private String descripcionPlantilla="";
    private String idTipoEnvios="";
    private String plantilla="";
    private String idTipoEnvio="";

//    public String getModo() 
//    {
//        return modo;
//    }
//    
//    public void setModo(String modo) 
//    {
//        this.modo = modo;
//    }
    
    public String getDescripcionPlantilla()
    {
    	return descripcionPlantilla;
    }
    
    public void setDescripcionPlantilla(String descripcionPlantilla)
    {
    	this.descripcionPlantilla=descripcionPlantilla;
    }

    public String getIdTipoEnvios()
    {
    	return idTipoEnvios;
    }
    
    public void setIdTipoEnvios(String idTipoEnvios)
    {
    	this.idTipoEnvios=idTipoEnvios;
    }

    public String getPlantilla()
    {
    	return plantilla;
    }
    
    public void setPlantilla(String descPlantilla)
    {
    	this.plantilla=descPlantilla;
    }

    public String getIdTipoEnvio()
    {
    	return idTipoEnvio;
    }
    
    public void setIdTipoEnvio(String idTipoEnvio)
    {
    	this.idTipoEnvio=idTipoEnvio;
    }
}