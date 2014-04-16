/*
 * Created on Dec 14, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.censo.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenSolicModiCuentasBean;
import com.siga.general.MasterForm;

/**
 * @author nuria.rgonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CuentasBancariasForm extends MasterForm
{
	private String incluirRegistrosConBajaLogica;
	private String IBAN;
	private String BIC;	
	private String[] modos = {"informacionCuentaBancaria", "listadoMandatosCuentaBancaria"};

	public void setCuentaAbono(Boolean dato) {
		UtilidadesHash.set(this.datos, "CUENTA_ABONO", dato);
	}
	
	public void setCuentaCargo(Boolean dato) {
		UtilidadesHash.set(this.datos, "CUENTA_CARGO", dato);
	}
	
	public void setSociedad(Boolean dato) {
		UtilidadesHash.set(this.datos, "SOCIEDAD", dato);
	}
	
	public void setTitular(String titular) {	
		this.datos.put(CenCuentasBancariasBean.C_TITULAR, titular);
	}
	
	public void setAbonoCargo(Boolean abonoCargo) {		
		UtilidadesHash.set(this.datos, CenCuentasBancariasBean.C_ABONOCARGO, abonoCargo); 
	}
	
	public void setAbonoSJCS(Boolean abonoSJCS) {
		UtilidadesHash.set(this.datos, CenCuentasBancariasBean.C_ABONOSJCS, abonoSJCS);
	}
	
	public void setCuentaContable(String cuentaContable) {
		this.datos.put(CenCuentasBancariasBean.C_CUENTACONTABLE, cuentaContable);
	}
	
	public void setCbo_Codigo(String cbo_Codigo) {
		this.datos.put(CenCuentasBancariasBean.C_CBO_CODIGO, cbo_Codigo);
	}
	
	public void setCodigoSucursal(String codigoSucursal) {
		this.datos.put(CenCuentasBancariasBean.C_CODIGOSUCURSAL, codigoSucursal);
	}
	
	public void setDigitoControl(String digitoControl) {
		this.datos.put(CenCuentasBancariasBean.C_DIGITOCONTROL, digitoControl);
	}
	
	public void setNumeroCuenta(String numeroCuenta) {
		this.datos.put(CenCuentasBancariasBean.C_NUMEROCUENTA, numeroCuenta);
	}
	
	public void setMotivo(String motivo) {
		UtilidadesHash.set(this.datos, CenSolicModiCuentasBean.C_MOTIVO, motivo);
	}
	
	public void setIdPersona(Long dato) {
		UtilidadesHash.set(this.datos, CenCuentasBancariasBean.C_IDPERSONA, dato);
	}
	
	public void setIdInstitucion(Integer dato) {
		UtilidadesHash.set(this.datos, CenCuentasBancariasBean.C_IDINSTITUCION, dato);
	}
	
	public void setIdCuenta(Integer dato) {
		UtilidadesHash.set(this.datos, CenCuentasBancariasBean.C_IDCUENTA, dato);
	}
	
//	metodos get de los campos del formulario
	public Boolean getCuentaAbono() {
		return UtilidadesHash.getBoolean(this.datos, "CUENTA_ABONO");
	}
	
	public Boolean getCuentaCargo() {
		return UtilidadesHash.getBoolean(this.datos, "CUENTA_CARGO");
	}
	
	public Boolean getSociedad() {
		return UtilidadesHash.getBoolean(this.datos, "SOCIEDAD");
	}
	
	public String getTitular() {
		return (String)this.datos.get(CenCuentasBancariasBean.C_TITULAR);
	}
	
	public Boolean getAbonoCargo() {
		return UtilidadesHash.getBoolean(this.datos, CenCuentasBancariasBean.C_ABONOCARGO);
	}	

	public Boolean getAbonoSJCS() {
		return UtilidadesHash.getBoolean(this.datos, CenCuentasBancariasBean.C_ABONOSJCS);
	}
			
	public String getCuentaContable() {
		return (String)this.datos.get(CenCuentasBancariasBean.C_CUENTACONTABLE);
	}
	
	public String getCbo_Codigo() {
		return (String)this.datos.get(CenCuentasBancariasBean.C_CBO_CODIGO);
	}	
	
	public String getCodigoSucursal() {
		return (String)this.datos.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL);
	}
	
	public String getDigitoControl() {
		return (String)this.datos.get(CenCuentasBancariasBean.C_DIGITOCONTROL);
	}
	
	public String getNumeroCuenta() {
		return (String)this.datos.get(CenCuentasBancariasBean.C_NUMEROCUENTA);
	}
	
	public String getMotivo() {
		return UtilidadesHash.getString(this.datos, CenSolicModiCuentasBean.C_MOTIVO);
	}

	public Long getIdPersona() {
		return UtilidadesHash.getLong(this.datos, CenCuentasBancariasBean.C_IDPERSONA);
	}

	public Integer getIdInstitucion() {
		return UtilidadesHash.getInteger(this.datos, CenCuentasBancariasBean.C_IDINSTITUCION);
	}
	
	public Integer getIdCuenta() {
		return UtilidadesHash.getInteger(this.datos, CenCuentasBancariasBean.C_IDCUENTA);
	}

	public String getIncluirRegistrosConBajaLogica() {
		return this.incluirRegistrosConBajaLogica;
	}
	public void setIncluirRegistrosConBajaLogica(String s) {
		this.incluirRegistrosConBajaLogica = s;
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
	
	public String[] getModos () {return this.modos;}
}