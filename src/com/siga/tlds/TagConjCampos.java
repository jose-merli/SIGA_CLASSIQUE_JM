package com.siga.tlds;
import java.io.PrintWriter;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.*;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

/**
 * Tag que implementa un conjunto de campos (etiqueta fieldset + legend) 
 * @author raul.ggonzalez 07-12-04
 */

public class TagConjCampos extends TagSupport {

	/** Leyenda a mostrar en la caja */
	String leyenda;
	
/**
 * Da valor al atributo 
 * @author raul.ggonzalez. 10-12-04
 * @param dato 
 */
	public void setLeyenda(String dato) {
		this.leyenda=dato;
	}
	
/**
 * Acciones a pintar antes del tag 
 * @author raul.ggonzalez. 10-12-04
 * @return codigo de respuesta 
 */
	public int doStartTag() 
	{
		try {
			pageContext.getResponse().setContentType("text/html");
			HttpSession session = pageContext.getSession();
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
			PrintWriter out = pageContext.getResponse().getWriter();

			
			out.println("	<!-- SUBCONJUNTO DE DATOS -->");
			out.println("<fieldset>");
			if (this.leyenda!=null) {
				out.println("<legend>");
				out.println(UtilidadesString.getMensajeIdioma(usrbean, this.leyenda));
				out.println("</legend>");
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return EVAL_BODY_INCLUDE;	 	 	
	}
	
/**
 * Acciones a pintar despues del tag 
 * @author raul.ggonzalez. 10-12-04
 * @return codigo de respuesta 
 */
	public int doEndTag() 
	{
		try {
			String aux = "";
			pageContext.getResponse().setContentType("text/html");
			PrintWriter out = pageContext.getResponse().getWriter();

			out.println("</fieldset>");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return EVAL_PAGE; 			// continua la ejecucion de la pagina
	}
}
