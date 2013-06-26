package com.siga.general;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.redabogacia.sigaservices.app.services.gen.SelectDataService;
import org.redabogacia.sigaservices.app.util.KeyValue;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.tlds.TagSelect;

import es.satec.businessManager.BusinessManager;

/**
 * Action genérico que obtiene los elementos option de los select a través del 
 * {@link org.redabogacia.sigaservices.app.services.gen.SelectDataService}. 
 * Para invocarlo la request debería tener el parámetro {@link SelectDataAction.QUERY_ID_PARAMETER}
 * que es el nombre del método que se quiere invocar del {@link org.redabogacia.sigaservices.app.services.gen.SelectDataService} y opcionalmente,
 * un parámetro {@link SelectDataAction.QUERY_PARAMETERS_JSON_PARAMETER} que contiene los parámetros de la query formateados en un JSON. Además
 * puede recibir los IDs que están seleccionados {@link SelectDataAction.SELECTED_IDS_PARAMETER}, en caso de ser una carga inicial, y si es
 * un campo obligatório o no incluyendo la opción por defecto si procede {@link SelectDataAction.REQUIRED_PARAMETER}.
 * El HTML finalmente se escribe en el {@link javax.servlet.http.HttpServletResponse} directamente.
 * 
 * @author borjans
 *
 */
public class SelectDataAction extends Action {

	public static final String QUERY_ID_PARAMETER = "queryId";
	public static final String QUERY_PARAMETERS_JSON_PARAMETER = "params";
	public static final String SELECTED_IDS_PARAMETER = "selectedIds";
	public static final String SHOW_SEARCH_BOX_PARAMETER = "showsearchbox";
	public static final String REQUIRED_PARAMETER = "required";

	@SuppressWarnings("unchecked")
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String queryId = request.getParameter(QUERY_ID_PARAMETER);
		String sParams = request.getParameter(QUERY_PARAMETERS_JSON_PARAMETER);
		String selectedIds = request.getParameter(SELECTED_IDS_PARAMETER);
		boolean bShowsearchbox = UtilidadesString.stringToBoolean(request.getParameter(SHOW_SEARCH_BOX_PARAMETER));
		boolean required = UtilidadesString.stringToBoolean(request.getParameter(REQUIRED_PARAMETER));
		
		List<KeyValue> options = new ArrayList<KeyValue>();
		if (!required){
			options.add(new KeyValue(KeyValue.DEFAULT_KEY, KeyValue.DEFAULT_VALUE));
		}
		
		if (queryId != null && !queryId.equals("")){
			// GET SERVICE AND FIND METHOD
			SelectDataService selectDataService = (SelectDataService) BusinessManager.getInstance().getService(SelectDataService.class);
			java.lang.reflect.Method selectDataMethod = TagSelect.getSelectDataServiceMethodIgnoreCase(queryId, bShowsearchbox);
			
			if (selectDataMethod != null){
				//BUILD PARAMETERS
				HttpSession session = request.getSession();
				UsrBean user=(UsrBean)session.getAttribute("USRBEAN");
				
				HashMap<String, String> params = new HashMap<String, String>();
				if (sParams != null && !sParams.equals("")){
					try {
						params.putAll(new ObjectMapper().readValue(sParams, HashMap.class));
					} catch (JsonParseException e) {
						ClsLogging.writeFileLogError("ERROR DE FORMATO DE JSON AL TRATAR DE OBTENER LOS PARÁMETROS DEL QUERY ID: " + queryId + ". (PARAMS = "+sParams+")", e, 10);
					} catch (JsonMappingException e) {
						ClsLogging.writeFileLogError("ERROR DE FORMATO DE JSON AL TRATAR DE OBTENER LOS PARÁMETROS DEL QUERY ID: " + queryId + ". (PARAMS = "+sParams+")", e, 10);
					} catch (IOException e) {
						ClsLogging.writeFileLogError("ERROR AL TRATAR DE OBTENER LOS PARÁMETROS DEL QUERY ID: " + queryId + ". (PARAMS = "+sParams+")", e, 10);
					}
				}
				
				if (!params.containsKey("idioma"))
					params.put("idioma", user.getLanguage());
				if (!params.containsKey("idinstitucion"))
					params.put("idinstitucion", user.getLocation());
				params = TagSelect.normalizeJSONparams(params);
				// CALL SelectDataService				
				try {
					options.addAll((List<KeyValue>) selectDataMethod.invoke(selectDataService, params));
				} catch (IllegalArgumentException e) {
					ClsLogging.writeFileLogError("ERROR AL EJECUTAR QUERY ID: " + queryId + ".", e, 10);
				} catch (IllegalAccessException e) {
					ClsLogging.writeFileLogError("ERROR AL EJECUTAR QUERY ID: " + queryId + ".", e, 10);
				} catch (InvocationTargetException e) {
					ClsLogging.writeFileLogError("ERROR AL EJECUTAR QUERY ID: " + queryId + ".", e, 10);
				}
			} else {
				ClsLogging.writeFileLog("NO SE HA ENCONTRADO EL MÉTODO " + queryId + " EN EL SELECTDATASERVICE", 10);
			}
		} else {
			ClsLogging.writeFileLog("NO SE HA ENCONTRADO EL PARÁMETRO " + QUERY_ID_PARAMETER + " EN LA REQUEST", 10);
		}
		
		try{
			writeSelect(options, response, selectedIds);
		} catch (IOException e){
			ClsLogging.writeFileLogError("ERROR AL TRATAR DE ESCRIBIR LAS OPCIONES DEL SELECT", e, 10);
		}
		
		return null;
	}
	
	private void writeSelect(List<KeyValue> selectOptions, HttpServletResponse response, String selectedIds) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		Iterator<KeyValue> iteraOptions = selectOptions.iterator();
		List<String> selectedIdsList = null;
		if (selectedIds != null && !selectedIds.equals(""))
			selectedIdsList = Arrays.asList(selectedIds.replaceAll("\\[|\\]", "").split(","));
		while(iteraOptions.hasNext()){
			KeyValue option = iteraOptions.next();
			boolean bSelected = false;
			if (selectedIdsList != null && selectedIdsList.contains(option.getKey()))
				bSelected = true;
			response.getWriter().write(KeyValue.getHtmlOption(option, bSelected));
		}
		response.getWriter().flush();
	}
	
}
