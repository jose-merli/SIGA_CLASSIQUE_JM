<!-- exito.jsp -->
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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));	
    
    //Se detecta si es petición HTTP ó HTTPS.
    String sProtocolo = request.getRequestURL().toString().toLowerCase().indexOf("https")>-1 ? "https" : "http"; 
    String queryString = sProtocolo + "://" + request.getServerName() + ":" + request.getServerPort();
    String scodebase = queryString;// + app + "/html/jsp/general";

%>

</head>

<body>
		
	  <form name="formularioOcultoParaIP" target="submitApplet" action="/SIGA/SIGADireccionIP.svrl">
		    <input type="hidden" name="IPCLIENTE" value="">
	  </form>
	
		
		<iframe name="submitApplet" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none">
		</iframe>

		<!-- APPLET IP -->
		<object classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" width="0" height="0" codebase="<%=scodebase%>">
					<param name="archive" value="AppletDireccionIP.jar">
					<param name="code" value="com.siga.Utilidades.AppletDireccionIP.class">
					<param name="codebase" value="<%=scodebase%>">
					<param name="mayscript" value="true">
		</object>
		<!-- APPLET IP -->	

	<script type="text/javascript" src="<%=app%>/html/js/activateActiveX.js"></script>
		
</body>
</html>