<!-- buscarListadoSolicitudes.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.*"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	String sTemp = (String)request.getAttribute("idsTemp");
	
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">

		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

		<script>
			function generar(fila)
			{
	
				SolicitudesCertificadosForm.idsTemp.value=SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value;
				
				SolicitudesCertificadosForm.modo.value="asignarPlantillaCertificado";
				
				var resultado = ventaModalGeneral("SolicitudesCertificadosForm", "P");
				if (resultado!=undefined)
				{
					SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value=SolicitudesCertificadosForm.idsParaGenerarFicherosPDF.value + "%%" + resultado;
					SolicitudesCertificadosForm.action="<%=app%>/CER_GestionSolicitudes.do";
					SolicitudesCertificadosForm.modo.value="generarPDF";
					SolicitudesCertificadosForm.target="submitArea";
					SolicitudesCertificadosForm.submit();
				}
			}



		</script>
	</head>

	<body class="tablaCentralCampos" onload="generar();">
		<html:form action="/CER_GestionSolicitudes.do" method="POST" target="resultado">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>

			<input type="hidden" name="idsParaGenerarFicherosPDF" value="<%=sTemp%>">
			<input type="hidden" name="idsTemp" value="">
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>

				
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>