<!DOCTYPE html>
<html>
<head>
<!-- blank.jsp -->
 <%@ page pageEncoding="ISO-8859-1"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ page import="com.atos.utils.UsrBean"%>
<% 
	String app=request.getContextPath();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	if (usr==null) usr=UsrBean.UsrBeanAutomatico("2000");

	String lenguaje = usr.getLanguage();
%>


	
		<meta http-equiv="Expires" content="0">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	    <title><siga:Idioma key="index.title"/></title>

<style>
.labelEntrada{
	text-align: left;
	font-family: Arial;
	color:#000000;
	font-size: 12px;
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 2px;
	padding-bottom: 1px;
	vertical-align: top;
} 
</style>	    
	    <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	</head>
	<body style="background-color:#ffffff;">
	
	<div style="position:absolute;width:100%;">
		<% if (lenguaje.equals("2")) { // en catalan %>	
			<img src="<%=app%>/html/imagenes/logoSIGAblanco_CA.JPG" style="display:block;margin-left:auto;margin-right:auto;" />
		<% } else { %>	
			<img src="<%=app%>/html/imagenes/logoSIGAblanco.JPG" style="display:block;margin-left:auto;margin-right:auto;" />
		<% } %>	
	</div>
	
	<div style="position:absolute;left:20px;bottom:20px;">
	<siga:Idioma key="messages.entrada.portada"/>
	<br><br>
	<img src="<%=app%>/html/imagenes/logoRedAbogacia.jpg" aling="center" valign="bottom" />
	</div>
	
	<div style="position:absolute;right:20px;bottom:20px;">
	<siga:Idioma key="messages.entrada.escrito"/>
	<br><br>
	<img src="<%=app%>/html/imagenes/logoMinisterio.jpg"  aling="center" valign="bottom"/>
	<img src="<%=app%>/html/imagenes/logoAvanza.jpg"  aling="center" valign="bottom"/>
	</div>
	
	</body>
</html>