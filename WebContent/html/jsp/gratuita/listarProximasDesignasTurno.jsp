<!DOCTYPE html>
<html>
<head>
<!-- listarProximasDesignasTurno.jsp -->

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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>
<%@ page import="com.siga.gratuita.util.calendarioSJCS.LetradoInscripcion"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	request.getSession().removeAttribute("pestanasG");
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	List<ScsInscripcionTurnoBean> resultado = (ArrayList)request.getAttribute("resultado");
	
	//Datos del Colegiado si procede:
	String nombrePestanha = (String)request.getAttribute("NOMBRECOLEGPESTAÑA");
	String numeroPestanha = (String)request.getAttribute("NUMEROCOLEGPESTAÑA");
	String estadoColegial = (String)request.getAttribute("ESTADOCOLEGIAL");
	//Si entrada=2 venimos de la pestanha de SJCS:
	String entrada = (String)ses.getAttribute("entrada");

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt titulo="censo.fichaCliente.sjcs.proximasDesignas.cabecera" localizacion="censo.fichaCliente.sjcs.proximasDesignas.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->		
</head>

<body class="tablaCentralCampos">
    <table class="tablaTitulo" align="center" cellspacing="0">
<%
		//Entrada desde el menu de Censo:
		if (entrada.equalsIgnoreCase("2")) { 
%>
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.fichaCliente.proximasDesignas.pestana.titulito"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
<% 
					if (numeroPestanha!= null && !numeroPestanha.equalsIgnoreCase("")) { 
						if (estadoColegial!=null && !estadoColegial.equals("")){
%>
							<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>
							 <%=UtilidadesString.mostrarDatoJSP(numeroPestanha)%> 
							 &nbsp; 
							 (<%=UtilidadesString.mostrarDatoJSP(estadoColegial)%>)
<%
						} else {
%> 
						 	(<siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial"/>) 
<%
						}
						
					} else { 
%>
				   		<siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
<% 
					} 
%>
				</td>
			</tr>
<% 
		} 
%>
	</table>

	<html:form action="JGR_PestanaDesignas.do" method="post" target="mainPestanas"	 style="display:none">
		<!-- Campo obligatorio -->
		<html:hidden property = "modo" value = "abrirAvanzada"/>
		<!-- RGG: cambio a formularios ligeros -->
		
		<input type="hidden" name="actionModal" value="">
	</html:form>	

	<siga:Table 
		name="tablaDatos"
	   	border="1"
	   	columnNames="gratuita.definirTurnosIndex.literal.abreviatura,
	   		gratuita.listarProximasDesignas.literal.nombreTurno,
	   		gratuita.definirTurnosIndex.literal.area,
	   		gratuita.definirTurnosIndex.literal.materia,
	   		gratuita.definirTurnosIndex.literal.zona,
	   		gratuita.definirTurnosIndex.literal.subzona,
	   		gratuita.listarProximasDesignas.literal.posicion"
	   columnSizes="17,20,15,15,12,11,11">

<% 
		if ((resultado==null) || (resultado.size()==0)) { 
%>
			<tr class="notFound">
				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<% 
		} else {
    		int contador=1;
			while ((contador) <= resultado.size()) {	 
				ScsInscripcionTurnoBean inscripcionLetradoTurno = (ScsInscripcionTurnoBean)resultado.get(contador-1);
%>
				<siga:FilaConIconos	fila='<%=String.valueOf(contador)%>'
  					botones="" 
  					pintarEspacio="no"
  					visibleConsulta="no"
  					visibleEdicion = "no"
  					visibleBorrado = "no"
  					clase="listaNonEdit">
  					
					<td><%=inscripcionLetradoTurno.getTurno().getAbreviatura()%>&nbsp;</td>
					<td><%=inscripcionLetradoTurno.getTurno().getNombre()%>&nbsp;</td>
					<td><%=inscripcionLetradoTurno.getTurno().getArea().getNombre()%>&nbsp;</td>
					<td><%=inscripcionLetradoTurno.getTurno().getMateria().getNombre()%>&nbsp;</td>
					<td><%=inscripcionLetradoTurno.getTurno().getZona().getNombre()%>&nbsp;</td>
					<td><%=inscripcionLetradoTurno.getTurno().getSubZona().getNombre()%>&nbsp;</td>
					<td><%=inscripcionLetradoTurno.getTurno().getIdOrdenacionColas()==null?"":inscripcionLetradoTurno.getTurno().getIdOrdenacionColas()%>&nbsp;</td>
				</siga:FilaConIconos>
<%	
				contador++;
			}
		} 
%>
	</siga:Table>

	<% if (!busquedaVolver.equals("volverNo")) { %>
		<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
	<% } %>

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
</body>
</html>