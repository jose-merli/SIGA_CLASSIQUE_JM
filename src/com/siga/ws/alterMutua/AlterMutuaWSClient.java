/**
 * 
 */
package com.siga.ws.alterMutua;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.SimpleChain;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.http.HTTPTransport;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.LogBDDHandler;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.GenParametrosAdm;
import com.siga.general.SIGAException;
import com.siga.ws.SIGAWSClientAbstract;
import com.siga.ws.mutualidad.WSHttpBinding_IIntegracion_MetodosStub;

/**
 * 
 * @author josebd
 *
 */
public class AlterMutuaWSClient extends SIGAWSClientAbstract {
	
	private static final String URLALTERMUTUA = "WS_ALTERM_URL";
	
	
	public AlterMutuaWSClient(UsrBean user) {
		super.setUsrBean(user);
		super.setIdInstitucion(Integer.parseInt(user.getLocation()));
	}
	

	public WSRespuesta getEstadoSolicitud (int idSolicitud, boolean certificado) throws IOException, SIGAException{
		WSRespuesta respuesta = new WSRespuesta();
        try{
            WSSIGASoap_BindingStub stub = getStubNoLog();
            respuesta = stub.getEstadoSolicitud(idSolicitud, certificado);
        } catch (Exception e) {
            escribeLog("Error en llamada a getEstadoSolicitud: " + e.getMessage());
            throw new SIGAException("S'ha produït un error en comunicar amb  Alter Mutua");
        }
        return respuesta;
	}
	
	public WSRespuesta getEstadoColegiado (int tipoIdent, String ident) throws IOException, SIGAException{
		WSRespuesta respuesta = new WSRespuesta();
        try{
            WSSIGASoap_BindingStub stub = getStubNoLog();
            respuesta = stub.getEstadoColegiado(tipoIdent, ident);
        } catch (Exception e) {
            escribeLog("Error en llamada a getEstadoColegiado: " + e.getMessage());
            throw new SIGAException("S'ha produït un error en comunicar amb  Alter Mutua");
        }
        return respuesta;
	}
	
	/**
	 * Metodo que obtiene las distinta propuestas que un futuro colegiado puede contratar 
	 * 
	 * @param tipoIdent
	 * @param ident
	 * @param fechaNacimiento
	 * @param sexo
	 * @param propuesta
	 * @return
	 * @throws IOException
	 * @throws SIGAException
	 */
	public WSRespuesta getPropuestas (int tipoIdent, String ident, Calendar fechaNacimiento, int sexo, int propuesta) throws IOException, SIGAException{
		WSRespuesta respuesta = new WSRespuesta();
        try{
            WSSIGASoap_BindingStub stub = getStubNoLog();
            respuesta = stub.getPropuestas(tipoIdent, ident, fechaNacimiento, sexo, propuesta);
        } catch (Exception e) {
            escribeLog("Error en llamada a getPropuestas: " + e.getMessage());
            throw new SIGAException("S'ha produït un error en comunicar amb  Alter Mutua");
        }
        return respuesta;
	}
	
	/**
	 * Realiza la solicitud de alta en alter mutua.
	 * @param solicitud
	 * @return
	 * @throws IOException
	 * @throws SIGAException
	 */
	public WSRespuesta realizarSolicitudAlter (WSSolicitud solicitud) throws IOException, SIGAException{
		WSRespuesta respuesta = new WSRespuesta();
        try{
            WSSIGASoap_BindingStub stub = getStubLog();
            respuesta = stub.setSolicitudAlter(solicitud);
        } catch (Exception e) {
            escribeLog("Error al realizar la solicitud de alta: " + e.getMessage());
            throw new SIGAException("S'ha produït un error en comunicar amb  Alter Mutua");
        }
        return respuesta;
	}
	
	/**
	 * Metodo que obtiene las distintas tarifas para la solicitud de alta 
	 * @param solicitud
	 * @return
	 * @throws IOException
	 * @throws SIGAException
	 */
	public WSRespuesta getTarifaSolicitud (WSSolicitud solicitud) throws IOException, SIGAException{
		WSRespuesta respuesta = new WSRespuesta();
		try{
			WSSIGASoap_BindingStub stub = getStubNoLog();
			respuesta = stub.getTarifaSolicitud(solicitud);
		} catch (Exception e) {
			escribeLog("Error al recuperar las tarifas: " + e.getMessage());
			throw new SIGAException("Sha produït un error en comunicar amb Alter Mutua");
		}
		return respuesta;
	}
	
	/**
	 * 
	 * @return
	 */
	private EngineConfiguration createClientConfig(UsrBean usrBean, String idInstitucion, String logDescripcion, boolean log) {
		
		SimpleProvider clientConfig = new SimpleProvider();		
		Handler logSIGAasignaHandler = (Handler) new LogBDDHandler(usrBean, idInstitucion, logDescripcion);
				
		SimpleChain reqHandler = new SimpleChain();
		SimpleChain respHandler = new SimpleChain();
		
		if(log)reqHandler.addHandler(logSIGAasignaHandler);
		
		if(log)respHandler.addHandler(logSIGAasignaHandler);
		Handler pivot = (Handler) new HTTPSender();
		
		Handler transport = new SimpleTargetedChain(reqHandler, pivot, respHandler);
		clientConfig.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME, transport);
		return clientConfig;
		
	}



	private WSSIGASoap_BindingStub getStubLog() throws ClsExceptions {return getStub(true);}
	private WSSIGASoap_BindingStub getStubNoLog() throws ClsExceptions {return getStub(false);}
	
	
	/**
	 * 
	 * @param st
	 * @return
	 * @throws ClsExceptions 
	 */
	public WSSIGASoap_BindingStub getStub(boolean log) throws ClsExceptions {

		String urlWS = getUrlWSParametro(URLALTERMUTUA);
		//String urlWS = "https://www.altermutua.com/WSSIGATEST/ServiciosAlter.asmx";

		WSSIGASoap_BindingStub stub;
		try {
			WSSIGALocator locator = new WSSIGALocator(createClientConfig(getUsrBean(), String.valueOf(getIdInstitucion()), "Solicitud AlterMutua desde " + getIdInstitucion(),log));
			
			stub = new WSSIGASoap_BindingStub(new java.net.URL(urlWS), locator);

			stub.setHeader(getUsuarioClave(getUsrBean()));

		} catch (Exception e) {
			throw new ClsExceptions("error.inesperado.estadoMutualista");
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
	private SOAPHeaderElement getUsuarioClave(UsrBean usr) throws SOAPException, ClsExceptions{
		
			SOAPHeaderElement oHeaderElement;
			SOAPElement oElement;   
			
			GenParametrosAdm paramAdm = new GenParametrosAdm(usr);
			String user = paramAdm.getValor(usr.getLocation(),"CEN", "WS_ALTERM_USER", "");
			String password = paramAdm.getValor(usr.getLocation(),"CEN", "WS_ALTERM_PASS", "");
		
		    oHeaderElement = new SOAPHeaderElement("http://www.altermutua.com/WSSIGA", "Credenciales");
		    oHeaderElement.setPrefix("wss");
		
		    oElement = oHeaderElement.addChildElement("Usuario");
		    //oElement.addTextNode("WS_SIGA_TEST");
		    oElement.addTextNode(user);
		    oElement = oHeaderElement.addChildElement("Clave");
		    //oElement.addTextNode("Hutfnw54oi62ywQDhr");
		    oElement.addTextNode(password);
		    
		    return oHeaderElement;
	}
	
	
	private String getRutaPDF(byte[] pdf, String NIF, String institucion) throws IOException{
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
	    StringBuffer rutaPDF = new StringBuffer();
	    rutaPDF.append("");
	    if(pdf!=null && pdf.length>0){
		    try {
			    rutaPDF.append( rp.returnProperty("alterMutua.directorioFicheros") );
			    rutaPDF.append( rp.returnProperty("alterMutua.directorioLog") );
			    rutaPDF.append( "/" + institucion + "/" +NIF + "_" +  UtilidadesString.getTimeStamp() + ".pdf" );
				FileOutputStream fos;
				File ficheroTemp = new File(rutaPDF.toString()); 
				fos = new FileOutputStream(ficheroTemp);
				fos.write(pdf);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		return rutaPDF.toString();
	}


	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
