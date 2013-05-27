<!-- exitoSolicitudIncorporacionDatos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>

<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje = (String)request.getAttribute("mensaje");
	String modal = (String)request.getAttribute("modal");
	String sinrefresco = (String)request.getAttribute("sinrefresco");
	// claves de registro insertado
	String idSolicitud = (String)request.getAttribute("idSolicitud");
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<script type="text/javascript">
		function reloadPage() {
			<%if (mensaje!=null){%>
				var type = '<siga:Idioma key="<%=mensaje%>"/>';
				alert(type);
			<%}%>
			document.SolicitudIncorporacionForm.target="modal";
			document.SolicitudIncorporacionForm.modo.value="editar";
			document.SolicitudIncorporacionForm.submit();
		} 
	</script>
	
</head>
<body onload="reloadPage();">

	<html:form action="/CEN_SolicitudesIncorporacion.do" method="POST">
		<html:hidden property = "modo" value = "editar"/>
		<html:hidden property = "editarIdSolicitud" value = "<%=idSolicitud%>"/>
	</html:form>
</body>
</html:html>


