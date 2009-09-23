/**
 * ServiciosECOSServiceSOAPSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package service.ServiciosECOS;

public class ServiciosECOSServiceSOAPSkeleton implements service.ServiciosECOS.ServiciosECOSService_PortType, org.apache.axis.wsdl.Skeleton {
    private service.ServiciosECOS.ServiciosECOSService_PortType impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "EnviarFaxRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://solicitarEnvio.ws.ecos.com", "solicitudEnvioFax"), com.ecos.ws.solicitarEnvio.SolicitudEnvioFax.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("enviarFax", _params, new javax.xml.namespace.QName("", "EnviarFaxResponse"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://solicitarEnvio.ws.ecos.com", "resultadoSolicitudEnvio"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "EnviarFax"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("enviarFax") == null) {
            _myOperations.put("enviarFax", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("enviarFax")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "EnviarSMSRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://solicitarEnvio.ws.ecos.com", "solicitudEnvioSMS"), com.ecos.ws.solicitarEnvio.SolicitudEnvioSMS.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("enviarSMS", _params, new javax.xml.namespace.QName("", "EnviarSMSResponse"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://solicitarEnvio.ws.ecos.com", "resultadoSolicitudEnvio"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "EnviarSMS"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("enviarSMS") == null) {
            _myOperations.put("enviarSMS", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("enviarSMS")).add(_oper);
    }

    public ServiciosECOSServiceSOAPSkeleton() {
        this.impl = new service.ServiciosECOS.ServiciosECOSServiceSOAPImpl();
    }

    public ServiciosECOSServiceSOAPSkeleton(service.ServiciosECOS.ServiciosECOSService_PortType impl) {
        this.impl = impl;
    }
    public com.ecos.ws.solicitarEnvio.ResultadoSolicitudEnvio enviarFax(com.ecos.ws.solicitarEnvio.SolicitudEnvioFax enviarFaxRequest) throws java.rmi.RemoteException
    {
        com.ecos.ws.solicitarEnvio.ResultadoSolicitudEnvio ret = impl.enviarFax(enviarFaxRequest);
        return ret;
    }

    public com.ecos.ws.solicitarEnvio.ResultadoSolicitudEnvio enviarSMS(com.ecos.ws.solicitarEnvio.SolicitudEnvioSMS enviarSMSRequest) throws java.rmi.RemoteException
    {
        com.ecos.ws.solicitarEnvio.ResultadoSolicitudEnvio ret = impl.enviarSMS(enviarSMSRequest);
        return ret;
    }

}
