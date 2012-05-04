package com.siga.censo.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ReadProperties;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGAReferences;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenColegiadoAdm;
import com.siga.beans.CenColegiadoBean;
import com.siga.beans.CenCuentasBancariasAdm;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenEstadoColegialAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenPaisAdm;
import com.siga.beans.CenPaisBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPoblacionesAdm;
import com.siga.beans.CenSolicitudAlterMutuaAdm;
import com.siga.beans.CenSolicitudAlterMutuaBean;
import com.siga.beans.CenSolicitudIncorporacionAdm;
import com.siga.beans.CenSolicitudIncorporacionBean;
import com.siga.beans.GenCatalogosWSAdm;
import com.siga.censo.form.AlterMutuaForm;
import com.siga.censo.service.AlterMutuaService;
import com.siga.ws.alterMutua.AlterMutuaHelper;
import com.siga.ws.alterMutua.AlterMutuaWSClient;
import com.siga.ws.alterMutua.WSAsegurado;
import com.siga.ws.alterMutua.WSCuentaBancaria;
import com.siga.ws.alterMutua.WSDireccion;
import com.siga.ws.alterMutua.WSPersona;
import com.siga.ws.alterMutua.WSPropuesta;
import com.siga.ws.alterMutua.WSRespuesta;
import com.siga.ws.alterMutua.WSSolicitud;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.JtaBusinessServiceTemplate;

public class AtosAlterMutuaService extends JtaBusinessServiceTemplate 
	implements AlterMutuaService {

	public AtosAlterMutuaService(BusinessManager businessManager) {
		super(businessManager);
	}

	public Object executeService(Object... parameters) throws BusinessException {
		WSRespuesta respuesta = null;
		return respuesta;
	}

	public AlterMutuaForm getPropuestas(AlterMutuaForm alterMutuaForm, UsrBean usr)
			throws Exception {
		// Convertimos los datos de entrada a datos que entienda el WS
		int tipoIdent = AlterMutuaHelper.getTipoIdentificacionAM(alterMutuaForm.getIdTipoIdentificacion());
		String ident = alterMutuaForm.getIdentificador();
		Calendar fechaNacimiento = UtilidadesFecha.stringToCalendar(alterMutuaForm.getFechaNacimiento());
		int sexo = AlterMutuaHelper.getSexoAM(alterMutuaForm.getSexo());
		int propuesta = alterMutuaForm.getPropuesta();
		
		// Realizamos la llamada al WS
		AlterMutuaWSClient wsClient = new AlterMutuaWSClient(usr);
		WSRespuesta respuesta = wsClient.getPropuestas(tipoIdent, ident, fechaNacimiento, sexo, propuesta);
		
		// Si no devuelve error, y devuelve algo coherente, lo metemos en el formulario
		if(!respuesta.isError() && respuesta.getPropuestas()!=null){
			alterMutuaForm.setPropuestas(respuesta.getPropuestas());
			alterMutuaForm.setNumeroPropuestas(respuesta.getPropuestas().length);
		}
		
		// Por ultimo actualizamos el error y mensaje
		alterMutuaForm.setError(respuesta.isError());
		if(alterMutuaForm.isError())
			alterMutuaForm.setMsgRespuesta(respuesta.getMensaje());
		
		return alterMutuaForm;
	}

	public AlterMutuaForm getEstadoColegiado(AlterMutuaForm alterMutuaForm, UsrBean usr)
			throws Exception {
		// Convertimos los datos de entrada a datos que entienda el WS
		int tipoIdent = AlterMutuaHelper.getTipoIdentificacionAM(alterMutuaForm.getIdTipoIdentificacion());
		String ident = alterMutuaForm.getIdentificador();
		
		// Realizamos la llamada al WS
		AlterMutuaWSClient wsClient = new AlterMutuaWSClient(usr);
		WSRespuesta respuesta = wsClient.getEstadoColegiado(tipoIdent, ident);
		
		// Si no devuelve error, y devuelve algo coherente, lo metemos en el formulario
		if(!respuesta.isError() && respuesta.getDocumento()!=null){
			alterMutuaForm.setRutaFichero(this.getRutaPDF(respuesta.getDocumento(),alterMutuaForm.getIdentificador(), usr.getLocation()));
		}else{
			alterMutuaForm.setRutaFichero("");
		}
		
		// Por ultimo actualizamos el error y mensaje
		alterMutuaForm.setError(respuesta.isError());
		alterMutuaForm.setMsgRespuesta(respuesta.getMensaje());
		
		return alterMutuaForm;
	}

	public AlterMutuaForm getEstadoSolicitud(AlterMutuaForm alterMutuaForm, UsrBean usr)
			throws Exception {
		// Convertimos los datos de entrada a datos que entienda el WS
		int idSolicitud = Integer.parseInt(alterMutuaForm.getIdSolicitudalter());
		boolean certificado = alterMutuaForm.quiereCertificado();
		
		// Realizamos la llamada al WS
		AlterMutuaWSClient wsClient = new AlterMutuaWSClient(usr);
		WSRespuesta respuesta = wsClient.getEstadoSolicitud(idSolicitud, true);
		
				// Si no devuelve error, y devuelve algo coherente, lo metemos en el formulario
		if(!respuesta.isError() && respuesta.getDocumento()!=null){
			alterMutuaForm.setRutaFichero(this.getRutaPDF(respuesta.getDocumento(),alterMutuaForm.getIdentificador(), usr.getLocation()));
		}else{
			alterMutuaForm.setRutaFichero("");
		}
		// Por ultimo actualizamos el error y mensaje
		alterMutuaForm.setError(respuesta.isError());
		alterMutuaForm.setMsgRespuesta(respuesta.getMensaje());
		
		return alterMutuaForm;
	}

	public AlterMutuaForm setSolicitudAlta(AlterMutuaForm alterMutuaForm, UsrBean usr)
			throws Exception {
		
		WSSolicitud solicitud = new WSSolicitud(); 
		AlterMutuaWSClient wsClient = new AlterMutuaWSClient(usr);
		ArrayList paquetes = alterMutuaForm.getPaquetesSeleccionados();
		WSRespuesta respuesta = new WSRespuesta();
		
		CenSolicitudAlterMutuaBean amBean = new CenSolicitudAlterMutuaBean();  
		CenSolicitudAlterMutuaAdm amAdm = new CenSolicitudAlterMutuaAdm(usr);  
		
		for (int i = 0; i < paquetes.size(); i++) {
			alterMutuaForm.setIdPaquete(paquetes.get(i).toString());
			solicitud = this.getSolicitudForm(solicitud, alterMutuaForm);
			
			respuesta = wsClient.realizarSolicitudAlter(solicitud);
			
			if(!respuesta.isError()){
				alterMutuaForm.setIdSolicitudAlter(respuesta.getIdentificador().toString());
				amBean = alterMutuaForm.getSolicitudAlterMutuaBean();
				amAdm.insert(amBean);
			}
			
			// Actualizamos el error y mensaje
			alterMutuaForm.setIdSolicitudAlter(respuesta.getIdentificador().toString());
			alterMutuaForm.setError(respuesta.isError());
			alterMutuaForm.setMsgRespuesta(respuesta.getMensaje());
		}
		
		return alterMutuaForm;
	}
	
	public AlterMutuaForm getTarifaSolicitud(AlterMutuaForm alterMutuaForm, UsrBean usr)
			throws Exception {
		
		// Me creo la solicitud con los datos del formulario
		WSSolicitud solicitud = new WSSolicitud(); 
		solicitud = this.getSolicitudForm(solicitud, alterMutuaForm);
		
		AlterMutuaWSClient wsClient = new AlterMutuaWSClient(usr);
		WSRespuesta respuesta = wsClient.getTarifaSolicitud(solicitud);
		// Por ultimo actualizamos el error y mensaje
		alterMutuaForm.setError(respuesta.isError());
		alterMutuaForm.setMsgRespuesta(respuesta.getMensaje());
		if(!respuesta.isError()){
			WSPropuesta propuesta = respuesta.getPropuestas()[0];
			alterMutuaForm.setBrevePropuesta(propuesta.getBreve());
			alterMutuaForm.setTarifaPropuesta(propuesta.getTarifa().toString());
		}
		
		return alterMutuaForm;
	}
	
	private WSSolicitud getSolicitudForm(WSSolicitud solicitud, AlterMutuaForm form) {
		
		// Creamos el registro de direccion
		WSDireccion direccion = new WSDireccion();
		direccion.setCodigoPostal(form.getCodigoPostal());
		direccion.setDireccionPostal(form.getDomicilio());
		direccion.setTelefono1(form.getTelefono1());
		direccion.setTelefono2(form.getTelefono2());
		direccion.setMovil(form.getMovil());
		direccion.setPoblacion(form.getPoblacion());
		direccion.setEmail(form.getCorreoElectronico());
		direccion.setFax(form.getFax());
		direccion.setProvincia(form.getIdProvincia());
		direccion.setPais(form.getIdPais());
		
		WSCuentaBancaria cuenta = new WSCuentaBancaria();
		cuenta.setEntidad(form.getCboCodigo());
		cuenta.setSucursal(form.getCodigoSucursal());
		cuenta.setDC(form.getDigitoControl());
		cuenta.setCuenta(form.getNumeroCuenta());
		cuenta.setIBAN(form.getIban());
		cuenta.setSWIFT(form.getSwift());
		cuenta.setPais(form.getIdPaisCuenta());

		// Creamos el registro de asegurado
		WSAsegurado asegurado = new WSAsegurado();
		asegurado.setNombre(form.getNombre());
		asegurado.setApellidos(form.getApellidos());
		asegurado.setIdentificador(form.getIdentificador());
		asegurado.setPublicidad(form.getAdmitePublicidad());
		asegurado.setTipoIdentificador(AlterMutuaHelper.getTipoIdentificacionAM(form.getIdTipoIdentificacion()));
		asegurado.setSexo(AlterMutuaHelper.getSexoAM(form.getSexo()));
		asegurado.setFechaNacimiento(UtilidadesFecha.stringToCalendar(form.getFechaNacimiento()));
		//asegurado.setEstadoCivil(AlterMutuaHelper.getEstadoCivilAM(form.getIdEstadoCivil()));
		asegurado.setEstadoCivil(Integer.parseInt(form.getIdEstadoCivil()));
		asegurado.setIdioma(Integer.valueOf(form.getIdioma()));
		asegurado.setTipoEjercicio(AlterMutuaHelper.getTipoEjercicioAM(form.getTipoEjercicio()));
		asegurado.setColegio(form.getCodigoInstitucion());
		asegurado.setTipoComunicacion(Integer.valueOf(form.getTipoComunicacion()));
		
		// Añadimos la direccion al asegurado
		asegurado.setDireccion(direccion);
		asegurado.setCuentaBancaria(cuenta);
		
		asegurado.setFamiliares(this.getPersonas(form.getFamiliares()));
		if(form.getSelectBeneficiarios()!=null){
			if(form.getSelectBeneficiarios().equalsIgnoreCase("3")){
				solicitud.setHerederos(this.getPersonas(form.getHerederos()));
			}else if(form.getSelectBeneficiarios().equalsIgnoreCase("2")){
				solicitud.setHerederos(this.getPersonas(form.getFamiliares()));
			}else{
				solicitud.setHerederos(null);
			}
		}
		solicitud.setAsegurado(asegurado);
		solicitud.setIdPaquete(Integer.parseInt(form.getIdPaquete()));
		solicitud.setObservaciones(form.getObservaciones());
		
		return solicitud;
	}
	
	
	private WSPersona[] getPersonas(String personas){
		
		StringTokenizer st = new StringTokenizer(personas,"%%%");
		
		WSPersona p = new WSPersona();
		WSPersona[] listaPersonas = new WSPersona[st.countTokens()];
		//Parentesco, Sexo, TipoIdentificador, Nombre, Apellidos, Identificador, Fnacimiento
		int i=0;
	     while (st.hasMoreTokens()) {
	         StringTokenizer stp = new StringTokenizer(st.nextToken(),"&&");
	         p.setParentesco(Integer.parseInt(stp.nextToken()));
	         p.setSexo( Integer.parseInt(stp.nextToken()));
	         p.setTipoIdentificador(Integer.parseInt(stp.nextToken()));
	         p.setNombre(stp.nextToken());
	         p.setApellidos(stp.nextToken());
	         p.setIdentificador(stp.nextToken());
	         if(p.getIdentificador().equalsIgnoreCase(" ")){
	        	 p.setIdentificador(null);
	        	 p.setTipoIdentificador(-1);
	         }
	         p.setFechaNacimiento(UtilidadesFecha.stringToCalendar(stp.nextToken()));
	         listaPersonas[i]=p;
	         i++;
	     }
		return listaPersonas;
	}


	// Guarda el PDF en una ruta local y devuelve su ruta
	private String getRutaPDF(byte[] pdf, String NIF, String institucion) throws IOException{
	    ReadProperties rp= new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
	    StringBuffer rutaPDF = new StringBuffer();
	    rutaPDF.append("");
	    if(pdf!=null && pdf.length>0){
		    try {
			    rutaPDF.append( rp.returnProperty("alterMutua.directorioFicheros") );
			    rutaPDF.append( rp.returnProperty("alterMutua.directorioLog") );
			    rutaPDF.append( "/" + institucion );
			    //File folder = new File(rutaPDF.toString());
   	   			File fDirectorio = new File(rutaPDF.toString());
   	   			if(!fDirectorio.exists())
   	   				fDirectorio.mkdirs();
			    rutaPDF.append( "/" +NIF + "_" +  UtilidadesString.getTimeStamp() + ".pdf" );
				FileOutputStream fos;
				File ficheroTemp = new File(rutaPDF.toString()); 
				fos = new FileOutputStream(ficheroTemp);
				fos.write(pdf);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		return rutaPDF.toString();
	}

	public AlterMutuaForm getDatosSolicitud(AlterMutuaForm form,UsrBean usr) throws Exception {
		CenSolicitudIncorporacionAdm solIncAdm = new CenSolicitudIncorporacionAdm(usr);
		CenSolicitudAlterMutuaAdm solAlterAdm = new CenSolicitudAlterMutuaAdm(usr);
		GenCatalogosWSAdm catAdm = new GenCatalogosWSAdm(usr); 
		CenInstitucionAdm instAdm = new CenInstitucionAdm(usr); 
		CenPersonaAdm perAdm = new CenPersonaAdm(usr); 
		
		Hashtable hash = new Hashtable();
		if(form.getIdSolicitud()!=null){
			hash.put(CenSolicitudAlterMutuaBean.C_IDSOLICITUD, form.getIdSolicitud());
			hash.put(CenSolicitudAlterMutuaBean.C_PROPUESTA, form.getPropuesta());
			Vector v = solAlterAdm.select(hash);
			if(v.size()>0){
				//Rellenamos el formulario con los datos de la bbdd
				CenSolicitudAlterMutuaBean bean = (CenSolicitudAlterMutuaBean)v.get(0);
				form.setIdInstitucion(bean.getIdInstitucion().toString());
				form.setColegio(instAdm.getAbreviaturaInstitucion(bean.getIdInstitucion().toString()));
				form.setIdentificador(bean.getIdentificador());
				form.setIdTipoIdentificacion(bean.getIdTipoIdentificacion());
				form.setIdentificador(bean.getIdentificador());
				form.setIdSexo(bean.getIdSexo()); 
				form.setNombre(bean.getNombre());
				form.setApellidos(bean.getApellidos());
				form.setFechaNacimiento(UtilidadesString.formatoFecha( bean.getFechaNacimiento(), ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH));
				//form.setIdEstadoCivil(bean.getIdEstadoCivil()!=null?bean.getIdEstadoCivil().toString():"");
				if(bean.getIdEstadoCivil()!=null){
					form.setIdEstadoCivil(String.valueOf(AlterMutuaHelper.getEstadoCivilAM(String.valueOf(bean.getIdEstadoCivil()))));
					form.setEstadoCivil(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_ESTADOCIVIL, form.getIdEstadoCivil()));
				}else{
					form.setIdEstadoCivil("");
					form.setEstadoCivil("");
				}
				form.setIdProvincia(bean.getIdProvincia());
				form.setPoblacion(bean.getPoblacion());
				form.setDomicilio(bean.getDomicilio());
				form.setCodigoPostal(bean.getCodigoPostal());
				form.setTelefono1(bean.getTelefono1());
				form.setTelefono2(bean.getTelefono2());
				form.setMovil(bean.getMovil());
				form.setFax(bean.getFax());
				form.setCorreoElectronico(bean.getCorreoElectronico());
				form.setCodigoSucursal(bean.getCodigoSucursal()); 
				form.setDigitoControl(bean.getDigitoControl());
				form.setNumeroCuenta(bean.getNumeroCuenta());
				form.setCboCodigo(bean.getCboCodigo());
				form.setIdPais(bean.getIdPais());
				form.setIdPaisCuenta(bean.getPais());
				form.setCodigoInstitucion(catAdm.getCodigoExtInstitucion(usr.getLocation()));
				form.setIdTipoEjercicio(bean.getIdTipoEjercicio());
				form.setTipoEjercicio(catAdm.getValorIdExt(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_TIPOEJERCICIO, form.getIdTipoEjercicio()));
				form.setTipoIdentificacion(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_TIPOIDENTIFICADOR, bean.getIdTipoIdentificacion().toString()));
				form.setSexo(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_SEXO, bean.getIdSexo()));
				
				form.setIdSolicitudAlter(bean.getIdSolicitudAlter());
			}else{
			
				hash = new Hashtable();
				hash.put(CenSolicitudIncorporacionBean.C_IDSOLICITUD, form.getIdSolicitud());
				v = solIncAdm.selectByPK(hash);
				
				if(v.size()==1){
					
					CenSolicitudIncorporacionBean bean = (CenSolicitudIncorporacionBean)v.get(0);
					form.setIdInstitucion(bean.getIdInstitucion().toString());
					form.setColegio(instAdm.getAbreviaturaInstitucion(bean.getIdInstitucion().toString()));
					form.setIdentificador(bean.getNumeroIdentificador());
					form.setIdTipoIdentificacion(bean.getIdTipoIdentificacion().toString());
					form.setIdentificador(bean.getNumeroIdentificador());
					form.setSexo(bean.getSexo()); 
					form.setIdSexo(bean.getSexo()); 
					form.setNombre(bean.getNombre());
					form.setApellidos(bean.getApellido1()+" "+bean.getApellido2());
					form.setFechaNacimiento(UtilidadesString.formatoFecha( bean.getFechaNacimiento(), ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH));
					//form.setIdEstadoCivil(bean.getIdEstadoCivil()!=null?bean.getIdEstadoCivil().toString():"");
					if(bean.getIdEstadoCivil()!=null){
						form.setIdEstadoCivil(String.valueOf(AlterMutuaHelper.getEstadoCivilAM(String.valueOf(bean.getIdEstadoCivil()))));
						form.setEstadoCivil(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_ESTADOCIVIL, form.getIdEstadoCivil()));
					}else{
						form.setIdEstadoCivil("");
						form.setEstadoCivil("");
					}
					form.setIdProvincia(bean.getIdProvincia());
					form.setIdPoblacion(bean.getIdPoblacion());
					form.setDomicilio(bean.getDomicilio());
					form.setCodigoPostal(bean.getCodigoPostal());
					form.setTelefono1(bean.getTelefono1());
					form.setTelefono2(bean.getTelefono2());
					form.setMovil(bean.getMovil());
					form.setFax(bean.getFax1());
					form.setCorreoElectronico(bean.getCorreoElectronico());
					form.setCodigoSucursal(bean.getCodigoSucursal()); 
					form.setDigitoControl(bean.getDigitoControl());
					form.setNumeroCuenta(bean.getNumeroCuenta());
					form.setCboCodigo(bean.getCbo_Codigo());
					if(bean.getIdPais()!=null)
						form.setIdPais(catAdm.getIdExternoPais(bean.getIdPais()));
					form.setCodigoInstitucion(catAdm.getCodigoExtInstitucion(usr.getLocation()));
					form.setIdTipoEjercicio(String.valueOf(AlterMutuaHelper.getTipoEjercicioAM(String.valueOf(bean.getIdTipoColegiacion()))));
					form.setTipoEjercicio(catAdm.getValorIdExt(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_TIPOEJERCICIO, form.getIdTipoEjercicio()));
					form.setTipoIdentificacion(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_TIPOIDENTIFICADOR, bean.getIdTipoIdentificacion().toString()));
					form.setSexo(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_SEXO, bean.getSexo()));
					form.setPoblacion(bean.getIdPoblacion().equalsIgnoreCase("")?bean.getPoblacionExtranjera():catAdm.getNombrePoblacion(bean.getIdPoblacion()));
				}
			}
		}else{
			Vector vSol = solAlterAdm.getSolicitudes(form.getIdPersona(), String.valueOf(form.getPropuesta()));
			if(vSol.size()>0){
				//Rellenamos el formulario con los datos de la bbdd
				CenSolicitudAlterMutuaBean bean = (CenSolicitudAlterMutuaBean )vSol.get(0);
				form.setIdInstitucion(bean.getIdInstitucion().toString());
				form.setColegio(instAdm.getAbreviaturaInstitucion(bean.getIdInstitucion().toString()));
				form.setIdentificador(bean.getIdentificador());
				form.setIdTipoIdentificacion(bean.getIdTipoIdentificacion());
				form.setIdentificador(bean.getIdentificador());
				form.setIdSexo(bean.getIdSexo()); 
				form.setNombre(bean.getNombre());
				form.setApellidos(bean.getApellidos());
				form.setFechaNacimiento(UtilidadesString.formatoFecha( bean.getFechaNacimiento(), ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH));
				//form.setIdEstadoCivil(bean.getIdEstadoCivil()!=null?bean.getIdEstadoCivil().toString():"");
				if(bean.getIdEstadoCivil()!=null){
					form.setIdEstadoCivil(String.valueOf(AlterMutuaHelper.getEstadoCivilAM(String.valueOf(bean.getIdEstadoCivil()))));
					form.setEstadoCivil(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_ESTADOCIVIL, form.getIdEstadoCivil()));
				}else{
					form.setIdEstadoCivil("");
					form.setEstadoCivil("");
				}
				form.setIdProvincia(bean.getIdProvincia());
				form.setPoblacion(bean.getPoblacion());
				form.setDomicilio(bean.getDomicilio());
				form.setCodigoPostal(bean.getCodigoPostal());
				form.setTelefono1(bean.getTelefono1());
				form.setTelefono2(bean.getTelefono2());
				form.setMovil(bean.getMovil());
				form.setFax(bean.getFax());
				form.setCorreoElectronico(bean.getCorreoElectronico());
				form.setCodigoSucursal(bean.getCodigoSucursal()); 
				form.setDigitoControl(bean.getDigitoControl());
				form.setNumeroCuenta(bean.getNumeroCuenta());
				form.setCboCodigo(bean.getCboCodigo());
				form.setIdPais(bean.getIdPais());
				form.setIdPaisCuenta(bean.getPais());
				form.setCodigoInstitucion(catAdm.getCodigoExtInstitucion(usr.getLocation()));
				form.setIdTipoEjercicio(bean.getIdTipoEjercicio());
				form.setTipoEjercicio(catAdm.getValorIdExt(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_TIPOEJERCICIO, form.getIdTipoEjercicio()));
				form.setTipoIdentificacion(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_TIPOIDENTIFICADOR, bean.getIdTipoIdentificacion().toString()));
				form.setSexo(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_SEXO, bean.getIdSexo()));
				
				form.setIdSolicitudAlter(bean.getIdSolicitudAlter());
			}else{
				Long idPersona = Long.parseLong(form.getIdPersona());
				int idInstitucion = Integer.parseInt(usr.getLocation());
				
				CenClienteAdm cliAdm = new CenClienteAdm(usr);
				Vector v = cliAdm.getDatosPersonales(idPersona, idInstitucion);
				
				CenDireccionesAdm dirAdm = new CenDireccionesAdm(usr);
				CenDireccionesBean dirBean = dirAdm.obtenerDireccionTipo(String.valueOf(idPersona), String.valueOf(idInstitucion), Integer.toString(ClsConstants.TIPO_DIRECCION_CENSOWEB));
				
				CenCuentasBancariasAdm cuentaAdm = new CenCuentasBancariasAdm(usr);
				ArrayList<CenCuentasBancariasBean> cuentasArr = cuentaAdm.getCuentasCargo(idPersona, idInstitucion);
				
				
				if (v.size()>0){
					Hashtable per = (Hashtable)v.get(0);
					per.size();
					form.setApellidos(UtilidadesHash.getString(per,"APELLIDOS1") +" "+ UtilidadesHash.getString(per,"APELLIDOS2"));
					form.setNombre(UtilidadesHash.getString(per,"NOMBRE"));
					form.setIdSexo(UtilidadesHash.getString(per,"SEXO"));
					form.setSexo(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_SEXO, form.getIdSexo()));
					form.setIdEstadoCivil(UtilidadesHash.getString(per,"IDESTADOCIVIL"));
					form.setIdTipoIdentificacion(UtilidadesHash.getString(per,"IDTIPOIDENTIFICACION"));
					form.setIdentificador(UtilidadesHash.getString(per,"NIFCIF"));
					form.setTipoIdentificacion(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_TIPOIDENTIFICADOR, form.getIdTipoIdentificacion().toString()));
					form.setColegio(instAdm.getAbreviaturaInstitucion(usr.getLocation()));
					form.setIdInstitucion(usr.getLocation());
					form.setFechaNacimiento(
						UtilidadesString.formatoFecha(
							UtilidadesHash.getString(per,"FECHANACIMIENTO"),
							ClsConstants.DATE_FORMAT_JAVA, 
							ClsConstants.DATE_FORMAT_SHORT_SPANISH)
					);
					
					if(dirBean!=null) {
						CenPoblacionesAdm pobAdm = new CenPoblacionesAdm(usr);
						CenPaisAdm paisAdm = new CenPaisAdm(usr);
						form.setCodigoPostal(dirBean.getCodigoPostal());
						if(dirBean.getIdPais()!=null)
							form.setIdPais(paisAdm.getCodigoExt( dirBean.getIdPais() ));
						form.setIdProvincia(dirBean.getIdProvincia());
						form.setDomicilio(dirBean.getDomicilio());
						form.setTelefono1(dirBean.getTelefono1());
						form.setFax(dirBean.getFax1());
						form.setMovil(dirBean.getMovil());
						if(dirBean.getPoblacionExtranjera()!=null&&!dirBean.getPoblacionExtranjera().equalsIgnoreCase(""))
							form.setPoblacion(dirBean.getPoblacionExtranjera());
						else
							form.setPoblacion(pobAdm.getDescripcion(dirBean.getIdPoblacion()));
						form.setCorreoElectronico(dirBean.getCorreoElectronico());
					}
					//form.setIdSolicitudIncorporacion(idPersona);
					
					if(!cuentasArr.isEmpty()){
						CenCuentasBancariasBean cuentaBean = cuentasArr.get(cuentasArr.size()-1);
						form.setCboCodigo(cuentaBean.getCbo_Codigo());
						form.setCodigoSucursal(cuentaBean.getCodigoSucursal());
						form.setDigitoControl(cuentaBean.getDigitoControl());
						form.setNumeroCuenta(cuentaBean.getNumeroCuenta());
					}
					
					String estadoCivil = perAdm.getEstadoCivil(idPersona);
					if(estadoCivil!=null && !estadoCivil.equalsIgnoreCase("")){
						form.setIdEstadoCivil(String.valueOf(AlterMutuaHelper.getEstadoCivilAM(estadoCivil)));
						form.setEstadoCivil(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_ESTADOCIVIL, form.getIdEstadoCivil()));
					}else{
						form.setIdEstadoCivil("");
						form.setEstadoCivil("");
					}
					
					CenEstadoColegialAdm colAdm = new CenEstadoColegialAdm(usr);
					Vector col = colAdm.getEstadoColegial(String.valueOf(idInstitucion), String.valueOf(idPersona), "1");
					String tipoEjercicio = "";//col.get(0);
					if(col.size()>0){
						Hashtable h = (Hashtable)col.get(0);
						tipoEjercicio = h.get("IDESTADO").toString();
					}
					form.setIdTipoEjercicio(String.valueOf(AlterMutuaHelper.getTipoEjercicioAM(tipoEjercicio)));
					form.setTipoEjercicio(catAdm.getValorIdExt(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_TIPOEJERCICIO, form.getIdTipoEjercicio()));
				}
			}
		}
		return form;
	}
}
