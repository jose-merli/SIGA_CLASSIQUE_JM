<!-- administrarPermisos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.atos.utils.UsrBean"%>

<%
	//LMS 19/09/2006
	//Se detecta si es petición HTTP ó HTTPS.
	String sProtocolo = request.getRequestURL().toString().toLowerCase().indexOf("https")>-1 ? "https" : "http"; 
	String app=request.getContextPath(); 
	String queryString = sProtocolo + "://" + request.getServerName() + ":" + request.getServerPort();
%>

<html>
	<head>
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	</head>
	
	<body >
		<%
			String perfil = request.getParameter("idPerfil");
			String scodebase = queryString + app + "/html/jsp/administracion/processTree";
			String sURLservlet = queryString + app + "/SIGASvlPermisosAplicacion.svrl";
			String surlPrefix = queryString + app + "/html/jsp/administracion/processTree/";
			String siconPrefix = surlPrefix + "imagenes/";
			String saccess = ((UsrBean)request.getSession().getAttribute("USRBEAN")).getAccessType();
		%>
		
		<!-- La etiqueta <object> es para ejecutar con la VM de Microsoft -->
		<object classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" width="100%" height="98%" codebase="<%=scodebase%>">
			
			<!-- Los siguientes 3 parámetros (archive, code y codebase) son para ejecutar con la VM de Microsoft -->
			<!-- Con la VM de Sun no hacen falta -->
			<param name="archive" value="AppletPermisos.jar">
			<param name="code" value="com.siga.gui.processTree.SIGAAppletAccess.class">
			<param name="codebase" value="<%=scodebase%>">
			
			<param name="type" value="application/x-java-applet;jpi-version=1.3">
			<param name="URLservlet" value="<%=sURLservlet%>">
			<param name="urlPrefix" value="<%=surlPrefix%>">
			<param name="iconPrefix" value="<%=siconPrefix%>">
			<param name="access" value="<%=saccess%>">
			<param name="dnd" value="0">
			<param name="initAction" value="loadprocess">
			<param name="numberofparams" value="2">
			<param name="param0" value="profile">
			<param name="param1" value="<%=perfil%>">
			<param name="process" value="accessright">
			<param name="icontree" value="imagenes/">
			<param name="progressbar" value="true">
			<param name="boxmessage" value="">
			<param name="boxbgcolor" value="#DEDBE8">
			<param name="boxfgcolor" value="red">
		</object>


	</body>

	
</html>
