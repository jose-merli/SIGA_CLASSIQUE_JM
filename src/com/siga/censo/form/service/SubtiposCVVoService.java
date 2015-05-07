package com.siga.censo.form.service;

import java.util.ArrayList;
import java.util.List;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.vo.cen.SubtiposCVVo;

import com.siga.censo.form.SubtiposCVForm;
import com.siga.comun.VoUiService;

/**
 * 
 * @author jorgeta 
 * @date   26/01/2015
 *
 * La imaginación es más importante que el conocimiento
 *
 */
public class SubtiposCVVoService implements VoUiService<SubtiposCVForm, SubtiposCVVo> {
	public static final String SUBTIPOCV1="1";
	public static final String SUBTIPOCV2="2";
	public List<SubtiposCVForm> getVo2FormList(
			List<SubtiposCVVo> voList) {
		List<SubtiposCVForm> SubtiposCVForms = new ArrayList<SubtiposCVForm>();
		SubtiposCVForm SubtiposCVForm   = null;
		for (SubtiposCVVo objectVo : voList) {
			SubtiposCVForm = getVo2Form(objectVo);
			SubtiposCVForms.add(SubtiposCVForm);
			
		}
		return SubtiposCVForms;
	}

	public SubtiposCVVo getForm2Vo(SubtiposCVForm objectForm) {
		
		SubtiposCVVo tiposDatosCurricularesVo = new SubtiposCVVo();
		if(objectForm.getIdInstitucion()!=null && !objectForm.getIdInstitucion().equals(""))
			tiposDatosCurricularesVo.setIdInstitucion(Short.valueOf(objectForm.getIdInstitucion()));
		if(objectForm.getIdTipoCV()!=null && !objectForm.getIdTipoCV().equals(""))
			tiposDatosCurricularesVo.setIdtipocv(Short.valueOf(objectForm.getIdTipoCV()));
		if(objectForm.getTipoDescripcion()!=null && !objectForm.getTipoDescripcion().equals(""))
			tiposDatosCurricularesVo.setDescripcion(objectForm.getTipoDescripcion());
		if(objectForm.getCodigoExt()!=null && !objectForm.getCodigoExt().equals(""))
			tiposDatosCurricularesVo.setCodigoext(objectForm.getCodigoExt());
		if(objectForm.getTodos()!=null && !objectForm.getTodos().equals(""))
			tiposDatosCurricularesVo.setTodos(objectForm.getTodos());
		
		
		
		if(objectForm.getSubTipo()!=null && objectForm.getSubTipo().equals(SUBTIPOCV1)){
			if(objectForm.getSubTipo1IdTipo()!=null && !objectForm.getSubTipo1IdTipo().equals(""))
				tiposDatosCurricularesVo.setSubTipo1IdTipo(Short.valueOf(objectForm.getSubTipo1IdTipo()));
			if(objectForm.getSubTipoDescripcion()!=null && !objectForm.getSubTipoDescripcion().equals(""))
				tiposDatosCurricularesVo.setSubTipo1Descripcion(objectForm.getSubTipoDescripcion());
			else if(objectForm.getSubTipo1Descripcion()!=null && !objectForm.getSubTipo1Descripcion().equals("")){
				tiposDatosCurricularesVo.setSubTipo1Descripcion(objectForm.getSubTipo1Descripcion());
			
				
			}
			if(objectForm.getSubTipo1IdRecursoDescripcion()!=null && !objectForm.getSubTipo1IdRecursoDescripcion().equals(""))
				tiposDatosCurricularesVo.setSubTipo1IdRecursoDescripcion(objectForm.getSubTipo1IdRecursoDescripcion());
			
			if(objectForm.getSubTipoCodigoExt()!=null && !objectForm.getSubTipoCodigoExt().equals(""))
				tiposDatosCurricularesVo.setSubTipo1CodigoExt(objectForm.getSubTipoCodigoExt());
			else if(objectForm.getSubTipo1CodigoExt()!=null && !objectForm.getSubTipo1CodigoExt().equals(""))
				tiposDatosCurricularesVo.setSubTipo1CodigoExt(objectForm.getSubTipo1CodigoExt());
				
		}else if(objectForm.getSubTipo()!=null && objectForm.getSubTipo().equals(SUBTIPOCV2)){
			if(objectForm.getSubTipo2IdTipo()!=null && !objectForm.getSubTipo2IdTipo().equals(""))
				tiposDatosCurricularesVo.setSubTipo2IdTipo(Short.valueOf(objectForm.getSubTipo2IdTipo()));
			if(objectForm.getSubTipoDescripcion()!=null && !objectForm.getSubTipoDescripcion().equals(""))
				tiposDatosCurricularesVo.setSubTipo2Descripcion(objectForm.getSubTipoDescripcion());
			else if(objectForm.getSubTipo2Descripcion()!=null && !objectForm.getSubTipo2Descripcion().equals("")){
				tiposDatosCurricularesVo.setSubTipo2Descripcion(objectForm.getSubTipo2Descripcion());
				
			}
			if(objectForm.getSubTipo2IdRecursoDescripcion()!=null && !objectForm.getSubTipo2IdRecursoDescripcion().equals(""))
				tiposDatosCurricularesVo.setSubTipo2IdRecursoDescripcion(objectForm.getSubTipo2IdRecursoDescripcion());
			
			if(objectForm.getSubTipoCodigoExt()!=null && !objectForm.getSubTipoCodigoExt().equals(""))
				tiposDatosCurricularesVo.setSubTipo2CodigoExt(objectForm.getSubTipoCodigoExt());
			else if(objectForm.getSubTipo2CodigoExt()!=null && !objectForm.getSubTipo2CodigoExt().equals(""))
				tiposDatosCurricularesVo.setSubTipo2CodigoExt(objectForm.getSubTipo2CodigoExt());
		}else{
			if(objectForm.getSubTipo1IdTipo()!=null && !objectForm.getSubTipo1IdTipo().equals(""))
				tiposDatosCurricularesVo.setSubTipo1IdTipo(Short.valueOf(objectForm.getSubTipo1IdTipo()));
			if(objectForm.getSubTipo1Descripcion()!=null && !objectForm.getSubTipo1Descripcion().equals(""))
				tiposDatosCurricularesVo.setSubTipo1Descripcion(objectForm.getSubTipo1Descripcion());
			if(objectForm.getSubTipo1IdRecursoDescripcion()!=null && !objectForm.getSubTipo1IdRecursoDescripcion().equals(""))
				tiposDatosCurricularesVo.setSubTipo1IdRecursoDescripcion(objectForm.getSubTipo1IdRecursoDescripcion());
			
			if(objectForm.getSubTipo1CodigoExt()!=null && !objectForm.getSubTipo1CodigoExt().equals(""))
				tiposDatosCurricularesVo.setSubTipo1CodigoExt(objectForm.getSubTipo1CodigoExt());
			
			if(objectForm.getSubTipo2IdTipo()!=null && !objectForm.getSubTipo2IdTipo().equals(""))
				tiposDatosCurricularesVo.setSubTipo2IdTipo(Short.valueOf(objectForm.getSubTipo2IdTipo()));
			if(objectForm.getSubTipo2Descripcion()!=null && !objectForm.getSubTipo2Descripcion().equals(""))
				tiposDatosCurricularesVo.setSubTipo2Descripcion(objectForm.getSubTipo2Descripcion());
			if(objectForm.getSubTipo2IdRecursoDescripcion()!=null && !objectForm.getSubTipo2IdRecursoDescripcion().equals(""))
				tiposDatosCurricularesVo.setSubTipo2IdRecursoDescripcion(objectForm.getSubTipo2IdRecursoDescripcion());
			if(objectForm.getSubTipo2CodigoExt()!=null && !objectForm.getSubTipo2CodigoExt().equals(""))
				tiposDatosCurricularesVo.setSubTipo2CodigoExt(objectForm.getSubTipo2CodigoExt());
			
		}
		
		
		
		
		
		return tiposDatosCurricularesVo;
	}

	
	
	public SubtiposCVForm getVo2Form(SubtiposCVVo objectVo) {
		return getVo2Form(objectVo,new SubtiposCVForm());
	}
	public SubtiposCVForm getVo2Form(SubtiposCVVo objectVo, SubtiposCVForm objectForm) {
		
		
		if(objectVo.getIdInstitucion()!=null && !objectVo.getIdInstitucion().equals(""))
			objectForm.setIdInstitucion(objectVo.getIdInstitucion().toString());
		if(objectVo.getTodos()!=null && !objectVo.getTodos().equals(""))
			objectForm.setTodos(objectVo.getTodos());
		
		
		if(objectVo.getIdtipocv()!=null && !objectVo.getIdtipocv().equals(""))
			objectForm.setIdTipoCV(objectVo.getIdtipocv().toString());
		
		if(objectVo.getDescripcion()!=null && !objectVo.getDescripcion().equals(""))
			objectForm.setTipoDescripcion(objectVo.getDescripcion());
		
		if(objectVo.getCodigoext()!=null && !objectVo.getCodigoext().equals(""))
			objectForm.setCodigoExt(objectVo.getCodigoext());
		
		if(objectVo.getSubTipo1IdInstitucion()!=null && !objectVo.getSubTipo1IdInstitucion().equals(""))
			objectForm.setSubTipo1IdInstitucion(objectVo.getSubTipo1IdInstitucion().toString());
		if(objectVo.getSubTipo1IdTipo()!=null && !objectVo.getSubTipo1IdTipo().equals(""))
			objectForm.setSubTipo1IdTipo(objectVo.getSubTipo1IdTipo().toString());
		if(objectVo.getSubTipo1Descripcion()!=null && !objectVo.getSubTipo1Descripcion().equals("")){
			
			if(objectVo.getIdInstitucion()!=null && objectVo.getIdInstitucion().shortValue()==AppConstants.IDINSTITUCION_2000
					&& objectVo.getSubTipo1IdInstitucion().shortValue()!=AppConstants.IDINSTITUCION_2000
					){
				objectForm.setSubTipo1Descripcion(objectVo.getSubTipo1Descripcion()+" ("+objectVo.getAbreviaturaInstitucion1()+")");
			}else{
				objectForm.setSubTipo1Descripcion(objectVo.getSubTipo1Descripcion());
			}
		}
		if(objectVo.getSubTipo1IdRecursoDescripcion()!=null && !objectVo.getSubTipo1IdRecursoDescripcion().equals(""))
			objectForm.setSubTipo1IdRecursoDescripcion(objectVo.getSubTipo1IdRecursoDescripcion());
		if(objectVo.getSubTipo1CodigoExt()!=null && !objectVo.getSubTipo1CodigoExt().equals(""))
			objectForm.setSubTipo1CodigoExt(objectVo.getSubTipo1CodigoExt());
		
		
		if(objectVo.getSubTipo2IdInstitucion()!=null && !objectVo.getSubTipo2IdInstitucion().equals(""))
			objectForm.setSubTipo2IdInstitucion(objectVo.getSubTipo2IdInstitucion().toString());
		if(objectVo.getSubTipo2IdTipo()!=null && !objectVo.getSubTipo2IdTipo().equals(""))
			objectForm.setSubTipo2IdTipo(objectVo.getSubTipo2IdTipo().toString());
		if(objectVo.getSubTipo2Descripcion()!=null && !objectVo.getSubTipo2Descripcion().equals("")){
			if(objectVo.getIdInstitucion()!=null && objectVo.getIdInstitucion().shortValue()==AppConstants.IDINSTITUCION_2000
					&& objectVo.getSubTipo2IdInstitucion().shortValue()!=AppConstants.IDINSTITUCION_2000
					){
				objectForm.setSubTipo2Descripcion(objectVo.getSubTipo2Descripcion()+" ("+objectVo.getAbreviaturaInstitucion2()+")");
			}else{
				objectForm.setSubTipo2Descripcion(objectVo.getSubTipo2Descripcion());
			}
			
		}
			
		if(objectVo.getSubTipo2IdRecursoDescripcion()!=null && !objectVo.getSubTipo2IdRecursoDescripcion().equals(""))
			objectForm.setSubTipo2IdRecursoDescripcion(objectVo.getSubTipo2IdRecursoDescripcion());
		if(objectVo.getSubTipo2CodigoExt()!=null && !objectVo.getSubTipo2CodigoExt().equals(""))
			objectForm.setSubTipo2CodigoExt(objectVo.getSubTipo2CodigoExt());
		
		
		
		
		
		
		return objectForm;
	}




	

	
	
	

}
