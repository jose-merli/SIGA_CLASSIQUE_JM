package com.siga.expedientes.service.impl;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.ExpExpedienteAdm;
import com.siga.expedientes.service.ExpedientesService;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.JtaBusinessServiceTemplate;

public class AtosExpedientesService extends JtaBusinessServiceTemplate 
	implements ExpedientesService  {
	private static Boolean alguienEjecutando=Boolean.FALSE;
	private static Boolean algunaEjecucionDenegada=Boolean.FALSE;
	public AtosExpedientesService(BusinessManager businessManager) {
		super(businessManager);
	}
	public Object executeService(Object... parameters) throws BusinessException {
		return null;
	}
	
	public void procesarAutomaticamenteComprobacionAlarmas()throws Exception{
		if (isAlguienEjecutando()){
			ClsLogging.writeFileLogWithoutSession("YA SE ESTA EJECUTANDO LA COMPROBACIÓN DE LAS ALARMAS DE EXPEDIENTES EN BACKGROUND. CUANDO TERMINE SE INICIARA OTRA VEZ EL PROCESO.", 3);
			//ClsLogging.writeFileLogError("gratuita.eejg.message.isAlguienEjecutando",new SchedulerException("gratuita.eejg.message.isAlguienEjecutando"), 3);
			setAlgunaEjecucionDenegada();
			return;
		}
		UsrBean usr = new UsrBean();
		try {
			
			comprobarAlarmas(usr);

		} catch(Exception e){
			throw e;
		}
		finally {
			setNadieEjecutando();
			if(isAlgunaEjecucionDenegada()){
				setNingunaEjecucionDenegada();
				comprobarAlarmas(usr);

			}
		}
	}
	private void comprobarAlarmas(UsrBean usr) throws ClsExceptions{
		getBusinessManager().endTransaction();
		ExpExpedienteAdm admExpediente = new ExpExpedienteAdm(usr); // Este usrbean esta controlado que no se necesita el valor
	    admExpediente.chequearAlarmas();
		
	}
	public boolean isAlguienEjecutando(){
		synchronized(AtosExpedientesService.alguienEjecutando){
			if (!AtosExpedientesService.alguienEjecutando){
				AtosExpedientesService.alguienEjecutando=Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}
	private void setNadieEjecutando(){
		synchronized(AtosExpedientesService.alguienEjecutando){
			AtosExpedientesService.alguienEjecutando=Boolean.FALSE;
		}
	}
	private boolean isAlgunaEjecucionDenegada(){
		synchronized(AtosExpedientesService.algunaEjecucionDenegada){
			if (!AtosExpedientesService.algunaEjecucionDenegada){
				AtosExpedientesService.algunaEjecucionDenegada=Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}

	private void setNingunaEjecucionDenegada(){
		synchronized(AtosExpedientesService.algunaEjecucionDenegada){
			AtosExpedientesService.algunaEjecucionDenegada=Boolean.FALSE;
		}
	}
	private void setAlgunaEjecucionDenegada(){
		synchronized(AtosExpedientesService.algunaEjecucionDenegada){
			AtosExpedientesService.algunaEjecucionDenegada=Boolean.TRUE;
		}
	}
	public Object executeService() throws SIGAException, ClsExceptions {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	

}
