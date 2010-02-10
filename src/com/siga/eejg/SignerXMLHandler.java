/**
 * 
 */
package com.siga.eejg;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.RPCElement;
import org.apache.axis.message.RPCParam;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.IdResolver;
import org.redabogacia.www.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.FirmaInformacion;
import org.redabogacia.www.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.Informacion;
import org.redabogacia.www.pjgpra.wspjgpra.SolicitudPeticionInfoAAPP.SolicitudPeticionInfoAAPP;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.siga.Utilidades.SIGAReferences;



/**
 * @author angelcpe
 *
 */
public class SignerXMLHandler extends BasicHandler {
	
	private String xmlSigNSPrefix = "ds";
	private String keyStoreType = "JKS";
	private String keyStoreFile = null;
	private String keyStorePass = "xmlsecurity";
	
	private String privateKeyAlias = "test";
	private String privateKeyPass = "xmlsecurity";
	private String signatureAlgorithm = XMLSignature.ALGO_ID_SIGNATURE_DSA;
	private String certificateAlias = "test";
	

	/* (non-Javadoc)
	 * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {
		Message msg = messageContext.getCurrentMessage();		
		try {	
						
			if (SolicitudPeticionInfoAAPP.class.getSimpleName().equals(msg.getSOAPBody().getFirstChild().getNodeName())) {
				RPCElement rpcElement = (RPCElement)msg.getSOAPBody().getFirstChild();
//				solicitudPeticionInfoAAPP(msg, rpcElement);
			}
			
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error en SignerXMLHandler", e, 3);
		}
	}


	/**
	 * 
	 * @param msg
	 * @param node
	 * @throws Exception 
	 */
	private void solicitudPeticionInfoAAPP(Message msg, RPCElement rpcElement) throws Exception {
		
		Document doc = rpcElement.getAsDocument();		
			
		Element signedObject = (Element) doc.getElementsByTagName(Informacion.class.getSimpleName()).item(0);
		Element signPlace = (Element) doc.getElementsByTagName(FirmaInformacion.class.getSimpleName()).item(0);
		firmaXML(signedObject, signPlace);
		msg.getSOAPBody().removeContents();
		msg.getSOAPBody().addDocument(doc);
		ClsLogging.writeFileLog(msg.getSOAPEnvelope().getAsString(), 3);
		
	}

	/**
	 * 
	 * @param signedObject
	 * @param signPlace
	 * @throws Exception
	 */
	public void firmaXML(Element signedObject, Element signPlace) throws Exception {
		
		ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);			
		keyStoreFile = rp.returnProperty("eejg.keystoreFile");
		
		
		org.apache.xml.security.Init.init();		
		Constants.setSignatureSpecNSprefix(xmlSigNSPrefix);
		IdResolver.registerElementById(signedObject, "id");
		
		KeyStore ks = KeyStore.getInstance(keyStoreType);
		
		FileInputStream fis = new FileInputStream(SIGAReferences.getDirectoryReference(SIGAReferences.RESOURCE_FILES.PROPERTIES_DIR) + File.separator + keyStoreFile);
		ks.load(fis, keyStorePass.toCharArray());
		PrivateKey privateKey = (PrivateKey) ks.getKey(privateKeyAlias, privateKeyPass.toCharArray());

		XMLSignature sig = new XMLSignature(signedObject.getOwnerDocument(), null, signatureAlgorithm);
		signPlace.appendChild(sig.getElement());
		
		Transforms transforms = new Transforms(signedObject.getOwnerDocument());
		transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
		transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
		sig.addDocument("#"+signedObject.getAttribute("id"), transforms, Constants.ALGO_ID_DIGEST_SHA1);

		X509Certificate cert = (X509Certificate) ks.getCertificate(certificateAlias);
		sig.addKeyInfo(cert);
		sig.addKeyInfo(cert.getPublicKey());
		sig.sign(privateKey);
	}
	
}
