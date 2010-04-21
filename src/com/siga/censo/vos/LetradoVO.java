package com.siga.censo.vos;


/**
 */
public class LetradoVO extends PersonaVO {

	private String residencia;
	private String idInstitucion;

	
	public String getId(){
		return getPk(getIdPersona(),getIdInstitucion());
	}
	public void setId(String id){
		String[] values = id.split(PK_SEPARATOR);
		setIdPersona(values[0]);
		setIdInstitucion(values[1]);
	}
	
	public void setResidencia(String residencia) {
		this.residencia = residencia;
	}

	public String getResidencia() {
		return residencia;
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


	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}


	public String getIdInstitucion() {
		return idInstitucion;
	}
}