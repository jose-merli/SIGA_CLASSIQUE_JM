/*
 * Created on 13-abr-2005
 *
 */
package com.siga.envios;

import java.io.File;

/**
 * @author juan.grau
 *
 */
public class Documento {
    
    private File documento;
    private String descripcion;
    private Object beanAsociado;
    
    /**
     * @param documento
     * @param descripcion
     */
    public Documento(File documento, String descripcion) {
        this.documento = documento;
        this.descripcion = descripcion;
    }
    
    /**
     * @param ruta
     * @param descripcion
     */
    public Documento(String ruta, String descripcion) {
        File doc = new File(ruta);
        this.documento = doc;
        this.descripcion = descripcion;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public File getDocumento() {
        return documento;
    }
    public void setDocumento(File documento) {
        this.documento = documento;
    }

	public Object getBeanAsociado() {
		return beanAsociado;
	}

	public void setBeanAsociado(Object beanAsociado) {
		this.beanAsociado = beanAsociado;
	}    
}
