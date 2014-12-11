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
 * @author jose.barrientos 29/10/2013 A�adimos clases al conjunto de campos para poder ocultar/mostrar mediante jQuery
 */

public class TagConjCampos extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7268268236030943992L;
	/** Leyenda a mostrar en la caja */
	String leyenda;
	boolean desplegable;
	boolean oculto;
	private String preFunction;
	private String postFunction;
	private String clase="";
	
	/**
	 * Da valor al atributo 
	 * @author raul.ggonzalez. 10-12-04
	 * @param dato 
	 */
	public void setLeyenda(String dato) {
		this.leyenda=dato;
	}
	
	/**
	 * Da valor al atributo 
	 * @author jose.barrientos 9-12-09
	 * @param dato 
	 */
	public void setDesplegable(String dato) {
		if ((dato!=null)&&(dato.equalsIgnoreCase("true")))
			this.desplegable=true;
		else
			this.desplegable=false;
	}

	/**
	 * Da valor al atributo 
	 * @author jose.barrientos 9-12-09
	 * @param dato 
	 */
	public void setOculto(String dato) {
		if ((dato!=null)&&(dato.equalsIgnoreCase("true")))
			this.oculto=true;
		else
			this.oculto=false;
	}
	
	
	
	public String getPreFunction() {
		return preFunction==null?"":preFunction;
	}

	public void setPreFunction(String preFunction) {
		this.preFunction = preFunction;
	}

	public String getPostFunction() {
		return postFunction==null?"":postFunction;
	}

	public void setPostFunction(String postFunction) {
		this.postFunction = postFunction;
	}
	
	/**
	 * Da valor al atributo 
	 * @author jose.barrientos 9-12-09
	 * @param dato 
	 */
	public void setClase(String clase) {
		this.clase = clase;
	}

	/**
	 * Devuelve el valor de la clase o clases del conjunto de campos
	 * @return
	 */
	public String getClase() {
		return clase;
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
			String app= ((HttpServletRequest)pageContext.getRequest()).getContextPath();
			
			out.println("	<!-- SUBCONJUNTO DE DATOS -->");
			if (oculto){
				out.println("<fieldset class='legendNoBorder,"+getClase()+"'>");
			}else{
				out.println("<fieldset class='"+getClase()+"'>");
			}
			if (this.leyenda!=null) {
				
				String identificador = UtilidadesString.replaceAllIgnoreCase(this.leyenda, ".", "");
					
				if (desplegable){	
					out.println("<legend style=\"cursor:hand;\"> ");
					out.println("<a onclick=\""+ getPreFunction()+" ocultarDIV('"+ identificador +"');"+ getPostFunction() +"\">");
					if (oculto){
						out.println("<img id=\"" +identificador + "ImMas\" src=\""+app+"/html/imagenes/simboloMas.gif\"style=\"display:inline\">");
						out.println("<img id=\"" +identificador + "ImMenos\" src=\""+app+"/html/imagenes/simboloMenos.gif\"style=\"display:none\">");
					}else{
						out.println("<img id=\"" +identificador + "ImMas\" src=\""+app+"/html/imagenes/simboloMas.gif\"style=\"display:none\">");
						out.println("<img id=\"" +identificador + "ImMenos\" src=\""+app+"/html/imagenes/simboloMenos.gif\"style=\"display:inline\">" );
					}
					out.println(UtilidadesString.getMensajeIdioma(usrbean, this.leyenda)+ "</a>");
				}else{
					out.println("<legend>");
					out.println(" " + UtilidadesString.getMensajeIdioma(usrbean, this.leyenda));
				}
				
				out.println("</legend>");
				
				if(oculto){
					out.println("<div style=\"display: none\" id=\""+ identificador + "\">");
				}else{
					out.println("<div style=\"display: inline\" id=\""+ identificador + "\">");
				}
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
			pageContext.getResponse().setContentType("text/html");
			PrintWriter out = pageContext.getResponse().getWriter();
			if (this.leyenda!=null) {
				out.println("</div>");
			}
			out.println("</fieldset>");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return EVAL_PAGE; 			// continua la ejecucion de la pagina
	}
}
