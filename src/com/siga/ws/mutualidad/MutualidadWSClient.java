/**
 * 
 */
package com.siga.ws.mutualidad;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sf.antcontrib.logic.Switch.Case;

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
import com.siga.general.SIGAException;
import com.siga.ws.mutualidad.xmlbeans.ObtenerCuotaYCapObjetivoDocument.ObtenerCuotaYCapObjetivo;

/**
 * 
 * @author josebd
 *
 */
public class MutualidadWSClient extends MutualidadWSClientAbstract {
	
	private static final String URLMUTUALIDADABOGACIA = "URL_WS_MUTUALIDAD";
	
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

	
	public MutualidadWSClient(UsrBean user) {
		super.setUsrBean(user);
		super.setIdInstitucion(Integer.parseInt(user.getLocation()));
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
            respuesta.setValorRespuesta(response.getValorRespuesta());
            respuesta.setPDF(response.getPDF());          	
            if(respuesta.getValorRespuesta().trim().equalsIgnoreCase("1")){
            	respuesta.setPosibleAlta(true);
            }else{
            	respuesta.setPosibleAlta(false);
            }
           
        } catch (Exception e) {
            escribeLog("Error en llamada a getPosibilidadSolicitudAlta: " + e.getMessage());
            e.printStackTrace();
            respuesta.setCorrecto(false);
            respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos.");
            throw new SIGAException("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos");
        }
        return respuesta;

    }
		
	
	public RespuestaMutualidad getEstadoMutualista(String nif, String fechaNacimiento) throws Exception {
		RespuestaMutualidad respuesta = new RespuestaMutualidad();
        try{
            WSHttpBinding_IIntegracion_MetodosStub stub = getStub();
           
            Calendar fechaNacimientoCal = Calendar.getInstance();
            fechaNacimientoCal = UtilidadesFecha.stringToCalendar(fechaNacimiento);

            Integracion_Solicitud_Respuesta response = stub.estadoMutualista(nif, fechaNacimientoCal);
            respuesta.setCorrecto(true);
            respuesta.setValorRespuesta(response.getValorRespuesta());
            respuesta.setPDF(response.getPDF());          	
            if(respuesta.getValorRespuesta().equalsIgnoreCase("1")){
            	respuesta.setPosibleAlta(true);
            }else{
            	respuesta.setPosibleAlta(false);
            }
           
        } catch (Exception e) {
            escribeLog("Error en llamada a getPosibilidadSolicitudAlta: " + e.getMessage());
            e.printStackTrace();
            respuesta.setCorrecto(false);
            respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos.");
            throw new SIGAException("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos");
        }
        return respuesta;
	}
		
	/**
	 * En funcion de los parametros de entrada nos da la cuota y capital para la cobertura elegida
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
			escribeLog("Error en llamada a getCuotaYCapital: " + e.getMessage());
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos.");
			throw new SIGAException("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos");
		}
		return respuesta;

	}
	
	/**
	 * Devuelve el estado de una solicitud
	 * @param idSolicitud
	 * @return RespuestaMutualidad - La respuesta del sistema en ValorRespues y el PDF de la solicitud
	 * @throws Exception
	 */
	public RespuestaMutualidad getEstadoSolicitud(Long idSolicitud) throws Exception {

        RespuestaMutualidad respuesta = new RespuestaMutualidad();
        try{
            WSHttpBinding_IIntegracion_MetodosStub stub = getStub();
           
            Integracion_Solicitud_Respuesta response = stub.estadoSolicitud(idSolicitud,Boolean.FALSE );
            respuesta.setValorRespuesta(response.getValorRespuesta());
            respuesta.setCorrecto(true);
            respuesta.setPDF(response.getPDF());
        } catch (Exception e) {
            escribeLog("Error en llamada a getEstadoSolicitud: " + e.getMessage());
            respuesta.setCorrecto(false);
            respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos.");
            throw new SIGAException("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos");
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
			respuesta.setCorrecto(true);
			
		}catch (Exception e) {
			escribeLog("Error en llamada al recuperar enumerados: " + e.getMessage());
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos.");
			throw new SIGAException("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos");
		}
		return respuesta;
		
	}

	public RespuestaMutualidad altaAccidentesUniversal(Hashtable<String, Hashtable> ht) throws Exception {
		
		RespuestaMutualidad respuesta = new RespuestaMutualidad();
		try{
			WSHttpBinding_IIntegracion_MetodosStub stub = getStub();
			
			Integracion_DatosBancarios		datosBancarios = null;
			Integracion_DatosPoliza			datosPoliza = null;
			Integracion_Domicilio			datosDireccionDomicilio = null;
			Integracion_Domicilio			datosDireccionDespacho = null;
			Integracion_Persona				datosPersona = null;
			Integracion_Solicitud			datosSolicitud = null;
			Integracion_Solicitud_Estados	datosSolicitudEstados = null;
			Integracion_Beneficiarios		datosBeneficiarios = null;
			Integracion_Domicilio[]			datosDirecciones = new Integracion_Domicilio[1];
			
			datosDireccionDespacho = rellenarDatosDireccion(ht.get("datosDireccionDespacho"));
			datosDirecciones[0]=datosDireccionDespacho;
			datosPersona = rellenarDatosPersona(ht.get("datosPersona"));
			datosSolicitud = rellenarDatosSolicitud(ht.get("datosSolicitudEstados"));
			datosBancarios = new Integracion_DatosBancarios();
			datosPoliza = new Integracion_DatosPoliza();
			datosBeneficiarios = new Integracion_Beneficiarios();
			datosSolicitudEstados = new Integracion_Solicitud_Estados();
			datosSolicitud.setIdTipoSolicitud(ACCIDENTES_GRATUITO);
			
			Integracion_Solicitud_Respuesta response = stub.MGASolicitudPolizaAccuGratuitos(datosBancarios, datosPoliza, datosDirecciones, datosPersona, datosSolicitud, datosSolicitudEstados, datosBeneficiarios);
			
			respuesta.setCorrecto(true);
			respuesta.setIdSolicitud(response.getIdSolicitud());
			respuesta.setPDF(response.getPDF());
			
		}catch (Exception e) {
			escribeLog("Error en llamada al solicitar el alta en el seguro gratuito: " + e.getMessage());
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos.");
			throw new SIGAException("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos");
		}
		return respuesta;
		
	}

	public RespuestaMutualidad altaPlanProfesional(Hashtable<String, Hashtable> ht) throws Exception {
		
		RespuestaMutualidad respuesta = new RespuestaMutualidad();
		try{
			WSHttpBinding_IIntegracion_MetodosStub stub = getStub();
			
			Integracion_DatosBancarios		datosBancarios = null;
			Integracion_DatosPoliza			datosPoliza = null;
			Integracion_Domicilio			datosDireccionDomicilio = null;
			Integracion_Domicilio			datosDireccionDespacho = null;
			Integracion_Persona				datosPersona = null;
			Integracion_Solicitud			datosSolicitud = null;
			Integracion_Solicitud_Estados	datosSolicitudEstados = null;
			Integracion_Beneficiarios		datosBeneficiarios = null;
			Integracion_Domicilio[]			datosDirecciones = new Integracion_Domicilio[2];
			

			datosBancarios = rellenarDatosBancarios(ht.get("datosBancarios"));
			datosPoliza = rellenarDatosPoliza(ht.get("datosPoliza"));
			datosDireccionDomicilio = rellenarDatosDireccion(ht.get("datosDireccionDomicilio"));
			datosDireccionDomicilio.setTipoDomicilio(1);
			datosDireccionDomicilio.setDireccionContacto(1);
			datosDireccionDespacho = rellenarDatosDireccion(ht.get("datosDireccionDespacho"));
			datosDireccionDespacho.setTipoDomicilio(2);
			datosDireccionDespacho.setDireccionContacto(2);
			datosDirecciones[0]=datosDireccionDomicilio;
			datosDirecciones[1]=datosDireccionDespacho;
			datosPersona = rellenarDatosPersona(ht.get("datosPersona"));
			datosBeneficiarios = rellenarDatosBeneficiarios(ht.get("datosBeneficiarios"));
			datosSolicitud = rellenarDatosSolicitud(ht.get("datosSolicitudEstados"));
			datosSolicitudEstados = rellenarDatosSolicitudEstados(ht.get("datosSolicitudEstados"));
			datosSolicitud.setIdTipoSolicitud(PLAN_PROFESIONAL);
			
			Integracion_Solicitud_Respuesta response = stub.MGASolicitudPolizaProfesional(datosBancarios, datosPoliza, datosDirecciones, datosPersona, datosSolicitud, datosSolicitudEstados, datosBeneficiarios);
			
			respuesta.setCorrecto(true);
			respuesta.setIdSolicitud(response.getIdSolicitud());
			respuesta.setPDF(response.getPDF());

		}catch (Exception e) {
			escribeLog("Error en llamada al solicitar alta en el plan profesional: " + e.getMessage());
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos.");
			throw new SIGAException("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos");
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

	private Integracion_Solicitud rellenarDatosSolicitud(Hashtable<String, String> ht) {
		Integracion_Solicitud ds = new Integracion_Solicitud();
		
		Calendar cal = Calendar.getInstance();
		ds.setFecha(cal);
		ds.setValorEntrada(ht.get("numeroIdentificacion"));
		ds.setIdTipoIdentificador(TIPO_IDENT_NIF);
		// ds.setIdTipoSolicitud(idTipoSolicitud) // Se setea en el metodo que crea alta
		
		return ds;
	}

	private Integracion_Solicitud_Estados rellenarDatosSolicitudEstados(Hashtable<String, String> ht) {
		Integracion_Solicitud_Estados ds = new Integracion_Solicitud_Estados();
		return ds;
	}
		
	private Integracion_Persona rellenarDatosPersona(Hashtable<String, String> ht) {
		Integracion_Persona dp = new Integracion_Persona();
		
		dp.setApellido1(ht.get("apellido1"));
		dp.setApellido2(ht.get("apellido2"));
		dp.setNIF(ht.get("numeroIdentificacion"));
		dp.setNacionalidad(ht.get("nacionalidad")); // natural de?
		dp.setNombre(ht.get("nombre"));
		Integer sexoInt=1;
		if(ht.get("idSexo")!=null && !ht.get("idSexo").equalsIgnoreCase("H"))sexoInt=2;
		dp.setSexo(sexoInt);

		if(ht.get("numHijos")!=null && !ht.get("numHijos").equalsIgnoreCase("")){
			dp.setNumHijos(Integer.parseInt(ht.get("numHijos")));
		}else{
			dp.setNumHijos(0);
		}
		String edades = ht.get("edadesHijos");
		if(!edades.equalsIgnoreCase(""))
			dp.setEdadesHijos(edades.split(","));
		
		dp.setProfesion("Abogado"); // Ponemos fijo Abogado porque nadie mas lo va a usar
		
		dp.setColegio("");
		
		dp.setAsistenciaSanitaria(Integer.parseInt(ht.get("asistenciaSanitaria")));
		
		dp.setEjerciente(parseaEjerciente(ht.get("ejerciente")));
		dp.setEstadoCivil(parseaEstadoCivil(ht.get("estadoCivil")));
		
		if(ht.get("fechaNacimiento")!=null && !ht.get("fechaNacimiento").equalsIgnoreCase("")){
			Calendar fechaNacimientoCal = Calendar.getInstance();
			fechaNacimientoCal.setTime(UtilidadesFecha.stringToCalendar(ht.get("fechaNacimiento")).getTime());
			dp.setFNacimiento(fechaNacimientoCal);
		}
		
		if(ht.get("fechaNacimientoConyuge")!=null && !ht.get("fechaNacimientoConyuge").equalsIgnoreCase("")){
			Calendar fechaNacimientoConyugeCal = Calendar.getInstance();
			fechaNacimientoConyugeCal.setTime(UtilidadesFecha.stringToCalendar(ht.get("fechaNacimientoConyuge")).getTime());
			dp.setFNacimientoConyuge(fechaNacimientoConyugeCal);
		}
		return dp;
	}


	private Integracion_DatosPoliza rellenarDatosPoliza(Hashtable<String, String> ht) {
		Integracion_DatosPoliza dp = new Integracion_DatosPoliza();
		
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
			
		// Estos valores no aplican porque en SIGA no se usan
		dd.setTipoVia("");
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
	 * @throws ClsExceptions 
	 */


	private WSHttpBinding_IIntegracion_MetodosStub getStub() throws ClsExceptions {

		String urlWS = getUrlWS(URLMUTUALIDADABOGACIA);

		WSHttpBinding_IIntegracion_MetodosStub stub;
		try {
			Integracion_MetodosLocator locator = new Integracion_MetodosLocator(createClientConfig(getUsrBean(), String.valueOf(getIdInstitucion()), "Solicitud WSMutualidad desde " + getIdInstitucion()));
			stub = new WSHttpBinding_IIntegracion_MetodosStub(new java.net.URL(urlWS), locator);
		} catch (Exception e) {
			throw new ClsExceptions("error.inesperado.estadoMutualista");
		}

		return stub;
	}

	
	
	private TreeMap<String, String> transformaCombo(Integracion_TextoValor[] combo){
		TreeMap<String, String> map= new TreeMap<String, String>();
		for (Integracion_TextoValor elemento:combo){
			map.put(elemento.getValor().toString(), elemento.getOpcion());
		}
		return map;
		
	}
	
	/**
	 * Parsea el modo de ejercicio de los valores del SIGA a los de la Mutualidad
	 * @param ejerciente
	 * @return
	 */
	private Integer parseaEjerciente(String ejerciente) {
		int ejercienteWS = 0;
		int ejercienteSIGA = 0;
		if(!ejerciente.equalsIgnoreCase("")){
			ejercienteSIGA = Integer.parseInt(ejerciente);
		}
		
		switch (ejercienteSIGA){
		case 10:	ejercienteWS = 1; break; // Reincorporación Ejerciente
		case 20:	ejercienteWS = 1; break; // Reincorporación No Ejerciente
		case 30:	ejercienteWS = 1; break; // Incorporación Ejerciente
		case 40:	ejercienteWS = 1; break; // Incorporación No Ejerciente
		default:	ejercienteWS = 1; break; // No se debe dar el caso
		// De momento ponemos todo como cuenta propia en vez de ajena
		}
		return ejercienteWS;
	}

	/**
	 * Parsea el estado civil de los valores del SIGA a los de la Mutualidad
	 * @param eCivil
	 * @return
	 */
	private Integer parseaEstadoCivil(String eCivil) {
		
		int eCivilWS = 0;
		int eCivilSIGA = 0;
		if(!eCivil.equalsIgnoreCase("")){
			eCivilSIGA = Integer.parseInt(eCivil);
		}
		switch (eCivilSIGA){
		case 1:	eCivilWS = 1; break; // Casado
		case 2:	eCivilWS = 0; break; // Soltero
		case 3:	eCivilWS = 2; break; // Viudo
		case 4:	eCivilWS = 3; break; // Separado
		case 5:	eCivilWS = 4; break; // Divorciado
		default:eCivilWS = 5; break; // Desconocido
		}
		return eCivilWS;
	}

}
