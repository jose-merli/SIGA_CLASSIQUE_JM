/*
 * VESIONES:
 * Luis Miguel Sánchez PIÑA 04/01/2005 Las imágenes se estaban metiendo dentro de enlaces. Ahora ya no hay enlaces.
 * Miguel Angel Villegas 01/03/2005 Incorporacion atributo "pintarEspacio" que reserva o no espacios en blanco en el
 * 									lugar de los iconos característicos del TagTabla (consulta, edicion y borrado) 
 */
package com.siga.tlds;

import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.servlet.http.*;
import javax.servlet.jsp.tagext.TagSupport;
import com.atos.utils.*;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * @author daniel.campos
 */
public class TagFila extends TagSupport 
{
	private boolean botonesPintados = false; 
	
	static final String accConsultar = "consultar";
	static final String accEditar = "editar";
	static final String accBorrar = "borrar";


	Hashtable botones = new Hashtable();
	int fila;
	String clase;
	String modo = "EDICION" ;
	boolean visibleConsulta = true;
	boolean visibleEdicion = true;
	boolean visibleBorrado = true;
	boolean pintarEspacio = true;
	private ArrayList valoresEdicion;  
	private ArrayList valoresConsulta;  
	private String id;
	
	public void setBotones (String dato) 	
	{
		try {
			UtilidadesHash.set (botones, TagFila.accConsultar, 	"no");
			UtilidadesHash.set (botones, TagFila.accEditar, 	"no");
			UtilidadesHash.set (botones, TagFila.accBorrar, 	"no");

			if (dato == null) dato = "C,E,B";
			
			dato += ",";
			StringTokenizer datos = new StringTokenizer(dato, ",");
			for (int i = 0; datos.hasMoreElements(); i++) {
				String tipo = datos.nextToken();
				tipo = tipo.trim();
				if (tipo.equalsIgnoreCase("c")) { 
					UtilidadesHash.set (botones, TagFila.accConsultar, "si");
				}
				else if (tipo.equalsIgnoreCase("e")) {
					UtilidadesHash.set (botones, TagFila.accEditar, "si");
				} 
				else if (tipo.equalsIgnoreCase("b")) {
					UtilidadesHash.set (botones, TagFila.accBorrar, "si");
				}
			}
			this.botonesPintados = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void setFila (String dato) { 	this.fila = Integer.parseInt(dato); }
	public void setClase(String dato) 	{ this.clase	= dato;	} // No se usa la clase
	
	public void setModo(String modo) 	{ 
		if (modo!=null) {
			this.modo = modo.toUpperCase();
		} else {
			this.modo = "EDICION"; 
		}
	}

	public void setVisibleConsulta(String visible) 	{ 
		this.visibleConsulta = UtilidadesString.stringToBoolean(visible);
	}

	public void setVisibleEdicion(String visible) 	{ 
		this.visibleEdicion = UtilidadesString.stringToBoolean(visible);	
	}

	public void setVisibleBorrado(String visible) 	{ 
		this.visibleBorrado= UtilidadesString.stringToBoolean(visible);
	}
	
	public void setPintarEspacio(String visible) 	{ 
		this.pintarEspacio= UtilidadesString.stringToBoolean(visible);
	}

	public int doStartTag() 
	{
		try {
			if (!this.botonesPintados) {
				UtilidadesHash.set (botones, TagFila.accConsultar, 	"si");
				UtilidadesHash.set (botones, TagFila.accEditar, 	"si");
				UtilidadesHash.set (botones, TagFila.accBorrar, 	"si");
			}
			pageContext.getResponse().setContentType("text/html");
			PrintWriter out = pageContext.getResponse().getWriter();
			
			this.clase = (this.fila%2==0?"filaTablaPar":"filaTablaImpar");
			
			out.println("	<tr class=\""+ this.clase+"\"" );
			if(this.id!=null && !this.id.equals("")){
				out.println(" id=\""+ this.id+"\"" );
			}
			out.println(">");
			
			
			
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
			pageContext.getResponse().setContentType("text/html");
			PrintWriter out = pageContext.getResponse().getWriter();
			out.println("<td name='celda' align=\"left\">");
			
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			HttpSession session = (HttpSession) request.getSession();
			UsrBean usrBean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
			String tipoAcceso = usrBean.getAccessType();
			String pathAplicacion = request.getContextPath();
			
			printLines(out,usrBean,tipoAcceso,pathAplicacion);
			
			out.println("</td>");
			out.println("</tr>");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return EVAL_PAGE; 			// continua la ejecucion de la pagina
	}


	protected void printLines(PrintWriter out,
			UsrBean usrBean,String tipoAcceso,String pathAplicacion) throws Exception {
		do {
			out.println("&nbsp;");
			if (tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_NONE)) { // Sin acceso
				pintaImagen (out, pathAplicacion, TagFila.accConsultar, false, usrBean);
				pintaImagen (out, pathAplicacion, TagFila.accEditar, false, usrBean);
				pintaImagen (out, pathAplicacion, TagFila.accBorrar, false, usrBean);
				break;
			}
			if (tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_DENY)) { // Denegado
				pintaImagen (out, pathAplicacion, TagFila.accConsultar, false, usrBean);
				pintaImagen (out, pathAplicacion, TagFila.accEditar, false, usrBean);
				pintaImagen (out, pathAplicacion, TagFila.accBorrar, false, usrBean);
				break;
			}
			if (tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_READ)) { // Solo Lectura
				if (UtilidadesHash.getString(this.botones, TagFila.accConsultar).equalsIgnoreCase("si")) {
					pintaImagen (out, pathAplicacion, TagFila.accConsultar, true, usrBean);
				}
				else {
					if (this.visibleConsulta) {
						pintaImagen (out, pathAplicacion, TagFila.accConsultar, false, usrBean);
					}
					else {
						if (this.pintarEspacio) {
							pintaEspacio (out, pathAplicacion);
						}	
					}
				}
				if (UtilidadesHash.getString(this.botones, TagFila.accEditar).equalsIgnoreCase("si")) {
					pintaImagen (out, pathAplicacion, TagFila.accEditar, false, usrBean);
				}
				else {
					if (this.visibleEdicion) { 
						pintaImagen (out, pathAplicacion, TagFila.accEditar, false, usrBean);
					}
					else {
						if (this.pintarEspacio) {
							pintaEspacio (out, pathAplicacion);
						}
					}
				}
				if (UtilidadesHash.getString(this.botones, TagFila.accBorrar).equalsIgnoreCase("si")) {
					pintaImagen (out, pathAplicacion, TagFila.accBorrar, false, usrBean);
				}
				else {
					if (this.visibleBorrado) { 
						pintaImagen (out, pathAplicacion, TagFila.accBorrar, false, usrBean);
					}
					else {
						if (this.pintarEspacio) {
							pintaEspacio (out, pathAplicacion);
						}
					}
				}		
				break;
			}
			if (tipoAcceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_FULL)) { // Total
				if (UtilidadesHash.getString(this.botones, TagFila.accConsultar).equalsIgnoreCase("si")) {
					pintaImagen (out, pathAplicacion, TagFila.accConsultar, true, usrBean);
				}
				else {
					if (this.visibleConsulta) {
						pintaImagen (out, pathAplicacion, TagFila.accConsultar, false, usrBean);
					}
					else {
						if (this.pintarEspacio) {
							pintaEspacio (out, pathAplicacion);
						}
					}
				}
				
				if (UtilidadesHash.getString(this.botones, TagFila.accEditar).equalsIgnoreCase("si")) {
					if (this.valoresEdicion.contains(this.modo)){
						pintaImagen (out, pathAplicacion, TagFila.accEditar, true, usrBean);
					}
					else { // modo = modoConsulta
						pintaImagen (out, pathAplicacion, TagFila.accEditar, false, usrBean);
					}
				}
				else {
					if (this.visibleEdicion) { 
						pintaImagen (out, pathAplicacion, TagFila.accEditar, false, usrBean);
					}
					else {
						if (this.pintarEspacio) {
							pintaEspacio (out, pathAplicacion);
						}
					}
				}
				
				if (UtilidadesHash.getString(this.botones, TagFila.accBorrar).equalsIgnoreCase("si")) {
					if (this.valoresEdicion.contains(this.modo)){
						pintaImagen (out, pathAplicacion, TagFila.accBorrar, true, usrBean);
					}
					else { // modo = modoConsulta
						pintaImagen (out, pathAplicacion, TagFila.accBorrar,false, usrBean);
					}
				}
				else {
					if (this.visibleBorrado) { 
						pintaImagen (out, pathAplicacion, TagFila.accBorrar, false, usrBean);
					}
					else {
						if (this.pintarEspacio) {
							pintaEspacio (out, pathAplicacion);
						}
					}
				}
				break;
			}
		} while (false);
		
	}
	
	void pintaImagen (PrintWriter out, String path, String accion, boolean permitido, UsrBean usrBean) 
	{
		String aux = "";
		
		if (permitido) {
			/* LMS (04/01/2005) */
		    /* En lugar de poner una imagen dentro de un enlace, se pone únicamente la imagen. */
		    /*aux = "<a href='javascript://' " +
				  "onClick=\"selectRow(" + this.fila + "); "+ accion + "(" + this.fila + ");\" " +
				  "onMouseOut=\"MM_swapImgRestore()\" " +
				  "onMouseOver=\"MM_swapImage('" + accion + "_" + this.fila + "','','" + path + "/html/imagenes/b" + accion + "_on.gif',1)\">";
	
			aux += "<img src=\"" + path + "/html/imagenes/b" + accion + "_off.gif\" " +
				  "alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, "general.boton." + accion) + "\" " +
				  "name=\"" + accion + "_" + this.fila + "\" " +
//				  "width='26' " +
//				  "height='27' " +
				  "border=\"0\"" +
				  ">";
			
			aux += "</a>";*/
	aux = "<img id=\"iconoboton_"+ accion + this.fila + "\" src=\"" + path + "/html/imagenes/b" + accion + "_off.gif\" " +
	  "style=\"cursor:hand;\" " +
			  "alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, "general.boton." + accion) + "\" " +
			  "name=\"" + accion + "_" + this.fila + "\" " +
			  //"name=\"iconoFila\" " +
//			  "width='26' " +
//			  "height='27' " +
			  "border=\"0\" " +
			  //"onClick=\"selectRow(" + this.fila + "); "+ accion + "(" + this.fila + "); parent.buscar();\" " +
			  //"onClick=\" deshabilitariconos('iconoFila');selectRow(" + this.fila + "); "+ accion + "(" + this.fila + ");habilitariconos('iconoFila'); \" " +
			  "onClick=\" selectRow(" + this.fila + "); "+ accion + "(" + this.fila + "); \" " +
			  "onMouseOut=\"MM_swapImgRestore()\" " +
			  "onMouseOver=\"MM_swapImage('" + accion + "_" + this.fila + "','','" + path + "/html/imagenes/b" + accion + "_on.gif',1)\">";
		
			out.println(aux);
		}
		else {
		 	aux = "<img id=\"iconoboton_"+ accion + this.fila + "\"  src=\"" + path + "/html/imagenes/b" + accion + "_disable.gif\" " +
	     		"alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, "general.boton." + accion) + "\" " +
				  "name=\"" + accion + "_" + this.fila + "\" " +
	     		//"name=\"iconoFila\" " +
//				  "width='26' " +
//				  "height='27' " +
				  "border=\"0\"" +
				  ">";
			out.println(aux);
		}
	}
	
	void pintaEspacio (PrintWriter out, String path) 
	{
		String aux = "";
		aux = "<img src=\"" + path + "/html/imagenes/bEspacio.gif\" " +
//		  "width='26' " +
//		  "height='27' " +
		  "border=\"0\"" +
		  ">";
	   out.println(aux);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
