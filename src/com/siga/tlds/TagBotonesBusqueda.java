//VERSIONES
//raul.ggonzalez 10-12-2004 Creacion
//raul.ggonzalez 21-12-2004 cambio para que ante ninguna clave de boton no se muestre mas que la barra
//Luis Miguel Sánchez PIÑA - 12/04/2005 Se anhade el botón "Generar PDF".
//raul.ggonzalez 24-01-2006 cambio boton anhadir poblaciones
//

package com.siga.tlds;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import com.atos.utils.ClsConstants;
import com.atos.utils.SearchButtonsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SearchButtonComparator;
import com.siga.Utilidades.UtilidadesString;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * Tag que trata la linea de botones de una barra de busqueda 
 * @author AtosOrigin 10-12-04
 */

public class TagBotonesBusqueda extends TagSupport {

	private static final long serialVersionUID = 2444000806294867577L;

	/** claves de los botones a mostrar en el tag */
	String botones; 
	/** Titulo a mostrar en la barra de busqueda (cabecera de la tabla) */
	String titulo; 
	String modal=""; 
	String clase = "";
	String modo="EDICION";
	
	private static String SEPARATOR = ",";
	
	private ArrayList<String> valoresEdicion;  
	private ArrayList<String> valoresConsulta; 

	private List<SearchButtonsConstants> botonera = null;


	//No se pueden utilizar genericos en el tipo de parametro, se produce un error
	/**
 	 * Rellena la lista de botones
	 * @param lista
	 */
	public void setBotonera (ArrayList lista){
		botonera = lista;
	}
	
	/**
	 * Rellena la lista de botones 
	 * @param dato - Contiene una lista de claves separados por comas de los botones a mostrar
	 * @see SearchButtonsConstants
	 */
	public void setBotones(String dato) {
		botonera = SearchButtonsConstants.getEnums(dato, SEPARATOR);
	}

	/**
	 * Da valor al atributo 
	 * @author raul.ggonzalez. 10-12-04
	 * @param dato 
	 */
	public void setTitulo(String dato) {
		this.titulo = dato;
	}
	

	/**
	 * Da valor al atributo 
	 * @author raul.ggonzalez. 10-12-04
	 * @param dato - Contiene el tamanho de la ventana en la que se encuentra, si es modal
	 * 				 <br>P pequenha, G grande, M mediana 
	 */
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
	
	/**
	 * Da valor al atributo 
	 * @author ana.combarros. 03-01-05
	 * @param dato 
	 */
	public void setModo(String modo) {
		if (modo!=null) {
			this.modo = modo.toUpperCase();
		} else {
			this.modo = "EDICION"; 
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
			UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
			String tipoAcceso = usrbean.getAccessType();
			PrintWriter out = pageContext.getResponse().getWriter();
		
			//valores permitidos para el parametro modo cuando se entra en modo edicion
			this.valoresEdicion = new ArrayList<String>();
			this.valoresEdicion.add("EDICION");
			this.valoresEdicion.add("EDITAR");
			this.valoresEdicion.add("NUEVO");
			
			//valores permitidos para el parametro modo cuando se entra en modo consulta
			this.valoresConsulta = new ArrayList<String>();
			this.valoresConsulta.add("VER");
			this.valoresConsulta.add("CONSULTA");
	
			out.println("	<!-- BOTONES BUSQUEDA -->");

			if (this.clase==null || this.clase.trim().equals("")) {
				this.clase = "botonesSeguido"; 
			}
			out.println("<table class=\""+this.clase+"\" id=\"idBotonesBusqueda\" align=\"center\" >");
			out.println("<tr> ");
			out.println("<td class=\"titulitos\">");
			if (this.titulo!=null) {
				out.println(UtilidadesString.getMensajeIdioma(usrbean, this.titulo));
			} else { 
				out.println("&nbsp;");
			}
			out.println("</td>");
			
			Collections.sort(botonera, new SearchButtonComparator());
			
			for (SearchButtonsConstants sbc: botonera){
				if(sbc==null){
					out.println("<td class=\"tdBotones\">");
					out.println("</td>");
				}else{
				
					switch(sbc){
				
					case BUSCAR: 
								out.println("<td class=\"tdBotones\">");
								
								out.println("   <script type='text/javascript'>");
								out.println("     function setFilaSeleccionadaD (valor) {");
								out.println("       a = document.getElementById('limpiarFilaSeleccionada')");
								out.println("       if (a == undefined) return;");
								out.println("       a.value = valor;");
								out.println("       return;");
								out.println("     }");
								
								out.println("     registrarEnterFormularios ();");
								
								out.println("   </script>");
			
								out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, sbc.getLabel()) +"\" name=\"idButton\"  onclick=\"setFilaSeleccionadaD('true'); "+ sbc.getAccion() +"; setFilaSeleccionadaD('false');\" class=\"button\" value=\"");
								out.print(UtilidadesString.getMensajeIdioma(usrbean, sbc.getLabel()));
								out.println("\">");
								out.println("</td>");
								break;
	
					case NUEVO:	if (tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) &&
									this.valoresEdicion.contains(this.modo)){
									printButton(usrbean, out, sbc);
								}
								break;
								
					default:	printButton(usrbean, out, sbc);
								break;
					}
				}
			}
			
			out.println("</tr>	 ");  
  	        out.println("</table>");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return EVAL_BODY_INCLUDE;	 	 	
	}

	
	private void printButton(UsrBean usrbean, PrintWriter out,	SearchButtonsConstants sbc) {
		out.println("<td class=\"tdBotones\">");
		out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, sbc.getLabel()) +"\" id=\"idButton\" onclick=\"return "+ sbc.getAccion() +";\" class=\"button\" value=\"");
		out.print(UtilidadesString.getMensajeIdioma(usrbean, sbc.getLabel()));
		out.println("\">");
		out.println("</td>");
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}
	
}
