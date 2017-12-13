/**
 * @author CGP -  02/11/2017	
 */

package com.siga.beans;


public class FacHistoricoFacturaBean extends MasterBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 732483688847351168L;
	/* Variables */
	private Integer idInstitucion;
	private String 	idFactura;
	private Integer idHistorico;
	private String  fechaModificacion;
	private Integer usuModificacion;
	private Integer idTipoAccion;
	private Integer idFormaPago;
	private Integer idPersona;
	private Integer idCuenta;
	private Integer IdPersonaDeudor;
	private Integer idCuentaDeudor;
	private Double impTotalAnticipado;
	private Double impTotalPagadoPorCaja;
	private Double impTotalPagadoSoloCaja;
	private Double impTotalPagadoSoloTarjeta;
	private Double impTotalPagadoPorBanco;
	private Double impTotalPagado;
	private Double impTotalPorPagar;
	private Double impTotalCompensado;
	private Integer estado;
	private Integer idPagoPorCaja;
	private Integer idDisqueteCargos;
	private Integer idFacturaIncluidaEnDisquete;
	private Integer idDisqueteDevoluciones;
	private String idRecibo;
	private Integer idRenegociacion;
	private Integer idAbono;
	private String comisionIdFactura;
	

	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_HISTORICOFACTURA";
	
	/* Nombre campos de la tabla */
	static public final String C_IDFACTURA		= "IDFACTURA";	
	static public final String C_IDINSTITUCION	= "IDINSTITUCION";
	static public final String C_FECHAMODIFICACION	= "FECHAMODIFICACION";
	static public final String C_IDHISTORICO	= "IDHISTORICO";
	static public final String C_USUMODIFICACION	= "USUMODIFICACION";
	static public final String C_IDTIPOACCION	= "IDTIPOACCION";
	static public final String C_IDFORMAPAGO	= "IDFORMAPAGO";
	static public final String C_IDPERSONA	= "IDPERSONA";
	static public final String C_IDCUENTA		= "IDCUENTA";
	static public final String C_IDPERSONADEUDOR= "IDPERSONADEUDOR";
	static public final String C_IDCUENTADEUDOR = "IDCUENTADEUDOR";

	static public final String C_IMPTOTALANTICIPADO = "IMPTOTALANTICIPADO";
	static public final String C_IMPTOTALPAGADOPORCAJA = "IMPTOTALPAGADOPORCAJA";
	static public final String C_IMPTOTALPAGADOSOLOCAJA = "IMPTOTALPAGADOSOLOCAJA";
	static public final String C_IMPTOTALPAGADOSOLOTARJETA = "IMPTOTALPAGADOSOLOTARJETA";
	static public final String C_IMPTOTALPAGADOPORBANCO = "IMPTOTALPAGADOPORBANCO";
	static public final String C_IMPTOTALPAGADO = "IMPTOTALPAGADO";
	static public final String C_IMPTOTALPORPAGAR = "IMPTOTALPORPAGAR";
	static public final String C_IMPTOTALCOMPENSADO = "IMPTOTALCOMPENSADO";	
	
	static public final String C_ESTADO = "ESTADO";
	static public final String C_IDPAGOPORCAJA = "IDPAGOPORCAJA";
	static public final String C_IDDISQUETECARGOS = "IDDISQUETECARGOS";
	static public final String C_IDFACTURAINCLUIDAENDISQUETE = "IDFACTURAINCLUIDAENDISQUETE";
	static public final String C_IDDISQUETEDEVOLUCIONES = "IDDISQUETEDEVOLUCIONES";
	static public final String C_IDRECIBO = "IDRECIBO";
	static public final String C_IDRENEGOCIACION = "IDRENEGOCIACION";
	static public final String C_IDABONO = "IDABONO";
	static public final String C_COMISIONIDFACTURA = "COMISIONIDFACTURA";
	
	
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
	public Integer getIdHistorico() {
		return idHistorico;
	}
	public void setIdHistorico(Integer idHistorico) {
		this.idHistorico = idHistorico;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
	public Integer getIdTipoAccion() {
		return idTipoAccion;
	}
	public void setIdTipoAccion(Integer idTipoAccion) {
		this.idTipoAccion = idTipoAccion;
	}
	public Integer getIdFormaPago() {
		return idFormaPago;
	}
	public void setIdFormaPago(Integer idFormaPago) {
		this.idFormaPago = idFormaPago;
	}
	public Integer getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	public Integer getIdCuenta() {
		return idCuenta;
	}
	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}
	public Integer getIdPersonaDeudor() {
		return IdPersonaDeudor;
	}
	public void setIdPersonaDeudor(Integer idPersonaDeudor) {
		IdPersonaDeudor = idPersonaDeudor;
	}
	public Integer getIdCuentaDeudor() {
		return idCuentaDeudor;
	}
	public void setIdCuentaDeudor(Integer idCuentaDeudor) {
		this.idCuentaDeudor = idCuentaDeudor;
	}
	public Double getImpTotalAnticipado() {
		return impTotalAnticipado;
	}
	public void setImpTotalAnticipado(Double impTotalAnticipado) {
		this.impTotalAnticipado = impTotalAnticipado;
	}
	public Double getImpTotalPagadoPorCaja() {
		return impTotalPagadoPorCaja;
	}
	public void setImpTotalPagadoPorCaja(Double impTotalPagadoPorCaja) {
		this.impTotalPagadoPorCaja = impTotalPagadoPorCaja;
	}
	public Double getImpTotalPagadoSoloCaja() {
		return impTotalPagadoSoloCaja;
	}
	public void setImpTotalPagadoSoloCaja(Double impTotalPagadoSoloCaja) {
		this.impTotalPagadoSoloCaja = impTotalPagadoSoloCaja;
	}
	public Double getImpTotalPagadoSoloTarjeta() {
		return impTotalPagadoSoloTarjeta;
	}
	public void setImpTotalPagadoSoloTarjeta(Double impTotalPagadoSoloTarjeta) {
		this.impTotalPagadoSoloTarjeta = impTotalPagadoSoloTarjeta;
	}
	public Double getImpTotalPagadoPorBanco() {
		return impTotalPagadoPorBanco;
	}
	public void setImpTotalPagadoPorBanco(Double impTotalPagadoPorBanco) {
		this.impTotalPagadoPorBanco = impTotalPagadoPorBanco;
	}
	public Double getImpTotalPagado() {
		return impTotalPagado;
	}
	public void setImpTotalPagado(Double impTotalPagado) {
		this.impTotalPagado = impTotalPagado;
	}
	public Double getImpTotalPorPagar() {
		return impTotalPorPagar;
	}
	public void setImpTotalPorPagar(Double impTotalPorPagar) {
		this.impTotalPorPagar = impTotalPorPagar;
	}
	public Double getImpTotalCompensado() {
		return impTotalCompensado;
	}
	public void setImpTotalCompensado(Double impTotalCompensado) {
		this.impTotalCompensado = impTotalCompensado;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public Integer getIdPagoPorCaja() {
		return idPagoPorCaja;
	}
	public void setIdPagoPorCaja(Integer idPagoPorCaja) {
		this.idPagoPorCaja = idPagoPorCaja;
	}
	public Integer getIdDisqueteCargos() {
		return idDisqueteCargos;
	}
	public void setIdDisqueteCargos(Integer idDisqueteCargos) {
		this.idDisqueteCargos = idDisqueteCargos;
	}
	public Integer getIdFacturaIncluidaEnDisquete() {
		return idFacturaIncluidaEnDisquete;
	}
	public void setIdFacturaIncluidaEnDisquete(Integer idFacturaIncluidaEnDisquete) {
		this.idFacturaIncluidaEnDisquete = idFacturaIncluidaEnDisquete;
	}
	public Integer getIdDisqueteDevoluciones() {
		return idDisqueteDevoluciones;
	}
	public void setIdDisqueteDevoluciones(Integer idDisqueteDevoluciones) {
		this.idDisqueteDevoluciones = idDisqueteDevoluciones;
	}
	public String getIdRecibo() {
		return idRecibo;
	}
	public void setIdRecibo(String idRecibo) {
		this.idRecibo = idRecibo;
	}
	public Integer getIdRenegociacion() {
		return idRenegociacion;
	}
	public void setIdRenegociacion(Integer idRenegociacion) {
		this.idRenegociacion = idRenegociacion;
	}
	public Integer getIdAbono() {
		return idAbono;
	}
	public void setIdAbono(Integer idAbono) {
		this.idAbono = idAbono;
	}
	public String getComisionIdFactura() {
		return comisionIdFactura;
	}
	public void setComisionIdFactura(String comisionIdFactura) {
		this.comisionIdFactura = comisionIdFactura;
	}

}
