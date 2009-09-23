package com.siga.envios.form;

import com.siga.general.MasterForm;

public class SIGAEnviosCorreoElectronicoForm extends MasterForm
{
//    private String modo="";
    private String accion = "";
    private String idInstitucion="";
    private String idEnvio="";
    private String asunto="";
    private String cuerpo="";

//    public void setModo(String modo)
//    {
//        this.modo=modo;
//    }
//
//    public String getModo()
//    {
//        return modo;
//    }

    public void setAccion(String accion)
    {
        this.accion=accion;
    }

    public String getAccion()
    {
        return accion;
    }

    public void setIdInstitucion(String idInstitucion)
    {
        this.idInstitucion = idInstitucion;
    }

    public String getIdInstitucion()
    {
        return idInstitucion;
    }

    public void setIdEnvio(String idEnvio)
    {
        this.idEnvio = idEnvio;
    }

    public String getIdEnvio()
    {
        return idEnvio;
    }

    public void setAsunto(String asunto)
    {
        this.asunto = asunto;
    }

    public String getAsunto()
    {
        return asunto;
    }

    public void setCuerpo(String cuerpo)
    {
        this.cuerpo = cuerpo;
    }

    public String getCuerpo()
    {
        return cuerpo;
    }
}
