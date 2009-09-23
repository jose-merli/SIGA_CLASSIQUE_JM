/*
 * Created on Jul 13, 2005
 * Autor: david.sanchezp
 * Esta clase define un usuario de fax. Es usada en la clase ZetaFax.
 */
package com.siga.envios;

/**
 * @author david.sanchezp
 * Esta clase define un usuario de fax. Es usada en la clase ZetaFax.
 */
public class UsuarioFax {
	
	private String nombre = null;
	private String fax = null;
	
	public UsuarioFax(String nombre, String fax){
		this.nombre = nombre;
		this.fax = fax;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
