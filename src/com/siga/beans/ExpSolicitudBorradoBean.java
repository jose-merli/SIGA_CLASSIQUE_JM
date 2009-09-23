/*
 * Created on Dec 27, 2004
 * @author emilio.grau
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla de expedientes
 */
public class ExpSolicitudBorradoBean extends MasterBean {

	//Variables
	
    private Integer idSolicitud;
	private String motivo;
	private Integer idEstadoSolic;
	private Integer idPersona;
	private Integer idInstitucion;
	private Integer idInstitucion_tipoExpediente;
	private Integer idTipoExpediente;
	private Integer numeroExpediente;
	private Integer anioExpediente;
	private String fechaAlta;	

	
	// Nombre campos de la tabla
	
	static public final String C_IDSOLICITUD ="IDSOLICITUD";
	static public final String C_MOTIVO ="MOTIVO";
	static public final String C_IDESTADOSOLIC ="IDESTADOSOLIC";
	static public final String C_IDPERSONA ="IDPERSONA";
	static public final String C_FECHAALTA ="FECHAALTA";
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_IDINSTITUCION_TIPOEXPEDIENTE ="IDINSTITUCION_TIPOEXPEDIENTE";
	static public final String C_IDTIPOEXPEDIENTE ="IDTIPOEXPEDIENTE";	
	static public final String C_NUMEROEXPEDIENTE ="NUMEROEXPEDIENTE";
	static public final String C_ANIOEXPEDIENTE ="ANIOEXPEDIENTE";
	
	static public final String T_NOMBRETABLA = "EXP_SOLICITUDBORRADO";
	
	

    public Integer getAnioExpediente() {
        return anioExpediente;
    }
    public void setAnioExpediente(Integer anioExpediente) {
        this.anioExpediente = anioExpediente;
    }
    public String getFechaAlta() {
        return fechaAlta;
    }
    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    public Integer getIdEstadoSolic() {
        return idEstadoSolic;
    }
    public void setIdEstadoSolic(Integer idEstadoSolic) {
        this.idEstadoSolic = idEstadoSolic;
    }
    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    public Integer getIdInstitucion_tipoExpediente() {
        return idInstitucion_tipoExpediente;
    }
    public void setIdInstitucion_tipoExpediente(
            Integer idInstitucion_tipoExpediente) {
        this.idInstitucion_tipoExpediente = idInstitucion_tipoExpediente;
    }
    public Integer getIdPersona() {
        return idPersona;
    }
    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }
    public Integer getIdSolicitud() {
        return idSolicitud;
    }
    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }
    public Integer getIdTipoExpediente() {
        return idTipoExpediente;
    }
    public void setIdTipoExpediente(Integer idTipoExpediente) {
        this.idTipoExpediente = idTipoExpediente;
    }
    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    public Integer getNumeroExpediente() {
        return numeroExpediente;
    }
    public void setNumeroExpediente(Integer numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }
}
