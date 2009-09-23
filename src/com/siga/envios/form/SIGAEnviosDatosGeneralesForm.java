package com.siga.envios.form;

import java.util.StringTokenizer;

import com.siga.general.MasterForm;

/**
 * Formulario para la definición de destinatarios manuales.
 */
public class SIGAEnviosDatosGeneralesForm extends MasterForm {
	
//    private String modo="";
    private String modal="";
    private String accion = "";
    private String idEnvio;
    private String idTipoEnvio;
	private String idPersona;
	private String idInstitucion;
	
	private String idPlantillaEnvio="";
	private String idPlantillaGeneracion="";
	private String idCampo="";
	private String tipoCampo="";
	private String idFormato="";
	private String valor="";
	private String descripcionEnvio="";
    
    public String getIdEnvio()
    {
        return idEnvio;
    }
    
    public void setIdEnvio(String idEnvio)
    {
        this.idEnvio = idEnvio;
    }
    
    public String getIdPersona()
    {
        return idPersona;
    }
    
    public void setIdPersona(String idPersona)
    {
        this.idPersona = idPersona;
    }
    
    public String getModal()
    {
        return modal;
    }
    
    public void setModal(String modal)
    {
        this.modal = modal;
    }
    
//    public String getModo()
//    {
//        return modo;
//    }
//    
//    public void setModo(String modo)
//    {
//        this.modo = modo;
//    }    

    public String getIdInstitucion()
    {
        return idInstitucion;
    }
    
    public void setIdInstitucion(String idInstitucion)
    {
        this.idInstitucion = idInstitucion;
    }
    
    public String getAccion()
    {
        return accion;
    }
    
    public void setAccion(String accion)
    {
        this.accion = accion;
    }
    
    public String getIdTipoEnvio()
    {
        return idTipoEnvio;
    }
    
    public void setIdTipoEnvio(String idTipoEnvio)
    {
        this.idTipoEnvio = idTipoEnvio;
    }
    
    public String getIdPlantillaEnvio()
    {
        return idPlantillaEnvio;
    }
    
    public void setIdPlantillaEnvio(String idPlantillaEnvio)
    {
        this.idPlantillaEnvio=idPlantillaEnvio;
    }

    public String getIdPlantillaGeneracion()
    {
        return idPlantillaGeneracion;
    }
    
    public void setIdPlantillaGeneracion(String idPlantillaGeneracion)
    {
        this.idPlantillaGeneracion=idPlantillaGeneracion;
    }

    public String getIdCampo()
    {
        return idCampo;
    }
    
    public void setIdCampo(String idCampo)
    {
        this.idCampo=idCampo;
    }

    public String getTipoCampo()
    {
        return tipoCampo;
    }
    
    public void setTipoCampo(String tipoCampo)
    {
        this.tipoCampo=tipoCampo;
    }
    
    public String getIdFormato()
    {
        StringTokenizer st = new StringTokenizer(idFormato, ",");
        
        return st.nextToken();
    }
    
    public void setIdFormato(String idFormato)
    {
        this.idFormato=idFormato;
    }
    
    public String getValor()
    {
        return valor;
    }
    
    public void setValor(String valor)
    {
        this.valor=valor;
    }

    public String getDescripcionEnvio()
    {
        return descripcionEnvio;
    }
    
    public void setDescripcionEnvio(String descripcionEnvio)
    {
        this.descripcionEnvio=descripcionEnvio;
    }
}