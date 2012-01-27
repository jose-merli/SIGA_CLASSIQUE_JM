/*
 * Created on Mar 9, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

/**
 * Bean de la tabla de consulta-modulo
 */
public class ConModuloBean extends MasterBean {
	
	//	Variables
	private Integer idModulo;
	private String nombre;
	private String fechaModificacion;
	private Integer usuModificacion;
	
	//	 Nombre campos de la tabla 
	static public final String C_IDMODULO ="IDMODULO";
	static public final String C_NOMBRE ="NOMBRE";
	static public final String C_FECHAMODIFICACION ="FECHAMODIFICACION";
	static public final String C_USUMODIFICACION ="USUMODIFICACION";
	
	
	static public final String T_NOMBRETABLA = "CON_MODULO";
	
	static public final String IDMODULO_ADMINISTRACION="1";
	static public final String IDMODULO_CENSO="3";
	static public final String IDMODULO_CERTIFICADOS="2";
	static public final String IDMODULO_ENVIOS="4";
	static public final String IDMODULO_EXPEDIENTES="5";
	static public final String IDMODULO_FACTURACION="6";
	static public final String IDMODULO_FACTURACIONSJCS="7";
	static public final String IDMODULO_GENERAL="8";
	static public final String IDMODULO_PRODUCTOSYSERVICIOS="9";
	static public final String IDMODULO_SJCS="10";

	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Integer idModulo) {
		this.idModulo = idModulo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getUsuModificacion() {
		return usuModificacion;
	}
	public void setUsuModificacion(Integer usuModificacion) {
		this.usuModificacion = usuModificacion;
	}
}
