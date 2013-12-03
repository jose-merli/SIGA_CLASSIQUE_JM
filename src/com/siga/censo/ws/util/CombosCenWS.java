package com.siga.censo.ws.util;

import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.AppConstants.ECOM_CEN_MAESESTADOCOLEGIAL;
import org.redabogacia.sigaservices.app.AppConstants.ECOM_CEN_MAESESTADOENVIO;
import org.redabogacia.sigaservices.app.AppConstants.ECOM_CEN_SEXO;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenSituacionejerciente;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenSituacionejercienteExample;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenTipoidentificacion;
import org.redabogacia.sigaservices.app.autogen.model.EcomCenTipoidentificacionExample;
import org.redabogacia.sigaservices.app.services.ecom.EcomCenSituacionEjercienteService;
import org.redabogacia.sigaservices.app.services.ecom.EcomCenTipoIdentificacionService;

import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.comun.vos.ValueKeyVO;

import es.satec.businessManager.BusinessManager;

public class CombosCenWS {
	
	public static List<ValueKeyVO> getSituacionesEjeciente(UsrBean usrBean) {
		List<ValueKeyVO> lista = null;
		EcomCenSituacionEjercienteService ecomCenSituacionEjercienteService = (EcomCenSituacionEjercienteService) BusinessManager.getInstance().getService(EcomCenSituacionEjercienteService.class);
		EcomCenSituacionejercienteExample ecomCenSituacionejercienteExample = new EcomCenSituacionejercienteExample();
		
		List<EcomCenSituacionejerciente> listEcomCenSituacionejercientes = ecomCenSituacionEjercienteService.getList(ecomCenSituacionejercienteExample);
		if (listEcomCenSituacionejercientes != null) {
			lista = new ArrayList<ValueKeyVO>();
			for (EcomCenSituacionejerciente ecomCenSituacionejerciente : listEcomCenSituacionejercientes) {
				lista.add(getValueKeyVO(usrBean, ecomCenSituacionejerciente.getIdecomcensosituacionejer().toString(), ecomCenSituacionejerciente.getDescripcion()));
			}
		}
		return lista;
	}
	
	public static List<ValueKeyVO> getTiposIdentificacion(UsrBean usrBean) {
		List<ValueKeyVO> lista = null;
		
		EcomCenTipoIdentificacionService ecomCenTipoIdentificacionService = (EcomCenTipoIdentificacionService) BusinessManager.getInstance().getService(EcomCenTipoIdentificacionService.class);
		EcomCenTipoidentificacionExample ecomCenTipoidentificacionExample = new EcomCenTipoidentificacionExample();
		//debe traer toda la lista
		List<EcomCenTipoidentificacion> listEcomCenTipoidentificacions = ecomCenTipoIdentificacionService.getList(ecomCenTipoidentificacionExample);
		if (listEcomCenTipoidentificacions != null) {
			lista = new ArrayList<ValueKeyVO>();
			for (EcomCenTipoidentificacion ecomCenTipoidentificacion : listEcomCenTipoidentificacions) {
				lista.add(getValueKeyVO(usrBean, ecomCenTipoidentificacion.getIdcensotipoidentificacion().toString(), ecomCenTipoidentificacion.getDescripcion()));
			}
		}
		return lista;
	}
	
	public static List<ValueKeyVO> getEstadosColegiado(UsrBean usrBean) {
		List<ValueKeyVO> lista = new ArrayList<ValueKeyVO>();		
		for (ECOM_CEN_MAESESTADOCOLEGIAL maestroEstadosColegiado : ECOM_CEN_MAESESTADOCOLEGIAL.values()) {
			lista.add(getValueKeyVO(usrBean, String.valueOf(maestroEstadosColegiado.getCodigo()), maestroEstadosColegiado.getRecurso()));
		}
		
		return lista;
	}
	
	public static List<ValueKeyVO> getEstadosEnvio(UsrBean usrBean) {
		List<ValueKeyVO> lista = new ArrayList<ValueKeyVO>();		
		for (ECOM_CEN_MAESESTADOENVIO maestroEstadosEnvio : ECOM_CEN_MAESESTADOENVIO.values()) {
			lista.add(getValueKeyVO(usrBean, String.valueOf(maestroEstadosEnvio.getCodigo()), maestroEstadosEnvio.getRecurso()));
		}
		
		return lista;
	}

	private static ValueKeyVO getValueKeyVO(UsrBean usrBean, String key, String value) {
		ValueKeyVO valueKeyVO = new ValueKeyVO();
		valueKeyVO.setKey(key);
		valueKeyVO.setValue(UtilidadesString.getMensajeIdioma(usrBean, value));
		return valueKeyVO;
	}
	
	

	public static List<ValueKeyVO> getSexos(UsrBean userBean) {
		List<ValueKeyVO> lista = new ArrayList<ValueKeyVO>();		
		for (ECOM_CEN_SEXO ecomCen_SEXO : ECOM_CEN_SEXO.values()) {
			lista.add(getValueKeyVO(userBean, ecomCen_SEXO.getCodigo(), ecomCen_SEXO.getRecurso()));
		}
		
		return lista;
	}
}
