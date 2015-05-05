package com.siga.censo.form;


/**
 * 
 * @author jorgeta 
 * @date   06/04/2015
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class CargaMasivaGFForm extends CargaMasivaForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private String idGrupo;
	private String nombreGrupo;
	
	
	public void clear() {
		setModo("");
	}

	public CargaMasivaGFForm clone() {
		CargaMasivaGFForm miForm = new CargaMasivaGFForm();
		miForm.setIdInstitucion(this.getIdInstitucion());
		miForm.setFechaCarga(this.getFechaCarga());
		miForm.setTheFile(this.getTheFile());
		return miForm;
	}

	/**
	 * @return the idGrupo
	 */
	public String getIdGrupo() {
		return idGrupo;
	}

	/**
	 * @param idGrupo the idGrupo to set
	 */
	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}

	/**
	 * @return the nombreGrupo
	 */
	public String getNombreGrupo() {
		return nombreGrupo;
	}

	/**
	 * @param nombreGrupo the nombreGrupo to set
	 */
	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}
	
	/**
	 * @return the colegiadoNif
	 */
	
	
	
	

	
}