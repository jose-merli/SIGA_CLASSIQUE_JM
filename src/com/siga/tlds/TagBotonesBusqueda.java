//VERSIONES
//raul.ggonzalez 10-12-2004 Creacion
//raul.ggonzalez 21-12-2004 cambio para que ante ninguna clave de boton no se muestre mas que la barra
//Luis Miguel Sánchez PIÑA - 12/04/2005 Se anhade el botón "Generar PDF".
//raul.ggonzalez 24-01-2006 cambio boton anhadir poblaciones
//

package com.siga.tlds;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.*;

import com.atos.utils.ClsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * Tag que trata la linea de botones de una barra de busqueda 
 * @author AtosOrigin 10-12-04
 */

public class TagBotonesBusqueda extends TagSupport {

	/** claves de los botones a mostrar en el tag */
	String botones; 
	/** Titulo a mostrar en la barra de busqueda (cabecera de la tabla) */
	String titulo; 
	String modal=""; 
	String clase = "";
	String modo="EDICION";
	
	private boolean volver;
	private boolean buscar;
	private boolean avanzada;
	private boolean simple;
	private boolean nuevo;
	private boolean anadirPob;
	private boolean limpiar;
	private boolean borrarlog;
	private boolean nuevaRegularizacion;
	private boolean generarPDFs;
	private boolean generarCarta;
	private boolean nuevaSociedad;
	private boolean generarPDF;
	private boolean generarRecursos;
	private boolean validarVolante;
	private boolean informeJustificacion;
	private boolean listoParaEnviar;
	private boolean aniadirARemesa;
	private boolean generaExcel;
	private boolean isDescargaEejg;
	
	private ArrayList valoresEdicion;  
	private ArrayList valoresConsulta;  

	/**
	 * Da valor al atributo 
	 * @author raul.ggonzalez. 10-12-04
	 * @param dato - Contiene una lista de claves separados por comas de los botones a mostrar:
	 * 				 <br>V Volver, B Buscar, A Avanzada, S Simple, N Nuevo registro, L Limpiar, R Borrar Log, NR Nueva Regularización, P Generar PDF
	 */
	public void setBotones(String dato) {
		try {
			buscar=false;
			avanzada=false;
			simple=false;
			nuevo=false;
			limpiar=false;
			borrarlog=false;
			nuevaRegularizacion=false;
			generarPDF=false;
			generarPDFs=false;
			anadirPob=false;
			generarCarta=false;
			nuevaSociedad=false;
			generarRecursos=false;
			validarVolante=false;
			informeJustificacion = false;
			listoParaEnviar=false;
			aniadirARemesa=false;
			generaExcel = false;
			
			// RGG 21-12-2004 if (dato == null) dato = "V,B,A,S,N,L,R";
			if (dato == null) dato = "";
			
			dato += ",";
			StringTokenizer datos = new StringTokenizer(dato, ",");
			for (int i = 0; datos.hasMoreElements(); i++) {
				String tipo = datos.nextToken();
				tipo = tipo.trim();
				if (tipo.equalsIgnoreCase("ge")) {
					generaExcel=true;
				}
				if (tipo.equalsIgnoreCase("gps")) {
					generarPDFs=true;
				}
				else if (tipo.equalsIgnoreCase("b")) { 
					buscar=true;
				}
				else if (tipo.equalsIgnoreCase("v")) {
					volver=true;
				} 
				else if (tipo.equalsIgnoreCase("a")) {
					avanzada=true;
				} 
				else if (tipo.equalsIgnoreCase("s")) {
					simple=true;
				} 
				else if (tipo.equalsIgnoreCase("n")) {
					nuevo=true;
				} 
				else if (tipo.equalsIgnoreCase("an")) {
					anadirPob=true;
				} 
				else if (tipo.equalsIgnoreCase("l")) {
					limpiar=true;
				}
				else if (tipo.equalsIgnoreCase("r")) {
					borrarlog=true;
				}
				else if (tipo.equalsIgnoreCase("nr")) {
					nuevaRegularizacion=true;
				}
				else if (tipo.equalsIgnoreCase("p")) {
					generarPDF=true;
				}
				else if (tipo.equalsIgnoreCase("c")) {
					generarCarta=true;
				}
				else if (tipo.equalsIgnoreCase("ns")) {
					nuevaSociedad=true;
				}
				else if (tipo.equalsIgnoreCase("gr")) {
					generarRecursos=true;
				}
				else if (tipo.equalsIgnoreCase("vol")) {
				    validarVolante=true;
				}
				else if (tipo.equalsIgnoreCase("ij")) {
					informeJustificacion=true;
				}
				else if (tipo.equalsIgnoreCase("le")) {
					listoParaEnviar=true;
				}else if (tipo.equalsIgnoreCase("ar")) {
					aniadirARemesa=true;
				}else if (tipo.equalsIgnoreCase("dee")) {
					isDescargaEejg=true;
				}
					
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
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
			this.valoresEdicion = new ArrayList();
			this.valoresEdicion.add("EDICION");
			this.valoresEdicion.add("EDITAR");
			this.valoresEdicion.add("NUEVO");
			
			//valores permitidos para el parametro modo cuando se entra en modo consulta
			this.valoresConsulta = new ArrayList();
			this.valoresConsulta.add("VER");
			this.valoresConsulta.add("CONSULTA");
	
			out.println("	<!-- BOTONES BUSQUEDA -->");
			/*
			if (modal.equals("G")) {
				this.clase = "tablaCentralGrande"; 
			} else
			if (modal.equals("M")) {
				this.clase = "tablaCentralMedia"; 
			} else
			if (modal.equals("P")) {
				this.clase = "tablaCentralPeque"; 
			}
			*/
			// RGG 30-12-2004
			if (this.clase==null || this.clase.trim().equals("")) {
				this.clase = "botonesSeguido"; 
			}
			out.println("<table class=\""+this.clase+"\" align=\"center\" >");
			out.println("<tr> ");
			out.println("<td class=\"titulitos\">");
			if (this.titulo!=null) {
				out.println(UtilidadesString.getMensajeIdioma(usrbean, this.titulo));
			} else { 
				out.println("&nbsp;");
			}
			out.println("</td>");
			if (buscar) {
				out.println("<td class=\"tdBotones\">");
				
				out.println("   <script language='JavaScript'>");
				out.println("     function setFilaSeleccionadaD (valor) {");
				out.println("       a = document.getElementById('limpiarFilaSeleccionada')");
				out.println("       if (a == undefined) return;");
				out.println("       a.value = valor;");
				out.println("       return;");
				out.println("     }");
				
				out.println("     registrarEnterFormularios ();");
				
				out.println("   </script>");

				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.search") +"\" id=\"idButton\"  onclick=\"setFilaSeleccionadaD('true'); buscar(); setFilaSeleccionadaD('false');\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.search"));
				out.println("\">");
				out.println("</td>");
			}
			if (avanzada) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.searchAvanzada") +"\" id=\"idButton\" onclick=\"return buscarAvanzada();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.searchAvanzada"));
				out.println("\">");
				out.println("</td>");
			}
			if (simple) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.searchSimple") +"\" id=\"idButton\"  onclick=\"return buscarSimple();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.searchSimple"));
				out.println("\">");
				out.println("</td>");
			}
			if (nuevo && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.new") +"\" id=\"idButton\"  onclick=\"return nuevo();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.new"));
				out.println("\">");
				out.println("</td>");
			}
			if (limpiar) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.clear") +"\" id=\"idButton\"   onclick=\"return limpiar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.clear"));
				out.println("\">");
				out.println("</td>");
			}
			if (borrarlog) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "administracion.auditoria.borrarlog") +"\"   id=\"idButton\"  onclick=\"return borrarLog();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "administracion.auditoria.borrarlog"));
				out.println("\">");
				out.println("</td>");
			}
			if (nuevaRegularizacion) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.nuevaRegularizacion") +"\" id=\"idButton\"    onclick=\"return nuevaRegularizacion();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.nuevaRegularizacion"));
				out.println("\">");
				out.println("</td>");
			}
			if (nuevaSociedad) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.nuevaSociedad") +"\"   id=\"idButton\"  onclick=\"return nuevaSociedad();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.nuevaSociedad"));
				out.println("\">");
				out.println("</td>");
			}
			if (generarPDF) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarPDF") +"\"   id=\"idButton\"  onclick=\"return generarPDF();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarPDF"));
				out.println("\">");
				out.println("</td>");
			}
			if (generarPDFs) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarPDFmostrados") +"\"   id=\"idButton\"  onclick=\"return generarPDFMostrados();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarPDFmostrados"));
				out.println("\">");
				out.println("</td>");
			}
			if (generaExcel) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.exportarExcel") +"\"   id=\"idButton\"  onclick=\"return generaExcel();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.exportarExcel"));
				out.println("\">");
				out.println("</td>");
			}
			if (validarVolante) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.validarVolante") +"\"   id=\"idButton\"  onclick=\"return validarVolante();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.validarVolante"));
				out.println("\">");
				out.println("</td>");
			}
			if (informeJustificacion) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.informeJustificacion") +"\" id=\"idButton\"  onclick=\"return informeJustificacion();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.informeJustificacion"));
				out.println("\">");
				out.println("</td>");
			}

			// MAV 13/7/05 Creacion de un botón para generar cartas a interesados en SJCS
			if (generarCarta) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.cartaInteresados") +"\"  id=\"idButton\"  onclick=\"return generarCarta();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "gratuita.EJG.botonComunicaciones"));
				out.println("\">");
				out.println("</td>");
			}
			if (anadirPob) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.anadirPoblaciones") +"\"   id=\"idButton\"  onclick=\"return anadirPob();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.anadirPoblaciones"));
				out.println("\">");
				out.println("</td>");
			}
			if (volver) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.volver") +"\"  id=\"idButton\" onclick=\"return accionVolver();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.volver"));
				out.println("\">");
				out.println("</td>");
			}
			if (generarRecursos) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarRecursos") +"\"   id=\"idButton\"  onclick=\"return generarRecursos();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarRecursos"));
				out.println("\">");
				out.println("</td>");
			}
			if (listoParaEnviar) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaEJG_CAJG.listoParaEnviar") +"\"  id=\"idButton\" onclick=\"return accionListoParaEnviar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"gratuita.busquedaEJG_CAJG.listoParaEnviar"));
				out.println("\">");
				out.println("</td>");
			}
			if (aniadirARemesa) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.aniadirARemesa") +"\"  id=\"idButton\" onclick=\"return aniadirARemesa(true);\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.aniadirARemesa"));
				out.println("\">");
				out.println("</td>");
			}
			if (isDescargaEejg) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.descargaEejg") +"\"  id=\"idButton\" name=\"idButton\" onclick=\"return descargaEejg(true);\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.descargaEejg"));
				out.println("\">");
				out.println("</td>");
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
	
}
