package com.siga.informes.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * @author RGG
 */
public class InformesGenericosForm extends MasterForm
{
	//datos esta en MasterForm
	
	
	//GETTERS
	public String getIdInstitucion() { return UtilidadesHash.getString(this.datos, "IDINSTITUCION"); }
	public String getDatosInforme() { return UtilidadesHash.getString(this.datos, "DATOSINFORME");	}
	public String getIdInforme() { return UtilidadesHash.getString(this.datos, "IDINFORME"); }
	public String getIdTipoInforme() { return UtilidadesHash.getString(this.datos, "IDTIPOINFORME"); }
	public String getSeleccionados() { return UtilidadesHash.getString(this.datos, "SELECCIONADOS"); }
	public String getAsolicitantes() { return UtilidadesHash.getString(this.datos, "ASOLICITANTES"); }
	public String getIdTipoEJG() { return UtilidadesHash.getString(this.datos, "IDTIPOEJG"); }
	public String getAnio() { return UtilidadesHash.getString(this.datos, "ANIO"); }
	public String getNumero() { return UtilidadesHash.getString(this.datos, "NUMERO"); }
	public String getEnviar() { return UtilidadesHash.getString(this.datos, "ENVIAR"); }
	public String getDescargar() { return UtilidadesHash.getString(this.datos, "DESGARGAR"); }
	public String getTipoPersonas() { return (String) this.datos.get("tipoPersonas"); }
	public String getClavesIteracion() { return (String) this.datos.get("clavesIteracion"); }
	public String getDestinatarios() { return (String) this.datos.get("destinatarios"); }
	public String getPeriodo() { return UtilidadesHash.getString(this.datos, "PERIODO"); }
	
	
	//SETTERS
	public void setIdInstitucion(String valor) { UtilidadesHash.set(this.datos, "IDINSTITUCION", valor); }
	public void setDatosInforme(String valor) { UtilidadesHash.set(this.datos, "DATOSINFORME", valor); }
	public void setIdInforme(String valor) { UtilidadesHash.set(this.datos, "IDINFORME", valor); }
	public void setIdTipoInforme(String valor) { UtilidadesHash.set(this.datos, "IDTIPOINFORME", valor); }
	public void setSeleccionados(String valor) { UtilidadesHash.set(this.datos, "SELECCIONADOS", valor); }
	public void setAsolicitantes(String solicitantes) { UtilidadesHash.set(this.datos, "ASOLICITANTES", solicitantes); }
	public void setIdTipoEJG(String idTipoEJG) { UtilidadesHash.set(this.datos, "IDTIPOEJG", idTipoEJG); }
	public void setAnio(String anio) { UtilidadesHash.set(this.datos, "ANIO", anio); }
	public void setNumero(String numero) { UtilidadesHash.set(this.datos, "NUMERO", numero); }
	public void setEnviar(String enviar) { UtilidadesHash.set(this.datos, "ENVIAR", enviar); }
	public void setDescargar(String descargar) { UtilidadesHash.set(this.datos, "DESGARGAR", descargar); }
	public void setTipoPersonas(String tipoPersonas) { this.datos.put("tipoPersonas", tipoPersonas); }
	public void setClavesIteracion(String clavesIteracion) { this.datos.put("clavesIteracion", clavesIteracion); }
	public void setDestinatarios(String destinatarios) {
		if (destinatarios != null)
			this.datos.put("destinatarios", destinatarios);
		else
			this.datos.put("destinatarios", "CJS");
	}
	public void setPeriodo(String valor) { UtilidadesHash.set(this.datos, "PERIODO", valor); }
}
