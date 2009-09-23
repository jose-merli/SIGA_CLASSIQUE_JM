package com.siga.beans;

/**
 * Bean de la tabla SCS_COMISARIA
 * 
 * @author david.sanchezp
 * @since 23/01/2006
 */
public class ScsComisariaBean extends MasterBean{
	
	/* Variables */ 
	
	private Integer	idInstitucion, idComisaria;
	private String	nombre=null, direccion=null, codigoPostal=null, idProvincia=null, idPoblacion=null, 
					telefono1=null, telefono2=null, fax1=null;
	private String codigoExt;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_COMISARIA";
	
	
	
	/*Nombre de campos de la tabla*/

	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDCOMISARIA = 			"IDCOMISARIA";
	static public final String 	C_NOMBRE = 					"NOMBRE";
	static public final String 	C_DIRECCION = 				"DOMICILIO";
	static public final String 	C_CODIGOPOSTAL = 			"CODIGOPOSTAL";
	static public final String 	C_IDPROVINCIA = 			"IDPROVINCIA";
	static public final String 	C_IDPOBLACION = 			"IDPOBLACION";
	static public final String 	C_TELEFONO1 = 				"TELEFONO1";
	static public final String 	C_TELEFONO2 = 				"TELEFONO2";
	static public final String 	C_FAX1 = 					"FAX1";
	static public final String C_CODIGOEXT = "CODIGOEXT";
	
	/**
	 * @param codigoExt The codigoExt to set.
	 */
	public void setCodigoExt (String valor)
	{
		this.codigoExt = valor;
	}

	/**
	 * @return Returns the codigoExt.
	 */
	public String getCodigoExt ()
	{
		return codigoExt;
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
	 * @return Returns the idPrision.
	 */
	public Integer getIdComisaria() {
		return idComisaria;
	}
	/**
	 * @param idPrision The idPrision to set.
	 */
	public void setIdComisaria(Integer idComisaria) {
		this.idComisaria = idComisaria;
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
}