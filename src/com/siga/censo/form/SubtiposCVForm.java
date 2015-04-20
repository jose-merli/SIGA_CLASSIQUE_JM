package com.siga.censo.form;


import com.siga.general.MasterForm;
import com.siga.tlds.FilaExtElement;
/***
 * 
 * @author jorgeta 
 * @date   26/01/2015
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class SubtiposCVForm extends MasterForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Es la institucion de consulta
	 */
	String idInstitucion;
	String subTipo1Descripcion;
	String subTipo2Descripcion;
	String subTipo1IdInstitucion;
	String subTipo1IdTipo;
	
	String subTipo2IdTipo;
	String subTipo2IdInstitucion;
	String tipoDescripcion;
	String idTipoCV;
	String codigoExt;
	String subTipo;
	String subTipoDescripcion;
	String subTipoCodigoExt;
	String subTipo1CodigoExt;
	String subTipo2CodigoExt;
	
	String subTipo1IdRecursoDescripcion;
	String subTipo2IdRecursoDescripcion;
	
	private FilaExtElement[] elementosFila;
	private String botones;
	
	/**
	 * @return the idInstitucion
	 */
	public String getIdInstitucion() {
		return idInstitucion;
	}
	/**
	 * @param idInstitucion the idInstitucion to set
	 */
	public void setIdInstitucion(String idInstitucion) {
		this.idInstitucion = idInstitucion;
	}
	/**
	 * @return the subTipo1Descripcion
	 */
	public String getSubTipo1Descripcion() {
		return subTipo1Descripcion;
	}
	/**
	 * @param subTipo1Descripcion the subTipo1Descripcion to set
	 */
	public void setSubTipo1Descripcion(String subTipo1Descripcion) {
		this.subTipo1Descripcion = subTipo1Descripcion;
	}
	/**
	 * @return the subTipo2Descripcion
	 */
	public String getSubTipo2Descripcion() {
		return subTipo2Descripcion;
	}
	/**
	 * @param subTipo2Descripcion the subTipo2Descripcion to set
	 */
	public void setSubTipo2Descripcion(String subTipo2Descripcion) {
		this.subTipo2Descripcion = subTipo2Descripcion;
	}
	/**
	 * @return the tipoDescripcion
	 */
	public String getTipoDescripcion() {
		return tipoDescripcion;
	}
	/**
	 * @param tipoDescripcion the tipoDescripcion to set
	 */
	public void setTipoDescripcion(String tipoDescripcion) {
		this.tipoDescripcion = tipoDescripcion;
	}
	

	

	public void clear() {
		
	}

	public SubtiposCVForm clone() {
		SubtiposCVForm miForm = new SubtiposCVForm();
		miForm.setIdInstitucion(this.getIdInstitucion());
		miForm.setTipoDescripcion(this.getTipoDescripcion());
		miForm.setSubTipo1Descripcion(this.getSubTipo1Descripcion());
		miForm.setSubTipo2Descripcion(this.getSubTipo2Descripcion());
		
		

		return miForm;
		
	}
	/**
	 * @return the subTipo1IdInstitucion
	 */
	public String getSubTipo1IdInstitucion() {
		return subTipo1IdInstitucion;
	}
	/**
	 * @param subTipo1IdInstitucion the subTipo1IdInstitucion to set
	 */
	public void setSubTipo1IdInstitucion(String subTipo1IdInstitucion) {
		this.subTipo1IdInstitucion = subTipo1IdInstitucion;
	}
	/**
	 * @return the subTipo2IdInstitucion
	 */
	public String getSubTipo2IdInstitucion() {
		return subTipo2IdInstitucion;
	}
	/**
	 * @param subTipo2IdInstitucion the subTipo2IdInstitucion to set
	 */
	public void setSubTipo2IdInstitucion(String subTipo2IdInstitucion) {
		this.subTipo2IdInstitucion = subTipo2IdInstitucion;
	}
	/**
	 * @return the idTipoCV
	 */
	public String getIdTipoCV() {
		return idTipoCV;
	}
	/**
	 * @param idTipoCV the idTipoCV to set
	 */
	public void setIdTipoCV(String idTipoCV) {
		this.idTipoCV = idTipoCV;
	}
	/**
	 * @return the subTipoDescripcion
	 */
	public String getSubTipoDescripcion() {
		return subTipoDescripcion;
	}
	/**
	 * @param subTipoDescripcion the subTipoDescripcion to set
	 */
	public void setSubTipoDescripcion(String subTipoDescripcion) {
		this.subTipoDescripcion = subTipoDescripcion;
	}
	/**
	 * @return the subTipoCodigoExt
	 */
	public String getSubTipoCodigoExt() {
		return subTipoCodigoExt;
	}
	/**
	 * @param subTipoCodigoExt the subTipoCodigoExt to set
	 */
	public void setSubTipoCodigoExt(String subTipoCodigoExt) {
		this.subTipoCodigoExt = subTipoCodigoExt;
	}
	/**
	 * @return the subTipo
	 */
	public String getSubTipo() {
		return subTipo;
	}
	/**
	 * @param subTipo the subTipo to set
	 */
	public void setSubTipo(String subTipo) {
		this.subTipo = subTipo;
	}
	/**
	 * @return the subTipo1IdTipo
	 */
	public String getSubTipo1IdTipo() {
		return subTipo1IdTipo;
	}
	/**
	 * @param subTipo1IdTipo the subTipo1IdTipo to set
	 */
	public void setSubTipo1IdTipo(String subTipo1IdTipo) {
		this.subTipo1IdTipo = subTipo1IdTipo;
	}
	/**
	 * @return the subTipo2IdTipo
	 */
	public String getSubTipo2IdTipo() {
		return subTipo2IdTipo;
	}
	/**
	 * @param subTipo2IdTipo the subTipo2IdTipo to set
	 */
	public void setSubTipo2IdTipo(String subTipo2IdTipo) {
		this.subTipo2IdTipo = subTipo2IdTipo;
	}
	/**
	 * @return the subTipo1CodigoExt
	 */
	public String getSubTipo1CodigoExt() {
		return subTipo1CodigoExt;
	}
	/**
	 * @param subTipo1CodigoExt the subTipo1CodigoExt to set
	 */
	public void setSubTipo1CodigoExt(String subTipo1CodigoExt) {
		this.subTipo1CodigoExt = subTipo1CodigoExt;
	}
	/**
	 * @return the subTipo2CodigoExt
	 */
	public String getSubTipo2CodigoExt() {
		return subTipo2CodigoExt;
	}
	/**
	 * @param subTipo2CodigoExt the subTipo2CodigoExt to set
	 */
	public void setSubTipo2CodigoExt(String subTipo2CodigoExt) {
		this.subTipo2CodigoExt = subTipo2CodigoExt;
	}
	/**
	 * @return the subTipo1IdRecursoDescripcion
	 */
	public String getSubTipo1IdRecursoDescripcion() {
		return subTipo1IdRecursoDescripcion;
	}
	/**
	 * @param subTipo1IdRecursoDescripcion the subTipo1IdRecursoDescripcion to set
	 */
	public void setSubTipo1IdRecursoDescripcion(String subTipo1IdRecursoDescripcion) {
		this.subTipo1IdRecursoDescripcion = subTipo1IdRecursoDescripcion;
	}
	/**
	 * @return the subTipo2IdRecursoDescripcion
	 */
	public String getSubTipo2IdRecursoDescripcion() {
		return subTipo2IdRecursoDescripcion;
	}
	/**
	 * @param subTipo2IdRecursoDescripcion the subTipo2IdRecursoDescripcion to set
	 */
	public void setSubTipo2IdRecursoDescripcion(String subTipo2IdRecursoDescripcion) {
		this.subTipo2IdRecursoDescripcion = subTipo2IdRecursoDescripcion;
	}
	
	public FilaExtElement[] getElementosFila() {
		elementosFila = new FilaExtElement[3];
		return elementosFila;
	}
	public String getBotones() {
		if((idInstitucion!=null && subTipo1IdInstitucion!=null && idInstitucion.equals(subTipo1IdInstitucion))
			||(idInstitucion!=null && subTipo2IdInstitucion!=null && idInstitucion.equals(subTipo2IdInstitucion))
				){
			botones  = "B,E";	
		}else {
			botones= "";
		}
		

	return botones;
}
	/**
	 * @return the codigoExt
	 */
	public String getCodigoExt() {
		return codigoExt;
	}
	/**
	 * @param codigoExt the codigoExt to set
	 */
	public void setCodigoExt(String codigoExt) {
		this.codigoExt = codigoExt;
	}

	
	
	
	
	
}