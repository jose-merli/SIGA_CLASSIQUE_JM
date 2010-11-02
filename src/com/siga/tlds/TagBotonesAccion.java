//VERSIONES
//raul.ggonzalez 10-12-2004 Creacion
//raul.ggonzalez 21-12-2004 cambio para que ante ninguna clave de boton no se muestre mas que la barra
//raul.ggonzalez 22-12-2004 se anhade el boton solicitar... 
//ana.combarros	 04-01-2005 se anhade el boton solicitar nuevo
//ana.combarros	 04-01-2005 se anhade el atributo modo para controlar por permisos y el modo la aparicion de los botones: nuevo, guardar, guardar y cerrar y restablecer
//ana.combarros	 11-01-2005 se anhade boton Solicitar Modificacion
//ana.combarros	 19-01-2005 se anhade boton Marcar Todos, Desmarcar Todos, Procesar Solicitudes, Denegar Solicitudes
//raul.ggonzalez 25-01-2005 se anhade el boton generar Calendario 
//ana.combarros	 04-01-2005 se anhade boton Imprimir y Continuar y FinalizarCompra
//nuria.rodriguezg	14-02-2005	se anhade boton Desglose Pago.
//david.sanchezp 01-04-2005 se anhade el boton DefinirCriterio.
//raul.ggonzalez 06-04-2005 se anhade el boton generar Impreso 190 
//emilio.grau	 01/04/2005	se anhade botón Descargar
//Luis Miguel Sánchez PIÑA - 12/04/2005 Se anhade el botón "Aceptar".
//raul.ggonzalez 25-04-2005 se anhade el boton Descargas de ficehros 
//david.sanchez 22/04/2005: orden de botones modificado para dejar a la derecha los botones de guardar y cerrra, y cerrar.
//emilio.grau	 09/05/2005	se anhade botón Guardar y Ejecutar
//raul.ggonzalez 10-05-2005 se anhade el boton Detalle pago y detalle facturacion 
//raul.ggonzalez 30-05-2005 se anhade el boton Generar Informe
//david.sanchezp 28-09-2005: se anhade el boton Realizar Pago.
//david.sanchezp 20-02-2006: se anhade el boton Anular.
//RGG 10-04-2007: Se añade el botón de acceso rápido XXX
//11-04-2006 se anhade los botones, detalleLetrado y detalleConcepto para los detalles Pago en SJCS 
package com.siga.tlds;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import com.atos.utils.ActionButtonsConstants;
import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.SearchButtonsConstants;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.ActionButtonComparator;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmBotonAccionAdm;
import com.siga.beans.AdmBotonAccionBean;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * Tag que trata la linea de botones de una barra de acciones sobre registro
 * @author AtosOrigin 10-12-04
 */

public class TagBotonesAccion extends TagSupport {

	private static final long serialVersionUID = -6998358505626689282L;

	/** claves de los botones a mostrar en el tag */
	String botones; 
	/** estilo aplicado a la capa que lo presenta */
	String clase; 
	String modal=""; 
	String modo="EDICION";
	private String titulo ="";

	private static String SEPARATOR = ",";

	private ArrayList<String> valoresEdicion;  
	private ArrayList<String> valoresConsulta;  

	private String idBoton;
	private String idPersonaBA;
	private String idInstitucionBA;
	private boolean ordenar = true;


	private List<ActionButtonsConstants> botonera = null;


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
		botonera = ActionButtonsConstants.getEnums(dato, SEPARATOR);
	}

	/**
	 * Da valor al atributo 
	 * @author raul.ggonzalez. 10-12-04
	 * @param dato 
	 */
	public void setClase(String dato) {
		if (dato!=null) {
			this.clase = dato;
		} else {
			this.clase = "botonesDetalle"; 
		}
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


	public void setModo(String modo) {
		if (modo!=null) {
			this.modo = modo.toUpperCase();
		} else {
			this.modo = "EDICION"; 
		}
	}


	public void setIdBoton(String modo) {
		if (modo!=null && !modo.equals("")) {
			this.idBoton = modo.toUpperCase();
		} else {
			this.idBoton = null; 
		}
	}


	public void setIdPersonaBA(String modo) {
		if (modo!=null && !modo.equals("")) {
			this.idPersonaBA = modo.toUpperCase();
		} else {
			this.idPersonaBA= null; 
		}
	}


	public void setIdInstitucionBA(String modo) {
		if (modo!=null && !modo.equals("")) {
			this.idInstitucionBA = modo.toUpperCase();
		} else {
			this.idInstitucionBA = null; 
		}
	}



	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public boolean isOrdenar() {
		return ordenar;
	}
	public void setOrdenar(boolean ordenar) {
		this.ordenar = ordenar;
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

			if (!this.valoresEdicion.contains(this.modo) && !this.valoresConsulta.contains(this.modo)){
				this.modo = "EDICION";
			}

			out.println("<!-- INICIO: BOTONES ACCIONES -->"); 

			// RGG 13/03/2007 cambio para que siempre coja el mismo estilo botones detalle
			if (this.clase==null || (this.clase!=null && this.clase.trim().equals(""))) {
				this.clase = "botonesDetalle"; 
			}

			//out.println("<div id=\"divBotones\" class=\""+this.clase+"\">");
			out.println("<table class=\""+this.clase+"\" align=\"center\">");
			out.println("<tr>");
			if (this.titulo!=null && !titulo.equals("")) {
				out.println("<td class=\"titulitos\">");
				out.println(UtilidadesString.getMensajeIdioma(usrbean, this.titulo));
				out.println("</td>");			
			}


			if (botonera.contains(ActionButtonsConstants.VOLVER)){
				printButton(usrbean, out, ActionButtonsConstants.VOLVER);
			}

			out.println("<td  style=\"width:900px;\">");
			out.println("&nbsp;");
			out.println("</td>");
			if(this.isOrdenar())
				Collections.sort(botonera, new ActionButtonComparator());

			for (ActionButtonsConstants abc: botonera){

				switch(abc){
				case VOLVER: break;
				
				case IMPRIMIR_APAISADO:
					if (tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && 
							this.valoresEdicion.contains(this.modo)) {  					
						out.println("<td class=\"tdBotones\">");
						out.println("<acronym title=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.aviso.imprimirApaisado")+"\" >");
						out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.aviso.imprimirApaisado") +"\"  id=\"idButton\" onclick=\"return accionImprimirApaisado();\" class=\"button\" value=\"");
						out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.imprimir"));
						out.println("\">");
						out.println("</acronym>"); 
						out.println("</td>");
					}
					break;
				
				case BA:
					// tratamiento de botones de acceso rápido
					// de momento solamente puede haber uno.
					if (this.idBoton!=null) {
						StringTokenizer st = new StringTokenizer(this.idBoton,"#");
						int contador = 0;

						AdmBotonAccionAdm admBot = new AdmBotonAccionAdm(usrbean);
						while (st.hasMoreElements()) {
							contador++;
							String bot=(String)st.nextElement();
							AdmBotonAccionBean beanBot = null;
							try {
								Vector v = admBot.select("WHERE IDBOTON="+bot+" AND IDINSTITUCION="+usrbean.getLocation());
								if (v!=null && v.size()>0) {
									beanBot = (AdmBotonAccionBean) v.get(0);
								}
								// SOLAMENTE SI ESTA ACTIVO Y EXISTE EL IDPERSONA
								if (beanBot!=null && beanBot.getActivo().equals("1") && 
										this.idInstitucionBA.equals(usrbean.getLocation()) &&
										tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) &&
										this.valoresEdicion.contains(this.modo)) {
									// Se pinta el formulario						

									out.println("<form id=\"ba"+contador+"\" name=\"DummyForm"+contador+"\" action=\"/SIGA/"+beanBot.getTransaccion()+"\" target=\"submitArea\" method=\"POST\" >");
									out.println("<input type=\"hidden\" name=\"modo\" value=\""+beanBot.getModo()+"\">");
									out.println("<input type=\"hidden\" name=\"actionModal\" value=\"\">");
									out.println("<input type=\"hidden\" name=\"modal\" value=\""+beanBot.getModal()+"\">");
									out.println("<input type=\"hidden\" name=\"idBoton\" value=\""+bot+"\">");
									out.println("<input type=\"hidden\" name=\"idPersona\" value=\""+this.idPersonaBA+"\">");
									out.println("<input type=\"hidden\" name=\"idInstitucion\" value=\""+this.idInstitucionBA+"\">");
									out.println("<input type=\"hidden\" name=\""+beanBot.getParametro()+"\" value=\""+beanBot.getValorParametro()+"\">");
									out.println("</form>");

									// Se pinta la funcion javascript
									out.println("<script languaje=\"javascript\"> <!--");
									out.println("function accionBA"+contador+"() {");
									out.println("  var form = document.getElementById(\"ba"+contador+"\");	");
									out.println("  form.modo.value=\""+beanBot.getModo()+"\";	");
									out.println("  form.modal.value=\""+beanBot.getModal()+"\";	");
									out.println("  form.idPersona.value=\""+this.idPersonaBA+"\";	");
									out.println("  form.idInstitucion.value=\""+this.idInstitucionBA+"\";	");
									out.println("  if (form.modal.value==\"1\") {");
									out.println("  	  var res"+contador+" = ventaModalGeneral(form.name,'P');");
									out.println("  	  if(res"+contador+" && res"+contador+"=='MODIFICADO') refrescarLocal();");
									out.println("  } else {");
									out.println("  	  form.submit();");
									out.println("  }");
									out.println("}");
									out.println("--> </script>");

									out.println("<td class=\"tdBotones\">");
									out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, beanBot.getNombreBoton()) +"\"  id=\"idButton\" onclick=\"return accionBA"+contador+"();\" class=\"button\" value=\"");
									out.print(UtilidadesString.getMensajeIdioma(usrbean,beanBot.getNombreBoton()));
									out.println("\">");
									out.println("</td>");
								}						
							} catch (Exception e) {
								ClsLogging.writeFileLogError("Error en tagBotonesAccion, obteniendo boton accion. ",e,3);
							}
						} // del while 
					}
					break;
				default: 
					if (tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && 
							this.valoresEdicion.contains(this.modo)) {  
						printButton(usrbean, out, abc);
					}
					break;
				}
			}


			out.println("</tr>");
			out.println("</table>");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return EVAL_BODY_INCLUDE;	 	 	
	}
	
	
	private void printButton(UsrBean usrbean, PrintWriter out, ActionButtonsConstants abc) {
		out.println("<td class=\"tdBotones\">");
		out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, abc.getLabel()) +"\"  id=\"" + abc.getIdBoton() +"\" onclick=\"return "+abc.getAccion()+";\" class=\"button\" name=\"idButton\" value=\"");
		out.print(UtilidadesString.getMensajeIdioma(usrbean,abc.getLabel()));
		out.println("\">");
		out.println("</td>");
	}
	

}
