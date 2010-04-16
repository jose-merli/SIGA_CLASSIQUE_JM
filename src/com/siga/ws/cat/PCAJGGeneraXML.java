package com.siga.ws.cat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.transaction.UserTransaction;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Document;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.general.SIGAException;
import com.siga.gratuita.action.DefinirRemesasCAJGAction;
import com.siga.ws.SIGAWSClientAbstract;
import com.siga.ws.SigaWSHelper;
import com.siga.ws.pcajg.DatosDomicilio;
import com.siga.ws.pcajg.IntercambioDocument;
import com.siga.ws.pcajg.TipoAbogadoDesignado;
import com.siga.ws.pcajg.TipoCodigoExpediente;
import com.siga.ws.pcajg.TipoDatosContacto;
import com.siga.ws.pcajg.TipoDatosPersona;
import com.siga.ws.pcajg.TipoDatosProcurador;
import com.siga.ws.pcajg.TipoDocumentacionExpediente;
import com.siga.ws.pcajg.TipoDomiciliosPersona;
import com.siga.ws.pcajg.TipoElementoTipificadoEstandar;
import com.siga.ws.pcajg.TipoElementoTipificadoIntercambio;
import com.siga.ws.pcajg.TipoExpediente;
import com.siga.ws.pcajg.TipoIdentificacionIntercambio;
import com.siga.ws.pcajg.TipoIdentificacionTramite;
import com.siga.ws.pcajg.TipoInformacion;
import com.siga.ws.pcajg.DatosDomicilio.Municipio;
import com.siga.ws.pcajg.DatosDomicilio.Municipio.Municipio2;
import com.siga.ws.pcajg.IntercambioDocument.Intercambio;
import com.siga.ws.pcajg.IntercambioDocument.Intercambio.InformacionIntercambio;
import com.siga.ws.pcajg.TipoDocumentacionExpediente.Documentacion;
import com.siga.ws.pcajg.TipoDocumentacionExpediente.Documentacion.DatosDocumento;
import com.siga.ws.pcajg.TipoExpediente.Contrarios;
import com.siga.ws.pcajg.TipoExpediente.DatosAsistenciaDetenido;
import com.siga.ws.pcajg.TipoExpediente.DatosDefensaJudicial;
import com.siga.ws.pcajg.TipoExpediente.DatosExpediente;
import com.siga.ws.pcajg.TipoExpediente.DatosRepresentante;
import com.siga.ws.pcajg.TipoExpediente.DatosSolicitante;
import com.siga.ws.pcajg.TipoExpediente.DatosTramitacionExpediente;
import com.siga.ws.pcajg.TipoExpediente.Familiares;
import com.siga.ws.pcajg.TipoExpediente.ProfesionalesDesignados;
import com.siga.ws.pcajg.TipoExpediente.Contrarios.Contrario;
import com.siga.ws.pcajg.TipoExpediente.DatosExpediente.CodigoExpedienteServicio;
import com.siga.ws.pcajg.TipoExpediente.DatosExpediente.MarcasExpediente;
import com.siga.ws.pcajg.TipoExpediente.DatosExpediente.MarcasExpediente.MarcaExpediente;
import com.siga.ws.pcajg.TipoExpediente.DatosSolicitante.DatosEconomicosPersona;
import com.siga.ws.pcajg.TipoExpediente.DatosSolicitante.DatosEconomicosPersona.Ingresos;
import com.siga.ws.pcajg.TipoExpediente.DatosSolicitante.DatosEconomicosPersona.PropiedadesBienesInmuebles;
import com.siga.ws.pcajg.TipoExpediente.DatosSolicitante.DatosEconomicosPersona.PropiedadesBienesMuebles;
import com.siga.ws.pcajg.TipoExpediente.DatosSolicitante.DatosEconomicosPersona.PropiedadesBienesOtros;
import com.siga.ws.pcajg.TipoExpediente.DatosTramitacionExpediente.TramiteArchivo;
import com.siga.ws.pcajg.TipoExpediente.DatosTramitacionExpediente.TramiteDictamen;
import com.siga.ws.pcajg.TipoExpediente.DatosTramitacionExpediente.TramiteResolucion;
import com.siga.ws.pcajg.TipoExpediente.DatosTramitacionExpediente.TramiteResolucion.PrestacionesResolucion;
import com.siga.ws.pcajg.TipoExpediente.Familiares.Familiar;
import com.siga.ws.pcajg.TipoExpediente.ProfesionalesDesignados.AbogadosDesignados;
import com.siga.ws.pcajg.TipoExpediente.ProfesionalesDesignados.ProcuradorDesignado;
import com.siga.ws.pcajg.TipoInformacion.Expedientes;


/**
 * @author angelcpe
 *
 */
public class PCAJGGeneraXML extends SIGAWSClientAbstract implements PCAJGConstantes {
		
	private static final String INTERCAMBIO_ALTA_PRESENTACION = "IAP";
	private static final String INTERCAMBIO_EXPEDIENTES_DICTAMINADOS = "IED";
	private static final String INTERCAMBIO_EXPEDIENTES_ARCHIVADOS = "IEA";
	private static final String INTERCAMBIO_RESOLUCIONES = "IR";	
	
	private String idTipoEJG;
	private String anyo;
	private String numero;
	private String numejg;
	
	
	private Map htFamiliares = new Hashtable();
	private Map htMarcasExpediente = new Hashtable();
	private Map htAbogadosDesignados = new Hashtable();
	private Map htContrarios = new Hashtable();
	private Map htDocumentacionExpediente = new Hashtable();
		
		
	/**
	 * Metodo que genera el xml en el directorio padre que le pases por parametro
	 * @param dirPadre
	 * @return
	 * @throws Exception
	 */
	private List<File> generaFicherosXML(String dirFicheros, String dirPlantilla) throws Exception {
		
		List<File> ficheros = new ArrayList<File>();		
		CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
		
//		doc.setSchemaLocation("IntercambioEJG.xsd");
//		doc.setXsiType();		
		Vector datos = cajgEJGRemesaAdm.getDatosEJGs(getIdInstitucion(), getIdRemesa());
		Vector datosFamiliares = cajgEJGRemesaAdm.getFamiliares(getIdInstitucion(), getIdRemesa());
		construyeHTxEJG(datosFamiliares, htFamiliares);
		
		Vector datosMarcasExpediente = cajgEJGRemesaAdm.getDatosMarcasExpediente(getIdInstitucion(), getIdRemesa());
		construyeHTxEJG(datosMarcasExpediente, htMarcasExpediente);
		
		Vector datosAbogadosDesignados = cajgEJGRemesaAdm.getAbogadosDesignados(getIdInstitucion(), getIdRemesa());
		construyeHTxEJG(datosAbogadosDesignados, htAbogadosDesignados);
		
		Vector datosContrarios = cajgEJGRemesaAdm.getContrarios(getIdInstitucion(), getIdRemesa());
		construyeHTxEJG(datosContrarios, htContrarios);	
		
		Vector datosDocumentacionExpedienteDS = cajgEJGRemesaAdm.getDocumentacionExpedienteDS(getIdInstitucion(), getIdRemesa());
		construyeHTxPersona(datosDocumentacionExpedienteDS, htDocumentacionExpediente);
		
		Hashtable ht = null;
		String tipoIntercambio = "";
		Expedientes expedientes = null;
		Intercambio intercambio = null;
		IntercambioDocument intercambioDocument = null;
		TipoInformacion tipoInformacion = null;
		int numDetalles = 0;
		int sufijoIdIntercambio = 0;
		
		for (int i = 0; i < datos.size(); i++) {
			ht = (Hashtable)datos.get(i);
			
			if (!tipoIntercambio.equals(ht.get(TIPOINTERCAMBIO))) {				
				if (intercambio != null) {					
					ficheros.add(creaFichero(dirFicheros, dirPlantilla, intercambioDocument, intercambio, tipoInformacion, numDetalles));
				}
				numDetalles = 0;
				intercambioDocument = IntercambioDocument.Factory.newInstance();
				intercambio = intercambioDocument.addNewIntercambio();
				InformacionIntercambio informacionIntercambio = rellenaInformacionIntercambio(intercambio, ht, sufijoIdIntercambio++);
				
				tipoInformacion = informacionIntercambio.addNewInformacion();				
				expedientes = tipoInformacion.addNewExpedientes();										
			}
			tipoIntercambio = (String) ht.get(TIPOINTERCAMBIO);
			try {
				if (addExpediente(expedientes, ht, tipoIntercambio)) {
					numDetalles++;
				}
			} catch (IllegalArgumentException e) {
				escribeErrorExpediente(anyo, numejg, numero, idTipoEJG, e.getMessage());
			}
		}
		if (intercambio != null) {			
			ficheros.add(creaFichero(dirFicheros, dirPlantilla, intercambioDocument, intercambio, tipoInformacion, numDetalles));
		}
						
		return ficheros;
	}
	
	private File creaFichero(String dirFicheros, String dirPlantilla, IntercambioDocument intercambioDocument, Intercambio intercambio, TipoInformacion tipoInformacion, int numDetalles) throws Exception {
		
		File file = new File(dirFicheros);
		file.mkdirs();
		/*File[] files = file.listFiles();
		if (files != null) { //borramos todos los xml que hayamos hecho antes
			for (int i = 0; i < files.length; i++){
				if (files[i].getName().endsWith(".xml")) {
					files[i].delete();
				}
			}
		}*/
		//intercambio.getInformacionIntercambio().setInformacion(tipoInformacion);
		TipoIdentificacionIntercambio tipoIdentificacionIntercambio = intercambio.getInformacionIntercambio().getIdentificacionIntercambio();
		tipoIdentificacionIntercambio.setNumeroDetallesIntercambio(numDetalles);
		
		StringBuffer nombreFichero = new StringBuffer();
		nombreFichero.append(tipoIdentificacionIntercambio.getTipoIntercambio());
		nombreFichero.append("_" + tipoIdentificacionIntercambio.getOrigenIntercambio().getCodigo());
		nombreFichero.append("_" + tipoIdentificacionIntercambio.getDestinoIntercambio().getCodigo());
		nombreFichero.append("_" + tipoIdentificacionIntercambio.getIdentificadorIntercambio());
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fechaIntercambio = sdf.format(tipoIdentificacionIntercambio.getFechaIntercambio().getTime());
		nombreFichero.append("_" + fechaIntercambio);
		nombreFichero.append("_" + tipoIdentificacionIntercambio.getNumeroDetallesIntercambio());
		nombreFichero.append(".xml");
		
		file = new File(file, nombreFichero.toString());		
				
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrintIndent(4);
		xmlOptions.setSavePrettyPrint();	
		
		SigaWSHelper.deleteEmptyNode(intercambio.getDomNode());
		intercambioDocument.save(file, xmlOptions);
				
		File fileXSL = null;
		File dirXSL = new File(dirPlantilla);
		
		if (dirXSL.exists()) {
			for (int i = 0; i < dirXSL.listFiles().length; i++) {
				if (dirXSL.listFiles()[i].getName().endsWith(".xsl")){
					fileXSL = dirXSL.listFiles()[i];
					break;
				}
			}
			
			if (fileXSL != null) { //si tiene xsl lo transformo
				File xmlTrans = new File(file.getParentFile(), "T" + file.getName());
				file.renameTo(xmlTrans);
				TransformerFactory tFactory = TransformerFactory.newInstance();
				Transformer transformer = tFactory.newTransformer(new StreamSource(fileXSL));							
				transformer.transform(new StreamSource(xmlTrans), new StreamResult(file));
								
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document xmldoc = builder.parse(file);
				
				FileOutputStream fos = new FileOutputStream(file);		
				OutputFormat of = new OutputFormat("XML", "ISO-8859-15", true);				
				of.setIndent(1);
				of.setIndenting(true);
				of.setLineWidth(500);
				
				XMLSerializer serializer = new XMLSerializer(fos, of);
				serializer.asDOMSerializer();
				serializer.serialize( xmldoc.getDocumentElement() );
				fos.flush();
				fos.close();
				xmlTrans.delete();
			}
		}
		
		return file;
		
	}

	
	/**
	 * 
	 * @param datos
	 * @param htCarga
	 */	
	private void construyeHTxEJG(Vector datos, Map htCarga) {
		if (datos != null) {
			for (int i = 0; i < datos.size(); i++) {
				Hashtable ht = (Hashtable)datos.get(i);
				String key = getKey(new Object[]{ht.get(IDINSTITUCION), ht.get(ANIO), ht.get(NUMERO), ht.get(IDTIPOEJG)});
				
				List list = (List)htCarga.get(key);
				if (list != null) {
					list.add(ht);
				} else {
					list = new ArrayList();
					list.add(ht);
					htCarga.put(key, list);
				}				
			}		
		}
	}
	
	/**
	 * 
	 * @param datos
	 * @param htCarga
	 */
	private void construyeHTxPersona(Vector datos, Map htCarga) {
		if (datos != null) {
			for (int i = 0; i < datos.size(); i++) {
				Hashtable ht = (Hashtable)datos.get(i);
				String key = getKey(new Object[]{ht.get(IDINSTITUCION), ht.get(ANIO), ht.get(NUMERO), ht.get(IDTIPOEJG), ht.get(IDPERSONA)});
				
				List list = (List)htCarga.get(key);
				if (list != null) {
					list.add(ht);
				} else {
					list = new ArrayList();
					list.add(ht);
					htCarga.put(key, list);
				}				
			}		
		}
	}
	
	
	/**
	 * 
	 * @param args
	 * @return
	 */
	private String getKey(Object[] args) {
		String key = "";
		for (int i = 0; i < args.length; i++) {
			key += args[i] + "##";
		}		
		return key;
	}

	
	/**
	 * Metodo que te setea el encodig ISO-8859-15 en el fichero xml que le pases por parametro
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public File setEncodingISO_8859_15(File file) throws Exception {
//		IntercambioEJG2 doc = IntercambioEJG2.loadFromFile(file.getAbsolutePath());
//		FileOutputStream fos = new FileOutputStream(file, false);		
//		fos.write(doc.saveToBinary(true, "ISO-8859-15"));		
//		fos.flush();
//		fos.close();
		return file;
	}

	/**
	 * Añade un expediente en el fichero xml
	 * @param expedientesType
	 * @param htEJGs
	 * @return
	 * @throws Exception
	 */
	private boolean addExpediente(Expedientes expedientes, Hashtable htEJGs, String tipoIntercambio) throws Exception {		
		
		TipoExpediente tipoExpediente = expedientes.addNewExpediente();
		idTipoEJG = (String)htEJGs.get(IDTIPOEJG);
		anyo = (String)htEJGs.get(ANIO);
		numero = (String)htEJGs.get(NUMERO);
		numejg = (String)htEJGs.get(NUMEJG);
		
		datosExpediente(tipoExpediente, htEJGs);
		profesionalesDesignados(tipoExpediente, htEJGs);
		datosSolicitante(tipoExpediente, htEJGs);
		datosRepresentante(tipoExpediente, htEJGs);
		
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG});
		List list = (List) htFamiliares.get(key);
		if (list != null && list.size() > 0) {
			Familiares familiares = tipoExpediente.addNewFamiliares();
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable) list.get(i);			
				datosFamiliares(familiares, ht);
			}
		}
				
		list = (List)htContrarios.get(key);		
		if (list != null && list.size() > 0) {
			Contrarios contrarios = tipoExpediente.addNewContrarios();
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable) list.get(i);
				datosContrario(contrarios, ht);
			}
		}
		
		datosDefensaJudicial(tipoExpediente, htEJGs);
		datosAsistenciaDetenido(tipoExpediente, htEJGs);
		if (!tipoIntercambio.equals(INTERCAMBIO_ALTA_PRESENTACION)) {
			datosTramitacionExpediente(tipoExpediente, htEJGs, tipoIntercambio);
		}
		
		if(!validateXML_EJG(tipoExpediente, anyo, numejg, numero, idTipoEJG)){
			expedientes.removeExpediente(expedientes.sizeOfExpedienteArray()-1);
			return false;			
		}
		
		return true;
	}
	
	/**
	 * Añade profesionales designados a tu fichero xml
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception 
	 * @throws Exception
	 */
	private void profesionalesDesignados(TipoExpediente tipoExpediente, Hashtable htEJGs) throws Exception {
		ProfesionalesDesignados profesionalesDesignados = tipoExpediente.addNewProfesionalesDesignados();
		
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG});
		List list = (List) htAbogadosDesignados.get(key);
		
		if (list != null && list.size() > 0) {
			AbogadosDesignados abogadosDesignados = profesionalesDesignados.addNewAbogadosDesignados();
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable) list.get(i);			
				abogadoDesignado(abogadosDesignados, ht);
			}
		}
		
		
		ProcuradorDesignado procuradorDesignado = profesionalesDesignados.addNewProcuradorDesignado();		
		Integer valueInt = getInteger("baja procurador designado", (String)htEJGs.get(PD_PD_BAJAPROCURADORDESIGNADO));
		if (valueInt != null) {
			procuradorDesignado.setBajaProcuradorDesignado(valueInt.intValue());
		}
				
		TipoDatosProcurador datosProcurador = procuradorDesignado.addNewDatosProcurador();
		datosProcurador.setColegioProcurador((String)htEJGs.get(PD_PD_DP_COLEGIOPROCURADOR));
		Integer valueInteger = getInteger("número de colegiado del procurador", (String)htEJGs.get(PD_PD_DP_NUMCOLEGIADOPROCURADO));
		if (valueInteger != null) {
			datosProcurador.setNumColegiadoProcurador(valueInteger.intValue());
		}
		datosProcurador.setNombreCompletoProcurador((String)htEJGs.get(PD_PD_DP_NOMBRECOMPLETOPROCURA));		
		
		Calendar cal = getCalendar((String)htEJGs.get(PD_PD_FECHADESIGNACIONPROCURAD));
		if (cal != null) {
			procuradorDesignado.setFechaDesignacionProcurador(cal);
		}
		valueInteger = getInteger("número de designación del procurador", (String)htEJGs.get(PD_PD_NUMDESIGNACIONPROCURADOR));
		if (valueInteger != null) {
			procuradorDesignado.setNumDesignacionProcurador(valueInteger.intValue());
		}
	}

	/**
	 * Añade datos de tramitacion expediente a tu fichero xml
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private void datosTramitacionExpediente(TipoExpediente tipoExpediente, Hashtable htEJGs, String tipoIntercambio) throws Exception {
		DatosTramitacionExpediente datosTramitacionExpediente = null;
		if (tipoIntercambio.equals(INTERCAMBIO_EXPEDIENTES_ARCHIVADOS)) {
			datosTramitacionExpediente = tipoExpediente.addNewDatosTramitacionExpediente();
			TramiteArchivo tramiteArchivo = datosTramitacionExpediente.addNewTramiteArchivo();
			identificacionTramite(tramiteArchivo.addNewIdentificacionTramite(), htEJGs, DTE_TA_IT_ESTADOEXPEDIENTE_CDA, DTE_TA_IT_FECHAESTADO);
		} else if (tipoIntercambio.equals(INTERCAMBIO_EXPEDIENTES_DICTAMINADOS)) {		
			datosTramitacionExpediente = tipoExpediente.addNewDatosTramitacionExpediente();
			TramiteDictamen tramiteDictamen = datosTramitacionExpediente.addNewTramiteDictamen();
			identificacionTramite(tramiteDictamen.addNewIdentificacionTramite(), htEJGs, DTE_TD_IT_ESTADOEXPEDIENTE_CDA, DTE_TD_IT_FECHAESTADO);
			rellenaTipoElementoTipificadoEstandar(tramiteDictamen.addNewTipoDictamen(), (String)htEJGs.get(DTE_TD_TIPODICTAMEN_CDA));
			rellenaTipoElementoTipificadoEstandar(tramiteDictamen.addNewMotivoDictamen(), (String)htEJGs.get(DTE_TD_MOTIVODICTAMEN_CDA));
			tramiteDictamen.setIntervaloIngresosRecursos((String)htEJGs.get(DTE_TD_INTERVALOINGRESOSRECURS));
			tramiteDictamen.setObservacionesDictamen((String)htEJGs.get(DTE_TD_OBSERVACIONESDICTAMEN));
		} else if (tipoIntercambio.equals(INTERCAMBIO_RESOLUCIONES)) {		
			datosTramitacionExpediente = tipoExpediente.addNewDatosTramitacionExpediente();
			TramiteResolucion tramiteResolucion = datosTramitacionExpediente.addNewTramiteResolucion();
			identificacionTramite(tramiteResolucion.addNewIdentificacionTramite(), htEJGs, DTE_TR_IT_ESTADOEXPEDIENTE_CDA, DTE_TR_IT_FECHAESTADO);
			tramiteResolucion.setIdentificadorResolucion((String)htEJGs.get(DTE_TR_IDENTIFICADORRESOLUCION));
			rellenaTipoElementoTipificadoEstandar(tramiteResolucion.addNewTipoResolucion(), (String)htEJGs.get(DTE_TR_TIPORESOLUCION_CDA));
			rellenaTipoElementoTipificadoEstandar(tramiteResolucion.addNewMotivoResolucion(), (String)htEJGs.get(DTE_TR_MOTIVORESOLUCION_CDA));
			tramiteResolucion.setIntervaloIngresosRecursos((String)htEJGs.get(DTE_TR_INTERVALOINGRESOSRECURS));
			
			CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
			Vector datos = cajgEJGRemesaAdm.getDatosPrestacionesResolucion(getIdInstitucion(), getIdRemesa(), idTipoEJG, anyo, numero);
			for (int i = 0; i < datos.size(); i++) {
				Hashtable ht = (Hashtable) datos.get(i);
				PrestacionesResolucion prestacionesResolucion = tramiteResolucion.addNewPrestacionesResolucion();
				rellenaTipoElementoTipificadoEstandar(prestacionesResolucion.addNewTipoPrestacion(), (String)ht.get(DTE_TR_PR_TIPOPRESTACION_CDA));
			}
		}
	}

	/**
	 * Añade identificacion de tramite a tu fichero xml
	 * @param identificacionTramiteType
	 * @param htEJGs
	 * @param it_estadoexpediente_cda
	 * @param it_fechaestado
	 * @throws Exception
	 */
	private void identificacionTramite(TipoIdentificacionTramite tipoIdentificacionTramite, Hashtable htEJGs, String it_estadoexpediente_cda, String it_fechaestado) throws Exception {
		rellenaTipoElementoTipificadoEstandar(tipoIdentificacionTramite.addNewEstadoExpediente(), (String)htEJGs.get(it_estadoexpediente_cda));
		Calendar cal = getCalendar((String)htEJGs.get(it_fechaestado));
		if (cal != null) {
			tipoIdentificacionTramite.setFechaEstado(cal);
		}
	}

	/**
	 * Añade datos asistente detenido a tu fichero xml
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private void datosAsistenciaDetenido(TipoExpediente tipoExpediente, Hashtable htEJGs) throws Exception {
		DatosAsistenciaDetenido datosAsistenciaDetenido = tipoExpediente.addNewDatosAsistenciaDetenido();
		datosAsistenciaDetenido.setNumeroAtestado((String)htEJGs.get(DAD_NUMEROATESTADO));
		Calendar cal = getCalendar((String)htEJGs.get(DAD_FECHAASISTENCIAATESTADO));
		if (cal != null) {
			datosAsistenciaDetenido.setFechaAsistenciaAtestado(cal);
		}
		rellenaTipoElementoTipificadoEstandar(datosAsistenciaDetenido.addNewCentroDetencion(), (String)htEJGs.get(DAD_CENTRODETENCION_CDA));
		datosAsistenciaDetenido.setNumeroDiligencia((String)htEJGs.get(DAD_NUMERODILIGENCIA));
		cal = getCalendar((String)htEJGs.get(DAD_FECHAASISTENCIADILIGENCIA));
		if (cal != null) {
			datosAsistenciaDetenido.setFechaAsistenciaDiligencia(cal);
		}
		rellenaTipoElementoTipificadoEstandar(datosAsistenciaDetenido.addNewOrganoJudicial(), (String)htEJGs.get(DAD_ORGANOJUDICIAL_CDA));
		datosAsistenciaDetenido.addNewAbogadoAsistencia().setNumColegiadoAbogado((String)htEJGs.get(DAD_AA_NUMCOLEGIADOABOGADO));		
		
		Boolean b = getBoolean((String)htEJGs.get(DAD_ESATESTADO));
		if (b != null) {
			datosAsistenciaDetenido.setEsAtestado(b.booleanValue());
		}
		rellenaTipoElementoTipificadoEstandar(datosAsistenciaDetenido.addNewTipoDelito(), (String)htEJGs.get(DAD_TIPODELITO_CDA));
	}

	/**
	 * 
	 * @param abogadosDesignadosType
	 * @param ht
	 * @throws Exception
	 */
	
	private void abogadoDesignado(AbogadosDesignados abogadosDesignados, Hashtable ht) throws Exception {	
	
		TipoAbogadoDesignado tipoAbogadoDesignado = abogadosDesignados.addNewAbogadoDesignado();
		
		Integer valueInt = getInteger("baja abogado designado", (String)ht.get(PD_AD_AD_BAJAABOGADODESIGNADO));
		if (valueInt != null) {
			tipoAbogadoDesignado.setBajaAbogadoDesignado(valueInt.intValue());
		}
		
		tipoAbogadoDesignado.setNumColegiadoAbogado((String)ht.get(PD_AD_AD_NUMCOLEGIADOABOGADO));
		Calendar cal = getCalendar((String)ht.get(PD_AD_AD_FECHADESIGNACIONABOGA));
		if (cal != null) {
			tipoAbogadoDesignado.setFechaDesignacionAbogado(cal);
		}
		Long valueLong = getLong((String)ht.get(PD_AD_AD_NUMDESIGNACIONABOGADO));
		if (valueLong != null) {
			tipoAbogadoDesignado.setNumDesignacionAbogado(valueLong.longValue());
		}
		String st  = (String)ht.get(PD_AD_AD_NOMBRECOMPLETOABO);
		if (st != null) {
			tipoAbogadoDesignado.setNombreCompletoAbogado(st);
		}
	}

	
	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 */
	private void datosDefensaJudicial(TipoExpediente tipoExpediente, Hashtable htEJGs) {
		DatosDefensaJudicial datosDefensaJudicial = tipoExpediente.addNewDatosDefensaJudicial();
		rellenaTipoElementoTipificadoEstandar(datosDefensaJudicial.addNewPartidoJudicial(), (String)htEJGs.get(DDJ_PARTIDOJUDICIAL_CDA));
		rellenaTipoElementoTipificadoEstandar(datosDefensaJudicial.addNewJurisdiccion(), (String)htEJGs.get(DDJ_JURISDICCION_CDA));
		rellenaTipoElementoTipificadoEstandar(datosDefensaJudicial.addNewOrganoJudicial(), (String)htEJGs.get(DDJ_ORGANOJUDICIAL_CDA));
		rellenaTipoElementoTipificadoEstandar(datosDefensaJudicial.addNewTipoProcedimiento(), (String)htEJGs.get(DDJ_TIPOPROCEDIMIENTO_CDA));
		datosDefensaJudicial.setIdentificadorProcedimiento((String)htEJGs.get(DDJ_IDENTIFICADORPROCEDIMIENTO));
		datosDefensaJudicial.setResumenPretension((String)htEJGs.get(DDJ_RESUMENPRETENSION));
		
		Boolean b = getBoolean((String)htEJGs.get(DDJ_RENUNCIADESIGNACION));
		if (b != null){
			datosDefensaJudicial.setRenunciaDesignacion(b.booleanValue());			
		}
		b = getBoolean((String)htEJGs.get(DDJ_RENUNCIAHONORARIOS));
		if (b != null) {
			datosDefensaJudicial.setRenunciaHonorarios(b.booleanValue());
		}
		Integer value = getInteger("demandante o demandado", (String)htEJGs.get(DDJ_DEMANDADODEMANDANTE));
		if (value != null) {
			datosDefensaJudicial.setDemandadoDemandante(value.intValue());
		}
		
		rellenaTipoElementoTipificadoEstandar(datosDefensaJudicial.addNewPreceptivo(), (String)htEJGs.get(DDJ_PRECEPTIVO_CDA));
	}

	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private void datosContrario(Contrarios contrarios, Hashtable htEJGs) throws Exception {		
		Contrario contrario = contrarios.addNewContrario();
		datosPersona(contrario.addNewDatosPersona(), htEJGs, C_C_DP_TIPOPERSONA_CDA, C_C_DP_TIPOIDENTIFICACION_CDA, C_C_DP_IDENTIFICACION, C_C_DP_NOMBRE
				, C_C_DP_PRIMERAPELLIDO, C_C_DP_SEGUNDOAPELLIDO, C_C_DP_RAZONSOCIAL, C_C_DP_FECHANACIMIENTO, C_C_DP_NACIONALIDAD_CDA
				, C_C_DP_SITUACIONLABORAL_CDA, C_C_DP_PROFESION_CDA, C_C_DP_SEXO, C_C_DP_IDIOMACOMUNICACION, C_C_DP_ESTADOCIVIL_CDA
				, C_C_DP_REGIMENECONOMICO_CDA, C_C_DP_NUMHIJOS, C_C_DP_FECHAFORMALIZACION, C_C_DP_TIPOPERSONAJURIDICA_CDA);
		
		TipoDatosProcurador datosProcurador = contrario.addNewDatosProcurador();
		datosProcurador.setColegioProcurador((String)htEJGs.get(C_C_DP_COLEGIOPROCURADOR));
		Integer valueInteger = getInteger("número de colegiado del procurador", (String)htEJGs.get(C_C_DP_NUMCOLEGIADOPROCURADOR));
		if (valueInteger != null) {
			datosProcurador.setNumColegiadoProcurador(valueInteger.intValue());
		}
		datosProcurador.setNombreCompletoProcurador((String)htEJGs.get(C_C_DP_NOMBRECOMPLETOPROCURADO));
	}

	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private void datosFamiliares(Familiares datosFamiliares, Hashtable htFam) throws Exception {
		
		Familiar familiar = datosFamiliares.addNewFamiliar();
		rellenaTipoElementoTipificadoEstandar(familiar.addNewParentesco(), (String)htFam.get(F_F_PARENTESCO_CDA));
		
		Boolean b =  getBoolean((String)htFam.get(F_F_FAMILIARMENOR));
		if (b != null) {
			familiar.setFamiliarMenor(b.booleanValue());
		}
		datosPersona(familiar.addNewDatosPersona(), htFam, F_F_DP_TIPOPERSONA_CDA, F_F_DP_TIPOIDENTIFICACION_CDA, F_F_DP_IDENTIFICACION, F_F_DP_NOMBRE
				, F_F_DP_PRIMERAPELLIDO, F_F_DP_SEGUNDOAPELLIDO, F_F_DP_RAZONSOCIAL, F_F_DP_FECHANACIMIENTO, F_F_DP_NACIONALIDAD_CDA
				, F_F_DP_SITUACIONLABORAL_CDA, F_F_DP_PROFESION_CDA, F_F_DP_SEXO, F_F_DP_IDIOMACOMUNICACION, F_F_DP_ESTADOCIVIL_CDA
				, F_F_DP_REGIMENECONOMICO_CDA, F_F_DP_NUMHIJOS, F_F_DP_FECHAFORMALIZACION, F_F_DP_TIPOPERSONAJURIDICA_CDA);
		
		TipoDocumentacionExpediente tipoDocumentacionExpediente = familiar.addNewDocumentacionExpediente();
		
		String idPersona = (String)htFam.get(IDPERSONA);
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG, idPersona});
		List list = (List) htDocumentacionExpediente.get(key);
		
		
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Hashtable htDoc = (Hashtable)list.get(i);				
				documentacionExpediente(tipoDocumentacionExpediente.addNewDocumentacion(), htDoc, F_F_DE_D_TIPODOCUMENTACION_CDA
						, F_F_DE_D_DD_TIPODOCUMENTO_CDA, F_F_DE_D_DD_FECHAPRESENTACIDO, F_F_DE_D_DD_DESCRIPCIONAMPLIAD, F_F_DE_D_DD_PROCEDENTE);
			}
		} else {
			tipoDocumentacionExpediente.getDomNode().getParentNode().removeChild(tipoDocumentacionExpediente.getDomNode());
		}
//		ver si hay que añadir geronimos al classpath
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private Boolean getBoolean(String value) {
		Boolean b = null;
		if (value != null && !value.trim().equals("")) {
			if (value.trim().equals("1")) {
				b = Boolean.TRUE;
			} else if (value.trim().equals("0")) {
				b = Boolean.FALSE;
			}
		}
		return b;
	}

	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private void datosRepresentante(TipoExpediente tipoExpediente, Hashtable htEJGs) throws Exception {
		String st = getString((String)htEJGs.get(DR_DP_TIPOPERSONA_CDA));
		if (st != null) {
			DatosRepresentante datosRepresentante = tipoExpediente.addNewDatosRepresentante();
			datosPersona(datosRepresentante.addNewDatosPersona(), htEJGs, DR_DP_TIPOPERSONA_CDA, DR_DP_TIPOIDENTIFICACION_CDA, DR_DP_IDENTIFICACION, DR_DP_NOMBRE
				, DR_DP_PRIMERAPELLIDO, DR_DP_SEGUNDOAPELLIDO, DR_DP_RAZONSOCIAL, DR_DP_FECHANACIMIENTO, DR_DP_NACIONALIDAD_CDA
				, DR_DP_SITUACIONLABORAL_CDA, DR_DP_PROFESION_CDA, DR_DP_SEXO, DR_DP_IDIOMACOMUNICACION, DR_DP_ESTADOCIVIL_CDA
				, DR_DP_REGIMENECONOMICO_CDA, DR_DP_NUMHIJOS, DR_DP_FECHAFORMALIZACION, DR_DP_TIPOPERSONAJURIDICA_CDA);
			domiciliosPersona(datosRepresentante.addNewDomiciliosPersona(), htEJGs, DR_DP_TIPODOMICILIO_CDA
				, DR_DP_DD_TIPOVIA_CDA
				, DR_DP_DD_NOMBREVIA
				, DR_DP_DD_NUMERODIRECCION
				, DR_DP_DD_ESCALERADIRECCION
				, DR_DP_DD_PISODIRECCION
				, DR_DP_DD_PUERTADIRECCION
				, DR_DP_DD_PAIS_CDA
				, DR_DP_DD_PROVINCIA_CDA
				, DR_DP_DD_M_MUNICIPIO_CDA
				, DR_DP_DD_M_SUBCODIGOMUNICIPIO
				, DR_DP_DD_CODIGOPOSTAL);
		
			datosContacto(datosRepresentante.addNewDatosContacto(), htEJGs, DR_DC_NUMEROTELEFONO1, DR_DC_NUMEROTELEFONO2, DR_DC_EMAIL);
		}
	}

	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private void datosSolicitante(TipoExpediente tipoExpediente, Hashtable htEJGs) throws Exception {
		DatosSolicitante datosSolicitante = tipoExpediente.addNewDatosSolicitante();
		
		datosPersona(datosSolicitante.addNewDatosPersona(), htEJGs, DS_DP_TIPOPERSONA_CDA, DS_DP_TIPOIDENTIFICACION_CDA, DS_DP_IDENTIFICACION, DS_DP_NOMBRE
				, DS_DP_PRIMERAPELLIDO, DS_DP_SEGUNDOAPELLIDO, DS_DP_RAZONSOCIAL, DS_DP_FECHANACIMIENTO, DS_DP_NACIONALIDAD_CDA
				, DS_DP_SITUACIONLABORAL_CDA, DS_DP_PROFESION_CDA, DS_DP_SEXO, DS_DP_IDIOMACOMUNICACION, DS_DP_ESTADOCIVIL_CDA
				, DS_DP_REGIMENECONOMICO_CDA, DS_DP_NUMHIJOS, DS_DP_FECHAFORMALIZACION, DS_DP_TIPOPERSONAJURIDICA_CDA);
					
		domiciliosPersona(datosSolicitante.addNewDomiciliosPersona(), htEJGs, DS_DP_TIPODOMICILIO_CDA
				, DS_DP_DD_TIPOVIA_CDA
				, DS_DP_DD_NOMBREVIA
				, DS_DP_DD_NUMERODIRECCION
				, DS_DP_DD_ESCALERADIRECCION
				, DS_DP_DD_PISODIRECCION
				, DS_DP_DD_PUERTADIRECCION
				, DS_DP_DD_PAIS_CDA
				, DS_DP_DD_PROVINCIA_CDA
				, DS_DP_DD_M_MUNICIPIO_CDA
				, DS_DP_DD_M_SUBCODIGOMUNICIPIO
				, DS_DP_DD_CODIGOPOSTAL);
		
		datosContacto(datosSolicitante.addNewDatosContacto(), htEJGs, DS_DC_NUMEROTELEFONO1, DS_DC_NUMEROTELEFONO2, DS_DC_EMAIL);
		
		TipoDocumentacionExpediente tipoDocumentacionExpediente = datosSolicitante.addNewDocumentacionExpediente();
		
		String idPersona = (String)htEJGs.get(IDPERSONA);		
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG, idPersona});
		List list = (List) htDocumentacionExpediente.get(key);
		
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable)list.get(i);
				documentacionExpediente(tipoDocumentacionExpediente.addNewDocumentacion(), ht, DS_DE_D_TIPODOCUMENTACION_CDA, 
						DS_DE_D_DD_TIPODOCUMENTO_CDA, DS_DE_D_DD_FECHAPRESENTACIONDO, DS_DE_D_DD_DESCRIPCIONAMPLIADA, DS_DE_D_DD_PROCEDENTE);
			}
		} else {
			tipoDocumentacionExpediente.getDomNode().getParentNode().removeChild(tipoDocumentacionExpediente.getDomNode());
		}
		
		DatosEconomicosPersona datosEconomicosPersona = datosSolicitante.addNewDatosEconomicosPersona();

		Ingresos ingresos = datosEconomicosPersona.addNewIngresos();
		rellenaTipoElementoTipificadoEstandar(ingresos.addNewConceptoIngresos(), (String) htEJGs.get(DS_DEP_I_CONCEPTOINGRESOS_CDA));
		ingresos.setObservacionesIngresos((String) htEJGs.get(DS_DEP_I_OBSERVACIONESINGRESOS));
		ingresos.setDescripcionIngresos((String) htEJGs.get(DS_DEP_I_DESCRIPCIONINGRESOS));
		Double value = getDouble((String) htEJGs.get(DS_DEP_I_IMPORTEBRUTO));
		if (value != null) {
			ingresos.setImporteBruto(value.floatValue());
		}
		Integer valueInt = getInteger("acumulable de los ingresos", (String) htEJGs.get(DS_DEP_I_ACUMULABLE));
		if (valueInt != null) {
			ingresos.setAcumulable(valueInt.intValue());
		}
			
		PropiedadesBienesInmuebles propiedadesBienesInmuebles = datosEconomicosPersona.addNewPropiedadesBienesInmuebles();
		rellenaTipoElementoTipificadoEstandar(propiedadesBienesInmuebles.addNewTipoBienInmueble(), (String) htEJGs.get(DS_DEP_PBI_TIPOBIENINMUEBL_CDA));
		rellenaTipoElementoTipificadoEstandar(propiedadesBienesInmuebles.addNewOrigenValoracion(), (String) htEJGs.get(DS_DEP_PBI_ORIGENVALORACIO_CDA));
		propiedadesBienesInmuebles.setCargas((String) htEJGs.get(DS_DEP_PBI_CARGAS));
		propiedadesBienesInmuebles.setObservaciones((String) htEJGs.get(DS_DEP_PBI_OBSERVACIONES));
		value = getDouble((String) htEJGs.get(DS_DEP_PBI_VALORACIONECONOMICA));
		if (value != null) {
			propiedadesBienesInmuebles.setValoracionEconomica(value.floatValue());
		}
		valueInt = getInteger("acumulable de los bienes inmuebles", (String) htEJGs.get(DS_DEP_PBI_ACUMULABLE));
		if (valueInt != null) {
			propiedadesBienesInmuebles.setAcumulable(valueInt.intValue());
		}
		
		PropiedadesBienesMuebles propiedadesBienesMuebles = datosEconomicosPersona.addNewPropiedadesBienesMuebles();
		rellenaTipoElementoTipificadoEstandar(propiedadesBienesMuebles.addNewTipoBienMueble(), (String) htEJGs.get(DS_DEP_PBM_TIPOBIENMUEBLE_CDA));
		propiedadesBienesMuebles.setObservaciones((String) htEJGs.get(DS_DEP_PBM_OBSERVACIONES));
		value = getDouble((String) htEJGs.get(DS_DEP_PBM_VALORACIONECONOMICA));
		if (value != null) {
			propiedadesBienesMuebles.setValoracionEconomica(value.floatValue());
		}
		valueInt = getInteger("acumulable de los bienes muebles", (String) htEJGs.get(DS_DEP_PBM_ACUMULABLE));
		if (valueInt != null) {
			propiedadesBienesMuebles.setAcumulable(valueInt.intValue());
		}
		
		PropiedadesBienesOtros propiedadesBienesOtros = datosEconomicosPersona.addNewPropiedadesBienesOtros();
		propiedadesBienesOtros.setCargas((String) htEJGs.get(DS_DEP_PBO_CARGAS));
		propiedadesBienesOtros.setDescripcionBien((String) htEJGs.get(DS_DEP_PBO_DESCRIPCIONBIEN));
		propiedadesBienesOtros.setObservaciones((String) htEJGs.get(DS_DEP_PBO_OBSERVACIONES));
		value = getDouble((String) htEJGs.get(DS_DEP_PBO_VALORACIONECONOMICA));
		if (value != null) {
			propiedadesBienesOtros.setValoracionEconomica(value.floatValue());
		}

		valueInt = getInteger("acumulable de bienes otros",(String) htEJGs.get(DS_DEP_PBO_ACUMULABLE));
		if (valueInt != null) {
			propiedadesBienesOtros.setAcumulable(valueInt.intValue());
		}
		
		datosEconomicosPersona.setOtrosDatosEconomicos((String)htEJGs.get(DS_DEP_OTROSDATOSECONOMICOS));
	}

	
	/**
	 * 
	 * @param documentacionExpedienteType
	 * @param htEJGs
	 * @param de_tipodocumentacion_cda
	 * @param de_dd_tipodocumento_cda
	 * @param de_dd_fechapresentaciondocumento
	 * @param de_dd_descripcionampliadadocumento
	 * @param de_dd_procedente
	 * @throws Exception
	 */
	private void documentacionExpediente(Documentacion documentacion, Hashtable htDocumentacion, String de_tipodocumentacion_cda,
			String de_dd_tipodocumento_cda, String de_dd_fechapresentaciondocumento, String de_dd_descripcionampliadadocumento,
			String de_dd_procedente) throws Exception {
		
		
		rellenaTipoElementoTipificadoEstandar(documentacion.addNewTipoDocumentacion(), (String)htDocumentacion.get(de_tipodocumentacion_cda));
		
		DatosDocumento datosDocumento = documentacion.addNewDatosDocumento();
		rellenaTipoElementoTipificadoEstandar(datosDocumento.addNewTipoDocumento(), (String)htDocumentacion.get(de_dd_tipodocumento_cda));
		Calendar cal = getCalendar((String)htDocumentacion.get(de_dd_fechapresentaciondocumento));
		if (cal != null) {
			datosDocumento.setFechaPresentacionDocumento(cal);
		}
		datosDocumento.setDescripcionAmpliadaDocumento((String)htDocumentacion.get(de_dd_descripcionampliadadocumento));
		Integer valueInt = getInteger("procedente de la documentación", (String)htDocumentacion.get(de_dd_procedente));
		if (valueInt != null) {
			datosDocumento.setProcedente(valueInt.intValue());
		}
	}

	
	/**
	 * 
	 * @param datosContactoType
	 * @param htEJGs
	 * @param dc_numerotelefono1
	 * @param dc_numerotelefono2
	 * @param dc_email
	 */
	private void datosContacto(TipoDatosContacto tipoDatosContacto, Hashtable htEJGs, String dc_numerotelefono1, String dc_numerotelefono2, String dc_email) {
		tipoDatosContacto.setNumeroTelefono1((String)htEJGs.get(dc_numerotelefono1));
		tipoDatosContacto.setNumeroTelefono2((String)htEJGs.get(dc_numerotelefono2));
		tipoDatosContacto.setEmail((String)htEJGs.get(dc_email));
	}

	
	/**
	 * 
	 * @param domiciliosPersona
	 * @param htEJGs
	 * @param dop_codtipodomicilio
	 * @param dop_dd_tipovia_cda
	 * @param dop_dd_nombrevia
	 * @param dop_dd_numerodireccion
	 * @param dop_dd_escaleradireccion
	 * @param dop_dd_pisodireccion
	 * @param dop_dd_puertadireccion
	 * @param dop_dd_pais_cda
	 * @param dop_dd_provincia_cda
	 * @param dop_dd_m_municipio_cda
	 * @param dop_dd_m_subcodigomunicipio
	 * @param dop_dd_codigopostal
	 */
	private void domiciliosPersona(TipoDomiciliosPersona tipoDomiciliosPersona, Hashtable htEJGs, String dop_tipodomicilio_cda, String dop_dd_tipovia_cda,
			String dop_dd_nombrevia, String dop_dd_numerodireccion, String dop_dd_escaleradireccion, String dop_dd_pisodireccion,
			String dop_dd_puertadireccion, String dop_dd_pais_cda, String dop_dd_provincia_cda, String dop_dd_m_municipio_cda,
			String dop_dd_m_subcodigomunicipio, String dop_dd_codigopostal) {
		
		
		rellenaTipoElementoTipificadoEstandar(tipoDomiciliosPersona.addNewTipoDomicilio(), (String)htEJGs.get(dop_tipodomicilio_cda));
		DatosDomicilio datosDomicilio = tipoDomiciliosPersona.addNewDatosDomicilio();
		rellenaTipoElementoTipificadoEstandar(datosDomicilio.addNewTipoVia(), (String)htEJGs.get(dop_dd_tipovia_cda));
		datosDomicilio.setNombreVia((String)htEJGs.get(dop_dd_nombrevia));
		datosDomicilio.setNumeroDireccion((String)htEJGs.get(dop_dd_numerodireccion));
		datosDomicilio.setEscaleraDireccion((String)htEJGs.get(dop_dd_escaleradireccion));
		datosDomicilio.setPisoDireccion((String)htEJGs.get(dop_dd_pisodireccion));
		datosDomicilio.setPuertaDireccion((String)htEJGs.get(dop_dd_puertadireccion));
		rellenaTipoElementoTipificadoEstandar(datosDomicilio.addNewPais(), (String)htEJGs.get(dop_dd_pais_cda));
		rellenaTipoElementoTipificadoEstandar(datosDomicilio.addNewProvincia(), (String)htEJGs.get(dop_dd_provincia_cda));
		
		Municipio municipio = datosDomicilio.addNewMunicipio();
		rellenaMunicipio(municipio.addNewMunicipio(), (String)htEJGs.get(dop_dd_m_municipio_cda));
		municipio.setSubcodigoMunicipio((String)htEJGs.get(dop_dd_m_subcodigomunicipio));
		String st = getString((String)htEJGs.get(dop_dd_codigopostal));
		if (st != null) {
			datosDomicilio.setCodigoPostal(st);
		}
	}

	
	/**
	 * 
	 * @param datosPersonaType
	 * @param htEJGs
	 * @param dp_codtipopersona
	 * @param dp_codtipoidentificacion
	 * @param dp_identificacion
	 * @param dp_nombre
	 * @param dp_primerapellido
	 * @param dp_segundoapellido
	 * @param dp_razonsocial
	 * @param dp_fechanacimiento
	 * @param dp_nacionalidad_cda
	 * @param dp_situacionlaboral_cda
	 * @param dp_profesion_cda
	 * @param dp_sexo
	 * @param dp_idiomacomunicacion
	 * @param dp_estadocivil_cda
	 * @param dp_regimeneconomico_cda
	 * @param dp_numhijos
	 * @param dp_fechaformalizacion
	 * @param dp_tipopersonajuridica_cda
	 * @throws Exception
	 */
	private void datosPersona(TipoDatosPersona tipoDatosPersona, Hashtable htEJGs, String dp_tipopersona_cda, String dp_tipoidentificacion_cda,
			String dp_identificacion, String dp_nombre, String dp_primerapellido, String dp_segundoapellido, String dp_razonsocial,
			String dp_fechanacimiento, String dp_nacionalidad_cda, String dp_situacionlaboral_cda, String dp_profesion_cda, String dp_sexo,
			String dp_idiomacomunicacion, String dp_estadocivil_cda, String dp_regimeneconomico_cda, String dp_numhijos,
			String dp_fechaformalizacion, String dp_tipopersonajuridica_cda) throws Exception {
		
		rellenaTipoElementoTipificadoEstandar(tipoDatosPersona.addNewTipoPersona(), (String)htEJGs.get(dp_tipopersona_cda));
		rellenaTipoElementoTipificadoEstandar(tipoDatosPersona.addNewTipoIdentificacion(), (String)htEJGs.get(dp_tipoidentificacion_cda));
		tipoDatosPersona.setIdentificacion((String)htEJGs.get(dp_identificacion));
		tipoDatosPersona.setNombre((String)htEJGs.get(dp_nombre));
		tipoDatosPersona.setPrimerApellido((String)htEJGs.get(dp_primerapellido));
		tipoDatosPersona.setSegundoApellido((String)htEJGs.get(dp_segundoapellido));
		tipoDatosPersona.setRazonSocial((String)htEJGs.get(dp_razonsocial));
		Calendar cal = getCalendar((String)htEJGs.get(dp_fechanacimiento));
		if (cal != null) {
			tipoDatosPersona.setFechaNacimiento(cal);
		}
		rellenaTipoElementoTipificadoEstandar(tipoDatosPersona.addNewNacionalidad(), (String)htEJGs.get(dp_nacionalidad_cda));
		rellenaTipoElementoTipificadoEstandar(tipoDatosPersona.addNewSituacionLaboral(), (String)htEJGs.get(dp_situacionlaboral_cda));
		rellenaTipoElementoTipificadoEstandar(tipoDatosPersona.addNewProfesion(), (String)htEJGs.get(dp_profesion_cda));
		tipoDatosPersona.setSexo((String)htEJGs.get(dp_sexo));
		tipoDatosPersona.setIdiomaComunicacion((String)htEJGs.get(dp_idiomacomunicacion));		
		rellenaTipoElementoTipificadoEstandar(tipoDatosPersona.addNewEstadoCivil(), (String)htEJGs.get(dp_estadocivil_cda));
		rellenaTipoElementoTipificadoEstandar(tipoDatosPersona.addNewRegimenEconomico(), (String)htEJGs.get(dp_regimeneconomico_cda));
		Long value = getLong((String)htEJGs.get(dp_numhijos));
		if (value != null) {
			tipoDatosPersona.setNumHijos(value.longValue());
		}
		cal = getCalendar((String)htEJGs.get(dp_fechaformalizacion));
		if (cal != null) {
			tipoDatosPersona.setFechaFormalizacion(cal);
		}
		rellenaTipoElementoTipificadoEstandar(tipoDatosPersona.addNewTipoPersonaJuridica(), (String)htEJGs.get(dp_tipopersonajuridica_cda));		
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private Long getLong(String value) {
		if (value != null && !value.trim().equals("")) {
			return Long.valueOf(value);
		}
		return null;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Double getDouble(String value) {
		if (value != null && !value.trim().equals("")) {
			return Double.valueOf(value);
		}
		return null;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Integer getInteger(String campo, String value) {
		try {
			if (value != null && !value.trim().equals("")) {
				return Integer.valueOf(value);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("El campo \"" + campo + "\" debe ser un número y se ha obtenido \"" + value + "\"");
		}
		return null;
	}

	/**
	 * 
	 * @param fecha
	 * @return
	 * @throws Exception
	 * 
	 */
	private Calendar getCalendar(String fecha) throws Exception {		
		Calendar cal = null;		
		if (fecha != null && !fecha.trim().equals("")) {
			cal = Calendar.getInstance();
			cal.setTime(GstDate.convertirFecha(fecha));				
		}	
		
		return cal;
	}
	

	/**
	 * 
	 * @param tipoElementoTipificadoEstandar
	 * @param value
	 */
	private void rellenaTipoElementoTipificadoEstandar(TipoElementoTipificadoEstandar tipoElementoTipificadoEstandar, String value) {
		boolean relleno = false;
		if (value != null && !value.trim().equals("")) {
			String[] array = value.split("##");
			if (array != null && array.length > 0) {
				tipoElementoTipificadoEstandar.setCodigo(array[0]);
				relleno = true;
				if (array.length > 1) {
					tipoElementoTipificadoEstandar.setDescripcion(array[1]);
				}
				if (array.length > 2) {
					tipoElementoTipificadoEstandar.setAbreviatura(array[2]);
				}
			}
		}	
		if (!relleno) {
			tipoElementoTipificadoEstandar.getDomNode().getParentNode().removeChild(tipoElementoTipificadoEstandar.getDomNode());
		}
	}

	
	/**
	 * 
	 * @param tipoElementoTipificadoEstandar
	 * @param value
	 */
	private void rellenaTipoElementoTipificadoIntercambio(TipoElementoTipificadoIntercambio tipoElementoTipificadoIntercambio, String value) {
		if (value != null && !value.trim().equals("")) {
			String[] array = value.split("##");
			if (array != null && array.length > 0) {
				tipoElementoTipificadoIntercambio.setCodigo(array[0]);
				if (array.length > 1) {
					tipoElementoTipificadoIntercambio.setDescripcion(array[1]);
				}
				if (array.length > 2) {
					tipoElementoTipificadoIntercambio.setAbreviatura(array[2]);
				}
			}
		}
		
	}
		
	/**
	 * 
	 * @param municipioType2
	 * @param value
	 */
	private void rellenaMunicipio(Municipio2 municipio2, String value) {
		boolean relleno = false;
		if (value != null && !value.trim().equals("")) {
			String[] array = value.split("##");
			if (array != null && array.length > 0) {
				municipio2.setCodigo(array[0]);
				relleno = true;
				if (array.length > 1) {
					municipio2.setDescripcion(array[1]);
				}
				if (array.length > 2) {
					municipio2.setAbreviatura(array[2]);
				}
			}
		}
		if (!relleno) {
			municipio2.getDomNode().getParentNode().removeChild(municipio2.getDomNode());
		}
	}
	
	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */

	private void datosExpediente(TipoExpediente tipoExpediente, Hashtable htEJGs) throws Exception {
		DatosExpediente datosExpediente = tipoExpediente.addNewDatosExpediente();
		
		TipoCodigoExpediente codigoExpediente = datosExpediente.addNewCodigoExpediente();
		codigoExpediente.setColegioExpediente((String)htEJGs.get(DE_CE_COLEGIOEXPEDIENTE));
		String numExpediente = UtilidadesString.formatea(htEJGs.get(DE_CE_NUMEXPEDIENTE), 8, true);
		codigoExpediente.setNumExpediente(numExpediente);		
		Long anyoExpediente = getLong((String)htEJGs.get(DE_CE_ANYOEXPEDIENTE));		
		if (anyoExpediente != null) {
			codigoExpediente.setAnyoExpediente(anyoExpediente.longValue());	
		}
		
		CodigoExpedienteServicio codigoExpedienteServicio = null;
		
		String value = getString((String)htEJGs.get(DE_CES_ORIGENEXPEDIENTESERVICI));
		if (value != null) {
			if (codigoExpedienteServicio == null) {
				codigoExpedienteServicio = datosExpediente.addNewCodigoExpedienteServicio();
			}
			codigoExpedienteServicio.setOrigenExpedienteServicio(value);
		}
		
		value = getString((String)htEJGs.get(DE_CES_NUMEXPEDIENTESERVICIO));		
		if (value != null) {
			if (codigoExpedienteServicio == null) {
				codigoExpedienteServicio = datosExpediente.addNewCodigoExpedienteServicio();
			}
			codigoExpedienteServicio.setNumExpedienteServicio(value);
		}		
		
		Long anyoLong = getLong((String)htEJGs.get(DE_CES_ANYOEXPEDIENTESERVICIO));
		if (anyoLong != null) {
			if (codigoExpedienteServicio == null) {
				codigoExpedienteServicio = datosExpediente.addNewCodigoExpedienteServicio();
			}
			codigoExpedienteServicio.setAnyoExpedienteServicio(anyoLong.longValue());
		}		
		Calendar cal = getCalendar((String)htEJGs.get(DE_FECHASOLICITUD));
		if (cal != null) {
			datosExpediente.setFechaSolicitud(cal);
		}
		datosExpediente.setObservaciones((String)htEJGs.get(DE_OBSERVACIONES2));
		
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG});
		List list = (List) htMarcasExpediente.get(key);
		
		for (int i = 0; i < list.size(); i++) {
			Hashtable ht = (Hashtable) list.get(i);
			String st = getString((String)ht.get(DE_ME_ME_VALORMARCAEXPEDIENTE));
			if (st != null) {
				MarcasExpediente marcasExpediente = datosExpediente.addNewMarcasExpediente();
				MarcaExpediente marcaExpediente = marcasExpediente.addNewMarcaExpediente();
			
				rellenaTipoElementoTipificadoEstandar(marcaExpediente.addNewMarcaExpediente(), (String)ht.get(DE_ME_ME_MARCAEXPEDIENTE_CDA));
				marcaExpediente.setValorMarcaExpediente(st);
			}
		}
		
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getString(String value) {
		if (value == null || value.trim().equals("")) {
			return null;
		}
		return value;
	}


	/**
	 * 
	 * @param sufijoIdIntercambio 
	 * @param intercambioType
	 * @param numDetalles
	 * @return
	 */
	private InformacionIntercambio rellenaInformacionIntercambio(Intercambio intercambio, Hashtable ht, int sufijoIdIntercambio) throws SIGAException {
				
		InformacionIntercambio informacionIntercambio = intercambio.addNewInformacionIntercambio();
		TipoIdentificacionIntercambio identificacionIntercambio = informacionIntercambio.addNewIdentificacionIntercambio();
		
		if (ht != null) {		
			identificacionIntercambio.setTipoIntercambio((String)ht.get(TIPOINTERCAMBIO));
			rellenaTipoElementoTipificadoIntercambio(identificacionIntercambio.addNewOrigenIntercambio(), (String)ht.get(ORIGENINTERCAMBIO_CDA));
			rellenaTipoElementoTipificadoIntercambio(identificacionIntercambio.addNewDestinoIntercambio(), (String)ht.get(DESTINOINTERCAMBIO_CDA));		
			
			Long valueLong = getLong((String)ht.get(IDENTIFICADORINTERCAMBIO));
			identificacionIntercambio.setIdentificadorIntercambio((valueLong.longValue() * 10) + sufijoIdIntercambio);		
			identificacionIntercambio.setFechaIntercambio(Calendar.getInstance());			
			identificacionIntercambio.setVersionPCAJG((String)ht.get(VERSION_PCAJG));
		}
		return informacionIntercambio;
	}


	@Override
	public void execute() throws Exception {
				
		SFTPmanager sftp = null;
		
		UsrBean usr = getUsrBean();
		
		String keyPathFicheros = "cajg.directorioFisicoCAJG";
		String keyPathPlantillas = "cajg.directorioPlantillaCAJG";
		String keyPath2 = "cajg.directorioCAJGJava";
				
	    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String pathFichero = p.returnProperty(keyPathFicheros) + p.returnProperty(keyPath2);
		String pathPlantillas = p.returnProperty(keyPathPlantillas) + p.returnProperty(keyPath2);
		
		String dirFicheros = pathFichero + File.separator + getIdInstitucion()  + File.separator + getIdRemesa() + File.separator + "xml";
		String dirPlantillas = pathPlantillas + File.separator + getIdInstitucion();

		UserTransaction tx = usr.getTransaction();
		
		//si no queremos generar el fichero txt ademas del xml hay que cometar solamente esta línea
		generaTXT(dirFicheros);
		
		try {
			
			tx.begin();
			
			CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(usr);
			cajgRespuestaEJGRemesaAdm.eliminaAnterioresErrores(getIdInstitucion(), getIdRemesa());
			
			List<File> files = generaFicherosXML(dirFicheros, dirPlantillas);	
				
			sftp = new SFTPmanager(usr, String.valueOf(getIdInstitucion()));
						
			escribeLogRemesa("Conectando al servidor FTP");
			
			ChannelSftp chan = sftp.connect();
			chan.cd(sftp.getFtpDirectorioIN());
			
			for (File file : files) {
				FileInputStream fis = new FileInputStream(file);
				escribeLogRemesa("Subiendo XML generado al servidor FTP");
				chan.put(fis, file.getName());
				fis.close();				
				escribeLogRemesa("El archivo se ha subido correctamente al servidor FTP");
			}			
			
			
			CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(usr);
			CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(usr);
			
			
			// Marcar como generada
			cajgRemesaEstadosAdm.nuevoEstadoRemesa(usr, getIdInstitucion(), getIdRemesa(), Integer.valueOf("1"));

			
			//MARCAMOS COMO ENVIADA
			if (cajgRemesaEstadosAdm.nuevoEstadoRemesa(usr, getIdInstitucion(), getIdRemesa(), Integer.valueOf("2"))) {
				cajgEJGRemesaAdm.nuevoEstadoEJGRemitidoComision(usr, String.valueOf(getIdInstitucion()), String.valueOf(getIdRemesa()), ClsConstants.REMITIDO_COMISION);
			}
			tx.commit();

		} catch (JSchException e) {
			escribeLogRemesa("Se ha producido un error de conexión con el servidor FTP");
			ClsLogging.writeFileLogError("Error en el envío FTP", e, 3);			
		} finally {			
			if (sftp != null) {
				sftp.disconnect();
			}
		}
		
		
		
	}
	
	/**
	 * 
	 * @param pathFichero
	 */
	private void generaTXT(String pathFichero) {
		
		String nombreFichero = getIdInstitucion() + "_" + getIdRemesa() + "_TXT_BackupXML";
		StringBuffer mensaje = new StringBuffer();		
		DefinirRemesasCAJGAction definirRemesasCAJGAction = new DefinirRemesasCAJGAction();
		try {
			definirRemesasCAJGAction.generaFicherosTXT(String.valueOf(getIdInstitucion()), String.valueOf(getIdRemesa()), nombreFichero, mensaje, pathFichero);
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error al generar el archivo TXT", e, 3);			
		}
		
	}

	public static void main(String[] args) throws Exception {
		File fileXSL = new File("C:\\Datos\\plantillas\\CAJG\\2047\\plantilla.xsl");
		File file = new File("C:\\Documents and Settings\\angelcpe.ITCGAE-WS011\\Escritorio\\temp\\generalitat\\IED_2047_GEN_12_20091221_5.xml");
		File xmlTrans = new File(file.getParentFile(), "T" + file.getName());
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(new StreamSource(fileXSL));							
		transformer.transform(new StreamSource(file), new StreamResult(xmlTrans));
	}
	
}