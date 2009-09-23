/*
 * Created on Dec 28, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.expedientes.form;


import java.util.Vector;

import com.siga.beans.ExpPerfilBean;
import com.siga.general.MasterForm;

/**
 * Formulario para la administración de las fases de los expediente.
 */
public class PermisosTiposExpedientesForm extends MasterForm 
{
//    private String modo="";
    private String modal="";
    private String acceso="";
    private String fechaModificacion;
	private String usuModificacion;
	private String idInstitucion;
	private String idInstitucionTipoExpediente;
    private String idTipoExpediente;
    
    private Vector perfiles=new Vector();
    
    public String getFechaModificacion() {
        return fechaModificacion;
    }
    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
    public String getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(String idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public String getIdInstitucionTipoExpediente() {
        return idInstitucionTipoExpediente;
    }
    public void setIdInstitucionTipoExpediente(
            String idInstitucionTipoExpediente) {
        this.idInstitucionTipoExpediente = idInstitucionTipoExpediente;
    }
    public String getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(String idTipoExpediente) {
        this.idTipoExpediente = idTipoExpediente;
    }
    public String getModal() {
        return modal;
    }
    public void setModal(String modal) {
        this.modal = modal;
    }
//    public String getModo() {
//        return modo;
//    }
//    public void setModo(String modo) {
//        this.modo = modo;
//    }
    public ExpPerfilBean getPerfil(int index) {
        if(index>=perfiles.size()){
            perfiles.setSize(index==perfiles.size()?index+1:index);
            perfiles.add(index,new ExpPerfilBean());
            }
        Object obj=perfiles.get(index);
        if(obj==null) {
            obj=new ExpPerfilBean();
            perfiles.remove(index);
            perfiles.add(index,(ExpPerfilBean)obj);
        }
        return (ExpPerfilBean)obj;
    }
    public void setPerfil(int index, ExpPerfilBean perfil) {
         perfiles.add(index, perfil);        
    }
    public String getUsuModificacion() {
        return usuModificacion;
    }
    public void setUsuModificacion(String usuModificacion) {
        this.usuModificacion = usuModificacion;
    }
    public Vector getPerfiles() {
        return perfiles;
    }
    public void setPerfiles(Vector perfiles) {
        this.perfiles = perfiles;
    }
    public String getAcceso() {
        return acceso;
    }
    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }
}
