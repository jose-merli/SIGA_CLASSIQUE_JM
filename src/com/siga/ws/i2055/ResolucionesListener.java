package com.siga.ws.i2055;

import com.siga.general.SIGAListenerAbstract;

public class ResolucionesListener extends SIGAListenerAbstract {

	@Override
	protected void execute(Short idInstitucion) {				
		ResolucionesAsigna resolucionesAsigna = new ResolucionesAsigna();
		resolucionesAsigna.obtenerResoluciones(String.valueOf(idInstitucion));
	}

	@Override
	protected String getActivoParam() {		
		return "PCAJG_ASIGNA_RESOL_ACTIVO";
	}

	@Override
	protected String getFechaHoraInicioParam() {		
		return "PCAJG_ASIGNA_DIA_HORA_CONSULTA_RESOLUCIONES";
	}

	@Override
	protected String getDiasIntervaloParam() {		
		return "PCAJG_ASIGNA_INTERVALO_DIAS_RESOLUCIONES";
	}

}