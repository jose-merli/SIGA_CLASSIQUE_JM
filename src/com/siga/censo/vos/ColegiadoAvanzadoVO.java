package com.siga.censo.vos;

import java.util.Date;

public class ColegiadoAvanzadoVO extends ClienteVO {

	private int tipoColegiacion;
	private String residente;
	private String ejerciente;
	private String comunitario;
	private Date fechaIncorporacionHasta;
	private String letrado;
		
	public ColegiadoAvanzadoVO(){
	}

	public String getId(){
		return getPk(getIdPersona(),getIdInstitucion());
	}
	public void setId(String id){
		String[] values = id.split(PK_SEPARATOR);
		setIdPersona(values[0]);
		setIdInstitucion(values[1]);
	}
	
	public int getTipoColegiacion() {
		return tipoColegiacion;
	}

	public void setTipoColegiacion(int tipoColegiacion) {
		this.tipoColegiacion = tipoColegiacion;
	}

	public String getResidente() {
		return residente;
	}

	public void setResidente(String residente) {
		this.residente = residente;
	}

	public String getEjerciente() {
		return ejerciente;
	}

	public void setEjerciente(String ejerciente) {
		this.ejerciente = ejerciente;
	}

	public String getComunitario() {
		return comunitario;
	}

	public void setComunitario(String comunitario) {
		this.comunitario = comunitario;
	}

	public Date getFechaIncorporacionHasta() {
		return fechaIncorporacionHasta;
	}

	public void setFechaIncorporacionHasta(Date fechaIncorporacionHasta) {
		this.fechaIncorporacionHasta = fechaIncorporacionHasta;
	}
	
	/**
	 * 
	 * @return Los apellidos concatenados
	 */
	public String getApellidos(){
		StringBuffer apellidos = new StringBuffer();
		if (null != getApellidos1())
			apellidos.append(getApellidos1());
		if (null != getApellidos2()){
			apellidos.append(" ");
			apellidos.append(getApellidos2());
		}
		return apellidos.toString();
	}

	public void setLetrado(String letrado) {
		this.letrado = letrado;
	}

	public String getLetrado() {
		return letrado;
	}

}
