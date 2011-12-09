/**
 * 
 */
package com.siga.ws.mutualidad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
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
import com.siga.beans.GenParametrosAdm;
import com.siga.general.SIGAException;
import com.siga.ws.mutualidad.xmlbeans.ObtenerCuotaYCapObjetivoDocument.ObtenerCuotaYCapObjetivo;

/**
 * @author angelcpe
 *
 */
public class MutualidadWSClient extends MutualidadWSClientAbstract {
	
	private static final String URLMUTUALIDADABOGACIA = "URL_WS_MUTUALIDAD";
	
	// Constantes de vup
	private static final int  TIPO_CUENTA_ESPA�OLA = 1;
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
		this.setUsrBean(user);
		this.setIdInstitucion(Integer.parseInt(user.getLocation()));
	}
	
	/**
	 * Consulta la posibilidad de solicitar el alta en la mutualidad de la abogac�a
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
            if(!response.getValorRespuesta().equals("1"))
                throw new SIGAException("error.inesperado.estadoMutualista");
           
            respuesta.setCorrecto(true);
           
        } catch (SIGAException e) {
            escribeLog(e.getMessage());
            e.printStackTrace();
            respuesta.setCorrecto(false);
            respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Int�ntelo de nuevo en unos minutos.");
        } catch (Exception e) {
            escribeLog("Error en llamada a getPosibilidadSolicitudAlta");
            e.printStackTrace();
            respuesta.setCorrecto(false);
            respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Int�ntelo de nuevo en unos minutos.");
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
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Int�ntelo de nuevo en unos minutos.");
		}
		return respuesta;

	}
	
	public RespuestaMutualidad getEstadoSolicitud(Long idSolicitud) throws Exception {

        RespuestaMutualidad respuesta = new RespuestaMutualidad();
        try{
            WSHttpBinding_IIntegracion_MetodosStub stub = getStub();
           
            Integracion_Solicitud_Respuesta response = stub.estadoSolicitud(idSolicitud,Boolean.TRUE );
            respuesta.setValorRespuesta(response.getValorRespuesta());
            respuesta.setCorrecto(true);
        } catch (Exception e) {
            escribeLog("Error en llamada a getEstadoSolicitud");
            respuesta.setCorrecto(false);
            respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Int�ntelo de nuevo en unos minutos.");
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
			escribeLog("Error en llamada al recuperar enumerados: " + e );
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Int�ntelo de nuevo en unos minutos.");
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
			
			// Rellenar estos datos con los que lleguen del formulario
			datosBancarios = rellenarDatosBancarios(ht.get("datosBancarios"));
			datosPoliza = rellenarDatosPoliza(ht.get("datosPoliza"));
			datosDireccionDomicilio = rellenarDatosDireccion(ht.get("datosDireccionDomicilio"));
			datosDireccionDespacho = rellenarDatosDireccion(ht.get("datosDireccionDespacho"));
			datosPersona = rellenarDatosPersona(ht.get("datosPersona"));
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
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Int�ntelo de nuevo en unos minutos");
		}
		return respuesta;
		
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

			datosBancarios = rellenarDatosBancarios(ht.get("datosBancarios"));
			datosPoliza = rellenarDatosPoliza(ht.get("datosPoliza"));
			datosDireccionDomicilio = rellenarDatosDireccion(ht.get("datosDireccionDomicilio"));
			datosDireccionDespacho = rellenarDatosDireccion(ht.get("datosDireccionDespacho"));
			datosPersona = rellenarDatosPersona(ht.get("datosPersona"));
			datosBeneficiarios = rellenarDatosBeneficiarios(ht.get("datosBeneficiarios"));
			datosSolicitud = rellenarDatosSolicitudEstados(ht.get("datosSolicitudEstados"));
			datosSolicitud.setIdTipoSolicitud(PLAN_PROFESIONAL);
			
			Integracion_Solicitud_Respuesta response = stub.MGASolicitudPolizaProfesional(datosBancarios, datosPoliza, datosDirecciones, datosPersona, datosSolicitud, datosSolicitudEstados, datosBeneficiarios);
			
			respuesta.setCorrecto(true);

		}catch (Exception e) {
			escribeLog("Error en llamada al solicitar alta en el plan profesional");
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Int�ntelo de nuevo en unos minutos");
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

		dp.setNumHijos(Integer.parseInt(ht.get("numHijos")));
		// TODO // jbd // Recorrer las edades para meterlas en el los datos personales
//		dp.setEdadesHijos(ht.get("edadesHijos[]")));
		
		dp.setProfesion("Abogado"); // Ponemos fijo Abogado porque nadie mas lo va a usar
		
//		dp.setNumColegiado(ht.get("numColegiado")); // No se puede pasar si aun no es colegiado
		dp.setColegio(ht.get("colegio"));
		
		dp.setAsistenciaSanitaria(Integer.parseInt(ht.get("asistenciaSanitaria")));
		
		dp.setEjerciente(Integer.parseInt(ht.get("ejerciente")));	// Parsear
		dp.setEstadoCivil(Integer.parseInt(ht.get("estadoCivil"))); // Parsear
		dp.setIdSolicitud(Integer.parseInt(ht.get("idSolicitud")));
//		dp.setIdMutualista(Integer.parseInt(ht.get("idMutualista")));
		
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
			db.setTipoCuenta(TIPO_CUENTA_ESPA�OLA);
			db.setEntidad(ht.get("cboCodigo"));
			db.setOficina(ht.get("codigoSucursal"));
			db.setDC(ht.get("digitoControl"));
			db.setNCuenta(ht.get("numeroCuenta"));
		}
		
		return db;
	}
	

	private void trataError(Exception e) throws ClsExceptions, IOException {
		String descripcionError = "Error al enviar la solicitud. ";
		if (e.getMessage().indexOf("Non nillable element") > -1) {
			String campo = e.getMessage().substring(e.getMessage().indexOf("'"), e.getMessage().lastIndexOf("'"));
			descripcionError += "El campo '" + campo + "' no puede estar vac�o."; 
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

	/**
	 * Transforma la respuesta de integracion en una respuesta v�lida para integrarse con SIGA
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
			byte[] pdf=resWs.getPDF();
			FileOutputStream out = new FileOutputStream("C:/ABC_XYZ/1.pdf");
			out.write(pdf, 0, pdf.length);
			out.close();

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
	
	private Map<String, String> invertirMap(Map<String, String> map){
		Map<String, String> resul = null;
		
		Set<String> keys = map.keySet();
		Iterator it = keys.iterator();
		String clave,valor;
		while (it.hasNext()) {
	      clave=(String) it.next();
	      valor=map.get(clave);
	      resul.put(valor, clave);
	    }
		return resul;
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
