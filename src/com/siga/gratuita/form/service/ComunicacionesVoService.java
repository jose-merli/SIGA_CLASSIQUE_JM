package com.siga.gratuita.form.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.vo.env.ComunicacionesVo;

import com.siga.comun.VoUiService;
import com.siga.gratuita.form.ComunicacionesForm;

/**
 * 
 * @author jorgeta 
 * @date   18/07/2016
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class ComunicacionesVoService implements VoUiService<ComunicacionesForm, ComunicacionesVo> {
	
	public List<ComunicacionesForm> getVo2FormList(
			List<ComunicacionesVo> voList) {
		List<ComunicacionesForm> comunicacionesForms = new ArrayList<ComunicacionesForm>();
		ComunicacionesForm ComunicacionesForm   = null;
		for (ComunicacionesVo objectVo : voList) {
			ComunicacionesForm = getVo2Form(objectVo);
			comunicacionesForms.add(ComunicacionesForm);
			
		}
		return comunicacionesForms;
	}

	public ComunicacionesVo getForm2Vo(ComunicacionesForm objectForm) {
		ComunicacionesVo solicitudAceptadaCentralitaVo = new ComunicacionesVo();
		return solicitudAceptadaCentralitaVo;
	}
	
	public ComunicacionesForm getVo2Form(ComunicacionesVo objectVo) {
		return getVo2Form(objectVo,new ComunicacionesForm());
	}
	public ComunicacionesForm getVo2Form(ComunicacionesVo objectVo, ComunicacionesForm comunicacionesForm) {
		SimpleDateFormat sdfddmmyyyyhhmm = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		if(objectVo.getIdInstitucion()!=null)
			comunicacionesForm.setIdInstitucion(objectVo.getIdInstitucion().toString());
		if(objectVo.getFecha()!=null && !objectVo.getFecha().equals("")){
			comunicacionesForm.setFecha(sdfddmmyyyyhhmm.format(objectVo.getFecha()));
		}
		if(objectVo.getIdPersona()!=null)
			comunicacionesForm.setIdPersona(objectVo.getIdPersona().toString());
		if(objectVo.getIdEnvio()!=null)
			comunicacionesForm.setIdEnvio(objectVo.getIdEnvio().toString());
		comunicacionesForm.setTipo(objectVo.getTipo());
		comunicacionesForm.setNombre(objectVo.getNombre());
		comunicacionesForm.setAsunto(objectVo.getAsunto());
		comunicacionesForm.setDocumentos(objectVo.getDocumentos());
		
		
		
		return comunicacionesForm;
	}




	

	
	
	

}
