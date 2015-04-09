package com.siga.censo.form.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.vo.cen.CargaMasivaDatosCVVo;
import org.redabogacia.sigaservices.app.vo.gen.FicheroVo;

import com.siga.censo.form.CargaMasivaCVForm;
import com.siga.comun.VoUiService;

/**
 * 
 * @author jorgeta 
 * @date   08/04/2015
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class CargaMasivaDatosCVVoService implements VoUiService<CargaMasivaCVForm, CargaMasivaDatosCVVo> {

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getVo2FormList(java.util.List)
	 */
	@Override
	public List<CargaMasivaCVForm> getVo2FormList(
			List<CargaMasivaDatosCVVo> voList) {
		List<CargaMasivaCVForm> CargaMasivaCVForms = new ArrayList<CargaMasivaCVForm>();
		CargaMasivaCVForm cargaMasivaCVForm   = null;
		for (CargaMasivaDatosCVVo objectVo : voList) {
			cargaMasivaCVForm = getVo2Form(objectVo);
			CargaMasivaCVForms.add(cargaMasivaCVForm);

		}
		return CargaMasivaCVForms;
	}

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getForm2Vo(java.lang.Object)
	 */
	@Override
	public CargaMasivaDatosCVVo getForm2Vo(CargaMasivaCVForm objectForm) {
		CargaMasivaDatosCVVo objectVo = new CargaMasivaDatosCVVo(); 
		if(objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equals("")){
			objectVo.setIdinstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		}
		if(objectForm.getColegiadoNif()!=null && !objectForm.getColegiadoNif().equals(""))
			objectVo.setColegiadoNif(objectForm.getColegiadoNif());

		if(objectForm.getColegiadoNombre()!=null && !objectForm.getColegiadoNombre().equals(""))
			objectVo.setColegiadoNombre(objectForm.getColegiadoNombre());
		if(objectForm.getColegiadoNumero()!=null && !objectForm.getColegiadoNumero().equals(""))
			objectVo.setColegiadoNumero(objectForm.getColegiadoNumero());
		if(objectForm.getCreditos()!=null && !objectForm.getCreditos().equals(""))
			objectVo.setCreditos(Long.valueOf(objectForm.getCreditos()));
		if(objectForm.getDescripcion()!=null && !objectForm.getDescripcion().equals(""))
			objectVo.setDescripcion(objectForm.getDescripcion());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(objectForm.getFechaCarga()!=null && !objectForm.getFechaCarga().equals(""))
			try {
				objectVo.setFechaCarga(sdf.parse(objectForm.getFechaCarga()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		if(objectForm.getFechaFin()!=null && !objectForm.getFechaFin().equals(""))
			try {
				objectVo.setFechafin(sdf.parse(objectForm.getFechaFin()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		if(objectForm.getFechaInicio()!=null && !objectForm.getFechaInicio().equals(""))
			try {
				objectVo.setFechainicio(sdf.parse(objectForm.getFechaInicio()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		if(objectForm.getFechaVerificacion()!=null && !objectForm.getFechaVerificacion().equals(""))
			try {
				objectVo.setFechamovimiento(sdf.parse(objectForm.getFechaVerificacion()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		if(objectForm.getIdCargaMasivaCV()!=null && !objectForm.getIdCargaMasivaCV().equals(""))
			objectVo.setIdCargaMasivaCV(Long.valueOf(objectForm.getIdCargaMasivaCV()));

		if(objectForm.getNombreFichero()!=null && !objectForm.getNombreFichero().equals(""))
			objectVo.setNombreFichero(objectForm.getNombreFichero());
		if(objectForm.getNumRegistros()!=null && !objectForm.getNumRegistros().equals(""))
			objectVo.setNumRegistros(Short.valueOf(objectForm.getNumRegistros()));
		if(objectForm.getSubtipoCV1Cod()!=null && !objectForm.getSubtipoCV1Cod().equals(""))
			objectVo.setSubtipoCV1Cod(objectForm.getSubtipoCV1Cod());
		if(objectForm.getSubtipoCV1Nombre()!=null && !objectForm.getSubtipoCV1Nombre().equals(""))
			objectVo.setSubtipoCV1Nombre(objectForm.getSubtipoCV1Nombre());
		if(objectForm.getSubtipoCV2Cod()!=null && !objectForm.getSubtipoCV2Cod().equals(""))
			objectVo.setSubtipoCV2Cod(objectForm.getSubtipoCV2Cod());
		if(objectForm.getSubtipoCV2Nombre()!=null && !objectForm.getSubtipoCV2Nombre().equals(""))
			objectVo.setSubtipoCV2Nombre(objectForm.getSubtipoCV2Nombre());

		if(objectForm.getTipoCVCod()!=null && !objectForm.getTipoCVCod().equals(""))
			objectVo.setTipoCVCod(objectForm.getTipoCVCod());
		if(objectForm.getTipoCVNombre()!=null && !objectForm.getTipoCVNombre().equals(""))
			objectVo.setTipoCVNombre(objectForm.getTipoCVNombre());

		if(objectForm.getUsuario()!=null && !objectForm.getUsuario().equals(""))
			objectVo.setUsuario(objectForm.getUsuario());

		try {
			if(objectForm.getTheFile()!=null && objectForm.getTheFile().getFileData()!=null && objectForm.getTheFile().getFileData().length>0){
				FicheroVo ficheroLogVo = new FicheroVo();
				objectVo.setFicheroLog(ficheroLogVo);

				ficheroLogVo.setFichero(objectForm.getTheFile().getFileData());
				ficheroLogVo.setDescripcion(objectForm.getNombreFichero());
				ficheroLogVo.setNombre(objectForm.getNombreFichero());
				if(objectForm.getTheFile().getFileName().lastIndexOf(".")!=-1)
					ficheroLogVo.setExtension(objectForm.getTheFile().getFileName().substring(objectForm.getTheFile().getFileName().lastIndexOf(".")+1));


			}
		} catch (Exception e) {
			e.printStackTrace();
		} 	

		return objectVo;
	}

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getVo2Form(java.lang.Object)
	 */
	@Override
	public CargaMasivaCVForm getVo2Form(CargaMasivaDatosCVVo objectVo) {
		CargaMasivaCVForm objectForm = new CargaMasivaCVForm();

		if(objectVo.getIdinstitucion()!=null && !objectVo.getIdinstitucion().equals("")){
			objectForm.setIdInstitucion(objectVo.getIdinstitucion().toString());
		}
		if(objectVo.getColegiadoNif()!=null && !objectVo.getColegiadoNif().equals(""))
			objectForm.setColegiadoNif(objectVo.getColegiadoNif());

		if(objectVo.getColegiadoNombre()!=null && !objectVo.getColegiadoNombre().equals(""))
			objectForm.setColegiadoNombre(objectVo.getColegiadoNombre());
		if(objectVo.getColegiadoNumero()!=null && !objectVo.getColegiadoNumero().equals(""))
			objectForm.setColegiadoNumero(objectVo.getColegiadoNumero());
		if(objectVo.getCreditos()!=null && !objectVo.getCreditos().equals(""))
			objectForm.setCreditos(objectVo.getCreditos().toString());
		if(objectVo.getDescripcion()!=null && !objectVo.getDescripcion().equals(""))
			objectForm.setDescripcion(objectVo.getDescripcion());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(objectVo.getFechaCarga()!=null && !objectVo.getFechaCarga().equals(""))
			objectForm.setFechaCarga(sdf.format(objectVo.getFechaCarga()));
		if(objectVo.getFechafin()!=null && !objectVo.getFechafin().equals(""))
			objectForm.setFechaFin(sdf.format(objectVo.getFechafin()));
		if(objectVo.getFechainicio()!=null && !objectVo.getFechainicio().equals(""))
			objectForm.setFechaInicio(sdf.format(objectVo.getFechainicio()));
		if(objectVo.getFechamovimiento()!=null && !objectVo.getFechamovimiento().equals(""))
			objectForm.setFechaVerificacion(sdf.format(objectVo.getFechamovimiento()));
		if(objectVo.getIdCargaMasivaCV()!=null && !objectVo.getIdCargaMasivaCV().equals(""))
			objectForm.setIdCargaMasivaCV(objectVo.getIdCargaMasivaCV().toString());
		if(objectVo.getNombreFichero()!=null && !objectVo.getNombreFichero().equals(""))
			objectForm.setNombreFichero(objectVo.getNombreFichero());
		if(objectVo.getNumRegistros()!=null && !objectVo.getNumRegistros().equals(""))
			objectForm.setNumRegistros(objectVo.getNumRegistros().toString());
		if(objectVo.getSubtipoCV1Cod()!=null && !objectVo.getSubtipoCV1Cod().equals(""))
			objectForm.setSubtipoCV1Cod(objectVo.getSubtipoCV1Cod());
		if(objectVo.getSubtipoCV1Nombre()!=null && !objectVo.getSubtipoCV1Nombre().equals(""))
			objectForm.setSubtipoCV1Nombre(objectVo.getSubtipoCV1Nombre());
		if(objectVo.getSubtipoCV2Cod()!=null && !objectVo.getSubtipoCV2Cod().equals(""))
			objectForm.setSubtipoCV2Cod(objectVo.getSubtipoCV2Cod());
		if(objectVo.getSubtipoCV2Nombre()!=null && !objectVo.getSubtipoCV2Nombre().equals(""))
			objectForm.setSubtipoCV2Nombre(objectVo.getSubtipoCV2Nombre());
		if(objectVo.getTipoCVCod()!=null && !objectVo.getTipoCVCod().equals(""))
			objectForm.setTipoCVCod(objectVo.getTipoCVCod());
		if(objectVo.getTipoCVNombre()!=null && !objectVo.getTipoCVNombre().equals(""))
			objectForm.setTipoCVNombre(objectVo.getTipoCVNombre());
		if(objectVo.getUsuario()!=null && !objectVo.getUsuario().equals(""))
			objectForm.setUsuario(objectVo.getUsuario());
		//		if(objectForm.getIdfichero()!=null && !objectForm.getIdfichero().equals("")){
		//			objectVo.setIdFichero(objectForm.getIdfichero().toString());
		//			objectVo.setExtensionArchivo(objectForm.getExtensionArchivo());
		//			objectVo.setDirectorioArchivo(objectForm.getDirectorioArchivo());
		//			objectVo.setNombreArchivo(objectForm.getNombreArchivo());
		//			objectVo.setDescripcionArchivo(objectForm.getDescripcionArchivo());
		//			
		//			objectVo.setFechaArchivo(sdf.format(objectForm.getFechaArchivo()));
		//		}
		return objectForm;
	}

	/* (non-Javadoc)
	 * @see com.siga.comun.VoUiService#getVo2Form(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CargaMasivaCVForm getVo2Form(CargaMasivaDatosCVVo objectVo,
			CargaMasivaCVForm objectForm) {
		// TODO Auto-generated method stub
		return null;
	}









}
