
package com.siga.tlds;

import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.RequestUtils;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

public class TagError extends TagSupport 
{
	
	public int doStartTag() 
	{
		
		ActionMessages errors = null;
		try {
			errors = RequestUtils.getActionMessages(pageContext, Globals.ERROR_KEY);
		
			if ((errors == null) || errors.isEmpty()) {
				return (EVAL_BODY_INCLUDE);
			}						

			String cadena = "";			
			HttpSession session = pageContext.getSession();
			PrintWriter out = pageContext.getResponse().getWriter();
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
			if (usrbean==null) 
				usrbean = UsrBean.UsrBeanAutomatico("2000");
			
			Iterator iProperties = errors.properties();
			while (iProperties.hasNext()) {
				String sProperty = (String)iProperties.next();
				String[] aProperty = new String[1];
				aProperty[0] = sProperty;
				
				Iterator  iValores = errors.get(sProperty);
				 
				while (iValores.hasNext()) {
					ActionMessage am = (ActionMessage) iValores.next();
					String key = am.getKey();
					String s = UtilidadesString.getMensajeIdioma(usrbean, key);
					if (s.indexOf("{0}")>0)
						s = UtilidadesString.getMensajeIdioma(usrbean, key, aProperty);
					if (cadena != "")
						cadena = cadena + "\\n";
					cadena = cadena + s;
				}				
			}
						
			out.print("<script>alert('"+cadena+"');</script>");
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
