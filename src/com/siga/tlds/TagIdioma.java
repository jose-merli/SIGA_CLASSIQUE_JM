
package com.siga.tlds;

import java.io.PrintWriter;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

public class TagIdioma extends TagSupport 
{
	String key = "";
	String arg0 = "";
	String arg1 = "";

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getArg0() {
		return arg0;
	}
	public void setArg0(String key) {
		this.arg0 = key;
	}
	
	public String getArg1() {
		return arg1;
	}
	public void setArg1(String key) {
		this.arg1 = key;
	}
	
	public int doStartTag() 
	{
		try {
			HttpSession session = pageContext.getSession();
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
			if (usrbean==null) usrbean = UsrBean.UsrBeanAutomatico("2000");
			String s = ""; 
			int size = 0;
			if (!arg0.trim().equals("")) {
			    size++;
			}
			if (!arg1.trim().equals("")) {
			    size++;
			}
			String[] args = new String[size];
			if (!arg0.trim().equals("")) {
			    args[0]=arg0;
			}
			if (!arg1.trim().equals("")) {
			    args[1]=arg1;
			}
			if (size>0) {
			    s = UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),this.key, args);
			} else {
			    s = UtilidadesString.getMensajeIdioma(usrbean, this.key);
			}
			PrintWriter out = pageContext.getResponse().getWriter();
			out.print(s);
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;	 	 	
	}
	
	public int doEndTag() 
	{
		try { }
		catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
}
