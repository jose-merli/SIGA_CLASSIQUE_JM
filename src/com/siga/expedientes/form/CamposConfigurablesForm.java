/*
 * @author RGG
 *
 */
package com.siga.expedientes.form;

import java.util.List;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.beans.GenTipoCampoBean;
import com.siga.general.MasterForm;

/**
 * Formulario para la administración de las fases de los expediente.
 */
public class CamposConfigurablesForm extends MasterForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7352724184046297708L;
	private List<GenTipoCampoBean> tiposcampo;
	private Integer tipo;
	private Integer maxLong;
	
	
    public Integer getMaxLong() {
		return maxLong;
	}
	public void setMaxLong(Integer maxLong) {
		this.maxLong = maxLong;
	}
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public List<GenTipoCampoBean> getTiposcampo() {
		return tiposcampo;
	}
	public void setTiposcampo(List<GenTipoCampoBean> tiposcampo) {
		this.tiposcampo = tiposcampo;
	}
	public String getModo() {
        return UtilidadesHash.getString(this.datos,"Modo");
    }
    public void setModo(String valor) {
        UtilidadesHash.set(this.datos,"Modo",valor);
    }
    
    public String getAccion() {
        return UtilidadesHash.getString(this.datos,"Accion");
    }
    public void setAccion(String valor) {
        UtilidadesHash.set(this.datos,"Accion",valor);
    }
    
    public String getIdInstitucion() {
        return UtilidadesHash.getString(this.datos,"IdInstitucion");
    }
    public void setIdInstitucion(String valor) {
        UtilidadesHash.set(this.datos,"IdInstitucion",valor);
    }
    
    public String getIdTipoExpediente() {
        return UtilidadesHash.getString(this.datos,"IdTipoExpediente");
    }
    public void setIdTipoExpediente(String valor) {
        UtilidadesHash.set(this.datos,"IdTipoExpediente",valor);
    }
    
    public String getIdCampo() {
        return UtilidadesHash.getString(this.datos,"IdCampo");
    }
    public void setIdCampo(String valor) {
        UtilidadesHash.set(this.datos,"IdCampo",valor);
    }
    
    public String getIdPestanaConf() {
        return UtilidadesHash.getString(this.datos,"IdPestanaConf");
    }
    public void setIdPestanaConf(String valor) {
        UtilidadesHash.set(this.datos,"IdPestanaConf",valor);
    }
    
    public String getNombre() {
        return UtilidadesHash.getString(this.datos,"Nombre");
    }
    public void setNombre(String valor) {
        UtilidadesHash.set(this.datos,"Nombre",valor);
    }
    
    public String getIdCampoConf() {
        return UtilidadesHash.getString(this.datos,"IdCampoConf");
    }
    public void setIdCampoConf(String valor) {
        UtilidadesHash.set(this.datos,"IdCampoConf",valor);
    }
    
    public String getOrden() {
        return UtilidadesHash.getString(this.datos,"Orden");
    }
    public void setOrden(String valor) {
        UtilidadesHash.set(this.datos,"Orden",valor);
    }
    
    public String getSeleccionado() {
        return UtilidadesHash.getString(this.datos,"Seleccionado");
    }
    public void setSeleccionado(String valor) {
        UtilidadesHash.set(this.datos,"Seleccionado",valor);
    }
    
    public String getGeneral() {
        return UtilidadesHash.getString(this.datos,"General");
    }
    public void setGeneral(String valor) {
        UtilidadesHash.set(this.datos,"General",valor);
    }

}
