package com.siga.envios.form;

import com.siga.general.MasterForm;
import org.apache.struts.upload.*;

public class DocumentosForm extends MasterForm
{
//    private String modo="";
    private String editable="";
    private String idInstitucion="";
    private String idEnvio="";
    private String idDocumento="";
    private String descripcion="";
    private String pathDocumento="";
    
    private FormFile theFile;
    

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getEditable() {
        return editable;
    }
    public void setEditable(String editable) {
        this.editable = editable;
    }
    public String getIdDocumento() {
        return idDocumento;
    }
    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }
    public String getIdEnvio() {
        return idEnvio;
    }
    public void setIdEnvio(String idEnvio) {
        this.idEnvio = idEnvio;
    }
    public String getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(String idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
//    public String getModo() {
//        return modo;
//    }
//    public void setModo(String modo) {
//        this.modo = modo;
//    }
    public FormFile getTheFile() {
        return theFile;
    }
    public void setTheFile(FormFile theFile) {
        this.theFile = theFile;
    }
    public String getPathDocumento() {
        return pathDocumento;
    }
    public void setPathDocumento(String pathDocumento) {
        this.pathDocumento = pathDocumento;
    }
}