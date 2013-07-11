package com.siga.tlds;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;
import org.redabogacia.sigaservices.app.mapper.SelectDataMapper;
import org.redabogacia.sigaservices.app.services.gen.SelectDataService;
import org.redabogacia.sigaservices.app.util.KeyValue;

import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.bea.core.repackaged.springframework.util.Assert;
import com.bea.core.repackaged.springframework.util.StringUtils;
import com.siga.Utilidades.UtilidadesString;

import es.satec.businessManager.BusinessManager;

/**
 * 
 * @author borjans
 * 
 */
public class TagSelect extends TagSupport {

	//private final static Logger log = Logger.getLogger(TagSelect.class);
	
	private static final long serialVersionUID = 1661010558139313776L;

	public static final String DEFAULT_NOT_SELECTED_ID = "-1";	
	public static final String DATA_JSON_OPTION_KEY = "options";

	private static final int DEFAULT_SELECT_WIDTH = 165;

	private String id;
	private String label;
	private String queryId;
	private String queryParamId;
	//private String sql;
	private String childrenIds;
	private List<String> selectedIds;
	private boolean multiple = false;
	private int lines = 1;
	// TODO: PAGINACIÓN
	//private boolean showAll = false;
	// TODO: FILTRADO
	//private boolean showFilterBox = false;
	private boolean disabled = false;
	private boolean readOnly = false;
	private boolean required = false;
	private boolean hideIfnoOptions = false;
	private String params;
	private String parentQueryParamIds = "";
	private String cssClass = "boxCombo";
	private String width;
	private boolean showSearchBox = false;
	private String searchBoxMaxLength;
	private String searchBoxWidth;
	private String selectParentMsg = "";
	private String selectParentLocalizedMsg = "";
	private String searchkey = "";
	private String dataJSON = "";
	private String onLoadCallback = "";
	
	private UsrBean user;

	@Override
	public int doStartTag() throws JspException {
		this.user = (UsrBean) pageContext.getSession().getAttribute("USRBEAN");
		return super.doStartTag();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int doEndTag() throws JspException {
		List<KeyValue> options = new ArrayList<KeyValue>();
		if (StringUtils.hasText(this.dataJSON)){
			try {
				final HashMap<String, List<KeyValue>> hmData = (HashMap<String, List<KeyValue>>) UtilidadesString.createHashMap(this.dataJSON, new TypeReference<HashMap<String, List<KeyValue>>>() {});
				options = hmData.get(DATA_JSON_OPTION_KEY);				
			} catch (Exception e) {
				ClsLogging.writeFileLogError("ERROR AL TRATAR DE OBTENER LAS OPTIONS (" + this.dataJSON + ") DEL SELECT CON ID: " + this.id, e, 10);
			}
		} else {
			SelectDataService selectDataService = (SelectDataService) BusinessManager.getInstance().getService(SelectDataService.class);
			String selectDataServiceMethodName = this.queryId;			
			java.lang.reflect.Method selectDataMethod = TagSelect.getSelectDataServiceMethodIgnoreCase(selectDataServiceMethodName, this.showSearchBox);
			
			if (this.required){
				options.add(new KeyValue(KeyValue.DEFAULT_KEY, KeyValue.REQUIRED_VALUE));
			} else {
				options.add(new KeyValue(KeyValue.DEFAULT_KEY, KeyValue.DEFAULT_VALUE));
			}
			
			if (selectDataMethod != null){
				//BUILD PARAMETERS
				pageContext.getResponse().setContentType("text/html");
				
				HashMap<String, String> params = new HashMap<String, String>();
				
				this.selectParentLocalizedMsg =UtilidadesString.getMensajeIdioma(user.getLanguage(), this.selectParentMsg);
				
				// INTENTA CARGAR ESTE SELECT Y LOS SELECTs ANIDADOS USANDO LOS PARÁMETROS DE LA REQUEST
				// SI LOS PARÁMETROS DE LA REQUEST NO COINCIDEN CON LOS DE LA QUERY (FALTAN queryParamId)
				// LOS SELECTs ANIDADOS SE CARGARÁN EN EL EVENTO LOAD DE LA PÁGINA LLAMANDO POR AJAX AL
				// SELECTDATAACTION			
				Enumeration<String> requestParametersNames = pageContext.getRequest().getParameterNames();
				while (requestParametersNames.hasMoreElements()){
					String requestParameterName = requestParametersNames.nextElement();
					String requestParameterValue = pageContext.getRequest().getParameter(requestParameterName);
					params.put(requestParameterName, requestParameterValue);
				}
				//params.putAll(pageContext.getRequest().getParameterMap());
				
				if (this.params != null && !this.params.equals("")){
					try {
						params.putAll(new ObjectMapper().readValue(this.params, HashMap.class));
					} catch (JsonParseException e) {
						ClsLogging.writeFileLogError("ERROR DE FORMATO DE JSON AL TRATAR DE OBTENER LOS PARÁMETROS DEL QUERY ID: " + queryId + ". (PARAMS = "+params+")", e, 10);
					} catch (JsonMappingException e) {
						ClsLogging.writeFileLogError("ERROR DE FORMATO DE JSON AL TRATAR DE OBTENER LOS PARÁMETROS DEL QUERY ID: " + queryId + ". (PARAMS = "+params+")", e, 10);
					} catch (IOException e) {
						ClsLogging.writeFileLogError("ERROR AL TRATAR DE OBTENER LOS PARÁMETROS DEL QUERY ID: " + queryId + ". (PARAMS = "+params+")", e, 10);
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
				
				if (StringUtils.hasText(this.searchkey)){
					params.put(SelectDataMapper.SEARCH_KEY_PARAMETER, this.searchkey);
				}
				params = normalizeJSONparams(params);
				
				// IF SELECTED IDs IS EMPTY, TRY TO FIND SELECTED ID IN REQUEST
				if ((this.selectedIds == null || this.selectedIds.size() <= 0) && params.containsKey(this.id)){
					this.selectedIds = new ArrayList<String>();
					this.selectedIds.add(params.get(this.id));
				}
				
				boolean bGetSelectData = true;
				if (this.parentQueryParamIds != null && !"".equals(this.parentQueryParamIds)){
					String[] parentQueryIdsArray = this.parentQueryParamIds.split(",");
					for (String parentQueryId : parentQueryIdsArray){
						if (!params.containsKey(parentQueryId) || (params.containsKey(parentQueryId) && 
								(params.get(parentQueryId) == null || params.get(parentQueryId).equals("") || 
								params.get(parentQueryId).equals("-1") || 
								params.get(parentQueryId).equals("[]")))){
							bGetSelectData = false;
							break;
						}
					}
				}
				
				if (bGetSelectData){
					try {
						options.addAll((List<KeyValue>) selectDataMethod.invoke(selectDataService, params));
					} catch (IllegalArgumentException e) {
						ClsLogging.writeFileLogError("ERROR AL EJECUTAR QUERY ID: " + queryId + ".", e, 10);
					} catch (IllegalAccessException e) {
						ClsLogging.writeFileLogError("ERROR AL EJECUTAR QUERY ID: " + queryId + ".", e, 10);
					} catch (InvocationTargetException e) {
						ClsLogging.writeFileLogError("ERROR AL EJECUTAR QUERY ID: " + queryId + ".", e, 10);
					}
				} else if (this.selectParentMsg != null && !"".equals(this.selectParentMsg)){
					options = new ArrayList<KeyValue>();
					options.add(new KeyValue(KeyValue.DEFAULT_KEY, selectParentLocalizedMsg));
				}
							
			} else {
				ClsLogging.writeFileLogError("NO SE HA ENCONTRADO EL MÉTODO " + queryId + " EN EL SELECTDATASERVICE", (HttpServletRequest) pageContext.getRequest(), 10);
			}
		}
		try{
			writeSelect(options);
		} catch (IOException e){
			ClsLogging.writeFileLogError("ERROR AL TRATAR DE ESCRIBIR LAS OPCIONES DEL SELECT", e, 10);
		}
		
		return super.doEndTag();
	}
	
	public static HashMap<String, String> normalizeJSONparams(
			HashMap<String, String> params) {
		if (params != null){
			ArrayList<JSONObject> jsonParams = new ArrayList<JSONObject>();
			Iterator<String> iteraParamKeys = params.keySet().iterator();
			while (iteraParamKeys.hasNext()){
				String paramKey = iteraParamKeys.next();
				try {
					String paramValue = params.get(paramKey);
					jsonParams.add(new JSONObject(paramValue));
					iteraParamKeys.remove();
				} catch (Exception e) {
				}
			}
			Iterator<JSONObject> iteraJsonParams = jsonParams.iterator();
			while (iteraJsonParams.hasNext()){
				JSONObject jsonParam = iteraJsonParams.next();
				@SuppressWarnings("unchecked")
				Iterator<String> iteraJsonParamKeys = jsonParam.keys();
				while (iteraJsonParamKeys.hasNext()){
					String jsonParamKey = iteraJsonParamKeys.next();
					try {
						params.put(jsonParamKey, jsonParam.get(jsonParamKey).toString());
					} catch (JSONException e) {
						ClsLogging.writeFileLogError("Se ha producido un error al intentar parsear un parámetro JSON: ", e, 10);
					}
				}
			}
		}
		return params;
	}

	private void writeSelect(List<KeyValue> selectOptions) throws IOException {
		
		String childrenInfo = "";
		if (StringUtils.hasText(this.childrenIds)){
			childrenInfo = "class='parentSelect' data-childrenIds='"+this.childrenIds+"'";
		}
		
		String queryParam = "";
		if (StringUtils.hasText(this.queryParamId)){
			queryParam = " data-queryparamid='"+this.queryParamId+"' ";
		}
		
		String sRequired = " data-required='false' ";
		if (this.required){
			sRequired = " data-required='true' ";
		}
		
		String searchBoxSize = "";
		if (this.searchBoxWidth != null){
			searchBoxSize = " size='"+this.searchBoxWidth+"' ";
		}

		String sSearchBoxMaxLength = "";
		if (this.searchBoxMaxLength != null){
			sSearchBoxMaxLength = " maxlength='"+this.searchBoxMaxLength+"' ";
		}
		
		String sSearchKey = "";
		if (this.searchkey != null){
			sSearchKey = " data-searchkey='"+this.searchkey+"' ";
		}
		
		String dataSelectParentMsg = "";
		if (StringUtils.hasText(this.selectParentLocalizedMsg)){
			dataSelectParentMsg = " data-selectparentmsg='"+this.selectParentLocalizedMsg+"' ";
		}
		KeyValue selectedOption = new KeyValue(true);
		
		String sShowSearchBox = "";
		if (showSearchBox)
			sShowSearchBox = " data-showsearchbox='true' ";
		String sMultiple = "";
		
		String sHideIfnoOptions = " data-hideifnooptions='"+Boolean.toString(this.hideIfnoOptions).toLowerCase()+"' ";
		
		String disabledInputStyle = "style='display:none;";
		String selectStyle = "style='display:inline;";
		String styleSearchBox = "style='display:inline;'";
		if (this.disabled || this.readOnly){
			selectStyle = disabledInputStyle;
			disabledInputStyle = "style='display:inline;";
		}		
		
		String selectWidth = String.valueOf(DEFAULT_SELECT_WIDTH);
		String divWidth = String.valueOf(DEFAULT_SELECT_WIDTH + 20);
		if (StringUtils.hasText(this.width)){
			selectWidth = this.width;
			try{
				 int iWidth = Integer.valueOf(this.width);
				 divWidth = String.valueOf(iWidth + 20);
			} catch (Exception e){
				divWidth = this.width;
			}
		}
		
		selectStyle += " width:"+selectWidth+";";
		disabledInputStyle += " width:"+selectWidth+";";
		
		if (!selectStyle.equals(""))
			selectStyle += "'";
		if (!disabledInputStyle.equals(""))
			disabledInputStyle += "'";			
		
		String dataWidth = " data-width = '"+divWidth+"' ";
		
		//String wrapDivStyle = " style='display:inline;width:"+divWidth+";' ";
		String wrapDivStyle = " style='display:inline;' ";
		String selectLoaderStyle = "style='display:inline;'";
		
		if (multiple){
			sMultiple = " multiple size='"+this.lines+"'";
			selectStyle = " style='display:inline;width:"+selectWidth+";";
			if (this.disabled || this.readOnly)
				selectStyle += " disabled ";
		}
		
		// IMPRIME HTML
		PrintWriter out = pageContext.getResponse().getWriter();
		out.println("<div id='"+this.id+"_tagSelectDiv' class='tagSelectDiv' "+dataWidth+wrapDivStyle+">");
		String sOnloadCallback = "";
		if (this.onLoadCallback != null && !"".equals(this.onLoadCallback)){
			sOnloadCallback = " data-onloadcallback='"+this.id+"_callback' ";
			out.println("<script type=\"text/javascript\">var "+this.id+"_callback = function(){"+this.onLoadCallback+"};</script>");
		}
		if (StringUtils.hasText(this.label)){
			String localizedLabelText = UtilidadesString.getMensajeIdioma(user.getLanguage(), this.label);
			out.println("<label for='"+this.id+"' style='display:inline;'>"+localizedLabelText+"</label>");
		}
		
		if (!multiple && showSearchBox){
			out.println("<input type='text' id='"+this.id+"_searchBox' name='"+this.id+"_searchBox' class='tagSelect_searchBox box' "+styleSearchBox+sSearchBoxMaxLength+searchBoxSize+" />");
		}
		
		out.println("<div id='"+this.id+"_loader' class='tagSelect_loader'"+selectLoaderStyle+">");
		out.println("<select id='"+this.id+"' class='tagSelect "+cssClass+"' name='"+this.id+"' "+" data-queryId='"+this.queryId+"' data-iniVal='"+this.selectedIds+"' "+childrenInfo+sRequired+" "+queryParam+dataSelectParentMsg+sSearchKey+sMultiple+sShowSearchBox+sHideIfnoOptions+sOnloadCallback+selectStyle+">");
		Iterator<KeyValue> iteraOptions = selectOptions.iterator();
		while(iteraOptions.hasNext()){
			KeyValue keyValue = iteraOptions.next();
			if (keyValue == null)
				keyValue = new KeyValue();
			boolean selected = false;
			if (this.selectedIds != null && this.selectedIds.size() > 0)
				selected = this.selectedIds.contains(keyValue.getKey());
			if (selected)
				selectedOption = keyValue;
			out.println(KeyValue.getHtmlOption(keyValue,selected));
		}
		out.println("</select></div>");
		if (!multiple)
			out.println("<input type='text' readonly='readonly' class='boxConsulta tagSelect_disabled' "+disabledInputStyle+" id='"+this.id+"_disabled' name='"+this.id+"_disabled' value='"+(selectedOption.getValue().equals(KeyValue.DEFAULT_VALUE)?"":selectedOption.getValue())+"'/>");
		out.println("</div>");// this.id_div;
	}

	public static java.lang.reflect.Method getSelectDataServiceMethodIgnoreCase(String methodName, boolean bShowSearchBox){
		java.lang.reflect.Method SelectDataServiceMethod = null;
		try{
			Assert.hasText(methodName);
			methodName = methodName.trim();
			if (bShowSearchBox){
				methodName += SelectDataService.WITH_SEARCH_KEY_METHOD_SUFIX;
			}
			
			java.lang.reflect.Method[] selectDataMethods = SelectDataService.class.getMethods();
			for(java.lang.reflect.Method method : selectDataMethods){
				if (method.getName().equalsIgnoreCase(methodName)){
					SelectDataServiceMethod = method;
					break;
				}
			}
		} catch(Exception e){
			String errorMsg = "NO QUERY METHOD FOUND FOR "+methodName+" [TagSelect.getSelectDataServiceMethodIgnoreCase("+methodName+")]";
			//log.error("NO QUERY METHOD FOUND FOR "+methodName+" [TagSelect.getSelectDataServiceMethodIgnoreCase("+methodName+")]",e);
			ClsLogging.writeFileLogError(errorMsg, e, 10);
		}
		return SelectDataServiceMethod;
	}

	
	
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the queryId
	 */
	public String getQueryId() {
		return queryId;
	}

	/**
	 * @param queryId the queryId to set
	 */
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	/**
	 * @return the childrenIds
	 */
	public String getChildrenIds() {
		return childrenIds;
	}

	/**
	 * @param childrenIds the childrenIds to set
	 */
	public void setChildrenIds(String childrenIds) {
		this.childrenIds = childrenIds.trim();
	}

	/**
	 * @return the selectedIds
	 */
	public List<String> getSelectedIds() {
		return selectedIds;
	}

	/**
	 * @param selectedIds the selectedIds to set
	 */
	public void setSelectedIds(List<String> selectedIds) {
		this.selectedIds = selectedIds;
	}
	public void setSelectedIds(String selectedIds) {
		this.selectedIds = Arrays.asList(selectedIds.trim().split(","));
	}
	
	/**
	 * @return the multiple
	 */
	public boolean isMultiple() {
		return multiple;
	}

	/**
	 * @param multiple the multiple to set
	 */
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}
	
	public void setMultiple(String multiple) {
		this.multiple = UtilidadesString.stringToBoolean(multiple.trim());
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = UtilidadesString.stringToBoolean(disabled.trim());
	}

	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public void setReadOnly(String readOnly) {
		this.readOnly = UtilidadesString.stringToBoolean(readOnly.trim());
	}

	/**
	 * @return the params
	 */
	public String getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * @return the queryParamId
	 */
	public String getQueryParamId() {
		return queryParamId;
	}

	/**
	 * @param queryParamId the queryParamId to set
	 */
	public void setQueryParamId(String queryParamId) {
		this.queryParamId = queryParamId.trim();
	}

	/**
	 * @return the cssClass
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * @param cssClass the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}	

	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	public void setRequired(String required) {
		this.required = UtilidadesString.stringToBoolean(required.trim());
	}

	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width.trim();
	}

	/**
	 * @return the selectParentMsg
	 */
	public String getSelectParentMsg() {
		return selectParentMsg;
	}

	/**
	 * @param selectParentMsg the selectParentMsg to set
	 */
	public void setSelectParentMsg(String selectParentMsg) {
		this.selectParentMsg = selectParentMsg.trim();
	}

	/**
	 * @return the parentQueryIds
	 */
	public String getParentQueryParamIds() {
		return parentQueryParamIds;
	}

	/**
	 * @param parentQueryIds the parentQueryIds to set
	 */
	public void setParentQueryParamIds(String parentQueryIds) {
		this.parentQueryParamIds = parentQueryIds.trim();
	}

	/**
	 * @return the showSearchBox
	 */
	public boolean isShowSearchBox() {
		return showSearchBox;
	}

	/**
	 * @param showSearchBox the showSearchBox to set
	 */
	public void setShowSearchBox(boolean showSearchBox) {
		this.showSearchBox = showSearchBox;
	}
	public void setShowSearchBox(String showSearchBox) {
		this.showSearchBox = UtilidadesString.stringToBoolean(showSearchBox);
	}

	/**
	 * @return the searchkey
	 */
	public String getSearchkey() {
		return searchkey;
	}

	/**
	 * @param searchkey the searchkey to set
	 */
	public void setSearchkey(String searchkey) {
		this.searchkey = searchkey;
	}
	
	/**
	 * @return the searchBoxMaxLength
	 */
	public String getSearchBoxMaxLength() {
		return searchBoxMaxLength;
	}

	/**
	 * @param searchBoxMaxLength the searchBoxMaxLength to set
	 */
	public void setSearchBoxMaxLength(String searchBoxMaxLength) {
		this.searchBoxMaxLength = searchBoxMaxLength;
	}

	/**
	 * @return the searchBoxWidth
	 */
	public String getSearchBoxWidth() {
		return searchBoxWidth;
	}

	/**
	 * @param searchBoxWidth the searchBoxWidth to set
	 */
	public void setSearchBoxWidth(String searchBoxWidth) {
		this.searchBoxWidth = searchBoxWidth;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the dataJSON
	 */
	public String getDataJSON() {
		return dataJSON;
	}

	/**
	 * @param dataJSON the dataJSON to set
	 */
	public void setDataJSON(String dataJSON) {
		this.dataJSON = dataJSON;
	}

	/**
	 * @return the lines
	 */
	public int getLines() {
		return lines;
	}

	/**
	 * @param lines the lines to set
	 */
	public void setLines(int lines) {
		this.lines = lines;
	}

	/**
	 * @return the hideIfnoOptions
	 */
	public boolean isHideIfnoOptions() {
		return hideIfnoOptions;
	}

	/**
	 * @param hideIfnoOptions the hideIfnoOptions to set
	 */
	public void setHideIfnoOptions(boolean hideIfnoOptions) {
		this.hideIfnoOptions = hideIfnoOptions;
	}
	public void setHideIfnoOptions(String hideIfnoOptions) {
		this.hideIfnoOptions = UtilidadesString.stringToBoolean(hideIfnoOptions.trim());;
	}

	/**
	 * @return the onLoadCallback
	 */
	public String getOnLoadCallback() {
		return onLoadCallback;
	}

	/**
	 * @param onLoadCallback the onLoadCallback to set
	 */
	public void setOnLoadCallback(String onLoadCallback) {
		this.onLoadCallback = onLoadCallback.trim();
	}
	
}
