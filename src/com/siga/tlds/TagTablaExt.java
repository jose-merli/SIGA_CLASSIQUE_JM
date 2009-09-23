package com.siga.tlds;

/*
 * VESIONES:
 * Luis Miguel Sánchez PIÑA 21/02/2005 Primera versión.
 * raul.ggonzalez 14-03-2005 Se anhade javascript para que una vez se haya llamado a borrar con target = submitArea, 
 * 							 se quede el target original en el formulario
 */

import java.io.*;
import com.atos.utils.*;
import javax.servlet.http.*;
import com.siga.Utilidades.*;

public class TagTablaExt extends TagTabla
{
	boolean bVariasTablasEnLaMismaPagina=false;

	public void setVariasTablasEnLaMismaPagina(String dato)
	{
		this.bVariasTablasEnLaMismaPagina = UtilidadesString.stringToBoolean(dato);
	}

	public int doStartTag() 
	{
	    String aux = "";
		
	    try {
	        
	    
			HttpSession session = pageContext.getSession();
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);			
			PrintWriter out = pageContext.getResponse().getWriter();
			
			if (this.clase==null) this.clase="tableTitle";
			
			out.println("<!-- INICIO: TABLA DE DETALLES 1 -->"); 

/*			out.println("<!-- 0. Creamos el formulario asociado a la tabla -->");
			out.println("<form id=\"" + this.nombre + "Form\" action = \"" + this.action + "\">");
			for (int i = 0; i <  this.numColumnas; i++) 
			{
				out.println("<input type=\"hidden\" id=\"campo" + i + " >");
			}
			out.println("</form>");
			
*/			
			
			// RGG 07-12-04 se introduce el campo para hacer modal la ventana que se abra. 
			if (!this.modal.equals("")) {
				out.println("<input type=\"hidden\" name=\"actionModal\">");
			}
			out.println("<input type=\"hidden\" name=\"tablaDatosDinamicosD\">");
			out.println("<input type=\"hidden\" name=\"filaSelD\">");
			
			out.println("<script language='JavaScript'>");
			if (this.ajusteAlto) {
				
				int espacioMenos= 0;
				if (this.ajustePaginador) {
					espacioMenos += 20; 
				}
				if (this.ajusteBotonera) {
					espacioMenos += 32; 
				}
				if (!this.ajuste.equals("0")) {
					espacioMenos += new Integer(this.ajuste).intValue(); 
				}
				out.println("");
				out.println(" document.body.onLoad=ajusteAltoMain('" + this.nombre + "Div',"+espacioMenos+");");
				out.println("");
			}
	
			out.println("");
			out.println(" function selectRow_" + this.nombre + "(fila) {");
			out.println("   var tabla;");
			out.println("   tabla = document.getElementById('" + this.nombre + "');");
			out.println("   for (var i=0; i<tabla.rows.length; i++) {");
			out.println("     tabla.rows[i].className = 'listaNonEdit';");
			out.println("   }");
			out.println("   tabla.rows[fila].className = 'listaNonEditSelected';");
			out.println(" }");
			out.println("");
	
			// Funcion de consultar
			out.println(" function "+ TagFila.accConsultar + "_" + this.nombre + "(fila) {");
			out.println("   var datos;");
			out.println("   datos = document.getElementById('tablaDatosDinamicosD');");
			out.println("   datos.value = \"\"; ");
			out.println("   var i, j;");
			out.println("   for (i = 0; i < " + (this.numColumnas-1) + "; i++) {");
			out.println("      var tabla;");
			out.println("      tabla = document.getElementById('" + this.nombre + "');");
			out.println("      if (i == 0) {");
			out.println("        var flag = true;");
			out.println("        j = 1;");
			out.println("        while (flag) {");
			out.println("          var aux = 'oculto' + fila + '_' + j;");
			out.println("          var oculto = document.getElementById(aux);");
			out.println("          if (oculto == null)  { flag = false; }");
			out.println("          else { " );
			out.println("          alert('oculto.value'+oculto.value);" );
			out.println("          if(oculto.value=='')       		oculto.value=' ';");
			out.println("			datos.value = datos.value + oculto.value + ','; }");
			out.println("          j++;");
			out.println("        }");
			out.println("        datos.value = datos.value + \"%\"");
			out.println("      } else { j = 2; }");
			out.println("      if ((tabla.rows[fila].cells)[i].innerText == \"\")");
			out.println("        datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';");
			out.println("      else");
			out.println("        datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';");
			out.println("   }");
			out.println("   document.forms[0].modo.value = \"Ver\";");
	
			if (!this.modal.equals(""))
			{
				out.println("   ventaModalGeneral(document.forms[0].name,\""+this.modal+"\");");
			}
			
			else
			{
				out.println("   document.forms[0].submit();");
			}
			
			out.println(" }");
			out.println("");
	
			// Funcion Editar
			out.println(" function " + TagFila.accEditar + "_" + this.nombre + "(fila) {");
			out.println("   var datos;");
			out.println("   datos = document.getElementById('tablaDatosDinamicosD');"); 
			out.println("   datos.value = \"\"; ");
			out.println("   var i, j;");
			out.println("   for (i = 0; i < " + (this.numColumnas-1) + "; i++) {");
			out.println("      var tabla;");
			out.println("      tabla = document.getElementById('" + this.nombre + "');");
			out.println("      if (i == 0) {");
			out.println("        var flag = true;");
			out.println("        j = 1;");
			out.println("        while (flag) {");
			out.println("          var aux = 'oculto' + fila + '_' + j;");
			out.println("          var oculto = document.getElementById(aux);");
			out.println("          if (oculto == null)  { flag = false; }");
			out.println("          else { " );
			out.println("          if(oculto.value=='')       		oculto.value=' ';");
			out.println("			datos.value = datos.value + oculto.value + ','; }");
			out.println("          j++;");
			out.println("        }");
			out.println("        datos.value = datos.value + \"%\"");
			out.println("      } else { j = 2; }");
			out.println("      if ((tabla.rows[fila].cells)[i].innerText == \"\") ");
			out.println("        datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';");
			out.println("      else");
			out.println("        datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';");
			out.println("   }");
			out.println("   document.forms[0].modo.value = \"Editar\";");
	
			if (!this.modal.equals(""))
			{
				out.println("   var resultado = ventaModalGeneral(document.forms[0].name,\""+this.modal+"\");");
				out.println("   if (resultado) {");
				out.println("  	 	if (resultado[0]) {");
				out.println("   		refrescarLocalArray(resultado);");
				out.println("   	} else ");
				out.println("   	if (resultado==\"MODIFICADO\")");
				out.println("   	{");
				out.println("      		refrescarLocal();");
				out.println("   	}");
				out.println("   }");
			}
			
			else
			{
				out.println("   document.forms[0].submit();");
			}
			
			out.println(" }");
			out.println("");
	
			// Funcion Borrar
			out.println(" function " + TagFila.accBorrar + "_" + this.nombre + "(fila) {");
			out.println("   var datos;");
			out.println("   if (confirm(\'"+ UtilidadesString.getMensajeIdioma(usrbean,"messages.deleteConfirmation") + "\')){");			
			out.println("   	datos = document.getElementById('tablaDatosDinamicosD');");
			out.println("       datos.value = \"\"; ");
			out.println("   	var i, j;");
			out.println("   	for (i = 0; i < " + (this.numColumnas-1) + "; i++) {");
			out.println("      		var tabla;");
			out.println("      		tabla = document.getElementById('" + this.nombre + "');");
			out.println("      		if (i == 0) {");
			out.println("        		var flag = true;");
			out.println("        		j = 1;");
			out.println("        		while (flag) {");
			out.println("          			var aux = 'oculto' + fila + '_' + j;");
			out.println("          			var oculto = document.getElementById(aux);");
			out.println("          			if (oculto == null)  { flag = false; }");
			out.println("          else { " );
			out.println("          if(oculto.value=='')       		oculto.value=' ';");
			out.println("			datos.value = datos.value + oculto.value + ','; }");
			out.println("          			j++;");
			out.println("        		}");
			out.println("        		datos.value = datos.value + \"%\"");
			out.println("      		} else { j = 2; }");
			out.println("      		if ((tabla.rows[fila].cells)[i].innerText == \"\")");
			out.println("        		datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';");
			out.println("      		else");
			out.println("        		datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';");
			out.println("   	}");
			out.println("   	var auxTarget = document.forms[0].target;");
			out.println("   	document.forms[0].target=\"submitArea\";");
			out.println("   	document.forms[0].modo.value = \"Borrar\";");
			out.println("   	document.forms[0].submit();");			
			out.println("   	document.forms[0].target=auxTarget;");
			out.println(" 	}");			
			out.println(" }");
	
			out.println("</script>");	

			out.println("<!-- 1. Pintamos la cabecera de la tabla con los contenidos -->");
			
				out.println("<table id='" + this.nombre + "Cabeceras' border='" + this.borde + 
							"' width='98.43%' cellspacing='0' cellpadding='0'>");
				out.println("	<tr class = '" + this.clase + "'>");

				if (this.nombreCol.length == this.tamanoCol.length) {
					for (int i = 0; i < this.numColumnas; i++) {
						String fila = "<td align='center' width='" + this.tamanoCol[i] + "%'>" + (this.nombreCol[i].equalsIgnoreCase("&nbsp;")?"&nbsp;":UtilidadesString.getMensajeIdioma(usrbean, this.nombreCol[i].trim())) + "</td>";
						out.println(fila);
					}
				}
				else {
					out.println("<td>El numero de columnas no coincide con el tamano de las columnas<td>");
				}
				out.println("	</tr>");
				out.println("</table>");

				out.println("<!-- 2. Pintamos el contenido de la tabla -->");

				//out.println("<div id='" + this.nombre + "Div' style='height:" + this.alto + "px; position:absolute; width:100%; overflow-y:auto'>");
				// LMS 15/02/2005 Con el position absolute había que estar haciendo filigranas en los JPS. Le elimino.
				
				out.println("<div id='" + this.nombre + "Div' style='height:" + this.alto + "; position:absolute; width:100%; overflow-y:auto'>");
				out.println("<table id='" + this.nombre + "' border='" + this.borde + 
							"' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>"); 
				
				out.println("	<tr>");
				
				if (this.nombreCol.length == this.tamanoCol.length) {
					for (int i = 0; i < this.numColumnas; i++) {
						String fila = "<td align='center' width='" + this.tamanoCol[i] + "%'></td>";
						out.println(fila);
					}
				}
				else {
					out.println("<td>El numero de columnas no coincide con el tamano de las columnas<td>");
				}
				out.println("	</tr>");  

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() 
	{
		try
		{
			String aux = "";
			
			HttpSession session = pageContext.getSession();
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);			
			PrintWriter out = pageContext.getResponse().getWriter();
			out.println("</table>");
			out.println("</div>");

			out.println("<script>");
			out.println("");
			out.println(" function validarAncho_" + this.nombre + "() {");
	
			out.println("  if (document.all." + this.nombre +".clientHeight < document.all." + this.nombre + "Div.clientHeight) {");
			out.println("   document.all." + this.nombre + "Cabeceras.width='100%';");
			out.println("  } else {");
			out.println("   document.all." + this.nombre + "Cabeceras.width='98.43%';");
			out.println("  } ");
			out.println(" }");
			out.println("");
			out.println("");
			out.println(" validarAncho_" + this.nombre + "();");
	
			
			// si no tiene onload lo reescribo con la funcion validarAncho, si lo tiene se ejecuta en la misma pagina.
			out.println(" if (document.body.onload==null) {");
	//		out.println("  	alert(\"cargo\");");
			out.println(" 	document.body.onload=validarAncho_" + this.nombre);
			out.println(" } else {");
			out.println(" 	validarAncho_" + this.nombre + "();");
			out.println(" } ");
			out.println("</script>");
		
			
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return EVAL_PAGE;
	}
}
