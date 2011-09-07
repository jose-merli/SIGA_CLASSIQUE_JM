/*
 * Created on 14-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VleLetradosSigaBean extends MasterBean {
	//private static final long serialVersionUID = -2510899080052901261L;

	/* Variables */
	private String 	id_letrado, nombre, apellidos, num_doc, dir_profesional, poblacion,
	   provincia, cod_postal, telefono, mail;
	
	  
	static public String T_NOMBRETABLA = "VLE_LETRADOS_SIGA";
	/* Nombre campos de la tabla */
	//id_letrado, id_colegio, descripcion, num_colegiado, residencia, ejerciente,
	 //  fecha_modificacion, fecha_alta, colegio_cgae
	static public final String C_NOMBRE		        = "nombre";
	static public final String C_IDPERSONA			= "id_letrado";
	static public final String C_APELLIDOS   		= "apellidos";
	static public final String C_NUMDOC      		= "num_doc";
	static public final String C_DIRPROFESIONAL 	= "dir_profesional";
	static public final String C_POBLACION   		= "poblacion";
	static public final String C_PROVINCIA   		= "provincia";
	static public final String C_CODPOSTAL  		= "cod_postal";
	static public final String C_TELEFONO   		= "telefono";
	static public final String C_MAIL       		= "mail";
	public String getId_letrado() {
		return id_letrado;
	}
	public void setId_letrado(String id_letrado) {
		this.id_letrado = id_letrado;
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
	public String getNum_doc() {
		return num_doc;
	}
	public void setNum_doc(String num_doc) {
		this.num_doc = num_doc;
	}
	public String getDir_profesional() {
		return dir_profesional;
	}
	public void setDir_profesional(String dir_profesional) {
		this.dir_profesional = dir_profesional;
	}
	public String getPoblacion() {
		return poblacion;
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
	public String getCod_postal() {
		return cod_postal;
	}
	public void setCod_postal(String cod_postal) {
		this.cod_postal = cod_postal;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}	
}
