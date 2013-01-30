package com.siga.comun.decorator;

import java.util.List;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

public class CheckboxDecorator implements DisplaytagColumnDecorator {
	private static final String checkbox="<input type=\"checkbox\" name=\"%s\" id=\"%s\" value=\"%s\" %s onclick=\"pulsarCheck(this)\"/>";
	private static final String checked="checked";
	private static final String vacio="";
	private static final String deshabilitado="disabled";
	private String checkboxName=null;
	private List<String> selected=null;
	private List<String> disabled=null;
	private boolean allSelected=false;


	public CheckboxDecorator(String checkboxName, List<String> selected, boolean allSelected){
		this.checkboxName=checkboxName;
		this.selected=selected;
	}
	
	public Object decorate(Object value, PageContext ctx, MediaTypeEnum media) throws DecoratorException {
		StringBuffer strbuff = new StringBuffer();

		if (media.equals(MediaTypeEnum.HTML)){
			String seleccionado=null;
			if (disabled.contains(value))
				seleccionado = deshabilitado;
			else if (selected != null)
				seleccionado=selected.contains(value) || allSelected ? checked : vacio;
			strbuff.append(String.format(checkbox, checkboxName, checkboxName, (value==null)?"":value.toString(), seleccionado));
		}

		return strbuff.toString();
	}

	public void setDisabled(List<String> disabled) {
		this.disabled = disabled;
	}

	public List<String> getDisabled() {
		return disabled;
	}
}