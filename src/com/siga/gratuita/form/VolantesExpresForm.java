package com.siga.gratuita.form;

import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.MasterForm;

public class VolantesExpresForm extends MasterForm 
{
	
	public String getGuardiaDia() {
		return UtilidadesHash.getString(this.datos, "guardiaDia");
	}
	public void setGuardiaDia(String guardiaDia) {
		UtilidadesHash.set(this.datos, "guardiaDia", guardiaDia);
	}
	public String getLetrado() {
		return UtilidadesHash.getString(this.datos, "letrado");
	}
	public void setLetrado(String letrado) {
		UtilidadesHash.set(this.datos, "letrado", letrado);
	}
	public String getGuardia() {
		return UtilidadesHash.getString(this.datos, "guardia");
	}
	public void setGuardia(String guardia) {
		UtilidadesHash.set(this.datos, "guardia", guardia);
	}
	public String getSustitutoDe() {
		return UtilidadesHash.getString(this.datos, "sustitutoDe");
	}
	public void setSustitutoDe(String sustitutoDe) {
		UtilidadesHash.set(this.datos, "sustitutoDe", sustitutoDe);
	}
	public String getGuardiaDelSustituto() {
		return UtilidadesHash.getString(this.datos, "guardiaDelSustituto");
	}
	public void setGuardiaDelSustituto(String guardiaDelSustituto) {
		UtilidadesHash.set(this.datos, "guardiaDelSustituto", guardiaDelSustituto);
	}
	public String getTipoAsistencia() {
		return UtilidadesHash.getString(this.datos, "tipoAsistencia");
	}
	public void setTipoAsistencia(String tipoAsistencia) {
		UtilidadesHash.set(this.datos, "tipoAsistencia", tipoAsistencia);
	}
	public String getTipoAsistenciaColegio() {
		return UtilidadesHash.getString(this.datos, "tipoAsistenciaColegio");
	}
	public void setTipoAsistenciaColegio(String tipoAsistenciaColegio) {
		UtilidadesHash.set(this.datos, "tipoAsistenciaColegio", tipoAsistenciaColegio);
	}
	public String getTurno() {
		return UtilidadesHash.getString(this.datos, "turno");
	}
	public void setTurno(String turno) {
		UtilidadesHash.set(this.datos, "turno", turno);
	}
	public String getLugar() {
		return UtilidadesHash.getString(this.datos, "lugar");
	}
	public void setLugar(String lugar) {
		UtilidadesHash.set(this.datos, "lugar", lugar);
	}
	public String getDesdeNuevo() {
		return UtilidadesHash.getString(this.datos, "desdeNuevo");
	}
	public void setDesdeNuevo(String desdeNuevo) {
		UtilidadesHash.set(this.datos, "desdeNuevo", desdeNuevo);
	}
	public String getSinAvisos() {
		return UtilidadesHash.getString(this.datos, "sinAvisos");
	}
	public void setSinAvisos(String sinAvisos) {
		UtilidadesHash.set(this.datos, "sinAvisos", sinAvisos);
	}
	String fechaJustificacion ;
	public String getFechaJustificacion() {
		return fechaJustificacion;
	}
	public void setFechaJustificacion(String fechaJustificacion) {
		this.fechaJustificacion = fechaJustificacion;
	}
	
	
}