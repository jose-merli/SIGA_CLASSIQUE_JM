package com.siga.ws.i2055;

import com.atos.utils.UsrBean;
import com.siga.general.SIGAListenerAbstract;

public class DesignacionProcuradorListener extends SIGAListenerAbstract {
	
	private static enum CONFIG {
		PCAJG_ASIGNA_PROCURADOR_ACTIVO,
		PCAJG_ASIGNA_INTERVALO_DIAS_DESIGNACIONPROCURADOR,
		PCAJG_ASIGNA_DIA_HORA_CONSULTA_DESIGNACIONPROCURADOR		
	}

	@Override
	protected void execute(UsrBean usrBean, String idInstitucionSt) throws Exception {
		DesignacionProcuradorAsigna designacionProcuradorAsigna = new DesignacionProcuradorAsigna();
		Integer idInstitucion = null;
		if (idInstitucionSt != null) {
			idInstitucion = Integer.valueOf(idInstitucionSt);
		}
		designacionProcuradorAsigna.obtenerDesignaciones(usrBean, idInstitucion);
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
