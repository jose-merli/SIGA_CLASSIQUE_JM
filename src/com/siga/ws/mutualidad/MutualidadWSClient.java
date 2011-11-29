/**
 * 
 */
package com.siga.ws.mutualidad;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
			stub._setProperty(Constants.ENV_ADDRESSING_SET_MUST_UNDERSTAND, "0"); 

			Integracion_Solicitud_Respuesta response = stub.estadoMutualista(nif, fechaNacimientoCal);
			
			respuesta = transformaRespuesta(response);
			respuesta.setCorrecto(true);
			
		} catch (Exception e) {
			escribeLog("Error en llamada a getPosibilidadSolicitudAlta");
			e.printStackTrace();
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos \n" + e.toString());
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
			Integracion_CuotaYCapitalObjetivoJubilacion response = stub.obtenerCuotaYCapObjetivo(Integer.parseInt(cobertura), sexoInt, fechaNacimientoCal);

			respuesta.setCorrecto(true);
			respuesta.setCapital(response.getCapitalObjetivo());
			respuesta.setCuota(response.getCuota());

		}catch (Exception e) {
			escribeLog("Error en llamada a getCuotaYCapital");
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos");
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

		}catch (Exception e) {
			escribeLog("Error en llamada al recuperar enumerados");
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos " + e.toString());
		}
		return respuesta;
		
	}
/*
	public RespuestaMutualidad altaAccidentesUniversal(AltaMutualidadAbogaciaForm form) throws Exception {
		
		RespuestaMutualidad respuesta = new RespuestaMutualidad();
		try{
			WSHttpBinding_IIntegracion_MetodosStub stub = getStub();
			Calendar cal = Calendar.getInstance();
			
			Integracion_DatosBancarios		datosBancarios = null;
			Integracion_DatosPoliza			datosPoliza = null;
			Integracion_Domicilio[]			datosDomicilio = null;
			Integracion_Persona				datosPersona = null;
			Integracion_Solicitud			datosSolicitud = null;
			Integracion_Solicitud_Estados	datosSolicitudEstados = null;
			Integracion_Beneficiarios		datosBeneficiarios = null;
			// Rellenar estos datos con los que lleguen del formulario
			datosBancarios = rellenarDatosBancarios(form.getCuentaBean());
			//datosDomicilio = rellenarDatosDomicilio(form.getDireccionBean());
			
			Integracion_Solicitud_Respuesta response = stub.MGASolicitudPolizaAccuGratuitos(datosBancarios, datosPoliza,datosDomicilio,datosPersona,datosSolicitud, datosSolicitudEstados,datosBeneficiarios);
			respuesta.setCorrecto(true);

		}catch (Exception e) {
			escribeLog("Error en llamada al solicitar el alta en el seguro gratuito");
			respuesta.setCorrecto(false);
			respuesta.setMensajeError("Imposible comunicar con la mutualidad en estos momentos. Inténtelo de nuevo en unos minutos");
		}
		return respuesta;
		
	}
	*/
	private Integracion_DatosBancarios rellenarDatosBancarios(CenCuentasBancariasBean cuentaBean) {
		Integracion_DatosBancarios datosBancarios = new Integracion_DatosBancarios();
		datosBancarios.setDC(cuentaBean.getDigitoControl());
		datosBancarios.setEntidad(cuentaBean.getCbo_Codigo());
		datosBancarios.setOficina(cuentaBean.getCodigoSucursal());
		datosBancarios.setNCuenta(cuentaBean.getNumeroCuenta());
		// Falta rellenar
		/*
		datosBancarios.setSwift(swift);
		datosBancarios.setIban(iban);
		datosBancarios.setTipoCuenta(TIPO_CUENTA_ESPAÑOLA o TIPO_CUENTA_EXTRANJERA);
		datosBancarios.setIdPoliza(idPoliza);
		*/
		return datosBancarios;
	}

	public RespuestaMutualidad altaPlanProfesional() throws Exception {
		
		RespuestaMutualidad respuesta = new RespuestaMutualidad();
		try{
			WSHttpBinding_IIntegracion_MetodosStub stub = getStub();
			Calendar cal = Calendar.getInstance();
			
			Integracion_DatosBancarios		datosBancarios = null;
			Integracion_DatosPoliza			datosPoliza = null;
			Integracion_Domicilio[]			datosDomicilio = null;
			Integracion_Persona				datosPersona = null;
			Integracion_Solicitud			datosSolicitud = null;
			Integracion_Solicitud_Estados	datosSolicitudEstados = null;
			Integracion_Beneficiarios		datosBeneficiarios = null;
			// Rellenar estos datos con los que lleguen del formulario
			
			Integracion_Solicitud_Respuesta response = stub.MGASolicitudPolizaProfesional(datosBancarios, datosPoliza,datosDomicilio,datosPersona,datosSolicitud, datosSolicitudEstados,datosBeneficiarios);
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
/*			if (resWs.getPDF()!=null){
				DataHandler pdf=resWs.getPDF();
				HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false) ;
				StringBuffer strBuff = new StringBuffer();
		        strBuff.append(session.getServletContext().getRealPath("/")).append(AppConstants.UPLOAD_TEMP_DIRECTORY).append(session.getId());

		        File dir = new File(strBuff.toString());
		        dir.mkdirs();

		        strBuff.append(File.separator).append("Mutualidad_").append(System.currentTimeMillis()).append(".pdf");
		        File file = new File(strBuff.toString());
		        file.createNewFile();
				FileOutputStream out = new FileOutputStream(file);
				pdf.writeTo(out);
				out.close();
				respuesta.setDocumento(strBuff.toString());

			}
*/			

		} catch (Exception e) {
			escribeLog("Error al transformar la respuesta");
		}
		return respuesta;
	}
	
	
	private Map<String, String> transformaCombo(Integracion_TextoValor[] combo){
		Map<String, String> map= new HashMap<String, String>();
		for (Integracion_TextoValor elemento:combo){
			map.put(String.valueOf(elemento.getValor()), elemento.getOpcion());
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
