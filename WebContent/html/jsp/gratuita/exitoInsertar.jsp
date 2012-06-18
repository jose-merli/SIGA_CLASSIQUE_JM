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

<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));	
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje="", modo="", idPartido="";
	
	try {
	mensaje = (String)request.getAttribute("mensaje");
	modo = (String)request.getAttribute("modo");
	idPartido = (String)request.getAttribute("idPartido");
	} catch(Exception e) {
			mensaje="aa";
			modo="editar";
			idPartido="1";
	}
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script type="text/javascript">
	function reloadPage() {
	<%  if (mensaje!=null){	
		String msg=UtilidadesString.getMensajeIdioma(userBean.getLanguage(),mensaje);
	%>
		var type = '<%=msg%>';
		alert(type);
		document.forms[0].submit();
	<%  } %>
	
	}

</script>
</head>
<body onload="reloadPage()"></body>
<html:form action="/JGR_MantenimientoPartidosJudiciales.do" target="mainWorkArea" >
	<html:hidden property = "modo" value = "<%=modo%>"/>
	<html:hidden property = "idPartido" value = "<%=idPartido%>"/>
</html:form>

</html:html>