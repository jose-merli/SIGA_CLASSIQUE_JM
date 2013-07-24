<!DOCTYPE html>
<html>
<head>
<!-- partidaPresupuestaria.jsp -->
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

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsPartidaPresupuestariaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>

<!-- JSP -->
<% 	String app=request.getContextPath();
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
%>



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.partidaPresupuestaria.literal.partidaPresupuestaria" 		
		localizacion="sjcs.maestros.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onload="ajusteAlto('resultado');">

	<fieldset>
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<table class="tablaCentralCampos" align="center">

	<html:form action="/PartidaPresupuestariaAction.do" method="POST" target="resultado">
		<input type="hidden" name="modo" value="inicio">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="idInstitucion" value="<%=usr.getLocation()%>">
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		
	<tr>
	<td class="labelText">
		<siga:Idioma key="gratuita.partidaPresupuestaria.literal.nombre"/>
	</td>
	<td class="labelText">
		<html:text name="DefinirPartidaPresupuestariaForm" property="nombrePartida" size="30" maxlength="60" styleClass="box" value=""></html:text>
	</td>	
	<td class="labelText">
		<siga:Idioma key="gratuita.partidaPresupuestaria.literal.descripcion"/>
	</td>
	<td class="labelText">
		<html:text name="DefinirPartidaPresupuestariaForm" property="descripcion" size="30" maxlength="1024" styleClass="box" value=""></html:text>
	</td>	
	</tr>
	
	</html:form>
	
	</table>
	</fieldset>

	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: BOTONES BUSQUEDA -->	
	
	<siga:ConjBotonesBusqueda botones="N,B"  titulo="gratuita.partidaPresupuestaria.literal.partidaPresupuestaria" />
	
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
		
		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() 
		{		
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if(resultado=='MODIFICADO') buscar();
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