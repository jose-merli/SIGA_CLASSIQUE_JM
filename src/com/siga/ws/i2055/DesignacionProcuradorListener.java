package com.siga.ws.i2055;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.siga.general.SIGAListenerAbstract;

public class DesignacionProcuradorListener extends SIGAListenerAbstract {
	
	private static enum CONFIG {
		PCAJG_ASIGNA_PROCURADOR_ACTIVO,
		PCAJG_ASIGNA_INTERVALO_DIAS_DESIGNACIONPROCURADOR,
		PCAJG_ASIGNA_DIA_HORA_CONSULTA_DESIGNACIONPROCURADOR		
	}

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
	protected String getActivoParam() {
		return CONFIG.PCAJG_ASIGNA_PROCURADOR_ACTIVO.name();
	}

	@Override
	protected String getDiasIntervaloParam() {		
		return CONFIG.PCAJG_ASIGNA_INTERVALO_DIAS_DESIGNACIONPROCURADOR.name();
	}

	@Override
	protected String getFechaHoraInicioParam() {		
		return CONFIG.PCAJG_ASIGNA_DIA_HORA_CONSULTA_DESIGNACIONPROCURADOR.name();
	}

}
