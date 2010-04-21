
package com.siga.tlds;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

public class TagSiNo extends TagSupport {
	private static final long serialVersionUID = 3640857539602873319L;
	private static String YES = "general.yes";
	private static String NO = "general.no";
	private static String ERROR = "Error al enviar al cliente";

	private String value=null;

	public void setValue(String value) {
		this.value = value;
	}

	public TagSiNo() {
		super();
	}

	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();
		UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
		try {
			if (UtilidadesString.stringToBoolean(value)){
				pageContext.getOut().print(UtilidadesString.getMensajeIdioma(usrbean.getLocation(), YES));
			}
			else {
				pageContext.getOut().print(UtilidadesString.getMensajeIdioma(usrbean.getLocation(), NO));
			}
		} catch (IOException e){
			throw new JspException(ERROR + e.getMessage());
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

}


