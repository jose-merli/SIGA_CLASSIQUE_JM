/**
 * SolicitudEnvioFax.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.ecos.ws.solicitarEnvio;

public class SolicitudEnvioFax  implements java.io.Serializable {
    private java.lang.String idClienteECOS;

    private java.lang.String[] listaTOs;

    private java.lang.String[] listaPdfB64;

    private java.lang.String texto;

    private java.lang.String fecha;

    private java.lang.String hora;

    private boolean isProgramado;

    public SolicitudEnvioFax() {
    }

    public SolicitudEnvioFax(
           java.lang.String idClienteECOS,
           java.lang.String[] listaTOs,
           java.lang.String[] listaPdfB64,
           java.lang.String texto,
           java.lang.String fecha,
           java.lang.String hora,
           boolean isProgramado) {
           this.idClienteECOS = idClienteECOS;
           this.listaTOs = listaTOs;
           this.listaPdfB64 = listaPdfB64;
           this.texto = texto;
           this.fecha = fecha;
           this.hora = hora;
           this.isProgramado = isProgramado;
    }


    /**
     * Gets the idClienteECOS value for this SolicitudEnvioFax.
     * 
     * @return idClienteECOS
     */
    public java.lang.String getIdClienteECOS() {
        return idClienteECOS;
    }


    /**
     * Sets the idClienteECOS value for this SolicitudEnvioFax.
     * 
     * @param idClienteECOS
     */
    public void setIdClienteECOS(java.lang.String idClienteECOS) {
        this.idClienteECOS = idClienteECOS;
    }


    /**
     * Gets the listaTOs value for this SolicitudEnvioFax.
     * 
     * @return listaTOs
     */
    public java.lang.String[] getListaTOs() {
        return listaTOs;
    }


    /**
     * Sets the listaTOs value for this SolicitudEnvioFax.
     * 
     * @param listaTOs
     */
    public void setListaTOs(java.lang.String[] listaTOs) {
        this.listaTOs = listaTOs;
    }

    public java.lang.String getListaTOs(int i) {
        return this.listaTOs[i];
    }

    public void setListaTOs(int i, java.lang.String _value) {
        this.listaTOs[i] = _value;
    }


    /**
     * Gets the listaPdfB64 value for this SolicitudEnvioFax.
     * 
     * @return listaPdfB64
     */
    public java.lang.String[] getListaPdfB64() {
        return listaPdfB64;
    }


    /**
     * Sets the listaPdfB64 value for this SolicitudEnvioFax.
     * 
     * @param listaPdfB64
     */
    public void setListaPdfB64(java.lang.String[] listaPdfB64) {
        this.listaPdfB64 = listaPdfB64;
    }

    public java.lang.String getListaPdfB64(int i) {
        return this.listaPdfB64[i];
    }

    public void setListaPdfB64(int i, java.lang.String _value) {
        this.listaPdfB64[i] = _value;
    }


    /**
     * Gets the texto value for this SolicitudEnvioFax.
     * 
     * @return texto
     */
    public java.lang.String getTexto() {
        return texto;
    }


    /**
     * Sets the texto value for this SolicitudEnvioFax.
     * 
     * @param texto
     */
    public void setTexto(java.lang.String texto) {
        this.texto = texto;
    }


    /**
     * Gets the fecha value for this SolicitudEnvioFax.
     * 
     * @return fecha
     */
    public java.lang.String getFecha() {
        return fecha;
    }


    /**
     * Sets the fecha value for this SolicitudEnvioFax.
     * 
     * @param fecha
     */
    public void setFecha(java.lang.String fecha) {
        this.fecha = fecha;
    }


    /**
     * Gets the hora value for this SolicitudEnvioFax.
     * 
     * @return hora
     */
    public java.lang.String getHora() {
        return hora;
    }


    /**
     * Sets the hora value for this SolicitudEnvioFax.
     * 
     * @param hora
     */
    public void setHora(java.lang.String hora) {
        this.hora = hora;
    }


    /**
     * Gets the isProgramado value for this SolicitudEnvioFax.
     * 
     * @return isProgramado
     */
    public boolean isIsProgramado() {
        return isProgramado;
    }


    /**
     * Sets the isProgramado value for this SolicitudEnvioFax.
     * 
     * @param isProgramado
     */
    public void setIsProgramado(boolean isProgramado) {
        this.isProgramado = isProgramado;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SolicitudEnvioFax)) return false;
        SolicitudEnvioFax other = (SolicitudEnvioFax) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idClienteECOS==null && other.getIdClienteECOS()==null) || 
             (this.idClienteECOS!=null &&
              this.idClienteECOS.equals(other.getIdClienteECOS()))) &&
            ((this.listaTOs==null && other.getListaTOs()==null) || 
             (this.listaTOs!=null &&
              java.util.Arrays.equals(this.listaTOs, other.getListaTOs()))) &&
            ((this.listaPdfB64==null && other.getListaPdfB64()==null) || 
             (this.listaPdfB64!=null &&
              java.util.Arrays.equals(this.listaPdfB64, other.getListaPdfB64()))) &&
            ((this.texto==null && other.getTexto()==null) || 
             (this.texto!=null &&
              this.texto.equals(other.getTexto()))) &&
            ((this.fecha==null && other.getFecha()==null) || 
             (this.fecha!=null &&
              this.fecha.equals(other.getFecha()))) &&
            ((this.hora==null && other.getHora()==null) || 
             (this.hora!=null &&
              this.hora.equals(other.getHora()))) &&
            this.isProgramado == other.isIsProgramado();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getIdClienteECOS() != null) {
            _hashCode += getIdClienteECOS().hashCode();
        }
        if (getListaTOs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getListaTOs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getListaTOs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getListaPdfB64() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getListaPdfB64());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getListaPdfB64(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTexto() != null) {
            _hashCode += getTexto().hashCode();
        }
        if (getFecha() != null) {
            _hashCode += getFecha().hashCode();
        }
        if (getHora() != null) {
            _hashCode += getHora().hashCode();
        }
        _hashCode += (isIsProgramado() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SolicitudEnvioFax.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://solicitarEnvio.ws.ecos.com", "solicitudEnvioFax"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idClienteECOS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idClienteECOS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listaTOs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "listaTOs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listaPdfB64");
        elemField.setXmlName(new javax.xml.namespace.QName("", "listaPdfB64"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("texto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "texto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hora");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hora"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isProgramado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isProgramado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
