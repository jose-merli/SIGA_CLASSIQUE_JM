/*
 * nuria.rgonzalez - 21-03-2005 - Creacion
 *	
 */

package com.siga.beans;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase que recoge y establece los valores del bean CEN_BANCOS <br/>
 * Tiene tantos metodos set y get por cada uno de los campos de dicho formulario 
 */
public class CenBancosBean extends MasterBean {

	/* Variables */	
	private String 	codigo, nombre, bic, idPais;	
	
	/* Nombre tabla */
	static public String T_NOMBRETABLA = "CEN_BANCOS";
	
	/* Nombre campos de la tabla */
	static public final String C_CODIGO		= "CODIGO";
	static public final String C_NOMBRE		= "NOMBRE";
	static public final String C_BIC		= "BIC";
	static public final String C_IDPAIS		= "IDPAIS";
	
	CenBancosBean() {
		this.codigo = "";
		this.nombre = "";
		this.bic = "";
		this.idPais = "";
	}

	// Metodos SET
	public void setCodigo(String codigo) {this.codigo = codigo;}	
	public void setNombre(String nombre) {this.nombre = nombre;}
	
	//Metodos GET
	public String getCodigo() {return codigo;}	
	public String getNombre() {return nombre;}
	
	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}
	
	public String getIdPais() {
		return idPais;
	}

	public void setIdPais(String idPais) {
		this.idPais = idPais;
	}

	public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("idCodigo", this.codigo);
            obj.put("nombre", this.nombre);
            obj.put("bic", this.bic);
        } 
        catch (JSONException e) {
        }
        return obj;
    }
	
}
