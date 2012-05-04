package com.siga.censo.service;

import com.atos.utils.UsrBean;
import com.siga.censo.form.AlterMutuaForm;
import com.siga.ws.alterMutua.WSRespuesta;

import es.satec.businessManager.BusinessService;

public interface AlterMutuaService extends BusinessService{
	
	/**
	 * Obtiene las propuestas que el colegiado puede contratar
	 * @param alterMutuaForm Requiere el tipoIdentificador, identificador, fechaNacimiento, sexo y propuesta
	 * @return
	 * @throws Exception
	 */
	public AlterMutuaForm getPropuestas(AlterMutuaForm alterMutuaForm, UsrBean usr) throws Exception;
	
	/**
	 * Obtiene el estado en que se encuentra el colegiado
	 * @param alterMutuaForm Requiere el tipoIdentificador e identificador
	 * @return
	 * @throws Exception
	 */
	public AlterMutuaForm getEstadoColegiado(AlterMutuaForm alterMutuaForm, UsrBean usr) throws Exception;
	
	/**
	 * Obtiene el estado en que se encuentra una solicitud
	 * @param alterMutuaForm Requiere el idSolicitud y duplicado (para descargar el PDF)
	 * @return
	 * @throws Exception
	 */
	public AlterMutuaForm getEstadoSolicitud(AlterMutuaForm alterMutuaForm, UsrBean usr) throws Exception;
	
	/**
	 * Realiza la solicitud de alta a alter mutua
	 * @param alterMutuaForm Con todos los campos posibles rellenos
	 * @return
	 * @throws Exception
	 */
	public AlterMutuaForm setSolicitudAlta(AlterMutuaForm alterMutuaForm, UsrBean usr) throws Exception;

	/**
	 * Rellena el formulario con datos de la solicitud de incorporacion
	 * @param alterMutuaForm Con todos los campos posibles rellenos
	 * @return
	 * @throws Exception
	 */
	public AlterMutuaForm getDatosSolicitud(AlterMutuaForm form, UsrBean usr) throws Exception;
	
	/**
	 * Obtiene la tarifa final del alta en alter mutua
	 * @param alterMutuaForm Con todos los campos posibles rellenos
	 * @return
	 * @throws Exception
	 */
	public AlterMutuaForm getTarifaSolicitud(AlterMutuaForm alterMutuaForm, UsrBean usr) throws Exception;
}
