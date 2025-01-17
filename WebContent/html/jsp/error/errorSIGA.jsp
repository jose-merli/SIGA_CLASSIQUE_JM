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
<%@ page import="com.atos.utils.ClsLogging"%>
<%@ page import="com.atos.utils.UsrBean"%>

<% 
String app=request.getContextPath(); 
HttpSession ses=request.getSession();
UsrBean user = (UsrBean)ses.getAttribute("USRBEAN");

//ClsLogging.writeFileLogError("Error de programa en JSP", (Exception)exception,(UsrBean)user, 3);

%>
	
		<title><siga:Idioma key="index.title"/></title>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	</head>

	<body background="<%=app%>/html/imagenes/fondo2.gif">
		<br><br>
		<h1>
			<center> <siga:Idioma key="messages.general.errorExcepcion"/>
				<img width="10%" src="<%=app%>/html/imagenes/aviso.gif" border="0">
			</center>
		</h1>
		<%@ page isErrorPage="true" %> 	
		<table class="tablaTitulo" width="100%" align="center"> 
			<tr>
				<td class="titulosPeq"  align=center>
					<% if (exception!=null){
						String sal=exception.toString(); 
						%>
						<%=sal%>
					<%}%>
				</td>
			</tr>
			<tr>
				<td class="titulosPeq" align=center>
					<siga:Idioma key="messages.error.SIGA"/>
				</td>
			</tr>

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