<!DOCTYPE html>
<html>
<head>
<!-- detallePagoColegiado.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>



<%@ page import="java.util.*"%>

<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>

<%@page import="com.siga.Utilidades.UtilidadesHash"%>



<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	Hashtable filaDetalle = (Hashtable) request
			.getAttribute("detallePago");
	//Strings recuperados de los registros
	String importeSjcs = UtilidadesHash.getString(filaDetalle,
			"TOTALIMPORTESJCS");
	String importeRetencion = UtilidadesHash.getString(filaDetalle,
			"IMPORTETOTALRETENCIONES");
	String importeMvtos = UtilidadesHash.getString(filaDetalle,
			"IMPORTETOTALMOVIMIENTOS");
	String importeIRPF = UtilidadesHash.getString(filaDetalle,
			"TOTALIMPORTEIRPF");

	
	String totalBrutos = UtilidadesHash.getString(filaDetalle,	"BRUTO");
	String totalRetenido = UtilidadesHash.getString(filaDetalle,"TOTALRETENIDO");
	String neto = UtilidadesHash.getString(filaDetalle,	"NETO");
%>









<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

<!-- INICIO: SCRIPTS BOTONES -->
<script language="JavaScript">	
	
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				window.top.close();
			}
			
		</script>

<!-- FIN: SCRIPTS BOTONES -->

</head>

<body>



<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0">
	<tr>
		<td id="titulo" class="titulitosDatos"><siga:Idioma
			key="informes.sjcs.pagos.envio.detalle.titulo" /></td>
	</tr>
</table>


<table class="tablaCentralCampos" align="center">
	<tr>
		<td><siga:ConjCampos leyenda="informes.sjcs.pagos.envio.detalle.titulo">
			<table>
				<tr>
					<td class="labelText"><siga:Idioma
						key="informes.sjcs.pagos.envio.detalle.importeSjcs" />:</td>

					<td class="labelTextValue"><%=UtilidadesNumero.formatoCampo(importeSjcs)%></td>
					<td class="labelText"><siga:Idioma
						key="informes.sjcs.pagos.envio.detalle.importeMvtos" />:</td>
					<td class="labelTextValue"><%=UtilidadesNumero.formatoCampo(importeMvtos)%></td>
					<td class="labelText"><siga:Idioma
						key="informes.sjcs.pagos.envio.detalle.totalBrutos" />:</td>
					<td class="labelTextValue" align="right"><%=UtilidadesNumero.formatoCampo(totalBrutos)%>&nbsp;&euro;</td>
					<td></td>
				</tr>
				<tr>

					<td class="labelText"><siga:Idioma
						key="informes.sjcs.pagos.envio.detalle.importeIRPF" />:</td>
					<td class="labelTextValue"><%=UtilidadesNumero.formatoCampo(importeIRPF)%></td>
					<td class="labelText"><siga:Idioma
						key="informes.sjcs.pagos.envio.detalle.importeRetencion" />:</td>
					<td class="labelTextValue"><%=UtilidadesNumero.formatoCampo(importeRetencion)%></td>
					<td class="labelText"><siga:Idioma
						key="informes.sjcs.pagos.envio.detalle.totalRetenido" />:</td>
					<td class="labelTextValue" align="right"><%=UtilidadesNumero.formatoCampo(totalRetenido)%>&nbsp;&euro;</td>
					<td></td>
				</tr>
				<tr>

					<td class="labelText">&nbsp;</td>
					<td class="labelText">&nbsp;</td>
					<td class="labelText">&nbsp;</td>
					<td class="labelText">&nbsp;</td>
					<td class="labelText"><siga:Idioma
						key="informes.sjcs.pagos.envio.detalle.importeTotal" />:</td>
					<td class="labelTextValue" align="right"><%=UtilidadesNumero.formatoCampo(neto)%>&nbsp;&euro;</td>
					<td></td>
				</tr>


			</table>

		</siga:ConjCampos></td>
	</tr>
</table>

<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
	style="display: none"></iframe>
</body>
</html>

