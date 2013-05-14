<!-- cerrarSesion.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>


<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.BotonesMenu"%>

<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	String pathInicio="";
	if (request.getSession().getAttribute(("USRBEAN"))!=null ){ 
	  UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));	
	  pathInicio = BotonesMenu.getPathCerrarSesion(userBean.getLocation());
	  
	}else{
	  pathInicio = BotonesMenu.getPathCerrarSesion("0");//si se pierde el USERBEAN de la sesion se pasa por defecto la institucion 0
	}
	//Invalido/borro la sesion del usuario:
	request.getSession().invalidate();
%>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
<script type="text/javascript">
	
	function reloadPage() {
			top.location='<%=pathInicio%>';
			return false;
	}
	
</script>

</head>

<body onload="reloadPage();">
Invalidando la sesión...
</body>
</html:html>