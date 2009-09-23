/**
 * ServiciosECOSService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package service.ServiciosECOS;

public interface ServiciosECOSService_PortType extends java.rmi.Remote {
    public com.ecos.ws.solicitarEnvio.ResultadoSolicitudEnvio enviarFax(com.ecos.ws.solicitarEnvio.SolicitudEnvioFax enviarFaxRequest) throws java.rmi.RemoteException;
    public com.ecos.ws.solicitarEnvio.ResultadoSolicitudEnvio enviarSMS(com.ecos.ws.solicitarEnvio.SolicitudEnvioSMS enviarSMSRequest) throws java.rmi.RemoteException;
}
