package com.siga.ws.i2055;

import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;

import com.siga.general.SIGAListenerAbstract;

public class ResolucionesListener extends SIGAListenerAbstract {

	@Override
	protected void execute(Short idInstitucion) {				
		ResolucionesAsigna resolucionesAsigna = new ResolucionesAsigna();
		resolucionesAsigna.obtenerResoluciones(String.valueOf(idInstitucion));
	}

	@Override
	protected PARAMETRO getActivoParam() {		
		return PARAMETRO.PCAJG_ASIGNA_RESOL_ACTIVO;
	}

	@Override
	protected PARAMETRO getFechaHoraInicioParam() {		
		return PARAMETRO.PCAJG_ASIGNA_DIA_HORA_CONSULTA_RESOLUCIONES;
	}

	@Override
	protected PARAMETRO getDiasIntervaloParam() {		
		return PARAMETRO.PCAJG_ASIGNA_INTERVALO_DIAS_RESOLUCIONES;
	}

	@Override
	protected MODULO getModulo() {
		return MODULO.SCS;
	}

}