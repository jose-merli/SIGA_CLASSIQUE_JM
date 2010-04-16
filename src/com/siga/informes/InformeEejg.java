package com.siga.informes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.eejg.ScsEejgXmlAdm;
import com.siga.certificados.Plantilla;
import com.siga.general.SIGAException;


/**
 * Realiza un informe PDF mediante Xsl-FO de la informacion Económica de los ejg
 */

public class InformeEejg extends MasterReport 
{
	public InformeEejg(UsrBean usuario) {
		super(usuario);
	}

	public File generarInformeEejg (Map<Integer, Map<String, String>> mapInformes) throws ClsExceptions,SIGAException, UnsupportedEncodingException 
	{
		File file = null;
		List<File> file2zip = null;
		ScsEejgXmlAdm admXmlEejg = new ScsEejgXmlAdm(super.getUsuario());
		String nombreZip = null;
		List<String> lNumEjg = null; 
		String numEjg = null;
		String idInstitucion = getUsuario().getLocation();
		String fecha = null;
		
		for (Map.Entry<Integer, Map<String, String>> entrada:mapInformes.entrySet()){
			String xml = admXmlEejg.getEejgXml(entrada.getKey());
			Map<String, String> mapParameters = entrada.getValue();
			numEjg = mapParameters.get("numEjg") ;
			file = generarInformeEejg(xml,mapParameters);
			
			if(mapInformes.entrySet().size()>1){
				
				if(file2zip==null){
					fecha = UtilidadesBDAdm.getFechaCompletaBD("formato_ingles");
					fecha = fecha.replaceAll("/","");
					fecha = fecha.replaceAll(":","");
					fecha = fecha.replaceAll(" ","_");
					lNumEjg = new ArrayList<String>();
					file2zip = new ArrayList<File>();
					numEjg = UtilidadesString.replaceAllIgnoreCase(numEjg, "/", "-");
					nombreZip = "eejg"+ "_" + idInstitucion + "_" +numEjg + "_" + fecha;
					if(!lNumEjg.contains(numEjg)){
						lNumEjg.add(numEjg);
					}
					
					
				}
				file2zip.add(file);
			}
			
		}
		if(file2zip!=null){
			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String directorioEspecificoInforme = rp.returnProperty("sjcs.directorioPlantillaInformeEejg");
			String directorioSalida = rp.returnProperty("sjcs.directorioFisicoSalidaInformeEejg");
			if(lNumEjg.size()>1)
				nombreZip = "eejg"+ "_" + idInstitucion + "_"+ fecha;
			
			String rutaZip = directorioSalida    + directorioEspecificoInforme + ClsConstants.FILE_SEP + idInstitucion+ ClsConstants.FILE_SEP+nombreZip;
			File zip = MasterReport.doZip(file2zip, rutaZip);
			return zip;
		}else{
			return file;
			
		}
	
	}
	
	private File generarInformeEejg (String strXml,Map<String, String> mapParameters) throws ClsExceptions,SIGAException, UnsupportedEncodingException 
	{
//		InputStream inputXml = new ByteArrayInputStream(strXml.getBytes("ISO-8859-15"));
		InputStream inputXml = new ByteArrayInputStream(strXml.getBytes("ISO-8859-15"));
		return generarInformeEejg(inputXml,mapParameters);
	}
	/**
	 * 
	 * Metodo publico que llamaremos desde el Action encargado de generar el informe Eejg en pdf
	 *  
	 * @param request
	 * @param datosFormulario 
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	
	private File generarInformeEejg (InputStream inputXml,Map<String, String> mapParameters) throws ClsExceptions,SIGAException 
	{
		File fileFirmado = null;
		
		File rutaTmp=null;
		try {
			UsrBean usr = this.getUsuario();
			String idioma = mapParameters.get("idioma");
			String idInstitucion = usr.getLocation();
	
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
			String idiomaExt = idioma.substring(0,2).toUpperCase();
			String fecha = UtilidadesBDAdm.getFechaCompletaBD("formato_ingles");
			fecha = fecha.replaceAll("/","");
			fecha = fecha.replaceAll(":","");
			fecha = fecha.replaceAll(" ","_");
			
			String directorioPlantillas = rp.returnProperty("sjcs.directorioFisicoPlantillaInformeEejg");
			String directorioEspecificoInforme = rp.returnProperty("sjcs.directorioPlantillaInformeEejg");
			String directorioSalida = rp.returnProperty("sjcs.directorioFisicoSalidaInformeEejg");
			// Directorios y nombres de trabajo
			String plantillaNombre = "InformeEejg_"   + idiomaExt + ".xsl";
			String plantillaRuta   = directorioPlantillas + directorioEspecificoInforme + ClsConstants.FILE_SEP + idInstitucion;
			String numEjg = mapParameters.get("numEjg");
			String numEjgListado = UtilidadesString.replaceAllIgnoreCase(numEjg, "-", "/");
			mapParameters.put("numEjg", numEjgListado);
			
			String pdfNombre       = "eejg"+"_" + idInstitucion +"_"+numEjg+ "_"+mapParameters.get("idPersonaJG") + "_" + fecha+".pdf";
			String pdfRuta         = directorioSalida    + directorioEspecificoInforme + ClsConstants.FILE_SEP + idInstitucion;
			
			File rutaPDF = new File(pdfRuta);
			rutaPDF.mkdirs();
			if(!rutaPDF.exists()){
				throw new SIGAException("messages.envios.error.noPlantilla");					
			} 
			else {
				if(!rutaPDF.canWrite()){
					throw new SIGAException("messages.envios.error.noPlantilla");					
				}
			}
			pdfRuta += ClsConstants.FILE_SEP;
			
			File fileXsl = new File(plantillaRuta+ClsConstants.FILE_SEP+plantillaNombre);
			if(!fileXsl.exists()){
				throw new SIGAException("messages.informes.generico.noPlantilla");
			
			}
			
			File fPdf = new File(pdfRuta+pdfNombre+".tmp");
			
			fPdf = this.convertXML2PDF(inputXml,fileXsl,fPdf ,mapParameters);
			fileFirmado = firmarPDF(fPdf,pdfRuta+pdfNombre);
			if(fPdf.exists())
				fPdf.delete();
			
		}
		catch (SIGAException se) {
			throw se;
		}
		catch (ClsExceptions ex) {
			throw ex;
		}
		catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe: "+e.getLocalizedMessage());
		} 
		finally {
			if(rutaTmp!=null){
				Plantilla.borrarDirectorio(rutaTmp);
			}
		}
        return fileFirmado;
	}
	public File firmarPDF(File fileIn,String pathPdfFinal)throws Exception{
        FileInputStream fisID = null;
        FileOutputStream fos = null;
        String idInstitucion = this.getUsuario().getLocation();
        try
 		{
	    	GregorianCalendar gcFecha = new GregorianCalendar();
	        
            GenParametrosAdm admParametros = new GenParametrosAdm(this.getUsuario());
            String sPathCertificadosDigitales = admParametros.getValor(idInstitucion, "CER", "PATH_CERTIFICADOS_DIGITALES", "");
            String sNombreCertificadosDigitales = admParametros.getValor(idInstitucion, "CER", "NOMBRE_CERTIFICADOS_DIGITALES", "");
            String sClave = admParametros.getValor(idInstitucion, "CER", "CLAVE_CERTIFICADOS_DIGITALES", "");
            boolean tieneParametro = admParametros.tieneParametro(idInstitucion, "CER", "CLAVE_CERTIFICADOS_DIGITALES");
            String sIDDigital =""; 
            if (tieneParametro) {
                sIDDigital = sPathCertificadosDigitales + File.separator + idInstitucion + File.separator + sNombreCertificadosDigitales;
            } else {
                sIDDigital = sPathCertificadosDigitales + File.separator + sNombreCertificadosDigitales;
            }
            String sNombreFicheroEntrada = fileIn.getPath();
            
            String sNombreFicheroSalida = pathPdfFinal;
            File fOut = new File(sNombreFicheroSalida);
	        fisID = new FileInputStream(sIDDigital);
	        KeyStore ks = KeyStore.getInstance("PKCS12");
	        ks.load(fisID, sClave.toCharArray());

	        fisID.close();

	        
	        String sAlias = (String)ks.aliases().nextElement();
	
	        PrivateKey pKey = (PrivateKey)ks.getKey(sAlias, sClave.toCharArray());
	        
	        Certificate[] aCertificados = ks.getCertificateChain(sAlias);
	        
	        PdfReader reader = new PdfReader(fileIn.getPath());
	        
	        fos = new FileOutputStream(sNombreFicheroSalida);
	        
	        PdfStamper stamper = PdfStamper.createSignature(reader, fos, '\0');
	        PdfSignatureAppearance psa = stamper.getSignatureAppearance();
	        
	        psa.setCrypto(pKey, aCertificados, null, PdfSignatureAppearance.WINCER_SIGNED);
	        
	        psa.setSignDate(gcFecha);

	        stamper.close();
	        fos.close();
	        fileIn.delete();
	        return fOut;

 		}
 		
 		catch(Exception e)
 		{
 			ClsLogging.writeFileLog("***************** ERROR DE FIRMA DIGITAL EN DOCUMENTO *************************", 3);
 			ClsLogging.writeFileLog("Error al FIRMAR el PDF de Información económica de ejg: " + fileIn.getName() + " IdInstitucion: "+idInstitucion, 3);
 			ClsLogging.writeFileLog("*******************************************************************************", 3);
	        e.printStackTrace();
 			ClsLogging.writeFileLog("*******************************************************************************", 3);
 			throw e;
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