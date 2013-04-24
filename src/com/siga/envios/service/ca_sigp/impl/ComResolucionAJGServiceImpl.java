package com.siga.envios.service.ca_sigp.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.OPERACION;
import org.redabogacia.sigaservices.app.AppConstants.TipoIntercambioEnum;
import org.redabogacia.sigaservices.app.autogen.mapper.EcomColaMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EcomComunicacionresolucionajgMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EcomSolimpugresolucionajgMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EnvEnviosMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EnvTipoenviosMapper;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.autogen.model.EcomComunicacionresolucionajgExample;
import org.redabogacia.sigaservices.app.autogen.model.EcomComunicacionresolucionajgWithBLOBs;
import org.redabogacia.sigaservices.app.autogen.model.EcomSolimpugresolucionajgExample;
import org.redabogacia.sigaservices.app.autogen.model.EcomSolimpugresolucionajgWithBLOBs;
import org.redabogacia.sigaservices.app.autogen.model.EnvEnvios;
import org.redabogacia.sigaservices.app.autogen.model.EnvEnviosKey;
import org.redabogacia.sigaservices.app.autogen.model.EnvTipoenvios;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.beans.EnvEstadoEnvioAdm;
import com.siga.envios.form.IntercambioTelematicoForm;
import com.siga.envios.service.ca_sigp.ComResolucionAJGService;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.MyBatisBusinessServiceTemplate;



public class ComResolucionAJGServiceImpl extends MyBatisBusinessServiceTemplate	implements ComResolucionAJGService {
	
	public ComResolucionAJGServiceImpl(BusinessManager businessManager) {
		super(businessManager);
		// TODO Auto-generated constructor stub
	}
	public Object executeService() throws SIGAException, ClsExceptions {
		// TODO Auto-generated method stub
		return null;
	}
	public File getFicheroLog(String idEnvio,	String idInstitucion)throws BusinessException{		
		EcomComunicacionresolucionajgMapper comunicacionresolucionajgMapper = getMyBatisSqlSessionManager().getMapper(EcomComunicacionresolucionajgMapper.class);
		EcomComunicacionresolucionajgExample comunicacionresolucionajgExample = new EcomComunicacionresolucionajgExample();		
		comunicacionresolucionajgExample.createCriteria().andIdenvioEqualTo(Long.valueOf(idEnvio));
		comunicacionresolucionajgExample.createCriteria().andIdinstitucionEqualTo(Short.valueOf(idInstitucion));
		comunicacionresolucionajgExample.setOrderByClause("IDCOMNRESOLUCIONAJG DESC");		
		List<EcomComunicacionresolucionajgWithBLOBs>  comunicacionesList = comunicacionresolucionajgMapper.selectByExampleWithBLOBs(comunicacionresolucionajgExample);
		EcomComunicacionresolucionajgWithBLOBs ecomComunicacionresolucionajgWithBLOBs = comunicacionesList.get(0);
		
		File logFile = null;
		InputStream  inputStream = null;
		OutputStream salida = null;
		try {
			inputStream = new ByteArrayInputStream(ecomComunicacionresolucionajgWithBLOBs.getLogerror().getBytes("ISO-8859-15"));
			File logTmp = File.createTempFile("logFile_"+ecomComunicacionresolucionajgWithBLOBs.getIdenvio()+"_","");
			
			logFile = new File(logTmp.getPath()+".txt");
			logFile.deleteOnExit();
			logTmp.delete();
			salida=new FileOutputStream(logFile);
			byte[] buf =new byte[1024];
		   int len;				
		   while((len=inputStream.read(buf))>0){				
		      salida.write(buf,0,len);				
		   }
			  
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(salida!=null)
				try {
					salida.close();
				} catch (IOException e) {e.printStackTrace();}
			if(inputStream!=null)
				try {
					inputStream.close();
				} catch (IOException e) {e.printStackTrace();}
			
		}
		
		return logFile;
	}
	
	
	public IntercambioTelematicoForm getIntercambioTelematico(String idEnvio,	String idInstitucion, String idioma) throws BusinessException {		
		EcomComunicacionresolucionajgMapper comunicacionresolucionajgMapper = getMyBatisSqlSessionManager().getMapper(EcomComunicacionresolucionajgMapper.class);
		EcomComunicacionresolucionajgExample comunicacionresolucionajgExample = new EcomComunicacionresolucionajgExample();		
		comunicacionresolucionajgExample.createCriteria().andIdenvioEqualTo(Long.valueOf(idEnvio));
		comunicacionresolucionajgExample.createCriteria().andIdinstitucionEqualTo(Short.valueOf(idInstitucion));
		comunicacionresolucionajgExample.setOrderByClause("IDCOMNRESOLUCIONAJG DESC");		
		List<EcomComunicacionresolucionajgWithBLOBs>  comunicacionesList = comunicacionresolucionajgMapper.selectByExampleWithBLOBs(comunicacionresolucionajgExample);
		EcomComunicacionresolucionajgWithBLOBs ecomComunicacionresolucionajgWithBLOBs = comunicacionesList.get(0);

		EnvEnviosMapper envEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEnviosMapper.class);
		EnvEnviosKey envEnviosKey = new EnvEnviosKey();
		envEnviosKey.setIdenvio(ecomComunicacionresolucionajgWithBLOBs.getIdenvio());
		envEnviosKey.setIdinstitucion(ecomComunicacionresolucionajgWithBLOBs.getIdinstitucion());
		
		EnvEnvios envEnvios = envEnviosMapper.selectByPrimaryKey(envEnviosKey); 

		EnvTipoenviosMapper envTipoenviosMapper = getMyBatisSqlSessionManager().getMapper(EnvTipoenviosMapper.class);
		EnvTipoenvios envTipoenvios = envTipoenviosMapper.selectByPrimaryKey(envEnvios.getIdtipoenvios());
		
		EcomColaMapper ecomColaMapper = getMyBatisSqlSessionManager().getMapper(EcomColaMapper.class);
		EcomCola ecomCola = ecomColaMapper.selectByPrimaryKey(ecomComunicacionresolucionajgWithBLOBs.getIdecomcola());

		IntercambioTelematicoForm intercambioTelematicoForm = new IntercambioTelematicoForm();
		intercambioTelematicoForm.setId(ecomComunicacionresolucionajgWithBLOBs.getIdcomnresolucionajg().toString());
		intercambioTelematicoForm.setIdInstitucion(ecomComunicacionresolucionajgWithBLOBs.getIdinstitucion().toString());
		intercambioTelematicoForm.setIdEnvio(ecomComunicacionresolucionajgWithBLOBs.getIdenvio().toString());
		intercambioTelematicoForm.setEnvioNombre(envEnvios.getDescripcion());
		intercambioTelematicoForm.setEnvioTipo(UtilidadesMultidioma.getDatoMaestroIdioma(envTipoenvios.getNombre(),idioma));
		intercambioTelematicoForm.setTipoComunicacion(TipoIntercambioEnum.CAJG_SGP_COM_RESOL_SOL_AJG.getDescripcion().split(":")[1]);
		intercambioTelematicoForm.setEstadoComunicacion(AppConstants.EstadosCola.getDescripcion(ecomCola.getIdestadocola().shortValue()));
		intercambioTelematicoForm.setIdEstado(ecomCola.getIdestadocola().toString());
		if(ecomComunicacionresolucionajgWithBLOBs.getFecharespuesta()!=null){
			try {
				intercambioTelematicoForm.setFechaRespuesta(GstDate.getFormatedDateLong("", ecomComunicacionresolucionajgWithBLOBs.getFecharespuesta()));
			} catch (ClsExceptions e) {
				e.printStackTrace();
			}
		}
		intercambioTelematicoForm.setIdAcuse(ecomComunicacionresolucionajgWithBLOBs.getIdacuse());
		
		return intercambioTelematicoForm;
	}
	
	public void reprocesarIntercambio(String idEnvio, String idInstitucion,Integer idUsuario) throws BusinessException{
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdoperacion(OPERACION.EJIS_COMUNICACION_RESOLUCION_SOL_AJG.getId());			
		EcomColaService ecomColaService = (EcomColaService)BusinessManager.getInstance().getService(EcomColaService.class);
		if (ecomColaService.insert(ecomCola) != 1) {
			throw new BusinessException("No se ha podido insertar en la cola de comunicaciones.");
		}

		EcomComunicacionresolucionajgMapper comunicacionresolucionajgMapper = getMyBatisSqlSessionManager().getMapper(EcomComunicacionresolucionajgMapper.class);
		EcomComunicacionresolucionajgExample comunicacionresolucionajgExample = new EcomComunicacionresolucionajgExample();		
		comunicacionresolucionajgExample.createCriteria().andIdenvioEqualTo(Long.valueOf(idEnvio));
		comunicacionresolucionajgExample.createCriteria().andIdinstitucionEqualTo(Short.valueOf(idInstitucion));
		comunicacionresolucionajgExample.setOrderByClause("IDCOMNRESOLUCIONAJG DESC");		
		List<EcomComunicacionresolucionajgWithBLOBs>  comunicacionesList = comunicacionresolucionajgMapper.selectByExampleWithBLOBs(comunicacionresolucionajgExample);
		EcomComunicacionresolucionajgWithBLOBs ecomComunicacionresolucionajgWithBLOBs = comunicacionesList.get(0);	
		
		ecomComunicacionresolucionajgWithBLOBs.setLogerror("");
		ecomComunicacionresolucionajgWithBLOBs.setXml("");
		ecomComunicacionresolucionajgWithBLOBs.setFechapeticion(Calendar.getInstance().getTime());
		ecomComunicacionresolucionajgWithBLOBs.setFechamodificacion(Calendar.getInstance().getTime());
		ecomComunicacionresolucionajgWithBLOBs.setUsumodificacion(idUsuario);
		ecomComunicacionresolucionajgWithBLOBs.setIdecomcola(new Long(ecomCola.getIdecomcola()));
		comunicacionresolucionajgMapper.insertSelective(ecomComunicacionresolucionajgWithBLOBs);

		EnvEnviosMapper enviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEnviosMapper.class);
		EnvEnviosKey enviosKey = new EnvEnviosKey();
		enviosKey.setIdenvio(ecomComunicacionresolucionajgWithBLOBs.getIdenvio());
		enviosKey.setIdinstitucion(ecomComunicacionresolucionajgWithBLOBs.getIdinstitucion());
		
		//MODIFICAMOS EL estado del envio
		EnvEnvios envioBean = enviosMapper.selectByPrimaryKey(enviosKey);
        envioBean.setIdestado(new Short(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESANDO.shortValue()));
        envioBean.setFechamodificacion(Calendar.getInstance().getTime());
        envioBean.setUsumodificacion(idUsuario);
        enviosMapper.updateByPrimaryKeySelective(envioBean);
	}
	
	/* NO SE USA PARA NADA */
	public File getPdfIntercambio(String idEnvio,	String idInstitucion) throws BusinessException{		
		File pdfIntercambioFile = null;		
		/*EcomDesignaprovisionalMapper ecomDesignaprovisionalMapper = getMyBatisSqlSessionManager().getMapper(EcomDesignaprovisionalMapper.class);
		EcomDesignaprovisionalExample fkDesignaprovisionalExample = new EcomDesignaprovisionalExample();
		fkDesignaprovisionalExample.createCriteria().andIdenvioEqualTo(Long.valueOf(idEnvio));
		fkDesignaprovisionalExample.createCriteria().andIdinstitucionEqualTo(Short.valueOf(idInstitucion));
		fkDesignaprovisionalExample.setOrderByClause("IDECOMDESIGNAPROV DESC");
		
		List<EcomDesignaprovisionalWithBLOBs>  designaprovisionales = ecomDesignaprovisionalMapper.selectByExampleWithBLOBs(fkDesignaprovisionalExample);
		//como tenemos un unique key solo va a haber una. cogemosla primera
		EcomDesignaprovisionalWithBLOBs ecomDesignaprovisionalWithBLOBs = designaprovisionales.get(0);
		
//		EcomDesignaprovisionalMapper ecomDesignaprovisionalMapper = getMyBatisSqlSessionManager().getMapper(EcomDesignaprovisionalMapper.class);
//		EcomDesignaprovisionalWithBLOBs	ecomDesignaprovisionalWithBLOBs = ecomDesignaprovisionalMapper.selectByPrimaryKey(new BigDecimal(idIntercambio));
		
		AdmInformeMapper admInformeMapper = getMyBatisSqlSessionManager().getMapper(AdmInformeMapper.class);
		AdmInformeKey admInformeKey = new AdmInformeKey();
		admInformeKey.setIdinstitucion(ecomDesignaprovisionalWithBLOBs.getIdinstitucionPlantilla());
		admInformeKey.setIdplantilla(ecomDesignaprovisionalWithBLOBs.getIdplantilla());
		AdmInforme admInforme = admInformeMapper.selectByPrimaryKey(admInformeKey);
		
		try {
			File pdfIntercambioTmp = File.createTempFile("pdfIntercambio_"+ecomDesignaprovisionalWithBLOBs.getIdenvio()+"_","");
			pdfIntercambioFile = new File(pdfIntercambioTmp.getPath()+".pdf");
			//pdfIntercambioFile.deleteOnExit();
			//pdfIntercambioTmp.delete();
			InputStream inputStreamXmlIntercambio = MasterReport.getInputStream(ecomDesignaprovisionalWithBLOBs.getXml(),"ISO-8859-1");
			pdfIntercambioFile = MasterReport.convertXML2PDF(inputStreamXmlIntercambio, 
					MasterReport.getInputStream(admInforme.getPlantilla(),"ISO-8859-1"),pdfIntercambioFile);
		
		} catch (Exception e) {
			throw new BusinessException("Error en la fusion de xml y xsl"+e.toString());
		}*/
		
		return pdfIntercambioFile;
	}
	
	
	public Object executeService(Object... paramArrayOfObject) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void insertaIntercambioTelematico(EcomComunicacionresolucionajgWithBLOBs ecomComunicacionresolucionajgWithBLOBs, Integer idUsuario) throws BusinessException {		
		EcomComunicacionresolucionajgMapper comunicacionresolucionajgMapper = getMyBatisSqlSessionManager().getMapper(EcomComunicacionresolucionajgMapper.class);		
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdoperacion(OPERACION.EJIS_COMUNICACION_RESOLUCION_SOL_AJG.getId());			
		EcomColaService ecomColaService = (EcomColaService)BusinessManager.getInstance().getService(EcomColaService.class);
		if (ecomColaService.insert(ecomCola) != 1) {
			throw new BusinessException("No se ha podido insertar en la cola de comunicaciones.");
		}
		
		ecomComunicacionresolucionajgWithBLOBs.setIdecomcola(new Long(ecomCola.getIdecomcola()));
		ecomComunicacionresolucionajgWithBLOBs.setFechamodificacion(new Date());
		ecomComunicacionresolucionajgWithBLOBs.setFechapeticion(ecomComunicacionresolucionajgWithBLOBs.getFechamodificacion());
		ecomComunicacionresolucionajgWithBLOBs.setUsumodificacion(idUsuario);
		comunicacionresolucionajgMapper.insert(ecomComunicacionresolucionajgWithBLOBs);
	}
	
}
