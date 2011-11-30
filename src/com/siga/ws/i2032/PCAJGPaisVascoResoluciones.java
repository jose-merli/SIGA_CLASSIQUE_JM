package com.siga.ws.i2032;

import com.siga.ws.i2032.axis.ejg.EnvioSolicitudesImplPortBindingStub;
import com.siga.ws.i2032.axis.ejg.EnvioSolicitudesImplServiceLocator;
import com.siga.ws.i2032.axis.ejg.ObtResolucionesEnt;

public class PCAJGPaisVascoResoluciones extends PCAJGPaisVascoComun {

	@Override
	public void execute() throws Exception {
		EnvioSolicitudesImplServiceLocator locator;
		EnvioSolicitudesImplPortBindingStub stub;
		
		String mensajeLog = "Recepción webservice de las resoluciones del colegio " + getIdInstitucion() + " de la remesa " + getIdRemesa();
		
		locator = new EnvioSolicitudesImplServiceLocator(createClientConfig(getUsrBean(), String.valueOf(getIdInstitucion()), mensajeLog));
		stub = new EnvioSolicitudesImplPortBindingStub(new java.net.URL(getUrlWS()), locator);
		
		ObtResolucionesEnt obtResolucionesEnt = new ObtResolucionesEnt();
//		obtResolucionesEnt.set
		stub.obtenerResoluciones(obtResolucionesEnt);
		
	}

}
