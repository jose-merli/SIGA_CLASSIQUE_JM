package com.siga.test;

import java.util.Date;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.SimpleChain;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.message.addressing.Constants;
import org.apache.axis.message.addressing.handler.AddressingHandler;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.http.HTTPTransport;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.LogBDDHandler;
import com.siga.ws.mutualidad.IntegracionEnumsCombos;
import com.siga.ws.mutualidad.Integracion_MetodosLocator;
import com.siga.ws.mutualidad.RespuestaMutualidad;
import com.siga.ws.mutualidad.WSHttpBinding_IIntegracion_MetodosStub;

public class MutualidadTest {

	public static void main(String[] args) throws Exception {
		MutualidadTest mutualidadTest = new MutualidadTest();
		mutualidadTest.execute("2005");
//		String claveMD5 = SIGAServicesHelper.encrypt("PRUEBASCOMISIONES2020",SIGAServicesHelper.algoritmoMD5);
//		System.out.println("clave"+claveMD5);
		
	}

	private void execute(String idInstitucion) {
		RespuestaMutualidad respuesta = new RespuestaMutualidad();
		try{
			System.setProperty("javax.net.ssl.trustStore","C://Oracle//Middleware//jdk160_29//jre//lib//security//cacerts");
    	    System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
    	    
			WSHttpBinding_IIntegracion_MetodosStub stub = getStubNoLog(idInstitucion);
			System.out.println("Comienza:"+new Date());
			IntegracionEnumsCombos enumCombos=stub.getEnums();
			System.out.println("Recibida:"+new Date());
			System.out.println("Recibid:"+enumCombos.getOpcionesCoberturas());
			
			
//			respuesta.setBeneficiarios(transformaCombo(enumCombos.getDesignacionBeneficiarios()));
//			respuesta.setCoberturas(transformaCombo(enumCombos.getOpcionesCoberturas()));
//			respuesta.setEstadosCiviles(transformaCombo(enumCombos.getEstadosCiviles()));
//			respuesta.setPeriodicidades(transformaCombo(enumCombos.getFormaPago()));
//			respuesta.setEjerciente(transformaCombo(enumCombos.getEjerciente()));
//			respuesta.setSexos(transformaCombo(enumCombos.getSexos()));
//			respuesta.setTiposDireccion(transformaCombo(enumCombos.getTiposDireccion()));
//			respuesta.setTiposDomicilio(transformaCombo(enumCombos.getTiposDomicilio()));
//			respuesta.setTiposIdentificador(transformaCombo(enumCombos.getTiposIdentificador()));
//			respuesta.setAsistencia(transformaCombo(enumCombos.getAsistenciaSanitaria()));
			respuesta.setCorrecto(true);
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
//			escribeLog("Error en llamada al recuperar enumerados: " + e);
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos.");
//			throw new SIGAException("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutosz");
		}
//		return respuesta;
		
	}
	private WSHttpBinding_IIntegracion_MetodosStub getStubNoLog(String idInstitucion) throws ClsExceptions {return getStub(idInstitucion,false);}
	private WSHttpBinding_IIntegracion_MetodosStub getStub(String idInstitucion,boolean log) throws ClsExceptions {
//		http://demoproxywl.redabogacia.org/
		String urlWS = "https://demoproxywl.redabogacia.org/IntegracionColegiosDesarrollo/Integracion_Metodos.svc";
//		String urlWS = "https://www.mutualidadabogacia.net/IntegracionColegiosDesarrollo/Integracion_Metodos.svc";
		UsrBean usrBean = new UsrBean();
		WSHttpBinding_IIntegracion_MetodosStub stub;
		try {
			Integracion_MetodosLocator locator = new Integracion_MetodosLocator(createClientConfig(usrBean, idInstitucion, "Solicitud WSMutualidad desde " +idInstitucion,log));
			
			
			stub = new WSHttpBinding_IIntegracion_MetodosStub(new java.net.URL(urlWS), locator);
			
		} catch (Exception e) {
			throw new ClsExceptions("error.inesperado.estadoMutualista");
		}

		return stub;
	}
	private EngineConfiguration createClientConfig(UsrBean usrBean, String idInstitucion, String logDescripcion, boolean log) {
		
		SimpleProvider clientConfig = new SimpleProvider();		
		Handler logSIGAasignaHandler = (Handler) new LogBDDHandler(usrBean, idInstitucion, logDescripcion);		
		Handler ah = (Handler) new AddressingHandler();
		
		System.setProperty(Constants.ENV_ADDRESSING_NAMESPACE_URI, "http://www.w3.org/2005/08/addressing");
		
		
		
		SimpleChain reqHandler = new SimpleChain();
		SimpleChain respHandler = new SimpleChain();
		
		reqHandler.addHandler(ah);
		if(log) reqHandler.addHandler(logSIGAasignaHandler);
		
		respHandler.addHandler(ah);
		if(log) respHandler.addHandler(logSIGAasignaHandler);
		Handler pivot = (Handler) new HTTPSender();
		
		Handler transport = new SimpleTargetedChain(reqHandler, pivot, respHandler);
		clientConfig.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME, transport);
		
		
		return clientConfig;
	}
}
