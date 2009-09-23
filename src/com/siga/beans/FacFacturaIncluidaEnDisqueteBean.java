/*
 * Created on 17-mar-2005
 *
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 */
public class FacFacturaIncluidaEnDisqueteBean extends MasterBean {

	/* Variables */
	private Integer idInstitucion, idDisqueteCargos, idFacturaIncluidaEnDisquete;
	private String 	idFactura;
	private String  idRecibo;
	private Long 	idPersona;
	private Integer idCuenta;
	private Double  importe;
	private String 	devuelta;
	private String 	contabilizada;
	private String 	fechaDevolucion;
	private Integer idRenegociacion;

	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_FACTURAINCLUIDAENDISQUETE";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION					= "IDINSTITUCION";	
	static public final String C_IDDISQUETECARGOS				= "IDDISQUETECARGOS";
	static public final String C_IDFACTURAINCLUIDAENDISQUETE	= "IDFACTURAINCLUIDAENDISQUETE";
	static public final String C_IDFACTURA						= "IDFACTURA";	
	static public final String C_IDRECIBO						= "IDRECIBO";
	static public final String C_IDPERSONA						= "IDPERSONA";
	static public final String C_IDCUENTA						= "IDCUENTA";
	static public final String C_IMPORTE  						= "IMPORTE";
	static public final String C_DEVUELTA						= "DEVUELTA";
	static public final String C_CONTABILIZADA					= "CONTABILIZADA";
	static public final String C_FECHADEVOLUCION				= "FECHADEVOLUCION";
	static public final String C_IDRENEGOCIACION				= "IDRENEGOCIACION";
	
	
	/**
	 * @return Returns the contabilizada.
	 */
	public String getContabilizada() {
		return contabilizada;
	}
	/**
	 * @param contabilizada The contabilizada to set.
	 */
	public void setContabilizada(String contabilizada) {
		this.contabilizada = contabilizada;
	}
	/**
	 * @return Returns the devuelta.
	 */
	public String getDevuelta() {
		return devuelta;
	}
	/**
	 * @param devuelta The devuelta to set.
	 */
	public void setDevuelta(String devuelta) {
		this.devuelta = devuelta;
	}
	/**
	 * @return Returns the fechaDevolucion.
	 */
	public String getFechaDevolucion() {
		return fechaDevolucion;
	}
	/**
	 * @param fechaDevolucion The fechaDevolucion to set.
	 */
	public void setFechaDevolucion(String fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}
	/**
	 * @return Returns the idCuenta.
	 */
	public Integer getIdCuenta() {
		return idCuenta;
	}
	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}
	/**
	 * @return Returns the idDisqueteCargos.
	 */
	public Integer getIdDisqueteCargos() {
		return idDisqueteCargos;
	}
	/**
	 * @param idDisqueteCargos The idDisqueteCargos to set.
	 */
	public void setIdDisqueteCargos(Integer idDisqueteCargos) {
		this.idDisqueteCargos = idDisqueteCargos;
	}
	/**
	 * @return Returns the idFactura.
	 */
	public String getIdFactura() {
		return idFactura;
	}
	/**
	 * @param idFactura The idFactura to set.
	 */
	public void setIdFactura(String idFactura) {
		this.idFactura = idFactura;
	}
	/**
	 * @return Returns the idFacturaIncluidaEnDisquete.
	 */
	public Integer getIdFacturaIncluidaEnDisquete() {
		return idFacturaIncluidaEnDisquete;
	}
	/**
	 * @param idFacturaIncluidaEnDisquete The idFacturaIncluidaEnDisquete to set.
	 */
	public void setIdFacturaIncluidaEnDisquete(
			Integer idFacturaIncluidaEnDisquete) {
		this.idFacturaIncluidaEnDisquete = idFacturaIncluidaEnDisquete;
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
	 * @return Returns the idRecibo.
	 */
	public String  getIdRecibo() {
		return idRecibo;
	}
	/**
	 * @param idRecibo The idRecibo to set.
	 */
	public void setIdRecibo(String idRecibo) {
		this.idRecibo = idRecibo;
	}
	/**
	 * @return Returns the idRenegociacion.
	 */
	public Integer getIdRenegociacion() {
		return idRenegociacion;
	}
	/**
	 * @param idRenegociacion The idRenegociacion to set.
	 */
	public void setIdRenegociacion(Integer idRenegociacion) {
		this.idRenegociacion = idRenegociacion;
	}
	/**
	 * @return Returns the importe.
	 */
	public Double getImporte() {
		return importe;
	}
	/**
	 * @param importe The importe to set.
	 */
	public void setImporte(Double importe) {
		this.importe = importe;
	}
}
