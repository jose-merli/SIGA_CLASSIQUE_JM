/*
 * Created on Mar 28, 2005
 * @author jmgrau
 *
 */
package com.siga.beans;

public class EnvTempDestinatariosBean extends MasterBean {
    
	//Variables
    private Integer idEnvio;
    private Long idPersona;
	private Integer idInstitucion;
	private String domicilio;
	private String codigoPostal;
	private String fax1;
	private String fax2;
	private String correoElectronico;
	private String idPais;
    private String idProvincia;
	private String idPoblacion;
	private String nombre;
	private String apellidos1;
    private String apellidos2;
	private String nifcif;	
	private String mensaje;
	
	// Nombre campos de la tabla 
	static public final String C_IDENVIO = "IDENVIO";
	static public final String C_IDPERSONA = "IDPERSONA";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_DOMICILIO = "DOMICILIO";
	static public final String C_CODIGOPOSTAL = "CODIGOPOSTAL";
	static public final String C_FAX1 = "FAX1";
	static public final String C_FAX2 = "FAX2";
	static public final String C_CORREOELECTRONICO = "CORREOELECTRONICO";
	static public final String C_IDPAIS = "IDPAIS";
	static public final String C_IDPROVINCIA = "IDPROVINCIA";
	static public final String C_IDPOBLACION = "IDPOBLACION";	
	static public final String C_NOMBRE	= "NOMBRE";
	static public final String C_APELLIDOS1	= "APELLIDOS1";
	static public final String C_APELLIDOS2	= "APELLIDOS2";
	static public final String C_NIFCIF = "NIFCIF";
	static public final String C_MENSAJE = "MENSAJE";
	
	static public final String T_NOMBRETABLA = "ENV_TEMP_DESTINATARIOS";	


    public Integer getIdEnvio() {
        return idEnvio;
    }
    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }
    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public Long getIdPersona() {
        return idPersona;
    }
    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }
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
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
