<!DOCTYPE html>
<html>
<head>
<!-- administrarPermisos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.atos.utils.UsrBean"%>

<%
	//LMS 19/09/2006
	//Se detecta si es petici�n HTTP � HTTPS.
	String sProtocolo = request.getRequestURL().toString().toLowerCase().indexOf("https")>-1 ? "https" : "http"; 
	String app=request.getContextPath(); 
	String queryString = sProtocolo + "://" + request.getServerName() + ":" + request.getServerPort();
%>


	
		
		<script src="<%=app%>/html/js/SIGA.js?v=${sessionScope.VERSIONJS}" type="text/javascript"></script>	
		<script type="text/javascript">
		jQuery(function(){
			jQuery("embed").attr("height", jQuery("#resultado", window.parent.document).height());
		});
		</script>
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
			
			<!-- Los siguientes 3 par�metros (archive, code y codebase) son para ejecutar con la VM de Microsoft -->
			<!-- Con la VM de Sun no hacen falta -->
			<param name="archive" value="AppletPermisos.jar">
			<param name="code" value="com.siga.gui.processTree.SIGAAppletAccess.class">
			<param name="codebase" value="<%=scodebase%>">
			
			<param name="type" value="application/x-java-applet">
			<param name="java_version" value="1.6.0_18">
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
			<comment>
		      <embed archive="AppletPermisos.jar" 
		      	code="com.siga.gui.processTree.SIGAAppletAccess.class"
		      	codebase="<%=scodebase%>" 
		      	type="application/x-java-applet"
		      	URLservlet="<%=sURLservlet%>"
		      	urlPrefix="<%=surlPrefix%>"
		      	iconPrefix="<%=siconPrefix%>"
		      	access="<%=saccess%>"
		      	dnd="0"
		      	initAction="loadprocess"
		      	numberofparams="2"
		      	param0="profile"
		      	param1="<%=perfil%>"
		      	process="accessright"
		      	icontree="imagenes/"
		      	progressbar="true"
		      	boxmessage=""
		      	boxbgcolor="#DEDBE8"
		      	boxfgcolor="red"
		      	pluginspage="#"
		      	java_version="1.6.0_18"
		        width="100%" height="98%">
		        <noembed>
		          No Java Support.
		        </noembed>
		      </embed>
		    </comment>
		</object>


	</body>

	
</html>
