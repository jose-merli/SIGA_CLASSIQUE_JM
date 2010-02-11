/**
 * 
 */
package com.siga.eejg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.RPCElement;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.utils.Messages;
import org.apache.axis.utils.XMLUtils;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.IdResolver;
import org.apache.xpath.XPathAPI;
import org.redabogacia.www.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.FirmaInformacion;
import org.redabogacia.www.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.Informacion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.siga.Utilidades.FirmaXMLHelper;
import com.siga.Utilidades.SIGAReferences;



/**
 * @author angelcpe
 *
 */
public class SignerXMLHandler extends BasicHandler {
	
			
	

	/* (non-Javadoc)
	 * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {
		Message msg = messageContext.getCurrentMessage();	
		FirmaXMLHelper firmaXMLHelper = getFirmaXMLHelper();	
				
		if (messageContext.getResponseMessage() != null) {
			boolean verificado = false;
			try {
				Document doc = msg.getSOAPEnvelope().getAsDocument();
				Element signatureElement = (Element)doc.getElementsByTagName("ds:Signature").item(0);
				if (signatureElement != null) {
					verificado = firmaXMLHelper.verificarXML(msg.getSOAPEnvelope().getAsDocument());
				} else {
					verificado = true;
				}
			} catch (Exception e) {
				ClsLogging.writeFileLogError("Error verificando firma", e, 3);
			}
			if (!verificado) {
				throw new AxisFault("El xml no está firmado correctamente");
			}
		} else if (messageContext.getRequestMessage() != null) {
			try {
				Document doc = msg.getSOAPEnvelope().getAsDocument();
				Element signedObject = (Element)doc.getElementsByTagName("Informacion").item(0);				
				Element signPlace = (Element)doc.getElementsByTagName("FirmaInformacion").item(0);
				NodeList nodeList = signPlace.getChildNodes();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(0);
					signPlace.removeChild(node);
				}
				
				firmaXMLHelper.firmaXML(signedObject, signPlace);
				
				SOAPEnvelope soapEnvelope = new SOAPEnvelope();
				Canonicalizer c14n = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS);
		        byte[] canonicalMessage = c14n.canonicalizeSubtree(doc);

		        InputSource is = new InputSource(new java.io.ByteArrayInputStream(canonicalMessage));
		        DeserializationContext dser = new DeserializationContext(is, messageContext, Message.REQUEST, soapEnvelope);

		        dser.parse();
		        
				Message message = new Message(soapEnvelope);
				messageContext.setRequestMessage(message);
			} catch (Exception e) {
				ClsLogging.writeFileLogError("Error al firmar el xml", e, 3);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	private FirmaXMLHelper getFirmaXMLHelper() {
		
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);		
		
		String xmlSigNSPrefix = rp.returnProperty("eejg.xmlSigNSPrefix");
		String keyStoreType = rp.returnProperty("eejg.keyStoreType");
		String keyStoreFile = rp.returnProperty("eejg.keyStoreFile");
		String keyStorePass = rp.returnProperty("eejg.keyStorePass");
		
		String privateKeyAlias = rp.returnProperty("eejg.privateKeyAlias");
		String privateKeyPass = rp.returnProperty("eejg.privateKeyPass");
		String certificateAlias = rp.returnProperty("eejg.certificateAlias");
		
		FirmaXMLHelper firmaXMLHelper = new FirmaXMLHelper(xmlSigNSPrefix, keyStoreType, keyStoreFile, keyStorePass, privateKeyAlias, privateKeyPass, certificateAlias);
		return firmaXMLHelper;
	}


	
}
