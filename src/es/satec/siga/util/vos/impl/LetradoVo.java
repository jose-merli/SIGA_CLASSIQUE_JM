package es.satec.siga.util.vos.impl;

import java.util.Date;

import es.satec.siga.util.vos.Vo;

public class LetradoVo implements Vo{
	private Long idPersona;
	private String nifCif;
	private String nombre;
	private String apellidos1;
	private String apellidos2;
	private Date fechaNacimiento;
	private Integer idInstitucion;
	private Boolean noAparecerRedAbogacia;

	public String getId() {
		return getIdPersona().toString();
	}
	public Long getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	public String getNifCif() {
		return nifCif;
	}
	public void setNifCif(String nifCif) {
		this.nifCif = nifCif;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos1() {
		return apellidos1;
	}
	public void setApellidos1(String apellidos1) {
		this.apellidos1 = apellidos1;
	}
	public String getApellidos2() {
		return apellidos2;
	}
	public void setApellidos2(String apellidos2) {
		this.apellidos2 = apellidos2;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public Integer getIdInstitucion() {
		return idInstitucion;
	}
	public void setIdInstitucion(Integer idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	public Boolean getNoAparecerRedAbogacia() {
		return noAparecerRedAbogacia;
	}
	public void setNoAparecerRedAbogacia(Boolean noAparecerRedAbogacia) {
		this.noAparecerRedAbogacia = noAparecerRedAbogacia;
	}

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
		if (!(obj instanceof LetradoVo))
			return false;
		LetradoVo other = (LetradoVo) obj;
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
	
	public String toString() {
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
}
