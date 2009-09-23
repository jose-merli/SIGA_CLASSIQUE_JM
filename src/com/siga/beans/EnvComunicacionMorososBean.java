package com.siga.beans;

/**
 * 
 * @author jorgeta
 *
 */
public class EnvComunicacionMorososBean extends MasterBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* Variables */
	private Integer idPersona;
	private Integer idInstitucion;
	private String idFactura;
	private Integer idEnvio;
	private String descripcion;
	private String pathDocumento;
	private String fechaEnvio;
	private Integer idEnvioDoc;
	private Integer idDocumento;

	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDPERSONA = "IDPERSONA";
	static public final String C_IDFACTURA = "IDFACTURA";
	static public final String C_IDENVIO = "IDENVIO";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	static public final String C_PATHDOCUMENTO = "PATHDOCUMENTO";
	static public final String C_FECHAENVIO = "FECHA_ENVIO";
	static public final String C_IDENVIODOC = "IDENVIODOC";
	static public final String C_IDDOCUMENTO  = "IDDOCUMENTO";
	
	

	static public final String T_NOMBRETABLA = "ENV_COMUNICACIONMOROSOS";



	public Integer getIdPersona() {
		return idPersona;
	}



	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}



	public Integer getIdInstitucion() {
		return idInstitucion;
	}



	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}



	public String getIdFactura() {
		return idFactura;
	}



	public void setIdFactura(String idFactura) {
		this.idFactura = idFactura;
	}



	public Integer getIdEnvio() {
		return idEnvio;
	}



	public void setIdEnvio(Integer idEnvio) {
		this.idEnvio = idEnvio;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public String getPathDocumento() {
		return pathDocumento;
	}



	public void setPathDocumento(String pathDocumento) {
		this.pathDocumento = pathDocumento;
	}



	public String getFechaEnvio() {
		return fechaEnvio;
	}



	public void setFechaEnvio(String fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}



	public Integer getIdEnvioDoc() {
		return idEnvioDoc;
	}



	public void setIdEnvioDoc(Integer idEnvioDoc) {
		this.idEnvioDoc = idEnvioDoc;
	}



	public Integer getIdDocumento() {
		return idDocumento;
	}



	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}
	
	// Métodos SET
   
}