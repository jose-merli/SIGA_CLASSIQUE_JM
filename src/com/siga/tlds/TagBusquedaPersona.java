
package com.siga.tlds;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

public class TagBusquedaPersona extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8017160795406872669L;
	String tipo = "";
	String tipoClientes = "";
	String titulo = "";
	String accion = "";
	String idPersona = "";
	String numeroColegiado="";
	String anchoDesc = "";
	String anchoNum = "";
	String campoObligatorio = "";
	private String preFunction;
	private String postFunction;

	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	public String getTipoClientes() {
		return tipoClientes;
	}
	public void setTipoClientes(String tipoClientes) {
		this.tipoClientes = tipoClientes;
	}
	
	
	public String getAnchoDesc() {
		return anchoDesc;
	}
	public void setAnchoDesc(String anchoDesc) {
		this.anchoDesc = anchoDesc;
	}
	
	
	public String getAnchoNum() {
		return anchoNum;
	}
	public void setAnchoNum(String anchoNum) {
		this.anchoNum = anchoNum;
	}
	
	
	public String getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}
	
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	
	public String getPreFunction() {
		return preFunction==null?"":preFunction+"();";
	}
	public void setPreFunction(String preFunction) {
		this.preFunction = preFunction;
	}

	
	public String getPostFunction() {
		return postFunction==null?"":postFunction+"();";
	}
	public void setPostFunction(String postFunction) {
		this.postFunction = postFunction;
	}
	
	public String getCampoObligatorio() {
		return campoObligatorio;
	}	
	public void setCampoObligatorio(String campoObligatorio) {
		this.campoObligatorio = campoObligatorio;
	}	

	public int doStartTag() {
		try {
			HttpSession session = pageContext.getSession();
			
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
			if (this.anchoDesc ==null ||this.anchoDesc.equals("")){
				this.anchoDesc="45";
			}
			
			if (this.anchoNum ==null ||this.anchoNum.equals("")){
				this.anchoNum="6";
			}
			if (usrbean==null) usrbean = UsrBean.UsrBeanAutomatico("2000");

			PrintWriter out = pageContext.getResponse().getWriter();

			out.println("<!-- Inicio tag busqueda personas -->");

			if (this.titulo!=null && !this.titulo.equals("")) { 
				out.println("<fieldset>");
				out.println("<legend>"+UtilidadesString.getMensajeIdioma(usrbean,this.titulo));
				out.println("</legend>");
			}
			out.println("<table align=\"left\">");
			
			
			if (tipo.equals("colegiado")) {
				out.println("			<tr>");	
				out.println("				<td class=\"labelText\">");
				out.println("					"+UtilidadesString.getMensajeIdioma(usrbean,"gratuita.busquedaSOJ.literal.colegiado"));
				if(campoObligatorio!=null && campoObligatorio.equals("true"))
					out.println("(*)");
				out.println("				</td>");
				
			} else if (tipo.equals("personas")) {
				out.println("			<tr>");
				out.println("				<td class=\"labelText\">");
				out.println("					"+UtilidadesString.getMensajeIdioma(usrbean,"gratuita.busquedaSOJ.literal.nif"));
				if(campoObligatorio!=null && campoObligatorio.equals("true"))
					out.println("(*)");
				out.println("				</td>");
				
			} else if (tipo.equalsIgnoreCase("personasJG")) {
				out.println("			<tr>");
				out.println("				<td class=\"labelText\">");
				out.println("					"+UtilidadesString.getMensajeIdioma(usrbean,"gratuita.busquedaSOJ.literal.nif"));
				if (campoObligatorio!=null && campoObligatorio.equals("true"))
					out.println("(*)");
				out.println("				</td>");
			}
			
			out.println("<td>");
			out.println("	<input type=\"text\" id=\"numeroNifTagBusquedaPersonas\" name=\"numeroNifTagBusquedaPersonas\" size="+this.anchoNum+" maxlength=\"9\" class=\"box\" onBlur=\""+ getPreFunction()+"obtenerPersonas();"+ getPostFunction()+"\"/>");				
			out.println("</td>");
			
			out.println("<td>");
			out.println("	<input type=\"text\" id=\"nombrePersona\" name=\"nombrePersona\" size="+this.anchoDesc+" maxlength=\"50\" class=\"box\" readonly=\"true\"/>");							
			out.println("</td>");
			
			out.println("<td>");								
			if(tipo.equals("personas")){
				out.println("	<!-- Boton buscar -->");
				out.println("	<input type=\"button\" class=\"button\" id=\"idButton\" name=\"buscarCliente\" value='"+UtilidadesString.getMensajeIdioma(usrbean,"gratuita.inicio_SaltosYCompensaciones.literal.buscar")+"' onClick=\""+ getPreFunction()+"buscarPersonaDni();"+ getPostFunction()+"\">");
			} else {
				out.println("	<!-- Boton buscar -->");
				out.println("	<input type=\"button\" class=\"button\" id=\"idButton\" name=\"buscarCliente\" value='"+UtilidadesString.getMensajeIdioma(usrbean,"gratuita.inicio_SaltosYCompensaciones.literal.buscar")+"' onClick=\""+ getPreFunction()+"buscarPersona();"+ getPostFunction()+"\">");
			}				
			out.println("	<!-- Boton limpiar -->");
			out.println("	&nbsp;<input type=\"button\" class=\"button\" id=\"idButton\" name=\"limpiar\" value='"+UtilidadesString.getMensajeIdioma(usrbean,"gratuita.inicio_SaltosYCompensaciones.literal.limpiar")+"' onClick=\"limpiarPersona();\">");			
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");			
			if (this.titulo!=null && !this.titulo.equals("")) { 
				out.println("</fieldset>");
			}
			
			//request
			out.println("<script language=\"JavaScript\">	");
			out.println("");
			out.println("		function creaForm() {");
			out.println("			var vForm=document.forms['busquedaClientesForm'];");
			out.println("			if(vForm==null){");
			out.println("				var app=busquedaClientesModalForm.action;");
			out.println("				app=app.substring(0,app.substr(1).indexOf('/')+1);");
//			////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			//out.println("				var formu=document.createElement(\"<form name='busquedaClientesModalForm' action='\"+app+\"/CEN_BusquedaClientesModal.do'>\");");
			out.println("var formu = document.createElement('form'); ");
			out.println("formu.setAttribute('name', 'busquedaClientesModalForm');");
			out.println("formu.setAttribute('id', 'busquedaClientesModalForm');");
//			out.println("formu.setAttribute('method', 'POST');");
			//String contextPath = pageContext.getRequest().getc
			String contextPath = ((HttpServletRequest)pageContext.getRequest()).getContextPath();
			
			out.println("formu.setAttribute('action', '"+contextPath+"/CEN_BusquedaClientesModal.do');");
			
			// out.println("				formu.appendChild(document.createElement(\"<input type='hidden' name='actionModal' value=''>\"));");
			out.println("                var myinput =document.createElement('input');");
            out.println("  myinput.setAttribute('type','hidden');");
            out.println("  myinput.setAttribute('id','actionModal');");
            out.println("  myinput.setAttribute('name','actionModal');");
            out.println("  myinput.setAttribute('value','');");
            out.println("  formu.appendChild(myinput);");
            out.println(""); 
			
			//out.println("				formu.appendChild(document.createElement(\"<input type='hidden' name='modo' value=''>\"));");
            out.println("                var myinput2 =document.createElement('input');");
            out.println("  myinput2.setAttribute('type','hidden');");
            out.println("  myinput2.setAttribute('id','modo');");
            out.println("  myinput2.setAttribute('name','modo');");
            out.println("  myinput2.setAttribute('value','');");
            out.println("  formu.appendChild(myinput2);");
            out.println(""); 
            
			//out.println("				formu.appendChild(document.createElement(\"<input type='hidden' name='tipoBus' value='"+this.tipo+"'>\"));");
            out.println("                var myinput3 =document.createElement('input');");
            out.println("  myinput3.setAttribute('type','hidden');");
            out.println("  myinput3.setAttribute('id','tipoBus');");
            out.println("  myinput3.setAttribute('name','tipoBus');");
            out.println("  myinput3.setAttribute('value','"+this.tipo+"');");
            out.println("  formu.appendChild(myinput3);");
            
			//out.println("				formu.appendChild(document.createElement(\"<input type='hidden' name='numeroNif' value=''>\"));");
            out.println("");
            out.println("  var myinput4 =document.createElement('input');");
            out.println("  myinput4.setAttribute('type','hidden');");
            out.println("  myinput4.setAttribute('id','numeroNif');");
            out.println("  myinput4.setAttribute('name','numeroNif');");
            out.println("  myinput4.setAttribute('value','');"); 
            out.println("  formu.appendChild(myinput4);"); 
            
            if (this.tipoClientes!=null && !this.tipoClientes.equals("")) {
            	out.println("  var myinput5 =document.createElement('input');");
                out.println("  myinput5.setAttribute('type','hidden');");
                out.println("  myinput5.setAttribute('id','tipoCliente');");
                out.println("  myinput5.setAttribute('name','tipoCliente');");
                out.println("  myinput5.setAttribute('value','" + this.tipoClientes + "');"); 
                out.println("  formu.appendChild(myinput5);");
            }
			
//			////////////////////////////////////////////////////////////////////////////////////////////////////////
            out.println("  document.body.appendChild(formu);");
            out.println("				vForm=formu;");		
			
			out.println("			}");
			out.println("			return vForm;");
			out.println("		}");		
			out.println("");			
			out.println("		var vForm; ");		
			out.println("");			
			out.println("		function buscarPersona () ");
			out.println("		{");
//			out.println("			   alert(\"<-buscarPersona->\"+vForm);");	
			out.println("			   		if (!vForm) vForm=creaForm();");
			out.println("			   vForm.modo.value=\"abrirBusquedaModal\";");	
			
			out.println("					var resultado = ventaModalGeneral(vForm.name,\"G\");	");		
			out.println("					if (resultado != null && resultado[2]!=null)");
			out.println("					{");
			out.println("									o = document.getElementById('" + this.idPersona + "');");
			out.println("									if (!o) alert (\"Es obligatorio que exista un campo en el formulario para almacenar la informacion devuelta por el tag. Ej. idPersona (hidden)\");");
			out.println("						document.getElementById('" + this.idPersona + "').value = resultado[0];");
			out.println("						document.getElementById('numeroNifTagBusquedaPersonas').value = resultado[2];");
			out.println("					}");
			out.println("					if (resultado != null && resultado[4]!=null && resultado[5]!=null && resultado[6]!=null)");
			out.println("					{");
			out.println("						document.getElementById('nombrePersona').value = resultado[4] + \" \" + resultado[5] + \" \" + resultado[6];");
			out.println("					}");
			//funcion de mostrar persona con dni.
		
			if (accion != null && !accion.equals("")) {
				out.println("					if(resultado != null && document.getElementById('" + this.idPersona + "').value) { ");
				out.println("                     " + this.accion + ";");
				out.println("					}");
			}

			
			out.println("		}		");
			out.println("");
			
			
				//funcion de mostrar persona con dni.
			out.println("		function buscarPersonaDni () ");
			out.println("		{");
			out.println("			   		if (!vForm) vForm=creaForm();");
			out.println("			      vForm.modo.value=\"abrirBusquedaModal\";");
			out.println("					var resultado = ventaModalGeneral(vForm.name,\"G\");	");		
			out.println("					if (resultado != null && resultado[3]!=null)");
			out.println("					{");
			out.println("									o = document.getElementById('" + this.idPersona + "');");
			out.println("									if (!o) alert (\"Es obligatorio que exista un campo en el formulario para almacenar la informacion devuelta por el tag. Ej. idPersona (hidden)\");");
			out.println("						document.getElementById('" + this.idPersona + "').value = resultado[0];");
			out.println("						document.getElementById('numeroNifTagBusquedaPersonas').value = resultado[3];");
			out.println("					}");
			out.println("					if (resultado != null && resultado[4]!=null && resultado[5]!=null && resultado[6]!=null)");
			out.println("					{");
			out.println("						document.getElementById('nombrePersona').value = resultado[4] + \" \" + resultado[5] + \" \" + resultado[6];");
			out.println("					}");
		
			if (accion != null && !accion.equals("")) {
				out.println("					if(resultado != null && document.getElementById('" + this.idPersona + "').value) { ");
				out.println("                     " + this.accion + ";");
				out.println("					}");
			}

			out.println("		}		");
			out.println("");
			
			
			out.println("		function limpiarPersona () ");
			out.println("		{");
			out.println("				document.getElementById('" + this.idPersona + "').value = \"\";");			
			out.println("				document.getElementById('numeroNifTagBusquedaPersonas').value = \"\";");
			out.println("				document.getElementById('nombrePersona').value = \"\";");
			out.println("		 "+ getPostFunction()+"");			
			out.println("		}	");
			out.println("		function obtenerPersonas () ");
			out.println("		{sub();");
			out.println("			document.getElementById('nombrePersona').value = \"\";");
			out.println("			if(document.getElementById('numeroNifTagBusquedaPersonas').value!=\"\"){");			
			out.println("			   if (!vForm) vForm=creaForm();");
			out.println("			   vForm.target=\"submitArea\";");
			out.println("			   vForm.numeroNif.value=document.getElementById('numeroNifTagBusquedaPersonas').value;");
			out.println("			   vForm.modo.value=\"tagBusquedaPersona\";");	
			out.println("			   	vForm.submit();");	
			out.println("			}");
			out.println("			else{fin();");
			out.println("				limpiarPersona();");
			out.println("			}");
			out.println("		}");
			out.println("");
			out.println("		function traspasoDatosPersonaTag(resultado){");
//			out.println("			   alert(\"<-traspasoDatos->\"+vForm);");
			out.println("			if (resultado != null && resultado[0]!=\"\")");
			out.println("			{");
			out.println("					document.getElementById('" + this.idPersona + "').value = resultado[0];");
			out.println("					document.getElementById('nombrePersona').value = resultado[1];	");

			if (accion != null && !accion.equals("")) {
				out.println("                     " + this.accion + ";");
			}
			out.println("		 "+ getPostFunction()+"");			
			out.println("			}");
			out.println("			else{");
			out.println("				limpiarPersona();");
			out.println("			} ");
			out.println("		}");
			out.println("</script>");
			out.println("<!-- Inicio tag busqueda personas -->");

		}
		catch (Exception e)	{
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;	 	 	
	}
	
	public int doEndTag() {
		try { }
		catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
}