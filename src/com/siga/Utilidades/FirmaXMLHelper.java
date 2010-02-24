package com.siga.Utilidades;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.IdResolver;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FirmaXMLHelper {
	private String xmlSigNSPrefix;
	private String keyStoreType;
	private String keyStoreFile;
	private String keyStorePass;
	private String privateKeyAlias;
	private String privateKeyPass;	
	private String certificateAlias;	
	private static PrivateKey privateKey;
	private static X509Certificate cert;
	
	public FirmaXMLHelper(String xmlSigNSPrefix, String keyStoreType,
			String keyStoreFile, String keyStorePass, String privateKeyAlias,
			String privateKeyPass, String certificateAlias) {
		super();
		this.xmlSigNSPrefix = xmlSigNSPrefix;
		this.keyStoreType = keyStoreType;
		this.keyStoreFile = keyStoreFile;
		this.keyStorePass = keyStorePass;
		this.privateKeyAlias = privateKeyAlias;
		this.privateKeyPass = privateKeyPass;
		this.certificateAlias = certificateAlias;
	}	
	
	static {
		try {
			org.apache.xml.security.Init.init();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean verificarXML(Document entrada) throws Exception {
		Element nscontext = createDSctx(entrada, this.xmlSigNSPrefix, Constants.SignatureSpecNS);		
		Element sigElement = (Element) XPathAPI.selectSingleNode(entrada, "//"+this.xmlSigNSPrefix+":Signature", nscontext);
		XMLSignature signature = new XMLSignature(sigElement, "");

        KeyInfo ki = signature.getKeyInfo();
		if (ki != null) {
			X509Certificate cert = ki.getX509Certificate();
			if (cert != null) {
				return signature.checkSignatureValue(cert);
			} else {
				PublicKey pk = ki.getPublicKey();
				if (pk != null) {
					return signature.checkSignatureValue(pk);
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
	
    private Element createDSctx(Document doc, String prefix, String namespace) {

		if ((prefix == null) || (prefix.trim().length() == 0)) {
			throw new IllegalArgumentException("You must supply a prefix");
		}

		Element ctx = doc.createElementNS(null, "namespaceContext");
		ctx.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:" + prefix.trim(),	namespace);

		return ctx;
	}
    
	public void firmaCabecera(Element signedObject, Element signPlace) throws Exception {
		if (privateKey==null || cert==null){
			KeyStore ks = KeyStore.getInstance(keyStoreType);
			InputStream is = FirmaXMLHelper.class.getResourceAsStream(keyStoreFile);
			ks.load(is, keyStorePass.toCharArray());
			privateKey = (PrivateKey) ks.getKey(privateKeyAlias, privateKeyPass.toCharArray());
			cert = (X509Certificate) ks.getCertificate(certificateAlias);
		}
		
		Constants.setSignatureSpecNSprefix(xmlSigNSPrefix);
		IdResolver.registerElementById(signedObject, "id");		

		XMLSignature sig = new XMLSignature(signedObject.getOwnerDocument(), null, XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1);
		signPlace.appendChild(sig.getElement());

		Transforms transforms = new Transforms(signedObject.getOwnerDocument());
		transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
		sig.addDocument("#"+signedObject.getAttribute("id"), transforms, Constants.ALGO_ID_DIGEST_SHA1);

		sig.addKeyInfo(cert);
		sig.addKeyInfo(cert.getPublicKey());
		sig.sign(privateKey);
	}
}
