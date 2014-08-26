package com.siga.beans;

/**
 * Bean de la tabla FAC_SERIEFACTURACION_BANCO
 * @author maria jimenez
 * @since 21/08/2014
 */
public class FacSerieFacturacionBancoBean extends MasterBean {
	
	/* Variables */
	private Integer idInstitucion, idSerieFacturacion,idSufijo;
	private String bancos_codigo;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "FAC_SERIEFACTURACION_BANCO";
	
	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION 		= "IDINSTITUCION";
	static public final String C_IDSUFIJO 			= "IDSUFIJO";
	static public final String C_IDSERIEFACTURACION	= "IDSERIEFACTURACION";
	static public final String C_BANCOS_CODIGO		= "BANCOS_CODIGO";
	
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Integer getIdSerieFacturacion() {
		return idSerieFacturacion;
	}
	public void setIdSerieFacturacion(Integer idSerieFacturacion) {
		this.idSerieFacturacion = idSerieFacturacion;
	}
	public Integer getIdSufijo() {
		return idSufijo;
	}
	public void setIdSufijo(Integer idSufijo) {
		this.idSufijo = idSufijo;
	}
	public String getBancos_codigo() {
		return bancos_codigo;
	}
	public void setBancos_codigo(String bancos_codigo) {
		this.bancos_codigo = bancos_codigo;
	}

	
}
