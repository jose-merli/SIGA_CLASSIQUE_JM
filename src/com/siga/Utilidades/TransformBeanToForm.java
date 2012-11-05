package com.siga.Utilidades;

import java.text.SimpleDateFormat;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.autogen.model.EnvEntradaEnvios;
import org.redabogacia.sigaservices.app.autogen.model.ScsJuzgado;
import org.redabogacia.sigaservices.app.services.scs.ScsJuzgadoService;

import com.atos.utils.ClsConstants;
import com.siga.envios.form.EntradaEnviosForm;

import es.satec.businessManager.BusinessManager;

public class TransformBeanToForm {

	public static EntradaEnviosForm getEntradaEnviosForm(EnvEntradaEnvios entrada){
		
		EntradaEnviosForm formulario = new EntradaEnviosForm();
		SimpleDateFormat sdf = new SimpleDateFormat(ClsConstants.DATE_FORMAT_SHORT_SPANISH);
		
		if(entrada.getIdenvio() != null){
			formulario.setIdEnvio(entrada.getIdenvio().toString());
			
		}
		
		if(entrada.getIdentintercambio() != null){
			formulario.setIdentificador(entrada.getIdentintercambio().toString());
		}
		
		if(entrada.getIdentintercambiotrel() != null){
			formulario.setIdentificadorRelacionado(entrada.getIdentintercambiotrel().toString());
		}
		if(entrada.getIdenviorel() != null){
			formulario.setIdEnvioRelacionado(entrada.getIdenviorel().toString());
			
		}
		
		if(entrada.getRemitenteId() != null){
			ScsJuzgado obj = new ScsJuzgado();
			obj.setIdjuzgado(entrada.getRemitenteId());
			obj.setIdinstitucion(entrada.getRemitenteIdinstitucion());
			ScsJuzgadoService juzgadoService = (ScsJuzgadoService) BusinessManager.getInstance().getService(ScsJuzgadoService.class);
			obj = juzgadoService.get(obj);
			formulario.setDescripcionRemitente(obj.getNombre());
		}
		
		
		if(entrada.getFechapeticion() != null){
			formulario.setFechaPeticion(sdf.format(entrada.getFechapeticion()));
		}
		
		if(entrada.getFecharespuesta() != null){
			formulario.setFechaRespuesta(sdf.format(entrada.getFecharespuesta()));
		}
		
		if(entrada.getAsunto() != null){
			formulario.setAsunto(entrada.getAsunto());
		}
		
		if(entrada.getIdestado() != null){
			formulario.setIdEstado(entrada.getIdestado().toString());
			formulario.setDescripcionEstado(AppConstants.EstadosEntradaEnviosEnum.getEnum(entrada.getIdestado()).getDescripcion());
		}
		
		if(entrada.getIdtipointercambiotelematico() != null){
			formulario.setIdTipoIntercambioTelematico(entrada.getIdtipointercambiotelematico().toString());
		}		
		
		if(entrada.getIdinstitucion() != null){
			formulario.setIdInstitucion(entrada.getIdinstitucion().toString());
		}
		if(entrada.getNecesitarespuesta() != null){
			formulario.setNecesitaRespuesta(entrada.getNecesitarespuesta().toString());
		}
		
		
		

		return formulario;
	}
}
