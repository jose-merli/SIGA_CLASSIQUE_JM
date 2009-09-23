package com.siga.certificados.form;

import com.siga.general.MasterForm;

public class SIGAMantenimientoCertificadosForm extends MasterForm
{
//    private String modo="";
    private String certificado="";

//    public String getModo() 
//    {
//        return modo;
//    }
//    
//    public void setModo(String modo) 
//    {
//        this.modo = modo;
//    }
    
    public String getCertificado()
    {
    	return certificado;
    }
    
    public void setCertificado(String certificado)
    {
    	this.certificado=certificado;
    }
}