<!-- busquedaListadoRetenciones.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->

<% 	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="FactSJCS.listadoRetencionesJ.cabecera" 
		localizacion="FactSJCS.listadoRetencionesJ.ruta"/>	
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>

<body onLoad="ajusteAlto('resultado');">


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	
	<table width="100%" align="center">

	<tr>
	<td>
	<siga:ConjCampos leyenda="FactSJCS.mantRetencionesJ.leyenda">
	<table width="100%" align="center">

	<html:form action="/FCS_RetencionesJudiciales" method="POST" target="resultado">
	<html:hidden property = "modo" value = "inicio"/>
  		
	<tr>
	<td class="labelText">
		<siga:Idioma key="FactSJCS.listadoRetencionesJ.literal.destinatario"/>
	</td>
	<td class="labelText">
		<html:text name="MantenimientoRetencionesJudicialesForm" property="nombreDestinatario" size="50" maxlength="250" styleClass="box" value=""></html:text>
	</td>
	</tr>
	
	<tr>
	<td class="labelText">
		<siga:Idioma key="FactSJCS.listadoRetencionesJ.literal.fechaDesde"/>
	</td>				
	<td class="labelText">
		<html:text name="MantenimientoRetencionesJudicialesForm" property="fechaIniListado" size="10" maxlength="10" styleClass="box" value="" readonly="true"></html:text>
		&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaIniListado);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt=''<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>'  border="0"></a>
	</td>
	<td class="labelText">
		<siga:Idioma key="FactSJCS.mantRetencionesJ.literal.fechaFin"/>
	</td>				
	<td class="labelText">
		<html:text name="MantenimientoRetencionesJudicialesForm" property="fechaFinListado" size="10" styleClass="box" value="" readonly="true"></html:text>
		&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaFinListado);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt='<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>'  border="0"></a>
	</td>	
	</tr>
	
	</html:form>
	
	</table>
	</siga:ConjCampos>
	</td>	
	</tr>
	</table>
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="B"  titulo="FactSJCS.listadoRetencionesJ.cabecera" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();			
			document.forms[0].modo.value = "buscarListado";
			document.forms[0].submit();			
		}		
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[0].reset();
		}
				
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
			
	<siga:ConjBotonesAccion botones="I"/>

<script type="text/javascript">
	function accionImprimir() {
		resultado.focus();
		resultado.print();
	}
</script>

<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>


</body>
</html>