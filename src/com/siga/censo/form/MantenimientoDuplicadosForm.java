package com.siga.censo.form;

import java.util.List;
import java.util.Vector;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenInstitucionBean;
import com.siga.general.MasterForm;

public class MantenimientoDuplicadosForm extends MasterForm {


	private static final long serialVersionUID = 1916453132854234795L;
	String permitirAniadirNuevo= "N";
	String idPersona =  "";
	String idInstitucion;
	String modo = "";
	String nifcif="", nombre="", apellidos="", numeroColegiado="";
	String idPersonaOrigen = "", idPersonaDestino = "";
	private String listaDirecciones, listaEstados, listaDireccionesNoSeleccionadas, listaEstadosNoSeleccionados;
	String seleccionados;
	String agruparColegiaciones;
	String seleccion;
	String volver;
	private Vector resultadoBusqueda;
	private List<CenInstitucionBean> listadoInstituciones;

	public String getListaDireccionesNoSeleccionadas()	{
		return listaDireccionesNoSeleccionadas;
	}
	public void setListaDireccionesNoSeleccionadas(String listaDireccionesNoSeleccionadas)	{
		this.listaDireccionesNoSeleccionadas = listaDireccionesNoSeleccionadas;
	}
	public String getListaEstadosNoSeleccionados()	{
		return listaEstadosNoSeleccionados;
	}
	public void setListaEstadosNoSeleccionados(String listaEstadosNoSeleccionados)	{
		this.listaEstadosNoSeleccionados = listaEstadosNoSeleccionados;
	}
	public String getListaDirecciones() {
		return listaDirecciones;
	}
	public void setListaDirecciones(String listaDirecciones) {
		this.listaDirecciones = listaDirecciones;
	}
	public String getListaEstados() {
		return listaEstados;
	}
	public void setListaEstados(String listaEstados) {
		this.listaEstados = listaEstados;
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
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getNumeroColegiado() {
		return numeroColegiado;
	}
	public void setNumeroColegiado(String numeroColegiado) {
		this.numeroColegiado = numeroColegiado;
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
	public void setResultadoBusqueda(Vector resul) {
		this.resultadoBusqueda = resul;
	}
	public Vector getResultadoBusqueda() {
		return this.resultadoBusqueda;
	}
	public String getSeleccion() {
		return seleccion;
	}
	public void setSeleccion(String seleccion) {
		this.seleccion = seleccion;
	}
	public List<CenInstitucionBean> getListadoInstituciones() {
		return listadoInstituciones;
	}
	public void setListadoInstituciones(
			List<CenInstitucionBean> listadoInstituciones) {
		this.listadoInstituciones = listadoInstituciones;
	}
	public String getVolver() {
		return volver;
	}
	public void setVolver(String volver) {
		this.volver = volver;
	}	
	
	
}