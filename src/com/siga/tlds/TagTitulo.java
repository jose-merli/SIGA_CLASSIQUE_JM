package com.siga.tlds;
import java.io.PrintWriter;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.*;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

/**
 * Mediante este tag podemos actualizar automaticamente el titulo y localizacion
 * en lel frame principal de la aplicaci�n dentro de la navegacion por pantallas.
 * @author raul.ggonzalez. 07-12-04
 */

public class TagTitulo extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9005504571844487826L;
	/** titulo de la pagina */
	String titulo;
	/** localizacion de la pagina */
	String localizacion;
	
/**
 * Da valor al atributo 
 * @author raul.ggonzalez. 10-12-04
 * @param dato 
 */
	public void setTitulo			(String dato) 	{ this.titulo= dato;	}

/**
 * Da valor al atributo 
 * @author raul.ggonzalez. 10-12-04
 * @param dato 
 */
	public void setLocalizacion		(String dato) 	{ this.localizacion= dato;	}
	
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
		
			out.println("<!-- INICIO: TITULO -->"); 
			out.println("<script type=\"text/javascript\">");
			out.println("var siga =\""+ UtilidadesString.getMensajeIdioma(usrbean, "general.ventana.cgae") + "\";");
			out.println("var tit  =\""+ UtilidadesString.getMensajeIdioma(usrbean, this.titulo) +"\";");
			if (this.localizacion!=null && !this.localizacion.equals("")) {
				out.println("var loc =\""+ UtilidadesString.getMensajeIdioma(usrbean, this.localizacion) +" > "+ UtilidadesString.getMensajeIdioma(usrbean, this.titulo) +"\";");
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
