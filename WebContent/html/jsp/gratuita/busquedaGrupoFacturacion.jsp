<!DOCTYPE html>
<html>
<head>
<!-- busquedaGrupoFacturacion.jsp -->
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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>

<!-- JSP -->

<% 	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	HttpSession ses=request.getSession();
%>



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.gruposFacturacion.cabecera" 
		localizacion="sjcs.maestros.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body onLoad="ajusteAlto('resultado');">


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<siga:ConjCampos leyenda="gratuita.gruposFacturacion.leyenda">
	<table align="left">

	<html:form action="/JGR_MantenimientoGruposFacturacion.do" method="POST" target="resultado">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "actionModal" value = ""/>
  		
	<tr>
	<td class="labelText" width="150">
		<siga:Idioma key="gratuita.gruposFacturacion.literal.nombre"/>
	</td>
	<td class="labelText">
		<html:text name="MantenimientoGrupoFacturacionForm" property="nombre" size="100" maxlength="100" styleClass="box" value=""></html:text>
	</td>	
	</tr>
	
	</html:form>
	
	</table>
	</siga:ConjCampos>
	<siga:ConjBotonesBusqueda botones="N,B"  titulo="gratuita.gruposFacturacion.cabecera" />
	
	<!-- FIN: BOTONES BUSQUEDA -->
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{			
			document.forms[0].modo.value = "buscarPor";
			document.forms[0].submit();
		}		
		
		<!-- Funcion asociada a boton limpiar -->
		function limpiar() 
		{		
			document.forms[0].reset();
		}
		
		<!-- Funcion asociada a boton Nuevo -->		
		function nuevo(){
			document.forms[0].modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"P");
			if (resultado=="MODIFICADO") buscar();
		}
		
	</script>

	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>

			
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

</body>
</html>