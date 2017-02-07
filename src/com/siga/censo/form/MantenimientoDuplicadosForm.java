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
	String idInstitucionDuplicados;
	String modo = "";
	String nifcif="", nombreDuplicados="", apellidosDuplicados="", numeroColegiado="";
	private String apellido1="", apellido2=""; //solo se utilizan para mostrar en el listado
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
	public String getIdInstitucionDuplicados() {
		return idInstitucionDuplicados;
	}
	public void setIdInstitucionDuplicados(String idInstitucionDuplicados) {
		this.idInstitucionDuplicados = idInstitucionDuplicados;
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
	public String getNombreDuplicados() {
		return nombreDuplicados;
	}
	public void setNombreDuplicados(String nombreDuplicados) {
		this.nombreDuplicados = nombreDuplicados;
	}
	public String getApellidosDuplicados() {
		return apellidosDuplicados;
	}
	public void setApellidosDuplicados(String apellidosDuplicados) {
		this.apellidosDuplicados = apellidosDuplicados;
	}
	public String getApellido1()
	{
		return apellido1;
	}
	public void setApellido1(String apellido1)
	{
		this.apellido1 = apellido1;
	}
	public String getApellido2()
	{
		return apellido2;
	}
	public void setApellido2(String apellido2)
	{
		this.apellido2 = apellido2;
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