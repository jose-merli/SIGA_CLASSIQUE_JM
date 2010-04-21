package com.siga.censo.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

import com.siga.censo.vos.NombreVO;
import com.siga.comun.action.BaseAction;
import com.siga.comun.vos.PagedVo;
import com.siga.comun.vos.SortedVo;
import com.siga.comun.vos.Vo;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;

/**
 */
public class NoColegiadoAction extends BaseAction {

	public NoColegiadoAction(){

	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<String> ajaxGetNifList(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<String> ajaxGetCodigoPostal(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<String> ajaxGetTelefono(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<String> ajaxGetCorreoElectronico(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}


	/**
	 * 
	 * @param vo
	 * @param pagedVo
	 * @param sortedVo
	 * @param params
	 * @return
	 * @throws SIGAException
	 */
	public List<Vo> buscar(Vo vo, PagedVo pagedVo, SortedVo sortedVo) throws SIGAException{
		return null;
	};

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<NombreVO> ajaxGetNombre(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<NombreVO> ajaxGetApellido1(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 * @param formulario
	 * @param mapping
	 */
	public List<NombreVO> ajaxGetApellido2(HttpServletResponse response, HttpServletRequest request, MasterForm formulario, ActionMapping mapping){
		return null;

	}

}