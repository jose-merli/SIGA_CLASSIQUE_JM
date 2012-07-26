package com.siga.gratuita.service.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.form.InformeForm;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.ScsActuacionAsistCosteFijoAdm;
import com.siga.beans.ScsActuacionAsistCosteFijoBean;
import com.siga.beans.ScsActuacionAsistenciaAdm;
import com.siga.beans.ScsActuacionAsistenciaBean;
import com.siga.beans.ScsAsistenciasAdm;
import com.siga.beans.ScsAsistenciasBean;
import com.siga.beans.ScsCabeceraGuardiasAdm;
import com.siga.beans.ScsComisariaAdm;
import com.siga.beans.ScsComisariaBean;
import com.siga.beans.ScsJuzgadoAdm;
import com.siga.beans.ScsJuzgadoBean;
import com.siga.beans.ScsPrisionAdm;
import com.siga.beans.ScsPrisionBean;
import com.siga.beans.ScsTipoActuacionAdm;
import com.siga.beans.ScsTipoActuacionBean;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.ActuacionAsistenciaForm;
import com.siga.gratuita.form.AsistenciaForm;
import com.siga.gratuita.service.AsistenciasService;
import com.siga.gratuita.service.VolantesExpressService;
import com.siga.gratuita.vos.VolantesExpressVo;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.JtaBusinessServiceTemplate;

public class AtosAsistenciasService extends JtaBusinessServiceTemplate 
	implements AsistenciasService {

	public AtosAsistenciasService(BusinessManager businessManager) {
		super(businessManager);
	}
	public List<ActuacionAsistenciaForm> getActuacionesAsistencia(AsistenciaForm asistenciaForm, UsrBean usrBean)
			throws ClsExceptions {
		ScsActuacionAsistenciaAdm  actuacionAsistenciaAdm = new ScsActuacionAsistenciaAdm(usrBean);
		List<ActuacionAsistenciaForm> lista = actuacionAsistenciaAdm.getActuacionesAsistencia(asistenciaForm.getAsistenciaVO(), usrBean);
		return lista;
	}
	
	public ActuacionAsistenciaForm getActuacionAsistencia(ActuacionAsistenciaForm actuacionAsistenciaForm, UsrBean usrBean)throws ClsExceptions {
		ScsActuacionAsistenciaAdm actuacionAsistenciaAdm = new ScsActuacionAsistenciaAdm(usrBean);
		ScsActuacionAsistenciaBean actuacionAsistenciaBean = actuacionAsistenciaAdm.getActuacionAsistencia(actuacionAsistenciaForm);
		
		ActuacionAsistenciaForm actuacionAsistenciaFormEdicion= actuacionAsistenciaBean.getActuacionAsistenciaForm();
		ScsActuacionAsistCosteFijoAdm scsActuacionAsistCosteFijoAdm = new ScsActuacionAsistCosteFijoAdm(usrBean);
		Integer idCostefijo = scsActuacionAsistCosteFijoAdm.getTipoCosteFijoActuacion(actuacionAsistenciaFormEdicion.getActuacionAsistenciaVO());
		if(idCostefijo!=null){
			actuacionAsistenciaFormEdicion.setIdCosteFijoActuacion(idCostefijo.toString());
		}
		return actuacionAsistenciaFormEdicion;
	}
	
	public void borrarActuacionAsistencia(
			ActuacionAsistenciaForm actuacionAsistenciaForm, UsrBean usrBean)
			throws ClsExceptions{
	
		ScsActuacionAsistenciaAdm actuacionAsistenciaAdm = new ScsActuacionAsistenciaAdm(usrBean);
		ScsActuacionAsistenciaBean actuaAsistenciaBean = actuacionAsistenciaForm.getActuacionAsistenciaVO();
		
		ScsCabeceraGuardiasAdm cabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm(usrBean);
		cabeceraGuardiasAdm.actualizarValidacionCabecera(actuacionAsistenciaForm.getIdInstitucion(),actuacionAsistenciaForm.getAnio(),actuacionAsistenciaForm.getNumero(),false);
		
		ScsActuacionAsistCosteFijoAdm admActuacionAsistCosteFijo = new ScsActuacionAsistCosteFijoAdm(usrBean);
		admActuacionAsistCosteFijo.borrarCosteActuacionAsistencia(actuaAsistenciaBean);
		
		actuacionAsistenciaAdm.delete(actuacionAsistenciaForm.getActuacionAsistenciaVO());
		
		
	}
	public Object executeService(Object... parameters) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public AsistenciaForm getDatosAsistencia(AsistenciaForm asistenciaForm, UsrBean usrBean)
			throws ClsExceptions {
		ScsAsistenciasAdm asistenciasAdm = new ScsAsistenciasAdm(usrBean);
		asistenciaForm = asistenciasAdm.getDatosAsistencia(asistenciaForm.getAsistenciaVO());
		return asistenciaForm;
	}
	public List<ScsComisariaBean> getComisarias(AsistenciaForm asistenciaForm, UsrBean usrBean) throws ClsExceptions{
	  
	    	ScsComisariaAdm admComisarias = new ScsComisariaAdm(usrBean);
	    	ScsAsistenciasBean asistencia = asistenciaForm.getAsistenciaVO();
	    	List<ScsComisariaBean> alComisarias = admComisarias.getComisarias(asistencia.getIdInstitucion(),false, false, asistenciaForm.getComisaria());
	    	if(alComisarias==null)
	    		alComisarias = new ArrayList<ScsComisariaBean>();
	    	return alComisarias;
	}
	public List<ScsComisariaBean> getComisarias(AsistenciaForm asistenciaForm,String idComisaria, UsrBean usrBean) throws ClsExceptions{
		  
    	ScsComisariaAdm admComisarias = new ScsComisariaAdm(usrBean);
    	ScsAsistenciasBean asistencia = asistenciaForm.getAsistenciaVO();
    	List<ScsComisariaBean> alComisarias = admComisarias.getComisarias(asistencia.getIdInstitucion(),false, false, idComisaria);
    	if(alComisarias==null)
    		alComisarias = new ArrayList<ScsComisariaBean>();
    	return alComisarias;
}
	public List<ScsJuzgadoBean> getJuzgados(AsistenciaForm asistenciaForm,
			UsrBean usrBean) throws ClsExceptions {
		
		ScsJuzgadoAdm juzgadoAdm = new ScsJuzgadoAdm(usrBean);
    	List<ScsJuzgadoBean> juzgadosList = juzgadoAdm.getJuzgados(asistenciaForm.getIdInstitucion(),usrBean,false, false, asistenciaForm.getJuzgado());
    	if(juzgadosList==null)
    		juzgadosList = new ArrayList<ScsJuzgadoBean>();
    	return juzgadosList;
	}
	public List<ScsJuzgadoBean> getJuzgados(AsistenciaForm asistenciaForm,String idJuzgado,
			UsrBean usrBean) throws ClsExceptions {
		
		ScsJuzgadoAdm juzgadoAdm = new ScsJuzgadoAdm(usrBean);
    	List<ScsJuzgadoBean> juzgadosList = juzgadoAdm.getJuzgados(asistenciaForm.getIdInstitucion(),usrBean,false, false, idJuzgado);
    	if(juzgadosList==null)
    		juzgadosList = new ArrayList<ScsJuzgadoBean>();
    	return juzgadosList;
	}
	public List<ValueKeyVO> getTipoCosteFijoActuaciones(
			ActuacionAsistenciaForm actuacionAsistenciaForm,String idTipoActuacion, UsrBean usrBean)
			throws ClsExceptions {
		ScsActuacionAsistCosteFijoAdm actuacionAsistCosteFijoAdm = new ScsActuacionAsistCosteFijoAdm(usrBean);
		ScsActuacionAsistenciaBean actuacion = actuacionAsistenciaForm.getActuacionAsistenciaVO();
    	List<ValueKeyVO> costesFijosList = actuacionAsistCosteFijoAdm.getTipoCosteFijoActuaciones(actuacion.getIdInstitucion(),actuacion.getIdTipoAsistencia(),new Integer(idTipoActuacion),false);
    	if(costesFijosList==null)
    		costesFijosList = new ArrayList<ValueKeyVO>();
    	return costesFijosList;

	}
	public List<ScsTipoActuacionBean> getTiposActuacion(
			AsistenciaForm asistenciaForm, UsrBean usrBean)
			throws ClsExceptions {
		ScsTipoActuacionAdm tipoActuacionAdm = new ScsTipoActuacionAdm(usrBean);
		ScsAsistenciasBean asistencia = asistenciaForm.getAsistenciaVO();
    	List<ScsTipoActuacionBean> tipoActuacionList = tipoActuacionAdm.getTipoActuaciones(asistencia.getIdInstitucion(),asistencia.getIdTipoAsistenciaColegio(),true);
    	if(tipoActuacionList==null)
    		tipoActuacionList = new ArrayList<ScsTipoActuacionBean>();
    	return tipoActuacionList;
	}
	public List<ScsPrisionBean> getPrisiones(AsistenciaForm asistenciaForm,
			UsrBean usrBean) throws ClsExceptions {
		ScsPrisionAdm prisionAdm = new ScsPrisionAdm(usrBean);
		ScsAsistenciasBean asistencia = asistenciaForm.getAsistenciaVO();
    	List<ScsPrisionBean> prisionBeans = prisionAdm.getPrisiones(asistencia.getIdInstitucion(),false);
    	if(prisionBeans==null)
    		prisionBeans = new ArrayList<ScsPrisionBean>();
    	return prisionBeans;

	}
	public void modificarActuacionAsistencia(ActuacionAsistenciaForm actuacionAsistenciaForm, UsrBean usrBean)
		throws ClsExceptions, SIGAException {
		ScsActuacionAsistenciaBean actuaAsistenciaBean = actuacionAsistenciaForm.getActuacionAsistenciaVO();
		ScsActuacionAsistenciaAdm actuacionAsistenciaAdm = new ScsActuacionAsistenciaAdm(usrBean);
		ScsActuacionAsistCosteFijoAdm admActuacionAsistCosteFijo = new ScsActuacionAsistCosteFijoAdm(usrBean);
		admActuacionAsistCosteFijo.borrarCosteActuacionAsistencia(actuaAsistenciaBean);
		
		ScsCabeceraGuardiasAdm cabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm(usrBean);
		cabeceraGuardiasAdm.actualizarValidacionCabecera(actuacionAsistenciaForm.getIdInstitucion(),actuacionAsistenciaForm.getAnio(),actuacionAsistenciaForm.getNumero(),UtilidadesString.stringToBoolean(actuacionAsistenciaForm.getValidada()));
		
		actuacionAsistenciaAdm.updateDirect(actuaAsistenciaBean);
		
		if(actuacionAsistenciaForm.getIdComisaria()!=null && !actuacionAsistenciaForm.getIdComisaria().equals("")){
			if(actuacionAsistenciaForm.getComisariaAsistencia()==null||actuacionAsistenciaForm.getComisariaAsistencia().equals("")){
				ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm(usrBean);
				ScsAsistenciasBean asistencia = new ScsAsistenciasBean();
				asistencia.setIdInstitucion(actuaAsistenciaBean.getIdInstitucion());
				asistencia.setAnio(actuaAsistenciaBean.getAnio());
				asistencia.setNumero(actuaAsistenciaBean.getNumero().intValue());
				asistencia.setComisaria(actuaAsistenciaBean.getIdComisaria().longValue());
				asistencia.setComisariaIdInstitucion(actuaAsistenciaBean.getIdInstitucion());
				
				if((actuacionAsistenciaForm.getNumeroDiligenciaAsistencia()==null||actuacionAsistenciaForm.getNumeroDiligenciaAsistencia().equals(""))&&actuacionAsistenciaForm.getNumeroAsunto()!=null && !actuacionAsistenciaForm.getNumeroAsunto().equals("")){
					asistencia.setNumeroDiligencia(actuaAsistenciaBean.getNumeroAsunto());
				}else{
					asistencia.setNumeroDiligencia(actuacionAsistenciaForm.getNumeroDiligenciaAsistencia());
					
				}
				admAsistencias.updateAsistenciaDesdeActuacion(asistencia);
			}
			
		}
		if(actuacionAsistenciaForm.getIdJuzgado()!=null && !actuacionAsistenciaForm.getIdJuzgado().equals("")){
			if(actuacionAsistenciaForm.getJuzgadoAsistencia()==null||actuacionAsistenciaForm.getJuzgadoAsistencia().equals("")){
				ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm(usrBean);
				ScsAsistenciasBean asistencia = new ScsAsistenciasBean();
				asistencia.setIdInstitucion(actuaAsistenciaBean.getIdInstitucion());
				asistencia.setAnio(actuaAsistenciaBean.getAnio());
				asistencia.setNumero(actuaAsistenciaBean.getNumero().intValue());
				asistencia.setJuzgado(actuaAsistenciaBean.getIdJuzgado().longValue());
				asistencia.setJuzgadoIdInstitucion(actuaAsistenciaBean.getIdInstitucion());
				if((actuacionAsistenciaForm.getNumeroProcedimientoAsistencia()==null||actuacionAsistenciaForm.getNumeroProcedimientoAsistencia().equals(""))&&actuacionAsistenciaForm.getNumeroAsunto()!=null && !actuacionAsistenciaForm.getNumeroAsunto().equals("")){
					asistencia.setNumeroProcedimiento(actuaAsistenciaBean.getNumeroAsunto());
				}else{
					asistencia.setNumeroProcedimiento(actuacionAsistenciaForm.getNumeroProcedimientoAsistencia());
					
				}
				admAsistencias.updateAsistenciaDesdeActuacion(asistencia);
			}
			
		}
		
		
		if(actuacionAsistenciaForm.getIdCosteFijoActuacion()!=null &&!actuacionAsistenciaForm.getIdCosteFijoActuacion().equals(""))
			admActuacionAsistCosteFijo.insertarCosteActuacionAsistencia(actuaAsistenciaBean,new Integer(actuacionAsistenciaForm.getIdCosteFijoActuacion()));
		
		
		
	
	}
	public Integer getNuevaActuacionAsistencia(AsistenciaForm asistencia,UsrBean usrBean) throws ClsExceptions{
		ScsActuacionAsistenciaAdm scsActuacionAsistenciaAdm = new ScsActuacionAsistenciaAdm(usrBean);
		return scsActuacionAsistenciaAdm.getNuevaActuacionAsistencia(asistencia);
		
	}
	public void insertarActuacionAsistencia(
			ActuacionAsistenciaForm actuacionAsistenciaForm, UsrBean usrBean)
			throws ClsExceptions, SIGAException {
		ScsActuacionAsistenciaBean actuaAsistenciaBean = actuacionAsistenciaForm.getActuacionAsistenciaVO();
		ScsActuacionAsistenciaAdm actuacionAsistenciaAdm = new ScsActuacionAsistenciaAdm(usrBean);
		
		
		ScsActuacionAsistCosteFijoAdm admActuacionAsistCosteFijo = new ScsActuacionAsistCosteFijoAdm(usrBean);
		
		ScsCabeceraGuardiasAdm cabeceraGuardiasAdm = new ScsCabeceraGuardiasAdm(usrBean);
		cabeceraGuardiasAdm.actualizarValidacionCabecera(actuacionAsistenciaForm.getIdInstitucion(),actuacionAsistenciaForm.getAnio(),actuacionAsistenciaForm.getNumero(),UtilidadesString.stringToBoolean(actuacionAsistenciaForm.getValidada()));
		
		actuacionAsistenciaAdm.insert(actuaAsistenciaBean);
		
			
		if(actuacionAsistenciaForm.getIdComisaria()!=null && !actuacionAsistenciaForm.getIdComisaria().equals("")){
			if(actuacionAsistenciaForm.getComisariaAsistencia()==null||actuacionAsistenciaForm.getComisariaAsistencia().equals("")){
				ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm(usrBean);
				ScsAsistenciasBean asistencia = new ScsAsistenciasBean();
				asistencia.setIdInstitucion(actuaAsistenciaBean.getIdInstitucion());
				asistencia.setAnio(actuaAsistenciaBean.getAnio());
				asistencia.setNumero(actuaAsistenciaBean.getNumero().intValue());
				asistencia.setComisaria(actuaAsistenciaBean.getIdComisaria().longValue());
				asistencia.setComisariaIdInstitucion(actuaAsistenciaBean.getIdInstitucion());
				
				if((actuacionAsistenciaForm.getNumeroDiligenciaAsistencia()==null||actuacionAsistenciaForm.getNumeroDiligenciaAsistencia().equals(""))&&actuacionAsistenciaForm.getNumeroAsunto()!=null && !actuacionAsistenciaForm.getNumeroAsunto().equals("")){
					asistencia.setNumeroDiligencia(actuaAsistenciaBean.getNumeroAsunto());
				}else{
					asistencia.setNumeroDiligencia(actuacionAsistenciaForm.getNumeroDiligenciaAsistencia());
					
				}
				admAsistencias.updateAsistenciaDesdeActuacion(asistencia);
			}
			
		}
		if(actuacionAsistenciaForm.getIdJuzgado()!=null && !actuacionAsistenciaForm.getIdJuzgado().equals("")){
			if(actuacionAsistenciaForm.getJuzgadoAsistencia()==null||actuacionAsistenciaForm.getJuzgadoAsistencia().equals("")){
				ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm(usrBean);
				ScsAsistenciasBean asistencia = new ScsAsistenciasBean();
				asistencia.setIdInstitucion(actuaAsistenciaBean.getIdInstitucion());
				asistencia.setAnio(actuaAsistenciaBean.getAnio());
				asistencia.setNumero(actuaAsistenciaBean.getNumero().intValue());
				asistencia.setJuzgado(actuaAsistenciaBean.getIdJuzgado().longValue());
				asistencia.setJuzgadoIdInstitucion(actuaAsistenciaBean.getIdInstitucion());
				if((actuacionAsistenciaForm.getNumeroProcedimientoAsistencia()==null||actuacionAsistenciaForm.getNumeroProcedimientoAsistencia().equals(""))&&actuacionAsistenciaForm.getNumeroAsunto()!=null && !actuacionAsistenciaForm.getNumeroAsunto().equals("")){
					asistencia.setNumeroProcedimiento(actuaAsistenciaBean.getNumeroAsunto());
				}else{
					asistencia.setNumeroProcedimiento(actuacionAsistenciaForm.getNumeroProcedimientoAsistencia());
					
				}
				admAsistencias.updateAsistenciaDesdeActuacion(asistencia);
			}
			
		}
		
		if(actuacionAsistenciaForm.getNig()!=null && !actuacionAsistenciaForm.getNig().equals("")){
			ScsAsistenciasAdm admAsistencias = new ScsAsistenciasAdm(usrBean);
			ScsAsistenciasBean asistencia = new ScsAsistenciasBean();
			asistencia.setIdInstitucion(actuaAsistenciaBean.getIdInstitucion());
			asistencia.setAnio(actuaAsistenciaBean.getAnio());
			asistencia.setNumero(actuaAsistenciaBean.getNumero().intValue());
			asistencia.setNIG(actuaAsistenciaBean.getNIG());

			admAsistencias.updateAsistenciaDesdeActuacion(asistencia);
		}
			

		if(actuacionAsistenciaForm.getIdCosteFijoActuacion()!=null &&!actuacionAsistenciaForm.getIdCosteFijoActuacion().equals(""))
			admActuacionAsistCosteFijo.insertarCosteActuacionAsistencia(actuaAsistenciaBean,new Integer(actuacionAsistenciaForm.getIdCosteFijoActuacion()));	
		
		
		
		
		
	}
	

}
