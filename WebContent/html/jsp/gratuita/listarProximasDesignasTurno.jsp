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

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	request.getSession().removeAttribute("pestanasG");
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	List<ScsInscripcionTurnoBean> resultado = (ArrayList)request.getAttribute("resultado");
	
	//Datos del Colegiado si procede:
	String nombrePestanha = (String)request.getAttribute("NOMBRECOLEGPESTAÑA");
	String numeroPestanha = (String)request.getAttribute("NUMEROCOLEGPESTAÑA");
	String estadoColegial = (String)request.getAttribute("ESTADOCOLEGIAL");
	//Si entrada=2 venimos de la pestanha de SJCS:
	String entrada = (String)ses.getAttribute("entrada");
	//Si venimos del menu de Censo tenemos un alto menor ya que ponemos el nombre del colegiado:
	String alto = "330";
	if (entrada!=null && entrada.equals("2"))
		alto = "273";

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}

%>

<%@page import="com.siga.gratuita.util.calendarioSJCS.LetradoGuardia"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="censo.fichaCliente.sjcs.proximasDesignas.cabecera" 
			localizacion="censo.fichaCliente.sjcs.proximasDesignas.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->		
</head>

<body class="tablaCentralCampos">
    <table class="tablaTitulo" align="center" cellspacing=0>
	<%
		//Entrada desde el menu de Censo:
		if (entrada.equalsIgnoreCase("2")) { %>
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.fichaCliente.proximasDesignas.pestana.titulito"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
				    <% if(numeroPestanha!= null && !numeroPestanha.equalsIgnoreCase("")) { %>
							<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroPestanha)%>
							<%if (!estadoColegial.equals("")){%> &nbsp; (<%=UtilidadesString.mostrarDatoJSP(estadoColegial)%>) <%}%>
					<% } else { %>
						   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<% } %>
				</td>
			</tr>
	<% } %>

<html:form action="JGR_PestanaDesignas.do" method="post" target="mainPestanas"	 style="display:none">
		
	<!-- Campo obligatorio -->
	<html:hidden property = "modo" value = "abrirAvanzada"/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	

		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="gratuita.definirTurnosIndex.literal.abreviatura,
		   gratuita.listarProximasDesignas.literal.nombreTurno,
		   gratuita.definirTurnosIndex.literal.area
		   ,gratuita.definirTurnosIndex.literal.materia
		   ,gratuita.definirTurnosIndex.literal.zona
		   ,gratuita.definirTurnosIndex.literal.subzona
		   ,gratuita.listarProximasDesignas.literal.posicion"
		   tamanoCol="17,20,15,15,12,11,10"
		   			alto="100%"
		   			ajusteBotonera="true"		

		  >

	<% if ((resultado==null) || (resultado.size()==0)) { %>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<% } else { %>
		<%
	    	int contador=1;
			while ((contador) <= resultado.size())
			{	 
				ScsInscripcionTurnoBean inscripcionLetradoTurno = (ScsInscripcionTurnoBean)resultado.get(contador-1);
		%>
		<siga:FilaConIconos	fila='<%=String.valueOf(contador)%>'
	  				botones="" 
	  				pintarEspacio="no"
	  				visibleConsulta="no"
	  				visibleEdicion = "no"
	  				visibleBorrado = "no"
	  				clase="listaNonEdit">
				<td ><%=inscripcionLetradoTurno.getTurno().getAbreviatura()%>&nbsp;</td>
				<td ><%=inscripcionLetradoTurno.getTurno().getNombre()%>&nbsp;</td>
				<td ><%=inscripcionLetradoTurno.getTurno().getArea().getNombre()%>&nbsp;</td>
				<td ><%=inscripcionLetradoTurno.getTurno().getMateria().getNombre()%>&nbsp;</td>
				<td><%=inscripcionLetradoTurno.getTurno().getZona().getNombre()%>&nbsp;</td>
				<td><%=inscripcionLetradoTurno.getTurno().getSubZona().getNombre()%>&nbsp;</td>
				<td ><%=inscripcionLetradoTurno.getTurno().getIdOrdenacionColas()==null?"":inscripcionLetradoTurno.getTurno().getIdOrdenacionColas()%>&nbsp;</td>
		</siga:FilaConIconos>
		<%	contador++;}%>
	<% } %>

		</siga:TablaCabecerasFijas>


<script language="JavaScript">
		//function accionVolver() 
		//{		
		//	document.forms[0].action="JGR_DefinirTurnos.do";
		//	document.forms[0].target="mainWorkArea";
		//	document.forms[0].modo.value="abrirAvanzada";
		//	document.forms[0].submit();
		//}
</script>

<% if (!busquedaVolver.equals("volverNo")) { %>
	<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
<% } %>

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

	</table>
</body>
</html>
