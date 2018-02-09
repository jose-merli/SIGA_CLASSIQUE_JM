package com.siga.certificados;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CerPlantillasAdm;
import com.siga.beans.CerSolicitudCertificadosAdm;
import com.siga.beans.CerSolicitudCertificadosBean;
import com.siga.general.SIGAException;
import com.siga.informes.InformeCertificadosEspeciales;
import com.siga.informes.MasterReport;
import com.siga.informes.MasterWords;

public class Certificado {
	public static final String CERT_TIPO_CURSO = "NOMBRECURSO";
	public static final String CERT_TIPO_SEMINARIOS_CURSOS = "SEMINARIOS_CURSOS";
	public static final String CERT_TIPO_TURNO = "TURNOS";
	public static final String CERT_TIPO_PASANTIA = "LETRADOPASANTIAS";
	public static final String CERT_TIPO_PASANTIA2 = "PASANTIAS";
	public static final String CERT_TIPO_DESPACHO1 = "HISTORICO_DESPACHOS";
	public static final String CERT_TIPO_DESPACHO2 = "HISTORICO_DESPACHOS";
	public static final String CERT_TIPO_BANCO = "BANCOS";
	public static final String CERT_TIPO_DIRECCION = "HISTORICO_DIRECCIONES";
	public static final String CERT_TIPO_COMPONENTES = "COMPONENTES";
	public static final String INI = "INI_";

	public static void generarCertificadoPDF(String idTipoProducto, String idProducto, String idProductoInstitucion, String idInstitucion,
			String idPlantilla, String idPersona, File fIn, File fOut, String sBaseDir, String idSolicitud, String idInstitucionOrigen, boolean usarIdInstitucion, UsrBean usr) throws SIGAException {
		try {
			CerPlantillasAdm admPlantilla = new CerPlantillasAdm(usr);
			File fPlantilla = admPlantilla.obtenerPlantilla(String.valueOf(idInstitucion), idTipoProducto, idProducto, idProductoInstitucion, idPlantilla);

			Plantilla plantilla = new Plantilla(fPlantilla, usr);
			CenClienteAdm admCliente = new CenClienteAdm(usr);
			// Hashtable htDatos = admCliente.getDatosCertificado(new
			// Long(idPersona), new Integer(idInstitucion));
			Hashtable htParametros = new Hashtable();
			// formamos la consulta de datos
			htParametros.put("@idpersona@", "" + idPersona);
			// RGG Se obtiene siempre de la institucion origen si existe, a no
			// ser que nos digan que la cojamos del cgae.
			if (usarIdInstitucion) {
				htParametros.put("@idinstitucion@", "" + idInstitucion);
			} else {
				htParametros.put("@idinstitucion@", "" + idInstitucionOrigen);
			}
			htParametros.put("@idsolicitud@", "" + idSolicitud);
			htParametros.put("@idioma@", "" + usr.getLanguage());
			String[] nombreFicheroStrings = fPlantilla.getName().split("\\.");

			String extension = "";
			if (nombreFicheroStrings != null && nombreFicheroStrings.length > 1) {
				extension = nombreFicheroStrings[1];
			}

			String certificadoMultiRegistro = getEtiquetaMultiRegistro(fPlantilla);

			if (certificadoMultiRegistro != null && !certificadoMultiRegistro.equals("")) {
				htParametros.put("TIPOCERTIFICADO", certificadoMultiRegistro);
				htParametros.put("IDPLANTILLA", idPlantilla);
				if (extension.equalsIgnoreCase("doc")) {
					Vector<File> salidaFiles = new Vector<File>();
					InformeCertificadosEspeciales informe = new InformeCertificadosEspeciales(usr);
					Hashtable<String, Object> hDatosAWHashtable = informe.getDatosParaAsposeWords(htParametros);
					Vector datosInVector = new Vector();
					datosInVector.add(hDatosAWHashtable);
					MasterWords masterWords = new MasterWords(fPlantilla.getPath(), datosInVector);
					masterWords.generarInformePdfPorIdioma(fOut.getName(), fOut.getParent(), salidaFiles, usr.getLanguageInstitucion());

				} else {

					InformeCertificadosEspeciales informe = new InformeCertificadosEspeciales(usr);
					informe.generarListadoCertificados(usr, htParametros, fOut.getParent(), fOut.getName(), fPlantilla.getParent(), fPlantilla.getName(), sBaseDir);
				}
			} else {
				if (extension.equalsIgnoreCase("doc")) {
					Hashtable htDatos = admCliente.getDatosCertificado(htParametros, idInstitucion,false);
					htDatos = admPlantilla.darFormatoCampos(String.valueOf(idInstitucion), idTipoProducto, idProducto, idProductoInstitucion, idPlantilla, usr.getLanguageInstitucion(), htDatos);
					Vector datosInVector = new Vector();
					datosInVector.add(htDatos);
					Vector<File> salidaFiles = new Vector<File>();
					MasterWords masterWords = new MasterWords(fPlantilla.getPath(), datosInVector);
					masterWords.generarInformePdfPorIdioma(fOut.getName(), fOut.getParent(), salidaFiles, usr.getLanguageInstitucion());

				} else {
					// datos certificado en plantilla
					Hashtable htDatos = admCliente.getDatosCertificado(htParametros, idInstitucion,true);
					htDatos = admPlantilla.darFormatoCampos(String.valueOf(idInstitucion), idTipoProducto, idProducto, idProductoInstitucion, idPlantilla, usr.getLanguageInstitucion(), htDatos);
					plantilla.sustituirEtiquetas(htDatos, fIn);

					// proceso de generacion
					MasterReport masterReport = new MasterReport();
					masterReport.convertFO2PDF(fIn, fOut, sBaseDir);
				}

			}

		} catch (SIGAException e) {
			throw e;
		} catch (Exception e) {
			throw new SIGAException("Error al generar el PDF del Certificado.", e);
		}
	}
	
	private static String getEtiquetaMultiRegistro(File fichero) throws ClsExceptions, SIGAException {
		String etiquetasMultiregistro = null;
		try {

			String[] etiquetas = { Certificado.CERT_TIPO_CURSO, Certificado.CERT_TIPO_PASANTIA, Certificado.CERT_TIPO_TURNO, Certificado.CERT_TIPO_DESPACHO1, Certificado.CERT_TIPO_DESPACHO2, Certificado.CERT_TIPO_BANCO, Certificado.CERT_TIPO_DIRECCION, Certificado.CERT_TIPO_COMPONENTES, Certificado.CERT_TIPO_SEMINARIOS_CURSOS, Certificado.CERT_TIPO_PASANTIA2 };
			// Leo el fichero y lo paso a un string
			// Reader rArchivo = new FileReader(this.archivo);
			BufferedReader rArchivo = null;

			try {
				rArchivo = new BufferedReader(new InputStreamReader(new FileInputStream(fichero),"ISO-8859-15"));
			} catch (Exception e) {
				throw new SIGAException("messages.envios.error.noPlantilla");
			}
			if (!fichero.exists()) {
				throw new SIGAException("messages.envios.error.noPlantilla");
			}
			if (!fichero.canRead()) {
				throw new ClsExceptions("Error de lectura del fichero de la plantilla: " + fichero.getAbsolutePath());
			}

			StringBuffer sb = new StringBuffer();

			char[] b = new char[1000];
			int n;

			// Read a block. If it gets any chars, append them.
			while ((n = rArchivo.read(b)) > 0) {
				sb.append(b, 0, n);
			}

			rArchivo.close();

			String sArchivo = sb.toString();
			boolean findIt = false;
			for (int i = 0; i < etiquetas.length && !findIt; i++) {
				if (sArchivo.indexOf(Certificado.INI + etiquetas[i]) >= 0) {
					etiquetasMultiregistro = etiquetas[i];
					findIt = true;
				}
			}

		} catch (Exception e) {
			throw new SIGAException("Error al leer la plantilla del Certificado.", e);
		}
		return etiquetasMultiregistro;
	}
	
	/**
	 * Generar zip cuando hay varios ficheros de certificados
	 * @param rutaServidorDescargasZip
	 * @param nombreFichero
	 * @param ficherosPDF
	 * @throws ClsExceptions
	 * @throws SIGAException 
	 */
	public void doZip(String rutaServidorDescargasZip, String nombreFichero, ArrayList<File> ficherosPDF,CerSolicitudCertificadosBean solicitudCertificadoBean,CerSolicitudCertificadosAdm admSolicitud) throws ClsExceptions, SIGAException	{
		// Generar Zip
		File ficZip=null;
		byte[] buffer = new byte[8192];
		int leidos;
		ZipOutputStream outTemp = null;
		
		try {
		    ClsLogging.writeFileLog("DESCARGA DE CERTIFICADOS: numero de certificados = "+ficherosPDF.size(),10);

			if ((ficherosPDF!=null) && (ficherosPDF.size()>0)) {
				
				ficZip = new File(rutaServidorDescargasZip +  nombreFichero + ".zip");

				// RGG 
				if (ficZip.exists()) {
				    ficZip.delete();
				    ClsLogging.writeFileLog("DESCARGA DE CERTIFICADOS: el fichero zip ya existia. Se elimina",10);
				}
				
				outTemp = new ZipOutputStream(new FileOutputStream(ficZip));
				
				for (int i=0; i<ficherosPDF.size(); i++)
				{

				    File auxFile = (File)ficherosPDF.get(i);
				    ClsLogging.writeFileLog("DESCARGA DE CERTIFICADOS: fichero numero "+i+" longitud="+auxFile.length(),10);
					if (auxFile.exists()) {
						ZipEntry ze = new ZipEntry(admSolicitud.getNombreVariosFicheroSalida(solicitudCertificadoBean,auxFile.getName()));
						outTemp.putNextEntry(ze);
						FileInputStream fis=new FileInputStream(auxFile);
						
						buffer = new byte[8192];
						
						while ((leidos = fis.read(buffer, 0, buffer.length)) > 0)
						{
							outTemp.write(buffer, 0, leidos);
						}
						
						fis.close();
						outTemp.closeEntry();
					}
				}
			    ClsLogging.writeFileLog("DESCARGA DE CERTIFICADOS: ok ",10);
				
				outTemp.close();

			}
		} catch (FileNotFoundException e) {
			throw new ClsExceptions(e,"Error al crear fichero zip");
		} catch (IOException e) {
			throw new ClsExceptions(e,"Error al crear fichero zip");
		}
		finally {
			try {
				outTemp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}