<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<%@ page contentType="text/html" language="java"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

	<% String app=request.getContextPath(); %>
	
		<title><siga:Idioma key="index.title"/></title>
		
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	</head>
	
	<body background="<%=app%>/html/imagenes/fondo2.gif">
		<br><br>
		<h1>
			<center><siga:Idioma key="messages.general.errorHTTP"/> 400
				<img width="10%" src="<%=app%>/html/imagenes/aviso.gif" border="0">
			</center>
		</h1>
		<%@ page isErrorPage="true" %> 	
		<table class="tablaTitulo" width="100%" align="center"> 
			<tr>  
				<td class="titulosPeq" align=center>
					<b><siga:Idioma key="messages.error.descripcionExcepcion"/></b>
				</td>
			</tr>
			<tr>
				<td class="titulosPeq" align="center">
					Petici�n erronea.
					<%= request.getRequestURI() %>
					<br>
					<siga:Idioma key="messages.general.errorHTTP400"/>
				</td>
			</tr>
<!--
			<tr>
				<td align=center>
					<siga:Idioma key="messages.error.SIGA"/>
				</td>
			</tr>
	-->		
			<tr><td><br></td></tr>
		</table> 
<!--
		<center>
			<br>
			<img src="<%=app%>/html/imagenes/cgae.jpg" border="0">
			<br><br>
			<img src="<%=app%>/html/imagenes/atos.jpg" border="0">
			<br>
		</center>
-->
	</body>
</html>
