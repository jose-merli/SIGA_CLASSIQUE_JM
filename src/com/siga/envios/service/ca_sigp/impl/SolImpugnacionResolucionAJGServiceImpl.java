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
import org.redabogacia.sigaservices.app.autogen.mapper.EcomSolimpugresolucionajgMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EnvEnviosMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EnvTipoenviosMapper;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
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
import com.siga.envios.service.ca_sigp.SolImpugnacionResolucionAJGService;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.MyBatisBusinessServiceTemplate;



public class SolImpugnacionResolucionAJGServiceImpl extends MyBatisBusinessServiceTemplate	implements SolImpugnacionResolucionAJGService {
	
	public SolImpugnacionResolucionAJGServiceImpl(BusinessManager businessManager) {
		super(businessManager);
		// TODO Auto-generated constructor stub
	}
	public Object executeService() throws SIGAException, ClsExceptions {
		// TODO Auto-generated method stub
		return null;
	}
	public File getFicheroLog(String idEnvio,	String idInstitucion)throws BusinessException{
		EcomSolimpugresolucionajgMapper solimpugresolucionajgMapper = getMyBatisSqlSessionManager().getMapper(EcomSolimpugresolucionajgMapper.class);
		EcomSolimpugresolucionajgExample solimpugresolucionajgExample = new EcomSolimpugresolucionajgExample();
		solimpugresolucionajgExample.createCriteria().andIdenvioEqualTo(Long.valueOf(idEnvio));
		solimpugresolucionajgExample.createCriteria().andIdinstitucionEqualTo(Short.valueOf(idInstitucion));
		solimpugresolucionajgExample.setOrderByClause("IDECOMSOLIMPUGRESOLUCIONAJG DESC");		
		List<EcomSolimpugresolucionajgWithBLOBs>  solicitudesmpugnacion = solimpugresolucionajgMapper.selectByExampleWithBLOBs(solimpugresolucionajgExample);
		EcomSolimpugresolucionajgWithBLOBs ecomSolimpugresolucionajgWithBLOBs = solicitudesmpugnacion.get(0);
		
		File logFile = null;
		InputStream  inputStream = null;
		OutputStream salida = null;
		try {
			inputStream = new ByteArrayInputStream(ecomSolimpugresolucionajgWithBLOBs.getLogerror().getBytes("ISO-8859-15"));
			File logTmp = File.createTempFile("logFile_"+ecomSolimpugresolucionajgWithBLOBs.getIdenvio()+"_","");
			
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
		EcomSolimpugresolucionajgMapper solimpugresolucionajgMapper = getMyBatisSqlSessionManager().getMapper(EcomSolimpugresolucionajgMapper.class);
		EcomSolimpugresolucionajgExample solimpugresolucionajgExample = new EcomSolimpugresolucionajgExample();
		solimpugresolucionajgExample.createCriteria().andIdenvioEqualTo(Long.valueOf(idEnvio));
		solimpugresolucionajgExample.createCriteria().andIdinstitucionEqualTo(Short.valueOf(idInstitucion));
		solimpugresolucionajgExample.setOrderByClause("IDECOMSOLIMPUGRESOLUCIONAJG DESC");		
		List<EcomSolimpugresolucionajgWithBLOBs>  solicitudesmpugnacion = solimpugresolucionajgMapper.selectByExampleWithBLOBs(solimpugresolucionajgExample);
		EcomSolimpugresolucionajgWithBLOBs ecomSolimpugresolucionajgWithBLOBs = solicitudesmpugnacion.get(0);

		EnvEnviosMapper envEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEnviosMapper.class);
		EnvEnviosKey envEnviosKey = new EnvEnviosKey();
		envEnviosKey.setIdenvio(ecomSolimpugresolucionajgWithBLOBs.getIdenvio());
		envEnviosKey.setIdinstitucion(ecomSolimpugresolucionajgWithBLOBs.getIdinstitucion());
		
		EnvEnvios envEnvios = envEnviosMapper.selectByPrimaryKey(envEnviosKey); 

		EnvTipoenviosMapper envTipoenviosMapper = getMyBatisSqlSessionManager().getMapper(EnvTipoenviosMapper.class);
		EnvTipoenvios envTipoenvios = envTipoenviosMapper.selectByPrimaryKey(envEnvios.getIdtipoenvios());
		
		EcomColaMapper ecomColaMapper = getMyBatisSqlSessionManager().getMapper(EcomColaMapper.class);
		EcomCola ecomCola = ecomColaMapper.selectByPrimaryKey(ecomSolimpugresolucionajgWithBLOBs.getIdecomcola());

		IntercambioTelematicoForm intercambioTelematicoForm = new IntercambioTelematicoForm();
		intercambioTelematicoForm.setId(ecomSolimpugresolucionajgWithBLOBs.getIdecomsolimpugresolucionajg().toString());
		intercambioTelematicoForm.setIdInstitucion(ecomSolimpugresolucionajgWithBLOBs.getIdinstitucion().toString());
		intercambioTelematicoForm.setIdEnvio(ecomSolimpugresolucionajgWithBLOBs.getIdenvio().toString());
		intercambioTelematicoForm.setEnvioNombre(envEnvios.getDescripcion());
		intercambioTelematicoForm.setEnvioTipo(UtilidadesMultidioma.getDatoMaestroIdioma(envTipoenvios.getNombre(),idioma));
		intercambioTelematicoForm.setTipoComunicacion(TipoIntercambioEnum.CAJG_SGP_SOL_IMP_AJG.getDescripcion().split(":")[1]);
		intercambioTelematicoForm.setEstadoComunicacion(AppConstants.EstadosCola.getDescripcion(ecomCola.getIdestadocola().shortValue()));
		intercambioTelematicoForm.setIdEstado(ecomCola.getIdestadocola().toString());
		if(ecomSolimpugresolucionajgWithBLOBs.getFecharespuesta()!=null){
			try {
				intercambioTelematicoForm.setFechaRespuesta(GstDate.getFormatedDateLong("", ecomSolimpugresolucionajgWithBLOBs.getFecharespuesta()));
			} catch (ClsExceptions e) {
				e.printStackTrace();
			}
		}
		intercambioTelematicoForm.setIdAcuse(ecomSolimpugresolucionajgWithBLOBs.getIdacuse());
		
		return intercambioTelematicoForm;
	}
	
	public void reprocesarIntercambio(String idEnvio,	String idInstitucion,Integer idUsuario) throws BusinessException{
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdoperacion(OPERACION.EJIS_SOLICITUD_IMPUGNACION_RESOL_AJG.getId());			
		EcomColaService ecomColaService = (EcomColaService)BusinessManager.getInstance().getService(EcomColaService.class);
		if (ecomColaService.insert(ecomCola) != 1) {
			throw new BusinessException("No se ha podido insertar en la cola de comunicaciones.");
		}

		EcomSolimpugresolucionajgMapper solimpugresolucionajgMapper = getMyBatisSqlSessionManager().getMapper(EcomSolimpugresolucionajgMapper.class);
		EcomSolimpugresolucionajgExample solimpugresolucionajgExample = new EcomSolimpugresolucionajgExample();
		solimpugresolucionajgExample.createCriteria().andIdenvioEqualTo(Long.valueOf(idEnvio));
		solimpugresolucionajgExample.createCriteria().andIdinstitucionEqualTo(Short.valueOf(idInstitucion));
		solimpugresolucionajgExample.setOrderByClause("IDECOMSOLIMPUGRESOLUCIONAJG DESC");		
		List<EcomSolimpugresolucionajgWithBLOBs>  solicitudesmpugnacion = solimpugresolucionajgMapper.selectByExampleWithBLOBs(solimpugresolucionajgExample);
		EcomSolimpugresolucionajgWithBLOBs ecomSolimpugresolucionajgWithBLOBs = solicitudesmpugnacion.get(0);		
		
		ecomSolimpugresolucionajgWithBLOBs.setLogerror("");
		ecomSolimpugresolucionajgWithBLOBs.setXml("");
		ecomSolimpugresolucionajgWithBLOBs.setFechapeticion(Calendar.getInstance().getTime());
		ecomSolimpugresolucionajgWithBLOBs.setFechamodificacion(Calendar.getInstance().getTime());
		ecomSolimpugresolucionajgWithBLOBs.setUsumodificacion(idUsuario);
		ecomSolimpugresolucionajgWithBLOBs.setIdecomcola(new Long(ecomCola.getIdecomcola()));
		solimpugresolucionajgMapper.insertSelective(ecomSolimpugresolucionajgWithBLOBs);

		EnvEnviosMapper enviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEnviosMapper.class);
		EnvEnviosKey enviosKey = new EnvEnviosKey();
		enviosKey.setIdenvio(ecomSolimpugresolucionajgWithBLOBs.getIdenvio());
		enviosKey.setIdinstitucion(ecomSolimpugresolucionajgWithBLOBs.getIdinstitucion());
		
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
	
	public void insertaIntercambioTelematico(EcomSolimpugresolucionajgWithBLOBs ecomSolimpugresolucionajgWithBLOBs, Integer idUsuario) throws BusinessException {		
		EcomSolimpugresolucionajgMapper solimpugresolucionajgMapper = getMyBatisSqlSessionManager().getMapper(EcomSolimpugresolucionajgMapper.class);		
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdoperacion(OPERACION.EJIS_SOLICITUD_IMPUGNACION_RESOL_AJG.getId());			
		EcomColaService ecomColaService = (EcomColaService)BusinessManager.getInstance().getService(EcomColaService.class);
		if (ecomColaService.insert(ecomCola) != 1) {
			throw new BusinessException("No se ha podido insertar en la cola de comunicaciones.");
		}
		
		ecomSolimpugresolucionajgWithBLOBs.setIdecomcola(new Long(ecomCola.getIdecomcola()));
		ecomSolimpugresolucionajgWithBLOBs.setFechamodificacion(new Date());
		ecomSolimpugresolucionajgWithBLOBs.setFechapeticion(ecomSolimpugresolucionajgWithBLOBs.getFechamodificacion());
		ecomSolimpugresolucionajgWithBLOBs.setUsumodificacion(idUsuario);
		solimpugresolucionajgMapper.insert(ecomSolimpugresolucionajgWithBLOBs);
	}
	
}
