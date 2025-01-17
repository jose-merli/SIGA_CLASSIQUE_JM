package com.siga.beans;

/**
 * Bean de la tabla SCS_JUZGADO
 * 
 * @author david.sanchezp
 * @since 23/01/2006
 */
public class ScsJuzgadoBean extends MasterBean{
	
	/* Variables */ 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3795279638721762594L;
	private Integer	idInstitucion, idJuzgado ;
	private String	nombre=null, direccion=null, codigoPostal=null, idProvincia=null, idPoblacion=null, 
					telefono1=null, telefono2=null, fax1=null,codProcurador="", email="", visible="", visibleMovil="", movil="", esDecano="";
	
	private String fechabaja, isCodigoEjis;
	
	/* Nombre de Tabla*/
	
	static public String T_NOMBRETABLA = "SCS_JUZGADO";
	
	
	
	/*Nombre de campos de la tabla*/

	static public final String 	C_IDINSTITUCION = 			"IDINSTITUCION";
	static public final String 	C_IDJUZGADO = 				"IDJUZGADO";
	static public final String 	C_NOMBRE = 					"NOMBRE";
	static public final String 	C_DIRECCION = 				"DOMICILIO";
	static public final String 	C_CODIGOPOSTAL = 			"CODIGOPOSTAL";
	static public final String 	C_IDPROVINCIA = 			"IDPROVINCIA";
	static public final String 	C_IDPOBLACION = 			"IDPOBLACION";
	static public final String 	C_TELEFONO1 = 				"TELEFONO1";
	static public final String 	C_TELEFONO2 = 				"TELEFONO2";
	static public final String 	C_FAX1 = 					"FAX1";
	static public final String 	C_CODPROCURADOR = 			"CODIGOPROCURADOR";
	static public final String 	C_VISIBLE =      			"VISIBLE";
	static public final String 	C_FECHABAJA = 				"FECHABAJA";	
	static public final String 	C_MOVIL =      			"MOVIL";
	static public final String 	C_EMAIL = 					"EMAIL";
	/* cambio para codigo ext */
	private String codigoExt;
	private String codigoExt2;
	static public final String C_CODIGOEXT = "CODIGOEXT";
	static public final String C_CODIGOEXT2 = "CODIGOEXT2";
	static public final String C_ISCODIGOEJIS = "ISCODIGOEJIS";
	static public final String 	C_VISIBLEMOVIL =      "VISIBLEMOVIL";
	static public final String 	C_ESDECANO =      "ESDECANO";
	
	public void setCodigoExt (String valor)
	{
		this.codigoExt = valor;
	}
	public String getCodigoExt ()
	{
		return codigoExt;
	}
	//////
	public void setCodProcurador (String valor)
	{
		this.codProcurador = valor;
	}
	public String getCodProcurador ()
	{
		return codProcurador;
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
	public Integer getIdJuzgado() {
		return idJuzgado;
	}
	/**
	 * @param idPrision The idPrision to set.
	 */
	public void setIdJuzgado(Integer idJuzgado) {
		this.idJuzgado = idJuzgado;
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
	 * @return Returns the codigoPostal.
	 */
	public String getVisible() {
		return visible;
	}
	/**
	 * @param codigoPostal The codigoPostal to set.
	 */
	public void setVisible(String visible) {
		this.visible = visible;
	}
		/**
	 * @return Returns the codigoPostal.
	 */
	public String getMovil() {
		return movil;
	}
	/**
	 * @param codigoPostal The codigoPostal to set.
	 */
	public void setMovil(String movil) {
		this.movil = movil;
	}

	public String getFechabaja() {
		return fechabaja;
	}

	public void setFechabaja(String fechabaja) {
		this.fechabaja = fechabaja;
	}
	public String getCodigoExt2() {
		return codigoExt2;
	}
	public void setCodigoExt2(String codigoExt2) {
		this.codigoExt2 = codigoExt2;
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

	public String getIsCodigoEjis() {
		return isCodigoEjis;
	}

	public void setIsCodigoEjis(String isCodigoEjis) {
		this.isCodigoEjis = isCodigoEjis;
	}

	public String getVisibleMovil() {
		return visibleMovil;
	}

	public void setVisibleMovil(String visibleMovil) {
		this.visibleMovil = visibleMovil;
	}

	public String getEsDecano() {
		return esDecano;
	}

	public void setEsDecano(String esDecano) {
		this.esDecano = esDecano;
	}

}
