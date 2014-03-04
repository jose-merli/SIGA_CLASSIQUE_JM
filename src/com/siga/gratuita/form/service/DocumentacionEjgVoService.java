package com.siga.gratuita.form.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.vo.scs.DocumentacionEjgVo;

import com.siga.comun.VoUiService;
import com.siga.gratuita.form.DefinirDocumentacionEJGForm;

/**
 * 
 * @author jorgeta 
 * @date   23/05/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class DocumentacionEjgVoService implements VoUiService<DefinirDocumentacionEJGForm, DocumentacionEjgVo> {
	
	public List<DefinirDocumentacionEJGForm> getVo2FormList(
			List<DocumentacionEjgVo> voList) {
		List<DefinirDocumentacionEJGForm> DefinirDocumentacionEJGForms = new ArrayList<DefinirDocumentacionEJGForm>();
		DefinirDocumentacionEJGForm DefinirDocumentacionEJGForm   = null;
		for (DocumentacionEjgVo objectVo : voList) {
			DefinirDocumentacionEJGForm = getVo2Form(objectVo);
			DefinirDocumentacionEJGForms.add(DefinirDocumentacionEJGForm);
			
		}
		return DefinirDocumentacionEJGForms;
	}

	@Override
	public DocumentacionEjgVo getForm2Vo(DefinirDocumentacionEJGForm objectForm) {
		
		DocumentacionEjgVo documentacionEjgVo = new DocumentacionEjgVo(); 
		if(objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equals("")){
			documentacionEjgVo.setIdinstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		}
		if(objectForm.getAnio()!=null && !objectForm.getAnio().equals(""))
			documentacionEjgVo.setAnio(Short.valueOf(objectForm.getAnio()));
		if(objectForm.getIdTipoEJG()!=null && !objectForm.getIdTipoEJG().equals(""))
			documentacionEjgVo.setIdtipoejg(Short.valueOf(objectForm.getIdTipoEJG()));
		if(objectForm.getNumero()!=null && !objectForm.getNumero().equals(""))
			documentacionEjgVo.setNumero(Long.valueOf(objectForm.getNumero()));
		if(objectForm.getIdDocumentacion()!=null && !objectForm.getIdDocumentacion().equals(""))
			documentacionEjgVo.setIddocumentacion(Integer.valueOf(objectForm.getIdDocumentacion()));
		if(objectForm.getIdDocumento()!=null && !objectForm.getIdDocumento().equals(""))
			documentacionEjgVo.setIddocumento(Short.valueOf(objectForm.getIdDocumento()));
		if(objectForm.getIdTipoDocumento()!=null && !objectForm.getIdTipoDocumento().equals(""))
			documentacionEjgVo.setIdtipodocumento(Short.valueOf(objectForm.getIdTipoDocumento()));
		//Si trae el idpresentador es:
//		DECODE(PERSONA.IDPERSONA, NULL,'IDMAESTROPRESENTADOR_' || MAESTROPRESENTADOR.IDPRESENTADOR,'IDPERSONAJG_' || PERSONA.IDPERSONA) IDPRESENTADOR,
        
		if(objectForm.getIdPresentador()!=null && !objectForm.getIdPresentador().equals("")){
			String[] idsPresentador = objectForm.getIdPresentador().split("IDMAESTROPRESENTADOR_");
			
			if(idsPresentador.length>1)
				documentacionEjgVo.setIdmaestropresentador(Short.valueOf(idsPresentador[1]));
			else{
				idsPresentador = objectForm.getIdPresentador().split("IDPERSONAJG_");
				documentacionEjgVo.setPresentador(Long.valueOf(idsPresentador[1]));
			}
		
		}else if(objectForm.getPresentador()!=null && !objectForm.getPresentador().equals(""))
			documentacionEjgVo.setPresentador(Long.valueOf(objectForm.getPresentador()));
		if(objectForm.getNumEjg()!=null && !objectForm.getNumEjg().equals(""))
			documentacionEjgVo.setNumEjg(objectForm.getNumEjg());
		if(objectForm.getIdFichero()!=null && !objectForm.getIdFichero().equals(""))
			documentacionEjgVo.setIdfichero(Long.valueOf(objectForm.getIdFichero()));
		if(objectForm.getRegEntrada()!=null && !objectForm.getRegEntrada().equals(""))
			documentacionEjgVo.setRegentrada(objectForm.getRegEntrada());
		if(objectForm.getRegSalida()!=null && !objectForm.getRegSalida().equals(""))
			documentacionEjgVo.setRegsalida(objectForm.getRegSalida());
		if(objectForm.getDocumentacion()!=null && !objectForm.getDocumentacion().equals(""))
			documentacionEjgVo.setDocumentacion(objectForm.getDocumentacion());
		
		
		
		
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(objectForm.getFechaLimite()!=null && !objectForm.getFechaLimite().equals(""))
			try {
				documentacionEjgVo.setFechalimite(sdf.parse(objectForm.getFechaLimite()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		
		if(objectForm.getFechaEntrega()!=null && !objectForm.getFechaEntrega().equals("")){
			try {
				documentacionEjgVo.setFechaentrega(sdf.parse(objectForm.getFechaEntrega()));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		
		}
		try {
			if(objectForm.getTheFile()!=null && objectForm.getTheFile().getFileData()!=null && objectForm.getTheFile().getFileData().length>0){
				
					documentacionEjgVo.setFichero(objectForm.getTheFile().getFileData());
					documentacionEjgVo.setDescripcionArchivo(objectForm.getDescripcionArchivo());
					if(objectForm.getTheFile().getFileName().lastIndexOf(".")!=-1)
						documentacionEjgVo.setExtensionArchivo(objectForm.getTheFile().getFileName().substring(objectForm.getTheFile().getFileName().lastIndexOf(".")+1));
					
				
			}else{
				documentacionEjgVo.setNombreArchivo(objectForm.getNombreArchivo());
				documentacionEjgVo.setDirectorioArchivo(objectForm.getDirectorioArchivo());
				documentacionEjgVo.setDescripcionArchivo(objectForm.getDescripcionArchivo());
				documentacionEjgVo.setExtensionArchivo(objectForm.getExtensionArchivo());
				if(objectForm.getFechaArchivo()!=null && !objectForm.getFechaArchivo().equals("")){
					try {
						documentacionEjgVo.setFechaArchivo(sdf.parse(objectForm.getFechaArchivo()));
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
		documentacionEjgVo.setBorrarFichero(Boolean.parseBoolean(objectForm.getBorrarFichero()));
		return documentacionEjgVo;
	}

	
	
	public DefinirDocumentacionEJGForm getVo2Form(DocumentacionEjgVo objectVo) {
		DefinirDocumentacionEJGForm definirDocumentacionEJGForm = new DefinirDocumentacionEJGForm();
		definirDocumentacionEJGForm.setIdInstitucion(objectVo.getIdinstitucion().toString());
		definirDocumentacionEJGForm.setAnio(objectVo.getAnio().toString());
		
		definirDocumentacionEJGForm.setIdTipoEJG(objectVo.getIdtipoejg().toString());
		
		definirDocumentacionEJGForm.setNumero(objectVo.getNumero().toString());
		definirDocumentacionEJGForm.setIdDocumentacion(objectVo.getIddocumentacion().toString());
		definirDocumentacionEJGForm.setIdDocumento(objectVo.getIddocumento().toString());
		definirDocumentacionEJGForm.setIdTipoDocumento(objectVo.getIdtipodocumento().toString());
		
		
		definirDocumentacionEJGForm.setIdPresentador(objectVo.getIdPresentador());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(objectVo.getFechaentrega()!=null && !objectVo.getFechaentrega().equals(""))
			definirDocumentacionEJGForm.setFechaEntrega(sdf.format(objectVo.getFechaentrega()));
		if(objectVo.getFechalimite()!=null && !objectVo.getFechalimite().equals(""))
			definirDocumentacionEJGForm.setFechaLimite(sdf.format(objectVo.getFechalimite()));
		if(objectVo.getRegentrada()!=null && !objectVo.getRegentrada().equals(""))
			definirDocumentacionEJGForm.setRegEntrada(objectVo.getRegentrada());
		if(objectVo.getRegsalida()!=null && !objectVo.getRegsalida().equals(""))
			definirDocumentacionEJGForm.setRegSalida(objectVo.getRegsalida());
		if(objectVo.getDocumentacion()!=null && !objectVo.getDocumentacion().equals(""))
			definirDocumentacionEJGForm.setDocumentacion(objectVo.getDocumentacion());
		
		if(objectVo.getNumEjg()!=null && !objectVo.getNumEjg().equals(""))
			definirDocumentacionEJGForm.setNumEjg(objectVo.getNumEjg());
		if(objectVo.getIdfichero()!=null && !objectVo.getIdfichero().equals("")){
			definirDocumentacionEJGForm.setIdFichero(objectVo.getIdfichero().toString());
			definirDocumentacionEJGForm.setExtensionArchivo(objectVo.getExtensionArchivo());
			definirDocumentacionEJGForm.setDirectorioArchivo(objectVo.getDirectorioArchivo());
			definirDocumentacionEJGForm.setNombreArchivo(objectVo.getNombreArchivo());
			definirDocumentacionEJGForm.setDescripcionArchivo(objectVo.getDescripcionArchivo());
			
			definirDocumentacionEJGForm.setFechaArchivo(sdf.format(objectVo.getFechaArchivo()));
		}
		
			
		return definirDocumentacionEJGForm;
	}



	

	
	
	

}
