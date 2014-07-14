package com.siga.facturacion.form.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.FacBancoinstitucion;
import org.redabogacia.sigaservices.app.vo.fac.CuentaBancariaVo;

import com.atos.utils.ClsConstants;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.comun.VoUiService;
import com.siga.facturacion.form.CuentasBancariasForm;
/**
 * 
 * @author jorgeta 
 * @date   23/05/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class CuentaBancariaVoService implements VoUiService<CuentasBancariasForm,CuentaBancariaVo> {

	public List<org.redabogacia.sigaservices.app.vo.fac.CuentaBancariaVo> getDb2VoList(List<FacBancoinstitucion> dbList){
			
		List<CuentaBancariaVo> objectVos = new ArrayList<CuentaBancariaVo>();
		CuentaBancariaVo  objectVo = null;
		for (FacBancoinstitucion facBancoinstitucion : dbList) {
			objectVo = new CuentaBancariaVo(facBancoinstitucion);
			objectVos.add(objectVo);
			
		}
		return objectVos;
	}

	

	@Override
	public List<CuentasBancariasForm> getVo2FormList(
			List<CuentaBancariaVo> voList) {
		List<CuentasBancariasForm> cuentaBancariaForms = new ArrayList<CuentasBancariasForm>();
		CuentasBancariasForm cuentasBancariasForm   = null;
		for (CuentaBancariaVo objectVo : voList) {
			cuentasBancariasForm = getVo2Form(objectVo);
			cuentaBancariaForms.add(cuentasBancariasForm);
			
		}
		return cuentaBancariaForms;
	}

	@Override
	public CuentaBancariaVo getForm2Vo(CuentasBancariasForm objectForm) {
		CuentaBancariaVo objectVo = new CuentaBancariaVo();		
		if(objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equals(""))
			objectVo.setIdinstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		if(objectForm.getIdCuentaBancaria()!=null && !objectForm.getIdCuentaBancaria().equals(""))
			objectVo.setBancosCodigo(objectForm.getIdCuentaBancaria());
		if(objectForm.getCodigoBanco()!=null &&!objectForm.getCodigoBanco().equals(""))
			objectVo.setCodBanco(objectForm.getCodigoBanco());
		if(objectForm.getSucursalBanco()!=null &&!objectForm.getSucursalBanco().equals(""))
			objectVo.setCodSucursal(objectForm.getSucursalBanco());
		if(objectForm.getDigControlBanco()!=null &&!objectForm.getDigControlBanco().equals(""))
			objectVo.setDigitocontrol(objectForm.getDigControlBanco());
		if(objectForm.getCuentaBanco()!=null &&!objectForm.getCuentaBanco().equals(""))
			objectVo.setNumerocuenta(objectForm.getCuentaBanco());
		if(objectForm.getImpComisionAjenaAbono()!=null &&!objectForm.getImpComisionAjenaAbono().equals(""))
			objectVo.setImpcomisionajenaabono(UtilidadesNumero.getBigDecimal(objectForm.getImpComisionAjenaAbono()));
		if(objectForm.getImpComisionAjenaCargo()!=null &&!objectForm.getImpComisionAjenaCargo().equals(""))
			objectVo.setImpcomisionajenacargo(UtilidadesNumero.getBigDecimal(objectForm.getImpComisionAjenaCargo()));
		if(objectForm.getImpComisionPropiaAbono()!=null &&!objectForm.getImpComisionPropiaAbono().equals(""))
			objectVo.setImpcomisionpropiaabono(UtilidadesNumero.getBigDecimal(objectForm.getImpComisionPropiaAbono()));
		if(objectForm.getImpComisionPropiaCargo()!=null &&!objectForm.getImpComisionPropiaCargo().equals(""))
			objectVo.setImpcomisionpropiacargo(UtilidadesNumero.getBigDecimal(objectForm.getImpComisionPropiaCargo()));
		
		if(objectForm.getSjcs()!=null &&!objectForm.getSjcs().equals("")){
			objectVo.setSjcs(objectForm.getSjcs());
		}
		
		if(objectForm.getNosjcs()!=null &&!objectForm.getNosjcs().equals("")){
			objectVo.setNosjcs(objectForm.getNosjcs());
		}
		
		if(objectForm.getBaja()!=null && !objectForm.getBaja().equals("")){
			objectVo.setBaja(objectForm.getBaja());
		}
		
		if(objectForm.getFechaBaja() != null &&!objectForm.getFechaBaja().equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				objectVo.setFechabaja(sdf.parse(objectForm.getFechaBaja()));
			} catch (ParseException e) {e.printStackTrace();
				
			}
		} else {
			objectVo.setFechabaja(null);
		}
	
		if(objectForm.getAsientoContable()!=null && !objectForm.getAsientoContable().equals(""))
			objectVo.setAsientocontable(objectForm.getAsientoContable());
		
		if(objectForm.getCuentaContableTarjeta()!=null &&!objectForm.getCuentaContableTarjeta().equals(""))
			objectVo.setCuentacontabletarjeta(objectForm.getCuentaContableTarjeta());
		
		if(objectForm.getIBAN()!=null &&!objectForm.getIBAN().equals(""))
			objectVo.setIban(objectForm.getIBAN());
		
		if(objectForm.getIdSufijo()!=null&&!objectForm.getIdSufijo().toString().equals("0"))
			objectVo.setIdsufijo(objectForm.getIdSufijo());
		
		if(objectForm.getIdSufijosjcs()!=null&&!objectForm.getIdSufijosjcs().toString().equals("0"))
			objectVo.setIdsufijosjcs(objectForm.getIdSufijosjcs());
			
		return objectVo;
			
		
	}

	@Override
	public CuentasBancariasForm getVo2Form(CuentaBancariaVo objectVo) {
		CuentasBancariasForm cuentasBancariasForm = new CuentasBancariasForm();
			if(objectVo.getBancosCodigo()!=null)
				cuentasBancariasForm.setIdCuentaBancaria(objectVo.getBancosCodigo());
			
			
			if(objectVo.getCodBanco()!=null)
				cuentasBancariasForm.setCodigoBanco(objectVo.getCodBanco());
			
			if(objectVo.getCodSucursal()!=null)
				cuentasBancariasForm.setSucursalBanco(objectVo.getCodSucursal());
			
			if(objectVo.getDigitocontrol()!=null)
				cuentasBancariasForm.setDigControlBanco(objectVo.getDigitocontrol());
			
					
			if(objectVo.getNumerocuenta()!=null)
				cuentasBancariasForm.setCuentaBanco(objectVo.getNumerocuenta());
			
			if(objectVo.getIdinstitucion()!=null)
				cuentasBancariasForm.setIdInstitucion(objectVo.getIdinstitucion().toString());
			
			if(objectVo.getImpcomisionajenaabono()!=null)
				cuentasBancariasForm.setImpComisionAjenaAbono(UtilidadesString.formatoImporte(objectVo.getImpcomisionajenaabono().doubleValue()));
			if(objectVo.getImpcomisionajenacargo()!=null)
				cuentasBancariasForm.setImpComisionAjenaCargo(UtilidadesString.formatoImporte(objectVo.getImpcomisionajenacargo().doubleValue()));
			if(objectVo.getImpcomisionpropiaabono()!=null)
				cuentasBancariasForm.setImpComisionPropiaAbono(UtilidadesString.formatoImporte(objectVo.getImpcomisionpropiaabono().doubleValue()));
			if(objectVo.getImpcomisionpropiacargo()!=null)
				cuentasBancariasForm.setImpComisionPropiaCargo(UtilidadesString.formatoImporte(objectVo.getImpcomisionpropiacargo().doubleValue()));
			
			if(objectVo.getSjcs()!=null){
				cuentasBancariasForm.setSjcs(objectVo.getSjcs());
			} 
			
			if(objectVo.getNosjcs()!=null){
				cuentasBancariasForm.setNosjcs(objectVo.getNosjcs());
			} 
			
			if(objectVo.getFechabaja()!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				cuentasBancariasForm.setFechaBaja(sdf.format(objectVo.getFechabaja()));
			}
			
			if(objectVo.getAsientocontable()!=null)
				cuentasBancariasForm.setAsientoContable(objectVo.getAsientocontable());
			
			if(objectVo.getCuentacontabletarjeta()!=null)
				cuentasBancariasForm.setCuentaContableTarjeta(objectVo.getCuentacontabletarjeta());
			
			if(objectVo.getIban()!=null)
				cuentasBancariasForm.setIBAN(UtilidadesString.mostrarIBANConAsteriscos(objectVo.getIban()));
			
			cuentasBancariasForm.setBancoNombre(objectVo.getBancoNombre());
			cuentasBancariasForm.setBancoCuentaDescripcion(objectVo.getBancoCuentaDescripcion());
			cuentasBancariasForm.setUso(String.valueOf(objectVo.getUso()));
			
			if(objectVo.getIdsufijo()!=null&&!objectVo.getIdsufijo().toString().equals("0"))
				cuentasBancariasForm.setIdSufijo(objectVo.getIdsufijo());
			
			if(objectVo.getIdsufijosjcs()!=null&&!objectVo.getIdsufijosjcs().toString().equals("0"))
				cuentasBancariasForm.setIdSufijosjcs(objectVo.getIdsufijosjcs());
			
		return cuentasBancariasForm;
	}
	

}
