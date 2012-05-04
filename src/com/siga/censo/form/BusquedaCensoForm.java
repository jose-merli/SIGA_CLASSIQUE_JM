package com.siga.censo.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * Clase action form del caso de uso 
 * 
 * 
 */
 public class BusquedaCensoForm extends MasterForm {
 	
	// Metodos Set (Formulario (*.jsp))

	// BLOQUE PARA EL FORMULARIO DE BUSQUEDA SIMPLE 

 	
		
	 	String nombre;
	 	String apellido1;
	 	String apellido2;
	 	String nif;
	 	String accion;	 	

		String direccion;
	 	String codPostal;
	 	String fax1;
	 	String fax2;
	 	String idPersona;
	 	String idInstitucion;
	 	String mail;
	 	String movil;
	 	String telefono;
	 	String sexo;
	 	String paginaWeb;
	 	
	 	
		String numeroColegiado;
	 	String poblacion;
	 	String poblacionExt;
	 	String provincia;
	 	String pais;
	 	String telefono2;
	 	String tratamiento;
	 	String colegiadoen;
	 	String verFichaLetrado;
	 	String chkBusqueda;
		
		String preferente;
		String tipoDireccion;
		String direcciones;
		String idDireccion;
		String fechaNacimiento;
		String lugarNacimiento;
		String estadoCivil;
		String textoAlerta;
		String multiple;
		String existeNIF;
		
		public String getAccion() {
			return accion;
		}
		public void setAccion(String accion) {
			this.accion = accion;
		}
		
		public String getChkBusqueda() {
			return chkBusqueda;
		}
		public void setChkBusqueda(String chkBusqueda) {
			this.chkBusqueda = chkBusqueda;
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
		public String getDireccion() {
			return direccion;
		}
		public void setDireccion(String direccion) {
			this.direccion = direccion;
		}
		public String getCodPostal() {
			return codPostal;
		}
		public void setCodPostal(String codPostal) {
			this.codPostal = codPostal;
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
		public String getMail() {
			return mail;
		}
		public void setMail(String mail) {
			this.mail = mail;
		}
		public String getMovil() {
			return movil;
		}
		public void setMovil(String movil) {
			this.movil = movil;
		}
		public String getTelefono() {
			return telefono;
		}
		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}
		public String getSexo() {
			return sexo;
		}
		public void setSexo(String sexo) {
			this.sexo = sexo;
		}
		public String getPaginaWeb() {
			return paginaWeb;
		}
		public void setPaginaWeb(String paginaWeb) {
			this.paginaWeb = paginaWeb;
		}
		public String getNumeroColegiado() {
			return numeroColegiado;
		}
		public void setNumeroColegiado(String numeroColegiado) {
			this.numeroColegiado = numeroColegiado;
		}
		public String getPoblacion() {
			return poblacion;
		}
		public void setPoblacion(String poblacion) {
			this.poblacion = poblacion;
		}
		public String getPoblacionExt() {
			return poblacionExt;
		}
		public void setPoblacionExt(String poblacionExt) {
			this.poblacionExt = poblacionExt;
		}
		public String getProvincia() {
			return provincia;
		}
		public void setProvincia(String provincia) {
			this.provincia = provincia;
		}
		public String getPais() {
			return pais;
		}
		public void setPais(String pais) {
			this.pais = pais;
		}
		public String getTelefono2() {
			return telefono2;
		}
		public void setTelefono2(String telefono2) {
			this.telefono2 = telefono2;
		}
		public String getTratamiento() {
			return tratamiento;
		}
		public void setTratamiento(String tratamiento) {
			this.tratamiento = tratamiento;
		}
		public String getVerFichaLetrado() {
			return verFichaLetrado;
		}
		public void setVerFichaLetrado(String verFichaLetrado) {
			this.verFichaLetrado = verFichaLetrado;
		}
		public String getNif() {
			return nif;
		}
		public void setNif(String nif) {
			this.nif = nif;
		}
		
		public String getColegiadoen() {
			return colegiadoen;
		}
		public void setColegiadoen(String colegiadoen) {
			this.colegiadoen = colegiadoen;
		}
			
		public String getPreferente() {
			return preferente;
		}
		public void setPreferente(String preferente) {
			this.preferente = preferente;
		}
		
		public String getTipoDireccion() {
			return tipoDireccion;
		}
		public void setTipoDireccion(String tipoDireccion) {
			this.tipoDireccion = tipoDireccion;
		}
		
		public String getDirecciones() {
			return direcciones;
		}
		public void setDirecciones(String direcciones) {
			this.direcciones = direcciones;
		}
		
		public String getIdDireccion() {
			return idDireccion;
		}
		public void setIdDireccion(String idDireccion) {
			this.idDireccion = idDireccion;
		}
		public String getFechaNacimiento() {
			return fechaNacimiento;
		}
		public void setFechaNacimiento(String fechaNacimiento) {
			this.fechaNacimiento = fechaNacimiento;
		}
		public String getLugarNacimiento() {
			return lugarNacimiento;
		}
		public void setLugarNacimiento(String lugarNacimiento) {
			this.lugarNacimiento = lugarNacimiento;
		}
		public String getTextoAlerta() {
			return textoAlerta;
		}
		public void setTextoAlerta(String textoAlerta) {
			this.textoAlerta = textoAlerta;
		}
		public String getMultiple() {
			return multiple;
		}
		public void setMultiple(String multiple) {
			this.multiple = multiple;
		}
		public String getEstadoCivil() {
			return estadoCivil;
		}
		public void setEstadoCivil(String estadoCivil) {
			this.estadoCivil = estadoCivil;
		}
		public String getExisteNIF() {
			return existeNIF;
		}
		public void setExisteNIF(String existeNIF) {
			this.existeNIF = existeNIF;
		}
		public void reset(){
			
		 	  nombre="";
		 	  apellido1="";
		 	  apellido2="";
		 	  nif="";
		 	  accion="";	 	

			  direccion="";
		 	  codPostal="";
		 	  fax1="";
		 	  fax2="";
		 	  idPersona="";
		 	  idInstitucion="";
		 	  mail="";
		 	  movil="";
		 	  telefono="";
		 	  sexo="";
		 	  paginaWeb="";
		 	
		 	
			  numeroColegiado="";
		 	  poblacion="";
		 	  poblacionExt="";
		 	  provincia="";
		 	  pais="";
		 	  telefono2="";
		 	  tratamiento="";
		 	  verFichaLetrado="";
		 	  chkBusqueda="";
		}
	 	


}