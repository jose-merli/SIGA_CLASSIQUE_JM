package com.siga.tlds;
import java.io.PrintWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author AtosOrigin
 */

public class TagToolTip extends TagSupport 
{
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

//			Version 1.0			
			out.println("<img src=\"" + this.imagen + "\" style=\"cursor:hand;\" " + 
							 " onmouseenter =\"pintaToolTipDac('on', 'tooltipdac_" + this.id + "')\" " +
					         " onmouseleave =\"pintaToolTipDac('off', 'tooltipdac_" + this.id + "')\" " + 
							 "/>" +
						"<div id='tooltipdac_" + this.id + "' class='tooltipo' style='display:none; position:absolute; z-index=5;' " +
//					          " onmouseleave =\"pintaToolTipDac('off', 'tooltipdac_" + this.id + "')\" " + 
//					          " onclick = \"pintaToolTipDac('off', 'tooltipdac_" + this.id + "')\" " + 
						" > " +
					       "<span class='tooltipoletra'>" + this.texto + "</span>" +
						"</div>");
			
			
//			Version 2.0 			
//			HttpSession ses = pageContext.getSession();
//			Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
//			String colorFondo = (String)src.get("color.tableTitle.BG");
//			String colorTexto = (String)src.get("color.tableTitle.font");
//			String tipoLetra = (String)src.get("font.style");
//			
//			out.println("<img src=\"" + this.imagen + "\" style=\"cursor:hand;\" " + 
//					 " onmouseenter =\"pintaToolTipDacDegradado('on', '" + this.texto +"', '"+ colorFondo + "', '"+colorTexto+"', '" + tipoLetra + "')\" " +
//					 " onmouseleave =\"pintaToolTipDacDegradado('off','" + this.texto +"', '"+ colorFondo + "', '"+colorTexto+"', '" + tipoLetra + "')\" " +
//					 "/>" );
			

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
