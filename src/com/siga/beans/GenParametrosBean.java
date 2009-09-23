/*
 * Created on 20-dic-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import  com.atos.utils.ClsConstants;

/**
 * @author daniel.campos
 *
 */
public class GenParametrosBean extends MasterBean {
	
	/* Variables */
	private Integer idInstitucion= new Integer(ClsConstants.INSTITUCION_POR_DEFECTO);
	private String 	modulo, parametro, valor, idRecurso;
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "GEN_PARAMETROS";

	/* Nombre campos de la tabla */
	static public final String C_MODULO			= "MODULO";
	static public final String C_IDINSTITUCION	= "IDINSTITUCION";
	static public final String C_PARAMETRO		= "PARAMETRO";
	static public final String C_VALOR			= "VALOR";
	static public final String C_IDRECURSO		= "IDRECURSO";
	
	
	
	/**
	 * @return Returns the idInstitucion.
	 */
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion The idInstitucion to set.
	 */
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return Returns the modulo.
	 */
	public String getModulo() {
		return modulo;
	}
	/**
	 * @param modulo The modulo to set.
	 */
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	/**
	 * @return Returns the parametro.
	 */
	public String getParametro() {
		return parametro;
	}
	/**
	 * @param parametro The parametro to set.
	 */
	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
	/**
	 * @return Returns the valor.
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * @param valor The valor to set.
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	/**
	 * @return Returns the idRecurso.
	 */
	public String getIdRecurso() {
		return idRecurso;
	}
	/**
	 * @param idRecurso The idRecurso to set.
	 */
	public void setIdRecurso(String idRecurso) {
		this.idRecurso = idRecurso;
	}
}
