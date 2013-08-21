package com.siga.envios.service.impl;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.mapper.EnvComunicacionmorososMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EnvEnviosMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EnvEstatEnvioMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.GenParametrosMapper;
import org.redabogacia.sigaservices.app.autogen.model.EnvComunicacionmorosos;
import org.redabogacia.sigaservices.app.autogen.model.EnvComunicacionmorososExample;
import org.redabogacia.sigaservices.app.autogen.model.EnvEnvios;
import org.redabogacia.sigaservices.app.autogen.model.EnvEnviosKey;
import org.redabogacia.sigaservices.app.autogen.model.EnvEstatEnvioExample;
import org.redabogacia.sigaservices.app.autogen.model.GenParametros;
import org.redabogacia.sigaservices.app.autogen.model.GenParametrosKey;
import org.redabogacia.sigaservices.app.autogen.model.ScsComunicaciones;
import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.log4j.SatecLogger;
import org.redabogacia.sigaservices.app.mapper.EnvEnviosExtendsMapper;
import org.redabogacia.sigaservices.app.services.scs.ComunicacionesService;

import com.siga.envios.service.IntercambiosService;
import com.siga.envios.service.IntercambiosServiceDispatcher;
import com.siga.envios.service.SalidaEnviosService;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.MyBatisBusinessServiceTemplate;



public  class SalidaEnviosServiceImpl extends MyBatisBusinessServiceTemplate implements SalidaEnviosService {
	
	private static final SatecLogger log = (SatecLogger)SatecLogger.getLogger(SalidaEnviosServiceImpl.class);
	
	public SalidaEnviosServiceImpl(BusinessManager businessManager) {
		super(businessManager);
		// TODO Auto-generated constructor stub
	}

	public Object executeService(Object... arg0) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	public List<EnvEntradaEnvios> getEntradaEnvios(EntradaEnviosForm entradaEnviosForm) throws BusinessException {
		List<EnvEntradaEnvios>  entradaEnvios = null;
		EnvEntradaEnviosMapper envEntradaEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEntradaEnviosMapper.class);
		EnvEntradaEnviosExample entradaEnviosExample = new EnvEntradaEnviosExample();

		try {
			//Se van añadiendo los criterios para relaizar la query de busqueda
			Criteria criteria = entradaEnviosExample.createCriteria();
			criteria.andIdinstitucionEqualTo(Short.valueOf(entradaEnviosForm.getIdInstitucion()));
			
			if(entradaEnviosForm.getFechaDesde() != null && !entradaEnviosForm.getFechaDesde().equals("")){
				criteria.andFechapeticionGreaterThanOrEqualTo(UtilidadesFecha.getDate(entradaEnviosForm.getFechaDesde(), ClsConstants.DATE_FORMAT_SHORT_SPANISH));
			}
			
			if(entradaEnviosForm.getFechaHasta() != null && !entradaEnviosForm.getFechaHasta().equals("")){
				criteria.andFechapeticionLessThanOrEqualTo(UtilidadesFecha.getDate(entradaEnviosForm.getFechaHasta(), ClsConstants.DATE_FORMAT_SHORT_SPANISH));
			}
			
			
			
			if(entradaEnviosForm.getAsunto()!= null && !entradaEnviosForm.getAsunto().equals("")){
				criteria.andAsuntoLike("%"+entradaEnviosForm.getAsunto()+"%");
			}
			
			if(entradaEnviosForm.getIdEstado() != null && !entradaEnviosForm.getIdEstado().equals("")){
				criteria.andIdestadoEqualTo(Short.valueOf(entradaEnviosForm.getIdEstado()));
			}
			
			if(entradaEnviosForm.getIdTipoIntercambioTelematico() != null && !entradaEnviosForm.getIdTipoIntercambioTelematico().equals("")){
				criteria.andIdtipointercambiotelematicoEqualTo(entradaEnviosForm.getIdTipoIntercambioTelematico());
			}
			
			if(entradaEnviosForm.getComisionAJG()!=null && entradaEnviosForm.getComisionAJG().equals(AppConstants.DB_TRUE)){

				criteria.andComisionajgEqualTo(new Short(AppConstants.DB_TRUE));
			}else{
				entradaEnviosExample.or().andComisionajgEqualTo(new Short(AppConstants.DB_FALSE)).andComisionajgIsNull();
//				sql += " AND (COMISIONAJG IS NULL OR COMISIONAJG = 0 ) ";	
			}
			
			entradaEnviosExample.orderByFechapeticionDESC();
			entradaEnvios = envEntradaEnviosMapper.selectByExample(entradaEnviosExample);
			
		} catch (Exception e) {
			log.error("Se ha producido un error al realizar la busqueda en la bandeja de entrada de envíos", e);
			throw new BusinessException("Se ha producido un error al realizar la busqueda en la bandeja de entrada de envíos",e);
			
		} 	
		
		return entradaEnvios;
	}*/
	
	private void borrarRelacionMorosos(Long idEnvio,Short idInstitucion) throws BusinessException{
		EnvComunicacionmorososMapper envComunicacionmorososMapper = getMyBatisSqlSessionManager().getMapper(EnvComunicacionmorososMapper.class);
		EnvComunicacionmorososExample comunicacionmorososExample = new EnvComunicacionmorososExample();
		org.redabogacia.sigaservices.app.autogen.model.EnvComunicacionmorososExample.Criteria criteriaMorosos = comunicacionmorososExample.createCriteria();
		criteriaMorosos.andIdinstitucionEqualTo(idInstitucion);
		criteriaMorosos.andIdenviodocEqualTo(idEnvio);
		List<EnvComunicacionmorosos> comunicacionMorososList = envComunicacionmorososMapper.selectByExample(comunicacionmorososExample);


		if (comunicacionMorososList!=null && comunicacionMorososList.size()>=1){ 
			for (int i = 0; i < comunicacionMorososList.size(); i++) {
				EnvComunicacionmorosos comunicacionmorosos = (EnvComunicacionmorosos)comunicacionMorososList.get(i);


				comunicacionmorososExample = new EnvComunicacionmorososExample();
				criteriaMorosos = comunicacionmorososExample.createCriteria();
				criteriaMorosos.andIdinstitucionEqualTo(comunicacionmorosos.getIdinstitucion());
				criteriaMorosos.andIdfacturaEqualTo(comunicacionmorosos.getIdfactura());
				criteriaMorosos.andIdpersonaEqualTo(comunicacionmorosos.getIdpersona());
				comunicacionmorososExample.orderByIdenvio();
				List<EnvComunicacionmorosos> comunicacionMorososActualizar = envComunicacionmorososMapper.selectByExample(comunicacionmorososExample);
				TreeMap<Long,EnvComunicacionmorosos> tmComunicacionMorosos = new TreeMap<Long, EnvComunicacionmorosos>();
				if (comunicacionMorososActualizar!=null && comunicacionMorososActualizar.size()>=1){ 
					for (int j = 0; j < comunicacionMorososActualizar.size(); j++) {
						EnvComunicacionmorosos comunicacionmorososActualizar = (EnvComunicacionmorosos)comunicacionMorososActualizar.get(j);
						StringBuffer key = new StringBuffer();
						key.append(comunicacionmorososActualizar.getIdinstitucion());
						key.append(comunicacionmorososActualizar.getIdpersona());
						key.append(comunicacionmorososActualizar.getIdfactura());
						key.append(comunicacionmorososActualizar.getIdenvio());
						tmComunicacionMorosos.put(new Long(key.toString()), comunicacionmorososActualizar);
					}
				}

				StringBuffer key2 = new StringBuffer();
				key2.append(comunicacionmorosos.getIdinstitucion());
				key2.append(comunicacionmorosos.getIdpersona());
				key2.append(comunicacionmorosos.getIdfactura());
				key2.append(comunicacionmorosos.getIdenvio());
				//si solo hay uno se va es el que se va a eliminar luego no hay que actualizar nada.
				//Asi mismo si se elimina el ultimo envio tampoco actualiaremos ids
				if(tmComunicacionMorosos.size()>1 && !(tmComunicacionMorosos.lastKey().toString()).equals(key2.toString())){
					//Borramos el envio de moroso asociado al envio
					tmComunicacionMorosos.remove(Long.valueOf(key2.toString()));

				}else{
					tmComunicacionMorosos = null;

				}

				borraReferenciaMorosos(idInstitucion,idEnvio,comunicacionmorosos,tmComunicacionMorosos);

			}	


		}

	} 
	private void borraReferenciaMorosos(Short idInstitucion, Long idEnvio,
			EnvComunicacionmorosos comunicacionMorososBorrar,TreeMap<Long,EnvComunicacionmorosos> tmEnviosMorososAActualizar){
		EnvComunicacionmorososMapper envComunicacionmorososMapper = getMyBatisSqlSessionManager().getMapper(EnvComunicacionmorososMapper.class);
		envComunicacionmorososMapper.deleteByPrimaryKey(comunicacionMorososBorrar);

		if(tmEnviosMorososAActualizar!=null && tmEnviosMorososAActualizar.size()>0){
			Iterator<Long> itComunicacionMorosos = tmEnviosMorososAActualizar.keySet().iterator();
			int idEnvioActualizado = 1;
			long idEnvioOld = 0;
			while (itComunicacionMorosos.hasNext()) {
				Long key = (Long) itComunicacionMorosos.next();
				EnvComunicacionmorosos comunicacionMorosos = (EnvComunicacionmorosos) tmEnviosMorososAActualizar.get(key);
				idEnvioOld = comunicacionMorosos.getIdenvio();

				if(idEnvioActualizado!=idEnvioOld){
					comunicacionMorosos.setIdenvio(idEnvioOld);
					envComunicacionmorososMapper.deleteByPrimaryKey(comunicacionMorosos);
					comunicacionMorosos.setIdenvio(new Long(idEnvioActualizado));
					envComunicacionmorososMapper.insert(comunicacionMorosos);
				}
				idEnvioActualizado++;
			}	
		}


	}
	public EnvEnvios getEnvio(Long idEnvio,Short idInstitucion) throws BusinessException{
    	EnvEnviosMapper envEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEnviosMapper.class);
    	EnvEnviosKey envEnviosKey = new EnvEnviosKey();
    	envEnviosKey.setIdenvio(idEnvio);
    	envEnviosKey.setIdinstitucion(idInstitucion);
    	EnvEnvios envEnvios = envEnviosMapper.selectByPrimaryKey(envEnviosKey);
    	return envEnvios;
		
	}
 
   
    public File getLogComunicacion(Long idEnvio,Short idInstitucion)throws BusinessException{
		
		EnvEnviosMapper envEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEnviosMapper.class);
    	EnvEnviosKey envEnviosKey = new EnvEnviosKey();
    	envEnviosKey.setIdenvio(idEnvio);
    	envEnviosKey.setIdinstitucion(idInstitucion);
    	EnvEnvios envEnvios = envEnviosMapper.selectByPrimaryKey(envEnviosKey);
    	File fichero = null;
			
		if(envEnvios.getIdtipoenvios().equals(AppConstants.TiposEnvioEnum.TELEMATICO.getCodigo()) && envEnvios.getIdestado().equals(AppConstants.EstadosSalidaEnviosEnum.ESTADO_PROCESADOCONERRORES.getCodigo() )){
			IntercambiosService intercambiosService = (IntercambiosService) IntercambiosServiceDispatcher.getService(getBusinessManager(),envEnvios.getIdtipointercambiotelematico());
			fichero = intercambiosService.getFicheroLog(idEnvio.toString(), idInstitucion.toString());
		}else{
			String pathLogComunicacion =  getPathEnvio(envEnvios) + File.separator + "informeEnvio" + ".log.xls";
			fichero = new File(pathLogComunicacion);
			
		}
		return fichero;
	}
	
	public void borrarEnvio(EnvEnvios envio) throws BusinessException{
		try {
			
			EnvEnviosMapper envEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEnviosMapper.class);
			EnvEstatEnvioMapper estatEnvioMapper = getMyBatisSqlSessionManager().getMapper(EnvEstatEnvioMapper.class);
			
			ComunicacionesService comunicacionesService = (ComunicacionesService)getBusinessManager().getService(ComunicacionesService.class);
			ScsComunicaciones scsComunicaciones = new ScsComunicaciones();
			scsComunicaciones.setIdenviosalida(envio.getIdenvio());
			
			EnvEstatEnvioExample estatEnvioExample = new EnvEstatEnvioExample();
			org.redabogacia.sigaservices.app.autogen.model.EnvEstatEnvioExample.Criteria criteria =  estatEnvioExample.createCriteria();
			criteria.andIdenvioEqualTo(envio.getIdenvio());
			criteria.andIdinstitucionEqualTo(envio.getIdinstitucion());
			
			String rutaDocus = getPathEnvio(envio);
			File fDir = new File(rutaDocus);
			
			//iniciamos la transaccion
			getBusinessManager().startTransaction();
			
			//borrar relacion comunicacion morosos
			borrarRelacionMorosos(envio.getIdenvio(),envio.getIdinstitucion());
			
			//borramos relacion sjcs
			
			comunicacionesService.borrarComunicacion(scsComunicaciones);
			//borramos las estadisticas
			
			estatEnvioMapper.deleteByExample(estatEnvioExample);
			//borramos envio
			envEnviosMapper.deleteByPrimaryKey(envio);
			
			//Borramos los docuemntos del envio
			SIGAServicesHelper.borrarDirectorio(fDir);
			
			getBusinessManager().commitTransaction();		
		} catch (Exception e) {
			log.error("Se ha producido un error al borrar la comunicacion. Existen registros asociados", e);
			throw new BusinessException("messages.error.ora.02292", e);
		}finally{
			getBusinessManager().endTransaction();		
		}
		
		
	}
	
	private String getPathEnvio(EnvEnvios envEnvios)
	{
	
	    
		Calendar cal = Calendar.getInstance();
		Date d = envEnvios.getFecha();
	    cal.setTime(d);
	    
	    String anio = String.valueOf(cal.get(Calendar.YEAR));
	    String mes = String.valueOf(cal.get(Calendar.MONTH)+1);
	    
	    GenParametrosMapper genParametrosMapper = getMyBatisSqlSessionManager().getMapper(GenParametrosMapper.class);
	    GenParametrosKey genParametrosKey = new GenParametrosKey();
	    genParametrosKey.setIdinstitucion(AppConstants.IDINSTITUCION_0);
	    genParametrosKey.setModulo(AppConstants.MODULO.ENV.name());
	    genParametrosKey.setParametro("PATH_DOCUMENTOSADJUNTOS");
	    GenParametros genParametros = genParametrosMapper.selectByPrimaryKey(genParametrosKey);
	    String pathDoc = genParametros.getValor();
	    String sDirectorio = pathDoc + File.separator +
	    		envEnvios.getIdinstitucion() + File.separator +
							 anio + File.separator +
							 mes + File.separator + envEnvios.getIdenvio();
	
	    return sDirectorio;
	}
	
	public List<EnvEnvios> getComunicacionesEJG(Integer idInstitucion,Short anio,Short idTipoEjg, Integer numero ,short comisionAJG ) throws BusinessException {
		List<EnvEnvios>  entradaEnvios = null;
		EnvEnviosExtendsMapper envEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEnviosExtendsMapper.class);

		try {
			Map<String, Object> parametrosMap = new HashMap<String, Object>();
			parametrosMap.put("idInstitucion", idInstitucion);
			parametrosMap.put("anio", anio);
			parametrosMap.put("idTipoEjg", idTipoEjg);
			parametrosMap.put("numero", numero);
			parametrosMap.put("comisionAJG", comisionAJG);
			entradaEnvios = envEnviosMapper.getComunicacionesEJG(parametrosMap);
			
		} catch (Exception e) {
			log.error("Se ha producido un error al realizar la busqueda en la bandeja de salida de envíos", e);
			throw new BusinessException("Se ha producido un error al realizar la busqueda en la bandeja de entrada de envíos",e);
			
		} 	
		
		return entradaEnvios;
	}
	public List<EnvEnvios> getComunicacionesDesigna(Integer idInstitucion,Short anio,Short idTurno, Integer numero ,short comisionAJG ) throws BusinessException {
		List<EnvEnvios>  salidaEnvios = null;
		EnvEnviosExtendsMapper envEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEnviosExtendsMapper.class);

		try {
			Map<String, Object> parametrosMap = new HashMap<String, Object>();
			parametrosMap.put("idInstitucion", idInstitucion);
			parametrosMap.put("anio", anio);
			parametrosMap.put("idTurno", idTurno);
			parametrosMap.put("numero", numero);
			parametrosMap.put("comisionAJG", comisionAJG);
			salidaEnvios = envEnviosMapper.getComunicacionesDesigna(parametrosMap);
			
		} catch (Exception e) {
			log.error("Se ha producido un error al realizar la busqueda en la bandeja de salida de envíos", e);
			throw new BusinessException("Se ha producido un error al realizar la busqueda en la bandeja de entrada de envíos",e);
			
		} 	
		
		return salidaEnvios;
	}
	
	


}
