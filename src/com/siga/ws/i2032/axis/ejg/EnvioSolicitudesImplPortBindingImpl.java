package com.siga.ws.i2032.axis.ejg;

import java.rmi.RemoteException;

public class EnvioSolicitudesImplPortBindingImpl  implements EnvioSolicitudesImpl
{

    public EnvioSolicitudesImplPortBindingImpl()
    {
    }

    public RemSolicitudesSal remitirDatosSolicitudes(RemSolicitudesEnt remSolicitudesEnt) throws RemoteException {
    	RemSolicitudesSal remSolicitudesSal = new RemSolicitudesSal();
    	SolicitudesRemitidas solicitudesRemitidas = new SolicitudesRemitidas();
    	//00 no hay error general
    	solicitudesRemitidas.setErrorGeneral("00");
    	
    	remSolicitudesSal.setSolicitudes(solicitudesRemitidas);    	
        return remSolicitudesSal;
    }

    public AnexDocumentosSal anexarDocumentos(AnexDocumentosEnt arg0)
        throws RemoteException
    {
        return null;
    }

    public ObtResolucionesSal obtenerResoluciones(ObtResolucionesEnt arg0)
        throws RemoteException
    {
        return null;
    }
}
