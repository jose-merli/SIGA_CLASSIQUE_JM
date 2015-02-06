<!DOCTYPE html>
<html>
<head>
<!-- facturaPagosCaja.jsp -->

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
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 

	String  idFactura = (String)request.getAttribute("idFactura");
	Integer idInstitucion = (Integer)request.getAttribute("idInstitucion");
	Double importePendiente = (Double)request.getAttribute("importePendiente");
	String ultimaFecha = (String)request.getAttribute("ultimaFecha");
	
	importePendiente = new Double(UtilidadesNumero.redondea(importePendiente.doubleValue(),2));
	
	String fechaActual = UtilidadesBDAdm.getFechaBD("");
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		// Asociada al boton Volver
		function accionCerrar(){ 
			window.top.close();
			return 0;
		}	
	
		// Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {

			// Validamos los datos			
			if (document.GestionarFacturaForm.datosPagosCajaObservaciones.value.length < 1) {
				var mensaje = "<siga:Idioma key="facturacion.pagosFactura.Caja.literal.Observaciones"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return;
			}
			
			if (document.GestionarFacturaForm.datosPagosCajaObservaciones.value.length > 255) {
				var mensaje = "<siga:Idioma key="messages.censo.historico.motivoLargo"/>";
				alert (mensaje);
				return;
			}

			if (document.GestionarFacturaForm.datosPagosCajaFecha.value.length < 1) {
				var mensaje = "<siga:Idioma key="facturacion.pagosFactura.Caja.literal.Fecha"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return;
			}
			
			var ultimaFecha = "<%=ultimaFecha%>";
			if (compararFecha (document.GestionarFacturaForm.datosPagosCajaFecha, ultimaFecha) > 1) {
				mensaje = 'La fecha debe ser mayor o igual que: '+ultimaFecha;
				alert(mensaje);
				return false;
			}

			document.GestionarFacturaForm.datosPagosCajaImporteCobrado.value = document.GestionarFacturaForm.datosPagosCajaImporteCobrado.value.replace(/,/,".");
			var importeCobrado = document.GestionarFacturaForm.datosPagosCajaImporteCobrado.value;

		  	if (isNaN(importeCobrado)) {
				var mensaje = "<siga:Idioma key="facturacion.pagosFactura.Caja.literal.ImporteCobrado"/> <siga:Idioma key="messages.campoNumerico.error"/>";
				alert (mensaje);
		   		return false;
		  	}

			if (eval(importeCobrado) <= 0) {
				var mensaje = "<siga:Idioma key="facturacion.pagosFactura.Caja.literal.ImporteCobrado"/> no puede ser inferior o igual a 0";
				alert (mensaje);
				return false;
			}
		
			var importePendiente = <%=importePendiente%>;
			if (importeCobrado > importePendiente) {
				var mensaje = "<siga:Idioma key="facturacion.pagosFactura.Caja.literal.ImporteCobrado"/> no puede ser superior a " + importePendiente;
				alert (mensaje);
				return false;
			}
		
			document.GestionarFacturaForm.modo.value = "insertarPagoPorCaja";
			document.GestionarFacturaForm.submit();
			return 1;
		}		
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
				<td id="titulo" class="titulitosDatos"><siga:Idioma key="facturacion.pagosFactura.Caja.Titulo"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/FAC_PagosFactura.do" method="POST" target="submitArea">
		
		<html:hidden property = "modo" 					value = ""/>
		<html:hidden property = "idInstitucion" value = "<%=String.valueOf(idInstitucion)%>"/>
		<html:hidden property = "idFactura"			value = "<%=idFactura%>"/>
		
		<table class="tablaCentralCamposPeque" align="center">
			<tr>
				<td>
					<table class="tablaCampos" border="0">
						<tr>
							<td class="labelText" width="210px"><siga:Idioma key="facturacion.pagosFactura.Caja.literal.Observaciones"/>&nbsp;(*)</td>
							<td>
								<html:textarea property="datosPagosCajaObservaciones" 
								onkeydown="cuenta(this,4000)" onchange="cuenta(this,4000)" 
								style="overflow-y:auto; overflow-x:hidden; width:250px; height:50px; resize:none;"
								styleClass="box" value=""/></td>
						</tr>
						<tr>
							<td class="labelText"><siga:Idioma key="facturacion.pagosFactura.Caja.literal.Fecha"/></td>
							<td><siga:Fecha  nombreCampo= "datosPagosCajaFecha" valorInicial="<%=fechaActual %>" posicionX="50px" posicionY="10px"/></td>
						</tr>
						<tr>
							<td class="labelText"><siga:Idioma key="facturacion.pagosFactura.Caja.literal.ImportePendiente"/></td>
							<td class="labelTextNum"><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(importePendiente.doubleValue()))%>&nbsp;&euro;</td>
						</tr>
						<tr>
							<td class="labelText"><siga:Idioma key="facturacion.pagosFactura.Caja.literal.ImporteCobrado"/></td>
							<td><input type="text" name="datosPagosCajaImporteCobrado"  maxlength="10" size="10" style="boxNumber" value="0"/>&nbsp;&euro;
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
	<!-- FIN: CAMPOS -->

	<siga:ConjBotonesAccion botones='C,Y' modo='' modal="P" />

<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>