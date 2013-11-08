<!DOCTYPE html>
<html>
<head>
<!-- exitoEjecucion.jsp -->
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
<%@ page import="com.siga.beans.ConConsultaAdm"%>



<%
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession();
	
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje = (String)request.getAttribute("mensaje");
	String idConsulta = (String)request.getAttribute("idConsulta");
	String idInstitucion = (String)request.getAttribute("idInstitucion");
	String tipoConsulta = (String)request.getAttribute("tipoConsulta");
	if (mensaje==null) mensaje = "";
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script>
	function reloadPage() {
		<%  if (mensaje!=null){
				String estilo="notice";
				if(mensaje.contains("error")){
					estilo="error";
				}else if(mensaje.contains("success")){
					estilo="success";
				}
				%>
					var type = '<siga:Idioma key="<%=mensaje%>"/>';
					alert(type,"<%=estilo%>");
			<%  } %>
		<%  if (tipoConsulta.equals(ConConsultaAdm.TIPO_CONSULTA_ENV)){%>
				document.forms[0].modo.value="tipoEnvio";
				var tipoenvio = ventaModalGeneral(document.forms[0].name,"P");
				if (tipoenvio!=undefined && tipoenvio!="VACIO" && tipoenvio!=""){
					document.forms[0].tipoEnvio.value=tipoenvio;
				}else{
					return;
				}
		<%  } %>
			document.forms[0].modo.value="criteriosDinamicos";
			var valores = ventaModalGeneral(document.forms[0].name,"G");
			fin();
	}
	</script>

</head>

<body onload="reloadPage();">
		<html:form action="/CON_RecuperarConsultas.do" method="POST" target="submitArea">
			<html:hidden property = "hiddenFrame" value = "1"/>		
			<html:hidden property = "actionModal" value = ""/>
			<html:hidden property="modo" value=""/>	
			<html:hidden property ="idConsulta" value = "<%=idConsulta%>"/>
			<html:hidden property ="idInstitucion" value = "<%=idInstitucion%>"/>
			<html:hidden property ="tipoConsulta" value = "<%=tipoConsulta%>"/>
			<html:hidden property ="tipoEnvio" value = ""/>
		</html:form>
</body>
</html>