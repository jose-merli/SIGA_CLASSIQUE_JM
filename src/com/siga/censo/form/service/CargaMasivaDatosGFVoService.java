package com.siga.censo.form.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.vo.cen.CargaMasivaDatosGFVo;

import com.siga.censo.form.CargaMasivaGFForm;
import com.siga.comun.VoUiService;

/**
 * 
 * @author jorgeta 
 * @date   04/05/2015
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class CargaMasivaDatosGFVoService implements VoUiService<CargaMasivaGFForm, CargaMasivaDatosGFVo> {

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getVo2FormList(java.util.List)
	 */
	@Override
	public List<CargaMasivaGFForm> getVo2FormList(
			List<CargaMasivaDatosGFVo> voList) {
		List<CargaMasivaGFForm> cargaMasivaCVForms = new ArrayList<CargaMasivaGFForm>();
		CargaMasivaGFForm cargaMasivaCVForm   = null;
		for (CargaMasivaDatosGFVo objectVo : voList) {
			cargaMasivaCVForm = getVo2Form(objectVo);
			cargaMasivaCVForms.add(cargaMasivaCVForm);

		}
		return cargaMasivaCVForms;
	}

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getForm2Vo(java.lang.Object)
	 */
	@Override
	public CargaMasivaDatosGFVo getForm2Vo(CargaMasivaGFForm objectForm) {
		CargaMasivaDatosGFVo objectVo = new CargaMasivaDatosGFVo(); 
		if(objectForm.getIdGrupo()!=null && !objectForm.getIdGrupo().equals("")){
			objectVo.setIdGrupo(Short.valueOf(objectForm.getIdGrupo()));
		}
		if(objectForm.getNombreGrupo()!=null && !objectForm.getNombreGrupo().equals("")){
			objectVo.setNombreGrupo(objectForm.getNombreGrupo());
		}
		
		
		if(objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equals("")){
			objectVo.setIdInstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		}
		if(objectForm.getIdFichero()!=null && !objectForm.getIdFichero().equals("")){
			objectVo.setIdFichero(Long.valueOf(objectForm.getIdFichero()));
		}
		if(objectForm.getIdFicheroLog()!=null && !objectForm.getIdFicheroLog().equals("")){
			objectVo.setIdFicheroLog(Long.valueOf(objectForm.getIdFicheroLog()));
		}
		
		if(objectForm.getPersonaNif()!=null && !objectForm.getPersonaNif().equals(""))
			objectVo.setPersonaNif(objectForm.getPersonaNif());

		if(objectForm.getPersonaNombre()!=null && !objectForm.getPersonaNombre().equals(""))
			objectVo.setPersonaNombre(objectForm.getPersonaNombre());
		if(objectForm.getColegiadoNumero()!=null && !objectForm.getColegiadoNumero().equals(""))
			objectVo.setColegiadoNumero(objectForm.getColegiadoNumero());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(objectForm.getFechaCarga()!=null && !objectForm.getFechaCarga().equals(""))
			try {
				objectVo.setFechaCarga(sdf.parse(objectForm.getFechaCarga()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		if(objectForm.getIdCargaMasivaCV()!=null && !objectForm.getIdCargaMasivaCV().equals(""))
			objectVo.setIdCargaMasivaCV(Integer.valueOf(objectForm.getIdCargaMasivaCV()));

		if(objectForm.getNombreFichero()!=null && !objectForm.getNombreFichero().equals(""))
			objectVo.setNombreFichero(objectForm.getNombreFichero());
		if(objectForm.getNumRegistros()!=null && !objectForm.getNumRegistros().equals(""))
			objectVo.setNumRegistros(Short.valueOf(objectForm.getNumRegistros()));
		if(objectForm.getUsuario()!=null && !objectForm.getUsuario().equals(""))
			objectVo.setUsuario(objectForm.getUsuario());
		if(objectForm.getError()!=null && !objectForm.getError().equals(""))
			objectVo.setError(objectForm.getError());
		
		objectVo.setCodIdioma(objectForm.getCodIdioma());
		return objectVo;
	}

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getVo2Form(java.lang.Object)
	 */
	@Override
	public CargaMasivaGFForm getVo2Form(CargaMasivaDatosGFVo objectVo) {
		CargaMasivaGFForm objectForm = new CargaMasivaGFForm();

		if(objectVo.getIdGrupo()!=null && !objectVo.getIdGrupo().equals("")){
			objectForm.setIdGrupo(objectVo.getIdGrupo().toString());
		}
		if(objectVo.getNombreGrupo()!=null && !objectVo.getNombreGrupo().equals("")){
			objectForm.setNombreGrupo(objectVo.getNombreGrupo());
		}
		
		if(objectVo.getIdInstitucion()!=null && !objectVo.getIdInstitucion().equals("")){
			objectForm.setIdInstitucion(objectVo.getIdInstitucion().toString());
		}
		if(objectVo.getIdFichero()!=null && !objectVo.getIdFichero().equals("")){
			objectForm.setIdFichero(objectVo.getIdFichero().toString());
		}
		if(objectVo.getIdFicheroLog()!=null && !objectVo.getIdFicheroLog().equals("")){
			objectForm.setIdFicheroLog(objectVo.getIdFicheroLog().toString());
		}
		if(objectVo.getPersonaNif()!=null && !objectVo.getPersonaNif().equals(""))
			objectForm.setPersonaNif(objectVo.getPersonaNif());

		if(objectVo.getPersonaNombre()!=null && !objectVo.getPersonaNombre().equals(""))
			objectForm.setPersonaNombre(objectVo.getPersonaNombre());
		if(objectVo.getColegiadoNumero()!=null && !objectVo.getColegiadoNumero().equals(""))
			objectForm.setColegiadoNumero(objectVo.getColegiadoNumero());

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		if(objectVo.getFechaCarga()!=null && !objectVo.getFechaCarga().equals(""))
			objectForm.setFechaCarga(sdf.format(objectVo.getFechaCarga()));
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(objectVo.getIdCargaMasivaCV()!=null && !objectVo.getIdCargaMasivaCV().equals(""))
			objectForm.setIdCargaMasivaCV(objectVo.getIdCargaMasivaCV().toString());
		if(objectVo.getNombreFichero()!=null && !objectVo.getNombreFichero().equals(""))
			objectForm.setNombreFichero(objectVo.getNombreFichero());
		if(objectVo.getNumRegistros()!=null && !objectVo.getNumRegistros().equals(""))
			objectForm.setNumRegistros(objectVo.getNumRegistros().toString());
		if(objectVo.getUsuario()!=null && !objectVo.getUsuario().equals(""))
			objectForm.setUsuario(objectVo.getUsuario());
		if(objectVo.getError()!=null && !objectVo.getError().equals(""))
			objectForm.setError(objectVo.getError());
		else
			objectForm.setError("");
		
		objectForm.setCodIdioma(objectVo.getCodIdioma());
		return objectForm;
	}

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getVo2Form(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CargaMasivaGFForm getVo2Form(CargaMasivaDatosGFVo objectVo,
			CargaMasivaGFForm objectForm) {
		// TODO Auto-generated method stub
		return null;
	}









}
