/*
 * Created on Feb 2, 2005
 * @author emilio.grau
 *
 */
package com.siga.expedientes.form;


import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * Formulario para modal de nuevo expediente
 */
public class PestanaConfigurableForm extends MasterForm {
	private static final long serialVersionUID = 2631120868009987557L;

	public String getAccion() {
        return UtilidadesHash.getString(this.datos,"Accion");
    }
    public void setAccion(String valor) {
        UtilidadesHash.set(this.datos,"Accion",valor);
    }

    public String getModo() {
        return UtilidadesHash.getString(this.datos,"Modo");
    }
    public void setModo(String valor) {
        UtilidadesHash.set(this.datos,"Modo",valor);
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

    public String getNumeroExpediente() {
        return UtilidadesHash.getString(this.datos,"NumeroExpediente");
    }
    public void setNumeroExpediente(String valor) {
        UtilidadesHash.set(this.datos,"NumeroExpediente",valor);
    }

    public String getAnioExpediente() {
        return UtilidadesHash.getString(this.datos,"AnioExpediente");
    }
    public void setAnioExpediente(String valor) {
        UtilidadesHash.set(this.datos,"AnioExpediente",valor);
    }

    public String getIdPestanaConf() {
        return UtilidadesHash.getString(this.datos,"IdPestanaConf");
    }
    public void setIdPestanaConf(String valor) {
        UtilidadesHash.set(this.datos,"IdPestanaConf",valor);
    }

    public String getIdCampo() {
        return UtilidadesHash.getString(this.datos,"IdCampo");
    }
    public void setIdCampo(String valor) {
        UtilidadesHash.set(this.datos,"IdCampo",valor);
    }

    public String getNombrePestana() {
        return UtilidadesHash.getString(this.datos,"NombrePestana");
    }
    public void setNombrePestana(String valor) {
        UtilidadesHash.set(this.datos,"NombrePestana",valor);
    }
	public void setTituloVentana(String valor) {
		UtilidadesHash.set(this.datos,"TituloVentana",valor);
	}
	public String getTituloVentana() {
		return UtilidadesHash.getString(this.datos,"TituloVentana");
	}
}
