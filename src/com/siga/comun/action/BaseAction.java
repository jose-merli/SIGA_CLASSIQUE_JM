package com.siga.comun.action;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.atos.utils.ClsExceptions;
import com.siga.comun.form.BaseForm;
import com.siga.general.SIGAAuxAction;
import com.siga.general.SIGAException;

/**
 * Esta clase sirve de base para todos los Action del nuevo diseño
 */
public abstract class BaseAction extends SIGAAuxAction{
	private static final String DEFAULT_ACTION="inicio";


	public ActionForward executeInternal (ActionMapping mapping, ActionForm formulario,
			HttpServletRequest request, HttpServletResponse response) throws SIGAException {

		String destino = null;
		BaseForm miForm = null;

		try {

			miForm = (BaseForm) formulario;
			
			if (miForm != null) {
				String accion=miForm.getAccion();
				if (StringUtils.isEmpty(accion)){
					accion=DEFAULT_ACTION;
				}

				Method metodo=this.getClass().getMethod(accion, new Class[]{ActionMapping.class, miForm.getClass(), 
						HttpServletRequest.class, HttpServletResponse.class});
				if (metodo!=null)
					destino = (String) metodo.invoke(this, new Object[]{mapping, miForm, request, response});
				else
					throw new ClsExceptions("La accion "+miForm.getModo()+" a ejecutar debe existir.");
			}


			//Algunas metodos que responden a llamadas ajax no devuelven nada 
			if (destino==null){
				return null;
			}
			if (StringUtils.isEmpty(destino)){ 
				throw new ClsExceptions("El ActionMapping no puede ser nulo");
			}

			return mapping.findForward(destino);
		} catch (Exception e) {
			throw new SIGAException("messages.general.error",e);
		}
	}


	/**
	 *  
	 * @param idInstitucion
	 * @return <code>true</code> si la institucion del usuario conectado es un consejo, <code>false</code> en caso contrario.
	 */
	protected boolean checkConsejo(String idInstitucion) {
		return idInstitucion.equals("2000") || idInstitucion.startsWith("3");
	}

}
