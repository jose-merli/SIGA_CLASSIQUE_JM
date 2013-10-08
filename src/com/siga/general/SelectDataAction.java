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
public class SelectDataAction extends SIGAActionBase {

	public static final String QUERY_ID_PARAMETER = "queryId";
	public static final String QUERY_PARAMETERS_JSON_PARAMETER = "params";
	public static final String SELECTED_IDS_PARAMETER = "selectedIds";
	public static final String SHOW_SEARCH_BOX_PARAMETER = "showsearchbox";
	public static final String PAGINATED_PARAMETER = "paginated";
	public static final String PAGE_NUMBER_PARAMETER = "page";
	public static final String PAGE_SIZE_PARAMETER = "pageSize";
	public static final String REQUIRED_PARAMETER = "required";
	public static final String FIRST_LABEL_PARAMETER = "firstlabel";

	@SuppressWarnings("unchecked")
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String queryId = request.getParameter(QUERY_ID_PARAMETER);
		String sParams = request.getParameter(QUERY_PARAMETERS_JSON_PARAMETER);
		String selectedIds = request.getParameter(SELECTED_IDS_PARAMETER);
		String firstlabel = request.getParameter(FIRST_LABEL_PARAMETER);
		boolean bShowsearchbox = UtilidadesString.stringToBoolean(request.getParameter(SHOW_SEARCH_BOX_PARAMETER));
		boolean bPaginated = UtilidadesString.stringToBoolean(request.getParameter(PAGINATED_PARAMETER));
		String sPageNumber = request.getParameter(PAGE_NUMBER_PARAMETER);
		String sPageSize = request.getParameter(PAGE_SIZE_PARAMETER);
		boolean required = UtilidadesString.stringToBoolean(request.getParameter(REQUIRED_PARAMETER));
		
		
		HttpSession session = request.getSession();
		UsrBean user=getUserBean(session);
		List<KeyValue> options = new ArrayList<KeyValue>();
		if (required){
			options.add(new KeyValue(KeyValue.DEFAULT_KEY, UtilidadesString.getMensajeIdioma(user.getLanguage(), firstlabel!=null?firstlabel:KeyValue.REQUIRED_VALUE)));
		} else {
			options.add(new KeyValue(KeyValue.DEFAULT_KEY, firstlabel!=null?UtilidadesString.getMensajeIdioma(user.getLanguage(),firstlabel):KeyValue.DEFAULT_VALUE));
		}
		
		if (queryId != null && !queryId.equals("")){
			// GET SERVICE AND FIND METHOD
			SelectDataService selectDataService = (SelectDataService) BusinessManager.getInstance().getService(SelectDataService.class);
			java.lang.reflect.Method selectDataMethod = TagSelect.getSelectDataServiceMethodIgnoreCase(queryId, bShowsearchbox, bPaginated);
			
			if (selectDataMethod != null){
				//BUILD PARAMETERS
				
				
				
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
				if (!params.containsKey("idperfil")){
					String userProfiles = "";
					for (String idperfil: user.getProfile()){
						userProfiles += "," + idperfil;
					}
					userProfiles = userProfiles.substring(1);
					params.put("idperfil", userProfiles);
				}
				
				if (bPaginated){
					int pageNumber = 1;
					int pageSize = Integer.valueOf(SelectDataService.DEFAULT_PAGE_SIZE_VALUE);
					try{
						pageNumber = Integer.valueOf(sPageNumber);
						pageSize = Integer.valueOf(sPageSize);
					} catch (Exception e){
						
					}
					if (pageNumber < 1)
						pageNumber = 1;
					params.put(SelectDataService.PAGE_NUMBER_KEY, String.valueOf(pageNumber));
					if (pageSize < 1)
						pageSize = Integer.valueOf(SelectDataService.DEFAULT_PAGE_SIZE_VALUE);
					params.put(SelectDataService.PAGE_SIZE_KEY, String.valueOf(pageSize));
				}
				
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
			writeSelect(options, response, selectedIds, bPaginated);
		} catch (IOException e){
			ClsLogging.writeFileLogError("ERROR AL TRATAR DE ESCRIBIR LAS OPCIONES DEL SELECT", e, 10);
		}
		
		return null;
	}
	
	private void writeSelect(List<KeyValue> selectOptions, HttpServletResponse response, String selectedIds, boolean paginated) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		Iterator<KeyValue> iteraOptions = selectOptions.iterator();
		List<String> selectedIdsList = null;
		if (selectedIds != null && !selectedIds.equals(""))
			selectedIdsList = Arrays.asList(selectedIds.replaceAll("\\[|\\]", "").split(","));
		String oldLetterSelectNav = "";
		while(iteraOptions.hasNext()){
			KeyValue option = iteraOptions.next();
			if (option == null)
				option = new KeyValue();
			String newLetterSelectNav = "";
			if (option.getValue() != null && option.getValue().length() >= 1 && (oldLetterSelectNav.equals("") || !option.getValue().substring(0,1).equals(oldLetterSelectNav))){
				newLetterSelectNav = option.getValue().substring(0,1);
				oldLetterSelectNav = newLetterSelectNav;
			}
			boolean bSelected = false;
			if (selectedIdsList != null && selectedIdsList.contains(option.getKey()))
				bSelected = true;
			if (paginated)
				response.getWriter().write(KeyValue.getHtmlLi(option, bSelected, newLetterSelectNav));
			else
				response.getWriter().write(KeyValue.getHtmlOption(option, bSelected));
		}
		if (selectOptions.size() <= 1 && paginated){
			if (selectOptions.iterator().next().getKey().equals(KeyValue.DEFAULT_KEY))
				response.getWriter().write("<li class='notFound'></li>");
		}
		response.getWriter().flush();
	}
	
}
