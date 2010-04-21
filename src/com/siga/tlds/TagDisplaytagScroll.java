package com.siga.tlds;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class TagDisplaytagScroll extends TagSupport {
	private static final long serialVersionUID = 4679787908205484553L;
	private static final String style="<style type=\"text/css\" >.dataScroll{100%% }.dataScroll > tbody{height:%s !important; overflow:auto; overflow-x: hidden;}.dataScroll > tbody tr {height:15px;}</style>";
	private static final String styleIe="<!--[if IE]><style type=\"text/css\">div.windowDataScroll {height: %s; overflow-y: auto; overflow: hidden; }div.windowDataScroll thead tr {position: relative;}.dataScroll{100%%}</style><![endif]-->";
	private String height;

	public TagDisplaytagScroll() {
		super();
	}

	public int doStartTag() throws JspException {
		StringBuffer strBuf = new StringBuffer();

		try {
			strBuf.append(String.format(style, height));
			strBuf.append(String.format(styleIe, height));
			
			pageContext.getOut().print(strBuf.toString());
		} catch (Exception e){
			throw new JspException("Error al enviar al cliente" + e.getMessage());
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	

	public void setHeight(String height){
		this.height=height;
	}

	public String getHeight(){
		return height;
	}
}
