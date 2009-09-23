/*
 * Created on Apr 05, 2005
 * @author jmgrau
 *
 */
package com.siga.beans;

public class EnvRemitentesBean extends MasterBean {
    
	//Variables
    private Integer idEnvio;
    private Long idPersona;
	private Integer idInstitucion;
	private String domicilio;
	private String codigoPostal;
	private String fax1;
	private String fax2;
	private String movil;
	private String correoElectronico;
	private String idPais;
	private String idProvincia;
	private String poblacionExtranjera;
    
	private String idPoblacion;
	private String descripcion;
	
	// Nombre campos de la tabla 
	static public final String C_IDENVIO = "IDENVIO";
	static public final String C_IDPERSONA = "IDPERSONA";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_DOMICILIO = "DOMICILIO";
	static public final String C_CODIGOPOSTAL = "CODIGOPOSTAL";
	static public final String C_FAX1 = "FAX1";
	static public final String C_FAX2 = "FAX2";
	static public final String C_MOVIL = "MOVIL";
	static public final String C_CORREOELECTRONICO = "CORREOELECTRONICO";
	static public final String C_IDPAIS = "IDPAIS";
	static public final String C_IDPROVINCIA = "IDPROVINCIA";
	static public final String C_IDPOBLACION = "IDPOBLACION";	
	static public final String C_POBLACIONEXTRANJERA = "POBLACIONEXTRANJERA";	
	static public final String C_DESCRIPCION = "DESCRIPCION";	
	
	static public final String T_NOMBRETABLA = "ENV_REMITENTES";	


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
    public String getMovil() {
        return movil;
    }
    public void setMovil(String movil) {
        this.movil = movil;
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
    public String getPoblacionExtranjera() {
        return poblacionExtranjera;
    }
    public void setPoblacionExtranjera(String valor) {
        this.poblacionExtranjera = valor;
    }
    public String getIdProvincia() {
        return idProvincia;
    }
    public void setIdProvincia(String idProvincia) {
        this.idProvincia = idProvincia;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
