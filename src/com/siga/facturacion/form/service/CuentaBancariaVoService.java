package com.siga.facturacion.form.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.autogen.model.FacBancoinstitucion;
import org.redabogacia.sigaservices.app.vo.fac.CuentaBancariaVo;

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
		if (objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equals(""))
			objectVo.setIdinstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		
		if (objectForm.getIdCuentaBancaria()!=null && !objectForm.getIdCuentaBancaria().equals(""))
			objectVo.setBancosCodigo(objectForm.getIdCuentaBancaria());
		
		if (objectForm.getCodigoBanco()!=null && !objectForm.getCodigoBanco().equals(""))
			objectVo.setCodBanco(objectForm.getCodigoBanco());
		
		if (objectForm.getBancosCodigo()!=null && !objectForm.getBancosCodigo().equals(""))
			objectVo.setBancosCodigo(objectForm.getBancosCodigo());
		
		if (objectForm.getSucursalBanco()!=null && !objectForm.getSucursalBanco().equals(""))
			objectVo.setCodSucursal(objectForm.getSucursalBanco());
		
		if (objectForm.getDigControlBanco()!=null && !objectForm.getDigControlBanco().equals(""))
			objectVo.setDigitocontrol(objectForm.getDigControlBanco());
		
		if (objectForm.getCuentaBanco()!=null && !objectForm.getCuentaBanco().equals(""))
			objectVo.setNumerocuenta(objectForm.getCuentaBanco());
		
		if (objectForm.getSjcs()!=null && !objectForm.getSjcs().equals("")){
			objectVo.setSjcs(objectForm.getSjcs());
		}
		
		if (objectForm.getBaja()!=null && !objectForm.getBaja().equals("")){
			objectVo.setBaja(objectForm.getBaja());
		}
		
		if (objectForm.getFechaBaja() != null && !objectForm.getFechaBaja().equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				objectVo.setFechabaja(sdf.parse(objectForm.getFechaBaja()));
			} catch (ParseException e) {e.printStackTrace();
				
			}
		} else {
			objectVo.setFechabaja(null);
		}
	
		if (objectForm.getAsientoContable()!=null && !objectForm.getAsientoContable().equals(""))
			objectVo.setAsientocontable(objectForm.getAsientoContable());
		
		if (objectForm.getCuentaContableTarjeta()!=null && !objectForm.getCuentaContableTarjeta().equals(""))
			objectVo.setCuentacontabletarjeta(objectForm.getCuentaContableTarjeta());
		
		if (objectForm.getIBAN()!=null && !objectForm.getIBAN().equals(""))
			objectVo.setIban(objectForm.getIBAN());
		
		if (objectForm.getIdSufijosjcs()!=null && !objectForm.getIdSufijosjcs().toString().equals("0"))
			objectVo.setIdsufijosjcs(objectForm.getIdSufijosjcs());
		
		// JPT (19-08-2014): Nuevos valores para la comision
		if (objectForm.getComisionimporte()!=null && !objectForm.getComisionimporte().equals(""))
			objectVo.setComisionimporte(new BigDecimal(objectForm.getComisionimporte()));
		if (objectForm.getIdTipoIva()!=null && !objectForm.getIdTipoIva().equals(""))
			objectVo.setIdtipoiva(Integer.valueOf(objectForm.getIdTipoIva()));
		if (objectForm.getComisiondescripcion()!=null && !objectForm.getComisiondescripcion().equals(""))
			objectVo.setComisiondescripcion(objectForm.getComisiondescripcion());
		if (objectForm.getComisioncuentacontable()!=null && !objectForm.getComisioncuentacontable().equals(""))
			objectVo.setComisioncuentacontable(objectForm.getComisioncuentacontable());
		
		if (objectForm.getConfiguracionFicherosEsquema()!=null && !objectForm.getConfiguracionFicherosEsquema().equals(""))
			objectVo.setConfigficherosesquema(new BigDecimal(objectForm.getConfiguracionFicherosEsquema()));
		if (objectForm.getConfiguracionFicherosSecuencia()!=null && !objectForm.getConfiguracionFicherosSecuencia().equals(""))
			objectVo.setConfigficherossecuencia(new BigDecimal(objectForm.getConfiguracionFicherosSecuencia()));
		if (objectForm.getConfiguracionLugarEsquemaSecuencia()!=null && !objectForm.getConfiguracionLugarEsquemaSecuencia().equals(""))
			objectVo.setConfiglugaresquemasecuencia(new BigDecimal(objectForm.getConfiguracionLugarEsquemaSecuencia()));
			
		return objectVo;
	}

	@Override
	public CuentasBancariasForm getVo2Form(CuentaBancariaVo objectVo) {
		CuentasBancariasForm cuentasBancariasForm = new CuentasBancariasForm();
			if(objectVo.getBancosCodigo()!=null)
				cuentasBancariasForm.setIdCuentaBancaria(objectVo.getBancosCodigo());
			
			
			if(objectVo.getCodBanco()!=null)
				cuentasBancariasForm.setCodigoBanco(objectVo.getCodBanco());
			
			if(objectVo.getBancosCodigo()!=null)
				cuentasBancariasForm.setBancosCodigo(objectVo.getBancosCodigo());
			
			if(objectVo.getCodSucursal()!=null)
				cuentasBancariasForm.setSucursalBanco(objectVo.getCodSucursal());
			
			if(objectVo.getDigitocontrol()!=null)
				cuentasBancariasForm.setDigControlBanco(objectVo.getDigitocontrol());
			
					
			if(objectVo.getNumerocuenta()!=null)
				cuentasBancariasForm.setCuentaBanco(objectVo.getNumerocuenta());
			
			if(objectVo.getIdinstitucion()!=null)
				cuentasBancariasForm.setIdInstitucion(objectVo.getIdinstitucion().toString());
			
			if(objectVo.getSjcs()!=null){
				cuentasBancariasForm.setSjcs(objectVo.getSjcs());
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

			if(objectVo.getIdsufijosjcs()!=null&&!objectVo.getIdsufijosjcs().toString().equals("0"))
				cuentasBancariasForm.setIdSufijosjcs(objectVo.getIdsufijosjcs());
			
			// JPT (19-08-2014): Nuevos valores para la comision
			if (objectVo.getComisionimporte()!=null)
				cuentasBancariasForm.setComisionimporte(UtilidadesString.formatoImporte(objectVo.getComisionimporte().doubleValue()));
			if (objectVo.getIdtipoiva()!=null)
				cuentasBancariasForm.setIdTipoIva(objectVo.getIdtipoiva().toString());
			if (objectVo.getComisiondescripcion()!=null)
				cuentasBancariasForm.setComisiondescripcion(objectVo.getComisiondescripcion());
			if (objectVo.getComisioncuentacontable()!=null)
				cuentasBancariasForm.setComisioncuentacontable(objectVo.getComisioncuentacontable());
			
			if (objectVo.getConfigficherosesquema()!=null)
				cuentasBancariasForm.setConfiguracionFicherosEsquema(objectVo.getConfigficherosesquema().toString());
			if (objectVo.getConfigficherossecuencia()!=null)
				cuentasBancariasForm.setConfiguracionFicherosSecuencia(objectVo.getConfigficherossecuencia().toString());
			if (objectVo.getConfiglugaresquemasecuencia()!=null)
				cuentasBancariasForm.setConfiguracionLugarEsquemaSecuencia(objectVo.getConfiglugaresquemasecuencia().toString());
			
		return cuentasBancariasForm;
	}



	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getVo2Form(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CuentasBancariasForm getVo2Form(CuentaBancariaVo objectVo,
			CuentasBancariasForm objectForm) {
		// TODO Auto-generated method stub
		return null;
	}
}
