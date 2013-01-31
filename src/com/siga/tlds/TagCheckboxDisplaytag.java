package com.siga.tlds;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.comun.form.PagedSortedForm;

public class TagCheckboxDisplaytag extends TagSupport {
	private static final long serialVersionUID = 3640857539602873319L;
	private static final String startForm="<form id=\"%s\" name=\"%s\" action=\"%s\" method=\"POST\">\n";
	private static final String hiddenInputs="<input type=\"hidden\" value=\"%s\" name=\"page\"/>\n<input type=\"hidden\" id=\"%s\" name=\"%s\"/>";
	private static final String hiddenSelected="<input type=\"hidden\" value=\"%s\" id=\"%s\" name=\"%s\"/>";
	//BNS: SE HA MODIFICADO EL COMPORTAMIENTO DE LOS SELECCIONADOS PORQUE NO FUNCIONABA CORRECTAMENTE
	private static final String javascript="<script type=\"text/javascript\">\n\t var updatableChecks_query = \"input:checkbox:not(:disabled)[name='_chk']\";"+
			"\n\t var selectAllCheck_query = \"#%s\";\n\t $(document).ready(function(){\n\t\t $(selectAllCheck_query).click(function(){cargarChecksTodos(this);}); \n\t\t "+
			"if ($(\"#%s\").val()==\"%s\"){\n\t\t\t checkAll(true, true);\n\t\t } else {\n\t\t\t var aBackupSelected = getSelectedArray();\n\t\t\t "+
			"$(updatableChecks_query).each(function(){\n\t\t\t\t var checked = false;\n\t\t\t\t if ($.inArray($(this).val(), aBackupSelected) != -1)"+
			"\n\t\t\t\t\t checked = true;\n\t\t\t\t $(this).attr('checked', checked);\n\t\t\t }); \n\t\t } \n\t\t updateSelected(); \n\t\t updateSelectAllCheck(); \n\t });"+
			" \n\t function updateSelectAllCheck(){ \n\t\t var allSelected = true; \n\t\t if ($(updatableChecks_query).not(':checked').length > 0){ \n\t\t\t allSelected = false;"+
			" \n\t\t\t $('#%s').val(\"%s\"); \n\t\t} \n\t\t $(selectAllCheck_query).attr('checked', allSelected); \n\t } \n\t function displaySelected(iSelected){ \n\t\t if ($('#pageBannerSelected').length<=0) \n\t\t\t"+
			" $('span.pagebanner').append(\" <span id='pageBannerSelected'> %s: \"+iSelected+\"</span>\"); \n\t\t else \n\t\t\t "+
			"$('#pageBannerSelected').text(' %s: ' + iSelected);\n\t } \n\t function updateSelected(){ \n\t\t var iSeleccionados = 0; \n\t\t "+
			"var selectedArray = getSelectedArray(); \n\t\t displaySelected(selectedArray.length); \n\t } \n\t function getSelectedArray(){ \n\t\t var selectedArray = new Array();"+
			" \n\t\t var sSeleccionados =  new String(getSelected()); \n\t\t if (sSeleccionados != undefined && sSeleccionados != \"\"){ \n\t\t\t"+
			" if (sSeleccionados.indexOf(\",\") == 0) \n\t\t\t\t sSeleccionados = sSeleccionados.substring(1); \n\t\t\t selectedArray = sSeleccionados.split(','); \n\t\t }"+
			" \n\t\t return selectedArray; \n\t } \n\t function checkAll(checked, updateBackup){\t\n\tif (updateBackup === undefined)\n\t\tupdateBackup = false;\n\tif (updateBackup){"+
			"\t\t\t\t\n\t\t$(updatableChecks_query).each(function(){\n\t\t\t$(this).attr('checked', checked);\n\t\t\tvar aBackupSelected = getSelectedArray();\n\t\t\t"+
			" var newElem = $(this).val();\n\t\t\tif (checked){\n\t\t\t\taBackupSelected.splice(jQuery.inArray( newElem, aBackupSelected ),1, newElem);\n\t\t\t} else {\n\t\t\t\t"+
			"aBackupSelected.splice(jQuery.inArray( newElem, aBackupSelected ),1);\n\t\t\t}\n\t\t\t$(\"#backupSelected\").val(aBackupSelected.toString());\n\t\t});\t\t\n\t} else "+
			"\n\t\t$(updatableChecks_query).attr('checked', checked);\n}\n\nfunction pulsarCheck(obj){\n\tvar selectedArray = getSelectedArray();\n\tif (!obj.checked ){"+
			" \t\t\n\t\tselectedArray.splice(jQuery.inArray( obj.value, selectedArray ),1);\t\t\n\t} else {\n\t\tselectedArray.splice(jQuery.inArray( obj.value, selectedArray ),1, obj.value);"+
			"\n\t}\n\t$(\"#backupSelected\").val(selectedArray.toString());\t\n\tupdateSelectAllCheck();\n\tupdateSelected();\n}\n\nfunction cargarChecksTodos(o){\n\t"+
			"$(\"#%s\").val(\"%s\");\n\tvar conf = confirm('%s'); \t\t\t\n\t"+
			"if (conf){\n\t   \tif (o.checked){\n\t   \t\tvar formData = $(\"form[name='%s']\").serialize();\n\t   \t\t"+
			"$.ajax({\n\t   \t\t\ttype: \"POST\",\n\t            url: \"%s\","+
			"\n\t            data: formData,\n\t            dataType: \"json\",\n\t   \t\t}).done(function(data) {\n\t   \t\t\tvar allSearchedPKs = data.allSearchedPKs;"+
			"\n\t   \t\t\tvar selectedArray = new Array();\n\t   \t\t\tjQuery.each(allSearchedPKs, function(index, element){\n\t   \t\t\t\tselectedArray.push(element.id);\n\t   "+
			"\t\t\t});\n\t   \t\t\t$(\"#backupSelected\").val(selectedArray.toString());\n\t   \t\t\tcheckAll(true);\n\t   \t\t\t"+
			"$(\"#%s\").val(\"on\");\n\t   \t\t\tdisplaySelected(selectedArray.length);\n\t   \t\t})"+
			".fail(function() { \n\t   \t\t  alert(\"%s\");\n\t   \t\t"+
			"});\n\t\t} else {\t\t\t\t\t\n\t\t\t$(\"#backupSelected\").val(\"\");\n\t\t\tcheckAll(false);\n\t\t}\n\t} else {\n   \t\tcheckAll(o.checked, true);\t   \t\t\n   \t}\n"+
			"   \tupdateSelected();\n }</script>\n";
	//BNS: NO HACE FALTA
	//private static final String functionSeleccionar2="<script language=\"JavaScript\"></script>\n";
	private static final String desmarcarCheckCabecera="<script language=\"JavaScript\"></script>\n";
	//BNS: NO HACE FALTA
	//private static final String endJavascript="<script language=\"JavaScript\">\n\t\n\t$(\"#%s\").click(function(){cargarChecksTodos(this);});\n %s </script>\n";	
	private static final String endForm="</form>\n";
	private PagedSortedForm formBean=null;
	private String formName=null;
	private String submitUrl=null;
	private String selectAllAjaxUrl=null;
	private String decoratorName=null;
	private String checkboxName=null;
	private String selectAllName=null;
	private String selectAllPagesName=null;
	private String backupSelectedName=null;
	private String message=null;

	public TagCheckboxDisplaytag() {
		super();
	}

	public int doStartTag() throws JspException {
		StringBuffer strBuf = new StringBuffer();
		HttpSession session = pageContext.getSession();
		UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
		message = UtilidadesString.getMensajeIdioma(usrbean, message); 
		try {
			//TODO: [BNS] OBTENER LOS MENSAJES EN LOS IDIOMAS
			String msgErrorSelectAll = "Se ha producido un error al intentar seleccionar todos los elementos, por favor, inténtelo de nuevo más tarde";
			String msgRegSeleccionados = "Registros Seleccionados";
			
			strBuf.append(String.format(startForm, formName, formName, submitUrl));
			strBuf.append(String.format(hiddenInputs,formBean.getPage(),selectAllPagesName,selectAllPagesName,selectAllPagesName,selectAllPagesName));
			strBuf.append(String.format(hiddenSelected,getListString(formBean.getSelectedElements()),backupSelectedName,backupSelectedName));
			//BNS: CAMBIO EL JAVASCRIPT PARA QUE FUNCIONE CORRECTAMENTE LA SELECCIÓN
			String SELECT_ALL_FALSE = "off";			
			strBuf.append(String.format(javascript, selectAllName, selectAllPagesName, PagedSortedForm.SELECT_ALL_TRUE, selectAllPagesName, SELECT_ALL_FALSE, msgRegSeleccionados, msgRegSeleccionados, selectAllPagesName, SELECT_ALL_FALSE, message, formName, selectAllAjaxUrl, selectAllPagesName, msgErrorSelectAll));
			/*
			strBuf.append(String.format(startJavascript, message, selectAllName, selectAllName));
			strBuf.append(String.format(functionSeleccionar2, selectAllName, formName, formName, formName, formName));
			*/
			strBuf.append(String.format(desmarcarCheckCabecera, selectAllName, selectAllName, backupSelectedName));
			
			pageContext.setAttribute(decoratorName, formBean.getDecorator(checkboxName));
			pageContext.getOut().print(strBuf.toString());
		} catch (IOException e){
			throw new JspException("Error al enviar al cliente" + e.getMessage());
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		StringBuffer strBuf = new StringBuffer();
		
		String aux = "";
		//BEGIN BNS: NO HACE FALTA
		/*
		if (PagedSortedForm.SELECT_ALL_TRUE.equals(formBean.getSelectAll()))
			aux = "document.getElementById(\""+formBean.getSelectAllName()+"\").checked=1;";
		*/
		//END BNS
		
		try {
			//BNS: NO HACE FALTA
			//strBuf.append(String.format(endJavascript, selectAllName, aux));
			strBuf.append(endForm);
		
			pageContext.getOut().print(strBuf.toString());
		} catch (IOException e){
			throw new JspException("Error al enviar al cliente" + e.getMessage());
		}
		return EVAL_PAGE;
	}
	
	public void setFormBean(Object formBean){
		if (formBean instanceof PagedSortedForm)
			this.formBean=(PagedSortedForm)formBean;
		else{
			throw new IllegalArgumentException("El formulario debe extender PagedSortedForm.");
		}
	}
	
	public void setFormName(String formName){
		this.formName=formName;
	}
	
	public void setSubmitUrl(String submitUrl){
		this.submitUrl=submitUrl;
	}
	
	public void setDecoratorName(String decoratorName){
		this.decoratorName=decoratorName;
	}

	public void setCheckboxName(String checkboxName){
		this.checkboxName=checkboxName;
	}

	public void setSelectAllName(String selectAll){
		this.selectAllName=selectAll;
	}
	public void setSelectAllPagesName(String selectAllPages){
		this.selectAllPagesName=selectAllPages;
	}
	
	
	private String getListString(List<String> selectedElements){
		String coma = ",";
		StringBuffer buf = new StringBuffer();
		for(String str: selectedElements){
			buf.append(str);
			buf.append(coma);
		}
		if(buf.length()!=0)
			buf.deleteCharAt(buf.length()-1);
		return buf.toString();
	
	}

	public void setBackupSelectedName(String backupSelectedName) {
		this.backupSelectedName = backupSelectedName;
	}

	public String getBackupSelectedName() {
		return backupSelectedName;
	}
	
	public void setMessage(String message){
		this.message = message;
	}

	public String getSelectAllAjaxUrl() {
		return selectAllAjaxUrl;
	}

	public void setSelectAllAjaxUrl(String selectAllAjaxUrl) {
		this.selectAllAjaxUrl = selectAllAjaxUrl;
	}
	
}

