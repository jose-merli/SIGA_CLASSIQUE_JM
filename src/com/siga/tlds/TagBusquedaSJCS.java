//VERSIONES
//raul.ggonzalez 10-12-2004 Creacion
//raul.ggonzalez 21-12-2004 cambio para que ante ninguna clave de boton no se muestre mas que la barra
//Luis Miguel Sánchez PIÑA - 12/04/2005 Se anhade el botón "Generar PDF".
//raul.ggonzalez 24-01-2006 cambio boton anhadir poblaciones
//

package com.siga.tlds;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;

/**
 * Tag que trata la linea de botones de una barra de busqueda 
 * @author AtosOrigin 10-12-04
 */

public class TagBusquedaSJCS extends TagSupport {

	/**
	 * Botones que se van a mostrar en el tag [Automatica|Manual] <br>Por defecto aparecen ambos
	 */
	private boolean automatica=true;
	private boolean manual=true;
	private boolean art27=false;
	
	/**
	 * Para que concepto se implementan las busquedas:<br>
	 * [Designacion|Guardia|Asistencia|EJG|SOJ] <br> (OBLIGATORIO)
	 */
	private String concepto; 
	private static ArrayList valoresConcepto;
	
	/**
	 * Para que operaion se realiza la busqueda:<br>
	 * [Sustitucion|Asignacion] <br> (OBLIGATORIO)
	 */
	private String operacion;
	private static ArrayList valoresOperacion;
	
	/**
	 * Nombre del formulario <br> (OBLIGATORIO)
	 */
	private String nombre;
	
	/**
	 * Identificador del componente HTML
	 */
	private String propiedad;
	
	/**
	 * indica si se muestra el combo con la gente de guardia el día de la fecha
	 */
	private boolean diaGuardia;
	
	/**
	 * indica el modo
	 */
	private String modo;
	
	/**
	 * indica la idpersona seleccionada (modo edicion)
	 */
	private String idPersona;
	
	/**
	 * Nombre del campo Turno en el formulario <br> (OBLIGATORIO)
	 */
	private String campoTurno;
	
	/**
	 * Nombre del campo Guardia en el formulario <br> (OBLIGATORIO)
	 */
	private String campoGuardia;
	
	/**
	 * Nombre del campo Colegiado en el formulario <br> (OBLIGATORIO solo si no es designacion)
	 */
	private String campoColegiado;
	

	private String campoColegio;
	/**
	 * Nombre del campo Persona en el formulario
	 */
	private String campoPersona;
	
	/**
	 * Nombre del campo Fecha en el formulario <br> (OBLIGATORIO solo si no es designación)
	 */
	private String campoFecha;
	
	/**
	 * Si se muestra o no el nombre del colegiado <br> Por defecto se muestra
	 */
	private boolean mostrarNombreColegiado=true;
	
	/**
	 * Si se muestra o no el número del colegiado <br> Por defecto se muestra
	 */
	private boolean mostrarNColegiado=true;
	
	private String campoNombreColegiado="";
	
	/**
	 * Nombre del campo "requiere salto" en el formulario <br> (OBLIGATORIO)
	 */
	private String campoSalto;
	
	/**
	 * Nombre del campo "requiere compensación" en el formulario <br> (OBLIGATORIO)
	 */
	private String campoCompensacion;
	
	/**
	 * Nombre del campo flag salto en el formulario <br> (OBLIGATORIO)
	 */
	private String campoFlagSalto;
	
	/**
	 * Nombre del campo flag compensacion en el formulario <br> (OBLIGATORIO)
	 */
	private String campoFlagCompensacion;
	
	private boolean disabled;
	private boolean eliminarSeleccionado = true;
	
	/**
	 * Nombre del campo flag recurso en el formulario <br> (OBLIGATORIO)
	 */
	//private String recurso;
	
	

	/**
	 * Da valor al atributo 
	 * @author cristina.santos. 07-03-06
	 * @param dato - Contiene una lista de claves separados por comas de los botones a mostrar:
	 * 				 <br>A Automatica, M Manual
	 */
	public void setBotones(String dato) {
		try {
			automatica=false;
			manual=false;
			art27=false;
			
			if (dato != null){
				dato += ",";
				StringTokenizer datos = new StringTokenizer(dato, ",");
				for (int i = 0; datos.hasMoreElements(); i++) {
					String tipo = datos.nextToken();
					tipo = tipo.trim();
					if (tipo.equalsIgnoreCase("a")) { 
						automatica=true;
					}else if (tipo.equalsIgnoreCase("m")) {
						manual=true;
					}else if (tipo.equalsIgnoreCase("d")) {
						art27=true;
					}  
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Da valor al atributo 
	 * @author cristina.santos. 07-03-06
	 * @param dato 
	 */
	public void setConcepto(String dato) {
		this.concepto = dato.toUpperCase();
	}
	
	/**
	 * Da valor al atributo 
	 * @author cristina.santos. 07-03-06
	 * @param dato 
	 */
	public void setOperacion(String dato) {
		this.operacion = dato.toUpperCase();
	}
	
	/**
	 * Da valor al atributo 
	 * @author cristina.santos. 07-03-06
	 * @param dato 
	 */
	public void setNombre(String dato) {
		this.nombre = dato;
	}
	
	/**
	 * Da valor al atributo 
	 * @author cristina.santos. 07-03-06
	 * @param dato 
	 */
	public void setPropiedad(String dato) {
		this.propiedad = dato;
	}
	
	/**
	 * Da valor al atributo 
	 * @author cristina.santos. 07-03-06
	 * @param dato 
	 */
	public void setCampoTurno(String dato) {
		this.campoTurno = dato;
	}
	
	/**
	 * Da valor al atributo 
	 * @author cristina.santos. 07-03-06
	 * @param dato 
	 */
	public void setCampoGuardia(String dato) {
		this.campoGuardia = dato;
	}
	
	/**
	 * Da valor al atributo 
	 * @author 
	 * @param dato 
	 */
	public void setDiaGuardia(String dato) {
		this.diaGuardia = UtilidadesString.stringToBoolean(dato);
	}
	
	public void setModo(String dato) {
		this.modo = dato;
	}
	
	public void setCampoNombreColegiado(String dato) {
		this.campoNombreColegiado = dato;
	}
	
	public void setIdPersona(String dato) {
		this.idPersona = dato;
	}

	/**
	 * Da valor al atributo 
	 * @author cristina.santos. 07-03-06
	 * @param dato 
	 */
	public void setCampoColegiado(String dato) {
		this.campoColegiado = dato;
	}

	public void setCampoColegio(String dato) {
		this.campoColegio = dato;
	}
	/**
	 * Da valor al atributo 
	 * @author cristina.santos. 07-03-06
	 * @param dato 
	 */
	public void setCampoPersona(String dato) {
		this.campoPersona = dato;
	}

	/**
	 * Da valor al atributo 
	 * @author cristina.santos. 07-03-06
	 * @param dato 
	 */
	public void setDisabled(String dato) {
		this.disabled =UtilidadesString.stringToBoolean(dato); 
	}

	/**
	 * Da valor al atributo 
	 * @author raul.ggonzalez. 17-03-06
	 * @param dato 
	 */
	public void setCampoFecha(String dato) {
		this.campoFecha = dato;
	}
	
	/**
	 * Da valor al atributo 
	 * @author raul.ggonzalez. 17-03-06
	 * @param dato 
	 */
	public void setCampoSalto(String dato) {
		this.campoSalto = dato;
	}
	
	/**
	 * Da valor al atributo 
	 * @author raul.ggonzalez. 17-03-06
	 * @param dato 
	 */
	public void setCampoCompensacion(String dato) {
		this.campoCompensacion = dato;
	}
	
	/**
	 * Da valor al atributo 
	 * @author raul.ggonzalez. 17-03-06
	 * @param dato 
	 */
	public void setCampoFlagSalto(String dato) {
		this.campoFlagSalto = dato;
	}
	
	/**
	 * Da valor al atributo 
	 * @author raul.ggonzalez. 17-03-06
	 * @param dato 
	 */
	public void setCampoFlagCompensacion(String dato) {
		this.campoFlagCompensacion = dato;
	}
	
	/**
	 * Da valor al atributo 
	 * @author raul.ggonzalez. 17-03-06
	 * @param dato 
	 */
	public void setMostrarNColegiado(String dato) {
		this.mostrarNColegiado = UtilidadesString.stringToBoolean(dato);
	}
	
	/**
	 * Da valor al atributo 
	 * @author raul.ggonzalez. 17-03-06
	 * @param dato 
	 */
	public void setMostrarNombreColegiado(String dato) {
		this.mostrarNombreColegiado = UtilidadesString.stringToBoolean(dato);
	}
	
	/*public void setRecurso(String dato){
		this.recurso=dato;
	}*/
	
	/**
	 * Inicializador estatico
	 */	
	static{
		//valores permitidos para el Concepto
		valoresConcepto= new ArrayList();
		valoresConcepto.add("DESIGNACION");
		valoresConcepto.add("GUARDIA");
		valoresConcepto.add("ASISTENCIA");
		valoresConcepto.add("EJG");
		valoresConcepto.add("SOJ");
		valoresConcepto.add("SALTOSCOMP");
		
		//valores permitidos para el parametro Operacion
		valoresOperacion= new ArrayList();
		valoresOperacion.add("SUSTITUCION");
		valoresOperacion.add("ASIGNACION");
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
			PrintWriter out = pageContext.getResponse().getWriter();
			doCheckTag(out);
	
			out.println("	<!-- BUSQUEDA SJCS -->");
			//out.println("<fieldset class=\"labelTextDentro\"");
			//if(propiedad!=null) out.print("id=\""+propiedad+"\"");
			//if(disabled) out.print("disabled");
			//out.println(">");
			//out.println("<legend  class=\"labelTextDentro\" align=\"left\">");
			//if (recurso!=null){
			//out.println(UtilidadesString.getMensajeIdioma(usrbean, recurso));
			//}else{
			//out.println(UtilidadesString.getMensajeIdioma(usrbean, "gratuita.seleccionColegiadoJG.literal.titulo"));	
			//}
			//out.println("</legend>");
			
			this.addJavaScript(out,usrbean);
			
			// busco el idpersona si existe
			
			/*
			String nomLetrado="";
			String ncolLetrado="";
			if (this.idPersona!=null && !this.idPersona.equals("")) {
			    CenPersonaAdm admPer = new CenPersonaAdm(usrbean);  
			    Vector v = admPer.getDatosPersonaTag(usrbean.getLocation(),this.idPersona);
			    if (v!=null && v.size()>0) {
			        Hashtable ht = (Hashtable) v.get(0);
			        nomLetrado = (String) ht.get("NOMBRE");
			        ncolLetrado = (String) ht.get("NCOLEGIADO");
			    }
			    
			}
			*/
			
/*			
			out.println("<table align=\"center\" width=\"100%\">");
			out.println("<tr> ");

			out.println("<td class=\"labelText\" >");
			out.println(UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaSOJ.literal.nombre"));
			out.println("</td>");
			
			out.println("<td class='labelTextValue' >");
			if (this.mostrarNombreColegiado) { 
			    //out.println(UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaSOJ.literal.nombre"));
				out.println("<input type=text name=\"nombreMostradoSJCS\" size=\"30\" maxlength=\"150\" class=\"boxConsulta\" value=\"\" readOnly=\"true\">");
			}
			out.println("</td>");
			out.println("<td class='labelText' >");

			if (this.mostrarNColegiado) { 
				out.println(UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaSOJ.literal.numeroColegidado"));
				out.println("</td>");
				out.println("<td class='labelTextValue' >");
				out.println("<input type=text name=\"colegiadoSJCS\" size=\"10\" maxlength=\"100\" class=\"boxConsulta\" value=\"\" readOnly=\"true\">");
			}
			
			out.println("</td>");
			out.println("</tr>");  
			out.println("</table>");			
*/			

			
			if (!this.modo.equalsIgnoreCase("ver")) {
			    
				out.println("<table align=\"center\" width=\"100%\" border=\"0\">");
				out.println("<tr> ");
				// RGG 12/03/2008 combo de letrados de guardia
				if (this.diaGuardia) {
					//out.println("<tr>");  
					out.println("<td class='labelText' width='200'>"); 
					out.println(UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaSJCS.literal.letradosGuardia"));
					out.println("</td>");  
					out.println("<td  class='labelTextValue' align=\"left\">"); 
					out.println("<select  id=\"comboDiaGuardia\" onafterupdate=\"cargarPrimero(this);\"  onChange=\"seleccionarDatosCombo(this);\" name= \"letradoDiaGuardia\" value=\"\" class=\"boxCombo\" > ");
					out.println("<option value=\"\"></option> ");
					out.println("</select> ");
					out.println("</td>");  
					//out.println("</tr>"); 			
				} else {
					out.println("<td class='labelText' colspan=2>"); 
					out.println("&nbsp;");
					out.println("</td>");  
					out.println("<td>"); 
					out.println("&nbsp;");
					out.println("</td>");  
				}

				if (manual) {
					String msg=UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaSJCS.literal.manual");
					out.println("<td align=\"right\">");
					out.print("<input");
					out.print(" type=\"button\" id =\"idButton\" name =\"idButton\" class=\"button\" value=\""+msg+"\"");
					out.print(" onclick=\"buscarMan();\">");
					out.println("</td>");
				}
				
				if (art27) {
					String msg=UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaSJCS.literal.art27");
					out.println("<td align=\"center\">");
					out.print("<input");
					out.print(" type=\"button\" id =\"idButton\" name =\"idButton\" class=\"button\" value=\""+msg+"\"");
					out.print(" onclick=\"buscarDesig27();\">");
					out.println("</td>");
				}				
				
				if (automatica  && !diaGuardia) {
					String msg=UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaSJCS.literal.automatico");
					out.println("<td align=\"right\">");
					out.print("<input");
					out.print(" type=\"button\" id =\"idButton\" name =\"idButton\" class=\"button\" value=\""+msg+"\"");
					out.print(" onclick=\"buscarAut();\">");
					out.println("</td>");
				}
				
				if (eliminarSeleccionado ) {
					String msg=UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaSJCS.literal.limpiar");
					out.println("<td align=\"right\">");
					out.print("<input");
					out.print(" type=\"button\" name =\"idButton\" class=\"button\" id =\"idButton\" value=\""+msg+"\"");
					out.print(" onclick=\"eliminaLetradoSeleccionado();\">");
					out.println("</td>");
				}
				out.println("</tr>");  
				
				
				// Para pintar los check de salto o compensación (si procede) 
				out.println("<tr><td colspan=5>");  
				out.println("<table style=\"width:100%\"><tr>");  
				out.println("<td id='tdCheckSalto' class='labelText' style='display:none;'>"); 
				out.println("<div id=\"mensalto\" style=\"display:inline\">"+UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaSJCS.literal.incluirSalto")+"</div>");
				out.println(" <input type='Checkbox' id='"+campoSalto+"' name='"+campoSalto+"'>");  
				out.println("</td>"); 
	
				out.println("<td id='tdCheckCompensacion' class='labelText' colspan='2' style='visibility:hidden'>"); 
				out.println(UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaSJCS.literal.incluirCompensacion") + " " + UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaSJCS.literal.incluirCompensacionLetradoSaliente"));
				out.println(" <input type='Checkbox' id='"+campoCompensacion+"' name='"+campoCompensacion+"'>");  
				out.println("</td>");  
				out.println("</tr>");  
				out.println("</table>");
				out.println("</td></tr>");  
				out.println("</table>");
			}
			// FIN
			//out.println("</fieldset>");

		}catch (Exception e){
			e.printStackTrace();
		}

		return EVAL_BODY_INCLUDE;	 	 	
	}
	
	protected void doCheckTag(PrintWriter out) throws ClsExceptions{
		try {
			if(!valoresConcepto.contains(concepto)){//ejg...
					throw new ClsExceptions("El valor del atributo 'concepto' no es válido");
				}
				if(!valoresOperacion.contains(operacion)){//sus-asig
					throw new ClsExceptions("El valor del atributo 'operacion' no es válido");
				}
				if(!concepto.equals("DESIGNACION")){
					if(campoGuardia==null){
						throw new ClsExceptions("El atributo 'campoGuardia' es obligatorio cuando el concepto es '"+concepto+"'");
					}
					if(campoFecha==null){
						throw new ClsExceptions("El atributo 'campoFecha' es obligatorio cuando el concepto es '"+concepto+"'");
					}
				}else{
					if(campoFecha==null){
						throw new ClsExceptions("El atributo 'campoFecha' es obligatorio cuando el concepto es '"+concepto+"'");
					}
				}
				if(concepto.equals("GUARDIA")){
					if(!manual){
						throw new ClsExceptions("El botón 'manual' es obligatorio cuando el concepto es '"+concepto+"'");
					}
					automatica=false;
				}
				if(!manual && !automatica && !art27){
					throw new ClsExceptions("El valor del atributo 'botones' no es válido");
				}
		} catch (ClsExceptions e) {
			out.println(e.getMsg());
			out.println(e.getMessage());
			throw e;
		}
	}
	
	protected void addJavaScript(PrintWriter out,UsrBean usrbean){
		out.println("<script language=\"JavaScript\">");
		
		//Busca un elemento submitArea y si no existe lo crea
		out.println("");
		out.println("function creaSubmitArea() {");
		out.println("	var vSubmitArea=document.getElementById('submitArea');");
		out.println("	if(vSubmitArea==null){");
		out.println("		var formu=document.createElement(\"	<iframe name='submitArea' style='display:none'></iframe>\");");
		out.println("	}");
		out.println("}");

		//Busca el formulario que se va a enviar y si no existe lo crea
		out.println("");
		out.println("function creaForm() {");
		out.println("	var vForm=document.forms['busquedaClientesFiltrosForm'];");
		out.println("	if(vForm==null){");
		out.println("		var app="+nombre+".action;");
		out.println("		app=app.substring(0,app.substr(1).indexOf('/')+1);");
		out.println("		");
		out.println("		var formu=document.createElement('form');");
		out.println("		formu.setAttribute('name','busquedaClientesFiltrosForm');");			
		out.println("		formu.setAttribute('id','busquedaClientesFiltrosForm');");
		String contextPath = ((HttpServletRequest)pageContext.getRequest()).getContextPath();
		out.println("		formu.setAttribute('action', '"+contextPath+"/JGR_BusquedaClientesFiltros.do');");
		out.println("");			
		out.println("				var myinput =document.createElement('input');");			
		out.println("				myinput.setAttribute('type','hidden');");
		out.println("				myinput.setAttribute('id','actionModal');");			
		out.println("				myinput.setAttribute('name','actionModal');");
		out.println("				myinput.setAttribute('value','"+contextPath+"/JGR_BusquedaClientesFiltros.do');");		
		out.println("				formu.appendChild(myinput);");
		out.println("");			
		out.println("				var myinput2 =document.createElement('input');");			
		out.println("				myinput2.setAttribute('type','hidden');");
		out.println("				myinput2.setAttribute('id','modo');");			
		out.println("				myinput2.setAttribute('name','modo');");
		out.println("				myinput2.setAttribute('value','');");		
		out.println("				formu.appendChild(myinput2);");	
		out.println("");			
		out.println("				var myinput3 =document.createElement('input');");			
		out.println("				myinput3.setAttribute('type','hidden');");
		out.println("				myinput3.setAttribute('id','concepto');");			
		out.println("				myinput3.setAttribute('name','concepto');");
		out.println("				myinput3.setAttribute('value','');");		
		out.println("				formu.appendChild(myinput3);");
		out.println("");			
		out.println("				var myinput4 =document.createElement('input');");			
		out.println("				myinput4.setAttribute('type','hidden');");
		out.println("				myinput4.setAttribute('id','operacion');");			
		out.println("				myinput4.setAttribute('name','operacion');");
		out.println("				myinput4.setAttribute('value','');");		
		out.println("				formu.appendChild(myinput4);");
		out.println("");			
		out.println("				var myinput5 =document.createElement('input');");			
		out.println("				myinput5.setAttribute('type','hidden');");
		out.println("				myinput5.setAttribute('id','idTurno');");			
		out.println("				myinput5.setAttribute('name','idTurno');");
		out.println("				myinput5.setAttribute('value','');");		
		out.println("				formu.appendChild(myinput5);");
		out.println("");			
		out.println("				var myinput6 =document.createElement('input');");			
		out.println("				myinput6.setAttribute('type','hidden');");
		out.println("				myinput6.setAttribute('id','idGuardia');");			
		out.println("				myinput6.setAttribute('name','idGuardia');");
		out.println("				myinput6.setAttribute('value','');");		
		out.println("				formu.appendChild(myinput6);");
		out.println("");			
		out.println("				var myinput7 =document.createElement('input');");			
		out.println("				myinput7.setAttribute('type','hidden');");
		out.println("				myinput7.setAttribute('id','fecha');");			
		out.println("				myinput7.setAttribute('name','fecha');");
		out.println("				myinput7.setAttribute('value','');");		
		out.println("				formu.appendChild(myinput7);");
		out.println("");					
		out.println("				document.body.appendChild(formu);");			
		out.println("		vForm=formu;");
		out.println("	}");
		out.println("	return vForm;");
		out.println("}");
		
		//Busca el formulario que se va a enviar y si no existe lo crea
		out.println("");
		out.println("function creaFormArticulo27() {");
		out.println("	var vForm=document.forms['BusquedaClientesForm'];");
		out.println("	if(vForm==null){");
		out.println("		var app="+nombre+".action;");
		out.println("		app=app.substring(0,app.substr(1).indexOf('/')+1);");
		out.println("		var formu=document.createElement('form');");
		out.println("		formu.setAttribute('name','busquedaCensoModalForm');");			
		out.println("		formu.setAttribute('id','busquedaCensoModalForm');");
		out.println("		formu.setAttribute('action', '"+contextPath+"/CEN_BusquedaCensoModal.do');");
		out.println("");			
		out.println("				var myinput =document.createElement('input');");			
		out.println("				myinput.setAttribute('type','hidden');");
		out.println("				myinput.setAttribute('id','actionModal');");			
		out.println("				myinput.setAttribute('name','actionModal');");
		out.println("				myinput.setAttribute('value','"+contextPath+"/CEN_BusquedaCensoModal.do');");		
		out.println("				formu.appendChild(myinput);");
		out.println("");			
		out.println("				var myinput2 =document.createElement('input');");			
		out.println("				myinput2.setAttribute('type','hidden');");
		out.println("				myinput2.setAttribute('id','modo');");			
		out.println("				myinput2.setAttribute('name','modo');");
		out.println("				myinput2.setAttribute('value','');");		
		out.println("				formu.appendChild(myinput2);");	
		out.println("");			
		out.println("				var myinput3 =document.createElement('input');");			
		out.println("				myinput3.setAttribute('type','hidden');");
		out.println("				myinput3.setAttribute('id','concepto');");			
		out.println("				myinput3.setAttribute('name','concepto');");
		out.println("				myinput3.setAttribute('value','');");		
		out.println("				formu.appendChild(myinput3);");
		out.println("");			
		out.println("				var myinput4 =document.createElement('input');");			
		out.println("				myinput4.setAttribute('type','hidden');");
		out.println("				myinput4.setAttribute('id','operacion');");			
		out.println("				myinput4.setAttribute('name','operacion');");
		out.println("				myinput4.setAttribute('value','');");		
		out.println("				formu.appendChild(myinput4);");
		out.println("");			
		out.println("				var myinput5 =document.createElement('input');");			
		out.println("				myinput5.setAttribute('type','hidden');");
		out.println("				myinput5.setAttribute('id','idTurno');");			
		out.println("				myinput5.setAttribute('name','idTurno');");
		out.println("				myinput5.setAttribute('value','');");		
		out.println("				formu.appendChild(myinput5);");
		out.println("");			
		out.println("				var myinput6 =document.createElement('input');");			
		out.println("				myinput6.setAttribute('type','hidden');");
		out.println("				myinput6.setAttribute('id','idGuardia');");			
		out.println("				myinput6.setAttribute('name','idGuardia');");
		out.println("				myinput6.setAttribute('value','');");		
		out.println("				formu.appendChild(myinput6);");
		out.println("");			
		out.println("				var myinput7 =document.createElement('input');");			
		out.println("				myinput7.setAttribute('type','hidden');");
		out.println("				myinput7.setAttribute('id','fecha');");			
		out.println("				myinput7.setAttribute('name','fecha');");
		out.println("				myinput7.setAttribute('value','');");		
		out.println("				formu.appendChild(myinput7);");
		out.println("");					
		out.println("				document.body.appendChild(formu);");			
		out.println("		vForm=formu;");
		out.println("	}");
		out.println("	return vForm;");
		out.println("}");		

		//Valida que se han introducido valores en los campos obligatorios
		out.println("");
		out.println("function validaForm() {");
		//gratuita.nuevaAsistencia.mensaje.alert1 turno??
		//gratuita.nuevaAsistencia.mensaje.alert4 guradia??
		
		String msg2=UtilidadesString.getMensajeIdioma(usrbean, "messages.campoObligatorio.error");
		String msg1=UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaEJG.literal.turno");
		out.println("	if("+nombre+"."+campoTurno+".value==null || "+nombre+"."+campoTurno+".value==''|| "+nombre+"."+campoTurno+".value=='-1'){ alert('"+msg1+" "+msg2+"'); return false;}");
		if(!concepto.equalsIgnoreCase("DESIGNACION")&&!concepto.equalsIgnoreCase("SALTOSCOMP")){
			msg1=UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaEJG.literal.guardia");
			out.println("	if("+nombre+"."+campoGuardia+".value==null || "+nombre+"."+campoGuardia+".value==''){ alert('"+msg1+" "+msg2+"'); return false;}");
			msg1=UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaEJG.literal.fechaApertura");
			out.println("	if("+nombre+"."+campoFecha+".value==null || "+nombre+"."+campoFecha+".value==''){ alert('"+msg1+" "+msg2+"'); return false;}");
		}else if(concepto.equalsIgnoreCase("DESIGNACION")){
			msg1=UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaEJG.literal.fechaApertura");
			out.println("	if("+nombre+"."+campoFecha+".value==null || "+nombre+"."+campoFecha+".value==''){ alert('"+msg1+" "+msg2+"'); return false;}");
			
		}
		out.println("	return true;");
		out.println("}");
		
		out.println("function validaArt27Form() {");
		if(!concepto.equalsIgnoreCase("DESIGNACION")&&!concepto.equalsIgnoreCase("SALTOSCOMP")){
			msg1=UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaEJG.literal.guardia");
			out.println("	if("+nombre+"."+campoGuardia+".value==null || "+nombre+"."+campoGuardia+".value==''){ alert('"+msg1+" "+msg2+"'); return false;}");
			msg1=UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaEJG.literal.fechaApertura");
			out.println("	if("+nombre+"."+campoFecha+".value==null || "+nombre+"."+campoFecha+".value==''){ alert('"+msg1+" "+msg2+"'); return false;}");
		}else if(concepto.equalsIgnoreCase("DESIGNACION")){
			msg1=UtilidadesString.getMensajeIdioma(usrbean, "gratuita.busquedaEJG.literal.fechaApertura");
			out.println("	if("+nombre+"."+campoFecha+".value==null || "+nombre+"."+campoFecha+".value==''){ alert('"+msg1+" "+msg2+"'); return false;}");
			
		}
		out.println("	return true;");
		out.println("}");		

		//Reparte los resultados en los campos correspondientes
		out.println("");
		out.println("function manejaRespuestaForm(res) {");
		//out.println(" alert('MANEJARESPUESTASFORM.... concepto="+concepto+" ; operacion="+operacion+"');");
		out.println("	if (res != null && res[0]!=null && res[1]!=null){");
		if (this.mostrarNColegiado) { 
			out.println("		document.getElementById('"+this.campoColegiado+"').value=res[1];");
		}
		if (this.mostrarNombreColegiado) { 
			out.println("		document.getElementById('"+this.campoNombreColegiado+"').value=res[2] + ' ' + res[3] + ' ' + res[4];");
		}
		if(campoPersona!=null){
			out.println("		"+nombre+"."+campoPersona+".value=res[0];");
		}
		if(campoColegiado!=null){
			out.println("		"+nombre+"."+campoColegiado+".value=res[1];");
		}
		if(campoColegio!=null){
			out.println("		"+nombre+"."+campoColegio+".value=res[5];");
		}
		out.println("		if ("+nombre+".sustituta) {");
		out.println("	  	    "+nombre+".sustituta.value=res[9];");
		out.println("	    } ");
    	
    	//out.println(" alert('SALTOS: CheckSalto.style.display='+tdCheckSalto.style.display+' -- condiciones='+res[5]+' y '+res[6]);");
	
		// RGG 10/04/2008 Cambio pedido expresamente por Luis Pedro que consiste en:
		// 	Siempre que haya busquda manual se muestra lo del salto

		/*out.println("		"+nombre+"."+campoFlagSalto+".value='1';");
		out.println("		"+nombre+"."+campoFlagCompensacion+".value=res[6];");
		out.println("		document.getElementById('"+campoSalto+"').checked=false;");
		out.println("		tdCheckSalto.style.display='block'; ");
		*/
		
		out.println("		"+nombre+"."+campoFlagSalto+".value=res[5];");
		out.println("		"+nombre+"."+campoFlagCompensacion+".value=res[6];");
		//out.println("		document.getElementById('"+campoSalto+"').checked=(res[5]=='1' && res[6]=='N');");
		//out.println("		tdCheckSalto.style.display=(res[5]=='1' && res[6]=='N'?'block':'none'); ");
		out.println("	}");
       
		if ((concepto.equals("ASISTENCIA") || concepto.equals("EJG") || concepto.equals("SOJ")) && operacion.equals("ASIGNACION")){
			out.println("		document.getElementById('"+campoSalto+"').checked=false;");
			out.println("		document.getElementById('tdCheckSalto').style.visibility='visible'; ");
			out.println("		document.getElementById('tdCheckSalto').style.display='block'; ");
		}
		
		if ((concepto.equals("GUARDIA") || concepto.equals("DESIGNACION")) && operacion.equals("SUSTITUCION")){// inc-5917
			out.println("		document.getElementById('"+campoCompensacion+"').checked=false;");
			out.println("		document.getElementById('tdCheckCompensacion').style.visibility='visible'; ");
			out.println("		document.getElementById('tdCheckCompensacion').style.display='block'; ");
			
			out.println("		document.getElementById('"+campoSalto+"').checked=false;");
			out.println("		document.getElementById('tdCheckSalto').style.visibility='visible'; ");
			out.println("		document.getElementById('tdCheckSalto').style.display='block'; ");
		}

		if(concepto.equals("DESIGNACION")  && !operacion.equals("ASIGNACION")){
			out.println("		document.getElementById('"+campoCompensacion+"').checked=false;");
			out.println("		document.getElementById('tdCheckCompensacion').style.visibility='hidden'; ");
			out.println("		document.getElementById('tdCheckCompensacion').style.display='none'; ");
		}
		
		if(concepto.equals("DESIGNACION")  && operacion.equals("ASIGNACION")){
		    out.println("		document.getElementById('"+campoSalto+"').checked=false;");
		    out.println("		document.getElementById('tdCheckSalto').style.visibility='visible'; ");
			out.println("		document.getElementById('tdCheckSalto').style.display='block'; ");
		}
/*
 		out.println("	}else{");//si no hay respuesta ocultamos los resultados????
		if (this.mostrarNColegiado) { 
			out.println("		document.getElementById('colegiadoSJCS').value='';");
		}
		if (this.mostrarNombreColegiado) { 
			out.println("		document.getElementById('nombreMostradoSJCS').value='';");
		}
		if(campoPersona!=null){
			out.println("		"+nombre+"."+campoPersona+".value='';");
		}
		if(campoColegiado!=null){
			out.println("		"+nombre+"."+campoColegiado+".value='';");
		}
		out.println("		"+nombre+"."+campoFlagSalto+".value='';");
		out.println("		"+nombre+"."+campoFlagCompensacion+".value='';");
		out.println("		document.getElementById('"+campoSalto+"').checked=false;");
		out.println("		tdCheckSalto.style.display='none'; ");
		out.println("		document.getElementById('"+campoCompensacion+"').checked=false;");
		out.println("		tdCheckCompensacion.style.visibility='hidden'; ");
*/
		out.println("}");
		
		out.println("");
		out.println("function manejaRespuestaFormAutomatico(res) {");
		//out.println(" alert('MANEJARESPUESTASFORM AUTOMATICO.... concepto="+concepto+" ; operacion="+operacion+"');");
		out.println("	if (res != null && res[0]!=null && res[1]!=null){");
		if (this.mostrarNColegiado) { 
			out.println("		document.getElementById('"+this.campoColegiado+"').value=res[1];");
		}
		if (this.mostrarNombreColegiado) { 
			out.println("		document.getElementById('"+this.campoNombreColegiado+"').value=res[2] + ' ' + res[3] + ' ' + res[4];");
		}
		if(campoPersona!=null){
			out.println("		"+nombre+"."+campoPersona+".value=res[0];");
		}
		if(campoColegiado!=null){
			out.println("		"+nombre+"."+campoColegiado+".value=res[1];");
		}
		out.println("		tdCheckCompensacion.style.visibility='hidden'; ");
		//out.println("		tdCheckCompensacion.style.display='none'; ");

		out.println("		tdCheckSalto.style.visibility='hidden'; ");
		//out.println("		tdCheckSalto.style.display='none'; ");
		
		out.println("	}");

		out.println("}");
		

		if(manual){
			//Se implementa la busqueda manual
			out.println(""); 
			out.println("function buscarMan() {");
			out.println("	if(validaForm()){");
			out.println("		var vForm=creaForm();");
			out.println("		vForm.modo.value='abrir';");
			out.println("		vForm.concepto.value='"+concepto+"';");
			out.println("		vForm.operacion.value='"+operacion+"';");
			out.println("		vForm.idTurno.value="+nombre+"."+campoTurno+".value;");
			if(!concepto.equalsIgnoreCase("DESIGNACION")){
				out.println("		vForm.idGuardia.value="+nombre+"."+campoGuardia+".value;");
				out.println("		vForm.fecha.value="+nombre+"."+campoFecha+".value;");
			}else if(concepto.equalsIgnoreCase("DESIGNACION")){
				out.println("		vForm.fecha.value="+nombre+"."+campoFecha+".value;");
			}
			out.println("		var res = ventaModalGeneral(vForm.name,\"G\");");
			out.println("		manejaRespuestaForm(res);");
			out.println("	}");
			out.println("}");
		}
		
		if(art27){
			//Se implementa la busqueda manual
			out.println(""); 
			out.println("function buscarDesig27() {");
			out.println("	if(validaArt27Form()){");
			out.println("		var vForm=creaFormArticulo27();");
			out.println("		vForm.modo.value='designarArt27';");
			out.println("		vForm.concepto.value='"+concepto+"';");
			out.println("		vForm.operacion.value='"+operacion+"';");
			out.println("		vForm.idTurno.value="+nombre+"."+campoTurno+".value;");
			if(!concepto.equalsIgnoreCase("DESIGNACION")){
				out.println("		vForm.idGuardia.value="+nombre+"."+campoGuardia+".value;");
				out.println("		vForm.fecha.value="+nombre+"."+campoFecha+".value;");
			}else if(concepto.equalsIgnoreCase("DESIGNACION")){
				out.println("		vForm.fecha.value="+nombre+"."+campoFecha+".value;");
			}
			out.println("		var res = ventaModalGeneral(vForm.name,\"G\");");
			out.println("		manejaRespuestaForm(res);");
			out.println("	}");
			out.println("}");
		}
		
	
		if(automatica && !diaGuardia){
			//Se implementa la busqueda automatica
			out.println(""); 
			out.println("function buscarAut() {");
			out.println("	sub(); ");
			out.println("	if(validaForm()){");
			out.println("		var vForm=creaForm();");
			out.println("		vForm.target='submitArea';");
			out.println("		vForm.modo.value='buscarPor';");
			out.println("		vForm.concepto.value='"+concepto+"';");
			out.println("		vForm.operacion.value='"+operacion+"';");
			out.println("		vForm.idTurno.value="+nombre+"."+campoTurno+".value;");
			if(!concepto.equalsIgnoreCase("DESIGNACION")){
				out.println("		vForm.idGuardia.value="+nombre+"."+campoGuardia+".value;");
				out.println("		vForm.fecha.value="+nombre+"."+campoFecha+".value;");
			}else if(concepto.equalsIgnoreCase("DESIGNACION")){
				out.println("		vForm.fecha.value="+nombre+"."+campoFecha+".value;");
				
			}
			out.println("		creaSubmitArea();");
			out.println("		vForm.submit();");
			out.println("	}else{");
			out.println("		fin();");
			out.println("		return false;");
			out.println("	}");
			out.println("}");
		}
		
		if (eliminarSeleccionado) {
			out.println("function eliminaLetradoSeleccionado(){");
			if(campoPersona!=null){
				out.println("	"+nombre+"."+campoPersona+".value=\"\";");
			}			
			if(campoColegiado!=null){
				out.println("	"+nombre+"."+campoColegiado+".value=\"\";");
			}
			
			if (this.campoNombreColegiado != null) { 
				out.println("	var nombreColegiado = document.getElementById('"+this.campoNombreColegiado+"');");
				out.println("	if (nombreColegiado){");
				out.println("		nombreColegiado.value=\"\";");
				out.println("	}");
			}	
			if (campoCompensacion != null) {
				out.println("	var campo = document.getElementById('"+campoCompensacion+"');");
				out.println("	if (campo){");
				out.println("		campo.checked=false;");
				out.println("	}");
				out.println("	tdCheckCompensacion.style.visibility='hidden'; ");
			}
			if (campoSalto != null) {
				out.println("	var campo = document.getElementById('"+campoSalto+"');");
				out.println("	if (campo){");
				out.println("		campo.checked=false;");
				out.println("	}");
				out.println("	tdCheckSalto.style.visibility='hidden'; ");
			}
			
			out.println("	if ("+nombre+".sustituta) {");
			out.println("	    "+nombre+".sustituta.value=\"\";");
			out.println("	} ");
	    	
			out.println("	if ("+nombre+"."+campoFlagSalto+") {");	
			out.println("		"+nombre+"."+campoFlagSalto+".value=\"\";");
			out.println("	} ");
			
			
			out.println("	if ("+nombre+"."+campoFlagCompensacion+") {");	
			out.println("		"+nombre+"."+campoFlagCompensacion+".value=\"\";");
			out.println("	} ");
			
			
			out.println("}");
			
		}
		
		if(diaGuardia){
			// Se implementa la busqueda de combos con letrados del día de
			// guardia
			out.println(""); 
			out.println("function rellenarComboGuardia() {");
			if (!this.modo.equalsIgnoreCase("ver")) {
			    out.println("	if("+nombre+"."+campoTurno+".value!='' && "+nombre+"."+campoGuardia+".value!='' && "+nombre+"."+campoFecha+".value!='' && validarFechaRegExp("+nombre+"."+campoFecha+".value)){");
				out.println("		var vForm=creaForm();");
				out.println("		vForm.target='submitArea';");
				out.println("		vForm.modo.value='ver';");
				out.println("		vForm.idTurno.value="+nombre+"."+campoTurno+".value;");
				out.println("		vForm.idGuardia.value="+nombre+"."+campoGuardia+".value;");
				out.println("		vForm.fecha.value="+nombre+"."+campoFecha+".value;");
				out.println("		creaSubmitArea();");
				out.println("		vForm.submit();");
				out.println("	}");
			}
			out.println("	}");
			
			out.println("function cargarPrimero(objeto) {");
			out.println("	objeto.options[0].selected=true;");
			out.println("}");
			
			out.println("var ncolegiado;"); 
			
			out.println("function seleccionarDatosCombo(objeto) {");
			if (!this.modo.equalsIgnoreCase("ver")) {
				if(campoPersona!=null && campoColegiado!=null){
				    out.println(" if (objeto.value!='') { ");
				    	
				    	//out.println(" alert('COMBO: seleccionado letrado de guardia');");
					
				    	out.println(" "+nombre+"."+campoPersona+".value=objeto.value;");
						out.println(" "+nombre+"."+campoColegiado+".value=document.ncolegiado[objeto.selectedIndex];");
						out.println(" document.getElementById('"+this.campoNombreColegiado+"').value=objeto.options[objeto.selectedIndex].text;");
						//out.println(" document.getElementById('"+campoSalto+"').checked=true;");
						//out.println(" tdCheckSalto.style.display='block'; ");				
						out.println(" document.getElementById('"+campoSalto+"').checked=false;");
						//out.println(" tdCheckSalto.style.display='block'; ");				
				    out.println(" }  ");
//				    out.println(" if (document.ncolegiado.lenght==0) { ");
//				    	out.println(" "+nombre+"."+campoPersona+".value='';");
//						out.println(" "+nombre+"."+campoColegiado+".value='';");
//						out.println(" document.getElementById('"+this.campoNombreColegiado+"').value='';");
//						out.println(" document.getElementById('"+campoSalto+"').checked=false;");
//						out.println(" tdCheckSalto.style.display='none'; ");				
//				    out.println(" }  ");
				}
			}
			out.println("}");
			
			
		
		}
		
		out.println("");
		out.println("</script>");
	}

	public boolean isEliminarSeleccionado() {
		return eliminarSeleccionado;
	}

	public void setEliminarSeleccionado(boolean eliminarSeleccionado) {
		this.eliminarSeleccionado = eliminarSeleccionado;
	}
	
	
}
