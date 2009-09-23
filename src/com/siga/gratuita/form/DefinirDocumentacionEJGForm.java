/*
 * Fecha creación: 17/02/2005
 * Autor: julio.vicente
 */

package com.siga.gratuita.form;

import com.siga.beans.*;
import com.siga.general.MasterForm;

/**
 * Maneja el formulario que mantiene la tabla SCS_EJGFORM
 */
public class DefinirDocumentacionEJGForm extends MasterForm {

	private String presentadorAnterior;
	private String idTipoDocumentoAnterior;
	private String idDocumentoAnterior;
	/*
	 * Metodos SET
	 */
	public void setPresentador(String regEntrada) {
		this.datos.put(ScsDocumentacionEJGBean.C_PRESENTADOR, regEntrada);
	}

	public void setIdDocumento(String regEntrada) {
		this.datos.put(ScsDocumentacionEJGBean.C_IDDOCUMENTO, regEntrada);
	}

	public void setIdTipoDocumento(String regEntrada) {
		this.datos.put(ScsDocumentacionEJGBean.C_IDTIPODOCUMENTO, regEntrada);
	}

	/**
	 * Almacena en la Hashtable el reg. entrada
	 * 
	 * @param valor
	 *            reg. entrada
	 * @return void
	 */
	public void setRegEntrada(String regEntrada) {
		this.datos.put(ScsDocumentacionEJGBean.C_REGENTRADA, regEntrada);
	}

	/**
	 * Almacena en la Hashtable el re. salida del EJG
	 * 
	 * @param valor
	 *            reg.salida de la EJG.
	 * @return void
	 */
	public void setRegSalida(String regSalida) {
		this.datos.put(ScsDocumentacionEJGBean.C_REGSALIDA, regSalida);
	}

	/**
	 * Almacena en la Hashtable el anho del EJG
	 * 
	 * @param valor
	 *            Anho de la EJG. De tipo "Integer".
	 * @return void
	 */
	public void setAnio(String anio) {
		this.datos.put(ScsDocumentacionEJGBean.C_ANIO, anio);
	}

	/**
	 * Almacena en la Hashtable el numero del EJG
	 * 
	 * @param valor
	 *            Numero de la EJG. De tipo "String".
	 * @return void
	 */
	public void setNumero(String numero) {
		this.datos.put(ScsDocumentacionEJGBean.C_NUMERO, numero);
	}

	/**
	 * Almacena en la Hashtable el identificador del tipo de EJG del dictamen
	 * del EJG
	 * 
	 * @param valor
	 *            Identificador del tipo de EJG del dictamen del EJG. De tipo
	 *            "String".
	 * @return void
	 */
	public void setIdTipoEJG(String idTipoEJG) {
		this.datos.put(ScsDocumentacionEJGBean.C_IDTIPOEJG, idTipoEJG);
	}

	/**
	 * Almacena en la Hashtable el identificador de la documentacion
	 * 
	 * @param valor
	 *            Identificador documentacion. De tipo "String".
	 * @return void
	 */
	public void setIdDocumentacion(String idDocumentacion) {
		this.datos.put(ScsDocumentacionEJGBean.C_IDDOCUMENTACION,
				idDocumentacion);
	}

	/**
	 * Almacena en la Hashtable la la fecha límite de presentación de
	 * documentación del EJG
	 * 
	 * @param valor
	 *            Fecha de limite presentación documentación. De tipo "String".
	 * @return void
	 */
	public void setFechaLimitePresentacion(String fechaLimite) {
		this.datos.put(ScsDocumentacionEJGBean.C_FECHALIMITE, fechaLimite);
	}

	/**
	 * Almacena en la Hashtable la la fecha de presentación de documentación del
	 * EJG
	 * 
	 * @param valor
	 *            Fecha de ratifiacion de la EJG. De tipo "String".
	 * @return void
	 */
	public void setFechaPresentacion(String fechaPresentacion) {
		this.datos.put(ScsDocumentacionEJGBean.C_FECHAENTREGA,
				fechaPresentacion);
	}

	/**
	 * Almacena en la Hashtable la la fecha de presentación de documentación del
	 * EJG
	 * 
	 * @param valor
	 *            Fecha de ratifiacion de la EJG. De tipo "String".
	 * @return void
	 */
	public void setDocumentacion(String documentacion) {
		this.datos.put(ScsDocumentacionEJGBean.C_DOCUMENTACION, documentacion);
	}

	/**
	 * Almacena en la Hashtable el identificador de la institucion del dictamen
	 * del EJG
	 * 
	 * @param valor
	 *            Identificador de la institucion del dictamen del EJG. De tipo
	 *            "String".
	 * @return void
	 */
	public void setIdInstitucion(String idInstitucion) {
		this.datos.put(ScsDocumentacionEJGBean.C_IDINSTITUCION, idInstitucion);
	}

	/*
	 * Metodos GET
	 */

	public String getPresentador() {
		return this.datos.get(ScsDocumentacionEJGBean.C_PRESENTADOR).toString();
	}

	public String getIdDocumento() {
		return this.datos.get(ScsDocumentacionEJGBean.C_IDDOCUMENTO).toString();
	}

	public String getIdTipoDocumento() {
		return this.datos.get(ScsDocumentacionEJGBean.C_IDTIPODOCUMENTO)
				.toString();
	}

	/**
	 * Recupera de la Hashtable el registro de entrada de la EJG
	 * 
	 * @return registro de entrada de la EJG
	 */
	public String getRegEntrada() {
		return this.datos.get(ScsDocumentacionEJGBean.C_REGENTRADA).toString();
	}

	/**
	 * Recupera de la Hashtable el registro de salida de la EJG
	 * 
	 * @return registro de salida de la EJG
	 */
	public String getRegSalida() {
		return this.datos.get(ScsDocumentacionEJGBean.C_REGSALIDA).toString();
	}

	/**
	 * Recupera de la Hashtable el anho de la EJG
	 * 
	 * @return Anho de la EJG
	 */
	public String getAnio() {
		return this.datos.get(ScsDocumentacionEJGBean.C_ANIO).toString();
	}

	/**
	 * Recupera de la Hashtable el numero de la EJG
	 * 
	 * @return Numero de la EJG
	 */
	public String getNumero() {
		return this.datos.get(ScsDocumentacionEJGBean.C_NUMERO).toString();
	}

	/**
	 * Recupera de la Hashtable el identificador del tipo de la EJG
	 * 
	 * @return Identificador del tipo de la EJG
	 */
	public String getIdTipoEJG() {
		return this.datos.get(ScsDocumentacionEJGBean.C_IDTIPOEJG).toString();
	}

	/**
	 * Recupera de la Hashtable el identificador de la institucion de la EJG
	 * 
	 * @return Identificador de la institucion de la EJG
	 */
	public String getIdInstitucion() {
		return this.datos.get(ScsDocumentacionEJGBean.C_IDINSTITUCION)
				.toString();
	}

	/**
	 * Recupera de la Hashtable la la fecha límite de presentación de
	 * documentación del EJG
	 * 
	 * @return String
	 */
	public String getFechaLimitePresentacion() {
		return this.datos.get(ScsDocumentacionEJGBean.C_FECHALIMITE).toString();
	}

	/**
	 * Recupera de la Hashtable la la fecha de presentación de documentación del
	 * EJG
	 * 
	 * @return String
	 */
	public String getFechaPresentacion() {
		return this.datos.get(ScsDocumentacionEJGBean.C_FECHAENTREGA)
				.toString();
	}

	/**
	 * Recupera de la Hashtable la la fecha de presentación de documentación del
	 * EJG
	 * 
	 * @return String
	 */
	public String getDocumentacion() {
		return this.datos.get(ScsDocumentacionEJGBean.C_DOCUMENTACION)
				.toString();
	}

	/**
	 * Recupera de la Hashtable del identificador de la documentación del EJG
	 * 
	 * @return String
	 */
	public String getIdDocumentacion() {
		return this.datos.get(ScsDocumentacionEJGBean.C_IDDOCUMENTACION)
				.toString();
	}

	/**
	 * @return the presentadorAnterior
	 */
	public String getPresentadorAnterior() {
		return presentadorAnterior;
	}

	/**
	 * @param presentadorAnterior the presentadorAnterior to set
	 */
	public void setPresentadorAnterior(String presentadorAnterior) {
		this.presentadorAnterior = presentadorAnterior;
	}

	/**
	 * @return the idTipoDocumentoAnterior
	 */
	public String getIdTipoDocumentoAnterior() {
		return idTipoDocumentoAnterior;
	}

	/**
	 * @param idTipoDocumentoAnterior the idTipoDocumentoAnterior to set
	 */
	public void setIdTipoDocumentoAnterior(String idTipoDocumentoAnterior) {
		this.idTipoDocumentoAnterior = idTipoDocumentoAnterior;
	}

	/**
	 * @return the idDocumentoAnterior
	 */
	public String getIdDocumentoAnterior() {
		return idDocumentoAnterior;
	}

	/**
	 * @param idDocumentoAnterior the idDocumentoAnterior to set
	 */
	public void setIdDocumentoAnterior(String idDocumentoAnterior) {
		this.idDocumentoAnterior = idDocumentoAnterior;
	}
}