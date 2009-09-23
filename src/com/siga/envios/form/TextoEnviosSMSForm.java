/*
 * VERSIONES:
 * jose.barrientos 		4-6-2009		Creacion	
 *
 */

/**
 * Formulario de apoyo para la gestion de las plantillas de envio de sms  
 */

package com.siga.envios.form;

import java.util.StringTokenizer;
import com.siga.general.MasterForm;

public class TextoEnviosSMSForm extends MasterForm
{
//  private String modo="";
    private String accion = "";
    private String idInstitucion="";
    private String idEnvio="";
    private String cuerpo="";


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

    public void setCuerpo(String cuerpo)
    {
        this.cuerpo = cuerpo;
    }

    public String getCuerpo()
    {
        return cuerpo;
    }
}
