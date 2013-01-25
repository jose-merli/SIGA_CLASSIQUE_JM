package com.siga.beans;

/**
 * Bean de la tabla SCS_PROCURADOR
 * 
 * @author david.sanchezp
 * @since 17/01/2006
 */
public class ScsProcuradorBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer	idInstitucion, idProcurador;
	private String	nombre=null, apellido1=null, apellido2=null,ncolegiado=null, direccion=null, codigoPostal=null, idProvincia=null, idPoblacion=null, 
					telefono1=null, telefono2=null, fax1=null, email=null,codProcurador="", idColProcurador=null;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_PROCURADOR";
	
	/*Nombre de campos de la tabla*/

	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDPROCURADOR = 			"IDPROCURADOR";
	static public final String 	C_NCOLEGIADO = 				"NCOLEGIADO";
	static public final String 	C_IDCOLPROCURADOR = 		"IDCOLPROCURADOR";
	static public final String 	C_NOMBRE = 					"NOMBRE";
	static public final String 	C_APELLIDO1 = 				"APELLIDOS1";
	static public final String 	C_APELLIDO2 = 				"APELLIDOS2";
	static public final String 	C_DIRECCION = 				"DOMICILIO";
	static public final String 	C_CODIGOPOSTAL = 			"CODIGOPOSTAL";
	static public final String 	C_IDPROVINCIA = 			"IDPROVINCIA";
	static public final String 	C_IDPOBLACION = 			"IDPOBLACION";
	static public final String 	C_TELEFONO1 = 				"TELEFONO1";
	static public final String 	C_TELEFONO2 = 				"TELEFONO2";
	static public final String 	C_EMAIL = 					"EMAIL";
	static public final String 	C_FAX1 = 					"FAX1";
	static public final String 	C_FECHABAJA = 				"FECHABAJA";
	static public final String 	C_CODPROCURADOR =    		"CODIGO";

	
	
	/**
	 * @return Returns the ncolegiado.
	 */
	public String getNColegiado() {
		return ncolegiado;
	}
	/**
	 * @param ncolegiado The ncolegiado to set.
	 */
	public void setNColegiado(String ncolegiado) {
		this.ncolegiado = ncolegiado;
	}
	/**
	 * @return Returns the apellido1.
	 */
	public String getApellido1() {
		return apellido1;
	}
	/**
	 * @param apellido1 The apellido1 to set.
	 */
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	/**
	 * @return Returns the apellido2.
	 */
	public String getApellido2() {
		return apellido2;
	}
	/**
	 * @param apellido2 The apellido2 to set.
	 */
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return Returns the codigoPostal.
	 */
	public String getCodigoPostal() {
		return codigoPostal;
	}
	/**
	 * @param codigoPostal The codigoPostal to set.
	 */
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	/**
	 * @return Returns the direccion.
	 */
	public String getDireccion() {
		return direccion;
	}
	/**
	 * @param direccion The direccion to set.
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	/**
	 * @return Returns the fax1.
	 */
	public String getFax1() {
		return fax1;
	}
	/**
	 * @param fax1 The fax1 to set.
	 */
	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the idPoblacion.
	 */
	public String getIdPoblacion() {
		return idPoblacion;
	}
	/**
	 * @param idPoblacion The idPoblacion to set.
	 */
	public void setIdPoblacion(String idPoblacion) {
		this.idPoblacion = idPoblacion;
	}
	/**
	 * @return Returns the idProcurador.
	 */
	public Integer getIdProcurador() {
		return idProcurador;
	}
	/**
	 * @param idProcurador The idProcurador to set.
	 */
	public void setIdProcurador(Integer idProcurador) {
		this.idProcurador = idProcurador;
	}
	/**
	 * @return Returns the idProvincia.
	 */
	public String getIdProvincia() {
		return idProvincia;
	}
	/**
	 * @param idProvincia The idProvincia to set.
	 */
	public void setIdProvincia(String idProvincia) {
		this.idProvincia = idProvincia;
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return Returns the telefono1.
	 */
	public String getTelefono1() {
		return telefono1;
	}
	/**
	 * @param telefono1 The telefono1 to set.
	 */
	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}
	/**
	 * @return Returns the telefono2.
	 */
	public String getTelefono2() {
		return telefono2;
	}
	/**
	 * @param telefono2 The telefono2 to set.
	 */
	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}
	/**
	 * @return Returns the idProcurador.
	 */
	public String getCodProcurador() {
		return codProcurador;
	}
	/**
	 * @param idProcurador The idProcurador to set.
	 */
	public void setCodProcurador(String codProcurador) {
		this.codProcurador = codProcurador;
	}

	public String getIdColProcurador() {
		return idColProcurador;
	}

	public void setIdColProcurador(String idColProcurador) {
		this.idColProcurador = idColProcurador;
	}
	
}