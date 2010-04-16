package com.siga.envios.form;

import java.util.StringTokenizer;

import com.siga.beans.EnvPlantillasEnviosBean;
import com.siga.general.MasterForm;

public class SIGAPlantillasEnviosCorreoElectronicoForm extends MasterForm
{
//    private String modo="";
    private String idInstitucion="";
    private String idTipoEnvio="";
    private String idPlantillaEnvios="";
    
    private String asunto="";
    private String cuerpo="";
    private String idFormato="";
    
    private String plantilla="";
    
    private String descripcionPlantilla="";
    private String idTipoEnvios="";
    
    SIGAPlantillasEnviosForm plantillaEnvios;
    boolean editable;
    
    private String botones;
    public String getBotones() {
		
		if (isEditable()){
			this.botones = "V,G";
		}else{
			this.botones = "V";	
			
		}
		
		return this.botones;
	}
    public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
    public String getIdTipoEnvios()
    {
    	return idTipoEnvios;
    }
    
    public void setIdTipoEnvios(String idTipoEnvios)
    {
    	this.idTipoEnvios=idTipoEnvios;
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

    /*public String getTipoCampo()
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
    }*/

    /*public String getValor()
    {
    	return valor;
    }
    
    public void setValor(String valor)
    {
    	this.valor=valor;
    }*/

    /*public String getIdCampo()
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
    }*/

    public String getAsunto()
    {
    	return asunto;
    }
    
    public void setAsunto(String asunto)
    {
    	this.asunto=asunto;
    }

    public String getCuerpo()
    {
    	return cuerpo;
    }
    
    public void setCuerpo(String cuerpo)
    {
    	this.cuerpo=cuerpo;
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

	public SIGAPlantillasEnviosForm getPlantillaEnvios() {
		return plantillaEnvios;
	}

	public void setPlantillaEnvios(SIGAPlantillasEnviosForm plantillaEnvios) {
		this.plantillaEnvios = plantillaEnvios;
	}
}