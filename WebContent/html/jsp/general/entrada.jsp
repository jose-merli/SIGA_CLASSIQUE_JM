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

<html>
	<head>
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
	    <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	</head>
	<body style="background-color:#ffffff;">
	<table class="labelTextValue" style="height:100%;width:100%;" border="0">
	<tr>
	<td colspan=2 align="center" valign="middle">
<% if (lenguaje.equals("2")) { // en catalan %>	
	<img src="<%=app%>/html/imagenes/logoSIGAblanco_CA.JPG" aling="center" valign="middle" />
<% } else { %>	
	<img src="<%=app%>/html/imagenes/logoSIGAblanco.JPG" aling="center" valign="middle" />
<% } %>	
	</td>
	</tr>
	<tr style="height:100px;">
	<td class="labelEntrada"  style="width:500px">
	<siga:Idioma key="messages.entrada.portada"/>
	<br><br>
	<img src="<%=app%>/html/imagenes/logoRedAbogacia.jpg" aling="center" valign="middle" />
	</td>
	<td class="labelEntrada" style="width:410px">
	<siga:Idioma key="messages.entrada.escrito"/>
	<br><br>
	<img src="<%=app%>/html/imagenes/logoMinisterio.jpg"  aling="center" valign="middle"/>
	<img src="<%=app%>/html/imagenes/logoAvanza.jpg"  aling="center" valign="middle"/>
	</td>
	</tr>
	</table>	
	</body>
</html>