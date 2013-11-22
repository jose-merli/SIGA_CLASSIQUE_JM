<!DOCTYPE html>
<html>
<head>
<!-- facturaPagosComprobante.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri="struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri="struts-html.tld" 	prefix = "html"%>

<!-- IMPORTS -->
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.beans.FacPagosPorCajaBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>

<!-- JSP -->
<% 
	String app = request.getContextPath(); 

	FacFacturaBean factura 		= (FacFacturaBean)request.getAttribute("factura");
	FacPagosPorCajaBean pago 	= (FacPagosPorCajaBean)request.getAttribute("pago");
	String nombreInstitucion	= (String)request.getAttribute("nombreInstitucion");
	String nombrePersona			=	(String)request.getAttribute("nombrePersona");
	String numeroColegiado 		= (String)request.getAttribute("numeroColegiado");
	String medioPago 					= (String)request.getAttribute("medioPago");
	
	int altoImpresion = 275; 	// Especifica el alto para la impresion. Valor en pixeles
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		// Asociada al boton Cerrar
		function accionCerrar(){ 
			window.top.close();
			return 0;
		}	
	
		// Asociada al boton Imprimir
		function accionImprimir(){
			var divAreaImpresion = document.getElementById("areaImpresion");
			
			jQuery("#botones").hide();
			divAreaImpresion.style.height = "100%";
			window.print();
			divAreaImpresion.style.height = "<%=altoImpresion%>px";
			jQuery("#botones").show();
		}	
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="facturacion.pagosFactura.Comprobante.Titulo"/>
			</td>
		</tr>
	</table>

	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/FAC_PagosFactura.do" method="POST" target="submitArea">
		<div id='areaImpresion' style='height:<%=altoImpresion%>px; overflow-y:auto'>
			<table border="0">
				<tr>
					<td class="labelText" colspan="2"><%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%></td>
				</tr>
	
				<tr>
					<td class="labelText" width="150"><siga:Idioma key="facturacion.pagosFactura.Comprobante.literal.Fecha"/></td>
					<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("", pago.getFecha()))%></td>
				</tr>
	
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.pagosFactura.Comprobante.literal.Cliente"/></td>
					<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(nombrePersona)%></td>
				</tr>
	
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.pagosFactura.Comprobante.literal.NColegiado"/></td>
					<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(numeroColegiado)%></td>
				</tr>
	
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.pagosFactura.Comprobante.literal.NFactura"/></td>
					<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(factura.getNumeroFactura())%></td>
				</tr>
	
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.pagosFactura.Comprobante.literal.MedioPago"/></td>
					<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(medioPago)%></td>
				</tr>
	
				<tr>
					<td class="labelText"><siga:Idioma key="facturacion.pagosFactura.Comprobante.literal.ImportePagado"/></td>
					<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(pago.getImporte().doubleValue()))%>&nbsp;&euro;</td>
				</tr>
			</table>
		</div>
	</html:form>
	<!-- FIN: CAMPOS -->

	<div id="botones">
		<siga:ConjBotonesAccion botones="I,C" modal="M" clase="botonesDetalle"/>
	</div>
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>