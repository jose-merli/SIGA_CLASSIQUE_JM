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
import org.redabogacia.sigaservices.app.autogen.mapper.AdmInformeMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EcomColaMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EcomSolsusprocedimientoMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EnvEnviosMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EnvTipoenviosMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.ScsEjgMapper;
import org.redabogacia.sigaservices.app.autogen.model.AdmInforme;
import org.redabogacia.sigaservices.app.autogen.model.AdmInformeKey;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.autogen.model.EcomSolsusprocedimiento;
import org.redabogacia.sigaservices.app.autogen.model.EcomSolsusprocedimientoExample;
import org.redabogacia.sigaservices.app.autogen.model.EcomSolsusprocedimientoWithBLOBs;
import org.redabogacia.sigaservices.app.autogen.model.EnvEnvios;
import org.redabogacia.sigaservices.app.autogen.model.EnvEnviosKey;
import org.redabogacia.sigaservices.app.autogen.model.EnvTipoenvios;
import org.redabogacia.sigaservices.app.autogen.model.ScsEjg;
import org.redabogacia.sigaservices.app.autogen.model.ScsEjgKey;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.beans.EnvEstadoEnvioAdm;
import com.siga.envios.form.IntercambioTelematicoForm;
import com.siga.envios.form.SolSusProcedimientoForm;
import com.siga.envios.service.ca_sigp.SolSusProcedimientoService;
import com.siga.general.SIGAException;
import com.siga.informes.MasterReport;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.MyBatisBusinessServiceTemplate;

public class SolSusProcedimientoServiceImpl extends MyBatisBusinessServiceTemplate	implements SolSusProcedimientoService {
	
	public SolSusProcedimientoServiceImpl(BusinessManager businessManager) {
		super(businessManager);
	}
	public Object executeService(Object... parameters) throws BusinessException {
		return null;
	}
	
	
	
	public Object executeService() throws SIGAException, ClsExceptions {
		// TODO Auto-generated method stub
		return null;
	}
	public File getFicheroLog(String idEnvio,	String idInstitucion)throws BusinessException{
//		getBusinessManager().endTransaction();
//		EcomSolsusprocedimientoMapper ecomSolsusprocedimientoMapper = getMyBatisSqlSessionManager().getMapper(EcomSolsusprocedimientoMapper.class);
//		EcomSolsusprocedimientoWithBLOBs	ecomSolsusprocedimientoWithBLOBs = ecomSolsusprocedimientoMapper.selectByPrimaryKey(new BigDecimal(idIntercambio));

		EcomSolsusprocedimientoMapper ecomSolsusprocedimientoMapper = getMyBatisSqlSessionManager().getMapper(EcomSolsusprocedimientoMapper.class);
		EcomSolsusprocedimientoExample fkSolsusprocedimientoExample = new EcomSolsusprocedimientoExample();
		fkSolsusprocedimientoExample.createCriteria().andIdenvioEqualTo(Long.valueOf(idEnvio));
		fkSolsusprocedimientoExample.createCriteria().andIdinstitucionEqualTo(Short.valueOf(idInstitucion));
		fkSolsusprocedimientoExample.setOrderByClause("IDECOMSOLSUSPROC DESC");		
		List<EcomSolsusprocedimientoWithBLOBs>  Solsusprocedimientoes = ecomSolsusprocedimientoMapper.selectByExampleWithBLOBs(fkSolsusprocedimientoExample);
		//como tenemos un unique key solo va a haber una. cogemosla primera
		EcomSolsusprocedimientoWithBLOBs ecomSolsusprocedimientoWithBLOBs = Solsusprocedimientoes.get(0);
		
		File logFile = null;
			InputStream  inputStream = null;
			OutputStream salida = null;
			try {
				inputStream = new ByteArrayInputStream(ecomSolsusprocedimientoWithBLOBs.getLogerror().getBytes("ISO-8859-15"));
				File logTmp = File.createTempFile("logFile_"+ecomSolsusprocedimientoWithBLOBs.getIdenvio()+"_","");
				
				logFile = new File(logTmp.getPath()+".txt");
				logFile.deleteOnExit();
				logTmp.delete();
				//logFile.deleteOnExit();
				salida=new FileOutputStream(logFile);
				
				   byte[] buf =new byte[1024];
				   int len;
				
				   while((len=inputStream.read(buf))>0){
				
				      salida.write(buf,0,len);
				
				   }
				   
				  
			} catch (IOException e) {
				// TODO Auto-generated catch block
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


//		EcomSolsusprocedimientoMapper ecomSolsusprocedimientoMapper = getMyBatisSqlSessionManager().getMapper(EcomSolsusprocedimientoMapper.class);
//		EcomSolsusprocedimiento	ecomSolsusprocedimiento = ecomSolsusprocedimientoMapper.selectByPrimaryKey(new BigDecimal(idIntercambio));
		
		EcomSolsusprocedimientoMapper ecomSolsusprocedimientoMapper = getMyBatisSqlSessionManager().getMapper(EcomSolsusprocedimientoMapper.class);
		EcomSolsusprocedimientoExample fkSolsusprocedimientoExample = new EcomSolsusprocedimientoExample();
		fkSolsusprocedimientoExample.createCriteria().andIdenvioEqualTo(Long.valueOf(idEnvio));
		fkSolsusprocedimientoExample.createCriteria().andIdinstitucionEqualTo(Short.valueOf(idInstitucion));
		fkSolsusprocedimientoExample.setOrderByClause("IDECOMSOLSUSPROC DESC");		
		List<EcomSolsusprocedimiento>  Solsusprocedimientoes = ecomSolsusprocedimientoMapper.selectByExample(fkSolsusprocedimientoExample);
		//como tenemos un unique key solo va a haber una. cogemosla primera
		EcomSolsusprocedimiento ecomSolsusprocedimiento = Solsusprocedimientoes.get(0);

		EnvEnviosMapper envEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEnviosMapper.class);
		EnvEnviosKey envEnviosKey = new EnvEnviosKey();
		envEnviosKey.setIdenvio(ecomSolsusprocedimiento.getIdenvio());
		envEnviosKey.setIdinstitucion(ecomSolsusprocedimiento.getIdinstitucion());
		
		EnvEnvios envEnvios = envEnviosMapper.selectByPrimaryKey(envEnviosKey); 

		EnvTipoenviosMapper envTipoenviosMapper = getMyBatisSqlSessionManager().getMapper(EnvTipoenviosMapper.class);
		EnvTipoenvios envTipoenvios = envTipoenviosMapper.selectByPrimaryKey(envEnvios.getIdtipoenvios());
        	        
		
		EcomColaMapper ecomColaMapper = getMyBatisSqlSessionManager().getMapper(EcomColaMapper.class);
		EcomCola ecomCola = ecomColaMapper.selectByPrimaryKey(ecomSolsusprocedimiento.getIdecomcola());
		
		SolSusProcedimientoForm solSusProcedimientoForm =  new  SolSusProcedimientoForm(ecomSolsusprocedimiento);
		
			
		ScsEjgMapper scsEjgMapper = getMyBatisSqlSessionManager().getMapper(ScsEjgMapper.class);
		ScsEjgKey scsEjgKey = new ScsEjgKey();
		scsEjgKey.setIdinstitucion(ecomSolsusprocedimiento.getIdinstitucion());
		scsEjgKey.setIdtipoejg(ecomSolsusprocedimiento.getIdtipoejg());
		scsEjgKey.setAnio(ecomSolsusprocedimiento.getAnioejg());
		scsEjgKey.setNumero(ecomSolsusprocedimiento.getNumeroejg());
		ScsEjg scsEjg = scsEjgMapper.selectByPrimaryKey(scsEjgKey);
		solSusProcedimientoForm.setEjgCodigo(scsEjg.getAnio()+"/"+scsEjg.getNumejg());
		
		IntercambioTelematicoForm intercambioTelematicoForm = new IntercambioTelematicoForm();
		intercambioTelematicoForm.setId(ecomSolsusprocedimiento.getIdecomsolsusproc().toString());
		intercambioTelematicoForm.setIdInstitucion(ecomSolsusprocedimiento.getIdinstitucion().toString());
		intercambioTelematicoForm.setIdEnvio(ecomSolsusprocedimiento.getIdenvio().toString());
		intercambioTelematicoForm.setEnvioNombre(envEnvios.getDescripcion());
		intercambioTelematicoForm.setEnvioTipo(UtilidadesMultidioma.getDatoMaestroIdioma(envTipoenvios.getNombre(),idioma));
		intercambioTelematicoForm.setTipoComunicacion(TipoIntercambioEnum.ICA_SGP_ENV_SOL_SUSP_PROC.getDescripcion().split(":")[1]);
//		FIXME AAAAAAAAAA dEBO PONER LA DESCRIPCION
		intercambioTelematicoForm.setEstadoComunicacion(AppConstants.EstadosCola.getDescripcion(ecomCola.getIdestadocola().shortValue()));
		intercambioTelematicoForm.setIdEstado(ecomCola.getIdestadocola().toString());
		intercambioTelematicoForm.setSolSusProcedimiento(solSusProcedimientoForm);
		intercambioTelematicoForm.setFechaPeticion(solSusProcedimientoForm.getFechaPeticion());
		if(ecomSolsusprocedimiento.getFecharespuesta()!=null){
			try {
				intercambioTelematicoForm.setFechaRespuesta(GstDate.getFormatedDateLong("", ecomSolsusprocedimiento.getFecharespuesta()));
			} catch (ClsExceptions e) {
				e.printStackTrace();
			}
		}
		intercambioTelematicoForm.setIdAcuse(ecomSolsusprocedimiento.getIdacuse());
		return intercambioTelematicoForm;
	}
	
	
	public void reprocesarIntercambio(String idEnvio,	String idInstitucion,Integer idUsuario) throws BusinessException{
//		EcomDesignaprovisionalMapper ecomDesignaprovisionalMapper = getMyBatisSqlSessionManager().getMapper(EcomDesignaprovisionalMapper.class);
//		EcomDesignaprovisionalWithBLOBs	ecomDesignaprovisionalWithBLOBs = ecomDesignaprovisionalMapper.selectByPrimaryKey(new BigDecimal(idIntercambio));
		
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdoperacion(OPERACION.EJIS_COMUNICACION_SOL_SUSP_PROCEDIMIENTO.getId());			
		EcomColaService ecomColaService = (EcomColaService)BusinessManager.getInstance().getService(EcomColaService.class);
		if (ecomColaService.insert(ecomCola) != 1) {
			throw new BusinessException("No se ha podido insertar en la cola de comunicaciones.");
		}
		
		
		EcomSolsusprocedimientoMapper ecomSolsusprocedimientoMapper = getMyBatisSqlSessionManager().getMapper(EcomSolsusprocedimientoMapper.class);
		EcomSolsusprocedimientoExample fkSolsusprocedimientoExample = new EcomSolsusprocedimientoExample();
		fkSolsusprocedimientoExample.createCriteria().andIdenvioEqualTo(Long.valueOf(idEnvio));
		fkSolsusprocedimientoExample.createCriteria().andIdinstitucionEqualTo(Short.valueOf(idInstitucion));
		fkSolsusprocedimientoExample.setOrderByClause("IDECOMSOLSUSPROC DESC");
		
		List<EcomSolsusprocedimientoWithBLOBs>  Solsusprocedimientoes = ecomSolsusprocedimientoMapper.selectByExampleWithBLOBs(fkSolsusprocedimientoExample);
		//como tenemos un unique key solo va a haber una. cogemosla primera
		EcomSolsusprocedimientoWithBLOBs ecomSolsusprocedimientoWithBLOBs = Solsusprocedimientoes.get(0);
		
		ecomSolsusprocedimientoWithBLOBs.setLogerror("");
		ecomSolsusprocedimientoWithBLOBs.setXml("");
		ecomSolsusprocedimientoWithBLOBs.setFechapeticion(Calendar.getInstance().getTime());
		ecomSolsusprocedimientoWithBLOBs.setFechamodificacion(Calendar.getInstance().getTime());
		ecomSolsusprocedimientoWithBLOBs.setUsumodificacion(idUsuario);
		ecomSolsusprocedimientoWithBLOBs.setIdecomcola(new Long(ecomCola.getIdecomcola()));
		ecomSolsusprocedimientoMapper.insertSelective(ecomSolsusprocedimientoWithBLOBs);
		//MODIFICAMOS EL estado del envio

		EnvEnviosMapper enviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEnviosMapper.class);
		EnvEnviosKey enviosKey = new EnvEnviosKey();
		enviosKey.setIdenvio(ecomSolsusprocedimientoWithBLOBs.getIdenvio());
		enviosKey.setIdinstitucion(ecomSolsusprocedimientoWithBLOBs.getIdinstitucion());
		EnvEnvios envioBean = enviosMapper.selectByPrimaryKey(enviosKey);
        envioBean.setIdestado(new Short(EnvEstadoEnvioAdm.K_ESTADOENVIO_PROCESANDO.shortValue()));
        envioBean.setFechamodificacion(Calendar.getInstance().getTime());
        envioBean.setUsumodificacion(idUsuario);
        enviosMapper.updateByPrimaryKeySelective(envioBean);
        
        
        
		
		
	}
	
	
	
//	public File getPdfIntercambio(String idIntercambio,UsrBean usrBean) throws BusinessException{
	public File getPdfIntercambio(String idEnvio,	String idInstitucion) throws BusinessException{
		
		EcomSolsusprocedimientoMapper ecomSolsusprocedimientoMapper = getMyBatisSqlSessionManager().getMapper(EcomSolsusprocedimientoMapper.class);
		EcomSolsusprocedimientoExample fkSolsusprocedimientoExample = new EcomSolsusprocedimientoExample();
		fkSolsusprocedimientoExample.createCriteria().andIdenvioEqualTo(Long.valueOf(idEnvio));
		fkSolsusprocedimientoExample.createCriteria().andIdinstitucionEqualTo(Short.valueOf(idInstitucion));
		fkSolsusprocedimientoExample.setOrderByClause("IDECOMSOLSUSPROC DESC");
		
		List<EcomSolsusprocedimientoWithBLOBs>  Solsusprocedimientoes = ecomSolsusprocedimientoMapper.selectByExampleWithBLOBs(fkSolsusprocedimientoExample);
		//como tenemos un unique key solo va a haber una. cogemosla primera
		EcomSolsusprocedimientoWithBLOBs ecomSolsusprocedimientoWithBLOBs = Solsusprocedimientoes.get(0);
		
//		EcomSolsusprocedimientoMapper ecomSolsusprocedimientoMapper = getMyBatisSqlSessionManager().getMapper(EcomSolsusprocedimientoMapper.class);
//		EcomSolsusprocedimientoWithBLOBs	ecomSolsusprocedimientoWithBLOBs = ecomSolsusprocedimientoMapper.selectByPrimaryKey(new BigDecimal(idIntercambio));
		
		AdmInformeMapper admInformeMapper = getMyBatisSqlSessionManager().getMapper(AdmInformeMapper.class);
		AdmInformeKey admInformeKey = new AdmInformeKey();
		admInformeKey.setIdinstitucion(ecomSolsusprocedimientoWithBLOBs.getIdinstitucionPlantilla());
		admInformeKey.setIdplantilla(ecomSolsusprocedimientoWithBLOBs.getIdplantilla());
		AdmInforme admInforme = admInformeMapper.selectByPrimaryKey(admInformeKey);
		
		
		File pdfIntercambioFile = null;
		try {
			File pdfIntercambioTmp = File.createTempFile("pdfIntercambio_"+ecomSolsusprocedimientoWithBLOBs.getIdenvio()+"_","");
			pdfIntercambioFile = new File(pdfIntercambioTmp.getPath()+".pdf");
			//pdfIntercambioFile.deleteOnExit();
			//pdfIntercambioTmp.delete();
			InputStream inputStreamXmlIntercambio = MasterReport.getInputStream(ecomSolsusprocedimientoWithBLOBs.getXml(),"ISO-8859-1");
			pdfIntercambioFile = MasterReport.convertXML2PDF(inputStreamXmlIntercambio, 
					MasterReport.getInputStream(admInforme.getPlantilla(),"ISO-8859-1"),pdfIntercambioFile);
		
		} catch (Exception e) {
		
		
			throw new BusinessException("Error en la fusion de xml y xsl"+e.toString());
		}
		
		
		
		return pdfIntercambioFile;
		
		
	}
	public void insertaIntercambioTelematico(
			EcomSolsusprocedimientoWithBLOBs ecomSolsusprocedimientoWithBLOBs,Integer idUsuario)
			throws BusinessException {
		EcomSolsusprocedimientoMapper solsusprocedimientoMapper = getMyBatisSqlSessionManager().getMapper(EcomSolsusprocedimientoMapper.class);
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdoperacion(OPERACION.EJIS_COMUNICACION_SOL_SUSP_PROCEDIMIENTO.getId());			
		EcomColaService ecomColaService = (EcomColaService)BusinessManager.getInstance().getService(EcomColaService.class);
		if (ecomColaService.insert(ecomCola) != 1) {
			throw new BusinessException("No se ha podido insertar en la cola de comunicaciones.");
		}
		ecomSolsusprocedimientoWithBLOBs.setIdecomcola(new Long(ecomCola.getIdecomcola()));
		
		ecomSolsusprocedimientoWithBLOBs.setFechamodificacion(new Date());
		ecomSolsusprocedimientoWithBLOBs.setFechapeticion(ecomSolsusprocedimientoWithBLOBs.getFechamodificacion());
		ecomSolsusprocedimientoWithBLOBs.setUsumodificacion(idUsuario);
		solsusprocedimientoMapper.insert(ecomSolsusprocedimientoWithBLOBs);
	}
	
	
	

	

}
