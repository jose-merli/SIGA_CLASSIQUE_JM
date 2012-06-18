<!-- exitoInsercion.jsp -->
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

<html>
<head>
<%
   
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession();
	
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje = (String)request.getAttribute("mensaje");
	
	String idConsulta = (String)request.getAttribute("idConsulta");
	String idInstitucion = (String)request.getAttribute("idInstitucion");
	String tipoConsulta = (String)request.getAttribute("tipoConsulta");
	String ejecucion = (String)request.getAttribute("ejecucion");
	String consultaExperta = (String)request.getAttribute("consultaExperta");
	
	if (mensaje==null) mensaje = "";
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<script>
	function reloadPage() {
		<%  if (mensaje!=null){%>
			var type = '<siga:Idioma key="<%=mensaje%>"/>';
			alert(type);
		<%  } %>
		document.EditarConsultaForm.submit();
		<%  if (ejecucion!=null){
				if (tipoConsulta.equals(ConConsultaAdm.TIPO_CONSULTA_ENV)){%>
					document.forms[1].modo.value="tipoEnvio";
					var tipoenvio = ventaModalGeneral(document.forms[1].name,"P");
					if (tipoenvio!=undefined && tipoenvio!="VACIO" && tipoenvio!=""){
						document.forms[1].tipoEnvio.value=tipoenvio;
					}else{
						return;
					}
		<%  	} %>
			document.forms[1].modo.value="criteriosDinamicos";
			var valores = ventaModalGeneral(document.forms[1].name,"G");
		<%  } %>
	}
	</script>

</head>

<body onload="reloadPage();">
		<html:form action="/CON_EditarConsulta.do" method="POST" target="mainWorkArea">
			<html:hidden property="modo" value="abrirConParametros"/>
			<html:hidden property ="idConsulta" value = "<%=idConsulta%>"/>
			<html:hidden property ="idInstitucion" value = "<%=idInstitucion%>"/>
			<html:hidden property ="tipoConsulta" value = "<%=tipoConsulta%>"/>
			<html:hidden property ="consultaExperta" value = "<%=consultaExperta%>"/>
		</html:form>
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