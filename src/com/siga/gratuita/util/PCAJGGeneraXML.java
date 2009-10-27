package com.siga.gratuita.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.ws.axis2.EnviarIntercambioDocument;
import org.apache.ws.axis2.EnviarIntercambioDocument.EnviarIntercambio;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.general.SIGAException;

import es.pcajg.DatosDomicilio;
import es.pcajg.IntercambioDocument;
import es.pcajg.TipoAbogadoDesignado;
import es.pcajg.TipoDatosContacto;
import es.pcajg.TipoDatosPersona;
import es.pcajg.TipoDatosProcurador;
import es.pcajg.TipoDocumentacionExpediente;
import es.pcajg.TipoDomiciliosPersona;
import es.pcajg.TipoElementoTipificadoEstandar;
import es.pcajg.TipoElementoTipificadoIntercambio;
import es.pcajg.TipoExpediente;
import es.pcajg.TipoIdentificacionTramite;
import es.pcajg.DatosDomicilio.Municipio;
import es.pcajg.DatosDomicilio.Municipio.Municipio2;
import es.pcajg.ExpedientesDocument.Expedientes;
import es.pcajg.InformacionEJGDocument.InformacionEJG;
import es.pcajg.IntercambioDocument.Intercambio;
import es.pcajg.IntercambioDocument.Intercambio.InformacionIntercambio;
import es.pcajg.IntercambioDocument.Intercambio.InformacionIntercambio.IdentificacionIntercambio;
import es.pcajg.TipoDocumentacionExpediente.Documentacion;
import es.pcajg.TipoDocumentacionExpediente.Documentacion.DatosDocumento;
import es.pcajg.TipoExpediente.Contrarios;
import es.pcajg.TipoExpediente.DatosAsistenciaDetenido;
import es.pcajg.TipoExpediente.DatosDefensaJudicial;
import es.pcajg.TipoExpediente.DatosExpediente;
import es.pcajg.TipoExpediente.DatosRepresentante;
import es.pcajg.TipoExpediente.DatosSolicitante;
import es.pcajg.TipoExpediente.DatosTramitacionExpediente;
import es.pcajg.TipoExpediente.Familiares;
import es.pcajg.TipoExpediente.ProfesionalesDesignados;
import es.pcajg.TipoExpediente.Contrarios.Contrario;
import es.pcajg.TipoExpediente.DatosExpediente.CodigoExpediente;
import es.pcajg.TipoExpediente.DatosExpediente.CodigoExpedienteServicio;
import es.pcajg.TipoExpediente.DatosExpediente.MarcasExpediente;
import es.pcajg.TipoExpediente.DatosExpediente.MarcasExpediente.MarcaExpediente;
import es.pcajg.TipoExpediente.DatosSolicitante.DatosEconomicosPersona;
import es.pcajg.TipoExpediente.DatosSolicitante.DatosEconomicosPersona.Ingresos;
import es.pcajg.TipoExpediente.DatosSolicitante.DatosEconomicosPersona.PropiedadesBienesInmuebles;
import es.pcajg.TipoExpediente.DatosSolicitante.DatosEconomicosPersona.PropiedadesBienesMuebles;
import es.pcajg.TipoExpediente.DatosSolicitante.DatosEconomicosPersona.PropiedadesBienesOtros;
import es.pcajg.TipoExpediente.DatosTramitacionExpediente.TramiteArchivo;
import es.pcajg.TipoExpediente.DatosTramitacionExpediente.TramiteDictamen;
import es.pcajg.TipoExpediente.DatosTramitacionExpediente.TramiteResolucion;
import es.pcajg.TipoExpediente.DatosTramitacionExpediente.TramiteResolucion.PrestacionesResolucion;
import es.pcajg.TipoExpediente.Familiares.Familiar;
import es.pcajg.TipoExpediente.ProfesionalesDesignados.AbogadosDesignados;
import es.pcajg.TipoExpediente.ProfesionalesDesignados.ProcuradorDesignado;

/**
 * @author angelcpe
 *
 */
public class PCAJGGeneraXML extends PCAJGConstantes {
	
		
	private CajgEJGRemesaAdm cajgEJGRemesaAdm;
	private String idInstitucion;
	private String idRemesa;
	private String idTipoEJG;
	private String anyo;
	private String numero;
	
	private Map htFamiliares = new Hashtable();
	private Map htMarcasExpediente = new Hashtable();
	private Map htAbogadosDesignados = new Hashtable();
	private Map htContrarios = new Hashtable();
	private Map htDocumentacionExpediente = new Hashtable();
	

	/**
	 * Clase encargada de generar un fichero XML segun clases generadas por xmlSpy a partir de un XSD
	 * @param cajgEJGRemesaAdm
	 * @param idInstitucion
	 * @param idRemesa
	 */
	public PCAJGGeneraXML(CajgEJGRemesaAdm cajgEJGRemesaAdm, String idInstitucion, String idRemesa) {
		this.cajgEJGRemesaAdm = cajgEJGRemesaAdm;
		this.idInstitucion = idInstitucion;
		this.idRemesa = idRemesa;
	}

	
	/**
	 * Metodo que genera el xml en el directorio padre que le pases por parametro
	 * @param dirPadre
	 * @return
	 * @throws Exception
	 */
	public File generaXML(String dirFicheros, String dirPlantilla) throws Exception {
					
		IntercambioDocument intercambioDoc = IntercambioDocument.Factory.newInstance();
		Intercambio intercambio = intercambioDoc.addNewIntercambio();
		
//		doc.setSchemaLocation("IntercambioEJG.xsd");
//		doc.setXsiType();		
		Vector datos = cajgEJGRemesaAdm.getDatosEJGs(idInstitucion, idRemesa);
		Vector datosFamiliares = cajgEJGRemesaAdm.getFamiliares(idInstitucion, idRemesa);
		construyeHTxEJG(datosFamiliares, htFamiliares);
		
		Vector datosMarcasExpediente = cajgEJGRemesaAdm.getDatosMarcasExpediente(idInstitucion, idRemesa);
		construyeHTxEJG(datosMarcasExpediente, htMarcasExpediente);
		
		Vector datosAbogadosDesignados = cajgEJGRemesaAdm.getAbogadosDesignados(idInstitucion, idRemesa);
		construyeHTxEJG(datosAbogadosDesignados, htAbogadosDesignados);
		
		Vector datosContrarios = cajgEJGRemesaAdm.getContrarios(idInstitucion, idRemesa);
		construyeHTxEJG(datosContrarios, htContrarios);	
		
		Vector datosDocumentacionExpedienteDS = cajgEJGRemesaAdm.getDocumentacionExpedienteDS(idInstitucion, idRemesa);
		construyeHTxPersona(datosDocumentacionExpedienteDS, htDocumentacionExpediente);
		
		Vector datosIdenInter = cajgEJGRemesaAdm.getDatosIdentificacionIntercambio(idInstitucion, idRemesa);
				
		Hashtable ht = (Hashtable)datosIdenInter.get(0);
		InformacionIntercambio informacionIntercambio = getInformacionIntercambio(intercambio, datos.size(), ht);
		
		InformacionEJG informacionEJG = InformacionEJG.Factory.newInstance();
		
		Expedientes expedientes = informacionEJG.addNewExpedientes();
		
		for (int i = 0; i < datos.size(); i++) {
			addExpediente(expedientes, (Hashtable)datos.get(i));
		}
		informacionIntercambio.setInformacion(informacionEJG);		
		
		File file = new File(dirFicheros);
		file.mkdirs();
		File[] files = file.listFiles();
		if (files != null) { //borramos todos los xml que hayamos hecho antes
			for (int i = 0; i < files.length; i++){
				if (files[i].getName().endsWith(".xml")) {
					files[i].delete();
				}
			}
		}
		
		IdentificacionIntercambio identificacionIntercambio = informacionIntercambio.getIdentificacionIntercambio();
		StringBuffer nombreFichero = new StringBuffer();
		nombreFichero.append(identificacionIntercambio.getTipoIntercambio());
		nombreFichero.append("_" + identificacionIntercambio.getOrigenIntercambio().getCodigo());
		nombreFichero.append("_" + identificacionIntercambio.getDestinoIntercambio().getCodigo());
		nombreFichero.append("_" + identificacionIntercambio.getIdentificadorIntercambio());
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fechaIntercambio = sdf.format(identificacionIntercambio.getFechaIntercambio().getTime());
		nombreFichero.append("_" + fechaIntercambio);
		nombreFichero.append("_" + identificacionIntercambio.getNumeroDetallesIntercambio());
		nombreFichero.append(".xml");
		
		file = new File(file, nombreFichero.toString());		
		
		deleteEmptyNode(intercambio.getDomNode());
		
		EnviarIntercambioDocument doc = EnviarIntercambioDocument.Factory.newInstance();
		EnviarIntercambio req = doc.addNewEnviarIntercambio();
		req.setIntercambio(intercambio);
		
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrintIndent(4);
		xmlOptions.setSavePrettyPrint();		
						       
		req.save(file, xmlOptions);
				
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
				TransformerFactory tFactory = TransformerFactory.newInstance();
				Transformer transformer = tFactory.newTransformer(new StreamSource(fileXSL));							
				transformer.transform(new StreamSource(file), new StreamResult(xmlTrans));
				
				file.delete();
				xmlTrans.renameTo(file);
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document xmldoc = builder.parse(file);
//				Node node = xmldoc.getFirstChild();
				
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
	 * @param intercambio
	 */
	private void validateXML(Intercambio intercambio) {

		List listaErrores = new ArrayList();		
		if (!intercambio.validate(new XmlOptions().setErrorListener(listaErrores))) {
			int errorLevel = 10;
			ClsLogging.writeFileLog("ERRORES DE VALIDACION XML DEL CAJG:", errorLevel);

			for (int i = 0; i < listaErrores.size(); i++) {
				XmlError error = (XmlError) listaErrores.get(i);
				ClsLogging.writeFileLog(error.getMessage(), errorLevel);
				if (error.getObjectLocation() != null) {
					ClsLogging.writeFileLog(error.getObjectLocation().toString(), errorLevel);
				}
			}

		}		
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
	 * 
	 * @param nodeParent
	 * @throws Exception
	 */
	private void deleteEmptyNode(Node nodeParent) throws Exception {
		List arrayNodes = new ArrayList();
		arrayNodes.add(nodeParent);
		Node node = null;
		while(!arrayNodes.isEmpty()){
			node = (Node) arrayNodes.remove(0);
			if (node != null) {	
//				if (node.getNodeValue() != null) { //ñapa del euro TODO					
//					node.setNodeValue(new String(node.getNodeValue().getBytes(), "ISO-8859-1"));					
//				}
				if (node.getNodeType() != Node.TEXT_NODE) {
					StringBuffer contenido = getContenido(node);
					if (contenido.length() == 0) {				
						node.getParentNode().removeChild(node);
					} else {						
						NodeList nodeList = node.getChildNodes();
						for (int i = 0; i < nodeList.getLength(); i++) {
							arrayNodes.add(nodeList.item(i));	
						}	
					}
				}
			}	
		}
			
	}
	
	/**
	 * Obtiene el contenido de un nodo junto con sus hijos de manera recursiva
	 * @param node
	 * @return
	 */
	private static StringBuffer getContenido(Node node) {
		StringBuffer contenido = new StringBuffer();
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			contenido.append(nodeList.item(i).getNodeValue()!=null?nodeList.item(i).getNodeValue().replaceAll("\\n", "").trim():"");
			contenido.append(getContenido(nodeList.item(i)));
		}
		return contenido;
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
	private TipoExpediente addExpediente(Expedientes expedientes, Hashtable htEJGs) throws Exception {		
		
		TipoExpediente tipoExpediente = expedientes.addNewExpediente();
		idTipoEJG = (String)htEJGs.get(IDTIPOEJG);
		anyo = (String)htEJGs.get(ANIO);
		numero = (String)htEJGs.get(NUMERO);
		
		datosExpediente(tipoExpediente, htEJGs);
		profesionalesDesignados(tipoExpediente, htEJGs);
		datosSolicitante(tipoExpediente, htEJGs);
		datosRepresentante(tipoExpediente, htEJGs);
		
		String key = getKey(new Object[]{idInstitucion, anyo, numero, idTipoEJG});
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
		datosTramitacionExpediente(tipoExpediente, htEJGs);
		return tipoExpediente;
	}
	
	/**
	 * Añade profesionales designados a tu fichero xml
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private void profesionalesDesignados(TipoExpediente tipoExpediente, Hashtable htEJGs) throws Exception {
		ProfesionalesDesignados profesionalesDesignados = tipoExpediente.addNewProfesionalesDesignados();
		
		String key = getKey(new Object[]{idInstitucion, anyo, numero, idTipoEJG});
		List list = (List) htAbogadosDesignados.get(key);
		
		if (list != null && list.size() > 0) {
			AbogadosDesignados abogadosDesignados = profesionalesDesignados.addNewAbogadosDesignados();
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable) list.get(i);			
				abogadoDesignado(abogadosDesignados, ht);
			}
		}
		
		
		ProcuradorDesignado procuradorDesignado = profesionalesDesignados.addNewProcuradorDesignado();		
		Integer valueInt = getInteger((String)htEJGs.get(PD_PD_BAJAPROCURADORDESIGNADO));
		if (valueInt != null) {
			procuradorDesignado.setBajaProcuradorDesignado(valueInt.intValue());
		}
				
		TipoDatosProcurador datosProcurador = procuradorDesignado.addNewDatosProcurador();
		datosProcurador.setColegioProcurador((String)htEJGs.get(PD_PD_DP_COLEGIOPROCURADOR));
		Long valueLong = getLong((String)htEJGs.get(PD_PD_DP_NUMCOLEGIADOPROCURADO));
		if (valueLong != null) {
			datosProcurador.setNumColegiadoProcurador(valueLong.longValue());
		}
		datosProcurador.setNombreCompletoProcurador((String)htEJGs.get(PD_PD_DP_NOMBRECOMPLETOPROCURA));		
		
		Calendar cal = getCalendar((String)htEJGs.get(PD_PD_FECHADESIGNACIONPROCURAD));
		if (cal != null) {
			procuradorDesignado.setFechaDesignacionProcurador(cal);
		}
		valueLong = getLong((String)htEJGs.get(PD_PD_NUMDESIGNACIONPROCURADOR));
		if (valueLong != null) {
			procuradorDesignado.setNumDesignacionProcurador(valueLong.longValue());
		}
	}

	/**
	 * Añade datos de tramitacion expediente a tu fichero xml
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private void datosTramitacionExpediente(TipoExpediente tipoExpediente, Hashtable htEJGs) throws Exception {
		DatosTramitacionExpediente datosTramitacionExpediente = tipoExpediente.addNewDatosTramitacionExpediente();
		TramiteArchivo tramiteArchivo = datosTramitacionExpediente.addNewTramiteArchivo();
		identificacionTramite(tramiteArchivo.addNewIdentificacionTramite(), htEJGs, DTE_TA_IT_ESTADOEXPEDIENTE_CDA, DTE_TA_IT_FECHAESTADO);
		
		TramiteDictamen tramiteDictamen = datosTramitacionExpediente.addNewTramiteDictamen();
		identificacionTramite(tramiteDictamen.addNewIdentificacionTramite(), htEJGs, DTE_TD_IT_ESTADOEXPEDIENTE_CDA, DTE_TD_IT_FECHAESTADO);
		rellenaTipoElementoTipificadoEstandar(tramiteDictamen.addNewTipoDictamen(), (String)htEJGs.get(DTE_TD_TIPODICTAMEN_CDA));
		rellenaTipoElementoTipificadoEstandar(tramiteDictamen.addNewMotivoDictamen(), (String)htEJGs.get(DTE_TD_MOTIVODICTAMEN_CDA));
		tramiteDictamen.setIntervaloIngresosRecursos((String)htEJGs.get(DTE_TD_INTERVALOINGRESOSRECURS));
		tramiteDictamen.setObservacionesDictamen((String)htEJGs.get(DTE_TD_OBSERVACIONESDICTAMEN));
		
		TramiteResolucion tramiteResolucion = datosTramitacionExpediente.addNewTramiteResolucion();
		identificacionTramite(tramiteResolucion.addNewIdentificacionTramite(), htEJGs, DTE_TR_IT_ESTADOEXPEDIENTE_CDA, DTE_TR_IT_FECHAESTADO);
		tramiteResolucion.setIdentificadorResolucion((String)htEJGs.get(DTE_TR_IDENTIFICADORRESOLUCION));
		rellenaTipoElementoTipificadoEstandar(tramiteResolucion.addNewTipoResolucion(), (String)htEJGs.get(DTE_TR_TIPORESOLUCION_CDA));		
		
		rellenaTipoElementoTipificadoEstandar(tramiteResolucion.addNewMotivoResolucion(), (String)htEJGs.get(DTE_TR_MOTIVORESOLUCION_CDA));
		tramiteResolucion.setIntervaloIngresosRecursos((String)htEJGs.get(DTE_TR_INTERVALOINGRESOSRECURS));
		
		Vector datos = cajgEJGRemesaAdm.getDatosPrestacionesResolucion(idInstitucion, idRemesa, idTipoEJG, anyo, numero);
		for (int i = 0; i < datos.size(); i++) {
			Hashtable ht = (Hashtable) datos.get(i);
			PrestacionesResolucion prestacionesResolucion = tramiteResolucion.addNewPrestacionesResolucion();
			rellenaTipoElementoTipificadoEstandar(prestacionesResolucion.addNewTipoPrestacion(), (String)ht.get(DTE_TR_PR_TIPOPRESTACION_CDA));
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
		
		Integer valueInt = getInteger((String)ht.get(PD_AD_AD_BAJAABOGADODESIGNADO));
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
		Integer value = getInteger((String)htEJGs.get(DDJ_DEMANDADODEMANDANTE));
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
		Long value = getLong((String)htEJGs.get(C_C_DP_NUMCOLEGIADOPROCURADOR));
		if (value != null) {
			datosProcurador.setNumColegiadoProcurador(value.longValue());
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
		String key = getKey(new Object[]{idInstitucion, anyo, numero, idTipoEJG, idPersona});
		List list = (List) htDocumentacionExpediente.get(key);
		
		
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Hashtable htDoc = (Hashtable)list.get(i);				
				documentacionExpediente(tipoDocumentacionExpediente.addNewDocumentacion(), htDoc, F_F_DE_D_TIPODOCUMENTACION_CDA
						, F_F_DE_D_DD_TIPODOCUMENTO_CDA, F_F_DE_D_DD_FECHAPRESENTACIDO, F_F_DE_D_DD_DESCRIPCIONAMPLIAD, F_F_DE_D_DD_PROCEDENTE);
			}
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
		String key = getKey(new Object[]{idInstitucion, anyo, numero, idTipoEJG, idPersona});
		List list = (List) htDocumentacionExpediente.get(key);
		
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable)list.get(i);
				documentacionExpediente(tipoDocumentacionExpediente.addNewDocumentacion(), ht, DS_DE_D_TIPODOCUMENTACION_CDA, 
						DS_DE_D_DD_TIPODOCUMENTO_CDA, DS_DE_D_DD_FECHAPRESENTACIONDO, DS_DE_D_DD_DESCRIPCIONAMPLIADA, DS_DE_D_DD_PROCEDENTE);
			}
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
		Integer valueInt = getInteger((String) htEJGs.get(DS_DEP_I_ACUMULABLE));
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
		valueInt = getInteger((String) htEJGs.get(DS_DEP_PBI_ACUMULABLE));
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
		valueInt = getInteger((String) htEJGs.get(DS_DEP_PBM_ACUMULABLE));
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

		valueInt = getInteger((String) htEJGs.get(DS_DEP_PBO_ACUMULABLE));
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
		Integer valueInt = getInteger((String)htDocumentacion.get(de_dd_procedente));
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
		datosDomicilio.setCodigoPostal((String)htEJGs.get(dop_dd_codigopostal));
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
	private Integer getInteger(String value) {
		if (value != null && !value.trim().equals("")) {
			return Integer.valueOf(value);
		}
		return null;
	}

	/**
	 * 
	 * @param fecha
	 * @return
	 * @throws Exception
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
		if (value != null && !value.trim().equals("")) {
			String[] array = value.split("##");
			if (array != null && array.length > 0) {
				tipoElementoTipificadoEstandar.setCodigo(array[0]);
				if (array.length > 1) {
					tipoElementoTipificadoEstandar.setDescripcion(array[1]);
				}
				if (array.length > 2) {
					tipoElementoTipificadoEstandar.setAbreviatura(array[2]);
				}
			}
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
		if (value != null && !value.trim().equals("")) {
			String[] array = value.split("##");
			if (array != null && array.length > 0) {
				municipio2.setCodigo(array[0]);
				if (array.length > 1) {
					municipio2.setDescripcion(array[1]);
				}
				if (array.length > 2) {
					municipio2.setAbreviatura(array[2]);
				}
			}
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
		
		CodigoExpediente codigoExpediente = datosExpediente.addNewCodigoExpediente();
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
		
		String key = getKey(new Object[]{idInstitucion, anyo, numero, idTipoEJG});
		List list = (List) htMarcasExpediente.get(key);
		
		for (int i = 0; i < list.size(); i++) {
			Hashtable ht = (Hashtable) list.get(i);
			MarcasExpediente marcasExpediente = datosExpediente.addNewMarcasExpediente();
			MarcaExpediente marcaExpediente = marcasExpediente.addNewMarcaExpediente();
			
			rellenaTipoElementoTipificadoEstandar(marcaExpediente.addNewMarcaExpediente(), (String)ht.get(DE_ME_ME_MARCAEXPEDIENTE_CDA));
			marcaExpediente.setValorMarcaExpediente((String)ht.get(DE_ME_ME_VALORMARCAEXPEDIENTE));
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
	 * @param intercambioType
	 * @param numDetalles
	 * @return
	 */
	private InformacionIntercambio getInformacionIntercambio(Intercambio intercambio, int numDetalles, Hashtable ht) throws SIGAException {
				
		InformacionIntercambio informacionIntercambio = intercambio.addNewInformacionIntercambio();
		IdentificacionIntercambio identificacionIntercambio = informacionIntercambio.addNewIdentificacionIntercambio();
		
		if (ht != null) {		
			identificacionIntercambio.setTipoIntercambio((String)ht.get(TIPOINTERCAMBIO));
			rellenaTipoElementoTipificadoIntercambio(identificacionIntercambio.addNewOrigenIntercambio(), (String)ht.get(ORIGENINTERCAMBIO_CDA));
			rellenaTipoElementoTipificadoIntercambio(identificacionIntercambio.addNewDestinoIntercambio(), (String)ht.get(DESTINOINTERCAMBIO_CDA));		
			
			Long valueLong = getLong((String)ht.get(IDENTIFICADORINTERCAMBIO));
			identificacionIntercambio.setIdentificadorIntercambio(valueLong.longValue());		
			identificacionIntercambio.setFechaIntercambio(Calendar.getInstance());
			identificacionIntercambio.setNumeroDetallesIntercambio(numDetalles);
			identificacionIntercambio.setVersionPCAJG((String)ht.get(VERSION_PCAJG));
		}
		return informacionIntercambio;
	}

}