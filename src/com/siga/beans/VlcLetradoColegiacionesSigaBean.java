/*
 * Created on 14-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.beans;

/**
 * @author daniel.campos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VlcLetradoColegiacionesSigaBean extends MasterBean {
	//private static final long serialVersionUID = -2510899080052901261L;
	private String 	id_letrado, id_colegio, descripcion, num_colegiado, residencia, ejerciente, fecha_modificacion, fecha_alta, colegio_cgae;

	//private String identificadorDS;
	
	
	static public String T_NOMBRETABLA = "VLC_LETRADO_COLEGIACIONES_SIGA";
	/* Nombre campos de la tabla */
	//id_letrado, id_colegio, descripcion, num_colegiado, residencia, ejerciente,
	 //  fecha_modificacion, fecha_alta, colegio_cgae
	static public final String C_IDINSTITUCION		= "id_colegio";
	static public final String C_IDPERSONA			= "id_letrado";
	static public final String C_DESCRIPCION		= "descripcion";
	static public final String C_FECHAALTA   		= "fecha_alta";
	static public final String C_FECHAMODFICACION	= "fecha_modificacion";
	static public final String C_RESIDENCIA 		= "residencia";
	static public final String C_EJERCIENTE 		= "ejerciente";
	static public final String C_COLEGIOCGAE		= "colegio_cgae";

	public String getId_letrado() {
		return id_letrado;
	}
	public void setId_letrado(String id_letrado) {
		this.id_letrado = id_letrado;
	}
	public String getId_colegio() {
		return id_colegio;
	}
	public void setId_colegio(String id_colegio) {
		this.id_colegio = id_colegio;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNum_colegiado() {
		return num_colegiado;
	}
	public void setNum_colegiado(String num_colegiado) {
		this.num_colegiado = num_colegiado;
	}
	public String getResidencia() {
		return residencia;
	}
	public void setResidencia(String residencia) {
		this.residencia = residencia;
	}
	public String getEjerciente() {
		return ejerciente;
	}
	public void setEjerciente(String ejerciente) {
		this.ejerciente = ejerciente;
	}
	public String getFecha_modificacion() {
		return fecha_modificacion;
	}
	public void setFecha_modificacion(String fecha_modificacion) {
		this.fecha_modificacion = fecha_modificacion;
	}
	public String getFecha_alta() {
		return fecha_alta;
	}
	public void setFecha_alta(String fecha_alta) {
		this.fecha_alta = fecha_alta;
	}
	public String getColegio_cgae() {
		return colegio_cgae;
	}
	public void setColegio_cgae(String colegio_cgae) {
		this.colegio_cgae = colegio_cgae;
	}
	/* Variables */
	

}