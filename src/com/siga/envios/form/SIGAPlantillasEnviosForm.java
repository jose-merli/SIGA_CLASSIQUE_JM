package com.siga.envios.form;

import com.siga.general.MasterForm;

public class SIGAPlantillasEnviosForm extends MasterForm
{
//    private String modo="";
    private String descripcionPlantilla="";
    private String idTipoEnvios="";
    private String plantilla="";
    
    private String idTipoEnvio="";
    private String idPlantillaEnvios;
    private String acuseRecibo;
    private String checkFechaBaja;
    

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

	public String getIdPlantillaEnvios() {
		return idPlantillaEnvios;
	}

	public void setIdPlantillaEnvios(String idPlantillaEnvios) {
		this.idPlantillaEnvios = idPlantillaEnvios;
	}

	public String getAcuseRecibo() {
		return acuseRecibo;
	}

	public void setAcuseRecibo(String acuseRecibo) {
		this.acuseRecibo = acuseRecibo;
	}

	public String getCheckFechaBaja() {
		return checkFechaBaja;
	}

	public void setCheckFechaBaja(String checkFechaBaja) {
		this.checkFechaBaja = checkFechaBaja;
	}

}