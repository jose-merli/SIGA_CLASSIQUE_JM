/*
 * Created on Apr 05, 2005
 * @author juan.grau
 *
 */
package com.siga.envios.form;

import com.siga.general.MasterForm;

/**
 * Formulario para la definición de remitentes manuales.
 */
public class RemitentesForm extends MasterForm {
	
//    private String modo="";
    private String modal="";
    private String accion = "";
    private String idEnvio;
    private String idTipoEnvio;
	private String idPersona;
	private String idInstitucion;
	private String domicilio = "";
	private String codigoPostal = "";
	private String fax1 = "";
	private String fax2 = "";
	private String correoElectronico = "";
	private String movil = "";
	private String pais = "";
    private String provincia = "";
	private String poblacion = "";
	private String idPais="";
    private String idProvincia="";
	private String idPoblacion="";
	private String poblacionExt="";
	private String descripcion="";
	private String idDireccion="";
	
	private String nombre;
	private String apellidos1;
	private String apellidos2;
    private String nifcif;
	private String numColegiado;
	
	private String idTipoExpediente;
	
    
    public String getIdEnvio() {
        return idEnvio;
    }
    public void setIdEnvio(String idEnvio) {
        this.idEnvio = idEnvio;
    }
    public String getIdPersona() {
        return idPersona;
    }
    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
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

    public String getCodigoPostal() {
        return codigoPostal;
    }
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    public String getCorreoElectronico() {
        return correoElectronico;
    }
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
    public String getMovil() {
        return movil;
    }
    public void setMovil(String val) {
        this.movil = val;
    }
    public String getDomicilio() {
        return domicilio;
    }
    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }
    public String getFax1() {
        return fax1;
    }
    public void setFax1(String fax1) {
        this.fax1 = fax1;
    }
    public String getFax2() {
        return fax2;
    }
    public void setFax2(String fax2) {
        this.fax2 = fax2;
    }
    public String getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(String idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    
    public String getApellidos1() {
        return apellidos1;
    }
    public void setApellidos1(String apellidos1) {
        this.apellidos1 = apellidos1;
    }
    public String getApellidos2() {
        return apellidos2;
    }
    public void setApellidos2(String apellidos2) {
        this.apellidos2 = apellidos2;
    }
    public String getNifcif() {
        return nifcif;
    }
    public void setNifcif(String nifcif) {
        this.nifcif = nifcif;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getNumColegiado() {
        return numColegiado;
    }
    public void setNumColegiado(String numColegiado) {
        this.numColegiado = numColegiado;
    }
    public String getPais() {
        return pais;
    }
    public void setPais(String pais) {
        this.pais = pais;
    }
    public String getPoblacion() {
        return poblacion;
    }
    public void setPoblacionExt(String poblacion) {
        this.poblacionExt = poblacion;
    }
    public String getPoblacionExt() {
        return poblacionExt;
    }
    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }
    public String getProvincia() {
        return provincia;
    }
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
    public String getAccion() {
        return accion;
    }
    public void setAccion(String accion) {
        this.accion = accion;
    }
    public String getIdPais() {
        return idPais;
    }
    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }
    public String getIdPoblacion() {
        return idPoblacion;
    }
    public void setIdPoblacion(String idPoblacion) {
        this.idPoblacion = idPoblacion;
    }
    public String getIdProvincia() {
        return idProvincia;
    }
    public void setIdProvincia(String idProvincia) {
        this.idProvincia = idProvincia;
    }
    public String getIdTipoEnvio() {
        return idTipoEnvio;
    }
    public void setIdTipoEnvio(String idTipoEnvio) {
        this.idTipoEnvio = idTipoEnvio;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getIdDireccion() {
        return idDireccion;
    }
    public void setIdDireccion(String idDireccion) {
        this.idDireccion = idDireccion;
    }
	public String getIdTipoExpediente() {
		return idTipoExpediente;
	}
	public void setIdTipoExpediente(String idTipoExpediente) {
		this.idTipoExpediente = idTipoExpediente;
	}
}
