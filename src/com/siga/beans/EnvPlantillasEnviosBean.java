package com.siga.beans;

import org.json.JSONException;
import org.json.JSONObject;

import com.siga.Utilidades.AjaxXMLBuilderAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderNameAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderValueAnnotation;

@AjaxXMLBuilderAnnotation 

public class EnvPlantillasEnviosBean extends MasterBean
{
	/* Variables */
	private Integer idInstitucion;
	private Integer idTipoEnvios;
	private Integer idPlantillaEnvios;
	private String nombre;
	private String acuseRecibo;
	private String fechaBaja;

	/* Nombre campos de la tabla */
	static public final String C_IDINSTITUCION = "IDINSTITUCION";
	static public final String C_IDTIPOENVIOS = "IDTIPOENVIOS";
	static public final String C_IDPLANTILLAENVIOS = "IDPLANTILLAENVIOS";
	static public final String C_NOMBRE = "NOMBRE";
	static public final String C_ACUSERECIBO = "ACUSERECIBO";
	static public final String C_FECHABAJA = "FECHABAJA";
	
	static public final String T_NOMBRETABLA = "ENV_PLANTILLASENVIOS";
	
	// Métodos SET
    public Integer getIdInstitucion()
    {
        return idInstitucion;
    }
    
    public void setIdInstitucion(Integer idInstitucion)
    {
        this.idInstitucion = idInstitucion;
    }
	@AjaxXMLBuilderValueAnnotation(isCData=false)
    public Integer getIdTipoEnvios()
    {
        return idTipoEnvios;
    }
    
    public void setIdTipoEnvios(Integer idTipoEnvios)
    {
        this.idTipoEnvios = idTipoEnvios;
    }

    public Integer getIdPlantillaEnvios()
    {
        return idPlantillaEnvios;
    }
    
    public void setIdPlantillaEnvios(Integer idPlantillaEnvios)
    {
        this.idPlantillaEnvios = idPlantillaEnvios;
    }
    @AjaxXMLBuilderNameAnnotation
    public String getNombre()
    {
        return nombre;
    }
    
    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

	public String getAcuseRecibo() {
		return acuseRecibo;
	}

	public void setAcuseRecibo(String acuseRecibo) {
		this.acuseRecibo = acuseRecibo;
	}
	
	public String getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("idPlantillaEnvios", this.idPlantillaEnvios);
            obj.put("nombre", this.nombre);
            obj.put("acuseRecibo", this.acuseRecibo==null||this.acuseRecibo.equals("")?"0":this.acuseRecibo);
        } catch (JSONException e) {
        }
        return obj;
    }
}