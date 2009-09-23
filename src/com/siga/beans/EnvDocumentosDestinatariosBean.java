/*
 * Created on Apr 11, 2005
 * @author juan.grau
 *
 */
package com.siga.beans;

/**
 * Bean de Documentos de un destinatario
 */
public class EnvDocumentosDestinatariosBean extends MasterBean {

	// Variables
	private Integer idInstitucion;
	private Integer idEnvio;
	private Long idPersona;
	private Integer idDocumento;
	private String descripcion;
	private String pathDocumento;
	
	// Nombre campos de la tabla 
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_IDENVIO ="IDENVIO";
	static public final String C_IDPERSONA ="IDPERSONA";
	static public final String C_IDDOCUMENTO ="IDDOCUMENTO";
	static public final String C_DESCRIPCION ="DESCRIPCION";
	static public final String C_PATHDOCUMENTO ="PATHDOCUMENTO";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	
	static public final String T_NOMBRETABLA = "ENV_DOCUMENTOSDESTINATARIOS";
	
	
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Integer getIdDocumento() {
        return idDocumento;
    }
    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }
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
    public String getPathDocumento() {
        return pathDocumento;
    }
    public void setPathDocumento(String pathDocumento) {
        this.pathDocumento = pathDocumento;
    }
}
