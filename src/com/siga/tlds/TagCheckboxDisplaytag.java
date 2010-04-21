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
	private static final String startJavascript="<script language=\"JavaScript\">\n\tfunction seleccionar(evento){\n\t\t 	var conf = confirm('%s');\n\t\t	if (conf){document.getElementById(\"%s\").value = \"on\";}else{document.getElementById(\"%s\").value = \"off\";}\n\t\tseleccionar2();}\n</script>\n";
	private static final String functionSeleccionar2="<script language=\"JavaScript\">\n\tfunction seleccionar2(){\n\t\tvar elem = document.getElementById(\"%s\");\n\t\tvar valor=0;\n\t\tif (elem.checked==true)\n\t\t\tvalor=1;\n\t\tfor (i=0;i<document.%s.elements.length;i++)\n\t\t\tif(document.%s.elements[i].type == \"checkbox\" && document.%s.elements[i].disabled != true)\n\t\t\t\tdocument.%s.elements[i].checked=valor;\n\t}</script>\n";
	private static final String desmarcarCheckCabecera="<script language=\"JavaScript\">\n\tfunction desmarcarCheckCabecera(checked,value){\r\n\t var todos  = document.getElementById(\"%s\");\r\n\t var backup  = document.getElementById(\"%s\");\r\n\t if(!checked){\r\n\t\tbackup.value = backup.value.replace(value, \"\");\r\n\t}\r\n\tif(todos.checked){\r\n\t\ttodos.checked=todos.checked && checked;\n\t\tif(todos.checked){\n\t\t\ttodos.value=\"on\";\r\n\t\t}\r\n\t\telse{\r\n\t\t\ttodos.value=\"off\";\r\n\t\t\tbackup.value = \"\";\r\n\t\t}\r\n\t}\relse{todos.value=\"off\";}}</script>\n";
	private static final String endJavascript="<script language=\"JavaScript\">\n\t\n\tvar elem=document.getElementById(\"%s\");\n\tif (elem != null)\n\t\telem.attachEvent(\"onclick\", seleccionar);\n %s </script>\n";	
	private static final String endForm="</form>\n";
	private PagedSortedForm formBean=null;
	private String formName=null;
	private String submitUrl=null;
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
			strBuf.append(String.format(startForm, formName, formName, submitUrl));
			strBuf.append(String.format(hiddenInputs,formBean.getPage(),selectAllPagesName,selectAllPagesName,selectAllPagesName,selectAllPagesName));
			strBuf.append(String.format(hiddenSelected,getListString(formBean.getSelectedElements()),backupSelectedName,backupSelectedName));
			strBuf.append(String.format(startJavascript, message, selectAllName, selectAllName));
			strBuf.append(String.format(functionSeleccionar2, selectAllName, formName, formName, formName, formName));
			strBuf.append(String.format(desmarcarCheckCabecera, selectAllName, backupSelectedName));
			
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
		if (PagedSortedForm.SELECT_ALL_TRUE.equals(formBean.getSelectAll()))
			aux = "document.getElementById(\""+formBean.getSelectAllName()+"\").checked=1;seleccionar2();";
		
		try {
			strBuf.append(String.format(endJavascript, selectAllName, aux));
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
	
}

