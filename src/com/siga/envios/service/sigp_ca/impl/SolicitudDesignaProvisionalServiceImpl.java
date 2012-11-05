package com.siga.envios.service.sigp_ca.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.OPERACION;
import org.redabogacia.sigaservices.app.AppConstants.TipoIntercambioEnum;
import org.redabogacia.sigaservices.app.autogen.mapper.AdmInformeMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.CenColegiadoMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.CenPersonaMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EcomColaMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EcomDesignaprovisionalMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EnvEnviosMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.EnvTipoenviosMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.ScsDesignaMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.ScsEjgMapper;
import org.redabogacia.sigaservices.app.autogen.mapper.ScsProcuradorMapper;
import org.redabogacia.sigaservices.app.autogen.model.AdmInforme;
import org.redabogacia.sigaservices.app.autogen.model.AdmInformeKey;
import org.redabogacia.sigaservices.app.autogen.model.CenColegiado;
import org.redabogacia.sigaservices.app.autogen.model.CenColegiadoKey;
import org.redabogacia.sigaservices.app.autogen.model.CenPersona;
import org.redabogacia.sigaservices.app.autogen.model.EcomCola;
import org.redabogacia.sigaservices.app.autogen.model.EcomDesignaprovisional;
import org.redabogacia.sigaservices.app.autogen.model.EcomDesignaprovisionalExample;
import org.redabogacia.sigaservices.app.autogen.model.EcomDesignaprovisionalWithBLOBs;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnviosExample;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnviosWithBLOBs;
import org.redabogacia.sigaservices.app.autogen.model.EnvEnvios;
import org.redabogacia.sigaservices.app.autogen.model.EnvEnviosKey;
import org.redabogacia.sigaservices.app.autogen.model.EnvTipoenvios;
import org.redabogacia.sigaservices.app.autogen.model.ScsDesigna;
import org.redabogacia.sigaservices.app.autogen.model.ScsDesignaKey;
import org.redabogacia.sigaservices.app.autogen.model.ScsEjg;
import org.redabogacia.sigaservices.app.autogen.model.ScsEjgKey;
import org.redabogacia.sigaservices.app.autogen.model.ScsProcurador;
import org.redabogacia.sigaservices.app.autogen.model.ScsProcuradorKey;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;

import com.atos.utils.ClsExceptions;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.envios.form.DesignaProvisionalForm;
import com.siga.envios.form.IntercambioTelematicoForm;
import com.siga.envios.service.sigp_ca.SolicitudDesignaProvisionalService;
import com.siga.general.SIGAException;
import com.siga.informes.MasterReport;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.MyBatisBusinessServiceTemplate;



public class SolicitudDesignaProvisionalServiceImpl extends MyBatisBusinessServiceTemplate	implements SolicitudDesignaProvisionalService
	 {
	
	
	
	
	
	public SolicitudDesignaProvisionalServiceImpl(BusinessManager businessManager) {
		super(businessManager);
		// TODO Auto-generated constructor stub
	}
	public Object executeService() throws SIGAException, ClsExceptions {
		// TODO Auto-generated method stub
		return null;
	}
	public File getFicheroLog(String idEnvio,	String idInstitucion)throws BusinessException{
//		getBusinessManager().endTransaction();
//		EcomDesignaprovisionalMapper ecomDesignaprovisionalMapper = getMyBatisSqlSessionManager().getMapper(EcomDesignaprovisionalMapper.class);
//		EcomDesignaprovisionalWithBLOBs	ecomDesignaprovisionalWithBLOBs = ecomDesignaprovisionalMapper.selectByPrimaryKey(new BigDecimal(idIntercambio));
		
		EcomDesignaprovisionalMapper ecomDesignaprovisionalMapper = getMyBatisSqlSessionManager().getMapper(EcomDesignaprovisionalMapper.class);
		EcomDesignaprovisionalExample fkDesignaprovisionalExample = new EcomDesignaprovisionalExample();
		fkDesignaprovisionalExample.createCriteria().andIdenvioEqualTo(Long.valueOf(idEnvio));
		fkDesignaprovisionalExample.createCriteria().andIdinstitucionEqualTo(Short.valueOf(idInstitucion));
		fkDesignaprovisionalExample.setOrderByClause("IDECOMDESIGNAPROV DESC");		
		List<EcomDesignaprovisionalWithBLOBs>  designaprovisionales = ecomDesignaprovisionalMapper.selectByExampleWithBLOBs(fkDesignaprovisionalExample);
		//como tenemos un unique key solo va a haber una. cogemosla primera
		EcomDesignaprovisionalWithBLOBs ecomDesignaprovisionalWithBLOBs = designaprovisionales.get(0);
		
		File logFile = null;
			InputStream  inputStream = null;
			OutputStream salida = null;
			try {
				inputStream = new ByteArrayInputStream(ecomDesignaprovisionalWithBLOBs.getLogerror().getBytes("ISO-8859-15"));
				File logTmp = File.createTempFile("logFile_"+ecomDesignaprovisionalWithBLOBs.getIdenvio()+"_","");
				
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


//		EcomDesignaprovisionalMapper ecomDesignaprovisionalMapper = getMyBatisSqlSessionManager().getMapper(EcomDesignaprovisionalMapper.class);
//		EcomDesignaprovisional	ecomDesignaProvisional = ecomDesignaprovisionalMapper.selectByPrimaryKey(new BigDecimal(idIntercambio));
		
		EcomDesignaprovisionalMapper ecomDesignaprovisionalMapper = getMyBatisSqlSessionManager().getMapper(EcomDesignaprovisionalMapper.class);
		EcomDesignaprovisionalExample fkDesignaprovisionalExample = new EcomDesignaprovisionalExample();
		fkDesignaprovisionalExample.createCriteria().andIdenvioEqualTo(Long.valueOf(idEnvio));
		fkDesignaprovisionalExample.createCriteria().andIdinstitucionEqualTo(Short.valueOf(idInstitucion));
		fkDesignaprovisionalExample.setOrderByClause("IDECOMDESIGNAPROV DESC");		
		List<EcomDesignaprovisional>  designaprovisionales = ecomDesignaprovisionalMapper.selectByExample(fkDesignaprovisionalExample);
		//como tenemos un unique key solo va a haber una. cogemosla primera
		EcomDesignaprovisional ecomDesignaProvisional = designaprovisionales.get(0);

		EnvEnviosMapper envEnviosMapper = getMyBatisSqlSessionManager().getMapper(EnvEnviosMapper.class);
		EnvEnviosKey envEnviosKey = new EnvEnviosKey();
		envEnviosKey.setIdenvio(ecomDesignaProvisional.getIdenvio());
		envEnviosKey.setIdinstitucion(ecomDesignaProvisional.getIdinstitucion());
		
		EnvEnvios envEnvios = envEnviosMapper.selectByPrimaryKey(envEnviosKey); 

		EnvTipoenviosMapper envTipoenviosMapper = getMyBatisSqlSessionManager().getMapper(EnvTipoenviosMapper.class);
		EnvTipoenvios envTipoenvios = envTipoenviosMapper.selectByPrimaryKey(envEnvios.getIdtipoenvios());
        	        
		
		EcomColaMapper ecomColaMapper = getMyBatisSqlSessionManager().getMapper(EcomColaMapper.class);
		EcomCola ecomCola = ecomColaMapper.selectByPrimaryKey(ecomDesignaProvisional.getIdecomcola());
		
		DesignaProvisionalForm designaProvisionalForm =  new  DesignaProvisionalForm(ecomDesignaProvisional);

		ScsDesignaMapper scsDesignaMapper = getMyBatisSqlSessionManager().getMapper(ScsDesignaMapper.class);
		ScsDesignaKey scsDesignaKey = new ScsDesignaKey();
		scsDesignaKey.setIdinstitucion(ecomDesignaProvisional.getIdinstitucion());
		scsDesignaKey.setIdturno(ecomDesignaProvisional.getIdturno());
		scsDesignaKey.setAnio(ecomDesignaProvisional.getAnio());
		scsDesignaKey.setNumero(ecomDesignaProvisional.getNumero());
		ScsDesigna scsDesigna = scsDesignaMapper.selectByPrimaryKey(scsDesignaKey);

		designaProvisionalForm.setDesignaCodigo(scsDesigna.getAnio()+"/"+scsDesigna.getCodigo());
		
		if(ecomDesignaProvisional.getAnioejg()!=null){
			
			ScsEjgMapper scsEjgMapper = getMyBatisSqlSessionManager().getMapper(ScsEjgMapper.class);
			ScsEjgKey scsEjgKey = new ScsEjgKey();
			scsEjgKey.setIdinstitucion(ecomDesignaProvisional.getIdinstitucion());
			scsEjgKey.setIdtipoejg(ecomDesignaProvisional.getIdtipoejg());
			scsEjgKey.setAnio(ecomDesignaProvisional.getAnioejg());
			scsEjgKey.setNumero(ecomDesignaProvisional.getNumeroejg());
			ScsEjg scsEjg = scsEjgMapper.selectByPrimaryKey(scsEjgKey);
			designaProvisionalForm.setEjgCodigo(scsEjg.getAnio()+"/"+scsEjg.getNumejg());
		}
		
		
		CenPersonaMapper cenPersonaMapper = getMyBatisSqlSessionManager().getMapper(CenPersonaMapper.class);
		CenPersona cenPersona = cenPersonaMapper.selectByPrimaryKey(ecomDesignaProvisional.getIdpersona());
		CenColegiadoMapper cenColegiadoMapper = getMyBatisSqlSessionManager().getMapper(CenColegiadoMapper.class);
		CenColegiadoKey cenColegiadoKey = new CenColegiadoKey();
		cenColegiadoKey.setIdpersona(ecomDesignaProvisional.getIdpersona());
		cenColegiadoKey.setIdinstitucion(ecomDesignaProvisional.getIdinstitucion());
		CenColegiado cenColegiado = cenColegiadoMapper.selectByPrimaryKey(cenColegiadoKey);
		
		
		StringBuffer abogadoDesignado = new StringBuffer();
		if(cenColegiado.getNcomunitario()!=null && cenColegiado.equals("1"))
			abogadoDesignado.append(cenColegiado.getNcomunitario());
		else
			abogadoDesignado.append(cenColegiado.getNcolegiado());
		
		abogadoDesignado.append(" ");
		abogadoDesignado.append(cenPersona.getNombre());
		abogadoDesignado.append(" ");
		abogadoDesignado.append(cenPersona.getApellidos1());
		abogadoDesignado.append(" ");
		abogadoDesignado.append(cenPersona.getApellidos2());
		designaProvisionalForm.setAbogadoDesignado(abogadoDesignado.toString());
		
		
		if(ecomDesignaProvisional.getIdprocurador()!=null){
			ScsProcuradorMapper scsProcuradorMapper = getMyBatisSqlSessionManager().getMapper(ScsProcuradorMapper.class);
			ScsProcuradorKey scsProcuradorKey = new ScsProcuradorKey();
			scsProcuradorKey.setIdinstitucion(ecomDesignaProvisional.getIdinstitucion());
			scsProcuradorKey.setIdprocurador(ecomDesignaProvisional.getIdprocurador());
			ScsProcurador scsProcurador = scsProcuradorMapper.selectByPrimaryKey(scsProcuradorKey);
			StringBuffer procuradorDesignado = new StringBuffer();
			if(scsProcurador.getNcolegiado()!=null){
				procuradorDesignado.append(scsProcurador.getNcolegiado());
				procuradorDesignado.append(" ");
			}else{
				procuradorDesignado.append("-");
				procuradorDesignado.append(" ");
				
			}
			procuradorDesignado.append(scsProcurador.getNombre());
			procuradorDesignado.append(" ");
			procuradorDesignado.append(scsProcurador.getApellidos1());
			procuradorDesignado.append(" ");
			procuradorDesignado.append(scsProcurador.getApellidos2());
			
			designaProvisionalForm.setProcuradorDesignado(procuradorDesignado.toString());
			
		}
		IntercambioTelematicoForm intercambioTelematicoForm = new IntercambioTelematicoForm();
		intercambioTelematicoForm.setId(ecomDesignaProvisional.getIdecomdesignaprov().toString());
		intercambioTelematicoForm.setIdInstitucion(ecomDesignaProvisional.getIdinstitucion().toString());
		intercambioTelematicoForm.setIdEnvio(ecomDesignaProvisional.getIdenvio().toString());
		intercambioTelematicoForm.setEnvioNombre(envEnvios.getDescripcion());
		intercambioTelematicoForm.setEnvioTipo(UtilidadesMultidioma.getDatoMaestroIdioma(envTipoenvios.getNombre(),idioma));
		intercambioTelematicoForm.setTipoComunicacion(TipoIntercambioEnum.ICA_SGP_COM_DES_PROV_ABG_PRO.getDescripcion().split(":")[1]);
//		FIXME AAAAAAAAAA dEBO PONER LA DESCRIPCION
		intercambioTelematicoForm.setEstadoComunicacion(AppConstants.EstadosCola.getDescripcion(ecomCola.getIdestadocola().shortValue()));
		intercambioTelematicoForm.setIdEstado(ecomCola.getIdestadocola().toString());
		intercambioTelematicoForm.setDesignaProvisional(designaProvisionalForm);
		intercambioTelematicoForm.setFechaPeticion(designaProvisionalForm.getFechaPeticion());
		
		return intercambioTelematicoForm;
	}
	
	
	
	
//	public File getPdfIntercambio(String idIntercambio,UsrBean usrBean) throws BusinessException{
	public File getPdfIntercambio(String idEnvio,	String idInstitucion) throws BusinessException{
		
		EcomDesignaprovisionalMapper ecomDesignaprovisionalMapper = getMyBatisSqlSessionManager().getMapper(EcomDesignaprovisionalMapper.class);
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
		
		
		File pdfIntercambioFile = null;
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
		}
		
		
		
		return pdfIntercambioFile;
		
		
	}
	public Object executeService(Object... paramArrayOfObject)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void insertaIntercambioTelematico(
			EcomDesignaprovisionalWithBLOBs ecomDesignaprovisionalWithBLOBs, Integer idUsuario)
			throws BusinessException {
		EcomDesignaprovisionalMapper ecomDesignaprovisionalMapper = getMyBatisSqlSessionManager().getMapper(EcomDesignaprovisionalMapper.class);
		
		EcomCola ecomCola = new EcomCola();
		ecomCola.setIdoperacion(OPERACION.EJIS_COMUNICACION_DESIGNA_ABOGADO_PROCURADOR.getId());			
		EcomColaService ecomColaService = (EcomColaService)BusinessManager.getInstance().getService(EcomColaService.class);
		if (ecomColaService.insert(ecomCola) != 1) {
			throw new BusinessException("No se ha podido insertar en la cola de comunicaciones.");
		}
		ecomDesignaprovisionalWithBLOBs.setIdecomcola(new Long(ecomCola.getIdecomcola()));
		
		ecomDesignaprovisionalWithBLOBs.setFechamodificacion(new Date());
		ecomDesignaprovisionalWithBLOBs.setFechapeticion(ecomDesignaprovisionalWithBLOBs.getFechamodificacion());
		ecomDesignaprovisionalWithBLOBs.setUsumodificacion(idUsuario);
		ecomDesignaprovisionalMapper.insert(ecomDesignaprovisionalWithBLOBs);
	}
	public void procesarIntercambio(String idEnvio, String idInstitucion,
			Integer idUsuario) throws BusinessException {
		// TODO Auto-generated method stub
		
	}
	public List<EnvEntradaEnviosWithBLOBs> getEntradaEnvios(
			EnvEntradaEnviosExample entradaEnvios) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

	

}
