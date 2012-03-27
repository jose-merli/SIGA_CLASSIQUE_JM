/**
 * ServiceSoap_BindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.siga.ws.i2055;

import java.util.Calendar;

public class ServiceSoap_BindingImpl implements com.siga.ws.i2055.ServiceSoap_PortType{
    public com.siga.ws.i2055.Registrar_SolicitudResult registrar_Solicitud(com.siga.ws.i2055.SIGAAsigna SIGAAsigna) throws java.rmi.RemoteException {
        return null;
    }

    public com.siga.ws.i2055.ResultadoConsultaExpediente consultaExpediente(java.lang.String identificadorExpedienteSIGA) throws java.rmi.RemoteException {
        return null;
    }

    public com.siga.ws.i2055.ResultadoConsultaProcuradorAsignado[] consultaProcuradorAsignado(java.util.Calendar fechaInicio, java.util.Calendar fechaFin) throws java.rmi.RemoteException {
        return null;
    }

    public com.siga.ws.i2055.ResultadoEnvioDocumento envioDocumento(java.lang.String identificadorExpedienteSIGA, java.lang.String nombreDocumento, java.lang.String tituloDocumento, java.util.Calendar fecha, int identificadorDocumentoSIGA, int tipoArchivo, byte[] contenidoDocumento, long tamanioDocumento, int IDUsuarioRegistro) throws java.rmi.RemoteException {
        return null;
    }

    public com.siga.ws.i2055.ResultadoConsultaResolucionesExpediente[] consultaResolucionesExpediente(java.util.Calendar fechaInicio, java.util.Calendar fechaFin) throws java.rmi.RemoteException {
    	ResultadoConsultaResolucionesExpediente resuls[] = new ResultadoConsultaResolucionesExpediente[4];
    	resuls[0] = addResultado("204020120000000001", ECalificaciones.CONCED100, fechaFin);
    	resuls[1] = addResultado("204020130000000001", ECalificaciones.CONCED100, fechaFin);
    	resuls[2] = addResultado("204020130000000002", ECalificaciones.CONCED80, fechaFin);
    	
    	ResultadoConsultaResolucionesExpediente resul = new ResultadoConsultaResolucionesExpediente();
    	resul.setIdentificadorExpedienteSIGA("204020120000000003");    	
    	SCalificacion sCalificacion = new SCalificacion();
    	sCalificacion.setCalificacion(ECalificaciones.DENEGADO);
    	sCalificacion.setFecha(Calendar.getInstance());
    	
    	ArrayOfMotivosRechazo arrayOfMotivosRechazo = new ArrayOfMotivosRechazo();
    	arrayOfMotivosRechazo.setCodigo(EMotivosDeNegacion.OTROS);
    	arrayOfMotivosRechazo.setParam1("2010/02569");
    	arrayOfMotivosRechazo.setParam2("Procedimiento abreviado");
    	arrayOfMotivosRechazo.setParam3("Un juzgado estumpendo");
    	arrayOfMotivosRechazo.setDescripcion("Descripción enviada por asigna");
    	sCalificacion.setObservaciones(EObservaciones.Excepcionalidad);
    	sCalificacion.setMotivoRechazo(arrayOfMotivosRechazo);
    	
    	resul.setCalificacionConsultaResoluciones(sCalificacion);
    	
    	resuls[3] = resul;
    	
    	return resuls;
    }

	private ResultadoConsultaResolucionesExpediente addResultado(String identificadorExpedienteSIGA, ECalificaciones eCalificaciones, Calendar cal) {
		ResultadoConsultaResolucionesExpediente resul = new ResultadoConsultaResolucionesExpediente();
    	resul.setIdentificadorExpedienteSIGA(identificadorExpedienteSIGA);    	
    	SCalificacion sCalificacion = new SCalificacion();
    	sCalificacion.setCalificacion(eCalificaciones);
    	sCalificacion.setFecha(cal);
    	
    	resul.setCalificacionConsultaResoluciones(sCalificacion);
    	return resul;
	}

}
