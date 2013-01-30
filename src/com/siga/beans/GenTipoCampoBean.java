/*
 * Created on Dec 27, 2004
 * @author jmgrau
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;


public class GenTipoCampoBean extends MasterBean {
    
	//Variables
    private Integer idTipoCampo;
    private String 	descripcion;	
    private UsrBean usrBean;
	
	// Nombre campos de la tabla 
	static public final String C_IDTIPOCAMPO = "IDTIPOCAMPO";
	static public final String C_DESCRIPCION = "DESCRIPCION";
	
	static public final String T_NOMBRETABLA = "GEN_TIPOCAMPO";

	
	static public final Integer ID_TIPO_ALFANUMERICO 	= 1;
	static public final Integer ID_TIPO_NUMERICO 		= 2;
	static public final Integer ID_TIPO_FECHA 			= 3;
	
	public GenTipoCampoBean(UsrBean usrBean) {
		super();
		this.usrBean = usrBean;
	}
	public UsrBean getUsrBean() {
		return usrBean;
	}

	public void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	}

	public Integer getIdTipoCampo() {
		return idTipoCampo;
	}

	public void setIdTipoCampo(Integer idTipoCampo) {
		this.idTipoCampo = idTipoCampo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getRecursoDescripcion(){
		return UtilidadesString.getMensajeIdioma(this.usrBean, this.descripcion);
	}
}
