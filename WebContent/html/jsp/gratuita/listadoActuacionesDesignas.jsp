<!-- listadoActuacionesDesignas.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gratuita.form.PestanaDelitoDesignaForm"%>
<!-- JSP -->

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector obj = (Vector)request.getSession().getAttribute("resultado");
	request.getSession().removeAttribute("resultado");
	String modo = (String) ses.getAttribute("Modo");
	String deDonde=(String)request.getParameter("deDonde");
	String idPersona = (String) request.getAttribute("idPersonaActuacion");
	String alto ="415";
	if (deDonde!=null && deDonde.equals("ficha")) {
		alto ="315";
	}
	String idTurno=(String)request.getSession().getAttribute("DESIGNA_IDTURNO");
	String anio=(String)request.getSession().getAttribute("DESIGNA_ANIO");
	String numero=(String)request.getSession().getAttribute("DESIGNA_NUMERO");
	boolean botonNuevo = (Boolean)request.getSession().getAttribute("botonNuevo");
	
	
	
%>	
											
<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Properties"%>
<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt 
		titulo="gratuita.actuacionesDesigna.literal.titulo" 
		localizacion="gratuita.actuacionesDesigna.literal.location"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	
		function refrescarLocal (){
			parent.buscar();
		}
	</script>

</head>

<body class="tablaCentralCampos" >
	<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos">
	
					<%
					    String t_nombre = "", t_apellido1 = "", t_apellido2 = "", t_anio = "", t_numero = "";
						ScsDesignaAdm adm = new ScsDesignaAdm (usr);
						Hashtable hTitulo = adm.getTituloPantallaDesigna(usr.getLocation(), anio, numero,idTurno);

						if (hTitulo != null) {
							t_nombre    = (String)hTitulo.get(ScsPersonaJGBean.C_NOMBRE);
							t_apellido1 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO1);
							t_apellido2 = (String)hTitulo.get(ScsPersonaJGBean.C_APELLIDO2);
							t_anio      = (String)hTitulo.get(ScsDesignaBean.C_ANIO);
							t_numero    = (String)hTitulo.get(ScsDesignaBean.C_CODIGO);
							
						}
					
					%>
					<%=UtilidadesString.mostrarDatoJSP(t_anio)%>/<%=UtilidadesString.mostrarDatoJSP(t_numero)%>
					- <%=UtilidadesString.mostrarDatoJSP(t_nombre)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido1)%> <%=UtilidadesString.mostrarDatoJSP(t_apellido2)%>
			</td>
		</tr>
	</table>
	
		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/JGR_ActuacionesDesigna.do" method="post" target="submitArea"  style="display:none">
			<html:hidden property = "modo"  styleId = "modo"  value = ""/>
		</html:form>
			<!-- Campo obligatorio -->
			<siga:Table 
			   name="tablaDatos"
			   border="1"
			   columnNames="gratuita.actuacionesAsistencia.literal.fechaActuacion,gratuita.busquedaAsistencias.literal.numero,gratuita.actuacionesDesigna.literal.modulo,gratuita.procedimientos.literal.acreditacion,gratuita.actuacionesDesigna.literal.justificacion,gratuita.actuacionesDesigna.literal.validada,gratuita.procedimientos.literal.anulada,gratuita.actuacionesDesigna.literal.facturacion,"
			   columnSizes="8,6,20,15,8,6,6,20,10"
			   modal="G">

		<% if (obj==null || obj.size()==0) { %>
					<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
		<% } else { %>
		
			  <%
		    	int recordNumber=1;
		    	String select = "";
		    	String botones = "";
		    	Vector v = null;
		    	ScsActuacionDesignaAdm scsActuacionDesignaAdm = new ScsActuacionDesignaAdm(usr);
				while ((recordNumber) <= obj.size()){	 
				    modo = (String) ses.getAttribute("Modo");
					Hashtable hash = (Hashtable)obj.get(recordNumber-1);
					String idFacturacion = (String)hash.get("IDFACTURACION");
					
					boolean modificable = (idFacturacion==null||idFacturacion.equals(""));

// RGG 					String nombreProc = ((String) hash.get("NOMBREPROCEDIMIENTO")) + ((hash.get("NOMBREACREDITACION")==null)?"":(" - " + (String)hash.get("NOMBREACREDITACION")));
					String nombreProc = (String) hash.get("NOMBREPROCEDIMIENTO");
					boolean  validada = ((String) hash.get("VALIDADA")).equals("1")?true:false;
					

// RGG 14-03-2006 CAMBIOS para controlar los permisos de los botones de edicion y borrado
                  
				    if (deDonde!=null && deDonde.equals("ficha")){
						// cuando esta en la ficha solamente se puede consultar, sea quien sea
						botones = "C";
						
						if (usr.isLetrado() && !validada && modificable) {
							botones = "C,E,B";
						}
					} 
					else {
						if (usr.isLetrado()) {
							// caso de menu SJCS
							// si es letrado solamente en consulta
							botones = "C";
						} else {
							if(modificable) {
								// modificable
								botones = "C,E,B";
							} else {
							    botones = "C";						
							}
						}
					}

				 String anulacion = (String)hash.get("ANULACION");
				 if((anulacion!=null)&&(anulacion).equalsIgnoreCase("1")){
				 	modo="VER";	
				 }
				
			 	%>
				  	<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" clase="listaNonEdit" modo="<%=modo%>" >
						<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",(String)hash.get("FECHA")))%></td>
						<td><%=hash.get("NUMEROASUNTO")%></td>
						<td><%=UtilidadesString.mostrarDatoJSP(nombreProc)%></td>
						<td>
							<%=hash.get("NOMBREACREDITACION")%>
						</td>
						<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("",(String)hash.get("FECHAJUSTIFICACION")))%></td>
						
						<td><%if (validada) { %><siga:Idioma key="general.yes"/>
							<% } else { %><siga:Idioma key="general.no"/>
							<% } %>&nbsp;
						</td>
						<td>
							<%if(anulacion.equalsIgnoreCase("1")){%>
								<siga:Idioma key="gratuita.operarEJG.literal.si"/>
							<%}else if(anulacion.equalsIgnoreCase("0")){%>
								<siga:Idioma key="general.boton.no"/>
							<%}%>
						</td>
						<td><%=((String) hash.get("NOMBREFACTURACION")==null?"":(String) hash.get("NOMBREFACTURACION"))%>&nbsp;
						</td>
						
					</siga:FilaConIconos>	
				<%recordNumber++;%>
				<%}%>	 
		<%}%>

		</siga:Table>
	<div style="position:absolute; left:150px;bottom:50px;z-index:2;">
			<table align="center" border="0">
				<tr>
					<td class="labelText">
						<%if(!botonNuevo){%>
							<siga:Idioma key="gratuita.actuacionDesigna.art27.texto1"/>
						<%}%>
					</td>
				</tr>
			</table>
		</div>		

	</body>
</html>
		  
		
