/*
 * Created on 19-mar-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

public class CenColaCambioLetradoBean extends MasterBean
{
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_COLACAMBIOLETRADO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDPERSONA     = "IDPERSONA";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDCAMBIO	   = "IDCAMBIO";
	static public final String C_FECHACAMBIO   = "FECHACAMBIO";			
	static public final String C_IDTIPOCAMBIO  = "IDTIPOCAMBIO";			
	static public final String C_IDDIRECCION   = "IDDIRECCION";			

	private String  fechaCambio;
	private Long    idPersona, idCambio, idDireccion;
	private Integer idInstitucion, idTipoCambio;
	
	
    /**
     * @return Returns the fechaCambio.
     */
    public String getFechaCambio() {
        return fechaCambio;
    }
    /**
     * @param fechaCambio The fechaCambio to set.
     */
    public void setFechaCambio(String fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
    /**
     * @return Returns the idCambio.
     */
    public Long getIdCambio() {
        return idCambio;
    }
    /**
     * @param idCambio The idCambio to set.
     */
    public void setIdCambio(Long idCambio) {
        this.idCambio = idCambio;
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
    /**
     * @return Returns the idTipoCambio.
     */
    public Integer getIdTipoCambio() {
        return idTipoCambio;
    }
    /**
     * @param idTipoCambio The idTipoCambio to set.
     */
    public void setIdTipoCambio(Integer idTipoCambio) {
        this.idTipoCambio = idTipoCambio;
    }
}
