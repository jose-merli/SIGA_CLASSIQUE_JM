package com.siga.gratuita.vos;

import java.util.Collection;
import java.util.List;

import com.atos.utils.UsrBean;
import com.siga.beans.ScsAsistenciasBean;
public class VolantesExpressVo{
	Integer idInstitucion;
	String fechaGuardia;
	String lugar;
	Integer idTurno;
	Integer idGuardia;
	Long idColegiado;
	Long idColegiadoGuardia;
	String nombreColegiado;
	Boolean sustituto;
	Long idColegiadoSustituido;
	Integer idTipoAsistencia;
	Integer idTipoAsistenciaColegio;
	
	String datosAsistencias;
	
	List<ScsAsistenciasBean> asistencias;
	
	UsrBean usrBean;
	String fechaJustificacion;
	String msgAviso;
	
	public String getMsgAviso() {
		return msgAviso;
	}

	public void setMsgAviso(String msgAviso) {
		this.msgAviso = msgAviso;
	}

	public Integer getIdInstitucion() {
		return idInstitucion;
	}

	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}

	public String getFechaGuardia() {
		return fechaGuardia;
	}

	public void setFechaGuardia(String fechaGuardia) {
		this.fechaGuardia = fechaGuardia;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public Integer getIdTurno() {
		return idTurno;
	}

	public void setIdTurno(Integer idTurno) {
		this.idTurno = idTurno;
	}

	public Integer getIdGuardia() {
		return idGuardia;
	}

	public void setIdGuardia(Integer idGuardia) {
		this.idGuardia = idGuardia;
	}
	
	public Long getIdColegiado() {
		return idColegiado;
	}

	public void setIdColegiado(Long idColegiado) {
		this.idColegiado = idColegiado;
	}

	public Long getIdColegiadoSustituido() {
		return idColegiadoSustituido;
	}

	public void setIdColegiadoSustituido(Long idColegiadoSustituido) {
		this.idColegiadoSustituido = idColegiadoSustituido;
	}

	public Integer getIdTipoAsistencia() {
		return idTipoAsistencia;
	}

	public void setIdTipoAsistencia(Integer idTipoAsistencia) {
		this.idTipoAsistencia = idTipoAsistencia;
	}

	public Integer getIdTipoAsistenciaColegio() {
		return idTipoAsistenciaColegio;
	}

	public void setIdTipoAsistenciaColegio(Integer idTipoAsistenciaColegio) {
		this.idTipoAsistenciaColegio = idTipoAsistenciaColegio;
	}

	public UsrBean getUsrBean() {
		return usrBean;
	}

	public void setUsrBean(UsrBean usrBean) {
		this.usrBean = usrBean;
	}

	public String getFechaJustificacion() {
		return fechaJustificacion;
	}

	public void setFechaJustificacion(String fechaJustificacion) {
		this.fechaJustificacion = fechaJustificacion;
	}
/*
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idInstitucion == null) ? 0 : idInstitucion.hashCode());
		result = prime * result
				+ ((idPersona == null) ? 0 : idPersona.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VolantesExpressVo))
			return false;
		VolantesExpressVo other = (VolantesExpressVo) obj;
		if (idInstitucion == null) {
			if (other.idInstitucion != null)
				return false;
		} else if (!idInstitucion.equals(other.idInstitucion))
			return false;
		if (idPersona == null) {
			if (other.idPersona != null)
				return false;
		} else if (!idPersona.equals(other.idPersona))
			return false;
		return true;
	}
	*/
	/*public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("Persona [")
			.append("apellidos1=").append(apellidos1)
				.append(", apellidos2=").append(apellidos2)
				.append(", fechaNacimiento=").append(fechaNacimiento)
				.append(", idInstitucion=").append(idInstitucion)
				.append(", idPersona=").append(idPersona)
				.append(", nifCif=").append(nifCif)
				.append(", noAparecerRedAbogacia=").append(noAparecerRedAbogacia)
				.append(", nombre=").append(nombre)
				.append("]");

		return buffer.toString();
	}
	*/

	public String getNombreColegiado() {
		return nombreColegiado;
	}

	public void setNombreColegiado(String nombreColegiado) {
		this.nombreColegiado = nombreColegiado;
	}

	public Boolean getSustituto() {
		return sustituto;
	}

	public void setSustituto(Boolean sustituto) {
		this.sustituto = sustituto;
	}

	public Long getIdColegiadoGuardia() {
		return idColegiadoGuardia;
	}

	public void setIdColegiadoGuardia(Long idColegiadoGuardia) {
		this.idColegiadoGuardia = idColegiadoGuardia;
	}

	public String getDatosAsistencias() {
		return datosAsistencias;
	}

	public void setDatosAsistencias(String datosAsistencias) {
		this.datosAsistencias = datosAsistencias;
	}

	public List<ScsAsistenciasBean> getAsistencias() {
		return asistencias;
	}

	public void setAsistencias(List<ScsAsistenciasBean> asistencias) {
		this.asistencias = asistencias;
	}
}
