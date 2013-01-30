/*
 * Created on Jan 24, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla de denunciantes de un expediente
 */
public class ExpDenunciadoBean extends MasterBean {

	//Variables
	private Integer idInstitucion;
	private Integer idInstitucion_TipoExpediente;
	private Integer idTipoExpediente;
	private Integer numeroExpediente;
	private Integer anioExpediente;
	private Integer idDenunciado;
	private String fechaModificacion;
	private Integer usuModificacion;
	private Long idPersona, idDireccion;
	private Integer idInstitucionOrigen;
	
	// Nombre campos de la tabla 	
	static public final String C_IDINSTITUCION ="IDINSTITUCION";
	static public final String C_IDINSTITUCION_TIPOEXPEDIENTE ="IDINSTITUCION_TIPOEXPEDIENTE";
	static public final String C_IDTIPOEXPEDIENTE ="IDTIPOEXPEDIENTE";	
	static public final String C_NUMEROEXPEDIENTE ="NUMEROEXPEDIENTE";
	static public final String C_ANIOEXPEDIENTE ="ANIOEXPEDIENTE";
	static public final String C_IDDENUNCIADO ="IDDENUNCIADO";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	static public final String C_IDPERSONA ="IDPERSONA";
	static public final String C_IDDIRECCION ="IDDIRECCION";
	static public final String C_IDINSTITUCIONORIGEN ="IDINSTITUCIONORIGEN";
		
	
	static public final String T_NOMBRETABLA = "EXP_DENUNCIADO";
	
	/**
	 * El siguiente id sirve para distinguir al denunciado principal de los demas denunciados guardados en la misma tabla 
	 */
	static public final Integer ID_DENUNCIADO_PRINCIPAL = 1;
	
	public Integer getIdInstitucion_TipoExpediente() {
		return idInstitucion_TipoExpediente;
	}
	public void setIdInstitucion_TipoExpediente(
			Integer idInstitucion_TipoExpediente) {
		this.idInstitucion_TipoExpediente = idInstitucion_TipoExpediente;
	}
	public Integer getAnioExpediente() {
		return anioExpediente;
	}
	public void setAnioExpediente(Integer anioExpediente) {
		this.anioExpediente = anioExpediente;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getIdDenunciado() {
		return idDenunciado;
	}
	public void setIdDenunciado(Integer idDenunciado) {
		this.idDenunciado = idDenunciado;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdTipoExpediente() {
		return idTipoExpediente;
	}
	public void setIdTipoExpediente(Integer idTipoExpediente) {
		this.idTipoExpediente = idTipoExpediente;
	}
	public Integer getNumeroExpediente() {
		return numeroExpediente;
	}
	public void setNumeroExpediente(Integer numeroExpediente) {
		this.numeroExpediente = numeroExpediente;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
	
    /**
     * @return Returns the idDireccion.
     */
    public Long getIdDireccion() {
        return idDireccion;
    }
    /**
     * @param idDireccion The idDireccion to set.
     */
    public void setIdDireccion(Long idDireccion) {
        this.idDireccion = idDireccion;
    }
    /**
     * @return Returns the idPersona.
     */
    public Long getIdPersona() {
        return idPersona;
    }
    /**
     * @param idPersona The idPersona to set.
     */
    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }
    
	public Integer getIdInstitucionOrigen() {
		return idInstitucionOrigen;
	}
	public void setIdInstitucionOrigen(Integer idInstitucionOrigen) {
		this.idInstitucionOrigen = idInstitucionOrigen;
	}
    
}
