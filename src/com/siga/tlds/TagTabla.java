/*
 * VESIONES:
 * daniel.campos 12-11-2004
 * raul.ggonzalez 12-12-2004 Se anhade la propiedad Modal que abre una ventana modal en los mantenimientos
 * raul.ggonzalez 22-12-2004 Se cambia la propiedad modal para que acepte "p, m, o g" como tamanhos de ventana
 * Luis Miguel Sánchez PIÑA 04/01/2005 Se anhade un nuevo tratamiento para la edición en ventanas modales, que
 *                                     permite refrescar la lista de resultados una vez modificado el registro.
 * Luis Miguel Sánchez PIÑA 07/01/2005 Se cambia la función borrar para que el target sea SIEMPRE submitArea.
 * Luis Miguel Sánchez PIÑA 15/02/2005 Corrección de errores surgidos al utilizar varias tablas en un mismo JSP.
 * Luis Miguel Sánchez PIÑA 15/02/2005 Quito el position absolute para no tener que hacer filigranas en los JSPs.
 * Luis Miguel Sánchez PIÑA 18/02/2005 Vuelvo a poner el position absolute porque los JSPs no salen bien del todo
 *                                     con los desarrollos hechos hasta ahora.
 * raul.ggonzalez 14-03-2005 Se anhade javascript para que una vez se haya llamado a borrar con target = submitArea, 
 * 							 se quede el target original en el formulario
 * raul.ggonzalez 18-05-2005 Se anhade la propiedad scrollModal que indica si la ventana tendra o no scroll 
 */
package com.siga.tlds;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.*;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

/**
 * @author AtosOrigin
 */

public class TagTabla extends TagSupport {

	int borde=1;
	int numColumnas = 0;
	String alto = "0";
	int [] tamanoCol = new int [20];
	String [] nombreCol = new String [20];
	String nombre, estilo, clase;
// RGG 22-12-2004	boolean modal;
	String modal="";
	boolean scrollModal=false;
	boolean ajusteAlto=true;
	String ajuste="0";
	String ajusteChrome="0";
	boolean ajusteBotonera=false;
	boolean ajustePaginador=false;
	boolean activarFilaSel=false;			// Activa la seleccion automatica de la fila pulsada
	private String mensajeBorrado;
	
	public void setNombre		(String dato) 	{ this.nombre 	= dato;	}
	public void setBorde		(String dato) 	{
		try {
			if (dato != null) { 
//				this.borde 	= Integer.parseInt(dato);
// RGG 15-03-2006 para que el border simepre sea 1
				this.borde 	= 1;
			}
		}
		catch (Exception e) {
			this.borde = 1;
		}
	}
	public void setClase		(String dato) 	{ this.clase	= "tableTitle";	}
	public void setEstilo		(String dato) 	{ this.estilo	= dato;	}
	public void setTamanoCol	(String dato) 	{
		StringTokenizer datos = new StringTokenizer(dato, ",");
		for (int i = 0; datos.hasMoreElements(); i++) {
			String tamano = datos.nextToken();
			try {
				this.tamanoCol[i] = Integer.parseInt(tamano);
			}
			catch (Exception e) {
				this.tamanoCol[i] = 10;
			}
		}
	}
	public void setNombreCol	(String dato) 	{
		dato += ",";
		dato = dato.replaceAll(",,", ",&nbsp;,");
		dato = dato.replaceAll(",,", ",&nbsp;,");
		StringTokenizer datos = new StringTokenizer(dato, ",");
		this.numColumnas=0;
		this.nombreCol = new String [20];
		for (int i = 0; datos.hasMoreElements(); i++) {
			String nombre = datos.nextToken();
			this.nombreCol[i] = nombre.trim();
			this.numColumnas ++;
		}
	}
	
	public void setAlto (String dato) {
		try {
			if (dato != null) {
				if (dato.indexOf("%")==-1) {
					dato = dato+"px";
				}
				this.alto = dato;
			}
		}
		catch (Exception e) {
			this.alto = "135";
		}
	}
	
	public void setModal (String dato) {
		try {
			if (dato != null) 
				// RGG 22-12-2004 this.modal = new Boolean(dato).booleanValue();
				this.modal = dato.toUpperCase();
		}
		catch (Exception e) {
			this.modal = "";
		}
	}
	
	public void setScrollModal (String dato) {
		try {
			if (dato != null) 
				this.scrollModal = new Boolean(dato).booleanValue();
		}
		catch (Exception e) {
			this.scrollModal = false;
		}
	}
	public void setAjusteAlto (String dato) {
		try {
			if (dato != null) 
				this.ajusteAlto = new Boolean(dato).booleanValue();
		}
		catch (Exception e) {
			this.ajusteAlto = true;
		}
	}
	public void setAjuste (String dato) {
		try {
			if (dato != null) 
				this.ajuste = dato;
		}
		catch (Exception e) {
			this.ajuste = "0";
		}
	}
	public void setAjusteBotonera (String dato) {
		try {
			if (dato != null) 
				this.ajusteBotonera = new Boolean(dato).booleanValue();
		}
		catch (Exception e) {
			this.ajusteBotonera = false;
		}
	}

	public void setAjustePaginador (String dato) {
		try {
			if (dato != null) 
				this.ajustePaginador= new Boolean(dato).booleanValue();
		}
		catch (Exception e) {
			this.ajustePaginador = false;
		}
	}

	public void setActivarFilaSel (String dato) 
	{
		this.activarFilaSel = UtilidadesString.stringToBoolean(dato);		
	}
	public void setAjusteChrome (String dato) {
		try {
			if (dato != null) 
				this.ajusteChrome = dato;
		}
		catch (Exception e) {
			this.ajusteChrome = "0";
		}
	}		

	
	public int doStartTag() 
	{
		try {
			//pageContext.getResponse().setContentType("text/html");
			
			HttpSession session = pageContext.getSession();
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);

//			String idioma = usrbean.getLanguage();
//			ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources",new Locale(idioma));
			
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

			
			//out.println("<input type=\"hidden\" name=\"tablaDatosDinamicosD\" id=\"tablaDatosDinamicosD\" >");
			//out.println("<input type=\"hidden\" name=\"filaSelD\" id=\"filaSelD\" >");
			
			out.println("<!-- 0. Pintamos las funciones -->");
			
			//out.println("<SCRIPT FOR=window EVENT=onload LANGUAGE='JScript'>");
			//out.println("  validarAncho_" + this.nombre + "();"); 
			//out.println("</SCRIPT>");

			out.println("<script language='JavaScript'>");
			out.println("");
			

			out.println(" jQuery(document).ready(function() { ");
			out.println("    jQuery('form:first',document).append('<input type=\"hidden\" name=\"tablaDatosDinamicosD\" id=\"tablaDatosDinamicosD\" />');");
			out.println("  ");
			out.println("    jQuery('form:first',document).append('<input type=\"hidden\" name=\"filaSelD\" id=\"filaSelD\" />');  ");
			out.println("  ");
			if (!this.modal.equals("")) {
				out.println("    jQuery('form:first',document).append('<input type=\"hidden\" name=\"actionModal\" id=\"actionModal\">');  ");
			}			
			out.println("     });  ");
			
			out.println("");
			
			
			out.println(" function selectRow(fila) {");
			out.println("   if(document.getElementById('filaSelD')){ ");
			out.println("   document.getElementById('filaSelD').value = fila;");
			out.println("   var tabla;");
			out.println("   tabla = document.getElementById('" + this.nombre + "');");
			out.println("   for (var i=0; i<tabla.rows.length; i++) {");
			////////////////////////////
			// Antes:
			//   out.println("     tabla.rows[i].className = 'listaNonEdit';");
			// Ahroa:
			out.println("     if (i%2 == 0) tabla.rows[i].className = 'filaTablaPar';");
			out.println("     else          tabla.rows[i].className = 'filaTablaImpar';");
			////////////////////////////
			out.println("   }");
			out.println("   tabla.rows[fila].className = 'listaNonEditSelected';");
			out.println("  }");
			out.println(" }");
			out.println("");

			// Funcion de consultar
			out.println(" function "+ TagFila.accConsultar + "(fila) {");
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
			//out.println("      datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML + ',';");
			//out.println("      if ((tabla.rows[fila].cells)[i].all[0]!=null)");
			out.println("      if ((tabla.rows[fila].cells)[i].innerHTML == \"\")");
			out.println("        datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';");
			out.println("      else");
			out.println("        datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'') + ',';");
			out.println("   }");
			out.println("   document.forms[0].modo.value = \"Ver\";");

			// RGG 07-12-04 se introduce el cambio en la llamada para ventana modal 
			if (!this.modal.equals("")) {
				if (this.scrollModal) {
					out.println("   ventaModalGeneralScrollAuto(document.forms[0].name,\""+this.modal+"\");");
				} else {
					out.println("   ventaModalGeneral(document.forms[0].name,\""+this.modal+"\");");
				}
			} else {
				out.println("   document.forms[0].submit();");
			}
			
			out.println(" }");
			out.println("");

			// Funcion Editar
			out.println(" function " + TagFila.accEditar + "(fila) {");
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
			//out.println("      datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML + ',';");
			//out.println("      if ((tabla.rows[fila].cells)[i].all[0]=null)");
			out.println("      if ((tabla.rows[fila].cells)[i].innerHTML == \"\") {");
			//BNS CONTROLAMOS LA EXISTENCIA DE ALGÚN ELEMENTO EN LA CELDA Y LO DEJAMOS VACÍO SI NO EXISTE ADEMÁS SUSTITUYO .ALL POR .children QUE SOLO FUNCIONA EN EXPLORER
			out.println("      	if ((tabla.rows[fila].cells)[i].children != undefined && (tabla.rows[fila].cells)[i].children.length > 0) {");
			out.println("        datos.value = datos.value + (tabla.rows[fila].cells)[i].children[j-2].value + ',';");
			out.println("      	}else{");
			out.println("        datos.value = datos.value + ',';");
			out.println("      	}");
			out.println("      }else");
			out.println("        datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'') + ',';");
			out.println("   }");
			out.println("   document.forms[0].modo.value = \"Editar\";");

			// RGG 07-12-04 se introduce el cambio en la llamada para ventana modal 
			if (!this.modal.equals("")) {
				if (this.scrollModal) {
					out.println("   var resultado = ventaModalGeneralScrollAuto(document.forms[0].name,\""+this.modal+"\");");
				} else {
					out.println("   var resultado = ventaModalGeneral(document.forms[0].name,\""+this.modal+"\");");
				}
				out.println("   if (resultado) {");
				out.println("  	 	if (resultado==\"MODIFICADO\") {");
				out.println("   	    alert(\""+UtilidadesString.getMensajeIdioma(usrbean,"messages.updated.success")+"\",'success');");
				out.println("   		refrescarLocal();");
				out.println("       } else if (resultado[0]) {");
				out.println("   	    alert(\""+UtilidadesString.getMensajeIdioma(usrbean,"messages.updated.success")+"\",'success');");
				out.println("      		refrescarLocalArray(resultado);");
				out.println("   	}");
				out.println("   }");
			} else { 
				out.println("   document.forms[0].submit();");
			}
			out.println(" }");
			out.println("");

			// Funcion Borrar
			out.println(" function " + TagFila.accBorrar + "(fila) {");
			out.println("   var datos;");
			// Confirmacion borrado
			String mensaje = UtilidadesString.getMensajeIdioma(usrbean,"messages.deleteConfirmation");
			if (getMensajeBorrado() != null) {
				mensaje = UtilidadesString.getMensajeIdioma(usrbean, getMensajeBorrado());
			}
			out.println("   if (confirm(\'"+ mensaje + "\')){");			
			out.println("   	datos = document.getElementById('tablaDatosDinamicosD');");
// Antes: 
//			out.println("   	if (document.forms[0].modo.value == \"Borrar\") { datos.value = datos.value + \"#\"; } ");
//			out.println("   	else { datos.value = \"\"; } ");
// Ahora:
			out.println("       datos.value = \"\"; ");
//////////	
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
			//out.println("      datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML + ',';");
			//out.println("      if ((tabla.rows[fila].cells)[i].all[0]!=null)");
			out.println("      		if ((tabla.rows[fila].cells)[i].innerHTML == \"\")");
			out.println("        		datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';");
			out.println("      		else");
			out.println("        		datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'') + ',';");
			out.println("   	}");
			// Omitir borrado vista
//			out.println("   tabla.rows[fila].style.display = \"none\";");
//			out.println("   validarAncho();");
			/* LMS (07/01/2005) */
			/* Se modifica el target de Borrar a submitArea PARA TODOS LOS CASOS. */
			out.println("   	var auxTarget = document.forms[0].target;");
			out.println("   	document.forms[0].target=\"submitArea\";");
			out.println("   	document.forms[0].modo.value = \"Borrar\";");
			// Envio formulario
			out.println("   	document.forms[0].submit();");			
			out.println("   	document.forms[0].target=auxTarget;");
			out.println(" 	}");			
			out.println(" }");

			out.println("</script>");
			
			// fin de la cosa
			
			out.println("<!-- 1. Pintamos la cabecera de la tabla con los contenidos -->");
		
			out.println("<table id='" + this.nombre + "Cabeceras' border='" + this.borde + 
						"' width='983px' cellspacing='0' cellpadding='0' style='table-layout:fixed;'>");
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
			
			if(estilo!=null && !estilo.equals("")){
				out.println("<div id='" + this.nombre + "Div' name='" + this.nombre + "Div' style='" + this.estilo +"'>");		
			}else{
				out.println("<div id='" + this.nombre + "Div' name='" + this.nombre + "Div' style='display:none; height:" + this.alto + "; width:100%; overflow-y:auto'>");
			}
			out.println("<table id='" + this.nombre + "' name='" + this.nombre + "' border='" + this.borde + 
						"' align='center' width='100%' cellspacing='0' cellpadding='0'  style='table-layout:fixed; margin:0px;'>"); 
			
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
		try {
			//String aux = "";
			//pageContext.getResponse().setContentType("text/html");
			
			//HttpSession session = pageContext.getSession();
			//UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);			
			PrintWriter out = pageContext.getResponse().getWriter();
			out.println("</table>");
			out.println("</div>");

			//out.println("<SCRIPT FOR=window EVENT=onload LANGUAGE='JScript'>");
			//out.println("  validarAncho_" + this.nombre + "();"); 
			//out.println("</SCRIPT>");
			
			out.println("<script language='JavaScript'>");
			out.println("	jQuery(document).ready(function() {jQuery(\"#" + this.nombre + "Div\").show(); validarAncho_" + this.nombre + "();");			
			out.println("	});");
			if (this.ajusteAlto) {
				
				//int espacioMenos= 0;
//				if (this.ajustePaginador) {
//					espacioMenos += 20; 
//				}
//				if (this.ajusteBotonera) {
//					espacioMenos += 32; 
//				}
				String sAjusteBotonera = "";
				if (this.ajusteBotonera && !this.ajustePaginador){
					sAjusteBotonera = "var tablaBotones = undefined; if (jQuery('table.botonesDetalle').length > 0) tablaBotones = jQuery('table.botonesDetalle'); else if (jQuery('table.botonesDetalle', parent.document.body).length > 0) tablaBotones = jQuery('table.botonesDetalle', parent.document.body); if(tablaBotones != undefined){var totalDiv = (jQuery('#"+this.nombre+"Div').offset().top +jQuery('#"+this.nombre+"Div').outerHeight()); if (tablaBotones.offset().top > totalDiv) espacioMenos -= (tablaBotones.offset().top - totalDiv); else if (tablaBotones.offset().top < totalDiv) espacioMenos += (totalDiv - tablaBotones.offset().top);}";
				}
				/*
				if (!this.ajuste.equals("0")) {
					espacioMenos += new Integer(this.ajuste).intValue(); 
				}
				*/
				if (this.ajuste == null || "".equals(this.ajuste))
					this.ajuste = "0";
					
				out.println("");
				out.println("jQuery(document).ready(function() {");
				out.println("   validarAncho_" + this.nombre + "();");
				
				out.println(" 	var navegador = navigator.userAgent; ");
				out.println(" 	if (navigator.userAgent.indexOf('Chrome') !=-1) { ");
				out.println("    	var espacioMenos = " + this.ajuste + " + " + this.ajusteChrome + ";");
				out.println("   } else {");
				out.println("    	var espacioMenos = " + this.ajuste + ";");
				out.println("   }");
		
				out.println(    sAjusteBotonera);
				out.println("   ajusteAltoMain('" + this.nombre + "Div',espacioMenos);");
				out.println("});");
			}
			out.println("");
			out.println(" function validarAncho_" + this.nombre + "() {");
			out.println("  if (document.getElementById('" + this.nombre +"').clientHeight < document.getElementById('" + this.nombre + "Div').clientHeight) {");
			out.println("   document.getElementById('" + this.nombre + "Cabeceras').width='100%';document.getElementById('" + this.nombre + "').width='100%';");
			out.println("  }");

			// seleccionamos la fila
			out.println("");
			int filaSeleccionada = this.popFilaSeleccionada();
			if (filaSeleccionada > -1) {
				out.println("  var tabla;");
				out.println("  tabla = document.getElementById('" + this.nombre + "');");
				out.println("  if ((tabla.rows.length - 1) >= "+ filaSeleccionada + ") {");
			    out.println("     selectRow(" + filaSeleccionada + "); ");
				out.println("     tabla.rows["+ filaSeleccionada + "].scrollIntoView(false);");
				out.println("  }");
			}

			out.println(" }");
			out.println("");
			out.println("");
			out.println(" validarAncho_" + this.nombre + "();");
			
			// si no tiene onload lo reescribo con la funcion validarAncho, si lo tiene se ejecuta en la misma pagina.
			out.println(" if (window.onload==null) {");
//			out.println("  	alert(\"cargo\");");
			out.println(" 	window.onload=validarAncho_" + this.nombre);
			out.println(" } else {");
			out.println(" 	validarAncho_" + this.nombre + "();");
			out.println(" } ");
			
			//BNS INC_10371_SIGA			
			out.println("	jQuery(document).ready(function() {");
			out.println(" 	var hDatos = jQuery('#" + this.nombre + "').css('height');");
			out.println(" 	var hDiv = jQuery('#" + this.nombre + "Div').css('height'); ");		
			out.println("	if (hDatos.length > hDiv.length || (hDatos.length==hDiv.length && hDatos > hDiv))");
			//out.println("alert(jQuery('#" + this.nombre + "').css('height')+ ' > ' + jQuery('#" + this.nombre + "Div').css('height'))");
			//out.println("if (jQuery('#" + this.nombre + "').css('height') > jQuery('#" + this.nombre + "Div').css('height'))");
			//out.println("    	jQuery('#" + this.nombre + "Cabeceras').width(jQuery('#" + this.nombre + "').width() - scrollbarWidth());");scrollHeight
			out.println("    	jQuery('#" + this.nombre + "Cabeceras').width(jQuery('#" + this.nombre + "').get(0).clientWidth);");
			out.println("function scrollbarWidth() {");
			out.println("    var $inner = jQuery('<div style=\"width: 100%; height:200px;\">test</div>'),");
			out.println("        $outer = jQuery('<div style=\"width:200px;height:150px; position: absolute; top: 0; left: 0; visibility: hidden; overflow:hidden;\"></div>').append($inner),");
			out.println("        inner = $inner[0],");
			out.println("        outer = $outer[0];");
			out.println("    jQuery('body').append(outer);");
			out.println("    var width1 = inner.offsetWidth;");
			out.println("    $outer.css('overflow', 'scroll');");
			out.println("    var width2 = outer.clientWidth;");
			out.println("    $outer.remove();");
			out.println("    return (width1 - width2);");
			out.println("}");
			out.println("	});");
			out.println("</script>");
			

		// aqui iria la cosa
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return EVAL_PAGE; 			// continua la ejecucion de la pagina
	}
	
	int popFilaSeleccionada () 
	{
		try {
			if (activarFilaSel) {
				HttpSession session = pageContext.getSession();
				int fila = Integer.parseInt(((String)session.getAttribute("FILA_SELECCIONADA")));
				session.removeAttribute("FILA_SELECCIONADA");
				return fila;
			}
		}
		catch (Exception e) {
			return -1;
		}
		return -1;
	}
	/**
	 * @return the mensajeBorrado
	 */
	public String getMensajeBorrado() {
		return mensajeBorrado;
	}
	/**
	 * @param mensajeBorrado the mensajeBorrado to set
	 */
	public void setMensajeBorrado(String mensajeBorrado) {
		this.mensajeBorrado = mensajeBorrado;
	}
}
