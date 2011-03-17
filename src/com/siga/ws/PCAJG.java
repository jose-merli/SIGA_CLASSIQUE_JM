package com.siga.ws;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.SimpleChain;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.http.HTTPTransport;
import org.apache.axis.types.UnsignedInt;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.AxisObjectSerializerDeserializer;
import com.siga.Utilidades.LogHandler;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.beans.CajgEJGRemesaBean;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;
import com.siga.beans.ScsEJGAdm;
import com.siga.beans.ScsEJGBean;
import com.siga.eejg.SignerXMLHandler;
import com.siga.general.SIGAException;
import com.siga.gratuita.action.DefinirRemesasCAJGAction;
import com.siga.informes.MasterWords;
import com.siga.pcajg.ws.PCAJGBindingStub;
import com.siga.pcajg.ws.PCAJGLocator;
import com.siga.pcajg.ws.xsd.ContrarioType;
import com.siga.pcajg.ws.xsd.DatosDomicilio;
import com.siga.pcajg.ws.xsd.DatosDomicilioMunicipio;
import com.siga.pcajg.ws.xsd.DatosDomicilioMunicipioMunicipio;
import com.siga.pcajg.ws.xsd.DocumentacionType;
import com.siga.pcajg.ws.xsd.DocumentacionTypeDatosDocumento;
import com.siga.pcajg.ws.xsd.FamiliarType;
import com.siga.pcajg.ws.xsd.IntercambioTipo;
import com.siga.pcajg.ws.xsd.IntercambioTipoInformacionIntercambio;
import com.siga.pcajg.ws.xsd.MarcaExpedienteType;
import com.siga.pcajg.ws.xsd.TipoAbogadoDesignado;
import com.siga.pcajg.ws.xsd.TipoCodigoExpediente;
import com.siga.pcajg.ws.xsd.TipoDatosContacto;
import com.siga.pcajg.ws.xsd.TipoDatosPersona;
import com.siga.pcajg.ws.xsd.TipoDatosProcurador;
import com.siga.pcajg.ws.xsd.TipoDomiciliosPersona;
import com.siga.pcajg.ws.xsd.TipoElementoTipificadoEstandar;
import com.siga.pcajg.ws.xsd.TipoElementoTipificadoIntercambio;
import com.siga.pcajg.ws.xsd.TipoExpediente;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosAsistenciaDetenido;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosAsistenciaDetenidoAbogadoAsistencia;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosDefensaJudicial;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosExpediente;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosExpedienteCodigoExpedienteServicio;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosRepresentante;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosSolicitante;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosSolicitanteDatosEconomicosPersona;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosSolicitanteDatosEconomicosPersonaIngresos;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosSolicitanteDatosEconomicosPersonaPropiedadesBienesInmuebles;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosSolicitanteDatosEconomicosPersonaPropiedadesBienesMuebles;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosSolicitanteDatosEconomicosPersonaPropiedadesBienesOtros;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosTramitacionExpediente;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosTramitacionExpedienteTramiteArchivo;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosTramitacionExpedienteTramiteDictamen;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosTramitacionExpedienteTramiteResolucion;
import com.siga.pcajg.ws.xsd.TipoExpedienteDatosTramitacionExpedienteTramiteResolucionPrestacionesResolucion;
import com.siga.pcajg.ws.xsd.TipoExpedienteProfesionalesDesignados;
import com.siga.pcajg.ws.xsd.TipoExpedienteProfesionalesDesignadosAbogadosDesignados;
import com.siga.pcajg.ws.xsd.TipoExpedienteProfesionalesDesignadosProcuradorDesignado;
import com.siga.pcajg.ws.xsd.TipoIdentificacionIntercambio;
import com.siga.pcajg.ws.xsd.TipoIdentificacionTramite;
import com.siga.pcajg.ws.xsd.TipoInformacion;
import com.siga.pcajg.ws.xsd.TipoInformacionRespuesta;
import com.siga.pcajg.ws.xsd.TipoInformacionRespuestaResultado;
import com.siga.pcajg.ws.xsd.TipoInformacionRespuestaResultadoDatosError;
import com.siga.pcajg.ws.xsd.TipoInformacionRespuestaResultadoDatosErrorErrorContenido;
import com.siga.pcajg.ws.xsd.TipoInformacionRespuestaResultadoDatosErrorErrorContenidoDetalleError;





/**
 * @author angelcpe
 *
 */
public class PCAJG extends SIGAWSClientAbstract implements PCAJGConstantes {
		
	protected static enum SUBTIPOCAJG {
		DESCARGA_FICHERO,
		ENVIO_WEBSERVICE
	}
	
	private SUBTIPOCAJG subTipoCAJG = SUBTIPOCAJG.ENVIO_WEBSERVICE;
	
	public PCAJG(SUBTIPOCAJG subTipoCAJG) {
		super();
		this.subTipoCAJG = subTipoCAJG;
	}

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
	private Map htDelitos = new Hashtable();
		
		
	/**
	 * Metodo que genera el xml en el directorio padre que le pases por parametro
	 * @param dirPadre
	 * @return
	 * @throws Exception
	 */
	private List<File> generaFicherosXML(String dirFicheros) throws Exception {
		
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
		
		Vector datosDelitos = cajgEJGRemesaAdm.getDelitos(getIdInstitucion(), getIdRemesa());
		construyeHTxEJG(datosDelitos, htDelitos);
		
		Hashtable ht = null;
		String tipoIntercambio = "";
		
		IntercambioTipo intercambio = null;
		List<TipoExpediente> expedientes = null;
		
		TipoInformacion tipoInformacion = null;
		
		int sufijoIdIntercambio = 0;
		
		for (int i = 0; i < datos.size(); i++) {
			ht = (Hashtable)datos.get(i);
			
			if (!tipoIntercambio.equals(ht.get(TIPOINTERCAMBIO))) {								
				if (intercambio != null && expedientes.size() > 0) {
					File file = creaFichero(dirFicheros, intercambio, tipoInformacion, expedientes);
					if (file != null) {
						ficheros.add(file);
					}
				}
				expedientes = new ArrayList<TipoExpediente>();
				intercambio = new IntercambioTipo();
				
				IntercambioTipoInformacionIntercambio informacionIntercambio = rellenaInformacionIntercambio(ht, sufijoIdIntercambio++);
				intercambio.setInformacionIntercambio(informacionIntercambio);
				tipoInformacion = new TipoInformacion();
				informacionIntercambio.setInformacion(tipoInformacion);				
			}
			
			tipoIntercambio = (String) ht.get(TIPOINTERCAMBIO);
			try {
				addExpediente(expedientes, ht, tipoIntercambio);				
			} catch (IllegalArgumentException e) {
				escribeErrorExpediente(anyo, numejg, numero, idTipoEJG, e.getMessage());
			}
		}
		if (intercambio != null && expedientes.size() > 0) {
			File file = creaFichero(dirFicheros, intercambio, tipoInformacion, expedientes);
			if (file != null) {
				ficheros.add(file);
			}
		}
						
		return ficheros;
	}
	
	private File creaFichero(String dirFicheros, IntercambioTipo intercambio, TipoInformacion tipoInformacion, List<TipoExpediente> expedientes) throws Exception {
		
		File file = new File(dirFicheros);
		file.mkdirs();
		
		tipoInformacion.setExpedientes(expedientes.toArray(new TipoExpediente[expedientes.size()]));
		
		TipoIdentificacionIntercambio tipoIdentificacionIntercambio = intercambio.getInformacionIntercambio().getIdentificacionIntercambio();
		tipoIdentificacionIntercambio.setNumeroDetallesIntercambio(new UnsignedInt(expedientes.size()));
		
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
		
		String xml = AxisObjectSerializerDeserializer.serializeAxisObject(intercambio, true, true);
		
		ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document xmldoc = builder.parse(is);
		
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
		
		if (getUrlWS() == null || getUrlWS().trim().equals("")) {
			throw new SIGAException("Falta especificar la url del webservice para la institución " + getIdInstitucion());
		}
		
		if (SUBTIPOCAJG.ENVIO_WEBSERVICE.equals(subTipoCAJG) && expedientes.size() > 0) {
			
			PCAJGLocator locator = new PCAJGLocator(createClientConfig());
			URL url = new URL(getUrlWS());	
			PCAJGBindingStub stub = new PCAJGBindingStub(url, locator);
			IntercambioTipo intercambioRespuesta = null;
			try {
				intercambioRespuesta = stub.enviarIntercambio(intercambio);			
			} catch (Exception e) {
				file = null;
				ClsLogging.writeFileLogError("Se ha producido un error en el envío del webservice \"" + getUrlWS() + "\" para el colegio " + getIdInstitucion(), e, 3);
				if (e.getCause() instanceof ConnectException) {
					ClsLogging.writeFileLogError("Error de conexión con el webservice \"" + getUrlWS() + "\" para el colegio " + getIdInstitucion(), e, 3);
				}
			}
			if (intercambioRespuesta != null) {
				try {
					trataRespuesta(intercambioRespuesta);
				} catch (Exception e) {
					ClsLogging.writeFileLogError("Error al tratar la respuesta webservice", e, 3);
				}
			} else {
				ClsLogging.writeFileLog("Se ha obtenido un objeto null como respuesta webservice", 3);
			}
		}
		return file;
		
	}
	
	private void trataRespuesta(IntercambioTipo intercambioRespuesta) throws Exception {
		
		IntercambioTipoInformacionIntercambio informacionIntercambio = intercambioRespuesta.getInformacionIntercambio();
		TipoInformacionRespuesta informacionRespuesta = informacionIntercambio.getInformacion().getRespuesta();
		TipoInformacionRespuestaResultado intercambioErroneo = informacionRespuesta.getResultado();
		
		TipoInformacionRespuestaResultadoDatosError informacionRespuestaResultadoDatosError = intercambioErroneo.getDatosError();
		
		TipoIdentificacionIntercambio identificacionIntercambio = informacionRespuesta.getIdentificacionIntercambio();
		String idInstitucion = identificacionIntercambio.getOrigenIntercambio().getCodigo();
				
		if (!String.valueOf(getIdInstitucion()).equals(idInstitucion)) {
			escribeLogRemesa("La institucion del fichero es nula o distinta a la del usuario de SIGA");
			throw new ClsExceptions("La institucion del fichero es distinta a la del usuario de SIGA");
		}
				
		CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
		ScsEJGAdm scsEJGAdm = new ScsEJGAdm(getUsrBean());
		CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(getUsrBean());
		
		UserTransaction tx = getUsrBean().getTransaction();
		tx.begin();
		
		cajgRespuestaEJGRemesaAdm.eliminaAnterioresErrores(getIdInstitucion(), getIdRemesa());
		
		TipoElementoTipificadoEstandar errorGeneral = informacionRespuestaResultadoDatosError.getErrorGeneral();
		if (errorGeneral != null) {
			escribeLogRemesa("Error general WebService: [" + errorGeneral.getCodigo() + "] " + errorGeneral.getDescripcion());
		}
		
		TipoInformacionRespuestaResultadoDatosErrorErrorContenido[] erroresContenido = informacionRespuestaResultadoDatosError
				.getErrorContenido();

		if (erroresContenido != null) {
			
			for (TipoInformacionRespuestaResultadoDatosErrorErrorContenido errorContenido : erroresContenido) {
				TipoCodigoExpediente expedientError = errorContenido.getCodigoExpedienteError();
				int anioExp = expedientError.getAnyoExpediente().intValue();
				String numExp = expedientError.getNumExpediente();
				
				Vector vectorEJGs = scsEJGAdm.select(" WHERE " + ScsEJGBean.C_IDINSTITUCION + " = " + idInstitucion +
						" AND " + ScsEJGBean.C_ANIO   + " = " + anioExp +
						" AND TO_NUMBER(" +  ScsEJGBean.C_NUMEJG + ") = " + Integer.parseInt(numExp.trim()));
				
				if (vectorEJGs != null) {
					if (vectorEJGs.size() == 0) {
						// EJG no encontrado
						escribeLogRemesa("No se ha encontrado el EJG año/número = "	+ anioExp + "/" + numExp);
					} else if (vectorEJGs.size() > 1) {
						// se ha encontrado mas de un EJG
						escribeLogRemesa("Se ha encontrado más de un EJG con el año/número = " + anioExp + "/" + numExp);
					} else {
						// EJG encontrado
						ScsEJGBean scsEJGBean = (ScsEJGBean) vectorEJGs.get(0);
						TipoInformacionRespuestaResultadoDatosErrorErrorContenidoDetalleError[] detallErrors = errorContenido.getDetalleError();

						Hashtable<String, Object> hashEjgRem = new Hashtable<String, Object>();
						hashEjgRem.put(CajgEJGRemesaBean.C_IDINSTITUCION, idInstitucion);
						hashEjgRem.put(CajgEJGRemesaBean.C_ANIO, scsEJGBean.getAnio());
						hashEjgRem.put(CajgEJGRemesaBean.C_NUMERO, scsEJGBean.getNumero());
						hashEjgRem.put(CajgEJGRemesaBean.C_IDTIPOEJG, scsEJGBean.getIdTipoEJG());

						hashEjgRem.put(CajgEJGRemesaBean.C_IDINSTITUCIONREMESA,	idInstitucion);
						hashEjgRem.put(CajgEJGRemesaBean.C_IDREMESA, getIdRemesa());

						Vector vectorRemesa = cajgEJGRemesaAdm.select(hashEjgRem);
						
						if (vectorRemesa.size() == 0) {
							escribeLogRemesa("No se ha encontrado el EJG año/número = " + anioExp + "/" + numExp + " en la remesa");
						} else if (vectorRemesa.size() > 1) {
							escribeLogRemesa("Se ha encontrado más de un EJG año/número = " + anioExp + "/" + numExp + " en la remesa");
						} else {
							CajgEJGRemesaBean cajgEJGRemesaBean = (CajgEJGRemesaBean) vectorRemesa.get(0);

							for (TipoInformacionRespuestaResultadoDatosErrorErrorContenidoDetalleError detallError : detallErrors) {
								StringBuffer descripcion = new StringBuffer("<b>Campo:</b> " + detallError.getCampoError());
								if (detallError.getError() != null) {
									descripcion.append("<br>");
									descripcion.append("<b>Error:</b> " + detallError.getError().getDescripcion());
								}
								
								CajgRespuestaEJGRemesaBean cajgRespuestaEJGRemesaBean = new CajgRespuestaEJGRemesaBean();
								cajgRespuestaEJGRemesaBean.setIdEjgRemesa(cajgEJGRemesaBean.getIdEjgRemesa());
								cajgRespuestaEJGRemesaBean.setCodigo("-1");
								cajgRespuestaEJGRemesaBean.setDescripcion(descripcion.toString());
								cajgRespuestaEJGRemesaBean.setFecha("SYSDATE");

								cajgRespuestaEJGRemesaAdm.insert(cajgRespuestaEJGRemesaBean);
							}
						}
					}

				}
			}

		}
		tx.commit();
		
	}

	/**
	 * 
	 * @return
	 */
	private EngineConfiguration createClientConfig() {
		SimpleProvider clientConfig = new SimpleProvider();
		Handler logSIGAasignaHandler = (Handler) new LogHandler();
		Handler signerXMLHandler = null;
		SimpleChain reqHandler = new SimpleChain();
		SimpleChain respHandler = new SimpleChain();
		if (isFirmarXML()) {
			signerXMLHandler = (Handler) new SignerXMLHandler();
			reqHandler.addHandler(signerXMLHandler);
		}
		reqHandler.addHandler(logSIGAasignaHandler);
		respHandler.addHandler(logSIGAasignaHandler);
		if (isFirmarXML()) {
			respHandler.addHandler(signerXMLHandler);
		}
		Handler pivot = (Handler) new HTTPSender();
		Handler transport = new SimpleTargetedChain(reqHandler, pivot, respHandler);
		clientConfig.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME, transport);
		return clientConfig;
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
	 * @param expedientes 
	 * @param expedientesType
	 * @param htEJGs
	 * @return
	 * @throws Exception
	 */
	private void addExpediente(List<TipoExpediente> expedientes, Hashtable htEJGs, String tipoIntercambio) throws Exception {		
		
		TipoExpediente expediente = new TipoExpediente();
		idTipoEJG = (String)htEJGs.get(IDTIPOEJG);
		anyo = (String)htEJGs.get(ANIO);
		numero = (String)htEJGs.get(NUMERO);
		numejg = (String)htEJGs.get(NUMEJG);
		
		expediente.setDatosExpediente(datosExpediente(htEJGs));
		expediente.setProfesionalesDesignados(profesionalesDesignados(htEJGs));
		expediente.setDatosSolicitante(datosSolicitante(htEJGs));
		TipoExpedienteDatosRepresentante datosRepresentante = datosRepresentante(htEJGs);
		if (datosRepresentante != null) {
			expediente.setDatosRepresentante(datosRepresentante);
		}
		
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG});
		List list = (List) htFamiliares.get(key);
		if (list != null && list.size() > 0) {	
			
			FamiliarType[] tipoExpedienteFamiliaresFamiliars = new FamiliarType[list.size()];
			
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable) list.get(i);			
				tipoExpedienteFamiliaresFamiliars[i] = datosFamiliares(ht);
			}
			expediente.setFamiliares(tipoExpedienteFamiliaresFamiliars);
		}
				
		list = (List)htContrarios.get(key);		
		if (list != null && list.size() > 0) {
			ContrarioType[] tipoExpedienteContrariosContrarios = new ContrarioType[list.size()];
			
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable) list.get(i);
				tipoExpedienteContrariosContrarios[i] = datosContrario(ht);
			}
			expediente.setContrarios(tipoExpedienteContrariosContrarios);
		}
		
		expediente.setDatosDefensaJudicial(datosDefensaJudicial(htEJGs));
		TipoExpedienteDatosAsistenciaDetenido datosAsistenciaDetenido = datosAsistenciaDetenido(htEJGs);
		if (datosAsistenciaDetenido != null) {
			expediente.setDatosAsistenciaDetenido(datosAsistenciaDetenido);
		}
		if (!tipoIntercambio.equals(INTERCAMBIO_ALTA_PRESENTACION)) {
			datosTramitacionExpediente(expediente, htEJGs, tipoIntercambio);
		}
		
		try {
			//comprobamos que se puede serializar
			AxisObjectSerializerDeserializer.serializeAxisObject(expediente, true, true);
			expedientes.add(expediente);
		} catch (Exception e) {
			
			String texto = "No se ha completado correctamente la información del expediente";
			ClsLogging.writeFileLogError("No se ha completado correctamente la información del expediente para el colegio " + getIdInstitucion(), e, 3);
			String mensaje = e.getMessage();
			if (e.getCause() != null) {
				mensaje = e.getCause().getMessage();
			}
			if (mensaje != null) {
				if (mensaje.indexOf("Non nillable element") > -1) {
					texto = "Debe rellenar el campo \"" + mensaje.substring(mensaje.indexOf("'")+1, mensaje.lastIndexOf("'")) + "\"";
				}
			}
			escribeErrorExpediente(anyo, numejg, numero, idTipoEJG, texto);
		}
	
	}
	
	/**
	 * Añade profesionales designados a tu fichero xml
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception 
	 * @throws Exception
	 */
	private TipoExpedienteProfesionalesDesignados profesionalesDesignados(Hashtable htEJGs) throws Exception {
		TipoExpedienteProfesionalesDesignados profesionalesDesignados = new TipoExpedienteProfesionalesDesignados();
		
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG});
		List list = (List) htAbogadosDesignados.get(key);
		
		if (list != null && list.size() > 0) {
			TipoExpedienteProfesionalesDesignadosAbogadosDesignados abogadosDesignados = new TipoExpedienteProfesionalesDesignadosAbogadosDesignados();
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable) list.get(i);
				abogadosDesignados.setAbogadoDesignado(abogadoDesignado(ht));
				profesionalesDesignados.setAbogadosDesignados(abogadosDesignados);
			}
			profesionalesDesignados.setAbogadosDesignados(abogadosDesignados);
		}
		
		TipoExpedienteProfesionalesDesignadosProcuradorDesignado procuradorDesignado = new TipoExpedienteProfesionalesDesignadosProcuradorDesignado();		
		Integer valueInt = getInteger("baja procurador designado", (String)htEJGs.get(PD_PD_BAJAPROCURADORDESIGNADO));
		if (valueInt != null) {
			procuradorDesignado.setBajaProcuradorDesignado(BigInteger.valueOf(valueInt));
		}
				
		TipoDatosProcurador datosProcurador = new TipoDatosProcurador();
		String colegioProcurador = getString((String)htEJGs.get(PD_PD_DP_COLEGIOPROCURADOR));
		boolean tieneProcurador = false;
		if (colegioProcurador != null) {
			tieneProcurador = true;
			datosProcurador.setColegioProcurador(colegioProcurador);
		}
		Integer valueInteger = getInteger("número de colegiado del procurador", (String)htEJGs.get(PD_PD_DP_NUMCOLEGIADOPROCURADO));
		if (valueInteger != null) {
			tieneProcurador = true;
			datosProcurador.setNumColegiadoProcurador(valueInteger.intValue());
		}
		String st = getString((String)htEJGs.get(PD_PD_DP_NOMBRECOMPLETOPROCURA));
		if (st != null) {
			tieneProcurador = true;
			datosProcurador.setNombreCompletoProcurador(st);
		}
		if (tieneProcurador) {
			procuradorDesignado.setDatosProcurador(datosProcurador);
		}
		
		Date cal = getCalendar((String)htEJGs.get(PD_PD_FECHADESIGNACIONPROCURAD));
		if (cal != null) {
			procuradorDesignado.setFechaDesignacionProcurador(cal);
		}
		valueInteger = getInteger("número de designación del procurador", (String)htEJGs.get(PD_PD_NUMDESIGNACIONPROCURADOR));
		if (valueInteger != null) {
			procuradorDesignado.setNumDesignacionProcurador(new UnsignedInt(valueInteger));
		}
		profesionalesDesignados.setProcuradorDesignado(procuradorDesignado);
		
		if (procuradorDesignado.getDatosProcurador() == null) {
			profesionalesDesignados = null;
		}
		return profesionalesDesignados;
	}

	/**
	 * Añade datos de tramitacion expediente a tu fichero xml
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private TipoExpedienteDatosTramitacionExpediente datosTramitacionExpediente(TipoExpediente tipoExpediente, Hashtable htEJGs, String tipoIntercambio) throws Exception {
		TipoExpedienteDatosTramitacionExpediente datosTramitacionExpediente = new TipoExpedienteDatosTramitacionExpediente();
		
		if (tipoIntercambio.equals(INTERCAMBIO_EXPEDIENTES_ARCHIVADOS)) {
			TipoExpedienteDatosTramitacionExpedienteTramiteArchivo tramiteArchivo = new TipoExpedienteDatosTramitacionExpedienteTramiteArchivo();
			TipoIdentificacionTramite identificacionTramite = identificacionTramite(htEJGs, DTE_TA_IT_ESTADOEXPEDIENTE_CDA, DTE_TA_IT_FECHAESTADO);
			
			if (identificacionTramite != null) {
				tramiteArchivo.setIdentificacionTramite(identificacionTramite);
			}
		} else if (tipoIntercambio.equals(INTERCAMBIO_EXPEDIENTES_DICTAMINADOS)) {
			TipoExpedienteDatosTramitacionExpedienteTramiteDictamen tramiteDictamen = new TipoExpedienteDatosTramitacionExpedienteTramiteDictamen();
			
			TipoIdentificacionTramite identificacionTramite = identificacionTramite(htEJGs, DTE_TD_IT_ESTADOEXPEDIENTE_CDA, DTE_TD_IT_FECHAESTADO);
			if (identificacionTramite != null) {
				tramiteDictamen.setIdentificacionTramite(identificacionTramite);
			}
			TipoElementoTipificadoEstandar tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(DTE_TD_TIPODICTAMEN_CDA));
			if (tipoElementoTipificadoEstandar != null) {
				tramiteDictamen.setTipoDictamen(tipoElementoTipificadoEstandar);
			}
			
			tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(DTE_TD_MOTIVODICTAMEN_CDA));
			if (tipoElementoTipificadoEstandar != null) {
				tramiteDictamen.setMotivoDictamen(tipoElementoTipificadoEstandar);
			}
			tramiteDictamen.setIntervaloIngresosRecursos((String)htEJGs.get(DTE_TD_INTERVALOINGRESOSRECURS));
			tramiteDictamen.setObservacionesDictamen((String)htEJGs.get(DTE_TD_OBSERVACIONESDICTAMEN));
		} else if (tipoIntercambio.equals(INTERCAMBIO_RESOLUCIONES)) {	
			TipoExpedienteDatosTramitacionExpedienteTramiteResolucion tramiteResolucion = new TipoExpedienteDatosTramitacionExpedienteTramiteResolucion();
			
			TipoIdentificacionTramite identificacionTramite = identificacionTramite(htEJGs, DTE_TR_IT_ESTADOEXPEDIENTE_CDA, DTE_TR_IT_FECHAESTADO);
			if (identificacionTramite != null) {
				tramiteResolucion.setIdentificacionTramite(identificacionTramite);
			}
			tramiteResolucion.setIdentificadorResolucion((String)htEJGs.get(DTE_TR_IDENTIFICADORRESOLUCION));
			
			TipoElementoTipificadoEstandar tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(DTE_TR_TIPORESOLUCION_CDA));
			if (tipoElementoTipificadoEstandar != null) {
				tramiteResolucion.setTipoResolucion(tipoElementoTipificadoEstandar);
			}
			tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(DTE_TR_MOTIVORESOLUCION_CDA));
			if (tipoElementoTipificadoEstandar != null) {
				tramiteResolucion.setMotivoResolucion(tipoElementoTipificadoEstandar);
			}
			tramiteResolucion.setIntervaloIngresosRecursos((String)htEJGs.get(DTE_TR_INTERVALOINGRESOSRECURS));
			
			CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
			Vector datos = cajgEJGRemesaAdm.getDatosPrestacionesResolucion(getIdInstitucion(), getIdRemesa(), idTipoEJG, anyo, numero);
			if (datos != null && datos.size() > 0) {				
				for (int i = 0; i < datos.size(); i++) {
					Hashtable ht = (Hashtable) datos.get(i);
					TipoExpedienteDatosTramitacionExpedienteTramiteResolucionPrestacionesResolucion prestacionResolucion = new TipoExpedienteDatosTramitacionExpedienteTramiteResolucionPrestacionesResolucion(); 					
					prestacionResolucion.setTipoPrestacion(rellenaTipoElementoTipificadoEstandar((String)ht.get(DTE_TR_PR_TIPOPRESTACION_CDA)));
					tramiteResolucion.setPrestacionesResolucion(i, prestacionResolucion);
				}
			}
		}
		return datosTramitacionExpediente;
	}

	/**
	 * Añade identificacion de tramite a tu fichero xml
	 * @param identificacionTramiteType
	 * @param htEJGs
	 * @param it_estadoexpediente_cda
	 * @param it_fechaestado
	 * @throws Exception
	 */
	private TipoIdentificacionTramite identificacionTramite(Hashtable htEJGs, String it_estadoexpediente_cda, String it_fechaestado) throws Exception {
		TipoIdentificacionTramite identificacionTramite = new TipoIdentificacionTramite();
		
		TipoElementoTipificadoEstandar tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(it_estadoexpediente_cda));
		if (tipoElementoTipificadoEstandar != null) {
			identificacionTramite.setEstadoExpediente(tipoElementoTipificadoEstandar);
		}
		Date cal = getCalendar((String)htEJGs.get(it_fechaestado));
		if (cal != null) {
			identificacionTramite.setFechaEstado(cal);
		}
		return identificacionTramite;
	}

	/**
	 * Añade datos asistente detenido a tu fichero xml
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private TipoExpedienteDatosAsistenciaDetenido datosAsistenciaDetenido(Hashtable htEJGs) throws Exception {
		TipoExpedienteDatosAsistenciaDetenido datosAsistenciaDetenido = new TipoExpedienteDatosAsistenciaDetenido();
		boolean tieneDatos = false;
		String st = getString((String)htEJGs.get(DAD_NUMEROATESTADO));
		if (st != null) {
			tieneDatos = true;
			datosAsistenciaDetenido.setNumeroAtestado(st);
		}
		Date date = getCalendar((String)htEJGs.get(DAD_FECHAASISTENCIAATESTADO));
		if (date != null) {
			tieneDatos = true;
			datosAsistenciaDetenido.setFechaAsistenciaAtestado(date);
		}
		TipoElementoTipificadoEstandar tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(DAD_CENTRODETENCION_CDA));
		if (tipoElementoTipificadoEstandar != null) {
			tieneDatos = true;
			datosAsistenciaDetenido.setCentroDetencion(tipoElementoTipificadoEstandar);
		}
		st = getString((String)htEJGs.get(DAD_NUMERODILIGENCIA));
		if (st != null) {
			tieneDatos = true;
			datosAsistenciaDetenido.setNumeroDiligencia(st);
		}
		date = getCalendar((String)htEJGs.get(DAD_FECHAASISTENCIADILIGENCIA));
		if (date != null) {
			tieneDatos = true;
			datosAsistenciaDetenido.setFechaAsistenciaDiligencia(date);
		}
		tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(DAD_ORGANOJUDICIAL_CDA));
		if (tipoElementoTipificadoEstandar != null) {
			tieneDatos = true;
			datosAsistenciaDetenido.setOrganoJudicial(tipoElementoTipificadoEstandar);
		}
		String dato = getString((String)htEJGs.get(DAD_AA_NUMCOLEGIADOABOGADO));
		if (dato != null) {
			tieneDatos = true;
			TipoExpedienteDatosAsistenciaDetenidoAbogadoAsistencia abogadoAsistencia = new TipoExpedienteDatosAsistenciaDetenidoAbogadoAsistencia();
			abogadoAsistencia.setNumColegiadoAbogado(dato);
			datosAsistenciaDetenido.setAbogadoAsistencia(abogadoAsistencia);			
		}
		
		Boolean b = getBoolean((String)htEJGs.get(DAD_ESATESTADO));
		if (b != null) {
			tieneDatos = true;
			datosAsistenciaDetenido.setEsAtestado(b.booleanValue());
		}
		
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG});
		List list = (List) htDelitos.get(key);		
		if (list != null && list.size() > 0) {
			Hashtable ht = (Hashtable)list.get(0);//cogemos el último
			tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)ht.get(DELITO_CDA));
			if (tipoElementoTipificadoEstandar != null) {
				tieneDatos = true;
				datosAsistenciaDetenido.setTipoDelito(tipoElementoTipificadoEstandar);
			}
		}
		
		
		if (!tieneDatos) {
			datosAsistenciaDetenido = null;
		}		
		
		return datosAsistenciaDetenido;
	}

	/**
	 * 
	 * @param abogadosDesignadosType
	 * @param ht
	 * @throws Exception
	 */
	
	private TipoAbogadoDesignado abogadoDesignado(Hashtable ht) throws Exception {	
	
		TipoAbogadoDesignado tipoAbogadoDesignado = new TipoAbogadoDesignado();
		
		String st = getString((String)ht.get(PD_AD_AD_BAJAABOGADODESIGNADO));
		if (st != null) {
			tipoAbogadoDesignado.setBajaAbogadoDesignado(new BigInteger(st));
		}
		
		tipoAbogadoDesignado.setNumColegiadoAbogado((String)ht.get(PD_AD_AD_NUMCOLEGIADOABOGADO));
		Date cal = getCalendar((String)ht.get(PD_AD_AD_FECHADESIGNACIONABOGA));
		if (cal != null) {
			tipoAbogadoDesignado.setFechaDesignacionAbogado(cal);
		}
		Long valueLong = getLong((String)ht.get(PD_AD_AD_NUMDESIGNACIONABOGADO));
		if (valueLong != null) {
			tipoAbogadoDesignado.setNumDesignacionAbogado(new UnsignedInt(valueLong.longValue()));
		}
		st  = (String)ht.get(PD_AD_AD_NOMBRECOMPLETOABO);
		if (st != null) {
			tipoAbogadoDesignado.setNombreCompletoAbogado(st);
		}
		
		return tipoAbogadoDesignado;
	}

	
	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 */
	private TipoExpedienteDatosDefensaJudicial datosDefensaJudicial(Hashtable htEJGs) {
		TipoExpedienteDatosDefensaJudicial datosDefensaJudicial = new TipoExpedienteDatosDefensaJudicial();
		
		TipoElementoTipificadoEstandar tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(DDJ_PARTIDOJUDICIAL_CDA));
		if (tipoElementoTipificadoEstandar != null) {
			datosDefensaJudicial.setPartidoJudicial(tipoElementoTipificadoEstandar);
		}
		
		tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(DDJ_JURISDICCION_CDA));
		if (tipoElementoTipificadoEstandar != null) {
			datosDefensaJudicial.setJurisdiccion(tipoElementoTipificadoEstandar);
		}
		tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(DDJ_ORGANOJUDICIAL_CDA));
		if (tipoElementoTipificadoEstandar != null) {
			datosDefensaJudicial.setOrganoJudicial(tipoElementoTipificadoEstandar);
		}
		tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(DDJ_TIPOPROCEDIMIENTO_CDA));
		if (tipoElementoTipificadoEstandar != null) {
			datosDefensaJudicial.setTipoProcedimiento(tipoElementoTipificadoEstandar);
		}
		String st = getString((String)htEJGs.get(DDJ_IDENTIFICADORPROCEDIMIENTO));
		if (st != null) {
			datosDefensaJudicial.setIdentificadorProcedimiento(st);
		}
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
		
		tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(DDJ_PRECEPTIVO_CDA));
		if (tipoElementoTipificadoEstandar != null) {
			datosDefensaJudicial.setPreceptivo(tipoElementoTipificadoEstandar);
		}
		return datosDefensaJudicial;
	}

	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private ContrarioType datosContrario(Hashtable htEJGs) throws Exception {
		ContrarioType tipoExpedienteContrariosContrario = new ContrarioType();
		
		TipoDatosPersona tipoDatosPersona = datosPersona(htEJGs, C_C_DP_TIPOPERSONA_CDA, C_C_DP_TIPOIDENTIFICACION_CDA, C_C_DP_IDENTIFICACION, C_C_DP_NOMBRE
				, C_C_DP_PRIMERAPELLIDO, C_C_DP_SEGUNDOAPELLIDO, C_C_DP_RAZONSOCIAL, C_C_DP_FECHANACIMIENTO, C_C_DP_NACIONALIDAD_CDA
				, C_C_DP_SITUACIONLABORAL_CDA, C_C_DP_PROFESION_CDA, C_C_DP_SEXO, C_C_DP_IDIOMACOMUNICACION, C_C_DP_ESTADOCIVIL_CDA
				, C_C_DP_REGIMENECONOMICO_CDA, C_C_DP_NUMHIJOS, C_C_DP_FECHAFORMALIZACION, C_C_DP_TIPOPERSONAJURIDICA_CDA);
		if (tipoDatosPersona != null) {
			tipoExpedienteContrariosContrario.setDatosPersona(tipoDatosPersona);	
		}
		
		TipoDatosProcurador datosProcurador = new TipoDatosProcurador();
		
		datosProcurador.setColegioProcurador((String)htEJGs.get(C_C_DP_COLEGIOPROCURADOR));
		Integer valueInteger = getInteger("número de colegiado del procurador", (String)htEJGs.get(C_C_DP_NUMCOLEGIADOPROCURADOR));
		if (valueInteger != null) {
			datosProcurador.setNumColegiadoProcurador(valueInteger.intValue());
		}
		
		datosProcurador.setNombreCompletoProcurador((String)htEJGs.get(C_C_DP_NOMBRECOMPLETOPROCURADO));
		tipoExpedienteContrariosContrario.setDatosProcurador(datosProcurador);
		
		return tipoExpedienteContrariosContrario;
	}

	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private FamiliarType datosFamiliares(Hashtable htFam) throws Exception {
		
		FamiliarType tipoExpedienteFamiliaresFamiliar = new FamiliarType();
		TipoElementoTipificadoEstandar tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htFam.get(F_F_PARENTESCO_CDA));
		
		if (tipoElementoTipificadoEstandar != null) {
			tipoExpedienteFamiliaresFamiliar.setParentesco(tipoElementoTipificadoEstandar);
		}
		
		Boolean b =  getBoolean((String)htFam.get(F_F_FAMILIARMENOR));
		if (b != null) {
			tipoExpedienteFamiliaresFamiliar.setFamiliarMenor(b.booleanValue());
		}
		
		TipoDatosPersona tipoDatosPersona = datosPersona(htFam, F_F_DP_TIPOPERSONA_CDA, F_F_DP_TIPOIDENTIFICACION_CDA, F_F_DP_IDENTIFICACION, F_F_DP_NOMBRE
				, F_F_DP_PRIMERAPELLIDO, F_F_DP_SEGUNDOAPELLIDO, F_F_DP_RAZONSOCIAL, F_F_DP_FECHANACIMIENTO, F_F_DP_NACIONALIDAD_CDA
				, F_F_DP_SITUACIONLABORAL_CDA, F_F_DP_PROFESION_CDA, F_F_DP_SEXO, F_F_DP_IDIOMACOMUNICACION, F_F_DP_ESTADOCIVIL_CDA
				, F_F_DP_REGIMENECONOMICO_CDA, F_F_DP_NUMHIJOS, F_F_DP_FECHAFORMALIZACION, F_F_DP_TIPOPERSONAJURIDICA_CDA);
		
		if (tipoDatosPersona != null) {
			tipoExpedienteFamiliaresFamiliar.setDatosPersona(tipoDatosPersona);
		}
		
		
		
		String idPersona = (String)htFam.get(IDPERSONA);
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG, idPersona});
		List list = (List) htDocumentacionExpediente.get(key);		
		
		if (list != null && list.size() > 0) {
			
			DocumentacionType[] documentacionExpediente = new DocumentacionType[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Hashtable htDoc = (Hashtable)list.get(i);	
				documentacionExpediente[i] = documentacionExpediente(htDoc, D_TIPODOCUMENTACION_CDA
						, D_TIPODOCUMENTO_CDA, D_FECHAPRESENTACIDO, D_DESCRIPCIONAMPLIAD, D_PROCEDENTE);
			}
		}
		return tipoExpedienteFamiliaresFamiliar;
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
	private TipoExpedienteDatosRepresentante datosRepresentante(Hashtable htEJGs) throws Exception {
		TipoExpedienteDatosRepresentante datosRepresentante = null;
		String st = getString((String)htEJGs.get(DR_DP_TIPOPERSONA_CDA));
		if (st != null) {
			datosRepresentante = new TipoExpedienteDatosRepresentante();
			datosRepresentante.setDatosPersona(datosPersona(htEJGs, DR_DP_TIPOPERSONA_CDA, DR_DP_TIPOIDENTIFICACION_CDA, DR_DP_IDENTIFICACION, DR_DP_NOMBRE
				, DR_DP_PRIMERAPELLIDO, DR_DP_SEGUNDOAPELLIDO, DR_DP_RAZONSOCIAL, DR_DP_FECHANACIMIENTO, DR_DP_NACIONALIDAD_CDA
				, DR_DP_SITUACIONLABORAL_CDA, DR_DP_PROFESION_CDA, DR_DP_SEXO, DR_DP_IDIOMACOMUNICACION, DR_DP_ESTADOCIVIL_CDA
				, DR_DP_REGIMENECONOMICO_CDA, DR_DP_NUMHIJOS, DR_DP_FECHAFORMALIZACION, DR_DP_TIPOPERSONAJURIDICA_CDA));
			datosRepresentante.setDomiciliosPersona(domiciliosPersona(htEJGs, DR_DP_TIPODOMICILIO_CDA
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
				, DR_DP_DD_CODIGOPOSTAL));
		
			TipoDatosContacto datosContacto = datosContacto(htEJGs, DR_DC_NUMEROTELEFONO1, DR_DC_NUMEROTELEFONO2, DR_DC_EMAIL);
			if (datosContacto != null) {
				datosRepresentante.setDatosContacto(datosContacto);
			}
		}
		return datosRepresentante;
	}

	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private TipoExpedienteDatosSolicitante datosSolicitante(Hashtable htEJGs) throws Exception {
		TipoExpedienteDatosSolicitante datosSolicitante = new TipoExpedienteDatosSolicitante();
		
		TipoDatosPersona datosPersona = datosPersona(htEJGs, DS_DP_TIPOPERSONA_CDA, DS_DP_TIPOIDENTIFICACION_CDA, DS_DP_IDENTIFICACION, DS_DP_NOMBRE
				, DS_DP_PRIMERAPELLIDO, DS_DP_SEGUNDOAPELLIDO, DS_DP_RAZONSOCIAL, DS_DP_FECHANACIMIENTO, DS_DP_NACIONALIDAD_CDA
				, DS_DP_SITUACIONLABORAL_CDA, DS_DP_PROFESION_CDA, DS_DP_SEXO, DS_DP_IDIOMACOMUNICACION, DS_DP_ESTADOCIVIL_CDA
				, DS_DP_REGIMENECONOMICO_CDA, DS_DP_NUMHIJOS, DS_DP_FECHAFORMALIZACION, DS_DP_TIPOPERSONAJURIDICA_CDA);
		if (datosPersona != null) {
			datosSolicitante.setDatosPersona(datosPersona);
		}
					
		TipoDomiciliosPersona domiciliosPersona = domiciliosPersona(htEJGs, DS_DP_TIPODOMICILIO_CDA
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
		
		if (domiciliosPersona != null) {
			datosSolicitante.setDomiciliosPersona(domiciliosPersona);
		}
		
		TipoDatosContacto datosContacto = datosContacto(htEJGs, DS_DC_NUMEROTELEFONO1, DS_DC_NUMEROTELEFONO2, DS_DC_EMAIL);
		if (datosContacto != null) {
			datosSolicitante.setDatosContacto(datosContacto);
		}
				
		String idPersona = (String)htEJGs.get(IDPERSONA);		
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG, idPersona});
		List list = (List) htDocumentacionExpediente.get(key);
		
		if (list != null && list.size() > 0) {			
			DocumentacionType documentacionExpediente[] = new DocumentacionType[list.size()]; 
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable)list.get(i);
				documentacionExpediente[i] = documentacionExpediente(ht, D_TIPODOCUMENTACION_CDA, 
						D_TIPODOCUMENTO_CDA, D_FECHAPRESENTACIDO, D_DESCRIPCIONAMPLIAD, D_PROCEDENTE);
			}
			datosSolicitante.setDocumentacionExpediente(documentacionExpediente);
		} 
		TipoExpedienteDatosSolicitanteDatosEconomicosPersona datosEconomicosPersona = new TipoExpedienteDatosSolicitanteDatosEconomicosPersona();		

		TipoExpedienteDatosSolicitanteDatosEconomicosPersonaIngresos ingreso = new TipoExpedienteDatosSolicitanteDatosEconomicosPersonaIngresos();
		boolean tieneIngresos = false;
		
		TipoElementoTipificadoEstandar tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String) htEJGs.get(DS_DEP_I_CONCEPTOINGRESOS_CDA));
		if (tipoElementoTipificadoEstandar != null) {
			tieneIngresos = true;
			ingreso.setConceptoIngresos(tipoElementoTipificadoEstandar);
		}
		
		String st = getString((String) htEJGs.get(DS_DEP_I_OBSERVACIONESINGRESOS));
		if (st != null) {
			tieneIngresos = true;
			ingreso.setObservacionesIngresos(st);
		}
		st = getString((String) htEJGs.get(DS_DEP_I_DESCRIPCIONINGRESOS));
		if (st != null) {
			tieneIngresos = true;
			ingreso.setDescripcionIngresos(st);
		}
		Double value = getDouble((String) htEJGs.get(DS_DEP_I_IMPORTEBRUTO));
		if (value != null) {
			tieneIngresos = true;
			ingreso.setImporteBruto(value.floatValue());
		}
		Integer valueInt = getInteger("acumulable de los ingresos", (String) htEJGs.get(DS_DEP_I_ACUMULABLE));
		if (valueInt != null) {
			tieneIngresos = true;
			ingreso.setAcumulable(valueInt);
		}
		if (tieneIngresos) {
			datosEconomicosPersona.setIngresos(new TipoExpedienteDatosSolicitanteDatosEconomicosPersonaIngresos[]{ingreso});
		}
			
		TipoExpedienteDatosSolicitanteDatosEconomicosPersonaPropiedadesBienesInmuebles propiedadesBienesInmuebles = new TipoExpedienteDatosSolicitanteDatosEconomicosPersonaPropiedadesBienesInmuebles();
		boolean tienePBI = false;
		
		tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String) htEJGs.get(DS_DEP_PBI_TIPOBIENINMUEBL_CDA));
		if (tipoElementoTipificadoEstandar != null) {
			tienePBI = true;
			propiedadesBienesInmuebles.setTipoBienInmueble(tipoElementoTipificadoEstandar);
		}
		
		tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String) htEJGs.get(DS_DEP_PBI_ORIGENVALORACIO_CDA));
		if (tipoElementoTipificadoEstandar != null) {
			tienePBI = true;
			propiedadesBienesInmuebles.setOrigenValoracion(tipoElementoTipificadoEstandar);
		}
		st = getString((String) htEJGs.get(DS_DEP_PBI_CARGAS));
		if (st != null) {
			tienePBI = true;
			propiedadesBienesInmuebles.setCargas(st);
		}
		
		st = getString((String) htEJGs.get(DS_DEP_PBI_OBSERVACIONES));
		if (st != null) {
			tienePBI = true;
			propiedadesBienesInmuebles.setObservaciones(st);
		}
		value = getDouble((String) htEJGs.get(DS_DEP_PBI_VALORACIONECONOMICA));
		if (value != null) {
			tienePBI = true;
			propiedadesBienesInmuebles.setValoracionEconomica(value.floatValue());
		}
		valueInt = getInteger("acumulable de los bienes inmuebles", (String) htEJGs.get(DS_DEP_PBI_ACUMULABLE));
		if (valueInt != null) {
			tienePBI = true;
			propiedadesBienesInmuebles.setAcumulable(valueInt.intValue());
		}
		if (tienePBI) {
			datosEconomicosPersona.setPropiedadesBienesInmuebles(new TipoExpedienteDatosSolicitanteDatosEconomicosPersonaPropiedadesBienesInmuebles[]{propiedadesBienesInmuebles});
		}		
		
		
		TipoExpedienteDatosSolicitanteDatosEconomicosPersonaPropiedadesBienesMuebles propiedadesBienesMuebles = new TipoExpedienteDatosSolicitanteDatosEconomicosPersonaPropiedadesBienesMuebles();
		boolean tienePBM = false;
		
		tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String) htEJGs.get(DS_DEP_PBM_TIPOBIENMUEBLE_CDA));
		if (tipoElementoTipificadoEstandar != null) {
			tienePBM = true;
			propiedadesBienesMuebles.setTipoBienMueble(tipoElementoTipificadoEstandar);
		}
		st = getString((String) htEJGs.get(DS_DEP_PBM_OBSERVACIONES));
		if (st != null) {
			tienePBM = true;
			propiedadesBienesMuebles.setObservaciones(st);
		}
		value = getDouble((String) htEJGs.get(DS_DEP_PBM_VALORACIONECONOMICA));
		if (value != null) {
			tienePBM = true;
			propiedadesBienesMuebles.setValoracionEconomica(value.floatValue());
		}
		valueInt = getInteger("acumulable de los bienes muebles", (String) htEJGs.get(DS_DEP_PBM_ACUMULABLE));
		if (valueInt != null) {
			tienePBM = true;
			propiedadesBienesMuebles.setAcumulable(valueInt.intValue());
		}
		if (tienePBM) {
			datosEconomicosPersona.setPropiedadesBienesMuebles(new TipoExpedienteDatosSolicitanteDatosEconomicosPersonaPropiedadesBienesMuebles[]{propiedadesBienesMuebles});
		}
		
		TipoExpedienteDatosSolicitanteDatosEconomicosPersonaPropiedadesBienesOtros propiedadesBienesOtros = new TipoExpedienteDatosSolicitanteDatosEconomicosPersonaPropiedadesBienesOtros();
		boolean tieneBO = false;
		
		st = getString((String) htEJGs.get(DS_DEP_PBO_CARGAS));
		if (st != null) {
			tieneBO = true;
			propiedadesBienesOtros.setCargas(st);
		}
		
		st = getString((String) htEJGs.get(DS_DEP_PBO_DESCRIPCIONBIEN));
		if (st != null) {
			tieneBO = true;
			propiedadesBienesOtros.setDescripcionBien(st);
		}
		st = getString((String) htEJGs.get(DS_DEP_PBO_OBSERVACIONES));
		if (st != null) {
			tieneBO = true;
			propiedadesBienesOtros.setObservaciones(st);
		}
		
		value = getDouble((String) htEJGs.get(DS_DEP_PBO_VALORACIONECONOMICA));
		if (value != null) {
			tieneBO = true;
			propiedadesBienesOtros.setValoracionEconomica(value.floatValue());
		}

		valueInt = getInteger("acumulable de bienes otros",(String) htEJGs.get(DS_DEP_PBO_ACUMULABLE));
		if (valueInt != null) {
			tieneBO = true;
			propiedadesBienesOtros.setAcumulable(valueInt.intValue());
		}
		if (tieneBO) {
			datosEconomicosPersona.setPropiedadesBienesOtros(new TipoExpedienteDatosSolicitanteDatosEconomicosPersonaPropiedadesBienesOtros[]{propiedadesBienesOtros});
		}
		st = getString((String)htEJGs.get(DS_DEP_OTROSDATOSECONOMICOS));
		if (st != null) {
			datosEconomicosPersona.setOtrosDatosEconomicos(st);
		}
		
		if (tieneIngresos || tienePBI || tienePBM || tieneBO || datosEconomicosPersona.getOtrosDatosEconomicos() != null) {
			datosSolicitante.setDatosEconomicosPersona(datosEconomicosPersona);
		}
		
		return datosSolicitante;
	}

	
	/**
	 * 
	 * @param htEJGs
	 * @param de_tipodocumentacion_cda
	 * @param de_dd_tipodocumento_cda
	 * @param de_dd_fechapresentaciondocumento
	 * @param de_dd_descripcionampliadadocumento
	 * @param de_dd_procedente
	 * @throws Exception
	 */
	private DocumentacionType documentacionExpediente(Hashtable htDocumentacion, String de_tipodocumentacion_cda,
			String de_dd_tipodocumento_cda, String de_dd_fechapresentaciondocumento, String de_dd_descripcionampliadadocumento,
			String de_dd_procedente) throws Exception {
		
		TipoElementoTipificadoEstandar tipoDocumentacion = rellenaTipoElementoTipificadoEstandar((String)htDocumentacion.get(de_tipodocumentacion_cda));
		
		DocumentacionTypeDatosDocumento datosDocumento = new DocumentacionTypeDatosDocumento();
		
		TipoElementoTipificadoEstandar tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htDocumentacion.get(de_dd_tipodocumento_cda));
		if (tipoElementoTipificadoEstandar != null) {
			datosDocumento.setTipoDocumento(tipoElementoTipificadoEstandar);	
		}
		
		Date cal = getCalendar((String)htDocumentacion.get(de_dd_fechapresentaciondocumento));
		if (cal != null) {
			datosDocumento.setFechaPresentacionDocumento(cal);
		}
		datosDocumento.setDescripcionAmpliadaDocumento((String)htDocumentacion.get(de_dd_descripcionampliadadocumento));
		Integer valueInt = getInteger("procedente de la documentación", (String)htDocumentacion.get(de_dd_procedente));
		if (valueInt != null) {
			datosDocumento.setProcedente(valueInt.intValue());
		}
		
		DocumentacionType tipoDocumentacionExpedienteDocumentacion = new DocumentacionType();
		if (tipoDocumentacion != null) {
			tipoDocumentacionExpedienteDocumentacion.setTipoDocumentacion(tipoDocumentacion);
		}
		tipoDocumentacionExpedienteDocumentacion.setDatosDocumento(datosDocumento);
		return tipoDocumentacionExpedienteDocumentacion;
	}

	
	/**
	 * 
	 * @param datosContactoType
	 * @param htEJGs
	 * @param dc_numerotelefono1
	 * @param dc_numerotelefono2
	 * @param dc_email
	 */
	private TipoDatosContacto datosContacto(Hashtable htEJGs, String dc_numerotelefono1, String dc_numerotelefono2, String dc_email) {
		TipoDatosContacto tipoDatosContacto = null;
		String tel1 = getString((String)htEJGs.get(dc_numerotelefono1));
		String tel2 = getString((String)htEJGs.get(dc_numerotelefono2));
		String email = getString((String)htEJGs.get(dc_email));
		
		if (tel1 != null || tel2 != null || email != null) {
			tipoDatosContacto = new TipoDatosContacto();
			if (tel1 != null) {
				tipoDatosContacto.setNumeroTelefono1(tel1);
			}
			if (tel2 != null) {
				tipoDatosContacto.setNumeroTelefono2(tel2);
			}
			if (email != null) {
				tipoDatosContacto.setEmail(email);
			}
		}		
		return tipoDatosContacto;
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
	private TipoDomiciliosPersona domiciliosPersona(Hashtable htEJGs, String dop_tipodomicilio_cda, String dop_dd_tipovia_cda,
			String dop_dd_nombrevia, String dop_dd_numerodireccion, String dop_dd_escaleradireccion, String dop_dd_pisodireccion,
			String dop_dd_puertadireccion, String dop_dd_pais_cda, String dop_dd_provincia_cda, String dop_dd_m_municipio_cda,
			String dop_dd_m_subcodigomunicipio, String dop_dd_codigopostal) {
		
		TipoDomiciliosPersona domiciliosPersona = new TipoDomiciliosPersona();
		TipoElementoTipificadoEstandar tipoDomicilio = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(dop_tipodomicilio_cda));
		if (tipoDomicilio != null) {
			domiciliosPersona.setTipoDomicilio(tipoDomicilio);
		}
		
		DatosDomicilio datosDomicilio = new DatosDomicilio();
		TipoElementoTipificadoEstandar tipoVia = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(dop_dd_tipovia_cda));
		if (tipoVia != null) {
			datosDomicilio.setTipoVia(tipoVia);
		}
		datosDomicilio.setNombreVia((String)htEJGs.get(dop_dd_nombrevia));
		String st = getString((String)htEJGs.get(dop_dd_numerodireccion));
		if (st != null) {
			datosDomicilio.setNumeroDireccion(st);
		}
		st = getString((String)htEJGs.get(dop_dd_escaleradireccion));
		if (st != null) {
			datosDomicilio.setEscaleraDireccion(st);
		}
		st = getString((String)htEJGs.get(dop_dd_pisodireccion));
		if (st != null) {
			datosDomicilio.setPisoDireccion(st);
		}
		st = getString((String)htEJGs.get(dop_dd_puertadireccion));
		if (st != null) {
			datosDomicilio.setPuertaDireccion(st);
		}
		
		TipoElementoTipificadoEstandar pais = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(dop_dd_pais_cda));
		if (pais != null) {
			datosDomicilio.setPais(pais);
		}
		TipoElementoTipificadoEstandar provincia = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(dop_dd_provincia_cda));
		if (provincia != null) {
			datosDomicilio.setProvincia(provincia);
		}
		
		domiciliosPersona.setDatosDomicilio(datosDomicilio);
		
		DatosDomicilioMunicipio municipio = new DatosDomicilioMunicipio();
		DatosDomicilioMunicipioMunicipio municipio2 = rellenaMunicipio((String)htEJGs.get(dop_dd_m_municipio_cda));
		if (municipio2 != null) {
			municipio.setMunicipio(municipio2);
		}
		st = getString((String)htEJGs.get(dop_dd_m_subcodigomunicipio));
		if (st != null) {
			municipio.setSubcodigoMunicipio(st);
		}
		if (municipio.getMunicipio() != null || municipio.getSubcodigoMunicipio() != null) {
			datosDomicilio.setMunicipio(municipio);
		}
		
		st = getString((String)htEJGs.get(dop_dd_codigopostal));
		if (st != null) {
			datosDomicilio.setCodigoPostal(st);
		}
		return domiciliosPersona;
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
	private TipoDatosPersona datosPersona(Hashtable htEJGs, String dp_tipopersona_cda, String dp_tipoidentificacion_cda,
			String dp_identificacion, String dp_nombre, String dp_primerapellido, String dp_segundoapellido, String dp_razonsocial,
			String dp_fechanacimiento, String dp_nacionalidad_cda, String dp_situacionlaboral_cda, String dp_profesion_cda, String dp_sexo,
			String dp_idiomacomunicacion, String dp_estadocivil_cda, String dp_regimeneconomico_cda, String dp_numhijos,
			String dp_fechaformalizacion, String dp_tipopersonajuridica_cda) throws Exception {
		
		
		TipoDatosPersona tipoDatosPersona = new TipoDatosPersona();
		TipoElementoTipificadoEstandar tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(dp_tipopersona_cda));
		if (tipoElementoTipificadoEstandar != null) {
			tipoDatosPersona.setTipoPersona(tipoElementoTipificadoEstandar);
		}
		tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(dp_tipoidentificacion_cda));
		if (tipoElementoTipificadoEstandar != null) {
			tipoDatosPersona.setTipoIdentificacion(tipoElementoTipificadoEstandar);
		}
		String st = getString((String)htEJGs.get(dp_identificacion));
		if (st != null) {
			tipoDatosPersona.setIdentificacion(st);
		}
		tipoDatosPersona.setNombre((String)htEJGs.get(dp_nombre));
		tipoDatosPersona.setPrimerApellido((String)htEJGs.get(dp_primerapellido));
		st = getString((String)htEJGs.get(dp_segundoapellido));
		if (st != null) {
			tipoDatosPersona.setSegundoApellido(st);
		}
		st = getString((String)htEJGs.get(dp_razonsocial));
		if (st != null) {
			tipoDatosPersona.setRazonSocial(st);
		}
		Date cal = getCalendar((String)htEJGs.get(dp_fechanacimiento));
		if (cal != null) {
			tipoDatosPersona.setFechaNacimiento(cal);
		}
		TipoElementoTipificadoEstandar tipoETE = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(dp_nacionalidad_cda));
		if (tipoETE != null) {
			tipoDatosPersona.setNacionalidad(tipoETE);
		}
		tipoETE = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(dp_situacionlaboral_cda));
		if (tipoETE != null) {
			tipoDatosPersona.setSituacionLaboral(tipoETE);
		}
		
		tipoETE = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(dp_profesion_cda));
		if (tipoETE != null) {
			tipoDatosPersona.setProfesion(tipoETE);
		}
		st = getString((String)htEJGs.get(dp_sexo));
		if (st != null) {
			tipoDatosPersona.setSexo(st);
		}
		st = getString((String)htEJGs.get(dp_idiomacomunicacion));
		if (st != null) {
			tipoDatosPersona.setIdiomaComunicacion(st);
		}
		tipoETE = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(dp_estadocivil_cda));
		if (tipoETE != null) {
			tipoDatosPersona.setEstadoCivil(tipoETE);
		}
		tipoETE = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(dp_regimeneconomico_cda));
		if (tipoETE != null) {
			tipoDatosPersona.setRegimenEconomico(tipoETE);
		}
		Long value = getLong((String)htEJGs.get(dp_numhijos));
		if (value != null) {
			tipoDatosPersona.setNumHijos(new UnsignedInt(value));
		}
		cal = getCalendar((String)htEJGs.get(dp_fechaformalizacion));
		if (cal != null) {
			tipoDatosPersona.setFechaFormalizacion(cal);
		}
		tipoETE = rellenaTipoElementoTipificadoEstandar((String)htEJGs.get(dp_tipopersonajuridica_cda));
		if (tipoETE != null) {
			tipoDatosPersona.setTipoPersonaJuridica(tipoETE);
		}
		return tipoDatosPersona;
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
	private Date getCalendar(String fecha) throws Exception {		
		Date date = null;
		if (fecha != null && !fecha.trim().equals("")) {
			date = GstDate.convertirFecha(fecha);				
		}	
		
		return date;
	}
	

	/**
	 * 
	 * @param tipoElementoTipificadoEstandar
	 * @param value
	 */
	private TipoElementoTipificadoEstandar rellenaTipoElementoTipificadoEstandar(String value) {
		TipoElementoTipificadoEstandar tipoElementoTipificadoEstandar = null;
		boolean relleno = false;
		if (value != null && !value.trim().equals("")) {
			String[] array = value.split("##");
			if (array != null && array.length > 0) {
				tipoElementoTipificadoEstandar = new TipoElementoTipificadoEstandar();
				tipoElementoTipificadoEstandar.setCodigo(array[0]);				
				if (array.length > 1) {
					tipoElementoTipificadoEstandar.setDescripcion(array[1]);
				}
				if (array.length > 2) {
					tipoElementoTipificadoEstandar.setAbreviatura(array[2]);
				}
			}
		}	
		return tipoElementoTipificadoEstandar;
	}

	
	/**
	 * 
	 * @param tipoElementoTipificadoEstandar
	 * @param value
	 */
	private TipoElementoTipificadoIntercambio rellenaTipoElementoTipificadoIntercambio(String value) {
		TipoElementoTipificadoIntercambio tipoElementoTipificadoIntercambio = null;
		if (value != null && !value.trim().equals("")) {
			String[] array = value.split("##");
			if (array != null && array.length > 0) {
				tipoElementoTipificadoIntercambio = new TipoElementoTipificadoIntercambio();
				tipoElementoTipificadoIntercambio.setCodigo(array[0]);
				if (array.length > 1) {
					tipoElementoTipificadoIntercambio.setDescripcion(array[1]);
				}
				if (array.length > 2) {
					tipoElementoTipificadoIntercambio.setAbreviatura(array[2]);
				}
			}
		}
		return tipoElementoTipificadoIntercambio;
	}
		
	/**
	 * 
	 * @param municipioType2
	 * @param value
	 */
	private DatosDomicilioMunicipioMunicipio rellenaMunicipio(String value) {
		DatosDomicilioMunicipioMunicipio municipio = null;
		
		if (value != null && !value.trim().equals("")) {
			String[] array = value.split("##");
			if (array != null && array.length > 0) {
				municipio = new DatosDomicilioMunicipioMunicipio();
				municipio.setCodigo(array[0]);		
				if (array.length > 1) {
					municipio.setDescripcion(array[1]);
				}
			}
		}
		return municipio;
	}
	
	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */

	private TipoExpedienteDatosExpediente datosExpediente(Hashtable htEJGs) throws Exception {
		
		TipoExpedienteDatosExpediente datosExpediente = new TipoExpedienteDatosExpediente();		
		
		TipoCodigoExpediente codigoExpediente = new TipoCodigoExpediente();
		
		codigoExpediente.setColegioExpediente((String)htEJGs.get(DE_CE_COLEGIOEXPEDIENTE));
		String numExpediente = UtilidadesString.formatea(htEJGs.get(DE_CE_NUMEXPEDIENTE), 8, true);
		codigoExpediente.setNumExpediente(numExpediente);		
		Long anyoExpediente = getLong((String)htEJGs.get(DE_CE_ANYOEXPEDIENTE));		
		if (anyoExpediente != null) {
			codigoExpediente.setAnyoExpediente(new UnsignedInt(anyoExpediente.longValue()));	
		}
		datosExpediente.setCodigoExpediente(codigoExpediente);
		
		TipoExpedienteDatosExpedienteCodigoExpedienteServicio codigoExpedienteServicio = null;
		
		String value = getString((String)htEJGs.get(DE_CES_ORIGENEXPEDIENTESERVICI));
		if (value != null) {			
			if (codigoExpedienteServicio == null) {
				codigoExpedienteServicio = new TipoExpedienteDatosExpedienteCodigoExpedienteServicio();
			}
			codigoExpedienteServicio.setOrigenExpedienteServicio(value);
		}
		
		value = getString((String)htEJGs.get(DE_CES_NUMEXPEDIENTESERVICIO));		
		if (value != null) {
			if (codigoExpedienteServicio == null) {
				codigoExpedienteServicio = new TipoExpedienteDatosExpedienteCodigoExpedienteServicio();
			}
			codigoExpedienteServicio.setNumExpedienteServicio(value);
		}		
		
		Long anyoLong = getLong((String)htEJGs.get(DE_CES_ANYOEXPEDIENTESERVICIO));
		if (anyoLong != null) {
			if (codigoExpedienteServicio == null) {
				codigoExpedienteServicio = new TipoExpedienteDatosExpedienteCodigoExpedienteServicio();
			}
			codigoExpedienteServicio.setAnyoExpedienteServicio(new UnsignedInt(anyoLong.longValue()));
		}
		
		
		Date cal = getCalendar((String)htEJGs.get(DE_FECHASOLICITUD));
		if (cal != null) {
			datosExpediente.setFechaSolicitud(cal);
		}
		datosExpediente.setObservaciones((String)htEJGs.get(DE_OBSERVACIONES2));
		
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG});
		List list = (List) htMarcasExpediente.get(key);
		
		if (list != null && list.size() > 0) {
			
			List<MarcaExpedienteType> marcasExpediente = new ArrayList<MarcaExpedienteType>();			
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable) list.get(i);
				TipoElementoTipificadoEstandar tipoElementoTipificadoEstandar = rellenaTipoElementoTipificadoEstandar((String)ht.get(DE_ME_ME_MARCAEXPEDIENTE_CDA));
				if (tipoElementoTipificadoEstandar != null) {
					String st = getString((String)ht.get(DE_ME_ME_VALORMARCAEXPEDIENTE));
					marcasExpediente.add(new MarcaExpedienteType(tipoElementoTipificadoEstandar, st));	
				}				
			}
			if (marcasExpediente.size() > 0) {
				datosExpediente.setMarcasExpediente(marcasExpediente.toArray(new MarcaExpedienteType[]{}));
			}
		}
		
		return datosExpediente;
		
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
	private IntercambioTipoInformacionIntercambio rellenaInformacionIntercambio(Hashtable ht, int sufijoIdIntercambio) throws SIGAException {
				
		IntercambioTipoInformacionIntercambio informacionIntercambio = new IntercambioTipoInformacionIntercambio();
		TipoIdentificacionIntercambio identificacionIntercambio = new TipoIdentificacionIntercambio();
		
		if (ht != null) {		
			identificacionIntercambio.setTipoIntercambio((String)ht.get(TIPOINTERCAMBIO));
			
			TipoElementoTipificadoIntercambio tipoElementoTipificadoIntercambio = rellenaTipoElementoTipificadoIntercambio((String)ht.get(ORIGENINTERCAMBIO_CDA));
			if (tipoElementoTipificadoIntercambio != null) {
				identificacionIntercambio.setOrigenIntercambio(tipoElementoTipificadoIntercambio);
			}
			tipoElementoTipificadoIntercambio = rellenaTipoElementoTipificadoIntercambio((String)ht.get(DESTINOINTERCAMBIO_CDA));
			if (tipoElementoTipificadoIntercambio != null) {
				identificacionIntercambio.setDestinoIntercambio(tipoElementoTipificadoIntercambio);
			}
			
			Long valueLong = getLong((String)ht.get(IDENTIFICADORINTERCAMBIO));			
			identificacionIntercambio.setIdentificadorIntercambio(new UnsignedInt((valueLong.longValue() * 10) + sufijoIdIntercambio));		
			identificacionIntercambio.setFechaIntercambio(Calendar.getInstance());			
			identificacionIntercambio.setVersionPCAJG((String)ht.get(VERSION_PCAJG));
		}
		informacionIntercambio.setIdentificacionIntercambio(identificacionIntercambio);
		
		return informacionIntercambio;
	}


	@Override
	public void execute() throws Exception {
				
		UsrBean usr = getUsrBean();
		
		String dirFicheros = getDirXML(getIdInstitucion(), getIdRemesa());
		UserTransaction tx = usr.getTransaction();
		
		//si no queremos generar el fichero txt ademas del xml hay que cometar solamente esta línea
		if (isGeneraTXT()) {
			generaTXT(dirFicheros);
		}
		
		try {
					
			tx.begin();
			
			CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(usr);
			cajgRespuestaEJGRemesaAdm.eliminaAnterioresErrores(getIdInstitucion(), getIdRemesa());
			
			List<File> files = generaFicherosXML(dirFicheros);	
			tx.commit();//hacemos commit para setear los posibles errores de negocio
			
			if (files.size() > 0) {
				
				tx.begin();
										
				CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(usr);
				CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(usr);
				
				
				// Marcar como generada
				cajgRemesaEstadosAdm.nuevoEstadoRemesa(usr, getIdInstitucion(), getIdRemesa(), Integer.valueOf("1"));
	
				if (SUBTIPOCAJG.DESCARGA_FICHERO.equals(subTipoCAJG)) {
					//hacemos zip con el fichero					
					
					String nombreFichero = getNombreRutaZIPconXMLs(getIdInstitucion(), getIdRemesa());
					
					MasterWords.doZip((ArrayList<File>)files, nombreFichero);
				} else if (cajgRemesaEstadosAdm.nuevoEstadoRemesa(usr, getIdInstitucion(), getIdRemesa(), Integer.valueOf("2"))) {
					//MARCAMOS COMO ENVIADA
					cajgEJGRemesaAdm.nuevoEstadoEJGRemitidoComision(usr, String.valueOf(getIdInstitucion()), String.valueOf(getIdRemesa()), ClsConstants.REMITIDO_COMISION);
				}
				
				//TODO FALTA EL ENVIO WEBSERVICE !!!!
				tx.commit();
			}

		} catch (Exception e) {
			tx.rollback();
			escribeLogRemesa("Se ha producido un error en el envío WebService");
			ClsLogging.writeFileLogError("Error en el envío WebService", e, 3);			
		} finally {			
			
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