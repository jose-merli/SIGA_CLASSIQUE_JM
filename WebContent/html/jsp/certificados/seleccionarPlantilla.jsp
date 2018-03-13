<!DOCTYPE html>
<html>
<head>
<!-- buscarListadoSolicitudes.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
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


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

		<script>
			function generar(fila)
			{
				sub();
				CerDetalleSolicitudForm.idsTemp.value=CerDetalleSolicitudForm.idsParaGenerarFicherosPDF.value;
				CerDetalleSolicitudForm.modo.value="asignarPlantillaCertificado";
				var resultado = ventaModalGeneral("CerDetalleSolicitudForm", "P");
				if (resultado!=undefined) {
					CerDetalleSolicitudForm.idsParaGenerarFicherosPDF.value=CerDetalleSolicitudForm.idsParaGenerarFicherosPDF.value + "||" + resultado;
					CerDetalleSolicitudForm.action="<%=app%>/CER_DetalleSolicitud.do";
					CerDetalleSolicitudForm.modo.value="generarPDF";
					CerDetalleSolicitudForm.target="submitArea";
					CerDetalleSolicitudForm.submit();
				} else {
					fin();					
				}
				
			}



		</script>
	</head>

	<body class="tablaCentralCampos" onload="generar();">
		<html:form action="/CER_DetalleSolicitud.do" method="POST" target="resultado">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			<html:hidden property="idInstitucion" styleId="idInstitucion"/>
			<html:hidden property="idSolicitud" styleId="idSolicitud" />
			<html:hidden property="fechaSolicitud" styleId="fechaSolicitud" />
			<html:hidden property="idInstitucionOrigen" styleId="idInstitucionOrigen" />
			<html:hidden property="idInstitucionDestino" styleId="idInstitucionDestino" />
			<html:hidden property="fechaEmision" styleId="fechaEmision" />
			<html:hidden property="fechaDescarga" styleId="fechaDescarga" />
			<html:hidden property="fechaSolicitud" styleId="fechaSolicitud" />
			<html:hidden property="fechaCobro" styleId="fechaCobro" />
			<html:hidden property="codigoBanco" styleId="codigoBanco" />
			<html:hidden property="sucursalBanco" styleId="sucursalBanco" />
			<html:hidden property="fechaEntregaInfo" styleId="fechaEntregaInfo" />
			<html:hidden property="idInstitucionOrigen" styleId="idInstitucionOrigen" />
			<html:hidden property="idInstitucionColegiacion" styleId="idInstitucionColegiacion" />
			<html:hidden property="idInstitucionDestino" styleId="idInstitucionDestino" />
			<html:hidden property="comentario" styleId="comentario" />
			<html:hidden property="metodoSolicitud" styleId="metodoSolicitud" />
			<html:hidden property="idPersonaSolicitante" styleId="idPersonaSolicitante" />
			<html:hidden property="aceptaCesionMutualidad" styleId="aceptaCesionMutualidad" />
			<html:hidden property="regenerar" styleId="regenerar" />
			
			<input type="hidden" name="idsParaGenerarFicherosPDF" value="<%=sTemp%>">
			<input type="hidden" name="idsTemp" value="">
			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
		</html:form>

				
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>