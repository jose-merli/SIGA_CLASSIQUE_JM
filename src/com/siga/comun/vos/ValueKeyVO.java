package com.siga.comun.vos;

import com.siga.Utilidades.AjaxXMLBuilderAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderNameAnnotation;
import com.siga.Utilidades.AjaxXMLBuilderValueAnnotation;

@AjaxXMLBuilderAnnotation
public class ValueKeyVO {

	private String value;
	private String key;
	
	public ValueKeyVO() {
		super();
	}
	
	public ValueKeyVO(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@AjaxXMLBuilderNameAnnotation()
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@AjaxXMLBuilderValueAnnotation(isCData=true)
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
}
