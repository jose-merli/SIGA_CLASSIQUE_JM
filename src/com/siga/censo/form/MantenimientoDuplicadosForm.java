package com.siga.censo.form;

import java.util.Vector;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

public class MantenimientoDuplicadosForm extends MasterForm {



	private String solBusqueda="", codigo = "";
	String permitirAniadirNuevo= "N";
	String idPersona =  "";
	String idInstitucion;
	String modo = "";
	String nifcif="", nombre="", apellido1="", apellido2="", numeroColegiado="";
	String campoOrdenacion="", sentidoOrdenacion="", tipoConexion="";
	String idPersonaOrigen = "", idPersonaDestino = "", idInstOrigen = "";
	String listaDirecciones;
	String seleccionados;
	String agruparColegiaciones;

	public String getListaDirecciones() {
		return listaDirecciones;
	}
	public void setListaDirecciones(String listaDirecciones) {
		this.listaDirecciones = listaDirecciones;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getIdPersonaOrigen() {
		return idPersonaOrigen;
	}
	public void setIdPersonaOrigen(String idPersonaOrigen) {
		this.idPersonaOrigen = idPersonaOrigen;
	}
	public String getIdPersonaDestino() {
		return idPersonaDestino;
	}
	public void setIdPersonaDestino(String idPersonaDestino) {
		this.idPersonaDestino = idPersonaDestino;
	}
	public String getIdInstOrigen() {
		return idInstOrigen;
	}
	public void setIdInstOrigen(String idInstOrigen) {
		this.idInstOrigen = idInstOrigen;
	}
	// Checks para indicar el tipo de busqueda
	boolean chkApellidos,chkNombreApellidos,chkNumColegiado,chkIdentificador = false;

	public boolean getChkApellidos() {
		return chkApellidos;
	}
	public void setChkApellidos(boolean chkApellidos) {
		this.chkApellidos = chkApellidos;
	}
	public boolean getChkNombreApellidos() {
		return chkNombreApellidos;
	}
	public void setChkNombreApellidos(boolean chkNombreApellidos) {
		this.chkNombreApellidos = chkNombreApellidos;
	}
	public boolean getChkNumColegiado() {
		return chkNumColegiado;
	}
	public void setChkNumColegiado(boolean chkNumColegiado) {
		this.chkNumColegiado = chkNumColegiado;
	}
	public boolean getChkIdentificador() {
		return chkIdentificador;
	}
	public void setChkIdentificador(boolean chkIdentificador) {
		this.chkIdentificador = chkIdentificador;
	}
	public void setChkBusqueda(String valor){
		UtilidadesHash.set(this.datos, "ChkBusqueda", valor);
	}
	public String getModo() {
		return modo;
	}
	public void setModo(String modo) {
		this.modo = modo;
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
	public String getCampoOrdenacion() {
		return campoOrdenacion;
	}
	public void setCampoOrdenacion(String campoOrdenacion) {
		this.campoOrdenacion = campoOrdenacion;
	}
	public String getSentidoOrdenacion() {
		return sentidoOrdenacion;
	}
	public void setSentidoOrdenacion(String sentidoOrdenacion) {
		this.sentidoOrdenacion = sentidoOrdenacion;
	}
	public String getNumeroColegiado() {
		return numeroColegiado;
	}
	public void setNumeroColegiado(String numeroColegiado) {
		this.numeroColegiado = numeroColegiado;
	}
	public String getTipoConexion() {
		return tipoConexion;
	}
	public void setTipoConexion(String tipoConexion) {
		this.tipoConexion = tipoConexion;
	}
	
	public String getSeleccionados() {
		return seleccionados;
	}
	public void setSeleccionados(String seleccionados) {
		this.seleccionados = seleccionados;
	}
	public String getAgruparColegiaciones() {
		return agruparColegiaciones;
	}
	public void setAgruparColegiaciones(String agruparColegiaciones) {
		this.agruparColegiaciones = agruparColegiaciones;
	}
	
	
}