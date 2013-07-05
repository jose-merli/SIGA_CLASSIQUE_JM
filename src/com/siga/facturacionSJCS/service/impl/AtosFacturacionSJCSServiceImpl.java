package com.siga.facturacionSJCS.service.impl;

import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.beans.CenInstitucionAdm;
import com.siga.beans.CenInstitucionBean;
import com.siga.beans.FcsFacturacionJGAdm;
import com.siga.facturacion.Facturacion;
import com.siga.facturacionSJCS.service.FacturacionSJCSService;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessException;
import es.satec.businessManager.BusinessManager;
import es.satec.businessManager.template.JtaBusinessServiceTemplate;
/**
 * 
 * @author jorgeta 
 * @date   02/07/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class AtosFacturacionSJCSServiceImpl extends JtaBusinessServiceTemplate 
	implements FacturacionSJCSService {
	private static Boolean alguienEjecutando=Boolean.FALSE;
	private static Boolean algunaEjecucionDenegada=Boolean.FALSE;
	public AtosFacturacionSJCSServiceImpl(BusinessManager businessManager) {
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

	
	public void procesarAutomaticamenteFacturacionSJCS()throws Exception{
		if (isAlguienEjecutando()){
			ClsLogging.writeFileLogWithoutSession("YA SE ESTA EJECUTANDO LA FACTURACIÓN SJCS EN BACKGROUND. CUANDO TERMINE SE INICIARA OTRA VEZ EL PROCESO.", 3);
			//ClsLogging.writeFileLogError("gratuita.eejg.message.isAlguienEjecutando",new SchedulerException("gratuita.eejg.message.isAlguienEjecutando"), 3);
			setAlgunaEjecucionDenegada();
			return;
		}
		try {
			getBusinessManager().endTransaction();
			procesarFacturacionSJCS();

		} catch(Exception e){
			throw e;
		}
		finally {
			setNadieEjecutando();
			if(isAlgunaEjecucionDenegada()){
				setNingunaEjecucionDenegada();
				procesarFacturacionSJCS();

			}
		}
	}
	public boolean isAlguienEjecutando(){
		synchronized(AtosFacturacionSJCSServiceImpl.alguienEjecutando){
			if (!AtosFacturacionSJCSServiceImpl.alguienEjecutando){
				AtosFacturacionSJCSServiceImpl.alguienEjecutando=Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}
	private void setNadieEjecutando(){
		synchronized(AtosFacturacionSJCSServiceImpl.alguienEjecutando){
			AtosFacturacionSJCSServiceImpl.alguienEjecutando=Boolean.FALSE;
		}
	}
	private boolean isAlgunaEjecucionDenegada(){
		synchronized(AtosFacturacionSJCSServiceImpl.algunaEjecucionDenegada){
			if (!AtosFacturacionSJCSServiceImpl.algunaEjecucionDenegada){
				AtosFacturacionSJCSServiceImpl.algunaEjecucionDenegada=Boolean.TRUE;
				return false;
			} else {
				return true;
			}
		}
	}

	private void setNingunaEjecucionDenegada(){
		synchronized(AtosFacturacionSJCSServiceImpl.algunaEjecucionDenegada){
			AtosFacturacionSJCSServiceImpl.algunaEjecucionDenegada=Boolean.FALSE;
		}
	}
	private void setAlgunaEjecucionDenegada(){
		synchronized(AtosFacturacionSJCSServiceImpl.algunaEjecucionDenegada){
			AtosFacturacionSJCSServiceImpl.algunaEjecucionDenegada=Boolean.TRUE;
		}
	}
	
	
	public void procesarFacturacionSJCS(){
		boolean ejecutarOtraVez;

		try {
			CenInstitucionAdm admInstitucion = new CenInstitucionAdm(
					new UsrBean()); // Este usrbean esta controlado que no se
									// necesita el valor

			Vector vInstituciones = admInstitucion.obtenerInstitucionesAlta();

			Facturacion fac = null;
			if (vInstituciones != null) {
				CenInstitucionBean beanInstitucion = null;
				//esto es para controla que no queden facturas en estado PROGRAMADAS en ningun colegio.
				ejecutarOtraVez = false;
				do {
					for (int i = 0; i < vInstituciones.size(); i++) {
						beanInstitucion = (CenInstitucionBean) vInstituciones
								.elementAt(i);
						UsrBean usr = UsrBean.UsrBeanAutomatico(beanInstitucion
								.getIdInstitucion().toString());
						ClsLogging.writeFileLogWithoutSession(
								"INICIO proceso FacturacionSJCS  "+beanInstitucion.getNombre(), 3);

						
						try {
							FcsFacturacionJGAdm facAdm = new FcsFacturacionJGAdm(
									usr);
							/**
							 * Aqui se controla que no se queden facturas en
							 * estado Programadas
							 **/
							ejecutarOtraVez = facAdm
									.facturacionesSJCSProgramadas(""
											+ beanInstitucion
													.getIdInstitucion(), usr);
							if (ejecutarOtraVez) break;
							ClsLogging.writeFileLogWithoutSession(
									" FIN proceso FacturacionSJCS  "+beanInstitucion.getNombre(), 3);

						} catch (Exception e) {
							if (beanInstitucion != null
									&& beanInstitucion.getIdInstitucion() != null)
								ClsLogging.writeFileLogError(
										" ---------- ERROR EJECUCION FACTURACION SJCS. INSTITUCION: "
												+ beanInstitucion
														.getIdInstitucion(), e,
										3);
							else
								ClsLogging
										.writeFileLogError(
												" ---------- ERROR EJECUCION FACTURACION SJCS.",
												e, 3);
						}

					}
				} while (ejecutarOtraVez);
			}

			ClsLogging.writeFileLogWithoutSession(
					" OK procesarFacturacionSJCS ", 3);

		}catch (Exception e) {

			ClsLogging.writeFileLogWithoutSession("ProcesarFacturacionSJCS ejecutada ERROR. : " + e.toString(),	3);
		}

	}

	
	
}
