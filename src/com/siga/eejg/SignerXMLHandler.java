package com.siga.eejg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.SOAPEnvelope;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.autogen.model.GenParametros;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.FirmaXMLHelper;
import com.siga.beans.GenParametrosAdm;

import es.satec.businessManager.BusinessManager;

public class SignerXMLHandler extends BasicHandler {
	private static final long serialVersionUID = 4903930523115324378L;
	private FirmaXMLHelper firmaXMLHelper;

	public void invoke(MessageContext messageContext) throws AxisFault {
        if (messageContext.isClient()) { // Es el cliente
            if (!messageContext.getPastPivot()) { //Enviando solicitud
    			try {
    				Message message=firmar(messageContext.getRequestMessage().getSOAPEnvelope().getAsDocument(), messageContext);
    				messageContext.setRequestMessage(message);
    			} catch (AxisFault e) {
        			ClsLogging.writeFileLogError("Error al firmar el xml.", e, 3);
    				throw e;
            	} catch (Exception e){
        			ClsLogging.writeFileLogError("Error al firmar el xml.", e, 3);
    				throw new AxisFault("Error al firmar el xml.", e);
    			}
            } else {
            	try {
            		verificar(messageContext.getResponseMessage().getSOAPEnvelope().getAsDocument());
            	} catch (AxisFault e){
        			ClsLogging.writeFileLogError("Error verificando la firma.", e, 3);
    				throw e;
            	} catch (Exception e){
        			ClsLogging.writeFileLogError("Error verificando la firma.", e, 3);
    				throw new AxisFault("Error verificando firma.", e);
            	}
            }
        } else { // Es el servidor
            if (messageContext.getPastPivot()) { //Enviando solicitud
    			try {
    				Message message=firmar(messageContext.getResponseMessage().getSOAPEnvelope().getAsDocument(), messageContext);
    				messageContext.setResponseMessage(message);
    			} catch (AxisFault e) {
        			ClsLogging.writeFileLogError("Error al firmar el xml.", e, 3);
    				throw e;
            	} catch (Exception e){
        			ClsLogging.writeFileLogError("Error al firmar el xml.", e, 3);
    				throw new AxisFault("Error al firmar el xml.", e);
    			}
            } else {
            	try {
            		verificar(messageContext.getRequestMessage().getSOAPEnvelope().getAsDocument());
            	} catch (AxisFault e){
        			ClsLogging.writeFileLogError("Error verificando la firma.", e, 3);
    				throw e;
            	} catch (Exception e){
        			ClsLogging.writeFileLogError("Error verificando la firma.", e, 3);
    				throw new AxisFault("Error verificando firma.", e);
            	}
            }
        }
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 * @throws AxisFault
	 * @throws ClsExceptions
	 */
	private boolean verificar(Document doc) throws AxisFault, ClsExceptions {
        FirmaXMLHelper firmaXMLHelper = getFirmaXMLHelper();	

		try {
			Element signatureElement = (Element)doc.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature").item(0);
			if (signatureElement != null) {
				if (!firmaXMLHelper.verificarXML(doc)) {
					throw new AxisFault("El xml no está firmado correctamente");
				}
				return true;
			} else {
				return true;
			}
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error verificando la firma.", e, 3);
		}
		return false;
	}
	
	/**
	 * 
	 * @param doc
	 * @param messageContext
	 * @return
	 * @throws AxisFault
	 * @throws ClsExceptions
	 */
	private Message firmar(Document doc, MessageContext messageContext) throws AxisFault, ClsExceptions {
		FirmaXMLHelper firmaXMLHelper = getFirmaXMLHelper();	

		Element signedObject = (Element) ((Element) doc.getFirstChild()).getElementsByTagNameNS("*", "Body").item(0);
		String identifica = signedObject.getAttribute("id");
		if (identifica==null || identifica.equals("")){
			Attr attr=doc.createAttribute("id");
			attr.setValue("Body");
			signedObject.setAttributeNode(attr);
		}
		
		Element signPlace = (Element)doc.createElementNS(signedObject.getNamespaceURI(), signedObject.getPrefix()+":Header");
		doc.getFirstChild().insertBefore(signPlace, signedObject);
		NodeList nodeList = signPlace.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(0);
			signPlace.removeChild(node);
		}

		try {
			firmaXMLHelper.firmaCabecera(signedObject, signPlace);
		} catch (Exception e){
			throw new AxisFault("Error al firmar el mensaje.", e);
		}

		SOAPEnvelope soapEnvelope = new SOAPEnvelope();

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transform = tf.newTransformer();
			transform.transform(new DOMSource(doc), new StreamResult(out));
			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
	        DeserializationContext dser = new DeserializationContext(new InputSource(in), messageContext, Message.REQUEST, soapEnvelope);
	        dser.parse();
		} catch (Exception e){
			throw new AxisFault("Error al serializar el mensaje firmado.", e);
		}
        
		return new Message(soapEnvelope);
	}

	/**
	 * 
	 * @return
	 * @throws ClsExceptions
	 */
	private FirmaXMLHelper getFirmaXMLHelper() throws ClsExceptions {
				
		if (firmaXMLHelper==null){
			try {
				UsrBean usrBean = new UsrBean();
				usrBean.setUserName(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO));
				
				GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
	        	GenParametros genParametros = new GenParametros();
	    		genParametros.setModulo(MODULO.CER.name());
	    		genParametros.setParametro(PARAMETRO.PATH_CERTIFICADOS_DIGITALES.name());
	    		genParametros.setIdinstitucion((short)0);
	        	
	    		GenParametros certificadoDigitalDirectorio = genParametrosService.getGenParametroInstitucionORvalor0(genParametros);
	    		genParametros.setParametro(PARAMETRO.NOMBRE_CERTIFICADOS_DIGITALES.name());
	    		GenParametros certificadoDigitalNombre =  genParametrosService.getGenParametroInstitucionORvalor0(genParametros);
	    		genParametros.setParametro(PARAMETRO.CLAVE_CERTIFICADOS_DIGITALES.name());
	    		GenParametros pwdCertificadoDigital =  genParametrosService.getGenParametroInstitucionORvalor0(genParametros);

	    		StringBuilder certificadoDigitalPath = new StringBuilder();
	        	certificadoDigitalPath.append(certificadoDigitalDirectorio.getValor());
	        	certificadoDigitalPath.append(File.separator);
	        	certificadoDigitalPath.append(certificadoDigitalNombre.getValor());
	        	
	    		genParametros.setParametro(PARAMETRO.EEJG_FIRMA_KEYSTORETYPE.name());
	    		GenParametros keyStoreType =  genParametrosService.getGenParametroInstitucionORvalor0(genParametros);
	        	
	    		genParametros.setParametro(PARAMETRO.EEJG_FIRMA_XMLSIGNSPREFIX.name());
	    		GenParametros xmlSigNSPrefix =  genParametrosService.getGenParametroInstitucionORvalor0(genParametros);
	        	
	            
	            
	            
	            FileInputStream fisID = new FileInputStream(certificadoDigitalPath.toString());
		        KeyStore ks = KeyStore.getInstance(keyStoreType.getValor());
		        ks.load(fisID, pwdCertificadoDigital.getValor().toCharArray());
		        fisID.close();
		        String privateKeyAlias = (String)ks.aliases().nextElement();
				
				String privateKeyPass = pwdCertificadoDigital.getValor();
				String certificateAlias = privateKeyAlias;
	
				firmaXMLHelper = new FirmaXMLHelper(xmlSigNSPrefix.getValor(), keyStoreType.getValor(), certificadoDigitalPath.toString(),
						pwdCertificadoDigital.getValor(), privateKeyAlias, privateKeyPass, certificateAlias);
			
	        } catch (Exception e) {
				throw new ClsExceptions(e, "Error al recuperar los parámetros");
			}
		}

		return firmaXMLHelper;
	}
}
