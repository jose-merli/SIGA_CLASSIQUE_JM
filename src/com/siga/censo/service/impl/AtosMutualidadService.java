package com.siga.censo.service.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.CenSolicitudIncorporacionBean;
import com.siga.beans.CenSolicitudMutualidadAdm;
import com.siga.beans.CenSolicitudMutualidadBean;
import com.siga.censo.form.MutualidadForm;
import com.siga.censo.service.MutualidadService;
import com.siga.general.SIGAException;
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
	
	public void insertarSolicitudMutualidad(MutualidadForm mutualidadForm, UsrBean usrBean)throws Exception{
		CenSolicitudMutualidadBean solicitudMutualidadBean = mutualidadForm.getMutualidadBean();
		CenSolicitudMutualidadAdm solicitudMutualidadAdm = new CenSolicitudMutualidadAdm(usrBean);
		solicitudMutualidadBean.setIdEstado(CenSolicitudMutualidadBean.ESTADO_INICIAL);
		solicitudMutualidadBean.setIdSolicitud(solicitudMutualidadAdm.getNewIdSolicitud());
		mutualidadForm.setIdSolicitud(solicitudMutualidadBean.getIdSolicitud().toString());
		solicitudMutualidadBean.setFechaEstado("SYSDATE");
		solicitudMutualidadBean.setFechaSolicitud("SYSDATE");
		solicitudMutualidadBean.setIdInstitucion(new Integer(usrBean.getLocation()));
		solicitudMutualidadAdm.insert(solicitudMutualidadBean);
//		MutualidadWSClient mutualidadWSClient =  new MutualidadWSClient(usrBean);
		//altaMutualidad(mutualidadForm,usrBean);
		
		//solicitudMutualidadBean.setIdSolicitudAceptada(new Long(mutualidadForm.getIdSolicitudAceptada()));
		solicitudMutualidadBean.setIdSolicitudAceptada(new Long(99999));
		
		
		
//		RespuestaMutualidad respuestaSolicitud = mutualidadWSClient.getEstadoSolicitud(solicitudMutualidadBean.getIdSolicitudAceptada());
		//solicitudMutualidadBean.setEstado(respuestaSolicitud.getValorRespuesta());
		solicitudMutualidadBean.setEstado("Solicitud Formalizada");
		solicitudMutualidadBean.setIdEstado(CenSolicitudMutualidadBean.ESTADO_SOLICITADO);
		if(mutualidadForm.getIdTipoSolicitud().equals(CenSolicitudMutualidadBean.TIPOSOLICITUD_PLANPROFESIONAL));
			solicitudMutualidadBean.setEstadoMutualista("Pte. Estado Mutualista");
		mutualidadForm = solicitudMutualidadBean.getMutualidadForm(mutualidadForm);
		solicitudMutualidadAdm.actualizaSolicitudAceptada(solicitudMutualidadBean);
		if(mutualidadForm.getIdTipoSolicitud().equals(CenSolicitudMutualidadBean.TIPOSOLICITUD_PLANPROFESIONAL))
			solicitudMutualidadAdm.actualizaEstadoMutualista(solicitudMutualidadBean);
		
		
		
		
		
	}
	private void altaMutualidad(MutualidadForm mutualidadForm, UsrBean usrBean) throws Exception{
		MutualidadWSClient mutualidadWSClient =  new MutualidadWSClient(usrBean);
		RespuestaMutualidad respuestaAlta = null; 
		if(mutualidadForm.getIdTipoSolicitud().equals(CenSolicitudMutualidadBean.TIPOSOLICITUD_PLANPROFESIONAL))
			respuestaAlta = mutualidadWSClient.altaPlanProfesional(getFormularioAlta(mutualidadForm));
		else if(mutualidadForm.getIdTipoSolicitud().equals(CenSolicitudMutualidadBean.TIPOSOLICITUD_SEGUROUNIVERSAL))
			respuestaAlta = mutualidadWSClient.altaAccidentesUniversal(getFormularioAlta(mutualidadForm));
		else 
			throw new SIGAException("El tipo de solicitud no esta implementado");
		mutualidadForm.setIdSolicitudAceptada(respuestaAlta.getIdSolicitud().toString());
		
		
	}
	private void actualizaEstado(MutualidadForm mutualidadForm, UsrBean usrBean)throws Exception{
		CenSolicitudMutualidadBean solicitudMutualidadBean = mutualidadForm.getMutualidadBean();
		MutualidadWSClient mutualidadWSClient =  new MutualidadWSClient(usrBean);
		//RespuestaMutualidad respuestaSolicitud = mutualidadWSClient.getEstadoSolicitud(solicitudMutualidadBean.getIdSolicitudAceptada());
		//solicitudMutualidadBean.setEstado(respuestaSolicitud.getValorRespuesta());
		solicitudMutualidadBean.setIdEstado(CenSolicitudMutualidadBean.ESTADO_SOLICITADO);
		solicitudMutualidadBean.setEstado("Estado OK");
		
		CenSolicitudMutualidadAdm solicitudMutualidadAdm = new CenSolicitudMutualidadAdm(usrBean);
		solicitudMutualidadAdm.actualizaSolicitudAceptada(solicitudMutualidadBean);
		mutualidadForm = solicitudMutualidadBean.getMutualidadForm(mutualidadForm);
		
		
	}
	public void actualizaEstadoSolicitud(MutualidadForm mutualidadForm, UsrBean usrBean)throws Exception{
		if(mutualidadForm.getIdSolicitudAceptada()!=null && !mutualidadForm.getIdSolicitudAceptada().equals("")){
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
		//RespuestaMutualidad respuestaSolicitud = mutualidadWSClient.getEstadoMutualista(solicitudMutualidadBean.getIdSolicitudAceptada());
		//solicitudMutualidadBean.setEstadoMutualista(respuestaSolicitud.getValorRespuesta());
		solicitudMutualidadBean.setEstadoMutualista("Mesualidad conforme");
		CenSolicitudMutualidadAdm solicitudMutualidadAdm = new CenSolicitudMutualidadAdm(usrBean);
		solicitudMutualidadAdm.actualizaEstadoMutualista(solicitudMutualidadBean);
		mutualidadForm = solicitudMutualidadBean.getMutualidadForm(mutualidadForm);
		
	}
	
	
	
	private Hashtable getDatosBancarios(MutualidadForm mutualidadForm){
		Hashtable datosBancarios = new Hashtable();
		if(mutualidadForm.getSwift()!=null && !mutualidadForm.getSwift().equals("")){
			datosBancarios.put("iban", mutualidadForm.getIban());
			datosBancarios.put("swift", mutualidadForm.getSwift());
		}else{
			datosBancarios.put("cboCodigo", mutualidadForm.getCboCodigo());
			datosBancarios.put("codigoSucursal", mutualidadForm.getCodigoSucursal());
			datosBancarios.put("digitoControl", mutualidadForm.getDigitoControl());
			datosBancarios.put("numeroCuenta", mutualidadForm.getNumeroCuenta());
		}
		
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
		datosPersona.put("nacionalidad", mutualidadForm.getNaturalDe());
		datosPersona.put("nombre", mutualidadForm.getNombre());
		datosPersona.put("idSexo", mutualidadForm.getIdSexo());
		datosPersona.put("numHijos", mutualidadForm.getNumeroHijos());
		datosPersona.put("numColegiado", "");
		//No pasamos el colegio
//		datosSolicitud.put("colegio", mutualidadForm);
		//en ejerciente Pasamos el id del tipo de solicitud de incorporacion
//		datosSolicitud.put("ejerciente", mutualidadForm);
		datosPersona.put("estadoCivil", mutualidadForm.getIdEstadoCivil());
//		datosBeneficiarios.put("idSolicitud", mutualidadForm);
		datosPersona.put("fechaNacimiento", mutualidadForm.getFechaNacimiento());
		datosPersona.put("fechaNacimientoConyuge", mutualidadForm.getFechaNacimientoConyuge());
		
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

	public boolean isPosibilidadSolicitudAlta(String numeroIdentificacion,String fechaNacimiento,UsrBean usrBean) throws SIGAException,Exception{
		MutualidadWSClient mutualidadWSClient =  new MutualidadWSClient(usrBean);
		RespuestaMutualidad posibilidadSolicitudAlta = mutualidadWSClient.getPosibilidadSolicitudAlta(numeroIdentificacion,fechaNacimiento);
		if(!posibilidadSolicitudAlta.getMensajeError().equals(""))
			throw new SIGAException(posibilidadSolicitudAlta.getMensajeError());
		
		return posibilidadSolicitudAlta.isPosibleAlta();
		
	}
	public MutualidadForm setCobertura(MutualidadForm mutualidadForm, UsrBean usrBean) throws Exception{
		MutualidadWSClient mutualidadWSClient =  new MutualidadWSClient(usrBean);
		RespuestaMutualidad cuotaCapitalCobertura =  mutualidadWSClient.getCuotaYCapital(mutualidadForm.getFechaNacimiento(),mutualidadForm.getIdSexo(),mutualidadForm.getIdCobertura());
		mutualidadForm.setCuotaCobertura(cuotaCapitalCobertura.getCuota().toString());
		mutualidadForm.setCapitalCobertura(cuotaCapitalCobertura.getCapital().toString());
		return mutualidadForm;
		
	}
	
	public MutualidadForm setMutualidadForm(MutualidadForm mutualidadForm, UsrBean usrBean) throws Exception{
		MutualidadWSClient mutualidadWSClient =  new MutualidadWSClient(usrBean);
		RespuestaMutualidad combosMutualidad =  mutualidadWSClient.getCombos();
		if(combosMutualidad.getAsistencia()!=null)
			mutualidadForm.setAsistenciasSanitarias(combosMutualidad.getAsistencia());
		else
			mutualidadForm.setAsistenciasSanitarias(new HashMap<String, String>());
		if(combosMutualidad.getBeneficiarios()!=null)
			mutualidadForm.setBeneficiarios(combosMutualidad.getBeneficiarios());
		else
			mutualidadForm.setBeneficiarios(new HashMap<String, String>());
		if(combosMutualidad.getPeriodicidades()!=null)
			mutualidadForm.setPeriodicidadesPago(combosMutualidad.getPeriodicidades());
		else
			mutualidadForm.setPeriodicidadesPago(new HashMap<String, String>());
		if(combosMutualidad.getCoberturas()!=null){
			mutualidadForm.setOpcionesCobertura(combosMutualidad.getCoberturas());
			String primeraOpcionCobertura = mutualidadForm.getOpcionesCobertura().keySet().iterator().next();
			mutualidadForm.setIdCobertura(primeraOpcionCobertura);
			this.setCobertura(mutualidadForm,usrBean);
			
		}
		else{
			mutualidadForm.setOpcionesCobertura(new HashMap<String, String>());
			mutualidadForm.setCuotaCobertura("");
			mutualidadForm.setCapitalCobertura("");
		}
		
		
		
		return mutualidadForm;
		
	}
	
	public MutualidadForm getSolicitudMutualidad(MutualidadForm mutualidadForm,String idPersona,String idTipoSolicitud,UsrBean usrBean) throws ClsExceptions, SIGAException{
		CenSolicitudMutualidadAdm  solicitudMutualidadAdm = new CenSolicitudMutualidadAdm(usrBean);
		CenSolicitudMutualidadBean solicitudMutualidadBean =  solicitudMutualidadAdm.getSolicitudMutualidad(idPersona, idTipoSolicitud);
		if(solicitudMutualidadBean!=null)
			mutualidadForm = solicitudMutualidadBean.getMutualidadForm(mutualidadForm);
		else
			mutualidadForm.setEstado(CenSolicitudMutualidadBean.SERVICIO_NOSOLICITADO);
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

}
