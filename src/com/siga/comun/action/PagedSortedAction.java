package com.siga.comun.action;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.siga.comun.form.PagedSortedForm;
import com.siga.comun.vos.PagedVo;
import com.siga.comun.vos.SortedVo;
import com.siga.comun.vos.Vo;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessService;

public abstract class PagedSortedAction extends BaseAction {

	@SuppressWarnings("unchecked")
	public void executeServiceMethod(PagedSortedForm miForm, BusinessService service, String metodo)
			throws SIGAException {
		//se recupera el vo de paginacion para poder actualizar el tamaño total
		//de la lista devuelta en el bean de paginacion del formulario
		PagedVo pagedVo = miForm.toPagedVo();
		SortedVo sortedVo = miForm.toSortedVo();
		
		Method method = null;
		List<Vo> lista = null;
		try{
			method = service.getClass().getMethod(metodo, Vo.class,PagedVo.class,SortedVo.class);
			lista = (List<Vo>) method.invoke(service, miForm.toVO(), pagedVo, sortedVo);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e,new String[] {"modulo.censo"});
		}
		//actualiza la lista de resultados y su tamaño para que el display tag se muestre correctamente
		miForm.setTable(lista);
		miForm.fromPagedVo(pagedVo);
		miForm.fromSortedVo(sortedVo);
	}
	
	
	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		ActionForward forward=null;
		PagedSortedForm miForm=(PagedSortedForm)formulario;

		miForm.setRequest(request);
		miForm.updateRequest(request);
		forward=super.executeInternal(mapping, formulario, request, response);
		
		return forward;
	}

}
