package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

/**
 * @author RGG
 * @since 08/01/2008
 */
public class ValidarVolantesGuardiasForm extends MasterForm 
{
	public void setIdPersona	(String valor)	{
		this.datos.put("IdPersona", valor);
	}
	public String getIdPersona() {
		return UtilidadesHash.getString(this.datos, "IdPersona");
	}

	public void setNumColegiado	(String valor)	{
		this.datos.put("NumColegiado", valor);
	}
	public String getNumColegiado() {
		return UtilidadesHash.getString(this.datos, "NumColegiado");
	}

	public void setPendienteValidar	(String valor)	{
		this.datos.put("PendienteValidar", valor);
	}
	public String getPendienteValidar() {
		return UtilidadesHash.getString(this.datos, "PendienteValidar");
	}

	public void setFecha	(String valor)	{
		this.datos.put("Fecha", valor);
	}
	public String getFecha() {
		return UtilidadesHash.getString(this.datos, "Fecha");
	}

	public void setDatosValidar	(String valor)	{
		this.datos.put("DatosValidar", valor);
	}
	public String getDatosValidar() {
		return UtilidadesHash.getString(this.datos, "DatosValidar");
	}

	public void setDatosBorrar	(String valor)	{
		this.datos.put("DatosBorrar", valor);
	}
	public String getDatosBorrar() {
		return UtilidadesHash.getString(this.datos, "DatosBorrar");
	}

	public void setIdTurno	(String valor)	{
		this.datos.put("IdTurno", valor);
	}
	public String getIdTurno() {
		return UtilidadesHash.getString(this.datos, "IdTurno");
	}

	public void setIdGuardia	(String valor)	{
		this.datos.put("IdGuardia", valor);
	}
	public String getIdGuardia() {
		return UtilidadesHash.getString(this.datos, "IdGuardia");
	}

	public String getDesde() {
		return UtilidadesHash.getString(this.datos, "desde");
	}
	public void setDesde(String desde) {
		UtilidadesHash.set(this.datos, "desde", desde);
	}
	public String getBuscarFechaDesde() {
		return UtilidadesHash.getString(this.datos, "buscarFechaDesde");
	}
	public void setBuscarFechaDesde(String buscarFechaDesde) {
		UtilidadesHash.set(this.datos, "buscarFechaDesde", buscarFechaDesde);
	}
	public String getBuscarFechaHasta() {
		return UtilidadesHash.getString(this.datos, "buscarFechaHasta");
	}
	public void setBuscarFechaHasta(String buscarFechaHasta) {
		UtilidadesHash.set(this.datos, "buscarFechaHasta", buscarFechaHasta);
	}
	
	
	
	
}