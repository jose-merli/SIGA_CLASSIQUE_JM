<!-- exito.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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

<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));	
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje="", idInstitucion="", idSolicitud="", concepto="";
	
	mensaje = (String)request.getAttribute("BOT_mensaje");
	idInstitucion = (String)request.getAttribute("BOT_idInstitucion");
	idSolicitud = (String)request.getAttribute("BOT_idSolicitudCertificado");
	concepto = (String)request.getAttribute("BOT_concepto");
%>

<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
<script type="text/jscript" language="JavaScript1.2">
	function reloadPage() {
	<%  if (mensaje!=null){	
		String msg=UtilidadesString.getMensajeIdioma(userBean.getLanguage(),mensaje);
	%>
		var type = '<%=msg%>';
		alert(type);
		//document.forms[0].submit();
		
	<%  } %>
	
	}
</script>
</head>
<body onload="reloadPage()">
<!--<html:form action="/CER_GestionSolicitudes.do?buscar=true" target="mainWorkArea" >
	<html:hidden property = "modo" value = "abrir"/>
	<html:hidden property = "idSolicitudCertificado" value = "< %=idSolicitud%>"/>
	<html:hidden property = "idInstitucion" value = "< %=idInstitucion%>"/>
	<html:hidden property = "concepto" value = "< %=concepto%>"/>
</html:form>-->
</body>

</html:html>