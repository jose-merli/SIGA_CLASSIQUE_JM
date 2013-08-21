package com.siga.envios.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.EstadosEntradaEnviosEnum;
import org.redabogacia.sigaservices.app.AppConstants.OPERACION;
import org.redabogacia.sigaservices.app.autogen.mapper.EcomResolucionimpugnacionMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EcomRespsolsusprocedimientoMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EcomSoldesignaprovisionalMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EnvEntradaEnviosMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.ScsEjgMapper;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.autogen.model.EcomResolucionimpugnacion;
import org.redabogacia.sigaservices.app.autogen.model.EcomResolucionimpugnacionExample;
import org.redabogacia.sigaservices.app.autogen.model.EcomRespsolsusprocedimiento;
import org.redabogacia.sigaservices.app.autogen.model.EcomRespsolsusprocedimientoKey;
import org.redabogacia.sigaservices.app.autogen.model.EcomSoldesignaprovisional;
import org.redabogacia.sigaservices.app.autogen.model.EcomSoldesignaprovisionalExample;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnvios;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnviosExample;
import org.redabogacia.sigaservices.app.autogen.model.EnvEnvios;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnviosExample.Criteria;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnviosKey;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnviosWithBLOBs;
import org.redabogacia.sigaservices.app.autogen.model.ScsComunicaciones;
import org.redabogacia.sigaservices.app.autogen.model.ScsDesigna;
import org.redabogacia.sigaservices.app.autogen.model.ScsEjg;
import org.redabogacia.sigaservices.app.autogen.model.ScsEjgKey;
import org.redabogacia.sigaservices.app.log4j.SatecLogger;
import org.redabogacia.sigaservices.app.mapper.EnvEntradaEnviosExtendsMapper;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;
import org.redabogacia.sigaservices.app.services.scs.ComunicacionesService;
import org.redabogacia.sigaservices.app.services.scs.ScsDesignaService;
import org.redabogacia.sigaservices.app.services.scs.ScsEjgService;
import org.redabogacia.sigaservices.app.vo.Designacion;
import org.redabogacia.sigaservices.app.vo.Ejg;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.envios.form.EntradaEnviosForm;
import com.siga.envios.service.EntradaEnviosService;
import com.siga.envios.service.ca_sigp.mapper.SolDesignacionProvisionalMapper;
import com.siga.envios.service.ca_sigp.vos.TipoDesignaLetradoProcurador;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.MyBatisBusinessServiceTemplate;



public  class EntradaEnviosServiceImpl extends MyBatisBusinessServiceTemplate implements EntradaEnviosService {
	
	private static final SatecLogger log = (SatecLogger)SatecLogger.getLogger(EntradaEnviosServiceImpl.class);
	
	public EntradaEnviosServiceImpl(BusinessManager businessManager) {
		super(businessManager);
		// TODO Auto-generated constructor stub
	}

	public Object executeService(Object... arg0) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public List<EnvEntradaEnvios> getEntradaEnvios(EntradaEnviosForm entradaEnviosForm) throws BusinessException {
		List<EnvEntradaEnvios>  entradaEnvios = null;
		EnvEntradaEnviosMapper envEntradaEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEntradaEnviosMapper.class);
		EnvEntradaEnviosExample entradaEnviosExample = new EnvEntradaEnviosExample();

		try {
			//Se van a�adiendo los criterios para relaizar la query de busqueda
			Criteria criteria = entradaEnviosExample.createCriteria();
			criteria.andIdinstitucionEqualTo(Short.valueOf(entradaEnviosForm.getIdInstitucion()));
			
			if(entradaEnviosForm.getFechaDesde() != null && !entradaEnviosForm.getFechaDesde().equals("")){
				criteria.andFechapeticionGreaterThanOrEqualTo(UtilidadesFecha.getDate(entradaEnviosForm.getFechaDesde(), ClsConstants.DATE_FORMAT_SHORT_SPANISH));
			}
			
			if(entradaEnviosForm.getFechaHasta() != null && !entradaEnviosForm.getFechaHasta().equals("")){
				criteria.andFechapeticionLessThanOrEqualTo(UtilidadesFecha.getDate(entradaEnviosForm.getFechaHasta(), ClsConstants.DATE_FORMAT_SHORT_SPANISH));
			}
			
			/*if(entradaEnviosForm.getRemitente() != null && !entradaEnviosForm.getRemitente().equals("")){
				criteria.and
			}*/
			
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
			log.error("Se ha producido un error al realizar la busqueda en la bandeja de entrada de env�os", e);
			throw new BusinessException("Se ha producido un error al realizar la busqueda en la bandeja de entrada de env�os",e);
			
		} 	
		
		return entradaEnvios;
	}
	public List<EnvEntradaEnvios> getComunicacionesEJG(Integer idInstitucion,Short anio,Short idTipoEjg, Integer numero,short comisionAJG ) throws BusinessException {
		List<EnvEntradaEnvios>  entradaEnvios = null;
		EnvEntradaEnviosExtendsMapper envEntradaEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEntradaEnviosExtendsMapper.class);

		try {
			Map<String, Object> parametrosMap = new HashMap<String, Object>();
			parametrosMap.put("idInstitucion", idInstitucion);
			parametrosMap.put("anio", anio);
			parametrosMap.put("idTipoEjg", idTipoEjg);
			parametrosMap.put("numero", numero);
			parametrosMap.put("comisionAJG", comisionAJG);
			entradaEnvios = envEntradaEnviosMapper.getComunicacionesEJG(parametrosMap);
			
		} catch (Exception e) {
			log.error("Se ha producido un error al realizar la busqueda en la bandeja de entrada de env�os", e);
			throw new BusinessException("Se ha producido un error al realizar la busqueda en la bandeja de entrada de env�os",e);
			
		} 	
		
		return entradaEnvios;
	}
	public List<EnvEntradaEnvios> getComunicacionesDesigna(Integer idInstitucion,Short anio,Short idTurno, Integer numero,short comisionAJG ) throws BusinessException {
		List<EnvEntradaEnvios>  entradaEnvios = null;
		EnvEntradaEnviosExtendsMapper envEntradaEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEntradaEnviosExtendsMapper.class);

		try {
			Map<String, Object> parametrosMap = new HashMap<String, Object>();
			parametrosMap.put("idInstitucion", idInstitucion);
			parametrosMap.put("anio", anio);
			parametrosMap.put("idTurno", idTurno);
			parametrosMap.put("numero", numero);
			parametrosMap.put("comisionAJG", comisionAJG);
			entradaEnvios = envEntradaEnviosMapper.getComunicacionesDesigna(parametrosMap);
			
		} catch (Exception e) {
			log.error("Se ha producido un error al realizar la busqueda en la bandeja de entrada de env�os", e);
			throw new BusinessException("Se ha producido un error al realizar la busqueda en la bandeja de entrada de env�os",e);
			
		} 	
		
		return entradaEnvios;
	}
	
	

	public EnvEntradaEnviosWithBLOBs getEntradaEnviosWithBlobs(EntradaEnviosForm entradaEnviosForm) throws BusinessException {
		EnvEntradaEnviosMapper envEntradaEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEntradaEnviosMapper.class);
		EnvEntradaEnviosWithBLOBs  entradaEnviosWithBLOBs = null;
		try {
			//Se van a�adiendo los criterios para relaizar la query de busqueda
			EnvEntradaEnviosKey envEntradaEnviosKey = new EnvEntradaEnviosKey();
			envEntradaEnviosKey.setIdinstitucion(Short.valueOf(entradaEnviosForm.getIdInstitucion()));
			envEntradaEnviosKey.setIdenvio(Long.valueOf(entradaEnviosForm.getIdEnvio()));
			entradaEnviosWithBLOBs = envEntradaEnviosMapper.selectByPrimaryKey(envEntradaEnviosKey);
		
		} catch (Exception e) {
			log.error("Se ha producido un error al realizar la busqueda del envio en la bandeja de entrada de env�os", e);
			throw new BusinessException("Se ha producido un error al realizar la busqueda en la bandeja de entrada de env�os",e);
			
		} 	
		
		return entradaEnviosWithBLOBs;
	}
	
	public EnvEntradaEnviosWithBLOBs getEntradaEnvios(EnvEntradaEnviosExample entradaEnviosExample) throws BusinessException {
		EnvEntradaEnviosMapper envEntradaEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEntradaEnviosMapper.class);
		EnvEntradaEnviosWithBLOBs  entradaEnviosWithBLOBs = null;
		try {
			List<EnvEntradaEnviosWithBLOBs> list = envEntradaEnviosMapper.selectByExampleWithBLOBs(entradaEnviosExample);
			if(list!=null && list.size()>0){
				entradaEnviosWithBLOBs = envEntradaEnviosMapper.selectByExampleWithBLOBs(entradaEnviosExample).get(0);
			}
		
		} catch (Exception e) {
			log.error("Se ha producido un error al realizar la busqueda del envio en la bandeja de entrada de env�os", e);
			throw new BusinessException("Se ha producido un error al realizar la busqueda en la bandeja de entrada de env�os",e);
			
		} 	
		
		return entradaEnviosWithBLOBs;
	}	

	public void procesar(EntradaEnviosForm entradaEnviosForm,UsrBean usrBean,boolean nuevo) throws BusinessException {
		EnvEntradaEnviosWithBLOBs envEntradaEnviosWithBLOBs = getEntradaEnviosWithBlobs(entradaEnviosForm);
		EcomCola ecomCola = new EcomCola();
		EcomColaService ecomColaService = (EcomColaService)BusinessManager.getInstance().getService(EcomColaService.class);
		Short idEstado = -1;
		/*
		 * if((getDesignas()!=null &&getDesignas().size()>0) ||(getEjgs()!=null &&getEjgs().size()>0)){
	        BusinessManager bm = BusinessManager.getInstance();
	        //bm.startTransaction();
	        ComunicacionesService comunicacionesService = (ComunicacionesService)bm.getService(ComunicacionesService.class);
	        EnvEnvios envEnvios = new EnvEnvios();
	        envEnvios.setIdenvio(Long.parseLong(enviosBean.getIdEnvio().toString()));
	        try {
       		 if(getDesignas()!=null &&getDesignas().size()>0)
    	        	comunicacionesService.insertarComunicacionDesigna(getDesignas(), envEnvios);
    	        else if(getEjgs()!=null &&getEjgs().size()>0)
    	        	comunicacionesService.insertarComunicacionEjg(getEjgs(), envEnvios);	
            	if(this.usrBean.getTransaction().getStatus()==6)
            		bm.commitTransaction();
			} catch (Exception e) {
				if(this.usrBean.getTransaction()==null){
            		bm.endTransaction();
				}
				throw new SIGAException("Error al insertar en scs_comunicaciones"+e.toString());
				
			}
	       
	        
        }
		 * */
		
		
		if(envEntradaEnviosWithBLOBs.getIdtipointercambiotelematico().equals(AppConstants.TipoIntercambioEnum.SGP_ICA_ENV_SOL_ABG_PRO.getCodigo())){
			List<ScsDesigna> designas = null;
			List<ScsEjg> ejgs = null;
			EcomSoldesignaprovisional ecomSoldesignaprovisional = new EcomSoldesignaprovisional();
			EcomSoldesignaprovisionalMapper soldesignaprovisionalMapper = getMyBatisSqlSessionManager().getMapper(EcomSoldesignaprovisionalMapper.class);
			ecomSoldesignaprovisional.setIdenvio(envEntradaEnviosWithBLOBs.getIdenvio());
			ecomSoldesignaprovisional.setIdinstitucion(envEntradaEnviosWithBLOBs.getIdinstitucion());
			ecomSoldesignaprovisional.setCaso(new Short(entradaEnviosForm.getCaso()));
			ecomSoldesignaprovisional.setFechamodificacion(Calendar.getInstance().getTime());
			ecomSoldesignaprovisional.setUsumodificacion(new Integer (usrBean.getUserName()));
			
			if(nuevo){		
				if(entradaEnviosForm.getCaso().equals("2")){
					
					ecomSoldesignaprovisional.setDesignanewanio(new Short(entradaEnviosForm.getAnioDesignaNew()));
					ecomSoldesignaprovisional.setDesignanewnumero(new Long(entradaEnviosForm.getNumeroDesignaNew()));
					ecomSoldesignaprovisional.setDesignanewidturno(new Integer(entradaEnviosForm.getIdTurnoDesignaNew()));
					ecomSoldesignaprovisional.setDesignanewdletrado(new Long(entradaEnviosForm.getAbogadoDesignaSel()));
					designas = new ArrayList<ScsDesigna>();
					ScsDesigna designa = new ScsDesigna();
					designa.setIdinstitucion(ecomSoldesignaprovisional.getIdinstitucion());
					designa.setAnio(ecomSoldesignaprovisional.getDesignanewanio());
					designa.setIdturno(ecomSoldesignaprovisional.getDesignanewidturno());
					designa.setNumero(ecomSoldesignaprovisional.getDesignanewnumero());
					designas.add(designa);
					
					SimpleDateFormat sdf2 = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
					try {
						ecomSoldesignaprovisional.setDesignanewfecha(sdf2.parse(entradaEnviosForm.getFechaApertura()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				ecomCola.setIdoperacion(OPERACION.EJIS_PROCESAR_SOL_ABG_PRO.getId());
				if (ecomColaService.insert(ecomCola) != 1) {
					throw new BusinessException("No se ha podido insertar en la cola de comunicaciones.");
				}				
				ecomSoldesignaprovisional.setIdecomcola(ecomCola.getIdecomcola());
				idEstado = EstadosEntradaEnviosEnum.ESTADO_PROCESADO.getCodigo();
			
			}else{
				//CASO 1  -> Se mira si hay designaciones relacionadas
				if(entradaEnviosForm.getCaso().equals("1")){
					ecomSoldesignaprovisional.setEjgselidtipo(new Short(entradaEnviosForm.getIdTipoEJGSel()));
					ecomSoldesignaprovisional.setEjgselanio(new Short(entradaEnviosForm.getAnioEJGSel()));
					ecomSoldesignaprovisional.setEjgselnumero(new Long(entradaEnviosForm.getNumeroEJGSel()));
					
					ejgs = new ArrayList<ScsEjg>();
					ScsEjg ejg = new ScsEjg();
					ejg.setIdinstitucion(ecomSoldesignaprovisional.getIdinstitucion());
					ejg.setAnio(ecomSoldesignaprovisional.getEjgselanio());
					ejg.setIdtipoejg(ecomSoldesignaprovisional.getEjgselidtipo());
					ejg.setNumero(ecomSoldesignaprovisional.getEjgselnumero());
					ejgs.add(ejg);
					
					
					List<TipoDesignaLetradoProcurador> designasRelacionadas = getDesignasEJGRelacionadas(entradaEnviosForm.getIdInstitucion(), entradaEnviosForm.getAnioEJGSel(), entradaEnviosForm.getNumeroEJGSel(), entradaEnviosForm.getIdTipoEJGSel());
					if(designasRelacionadas != null){
						if(designasRelacionadas.size() == 1){//Existe una sola designa relacionada, por lo que se selecciona esta autom�ticamente
							TipoDesignaLetradoProcurador designaSeleccionada = designasRelacionadas.get(0);
							ecomSoldesignaprovisional.setDesignaselanio(new Short(designaSeleccionada.getAnio()));
							ecomSoldesignaprovisional.setDesignaselnumero(new Long(designaSeleccionada.getNumero()));
							ecomSoldesignaprovisional.setDesignaselidturno(new Integer(designaSeleccionada.getIdTurno()));
						} else if(designasRelacionadas.size() > 1 && entradaEnviosForm.getAnioDesignaSel() != null && !entradaEnviosForm.getAnioDesignaSel().equals("") ){
							//Existe mas de una designa y ha seleccionado una en el combo
							ecomSoldesignaprovisional.setDesignaselanio(new Short(entradaEnviosForm.getAnioDesignaSel()));
							ecomSoldesignaprovisional.setDesignaselnumero(new Long(entradaEnviosForm.getNumeroDesignaSel()));
							ecomSoldesignaprovisional.setDesignaselidturno(new Integer(entradaEnviosForm.getIdTurnoDesignaSel()));
						}
						
						designas = new ArrayList<ScsDesigna>();
						ScsDesigna designa = new ScsDesigna();
						designa.setIdinstitucion(ecomSoldesignaprovisional.getIdinstitucion());
						designa.setAnio(ecomSoldesignaprovisional.getDesignaselanio());
						designa.setIdturno(ecomSoldesignaprovisional.getDesignaselidturno());
						designa.setNumero(ecomSoldesignaprovisional.getDesignaselnumero());
						designas.add(designa);
						
					}
				
				}else if(entradaEnviosForm.getCaso().equals("2")){
					ecomSoldesignaprovisional.setDesignaselanio(new Short(entradaEnviosForm.getAnioDesignaSel()));
					ecomSoldesignaprovisional.setDesignaselnumero(new Long(entradaEnviosForm.getNumeroDesignaSel()));
					ecomSoldesignaprovisional.setDesignaselidturno(new Integer(entradaEnviosForm.getIdTurnoDesignaSel()));
					designas = new ArrayList<ScsDesigna>();
					ScsDesigna designa = new ScsDesigna();
					designa.setIdinstitucion(ecomSoldesignaprovisional.getIdinstitucion());
					designa.setAnio(ecomSoldesignaprovisional.getDesignaselanio());
					designa.setIdturno(ecomSoldesignaprovisional.getDesignaselidturno());
					designa.setNumero(ecomSoldesignaprovisional.getDesignaselnumero());
					designas.add(designa);
					
					
				}
				
				idEstado = EstadosEntradaEnviosEnum.ESTADO_PENDIENTE_ENVIAR.getCodigo();
			}
			
			//Se comprueba si existe ya un registro, si es asi se hace un UPDATE si no existe se hace un INSERT
			EcomSoldesignaprovisionalExample soldesignaprovisionalExample = new EcomSoldesignaprovisionalExample();
			org.redabogacia.sigaservices.app.autogen.model.EcomSoldesignaprovisionalExample.Criteria crit = soldesignaprovisionalExample.createCriteria();
			crit.andIdenvioEqualTo(envEntradaEnviosWithBLOBs.getIdenvio());
			crit.andIdinstitucionEqualTo(envEntradaEnviosWithBLOBs.getIdinstitucion());		
			List<EcomSoldesignaprovisional> record =soldesignaprovisionalMapper.selectByExample(soldesignaprovisionalExample);
			if(record != null && record.size() > 0){
				soldesignaprovisionalMapper.updateByPrimaryKeySelective(ecomSoldesignaprovisional); 
			}else{
				soldesignaprovisionalMapper.insert(ecomSoldesignaprovisional);
			}
			ComunicacionesService comunicacionesService = (ComunicacionesService)getBusinessManager().getService(ComunicacionesService.class);
			if(designas!=null &&designas.size()>0)
		        comunicacionesService.insertarComunicacionDesigna(designas, envEntradaEnviosWithBLOBs);
		     if(ejgs!=null && ejgs.size()>0)
		        comunicacionesService.insertarComunicacionEjg(ejgs, envEntradaEnviosWithBLOBs);
		}else if(envEntradaEnviosWithBLOBs.getIdtipointercambiotelematico().equals(AppConstants.TipoIntercambioEnum.SGP_ICA_RESP_SOL_SUSP_PROC.getCodigo())){
			
			EcomRespsolsusprocedimiento respsolProc = new EcomRespsolsusprocedimiento();
			EcomRespsolsusprocedimientoMapper respsolProcsMapper = getMyBatisSqlSessionManager().getMapper(EcomRespsolsusprocedimientoMapper.class);
			respsolProc.setIdenvio(envEntradaEnviosWithBLOBs.getIdenvio());
			respsolProc.setIdinstitucion(envEntradaEnviosWithBLOBs.getIdinstitucion());
			respsolProc.setFechamodificacion(new Date());
			respsolProc.setUsumodificacion(new Integer (usrBean.getUserName()));
			if(nuevo){
				ecomCola.setIdoperacion(OPERACION.EJIS_PROCESAR_RESPUESTAS.getId());
				if (ecomColaService.insert(ecomCola) != 1) {
					throw new BusinessException("No se ha podido insertar en la cola de comunicaciones.");
				}
				respsolProc.setIdecomcola(ecomCola.getIdecomcola());
				idEstado = EstadosEntradaEnviosEnum.ESTADO_PROCESADO.getCodigo();
				
			}else{
				respsolProc.setEjgselidtipo(new Short(entradaEnviosForm.getIdTipoEJGSel()));
				respsolProc.setEjgselanio(new Short(entradaEnviosForm.getAnioEJGSel()));
				respsolProc.setEjgselnumero(new Long(entradaEnviosForm.getNumeroEJGSel()));
				idEstado = EstadosEntradaEnviosEnum.ESTADO_FINALIZADO.getCodigo();
			}
			
			respsolProcsMapper.insert(respsolProc);
			
			
		}else if(envEntradaEnviosWithBLOBs.getIdtipointercambiotelematico().equals(AppConstants.TipoIntercambioEnum.SGP_CAJG_RES_SOL_IMP.getCodigo())){
			EcomResolucionimpugnacion resolucionimpugnacion = new EcomResolucionimpugnacion();
			EcomResolucionimpugnacionMapper resolucionimpugnacionMapper = getMyBatisSqlSessionManager().getMapper(EcomResolucionimpugnacionMapper.class);
			
			resolucionimpugnacion.setIdenvio(envEntradaEnviosWithBLOBs.getIdenvio());
			resolucionimpugnacion.setIdinstitucion(envEntradaEnviosWithBLOBs.getIdinstitucion());
			
			
			resolucionimpugnacion.setFechamodificacion(new Date());
			resolucionimpugnacion.setUsumodificacion(new Integer (usrBean.getUserName()));

			ecomCola.setIdoperacion(OPERACION.EJIS_PROCESAR_RESOLUCION_IMPUGNACION.getId());
			if (ecomColaService.insert(ecomCola) != 1) {
				throw new BusinessException("No se ha podido insertar en la cola de comunicaciones.");
			}
			resolucionimpugnacion.setIdecomcola(ecomCola.getIdecomcola());
			idEstado = EstadosEntradaEnviosEnum.ESTADO_PROCESADO.getCodigo();

			//Se comprueba si existe ya un registro, si es asi se hace un UPDATE si no existe se hace un INSERT
			EcomResolucionimpugnacionExample resolucionimpugnacionExample = new EcomResolucionimpugnacionExample();
			org.redabogacia.sigaservices.app.autogen.model.EcomResolucionimpugnacionExample.Criteria crit = resolucionimpugnacionExample.createCriteria();
			crit.andIdenvioEqualTo(envEntradaEnviosWithBLOBs.getIdenvio());
			crit.andIdinstitucionEqualTo(envEntradaEnviosWithBLOBs.getIdinstitucion());		
			List<EcomResolucionimpugnacion> recordList =resolucionimpugnacionMapper.selectByExample(resolucionimpugnacionExample);
			if(recordList != null && recordList.size() > 0){
				resolucionimpugnacionMapper.updateByExampleSelective(resolucionimpugnacion, resolucionimpugnacionExample); 
			}else{
				resolucionimpugnacionMapper.insert(resolucionimpugnacion);
			}			
		}
		//Actualizamos el estado. 
		this.actualizarEstado(envEntradaEnviosWithBLOBs.getIdenvio(),envEntradaEnviosWithBLOBs.getIdinstitucion(), idEstado);

	}
	
	public File getPdfIntercambio(String idEnvio, String idInstitucion) throws BusinessException{
		EnvEntradaEnviosMapper envEntradaEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEntradaEnviosMapper.class);
		EnvEntradaEnviosExample entradaEnviosExample = new EnvEntradaEnviosExample();
		Criteria criteria =  entradaEnviosExample.createCriteria(); 
		criteria.andIdinstitucionEqualTo(new Short(idInstitucion));
		criteria.andIdenvioEqualTo(new Long(idEnvio));
		
		// Como tenemos una unique key solo va a haber unregistro por lo que cogemos el primero
		List<EnvEntradaEnviosWithBLOBs>  entradaEnvios = envEntradaEnviosMapper.selectByExampleWithBLOBs(entradaEnviosExample);
		EnvEntradaEnviosWithBLOBs entradaEnvioWithBLOBs = entradaEnvios.get(0);				
		File pdfIntercambioFile = null;
		
		try {			
            InputStream entrada =  new ByteArrayInputStream(entradaEnvioWithBLOBs.getPdf());
			pdfIntercambioFile = new File("adjuntosIntercambioFile_"+entradaEnvioWithBLOBs.getIdinstitucion()+"_"+entradaEnvioWithBLOBs.getIdcabecera()+".zip");				

			pdfIntercambioFile.deleteOnExit();
            OutputStream salida=new FileOutputStream(pdfIntercambioFile);
            
            byte[] buf =new byte[1024];
            int len;
	        
            while((len=entrada.read(buf))>0){
	           salida.write(buf,0,len);
	        }
	        salida.flush();
	        salida.close();
		
		} catch (Exception e) {
			throw new BusinessException("Error en la fusion de xml y xsl"+e.toString());
		}
		
		return pdfIntercambioFile;
	}	
	
	public void actualizarEstado(Long idEnvio, Short idInstitucion, Short idEstado) throws BusinessException{	
		try {
			EnvEntradaEnviosMapper envEntradaEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEntradaEnviosMapper.class);
			EnvEntradaEnviosExample entradaEnviosExample = new EnvEntradaEnviosExample();
			EnvEntradaEnviosWithBLOBs envEntradaEnvios = new EnvEntradaEnviosWithBLOBs();
			Criteria criteria =  entradaEnviosExample.createCriteria(); 
			criteria.andIdinstitucionEqualTo(idInstitucion);
			criteria.andIdenvioEqualTo(idEnvio);
			
			// Se procede a la actualizaci�n del estado
			envEntradaEnvios.setIdestado(idEstado);		
			envEntradaEnviosMapper.updateByExampleSelective(envEntradaEnvios, entradaEnviosExample);
			
		} catch (Exception e) {
			log.error("Error actualizando el estado en env�o", e);
		}
	
	}
	
	public EcomSoldesignaprovisional getDatosSolDesignaProvisional(String idEnvio, String idInstitucion) throws BusinessException{
		EcomSoldesignaprovisional ecomSoldesignaprovisional = new EcomSoldesignaprovisional();
		Map<String, Object> solDesignacionMap = new HashMap<String, Object>();
		
		try {
			solDesignacionMap.put("idEnvio",idEnvio);
			solDesignacionMap.put("idInstitucion", idInstitucion);
			log.info("Obteniendo datos de la solicitud de designa...");
			SolDesignacionProvisionalMapper solDesignacionProvisionalMapper = getMyBatisSqlSessionManager().getMapper(SolDesignacionProvisionalMapper.class);
			ecomSoldesignaprovisional = solDesignacionProvisionalMapper.getDatosSolDesignaProvisional(solDesignacionMap);
		
		} catch (Exception e) {
			log.error("Se ha producido un error al recuperar datos de la solicitud de designa", e);
		}
		
		return  ecomSoldesignaprovisional;
		
	}
	
	public List<TipoDesignaLetradoProcurador> getDesignasEJGRelacionadas(String idInstitucion, String anio, String numero, String idTipoEJG) throws BusinessException{
		List<TipoDesignaLetradoProcurador> designasRelacionadas = null;
		Map<String, Object> solDesignacionMap = new HashMap<String, Object>();
		
		try {
			solDesignacionMap.put("idInstitucion", idInstitucion);
			solDesignacionMap.put("anio",anio);
			solDesignacionMap.put("numero",numero);
			solDesignacionMap.put("idTipoEJG",idTipoEJG);
			log.info("Obteniendo datos de las designas relacionadas con el EJG...");
			
			SolDesignacionProvisionalMapper solDesignacionProvisionalMapper = getMyBatisSqlSessionManager().getMapper(SolDesignacionProvisionalMapper.class);
			designasRelacionadas = solDesignacionProvisionalMapper.getDesignasEJGRelacionadas(solDesignacionMap);
			
		} catch (Exception e) {
			log.error("Se ha producido un error al recuperar datos de la solicitud de designa", e);
		}
		
		return  designasRelacionadas;
		
	}	
	
	public TipoDesignaLetradoProcurador getDesignaSeleccionada(String idInstitucion, String anio, String numero, String idTurno) throws BusinessException{
		TipoDesignaLetradoProcurador designaSeleccionada = null;
		Map<String, Object> solDesignacionMap = new HashMap<String, Object>();
		
		try {
			solDesignacionMap.put("idInstitucion", idInstitucion);
			solDesignacionMap.put("anio",anio);
			solDesignacionMap.put("numero",numero);
			solDesignacionMap.put("idTurno",idTurno);
			log.info("Obteniendo datos de la designa seleccionada...");
			
			SolDesignacionProvisionalMapper solDesignacionProvisionalMapper = getMyBatisSqlSessionManager().getMapper(SolDesignacionProvisionalMapper.class);
			designaSeleccionada = solDesignacionProvisionalMapper.getDesignaSeleccionada(solDesignacionMap);
			
		} catch (Exception e) {
			log.error("Se ha producido un error al recuperar datos de la designa", e);
		}
		
		return  designaSeleccionada;
		
	}	

	public void updateFormularioDatosSolDesignaProvisional(EntradaEnviosForm entradaEnviosForm) throws BusinessException {	
		String idEnvio = entradaEnviosForm.getIdEnvio();
		String idInstitucion = entradaEnviosForm.getIdInstitucion();
		try{
			EcomSoldesignaprovisional solDesProv = getDatosSolDesignaProvisional(idEnvio,idInstitucion);
			if(solDesProv != null){
				entradaEnviosForm.setCaso(solDesProv.getCaso().toString());
				
				if(solDesProv.getCaso() == 1){ //CASO 1
					ScsEjg scsEjg = null;
					ScsEjgMapper ejgMapper =  getMyBatisSqlSessionManager().getMapper(ScsEjgMapper.class);
					ScsEjgKey scsEjgKey = new ScsEjgKey();					
					if(solDesProv.getEjgnewanio() != null){
						entradaEnviosForm.setAnioEJGNew(solDesProv.getEjgnewanio().toString());
						entradaEnviosForm.setNumeroEJGNew(solDesProv.getEjgnewnumero().toString());
						entradaEnviosForm.setIdTipoEJGNew(solDesProv.getEjgnewidtipo().toString());
						scsEjgKey.setIdinstitucion(new Short(idInstitucion));
						scsEjgKey.setIdtipoejg(solDesProv.getEjgnewidtipo());
						scsEjgKey.setAnio(solDesProv.getEjgnewanio());
						scsEjgKey.setNumero(solDesProv.getEjgnewnumero());
						scsEjg = ejgMapper.selectByPrimaryKey(scsEjgKey);
						
						
						entradaEnviosForm.setNumEJGNew(scsEjg.getNumejg());
					}
					
					if(solDesProv.getEjgselanio() != null){
						entradaEnviosForm.setAnioEJGSel(solDesProv.getEjgselanio().toString());
						entradaEnviosForm.setNumeroEJGSel(solDesProv.getEjgselnumero().toString());
						entradaEnviosForm.setIdTipoEJGSel(solDesProv.getEjgselidtipo().toString());
						scsEjgKey.setIdinstitucion(new Short(idInstitucion));
						scsEjgKey.setIdtipoejg(solDesProv.getEjgselidtipo());
						scsEjgKey.setAnio(solDesProv.getEjgselanio());
						scsEjgKey.setNumero(solDesProv.getEjgselnumero());
						scsEjg = ejgMapper.selectByPrimaryKey(scsEjgKey);						
						if(scsEjg.getNumejg()!=null && !scsEjg.getNumejg().equals("")){
							entradaEnviosForm.setNumEJGSel(scsEjg.getNumejg());
						}
						
						
						
						if(solDesProv.getDesignaselanio() != null){ //Se comprueba si ya se ha seleccionado la designa
							entradaEnviosForm.setAnioDesignaSel(solDesProv.getDesignaselanio().toString());
							entradaEnviosForm.setNumeroDesignaSel(solDesProv.getDesignaselnumero().toString());
							entradaEnviosForm.setIdTurnoDesignaSel(solDesProv.getDesignaselidturno().toString());
							TipoDesignaLetradoProcurador designaSeleccionada = getDesignaSeleccionada(idInstitucion, solDesProv.getDesignaselanio().toString(), solDesProv.getDesignaselnumero().toString(), solDesProv.getDesignaselidturno().toString());
							entradaEnviosForm.setAbogadoDesignaSel(designaSeleccionada.getDescripcionLetrado());
							entradaEnviosForm.setCodSelDesigna(designaSeleccionada.getCodigo());
							
							//Se mira el procurador
							if(designaSeleccionada.getIdProcurador() != null && !designaSeleccionada.getIdProcurador().equals("")){
								entradaEnviosForm.setProcuradorDesignaSel(designaSeleccionada.getDescripcionProcurador());
							}
							
						}else{
							List<TipoDesignaLetradoProcurador> designasRelacionadas = getDesignasEJGRelacionadas(idInstitucion, solDesProv.getEjgselanio().toString(), solDesProv.getEjgselnumero().toString(), solDesProv.getEjgselidtipo().toString());
							if(designasRelacionadas != null && designasRelacionadas.size() > 0){
								if(designasRelacionadas.size() == 1){//Existe una sola designa relacionada, por lo que se selecciona esta autom�ticamente
									TipoDesignaLetradoProcurador designaSeleccionada = designasRelacionadas.get(0);
									entradaEnviosForm.setAnioDesignaSel(designaSeleccionada.getAnio());
									entradaEnviosForm.setNumeroDesignaSel(designaSeleccionada.getNumero());
									entradaEnviosForm.setCodSelDesigna(designaSeleccionada.getCodigo());
									entradaEnviosForm.setIdTurnoDesignaSel(designaSeleccionada.getIdTurno());
									entradaEnviosForm.setAbogadoDesignaSel(designaSeleccionada.getDescripcionLetrado());
									
									//Se mira el procurador
									if(designaSeleccionada.getIdProcurador() != null && !designaSeleccionada.getIdProcurador().equals("")){
										entradaEnviosForm.setProcuradorDesignaSel(designaSeleccionada.getDescripcionProcurador());
									}
									
								}else{
									//Lo convierto a JSON porque no soy capaz de recorrer un List<ScsDesigna> en jquery.
									JSONArray ejgSelDesignas = new JSONArray();
									for(TipoDesignaLetradoProcurador designa :designasRelacionadas){ 
										JSONObject obj = new JSONObject();
										obj.put("value", designa.getAnio()+","+designa.getIdInstitucion()+","+designa.getIdTurno()+","+designa.getNumero());
										obj.put("descripcion", designa.getAnio()+"/"+designa.getCodigo());
										ejgSelDesignas.put(obj);
										
									}
									entradaEnviosForm.setEjgSelDesignas(ejgSelDesignas);
									
									
									
									
								}
							}
						}
					}
					BusinessManager businessManager =  BusinessManager.getInstance();
					ScsEjgService ejgService = (ScsEjgService) businessManager.getService(ScsEjgService.class);
					boolean isPreceptivoProcurador = ejgService.isProcuradorPreceptivo(scsEjg);
					entradaEnviosForm.setPreceptivoProcurador(isPreceptivoProcurador);
					
				}else if(solDesProv.getCaso() == 2){ //CASO 2
					//En este caso LP decide que siempre sea preceptivo(Se puede hacer configurable)
					entradaEnviosForm.setPreceptivoProcurador(true);
					if(solDesProv.getDesignanewanio() != null){
						entradaEnviosForm.setAnioDesignaNew(solDesProv.getDesignanewanio().toString());
						entradaEnviosForm.setNumeroDesignaNew(solDesProv.getDesignanewnumero().toString());
						entradaEnviosForm.setIdTurnoDesignaNew(solDesProv.getDesignanewidturno().toString());
						TipoDesignaLetradoProcurador newDesignaSeleccionada = getDesignaSeleccionada(idInstitucion, solDesProv.getDesignanewanio().toString(), solDesProv.getDesignanewnumero().toString(), solDesProv.getDesignanewidturno().toString());
						entradaEnviosForm.setCodNewDesigna(newDesignaSeleccionada.getCodigo());
					}
					
					if(solDesProv.getDesignaselanio() != null){
						entradaEnviosForm.setAnioDesignaSel(solDesProv.getDesignaselanio().toString());
						entradaEnviosForm.setNumeroDesignaSel(solDesProv.getDesignaselnumero().toString());
						entradaEnviosForm.setIdTurnoDesignaSel(solDesProv.getDesignaselidturno().toString());
						TipoDesignaLetradoProcurador designaSeleccionada = getDesignaSeleccionada(idInstitucion, solDesProv.getDesignaselanio().toString(), solDesProv.getDesignaselnumero().toString(), solDesProv.getDesignaselidturno().toString());
						entradaEnviosForm.setAbogadoDesignaSel(designaSeleccionada.getDescripcionLetrado());
						entradaEnviosForm.setCodSelDesigna(designaSeleccionada.getCodigo());
						
						//Se mira el procurador
						if(designaSeleccionada.getIdProcurador() != null && !designaSeleccionada.getIdProcurador().equals("")){
							entradaEnviosForm.setProcuradorDesignaSel(designaSeleccionada.getDescripcionProcurador());
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Se ha producido un error al rellenar datos del formulario correspondiente a la zona de solicitud de designa", e);
		}
	}
	
	public void borrarRelacionEJG(EntradaEnviosForm entradaEnviosForm,UsrBean usrBean) throws BusinessException	{
		try{
			EcomSoldesignaprovisionalExample soldesignaprovisionalExample = new EcomSoldesignaprovisionalExample();
			EcomSoldesignaprovisionalMapper soldesignaprovisionalMapper = getMyBatisSqlSessionManager().getMapper(EcomSoldesignaprovisionalMapper.class);
			org.redabogacia.sigaservices.app.autogen.model.EcomSoldesignaprovisionalExample.Criteria crit = soldesignaprovisionalExample.createCriteria();
			crit.andIdenvioEqualTo(new Long(entradaEnviosForm.getIdEnvio()));
			crit.andIdinstitucionEqualTo(new Short(entradaEnviosForm.getIdInstitucion()));		
			List<EcomSoldesignaprovisional> listEcomSoldesignaprovisional = soldesignaprovisionalMapper.selectByExample(soldesignaprovisionalExample);
			if(listEcomSoldesignaprovisional != null){
				EcomSoldesignaprovisional ecomSoldesignaprovisional = listEcomSoldesignaprovisional.get(0);
				
				if(ecomSoldesignaprovisional.getEjgselanio()!=null){
					ComunicacionesService comunicacionesService = (ComunicacionesService)BusinessManager.getInstance().getService(ComunicacionesService.class);
					ScsComunicaciones scsComunicaciones = new ScsComunicaciones();
					scsComunicaciones.setIdenvioentrada(ecomSoldesignaprovisional.getIdenvio());
					scsComunicaciones.setIdinstitucion(ecomSoldesignaprovisional.getIdinstitucion());
					scsComunicaciones.setEjganio(ecomSoldesignaprovisional.getEjgselanio());
					scsComunicaciones.setEjgidtipo(ecomSoldesignaprovisional.getEjgselidtipo());
					scsComunicaciones.setEjgnumero(ecomSoldesignaprovisional.getEjgselnumero());
					comunicacionesService.borrarComunicacion(scsComunicaciones);
				}
				
				
				
				ecomSoldesignaprovisional.setDesignanewidturno(null);
				ecomSoldesignaprovisional.setDesignanewanio(null);
				ecomSoldesignaprovisional.setDesignanewnumero(null);
				ecomSoldesignaprovisional.setDesignanewfecha(null);
				ecomSoldesignaprovisional.setDesignanewidtipodescol(null);
				ecomSoldesignaprovisional.setDesignanewdletrado(null);
				ecomSoldesignaprovisional.setDesignanewidinstletrado(null);
				ecomSoldesignaprovisional.setEjgselidtipo(null);
				ecomSoldesignaprovisional.setEjgselanio(null);
				ecomSoldesignaprovisional.setEjgselnumero(null);
				ecomSoldesignaprovisional.setDesignaselidturno(null);
				ecomSoldesignaprovisional.setDesignaselanio(null);
				ecomSoldesignaprovisional.setDesignaselnumero(null);
				ecomSoldesignaprovisional.setFechamodificacion(Calendar.getInstance().getTime());
				ecomSoldesignaprovisional.setUsumodificacion(new Integer (usrBean.getUserName()));	
				if(entradaEnviosForm.getAnioEJGNew()!=null && !entradaEnviosForm.getAnioEJGNew().equals("")){
					ecomSoldesignaprovisional.setEjgnewidtipo(null);
					ecomSoldesignaprovisional.setEjgnewanio(null);
					ecomSoldesignaprovisional.setEjgnewnumero(null);
				}
				soldesignaprovisionalMapper.updateByPrimaryKey(ecomSoldesignaprovisional);
				
				
				
				
				//Se borra el registro del EJG
				if(entradaEnviosForm.getAnioEJGNew()!=null && !entradaEnviosForm.getAnioEJGNew().equals("")){
					ScsEjgService ejgService = (ScsEjgService) BusinessManager.getInstance().getService(ScsEjgService.class);
					ScsEjg obj = new ScsEjg();
					obj.setAnio(new Short(entradaEnviosForm.getAnioEJGNew()));
					obj.setIdinstitucion(new Short(entradaEnviosForm.getIdInstitucion()));
					obj.setNumero(new Long(entradaEnviosForm.getNumeroEJGNew()));
					obj.setIdtipoejg(new Short(entradaEnviosForm.getIdTipoEJGNew()));
					
					Ejg ejg = new Ejg();
					ejg.setEjg(obj);
					ejgService.delete(ejg);
				}
				
				
				
				
				
				
				//Actualizamos el estado. 
				this.actualizarEstado(ecomSoldesignaprovisional.getIdenvio(),ecomSoldesignaprovisional.getIdinstitucion(), EstadosEntradaEnviosEnum.ESTADO_LEIDO.getCodigo());
			}
			
		} catch (Exception e) {
			log.error("Se ha producido un error al borrar la relacion con el EJG", e);
			//Existe relacion con otro registros
			throw new BusinessException("messages.error.ora.02292", e);
		}		
	}
	
	public void borrarRelacionDesigna(EntradaEnviosForm entradaEnviosForm,UsrBean usrBean) throws BusinessException	{
		try{
			EcomSoldesignaprovisionalExample soldesignaprovisionalExample = new EcomSoldesignaprovisionalExample();
			EcomSoldesignaprovisionalMapper soldesignaprovisionalMapper = getMyBatisSqlSessionManager().getMapper(EcomSoldesignaprovisionalMapper.class);
			org.redabogacia.sigaservices.app.autogen.model.EcomSoldesignaprovisionalExample.Criteria crit = soldesignaprovisionalExample.createCriteria();
			crit.andIdenvioEqualTo(new Long(entradaEnviosForm.getIdEnvio()));
			crit.andIdinstitucionEqualTo(new Short(entradaEnviosForm.getIdInstitucion()));		
			List<EcomSoldesignaprovisional> listEcomSoldesignaprovisional =soldesignaprovisionalMapper.selectByExample(soldesignaprovisionalExample);
			if(listEcomSoldesignaprovisional != null){
				EcomSoldesignaprovisional ecomSoldesignaprovisional = listEcomSoldesignaprovisional.get(0);
				
				if(ecomSoldesignaprovisional.getEjgselanio()!=null){
					ComunicacionesService comunicacionesService = (ComunicacionesService)BusinessManager.getInstance().getService(ComunicacionesService.class);
					ScsComunicaciones scsComunicaciones = new ScsComunicaciones();
					scsComunicaciones.setIdenvioentrada(ecomSoldesignaprovisional.getIdenvio());
					
					scsComunicaciones.setIdinstitucion(ecomSoldesignaprovisional.getIdinstitucion());
					scsComunicaciones.setDesignaanio(ecomSoldesignaprovisional.getDesignaselanio());
					scsComunicaciones.setDesignaidturno(ecomSoldesignaprovisional.getDesignaselidturno());
					scsComunicaciones.setDesignanumero(ecomSoldesignaprovisional.getDesignaselnumero());
					comunicacionesService.borrarComunicacion(scsComunicaciones);
				}
				
				
				
				ecomSoldesignaprovisional.setDesignaselidturno(null);
				ecomSoldesignaprovisional.setDesignaselanio(null);
				ecomSoldesignaprovisional.setDesignaselnumero(null);
				ecomSoldesignaprovisional.setFechamodificacion(Calendar.getInstance().getTime());
				ecomSoldesignaprovisional.setUsumodificacion(new Integer (usrBean.getUserName()));
				Date fechaDesigna = ecomSoldesignaprovisional.getDesignanewfecha();
				if(entradaEnviosForm.getAnioDesignaNew()!=null && !entradaEnviosForm.getAnioDesignaNew().equals("")){
					ecomSoldesignaprovisional.setDesignanewidturno(null);
					ecomSoldesignaprovisional.setDesignanewanio(null);
					ecomSoldesignaprovisional.setDesignanewnumero(null);
					ecomSoldesignaprovisional.setDesignanewfecha(null);
					ecomSoldesignaprovisional.setDesignanewidtipodescol(null);
					ecomSoldesignaprovisional.setDesignanewdletrado(null);
					ecomSoldesignaprovisional.setDesignanewidinstletrado(null);
				}
				soldesignaprovisionalMapper.updateByPrimaryKey(ecomSoldesignaprovisional); 

				//Se borra el registro de la designacion
				if(entradaEnviosForm.getAnioDesignaNew()!=null && !entradaEnviosForm.getAnioDesignaNew().equals("")){
					Designacion designacion = new Designacion();
					ScsDesignaService designaService = (ScsDesignaService) BusinessManager.getInstance().getService(ScsDesignaService.class);
					
					//Borramos el registro de la designa
					ScsDesigna obj = new ScsDesigna();
					obj.setAnio(new Short(entradaEnviosForm.getAnioDesignaNew()));
					obj.setIdinstitucion(new Short(entradaEnviosForm.getIdInstitucion()));
					obj.setNumero(new Long(entradaEnviosForm.getNumeroDesignaNew()));
					obj.setIdturno(new Integer(entradaEnviosForm.getIdTurnoDesignaNew()));
					designacion.setDesignacion(obj);
					
					designaService.delete(designacion);

				}				
				
				//Actualizamos el estado. 
				this.actualizarEstado(ecomSoldesignaprovisional.getIdenvio(),ecomSoldesignaprovisional.getIdinstitucion(), EstadosEntradaEnviosEnum.ESTADO_LEIDO.getCodigo());
			}
			
		} catch (Exception e) {
			log.error("Se ha producido un error al borrar la relacion con el EJG", e);
			throw new BusinessException("messages.error.ora.02292", e);
		}		
	}
	
	public void updateFormularioDatosRespuestaSuspensionProcedimiento(EntradaEnviosForm entradaEnviosForm) throws BusinessException {	
		String idEnvio = entradaEnviosForm.getIdEnvio();
		String idInstitucion = entradaEnviosForm.getIdInstitucion();
		try{
			EcomRespsolsusprocedimiento respsolProc = new EcomRespsolsusprocedimiento();
			EcomRespsolsusprocedimientoMapper respsolProcsMapper = getMyBatisSqlSessionManager().getMapper(EcomRespsolsusprocedimientoMapper.class);
			EcomRespsolsusprocedimientoKey respSolProcKey = new EcomRespsolsusprocedimientoKey();
			respSolProcKey.setIdenvio(new Long(idEnvio));
			respSolProcKey.setIdinstitucion(new Short(idInstitucion));
			respsolProc = respsolProcsMapper.selectByPrimaryKey(respSolProcKey);
			
			if(respsolProc != null){
				ScsEjg scsEjg = null;
				ScsEjgMapper ejgMapper =  getMyBatisSqlSessionManager().getMapper(ScsEjgMapper.class);
				ScsEjgKey scsEjgKey = new ScsEjgKey();					
				if(respsolProc.getEjgnewanio() != null){
					entradaEnviosForm.setAnioEJGNew(respsolProc.getEjgnewanio().toString());
					entradaEnviosForm.setNumeroEJGNew(respsolProc.getEjgnewnumero().toString());
					entradaEnviosForm.setIdTipoEJGNew(respsolProc.getEjgnewidtipo().toString());
					scsEjgKey.setIdinstitucion(new Short(idInstitucion));
					scsEjgKey.setIdtipoejg(respsolProc.getEjgnewidtipo());
					scsEjgKey.setAnio(respsolProc.getEjgnewanio());
					scsEjgKey.setNumero(respsolProc.getEjgnewnumero());
					scsEjg = ejgMapper.selectByPrimaryKey(scsEjgKey);
					entradaEnviosForm.setNumEJGNew(scsEjg.getNumejg());
				}
				
				if(respsolProc.getEjgselanio() != null){
					entradaEnviosForm.setAnioEJGSel(respsolProc.getEjgselanio().toString());
					entradaEnviosForm.setNumeroEJGSel(respsolProc.getEjgselnumero().toString());
					entradaEnviosForm.setIdTipoEJGSel(respsolProc.getEjgselidtipo().toString());
					scsEjgKey.setIdinstitucion(new Short(idInstitucion));
					scsEjgKey.setIdtipoejg(respsolProc.getEjgselidtipo());
					scsEjgKey.setAnio(respsolProc.getEjgselanio());
					scsEjgKey.setNumero(respsolProc.getEjgselnumero());
					scsEjg = ejgMapper.selectByPrimaryKey(scsEjgKey);						
					if(scsEjg.getNumejg()!=null && !scsEjg.getNumejg().equals("")){
						entradaEnviosForm.setNumEJGSel(scsEjg.getNumejg());
					}
				}
			}
			
		} catch (Exception e) {
			log.error("Se ha producido un error al rellenar datos del formulario correspondiente a la zona de solicitud de designa", e);
		}
	}	
	
	public void relacionarEnvio(Long idEnvio, Short idInstitucion, Long idEnvioRelacionado)throws BusinessException{	
		try {
			EnvEntradaEnviosMapper envEntradaEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEntradaEnviosMapper.class);
			EnvEntradaEnviosExample entradaEnviosExample = new EnvEntradaEnviosExample();
			EnvEntradaEnviosWithBLOBs envEntradaEnvios = new EnvEntradaEnviosWithBLOBs();
			Criteria criteria =  entradaEnviosExample.createCriteria(); 
			criteria.andIdinstitucionEqualTo(idInstitucion);
			criteria.andIdenvioEqualTo(idEnvio);
			
			// Se procede a la actualizaci�n del estado
			envEntradaEnvios.setIdenviorelprogramado(idEnvioRelacionado);	
			envEntradaEnvios.setFecharespuesta(Calendar.getInstance().getTime());
			envEntradaEnviosMapper.updateByExampleSelective(envEntradaEnvios, entradaEnviosExample);
			
		} catch (Exception e) {
			log.error("Error relacionando el env�o", e);
		}
	
	}	
	
	public void actualizarEntradaEnvios(EnvEntradaEnviosExample entradaEnviosExample, EnvEntradaEnviosWithBLOBs envEntradaEnvios)throws BusinessException{	
		try {
			EnvEntradaEnviosMapper envEntradaEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEntradaEnviosMapper.class);
			envEntradaEnviosMapper.updateByExampleSelective(envEntradaEnvios, entradaEnviosExample);
			
		} catch (Exception e) {
			log.error("Error al actualizar", e);
		}
	
	}

}
