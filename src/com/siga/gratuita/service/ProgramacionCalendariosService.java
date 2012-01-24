package com.siga.gratuita.service;

import java.util.List;

import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.beans.ScsGuardiasTurnoBean;
import com.siga.beans.ScsTurnoBean;
import com.siga.general.SIGAException;
import com.siga.gratuita.beans.ScsProgCalendariosBean;
import com.siga.gratuita.form.ConfConjuntoGuardiasForm;
import com.siga.gratuita.form.ConjuntoGuardiasForm;
import com.siga.gratuita.form.DefinirCalendarioGuardiaForm;
import com.siga.gratuita.form.HcoConfProgrCalendarioForm;
import com.siga.gratuita.form.ProgrCalendariosForm;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessService;

public interface ProgramacionCalendariosService extends BusinessService{
	public Object executeService(Object... parameters) throws BusinessException;
	public List<ConjuntoGuardiasForm> getConjuntosGuardia(String idInstitucion,UsrBean usrBean)throws ClsExceptions;
	public ConjuntoGuardiasForm getUltimoConjuntoGuardiaInsertado(String idInstitucion,UsrBean usrBean)throws ClsExceptions;
	public List<ConfConjuntoGuardiasForm> getConfiguracionConjuntoGuardias(ConfConjuntoGuardiasForm confConjuntoGuardiasForm,boolean isMostrarSoloGuardiasConfiguradas,UsrBean usrBean) throws ClsExceptions ;
	public void insertaConjuntoGuardias(ConjuntoGuardiasForm ConjuntoGuardiasForm,UsrBean usrBean)throws ClsExceptions, SIGAException;
	public void insertarConfiguracionConjuntoGuardias(ConfConjuntoGuardiasForm confConjuntoGuardiasForm, UsrBean usrBean)throws ClsExceptions, SIGAException;
	public List<ProgrCalendariosForm> getProgramacionCalendarios(ProgrCalendariosForm progrCalendariosForm,UsrBean usrBean) throws ClsExceptions ;
	public void insertaProgrCalendarios(ProgrCalendariosForm progrCalendariosForm, UsrBean usrBean)throws ClsExceptions;
	public ScsProgCalendariosBean getProgrCalendario(ProgrCalendariosForm progrCalendariosForm, UsrBean usrBean)throws ClsExceptions;
	public void modificaProgrCalendarios(ProgrCalendariosForm progrCalendariosForm, UsrBean usrBean)throws ClsExceptions;
	public List<HcoConfProgrCalendarioForm> getHcoProgrCalendarios(ProgrCalendariosForm progrCalendariosForm ,UsrBean usrBean) throws ClsExceptions;
//	public void ejecutaProgrCalendarios(UsrBean usrBean)throws ClsExceptions,SIGAException;
	public void adelantarProgrCalendarios(ProgrCalendariosForm progrCalendariosForm,UsrBean usrBean)throws ClsExceptions;
	public void reprogramarCalendarios(ProgrCalendariosForm progrCalendariosForm,UsrBean usrBean)throws ClsExceptions;
	public void cancelarGeneracionCalendarios(ProgrCalendariosForm progrCalendariosForm,UsrBean usrBean)throws ClsExceptions;
	public void procesarAutomaticamenteGeneracionCalendarios()throws Exception;
	public void borrarHcoProgramacion(	HcoConfProgrCalendarioForm hcoConfProgrCalendarioForm,	UsrBean usrBean)throws ClsExceptions;
	public void borrarProgrCalendarios(	ProgrCalendariosForm progrCalendariosForm, UsrBean usrBean)throws ClsExceptions, SIGAException;
	public void borrarConjuntoGuardias(ConjuntoGuardiasForm ConjuntoGuardiasForm,UsrBean usrBean)throws ClsExceptions;
	public List<ScsTurnoBean> getTurnos(String idInstitucion,UsrBean usrBean) throws ClsExceptions ;
	public List<ScsGuardiasTurnoBean> getGuardiasTurnos(Integer idTurno,Integer idInstitucion, boolean b,UsrBean usrBean) throws ClsExceptions ;
	public List<DefinirCalendarioGuardiaForm> getCalendarios(DefinirCalendarioGuardiaForm calendarioGuardiaForm, UsrBean usrBean)throws ClsExceptions;
	
	
	
	
	

	
}
