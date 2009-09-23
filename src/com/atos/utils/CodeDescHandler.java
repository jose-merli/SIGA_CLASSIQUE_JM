/**
 * <p>Title: CodeDescHandler </p>
 * <p>Description: class that handles a zip file </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SchLumbergerSema </p>
 * @author 
 * @version 1.0
 */

package com.atos.utils;

public class CodeDescHandler {

	private String code = null;
	private String description = null;

    public CodeDescHandler(String cod, String desc) {
		setCode(cod);
		setDescription(desc);
    }

	public String getCode() {
		return code;
	}

	public void setCode(String cod) {
		code = cod;
	}

	public void setDescription(String desc) {
		description = desc;
	}

	public String getDescription() {
		return description;
	}


}