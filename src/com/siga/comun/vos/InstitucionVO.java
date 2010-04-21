package com.siga.comun.vos;

public class InstitucionVO extends Vo {

	private String nombre;
	private String idInstitucion;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	@Override
	public String getId() {
		return idInstitucion;
	}

	@Override
	public void setId(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	
}
