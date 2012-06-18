package com.siga.tlds;
import java.io.PrintWriter;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.*;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;

/**
 * Este tag presenta una linea de pestanhas con sus respectivas etiquetas y acciones asociadas.
 * <p> Permite la creación de varias lineas paralelas con un identificador cada una,
 * de tal modo que por medio de javascript podemos manejar diferentes niveles de 
 * pestanhas.  
 * @author raul.ggonzalez. 07-12-04
 */

public class TagPestana extends TagSupport {

	/** Identificador de linea de pestanhas */
	int conjunto=0;
	/** Conjunto de nombres de las pestanhas */
	String nombre[];
	/** Conjunto de actions de las pestanhas */
	String action[];
	/** Conjunto de targets de las pestanhas */
	String target[];
	/** Dice si la linea de pestanhas es visible  */
	boolean visible;
	private int contador=0;
	
/**
 * Da valor al atributo 
 * @author raul.ggonzalez. 10-12-04
 * @param dato 
 */
	public void setConjunto		(String dato) 	{ 
		if (dato!=null) {
			this.conjunto	= new Integer(dato).intValue();
		}
	}
/**
 * Da valor al atributo 
 * @author raul.ggonzalez. 10-12-04
 * @param dato 
 */
	public void setVisible		(String dato) 	{ 
		this.visible	= new Boolean(dato).booleanValue();
	}

/**
 * Da valor al atributo 
 * @author raul.ggonzalez. 10-12-04
 * @param dato - Array con el conjunto de nombres 
 */
	public void setNombre		(String dato[]) 	
	{ 
		try {
			if (contador==0) {
				contador = dato.length;
			} else {
				if (contador!=dato.length) {
					throw new Exception("El número de datos configurados no concuerda."); 
				}
			}
			this.nombre = dato;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/**
 * Da valor al atributo 
 * @author raul.ggonzalez. 10-12-04
 * @param dato - Array con el conjunto de actions
 */
	public void setAction		(String dato[]) 	
	{ 
		try {
			if (contador==0) {
				contador = dato.length;
			} else {
				if (contador!=dato.length) {
					throw new Exception("El número de datos configurados no concuerda."); 
				}
			}
			this.action = dato;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
											
/**
 * Da valor al atributo 
 * @author raul.ggonzalez. 10-12-04
 * @param dato - array con el conjunto de targets
 */
	public void setTarget		(String dato[]) 	
	{ 
		try {
			if (contador==0) {
				contador = dato.length;
			} else {
				if (contador!=dato.length) {
					throw new Exception("El número de datos configurados no concuerda."); 
				}
			}
			this.target = dato;
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			String path = request.getContextPath(); 
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
			PrintWriter out = pageContext.getResponse().getWriter();
			String hidden= "";
			if (!this.visible) {
			  hidden=" visibility:hidden ";
			}
			out.println("<!-- INICIO: TABLA DE PESTAÑAS -->"); 
			out.println("<script src=\""+ path +"/html/js/pestanas.js\" type=\"text/javascript\"></script>"); 
			out.println("<div style=\"position:relative; left:0px; width=100%; height=30px; top:0px;  "+ hidden +"\" id=\""+conjunto+"\">");
			out.println("<table  class=\"tablaLineaPestanasArriba\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
			out.println("<tr>");
			out.println("<td></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<table class=\"pest\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
			out.println("<tr>");

			for (int i=0;i<contador;i++) 
			{
				out.println("<!-- INICIO: PESTAÑA -->"); 
				out.print("<td class=\"pestanaTD\"><a id=\"");
				out.print(this.nombre[i]);
				out.print("\" name=\"pestana\" href=\"#\" action=\"");
				out.print(this.action[i]);
				out.print("\" onClick=\"sub();return pulsar(this,'" + this.target[i] + "')\">");
				out.print(this.nombre[i]);
				out.print("</a></td>");
				out.println("<!-- FIN: PESTAÑA -->"); 
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

			out.println("</tr>");
			out.println("</table>");
			out.println("<table  class=\"tablaLineaPestanas\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
			out.println("<tr>");
			out.println("<td></td>");
			out.println("</tr>");
			out.println("</table>");

			out.println("</div>");
			out.println("<!-- FIN: TABLA DE PESTAÑAS -->"); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return EVAL_PAGE; 			// continua la ejecucion de la pagina
	}

}
