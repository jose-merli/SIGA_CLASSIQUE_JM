package com.siga.informes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.exceptions.BusinessException;
import org.redabogacia.sigaservices.app.helper.ExcelHelper;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.aspose.words.Document;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.FileHelper;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.administracion.form.InformeForm;
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
import com.siga.general.SIGAException;
import com.siga.ws.InformeXML;

public class InformePersonalizable extends MasterReport
{
	// Informes personalizables
	public static final String I_CERTIFICADOPAGO = "CERPA";
	public static final String I_INFORMEFACTSJCS = "FACJ2";
		
	private String idFacturacion = null;
	private boolean eliminarFichero = false;
	private String tipoFicheroCAM;
	
	public File getFicheroGenerado(UsrBean usr,
								  String idtipoinforme,Vector informesList,
								  ArrayList<HashMap<String, String>> filtrosInforme)
		throws ClsExceptions, SIGAException
	{
		String idinstitucion = usr.getLocation();

		AdmInformeBean informe;
		ArrayList<File> listaFicheros;
		
		try {
			// obteniendo las plantillas por tipo
			AdmInformeAdm adm = new AdmInformeAdm(usr);
			if(informesList==null)
				informesList = adm.obtenerInformesTipo(idinstitucion, idtipoinforme,
						"N"/* aSolicitantes */, null);

			if (informesList.size() == 0)
				throw new SIGAException("Error al buscar la plantilla del informe");
			AdmTipoInformeAdm admT = new AdmTipoInformeAdm(usr);
			AdmTipoInformeBean tipoInformeBean = admT.obtenerTipoInforme(AdmTipoInformeBean.TIPOINFORME_CONSULTAS);
			
			// por cada plantilla, se genera el documento
			listaFicheros = new ArrayList<File>();
			for (Object inf : informesList) {
				informe = (AdmInformeBean) inf;
				if (informe.getTipoformato().equalsIgnoreCase(AdmInformeBean.TIPOFORMATO_WORD)){
					try {
						listaFicheros.addAll(this.generarInformeDOC(informe, filtrosInforme, usr));	
					} catch (SIGAException e) {
						if(e.getLiteral().equals("noExistePlantilla")){
							listaFicheros.addAll(InformePersonalizable.generarInformeXLS(informe, filtrosInforme,null, usr));
						}else
							throw e; 
							
					}
					
				} else if (informe.getTipoformato().equalsIgnoreCase(AdmInformeBean.TIPOFORMATO_EXCEL)) {
					listaFicheros.addAll(InformePersonalizable.generarInformeXLS(informe, filtrosInforme,null, usr));
				} else if (informe.getTipoformato().equalsIgnoreCase(AdmInformeBean.TIPOFORMATO_XML)) {
//					llamada
					String claseJava = informe.getClaseJava();
					if (claseJava == null || claseJava.trim().equals("")) {
						throw new IllegalArgumentException("Si se selecciona informe en formato XML se debe configurar la clase java que genera el XML.");
					}
					Class clazz = Class.forName(claseJava);
					Object obj = clazz.newInstance();
					if (obj instanceof InformeXML) {
						InformeXML informeXML = (InformeXML)obj;
						informeXML.setTipoFicheroCAM(this.tipoFicheroCAM);
						listaFicheros.addAll(informeXML.execute(informe.getDirectorio(), informe.getNombreSalida(), idinstitucion, idFacturacion, usr));
						setEliminarFichero(true);
					}else if (obj instanceof AragonEnviaJustificacionActuaciones) {
						AragonEnviaJustificacionActuaciones informeXML = (AragonEnviaJustificacionActuaciones)obj;
						List<File> files = getFicherosInformeXMLAragon(informeXML, informe, filtrosInforme, usr);
						listaFicheros.addAll(files);
						setEliminarFichero(true);
					} else  {
						throw new IllegalArgumentException("La clase java debe extender de " + InformeXML.class.getName());
					}
					
				} else {
					listaFicheros.addAll(InformePersonalizable.generarInformeXLS(informe, filtrosInforme,null, usr));
				}
			}
			File ficheroSalida = getFicheroSalida(listaFicheros, tipoInformeBean, usr);
			return ficheroSalida;
			// haciendo zip con todos los ficheros devueltos
			
		}
		catch (BusinessException e) {
			throw new SIGAException(e.getMessage());
		}
		catch (SIGAException e) {
			throw e;
		}catch (Exception e) {
//			e.printStackTrace();
			throw new SIGAException(e, new String [] {"Error al generar el informe"});
		}
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
	private ArrayList<File> generarInformeDOC(AdmInformeBean informe,
								  ArrayList<HashMap<String, String>> filtrosInforme,
								  UsrBean usr)
		throws ClsExceptions, SIGAException
	{
		// Variables
		AdmConsultaInformeConsultaBean consulta;
		String sentencia;
		Vector datos;
		
		// Controles generales
		//UserTransaction tx;
		
		//UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");
		
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
			throw new SIGAException("noExistePlantilla");
		Document doc = words.nuevoDocumento();
		FileHelper.mkdirs(rutaAlm);
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
				throw new SIGAException("informes.personalizable.error.configuracion.sinFiltros");
			
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
				
				throw new SIGAException("informes.personalizable.error.configuracion.filtros");

			// ejecutando la consulta
			//tx = usr.getTransactionLigera();
			try {
				//tx.begin();

				/** @TODO Convendria que cambiar a selectBind */
				datos = consultaAdm.selectGenerico(sentencia);

				//tx.commit();
			} catch (Exception sqle) {
				String mensaje = sqle.getMessage();
				if (mensaje.indexOf("TimedOutException") != -1
						|| mensaje.indexOf("timed out") != -1) {
					throw new SIGAException("messages.transaccion.timeout");
				} else if (sqle.toString().indexOf("ORA-") != -1) {
					throw new SIGAException("informes.personalizable.error.query", sqle, new String[] {"\""+consulta.getDescripcion()+"\"" });
				} else {
					throw new SIGAException("informes.personalizable.error.query.indeterminado");
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
		 *       descriptivo �?
		 */
		String nombreFichero = informe.getNombreSalida() + "_" + idinstitucion + "_" + usr.getUserName() + "_"
				+ UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "");
				
		if(informe.getTipoformato()!=null && informe.getTipoformato().equals("P"))
			nombreFichero= nombreFichero + ".pdf";
		else
			nombreFichero = nombreFichero + ".doc";
		
		File ficheroGenerado = words.grabaDocumento(doc, rutaAlm, nombreFichero);

		// devolviendo el fichero
		listaFicheros.add(ficheroGenerado);
		return listaFicheros;
		
	} // generarInformeDOC()
	
	public List<File> getFicherosInformeXMLAragon(AragonEnviaJustificacionActuaciones informeXML,AdmInformeBean informe,  ArrayList<HashMap<String, String>> filtrosInforme, UsrBean usr) throws BusinessException	{
		AdmConsultaInformeAdm consultaAdm = new AdmConsultaInformeAdm(usr);
		Hashtable<String, String> consultaHash = new Hashtable<String, String>();
		consultaHash = new Hashtable<String, String>();
		consultaHash.put(AdmConsultaInformeBean.C_IDINSTITUCION, informe.getIdInstitucion().toString());
		consultaHash.put(AdmConsultaInformeBean.C_IDPLANTILLA, informe.getIdPlantilla());
		List <File> returnList = new ArrayList<File>();
		
		try {
			Vector<AdmConsultaInformeConsultaBean> consultas = consultaAdm.selectConsultas(consultaHash);
			if(consultas!=null && consultas.size()>0) {
				// creando la ruta de salida
				Vector datos = null;
				for (int i = 0; i < consultas.size(); i++) {
					AdmConsultaInformeConsultaBean consulta = consultas.get(i);
					// sustituyendo los filtros en la consulta por los datos del informe
					String sentencia = consultaAdm.sustituirFiltrosConsulta(consulta, filtrosInforme); 
					if ( sentencia == null)		// falla si la consulta no tiene los filtros obligatorios 
						throw new SIGAException("informes.personalizable.error.configuracion.filtros");

					datos = consultaAdm.getCamposOrdenados(sentencia);
					if(datos!=null && datos.size()>0) {
						
						Hashtable primerRegistro = (Hashtable) datos.get(0); 
						List<String> columnasExcel = null;
						if(primerRegistro.get("TIPOJUSTIFICACION") ==null || (primerRegistro.get("TIPOJUSTIFICACION").equals("PG")||primerRegistro.get("TIPOJUSTIFICACION").equals("DE"))) {
							columnasExcel = Arrays.asList(AragonEnviaJustificacionActuaciones.camposExcelGuardias) ;
						}else if(primerRegistro.get("TIPOJUSTIFICACION").equals("JA")) {
							Arrays.asList(AragonEnviaJustificacionActuaciones.camposExcelOficio) ;
						}
						String a�o = primerRegistro.get("ANO").toString();
						String codPeriodo = primerRegistro.get("CODPERIODO").toString();
						String numPeriodo = primerRegistro.get("NUMPERIODO").toString();
						StringBuilder nombreFichero = new StringBuilder();
						nombreFichero.append(informe.getNombreSalida());
						nombreFichero.append("_");
						nombreFichero.append(numPeriodo);
						nombreFichero.append(codPeriodo);
						nombreFichero.append(a�o);
						
						File file = informeXML.getFile(nombreFichero.toString(), datos);
						ClsLogging.writeFileLog("Fichero xml generado "+nombreFichero);
						File fileXlsx =  ExcelHelper.createExcelFile(columnasExcel, datos ,informe.getNombreSalida()+"_"+consulta.getIdInstitucion(),AppConstants.TIPO_FICHERO.xlsx.toString());
						ClsLogging.writeFileLog("Fichero xlsx generado "+nombreFichero);
						returnList.add(fileXlsx);
						returnList.add(file);
					}

				}
			}
		} catch (SIGAException e) {
			throw new BusinessException(e.getMessage());
		}catch (Exception e) {
			ClsLogging.writeFileLogError("Error: generarInformeXML"+e.getMessage(),e,3);
			throw new BusinessException("Se haproducido un error al genrar el informe xml");
		}

		return returnList;
	}
	
	public static ArrayList<File> generarInformeXLS(AdmInformeBean informe,
								  ArrayList<HashMap<String, String>> filtrosInforme,
								  String sRutaJava, UsrBean usr)
		throws ClsExceptions, SIGAException
	{
		// Variables
		AdmConsultaInformeConsultaBean consulta;
		String sentencia;
		Vector<Vector<Hashtable<String, String>>> datos;
		
		// Controles generales
		//UserTransaction tx = null;
		
		// obteniendo ruta de almacenamiento
		String idinstitucionInforme = informe.getIdInstitucion().toString();
		String idinstitucion = usr.getLocation();
		ReadProperties rp = new ReadProperties(
				SIGAReferences.RESOURCE_FILES.SIGA);
		
		String rutaAlm="";
		
		if((sRutaJava==null)||(sRutaJava.isEmpty())){

			rutaAlm = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")
					+ ClsConstants.FILE_SEP
					+ informe.getDirectorio() + ClsConstants.FILE_SEP
					+ (idinstitucionInforme.equals("0") ? "2000" : idinstitucionInforme) + ClsConstants.FILE_SEP;
		}else{
			rutaAlm = sRutaJava;
		}
		
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
				throw new SIGAException("informes.personalizable.error.configuracion.sinFiltros");
			
		}

		// obteniendo las consultas del informe
		AdmConsultaInformeAdm consultaAdm = new AdmConsultaInformeAdm(usr);
		Hashtable<String, String> consultaHash = new Hashtable<String, String>();
		consultaHash = new Hashtable<String, String>();
		consultaHash.put(AdmConsultaInformeBean.C_IDINSTITUCION, informe.getIdInstitucion().toString());
		consultaHash.put(AdmConsultaInformeBean.C_IDPLANTILLA, informe.getIdPlantilla());
		Vector<AdmConsultaInformeConsultaBean> consultas = consultaAdm.selectConsultas(consultaHash);

		// creando la ruta de salida
		FileHelper.mkdirs(rutaAlm);

		ArrayList<File> listaFicheros = new ArrayList<File> ();

		// variables para los ficheros de salida
		/**
		 * @TODO Habria que obtener un identificador unico: de momento se genera
		 *       el numero de usuario, que vale si el mismo usuario no ejecuta
		 *       informes a la vez (por ejemplo en dos navegadores).
		 *       Ademas, estaria bien dar un numero unico pero que sea algo
		 *       descriptivo �?
		 */
		String nombreFichero;
		File ficheroGenerado = null;
		BufferedWriter out;

		for (Iterator<AdmConsultaInformeConsultaBean> iterConsultas = consultas.iterator(); iterConsultas.hasNext(); ) {
			consulta = iterConsultas.next();

			// sustituyendo los filtros en la consulta por los datos del informe
			if ((sentencia = consultaAdm.sustituirFiltrosConsulta(consulta, filtrosInforme)) == null)  //CR7 - CAMBIAR LA CONSULTA 1027 - Datos previsiones facturaci�n
				// falla si la consulta no tiene los filtros obligatorios
				throw new SIGAException(
						"informes.personalizable.error.configuracion.filtros");

			try {

				/** @TODO Convendria que cambiar a selectBind */
				datos = consultaAdm.selectCamposOrdenados(sentencia);

			} catch (Exception sqle) {
				String mensaje = sqle.getMessage();
				if (mensaje.indexOf("TimedOutException") != -1
						|| mensaje.indexOf("timed out") != -1) {
					throw new SIGAException("messages.transaccion.timeout");
				} else if (sqle.toString().indexOf("ORA-") != -1) {
					throw new SIGAException("informes.personalizable.error.query", sqle, new String[] {"\""+consulta.getDescripcion()+"\"" });
				} else {
					throw new SIGAException("informes.personalizable.error.query.indeterminado");
				}
			}

			// Si no hay datos no se genera informe en esta iteracion
			if (datos == null || datos.size() == 0)
				continue;
				
			// generando el fichero
			try {
				// creando fichero para cada consulta
				if(informe.getIdTipoInforme().equalsIgnoreCase("PREV"))
				{	
					//Si estamos generando un informe de previsiones 
					nombreFichero = informe.getNombreSalida()+ ".xls";
					
				}else{
					
					nombreFichero = informe.getNombreSalida() + "_" + idinstitucion + "_" + usr.getUserName() + "_"
						+ UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "") + '_'
						+ consulta.getNombre() + ".xls";
				}	

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
			} catch (Exception sqle) {
				throw new SIGAException("Problema en la generacion del fichero Excel", sqle, new String[] { sqle.toString() });
			}

		} // For

		// devolviendo el fichero
		return listaFicheros;
		
	} // generarInformeXLS()
	
	private File generarInformeDOC(AdmInformeBean informe,Vector<Hashtable> vDatos, UsrBean usr)
			throws ClsExceptions, SIGAException
			{
		// Variables

		// Controles generales
		//UserTransaction tx;

		//UsrBean usr = (UsrBean) request.getSession().getAttribute("USRBEAN");

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
			throw new SIGAException("No existe la plantilla");
		Document doc = words.nuevoDocumento();
		FileHelper.mkdirs(rutaAlm);
		
		doc = words.sustituyeRegionDocumento(doc, "region", vDatos);
		
		if(vDatos!=null && vDatos.size()>0)
			words.sustituyeDocumento(doc, (Hashtable)vDatos.get(0));
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
		 *       descriptivo �?
		 */
		String nombreFichero = informe.getNombreSalida() + "_" + idinstitucion + "_" + usr.getUserName() + "_"
		+ UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "");
		
		if(informe.getTipoformato()!=null && informe.getTipoformato().equals("P"))
			nombreFichero= nombreFichero + ".pdf";
		else
			nombreFichero = nombreFichero + ".doc";
		
		
		File ficheroGenerado = words.grabaDocumento(doc, rutaAlm, nombreFichero);
		
		return ficheroGenerado;

	} // generarInformeDOC()
	private File generarInformeXLS(AdmInformeBean informe,
			Vector<Hashtable> vDatos,
			String[] columnas, UsrBean usr)
	throws ClsExceptions, SIGAException
	{
		// Variables
		String nombreFichero;
		File ficheroGenerado = null;
		BufferedWriter out;
		// Controles generales
		//UserTransaction tx = null;

		// obteniendo ruta de almacenamiento
		String idinstitucionInforme = informe.getIdInstitucion().toString();
		String idinstitucion = usr.getLocation();
		ReadProperties rp = new ReadProperties(
				SIGAReferences.RESOURCE_FILES.SIGA);
		String rutaAlm = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")
		+ ClsConstants.FILE_SEP
		+ informe.getDirectorio() + ClsConstants.FILE_SEP
		+ (idinstitucionInforme.equals("0") ? "2000" : idinstitucionInforme) + ClsConstants.FILE_SEP;
		FileHelper.mkdirs(rutaAlm);
		

			try {
					// creando fichero para cada consulta
					nombreFichero = informe.getNombreSalida() + "_" + idinstitucion + "_" + usr.getUserName() + "_"
					+ UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "") + '_'
					+ informe.getAlias() + ".xls";
					ficheroGenerado = new File (rutaAlm + ClsConstants.FILE_SEP + nombreFichero);
					if (ficheroGenerado.exists())
						ficheroGenerado.delete();
					ficheroGenerado.createNewFile();
					out = new BufferedWriter(new FileWriter(ficheroGenerado));

					// escribiendo las cabeceras
					for (int i = 0; i < columnas.length; i++) {
						String columna = columnas[i];
						out.write(columna + "\t");
						
					}
					out.newLine(); //Cerramos la linea de cabeceras


					// escribiendo los resultados		
					for (int i = 0; i < vDatos.size(); i++) {
						Hashtable hashOrdenado = vDatos.get(i);
						for (int j = 0; j < columnas.length; j++) {
							String columna = columnas[j];
							out.write(hashOrdenado.get(columna) + "\t");
							
						}
						out.newLine();
						/*
						
						Iterator ite = hashOrdenado.keySet().iterator();
						while (ite.hasNext()) {
							String key = (String) ite.next();
							out.write(hashOrdenado.get(key) + "\t");
							
						}
						
						out.newLine();*/
					}
					
					
					
					
					// cerrando el fichero
					out.close();
				
				
			} catch (Exception sqle) {
				throw new SIGAException(
						"Problema en la generacion del fichero Excel",
						sqle, new String[] { sqle.toString() });
			}
		

		// devolviendo el fichero
		return ficheroGenerado;

	} // generarInformeXLS()

	public File getFicheroGenerado(List<InformeForm> informesForms, Vector datos,String[] columnas, UsrBean userBean) throws ClsExceptions, SIGAException{
		ArrayList<File> listaFicheros = new ArrayList<File>();
		AdmTipoInformeAdm admT = new AdmTipoInformeAdm(userBean);
		AdmTipoInformeBean tipoInformeBean = admT.obtenerTipoInforme(AdmTipoInformeBean.TIPOINFORME_CONSULTAS);
		boolean isGeneradoFicheroExcelDefecto = false;
		for (InformeForm informeForm2 : informesForms) {
			if(informeForm2.getTipoFormato()!=null && informeForm2.getTipoFormato().equals(AdmInformeBean.TIPOFORMATO_WORD)){
				try {
					File  fichero= generarInformeDOC(informeForm2.getInformeVO(), datos, userBean);
					listaFicheros.add(fichero);	
				} catch (SIGAException e) {
					if(!isGeneradoFicheroExcelDefecto){
						File  fichero= generarInformeXLS(informeForm2.getInformeVO(), datos,columnas, userBean);
						listaFicheros.add(fichero);
					}
					isGeneradoFicheroExcelDefecto = true;
					
				}
				
			}else if(informeForm2.getTipoFormato()!=null && informeForm2.getTipoFormato().equals(AdmInformeBean.TIPOFORMATO_EXCEL)){
				File  fichero= generarInformeXLS(informeForm2.getInformeVO(), datos,columnas, userBean);
				listaFicheros.add(fichero);
			}else if(informeForm2.getTipoFormato()!=null && informeForm2.getTipoFormato().equals(AdmInformeBean.TIPOFORMATO_PDF)){
				try {
					File  fichero= generarInformeDOC(informeForm2.getInformeVO(), datos, userBean);
					listaFicheros.add(fichero);	
				} catch (SIGAException e) {
					if(!isGeneradoFicheroExcelDefecto){
						File  fichero= generarInformeXLS(informeForm2.getInformeVO(), datos,columnas, userBean);
						listaFicheros.add(fichero);
					}
					isGeneradoFicheroExcelDefecto = true;
					
				}
				
			}
		}                
		File ficheroSalida = getFicheroSalida(listaFicheros,tipoInformeBean, userBean);
		
		return ficheroSalida;
	}
	private File getFicheroSalida(ArrayList<File> listaFicheros,AdmTipoInformeBean tipoInformeBean,UsrBean userBean) throws SIGAException, ClsExceptions{
		String idInstitucion = userBean.getLocation();
		File ficheroSalida = null;
		if(listaFicheros.size()==0){
			throw new SIGAException("No se ha generado ningun informe");
		
		}else if(listaFicheros.size() == 1){
			ficheroSalida = listaFicheros.get(0);	
		} 
		else if(listaFicheros.size() > 1) {
			ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			
			String rutaServidorDescargasZip = rp.returnProperty("informes.directorioFisicoSalidaInformesJava")
			+ ClsConstants.FILE_SEP            
			+ idInstitucion + ClsConstants.FILE_SEP
			+ "temp" + ClsConstants.FILE_SEP;
			String nombreFicheroZIP = tipoInformeBean.getDescripcion().trim() + "_"
			+ idInstitucion + "_" + userBean.getUserName() + "_"
			+ UtilidadesBDAdm.getFechaCompletaBD("").replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "");
			FileHelper.mkdirs(rutaServidorDescargasZip);
			Plantilla plantilla = new Plantilla();
			plantilla.doZip(rutaServidorDescargasZip, nombreFicheroZIP, listaFicheros);
			ficheroSalida= new File(rutaServidorDescargasZip+ClsConstants.FILE_SEP+nombreFicheroZIP+".zip");
		}
		return ficheroSalida;
		
		
		
	}
	
	public String getIdFacturacion() {
		return idFacturacion;
	}

	public void setIdFacturacion(String idFacturacion) {
		this.idFacturacion = idFacturacion;
	}

	public boolean isEliminarFichero() {
		return eliminarFichero;
	}

	public void setEliminarFichero(boolean eliminarFichero) {
		this.eliminarFichero = eliminarFichero;
	}



	public void setTipoFicheroCAM(String tipoFicheroCAM) {
		this.tipoFicheroCAM = tipoFicheroCAM;		
	}
}
