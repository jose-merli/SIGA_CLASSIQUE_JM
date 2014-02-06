package com.siga.censo.service.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesNumero;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenClienteAdm;
import com.siga.beans.CenCuentasBancariasAdm;
import com.siga.beans.CenCuentasBancariasBean;
import com.siga.beans.CenDireccionesAdm;
import com.siga.beans.CenDireccionesBean;
import com.siga.beans.CenInfluenciaAdm;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.CenSolicitudIncorporacionAdm;
import com.siga.beans.CenSolicitudIncorporacionBean;
import com.siga.beans.CenSolicitudMutualidadAdm;
import com.siga.beans.CenSolicitudMutualidadBean;
import com.siga.beans.CenTratamientoAdm;
import com.siga.beans.GenCatalogosWSAdm;
import com.siga.censo.form.MutualidadForm;
import com.siga.censo.service.MutualidadService;
import com.siga.comun.vos.ValueKeyVO;
import com.siga.general.SIGAException;
import com.siga.ws.alterMutua.AlterMutuaHelper;
import com.siga.ws.mutualidad.MutualidadWSClient;
import com.siga.ws.mutualidad.RespuestaMutualidad;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.JtaBusinessServiceTemplate;

public class AtosMutualidadService extends JtaBusinessServiceTemplate 
	implements MutualidadService {

	public AtosMutualidadService(BusinessManager businessManager) {
		super(businessManager);
	}
	
	public RespuestaMutualidad insertarSolicitudMutualidad(MutualidadForm mutualidadForm, UsrBean usrBean)throws Exception{
		CenSolicitudMutualidadBean solicitudMutualidadBean = mutualidadForm.getMutualidadBean();
		CenSolicitudMutualidadAdm solicitudMutualidadAdm = new CenSolicitudMutualidadAdm(usrBean);
		solicitudMutualidadBean.setIdEstado(CenSolicitudMutualidadBean.ESTADO_INICIAL);
		solicitudMutualidadBean.setEstado(CenSolicitudMutualidadBean.ESTADO_PTERESPUESTA);
		solicitudMutualidadBean.setIdSolicitud(solicitudMutualidadAdm.getNewIdSolicitud());
		if(mutualidadForm.getIdSolicitudIncorporacion()==null){
			mutualidadForm.setIdSolicitudIncorporacion(mutualidadForm.getIdPersona());
		}
		mutualidadForm.setIdSolicitud(solicitudMutualidadBean.getIdSolicitud().toString());
		solicitudMutualidadBean.setFechaEstado("SYSDATE");
		solicitudMutualidadBean.setFechaSolicitud("SYSDATE");
		solicitudMutualidadBean.setIdInstitucion(new Integer(usrBean.getLocation()));
		solicitudMutualidadBean.setIdSolicitudAceptada(null);
		solicitudMutualidadAdm.insert(solicitudMutualidadBean);
		RespuestaMutualidad respuestaSolicitud = altaMutualidad(mutualidadForm,usrBean);
		if(mutualidadForm.getIdSolicitudAceptada()!=null && !mutualidadForm.getIdSolicitudAceptada().equals("") && !mutualidadForm.getIdSolicitudAceptada().equals("0")){
			solicitudMutualidadBean.setIdSolicitudAceptada(new Long(mutualidadForm.getIdSolicitudAceptada()));
			
			solicitudMutualidadBean.setEstado(CenSolicitudMutualidadBean.ESTADO_PTERESPUESTA);
			solicitudMutualidadBean.setIdEstado(CenSolicitudMutualidadBean.ESTADO_SOLICITADO);
			if(mutualidadForm.getIdTipoSolicitud().equals(CenSolicitudMutualidadBean.TIPOSOLICITUD_PLANPROFESIONAL));
				solicitudMutualidadBean.setEstadoMutualista(CenSolicitudMutualidadBean.ESTADO_PTERESPUESTA);
			mutualidadForm = solicitudMutualidadBean.getMutualidadForm(mutualidadForm);
			solicitudMutualidadAdm.actualizaSolicitudAceptada(solicitudMutualidadBean);
			if(mutualidadForm.getIdTipoSolicitud().equals(CenSolicitudMutualidadBean.TIPOSOLICITUD_PLANPROFESIONAL))
				solicitudMutualidadAdm.actualizaEstadoMutualista(solicitudMutualidadBean);
			
			// Aqui tenemos que darlo de alta tambien en el seguro gratuito si la solicitud inicial es de plan profesional
			if(mutualidadForm.getIdTipoSolicitud().equals(CenSolicitudMutualidadBean.TIPOSOLICITUD_PLANPROFESIONAL)){
				mutualidadForm.setIdTipoSolicitud(CenSolicitudMutualidadBean.TIPOSOLICITUD_SEGUROUNIVERSAL);
				insertarSolicitudMutualidad(mutualidadForm, usrBean);
			}
		}
		return respuestaSolicitud;
	}
	private RespuestaMutualidad altaMutualidad(MutualidadForm mutualidadForm, UsrBean usrBean) throws Exception{
		MutualidadWSClient mutualidadWSClient =  new MutualidadWSClient(usrBean);
		RespuestaMutualidad respuestaAlta = null; 
		if(mutualidadForm.getIdTipoSolicitud().equals(CenSolicitudMutualidadBean.TIPOSOLICITUD_PLANPROFESIONAL))
			respuestaAlta = mutualidadWSClient.altaPlanProfesional(getFormularioAlta(mutualidadForm));
		else if(mutualidadForm.getIdTipoSolicitud().equals(CenSolicitudMutualidadBean.TIPOSOLICITUD_SEGUROUNIVERSAL))
			respuestaAlta = mutualidadWSClient.altaAccidentesUniversal(getFormularioAlta(mutualidadForm));
		else 
			throw new SIGAException("El tipo de solicitud no esta implementado");
		if(respuestaAlta.isCorrecto())
			mutualidadForm.setIdSolicitudAceptada(respuestaAlta.getIdSolicitud().toString());
		return respuestaAlta;
		
	}

	/**
	 * Actualiza el estado de una solicitud. 
	 * Se usa tanto para el plan profesional como el seguro gratuito, ya que se basa unicamente en el idSolicitud
	 * @param mutualidadForm - Requiere tener dentro el mutualidadBean con el idSolicitudAceptada relleno
	 * @param usrBean - El usuario que hace la solicitud de estado
	 * @throws Exception
	 */
	private void actualizaEstado(MutualidadForm mutualidadForm, UsrBean usrBean)throws Exception{
		CenSolicitudMutualidadBean solicitudMutualidadBean = mutualidadForm.getMutualidadBean();
		
		// Realizamos la llamada al WS de la mutualidad para obtener el estado de la solicitud
		MutualidadWSClient mutualidadWSClient =  new MutualidadWSClient(usrBean);
		RespuestaMutualidad respuestaSolicitud = mutualidadWSClient.getEstadoSolicitud(solicitudMutualidadBean.getIdSolicitudAceptada());
		
		// Seteamos el estado que nos haya devuelto la llamada
		solicitudMutualidadBean.setEstado(respuestaSolicitud.getValorRespuesta());
		solicitudMutualidadBean.setIdEstado(CenSolicitudMutualidadBean.ESTADO_SOLICITADO);
		solicitudMutualidadBean.setPDF(respuestaSolicitud.getRutaPDF());
		
		// Actualizamos la solicitud en base de datos
		CenSolicitudMutualidadAdm solicitudMutualidadAdm = new CenSolicitudMutualidadAdm(usrBean);
		solicitudMutualidadAdm.actualizaSolicitudAceptada(solicitudMutualidadBean);
		
		// Por último actualizamos el formulario
		mutualidadForm = solicitudMutualidadBean.getMutualidadForm(mutualidadForm);
	}
	
	public void actualizaEstadoSolicitud(MutualidadForm mutualidadForm, UsrBean usrBean)throws Exception{
		if(mutualidadForm.getIdSolicitudAceptada()!=null && !mutualidadForm.getIdSolicitudAceptada().equals("") && !mutualidadForm.getIdSolicitudAceptada().equals("0")){
			actualizaEstado(mutualidadForm,usrBean);
		}else{
			CenSolicitudMutualidadAdm solicitudMutualidadAdm = new CenSolicitudMutualidadAdm(usrBean);
			CenSolicitudMutualidadBean solicitudMutualidadBean = solicitudMutualidadAdm.getSolicitudMutualidad(mutualidadForm.getIdSolicitud());
			
			altaMutualidad(solicitudMutualidadBean.getMutualidadForm(mutualidadForm),usrBean);
			actualizaEstado(mutualidadForm,usrBean);
		}
			
		
	}
	public void actualizaEstadoMutualista(MutualidadForm mutualidadForm, UsrBean usrBean)throws Exception{
		CenSolicitudMutualidadBean solicitudMutualidadBean = mutualidadForm.getMutualidadBean();
		MutualidadWSClient mutualidadWSClient =  new MutualidadWSClient(usrBean);
		RespuestaMutualidad respuestaSolicitud = mutualidadWSClient.getEstadoMutualista(solicitudMutualidadBean.getNumeroIdentificacion(), solicitudMutualidadBean.getFechaNacimiento());
		solicitudMutualidadBean.setEstadoMutualista(respuestaSolicitud.getValorRespuesta());
		solicitudMutualidadBean.setPDF(respuestaSolicitud.getRutaPDF());
		CenSolicitudMutualidadAdm solicitudMutualidadAdm = new CenSolicitudMutualidadAdm(usrBean);
		solicitudMutualidadAdm.actualizaEstadoMutualista(solicitudMutualidadBean);
		mutualidadForm = solicitudMutualidadBean.getMutualidadForm(mutualidadForm);
		
	}
	
	
	
	private Hashtable getDatosBancarios(MutualidadForm mutualidadForm){
		Hashtable datosBancarios = new Hashtable();
		datosBancarios.put("iban", mutualidadForm.getIban());
		datosBancarios.put("swift", mutualidadForm.getSwift());
		
		return datosBancarios;
	}
	private Hashtable getDatosPoliza(MutualidadForm mutualidadForm){
		Hashtable datosPoliza = new Hashtable();
		datosPoliza.put("idPeriodicidadPago", mutualidadForm.getIdPeriodicidadPago());
		datosPoliza.put("idCobertura", mutualidadForm.getIdCobertura());
		if(mutualidadForm.getOtrosBeneficiarios()!=null)
			datosPoliza.put("otrosBeneficiarios", mutualidadForm.getOtrosBeneficiarios());
		else
			datosPoliza.put("otrosBeneficiarios", "");
		
		
		return datosPoliza;
	}
	private Hashtable getDatosDireccionDomicilio(MutualidadForm mutualidadForm){
		Hashtable datosDireccionDomicilio = new Hashtable();
		
		datosDireccionDomicilio.put("codigoPostal", mutualidadForm.getCodigoPostal());
		datosDireccionDomicilio.put("domicilio", mutualidadForm.getDomicilio());
		datosDireccionDomicilio.put("correoElectronico", mutualidadForm.getCorreoElectronico());
		datosDireccionDomicilio.put("telef1", mutualidadForm.getTelef1());
		datosDireccionDomicilio.put("movil", mutualidadForm.getMovil());
		datosDireccionDomicilio.put("pais", mutualidadForm.getPais());
		datosDireccionDomicilio.put("poblacion", mutualidadForm.getPoblacion());
		if(mutualidadForm.getPoblacionExtranjera()!=null && !mutualidadForm.getPoblacionExtranjera().equalsIgnoreCase("")){
			datosDireccionDomicilio.put("poblacion", mutualidadForm.getPoblacionExtranjera());
		}
		datosDireccionDomicilio.put("provincia", mutualidadForm.getProvincia());

		
		return datosDireccionDomicilio;
	}
	private Hashtable getDatosSolicitudEstados(MutualidadForm mutualidadForm){
		Hashtable datosSolicitudEstados = new Hashtable();
		datosSolicitudEstados.put("numeroIdentificacion", mutualidadForm.getNumeroIdentificacion());
		return datosSolicitudEstados;
	}
	
	private Hashtable getDatosBeneficiarios(MutualidadForm mutualidadForm){
		
		Hashtable datosBeneficiarios = new Hashtable();
		datosBeneficiarios.put("idBeneficiario", mutualidadForm.getIdBeneficiario());
		if(mutualidadForm.getOtrosBeneficiarios()!=null)
			datosBeneficiarios.put("otrosBeneficiarios", mutualidadForm.getOtrosBeneficiarios());
		else
			datosBeneficiarios.put("otrosBeneficiarios", "");
		
		return datosBeneficiarios;
	}
	private Hashtable getDatosPersona(MutualidadForm mutualidadForm){
		Hashtable datosPersona = new Hashtable();
		datosPersona.put("apellido1", mutualidadForm.getApellido1());
		datosPersona.put("apellido2", mutualidadForm.getApellido2());
		datosPersona.put("numeroIdentificacion", mutualidadForm.getNumeroIdentificacion());
		datosPersona.put("nacionalidad", mutualidadForm.getNacionalidad());
		datosPersona.put("nombre", mutualidadForm.getNombre());
		datosPersona.put("idSexo", mutualidadForm.getIdSexo());
		datosPersona.put("numHijos", mutualidadForm.getNumeroHijos()==null?"0":mutualidadForm.getNumeroHijos());
		datosPersona.put("numColegiado", "");
		String edadeshijos=mutualidadForm.getEdadHijo1()+","+
				mutualidadForm.getEdadHijo2()+","+
				mutualidadForm.getEdadHijo3()+","+
				mutualidadForm.getEdadHijo4()+","+
				mutualidadForm.getEdadHijo5()+","+ 
				mutualidadForm.getEdadHijo6()+","+
				mutualidadForm.getEdadHijo7()+","+
				mutualidadForm.getEdadHijo8()+","+
				mutualidadForm.getEdadHijo9()+","+
				mutualidadForm.getEdadHijo10();
		datosPersona.put("edadesHijos", edadeshijos);
		//No pasamos el colegio
		datosPersona.put("colegio", mutualidadForm.getColegio());
		//en ejerciente Pasamos el id del tipo de solicitud de incorporacion. hay dudas pasamos 0
		datosPersona.put("ejerciente", "");
		datosPersona.put("estadoCivil", mutualidadForm.getIdEstadoCivil());
		datosPersona.put("fechaNacimiento", mutualidadForm.getFechaNacimiento());
		datosPersona.put("fechaNacimientoConyuge", mutualidadForm.getFechaNacimientoConyuge());
		datosPersona.put("asistenciaSanitaria", mutualidadForm.getIdAsistenciaSanitaria());
		
		return datosPersona;
	}
	
	private Hashtable getFormularioAlta(MutualidadForm mutualidadForm){
		Hashtable mutualidadFormHashtable = new Hashtable();
		mutualidadFormHashtable.put("datosBancarios", getDatosBancarios(mutualidadForm));
		mutualidadFormHashtable.put("datosPoliza", getDatosPoliza(mutualidadForm));
		mutualidadFormHashtable.put("datosDireccionDomicilio", getDatosDireccionDomicilio(mutualidadForm) );
		mutualidadFormHashtable.put("datosDireccionDespacho", mutualidadFormHashtable.get("datosDireccionDomicilio"));
		mutualidadFormHashtable.put("datosPersona", getDatosPersona(mutualidadForm));
		mutualidadFormHashtable.put("datosBeneficiarios", getDatosBeneficiarios(mutualidadForm));
		mutualidadFormHashtable.put("datosSolicitudEstados", getDatosSolicitudEstados(mutualidadForm));
		return mutualidadFormHashtable;
		
	}

	/**
	 * 
	 */
	public RespuestaMutualidad isPosibilidadSolicitudAlta(String numeroIdentificacion,String fechaNacimiento,UsrBean usrBean) throws SIGAException,Exception{
		MutualidadWSClient mutualidadWSClient =  new MutualidadWSClient(usrBean);
		RespuestaMutualidad posibilidadSolicitudAlta = mutualidadWSClient.getPosibilidadSolicitudAlta(numeroIdentificacion,fechaNacimiento);
		//if(posibilidadSolicitudAlta.getValorRespuesta()==null || posibilidadSolicitudAlta.getValorRespuesta().equals(""))
		//	throw new SIGAException("No es posible Realizar el alta. Revise los datos del formulario");
		return posibilidadSolicitudAlta;
		
	}
	
	public MutualidadForm setCobertura(MutualidadForm mutualidadForm, UsrBean usrBean) throws Exception{
		MutualidadWSClient mutualidadWSClient =  new MutualidadWSClient(usrBean);
		RespuestaMutualidad cuotaCapitalCobertura =  mutualidadWSClient.getCuotaYCapital(mutualidadForm.getFechaNacimiento(),mutualidadForm.getIdSexo(),mutualidadForm.getIdCobertura());
		mutualidadForm.setCuotaCobertura(UtilidadesNumero.formato(cuotaCapitalCobertura.getCuota()));
		mutualidadForm.setCapitalCobertura(UtilidadesNumero.formato(cuotaCapitalCobertura.getCapital()));
		return mutualidadForm;
		
	}
	
	public MutualidadForm setMutualidadForm(MutualidadForm mutualidadForm, UsrBean usrBean) throws Exception{
		MutualidadWSClient mutualidadWSClient =  new MutualidadWSClient(usrBean);
		RespuestaMutualidad combosMutualidad =  mutualidadWSClient.getCombos();
		if(combosMutualidad.getAsistencia()!=null)
			mutualidadForm.setAsistenciasSanitarias(combosMutualidad.getAsistencia());
		else
			mutualidadForm.setAsistenciasSanitarias(new ArrayList<ValueKeyVO>());
		if(combosMutualidad.getBeneficiarios()!=null)
			mutualidadForm.setBeneficiarios(combosMutualidad.getBeneficiarios());
		else
			mutualidadForm.setBeneficiarios(new ArrayList<ValueKeyVO>());
		if(combosMutualidad.getPeriodicidades()!=null)
			mutualidadForm.setPeriodicidadesPago(combosMutualidad.getPeriodicidades());
		else
			mutualidadForm.setPeriodicidadesPago(new ArrayList<ValueKeyVO>());
		if(combosMutualidad.getCoberturas()!=null){
			mutualidadForm.setOpcionesCobertura(combosMutualidad.getCoberturas());
			//String primeraOpcionCobertura = mutualidadForm.getOpcionesCobertura().keySet().iterator().next();
			String idCobertura = ""; 
			if(mutualidadForm.getIdCobertura()!=null && !mutualidadForm.getIdCobertura().equalsIgnoreCase("")){
				idCobertura=mutualidadForm.getIdCobertura();
			}else{
				idCobertura=mutualidadForm.getOpcionesCobertura().get(0).getKey();
			}
			mutualidadForm.setIdCobertura(idCobertura);
			this.setCobertura(mutualidadForm,usrBean);
			
		}
		else{
			mutualidadForm.setOpcionesCobertura(new ArrayList<ValueKeyVO>());
			mutualidadForm.setCuotaCobertura("");
			mutualidadForm.setCapitalCobertura("");
		}
		
		
		
		return mutualidadForm;
		
	}
//	public MutualidadForm setMutualidadFormDefecto(MutualidadForm mutualidadForm) throws Exception{
//		
//		String id = "";
//		String descripcion = "";
//		
//		Iterator itePeriodicidad =  mutualidadForm.getPeriodicidadesPago().keySet().iterator();
//		while(itePeriodicidad.hasNext())	{
//			id = (String) itePeriodicidad.next();
//			descripcion = mutualidadForm.getPeriodicidadesPago().get(id);
//			if(descripcion.equalsIgnoreCase("mensual")){
//				mutualidadForm.setIdPeriodicidadPago(id);
//			break;
//			}
//			
//		}
//		Iterator iteOpciones =  mutualidadForm.getOpcionesCobertura().keySet().iterator();
//		while(iteOpciones.hasNext())	{
//			id = (String) iteOpciones.next();
//			descripcion = mutualidadForm.getOpcionesCobertura().get(id);
//			if(descripcion.equalsIgnoreCase("recomendada")){
//				mutualidadForm.setIdCobertura(id);
//				break;
//			}
//			
//		}
//		
//		
//		mutualidadForm.setIdAsistenciaSanitaria("3");
//		mutualidadForm.setIdBeneficiario("3");
//		
////		mutualidadForm.setAsistenciasSanitarias(combosMutualidad.getAsistencia());
////		mutualidadForm.setBeneficiarios(combosMutualidad.getBeneficiarios());
//		
//		
//		return mutualidadForm;
//		
//	}
	
	public MutualidadForm getDatosPersonaFicha(MutualidadForm mutualidadForm, UsrBean usr) throws Exception{

		String idInstitucion = usr.getLocation();
		CenClienteAdm cliAdm = new CenClienteAdm(usr);
		CenPersonaAdm perAdm = new CenPersonaAdm(usr);
		String idPersona = mutualidadForm.getIdPersona();
		Vector v = cliAdm.getDatosPersonales(Long.parseLong(idPersona), Integer.parseInt(idInstitucion));

		CenDireccionesAdm dirAdm = new CenDireccionesAdm(usr);
		CenDireccionesBean dirBean = dirAdm.obtenerDireccionTipo(idPersona, idInstitucion, Integer.toString(ClsConstants.TIPO_DIRECCION_CENSOWEB));
		
		CenInstitucionAdm instAdm = new CenInstitucionAdm(usr);
		
		CenCuentasBancariasAdm cuentaAdm = new CenCuentasBancariasAdm(usr);
		ArrayList<CenCuentasBancariasBean> cuentasArr = cuentaAdm.getCuentasCargo(Long.parseLong(idPersona), Integer.parseInt(idInstitucion));
		
		
		if (v.size()>0){
			Hashtable per = (Hashtable)v.get(0);
			per.size();
			mutualidadForm.setApellido1(UtilidadesHash.getString(per,"APELLIDOS1"));
			mutualidadForm.setApellido2(UtilidadesHash.getString(per,"APELLIDOS2"));
			mutualidadForm.setNombre(UtilidadesHash.getString(per,"NOMBRE"));
			mutualidadForm.setIdSexo(UtilidadesHash.getString(per,"SEXO"));
			mutualidadForm.setSexo(UtilidadesHash.getString(per,"SEXO"));
			mutualidadForm.setIdEstadoCivil(UtilidadesHash.getString(per,"IDESTADOCIVIL"));
			mutualidadForm.setIdTipoIdentificacion(UtilidadesHash.getString(per,"IDTIPOIDENTIFICACION"));
			mutualidadForm.setNumeroIdentificacion(UtilidadesHash.getString(per,"NIFCIF"));
			mutualidadForm.setIdTratamiento(UtilidadesHash.getString(per,"IDTRATAMIENTO"));
			mutualidadForm.setFechaNacimiento(
				UtilidadesString.formatoFecha(
					UtilidadesHash.getString(per,"FECHANACIMIENTO"),
					ClsConstants.DATE_FORMAT_JAVA, 
					ClsConstants.DATE_FORMAT_SHORT_SPANISH)
			);
			
			if(dirBean!=null) {
				mutualidadForm.setCodigoPostal(dirBean.getCodigoPostal());
				mutualidadForm.setIdPais(dirBean.getIdPais());
				mutualidadForm.setIdProvincia(dirBean.getIdProvincia());
				mutualidadForm.setIdPoblacion(dirBean.getIdPoblacion());
				mutualidadForm.setDomicilio(dirBean.getDomicilio());
				mutualidadForm.setTelef1(dirBean.getTelefono1());
				mutualidadForm.setFax1(dirBean.getFax1());
				mutualidadForm.setMovil(dirBean.getMovil());
				mutualidadForm.setPoblacionExtranjera(dirBean.getPoblacionExtranjera());
				mutualidadForm.setCorreoElectronico(dirBean.getCorreoElectronico());
			}
			mutualidadForm.setIdSolicitudIncorporacion(idPersona);
			
			if(!cuentasArr.isEmpty()){
				CenCuentasBancariasBean cuentaBean = cuentasArr.get(cuentasArr.size()-1);
				mutualidadForm.setCboCodigo(cuentaBean.getCbo_Codigo());
				mutualidadForm.setCodigoSucursal(cuentaBean.getCodigoSucursal());
				mutualidadForm.setDigitoControl(cuentaBean.getDigitoControl());
				mutualidadForm.setNumeroCuenta(cuentaBean.getNumeroCuenta());
				mutualidadForm.setIban(cuentaBean.getIban());
			}
			
			// Nos tenemos que ir a bbdd a por algunos valores
			mutualidadForm.setTratamiento(cliAdm.getTratmiento(Long.parseLong(idPersona), Integer.parseInt(idInstitucion)));
			mutualidadForm.setEstadoCivil(perAdm.getEstadoCivil(Long.parseLong(idPersona)));
			
			mutualidadForm.setIdInstitucion(usr.getLocation());
			mutualidadForm.setColegio(instAdm.getAbreviaturaInstitucion(mutualidadForm.getIdInstitucion()));
		}
		return mutualidadForm;
	}
	
	public MutualidadForm getSolicitudMutualidad(MutualidadForm mutualidadForm,String idPersona,String idTipoSolicitud,UsrBean usrBean) throws ClsExceptions, SIGAException{
		CenSolicitudMutualidadAdm  solicitudMutualidadAdm = new CenSolicitudMutualidadAdm(usrBean);
		CenSolicitudMutualidadBean solicitudMutualidadBean =  solicitudMutualidadAdm.getSolicitudMutualidad(idPersona, idTipoSolicitud);
		if(solicitudMutualidadBean!=null)
			mutualidadForm = solicitudMutualidadBean.getMutualidadForm(mutualidadForm);
		else{
			//mutualidadForm.setEstado(CenSolicitudMutualidadBean.SERVICIO_NOSOLICITADO);
			if(idTipoSolicitud.equalsIgnoreCase("P")){
				mutualidadForm.setIdTipoSolicitud(idTipoSolicitud);
				CenPersonaAdm personaAdm = new CenPersonaAdm(usrBean);
				CenPersonaBean personaBean = personaAdm.getPersonaPorId(idPersona);
				mutualidadForm.setNumeroIdentificacion(personaBean.getNIFCIF());
				mutualidadForm.setIdTipoIdentificacion(personaBean.getIdTipoIdentificacion().toString());
				try {
					mutualidadForm.setFechaNacimiento(UtilidadesString.formatoFecha(personaBean.getFechaNacimiento(), ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH ));
				} catch (Exception e) {
					mutualidadForm.setFechaNacimiento(personaBean.getFechaNacimiento());
				}
			}
		}
		return mutualidadForm;
		
	}
	
	
	public List<CenSolicitudMutualidadBean> getSolicitudesMutualidad(CenSolicitudIncorporacionBean solicitudIncorporacionBean, UsrBean usrBean)
			throws ClsExceptions {
		CenSolicitudMutualidadAdm  solicitudMutualidadAdm = new CenSolicitudMutualidadAdm(usrBean);
		List<CenSolicitudMutualidadBean> solicitudMutualidadBeans = solicitudMutualidadAdm.getSolicitudesMutualidad(solicitudIncorporacionBean);
		return solicitudMutualidadBeans;
	}
	public Object executeService(Object... parameters) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public MutualidadForm getDatosSolicitudIncorporacion(MutualidadForm form, String idSolicitud, UsrBean usr) throws Exception {
		CenSolicitudIncorporacionAdm solIncAdm = new CenSolicitudIncorporacionAdm(usr);
		GenCatalogosWSAdm catAdm = new GenCatalogosWSAdm(usr);
		CenInstitucionAdm instAdm = new CenInstitucionAdm(usr);
		CenTratamientoAdm tratamientoAdm = new CenTratamientoAdm(usr);
		Hashtable hash = new Hashtable();
		hash.put(CenSolicitudIncorporacionBean.C_IDSOLICITUD, idSolicitud);
		Vector v = solIncAdm.selectByPK(hash);
		if(v.size()==1){
			CenSolicitudIncorporacionBean bean = (CenSolicitudIncorporacionBean)v.get(0);
			form.setIdInstitucion(usr.getLocation());
			form.setTipoIdentificacion(bean.getIdTipoIdentificacion().toString());
			form.setIdTipoIdentificacion(bean.getIdTipoIdentificacion().toString());
			form.setNumeroIdentificacion(bean.getNumeroIdentificador()!=null?bean.getNumeroIdentificador().toString():"");
			form.setSexo(bean.getSexo()); 
			form.setTratamiento(tratamientoAdm.getTratamiento(bean.getIdTratamiento().toString())); 
			form.setNombre(bean.getNombre());
			form.setApellido1(bean.getApellido1());
			form.setApellido2(bean.getApellido2());
			form.setNacionalidad(""); 
			form.setFechaNacimiento(UtilidadesString.formatoFecha( bean.getFechaNacimiento(), ClsConstants.DATE_FORMAT_JAVA, ClsConstants.DATE_FORMAT_SHORT_SPANISH)); 
			form.setEstadoCivil(bean.getIdEstadoCivil()!=null?bean.getIdEstadoCivil().toString():"");
			form.setIdPais(bean.getIdPais());
			form.setIdProvincia(bean.getIdProvincia());
			form.setIdPoblacion(bean.getIdPoblacion());
			form.setDomicilio(bean.getDomicilio());
			form.setCodigoPostal(bean.getCodigoPostal());
			form.setTelef1(bean.getTelefono1());
			form.setTelef2(bean.getTelefono2());
			form.setMovil(bean.getMovil());
			form.setFax1(bean.getFax1());
			form.setFax2(bean.getFax1());
			form.setCorreoElectronico(bean.getCorreoElectronico());
			form.setCodigoSucursal(bean.getCodigoSucursal()); 
			form.setIban(bean.getIban());
			form.setDigitoControl(bean.getDigitoControl());
			form.setNumeroCuenta(bean.getNumeroCuenta());
			form.setCboCodigo(bean.getCbo_Codigo());
			form.setPoblacionExtranjera(bean.getPoblacionExtranjera()); 
			form.setIdSexo(bean.getSexo());
			form.setIdTratamiento(bean.getIdTratamiento()!=null?bean.getIdTratamiento().toString():"");
			form.setIdEstadoCivil(bean.getIdEstadoCivil()!=null?bean.getIdEstadoCivil().toString():"");
			form.setIdSolicitudIncorporacion(bean.getIdSolicitud().toString());
			form.setIdInstitucion(usr.getLocation());
			form.setColegio(instAdm.getAbreviaturaInstitucion(form.getIdInstitucion()));
			
			form.setTipoIdentificacion(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_TIPOIDENTIFICADOR, bean.getIdTipoIdentificacion().toString()));
			form.setSexo(bean.getSexo());
			form.setEstadoCivil(catAdm.getValor(AlterMutuaHelper.CATALOGO, AlterMutuaHelper.CONJUNTO_ESTADOCIVIL, form.getEstadoCivil()));
		}
		return form;
	}

	public MutualidadForm setMutualidadFormDefecto(MutualidadForm mutualidadForm)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
