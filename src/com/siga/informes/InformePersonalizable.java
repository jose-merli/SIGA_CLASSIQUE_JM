package com.siga.informes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.aspose.words.Document;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.beans.AdmConsultaInformeAdm;
import com.siga.beans.AdmConsultaInformeBean;
import com.siga.beans.AdmConsultaInformeConsultaBean;
import com.siga.beans.AdmInformeAdm;
import com.siga.beans.AdmInformeBean;
import com.siga.beans.AdmTipoFiltroInformeAdm;
import com.siga.beans.AdmTipoFiltroInformeBean;
import com.siga.beans.AdmTipoInformeAdm;
import com.siga.beans.AdmTipoInformeBean;
import com.siga.certificados.Plantilla;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.informes.MasterReport;

public class InformePersonalizable extends MasterReport
{
	// Informes personalizables
	public static final String I_CERTIFICADOPAGO = "CERPA";
	public static final String I_INFORMEFACTSJCS = "FACJ2";
	
	/**
	 * Invoca a la generacion de formulario personalizado pasandole los datos
	 * del formulario y el tipo
	 * @throws SIGAException 
	 */
	public String generarInformes(ActionMapping mapping,
								  ActionForm formulario,
								  HttpServletRequest request,
								  HttpServletResponse response,
								  String idtipoinforme,
								  ArrayList<HashMap<String, String>> filtrosInforme)
		throws ClsExceptions, SIGAException
	{
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		String idinstitucion = usr.getLocation();
		String salida = null;

		AdmInformeBean informe;
		ArrayList<File> listaFicheros;

		try {
			// obteniendo las plantillas por tipo
			AdmInformeAdm adm = new AdmInformeAdm(usr);
			Vector infs = adm.obtenerInformesTipo(idinstitucion, idtipoinforme,
					"N"/* aSolicitantes */, null);

			if (infs.size() == 0)
				throw new SIGAException("Error al buscar la plantilla del informe");
			
			// por cada plantilla, se genera el documento
			listaFicheros = new ArrayList<File>();
			for (Object inf : infs) {
				informe = (AdmInformeBean) inf;
				if (informe.getTipoformato().equalsIgnoreCase(AdmInformeBean.TIPOFORMATO_WORD))
					listaFicheros.addAll(this.generarInformeDOC(informe, filtrosInforme, request));
				else if (informe.getTipoformato().equalsIgnoreCase(AdmInformeBean.TIPOFORMATO_EXCEL))
					listaFicheros.addAll(this.generarInformeXLS(informe, filtrosInforme, request));
				else
					listaFicheros.addAll(this.generarInformeXLS(informe, filtrosInforme, request));
			}

			// haciendo zip con todos los ficheros devueltos
			if (listaFicheros.size() > 1) {
			    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
				AdmTipoInformeAdm admT = new AdmTipoInformeAdm(usr);
				AdmTipoInformeBean beanT = admT.obtenerTipoInforme(idtipoinforme);
				String rutaServidorDescargasZip = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")
						+ ClsConstants.FILE_SEP
						+ idinstitucion + ClsConstants.FILE_SEP
						+ "temp" + ClsConstants.FILE_SEP;
				String nombreFicheroZIP = beanT.getDescripcion().trim() + "_"
						+ idinstitucion + "_" + usr.getUserName() + "_"
						+ UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "");
				File ruta = new File(rutaServidorDescargasZip);
				ruta.mkdirs();
				Plantilla.doZip(rutaServidorDescargasZip, nombreFicheroZIP, listaFicheros);
				
				request.setAttribute("rutaFichero", rutaServidorDescargasZip + nombreFicheroZIP + ".zip");
			}
			else if (listaFicheros.size() == 1) {
				request.setAttribute("rutaFichero", listaFicheros.get(0).getAbsolutePath());
			}

			// devolviendo el fichero generado
			MasterForm miform = (MasterForm) formulario;
			miform.setRutaFichero((String) request.getAttribute("rutaFichero"));
			request.setAttribute("borrarFichero", "true");
			request.setAttribute("generacionOK", "OK");
			salida = "descarga";
			
		} catch (Exception e) {
			throw new SIGAException(e, new String [] {"Error al generar el informe"});
		}
		
		return salida;
	}
	
	/**
	 * Genera un informe
	 * 
	 * @param informe
	 * @param filtrosInforme
	 * @param request
	 * @return File (fichero con el informe generado)
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public ArrayList<File> generarInformeDOC(AdmInformeBean informe,
								  ArrayList<HashMap<String, String>> filtrosInforme,
								  HttpServletRequest request)
		throws ClsExceptions, SIGAException
	{
		// Variables
		AdmConsultaInformeConsultaBean consulta;
		String sentencia;
		Vector datos;
		
		// Controles generales
		UserTransaction tx;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		// obteniendo la plantilla
		String idinstitucionInforme = informe.getIdInstitucion().toString();
		String idinstitucion = usr.getLocation();
		String idiomainforme = usr.getLanguageExt();
		ReadProperties rp = new ReadProperties(
				SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaPl = rp.returnProperty("informes.directorioFisicoPlantillaInformesJava")
				+ ClsConstants.FILE_SEP
				+ informe.getDirectorio() + ClsConstants.FILE_SEP
				+ (idinstitucionInforme.equals("0") ? "2000" : idinstitucionInforme) + ClsConstants.FILE_SEP;
		String nombrePlantilla = informe.getNombreFisico() + "_"
				+ idiomainforme + ".doc";
		String rutaAlm = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")
				+ ClsConstants.FILE_SEP
				+ informe.getDirectorio() + ClsConstants.FILE_SEP
				+ (idinstitucionInforme.equals("0") ? "2000" : idinstitucionInforme) + ClsConstants.FILE_SEP;

		// creando el documento de salida
		MasterWords words = new MasterWords(rutaPl + nombrePlantilla);
		File plantilla = new File(rutaPl + nombrePlantilla);
		if (!plantilla.exists()) // si no existe la plantilla, generar un Excel
			return this.generarInformeXLS(informe, filtrosInforme, request);
		Document doc = words.nuevoDocumento();
		File crear = new File(rutaAlm);
		if (!crear.exists())
			crear.mkdirs();
		ArrayList<File> listaFicheros = new ArrayList<File> ();

		// obteniendo los tipos de filtros obligatorios
		AdmTipoFiltroInformeAdm tipoFiltroAdm = new AdmTipoFiltroInformeAdm(usr);
		Hashtable<String, String> tipoFiltroHash = new Hashtable<String, String>();
		tipoFiltroHash = new Hashtable<String, String>();
		tipoFiltroHash.put(AdmTipoFiltroInformeBean.C_IDTIPOINFORME, informe.getIdTipoInforme());
		tipoFiltroHash.put(AdmTipoFiltroInformeBean.C_OBLIGATORIO, ClsConstants.DB_TRUE);
		Vector tiposFiltro = tipoFiltroAdm.select(tipoFiltroHash);
		
		// comprobando que los filtros obligatorios estan en la lista de filtros del informe
		for (HashMap<String, String> filtro : filtrosInforme)
			filtro.put(AdmTipoFiltroInformeBean.C_OBLIGATORIO, ClsConstants.DB_FALSE);
		
		AdmTipoFiltroInformeBean tipoFiltro;
		String nombreCampo;
		boolean encontrado;
		for (Iterator iterTiposFiltros = tiposFiltro.iterator(); iterTiposFiltros.hasNext();) {
			tipoFiltro = (AdmTipoFiltroInformeBean) iterTiposFiltros.next();
			nombreCampo = (String) tipoFiltro.getNombreCampo();
			
			encontrado = false;
			for (HashMap<String, String> filtro : filtrosInforme) {
				if (filtro.get(AdmTipoFiltroInformeBean.C_NOMBRECAMPO).equals(nombreCampo)) {
					filtro.put(AdmTipoFiltroInformeBean.C_OBLIGATORIO, ClsConstants.DB_TRUE);
					encontrado = true;
					break;
				}
			}
			if (! encontrado)
				throw new SIGAException(
						"Problema en la configuracion del informe: No estan configurados todos los tipos de filtros obligatorios");
			
		}

		// obteniendo las consultas del informe
		AdmConsultaInformeAdm consultaAdm = new AdmConsultaInformeAdm(usr);
		Hashtable<String, String> consultaHash = new Hashtable<String, String>();
		consultaHash = new Hashtable<String, String>();
		consultaHash.put(AdmConsultaInformeBean.C_IDINSTITUCION, informe.getIdInstitucion().toString());
		consultaHash.put(AdmConsultaInformeBean.C_IDPLANTILLA, informe.getIdPlantilla());
		Vector<AdmConsultaInformeConsultaBean> consultas = consultaAdm.selectConsultas(consultaHash);

		for (Iterator<AdmConsultaInformeConsultaBean> iterConsultas = consultas.iterator(); iterConsultas.hasNext(); ) {
			consulta = iterConsultas.next();

			// sustituyendo los filtros en la consulta por los datos del informe
			if ((sentencia = consultaAdm.sustituirFiltrosConsulta(consulta, filtrosInforme)) == null)
				// falla si la consulta no tiene los filtros obligatorios
				throw new ClsExceptions(
						"Problema en la configuracion del informe: La consulta no lleva los filtros obligatorios o lleva de mas");

			// ejecutando la consulta
			tx = usr.getTransactionLigera();
			try {
				tx.begin();

				/** @TODO Convendria que cambiar a selectBind */
				datos = consultaAdm.selectGenerico(sentencia);

				tx.commit();
			} catch (Exception sqle) {
				String mensaje = sqle.getMessage();
				if (mensaje.indexOf("TimedOutException") != -1
						|| mensaje.indexOf("timed out") != -1) {
					throw new SIGAException("messages.transaccion.timeout", sqle);
				} else if (sqle.toString().indexOf("ORA-") != -1) {
					throw new SIGAException("messages.general.sql", sqle, new String[] { sqle.toString() });
				} else {
					throw new SIGAException(
							"Problema en la configuracion del informe: No funciona la consulta",
							sqle, new String[] { sqle.toString() });
				}
			}

			if (datos != null && datos.size() > 0) {
				// sustituyendo los valores en el fichero
				if (consulta.getVariasLineas().equals(ClsConstants.DB_TRUE))
					doc = words.sustituyeRegionDocumento(doc, consulta.getNombre(), datos);
				else
					doc = words.sustituyeDocumento(doc, (Hashtable<String, Object>) datos.get(0));
			}
		}
		
		// sustituyendo la descripcion del informe en el fichero
		Hashtable<String, Object> descripcionInforme = new Hashtable<String, Object>();
		descripcionInforme.put("DESCRIPCION_INFORME", informe.getDescripcion());
		doc = words.sustituyeDocumento(doc, descripcionInforme);

		// obteniendo el fichero de salida:
		/**
		 * @TODO Habria que obtener un identificador unico: de momento se genera
		 *       el numero de sesion, que vale si el mismo usuario no ejecuta
		 *       informes a la vez (por ejemplo en dos navegadores).
		 *       Ademas, estaria bien dar un numero unico pero que sea algo
		 *       descriptivo ¿?
		 */
		String nombreFichero = informe.getNombreSalida() + "_" + idinstitucion + "_" + usr.getUserName() + "_"
				+ UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "")
				+ ".doc";
		File ficheroGenerado = words.grabaDocumento(doc, rutaAlm, nombreFichero);

		// devolviendo el fichero
		listaFicheros.add(ficheroGenerado);
		return listaFicheros;
		
	} // generarInformeDOC()
	
	public ArrayList<File> generarInformeXLS(AdmInformeBean informe,
								  ArrayList<HashMap<String, String>> filtrosInforme,
								  HttpServletRequest request)
		throws ClsExceptions, SIGAException
	{
		// Variables
		AdmConsultaInformeConsultaBean consulta;
		String sentencia;
		Vector<Vector<Hashtable<String, String>>> datos;
		
		// Controles generales
		UserTransaction tx = null;
		UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
		// obteniendo ruta de almacenamiento
		String idinstitucionInforme = informe.getIdInstitucion().toString();
		String idinstitucion = usr.getLocation();
		String idiomainforme = usr.getLanguageExt();
		ReadProperties rp = new ReadProperties(
				SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlm = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")
				+ ClsConstants.FILE_SEP
				+ informe.getDirectorio() + ClsConstants.FILE_SEP
				+ (idinstitucionInforme.equals("0") ? "2000" : idinstitucionInforme) + ClsConstants.FILE_SEP;

		// obteniendo los tipos de filtros obligatorios
		AdmTipoFiltroInformeAdm tipoFiltroAdm = new AdmTipoFiltroInformeAdm(usr);
		Hashtable<String, String> tipoFiltroHash = new Hashtable<String, String>();
		tipoFiltroHash = new Hashtable<String, String>();
		tipoFiltroHash.put(AdmTipoFiltroInformeBean.C_IDTIPOINFORME, informe.getIdTipoInforme());
		tipoFiltroHash.put(AdmTipoFiltroInformeBean.C_OBLIGATORIO, ClsConstants.DB_TRUE);
		Vector tiposFiltro = tipoFiltroAdm.select(tipoFiltroHash);
		
		// comprobando que los filtros obligatorios estan en la lista de filtros del informe
		for (HashMap<String, String> filtro : filtrosInforme)
			filtro.put(AdmTipoFiltroInformeBean.C_OBLIGATORIO, ClsConstants.DB_FALSE);
		
		AdmTipoFiltroInformeBean tipoFiltro;
		String nombreCampo;
		boolean encontrado;
		for (Iterator iterTiposFiltros = tiposFiltro.iterator(); iterTiposFiltros.hasNext();) {
			tipoFiltro = (AdmTipoFiltroInformeBean) iterTiposFiltros.next();
			nombreCampo = (String) tipoFiltro.getNombreCampo();
			
			encontrado = false;
			for (HashMap<String, String> filtro : filtrosInforme) {
				if (filtro.get(AdmTipoFiltroInformeBean.C_NOMBRECAMPO).equals(nombreCampo)) {
					filtro.put(AdmTipoFiltroInformeBean.C_OBLIGATORIO, ClsConstants.DB_TRUE);
					encontrado = true;
					break;
				}
			}
			if (! encontrado)
				throw new SIGAException(
						"Problema en la configuracion del informe: No estan configurados todos los tipos de filtros obligatorios");
			
		}

		// obteniendo las consultas del informe
		AdmConsultaInformeAdm consultaAdm = new AdmConsultaInformeAdm(usr);
		Hashtable<String, String> consultaHash = new Hashtable<String, String>();
		consultaHash = new Hashtable<String, String>();
		consultaHash.put(AdmConsultaInformeBean.C_IDINSTITUCION, informe.getIdInstitucion().toString());
		consultaHash.put(AdmConsultaInformeBean.C_IDPLANTILLA, informe.getIdPlantilla());
		Vector<AdmConsultaInformeConsultaBean> consultas = consultaAdm.selectConsultas(consultaHash);

		// creando la ruta de salida
		File crear = new File(rutaAlm);
		if (!crear.exists())
			crear.mkdirs();
		ArrayList<File> listaFicheros = new ArrayList<File> ();

		// variables para los ficheros de salida
		/**
		 * @TODO Habria que obtener un identificador unico: de momento se genera
		 *       el numero de usuario, que vale si el mismo usuario no ejecuta
		 *       informes a la vez (por ejemplo en dos navegadores).
		 *       Ademas, estaria bien dar un numero unico pero que sea algo
		 *       descriptivo ¿?
		 */
		String nombreFichero;
		File ficheroGenerado = null;
		BufferedWriter out;
		
		for (Iterator<AdmConsultaInformeConsultaBean> iterConsultas = consultas.iterator(); iterConsultas.hasNext(); ) {
			consulta = iterConsultas.next();

			// sustituyendo los filtros en la consulta por los datos del informe
			if ((sentencia = consultaAdm.sustituirFiltrosConsulta(consulta, filtrosInforme)) == null)
				// falla si la consulta no tiene los filtros obligatorios
				throw new SIGAException(
						"Problema en la configuracion del informe: La consulta no lleva los filtros obligatorios o lleva de mas");

			// ejecutando la consulta
			tx = usr.getTransactionLigera();
			try {
				tx.begin();

				/** @TODO Convendria que cambiar a selectBind */
				datos = consultaAdm.selectCamposOrdenados(sentencia);

				tx.commit();
			} catch (Exception sqle) {
				String mensaje = sqle.getMessage();
				if (mensaje.indexOf("TimedOutException") != -1
						|| mensaje.indexOf("timed out") != -1) {
					throw new SIGAException("messages.transaccion.timeout", sqle);
				} else if (sqle.toString().indexOf("ORA-") != -1) {
					throw new SIGAException("messages.general.sql", sqle, new String[] { sqle.toString() });
				} else {
					throw new SIGAException(
							"Problema en la configuracion del informe: No funciona la consulta",
							sqle, new String[] { sqle.toString() });
				}
			}

			try {
				if (datos != null && datos.size() > 0) {
					// creando fichero para cada consulta
					nombreFichero = informe.getNombreSalida() + "_" + idinstitucion + "_" + usr.getUserName() + "_"
							+ UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "") + '_'
							+ consulta.getNombre() + ".xls";
					ficheroGenerado = new File (rutaAlm + ClsConstants.FILE_SEP + nombreFichero);
					if (ficheroGenerado.exists())
						ficheroGenerado.delete();
					ficheroGenerado.createNewFile();
					out = new BufferedWriter(new FileWriter(ficheroGenerado));
					
					// escribiendo las cabeceras
					Vector<Hashtable<String, String>> hashOrdenado = datos.get(0);
					for (int i = 0; i < hashOrdenado.size(); i++) {
						out.write(hashOrdenado.get(i).keys().nextElement() + "\t");
					}
					out.newLine();
					
					// escribiendo los resultados
					for (Iterator<Vector<Hashtable<String, String>>> iter = datos.iterator(); iter.hasNext(); ) {
						hashOrdenado = iter.next();
						for (int i = 0; i < hashOrdenado.size(); i++) {
							out.write(hashOrdenado.get(i).elements().nextElement() + "\t");
						}
						out.newLine();
					}
					
					// cerrando el fichero
					out.close();
					listaFicheros.add(ficheroGenerado);
				}
			} catch (Exception sqle) {
				throw new SIGAException(
						"Problema en la generacion del fichero Excel",
						sqle, new String[] { sqle.toString() });
			}
		}

		// devolviendo el fichero
		return listaFicheros;
		
	} // generarInformeXLS()

}
