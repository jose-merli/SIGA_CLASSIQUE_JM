package com.siga.censo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.CenBajasTemporalesAdm;
import com.siga.beans.CenBajasTemporalesBean;
import com.siga.beans.CenPersonaAdm;
import com.siga.beans.CenPersonaBean;
import com.siga.beans.GenParametrosAdm;
import com.siga.beans.ScsGuardiasColegiadoAdm;
import com.siga.censo.form.BajasTemporalesForm;
import com.siga.censo.service.BajasTemporalesService;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.JtaBusinessServiceTemplate;

public class AtosBajasTemporalesService extends JtaBusinessServiceTemplate 
	implements BajasTemporalesService {

	public AtosBajasTemporalesService(BusinessManager businessManager) {
		super(businessManager);
	}
	public Object executeService(Object... parameters) throws BusinessException {
		return null;
	}
	/**
	 * Me inserta
	 */
	public Object executeService(CenBajasTemporalesBean bajasTemporalesVo)
			throws SIGAException, ClsExceptions {
		return null;
	}
	
	public List<CenBajasTemporalesBean> getBajasTemporales(
			BajasTemporalesForm bajaTemporalForm,UsrBean usrBean)throws ClsExceptions {
		
		CenBajasTemporalesAdm btAdm = new CenBajasTemporalesAdm(usrBean);
		
		List<CenBajasTemporalesBean> lista = btAdm.getBajasTemporales(bajaTemporalForm);
		return lista;
	}
	public CenBajasTemporalesBean getBajaTemporal(	BajasTemporalesForm bajaTemporalForm,UsrBean usrBean)throws ClsExceptions {
		
		CenBajasTemporalesAdm btAdm = new CenBajasTemporalesAdm(usrBean);
		CenBajasTemporalesBean cenBajasTemporalesBean = btAdm.getBajaTemporal(bajaTemporalForm);
		return cenBajasTemporalesBean;
		
	}
	public void insertaBajasTemporales(BajasTemporalesForm bajasTemporalesForm,UsrBean usrBean)throws ClsExceptions{
		CenBajasTemporalesBean bajaTemporal = bajasTemporalesForm.getBajaTemporalBean();
		String datosSelecionados = bajasTemporalesForm.getDatosSeleccionados();
		if(datosSelecionados==null || datosSelecionados.equals("")){
			bajasTemporalesForm.setMsgAviso("No habia ninguna insercion pendiente");
			return;
		}
			
		String idPersonas[] = datosSelecionados.split("@@");
		List<String> lAuxiliar= new ArrayList<String>();
		List<String> alFechas = GstDate.getFechas(bajasTemporalesForm.getFechaDesde(), bajasTemporalesForm.getFechaHasta(), "dd/MM/yyyy");
		CenBajasTemporalesAdm btAdm = new CenBajasTemporalesAdm(usrBean);
		//Atencion si no es un letrado es un administrador por lo que al hacer la solicitud se supone que ya esta validada;
		GenParametrosAdm paramAdm = new GenParametrosAdm (usrBean);
		//Haria falta meter los parametros en con ClsConstants
		String codAceptarSolicitudes = paramAdm.getValor (usrBean.getLocation (), "SCS", ClsConstants.GEN_PARAM_ACEPTAR_SOLICITUD_BAJA_TEMPORAL, "");
		Boolean isAceptarSolicitudes = new Boolean((codAceptarSolicitudes!=null && codAceptarSolicitudes.equalsIgnoreCase(ClsConstants.DB_TRUE)));
        
		
		if(!usrBean.isLetrado()||isAceptarSolicitudes.booleanValue())
			bajaTemporal.setValidado("1");
		String fechAlta = UtilidadesString.formatoFecha(new Date(),"yyyy/MM/dd HH:mm:ss");
		for (int i = 0; i < idPersonas.length; i++) {
			
			String idPersona = idPersonas[i];
			if(lAuxiliar.contains(idPersona))
				continue;
			else
				lAuxiliar.add(idPersona);
			bajaTemporal.setIdPersona(new Long(idPersona));
			for(String fechaBT:alFechas){
				bajaTemporal.setFechaAlta(fechAlta);
				bajaTemporal.setFechaEstado(bajaTemporal.getFechaAlta());
				bajaTemporal.setFechaBT(GstDate.getApplicationFormatDate("", fechaBT));
				btAdm.insert(bajaTemporal);
				
				
			}
			
		}
		
	}
	
	public void comprobarInsercion(BajasTemporalesForm bajasTemporalesForm,UsrBean usrBean)throws ClsExceptions{
		CenBajasTemporalesBean bajaTemporal = bajasTemporalesForm.getBajaTemporalBean();
		
		String datosSelecionados = bajasTemporalesForm.getDatosSeleccionados();
		String auxDatosSeleccionados ="";
		String idPersonas[] = datosSelecionados.split("@@");
		List<String> lAuxiliar= new ArrayList<String>();
		List<String> alFechas = GstDate.getFechas(bajasTemporalesForm.getFechaDesde(), bajasTemporalesForm.getFechaHasta(), "dd/MM/yyyy");
		CenBajasTemporalesAdm btAdm = new CenBajasTemporalesAdm(usrBean);
		ScsGuardiasColegiadoAdm gcAdm = new ScsGuardiasColegiadoAdm(usrBean);
		//Atencion si no es un letrado es un administrador por lo que al hacer la solicitud se supone que ya esta validada;
		
		
		
		Map<Long, CenPersonaBean> tmColegiadosDeBaja =  btAdm.getColegiadosBajaTemporal(bajaTemporal.getIdInstitucion(), bajaTemporal.getFechaDesde(), bajaTemporal.getFechaHasta());
		Map<Long, CenPersonaBean> tmColegiadosDeGuardia =  gcAdm.getColegiadosDeGuardia(bajaTemporal.getIdInstitucion(), bajaTemporal.getFechaDesde(), bajaTemporal.getFechaHasta());
		List<CenPersonaBean> alPersonasDeBaja = new ArrayList<CenPersonaBean>();
		List<CenPersonaBean> alPersonasDeGuardia = new ArrayList<CenPersonaBean>();
		for (int i = 0; i < idPersonas.length; i++) {
			String idPersona = idPersonas[i];
			if(lAuxiliar.contains(idPersona))
				continue;
			else
				lAuxiliar.add(idPersona);
			bajaTemporal.setIdPersona(new Long(idPersona));
			if(tmColegiadosDeGuardia.containsKey(bajaTemporal.getIdPersona())){
				alPersonasDeGuardia.add((CenPersonaBean)tmColegiadosDeGuardia.get(bajaTemporal.getIdPersona()));
				if(tmColegiadosDeBaja.containsKey(bajaTemporal.getIdPersona()))
					alPersonasDeBaja.add((CenPersonaBean)tmColegiadosDeBaja.get(bajaTemporal.getIdPersona()));
				continue;
			}else{
				if(tmColegiadosDeBaja.containsKey(bajaTemporal.getIdPersona())){
					alPersonasDeBaja.add((CenPersonaBean)tmColegiadosDeBaja.get(bajaTemporal.getIdPersona()));
					continue;
				}
				
			}
			for(String fechaBT:alFechas){
				bajaTemporal.setFechaBT(GstDate.getApplicationFormatDate("", fechaBT));
				auxDatosSeleccionados+="@@"+idPersona;
				//alBajasTemporales.add(bajaTemporal);
			}
			
		}
		if(auxDatosSeleccionados.length()>2){
			auxDatosSeleccionados=auxDatosSeleccionados.substring(2);
			
		}
		bajasTemporalesForm.setDatosSeleccionados(auxDatosSeleccionados);
		bajasTemporalesForm.setPersonasDeGuardia(alPersonasDeGuardia);
		bajasTemporalesForm.setPersonasDeBaja(alPersonasDeBaja);

		
	}
	
	
	
	public void denegarSolicitudesBajaTemporal(BajasTemporalesForm bajasTemporalesForm,UsrBean usrBean)throws ClsExceptions{
		CenBajasTemporalesBean bajaTemporal = bajasTemporalesForm.getBajaTemporalBean();
		bajaTemporal.setFechaEstado(UtilidadesString.formatoFecha(new Date(),"yyyy/MM/dd HH:mm:ss"));
		bajaTemporal.setValidado("0");
		String datosSelecionados = bajasTemporalesForm.getDatosSeleccionados();
		String datosSeleccionados[] = datosSelecionados.split("@@");
		CenBajasTemporalesAdm btAdm = new CenBajasTemporalesAdm(usrBean);
		for (int i = 0; i < datosSeleccionados.length; i++) {
			String datoSeleccionado = datosSeleccionados[i];
			String[] datos= datoSeleccionado.split("##"); 
			String idPersona = datos[0];
			String fechaAlta = datos[1];
			bajaTemporal.setIdPersona(new Long(idPersona));
			bajaTemporal.setFechaAlta(fechaAlta);
			btAdm.validarBajaTemporal(bajaTemporal);
			
		}
		
	}
	public void validarSolicitudesBajaTemporal(BajasTemporalesForm bajasTemporalesForm,UsrBean usrBean)throws ClsExceptions{
		CenBajasTemporalesBean bajaTemporal = bajasTemporalesForm.getBajaTemporalBean();
		bajaTemporal.setFechaEstado(UtilidadesString.formatoFecha(new Date(),"yyyy/MM/dd HH:mm:ss"));
		bajaTemporal.setValidado("1");
		String datosSelecionados = bajasTemporalesForm.getDatosSeleccionados();
		String datosSeleccionados[] = datosSelecionados.split("@@");
		
		CenBajasTemporalesAdm btAdm = new CenBajasTemporalesAdm(usrBean);
		for (int i = 0; i < datosSeleccionados.length; i++) {
			String datoSeleccionado = datosSeleccionados[i];
			String[] datos= datoSeleccionado.split("##"); 
			String idPersona = datos[0];
			String fechaAlta = datos[1];
			bajaTemporal.setIdPersona(new Long(idPersona));
			bajaTemporal.setFechaAlta(fechaAlta);
			btAdm.validarBajaTemporal(bajaTemporal);
			
		}
		
	}
	public void borrarSolicitudBajaTemporal(BajasTemporalesForm bajasTemporalesForm,UsrBean usrBean)throws ClsExceptions{
		CenBajasTemporalesBean bajaTemporal = bajasTemporalesForm.getBajaTemporalBean();
		CenBajasTemporalesAdm btAdm = new CenBajasTemporalesAdm(usrBean);
		btAdm.borrarBajaTemporal(bajaTemporal);
		
	}
	public void modificarSolicitudBajaTemporal(BajasTemporalesForm bajasTemporalesForm,UsrBean usrBean)throws ClsExceptions{
		/* 
		CenBajasTemporalesBean bajaTemporal = bajasTemporalesForm.getBajaTemporalBean();
		CenBajasTemporalesAdm btAdm = new CenBajasTemporalesAdm(usrBean);
		btAdm.modificarBajaTemporal(bajaTemporal);
		*/
		//BNS INC_10980_SIGA
		try {
			this.borrarSolicitudBajaTemporal(bajasTemporalesForm, usrBean);
			if (bajasTemporalesForm.getDatosSeleccionados() == null || bajasTemporalesForm.getDatosSeleccionados().equals(""))
				bajasTemporalesForm.setDatosSeleccionados(bajasTemporalesForm.getIdPersona());
			this.comprobarInsercion(bajasTemporalesForm, usrBean);
			if((bajasTemporalesForm.getPersonasDeBaja()!=null && bajasTemporalesForm.getPersonasDeBaja().size()>0)||
					(bajasTemporalesForm.getPersonasDeGuardia()!=null && bajasTemporalesForm.getPersonasDeGuardia().size()>0)){
				throw new ClsExceptions("Incidencias");
			} else {
				this.insertaBajasTemporales(bajasTemporalesForm, usrBean);
			}
		} catch (ClsExceptions e){
			throw e;
		} catch(Exception e){
			throw new ClsExceptions("Ha ocurrido un error al procesar la acción");
		}
	}
	public void setColegiado(BajasTemporalesForm bajasTemporalesForm,UsrBean usrBean)throws ClsExceptions{
		CenPersonaAdm personaAdm = new CenPersonaAdm(usrBean);
		CenPersonaBean persona =  personaAdm.getPersonaColegiado(bajasTemporalesForm.getBajaTemporalBean().getIdPersona(), bajasTemporalesForm.getBajaTemporalBean().getIdInstitucion());
		StringBuffer nombre = new StringBuffer();
		nombre.append(persona.getNombre());
		nombre.append(" ");
		nombre.append(persona.getApellido1());
		if(persona.getApellido2()!=null){
			nombre.append(" ");
			nombre.append(persona.getApellido2());
		}
		bajasTemporalesForm.setColegiadoNombre(nombre.toString());
		bajasTemporalesForm.setColegiadoNumero(persona.getColegiado().getNColegiado());
		
	}
	

}
