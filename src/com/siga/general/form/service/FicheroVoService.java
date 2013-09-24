package com.siga.general.form.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.autogen.model.GenFichero;
import org.redabogacia.sigaservices.app.vo.gen.FicheroVo;
import org.redabogacia.sigaservices.app.vo.scs.DocumentacionEjgVo;

import com.siga.comun.VoUiService;
import com.siga.general.form.FicheroForm;

/**
 * 
 * @author jorgeta 
 * @date   23/05/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class FicheroVoService implements VoUiService<FicheroForm, FicheroVo> {
	public List<FicheroVo> getDb2VoList(List<GenFichero> dbList){
			
		List<FicheroVo> objectVos = new ArrayList<FicheroVo>();
		FicheroVo  objectVo = null;
		for (GenFichero genFichero : dbList) {
			objectVo = getDb2Vo(genFichero);
			objectVos.add(objectVo);
			
		}
		return objectVos;
	}
	@Override
	public List<FicheroForm> getVo2FormList(
			List<FicheroVo> voList) {
		List<FicheroForm> FicheroForms = new ArrayList<FicheroForm>();
		FicheroForm FicheroForm   = null;
		for (FicheroVo objectVo : voList) {
			FicheroForm = getVo2Form(objectVo);
			FicheroForms.add(FicheroForm);
			
		}
		return FicheroForms;
	}

	@Override
	public FicheroVo getForm2Vo(FicheroForm objectForm) {
		FicheroVo objectVo = new FicheroVo();
		
		if(objectForm.getIdTipoEJG()!=null && !objectForm.getIdTipoEJG().equalsIgnoreCase("")){
			DocumentacionEjgVo documentacionEjgVo = new DocumentacionEjgVo(); 
			objectVo.setDocumentacionEjgVo(documentacionEjgVo);
			if(objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equals("")){
				objectVo.setIdinstitucion(Short.valueOf(objectForm.getIdInstitucion()));
				documentacionEjgVo.setIdinstitucion(Short.valueOf(objectForm.getIdInstitucion()));
			}
			if(objectForm.getAnio()!=null && !objectForm.getAnio().equals(""))
				documentacionEjgVo.setAnio(Short.valueOf(objectForm.getAnio()));
			if(objectForm.getIdTipoEJG()!=null && !objectForm.getIdTipoEJG().equals(""))
				documentacionEjgVo.setIdtipoejg(Short.valueOf(objectForm.getIdTipoEJG()));
			if(objectForm.getNumero()!=null && !objectForm.getNumero().equals(""))
				documentacionEjgVo.setNumero(Long.valueOf(objectForm.getNumero()));
			if(objectForm.getIdDocumentacion()!=null && !objectForm.getIdDocumentacion().equals(""))
				documentacionEjgVo.setIddocumentacion(Short.valueOf(objectForm.getIdDocumentacion()));
			if(objectForm.getIdDocumento()!=null && !objectForm.getIdDocumento().equals(""))
				documentacionEjgVo.setIddocumento(Short.valueOf(objectForm.getIdDocumento()));
			if(objectForm.getIdTipoDocumento()!=null && !objectForm.getIdTipoDocumento().equals(""))
				documentacionEjgVo.setIdtipodocumento(Short.valueOf(objectForm.getIdTipoDocumento()));
			if(objectForm.getPresentador()!=null && !objectForm.getPresentador().equals(""))
				documentacionEjgVo.setPresentador(Long.valueOf(objectForm.getPresentador()));
			if(objectForm.getNumEjg()!=null && !objectForm.getNumEjg().equals(""))
				documentacionEjgVo.setNumEjg(objectForm.getNumEjg());
			
		}
		
		if(objectForm.getTheFile()!=null){
			try {
				objectVo.setFichero(objectForm.getTheFile().getFileData());
				objectVo.setNombre(objectForm.getNombreArchivo());
				if(objectForm.getTheFile().getFileName().lastIndexOf(".")!=-1)
					objectVo.setExtension(objectForm.getTheFile().getFileName().substring(objectForm.getTheFile().getFileName().lastIndexOf(".")+1));
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(objectForm.getIdFichero()!=null && !objectForm.getIdFichero().equals(""))
			objectVo.setIdfichero(Long.valueOf(objectForm.getIdFichero()));
		
			
		
		
		return objectVo;
	}

	@Override
	public FicheroForm getVo2Form(FicheroVo objectVo) {
		FicheroForm ficheroForm = new FicheroForm();
			
		if(objectVo.getIdinstitucion()!=null)
			ficheroForm.setIdInstitucion(objectVo.getIdinstitucion().toString());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		ficheroForm.setExtensionArchivo(objectVo.getExtension());
		if(objectVo.getFechamodificacion()!=null)
			ficheroForm.setFechaArchivo(sdf.format(objectVo.getFechamodificacion()));
		ficheroForm.setNombreArchivo(objectVo.getNombre());
		if(objectVo.getIdfichero()!=null)
			ficheroForm.setIdFichero(objectVo.getIdfichero().toString());
		
			
		return ficheroForm;
	}
	/* (non-Javadoc)
	 * @see org.redabogacia.sigaservices.app.vo.services.VoService#getVo2Db(java.lang.Object)
	 */
	public GenFichero getVo2Db(FicheroVo objectVo) {
		GenFichero genFichero = new GenFichero();
		genFichero.setExtension(objectVo.getExtension());
		genFichero.setNombre(objectVo.getNombre());
		genFichero.setIdfichero(objectVo.getIdfichero());
		genFichero.setIdinstitucion(objectVo.getIdinstitucion());
		genFichero.setFechamodificacion(objectVo.getFechamodificacion());
		genFichero.setUsumodificacion(objectVo.getUsumodificacion());
		return genFichero;
	}
	/* (non-Javadoc)
	 * @see org.redabogacia.sigaservices.app.vo.services.VoService#getDb2Vo(java.lang.Object)
	 */
	public FicheroVo getDb2Vo(GenFichero objectDb) {
		FicheroVo ficheroVo = new FicheroVo();
		ficheroVo.setExtension(objectDb.getExtension());
		ficheroVo.setNombre(objectDb.getNombre());
		ficheroVo.setIdfichero(objectDb.getIdfichero());
		ficheroVo.setIdinstitucion(objectDb.getIdinstitucion());
		ficheroVo.setFechamodificacion(objectDb.getFechamodificacion());
		ficheroVo.setUsumodificacion(objectDb.getUsumodificacion());
		return ficheroVo;
	}




	

	
	
	

}
