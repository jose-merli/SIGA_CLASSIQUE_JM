package com.siga.envios.form;

import java.util.StringTokenizer;
import com.siga.general.MasterForm;

public class SIGAPlantillasEnviosCamposForm extends MasterForm
{
//    private String modo="";
    private String editable="";

    private String idInstitucion="";
    private String idTipoEnvio="";
    private String idPlantillaEnvios="";
    
    private String idCampo="";
    private String idFormato="";
    private String tipoCampo="";
    private String valor="";
    
    private String plantilla="";
    
    private String descripcionPlantilla="";
    private String idTipoEnvios="";

//    public String getModo() 
//    {
//        return modo;
//    }
//    
//    public void setModo(String modo) 
//    {
//        this.modo = modo;
//    }
    
    public String getIdTipoEnvios()
    {
    	return idTipoEnvios;
    }
    
    public void setIdTipoEnvios(String idTipoEnvios)
    {
    	this.idTipoEnvios=idTipoEnvios;
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

    public String getIdFormato()
    {
    	return idFormato;
    }
    
    public void setIdFormato(String idFormato)
    {
        try
        {
	        StringTokenizer st = new StringTokenizer(idFormato, ",");
	        
	    	this.idFormato=st.nextToken();
        }
	    	
	    catch(Exception e)
	    {
	        this.idFormato="";
	    }
    }

    public String getTipoCampo()
    {
    	return tipoCampo; 
    }
    
    public void setTipoCampo(String tipoCampo)
    {
        try
        {
	        StringTokenizer st = new StringTokenizer(tipoCampo, ",");
	        
	    	this.tipoCampo=st.nextToken();
        }
        
        catch(Exception e)
        {
            this.tipoCampo="";
        }
    }

    public String getValor()
    {
    	return valor;
    }
    
    public void setValor(String valor)
    {
    	this.valor=valor;
    }

    public String getIdCampo()
    {
    	return idCampo;
    }
    
    public void setIdCampo(String idCampo)
    {
        if (idCampo.indexOf(",")>-1)
        {
            StringTokenizer st = new StringTokenizer(idCampo, ",");
        
            st.nextToken();
        
            this.idCampo=st.nextToken();
        }
        
        else
        {
            this.idCampo=idCampo;
        }
    }
    
    public String getDescripcionPlantilla()
    {
    	return descripcionPlantilla;
    }
    
    public void setDescripcionPlantilla(String descripcionPlantilla)
    {
    	this.descripcionPlantilla=descripcionPlantilla;
    }

    public String getPlantilla()
    {
    	return plantilla;
    }
    
    public void setPlantilla(String plantilla)
    {
    	this.plantilla=plantilla;
    }
}