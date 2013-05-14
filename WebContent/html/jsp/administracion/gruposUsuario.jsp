<!-- gruposUsuario.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.ClsLogging" %>

<% String app=request.getContextPath(); %>

<html>
	<head>
		<title><siga:Idioma key="index.title"/></title>
		
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
		
		<script language="JavaScript">
		function inicio()
		{
			document.frmGestion.submit();
		}
		</script>
	</head>
	
	<body onLoad="inicio();">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr><td class="rayita"></td></tr>
		</table>

		<p class="titulos" style="text-align:center">GESTIÓN DE GRUPOS DE USUARIOS</p>

		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr><td class="rayita"></td></tr>
		</table>
		
		<form name="frmGestion" target="registros" method="post" action="/SIGA/listadoGruposUsuarios.do">
			<input type="hidden" name="mode" value="listing">
		</form>
		
<IFRAME	src	= "<%=app%>/html/jsp/general/blank.jsp"
		width		= "100%"
		height		= "45%"
		scrolling	= "no"
		frameborder	= "100"
        name= "registros"
		> [Your user agent does not support inline frames or is currently 
              configured not to display frames. </IFRAME> 
<IFRAME	src	= "<%=app%>/html/jsp/general/blank.jsp"
		width		= "100%"
		height		= "41%"
		scrolling	= "no"
		frameborder	= "100"
      	name= "editar"
		> [Your user agent does not support inline frames or is currently 
        configured not to display frames. </IFRAME> 
	</body>
</html>