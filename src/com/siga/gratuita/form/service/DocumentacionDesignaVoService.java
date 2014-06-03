package com.siga.gratuita.form.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.vo.scs.DocumentacionDesignaVo;

import com.siga.comun.VoUiService;
import com.siga.gratuita.form.DefinirDocumentacionDesignaForm;

/**
 * 
 * @author jorgeta 
 * @date   26/05/2014
 *
 * La imaginación es más importante que el conocimiento
 *
 */

public class DocumentacionDesignaVoService implements VoUiService<DefinirDocumentacionDesignaForm, DocumentacionDesignaVo> {
	
	public List<DefinirDocumentacionDesignaForm> getVo2FormList(List<DocumentacionDesignaVo> voList) {
		List<DefinirDocumentacionDesignaForm> definirDocumentacionciaForms = new ArrayList<DefinirDocumentacionDesignaForm>();
		DefinirDocumentacionDesignaForm form   = null;
		for (DocumentacionDesignaVo objectVo : voList) {
			form = this.getVo2Form(objectVo);
			definirDocumentacionciaForms.add(form);
			
		}
		return definirDocumentacionciaForms;
	}

	@Override
	public DocumentacionDesignaVo getForm2Vo(DefinirDocumentacionDesignaForm objectForm) {
		
		DocumentacionDesignaVo documentacionDesignaVo = new DocumentacionDesignaVo(); 
		if(objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equals("")){
			documentacionDesignaVo.setIdinstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		}
		if(objectForm.getAnio()!=null && !objectForm.getAnio().equals(""))
			documentacionDesignaVo.setAnio(Short.valueOf(objectForm.getAnio()));
		
		if(objectForm.getIdTurno()!=null && !objectForm.getIdTurno().equals(""))
			documentacionDesignaVo.setIdturno(Integer.valueOf(objectForm.getIdTurno()));

		if(objectForm.getNumero()!=null && !objectForm.getNumero().equals(""))
			documentacionDesignaVo.setNumero(Long.valueOf(objectForm.getNumero()));

		if(objectForm.getIdDocumentacion()!=null && !objectForm.getIdDocumentacion().equals(""))
			documentacionDesignaVo.setIddocumentaciondes((Integer.valueOf(objectForm.getIdDocumentacion())));

		if(objectForm.getIdTipoDocumento()!=null && !objectForm.getIdTipoDocumento().equals(""))
			documentacionDesignaVo.setIdtipodocumento(Short.valueOf(objectForm.getIdTipoDocumento()));

		if(objectForm.getIdFichero()!=null && !objectForm.getIdFichero().equals(""))
			documentacionDesignaVo.setIdfichero(Long.valueOf(objectForm.getIdFichero()));
		
		if(objectForm.getObservaciones()!=null && !objectForm.getObservaciones().equals(""))
			documentacionDesignaVo.setObservaciones(objectForm.getObservaciones());
		
		if(objectForm.getIdActuacion()!=null && !objectForm.getIdActuacion().equals("")){
			documentacionDesignaVo.setIdactuacion(Long.valueOf(objectForm.getIdActuacion()));
		}
//		else{
//			documentacionDesignaVo.setIdactuacion(Long.valueOf("-1"));
//		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		if(objectForm.getFechaEntrada()!=null && !objectForm.getFechaEntrada().equals("")){
			try {
				documentacionDesignaVo.setFechaentrada(sdf.parse(objectForm.getFechaEntrada()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		
		}
		try {
			if(objectForm.getTheFile()!=null && objectForm.getTheFile().getFileData()!=null && objectForm.getTheFile().getFileData().length>0){
				
					documentacionDesignaVo.setFichero(objectForm.getTheFile().getFileData());
					documentacionDesignaVo.setDescripcionArchivo(objectForm.getDescripcionArchivo());
					if(objectForm.getTheFile().getFileName().lastIndexOf(".")!=-1)
						documentacionDesignaVo.setExtensionArchivo(objectForm.getTheFile().getFileName().substring(objectForm.getTheFile().getFileName().lastIndexOf(".")+1));
					
				
			}else{
				documentacionDesignaVo.setNombreArchivo(objectForm.getNombreArchivo());
				documentacionDesignaVo.setDirectorioArchivo(objectForm.getDirectorioArchivo());
				documentacionDesignaVo.setDescripcionArchivo(objectForm.getDescripcionArchivo());
				documentacionDesignaVo.setExtensionArchivo(objectForm.getExtensionArchivo());
				if(objectForm.getFechaArchivo()!=null && !objectForm.getFechaArchivo().equals("")){
					try {
						documentacionDesignaVo.setFechaArchivo(sdf.parse(objectForm.getFechaArchivo()));
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
		//DocumentacionDesignaVo.setBorrarFichero(Boolean.parseBoolean(objectForm.getBorrarFichero()));
		return documentacionDesignaVo;
	}

	
	
	public DefinirDocumentacionDesignaForm getVo2Form(DocumentacionDesignaVo objectVo) {
		DefinirDocumentacionDesignaForm definirDocumentacionForm = new DefinirDocumentacionDesignaForm();
		definirDocumentacionForm.setIdInstitucion(objectVo.getIdinstitucion().toString());
		definirDocumentacionForm.setAnio(objectVo.getAnio().toString());
		definirDocumentacionForm.setNumero(objectVo.getNumero().toString());
		definirDocumentacionForm.setIdTurno(objectVo.getIdturno().toString());
		definirDocumentacionForm.setIdDocumentacion(objectVo.getIddocumentaciondes().toString());
		definirDocumentacionForm.setIdTipoDocumento(objectVo.getIdtipodocumento().toString());
		definirDocumentacionForm.setObservaciones(objectVo.getObservaciones());
		if(objectVo.getIdactuacion()!=null)
			definirDocumentacionForm.setIdActuacion(objectVo.getIdactuacion().toString());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(objectVo.getFechaentrada()!=null && !objectVo.getFechaentrada().equals(""))
			definirDocumentacionForm.setFechaEntrada(sdf.format(objectVo.getFechaentrada()));
		if(objectVo.getIdfichero()!=null && !objectVo.getIdfichero().equals("")){
			definirDocumentacionForm.setIdFichero(objectVo.getIdfichero().toString());
			definirDocumentacionForm.setExtensionArchivo(objectVo.getExtensionArchivo());
			definirDocumentacionForm.setDirectorioArchivo(objectVo.getDirectorioArchivo());
			definirDocumentacionForm.setNombreArchivo(objectVo.getNombreArchivo());
			definirDocumentacionForm.setDescripcionArchivo(objectVo.getDescripcionArchivo());
			definirDocumentacionForm.setFechaArchivo(sdf.format(objectVo.getFechaArchivo()));
		}
		
		return definirDocumentacionForm;
	}

}

