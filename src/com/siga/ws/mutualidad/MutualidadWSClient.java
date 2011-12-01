/**
 * 
 */
package com.siga.ws.mutualidad;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.Handler;
import org.apache.axis.SimpleChain;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.message.addressing.Constants;
import org.apache.axis.message.addressing.handler.AddressingHandler;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.http.HTTPTransport;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.LogBDDHandler;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.ws.mutualidad.xmlbeans.ObtenerCuotaYCapObjetivoDocument.ObtenerCuotaYCapObjetivo;

/**
 * @author angelcpe
 *
 */
public class MutualidadWSClient extends MutualidadWSClientAbstract {
	
	private static final String URLMUTUALIDADABOGACIA = "http://www.mutualidadabogacia.com/IntegracionColegiosDesarrollo/Integracion_Metodos.svc";
	
	// Constantes de vup
	private static final int  TIPO_CUENTA_ESPAÑOLA = 1;
	private static final int  TIPO_CUENTA_EXTRANJERA = 2;
	private static final int  TIPO_DIRECCION_PARTICULAR = 1;
	private static final int  TIPO_DIRECCION_PROFESIONAL = 2;
	private static final int  TIPO_FISCAL = 1;
	private static final int  TIPO_NO_FISCAL = 2;
	private static final int  TIPO_CONTACTO_PPAL = 1;
	private static final int  TIPO_CONTACTO_PPAL_NO = 2;
	private static final int  TIPO_IDENT_NIF = 1;
	private static final int  ACCIDENTES_GRATUITO = 1;
	private static final int  PLAN_PROFESIONAL = 2;

	
	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Consulta la posibilidad de solicitar el alta en la mutualidad de la abogacía
	 * @param nif
	 * @param fechaNacimiento en formato dd/MM/yyyy
	 * @return
	 * @throws Exception
	 */
	public RespuestaMutualidad getPosibilidadSolicitudAlta(String nif, String fechaNacimiento) throws Exception {

		RespuestaMutualidad respuesta = new RespuestaMutualidad();
		try{
			WSHttpBinding_IIntegracion_MetodosStub stub = getStub();
			
			Calendar fechaNacimientoCal = Calendar.getInstance();
			fechaNacimientoCal = UtilidadesFecha.stringToCalendar(fechaNacimiento);

			Integracion_Solicitud_Respuesta response = stub.estadoMutualista(nif, fechaNacimientoCal);
			
			respuesta.setCorrecto(true);
			
		} catch (Exception e) {
			escribeLog("Error en llamada a getPosibilidadSolicitudAlta");
			e.printStackTrace();
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos.");
		}
		return respuesta;

	}
		
	/**
	 * 
	 * @param fechaNacimiento en formato dd/MM/yyyy
	 * @param sexo 'H'/'M'
	 * @param cobertura
	 * @return
	 * @throws Exception
	 */
	public RespuestaMutualidad getCuotaYCapital(String fechaNacimiento, String sexo, String cobertura)  throws Exception {

		RespuestaMutualidad respuesta = new RespuestaMutualidad();
		try{
			WSHttpBinding_IIntegracion_MetodosStub stub = getStub();
			ObtenerCuotaYCapObjetivo cuota;
			
			Calendar fechaNacimientoCal = Calendar.getInstance();
			fechaNacimientoCal = UtilidadesFecha.stringToCalendar(fechaNacimiento);
			Integer sexoInt = 1;
			if(!sexo.equalsIgnoreCase("H"))sexoInt=2;
			
			Integracion_CuotaYCapitalObjetivoJubilacion response = stub.obtenerCuotaYCapObjetivo(sexoInt, Integer.parseInt(cobertura), fechaNacimientoCal);

			respuesta.setCorrecto(true);
			respuesta.setCapital(response.getCapitalObjetivo());
			respuesta.setCuota(response.getCuota());

		}catch (Exception e) {
			escribeLog("Error en llamada a getCuotaYCapital");
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos.");
		}
		return respuesta;

	}

	/**
	 * Devuelve los combos para mostrar las opciones con las que se puede solicitar el alta
	 * @return Parejas <id,valor>
	 * @throws Exception
	 */
	public RespuestaMutualidad getCombos() throws Exception {
		
		RespuestaMutualidad respuesta = new RespuestaMutualidad();
		try{
			WSHttpBinding_IIntegracion_MetodosStub stub = getStub();
			
			IntegracionEnumsCombos enumCombos=stub.getEnums();
			
			respuesta.setCorrecto(true);
			respuesta.setBeneficiarios(transformaCombo(enumCombos.getDesignacionBeneficiarios()));
			respuesta.setCoberturas(transformaCombo(enumCombos.getOpcionesCoberturas()));
			respuesta.setEstadosCiviles(transformaCombo(enumCombos.getEstadosCiviles()));
			respuesta.setPeriodicidades(transformaCombo(enumCombos.getFormaPago()));
			respuesta.setEjerciente(transformaCombo(enumCombos.getEjerciente()));
			respuesta.setSexos(transformaCombo(enumCombos.getSexos()));
			respuesta.setTiposDireccion(transformaCombo(enumCombos.getTiposDireccion()));
			respuesta.setTiposDomicilio(transformaCombo(enumCombos.getTiposDomicilio()));
			respuesta.setTiposIdentificador(transformaCombo(enumCombos.getTiposIdentificador()));
			respuesta.setAsistencia(transformaCombo(enumCombos.getAsistenciaSanitaria()));
			
		}catch (Exception e) {
			escribeLog("Error en llamada al recuperar enumerados");
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos.");
		}
		return respuesta;
		
	}

	public RespuestaMutualidad altaAccidentesUniversal(Hashtable<String, Hashtable> ht) throws Exception {
		
		RespuestaMutualidad respuesta = new RespuestaMutualidad();
		RespuestaMutualidad combos = new RespuestaMutualidad();
		try{
			WSHttpBinding_IIntegracion_MetodosStub stub = getStub();
			Calendar cal = Calendar.getInstance();
			
			Integracion_DatosBancarios		datosBancarios = null;
			Integracion_DatosPoliza			datosPoliza = null;
			Integracion_Domicilio			datosDireccionDomicilio = null;
			Integracion_Domicilio			datosDireccionDespacho = null;
			Integracion_Persona				datosPersona = null;
			Integracion_Solicitud			datosSolicitud = null;
			Integracion_Solicitud_Estados	datosSolicitudEstados = null;
			Integracion_Beneficiarios		datosBeneficiarios = null;
			Integracion_Domicilio[]			datosDirecciones = new Integracion_Domicilio[2];
			
			// Usamos el getCombos para sacar el catalogo de valores que usa la mutualidad y poder traducir los del SIGA
			combos = this.getCombos();
//			combos.get
			
			// Rellenar estos datos con los que lleguen del formulario
			datosBancarios = rellenarDatosBancarios(ht.get("datosBancarios"));
			datosPoliza = rellenarDatosPoliza(ht.get("datosPoliza"));
			datosDireccionDomicilio = rellenarDatosDireccion(ht.get("datosDireccionDomicilio"));
			datosDireccionDespacho = rellenarDatosDireccion(ht.get("datosDireccionDespacho"));
			datosPersona = rellenarDatosPersona(combos, ht.get("datosPersona"));
			datosBeneficiarios = rellenarDatosBeneficiarios(ht.get("datosBeneficiarios"));
			datosSolicitud = rellenarDatosSolicitudEstados(ht.get("datosSolicitudEstados"));
			datosSolicitud.setIdTipoSolicitud(ACCIDENTES_GRATUITO);
			
			datosDirecciones[0]=datosDireccionDomicilio;
			datosDirecciones[1]=datosDireccionDespacho;
			
			Integracion_Solicitud_Respuesta response = stub.MGASolicitudPolizaAccuGratuitos(datosBancarios, datosPoliza, datosDirecciones, datosPersona, datosSolicitud, datosSolicitudEstados, datosBeneficiarios);
			
			respuesta.setCorrecto(true);

		}catch (Exception e) {
			//escribeLog("Error en llamada al solicitar el alta en el seguro gratuito");
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos");
		}
		return respuesta;
		
	}
	
	/** *****************************************
	 *  Metodos de copia a datos entendibles por el WS
	 ** *****************************************/
	private Integracion_Beneficiarios rellenarDatosBeneficiarios(Hashtable<String, String> ht) {
		Integracion_Beneficiarios db = new Integracion_Beneficiarios();

//		db.setIdPoliza(ht.get("idPoliza"));
		db.setIdTipoBeneficiario(Integer.parseInt(ht.get("idBeneficiario")));
		db.setTextoOtros(ht.get("otrosBeneficiarios"));
		
		return db;
	}

	private Integracion_Solicitud rellenarDatosSolicitudEstados(Hashtable<String, String> ht) {
		Integracion_Solicitud ds = new Integracion_Solicitud();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		ds.setFecha(cal);
		ds.setValorEntrada(ht.get("numeroIdentificacion"));
		ds.setIdTipoIdentificador(TIPO_IDENT_NIF);
		// ds.setIdTipoSolicitud(idTipoSolicitud) // Se setea en cada alta
		
		return ds;
	}

	private Integracion_Persona rellenarDatosPersona(RespuestaMutualidad combos, Hashtable<String, String> ht) {
		Integracion_Persona dp = new Integracion_Persona();
		
		Map<String, String> sexoMap = combos.getSexos();
		dp.setApellido1(ht.get("apellido1"));
		dp.setApellido2(ht.get("apellido2"));
		dp.setNIF(ht.get("numeroIdentificacion"));
		dp.setNacionalidad(ht.get("nacionalidad")); // natural de?
		dp.setNombre(ht.get("nombre"));
		Integer sexoInt=1;
		sexoMap.get("");
		Collection col = sexoMap.values();
		//col.
		if(ht.get("idSexo")!=null && !ht.get("idSexo").equalsIgnoreCase("H"))sexoInt=1;
		dp.setSexo(sexoInt);

		dp.setNumHijos(Integer.parseInt(ht.get("numHijos")));
//		dp.setEdadesHijos(ht.get("edadesHijos[]")));
		
		dp.setProfesion("Abogado"); // Abogado?
		
		dp.setNumColegiado(ht.get("numColegiado"));
		dp.setColegio(ht.get("colegio"));
		
		dp.setEjerciente(Integer.parseInt(ht.get("ejerciente")));	// Parsear
		dp.setEstadoCivil(Integer.parseInt(ht.get("estadoCivil"))); // Parsear
//		dp.setIdMutualista(Integer.parseInt(ht.get("idMutualista")));
		dp.setIdSolicitud(Integer.parseInt(ht.get("idSolicitud")));
//		dp.setAsistenciaSanitaria(Integer.parseInt(ht.get("asistenciaSanitaria")));
		
		Calendar fechaNacimientoCal = Calendar.getInstance();
		fechaNacimientoCal = UtilidadesFecha.stringToCalendar(ht.get("fechaNacimiento"));
		dp.setFNacimiento(fechaNacimientoCal);
		
		Calendar fechaNacimientoConyugeCal = Calendar.getInstance();
		fechaNacimientoConyugeCal = UtilidadesFecha.stringToCalendar(ht.get("fechaNacimientoConyuge"));
		dp.setFNacimientoConyuge(fechaNacimientoConyugeCal);
		
		return dp;
	}

	private Integracion_DatosPoliza rellenarDatosPoliza(Hashtable<String, String> ht) {
		Integracion_DatosPoliza dp = new Integracion_DatosPoliza();
		
		// dp.setFEfecto(FEfecto)
		// dp.setIdMutualista(idMutualista)
		dp.setFormaPago(Integer.parseInt( ht.get("idPeriodicidadPago").toString())); 
		dp.setOpcionesCobertura(Integer.parseInt(ht.get("idCobertura").toString()));
		dp.setTextoOtros(ht.get("otrosBeneficiarios"));
		
		return dp;
	}

	private Integracion_Domicilio rellenarDatosDireccion(Hashtable<String, String> ht) {
		Integracion_Domicilio dd = new Integracion_Domicilio();
		
		dd.setCP(ht.get("codigoPostal"));
		dd.setDireccion(ht.get("domicilio"));
		dd.setEmail(ht.get("correoElectronico"));
		dd.setTfno(ht.get("telef1"));
		dd.setMovil(ht.get("movil"));
		
		dd.setPais(ht.get("pais"));
		dd.setPoblacion(ht.get("poblacion"));
		dd.setProvincia(ht.get("provincia"));
		
//		dd.setIdDireccion(Integer.parseInt( ht.get("idDireccion")));
//		dd.setIdMutualista(Integer.parseInt( ht.get("idMutualista")));

//		dd.setTipoDireccion(Integer.parseInt( ht.get("tipoDireccion")));
//		dd.setTipoDomicilio(Integer.parseInt( ht.get("tipoDomicilio")));
//		dd.setDireccionContacto(Integer.parseInt( ht.get("direccionContacto")));
		
		// Estos valores no aplican porque en SIGA no se usan
		dd.setTipoVia(ht.get(""));
		dd.setBloque(ht.get(""));
		dd.setEsc(ht.get(""));
		dd.setLetra(ht.get(""));
		dd.setNum(ht.get(""));
		dd.setPiso(ht.get(""));
		
		return dd;
	}

	private Integracion_DatosBancarios rellenarDatosBancarios(Hashtable<String, String> ht) {
		Integracion_DatosBancarios db = new Integracion_DatosBancarios();
		
		// Solo puede ser uno de los dos
		if(ht.get("swift")!=null && ht.get("iban")!=null){
			db.setTipoCuenta(TIPO_CUENTA_EXTRANJERA);
			db.setIban(ht.get("iban"));
			db.setSwift(ht.get("swift"));
		}else{
			db.setTipoCuenta(TIPO_CUENTA_ESPAÑOLA);
			db.setEntidad(ht.get("cboCodigo"));
			db.setOficina(ht.get("codigoSucursal"));
			db.setDC(ht.get("digitoControl"));
			db.setNCuenta(ht.get("numeroCuenta"));
		}
		
		return db;
	}

	public RespuestaMutualidad altaPlanProfesional(Hashtable<String, Hashtable> ht) throws Exception {
		
		RespuestaMutualidad respuesta = new RespuestaMutualidad();
		RespuestaMutualidad combos = new RespuestaMutualidad();
		try{
			WSHttpBinding_IIntegracion_MetodosStub stub = getStub();
			Calendar cal = Calendar.getInstance();
			
			Integracion_DatosBancarios		datosBancarios = null;
			Integracion_DatosPoliza			datosPoliza = null;
			Integracion_Domicilio			datosDireccionDomicilio = null;
			Integracion_Domicilio			datosDireccionDespacho = null;
			Integracion_Persona				datosPersona = null;
			Integracion_Solicitud			datosSolicitud = null;
			Integracion_Solicitud_Estados	datosSolicitudEstados = null;
			Integracion_Beneficiarios		datosBeneficiarios = null;
			Integracion_Domicilio[]			datosDirecciones = new Integracion_Domicilio[2];
			
			// Usamos el getCombos para sacar el catalogo de valores que usa la mutualidad y poder traducir los del SIGA
			combos = this.getCombos();
//			combos.get

			datosBancarios = rellenarDatosBancarios(ht.get("datosBancarios"));
			datosPoliza = rellenarDatosPoliza(ht.get("datosPoliza"));
			datosDireccionDomicilio = rellenarDatosDireccion(ht.get("datosDireccionDomicilio"));
			datosDireccionDespacho = rellenarDatosDireccion(ht.get("datosDireccionDespacho"));
			datosPersona = rellenarDatosPersona(combos, ht.get("datosPersona"));
			datosBeneficiarios = rellenarDatosBeneficiarios(ht.get("datosBeneficiarios"));
			datosSolicitud = rellenarDatosSolicitudEstados(ht.get("datosSolicitudEstados"));
			datosSolicitud.setIdTipoSolicitud(PLAN_PROFESIONAL);
			
			Integracion_Solicitud_Respuesta response = stub.MGASolicitudPolizaProfesional(datosBancarios, datosPoliza, datosDirecciones, datosPersona, datosSolicitud, datosSolicitudEstados, datosBeneficiarios);
			
			respuesta.setCorrecto(true);

		}catch (Exception e) {
			escribeLog("Error en llamada al solicitar alta en el plan profesional");
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos");
		}
		return respuesta;
		
	}
	
/*
	private void saveXML(SIGAAsignaDocument sigaAsignaDocument) throws IOException {
		
		String keyPathFicheros = "cajg.directorioFisicoCAJG";		
		String keyPath2 = "cajg.directorioCAJGJava";				
	    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String pathFichero = p.returnProperty(keyPathFicheros) + p.returnProperty(keyPath2);
		pathFichero += File.separator + getIdInstitucion() + File.separator + "xml";
		
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrintIndent(4);
		xmlOptions.setSavePrettyPrint();		
		
		File file = new File(pathFichero);
		file.mkdirs();
		file = new File(file, sigaAsignaDocument.getSIGAAsigna().getDtExpedientes().getIDExpedienteSIGA()+".xml");
		
		ClsLogging.writeFileLog("Guardando fichero de envío webservice del colegio " + getIdInstitucion(), 3);
		ClsLogging.writeFileLog("Ruta del fichero: " + file.getAbsolutePath(), 3);
		sigaAsignaDocument.save(file, xmlOptions);
		
	}
*/


	private void trataError(Exception e) throws ClsExceptions, IOException {
		String descripcionError = "Error al enviar la solicitud. ";
		if (e.getMessage().indexOf("Non nillable element") > -1) {
			String campo = e.getMessage().substring(e.getMessage().indexOf("'"), e.getMessage().lastIndexOf("'"));
			descripcionError += "El campo '" + campo + "' no puede estar vacío."; 
		} else {
			 descripcionError += e.getMessage();
		}
		
		escribeLog(descripcionError);
		escribeLog("Se ha producido un error al enviar la solicitud");					
		ClsLogging.writeFileLogError(descripcionError, e, 3);		
	}



	/**
	 * 
	 * @return
	 */
	private EngineConfiguration createClientConfig(UsrBean usrBean, String idInstitucion, String logDescripcion) {
		
		SimpleProvider clientConfig = new SimpleProvider();		
		Handler logSIGAasignaHandler = (Handler) new LogBDDHandler(usrBean, idInstitucion, logDescripcion);		
		Handler ah = (Handler) new AddressingHandler();
		
		System.setProperty(Constants.ENV_ADDRESSING_NAMESPACE_URI, "http://www.w3.org/2005/08/addressing"); 
		
		SimpleChain reqHandler = new SimpleChain();
		SimpleChain respHandler = new SimpleChain();
		
		reqHandler.addHandler(ah);
		reqHandler.addHandler(logSIGAasignaHandler);
		
		respHandler.addHandler(ah);
		respHandler.addHandler(logSIGAasignaHandler);
		Handler pivot = (Handler) new HTTPSender();
		
		Handler transport = new SimpleTargetedChain(reqHandler, pivot, respHandler);
		clientConfig.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME, transport);
		return clientConfig;
	}



	/**
	 * 
	 * @param st
	 * @return
	 */


	private WSHttpBinding_IIntegracion_MetodosStub getStub() throws AxisFault, MalformedURLException {

		Integracion_MetodosLocator locator = new Integracion_MetodosLocator(createClientConfig(getUsrBean(), String.valueOf(getIdInstitucion()), "Envío y recepción webservice del colegio " + getIdInstitucion()));
		WSHttpBinding_IIntegracion_MetodosStub stub = new WSHttpBinding_IIntegracion_MetodosStub(new java.net.URL(URLMUTUALIDADABOGACIA), locator);

		return stub;
	}

	/**
	 * Transforma la respuesta de integracion en una respuesta válida para integrarse con SIGA
	 * @param resWs Integracion_Solicitud_Respuesta
	 * @return RespuestaMutualidad
	 * @throws IOException
	 */
	private RespuestaMutualidad transformaRespuesta(Integracion_Solicitud_Respuesta resWs) throws IOException{
		RespuestaMutualidad respuesta = new RespuestaMutualidad();
		try{
			if (resWs.getValorRespuesta()!=null && (resWs.getValorRespuesta().equals("1") || resWs.getValorRespuesta().equals("NIF no encontrado."))) {
				respuesta.setCorrecto(true);
			}else {
				respuesta.setCorrecto(false);
				respuesta.setMensajeError(resWs.getValorRespuesta());
			}
			respuesta.setIdSolicitud(resWs.getIdSolicitud());
			
//			if (resWs.getPDF()!=null){
//				DataHandler pdf=resWs.getPDF();
//				HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false) ;
//				StringBuffer strBuff = new StringBuffer();
//		        strBuff.append(session.getServletContext().getRealPath("/")).append(AppConstants.UPLOAD_TEMP_DIRECTORY).append(session.getId());
//
//		        File dir = new File(strBuff.toString());
//		        dir.mkdirs();
//
//		        strBuff.append(File.separator).append("Mutualidad_").append(System.currentTimeMillis()).append(".pdf");
//		        File file = new File(strBuff.toString());
//		        file.createNewFile();
//				FileOutputStream out = new FileOutputStream(file);
//				pdf.writeTo(out);
//				out.close();
//				respuesta.setDocumento(strBuff.toString());
//
//			}

		} catch (Exception e) {
			escribeLog("Error al transformar la respuesta");
		}
		return respuesta;
	}
	
	
	private TreeMap<String, String> transformaCombo(Integracion_TextoValor[] combo){
		TreeMap<String, String> map= new TreeMap<String, String>();
		for (Integracion_TextoValor elemento:combo){
			map.put(elemento.getValor().toString(), elemento.getOpcion());
		}
		return map;
		
	}
	
	
	/** ***********************
	 * Metodos de conversion no utilizados
	 * ************************
	
	private boolean isNull(String st) {
		return st == null || st.trim().equals("");
	}
	
	private BigInteger getBigInteger(String st) {
		BigInteger bigInteger = null;
		if (st != null && !st.trim().equals("")) {
			bigInteger = new BigInteger(st);
		}
		return bigInteger;
	}
	
	private Integer getInteger(String st) {		
		Integer in = null;
		if (st != null && !st.trim().equals("")) {
			in = Integer.valueOf(st);
		}
		return in;
	}
	
	private PositiveInteger getPositiveInteger(String st) {
		PositiveInteger posInt = null;
		if (st != null && !st.trim().equals("")) {
			posInt = new PositiveInteger(st);
		}
		return posInt;
	}
	
	private Boolean getBoolean(String st) {
		Boolean b = null;
		if (st != null){
			if (st.trim().equals("1")) {
				b = true;
			} else {
				b = false;
			}			
		}
		return b;
	}
	 */

}
