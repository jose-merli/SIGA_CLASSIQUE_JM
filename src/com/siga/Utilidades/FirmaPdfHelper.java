/**
 * 
 */
package com.siga.Utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.GregorianCalendar;

import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.autogen.model.GenParametros;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;

import com.atos.utils.ClsLogging;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;

import es.satec.businessManager.BusinessManager;

/**
 * @author jorgeta 
 * @date   31/05/2016
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class FirmaPdfHelper {

	
	private static KeyStore getKeyStore(String keyStoreType, String certificadoDigitalPath, String pwdCertificadoDigital) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
    	KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    	InputStream inputStream = new FileInputStream(certificadoDigitalPath);
    	keyStore.load(inputStream, pwdCertificadoDigital.toCharArray());
    	if(inputStream!=null){
    		inputStream.close();
    		inputStream = null;
    	}
    	return keyStore;
    	
    }
    
   
    
    public static boolean firmarPDF(Short idInstitucion, String nombreFicheroEntrada) throws Exception
 	{
        FileInputStream fisID = null;
        FileOutputStream fos = null;
        try
 		{
	    	GregorianCalendar gcFecha = new GregorianCalendar();
            
            GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
        	GenParametros genParametros = new GenParametros();
    		genParametros.setModulo(MODULO.CER.name());
    		genParametros.setParametro(PARAMETRO.PATH_CERTIFICADOS_DIGITALES.name());
    		genParametros.setIdinstitucion(idInstitucion);
        	
    		GenParametros certificadoDigitalDirectorio = genParametrosService.getGenParametroInstitucionORvalor0(genParametros);
    		genParametros.setParametro(PARAMETRO.NOMBRE_CERTIFICADOS_DIGITALES.name());
    		GenParametros certificadoDigitalNombre =  genParametrosService.getGenParametroInstitucionORvalor0(genParametros);
    		genParametros.setParametro(PARAMETRO.CLAVE_CERTIFICADOS_DIGITALES.name());
    		GenParametros pwdCertificadoDigital =  genParametrosService.getGenParametroInstitucionORvalor0(genParametros);
    		
    		StringBuilder certificadoDigitalPath = new StringBuilder();
        	certificadoDigitalPath.append(certificadoDigitalDirectorio.getValor());
        	certificadoDigitalPath.append(File.separator);
        	certificadoDigitalPath.append(certificadoDigitalNombre.getValor());
    		
        	KeyStore keyStore = getKeyStore("PKCS12",certificadoDigitalPath.toString(),pwdCertificadoDigital.getValor());
            
            String sNombreFicheroSalida = nombreFicheroEntrada + ".tmp";
            File fOut = new File(sNombreFicheroSalida);
            File fIn = new File(nombreFicheroEntrada);
	        
	        String sAlias = (String)keyStore.aliases().nextElement();
	        PrivateKey pKey = (PrivateKey)keyStore.getKey(sAlias, pwdCertificadoDigital.getValor().toCharArray());
	        Certificate[] aCertificados = keyStore.getCertificateChain(sAlias);
	        
	        PdfReader reader = new PdfReader(nombreFicheroEntrada);
	        
	        fos = new FileOutputStream(sNombreFicheroSalida);
	        
	        PdfStamper stamper = PdfStamper.createSignature(reader, fos, '\0');
	        PdfSignatureAppearance psa = stamper.getSignatureAppearance();
	        
	        psa.setCrypto(pKey, aCertificados, null, PdfSignatureAppearance.WINCER_SIGNED);
	        psa.setSignDate(gcFecha);

	        stamper.close();
	        fos.close();
	        fIn.delete();
	        fOut.renameTo(fIn);

 			return true;
 		}
 		
 		catch(Exception e)
 		{
 			ClsLogging.writeFileLog("***************** ERROR DE FIRMA DIGITAL EN DOCUMENTO *************************", 3);
 			ClsLogging.writeFileLog("Error al FIRMAR el PDF: " + nombreFicheroEntrada + " IdInstitucion: "+idInstitucion, 3);
 			ClsLogging.writeFileLog("Error al FIRMAR el PDF: " +e.getMessage(), 3);
 			ClsLogging.writeFileLog("*******************************************************************************", 3);
//	        e.printStackTrace();
 			ClsLogging.writeFileLog("*******************************************************************************", 3);
 			return false;
 		}
 		finally {
 			try {
 				if(fos!=null)fos.close();
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 			try {
 				if(fisID!=null)	fisID.close();
 			} catch (Exception e) {
 				e.printStackTrace();
 			}

			
 		}
 	}
 	
	
}
