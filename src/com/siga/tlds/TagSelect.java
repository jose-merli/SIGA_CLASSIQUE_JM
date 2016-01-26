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
import java.util.Map;

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
	// TODO: PAGINACI�N
	private boolean paginated = false;
	private int page = 0;
	private int pageSize = 1000;
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
	private String firstLabel=null;
	

	@Override
	public int doStartTag() throws JspException {
		this.user = (UsrBean) pageContext.getSession().getAttribute("USRBEAN");
		return super.doStartTag();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int doEndTag() throws JspException {
		List<KeyValue> options = new ArrayList<KeyValue>();
		if (this.required){
			options.add(new KeyValue(KeyValue.DEFAULT_KEY, UtilidadesString.getMensajeIdioma(user.getLanguage(), this.firstLabel!=null?this.firstLabel:KeyValue.REQUIRED_VALUE)));
		} else {
			options.add(new KeyValue(KeyValue.DEFAULT_KEY, this.firstLabel!=null?UtilidadesString.getMensajeIdioma(user.getLanguage(),this.firstLabel):KeyValue.DEFAULT_VALUE));
		}
		
		
		
		if (StringUtils.hasText(this.dataJSON)){
			try {
				final HashMap<String, List<KeyValue>> hmData = (HashMap<String, List<KeyValue>>) UtilidadesString.createHashMap(this.dataJSON, new TypeReference<HashMap<String, List<KeyValue>>>() {});
				options.addAll(hmData.get(DATA_JSON_OPTION_KEY));				
			} catch (Exception e) {
				ClsLogging.writeFileLogError("ERROR AL TRATAR DE OBTENER LAS OPTIONS (" + this.dataJSON + ") DEL SELECT CON ID: " + this.id, e, 10);
			}
		} else {
			SelectDataService selectDataService = (SelectDataService) BusinessManager.getInstance().getService(SelectDataService.class);
			String selectDataServiceMethodName = this.queryId;			
			java.lang.reflect.Method selectDataMethod = TagSelect.getSelectDataServiceMethodIgnoreCase(selectDataServiceMethodName, this.showSearchBox, this.paginated);
			
			if (selectDataMethod != null){
				//BUILD PARAMETERS
				pageContext.getResponse().setContentType("text/html");
				
				HashMap<String, String> params = new HashMap<String, String>();
				
				this.selectParentLocalizedMsg =UtilidadesString.getMensajeIdioma(user.getLanguage(), this.selectParentMsg);
				
				// INTENTA CARGAR ESTE SELECT Y LOS SELECTs ANIDADOS USANDO LOS PAR�METROS DE LA REQUEST
				// SI LOS PAR�METROS DE LA REQUEST NO COINCIDEN CON LOS DE LA QUERY (FALTAN queryParamId)
				// LOS SELECTs ANIDADOS SE CARGAR�N EN EL EVENTO LOAD DE LA P�GINA LLAMANDO POR AJAX AL
				// SELECTDATAACTION			
				Enumeration<String> requestParametersNames = pageContext.getRequest().getParameterNames();
				while (requestParametersNames.hasMoreElements()){
					String requestParameterName = requestParametersNames.nextElement();
					String requestParameterValue = pageContext.getRequest().getParameter(requestParameterName);
					params.put(requestParameterName.toLowerCase(), requestParameterValue);
				}
				//params.putAll(pageContext.getRequest().getParameterMap());
				
				if (this.params != null && !this.params.equals("")){
					try {
						params.putAll(new ObjectMapper().readValue(this.params, HashMap.class));
					} catch (JsonParseException e) {
						ClsLogging.writeFileLogError("ERROR DE FORMATO DE JSON AL TRATAR DE OBTENER LOS PAR�METROS DEL QUERY ID: " + queryId + ". (PARAMS = "+params+")", e, 10);
					} catch (JsonMappingException e) {
						ClsLogging.writeFileLogError("ERROR DE FORMATO DE JSON AL TRATAR DE OBTENER LOS PAR�METROS DEL QUERY ID: " + queryId + ". (PARAMS = "+params+")", e, 10);
					} catch (IOException e) {
						ClsLogging.writeFileLogError("ERROR AL TRATAR DE OBTENER LOS PAR�METROS DEL QUERY ID: " + queryId + ". (PARAMS = "+params+")", e, 10);
					}
				}
				
				if (!params.containsKey("idioma")|| params.get("idioma").equals(""))
					params.put("idioma", user.getLanguage());
				if (!params.containsKey("idinstitucion") || params.get("idinstitucion").equals(""))
					params.put("idinstitucion", user.getLocation());
				if (!params.containsKey("idperfil") || params.get("idperfil").equals("")){
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
				
				if (this.paginated){
					if (this.page < 1)
						this.page = 1;
					params.put(SelectDataService.PAGE_NUMBER_KEY, String.valueOf(this.page));
					if (this.pageSize < 1)
						this.pageSize = Integer.valueOf(SelectDataService.DEFAULT_PAGE_SIZE_VALUE);
					params.put(SelectDataService.PAGE_SIZE_KEY, String.valueOf(this.pageSize));
					if (this.selectedIds != null && this.selectedIds.size() == 1){
						//TODO: Ojo, un select paginado no puede ser m�ltiple
						//		habr�a que restringirlo de alguna forma en la configuraci�n
						params.put("selectedid", this.selectedIds.iterator().next());
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
				ClsLogging.writeFileLogError("NO SE HA ENCONTRADO EL M�TODO " + queryId + " EN EL SELECTDATASERVICE", (HttpServletRequest) pageContext.getRequest(), 10);
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
						ClsLogging.writeFileLogError("Se ha producido un error al intentar parsear un par�metro JSON: ", e, 10);
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
		String divDataReadOnly = " data-readonly='false' ";
		String divClass = " class='tagSelect tagSelectDiv' ";
		String selectReadOnly = " data-readonly='false' ";
		String selectClass = " class='tagSelect tagSelect_searchBox box' ";
		String divLoaderClass= " class='tagSelect tagSelect_loader' ";
		String labelClass = "  class='tagSelect tagSelect_label' ";
		String inputDisabledClass= " class='tagSelect boxConsulta tagSelect_disabled' ";
		String selectLoaderStyle = "style='display:inline;'";
		
		if (this.disabled){
			selectStyle = disabledInputStyle;
			disabledInputStyle = "style='display:inline;";
			divDataReadOnly = " data-readonly='true' ";
			selectReadOnly = " disabled='disabled' data-readonly='true' ";
			divClass = " class='tagSelect tagSelectDiv disabled' ";
			selectClass = " class='tagSelect tagSelect_searchBox boxConsulta disabled' readonly ";
			divLoaderClass= " class='tagSelect tagSelect_loader disabled' ";
			labelClass = "  class='tagSelect tagSelect_label disabled' ";
			inputDisabledClass= " class='tagSelect boxConsulta tagSelect_disabled disabled' ";
			selectLoaderStyle = "style='display:none;'";
			
		} else {
			if (this.readOnly){
				selectStyle = disabledInputStyle;
				disabledInputStyle = "style='display:inline;";
				divDataReadOnly = " data-readonly='true' ";
				selectReadOnly = " data-readonly='true' ";
				divClass = " class='tagSelect tagSelectDiv' ";
				selectClass = " class='tagSelect tagSelect_searchBox boxConsulta' readonly";
				divLoaderClass= " class='tagSelect tagSelect_loader' ";
				labelClass = "  class='tagSelect tagSelect_label' ";
				inputDisabledClass= " class='tagSelect boxConsulta tagSelect_disabled' ";
				selectLoaderStyle = "style='display:none;'";
			}
		}
		
		String selectWidth = String.valueOf(DEFAULT_SELECT_WIDTH);
		String divWidth = String.valueOf(DEFAULT_SELECT_WIDTH + 20);
		if (StringUtils.hasText(this.width)){			 			
			selectWidth = this.width;
			try{
				 int iWidth = Integer.valueOf(this.width) + 20;
				 
				 if (showSearchBox){
					 if (this.searchBoxWidth != null && !this.searchBoxWidth.equals("")){
						 iWidth += (Integer.valueOf(this.searchBoxWidth) * 5);
					 } else {
						 iWidth += 50;
					 }
				 }
					 
				divWidth = String.valueOf(iWidth);
			} catch (Exception e){
				divWidth = this.width;
			}
		}
		selectWidth = selectWidth + (selectWidth.indexOf("px")==-1 && selectWidth.indexOf("%")==-1 ? "px" : "");
		divWidth = divWidth + (divWidth.indexOf("px")==-1 && divWidth.indexOf("%")==-1 ? "px" : "");		
		
		selectStyle += " width:"+selectWidth+";";
		disabledInputStyle += " width:"+selectWidth+";";
		
		if (!disabledInputStyle.equals(""))
			disabledInputStyle += "'";			
		
		String dataWidth = " data-width = '"+divWidth+"' ";		
		String wrapDivStyle = " style='display:inline;' ";		
		
		if (multiple){
			sMultiple = " multiple size='"+this.lines+"'";
			if (this.disabled)
				selectStyle += " disabled ";
		}
		
		if (!selectStyle.equals(""))
			selectStyle += "'";		
		
		String firstLabelData = "";
		if (this.firstLabel != null){
			firstLabelData = " data-firstlabel='"+this.firstLabel+"' ";
		}
		
		// *** IMPRIME HTML *** //
		PrintWriter out = pageContext.getResponse().getWriter();
		out.println("<div id='"+this.id+"_tagSelectDiv' " + divClass + dataWidth + divDataReadOnly + wrapDivStyle + ">");
		String sOnloadCallback = "";
		if (this.onLoadCallback != null && !"".equals(this.onLoadCallback)){
			sOnloadCallback = " data-onloadcallback='"+this.id+"_callback' ";
			out.println("<script type=\"text/javascript\">var "+this.id+"_callback = function(){"+this.onLoadCallback+"};</script>");
		}
		if (StringUtils.hasText(this.label)){
			String localizedLabelText = UtilidadesString.getMensajeIdioma(user.getLanguage(), this.label);
			out.println("<label for='"+this.id+"' "+labelClass+" style='display:inline;'>"+localizedLabelText+"</label>");
		}
		
		if (!multiple && showSearchBox){
			if (this.readOnly){
				out.println("<input type='text' id='"+this.id+"_searchBox' name='"+this.id+"_searchBox' "+selectClass+styleSearchBox+sSearchBoxMaxLength+searchBoxSize+" readonly />");
			} else {
				out.println("<input type='text' id='"+this.id+"_searchBox' name='"+this.id+"_searchBox' "+selectClass+styleSearchBox+sSearchBoxMaxLength+searchBoxSize+" />");
			}
		}
		
		String pagination = "";
		if(this.paginated){
			pagination += " data-paginated='true'";
			pagination += " data-page='"+this.page+"'";
			pagination += " data-pagesize='"+this.pageSize+"'";
		}
		
		out.println("<div id='"+this.id+"_loader' "+divLoaderClass+selectLoaderStyle+">");
		out.println("<select id='"+this.id+"' class='tagSelect "+cssClass+"' name='"+this.id+"' "+" data-queryId='"+this.queryId+"' data-iniVal='"+this.selectedIds+"' "+pagination+childrenInfo+sRequired+" "+queryParam+selectReadOnly+dataSelectParentMsg+sSearchKey+sMultiple+sShowSearchBox+sHideIfnoOptions+sOnloadCallback+selectStyle+firstLabelData+">");
		Iterator<KeyValue> iteraOptions = selectOptions.iterator();
		String sInputMultiple = "";
		String sPaginatedDivHTML = "<div id='"+this.id+"_paginated_div' class='tagSelect tagSelectPaginatedDiv' style='visibility:hidden;z-index: 999; position: absolute; background: #fff; border-width: 1px; border-style: solid; overflow-y: auto; overflow-x: hidden;'><ul class='selectOptions'>";
		String oldLetterSelectNav = "";
		while(iteraOptions.hasNext()){
			KeyValue keyValue = iteraOptions.next();
			if (keyValue == null)
				keyValue = new KeyValue();
			String newLetterSelectNav = "";
			if (keyValue.getValue() != null && keyValue.getValue().length() >= 1 && (oldLetterSelectNav.equals("") || !keyValue.getValue().substring(0,1).equals(oldLetterSelectNav))){
				newLetterSelectNav = keyValue.getValue().substring(0,1);
				oldLetterSelectNav = newLetterSelectNav;
			}
			boolean selected = false;
			if (this.selectedIds != null && this.selectedIds.size() > 0)
				selected = this.selectedIds.contains(keyValue.getKey());
			if (selected) {
				selectedOption = keyValue;
				
				// JPT: Codigo para generar los input text de los select multiples
				if (multiple) {
					sInputMultiple += (sInputMultiple.equals("")?"":"\n<br>") + "<input type='text' readonly='readonly' " + inputDisabledClass + disabledInputStyle +
						" id='" + this.id + "_disabled' " + 
						" name='" + this.id + "_disabled' " +
						" value='" + selectedOption.getValue() +"'/>";
				}
			}
			out.println(KeyValue.getHtmlOption(keyValue,selected));
			if (this.paginated){
				sPaginatedDivHTML += KeyValue.getHtmlLi(keyValue, selected, newLetterSelectNav);
			}
		}
		sPaginatedDivHTML += "</ul></div>";
		
		String loadingElement = "<img id='"+this.id+"_loading' src='html/imagenes/loading-spinner.gif' alt='Cargando...' style='visibility:hidden' />";
					
		out.println("</select>"+(paginated?sPaginatedDivHTML:"")+loadingElement+"</div>");
		if (!multiple)
			out.println("<input type='text' readonly='readonly' "+inputDisabledClass+disabledInputStyle+" id='"+this.id+"_disabled' name='"+this.id+"_disabled' value='"+(selectedOption.getKey().equals(KeyValue.DEFAULT_KEY)?"":selectedOption.getValue())+"'/>");
		else
			out.println(sInputMultiple);
		out.println("</div>");// this.id_div;
	}

	public static java.lang.reflect.Method getSelectDataServiceMethodIgnoreCase(String methodName, boolean bShowSearchBox, boolean bPaginated){
		java.lang.reflect.Method SelectDataServiceMethod = null;
		try{
			Assert.hasText(methodName);
			methodName = methodName.trim();
			if (bShowSearchBox){
				methodName += SelectDataService.WITH_SEARCH_KEY_METHOD_SUFIX;
			}
			if (bPaginated){
				methodName += SelectDataService.WITH_PAGINATED_METHOD_SUFIX;
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
		this.selectedIds = Arrays.asList(selectedIds.trim().split("@@"));
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
	public void setParams(Map<String,String> params) {
		try {
			this.params = UtilidadesString.createJsonString(params);
		} catch (IOException e) {
			ClsLogging.writeFileLogError("ERROR AL TRATAR DE OBTENER LOS PAR�METROS DESDE EL JSP", e, 10);
		}
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
	
	public String getFirstLabel() {
		return firstLabel;
	}

	public void setFirstLabel(String firstLabel) {
		this.firstLabel = firstLabel;
	}
	

	/**
	 * @return the paginated
	 */
	public boolean isPaginated() {
		return paginated;
	}

	/**
	 * @param paginated the paginated to set
	 */
	public void setPaginated(boolean paginated) {
		this.paginated = paginated;
	}
	public void setPaginated(String paginated) {
		this.paginated = UtilidadesString.stringToBoolean(paginated);
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
