package com.siga.facturacion.form;

import java.util.ArrayList;
import java.util.List;

import com.siga.general.MasterForm;
/**
 * 
 * @author jorgeta 
 * @date   23/05/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class CuentasBancariasForm extends MasterForm {
	public CuentasBancariasForm() {
		
	}
	
//	public CuentasBancariasForm(CuentaBancariaVo cuentaBancariaVo) {
//		this.setAsientoContable(cuentaBancariaVo.getAsientocontable());
//		this.setIdCuentaBancaria(cuentaBancariaVo.getBancosCodigo());
//		this.setCodigoBanco(cuentaBancariaVo.getCodBanco());
//		this.setCodigoBanco(cuentaBancariaVo.getCodSucursal());
//		this.setCuentaContableTarjeta(cuentaBancariaVo.getCuentacontabletarjeta());
//		this.setDigControlBanco(cuentaBancariaVo.getDigitocontrol());
//
//		if(cuentaBancariaVo.getFechabaja()!=null){
//			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//			this.setFechaBaja(sdf.format(cuentaBancariaVo.getFechabaja()));
//		}
////		this.setFechaBaja(facBancoinstitucion.getFechamodificacion());
//		
//		
//		
//		
//		
//		if(cuentaBancariaVo.getIdinstitucion()!=null)
//			this.setIdInstitucion(cuentaBancariaVo.getIdinstitucion().toString());
//		
//		if(cuentaBancariaVo.getImpcomisionajenaabono()!=null)
//			this.setImpComisionAjenaAbono(UtilidadesString.formatoImporte(cuentaBancariaVo.getImpcomisionajenaabono().doubleValue()));
//		if(cuentaBancariaVo.getImpcomisionajenacargo()!=null)
//			this.setImpComisionAjenaCargo(UtilidadesString.formatoImporte(cuentaBancariaVo.getImpcomisionajenacargo().doubleValue()));
//		if(cuentaBancariaVo.getImpcomisionpropiaabono()!=null)
//			this.setImpComisionPropiaAbono(UtilidadesString.formatoImporte(cuentaBancariaVo.getImpcomisionpropiaabono().doubleValue()));
//		if(cuentaBancariaVo.getImpcomisionpropiacargo()!=null)
//			this.setImpComisionPropiaCargo(UtilidadesString.formatoImporte(cuentaBancariaVo.getImpcomisionpropiacargo().doubleValue()));
//
//		this.setNif(cuentaBancariaVo.getNif());
//		this.setCuentaBanco(cuentaBancariaVo.getNumerocuenta());
//		this.setSufijo(cuentaBancariaVo.getSufijo());
//		this.setBancoNombre(cuentaBancariaVo.getBancoNombre());
//		this.setBancoCuentaDescripcion(cuentaBancariaVo.getBancoCuentaDescripcion());
//	}
	
	String modo;
	String idInstitucion;
	
//	String bancosCodigo; _Sustituyo el nombre del campo por idcuentaBancaria para evistar confusiones con el codigo del banco
	String idCuentaBancaria;
	String nif;
	String codigoBanco;
	String sucursalBanco;
	String digControlBanco;
	String cuentaBanco;
	String impComisionPropiaCargo;
	String impComisionPropiaAbono;
	String impComisionAjenaCargo;
	String impComisionAjenaAbono;
	String fechaBaja;
	String baja;
	String asientoContable;
	String cuentaContableTarjeta;
	String sjcs;
	Integer idSufijosjcs;
	String idSerieFacturacion;
	
	
	String bancoNombre;
	String bancoCuentaDescripcion;
	String uso;
	String bancoAjaxIn;
	String bancoAjaxOut;
	String IBAN;
	String BIC;
	
	String listaSeries;
	String bancosCodigo;

	
//	FIXME Por prisas no hemos creado Formulario de series de facturacion. Deberia ser una lis de Forms 
//	List<FacSeriefacturacion> seriesFacturacion;
	
	
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getCodigoBanco() {
		return codigoBanco;
	}
	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}
	public String getSucursalBanco() {
		return sucursalBanco;
	}
	public void setSucursalBanco(String sucursalBanco) {
		this.sucursalBanco = sucursalBanco;
	}
	
	public String getCuentaBanco() {
		return cuentaBanco;
	}
	public void setCuentaBanco(String cuentaBanco) {
		this.cuentaBanco = cuentaBanco;
	}
	public String getImpComisionPropiaCargo() {
		return impComisionPropiaCargo;
	}
	public void setImpComisionPropiaCargo(String impComisionPropiaCargo) {
		this.impComisionPropiaCargo = impComisionPropiaCargo;
	}
	public String getImpComisionPropiaAbono() {
		return impComisionPropiaAbono;
	}
	public void setImpComisionPropiaAbono(String impComisionPropiaAbono) {
		this.impComisionPropiaAbono = impComisionPropiaAbono;
	}
	public String getImpComisionAjenaCargo() {
		return impComisionAjenaCargo;
	}
	public void setImpComisionAjenaCargo(String impComisionAjenaCargo) {
		this.impComisionAjenaCargo = impComisionAjenaCargo;
	}
	public String getImpComisionAjenaAbono() {
		return impComisionAjenaAbono;
	}
	public void setImpComisionAjenaAbono(String impComisionAjenaAbono) {
		this.impComisionAjenaAbono = impComisionAjenaAbono;
	}
	public String getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	public String getAsientoContable() {
		return asientoContable;
	}
	public void setAsientoContable(String asientoContable) {
		this.asientoContable = asientoContable;
	}
	public String getCuentaContableTarjeta() {
		return cuentaContableTarjeta;
	}
	public void setCuentaContableTarjeta(String cuentaContableTarjeta) {
		this.cuentaContableTarjeta = cuentaContableTarjeta;
	}
	public String getSjcs() {
		return sjcs;
	}
	public void setSjcs(String sjcs) {
		this.sjcs = sjcs;
	}
	public Integer getIdSufijosjcs() {
		return idSufijosjcs;
	}
	public void setIdSufijosjcs(Integer idSufijosjcs) {
		this.idSufijosjcs = idSufijosjcs;
	}
	public String getModo() {
		return modo;
	}
	public void setModo(String modo) {
		this.modo = modo;
	}
	public String getDigControlBanco() {
		return digControlBanco;
	}
	public void setDigControlBanco(String digControlBanco) {
		this.digControlBanco = digControlBanco;
	}
	public String getBancoAjaxIn() {
		return bancoAjaxIn;
	}
	public void setBancoAjaxIn(String bancoAjaxIn) {
		this.bancoAjaxIn = bancoAjaxIn;
	}
	public String getBancoAjaxOut() {
		return bancoAjaxOut;
	}
	public void setBancoAjaxOut(String bancoAjaxOut) {
		this.bancoAjaxOut = bancoAjaxOut;
	}
	public String getIdCuentaBancaria() {
		return idCuentaBancaria;
	}
	public void setIdCuentaBancaria(String idCuentaBancaria) {
		this.idCuentaBancaria = idCuentaBancaria;
	}
	public String getBancoNombre() {
		return bancoNombre;
	}
	public void setBancoNombre(String bancoNombre) {
		this.bancoNombre = bancoNombre;
	}
	
	public String getUso() {
		return uso;
	}
	public void setUso(String uso) {
		this.uso = uso;
	}
	public String getBaja() {
		return baja;
	}
	public void setBaja(String baja) {
		this.baja = baja;
	}
	public String getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public String getIBAN() {
		return IBAN;
	}
	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}
	public String getBIC() {
		return BIC;
	}
	public void setBIC(String bIC) {
		BIC = bIC;
	}
	public String getBancoCuentaDescripcion() {
		if(bancoCuentaDescripcion!=null && !bancoCuentaDescripcion.equals(""))
			return bancoCuentaDescripcion;
		else{
			if(getIdCuentaBancaria()!=null && !getIdCuentaBancaria().equals("")){
				StringBuffer buffer = new StringBuffer();
				buffer.append(getCodigoBanco());
				buffer.append("-");
				buffer.append(getSucursalBanco());
				buffer.append("-");
				buffer.append(getDigControlBanco()!=null?getDigControlBanco():"");
				buffer.append("-");
				buffer.append(getCuentaBanco());
				return buffer.toString();
				
			}else
				return null;
			
		}
		
	}

	public void setBancoCuentaDescripcion(String bancoCuentaDescripcion) {
		this.bancoCuentaDescripcion = bancoCuentaDescripcion;
	}
	
//	public List<FacSeriefacturacion> getSeriesFacturacion() {
//		return seriesFacturacion;
//	}
//	public void setSeriesFacturacion(List<FacSeriefacturacion> seriesFacturacion) {
//		this.seriesFacturacion = seriesFacturacion;
//	}
//	 
	
	public String getListaSeries() {
		return listaSeries;
	}
	public void setListaSeries(String listaSeries) {
		this.listaSeries = listaSeries;
	}

	
	public String getIdSerieFacturacion() {
		return idSerieFacturacion;
	}
	public void setIdSerieFacturacion(String idSerieFacturacion) {
		this.idSerieFacturacion = idSerieFacturacion;
	}
	public String getBancosCodigo() {
		return bancosCodigo;
	}
	public void setBancosCodigo(String bancosCodigo) {
		this.bancosCodigo = bancosCodigo;
	}
	
	
}
