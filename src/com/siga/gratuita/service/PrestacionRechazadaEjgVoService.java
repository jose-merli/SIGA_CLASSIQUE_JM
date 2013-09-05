package com.siga.gratuita.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.redabogacia.sigaservices.app.autogen.model.ScsEjgPrestacionRechazada;
import org.redabogacia.sigaservices.app.vo.scs.PrestacionRechazadaEjgVo;
import org.redabogacia.sigaservices.app.vo.services.VoService;

import com.siga.gratuita.form.PrestacionRechazadaEjgForm;
/**
 * 
 * @author jorgeta 
 * @date   27/06/2013
 *
 * La imaginación es más importante que el conocimiento
 *
 */

public class PrestacionRechazadaEjgVoService implements VoService<PrestacionRechazadaEjgForm,PrestacionRechazadaEjgVo,ScsEjgPrestacionRechazada>{

	public List<PrestacionRechazadaEjgVo> getDb2VoList(List<ScsEjgPrestacionRechazada> dbList){
			
		List<PrestacionRechazadaEjgVo> objectVos = new ArrayList<PrestacionRechazadaEjgVo>();
		PrestacionRechazadaEjgVo  objectVo = null;
		for (ScsEjgPrestacionRechazada ejgPrestacionRechazada : dbList) {
			objectVo = new PrestacionRechazadaEjgVo(ejgPrestacionRechazada);
			objectVos.add(objectVo);
			
		}
		return objectVos;
	}

	
	
	public List<PrestacionRechazadaEjgForm> getVo2FormList(
			List<PrestacionRechazadaEjgVo> voList) {
		List<PrestacionRechazadaEjgForm> prestacionRechazadaForms = new ArrayList<PrestacionRechazadaEjgForm>();
		PrestacionRechazadaEjgForm prestacionesRechazadasForm   = null;
		for (PrestacionRechazadaEjgVo objectVo : voList) {
			prestacionesRechazadasForm = getVo2Form(objectVo);
			prestacionRechazadaForms.add(prestacionesRechazadasForm);
			
		}
		return prestacionRechazadaForms;
	}

	public PrestacionRechazadaEjgVo getForm2Vo(PrestacionRechazadaEjgForm objectForm) {
		PrestacionRechazadaEjgVo objectVo = new PrestacionRechazadaEjgVo();
		
		if(objectForm.getEjgAnio()!=null && !objectForm.getEjgAnio().equals(""))
			objectVo.setAnio(Short.valueOf(objectForm.getEjgAnio()));
		if(objectForm.getPrestacionDescripcion()!=null && !objectForm.getPrestacionDescripcion().equals(""))
			objectVo.setDescripcionPrestacion(objectForm.getPrestacionDescripcion());
		if(objectForm.getEjgIdInstitucion()!=null &&!objectForm.getEjgIdInstitucion().equals(""))
			objectVo.setIdinstitucion(Short.valueOf(objectForm.getEjgIdInstitucion()));
		if(objectForm.getPrestacionId()!=null &&!objectForm.getPrestacionId().equals(""))
			objectVo.setIdprestacion(Short.valueOf(objectForm.getPrestacionId()));
		if(objectForm.getEjgIdTipo()!=null &&!objectForm.getEjgIdTipo().equals(""))
			objectVo.setIdtipoejg(Short.valueOf(objectForm.getEjgIdTipo()));
		if(objectForm.getEjgNumero()!=null &&!objectForm.getEjgNumero().equals(""))
			objectVo.setNumero(Long.valueOf(objectForm.getEjgNumero()));
		if(objectForm.getIdsBorrarRechazadas()!=null &&!objectForm.getIdsBorrarRechazadas().equals("")){
			List<String> borrar = Arrays.asList(objectForm.getIdsBorrarRechazadas().split(","));
			objectVo.setIdsPrestacionesBorrarRechazadas(borrar);
		}
		if(objectForm.getIdsInsertarRechazadas()!=null &&!objectForm.getIdsInsertarRechazadas().equals("")){
			List<String> insertar = Arrays.asList(objectForm.getIdsInsertarRechazadas().split(","));
			objectVo.setIdsPrestacionesInsertarRechazadas(insertar);
		}
		
		return objectVo;
			
		
	}
	

	public PrestacionRechazadaEjgForm getVo2Form(PrestacionRechazadaEjgVo objectVo) {
		PrestacionRechazadaEjgForm prestacionesRechazadasForm = new PrestacionRechazadaEjgForm();
		if(objectVo.getAnio()!=null)
			prestacionesRechazadasForm.setEjgAnio(objectVo.getAnio().toString());
		
		if(objectVo.getDescripcionPrestacion()!=null)
			prestacionesRechazadasForm.setPrestacionDescripcion(objectVo.getDescripcionPrestacion());
		
		if(objectVo.getIdinstitucion()!=null)
			prestacionesRechazadasForm.setEjgIdInstitucion(objectVo.getIdinstitucion().toString());
		
		if(objectVo.getIdprestacion()!=null)
			prestacionesRechazadasForm.setPrestacionId(objectVo.getIdprestacion().toString());
		
				
		if(objectVo.getIdtipoejg()!=null)
			prestacionesRechazadasForm.setEjgIdTipo(objectVo.getIdtipoejg().toString());
		
		if(objectVo.getNumero()!=null)
			prestacionesRechazadasForm.setEjgNumero(objectVo.getNumero().toString());
			
		return prestacionesRechazadasForm;
	}



	/* (non-Javadoc)
	 * @see org.redabogacia.sigaservices.app.vo.services.VoService#getVo2Db(java.lang.Object)
	 */
	@Override
	public ScsEjgPrestacionRechazada getVo2Db(PrestacionRechazadaEjgVo objectVo) {
		// TODO Auto-generated method stub
		return null;
	}


	

	

	
	
	

}
