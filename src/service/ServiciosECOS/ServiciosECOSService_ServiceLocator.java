/**
 * ServiciosECOSService_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package service.ServiciosECOS;

public class ServiciosECOSService_ServiceLocator extends org.apache.axis.client.Service implements service.ServiciosECOS.ServiciosECOSService_Service {

    public ServiciosECOSService_ServiceLocator() {
    }
    
    public ServiciosECOSService_ServiceLocator(String address){
    	super();
    	ServiciosECOSServiceService_address = address;
    }


    public ServiciosECOSService_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ServiciosECOSService_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ServiciosECOSServiceService
    private java.lang.String ServiciosECOSServiceService_address = "http://desarrollo.redabogacia.org:7001/ecos/wsecos/services/";

    public java.lang.String getServiciosECOSServiceServiceAddress() {
        return ServiciosECOSServiceService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ServiciosECOSServiceServiceWSDDServiceName = "ServiciosECOSService.service";

    public java.lang.String getServiciosECOSServiceServiceWSDDServiceName() {
        return ServiciosECOSServiceServiceWSDDServiceName;
    }

    public void setServiciosECOSServiceServiceWSDDServiceName(java.lang.String name) {
        ServiciosECOSServiceServiceWSDDServiceName = name;
    }

    public service.ServiciosECOS.ServiciosECOSService_PortType getServiciosECOSServiceService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ServiciosECOSServiceService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getServiciosECOSServiceService(endpoint);
    }

    public service.ServiciosECOS.ServiciosECOSService_PortType getServiciosECOSServiceService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            service.ServiciosECOS.ServiciosECOSServiceSOAPStub _stub = new service.ServiciosECOS.ServiciosECOSServiceSOAPStub(portAddress, this);
            _stub.setPortName(getServiciosECOSServiceServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setServiciosECOSServiceServiceEndpointAddress(java.lang.String address) {
        ServiciosECOSServiceService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (service.ServiciosECOS.ServiciosECOSService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                service.ServiciosECOS.ServiciosECOSServiceSOAPStub _stub = new service.ServiciosECOS.ServiciosECOSServiceSOAPStub(new java.net.URL(ServiciosECOSServiceService_address), this);
                _stub.setPortName(getServiciosECOSServiceServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ServiciosECOSService.service".equals(inputPortName)) {
            return getServiciosECOSServiceService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ServiciosECOS.service", "ServiciosECOSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ServiciosECOS.service", "ServiciosECOSService.service"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ServiciosECOSServiceService".equals(portName)) {
            setServiciosECOSServiceServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
