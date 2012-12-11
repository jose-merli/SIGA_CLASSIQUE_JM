package com.siga.informes;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import weblogic.apache.xml.serialize.OutputFormat;
import weblogic.apache.xml.serialize.XMLSerializer;
import weblogic.xml.dom.DocumentImpl;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.FacFacturaAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.certificados.Plantilla;
import com.siga.general.SIGAException;


/**
 * Realiza un informe PDF mediante FOP de las facturas emitidas según plantilla introducida
 */

public class InformeFacturasEmitidas extends MasterReport 
{
	public InformeFacturasEmitidas(UsrBean usuario) {
		super(usuario);
	}

	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO, Hashtable datosBase) throws ClsExceptions, SIGAException 
	{
		String institucion = this.getUsuario().getLocation();
		String fDesde = UtilidadesHash.getString(datosBase, "FECHA_DESDE");
		String fHasta = UtilidadesHash.getString(datosBase, "FECHA_HASTA");
		
		// Colocamos los registros
		FacFacturaAdm adm = new FacFacturaAdm(this.getUsuario());
		Hashtable auxFac = adm.getFacturasEmitidas(institucion, fDesde, fHasta);
		ClsLogging.writeFileLog("INFORMEFACTURAS: Despues de ejecutar la consulta Facturas Emitidas",10);
		Vector vDatos = (Vector)auxFac.get("REGISTROS");
		if (vDatos != null){
			ClsLogging.writeFileLog("INFORMEFACTURAS: ANTES DE REEMPLAZAR VARIABLE REGISTROS",10);
			plantillaFO = this.reemplazaRegistros(plantillaFO, vDatos, null, "FACTURA_EMITIDA");
		}	
		ClsLogging.writeFileLog("INFORMEFACTURAS: DESPUES DE REEMPLAZAR VARIABLE REGISTROS",10);
		// Colocamos los totales
		Vector vTotalesXiva = (Vector)auxFac.get("TOTALES_X_IVA");
		if (vTotalesXiva != null){
			ClsLogging.writeFileLog("INFORMEFACTURAS: ANTES DE REEMPLAZAR VARIABLE TOTALES_X_IVA",10);
			plantillaFO = this.reemplazaRegistros(plantillaFO, vTotalesXiva, null, "TOTAL_POR_IVA");
		}	
		ClsLogging.writeFileLog("INFORMEFACTURAS: DESPUES DE REEMPLAZAR VARIABLE TOTALES_X_IVA",10);
		// Colocamos datos fijos (cabeceras, etc)
		CenInstitucionAdm admInstitucion= new CenInstitucionAdm (this.getUsuario());
		Hashtable hDatosFijos = admInstitucion.getDatosInformeFacturasEmitidas(institucion);
		ClsLogging.writeFileLog("INFORMEFACTURAS: Despues de ejecutar la consulta de Datos Informe Facturas Emitidas",10);
		UtilidadesHash.set(hDatosFijos, "FECHA_DESDE", fDesde);
		UtilidadesHash.set(hDatosFijos, "FECHA_HASTA", fHasta);
		if (vDatos != null && vDatos.size() > 0) {
			Hashtable aux = (Hashtable)vDatos.get(vDatos.size()-1);
			UtilidadesHash.set(hDatosFijos, "TOTAL_IVA", UtilidadesHash.getString(aux, "TOTAL_ACUMULADO_IVA"));
			UtilidadesHash.set(hDatosFijos, "TOTAL_TOTAL_FACTURA", UtilidadesHash.getString(aux, "TOTAL_ACUMULADO_TOTAL_FACTURA"));
			UtilidadesHash.set(hDatosFijos, "TOTAL_BASE_IMPONIBLE", UtilidadesHash.getString(aux, "TOTAL_ACUMULADO_BASE_IMPONIBLE"));
		}
		ClsLogging.writeFileLog("NFORMEFACTURAS:Antes de reemplazar las variables del informe",10);
		plantillaFO = this.reemplazaVariables(hDatosFijos, plantillaFO);
		ClsLogging.writeFileLog("NFORMEFACTURAS:Despues de reemplazar las variables del informe",10);

		return plantillaFO;
	}	
	
	public File generarListadoFacturasEmitidasOld (HttpServletRequest request, Hashtable datos) throws ClsExceptions,SIGAException 
	{
		String resultado="exito";
		File fPdf = null;
		
		ArrayList ficherosPDF= new ArrayList();
		File rutaFin=null;
		File rutaTmp=null;
		int numeroCarta=0;
			
		try {
			UsrBean usr = this.getUsuario();
			String idioma = usr.getLanguage();
			String idInstitucion = usr.getLocation();
			
			GenParametrosAdm admParametros = new GenParametrosAdm(usr);
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			
			// RGG 26/02/2007 cambio en los codigos de lenguajes
			AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
			String idiomaExt = a.getLenguajeExt(idioma);
			
			// YYYY/MM/DD HH24:MI:SS
			String fecha = UtilidadesBDAdm.getFechaCompletaBD("formato_ingles");
			fecha = fecha.replaceAll("/","");
			fecha = fecha.replaceAll(":","");
			fecha = fecha.replaceAll(" ","_");
			
			String directorioPlatillas         = rp.returnProperty("facturacion.directorioFisicoPlantillaListadoFacturaEmitidasJava"),
				   directorioEspecificoInforme = rp.returnProperty("facturacion.directorioPlantillaListadoFacturaEmitidaJava"),
				   directorioSalida            = rp.returnProperty("facturacion.directorioFisicoSalidaListadoFacturaEmitidasJava");

			// Directorios y nombres de trabajo
			String plantillaNombre = "facturaEmitida_"   + idiomaExt + ".fo";
			String plantillaRuta   = directorioPlatillas + directorioEspecificoInforme + ClsConstants.FILE_SEP + idInstitucion;
			String pdfNombre       = "listFactEmitidas"  + "_" + idInstitucion + "_" + usr.getUserName() + "_" + fecha;
			String pdfRuta         = directorioSalida    + directorioEspecificoInforme + ClsConstants.FILE_SEP + idInstitucion;

			// obtener ruta almacen
			File rutaPDF = new File(pdfRuta);
			rutaPDF.mkdirs();
			if(!rutaPDF.exists()){
				throw new SIGAException("messages.facturacion.comprueba.noPathFacturas");					
			} 
			else {
				if(!rutaPDF.canWrite()){
					throw new SIGAException("messages.facturacion.comprueba.noPermisosPathFacturas");					
				}
			}
			pdfRuta += ClsConstants.FILE_SEP;

			String contenidoPlantilla = this.obtenerContenidoPlantilla(plantillaRuta,plantillaNombre);

			fPdf = this.generarInforme(request, datos, pdfRuta, contenidoPlantilla, pdfRuta, pdfNombre);
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
        return fPdf;
	}	
	
	/**
	 * 
	 * Metodo publico que llamaremos desde el Action encargado de generar el informe de facturas emitidas en pdf
	 *  
	 * @param request
	 * @param datosFormulario 
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	
	public File generarListadoFacturasEmitidasPdfFromXmlToFoXsl (HttpServletRequest request, Hashtable datosFormulario) throws ClsExceptions,SIGAException 
	{
	
		File fPdf = null;
		
	
		File rutaTmp=null;
	
			
		try {
			UsrBean usr = this.getUsuario();
			String idioma = usr.getLanguage();
			String idInstitucion = usr.getLocation();
			
	
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			
			// RGG 26/02/2007 cambio en los codigos de lenguajes
			AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
			String idiomaExt = a.getLenguajeExt(idioma);
			
			// YYYY/MM/DD HH24:MI:SS
			String fecha = UtilidadesBDAdm.getFechaCompletaBD("formato_ingles");
			fecha = fecha.replaceAll("/","");
			fecha = fecha.replaceAll(":","");
			fecha = fecha.replaceAll(" ","_");
			
			String directorioPlatillas         = rp.returnProperty("facturacion.directorioFisicoPlantillaListadoFacturaEmitidasJava"),
				   directorioEspecificoInforme = rp.returnProperty("facturacion.directorioPlantillaListadoFacturaEmitidaJava"),
				   directorioSalida            = rp.returnProperty("facturacion.directorioFisicoSalidaListadoFacturaEmitidasJava");

			// Directorios y nombres de trabajo
			String plantillaNombre = "facturaEmitida_"   + idiomaExt + ".xsl";
			String plantillaRuta   = directorioPlatillas + directorioEspecificoInforme + ClsConstants.FILE_SEP + idInstitucion;
			String pdfNombre       = "listFactEmitidas"  + "_" + idInstitucion + "_" + usr.getUserName() + "_" + fecha+".pdf";
			String pdfRuta         = directorioSalida    + directorioEspecificoInforme + ClsConstants.FILE_SEP + idInstitucion;
			
			String pathXml = plantillaRuta+ClsConstants.FILE_SEP+"facturaEmitida_"   + idiomaExt + ".xml";
			
			
			
			// obtener ruta almacen
			File rutaPDF = new File(pdfRuta);
			rutaPDF.mkdirs();
			if(!rutaPDF.exists()){
				throw new SIGAException("messages.facturacion.comprueba.noPathFacturas");					
			} 
			else {
				if(!rutaPDF.canWrite()){
					throw new SIGAException("messages.facturacion.comprueba.noPermisosPathFacturas");					
				}
			}
			pdfRuta += ClsConstants.FILE_SEP;
			
			//File fileXml = getXmlFacturasEmitidas(idInstitucion, fechaDesde, fechaHasta, pathXml)
			
			
			
			fPdf = this.generarInformePdfFromXmlToFoXsl(pathXml,plantillaRuta,plantillaNombre,pdfRuta,pdfNombre, datosFormulario);
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
        return fPdf;
	}
	public File generarComunicacionOficioPdfFromXmlToFoXsl (AdmInformeBean beanInforme,String identificador) throws ClsExceptions,SIGAException 
	{
	
		File fPdf = null;
		
	
		File rutaTmp=null;
	
			
		try {
			UsrBean usr = this.getUsuario();
			String idioma = usr.getLanguage();
			String idInstitucion = usr.getLocation();
			
	
		    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
//			ReadProperties rp = new ReadProperties("SIGA.properties");	
			
			// RGG 26/02/2007 cambio en los codigos de lenguajes
			AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
			String idiomaExt = a.getLenguajeExt(idioma);
			
			// YYYY/MM/DD HH24:MI:SS
			String fecha = UtilidadesBDAdm.getFechaCompletaBD("formato_ingles");
			fecha = fecha.replaceAll("/","");
			fecha = fecha.replaceAll(":","");
			fecha = fecha.replaceAll(" ","_");
			
			String carpetaInstitucion = "";
			if (beanInforme.getIdInstitucion() == null
					|| beanInforme.getIdInstitucion().compareTo(Integer.valueOf(0)) == 0) {
				carpetaInstitucion = "2000";
			} else {
				carpetaInstitucion = "" + beanInforme.getIdInstitucion();
			}
			
			String directorioPlatillas         = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava"),
				   directorioEspecificoInforme = rp.returnProperty("informes.directorioPlantillaInformesJava"),
				   directorioSalida            = rp.returnProperty("informes.directorioFisicoSalidaInformesJava");

			// Directorios y nombres de trabajo
			String plantillaNombre = beanInforme.getNombreFisico() + "_"+ idiomaExt + ".xsl";
			
			String plantillaRuta   = directorioPlatillas + directorioEspecificoInforme + ClsConstants.FILE_SEP + carpetaInstitucion+ClsConstants.FILE_SEP+beanInforme.getDirectorio();
			

			String pdfRuta         = directorioSalida    + directorioEspecificoInforme + ClsConstants.FILE_SEP + idInstitucion;
			
			String pathXml = plantillaRuta+ClsConstants.FILE_SEP+"identificador_"   + idiomaExt + ".xml";
			
			identificador = identificador + ".pdf";
			String pdfNombre = beanInforme.getNombreSalida() + "_"	+ identificador;
			
			// obtener ruta almacen
			File rutaPDF = new File(pdfRuta);
			rutaPDF.mkdirs();
			if(!rutaPDF.exists()){
				throw new SIGAException("messages.facturacion.comprueba.noPathFacturas");					
			} 
			else {
				if(!rutaPDF.canWrite()){
					throw new SIGAException("messages.facturacion.comprueba.noPermisosPathFacturas");					
				}
			}
			pdfRuta += ClsConstants.FILE_SEP;
			
			
			
			
			fPdf = this.generarInformePdfFromXmlToFoXsl(pathXml,plantillaRuta,plantillaNombre,pdfRuta,pdfNombre, null);
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
        return fPdf;
	}
	
	/**
	 * Metodo que sobreescribe al método homonimo del MasterReport
	 * 
	 * @param pathXml Es el path donde se va a crear el fichero que va a ser la fuente de datos 
	 * @param datosFormulario Hash con los valores del formulario necesario para el acceso a BBDD
	 * @return el metodo genera el fichero xml que, aplicandole la un xsl generara un pdf
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	
	public File getXmlFileToReportWithXslFo (String pathXml, Hashtable datosFormulario) throws ClsExceptions, SIGAException 
	{
		
		
		File file = null;
		
		
		try {
			String idInstitucion = this.getUsuario().getLocation();
			String fDesde = UtilidadesHash.getString(datosFormulario, "FECHA_DESDE");
			String fHasta = UtilidadesHash.getString(datosFormulario, "FECHA_HASTA");
			
//			String idInstitucion = "2040";
//			String fDesde = "11/01/2011";
//			String fHasta = "11/01/2011";
//			
			
			//Nos traemos las filas del informe 
			FacFacturaAdm adm = new FacFacturaAdm(this.getUsuario());
			RowsContainer rc = adm.getRowsFacturasEmitidas(idInstitucion, fDesde, fHasta);
			//Accedemos a los datos de cabecera.(Empresa y fecha de generacion institucion )
			CenInstitucionAdm admInstitucion= new CenInstitucionAdm (this.getUsuario());
			Hashtable hDatosFijos = admInstitucion.getDatosInformeFacturasEmitidas(idInstitucion);
			String institucion = UtilidadesHash.getString(hDatosFijos, "EMPRESA");
			String fechaGeneracion = UtilidadesHash.getString(hDatosFijos, "FECHA_GENERACION");
			
            	double totalAcumuladoIVA = 0.0d,
					  totalAcumuladoTotalFactura = 0.0d,
					  totalAcumuladoBaseImponible = 0.0d;


            	Document xmlDoc = new DocumentImpl();
            	
        		Element root = xmlDoc.createElement("Report");
        		Element empresa = xmlDoc.createElement("empresa");
        		empresa.appendChild(xmlDoc.createTextNode(institucion));
        		Element fecha = xmlDoc.createElement("fechaGeneracion");
        		fecha.appendChild(xmlDoc.createTextNode(fechaGeneracion));
        		Element eFechaDesde = xmlDoc.createElement("fechaDesde");
        		eFechaDesde.appendChild(xmlDoc.createTextNode(fDesde));
        		Element eFechaHasta = xmlDoc.createElement("fechaHasta");
        		eFechaHasta.appendChild(xmlDoc.createTextNode(fHasta));
        		
        		root.appendChild(empresa);
        		root.appendChild(fecha);
        		root.appendChild(eFechaDesde);
        		root.appendChild(eFechaHasta);

        		
        		TreeMap tmTotalesIva = new TreeMap();
            	for (int i = 0; i < rc.size(); i++){
            		Hashtable fila = ((Row)rc.get(i)).getRow();
            		Element lineaFactura= getLineaFacturaInformeFacturasEmitidas(fila, xmlDoc);
            		root.appendChild(lineaFactura);
            		
            		
            		
            		// Calculo de total x iva
            		Float porcentajeIVA  = UtilidadesHash.getFloat(fila, "IVA_PORCENTAJE");
            		Hashtable htTotalesIva = (Hashtable) tmTotalesIva.get(porcentajeIVA);
            		htTotalesIva = almacenaDesglosesIva(htTotalesIva, porcentajeIVA.floatValue(),
            								   UtilidadesHash.getFloat(fila, "BASE_IMPONIBLE").floatValue(),
            				                   UtilidadesHash.getFloat(fila, "IVA").floatValue(), 
											   UtilidadesHash.getFloat(fila, "TOTAL_FACTURA").floatValue());
            		tmTotalesIva.put(porcentajeIVA, htTotalesIva);
            		
            		// Calculo de total acumulado
            		totalAcumuladoIVA           += UtilidadesHash.getFloat(fila, "IVA").floatValue();
            		totalAcumuladoTotalFactura  += UtilidadesHash.getFloat(fila, "TOTAL_FACTURA").floatValue();
            		totalAcumuladoBaseImponible += UtilidadesHash.getFloat(fila, "BASE_IMPONIBLE").floatValue();
            		
            		// Formato numeros
            		UtilidadesHash.set(fila, "IVA", UtilidadesNumero.formato(UtilidadesHash.getFloat(fila, "IVA").floatValue()));
            		UtilidadesHash.set(fila, "IVA_PORCENTAJE", UtilidadesNumero.formato(UtilidadesHash.getFloat(fila, "IVA_PORCENTAJE").floatValue()));
            		UtilidadesHash.set(fila, "TOTAL_FACTURA", UtilidadesNumero.formato(UtilidadesHash.getFloat(fila, "TOTAL_FACTURA").floatValue()));
            		UtilidadesHash.set(fila, "BASE_IMPONIBLE", UtilidadesNumero.formato(UtilidadesHash.getFloat(fila, "BASE_IMPONIBLE").floatValue()));

            		
            		
            		
            	}
            	//Añadimos al fichero Xml los elemnetos de desglose de Iva
            	Iterator iteIva = tmTotalesIva.keySet().iterator();
            	Element desgloseIvaE = null;
            	//Miramo se es el primer registro para poner el titulo
            	String textoDesgloseIva = UtilidadesString.getMensajeIdioma(this.getUsuario(), "facturacion.facturas.emitidas.desgloseiva");
            	while (iteIva.hasNext()) {
					Float keyPorcentajeIva = (Float) iteIva.next();
					Hashtable htTotalIva = (Hashtable)tmTotalesIva.get(keyPorcentajeIva);
					desgloseIvaE = xmlDoc.createElement("desgloseIva");
					
				    //desgloseIva.addContent(new Element("textoDesgloseIva").setText(textoDesgloseIva));
					Element textoDesglose = xmlDoc.createElement("textoDesgloseIva");
					textoDesglose.appendChild(xmlDoc.createTextNode(textoDesgloseIva));
					
				    //solo queremos que ponga el titulo en el primero
				    textoDesgloseIva = "";
					
				    Element ivaBaseImponibleE = xmlDoc.createElement("ivaBaseImponible");
				    ivaBaseImponibleE.appendChild(xmlDoc.createTextNode(UtilidadesNumero.formato(UtilidadesHash.getString(htTotalIva, "POR_IVA_BASE_IMPONIBLE"))));
				    Element ivaPorcE = xmlDoc.createElement("ivaPorc");
				    ivaPorcE.appendChild(xmlDoc.createTextNode(UtilidadesNumero.formato(UtilidadesHash.getString(htTotalIva, "POR_IVA_IVA_PORCENTAJE"))));
				    Element ivaCuotaE = xmlDoc.createElement("ivaCuota");
				    ivaCuotaE.appendChild(xmlDoc.createTextNode(UtilidadesNumero.formato(UtilidadesHash.getString(htTotalIva, "POR_IVA_IVA"))));
					Element ivaImporteTotalE = xmlDoc.createElement("ivaImporteTotal");
					ivaImporteTotalE.appendChild(xmlDoc.createTextNode(UtilidadesNumero.formato(UtilidadesHash.getString(htTotalIva, "POR_IVA_TOTAL_FACTURA"))));
					
					desgloseIvaE.appendChild(ivaBaseImponibleE);
					desgloseIvaE.appendChild(ivaPorcE);
					desgloseIvaE.appendChild(ivaCuotaE);
					desgloseIvaE.appendChild(ivaImporteTotalE);
					desgloseIvaE.appendChild(textoDesglose);
					
					root.appendChild(desgloseIvaE);
					
				}
            	
            	
            	//Añadimos los elementos totales
        		Element totalBaseImponibleE = xmlDoc.createElement("totalBaseImponible");
			    totalBaseImponibleE.appendChild(xmlDoc.createTextNode(UtilidadesNumero.formato(totalAcumuladoBaseImponible)));
			    Element totalCuotaIvaE = xmlDoc.createElement("totalCuotaIva");
			    totalCuotaIvaE.appendChild(xmlDoc.createTextNode(UtilidadesNumero.formato(totalAcumuladoIVA)));
			    Element totalImporteFacturasE = xmlDoc.createElement("totalImporteFacturas");
			    totalImporteFacturasE.appendChild(xmlDoc.createTextNode(UtilidadesNumero.formato(totalAcumuladoTotalFactura)));
        		
        		
        		root.appendChild(totalBaseImponibleE);
        		root.appendChild(totalCuotaIvaE);
        		root.appendChild(totalImporteFacturasE);
        		
            	
            	          	   
         	    //Vamos a almacenarlo en un fichero y ademas lo sacaremos por pantalla          	
         	     xmlDoc.appendChild(root);
         	     file = new File(pathXml);
         	     FileOutputStream fos = new FileOutputStream(file);
         	     // XERCES 1 or 2 additionnal classes.
         	     OutputFormat of = new OutputFormat("XML","ISO-8859-1",true);
         	     of.setIndent(1);
         	     of.setIndenting(true);
         	     XMLSerializer serializer = new XMLSerializer(fos,of);
         	     serializer.asDOMSerializer();
         	     serializer.serialize( xmlDoc.getDocumentElement() );
         	     fos.close();

               
             
       }catch (SIGAException e) {
       		throw e;
       }
		
       catch (Exception e) {
       		throw new ClsExceptions (e, "InformeFacturasEmitidas.getXmlFileToReportWithXslFo.Error al ejecutar consulta.");
       }
       return file;
    }
	/**
	 * 
	 * @param registro Hashtable con los datos de cada una delas filas del informe
	 * @param porcentajeIVA Porcentaje de Iva a almacenar en el hash registro
	 * @param baseImponible Base imponible a almacenar en el hash registro
	 * @param iva Iva a almacenar en el hash registro
	 * @param totalFactura Importe totald de la factura a almacenar en el hash registro
	 * @return
	 */
	
	private Hashtable almacenaDesglosesIva (Hashtable registro, float porcentajeIVA, float baseImponible, float iva, float totalFactura) 
	{
		try {
			if (registro == null) {
				registro = new Hashtable();
				UtilidadesHash.set(registro, "POR_IVA_IVA_PORCENTAJE", new Double(porcentajeIVA));
			}
			
			double aux = 0.0d;
	
			try {	
				Double d = UtilidadesHash.getDouble(registro, "POR_IVA_BASE_IMPONIBLE");
				aux = d.doubleValue(); 
			} 
			catch (Exception e) { 
				aux = 0.0d; 
			}
			UtilidadesHash.set(registro, "POR_IVA_BASE_IMPONIBLE", new Double(aux + baseImponible));
			
			try {	
				Double d = UtilidadesHash.getDouble(registro, "POR_IVA_IVA");
				aux = d.doubleValue(); 
			} 
			catch (Exception e) { 
				aux = 0.0d; 
			}
			UtilidadesHash.set(registro, "POR_IVA_IVA", new Double(aux + iva));

			try {	
				Double d = UtilidadesHash.getDouble(registro, "POR_IVA_TOTAL_FACTURA");
				aux = d.doubleValue(); 
			} 
			catch (Exception e) { 
				aux = 0.0d; 
			}
			UtilidadesHash.set(registro, "POR_IVA_TOTAL_FACTURA", new Double(aux + totalFactura));
			
			return registro;
		}
		catch (Exception e) {
			return registro;
		}
	}
	/**
	 * Metodo que nos genera el elemento del fichero xml de cada factura a imprimir
	 * @param registro Hashtable con los datos de la factura que hay que almacenar en el Elemento del fichero xml
	 * @return
	 */
	
	private Element getLineaFacturaInformeFacturasEmitidas(Hashtable registro, Document doc){
		
		Element lineaFactura = doc.createElement("lineaFactura");

		Element facturaNumero = doc.createElement("facturaNumero");
		Element facturaFecha = doc.createElement("facturaFecha");
		Element facturaSubcuenta = doc.createElement("facturaSubcuenta");
		Element facturaNif = doc.createElement("facturaNif");
		Element facturaNombre = doc.createElement("facturaNombre");
		Element facturaObservaciones = doc.createElement("facturaObservaciones");
		Element facturaBaseImponible = doc.createElement("facturaBaseImponible");
		Element facturaPorcIva = doc.createElement("facturaPorcIva");
		Element facturaCuotaIva = doc.createElement("facturaCuotaIva");
		Element facturaImporteTotal = doc.createElement("facturaImporteTotal");
		
		facturaNumero.appendChild(doc.createTextNode(UtilidadesHash.getString(registro, "NUMERO_FACTURA")));
		facturaFecha.appendChild(doc.createTextNode(UtilidadesHash.getString(registro, "FECHA")));
		facturaSubcuenta.appendChild(doc.createTextNode(UtilidadesHash.getString(registro, "SUBCUENTA")));
		facturaNif.appendChild(doc.createTextNode(UtilidadesHash.getString(registro, "NIF")));
		facturaNombre.appendChild(doc.createTextNode(UtilidadesHash.getString(registro, "NOMBRE")));
		facturaObservaciones.appendChild(doc.createTextNode(UtilidadesHash.getString(registro, "OBSERVACIONES")));
		facturaBaseImponible.appendChild(doc.createTextNode(UtilidadesNumero.formato(UtilidadesHash.getDouble(registro, "BASE_IMPONIBLE").doubleValue())));
		facturaPorcIva.appendChild(doc.createTextNode(UtilidadesNumero.formato(UtilidadesHash.getDouble(registro, "IVA_PORCENTAJE").doubleValue())));
		facturaCuotaIva.appendChild(doc.createTextNode(UtilidadesNumero.formato(UtilidadesHash.getDouble(registro, "IVA").doubleValue())));
		facturaImporteTotal.appendChild(doc.createTextNode(UtilidadesNumero.formato(UtilidadesHash.getDouble(registro, "TOTAL_FACTURA").doubleValue())));
		
		lineaFactura.appendChild(facturaNumero);
		lineaFactura.appendChild(facturaFecha);
		lineaFactura.appendChild(facturaSubcuenta);
		lineaFactura.appendChild(facturaNif);
		lineaFactura.appendChild(facturaNombre);
		lineaFactura.appendChild(facturaObservaciones);
		lineaFactura.appendChild(facturaBaseImponible);
		lineaFactura.appendChild(facturaPorcIva);
		lineaFactura.appendChild(facturaCuotaIva);
		lineaFactura.appendChild(facturaImporteTotal);
		
		return lineaFactura;
			
	}  
	
	
   
    
	
}