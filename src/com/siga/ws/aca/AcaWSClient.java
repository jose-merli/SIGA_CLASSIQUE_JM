package com.siga.ws.aca;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.soap.SOAPException;

import org.acabogacia.www.aca2.ws.certrev.CertificateReview;
import org.acabogacia.www.aca2.ws.certrev.CertificateReviewResponse;
import org.acabogacia.www.aca2.ws.certrev.CertificateReviewServiceSOAPStub;
import org.acabogacia.www.aca2.ws.certrev.CertificateReviewService_ServiceLocator;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.SimpleChain;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.http.HTTPTransport;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;


import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.LogBDDHandler;
import com.siga.Utilidades.UtilidadesString;


import es.satec.businessManager.BusinessManager;

/**
 * 
 * @author jorgeta 
 * @date   09/06/2016
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class AcaWSClient {
	

	
	
	
	
	public AcaWSClient() {
		
	}
	

	public CertificateReviewResponse  getCertificateReviewResponse(String tipoIdentificacion,String numeroIdentificacion,Short idInstitucion,UsrBean user) throws  BusinessException,Exception{
		 CertificateReviewResponse respuesta = new 	CertificateReviewResponse();
        try{
        	if(tipoIdentificacion==null)
        		throw new BusinessException(UtilidadesString.getMensajeIdioma(user,"messages.bajacolegial.identNoValidaSolicitudRevisionAca"));
        	
        	CertificateReviewServiceSOAPStub stub = getStub(OperationTypesId.REVISION_CERTIFICADO.getId(), idInstitucion,user);
        	CertificateReview certificateReview = getCertificateReview(idInstitucion,user); 
        	
    		certificateReview.setPersonalIdType(tipoIdentificacion);
    		certificateReview.setPersonalIdNumber(numeroIdentificacion);
            respuesta = stub.certificateReview(certificateReview);
        } catch (BusinessException e) {
        	ClsLogging.writeFileLog("Tipo de identificación no contemplado en el Servicio Web ACA "+e.getMessage(),7);
            throw e;
        }
        return respuesta;
	}
	private static String formateaFecha(Date date,String pattern){
		if(date!=null && !date.equals("")){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			return simpleDateFormat.format(date);
		}
		else 
			return "";
		
		
	}
	
	/**
	 * 
	 * @return
	 */
	private EngineConfiguration createClientConfig(String operacion, String idInstitucion,UsrBean usrBean) {
		
		SimpleProvider clientConfig = new SimpleProvider();		
		Handler logSIGAasignaHandler = (Handler) new LogBDDHandler(usrBean, idInstitucion, "AcaWSClient "+OperationTypesId.getEnum(operacion).getDescripcion()+" " +idInstitucion);
				
		SimpleChain reqHandler = new SimpleChain();
		SimpleChain respHandler = new SimpleChain();
		
		reqHandler.addHandler(logSIGAasignaHandler);
		
		respHandler.addHandler(logSIGAasignaHandler);
		Handler pivot = (Handler) new HTTPSender();
		
		Handler transport = new SimpleTargetedChain(reqHandler, pivot, respHandler);
		clientConfig.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME, transport);
		return clientConfig;
		
	}

	

	
	/**
	 * 
	 * @param st
	 * @return
	 * @throws ClsExceptions 
	 */
	public CertificateReviewServiceSOAPStub getStub(String operacion,Short idInsntitucion,UsrBean usrBean)throws BusinessException {

		
		GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
		
		
		
		String urlWS = genParametrosService.getValorParametro((short)0,PARAMETRO.WS_ACA_URL,MODULO.CEN); 

		CertificateReviewServiceSOAPStub stub;
		try {
			CertificateReviewService_ServiceLocator locator = new CertificateReviewService_ServiceLocator(createClientConfig(operacion, String.valueOf(idInsntitucion),usrBean));
			
			stub = new CertificateReviewServiceSOAPStub(new java.net.URL(urlWS), locator);

		} catch (Exception e) {
			throw new BusinessException("AcaWSClientCertificate.ReviewServiceSOAPStub");
		}

		return stub;
	}

	/**
	 * Añade el usuario y contraseña a la cabecera SOAP
	 * Esto se deberia parametrizar
	 * @return
	 * @throws SOAPException
	 * @throws ClsExceptions 
	 */
	private CertificateReview getCertificateReview(Short idInstitucion,UsrBean usr){
		GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
		CertificateReview certificateReview = new CertificateReview(); 
		certificateReview.setEchoToken(formateaFecha(new Date(),"ddMMyyyyHHmmssSSS"));
		certificateReview.setTimeStamp(Calendar.getInstance());
		certificateReview.setAplicationId(genParametrosService.getValorParametro((short)0,PARAMETRO.WS_ACA_USER,MODULO.CEN));
		certificateReview.setPassword(genParametrosService.getValorParametro((short)0,PARAMETRO.WS_ACA_PASS,MODULO.CEN));
		certificateReview.setOperationTypeId(OperationTypesId.REVISION_CERTIFICADO.getId());
		certificateReview.setRegistrationAuthorityCode(String.valueOf(idInstitucion));
		return certificateReview;
	}
	

	/*
	 * Tipos de Operacion del wsdl
	 */
	public static enum OperationTypesId {
		REVISION_CERTIFICADO ("1","Revisión  para revocación de certificado.");


	    private final String id;   
		private final String descripcion;   
	    
	    
		OperationTypesId(String id, String descripcion) {
	    	this.id = id;
	        this.descripcion = descripcion;
	    }
		
		public String getId() {
			return id;
		}

		public String getDescripcion() {
			return descripcion;
		}
		public static OperationTypesId getEnum(String id){
			for(OperationTypesId sc : values()){
				if (sc.getId().equalsIgnoreCase(id)){
					return sc;
				}
			}
			return null;
		}

	}
	/*
	 * Conversion de tipos de identificacion de SIGA  a los tipos de identificacion de los wsdl
	 */
	public static enum PersonalIdTypes {

		
		DNI ((short)10,"1",  "D.N.I."),
		NIE ((short)40,"2", "N.I.E");


	    private final short idSIGA;   
	    private final String idType;
		private final String descripcion;   
	    
	    
		PersonalIdTypes(short idSIGA,String idType, String descripcion) {
	    	this.idSIGA = idSIGA;
	    	this.idType = idType;
	        this.descripcion = descripcion;
	    }
		
		
		public String getDescripcion() {
			return descripcion;
		}
		public static PersonalIdTypes getEnum(short idSIGA){
			for(PersonalIdTypes idTypes : values()){
				if (idTypes.getIdSIGA()==idSIGA){
					return idTypes;
				}
			}
			return null;
		}


		public short getIdSIGA() {
			return idSIGA;
		}


		public String getIdType() {
			return idType;
		}

	}
	
	
}
