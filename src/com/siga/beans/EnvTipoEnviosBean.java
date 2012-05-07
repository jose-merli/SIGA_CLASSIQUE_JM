/*
 * Created on Mar 15, 2005
 * @author jmgrau
 *
 */
package com.siga.beans;

import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

import com.siga.Utilidades.AjaxXMLBuilderAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderNameAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderValueAnnotation;

public class EnvTipoEnviosBean extends MasterBean {
    
	//Variables
    private String nombre;
	private Integer idTipoEnvios;
	private String defecto;
	private String idPlantillaDefecto;
	// Nombre campos de la tabla 
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_IDTIPOENVIOS = "IDTIPOENVIOS";	
	
	static public final String T_NOMBRETABLA = "ENV_TIPOENVIOS";	

    public Integer getIdTipoEnvios() {
        return idTipoEnvios;
    }
    public void setIdTipoEnvios(Integer idTipoEnvios) {
        this.idTipoEnvios = idTipoEnvios;
    }
   
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
	public String getIdPlantillaDefecto() {
		return idPlantillaDefecto;
	}
	public void setIdPlantillaDefecto(String idPlantillaDefecto) {
		this.idPlantillaDefecto = idPlantillaDefecto;
	}
	public String getDefecto() {
		return defecto;
	}
	public void setDefecto(String defecto) {
		this.defecto = defecto;
	}
	public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("idTipoEnvios", this.idTipoEnvios);
            obj.put("nombre", this.nombre);
        } catch (JSONException e) {
        }
        return obj;
    }
}
