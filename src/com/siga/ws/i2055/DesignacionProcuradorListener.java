package com.siga.ws.i2055;

import org.redabogacia.sigaservices.app.AppConstants.MODULO;
import org.redabogacia.sigaservices.app.AppConstants.PARAMETRO;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.siga.general.SIGAListenerAbstract;

public class DesignacionProcuradorListener extends SIGAListenerAbstract {
	
	
	@Override
	protected void execute(Short idInstitucion) {
		DesignacionProcuradorAsigna designacionProcuradorAsigna = new DesignacionProcuradorAsigna();		
		try {
			designacionProcuradorAsigna.obtenerDesignaciones(idInstitucion);
		} catch (ClsExceptions e) {
			ClsLogging.writeFileLogError("Error al insertar en la cola ecom", e, 3);
		}
	}

	@Override
	protected PARAMETRO getActivoParam() {
		return PARAMETRO.PCAJG_ASIGNA_PROCURADOR_ACTIVO;
	}

	@Override
	protected PARAMETRO getDiasIntervaloParam() {		
		return PARAMETRO.PCAJG_ASIGNA_INTERVALO_DIAS_DESIGNACIONPROCURADOR;
	}

	@Override
	protected PARAMETRO getFechaHoraInicioParam() {		
		return PARAMETRO.PCAJG_ASIGNA_DIA_HORA_CONSULTA_DESIGNACIONPROCURADOR;
	}
	
	@Override
	protected MODULO getModulo() {
		return MODULO.SCS;
	}

}
