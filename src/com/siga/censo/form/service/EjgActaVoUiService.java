package com.siga.censo.form.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.vo.scs.EjgActaVo;

import com.siga.comun.VoUiService;
import com.siga.gratuita.form.EjgActaForm;

/**
 * 
 * @author jorgeta 
 * @date   02/11/2015
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class EjgActaVoUiService implements VoUiService<EjgActaForm, EjgActaVo> {

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getVo2FormList(java.util.List)
	 */
	public List<EjgActaForm> getVo2FormList(
			List<EjgActaVo> voList) {
		List<EjgActaForm> cargaMasivaCVForms = new ArrayList<EjgActaForm>();
		EjgActaForm cargaMasivaCVForm   = null;
		for (EjgActaVo objectVo : voList) {
			cargaMasivaCVForm = getVo2Form(objectVo);
			cargaMasivaCVForms.add(cargaMasivaCVForm);

		}
		return cargaMasivaCVForms;
	}

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getForm2Vo(java.lang.Object)
	 */
	public EjgActaVo getForm2Vo(EjgActaForm objectForm) {
		EjgActaVo objectVo = new EjgActaVo(); 
		
		objectVo.setIdinstitucionacta(objectForm.getIdInstitucionActa()!=null && !objectForm.getIdInstitucionActa().toString().trim().equals("") ?Short.valueOf(objectForm.getIdInstitucionActa()):null);
		objectVo.setIdacta(objectForm.getIdActa()!=null && !objectForm.getIdActa().toString().trim().equals("") ?Long.valueOf(objectForm.getIdActa()):null);
		objectVo.setAnioacta(objectForm.getAnioActa()!=null && !objectForm.getAnioActa().toString().trim().equals("") ?Short.valueOf(objectForm.getAnioActa()):null);
		objectVo.setIdinstitucionejg(objectForm.getIdInstitucionEjg()!=null && !objectForm.getIdInstitucionEjg().toString().trim().equals("") ?Short.valueOf(objectForm.getIdInstitucionEjg()):null);
		objectVo.setIdtipoejg(objectForm.getIdTipoEjg()!=null && !objectForm.getIdTipoEjg().toString().trim().equals("") ?Short.valueOf(objectForm.getIdTipoEjg()):null);
		objectVo.setAnioejg(objectForm.getAnioEjg()!=null && !objectForm.getAnioEjg().toString().trim().equals("") ?Short.valueOf(objectForm.getAnioEjg()):null);
		objectVo.setNumeroejg(objectForm.getNumeroEjg()!=null && !objectForm.getNumeroEjg().toString().trim().equals("") ?Long.valueOf(objectForm.getNumeroEjg()):null);
		objectVo.setIdfundamentojuridico(objectForm.getIdFundamento()!=null && !objectForm.getIdFundamento().toString().trim().equals("") ?Short.valueOf(objectForm.getIdFundamento()):null);
		objectVo.setIdtiporatificacionejg(objectForm.getIdResolucion()!=null && !objectForm.getIdResolucion().toString().trim().equals("") ?Short.valueOf(objectForm.getIdResolucion()):null);
		
		objectVo.setDescripcionEjg(objectForm.getDescripcionEjg());
		objectVo.setDescripcionResolucion(objectForm.getDescripcionResolucion());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (objectForm.getFechaEjg() != null && !objectForm.getFechaEjg().equals(""))
			try {
				objectVo.setFechaEjg(sdf.parse(objectForm.getFechaEjg()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		objectVo.setTurnoGuardiaEjg(objectForm.getTurnoGuardiaEjg());
		objectVo.setSolicitanteEjg(objectForm.getSolicitanteEjg());
		objectVo.setNombreInstitucionEjg(objectForm.getNombreInstitucionEjg());
		objectVo.setLongitudNumEjg(objectForm.getLongitudNumEjg());
		
		
		return objectVo;
	}

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getVo2Form(java.lang.Object)
	 */
	public EjgActaForm getVo2Form(EjgActaVo objectVo) {
		EjgActaForm objectForm = new EjgActaForm();
		
		return getVo2Form(objectVo,objectForm);
		
	}

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getVo2Form(java.lang.Object, java.lang.Object)
	 */
	public EjgActaForm getVo2Form(EjgActaVo objectVo,
			EjgActaForm objectForm) {
		objectForm.setIdInstitucionActa(objectVo.getIdinstitucionacta()!=null?objectVo.getIdinstitucionacta().toString():"");
		objectForm.setIdActa(objectVo.getIdacta()!=null?objectVo.getIdacta().toString():"");
		objectForm.setAnioActa(objectVo.getAnioacta()!=null?objectVo.getAnioacta().toString():"");
		objectForm.setIdInstitucionEjg(objectVo.getIdinstitucionejg()!=null?objectVo.getIdinstitucionejg().toString():"");
		objectForm.setIdTipoEjg(objectVo.getIdtipoejg()!=null?objectVo.getIdtipoejg().toString():"");
		objectForm.setAnioEjg(objectVo.getAnioejg()!=null?objectVo.getAnioejg().toString():"");
		objectForm.setNumeroEjg(objectVo.getNumeroejg()!=null?objectVo.getNumeroejg().toString():"");
		objectForm.setIdFundamento(objectVo.getIdfundamentojuridico()!=null?objectVo.getIdfundamentojuridico().toString():"");
		objectForm.setIdResolucion(objectVo.getIdtiporatificacionejg()!=null?objectVo.getIdtiporatificacionejg().toString():"");
		
		objectForm.setDescripcionEjg(objectVo.getDescripcionEjg()!=null?objectVo.getDescripcionEjg():"");
		objectForm.setDescripcionResolucion(objectVo.getDescripcionResolucion()!=null?objectVo.getDescripcionResolucion():"");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (objectVo.getFechaEjg() != null && !objectVo.getFechaEjg().equals(""))
			objectForm.setFechaEjg(sdf.format(objectVo.getFechaEjg()));
		
		objectForm.setTurnoGuardiaEjg(objectVo.getTurnoGuardiaEjg()!=null?objectVo.getTurnoGuardiaEjg():"");
		objectForm.setSolicitanteEjg(objectVo.getSolicitanteEjg()!=null?objectVo.getSolicitanteEjg():"");
		objectForm.setNombreInstitucionEjg(objectVo.getNombreInstitucionEjg()!=null?objectVo.getNombreInstitucionEjg():"");
		objectForm.setLongitudNumEjg(objectVo.getLongitudNumEjg());
		
		return objectForm;
	}









}
