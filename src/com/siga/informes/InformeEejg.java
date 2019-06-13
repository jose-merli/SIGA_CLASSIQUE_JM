package com.siga.informes;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.FileHelper;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.eejg.ScsEejgXmlAdm;
import com.siga.certificados.Plantilla;
import com.siga.eejg.SolicitudesEEJG;
import com.siga.general.SIGAException;
import com.sis.firma.core.B64.Base64CODEC;


/**
 * Realiza un informe PDF mediante Xsl-FO de la informacion Econ�mica de los ejg
 */

public class InformeEejg extends MasterReport 
{
	public InformeEejg(UsrBean usuario) {
		super(usuario);
	}

	public File generarInformeEejg (Map<Integer, Map<String, String>> mapInformes) throws ClsExceptions,SIGAException, UnsupportedEncodingException {
		File file = null;
		List<File> file2zip = null;
		ScsEejgXmlAdm admXmlEejg = new ScsEejgXmlAdm(super.getUsuario());
		String nombreZip = null;
		List<String> lNumEjg = null; 
		String numEjg = null, csv = "";
		String idInstitucion = getUsuario().getLocation();
		String fecha = null;
		
		for (Map.Entry<Integer, Map<String, String>> entrada:mapInformes.entrySet()){
			//String xml = admXmlEejg.getEejgXml(entrada.getKey());
			Map<String, String> mapParameters = entrada.getValue();
			numEjg = mapParameters.get("numEjg") ;
			
			//AQUI SE CAMBIA LA LLAMADA A LA PLATAFORMA DE FIRMA (PFD)
			csv = mapParameters.get("csv");
			if(csv != null && !csv.equals("")){
				String contenidoPDF = null;
				//LLamamos al servico de EEJG para obtener el PDF a traves de la PFD
				
				SolicitudesEEJG solicitudesEEJG = new SolicitudesEEJG();
				contenidoPDF = solicitudesEEJG.getDocumentoTO(csv);
			
				
				file = generarInformeEejg(contenidoPDF,mapParameters);
			
			} else {
				//Mantenemos durante un tiempo la antigua descarga de PDF para las peticiones antiguias
				//si no viene csv lanzamos continuamos	
				continue; 
			}
			
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
	
	private File generarInformeEejg (String contenidoPDF,Map<String, String> mapParameters) throws ClsExceptions,SIGAException {
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
			
			String pdfNombre       = "eejg"+"_" + idInstitucion +"_"+numEjg+ "_"+mapParameters.get("nif") + "_" + fecha+".pdf";
			String pdfRuta         = directorioSalida    + directorioEspecificoInforme + ClsConstants.FILE_SEP + idInstitucion;
			
			File rutaPDF = new File(pdfRuta);
			FileHelper.mkdirs(pdfRuta);
			if(!rutaPDF.canWrite()){
				throw new SIGAException("messages.envios.error.noPlantilla");					
			}
			
			//Nos creamos el fichero PDF que se va a mostrar al usuario
			pdfRuta += ClsConstants.FILE_SEP;			
			fileFirmado = new File(pdfRuta+pdfNombre);			
			
			//Realizamos la decodificacion para su correcta visualizaci�n
			Base64CODEC.decodeToFile(contenidoPDF,pdfRuta+pdfNombre);
			
		} catch (SIGAException se) {
			throw se;

		} catch (ClsExceptions ex) {
			throw ex;
		
		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe: "+e.getLocalizedMessage());
		
		} finally {
			if(rutaTmp!=null){
				Plantilla.borrarDirectorio(rutaTmp);
			}
		}
		
        return fileFirmado;
	}	
	
	
}