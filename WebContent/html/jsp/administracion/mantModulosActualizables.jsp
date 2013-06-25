<!-- mantModulosActualizables.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.Properties" %>
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession();
		
	String color=(String)request.getAttribute(SIGAConstants.COLOR);
	String messageName = (String)request.getAttribute("descOperation");
%>

<html>
<head>
<title><"Mantenimiento Modulos Actualizables.Titulo"/></title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
<script language="JavaScript">
	
	function aceptar() {
		if(confirm('<siga:Idioma key="messages.confirm.updateData"/>'))
			document.all.confInterfazForm.submit();
		return false;
	}	
	
	function cancelar() {
		if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
			document.all.confInterfazForm.reset();				
		}
		return false;
	}
	
	function crearCampo(nombre) {
		var HTMLCode = "";
		HTMLCode = "<INPUT TYPE=CHECKBOX NAME='" + nombre + "'>";
		document.write(HTMLCode);
	}

	
</script>
</head>
<!-- <body background="<%=app%>/html/imagenes/fondo<%=src.get("color")%>.gif"> -->
<body>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr><td class="rayita"></td></tr>
	</table>
	<p class="titulos" style="text-align:right"><"Mantenimiento Modulos Actualizables.Titulo">&nbsp</p>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr><td class="rayita"></td></tr>
	</table>
	
	<html:form action="/mantModulosActualizables.do" method="POST" target="submitArea" enctype="multipart/form-data">
		<input type="hidden" name="mode" value="update">
		
		<script>crearCampo("hola")</script>
	    		

	
</html:form>

<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!--<iframe name="submitArea"></iframe>-->
</body>
</html>

