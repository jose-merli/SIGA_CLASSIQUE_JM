package com.siga.tlds;
import java.io.PrintWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author AtosOrigin
 */

public class TagToolTip extends TagSupport 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7826411496935001431L;
	String id = "", texto = "", imagen = "";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}

	public int doStartTag() 
	{
		try {
			PrintWriter out = pageContext.getResponse().getWriter();
	
			out.println("<img src=\"" + this.imagen + "\" style=\"cursor:hand;\"  title =\"" + this.texto + "\"/>");

			

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return EVAL_BODY_INCLUDE;	 	 	
	}
	
	public int doEndTag() 
	{
		try {
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
}
