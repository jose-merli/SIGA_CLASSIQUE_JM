package com.siga.certificados.form;

import com.siga.general.MasterForm;

public class SIGASolicitudCertificadoForm extends MasterForm
{
//    private String modo="";
    private String idPersona=null;
    private String nombre="";
    private String apellido1="";
    private String apellido2="";
    private String nif="";
    private String idInstitucion;
    private String idInstitucionOrigen;
    private String idInstitucionDestino;
    private String descripcion;
    


    public String getApellido1() {
        return apellido1;
    }
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }
    public String getApellido2() {
        return apellido2;
    }
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }
    public String getIdInstitucionDestino() {
        return idInstitucionDestino;
    }
    public void setIdInstitucionDestino(String idInstitucionDestino) {
        this.idInstitucionDestino = idInstitucionDestino;
    }
    public String getIdInstitucionOrigen() {
        return idInstitucionOrigen;
    }
    public void setIdInstitucionOrigen(String idInstitucionOrigen) {
        this.idInstitucionOrigen = idInstitucionOrigen;
    }
//    public String getModo() {
//        return modo;
//    }
//    public void setModo(String modo) {
//        this.modo = modo;
//    }
    public String getNif() {
        return nif;
    }
    public void setNif(String nif) {
        this.nif = nif;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getIdPersona() {
        return idPersona;
    }
    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }
    public String getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(String idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
   
}