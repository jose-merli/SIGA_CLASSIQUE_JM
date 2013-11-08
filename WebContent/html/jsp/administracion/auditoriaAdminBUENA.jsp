<!DOCTYPE html>
<html>
<head>
<!-- auditoriaAdminBUENA.jsp
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.Properties" %>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
%>	
	


<!-- HEAD -->

	<title><siga:Idioma key="administracion.auditoria.titulo"/></title>

	<!-- ESTILOS Y JAVASCRIPT -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	

	<script language="JavaScript">

	function buscar() 
	{		
		document.getElementById("modo").value="buscar";						
		auditoriaAdminForm.submit();		
	}
	
	function borrarLog() 
	{
		if(confirm('<siga:Idioma key="messages.deleteConfirmation"/>')){
			document.getElementById("modo").value="borrar";	
			auditoriaAdminForm.submit();
			}
		return false;
	}
	
	function limpiar()
	{
		auditoriaAdminForm.reset();
		document.getElementById("modo").value="";
		document.getElementById("resultado").src="<%=app%>/html/jsp/general/blank.jsp";	
	}	
	
	
	</script>

</head>

<body onLoad="ajusteAlto('resultado');">

	<!-- CAMPOS DE BUSQUEDA-->
	<table class="tablaCentralCampos" align="center">
	<html:form action="/ADM_AuditoriaAdmin.do" method="POST" target="resultado">
	<input type="hidden" name="modo" value="inicio">
	<tr>				
	<td class="labelText">
		<siga:Idioma key="administracion.auditoria.dirip"/>
	</td>				
	<td>
		<html:text name="auditoriaAdminForm" property="direccionIP" size="15" maxlength="15" styleClass="box"></html:text>
	</td>
	<td class="labelText">
		<siga:Idioma key="administracion.auditoria.usuario"/>
	</td>
	<td>
		<html:text name="auditoriaAdminForm" property="usuario" size="40" styleClass="box"></html:text>
	</td>
	<td class="labelText">
		<siga:Idioma key="administracion.auditoria.accion"/>
	</td>
	<td>
		<html:text name="auditoriaAdminForm" property="accion" size="85" styleClass="box"></html:text>
	</td>
	</tr>
	</table>

	<!-- BOTONES BUSQUEDA -->
	<table class="tablaTitulo" align="center">
	<tr> 
	<td class="titulitos">
		<siga:Idioma key="administracion.auditoria.titulo"/>
	</td>
	<td class="tdBotones">
		<html:button property="searchButton"  onclick="return buscar();" styleClass="button">
			<siga:Idioma key="general.search"/>
		</html:button>
	</td>
	<td class="tdBotones">
		<html:button property="searchButton"  onclick="return buscar();" styleClass="button">
			<siga:Idioma key="general.searchAvanzada"/>
		</html:button>
	</td>
	<td class="tdBotones">
		<html:button property="deleteButton" onclick="return borrarLog();" styleClass="button">
			<siga:Idioma key="administracion.auditoria.borrarlog"/>
		</html:button>
	</td>
	<td class="tdBotones">
		<html:button property="clearButton" onclick="return limpiar();" styleClass="button"> 
			<siga:Idioma key="general.new"/> 
		</html:button> 
	</td>
	</tr>	   
	</table>

	<!-- IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
			
</html:form>

<!-- SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

</body>
</html>
