package com.siga.informes;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ReadProperties;
import com.atos.utils.Row;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmLenguajesAdm;
import com.siga.beans.FacAbonoAdm;
import com.siga.beans.FacLineaAbonoAdm;
import com.siga.beans.GenParametrosAdm;
import com.siga.certificados.Plantilla;
import com.siga.general.SIGAException;

/**
 * Realiza un informe PDF mediante FOP del abono según plantilla introducida
 * @author RGG
 * @since 11/12/2007
 */
public class InformeAbono extends MasterReport {

	public InformeAbono() {
		super();
	}
	
	public InformeAbono(UsrBean usuario) {
		super(usuario);
	}
	/**
	 * Invoca varias clases para obtener los datos fijos de la factura 
	 * @param usuario Codigo de usuario conectado
	 * @param institucion Codigo institucion seleccionada
	 * @param idFactura Codigo factura
	 * @throws ClsExceptions Error interno
	 */	
	protected Hashtable cargarDatosFijos(UsrBean usuario,String institucion, String idAbono) throws ClsExceptions
	{
		Hashtable ht = new Hashtable();
		FacAbonoAdm aboAdm = new FacAbonoAdm(usuario);
		ht=aboAdm.getDatosImpresionInformeAbono(institucion,idAbono);
		return ht;
	}
	


	protected String reemplazarDatos(HttpServletRequest request, String plantillaFO) throws ClsExceptions{
		Hashtable htDatos= null;
				
		String plantilla=plantillaFO;
		
		HttpSession ses = request.getSession();
		String idAbono =  (String)request.getAttribute("IDABONO_INFORME");
		UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
		
		String institucion =usr.getLocation();
		
		FacLineaAbonoAdm lineaAbonoAdm = new FacLineaAbonoAdm(usr);
		
		//Cargar datos fijos
		htDatos=cargarDatosFijos(usr, institucion, idAbono);
		
		//Cargar listado de letrados en cola
		Vector vLineasAbono=lineaAbonoAdm.getLineasImpresionInforme(institucion,idAbono);
		plantilla = this.reemplazaRegistros(plantilla, vLineasAbono, null, "LINEAABONO");
		
		
		{	// Comprobamos si tenemos que mostar el texto de "exento de iva" (si hay algun registro con iva = 0.0%)
			UtilidadesHash.set(htDatos, "EXENTO_IVA", "");
			if (vLineasAbono != null) {
				for (int i = 0; i < vLineasAbono.size(); i++) {
					Hashtable h = ((Row)vLineasAbono.get(i)).getRow();
					Double d = new Double(UtilidadesHash.getString(h, "IVA_LINEA").replaceAll(",","."));
					if (d != null) {
						if (d.doubleValue() == 0.0d) {
							UtilidadesHash.set(htDatos, "EXENTO_IVA", UtilidadesString.getMensajeIdioma(usr.getLanguage(),"messages.factura.LIVA")); //"Operaciones exentas de IVA de acuerdo con lo establecido en el artículo 20 de la LIVA.";
							break;
						}
					}
				}
			}
		}
		
		plantilla = this.reemplazaVariables(htDatos, plantilla);
		return plantilla;
	}

	/**
	 * Metodo que implementa la generación de la factura en PDF
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @param  idAbono - 
	 * @return  File - fichero PDF generado  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public File generarAbono (HttpServletRequest request, String idioma, String institucion, String idAbono, String idInforme) throws ClsExceptions,SIGAException {

		String resultado="exito";
		File fPdf = null;
		
		
		
		ArrayList ficherosPDF= new ArrayList();
		File rutaFin=null;
		File rutaTmp=null;
		int numeroCarta=0;
			
		try {
		    HttpSession ses = request.getSession();
			UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN"); 
			
			GenParametrosAdm admParametros = new GenParametrosAdm(usr);
			ReadProperties rp = new ReadProperties("SIGA.properties");	
			
			// RGG 26/02/2007 cambio en los codigos de lenguajes
			AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
			String idiomaExt = a.getLenguajeExt(idioma);
			
			// necesario para que lo pueda obtener la otra funcion
			request.setAttribute("IDABONO_INFORME",idAbono);
	
			AdmInformeAdm admI = new AdmInformeAdm(usr);
			AdmInformeBean beanI = admI.obtenerInforme(institucion,idInforme);
			
			String rutaPlantilla = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
			rutaPlantilla += ClsConstants.FILE_SEP+institucion.toString()+ClsConstants.FILE_SEP+beanI.getDirectorio();
			
			String nombrePlantilla=beanI.getNombreFisico()+"_"+idiomaExt+".fo";
			
			// obtener ruta almacen
			String rutaAlmacen = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")+rp.returnProperty("informes.directorioPlantillaInformesJava");
    		rutaAlmacen += ClsConstants.FILE_SEP+usr.getLocation()+ClsConstants.FILE_SEP+beanI.getDirectorio();
			File rutaPDF=new File(rutaAlmacen);
			rutaPDF.mkdirs();
			if(!rutaPDF.canWrite()){
				throw new SIGAException("messages.informes.generico.noPlantilla");					
			}
			rutaAlmacen+=ClsConstants.FILE_SEP;
			String nombrePDF = beanI.getNombreSalida()+"_"+idAbono;

			// utilizamos la ruta de la plantilla para el temporal
			String rutaServidorTmp=rutaPlantilla+ClsConstants.FILE_SEP+"tmp_"+beanI.getNombreFisico()+"_"+System.currentTimeMillis();
			rutaTmp = new File(rutaServidorTmp);
			String contenidoPlantilla = this.obtenerContenidoPlantilla(rutaPlantilla,nombrePlantilla);
			
			fPdf = this.generarInforme(request,rutaServidorTmp,contenidoPlantilla,rutaAlmacen,nombrePDF);
			
		}catch (SIGAException se) {
			throw se;
		}catch (ClsExceptions ex) {
			throw ex;
		}catch (Exception e) {
			throw new ClsExceptions(e,"Error al generar el informe: "+e.getLocalizedMessage());
		} finally{
			if(rutaTmp!=null){
				Plantilla.borrarDirectorio(rutaTmp);
			}
		}
        return fPdf;
	}

}