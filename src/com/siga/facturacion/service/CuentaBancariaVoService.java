package com.siga.facturacion.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.FacBancoinstitucion;
import org.redabogacia.sigaservices.app.vo.CuentaBancariaVo;
import org.redabogacia.sigaservices.app.vo.services.VoService;

import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.facturacion.form.CuentasBancariasForm;
/**
 * 
 * @author jorgeta 
 * @date   23/05/2013
 *
 * La imaginaci�n es m�s importante que el conocimiento
 *
 */
public class CuentaBancariaVoService implements VoService<CuentasBancariasForm,CuentaBancariaVo,FacBancoinstitucion> {

	public List<CuentaBancariaVo> getDb2VoList(List<FacBancoinstitucion> dbList){
			
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
			
			if(objectForm.getSjcs()!=null &&!objectForm.getSjcs().equals(""))
				objectVo.setSjcs(objectForm.getSjcs());
			
			if(objectForm.getSufijo()!=null &&!objectForm.getSufijo().equals(""))
				objectVo.setSufijo(objectForm.getSufijo());
			if(objectForm.getBaja()!=null && !objectForm.getBaja().equals("")){
				objectVo.setBaja(objectForm.getBaja());
			}
			if(objectForm.getBaja()!=null && objectForm.getBaja().equals(AppConstants.DB_FALSE)){
				objectVo.setFechabaja(null);
			}else if(objectForm.getBaja()!=null && objectForm.getBaja().equals(AppConstants.DB_TRUE)){
				if(objectForm.getFechaBaja()!=null &&!objectForm.getFechaBaja().equals("")){
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					try {
						objectVo.setFechabaja(sdf.parse(objectForm.getFechaBaja()));
					} catch (ParseException e) {e.printStackTrace();
						
					}
				}
				objectVo.setBaja(AppConstants.DB_TRUE);
				
			}else{
				objectVo.setFechabaja(null);
				objectVo.setBaja(null);
			}
			if(objectForm.getNif()!=null &&!objectForm.getNif().equals(""))
				objectVo.setNif(objectForm.getNif());
			
			if(objectForm.getAsientoContable()!=null && !objectForm.getAsientoContable().equals(""))
				objectVo.setAsientocontable(objectForm.getAsientoContable());
			
			if(objectForm.getCuentaContableTarjeta()!=null &&!objectForm.getCuentaContableTarjeta().equals(""))
				objectVo.setCuentacontabletarjeta(objectForm.getCuentaContableTarjeta());
			
			
				
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
			
			if(objectVo.getSufijo()!=null)
				cuentasBancariasForm.setSufijo(objectVo.getSufijo());
			
			if(objectVo.getFechabaja()!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				cuentasBancariasForm.setFechaBaja(sdf.format(objectVo.getFechabaja()));
				cuentasBancariasForm.setBaja(AppConstants.DB_TRUE);
			}else{
				
				cuentasBancariasForm.setBaja(AppConstants.DB_FALSE);
			}
			
			if(objectVo.getNif()!=null)
				cuentasBancariasForm.setNif(objectVo.getNif());
			
			if(objectVo.getAsientocontable()!=null)
				cuentasBancariasForm.setAsientoContable(objectVo.getAsientocontable());
			
			if(objectVo.getCuentacontabletarjeta()!=null)
				cuentasBancariasForm.setCuentaContableTarjeta(objectVo.getCuentacontabletarjeta());
			
			cuentasBancariasForm.setBancoNombre(objectVo.getBancoNombre());
			cuentasBancariasForm.setBancoCuentaDescripcion(objectVo.getBancoCuentaDescripcion());
			cuentasBancariasForm.setUso(String.valueOf(objectVo.getUso()));
		return cuentasBancariasForm;
	}



	/* (non-Javadoc)
	 * @see org.redabogacia.sigaservices.app.vo.services.VoService#getVo2Db(java.lang.Object)
	 */
	@Override
	public FacBancoinstitucion getVo2Db(CuentaBancariaVo objectVo) {
		// TODO Auto-generated method stub
		return null;
	}




	

	
	
	

}
