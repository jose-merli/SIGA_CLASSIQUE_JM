package com.siga.ws.cen;

import org.apache.log4j.Logger;
import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;
import org.redabogacia.sigaservices.app.services.ecom.EcColaService;
import org.redabogacia.sigaservices.app.services.ecom.EcomColaService;
import org.redabogacia.sigaservices.app.services.gen.GenParametrosService;

import com.siga.general.SIGAListenerAbstract;

import es.satec.businessManager.BusinessManager;

public class CargaCensoWSListener extends SIGAListenerAbstract {
	
	private static final Logger log = Logger.getLogger(CargaCensoWSListener.class);

	@Override
	protected PARAMETRO getFechaHoraInicioParam() {		
		return PARAMETRO.CEN_WS_CARGA_DIA_HORA;
	}

	@Override
	protected PARAMETRO getDiasIntervaloParam() {
		return PARAMETRO.CEN_WS_CARGA_INTERVALO_DIAS;
	}

	@Override
	protected PARAMETRO getActivoParam() {
		return PARAMETRO.CEN_WS_CARGA_ACTIVO;
	}

	@Override
	protected MODULO getModulo() {
		return MODULO.CEN;
	}

	@Override
	protected void execute(Short idInstitucion) {
		try {
			
			GenParametrosService genParametrosService = (GenParametrosService) BusinessManager.getInstance().getService(GenParametrosService.class);
			String activo = genParametrosService.getValorParametro(AppConstants.IDINSTITUCION_2000, PARAMETRO.CEN_WS_PROXY_ACTIVO, MODULO.CEN);
			
			if(AppConstants.DB_TRUE.equals(activo)){
				EcColaService ecColaService = (EcColaService) BusinessManager.getInstance().getService(EcColaService.class);
				if (ecColaService.insertaColaCargaCenso(idInstitucion, true) != 1) {
					throw new Exception("No se ha podido insertar correctamente en la cola de carga de censoWS para el colegio " + idInstitucion);
				}
			}else{
				EcomColaService ecomColaService = (EcomColaService) BusinessManager.getInstance().getService(EcomColaService.class);
				if (ecomColaService.insertaColaCargaCenso(idInstitucion, true) != 1) {
					throw new Exception("No se ha podido insertar correctamente en la cola de carga de censo para el colegio " + idInstitucion);
				}
			}			
		} catch (Exception e) {
			log.error("Error al insertar en la cola para el colegio " + idInstitucion, e);
		}
	}

}
