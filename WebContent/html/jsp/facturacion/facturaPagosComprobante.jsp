<!-- facturaPagosComprobante.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>

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
	
	int anchoImpresion 	  = 600; 	// Especifica el ancho para la impresion. Valor en pixeles
%>


<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Cerrar -->
		function accionCerrar(){ 
			window.top.close();
			return 0;
		}	
	
		<!-- Asociada al boton Imprimir -->
		function accionImprimir(){
			var divBotones = document.getElementById("botones");
			var divAreaImpresion = document.getElementById("areaImpresion");
			
			divBotones.style.visibility = "hidden";
			divAreaImpresion.style.height = "100%";
			window.print();
			divAreaImpresion.style.height = "275px";
			divBotones.style.visibility = "visible";
		}	
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
				<td id="titulo" class="titulitosDatos" width="<%=anchoImpresion%>"><siga:Idioma key="facturacion.pagosFactura.Comprobante.Titulo"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->


	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/FAC_PagosFactura.do" method="POST" target="submitArea">
		

		<div id='areaImpresion' style='height:275px; width:100%; overflow-y:auto'>

		
		<table class=""  border="0" width="<%=anchoImpresion%>">
			<tr>
				<td class="labelTextValue" colspan="2"><%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%></td>
			</tr>

			<tr>
				<td class="labelText" width="150"><siga:Idioma key="facturacion.pagosFactura.Comprobante.literal.Fecha"/></td>
				<td class="labelTextValue"><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateMedium("", pago.getFecha()))%></td>
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

</div>

<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>