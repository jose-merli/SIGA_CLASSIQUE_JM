package com.siga.ws.cat;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.transaction.UserTransaction;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.jcraft.jsch.JSchException;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CajgEJGRemesaAdm;
import com.siga.beans.CajgRemesaEstadosAdm;
import com.siga.beans.CajgRespuestaEJGRemesaAdm;
import com.siga.beans.CajgRespuestaEJGRemesaBean;
import com.siga.general.SIGAException;
import com.siga.gratuita.action.DefinirRemesasCAJGAction;
import com.siga.ws.PCAJGConstantes;
import com.siga.ws.SIGAWSClientAbstract;
import com.siga.ws.SigaWSHelper;
import com.siga.ws.cat.ftp.FtpPcajgAbstract;
import com.siga.ws.cat.ftp.FtpPcajgFactory;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument;
import com.siga.ws.pcajg.cat.xsd.TipoCodigoExpediente;
import com.siga.ws.pcajg.cat.xsd.TipoCodigoExpedienteServicio;
import com.siga.ws.pcajg.cat.xsd.TipoIdentificacionIntercambio;
import com.siga.ws.pcajg.cat.xsd.TipoProfesionalesDesignados;
import com.siga.ws.pcajg.cat.xsd.DatosContactoDocument.DatosContacto;
import com.siga.ws.pcajg.cat.xsd.DatosDomicilioDocument.DatosDomicilio;
import com.siga.ws.pcajg.cat.xsd.DatosPersonaDocument.DatosPersona;
import com.siga.ws.pcajg.cat.xsd.DatosProcuradorDocument.DatosProcurador;
import com.siga.ws.pcajg.cat.xsd.DocumentacionExpedienteDocument.DocumentacionExpediente;
import com.siga.ws.pcajg.cat.xsd.DocumentacionExpedienteDocument.DocumentacionExpediente.DatosDocumento;
import com.siga.ws.pcajg.cat.xsd.DomiciliosPersonaDocument.DomiciliosPersona;
import com.siga.ws.pcajg.cat.xsd.IdentificacionTramiteDocument.IdentificacionTramite;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoICD;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosContrario;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosExpediente;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosFamiliares;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosRepresentante;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosSolicitante;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosTramitacionExpediente;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.Pretension;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosExpediente.MarcasExpediente;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosSolicitante.DatosEconomicosPersona;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosSolicitante.DatosEconomicosPersona.Ingresos;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosSolicitante.DatosEconomicosPersona.PropiedadesBienesInmuebles;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosSolicitante.DatosEconomicosPersona.PropiedadesBienesMuebles;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosSolicitante.DatosEconomicosPersona.PropiedadesBienesOtros;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosTramitacionExpediente.TramiteArchivo;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosTramitacionExpediente.TramiteDictamen;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosTramitacionExpediente.TramiteResolucion;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.DatosTramitacionExpediente.TramiteResolucion.PrestacionesResolucion;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.Pretension.AsistenciaPenal;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.Pretension.ProcedimientoAdministrativo;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.Pretension.ProcedimientoJudicial;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.Pretension.AsistenciaPenal.AsistenciaCentroDetencion;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.Pretension.AsistenciaPenal.AsistenciaOrganoJudicial;
import com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoGenerico.Expediente.Pretension.AsistenciaPenal.Delito;
import com.siga.ws.pcajg.cat.xsd.TipoProfesionalesDesignados.AbogadoDesignado;
import com.siga.ws.pcajg.cat.xsd.TipoProfesionalesDesignados.ProcuradorDesignado;
import com.siga.ws.pcajg.cat.xsd.TipoProfesionalesDesignados.AbogadoDesignado.LibreDatosAbogado;


/**
 * @author angelcpe
 *
 */
public class PCAJGGeneraXML extends SIGAWSClientAbstract implements PCAJGConstantes {
		
	private static final String INTERCAMBIO_ALTA_PRESENTACION = "IAP";//INTERCAMBIO ALTA PRESENTACION
	private static final String INTERCAMBIO_EXPEDIENTES_DICTAMINADOS = "IED";//INTERCAMBIO EXPEDIENTES DICTAMINADOS
	private static final String INTERCAMBIO_EXPEDIENTES_ARCHIVADOS = "IEA";//INTERCAMBIO EXPEDIENTES ARCHIVADOS
	private static final String INTERCAMBIO_RESOLUCIONES = "IR";//INTERCAMBIO RESOLUCIONES
	private static final String INTERCAMBIO_CAMBIO_DESIGNACION = "ICD";//INTERCAMBIO CAMBIO DESIGNACION
	public static final String INTERCAMBIO_ERRORES_EXPEDIENTE = "IEE";
	
		
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
	
	private IntercambioDocument intercambioDocument = null;
		
		
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
		Vector datos = cajgEJGRemesaAdm.getDatosEJGs(getIdInstitucion(), getIdRemesa(), getUsrBean().getLanguage());
		Vector datosFamiliares = cajgEJGRemesaAdm.getFamiliares(getIdInstitucion(), getIdRemesa(), getUsrBean().getLanguage());
		construyeHTxEJG(datosFamiliares, htFamiliares);
		
		Vector datosMarcasExpediente = cajgEJGRemesaAdm.getDatosMarcasExpediente(getIdInstitucion(), getIdRemesa());
		construyeHTxEJG(datosMarcasExpediente, htMarcasExpediente);
		
		Vector datosAbogadosDesignados = cajgEJGRemesaAdm.getAbogadosDesignados(getIdInstitucion(), getIdRemesa());
		construyeHTxEJG(datosAbogadosDesignados, htAbogadosDesignados);
		
		Vector datosContrarios = cajgEJGRemesaAdm.getContrarios(getIdInstitucion(), getIdRemesa(), getUsrBean().getLanguage());
		construyeHTxEJG(datosContrarios, htContrarios);	
		
		Vector datosDocumentacionExpedienteDS = cajgEJGRemesaAdm.getDocumentacionExpedienteDS(getIdInstitucion(), getIdRemesa(), getUsrBean().getLanguage());
		construyeHTxPersona(datosDocumentacionExpedienteDS, htDocumentacionExpediente);
		
		Vector datosDelitos = cajgEJGRemesaAdm.getDelitos(getIdInstitucion(), getIdRemesa(), getUsrBean().getLanguage());
		construyeHTxEJG(datosDelitos, htDelitos);
		
		Hashtable ht = null;
		String tipoIntercambio = "";
		InformacionIntercambio informacionIntercambio = null;
		Intercambio intercambio = null;		
		
		int numDetalles = 0;
		int sufijoIdIntercambio = 0;
		TipoICD tipoICD = null;
		TipoGenerico tipoGenerico = null;
		
		for (int i = 0; i < datos.size(); i++) {
			ht = (Hashtable)datos.get(i);
			
			if (!tipoIntercambio.equals(ht.get(TIPOINTERCAMBIO))) {				
				if (intercambio != null && numDetalles > 0) {					
					ficheros.add(creaFichero(dirFicheros, dirPlantilla, intercambioDocument, intercambio, numDetalles));
				}
				numDetalles = 0;
				intercambioDocument = IntercambioDocument.Factory.newInstance();
				intercambio = intercambioDocument.addNewIntercambio();
				informacionIntercambio = rellenaInformacionIntercambio(intercambio, ht, sufijoIdIntercambio++);
				if (INTERCAMBIO_CAMBIO_DESIGNACION.equals(ht.get(TIPOINTERCAMBIO))) {
					tipoICD = informacionIntercambio.addNewTipoICD();
				} else {
					tipoGenerico = informacionIntercambio.addNewTipoGenerico();
				}					
			}
			
			tipoIntercambio = (String) ht.get(TIPOINTERCAMBIO);
			idTipoEJG = (String)ht.get(IDTIPOEJG);
			anyo = (String)ht.get(ANIO);
			numero = (String)ht.get(NUMERO);
			numejg = (String)ht.get(NUMEJG);
			
			try {	
				
				if (tipoIntercambio.equals(INTERCAMBIO_CAMBIO_DESIGNACION)) {
					numDetalles += addExpedienteTipoICD(tipoICD, ht, tipoIntercambio);
				} else {
					numDetalles += addExpedienteTipoGenerico(tipoGenerico, ht, tipoIntercambio);
				}
				
			} catch (IllegalArgumentException e) {	
				if (tipoGenerico.sizeOfExpedienteArray() > 0) {
					tipoGenerico.removeExpediente(tipoGenerico.sizeOfExpedienteArray()-1);
				}
				escribeErrorExpediente(anyo, numejg, numero, idTipoEJG, e.getMessage(), CajgRespuestaEJGRemesaBean.TIPO_RESPUESTA_SIGA);
			}
			
		}
		if (intercambio != null && numDetalles > 0) {			
			ficheros.add(creaFichero(dirFicheros, dirPlantilla, intercambioDocument, intercambio, numDetalles));
		}
						
		return ficheros;
	}
	
	private File creaFichero(String dirFicheros, String dirPlantilla, IntercambioDocument intercambioDocument, Intercambio intercambio, int numDetalles) throws Exception {
		
		File file = new File(dirFicheros);
		file.mkdirs();
		
		TipoIdentificacionIntercambio tipoIdentificacionIntercambio = intercambio.getInformacionIntercambio().getIdentificacionIntercambio();
		tipoIdentificacionIntercambio.setNumeroDetallesIntercambio(numDetalles);
		
		String nombreFichero = getNombreFichero(tipoIdentificacionIntercambio);
		
		file = new File(file, nombreFichero);
		guardaFicheroFormatoCatalan(intercambioDocument, file);
		
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
	
	
	private int addExpedienteTipoICD(TipoICD tipoICD, Hashtable htEJGs, String tipoIntercambio) throws Exception {
		com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoICD.Expediente expediente = tipoICD.addNewExpediente();
		
		com.siga.ws.pcajg.cat.xsd.IntercambioDocument.Intercambio.InformacionIntercambio.TipoICD.Expediente.DatosExpediente datosExpediente = expediente.addNewDatosExpediente();
		
		TipoCodigoExpediente tipoCodigoExpediente = getTipoCodigoExpediente(htEJGs);
		TipoCodigoExpedienteServicio codigoExpedienteServicio = getTipoCodigoExpedienteServicio(htEJGs);
		
		if (codigoExpedienteServicio.getAnyoExpedienteServicio() > 0 && codigoExpedienteServicio.getNumExpedienteServicio() != null && codigoExpedienteServicio.getOrigenExpedienteServicio() != null) {
			datosExpediente.addNewCodigoExpedienteServicio().set(codigoExpedienteServicio);			
		} else {
			datosExpediente.addNewCodigoExpediente().set(tipoCodigoExpediente);
		}
		
		expediente.addNewProfesionalesDesignados().set(getTipoProfesionalesDesignados(htEJGs));
		
		if(!validateXML_EJG(expediente, anyo, numejg, numero, idTipoEJG)){
			tipoICD.removeExpediente(tipoICD.sizeOfExpedienteArray()-1);
			return 0;			
		}
		
		return 1;
	}

	private TipoCodigoExpediente getTipoCodigoExpediente(Hashtable htEJGs) throws ClsExceptions {
		TipoCodigoExpediente codigoExpediente = TipoCodigoExpediente.Factory.newInstance();
		codigoExpediente.setColegioExpediente((String)htEJGs.get(DE_CE_COLEGIOEXPEDIENTE));
		String numExpediente = UtilidadesString.formatea(htEJGs.get(DE_CE_NUMEXPEDIENTE), 8, true);
		codigoExpediente.setNumExpediente(numExpediente);		
		Integer anyoExpediente = SigaWSHelper.getInteger("año del expediente", (String)htEJGs.get(DE_CE_ANYOEXPEDIENTE));		
		if (anyoExpediente != null) {
			codigoExpediente.setAnyoExpediente(anyoExpediente);	
		}
		return codigoExpediente;
	}

	private TipoCodigoExpedienteServicio getTipoCodigoExpedienteServicio(Hashtable htEJGs) {

		TipoCodigoExpedienteServicio codigoExpedienteServicio = TipoCodigoExpedienteServicio.Factory.newInstance();
		
		String value = getString((String)htEJGs.get(DE_CES_ORIGENEXPEDIENTESERVICI));
		if (value != null) {			
			codigoExpedienteServicio.setOrigenExpedienteServicio(value);
		}
		
		value = getString((String)htEJGs.get(DE_CES_NUMEXPEDIENTESERVICIO));		
		if (value != null) {			
			codigoExpedienteServicio.setNumExpedienteServicio(value);
		}		
		
		Integer anyoExpServicio = SigaWSHelper.getInteger("año CAJG" ,(String)htEJGs.get(DE_CES_ANYOEXPEDIENTESERVICIO));
		if (anyoExpServicio != null) {			
			codigoExpedienteServicio.setAnyoExpedienteServicio(anyoExpServicio);
		}
		
		return codigoExpedienteServicio;
	}

	/**
	 * Añade un expediente en el fichero xml
	 * @param expedientesType
	 * @param htEJGs
	 * @return
	 * @throws Exception
	 */
	private int addExpedienteTipoGenerico(TipoGenerico tipoGenerico, Hashtable htEJGs, String tipoIntercambio) throws Exception {		
		
		Expediente expediente = tipoGenerico.addNewExpediente();		
		
		datosExpediente(expediente, htEJGs);
		profesionalesDesignados(expediente, htEJGs);	
	
		datosSolicitante(expediente, htEJGs);
		datosRepresentante(expediente, htEJGs);
	
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG});
		List list = (List) htFamiliares.get(key);
		if (list != null && list.size() > 0) {			
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable) list.get(i);			
				datosFamiliares(expediente.addNewDatosFamiliares(), ht);
			}
		}
			
		list = (List)htContrarios.get(key);		
		if (list != null && list.size() > 0) {			
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable) list.get(i);
				datosContrario(expediente.addNewDatosContrario(), ht);
			}
		}
		//solo si IED o IR
		if (tipoIntercambio.equals(INTERCAMBIO_EXPEDIENTES_DICTAMINADOS)) {
			rellenaPretension(expediente, htEJGs);
		}
	
		if (!tipoIntercambio.equals(INTERCAMBIO_ALTA_PRESENTACION)) {
			datosTramitacionExpediente(expediente, htEJGs, tipoIntercambio);
		}
		
		
		if(!validateXML_EJG(expediente, anyo, numejg, numero, idTipoEJG)){
			tipoGenerico.removeExpediente(tipoGenerico.sizeOfExpedienteArray()-1);
			return 0;			
		}
		
		return 1;
	}
	
	/**
	 * Añade profesionales designados a tu fichero xml
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception 
	 * @throws Exception
	 */
	private void profesionalesDesignados(Expediente expediente, Hashtable htEJGs) throws Exception {		
		expediente.addNewProfesionalesDesignados().set(getTipoProfesionalesDesignados(htEJGs));
	}

	private TipoProfesionalesDesignados getTipoProfesionalesDesignados(Hashtable htEJGs) throws Exception {
		TipoProfesionalesDesignados profesionalesDesignados = TipoProfesionalesDesignados.Factory.newInstance();
		
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG});
		List list = (List) htAbogadosDesignados.get(key);
		
		if (list != null && list.size() > 0) {
			AbogadoDesignado abogadoDesignado = profesionalesDesignados.addNewAbogadoDesignado();		
			Hashtable ht = (Hashtable) list.get(0);			
			abogadoDesignado(abogadoDesignado, ht);		
		}
		
		
		ProcuradorDesignado procuradorDesignado = profesionalesDesignados.addNewProcuradorDesignado();		
		Integer valueInt = SigaWSHelper.getInteger("baja procurador designado", (String)htEJGs.get(PD_PD_BAJAPROCURADORDESIGNADO));
		if (valueInt != null) {
			procuradorDesignado.setBajaProcuradorDesignado(valueInt.intValue());
		}
				
		DatosProcurador datosProcurador = procuradorDesignado.addNewDatosProcurador();
		datosProcurador.setColegioProcurador((String)htEJGs.get(PD_PD_DP_COLEGIOPROCURADOR));
		datosProcurador.setNumColegiadoProcurador((String)htEJGs.get(PD_PD_DP_NUMCOLEGIADOPROCURADO));		
		datosProcurador.setNombreCompletoProcurador((String)htEJGs.get(PD_PD_DP_NOMBRECOMPLETOPROCURA));		
		
		Calendar cal = SigaWSHelper.getCalendar((String)htEJGs.get(PD_PD_FECHADESIGNACIONPROCURAD));
		if (cal != null) {
			procuradorDesignado.setFechaDesignacionProcurador(cal);
		}
		Long valueLong = getLong((String)htEJGs.get(PD_PD_NUMDESIGNACIONPROCURADOR));
		if (valueLong != null) {
			procuradorDesignado.setNumDesignacionProcurador(valueLong);
		}
		return profesionalesDesignados;
	}

	/**
	 * Añade datos de tramitacion expediente a tu fichero xml
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private void datosTramitacionExpediente(Expediente expediente, Hashtable htEJGs, String tipoIntercambio) throws Exception {
		DatosTramitacionExpediente datosTramitacionExpediente = null;
		if (tipoIntercambio.equals(INTERCAMBIO_EXPEDIENTES_ARCHIVADOS)) {
			datosTramitacionExpediente = expediente.addNewDatosTramitacionExpediente();
			TramiteArchivo tramiteArchivo = datosTramitacionExpediente.addNewTramiteArchivo();
			identificacionTramite(tramiteArchivo.addNewIdentificacionTramite(), htEJGs, DTE_TA_IT_ESTADOEXPEDIENTE_CDA, DTE_TA_IT_FECHAESTADO);
		} else if (tipoIntercambio.equals(INTERCAMBIO_EXPEDIENTES_DICTAMINADOS)) {		
			datosTramitacionExpediente = expediente.addNewDatosTramitacionExpediente();
			TramiteDictamen tramiteDictamen = datosTramitacionExpediente.addNewTramiteDictamen();
			identificacionTramite(tramiteDictamen.addNewIdentificacionTramite(), htEJGs, DTE_TD_IT_ESTADOEXPEDIENTE_CDA, DTE_TD_IT_FECHAESTADO);
			tramiteDictamen.setCodTipoDictamen(getCodigoElementoTipificado((String)htEJGs.get(DTE_TD_TIPODICTAMEN_CDA)));
			tramiteDictamen.setDescTipoDictamen(getDescripcionElementoTipificado((String)htEJGs.get(DTE_TD_TIPODICTAMEN_CDA)));
			
			tramiteDictamen.setCodMotivoDictamen(getCodigoElementoTipificado((String)htEJGs.get(DTE_TD_MOTIVODICTAMEN_CDA)));			
			tramiteDictamen.setDescMotivoDictamen(reemplazaCaracteres(getDescripcionElementoTipificado((String)htEJGs.get(DTE_TD_MOTIVODICTAMEN_CDA))));
			
			tramiteDictamen.setIntervaloIngresosRecursos((String)htEJGs.get(DTE_TD_INTERVALOINGRESOSRECURS));
			tramiteDictamen.setObservacionesDictamen((String)htEJGs.get(DTE_TD_OBSERVACIONESDICTAMEN));
			
			if (tramiteDictamen.getCodTipoDictamen() == null || tramiteDictamen.getCodTipoDictamen().trim().equals("")) {
				throw new IllegalArgumentException("Debe rellenar el tipo de dictamen.");
			}
		} else if (tipoIntercambio.equals(INTERCAMBIO_RESOLUCIONES)) {		
			datosTramitacionExpediente = expediente.addNewDatosTramitacionExpediente();
			TramiteResolucion tramiteResolucion = datosTramitacionExpediente.addNewTramiteResolucion();
			identificacionTramite(tramiteResolucion.addNewIdentificacionTramite(), htEJGs, DTE_TR_IT_ESTADOEXPEDIENTE_CDA, DTE_TR_IT_FECHAESTADO);
			tramiteResolucion.setIdentificadorResolucion((String)htEJGs.get(DTE_TR_IDENTIFICADORRESOLUCION));
			
			tramiteResolucion.setCodTipoResolucion(getCodigoElementoTipificado((String)htEJGs.get(DTE_TR_TIPORESOLUCION_CDA)));
			tramiteResolucion.setDescTipoResolucion(getDescripcionElementoTipificado((String)htEJGs.get(DTE_TR_TIPORESOLUCION_CDA)));
			
			tramiteResolucion.setCodMotivoResolucion(getCodigoElementoTipificado((String)htEJGs.get(DTE_TR_MOTIVORESOLUCION_CDA)));
			tramiteResolucion.setDescMotivoResolucion(getDescripcionElementoTipificado((String)htEJGs.get(DTE_TR_MOTIVORESOLUCION_CDA)));
			
			tramiteResolucion.setIntervaloIngresosRecursos((String)htEJGs.get(DTE_TR_INTERVALOINGRESOSRECURS));
			
			CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(getUsrBean());
			Vector datos = cajgEJGRemesaAdm.getDatosPrestacionesResolucion(getIdInstitucion(), getIdRemesa(), idTipoEJG, anyo, numero);
			for (int i = 0; i < datos.size(); i++) {
				Hashtable ht = (Hashtable) datos.get(i);
				PrestacionesResolucion prestacionesResolucion = tramiteResolucion.addNewPrestacionesResolucion();
				prestacionesResolucion.setCodTipoPrestacion(getCodigoElementoTipificado((String)ht.get(DTE_TR_PR_TIPOPRESTACION_CDA)));
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
	private void identificacionTramite(IdentificacionTramite identificacionTramite, Hashtable htEJGs, String it_estadoexpediente_cda, String it_fechaestado) throws Exception {
		identificacionTramite.setCodEstadoExpediente(getCodigoElementoTipificado((String)htEJGs.get(it_estadoexpediente_cda)));
		identificacionTramite.setDescEstadoExpediente(getDescripcionElementoTipificado((String)htEJGs.get(it_estadoexpediente_cda)));
				
		Calendar cal = SigaWSHelper.getCalendar((String)htEJGs.get(it_fechaestado));
		if (cal != null) {
			identificacionTramite.setFechaEstado(cal);
		}
	}
	
	private void rellenaProcedimientoJudicial(ProcedimientoJudicial procedimientoJudicial, Hashtable htEJGs) throws Exception {
		procedimientoJudicial.setCodPartidoJudicial(getCodigoElementoTipificado((String)htEJGs.get(DDJ_PARTIDOJUDICIAL_CDA)));
		procedimientoJudicial.setDescPartidoJudicial(getDescripcionElementoTipificado((String)htEJGs.get(DDJ_PARTIDOJUDICIAL_CDA)));
		
		procedimientoJudicial.setCodJurisdiccion(getCodigoElementoTipificado((String)htEJGs.get(DDJ_JURISDICCION_CDA)));
		procedimientoJudicial.setDescJurisdiccion(getDescripcionElementoTipificado((String)htEJGs.get(DDJ_JURISDICCION_CDA)));
		
		String codOrganoJudicial = getCodigoElementoTipificado((String)htEJGs.get(DDJ_ORGANOJUDICIAL_CDA));
		procedimientoJudicial.setCodOrganoJudicial(codOrganoJudicial);
		procedimientoJudicial.setDescOrganoJudicial(getDescripcionElementoTipificado((String)htEJGs.get(DDJ_ORGANOJUDICIAL_CDA)));
		
		String identificadorProcedimiento = getString((String)htEJGs.get(DDJ_IDENTIFICADORPROCEDIMIENTO));
		Boolean b = getBoolean((String)htEJGs.get(DDJ_ORGANOJUDICIAL_ESDECANO));
		//si el identificadorProcedimiento es null y no es juzgado decano
		if (codOrganoJudicial != null && identificadorProcedimiento == null && (b == null || !b.booleanValue())) {
			throw new IllegalArgumentException("Debe rellenar número de procedimiento");
		}
		procedimientoJudicial.setIdentificadorProcedimiento(identificadorProcedimiento);
	}

	/**
	 * Añade datos asistente detenido a tu fichero xml
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private void rellenaAsistenciaPenal(AsistenciaPenal asistenciaPenal, Hashtable htEJGs) throws Exception {
		
		asistenciaPenal.setCodPartidoJudicial(getCodigoElementoTipificado((String)htEJGs.get(CAT_PARTIDO_JUDICIAL_ASIS)));
		asistenciaPenal.setDescPartidoJudicial(getDescripcionElementoTipificado((String)htEJGs.get(CAT_PARTIDO_JUDICIAL_ASIS)));
		
		String codOrganoJudicial = getCodigoElementoTipificado((String)htEJGs.get(DAD_ORGANOJUDICIAL_CDA));
		if (codOrganoJudicial != null) {//por juzgado DILIGENCIA
			Calendar cal = SigaWSHelper.getCalendar((String)htEJGs.get(DAD_FECHAASISTENCIADILIGENCIA));
			if (cal != null) {
				asistenciaPenal.setFechaAsistencia(cal);
			}
			AsistenciaOrganoJudicial asistenciaOrganoJudicial = asistenciaPenal.addNewAsistenciaOrganoJudicial();
			asistenciaOrganoJudicial.setCodOrganoJudicial(codOrganoJudicial);
			asistenciaOrganoJudicial.setDescOrganoJudicial(getDescripcionElementoTipificado((String)htEJGs.get(DAD_ORGANOJUDICIAL_CDA)));
			asistenciaPenal.setIdentificadorAsistencia((String)htEJGs.get(DAD_NUMERODILIGENCIA));
		} else { //por comisaría ATESTADO
			Calendar cal = SigaWSHelper.getCalendar((String)htEJGs.get(DAD_FECHAASISTENCIAATESTADO));
			if (cal != null) {
				asistenciaPenal.setFechaAsistencia(cal);
			}
			AsistenciaCentroDetencion asistenciaCentroDetencion = asistenciaPenal.addNewAsistenciaCentroDetencion();
			asistenciaCentroDetencion.setCodCentroDetencion(getCodigoElementoTipificado((String)htEJGs.get(DAD_CENTRODETENCION_CDA)));
			asistenciaCentroDetencion.setDescCentroDetencion(getDescripcionElementoTipificado((String)htEJGs.get(DAD_CENTRODETENCION_CDA)));
			asistenciaPenal.setIdentificadorAsistencia((String)htEJGs.get(DAD_NUMEROATESTADO));
		}
		
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG});
		List list = (List) htDelitos.get(key);
		
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable)list.get(i);	
				Delito delito = asistenciaPenal.addNewDelito();
				delito.setCodTipoDelito(getCodigoElementoTipificado((String)ht.get(DELITO_CDA)));
				delito.setDescTipoDelito(truncar(getDescripcionElementoTipificado((String)ht.get(DELITO_CDA)), 50));
			}
		}		
				
	}

	private String truncar(String st, int i) {
		String resultado = st;
		if (st != null && st.length() > i) {
			resultado = st.substring(0, i-3) + "...";			
		}
		return resultado;
	}
	

	/**
	 * 
	 * @param abogadosDesignadosType
	 * @param ht
	 * @throws Exception
	 */
	
	private void abogadoDesignado(AbogadoDesignado abogadoDesignado, Hashtable ht) throws Exception {	
		
		Integer valueInt = SigaWSHelper.getInteger("baja abogado designado", (String)ht.get(PD_AD_AD_BAJAABOGADODESIGNADO));
		if (valueInt != null) {
			abogadoDesignado.setBajaAbogadoDesignado(valueInt.intValue());
		}
		
		abogadoDesignado.setNumColegiadoAbogado((String)ht.get(PD_AD_AD_NUMCOLEGIADOABOGADO));
		Calendar cal = SigaWSHelper.getCalendar((String)ht.get(PD_AD_AD_FECHADESIGNACIONABOGA));
		if (cal != null) {
			abogadoDesignado.setFechaDesignacionAbogado(cal);
		}
		Long valueLong = getLong((String)ht.get(PD_AD_AD_NUMDESIGNACIONABOGADO));
		if (valueLong != null) {
			abogadoDesignado.setNumDesignacionAbogado(valueLong.longValue());
		}
		String nombreAbo  = getString((String)ht.get(PD_AD_AD_NOMBREABO));
		String apellido1Abo = getString((String)ht.get(PD_AD_AD_APELLIDO1ABO));
		
		if (nombreAbo != null && apellido1Abo != null) {
			LibreDatosAbogado libreDatosAbogado = abogadoDesignado.addNewLibreDatosAbogado();
			libreDatosAbogado.setNombreAbogado(nombreAbo);
			libreDatosAbogado.setPrimerApellidoAbogado(apellido1Abo);
			libreDatosAbogado.setSegundoApellidoAbogado(getString((String)ht.get(PD_AD_AD_APELLIDO2ABO)));
		}
	}

	
	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception 
	 */
	private void rellenaPretension(Expediente expediente, Hashtable htEJGs) throws Exception {
		Pretension pretension = expediente.addNewPretension();
				
		String pretensionChoice = (String)htEJGs.get(PRETENSION_CHOICE);
		if (pretensionChoice != null) {
			if (pretensionChoice.trim().equals("1")) {//PROCEDIMIENTO JUDICIAL
				rellenaProcedimientoJudicial(pretension.addNewProcedimientoJudicial(), htEJGs);		
			} else if (pretensionChoice.trim().equals("2")) { //ASISTENCIA PENAL
				rellenaAsistenciaPenal(pretension.addNewAsistenciaPenal(), htEJGs);  
			} else if (pretensionChoice.trim().equals("3")) { //PROCEDIMIENTO ADMINISTRATIVO
				ProcedimientoAdministrativo procedimientoAdministrativo = pretension.addNewProcedimientoAdministrativo();
				procedimientoAdministrativo.setCodOrganoJudicial("-2");//siempre -2 segun el xsd 
				procedimientoAdministrativo.setDescOrganoJudicial("Altres");//siempre altres segun xsd
				procedimientoAdministrativo.setIdentificadorProcedimiento((String)htEJGs.get(DDJ_IDENTIFICADORPROCEDIMIENTO));
			}
		}
		pretension.setCodTipoProcedimiento(getCodigoElementoTipificado((String)htEJGs.get(DDJ_TIPOPROCEDIMIENTO_CDA)));
		pretension.setDescTipoProcedimiento(getDescripcionElementoTipificado((String)htEJGs.get(DDJ_TIPOPROCEDIMIENTO_CDA)));
		pretension.setResumenPretension((String)htEJGs.get(DDJ_RESUMENPRETENSION));
		
		Integer renunciaDesignacion = SigaWSHelper.getInteger("renuncia designación", (String)htEJGs.get(DDJ_RENUNCIADESIGNACION));
		if (renunciaDesignacion != null){
			pretension.setRenunciaDesignacion(renunciaDesignacion.shortValue());			
		}
		
		BigInteger renunciaHonorarios = SigaWSHelper.getBigInteger("renuncia honorarios", (String)htEJGs.get(DDJ_RENUNCIAHONORARIOS));
		if (renunciaHonorarios != null){
			pretension.setRenunciaHonorarios(renunciaHonorarios);			
		}
		
		Integer demandanteDemandado = SigaWSHelper.getInteger("demandante o demandado", (String)htEJGs.get(DDJ_DEMANDADODEMANDANTE));
		if (demandanteDemandado != null) {
			pretension.setDemandadoDemandante(demandanteDemandado.shortValue());
		}
		pretension.setCodPreceptivo(getCodigoElementoTipificado((String)htEJGs.get(DDJ_PRECEPTIVO_CDA)));
		pretension.setDescPreceptivo(getDescripcionElementoTipificado((String)htEJGs.get(DDJ_PRECEPTIVO_CDA)));		
	}

	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private void datosContrario(DatosContrario contrario, Hashtable htEJGs) throws Exception {		
		
		datosPersona(contrario.addNewDatosPersona(), htEJGs, C_C_DP_TIPOPERSONA_CDA, C_C_DP_TIPOIDENTIFICACION_CDA, C_C_DP_IDENTIFICACION, C_C_DP_NOMBRE
				, C_C_DP_PRIMERAPELLIDO, C_C_DP_SEGUNDOAPELLIDO, C_C_DP_RAZONSOCIAL, C_C_DP_FECHANACIMIENTO, C_C_DP_NACIONALIDAD_CDA
				, C_C_DP_SITUACIONLABORAL_CDA, C_C_DP_PROFESION_CDA, C_C_DP_SEXO, C_C_DP_IDIOMACOMUNICACION, C_C_DP_ESTADOCIVIL_CDA
				, C_C_DP_REGIMENECONOMICO_CDA, C_C_DP_NUMHIJOS, C_C_DP_FECHAFORMALIZACION, C_C_DP_TIPOPERSONAJURIDICA_CDA);
		
		DatosProcurador datosProcurador = contrario.addNewDatosProcurador();
		datosProcurador.setColegioProcurador((String)htEJGs.get(C_C_DP_COLEGIOPROCURADOR));		
		datosProcurador.setNumColegiadoProcurador(getString((String)htEJGs.get(C_C_DP_NUMCOLEGIADOPROCURADOR)));		
		datosProcurador.setNombreCompletoProcurador((String)htEJGs.get(C_C_DP_NOMBRECOMPLETOPROCURADO));
	}

	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */
	private void datosFamiliares(DatosFamiliares datosFamiliares, Hashtable htFam) throws Exception {
		
		datosFamiliares.setCodParentesco(getCodigoElementoTipificado((String)htFam.get(F_F_PARENTESCO_CDA)));		
		datosFamiliares.setDescParentesco(getDescripcionElementoTipificado((String)htFam.get(F_F_PARENTESCO_CDA)));
						
		Integer familiarMenor = SigaWSHelper.getInteger("familiar menor", ((String)htFam.get(F_F_FAMILIARMENOR)));
		if (familiarMenor != null) {
			datosFamiliares.setFamiliarMenor(familiarMenor.shortValue());
		}
		datosPersona(datosFamiliares.addNewDatosPersona(), htFam, F_F_DP_TIPOPERSONA_CDA, F_F_DP_TIPOIDENTIFICACION_CDA, F_F_DP_IDENTIFICACION, F_F_DP_NOMBRE
				, F_F_DP_PRIMERAPELLIDO, F_F_DP_SEGUNDOAPELLIDO, F_F_DP_RAZONSOCIAL, F_F_DP_FECHANACIMIENTO, F_F_DP_NACIONALIDAD_CDA
				, F_F_DP_SITUACIONLABORAL_CDA, F_F_DP_PROFESION_CDA, F_F_DP_SEXO, F_F_DP_IDIOMACOMUNICACION, F_F_DP_ESTADOCIVIL_CDA
				, F_F_DP_REGIMENECONOMICO_CDA, F_F_DP_NUMHIJOS, F_F_DP_FECHAFORMALIZACION, F_F_DP_TIPOPERSONAJURIDICA_CDA);
			
		String idPersona = (String)htFam.get(IDPERSONA);
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG, idPersona});
		List list = (List) htDocumentacionExpediente.get(key);
		
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Hashtable htDoc = (Hashtable)list.get(i);				
				crearDocumentacionExpediente(datosFamiliares.addNewDocumentacionExpediente(), htDoc, D_TIPODOCUMENTACION_CDA
						, D_TIPODOCUMENTO_CDA, D_FECHAPRESENTACIDO, D_DESCRIPCIONAMPLIAD, D_PROCEDENTE);
			}
		}
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
	private void datosRepresentante(Expediente expediente, Hashtable htEJGs) throws Exception {
		String st = getString((String)htEJGs.get(DR_DP_TIPOPERSONA_CDA));
		if (st != null) {
			DatosRepresentante datosRepresentante = expediente.addNewDatosRepresentante();
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
	private void datosSolicitante(Expediente expediente, Hashtable htEJGs) throws Exception {
		DatosSolicitante datosSolicitante = expediente.addNewDatosSolicitante();
		
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
		
		//DocumentacionExpediente documentacionExpediente = datosSolicitante.addNewDocumentacionExpediente();
		
		String idPersona = (String)htEJGs.get(IDPERSONA);		
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG, idPersona});
		List list = (List) htDocumentacionExpediente.get(key);
		
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable)list.get(i);
				crearDocumentacionExpediente(datosSolicitante.addNewDocumentacionExpediente(), ht, D_TIPODOCUMENTACION_CDA, 
						D_TIPODOCUMENTO_CDA, D_FECHAPRESENTACIDO, D_DESCRIPCIONAMPLIAD, D_PROCEDENTE);
			}
		}
		
		DatosEconomicosPersona datosEconomicosPersona = datosSolicitante.addNewDatosEconomicosPersona();

		Ingresos ingresos = datosEconomicosPersona.addNewIngresos();
		ingresos.setCodConceptoIngresos(getCodigoElementoTipificado((String) htEJGs.get(DS_DEP_I_CONCEPTOINGRESOS_CDA)));
		ingresos.setDescConceptoIngresos(getDescripcionElementoTipificado((String) htEJGs.get(DS_DEP_I_CONCEPTOINGRESOS_CDA)));
				
		ingresos.setObservacionesIngresos((String) htEJGs.get(DS_DEP_I_OBSERVACIONESINGRESOS));
		ingresos.setDescripcionIngresos((String) htEJGs.get(DS_DEP_I_DESCRIPCIONINGRESOS));
		Double value = getDouble((String) htEJGs.get(DS_DEP_I_IMPORTEBRUTO));
		if (value != null) {
			ingresos.setImporteBruto(value.floatValue());
		}
		Integer valueInt = SigaWSHelper.getInteger("acumulable de los ingresos", (String) htEJGs.get(DS_DEP_I_ACUMULABLE));
		if (valueInt != null) {
			ingresos.setAcumulable(valueInt.shortValue());
		}
			
		PropiedadesBienesInmuebles propiedadesBienesInmuebles = datosEconomicosPersona.addNewPropiedadesBienesInmuebles();
		
		propiedadesBienesInmuebles.setCodTipoBienInmueble(getCodigoElementoTipificado((String) htEJGs.get(DS_DEP_PBI_TIPOBIENINMUEBL_CDA)));
		propiedadesBienesInmuebles.setDescTipoBienInmueble(getDescripcionElementoTipificado((String) htEJGs.get(DS_DEP_PBI_TIPOBIENINMUEBL_CDA)));
		
		propiedadesBienesInmuebles.setCodOrigenValoracion(getCodigoElementoTipificado((String) htEJGs.get(DS_DEP_PBI_ORIGENVALORACIO_CDA)));
		propiedadesBienesInmuebles.setDescOrigenValoracion(getDescripcionElementoTipificado((String) htEJGs.get(DS_DEP_PBI_ORIGENVALORACIO_CDA)));
				
		propiedadesBienesInmuebles.setCargas((String) htEJGs.get(DS_DEP_PBI_CARGAS));
		propiedadesBienesInmuebles.setObservaciones((String) htEJGs.get(DS_DEP_PBI_OBSERVACIONES));
		value = getDouble((String) htEJGs.get(DS_DEP_PBI_VALORACIONECONOMICA));
		if (value != null) {
			propiedadesBienesInmuebles.setValoracionEconomica(value.floatValue());
		}
		valueInt = SigaWSHelper.getInteger("acumulable de los bienes inmuebles", (String) htEJGs.get(DS_DEP_PBI_ACUMULABLE));
		if (valueInt != null) {
			propiedadesBienesInmuebles.setAcumulable(valueInt.shortValue());
		}
		
		PropiedadesBienesMuebles propiedadesBienesMuebles = datosEconomicosPersona.addNewPropiedadesBienesMuebles();
		propiedadesBienesMuebles.setCodTipoBienMueble(getCodigoElementoTipificado((String) htEJGs.get(DS_DEP_PBM_TIPOBIENMUEBLE_CDA)));
		propiedadesBienesMuebles.setDescTipoBienMueble(getDescripcionElementoTipificado((String) htEJGs.get(DS_DEP_PBM_TIPOBIENMUEBLE_CDA)));
				
		propiedadesBienesMuebles.setObservaciones((String) htEJGs.get(DS_DEP_PBM_OBSERVACIONES));
		value = getDouble((String) htEJGs.get(DS_DEP_PBM_VALORACIONECONOMICA));
		if (value != null) {
			propiedadesBienesMuebles.setValoracionEconomica(value.floatValue());
		}
		valueInt = SigaWSHelper.getInteger("acumulable de los bienes muebles", (String) htEJGs.get(DS_DEP_PBM_ACUMULABLE));
		if (valueInt != null) {
			propiedadesBienesMuebles.setAcumulable(valueInt.shortValue());
		}
		
		PropiedadesBienesOtros propiedadesBienesOtros = datosEconomicosPersona.addNewPropiedadesBienesOtros();
		propiedadesBienesOtros.setCargas((String) htEJGs.get(DS_DEP_PBO_CARGAS));
		propiedadesBienesOtros.setDescripcionBien((String) htEJGs.get(DS_DEP_PBO_DESCRIPCIONBIEN));
		propiedadesBienesOtros.setObservaciones((String) htEJGs.get(DS_DEP_PBO_OBSERVACIONES));
		value = getDouble((String) htEJGs.get(DS_DEP_PBO_VALORACIONECONOMICA));
		if (value != null) {
			propiedadesBienesOtros.setValoracionEconomica(value.floatValue());
		}

		valueInt = SigaWSHelper.getInteger("acumulable de bienes otros",(String) htEJGs.get(DS_DEP_PBO_ACUMULABLE));
		if (valueInt != null) {
			propiedadesBienesOtros.setAcumulable(valueInt.shortValue());
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
	private void crearDocumentacionExpediente(DocumentacionExpediente documentacion, Hashtable htDocumentacion, String de_tipodocumentacion_cda,
			String de_dd_tipodocumento_cda, String de_dd_fechapresentaciondocumento, String de_dd_descripcionampliadadocumento,
			String de_dd_procedente) throws Exception {		
		
		
		documentacion.setCodTipoDocumentacion(getCodigoElementoTipificado((String)htDocumentacion.get(de_tipodocumentacion_cda)));
		documentacion.setDescTipoDocumentacion(getDescripcionElementoTipificado((String)htDocumentacion.get(de_tipodocumentacion_cda)));
				
		DatosDocumento datosDocumento = documentacion.addNewDatosDocumento();
		datosDocumento.setCodTipoDocumento(getCodigoElementoTipificado((String)htDocumentacion.get(de_dd_tipodocumento_cda)));
		datosDocumento.setDescTipoDocumento(getDescripcionElementoTipificado((String)htDocumentacion.get(de_dd_tipodocumento_cda)));
				
		Calendar cal = SigaWSHelper.getCalendar((String)htDocumentacion.get(de_dd_fechapresentaciondocumento));
		if (cal != null) {
			datosDocumento.setFechaPresentacionDocumento(cal);
		}
		datosDocumento.setDescripcionAmpliadaDocumento((String)htDocumentacion.get(de_dd_descripcionampliadadocumento));
		Integer valueInt = SigaWSHelper.getInteger("procedente de la documentación", (String)htDocumentacion.get(de_dd_procedente));
		if (valueInt != null) {
			datosDocumento.setProcedente(valueInt.shortValue());
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
	private void datosContacto(DatosContacto datosContacto, Hashtable htEJGs, String dc_numerotelefono1, String dc_numerotelefono2, String dc_email) {
		datosContacto.setNumeroTelefono1((String)htEJGs.get(dc_numerotelefono1));
		datosContacto.setNumeroTelefono2((String)htEJGs.get(dc_numerotelefono2));
		datosContacto.setEmail((String)htEJGs.get(dc_email));
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
	private void domiciliosPersona(DomiciliosPersona domiciliosPersona, Hashtable htEJGs, String dop_tipodomicilio_cda, String dop_dd_tipovia_cda,
			String dop_dd_nombrevia, String dop_dd_numerodireccion, String dop_dd_escaleradireccion, String dop_dd_pisodireccion,
			String dop_dd_puertadireccion, String dop_dd_pais_cda, String dop_dd_provincia_cda, String dop_dd_m_municipio_cda,
			String dop_dd_m_subcodigomunicipio, String dop_dd_codigopostal) {
		
		
		domiciliosPersona.setCodTipoDomicilioArray(new String[]{getCodigoElementoTipificado((String)htEJGs.get(dop_tipodomicilio_cda))});
				
		DatosDomicilio datosDomicilio = domiciliosPersona.addNewDatosDomicilio();
		
		datosDomicilio.setCodTipoVia(getCodigoElementoTipificado((String)htEJGs.get(dop_dd_tipovia_cda)));
		datosDomicilio.setDescTipoVia(getDescripcionElementoTipificado((String)htEJGs.get(dop_dd_tipovia_cda)));
		
		datosDomicilio.setNombreVia((String)htEJGs.get(dop_dd_nombrevia));
		datosDomicilio.setNumeroDireccion((String)htEJGs.get(dop_dd_numerodireccion));
		datosDomicilio.setEscaleraDireccion((String)htEJGs.get(dop_dd_escaleradireccion));
		datosDomicilio.setPisoDireccion((String)htEJGs.get(dop_dd_pisodireccion));
		datosDomicilio.setPuertaDireccion((String)htEJGs.get(dop_dd_puertadireccion));
		
		datosDomicilio.setCodPais(getCodigoElementoTipificado((String)htEJGs.get(dop_dd_pais_cda)));
		datosDomicilio.setDescPais(getDescripcionElementoTipificado((String)htEJGs.get(dop_dd_pais_cda)));
		
		datosDomicilio.setCodProvincia(getCodigoElementoTipificado((String)htEJGs.get(dop_dd_provincia_cda)));
		datosDomicilio.setDescProvincia(getDescripcionElementoTipificado((String)htEJGs.get(dop_dd_provincia_cda)));
		
		datosDomicilio.setSubCodigoMunicipio((String)htEJGs.get(dop_dd_m_subcodigomunicipio));
		datosDomicilio.setCodMunicipio(getCodigoElementoTipificado((String)htEJGs.get(dop_dd_m_municipio_cda)));
		datosDomicilio.setDescMunicipio(getDescripcionElementoTipificado((String)htEJGs.get(dop_dd_m_municipio_cda)));
				
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
	private void datosPersona(DatosPersona datosPersona, Hashtable htEJGs, String dp_tipopersona_cda, String dp_tipoidentificacion_cda,
			String dp_identificacion, String dp_nombre, String dp_primerapellido, String dp_segundoapellido, String dp_razonsocial,
			String dp_fechanacimiento, String dp_nacionalidad_cda, String dp_situacionlaboral_cda, String dp_profesion_cda, String dp_sexo,
			String dp_idiomacomunicacion, String dp_estadocivil_cda, String dp_regimeneconomico_cda, String dp_numhijos,
			String dp_fechaformalizacion, String dp_tipopersonajuridica_cda) throws Exception {
		
		datosPersona.setCodTipoPersona(getCodigoElementoTipificado((String)htEJGs.get(dp_tipopersona_cda)));
		datosPersona.setCodTipoIdentificacion(getCodigoElementoTipificado((String)htEJGs.get(dp_tipoidentificacion_cda)));		
		
		datosPersona.setIdentificacion((String)htEJGs.get(dp_identificacion));
		datosPersona.setNombre((String)htEJGs.get(dp_nombre));
		datosPersona.setPrimerApellido((String)htEJGs.get(dp_primerapellido));
		datosPersona.setSegundoApellido((String)htEJGs.get(dp_segundoapellido));
		datosPersona.setRazonSocial((String)htEJGs.get(dp_razonsocial));
		Calendar cal = SigaWSHelper.getCalendar((String)htEJGs.get(dp_fechanacimiento));
		if (cal != null) {
			datosPersona.setFechaNacimiento(cal);
		}
		datosPersona.setCodNacionalidad(getCodigoElementoTipificado((String)htEJGs.get(dp_nacionalidad_cda)));
		datosPersona.setDescNacionalidad(getDescripcionElementoTipificado((String)htEJGs.get(dp_nacionalidad_cda)));
		
		datosPersona.setCodSituacionLaboral(getCodigoElementoTipificado((String)htEJGs.get(dp_situacionlaboral_cda)));
		datosPersona.setDescSituacionLaboral(getDescripcionElementoTipificado((String)htEJGs.get(dp_situacionlaboral_cda)));
		
		datosPersona.setCodProfesion(getCodigoElementoTipificado((String)htEJGs.get(dp_profesion_cda)));
		datosPersona.setDescProfesion(getDescripcionElementoTipificado((String)htEJGs.get(dp_profesion_cda)));
				
		datosPersona.setSexo((String)htEJGs.get(dp_sexo));
		datosPersona.setIdiomaComunicacion((String)htEJGs.get(dp_idiomacomunicacion));
		
		datosPersona.setCodEstadoCivil(getCodigoElementoTipificado((String)htEJGs.get(dp_estadocivil_cda)));
		datosPersona.setDescEstadoCivil(getDescripcionElementoTipificado((String)htEJGs.get(dp_estadocivil_cda)));
		
		datosPersona.setCodRegimenEconomico(getCodigoElementoTipificado((String)htEJGs.get(dp_regimeneconomico_cda)));
				
		Long value = getLong((String)htEJGs.get(dp_numhijos));
		if (value != null) {
			datosPersona.setNumHijos(value.longValue());
		}
		cal = SigaWSHelper.getCalendar((String)htEJGs.get(dp_fechaformalizacion));
		if (cal != null) {
			datosPersona.setFechaFormalizacion(cal);
		}
		
		datosPersona.setCodTipoPersonaJuridica(getCodigoElementoTipificado((String)htEJGs.get(dp_tipopersonajuridica_cda)));
		datosPersona.setDescTipoPersonaJuridica(getDescripcionElementoTipificado((String)htEJGs.get(dp_tipopersonajuridica_cda)));						
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
	
	private String getCodigoElementoTipificado(String elementoTipificado) {
		String code = null;
		if (elementoTipificado != null && !elementoTipificado.trim().equals("")) {
			String[] array = elementoTipificado.split("##");
			if (array != null && array.length > 0) {
				code = array[0];
			}
		}
		return code;
	}
	
	private String getDescripcionElementoTipificado(String elementoTipificado) {
		String desc = null;
		if (elementoTipificado != null && !elementoTipificado.trim().equals("")) {
			String[] array = elementoTipificado.split("##");
			if (array != null && array.length > 1) {
				desc = array[1];				
			}
		}
		return desc;
	}
	
	
	private String reemplazaCaracteres(String texto) {
		if (texto != null) {
			for (int i = 0 ; i < texto.getBytes().length; i++) {
				byte b = texto.getBytes()[i];
				if (b == 63) {//cambio de apostrofe ’ por ' 
					byte[] bs = texto.getBytes(); 
					bs[i] = 39;
					texto = new String(bs);
				}
			}
		}
		return texto;
	}
	
	/**
	 * 
	 * @param expedienteType
	 * @param htEJGs
	 * @throws Exception
	 */

	private void datosExpediente(Expediente expediente, Hashtable htEJGs) throws Exception {
		DatosExpediente datosExpediente = expediente.addNewDatosExpediente();
		
		TipoCodigoExpediente tipoCodigoExpediente = getTipoCodigoExpediente(htEJGs);
		TipoCodigoExpedienteServicio codigoExpedienteServicio = getTipoCodigoExpedienteServicio(htEJGs);		
		
		if (codigoExpedienteServicio.getAnyoExpedienteServicio() > 0 && codigoExpedienteServicio.getNumExpedienteServicio() != null && codigoExpedienteServicio.getOrigenExpedienteServicio() != null) {
			datosExpediente.addNewCodigoExpedienteServicio().set(codigoExpedienteServicio);			
		} else {
			datosExpediente.addNewCodigoExpediente().set(tipoCodigoExpediente);			
		}
		
		
		Calendar cal = SigaWSHelper.getCalendar((String)htEJGs.get(DE_FECHASOLICITUD));
		if (cal != null) {
			datosExpediente.setFechaSolicitud(cal);
		}
		datosExpediente.setObservaciones((String)htEJGs.get(DE_OBSERVACIONES2));
		
		String key = getKey(new Object[]{getIdInstitucion(), anyo, numero, idTipoEJG});
		List list = (List) htMarcasExpediente.get(key);
		
		if (list != null && list.size() > 0) {
			MarcasExpediente marcasExpediente = null;
			for (int i = 0; i < list.size(); i++) {
				Hashtable ht = (Hashtable) list.get(i);
				String st = getString((String)ht.get(DE_ME_ME_VALORMARCAEXPEDIENTE));
				if (st != null) {
					marcasExpediente = datosExpediente.addNewMarcasExpediente();
					marcasExpediente.setCodMarcaExpediente(getCodigoElementoTipificado((String)ht.get(DE_ME_ME_MARCAEXPEDIENTE_CDA)));				
					marcasExpediente.setDescMarcaExpediente(getDescripcionElementoTipificado((String)ht.get(DE_ME_ME_MARCAEXPEDIENTE_CDA)));					
					marcasExpediente.setValorMarcaExpediente(st);
				}
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
			
			identificacionIntercambio.setCodOrigenIntercambio(getCodigoElementoTipificado((String)ht.get(ORIGENINTERCAMBIO_CDA)));
			identificacionIntercambio.setDescOrigenIntercambio(getDescripcionElementoTipificado((String)ht.get(ORIGENINTERCAMBIO_CDA)));
			
			identificacionIntercambio.setCodDestinoIntercambio(getCodigoElementoTipificado((String)ht.get(DESTINOINTERCAMBIO_CDA)));
			identificacionIntercambio.setDescDestinoIntercambio(getDescripcionElementoTipificado((String)ht.get(DESTINOINTERCAMBIO_CDA)));
						
			Long valueLong = getLong((String)ht.get(IDENTIFICADORINTERCAMBIO));
			identificacionIntercambio.setIdentificadorIntercambio((valueLong.longValue() * 10) + sufijoIdIntercambio);		
			identificacionIntercambio.setFechaIntercambio(SigaWSHelper.clearCalendar(Calendar.getInstance()));			
			identificacionIntercambio.setVersion((String)ht.get(VERSION_XSD_GENERALITAT));
		}
		return informacionIntercambio;
	}


	@Override
	public void execute() throws Exception {
		
		UsrBean usr = getUsrBean();
		
		String keyPathFicheros = "cajg.directorioFisicoCAJG";
		String keyPathPlantillas = "cajg.directorioPlantillaCAJG";
		String keyPath2 = "cajg.directorioCAJGJava";
				
	    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String pathFichero = p.returnProperty(keyPathFicheros) + p.returnProperty(keyPath2);
		String pathPlantillas = p.returnProperty(keyPathPlantillas) + p.returnProperty(keyPath2);
		
		String dirFicheros = pathFichero + File.separator + getIdInstitucion()  + File.separator + getIdRemesa();
		String dirPlantillas = pathPlantillas + File.separator + getIdInstitucion();

		UserTransaction tx = usr.getTransaction();
		dirFicheros = dirFicheros + File.separator + "xml";
		
		//si se ha hecho una remesa txt previa hay que borrarla previamente
		DefinirRemesasCAJGAction.eliminaFicheroTXTGenerado(String.valueOf(getIdInstitucion()), String.valueOf(getIdRemesa()));//por si se estan regenerando...
		
		//si no queremos generar el fichero txt ademas del xml hay que comentar solamente esta línea
		if (isGeneraTXT()) {
			generaTXT(dirFicheros);
		}
				
		FtpPcajgAbstract ftpPcajgAbstract = null;
		
		try {
			
			tx.begin();
			
			CajgRespuestaEJGRemesaAdm cajgRespuestaEJGRemesaAdm = new CajgRespuestaEJGRemesaAdm(usr);
			cajgRespuestaEJGRemesaAdm.eliminaAnterioresErrores(getIdInstitucion(), getIdRemesa());
			
			List<File> files = generaFicherosXML(dirFicheros, dirPlantillas);
			//hacemos commit por los posibles errores de validación del xml con el xsd
			tx.commit();
			
			if (!isSimular()) {
				if (files != null && files.size() > 0) {
					tx.begin();				
					ftpPcajgAbstract = FtpPcajgFactory.getInstance(usr, String.valueOf(getIdInstitucion()));
					escribeLogRemesa("Conectando al servidor FTP");			
					ftpPcajgAbstract.connect();				
				
					for (File file : files) {
						FileInputStream fis = new FileInputStream(file);
						escribeLogRemesa("Subiendo XML generado al servidor FTP");
						ftpPcajgAbstract.upload(file.getName(), fis);				
						fis.close();				
						escribeLogRemesa("El archivo se ha subido correctamente al servidor FTP");
					}
				
					CajgRemesaEstadosAdm cajgRemesaEstadosAdm = new CajgRemesaEstadosAdm(usr);
					CajgEJGRemesaAdm cajgEJGRemesaAdm = new CajgEJGRemesaAdm(usr);						
					// Marcar como generada
					cajgRemesaEstadosAdm.nuevoEstadoRemesa(usr, getIdInstitucion(), getIdRemesa(), ClsConstants.ESTADO_REMESA_GENERADA);				
					//MARCAMOS COMO ENVIADA
					if (cajgRemesaEstadosAdm.nuevoEstadoRemesa(usr, getIdInstitucion(), getIdRemesa(), ClsConstants.ESTADO_REMESA_ENVIADA )) {
						cajgEJGRemesaAdm.nuevoEstadoEJGRemitidoComision(usr, String.valueOf(getIdInstitucion()), String.valueOf(getIdRemesa()), ClsConstants.REMITIDO_COMISION);
						//cuando se envía el intercambio se envía * 10
						guardarIdIntercambioRemesa((int)intercambioDocument.getIntercambio().getInformacionIntercambio().getIdentificacionIntercambio().getIdentificadorIntercambio()/10);						
					}
					tx.commit();
				}				
			}

		} catch (JSchException e) {
			tx.rollback();
			escribeLogRemesa("Se ha producido un error de conexión con el servidor FTP en la institución " + getIdInstitucion());
			ClsLogging.writeFileLogError("Se ha producido un error en el envío FTP para la institución " + getIdInstitucion(), e, 3);			
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error en al generar y enviar el fichero xml para la institución " + getIdInstitucion(), e, 3);
			tx.rollback();
			throw e;
		} finally {			
			if (ftpPcajgAbstract != null) {
				ftpPcajgAbstract.disconnect();
			}
		}
		
		
		
	}
	


	/**
	 * 
	 * @param pathFichero
	 */
	private void generaTXT(String pathFichero) {
		
		String nombreFichero = getIdInstitucion() + "_" + getIdRemesa() + "_TXT";		
		
		StringBuffer mensaje = new StringBuffer();		
		DefinirRemesasCAJGAction definirRemesasCAJGAction = new DefinirRemesasCAJGAction();
		try {
			definirRemesasCAJGAction.generaFicherosTXT(String.valueOf(getIdInstitucion()), String.valueOf(getIdRemesa()), nombreFichero, mensaje, pathFichero);
		} catch (Exception e) {
			ClsLogging.writeFileLogError("Error al generar el archivo TXT", e, 3);			
		}
		
	}

	public static void main2(String[] args) throws Exception {
		File fileXSL = new File("C:\\Datos\\plantillas\\CAJG\\2047\\plantilla.xsl");
		File file = new File("C:\\Documents and Settings\\angelcpe.ITCGAE-WS011\\Escritorio\\temp\\generalitat\\IED_2047_GEN_12_20091221_5.xml");
		File xmlTrans = new File(file.getParentFile(), "T" + file.getName());
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(new StreamSource(fileXSL));							
		transformer.transform(new StreamSource(file), new StreamResult(xmlTrans));
	}
	
}