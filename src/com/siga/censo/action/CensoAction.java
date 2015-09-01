package com.siga.censo.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.services.gen.SelectDataService;
import org.redabogacia.sigaservices.app.util.KeyValue;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.general.MasterAction;
import com.siga.general.SIGAException;

import es.satec.businessManager.BusinessManager;

public class CensoAction  extends MasterAction{
	
	/** 
	 *  Funcion que atiende a las peticiones. Segun el valor del parametro modo del formulario ejecuta distintas acciones
	 * @param  mapping - Mapeo de los struts
	 * @param  formulario -  Action Form asociado a este Action
	 * @param  request - objeto llamada HTTP 
	 * @param  response - objeto respuesta HTTP
	 * @return  String  Destino del action  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	
	protected ActionForward executeInternal (ActionMapping mapping, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SIGAException {
		try {				    		
			String modo = (String) request.getParameter("modo");

			 if (modo == null || modo.equalsIgnoreCase("") || modo.equalsIgnoreCase("getTratamientoAPartirDelSexo")){
			
					getTratamientoAPartirDelSexo(request, response);
			 }
				
		} catch (Exception e) {
			throw new SIGAException("Error en la Aplicación",e);
		}
		
		return null;
	}
	
	
	
	/**
	 * Método encargado de obtener los tratamientos a partir del Sexo y convertir la información en un objeto JSON
	 * @param request
	 * @param response
	 * @throws JSONException
	 * @throws IOException
	 */
	protected void getTratamientoAPartirDelSexo(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException 
	{
		//Declaración de atributos
		HashMap<String, String> params = new HashMap<String, String>();
		JSONObject objetoJSON = new JSONObject();
		JSONArray listaArrayJSON = new JSONArray();
    	
		//Obtenemos el usuario de sesión
		UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
		//Recogemos el parametro enviado por ajax
		String idSexo = request.getParameter("idSexo");
				
		//Si el valor de idSexo es cero es que es neutro caro en el que exista neutro.
		if(idSexo != null && ("".equalsIgnoreCase(idSexo) || idSexo.equals(ClsConstants.CERO)))
		{
			idSexo=ClsConstants.GENERO_NEUTRO;
		}
		//Introducimos los parametros necesarios para la query
		params.put("idioma", user.getLanguage());
		params.put("idSexo", idSexo);
		
		//Llamada al servicio
		SelectDataService service = (SelectDataService) BusinessManager.getInstance().getService(SelectDataService.class);
		List<KeyValue> tratamientos = service.getTratamientosPorSexo(params);
		
		//Obtenemos los datos y lo convertimos en objeto json
		for(int i=0;i<tratamientos.size();i++){
			KeyValue tratamientoAux = (KeyValue) tratamientos.get(i);
			objetoJSON.put("id",tratamientoAux.getKey());
			objetoJSON.put("descripcion",tratamientoAux.getValue());
		
			listaArrayJSON.put(objetoJSON);
			objetoJSON = new JSONObject();
			
		}
		
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Content-Type", "application/json;charset=utf-8"); 
	     response.setHeader("X-JSON", listaArrayJSON.toString());
		 response.getWriter().write(listaArrayJSON.toString());
	
	}	
}
