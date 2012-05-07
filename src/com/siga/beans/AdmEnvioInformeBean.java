
package com.siga.beans;

import com.atos.utils.UsrBean;

/**
 * @author RGG
 *
 * Clase que recoge y establece los valores del bean ADM_TIPOINFORME
 */

public class AdmEnvioInformeBean extends MasterBean {

	// Variables	
	private String idPlantilla;
	private Integer idInstitucion;
	private String idTipoEnvios;
	private String defecto;
	private String idPlantillaEnvioDef;
	
	
	// Nombre tabla
	static public String T_NOMBRETABLA = "ADM_ENVIOINFORME";
	UsrBean usrBean;

	// Nombre campos de la tabla
	static public final String C_IDPLANTILLA = "IDPLANTILLA";
	static public final String C_FECHAMODIFICACION = "FECHAMODIFICACION";
	static public final String C_USUMODIFICACION = "USUMODIFICACION";
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOENVIOS = "IDTIPOENVIOS";
	static public final String C_DEFECTO = "DEFECTO";
	static public final String C_IDPLANTILLAENVIODEF = "IDPLANTILLAENVIODEF";
	public String getIdPlantilla() {
		return idPlantilla;
	}
	public void setIdPlantilla(String idPlantilla) {
		this.idPlantilla = idPlantilla;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	
	public UsrBean getUsrBean() {
		return usrBean;
	}
	public void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	}
	public String getIdTipoEnvios() {
		return idTipoEnvios;
	}
	public void setIdTipoEnvios(String idTipoEnvios) {
		this.idTipoEnvios = idTipoEnvios;
	}
	public String getIdPlantillaEnvioDef() {
		return idPlantillaEnvioDef;
	}
	public void setIdPlantillaEnvioDef(String idPlantillaEnvioDef) {
		this.idPlantillaEnvioDef = idPlantillaEnvioDef;
	}
	public String getDefecto() {
		return defecto;
	}
	public void setDefecto(String defecto) {
		this.defecto = defecto;
	}
	

	
	
	

	
}
