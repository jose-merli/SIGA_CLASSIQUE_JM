//VERSIONES
package com.siga.tlds;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

/**
* Tag que implementa un botón para infomres genéricos (ADM_INFORME, ADM_TIPOINFORME) 
* @author RGG
*/

public class TagInformeSimple extends TagSupport {

    private String recurso; 
    private String idInforme; 
    private String idTipoInforme; 
    private String idInstitucion; 
    private String formularioDatos; 
	

	public void setRecurso(String dato) {
		try {
			this.recurso=dato;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setIdInforme(String dato) {
		try {
			this.idInforme=dato;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setIdTipoInforme(String dato) {
		try {
			this.idTipoInforme=dato;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setIdInstitucion(String dato) {
		try {
			this.idInstitucion=dato;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setFormularioDatos(String dato) {
		try {
			this.formularioDatos=dato;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	

	/**
	 * Acciones a pintar antes del tag 
	 * @author cristina.santos. 07-03-06
	 * @return codigo de respuesta 
	 */
	public int doStartTag() {
		try {
			pageContext.getResponse().setContentType("text/html");
			HttpSession session = pageContext.getSession();
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
			String app=((HttpServletRequest)pageContext.getRequest()).getContextPath();
			PrintWriter out = pageContext.getResponse().getWriter();
			
			out.println("<!-- boton informe generico simple -->");
			out.println("<script>");
			out.println("function creaFormInforme() {");
			out.println("	var formu=document.createElement(\"<form name='InformesGenericosForm'  method='POST'  action='"+app+"/INF_InformesGenericos.do' target='submitArea'>\");");
			out.println("	formu.appendChild(document.createElement(\"<input type='hidden' name='idInstitucion' value='"+this.idInstitucion+"'>\"));");
			out.println("	formu.appendChild(document.createElement(\"<input type='hidden' name='idInforme' value='"+this.idInforme+"'>\"));");
			out.println("	formu.appendChild(document.createElement(\"<input type='hidden' name='idTipoInforme' value='"+this.idTipoInforme+"'>\"));");
			out.println("	formu.appendChild(document.createElement(\"<input type='hidden' name='idInstitucion' value='"+this.idInstitucion+"'>\"));");
			out.println("	formu.appendChild(document.createElement(\"<input type='hidden' name='datosInforme' value=''>\"));");
			out.println("	formu.appendChild(document.createElement(\"<input type='hidden' name='seleccionados' value='0'>\"));");
			out.println("	document.appendChild(formu);");
			out.println("	return formu;");
			out.println("}");
			out.println("");
			out.println("// EJEMPLO: var dat=\"idAbono==25##idinstitucion==2040%%%idAbono==26##idinstitucion==2040%%%idAbono==27##idinstitucion==2040%%%idAbono==28##idinstitucion==2040\";");
			out.println("function generaInformeGenericoSimple() {");
			out.println("	sub();");
			out.println("	var dat = \"\";");
			out.println("	var f = document.getElementById(\""+this.formularioDatos+"\");");
			out.println("	for (var i=0;i<f.length;i++) {");
			out.println("		if (f[i].value) {");
			out.println("			dat += f[i].name + \"==\" + f[i].value + \"##\";");
			out.println("		}");
			out.println("	}");
			out.println("	if (dat.length>2)"); 
			out.println("		dat = dat.substring(0,dat.length-2);");
			out.println("	var formularioInformes = creaFormInforme();");
			out.println("	formularioInformes.datosInforme.value=dat;");
			out.println("	formularioInformes.submit();");
			out.println("} 	");
			out.println("</script>");
			out.println("<input type=\"button\" name=\"idButton\" id =\"idButton\" value=\""+UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),this.recurso)+"\" onclick=\"generaInformeGenericoSimple();\" class=\"button\">");


		}catch (Exception e){
			e.printStackTrace();
		}

		return EVAL_BODY_INCLUDE;	 	 	
	}
	
	
}
