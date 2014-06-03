package com.siga.gratuita.form.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.vo.scs.DocumentacionAsistenciaVo;

import com.siga.comun.VoUiService;
import com.siga.gratuita.form.DefinirDocumentacionAsistenciaForm;

/**
 * @author Carlos Ruano Martínez 
 * @date 29/11/2013
 *
 * Ser Campeón no es una Meta, es una Actitud	
 *
 */

public class DocumentacionAsistenciaVoService implements VoUiService<DefinirDocumentacionAsistenciaForm, DocumentacionAsistenciaVo> {
	
	public List<DefinirDocumentacionAsistenciaForm> getVo2FormList(List<DocumentacionAsistenciaVo> voList) {
		List<DefinirDocumentacionAsistenciaForm> definirDocumentacionAsistenciaForms = new ArrayList<DefinirDocumentacionAsistenciaForm>();
		DefinirDocumentacionAsistenciaForm form   = null;
		for (DocumentacionAsistenciaVo objectVo : voList) {
			form = this.getVo2Form(objectVo);
			definirDocumentacionAsistenciaForms.add(form);
			
		}
		return definirDocumentacionAsistenciaForms;
	}

	@Override
	public DocumentacionAsistenciaVo getForm2Vo(DefinirDocumentacionAsistenciaForm objectForm) {
		
		DocumentacionAsistenciaVo DocumentacionAsistenciaVo = new DocumentacionAsistenciaVo(); 
		if(objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equals("")){
			DocumentacionAsistenciaVo.setIdinstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		}
		if(objectForm.getAnio()!=null && !objectForm.getAnio().equals(""))
			DocumentacionAsistenciaVo.setAnio(Short.valueOf(objectForm.getAnio()));

		if(objectForm.getNumero()!=null && !objectForm.getNumero().equals(""))
			DocumentacionAsistenciaVo.setNumero(Long.valueOf(objectForm.getNumero()));

		if(objectForm.getIdDocumentacion()!=null && !objectForm.getIdDocumentacion().equals(""))
			DocumentacionAsistenciaVo.setIddocumentacionasi((Integer.valueOf(objectForm.getIdDocumentacion())));

		if(objectForm.getIdTipoDocumento()!=null && !objectForm.getIdTipoDocumento().equals(""))
			DocumentacionAsistenciaVo.setIdtipodocumento(Short.valueOf(objectForm.getIdTipoDocumento()));

		if(objectForm.getIdFichero()!=null && !objectForm.getIdFichero().equals(""))
			DocumentacionAsistenciaVo.setIdfichero(Long.valueOf(objectForm.getIdFichero()));
		
		if(objectForm.getObservaciones()!=null && !objectForm.getObservaciones().equals(""))
			DocumentacionAsistenciaVo.setObservaciones(objectForm.getObservaciones());
		
		if(objectForm.getIdActuacion()!=null && !objectForm.getIdActuacion().equals("")){
			DocumentacionAsistenciaVo.setIdactuacion(Long.valueOf(objectForm.getIdActuacion()));
		}
//		else{
//			DocumentacionAsistenciaVo.setIdactuacion(Long.valueOf("-1"));
//		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		if(objectForm.getFechaEntrada()!=null && !objectForm.getFechaEntrada().equals("")){
			try {
				DocumentacionAsistenciaVo.setFechaentrada(sdf.parse(objectForm.getFechaEntrada()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		
		}
		try {
			if(objectForm.getTheFile()!=null && objectForm.getTheFile().getFileData()!=null && objectForm.getTheFile().getFileData().length>0){
				
					DocumentacionAsistenciaVo.setFichero(objectForm.getTheFile().getFileData());
					DocumentacionAsistenciaVo.setDescripcionArchivo(objectForm.getDescripcionArchivo());
					if(objectForm.getTheFile().getFileName().lastIndexOf(".")!=-1)
						DocumentacionAsistenciaVo.setExtensionArchivo(objectForm.getTheFile().getFileName().substring(objectForm.getTheFile().getFileName().lastIndexOf(".")+1));
					
				
			}else{
				DocumentacionAsistenciaVo.setNombreArchivo(objectForm.getNombreArchivo());
				DocumentacionAsistenciaVo.setDirectorioArchivo(objectForm.getDirectorioArchivo());
				DocumentacionAsistenciaVo.setDescripcionArchivo(objectForm.getDescripcionArchivo());
				DocumentacionAsistenciaVo.setExtensionArchivo(objectForm.getExtensionArchivo());
				if(objectForm.getFechaArchivo()!=null && !objectForm.getFechaArchivo().equals("")){
					try {
						DocumentacionAsistenciaVo.setFechaArchivo(sdf.parse(objectForm.getFechaArchivo()));
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}
			
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//DocumentacionAsistenciaVo.setBorrarFichero(Boolean.parseBoolean(objectForm.getBorrarFichero()));
		return DocumentacionAsistenciaVo;
	}

	
	
	public DefinirDocumentacionAsistenciaForm getVo2Form(DocumentacionAsistenciaVo objectVo) {
		DefinirDocumentacionAsistenciaForm definirDocumentacionAsistenciaForm = new DefinirDocumentacionAsistenciaForm();
		definirDocumentacionAsistenciaForm.setIdInstitucion(objectVo.getIdinstitucion().toString());
		definirDocumentacionAsistenciaForm.setAnio(objectVo.getAnio().toString());
		definirDocumentacionAsistenciaForm.setNumero(objectVo.getNumero().toString());
		definirDocumentacionAsistenciaForm.setIdDocumentacion(objectVo.getIddocumentacionasi().toString());
		definirDocumentacionAsistenciaForm.setIdTipoDocumento(objectVo.getIdtipodocumento().toString());
		definirDocumentacionAsistenciaForm.setObservaciones(objectVo.getObservaciones());
		if(objectVo.getIdactuacion()!=null)
			definirDocumentacionAsistenciaForm.setIdActuacion(objectVo.getIdactuacion().toString());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(objectVo.getFechaentrada()!=null && !objectVo.getFechaentrada().equals(""))
			definirDocumentacionAsistenciaForm.setFechaEntrada(sdf.format(objectVo.getFechaentrada()));
		if(objectVo.getIdfichero()!=null && !objectVo.getIdfichero().equals("")){
			definirDocumentacionAsistenciaForm.setIdFichero(objectVo.getIdfichero().toString());
			definirDocumentacionAsistenciaForm.setExtensionArchivo(objectVo.getExtensionArchivo());
			definirDocumentacionAsistenciaForm.setDirectorioArchivo(objectVo.getDirectorioArchivo());
			definirDocumentacionAsistenciaForm.setNombreArchivo(objectVo.getNombreArchivo());
			definirDocumentacionAsistenciaForm.setDescripcionArchivo(objectVo.getDescripcionArchivo());
			definirDocumentacionAsistenciaForm.setFechaArchivo(sdf.format(objectVo.getFechaArchivo()));
		}
		
		return definirDocumentacionAsistenciaForm;
	}

}

