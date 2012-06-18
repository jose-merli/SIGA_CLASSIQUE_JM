package com.siga.tlds;
import java.io.PrintWriter;
import javax.servlet.http.*;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

/**
 * Mediante este tag podemos actualizar automaticamente el titulo y localizacion
 * en lel frame principal de la aplicación dentro de la navegacion por pantallas.
 * La diferencia con TagTitulo radica en que las localizaciones de este tag 
 * contienen el título, por lo que no hay que concatenarlo
 * @author raul.ggonzalez. 12-12-05
 */

public class TagTituloExt extends TagTitulo {

	
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
		
			out.println("<!-- INICIO: TITULO EXT -->"); 
			out.println("<script>");
			out.println("var siga =\""+ UtilidadesString.getMensajeIdioma(usrbean, "general.ventana.cgae") + "\";");
			out.println("var tit =\""+ UtilidadesString.getMensajeIdioma(usrbean, this.titulo) +"\";");
			if (this.localizacion!=null && !this.localizacion.equals("")) {
				out.println("var loc =\""+ UtilidadesString.getMensajeIdioma(usrbean, this.localizacion) +"\";");
				out.println("setLocalizacion(loc);");
			}
			out.println("setTitulo(siga, tit);");
			out.println("</script>");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return EVAL_BODY_INCLUDE;	 	 	
	}
}

