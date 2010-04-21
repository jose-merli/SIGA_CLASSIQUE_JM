package com.siga.tlds;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.text.StrTokenizer;

public class TagCSS extends TagSupport {
	private static final long serialVersionUID = 4679787908205484553L;
	private static final String link="<link id=\"default\" rel=\"stylesheet\" type=\"text/css\" href=\"%s%s%s\">\n";
	private List<String> ficheros;
	private String relativePath;

	public TagCSS() {
		super();
	}

	public int doStartTag() throws JspException {
		StringBuffer strBuf = new StringBuffer();

		try {
			
			for (String fichero:ficheros){
				strBuf.append(String.format(link, ((HttpServletRequest)pageContext.getRequest()).getContextPath(), relativePath, fichero));
			}
			
			pageContext.getOut().print(strBuf.toString());
		} catch (IOException e){
			throw new JspException("Error al enviar al cliente" + e.getMessage());
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	
	@SuppressWarnings("unchecked")
	public void setFiles(String files){
		StrTokenizer strtk= StrTokenizer.getCSVInstance(files);
		
		ficheros=strtk.getTokenList();
	}

	public String getFiles(){
		StringBuffer strbuf = new StringBuffer();
	    String comma = null;

		Iterator<String> iter = ficheros.iterator();
		while (iter.hasNext()) {
			if (comma != null) {
				strbuf.append(comma);
			} else {
				comma = ", ";
			}
			strbuf.append(iter.next());
		}


		return strbuf.toString();
	}
	
	public void setRelativePath(String relativePath){
		this.relativePath=relativePath;
	}

	public String getRelativePath(){
		return relativePath;
	}
}
