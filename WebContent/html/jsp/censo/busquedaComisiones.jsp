<!-- busquedaComisiones.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsZonaBean"%>
<%@ page import="com.siga.beans.ScsSubzonaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->

<% 	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
	String parametro[] = new String[2];

	parametro[0] = (String)usr.getLocation();
	parametro[1] = (String)usr.getLanguage().toUpperCase();
	


%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:TituloExt 
		titulo="censo.comisiones.literal.comisiones" 
		localizacion="censo.comisiones.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
</head>

<body  onLoad="ajusteAlto('resultado');" >

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<fieldset>
	<table class="tablaCentralCampos" align="center">

	<html:form action="/CEN_GestionarComisiones.do" method="POST" target="resultado">
	<html:hidden property = "modo" value = "inicio"/>
	
	
	<input type="hidden" name="limpiarFilaSeleccionada" value="">
  		
	<tr>
	<td class="labelText">
		<siga:Idioma key="censo.busquedaComisiones.literal.comision"/>
	</td>
	<td class="labelText">
		<siga:ComboBD nombre="comision" tipo="cmbComision" parametro="<%=parametro%>" clase="boxCombo" />
	</td>
	
	<td class="labelText">
		<siga:Idioma key="censo.busquedaComisiones.literal.cargos"/>
	</td>
	<td class="labelText">
		<siga:ComboBD nombre="cargos" tipo="cmbCargos" parametro="<%=parametro%>" clase="boxCombo" />
	</td>
	</tr>
	<tr>
	<td class="labelText">
		<siga:Idioma key="censo.busquedaComisiones.literal.fechaCargo"/>
	</td>
	<td class="labelText">
		<html:text styleClass="box" property="fechaCargo" size="8" maxlength="10" readonly="true"/>
		<a href='javascript://'onClick="return showCalendarGeneral(fechaCargo);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"> </a>
	</td>
	</tr>
	
	</html:form>
	
	</table>
	</fieldset>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="B"  titulo="censo.comisiones.literal.consultarComisiones" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();			
			document.forms[0].modo.value = "buscar";
			document.forms[0].submit();
		}		
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[0].reset();
		}
		
		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() 
		{
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "nuevo";
			document.forms[0].submit();
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
	<!-- FIN: IFRAME LISTA RESULTADOS -->	

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>