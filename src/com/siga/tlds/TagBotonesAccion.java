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
import java.util.*;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.*;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.AdmBotonAccionAdm;
import com.siga.beans.AdmBotonAccionBean;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * Tag que trata la linea de botones de una barra de acciones sobre registro
 * @author AtosOrigin 10-12-04
 */

public class TagBotonesAccion extends TagSupport {

	/** claves de los botones a mostrar en el tag */
	String botones; 
	/** estilo aplicado a la capa que lo presenta */
	String clase; 
	String modal=""; 
	String modo="EDICION";
	private String titulo ="";
		
	private boolean volver;
	private boolean ba;
	private boolean guardarAnyadirHistorico;
	private boolean nuevo;
	private boolean cancelar;
	private boolean restablecer;
	private boolean guardar;
	private boolean guardarcerrar;
	private boolean cerrar;
	private boolean siguiente;
	private boolean finalizar;
	private boolean solicitar;
	private boolean solicitarNuevo;
	private boolean solicitarModificacion;
	private boolean marcarTodos;
	private boolean desmarcarTodos;
	private boolean procesarSolicitudes;
	private boolean procesarDevoluciones;
	private boolean facturacionRapida;
	private boolean denegarSolicitudes;
	private boolean generarCalendario;
	private boolean imprimir;
	private boolean imprimirApaisado;
	private boolean continuar;
	private boolean finalizarCompra;
	private boolean desglosePago;
	private boolean confirmaCompra;
	private boolean listaConsejo;
	private boolean ejecutaFacturacion;
	private boolean nuevaRegularizacion;
	private boolean cerrarPago;
	private boolean definirCriterio;
	private boolean generarImpreso190;
	private boolean download;
	private boolean aceptar;
	private boolean descargas;
	private boolean detallePago;
	private boolean detalleFacturacion;
	private boolean guardarejecutar;
	private boolean generarInforme;
	private boolean realizarPago;
	private boolean generarPDF;
	private boolean crearEJG;
	private boolean crearDesignacion;
	private boolean relacionarEJG;
	private boolean relacionarSOJ;
	private boolean relacionarDesignacion;
	private boolean anular;
	private boolean solicitudAsistencia;
	private boolean detalleLetrado;
	private boolean detalleConcepto;
	private boolean visualizarCriterios;
	private boolean nuevoLetrado;
	private boolean enviarSel;
	private boolean finalizarSel;
	private boolean bajaEnTodosLosTurnos;
	private boolean altaEnTurnosSel;
	private boolean generarFichero;
	private boolean generaXML;
	private boolean aniadirExpedientes;
	private boolean informeEnvios;
	private boolean logFacturacion;
	private boolean envioFTP;
	private boolean envioWS;
	private boolean respuestaFTP;
	private boolean resolucionFTP;
	
	
	private ArrayList valoresEdicion;  
	private ArrayList valoresConsulta;  
	private boolean generarPDFS;
	private String idBoton;
	private String idPersonaBA;
	private String idInstitucionBA;
	private boolean enviar;
	private boolean generarCarta;
	private boolean generarCerrar;
	private boolean informeRetencionIRPF;
	private boolean generarExcels;
	private boolean comunicar;
	

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
	 * @param dato - Contiene una lista de claves separadas por comas de los botones a mostrar:
	 * 				 <br>V Volver, G Guardar,Y GuardaryCerrar, GE GuardaryEjecutar, R Restablecer,N Nuevo,C Cerrar,X Cancelar,S Siguientesm,F Finalizar, L Solicitar, SN Solicitar Nuevo,
	 * 				 <br>MT Marcar Todos, DT Desmarcar Todos, PS Procesar Solicitud, DS Denegar Solicitud, I Imprimir, IA ImprimirApaisado, CT Continuar, FC Finalizar Compra, 
	 * 				 <br>D Download, A Aceptar
	 */
	public void setBotones(String dato) {
		try {

			cancelar=false;
			volver=false;
			ba=false;
			guardarAnyadirHistorico=false;
			idBoton=null;
			restablecer=false;
			guardar=false;
			guardarcerrar=false;
			cerrar=false;
			nuevo=false;
			siguiente=false;
			finalizar=false;
			solicitar=false;
			solicitarNuevo=false;
			solicitarModificacion=false;
			marcarTodos=false;
			desmarcarTodos=false;
			procesarSolicitudes=false;
			denegarSolicitudes=false;
			generarCalendario=false;
			imprimir=false;
			imprimirApaisado=false;
		    continuar=false;
		    finalizarCompra=false;
		    desglosePago=false;
			confirmaCompra=false;
			listaConsejo=false;
			ejecutaFacturacion=false;
			nuevaRegularizacion=false;
			cerrarPago=false;
			definirCriterio=false;
			generarImpreso190=false;
			download=false;
			aceptar=false;
			descargas=false;
			detallePago=false;
			detalleFacturacion=false;
			guardarejecutar=false;
			generarExcels=false;
			comunicar = false;
			generarInforme=false;
			realizarPago=false;
			generarPDF=false;
			crearEJG=false;
			crearDesignacion=false;
			relacionarEJG=false;
			relacionarSOJ=false;
			relacionarDesignacion=false;
			anular=false;
			solicitudAsistencia=false;
			detalleConcepto = false;
			detalleLetrado  = false;
			visualizarCriterios = false;
			nuevoLetrado = false;
			generarPDFS=false;
			enviarSel=false;
			finalizarSel=false;
			enviar=false;
			generarCarta = false;
			bajaEnTodosLosTurnos=false;
			altaEnTurnosSel=false;
			generarFichero=false;
			generaXML=false;
			aniadirExpedientes=false;
			generarCerrar = false;
			informeRetencionIRPF = false;
			informeEnvios = false;
			logFacturacion = false;
			envioFTP = false;
			envioWS = false;
			respuestaFTP = false;
			resolucionFTP = false;
			
			if (dato == null) dato = "";
			
			dato += ",";
			StringTokenizer datos = new StringTokenizer(dato, ",");
			for (int i = 0; datos.hasMoreElements(); i++) {
				String tipo = datos.nextToken();
				tipo = tipo.trim();
				if (tipo.equalsIgnoreCase("g")) { 
					guardar=true;
				}
				else if (tipo.equalsIgnoreCase("y")) {
					guardarcerrar=true;
				}else if (tipo.equalsIgnoreCase("ygc")) {
					generarCerrar=true;
				}
				else if (tipo.equalsIgnoreCase("ge")) {
					guardarejecutar=true;
				} 
				else if (tipo.equalsIgnoreCase("v")) {
					volver=true;
				} 
				else if (tipo.equalsIgnoreCase("ba")) {
					ba=true;
				} 
				else if (tipo.equalsIgnoreCase("gah")) {
					guardarAnyadirHistorico=true;
				} 
				else if (tipo.equalsIgnoreCase("r")) {
					restablecer=true;
				} 
				else if (tipo.equalsIgnoreCase("c")) {
					cerrar=true;
				} 
				else if (tipo.equalsIgnoreCase("x")) {
					cancelar=true;
				}
				else if (tipo.equalsIgnoreCase("n")) {
					nuevo=true;
				}
				else if (tipo.equalsIgnoreCase("ie")) {
				    informeEnvios=true;
				}
				
				else if (tipo.equalsIgnoreCase("s")) {
					siguiente=true;
				}
				else if (tipo.equalsIgnoreCase("f")) {
					finalizar=true;
				}
				else if (tipo.equalsIgnoreCase("l")) {
					solicitar=true;
				}
				else if (tipo.equalsIgnoreCase("sn")) {
					solicitarNuevo=true;
				}
				else if (tipo.equalsIgnoreCase("sm")) {
					solicitarModificacion=true;
				}
				else if (tipo.equalsIgnoreCase("mt")) {
					marcarTodos=true;
				}
				else if (tipo.equalsIgnoreCase("dt")) {
					desmarcarTodos=true;
				}
				else if (tipo.equalsIgnoreCase("ps")) {
					procesarSolicitudes=true;
				}
				else if (tipo.equalsIgnoreCase("pd")) {
					procesarDevoluciones=true;
				}
				else if (tipo.equalsIgnoreCase("ds")) {
					denegarSolicitudes=true;
				}
				else if (tipo.equalsIgnoreCase("gc")) {
					generarCalendario=true;
				}
				else if (tipo.equalsIgnoreCase("i")) {
					imprimir=true;
				}
				else if (tipo.equalsIgnoreCase("ia")) {
					imprimirApaisado=true;
				}
				else if (tipo.equalsIgnoreCase("fr")) {
					facturacionRapida=true;
				}
				else if (tipo.equalsIgnoreCase("ct")) {
					continuar=true;
				}
				else if (tipo.equalsIgnoreCase("fc")) {
					finalizarCompra=true;
				}
				else if (tipo.equalsIgnoreCase("dp")) {
					desglosePago=true;
				}
				else if (tipo.equalsIgnoreCase("cc")) {
					confirmaCompra=true;
				}
				else if (tipo.equalsIgnoreCase("lc")) {
					listaConsejo=true;
				}
				else if (tipo.equalsIgnoreCase("ef")) {
					ejecutaFacturacion=true;
				}
				else if (tipo.equalsIgnoreCase("nr")) {
					nuevaRegularizacion=true;
				}
				else if (tipo.equalsIgnoreCase("cp")) {
					cerrarPago=true;
				}
				else if (tipo.equalsIgnoreCase("dc")) {
					definirCriterio=true;
				}
				else if (tipo.equalsIgnoreCase("gi")) {
					generarImpreso190=true;
				}
				else if (tipo.equalsIgnoreCase("d")) {
					download=true;
				}
				else if (tipo.equalsIgnoreCase("a")) {
					aceptar=true;
				}
				else if (tipo.equalsIgnoreCase("de")) {
					descargas=true;
				}
				else if (tipo.equalsIgnoreCase("dpa")) {
					detallePago=true;
				}
				else if (tipo.equalsIgnoreCase("df")) {
					detalleFacturacion=true;
				}
				else if (tipo.equalsIgnoreCase("gx")) {
					generarExcels=true;
				}else if (tipo.equalsIgnoreCase("com")) {
					comunicar=true;
				}
				else if (tipo.equalsIgnoreCase("gm")) {
					generarInforme=true;
				}
				else if (tipo.equalsIgnoreCase("rp")) {
					realizarPago=true;
				}
				else if (tipo.equalsIgnoreCase("gp")) {
					generarPDF=true;
				}
				else if (tipo.equalsIgnoreCase("gps")) {
					generarPDFS=true;
				}
				else if (tipo.equalsIgnoreCase("ce")) {
					crearEJG=true;
				}
				else if (tipo.equalsIgnoreCase("cd")) {
					crearDesignacion=true;
				}
				else if (tipo.equalsIgnoreCase("re")) {
					relacionarEJG=true;
				}
				else if (tipo.equalsIgnoreCase("rs")) {
					relacionarSOJ=true;
				}
				else if (tipo.equalsIgnoreCase("rd")) {
					relacionarDesignacion=true;
				}
				else if (tipo.equalsIgnoreCase("an")) {
					anular=true;
				}
				else if (tipo.equalsIgnoreCase("sas")) { 
					solicitudAsistencia = true;
				}
				else if (tipo.equalsIgnoreCase("dLetrado")) {
					detalleLetrado=true;
				}
				else if (tipo.equalsIgnoreCase("dConcepto")) {
					detalleConcepto=true;
				}
				else if (tipo.equalsIgnoreCase("vc")) {
					visualizarCriterios=true;
				}
				else if (tipo.equalsIgnoreCase("es")) {
					enviarSel=true;
				}
				else if (tipo.equalsIgnoreCase("fs")) {
					finalizarSel=true;
				}
				else if (tipo.equalsIgnoreCase("nl")) {
					nuevoLetrado=true;
				}	
				else if (tipo.equalsIgnoreCase("en")) {
					enviar=true;
				}
				else if (tipo.equalsIgnoreCase("bajaEnTodosLosTurnos")) {
					bajaEnTodosLosTurnos=true;
				}else if (tipo.equalsIgnoreCase("altaEnTurnosSel")){
					
					altaEnTurnosSel=true;
			
				}else if (tipo.equalsIgnoreCase("cg")) {
					generarCarta=true;
				}else if (tipo.equalsIgnoreCase("ae")) {
					aniadirExpedientes=true;
				}else if (tipo.equalsIgnoreCase("gf")) {
					generarFichero=true;
				}else if (tipo.equalsIgnoreCase("gxml")){
					generaXML = true;
				}else if (tipo.equalsIgnoreCase("iri")) {
					informeRetencionIRPF=true;
				}else if (tipo.equalsIgnoreCase("lf")) {
				    logFacturacion=true;
				}else if (tipo.equalsIgnoreCase("ftp")){
					envioFTP = true;
				}else if (tipo.equalsIgnoreCase("ws")){
					envioWS = true;					
				}else if (tipo.equalsIgnoreCase("respFTP")){
					respuestaFTP = true;					
				}else if (tipo.equalsIgnoreCase("resolucionFTP")){
					resolucionFTP = true;					
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
		 * Da valor al atributo 
		 * @param dato 
		 */
			public void setIdBoton(String modo) {
				if (modo!=null && !modo.equals("")) {
					this.idBoton = modo.toUpperCase();
				} else {
					this.idBoton = null; 
				}
			}
			
			
		/**
		 * Da valor al atributo 
		 * @param dato 
		 */
			public void setIdPersonaBA(String modo) {
				if (modo!=null && !modo.equals("")) {
					this.idPersonaBA = modo.toUpperCase();
				} else {
					this.idPersonaBA= null; 
				}
			}
			
			
		/**
		 * Da valor al atributo 
		 * @param dato 
		 */
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
				
			if (!this.valoresEdicion.contains(this.modo) && !this.valoresConsulta.contains(this.modo)){
				this.modo = "EDICION";
			}
				 
			out.println("<!-- INICIO: BOTONES ACCIONES -->"); 
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
			// RGG 30-12-2004
			if (this.clase==null || (this.clase!=null && this.clase.trim().equals(""))) {
				this.clase = "tablaCentral"; 
			}
			*/
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
			if (volver) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.volver") +"\"  id=\"idButton\" onclick=\"return accionVolver();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.volver"));
				out.println("\">");
				out.println("</td>");
			}
			out.println("<td  style=\"width:900px;\">");
			out.println("&nbsp;");
			out.println("</td>");
			if (generarCalendario && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarCalendario") +"\"  id=\"idButton\" onclick=\"return accionGenerarCalendario();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.generarCalendario"));
				out.println("\">");
				out.println("</td>");
			}
			if (informeEnvios && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.informeEnvios") +"\"  id=\"idButton\" onclick=\"return accionInformeEnvios();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.informeEnvios"));
				out.println("\">");
				out.println("</td>");
			}
			if (nuevoLetrado && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "gratuita.modalCambioLetradoDesigna.titulo") +"\"  id=\"idButton\" onclick=\"return accionNuevoLetrado();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"gratuita.modalCambioLetradoDesigna.titulo"));
				out.println("\">");
				out.println("</td>");
			}
			if (crearEJG && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.crearEJG") +"\"  id=\"idButton\" onclick=\"return accionCrearEJG();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.crearEJG"));
				out.println("\">");
				out.println("</td>");
			}
			if (crearDesignacion && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.crearDesignacion") +"\"  id=\"idButton\" onclick=\"return accionCrearDesignacion();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.crearDesignacion"));
				out.println("\">");
				out.println("</td>");
			}
			if (relacionarEJG && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.relacionarEJG") +"\"  id=\"idButton\" onclick=\"return relacionarConEJG();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.relacionarEJG"));
				out.println("\">");
				out.println("</td>");
			}
			if (relacionarSOJ && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.relacionarSOJ") +"\"  id=\"idButton\" onclick=\"return relacionarConSOJ();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.relacionarSOJ"));
				out.println("\">");
				out.println("</td>");
			}
			if (relacionarDesignacion && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.relacionarDesigna") +"\"  id=\"idButton\" onclick=\"return relacionarConDesigna();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.relacionarDesigna"));
				out.println("\">");
				out.println("</td>");
			}
			if (solicitudAsistencia && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "gratuita.operarEJG.boton.solicitudAsistencia") +"\"  id=\"idButton\" onclick=\"return accionSolicitudAsistencia();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"gratuita.operarEJG.boton.solicitudAsistencia"));
				out.println("\">");
				out.println("</td>");
			}
			if (anular && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.anular") +"\"  id=\"idButton\" onclick=\"return accionAnular();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.anular"));
				out.println("\">");
				out.println("</td>");
			}
			if (nuevo && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.new") +"\"  id=\"idButton\" onclick=\"return accionNuevo();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.new"));
				out.println("\">");
				out.println("</td>");
			}
			if (realizarPago) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "facturacion.abonosPagos.boton.realizarPago") +"\"  id=\"idButton\" onclick=\"return accionRealizarPago();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"facturacion.abonosPagos.boton.realizarPago"));
				out.println("\">");
				out.println("</td>");
			}
			if (generarPDF) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarPDF") +"\"  id=\"idButton\" onclick=\"return accionGenerarPDF();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.generarPDF"));
				out.println("\">");
				out.println("</td>");
			}
			if (generarPDFS) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarPDFS") +"\"  id=\"idButton\" onclick=\"return accionGenerarPDFS();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.generarPDFS"));
				out.println("\">");
				out.println("</td>");
			}
			if (solicitar) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.solicitarTurno") +"\"  id=\"idButton\" onclick=\"return accionSolicitar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.solicitarTurno"));
				out.println("\">");
				out.println("</td>");
			}
			if (detallePago) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.detallePago") +"\"  id=\"idButton\" onclick=\"return accionDetallePago();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.detallePago"));
				out.println("\">");
				out.println("</td>");
			}
			if (detalleFacturacion) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.detalleFacturacion") +"\"  id=\"idButton\" onclick=\"return accionDetalleFacturacion();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.detalleFacturacion"));
				out.println("\">");
				out.println("</td>");
			}
			if (solicitarNuevo) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.solicitarNuevo") +"\"  id=\"idButton\" onclick=\"return accionSolicitarNuevo();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.solicitarNuevo"));
				out.println("\">");
				out.println("</td>");
			}
			if (solicitarModificacion) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.solicitarModificacion") +"\"  id=\"idButton\" onclick=\"return accionSolicitarModificacion();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.solicitarModificacion"));
				out.println("\">");
				out.println("</td>");
			}
			if (aceptar) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "global.boton.aceptar") +"\"  id=\"idButton\" onclick=\"return accionAceptar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "global.boton.aceptar"));
				out.println("\">");
				out.println("</td>");
			}
			if (download) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.download") +"\"  id=\"idButton\" onclick=\"return accionDownload();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.download"));
				out.println("\">");
				out.println("</td>");
			}
			if (descargas) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.descargar") +"\"  id=\"idButton\" onclick=\"return accionDescargas();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.descargar"));
				out.println("\">");
				out.println("</td>");
			}
			if (imprimir) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.imprimir") +"\"  id=\"idButton\" onclick=\"return accionImprimir();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.imprimir"));
				out.println("\">");
				out.println("</td>");
			}
			if (facturacionRapida) {
				out.println("<td class=\"tdBotones\">");
				out.println("<acronym title=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.aviso.facturacionRapida")+"\" >");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.aviso.facturacionRapida") +"\"  id=\"idButton\" onclick=\"return accionFacturacionRapida();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.aviso.facturacionRapida"));
				out.println("\">");
				out.println("</acronym>"); 
				out.println("</td>");
			}
			if (imprimirApaisado) {
				out.println("<td class=\"tdBotones\">");
				out.println("<acronym title=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.aviso.imprimirApaisado")+"\" >");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.aviso.imprimirApaisado") +"\"  id=\"idButton\" onclick=\"return accionImprimirApaisado();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.imprimir"));
				out.println("\">");
				out.println("</acronym>"); 
				out.println("</td>");
			}
			if (continuar) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.continuar") +"\"  id=\"idButton\" onclick=\"return accionContinuar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.continuar"));
				out.println("\">");
				out.println("</td>");
			}
			if (finalizarCompra) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.finalizarCompra") +"\"  id=\"idButton\" onclick=\"return accionfinalizarCompra();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.finalizarCompra"));
				out.println("\">");
				out.println("</td>");
			}
			if (desglosePago) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.desglosePago") +"\"  id=\"idButton\" onclick=\"return acciondesglosePago();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.desglosePago"));
				out.println("\">");
				out.println("</td>");
			}			
			if (marcarTodos) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.marcarTodos") +"\"  id=\"idButton\" onclick=\"return accionMarcarTodos();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.marcarTodos"));
				out.println("\">");
				out.println("</td>");
			}
			if (desmarcarTodos) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.cancel") +"\"  id=\"idButton\" onclick=\"return accionDesmarcarTodos();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.desmarcarTodos"));
				out.println("\">");
				out.println("</td>");
			}
			if (enviarSel) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.enviarSel") +"\"  id=\"idButton\" onclick=\"return accionEnviarSel();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.enviarSel"));
				out.println("\">");
				out.println("</td>");
			}

			if (finalizarSel) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.finalizarSel") +"\"  id=\"idButton\" onclick=\"return accionFinalizarSel();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.finalizarSel"));
				out.println("\">");
				out.println("</td>");
			}
			if (procesarSolicitudes && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.procesarSolicitud") +"\"  id=\"idButton\" onclick=\"return accionProcesarSolicitud();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.procesarSolicitud"));
				out.println("\">");
				out.println("</td>");
			}
			if (procesarDevoluciones && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.procesarDevoluciones") +"\"  id=\"idButton\" onclick=\"return accionProcesarDevoluciones();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.procesarDevoluciones"));
				out.println("\">");
				out.println("</td>");
			}
			if (denegarSolicitudes && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.denegarSolicitud") +"\"  id=\"idButton\" onclick=\"return accionDenegarSolicitud();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.denegarSolicitud"));
				out.println("\">");
				out.println("</td>");
			}
			if (confirmaCompra && this.valoresEdicion.contains(this.modo)) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.confirmarCompra") +"\"  id=\"idButton\" onclick=\"return accionConfirmarCompra();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.confirmarCompra"));
				out.println("\">");
				out.println("</td>");
			}
			if (listaConsejo && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.listaParaConsejo") +"\"  id=\"idButton\" onclick=\"return accionListaConsejo();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.listaParaConsejo"));
				out.println("\">");
				out.println("</td>");
			}
			if (ejecutaFacturacion && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.ejecutarFacturacion") +"\"  id=\"idButton\" onclick=\"return accionEjecutaFacturacion();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.ejecutarFacturacion"));
				out.println("\">");
				out.println("</td>");
			}
			if (nuevaRegularizacion && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.nuevaRegularizacion") +"\"  id=\"idButton\" onclick=\"return accionNuevaRegularizacion();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.nuevaRegularizacion"));
				out.println("\">");
				out.println("</td>");
			}
			if (cerrarPago && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.cerrarPago") +"\"  id=\"idButton\" onclick=\"return accionCerrarPago();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.cerrarPago"));
				out.println("\">");
				out.println("</td>");
			}
			if (visualizarCriterios && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.visualizarCriterio") +"\"  id=\"idButton\" onclick=\"return accionVisualizarCriterios();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.visualizarCriterio"));
				out.println("\">");
				out.println("</td>");
			}
			if (definirCriterio && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.definirCriterio") +"\"  id=\"idButton\" onclick=\"return accionDefinirCriterio();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.definirCriterio"));
				out.println("\">");
				out.println("</td>");
			}
			
			if (enviar) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.enviar") +"\"  id=\"idButton\" onclick=\"return accionEnviar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.enviar"));
				out.println("\">");
				out.println("</td>");
			}
			if (bajaEnTodosLosTurnos) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.bajaEnTodosLosTurnos") +"\"  id=\"idButton\" onclick=\"return darDeBajaEnTodosLosTurnos(true);\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.bajaEnTodosLosTurnos"));
				out.println("\">");
				out.println("</td>");
			}
			if (altaEnTurnosSel) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.altaEnTurnosSel") +"\"  id=\"idButton\" onclick=\"return darDeAltaEnTurnosSel(true);\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.altaEnTurnosSel"));
				out.println("\">");
				out.println("</td>");
			}
			if (ba) {
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
			}
			if (guardar && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)){  // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.guardar") +"\"  id=\"idButton\" onclick=\"return accionGuardar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.guardar"));
				out.println("\">");
				out.println("</td>");
			}
			if (generarImpreso190 && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarImpreso") +"\"  id=\"idButton\" onclick=\"return accionGenerarImpreso();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.generarImpreso"));
				out.println("\">");
				out.println("</td>");
			}
			if (guardarejecutar && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)){  // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.guardarEjecutar") +"\"  id=\"idButton\" onclick=\"return accionGuardarEjecutar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.guardarEjecutar"));
				out.println("\">");
				out.println("</td>");
			}
			if (guardarcerrar && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.guardarCerrar") +"\"  id=\"idButton\" onclick=\"return accionGuardarCerrar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.guardarCerrar"));
				out.println("\">");
				out.println("</td>");
			}
			if (guardarAnyadirHistorico && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.guardarAnyadirHistorico") +"\"  id=\"idButton\" onclick=\"return accionGuardarAnyadirHistorico();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.guardarAnyadirHistorico"));
				out.println("\">");
				out.println("</td>");
			}
			if (generarCerrar && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarCerrar") +"\"  id=\"idButton\" onclick=\"return accionGenerarCerrar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.generarCerrar"));
				out.println("\">");
				out.println("</td>");
			}
			if (informeRetencionIRPF) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.informeRetencionesIRPF") +"\"  id=\"idButton\" onclick=\"return accionInformeRetencionesIRPF();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.informeRetencionesIRPF"));
				out.println("\">");
				out.println("</td>");
			}
			if (restablecer && tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) { // Total 
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.restablecer") +"\"  id=\"idButton\" onclick=\"return accionRestablecer();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.restablecer"));
				out.println("\">");
				out.println("</td>");
			}
			if (siguiente) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.siguiente") +"\"  id=\"idButton\" onclick=\"return accionSiguiente();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.siguiente"));
				out.println("\">");
				out.println("</td>");
			}
			if (generarExcels) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarExcels") +"\"  id=\"idButton\" onclick=\"return accionGenerarExcels();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.generarExcels"));
				out.println("\">");
				out.println("</td>");
			}
			if (comunicar) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.comunicar") +"\"  id=\"idButton\" onclick=\"return accionComunicar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.comunicar"));
				out.println("\">");
				out.println("</td>");
			}
			if (generarInforme) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarInforme") +"\"  id=\"idButton\" onclick=\"return accionGenerarInforme();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.generarInforme"));
				out.println("\">");
				out.println("</td>");
			}
			if (detalleLetrado) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.detalleLetrado") +"\"  id=\"idButton\" onclick=\"return accionDetalleLetrado();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.detalleLetrado"));
				out.println("\">");
				out.println("</td>");
			}
			if (detalleConcepto) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.detalleConcepto") +"\"  id=\"idButton\" onclick=\"return accionDetalleConcepto();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.detalleConcepto"));
				out.println("\">");
				out.println("</td>");
			}
			if (finalizar) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.finalizar") +"\"  id=\"idButton\" onclick=\"return accionFinalizar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.finalizar"));
				out.println("\">");
				out.println("</td>");
			}
			if (cancelar) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.cancel") +"\"  id=\"idButton\" onclick=\"return accionCancelar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.cancel"));
				out.println("\">");
				out.println("</td>");
			}
			
			if (generarCarta&& tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL) && this.valoresEdicion.contains(this.modo)) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.cartaInteresados") +"\"  id=\"idButton\"  onclick=\"return generarCarta();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean, "gratuita.EJG.botonComunicaciones"));
				out.println("\">");
				out.println("</td>");
			}
			
			if (aniadirExpedientes) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.aniadirExpedientes") +"\"  id=\"idButtonAniadirExpedientes\" onclick=\"return aniadirExpedientes(true);\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.aniadirExpedientes"));
				out.println("\">");
				out.println("</td>");
			}
			if (generarFichero) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generarFichero") +"\"  id=\"idButton\" onclick=\"return generarFichero(true);\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.generarFichero"));
				out.println("\">");
				out.println("</td>");
			}
			if (generaXML) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.generaXML") +"\"  id=\"idButton\" onclick=\"return generaXML();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.generaXML"));
				out.println("\">");
				out.println("</td>");
			}
			if (logFacturacion) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.descargaLogFacturacion") +"\"  id=\"idButton\" onclick=\"return descargaLogFacturacion();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.descargaLogFacturacion"));
				out.println("\">");
				out.println("</td>");
			}
			if (cerrar) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.close") +"\"  id=\"idButton\" onclick=\"return accionCerrar();\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.close"));
				out.println("\">");
				out.println("</td>");
			}
			if (envioFTP) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.envioFTP") +"\"  id=\"idButtonEnvioFTP\" onclick=\"return envioFTP(this);\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.envioFTP"));
				out.println("\">");
				out.println("</td>");
			}
			if (envioWS) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.envioWS") +"\"  id=\"idButtonEnvioWS\" onclick=\"return envioWS(this);\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.envioWS"));
				out.println("\">");
				out.println("</td>");
			}
			if (respuestaFTP) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.respuestaFTP") +"\"  id=\"idButtonRespuestaFTP\" onclick=\"return respuestaFTP(this);\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.respuestaFTP"));
				out.println("\">");
				out.println("</td>");
			}
			if (resolucionFTP) {
				out.println("<td class=\"tdBotones\">");
				out.print("<input type=\"button\" alt=\""+UtilidadesString.getMensajeIdioma(usrbean, "general.boton.resolucionFTP") +"\"  id=\"idButtonResolucionFTP\" onclick=\"return resolucionFTP(this);\" class=\"button\" value=\"");
				out.print(UtilidadesString.getMensajeIdioma(usrbean,"general.boton.resolucionFTP"));
				out.println("\">");
				out.println("</td>");
			}
			

			out.println("</tr>");
			out.println("</table>");
			//out.println("</div>");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return EVAL_BODY_INCLUDE;	 	 	
	}
	
}
