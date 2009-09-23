/*
 * Created on Dec 22, 2004
 * @author emilio.grau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.envios.form;

import com.siga.general.MasterForm;

/**
 * Formulario para la administración de las listas de correo.
 */
public class ListaCorreosForm extends MasterForm {
	
//    private String modo="";
    private String modal="";
	private String nombre="";
	private String descripcion="";
	private String fechaModificacion;
	private String usuModificacion;
	private String idInstitucion;
	private String dinamica="";
	private String idListaCorreos;
	private String refresh=null;
	
	private String nombreCliente;
	private String primerApellido;
	private String segundoApellido;
	private String nif;
	private String idPersona;
	private String numColegiado;
	
	private String campoLista="";
	private String campoDinamica="";
	
	private String idConsulta="";
	
	
	
	public String getIdConsulta() {
		return idConsulta;
	}
	public void setIdConsulta(String idConsulta) {
		this.idConsulta = idConsulta;
	}
	public String getDinamica() {
		return dinamica;
	}
	public void setDinamica(String dinamica) {
		this.dinamica = dinamica;
	}
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
	public String getModal() {
		return modal;
	}
	public void setModal(String modal) {
		this.modal = modal;
	}
//	public String getModo() {
//		return modo;
//	}
//	public void setModo(String modo) {
//		this.modo = modo;
//	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(String usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
    public String getRefresh() {
        return refresh;
    }
    public void setRefresh(String refresh) {
        this.refresh = "";
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getCampoLista() {
        return campoLista;
    }
    public void setCampoLista(String campoLista) {
        this.campoLista = campoLista;
    }
    public String getIdListaCorreos() {
        return idListaCorreos;
    }
    public void setIdListaCorreos(String idListaCorreos) {
        this.idListaCorreos = idListaCorreos;
    }
    public String getCampoDinamica() {
        return campoDinamica;
    }
    public void setCampoDinamica(String campoDinamica) {
        this.campoDinamica = campoDinamica;
    }
    public String getIdPersona() {
        return idPersona;
    }
    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }
    public String getNif() {
        return nif;
    }
    public void setNif(String nif) {
        this.nif = nif;
    }
    public String getNombreCliente() {
        return nombreCliente;
    }
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    public String getPrimerApellido() {
        return primerApellido;
    }
    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }
    public String getSegundoApellido() {
        return segundoApellido;
    }
    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }
    public String getNumColegiado() {
        return numColegiado;
    }
    public void setNumColegiado(String numColegiado) {
        this.numColegiado = numColegiado;
    }
}
