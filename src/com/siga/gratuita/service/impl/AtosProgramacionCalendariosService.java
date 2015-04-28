package com.siga.gratuita.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.transaction.Status;

import org.redabogacia.sigaservices.app.helper.SIGAServicesHelper;
import org.redabogacia.sigaservices.app.services.gen.FicherosService;
import org.redabogacia.sigaservices.app.services.scs.CargaMasivaCalendariosService;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;
import org.redabogacia.sigaservices.app.vo.gen.FicheroVo;
import org.redabogacia.sigaservices.app.vo.scs.CargaMasivaCalendariosVo;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.GstDate;
import com.atos.utils.GstStringTokenizer;
import com.atos.utils.LogFileWriter;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.ScsCalendarioGuardiasAdm;
import com.siga.beans.ScsCalendarioGuardiasBean;
import com.siga.beans.ScsGuardiasTurnoAdm;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsTurnoAdm;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.SIGAException;
import com.siga.gratuita.adm.ScsConfConjuntoGuardiasAdm;
import com.siga.gratuita.adm.ScsConjuntoGuardiasAdm;
import com.siga.gratuita.adm.ScsHcoConfProgrCalendariosAdm;
import com.siga.gratuita.adm.ScsProgrCalendariosAdm;
import com.siga.gratuita.beans.ScsConjuntoGuardiasBean;
import com.siga.gratuita.beans.ScsHcoConfProgCalendariosBean;
import com.siga.gratuita.beans.ScsProgCalendariosBean;
import com.siga.gratuita.form.ConfConjuntoGuardiasForm;
import com.siga.gratuita.form.ConjuntoGuardiasForm;
import com.siga.gratuita.form.DefinirCalendarioGuardiaForm;
import com.siga.gratuita.form.HcoConfProgrCalendarioForm;
import com.siga.gratuita.form.ProgrCalendariosForm;
import com.siga.gratuita.service.ProgramacionCalendariosService;
import com.siga.gratuita.util.calendarioSJCS.CalendarioSJCS;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.JtaBusinessServiceTemplate;

public class AtosProgramacionCalendariosService extends JtaBusinessServiceTemplate 
	implements ProgramacionCalendariosService {
	private static Boolean alguienEjecutando=Boolean.FALSE;
	private static Boolean algunaEjecucionDenegada=Boolean.FALSE;
	public AtosProgramacionCalendariosService(BusinessManager businessManager) {
		super(businessManager);
	}
	public Object executeService(Object... parameters) throws BusinessException {
		return null;
	}
	/**
	 * Me inserta
	 */
	public Object executeService()
			throws SIGAException, ClsExceptions {
		return null;
	}
	public List<ConjuntoGuardiasForm> getConjuntosGuardia(String idInstitucion,UsrBean usrBean)throws ClsExceptions{
		ScsConjuntoGuardiasAdm ConjuntoGuardiasAdm = new ScsConjuntoGuardiasAdm(usrBean);
		List<ConjuntoGuardiasForm> lista = ConjuntoGuardiasAdm.getConjuntosGuardia(idInstitucion);
		return lista;
		
	}
	public void insertaConjuntoGuardias(ConjuntoGuardiasForm ConjuntoGuardiasForm, UsrBean usrBean)
			throws ClsExceptions, SIGAException {
		ScsConjuntoGuardiasBean ConjuntoGuardiasBean = ConjuntoGuardiasForm.getConjuntoGuardiaVO();
		ScsConjuntoGuardiasAdm ConjuntoGuardiasAdm = new ScsConjuntoGuardiasAdm(usrBean);
		Long idConjuntoGuardias = ConjuntoGuardiasAdm.getNewIdConjuntoGuardias();
		ConjuntoGuardiasBean.setIdConjuntoGuardia(idConjuntoGuardias);
		ConjuntoGuardiasAdm.insert(ConjuntoGuardiasBean);
		String guardiasInsertar = ConjuntoGuardiasForm.getGuardiasInsertar();
		if(guardiasInsertar!=null && !guardiasInsertar.equals("")){
			ScsConfConjuntoGuardiasAdm confConjuntoGuardiasAdm = new ScsConfConjuntoGuardiasAdm(usrBean);
			List<ConfConjuntoGuardiasForm> listConfGuardiasInsertar = getDatosConfGuardias(guardiasInsertar);
			for (ConfConjuntoGuardiasForm confConjuntoGuardiasForm : listConfGuardiasInsertar) {
				confConjuntoGuardiasForm.setIdInstitucion(ConjuntoGuardiasBean.getIdInstitucion().toString());
				confConjuntoGuardiasForm.setIdConjuntoGuardia(ConjuntoGuardiasBean.getIdConjuntoGuardia().toString());
				try {
					confConjuntoGuardiasAdm.insert(confConjuntoGuardiasForm.getConfConjuntoGuardiaVO());
				} catch (Exception e) {
					throw new SIGAException("Existen incompatibilidades en la configuracion del conjunto de guardias. Seguramente haya lgun orden repetido");
				}
			}
		}
	}

	public ConjuntoGuardiasForm getUltimoConjuntoGuardiaInsertado(
			String idInstitucion, UsrBean usrBean) throws ClsExceptions {
		ScsConjuntoGuardiasAdm ConjuntoGuardiasAdm = new ScsConjuntoGuardiasAdm(usrBean);
		ScsConjuntoGuardiasBean ConjuntoGuardiasBean = ConjuntoGuardiasAdm.getUltimoConjuntoGuardiaInsertado(idInstitucion);
		
		return ConjuntoGuardiasBean.getConjuntoGuardiaForm();
	}
	public List<ConfConjuntoGuardiasForm> getConfiguracionConjuntoGuardias(ConfConjuntoGuardiasForm confConjuntoGuardiasForm,boolean isMostrarSoloGuardiasConfiguradas,UsrBean usrBean) throws ClsExceptions {
		ScsConfConjuntoGuardiasAdm confConjuntoGuardiasAdm = new ScsConfConjuntoGuardiasAdm(usrBean);
		List<ConfConjuntoGuardiasForm> lista = confConjuntoGuardiasAdm.getConfiguracionConjuntoGuardias(confConjuntoGuardiasForm,isMostrarSoloGuardiasConfiguradas);
		return lista;
	}
	public void insertarConfiguracionConjuntoGuardias(
			ConfConjuntoGuardiasForm confConjuntoGuardiasForm, UsrBean usrBean)
			throws ClsExceptions, SIGAException {
		ScsConfConjuntoGuardiasAdm confConjuntoGuardiasAdm = new ScsConfConjuntoGuardiasAdm(usrBean);
		String guardiasInsertar = confConjuntoGuardiasForm.getGuardiasInsertar();
		String guardiasBorrar = confConjuntoGuardiasForm.getGuardiasBorrar();
		
		if(guardiasBorrar!=null && !guardiasBorrar.equals("")){
			List<ConfConjuntoGuardiasForm> listConfGuardiasBorrar = getDatosConfGuardias(guardiasBorrar);
			for (ConfConjuntoGuardiasForm confConjuntoGuardiasForm2 : listConfGuardiasBorrar) {
				confConjuntoGuardiasForm2.setIdInstitucion(confConjuntoGuardiasForm.getIdInstitucion());
				confConjuntoGuardiasForm2.setIdConjuntoGuardia(confConjuntoGuardiasForm.getIdConjuntoGuardia());
				confConjuntoGuardiasAdm.delete(confConjuntoGuardiasForm2.getConfConjuntoGuardiaVO());
				
			}
			
		}
		if(guardiasInsertar!=null && !guardiasInsertar.equals("")){
			List<ConfConjuntoGuardiasForm> listConfGuardiasInsertar = getDatosConfGuardias(guardiasInsertar);
			for (ConfConjuntoGuardiasForm confConjuntoGuardiasForm2 : listConfGuardiasInsertar) {
				confConjuntoGuardiasForm2.setIdInstitucion(confConjuntoGuardiasForm.getIdInstitucion());
				confConjuntoGuardiasForm2.setIdConjuntoGuardia(confConjuntoGuardiasForm.getIdConjuntoGuardia());
				try {
					confConjuntoGuardiasAdm.insert(confConjuntoGuardiasForm2.getConfConjuntoGuardiaVO());
				} catch (Exception e) {
					throw new SIGAException("gratuita.calendarios.incompatibilidad.orden");
				}
			}
		}
	}
	
	public List<ConfConjuntoGuardiasForm> getDatosConfGuardias(String datos){
		List<ConfConjuntoGuardiasForm> listConfGuardias= new ArrayList<ConfConjuntoGuardiasForm>();
		GstStringTokenizer st = new GstStringTokenizer(datos, "##");
		ConfConjuntoGuardiasForm  confConjuntoGuardiasForm = null;
		while (st.hasMoreTokens()) {
			String dupla = st.nextToken();
			if(dupla.equals(""))
				break;
			String parametros[] = dupla.split(",");
			confConjuntoGuardiasForm = new ConfConjuntoGuardiasForm();
			for (int i = 0; i < parametros.length; i++) {
				String parametro = parametros[i];
				String d[] = parametro.split("==");
				if (d.length > 1){
					 String key = d[0];
					 String value = d[1];
					 if(key.equals("idTurno"))
						 confConjuntoGuardiasForm.setIdTurno(value);
					 else if(key.equals("idGuardia")){
						 confConjuntoGuardiasForm.setIdGuardia(value);
						 
					 }else if(key.equals("orden")){
						 confConjuntoGuardiasForm.setOrden(value);
						 
					 }
					
				}
				
			}
			listConfGuardias.add(confConjuntoGuardiasForm);
			
		}
		return listConfGuardias;

	}
	
	public List<ProgrCalendariosForm> getProgramacionCalendarios(
			ProgrCalendariosForm progrCalendariosForm, UsrBean usrBean)
			throws ClsExceptions {
		ScsProgrCalendariosAdm  progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
		List<ProgrCalendariosForm> lista = progrCalendariosAdm.getProgrCalendarios(progrCalendariosForm);
		return lista;
	}
	
	public void insertaProgrCalendarios(ProgrCalendariosForm progrCalendariosForm, UsrBean usrBean)	throws ClsExceptions {
		ScsProgCalendariosBean progCalendariosBean = progrCalendariosForm.getProgCalendariosVO();
		ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
		Long idProgrCalendarios = progrCalendariosAdm.getNewIdProgrCalendarios();
		progCalendariosBean.setIdProgrCalendario(idProgrCalendarios);
		progrCalendariosAdm.insert(progCalendariosBean);
		
	}
	public ScsProgCalendariosBean getProgrCalendario(ProgrCalendariosForm progrCalendariosForm, UsrBean usrBean)throws ClsExceptions {
		ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
		ScsProgCalendariosBean progCalendariosBean = progrCalendariosAdm.getProgrCalendario(progrCalendariosForm);
		return progCalendariosBean;
	}
	public void modificaProgrCalendarios(
			ProgrCalendariosForm progrCalendariosForm, UsrBean usrBean)
			throws ClsExceptions {
		ScsProgCalendariosBean progCalendariosBean = progrCalendariosForm.getProgCalendariosVO();
		ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
		progrCalendariosAdm.updateDirect(progCalendariosBean);
		
	}
	
	public List<HcoConfProgrCalendarioForm> getHcoProgrCalendarios(ProgrCalendariosForm progrCalendariosForm ,UsrBean usrBean) throws ClsExceptions {
		ScsHcoConfProgrCalendariosAdm hcoConfProgrCalendariosAdm = new ScsHcoConfProgrCalendariosAdm(usrBean);
		List<HcoConfProgrCalendarioForm> lista = hcoConfProgrCalendariosAdm.getHcoConfProgrCalendario(progrCalendariosForm);
		return lista;
	}
	
	public void ejecutaProgrCalendarios(UsrBean usrBean)
			throws ClsExceptions, SIGAException {
		getBusinessManager().endTransaction();
		ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
		ScsHcoConfProgrCalendariosAdm hcoConfProgrCalendariosAdm = new ScsHcoConfProgrCalendariosAdm(usrBean);
		ScsProgCalendariosBean progCalendariosBean = progrCalendariosAdm.getNextProgrCalendario();
		//aalg. INC_09393_SIGA
		String textoAutomatico;
		if (progCalendariosBean != null){
			UsrBean usr = UsrBean.UsrBeanAutomatico(progCalendariosBean.getIdInstitucion().toString());
			textoAutomatico = UtilidadesString.getMensajeIdioma(usr, "gratuita.calendarios.generado.automatico") ;
		}
		else
			textoAutomatico = UtilidadesString.getMensajeIdioma(usrBean, "gratuita.calendarios.generado.automatico") ;
		if(progCalendariosBean!=null){
			ScsHcoConfProgCalendariosBean hcoConfProgCalendariosBean = null;
			try {
				if(progCalendariosBean.getEstado().equals(ScsProgCalendariosBean.estadoProgramado)){
					//Insertamos en el historico
					hcoConfProgrCalendariosAdm.insertar(progCalendariosBean.getIdProgrCalendario());
					progCalendariosBean.setEstado(ScsProgCalendariosBean.estadoProcesando);
					progrCalendariosAdm.updateEstado(progCalendariosBean);
					
				}else if(progCalendariosBean.getEstado().equals(ScsProgCalendariosBean.estadoReprogramado)){
					progCalendariosBean.setEstado(ScsProgCalendariosBean.estadoProcesando);
					progrCalendariosAdm.updateEstado(progCalendariosBean);
					
				}
				//Obtenemos la sigueinte guardia programada y no generada
				hcoConfProgCalendariosBean = hcoConfProgrCalendariosAdm.getNextGuardiaConfigurada(progCalendariosBean);
				if(hcoConfProgCalendariosBean!=null){
				
					//Le cambiamos el estado a procesando
					hcoConfProgCalendariosBean.setEstado(ScsHcoConfProgCalendariosBean.estadoProcesando);
					hcoConfProgrCalendariosAdm.updateEstado(hcoConfProgCalendariosBean);
					// El metodo crear calerndario nos creara los calendarios. Hay mas de uno ya que pueden tener guardias vincualdas
					CalendarioSJCS calendarioSJCS = new CalendarioSJCS();
					int idCalendario =  calendarioSJCS.crearCalendario(hcoConfProgCalendariosBean.getIdInstitucion(), hcoConfProgCalendariosBean.getIdTurno(),
							hcoConfProgCalendariosBean.getIdGuardia(), progCalendariosBean.getFechaCalInicio(), progCalendariosBean.getFechaCalFin(), textoAutomatico, null, null, null, usrBean);
					if(idCalendario<=0)
						throw new SIGAException("Error al crear el Calendario de guardias");
					
					calendarioSJCS.inicializaParaGenerarCalendario(hcoConfProgCalendariosBean.getIdInstitucion(),hcoConfProgCalendariosBean.getIdTurno(),
							hcoConfProgCalendariosBean.getIdGuardia(),new Integer(idCalendario), progCalendariosBean.getFechaCalInicio(),progCalendariosBean.getFechaCalFin(), usrBean);
					calendarioSJCS.generarCalendario();
					hcoConfProgCalendariosBean.setEstado(ScsHcoConfProgCalendariosBean.estadoFinalizado);
					hcoConfProgrCalendariosAdm.updateEstado(hcoConfProgCalendariosBean);
					
					
				}
				//si ya no quedan guardias pendientes de esta programacion la ponemos en estado finalizada
				if(hcoConfProgrCalendariosAdm.getNextGuardiaConfigurada(progCalendariosBean)==null){
					progCalendariosBean.setEstado(ScsProgCalendariosBean.estadoFinalizado);
					progrCalendariosAdm.updateEstado(progCalendariosBean);
				}

			} catch (Exception e) {

				if(hcoConfProgCalendariosBean!=null){
					//Cancelamos
					hcoConfProgrCalendariosAdm.cancelarHcoCalendarios(progCalendariosBean);
					hcoConfProgCalendariosBean.setEstado(ScsHcoConfProgCalendariosBean.estadoError);
					hcoConfProgrCalendariosAdm.updateEstado(hcoConfProgCalendariosBean);
					
				}
				progCalendariosBean.setEstado(ScsProgCalendariosBean.estadoError);
				progrCalendariosAdm.updateEstado(progCalendariosBean);
			}
			
		}
	}
	
	public void adelantarProgrCalendarios(
			ProgrCalendariosForm progrCalendariosForm, UsrBean usrBean)
			throws ClsExceptions {
		ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
		ScsProgCalendariosBean progCalendariosBean = progrCalendariosAdm.getProgrCalendario(progrCalendariosForm);
		progCalendariosBean.setFechaProgramacion("sysdate");
		progrCalendariosAdm.updateDirect(progCalendariosBean);
		
	}
	public void reprogramarCalendarios(
			ProgrCalendariosForm progrCalendariosForm, UsrBean usrBean)
			throws ClsExceptions {
		ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
		ScsProgCalendariosBean progCalendariosBean = progrCalendariosAdm.getProgrCalendario(progrCalendariosForm);
		progrCalendariosAdm.reprogramarCalendarios(progCalendariosBean);
		
	}
	public void cancelarGeneracionCalendarios(
			ProgrCalendariosForm progrCalendariosForm, UsrBean usrBean)
			throws ClsExceptions {
		ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
		ScsProgCalendariosBean progCalendariosBean = progrCalendariosAdm.getProgrCalendario(progrCalendariosForm);
		progrCalendariosAdm.cancelarGeneracionCalendarios(progCalendariosBean);
		
	}
	public void procesarAutomaticamenteGeneracionCalendarios()throws Exception{
		if (isAlguienEjecutando()){
			ClsLogging.writeFileLogWithoutSession("YA SE ESTA EJECUTANDO LA GENERACION DE CALENDARIOS EN BACKGROUND. CUANDO TERMINE SE INICIARA OTRA VEZ EL PROCESO.", 3);
			//ClsLogging.writeFileLogError("gratuita.eejg.message.isAlguienEjecutando",new SchedulerException("gratuita.eejg.message.isAlguienEjecutando"), 3);
			setAlgunaEjecucionDenegada();
			return;
		}
		UsrBean usr = UsrBean.UsrBeanAutomatico("2000");
		try {
			
			ejecutaProgrCalendarios(usr);
			ejecutaProgrCalendariosFicheroCarga(usr);

		} catch(Exception e){
			throw e;
		}
		finally {
			setNadieEjecutando();
			if(isAlgunaEjecucionDenegada()){
				setNingunaEjecucionDenegada();
				ejecutaProgrCalendarios(usr);
				ejecutaProgrCalendariosFicheroCarga(usr);

			}
		}
	}
	public boolean isAlguienEjecutando(){
		synchronized(AtosProgramacionCalendariosService.alguienEjecutando){
			if (!AtosProgramacionCalendariosService.alguienEjecutando){
				AtosProgramacionCalendariosService.alguienEjecutando=Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}
	private void setNadieEjecutando(){
		synchronized(AtosProgramacionCalendariosService.alguienEjecutando){
			AtosProgramacionCalendariosService.alguienEjecutando=Boolean.FALSE;
		}
	}
	private boolean isAlgunaEjecucionDenegada(){
		synchronized(AtosProgramacionCalendariosService.algunaEjecucionDenegada){
			if (!AtosProgramacionCalendariosService.algunaEjecucionDenegada){
				AtosProgramacionCalendariosService.algunaEjecucionDenegada=Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}

	private void setNingunaEjecucionDenegada(){
		synchronized(AtosProgramacionCalendariosService.algunaEjecucionDenegada){
			AtosProgramacionCalendariosService.algunaEjecucionDenegada=Boolean.FALSE;
		}
	}
	private void setAlgunaEjecucionDenegada(){
		synchronized(AtosProgramacionCalendariosService.algunaEjecucionDenegada){
			AtosProgramacionCalendariosService.algunaEjecucionDenegada=Boolean.TRUE;
		}
	}
	public void borrarHcoProgramacion(
			HcoConfProgrCalendarioForm hcoConfProgrCalendarioForm,
			UsrBean usrBean) throws ClsExceptions {
		ScsHcoConfProgCalendariosBean hcoConfProgCalendariosBean = hcoConfProgrCalendarioForm.getHcoConfProgCalendariosVO();
		ScsHcoConfProgrCalendariosAdm hcoConfProgrCalendariosAdm = new ScsHcoConfProgrCalendariosAdm(usrBean);
		hcoConfProgrCalendariosAdm.delete(hcoConfProgCalendariosBean);
		
	}
	public void borrarProgrCalendarios(
			ProgrCalendariosForm progrCalendariosForm, UsrBean usrBean)
			throws ClsExceptions, SIGAException {
		
		CalendarioSJCS calendarioSJCS = new CalendarioSJCS();
		ScsCalendarioGuardiasAdm calendarioGuardiasAdm = new ScsCalendarioGuardiasAdm(usrBean);
		ScsProgCalendariosBean progCalendariosBean = progrCalendariosForm.getProgCalendariosVO();
		 List<ScsCalendarioGuardiasBean> calendarioGuardiasBeans=calendarioGuardiasAdm.getCalendariosProgramados(progCalendariosBean);
		for (ScsCalendarioGuardiasBean calendarioGuardiasBean : calendarioGuardiasBeans) {
			calendarioSJCS.inicializaParaBorrarCalendarios(calendarioGuardiasBean.getIdInstitucion(), calendarioGuardiasBean.getIdTurno(), calendarioGuardiasBean.getIdGuardia(), calendarioGuardiasBean.getIdCalendarioGuardias(), usrBean);
			calendarioSJCS.borrarCalendario();
			
		}
		ScsHcoConfProgrCalendariosAdm hcoConfProgrCalendariosAdm = new ScsHcoConfProgrCalendariosAdm(usrBean);
		 
		hcoConfProgrCalendariosAdm.delete(progCalendariosBean);
		ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
		progrCalendariosAdm.delete(progCalendariosBean);
		
		
	}
	public void borrarConjuntoGuardias(ConjuntoGuardiasForm ConjuntoGuardiasForm,
			UsrBean usrBean) throws ClsExceptions {
		ScsConjuntoGuardiasAdm ConjuntoGuardiasAdm = new ScsConjuntoGuardiasAdm(usrBean);
		ConjuntoGuardiasAdm.delete(ConjuntoGuardiasForm.getConjuntoGuardiaVO());
	}
	public List<ScsTurnoBean> getTurnos(String idInstitucion,UsrBean usrBean) throws ClsExceptions {
		ScsTurnoAdm admTurnos = new ScsTurnoAdm(usrBean);
		List<ScsTurnoBean> alTurnos = admTurnos.getTurnos(idInstitucion);
		if(alTurnos==null){
			alTurnos = new ArrayList<ScsTurnoBean>();
		}else{
			//Sustituimos la primera linea por "" en la descripcion ya que nos admite nulos
			if(alTurnos.size()>0){
				ScsTurnoBean turnoBean = new ScsTurnoBean();
    			turnoBean.setNombre("");
    			turnoBean.setIdTurno(new Integer(-1));
				alTurnos.set(0, turnoBean);
			}
			
		}
		return alTurnos;
	}
	public List<ScsGuardiasTurnoBean> getGuardiasTurnos(Integer idTurno,
			Integer idInstitucion, boolean b,UsrBean usrBean) throws ClsExceptions {
		ScsGuardiasTurnoAdm admGuardias = new ScsGuardiasTurnoAdm(usrBean);
		List<ScsGuardiasTurnoBean> alGuardias = admGuardias.getGuardiasTurnos(idTurno,idInstitucion,true);//admGuardias.getGuardiasTurnos(new Integer(miForm.getIdTurno()),new Integer(miForm.getIdInstitucion()),true);
	
		if(alGuardias==null)
			alGuardias = new ArrayList<ScsGuardiasTurnoBean>();
		else{
			
			ScsGuardiasTurnoBean guardiaBean = new ScsGuardiasTurnoBean();
			guardiaBean.setIdGuardia(new Integer(-1));
			guardiaBean.setNombre("");
			if(alGuardias.size()==1)
				alGuardias.add(0, guardiaBean);
			else
				alGuardias.set(0, guardiaBean);
			
		}
		return alGuardias;
	}
	public List<DefinirCalendarioGuardiaForm> getCalendarios(
			DefinirCalendarioGuardiaForm calendarioGuardiaForm, UsrBean usrBean) throws ClsExceptions{
		ScsCalendarioGuardiasAdm calendarioGuardiasAdm = new ScsCalendarioGuardiasAdm(usrBean);
		List<DefinirCalendarioGuardiaForm> calendarioGuardiaForms = calendarioGuardiasAdm.getCalendarios(calendarioGuardiaForm);
		return calendarioGuardiaForms;		
	}
	
	public void insertaProgrCalendariosFicheroCarga(int idFicheroCalendario, int idInstitucion, UsrBean usrBean) throws ClsExceptions {
		ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
		Calendar myCalendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ClsConstants.DATE_FORMAT_JAVA);
		ScsProgCalendariosBean progCalendariosBean = new ScsProgCalendariosBean();
		Long idProgrCalendarios = progrCalendariosAdm.getNewIdProgrCalendarios();
		progCalendariosBean.setIdProgrCalendario(idProgrCalendarios);
		progCalendariosBean.setIdConjuntoGuardia(new Long(0));
		progCalendariosBean.setIdInstitucion(idInstitucion);
		progCalendariosBean.setEstado(new Short("0"));
		progCalendariosBean.setFechaProgramacion(simpleDateFormat.format(myCalendar.getTime()));
		progCalendariosBean.setFechaCalInicio("SYSDATE");
		progCalendariosBean.setFechaCalFin("SYSDATE");
		progCalendariosBean.setIdFicheroCalendario(idFicheroCalendario);

		progrCalendariosAdm.insert(progCalendariosBean);
	}
	
	public void ejecutaProgrCalendariosFicheroCarga(UsrBean usrBean) throws ClsExceptions, SIGAException {
		getBusinessManager().endTransaction();
		ScsProgrCalendariosAdm progrCalendariosAdm = new ScsProgrCalendariosAdm(usrBean);
		ScsProgCalendariosBean progCalendariosBean = progrCalendariosAdm.getNextProgrCalendarioFicheroCarga();
		LogFileWriter log = null;
		List <ArrayList<String>> fileLog = new ArrayList<ArrayList<String>>();
		UserTransaction tx = null;
		
		if(progCalendariosBean!=null){
			try {
				if(progCalendariosBean.getEstado().equals(ScsProgCalendariosBean.estadoProgramado)){
					//Insertamos en el historico
					progCalendariosBean.setEstado(ScsProgCalendariosBean.estadoProcesando);
					progrCalendariosAdm.updateEstado(progCalendariosBean);
					
				}else if(progCalendariosBean.getEstado().equals(ScsProgCalendariosBean.estadoReprogramado)){
					progCalendariosBean.setEstado(ScsProgCalendariosBean.estadoProcesando);
					progrCalendariosAdm.updateEstado(progCalendariosBean);
				}
				
				//Obtenemos la siguiente guardia programada y no generada
				if(progCalendariosBean.getEstado().equals(ScsProgCalendariosBean.estadoProcesando)){				
					BusinessManager bm = getBusinessManager();
					
					/** Creamos el regstro del Log **/
					ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
					log = LogFileWriter.getLogFileWriter(getDirectorioFicheroLog(progCalendariosBean.getIdInstitucion()), "LOG_" + progCalendariosBean.getIdInstitucion() + "_" + progCalendariosBean.getIdFicheroCalendario());
					log.clear();				
					
					/** Obtenemos el documento **/
					FicherosService ficherosService = (FicherosService)bm.getService(FicherosService.class);
					FicheroVo ficheroVo = new FicheroVo();
					ficheroVo.setIdfichero(progCalendariosBean.getIdFicheroCalendario().longValue());
					ficheroVo.setIdinstitucion(progCalendariosBean.getIdInstitucion().shortValue());
					ficheroVo = ficherosService.getFichero(ficheroVo);
					
					if (ficheroVo != null) {
						StringBuffer pathFichero = new StringBuffer(ficheroVo.getDirectorio());
						pathFichero.append(File.separator);
						pathFichero.append(ficheroVo.getIdinstitucion() );
						pathFichero.append("_");
						pathFichero.append(ficheroVo.getIdfichero());
						pathFichero.append(".");
						pathFichero.append(ficheroVo.getExtension());
						File ficheroCargaExcel = new File(pathFichero.toString());	
						if(ficheroCargaExcel != null && ficheroCargaExcel.length() > 0){
							CargaMasivaCalendariosService carga = (CargaMasivaCalendariosService) bm.getService(CargaMasivaCalendariosService.class);
							ClsLogging.writeFileLogWithoutSession("PARSEO DEL FICHERO CALENDARIO "+ progCalendariosBean.getIdInstitucion() + "_" + progCalendariosBean.getIdFicheroCalendario(), 3);
							List<CargaMasivaCalendariosVo> datosExcel = carga.parseExcelFile(SIGAServicesHelper.getBytes(ficheroCargaExcel), progCalendariosBean.getIdInstitucion().shortValue(),fileLog);
							// Si no ha ocurrido errores en la carga del excel eliminamos el fichero log 
							fileLog=null;
							
							String observacion = "";
							if(ficheroVo.getDescripcion()!=null){
								observacion = ficheroVo.getDescripcion();
							}
							
							/** Generamos el calendario **/	
							tx = usrBean.getTransactionPesada();
							tx.begin();
											
							ClsLogging.writeFileLogWithoutSession("GENERACION DEL CALENDARIO "+progCalendariosBean.getIdInstitucion() + "_" + progCalendariosBean.getIdFicheroCalendario(), 3);
							CalendarioSJCS calendarioSJCS = new CalendarioSJCS();
							calendarioSJCS.generarCalendarioCargaFichero(datosExcel,observacion,usrBean);
							
							/** ACTUALIZAMOS LOS DATOS DEL CALENDARIO QUE SE VAN A MOSTRAR EN EL LISTADO (FECHAS Y ESTADO)**/
							SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
							String primerDia = sdf2.format(datosExcel.get(0).getFechaInicioCalendario());
							String ultimoDia = sdf2.format(datosExcel.get(datosExcel.size() - 1).getFechaInicioCalendario());							
							progCalendariosBean.setFechaCalInicio(GstDate.getApplicationFormatDate("", primerDia));
							progCalendariosBean.setFechaCalFin(GstDate.getApplicationFormatDate("", ultimoDia));
							progCalendariosBean.setEstado(ScsProgCalendariosBean.estadoFinalizado);
							progrCalendariosAdm.updateDirect(progCalendariosBean);							
							ClsLogging.writeFileLogWithoutSession("CALENDARIO GENERADO"+progCalendariosBean.getIdInstitucion() + "_" + progCalendariosBean.getIdFicheroCalendario(), 3);
							tx.commit();
						}
					}
				}

			} catch (Exception e) {
				//ESTADO ERROR
				try {
					if (tx!=null && Status.STATUS_ACTIVE == tx.getStatus()){
						tx.rollback();
					}
					
					ClsLogging.writeFileLogWithoutSession("ERROR EN CALENDARIO: "+ progCalendariosBean.getIdInstitucion() + "_" + progCalendariosBean.getIdFicheroCalendario(), 3);
					progCalendariosBean.setEstado(ScsProgCalendariosBean.estadoError);
					progrCalendariosAdm.updateEstado(progCalendariosBean);
					String msgError = e.getMessage();
					if(e instanceof SIGAException){
						msgError = ((SIGAException) e).getLiteral();
					} else if (e instanceof ClsExceptions){
						ClsExceptions d = ((ClsExceptions) e);
						msgError = d.getMessage();
					}
					
					/** Escribimos el log de cada fila del fichero de carga si hubiera habido errores **/
					log.addLog(new String[] {"ERROR OCURRIDO: ", msgError});
					if(fileLog!=null){
						for (ArrayList<String> lineLog : fileLog) {
							log.addLog(lineLog);
						}
					}
					log.flush();					
					
				} catch (IllegalStateException e1) {
					e1.printStackTrace();
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (SystemException e1) {
					e1.printStackTrace();
				}			
			}
		}
	}		

	private String getDirectorioFicheroLog(Integer idInstitucion) {
		ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String pathFicheros = rp.returnProperty("gen.ficheros.path");
		StringBuffer directorioFichero = new StringBuffer(pathFicheros);
		directorioFichero.append(idInstitucion);
		directorioFichero.append(File.separator);
		String directorio = rp.returnProperty("scs.ficheros.ficheroCalendario");
		directorioFichero.append(directorio);
		return directorioFichero.toString();
	}	
	
}
