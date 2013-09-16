<!DOCTYPE html>
<html>
<head>
<!-- facturaPagosRenegociar.jsp -->
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
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.beans.ConsPLFacturacion"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.CenCuentasBancariasBean"%>
<%@ page import="com.atos.utils.UsrBean"%>



<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = ((UsrBean)request.getSession().getAttribute(("USRBEAN")));	

	Double importePendiente = (Double) request.getAttribute("importePendiente");
	FacFacturaBean factura = (FacFacturaBean) request.getAttribute("factura");
	Integer estadoFactura = (Integer) request.getAttribute("estadoFactura");
	String mensaje = (String) request.getAttribute("mensaje");

	String numeroCuenta = (String) request.getAttribute("numeroCuenta");
	String codigoEntidad = (String) request.getAttribute("codigoEntidad");
	String cuentaBancariaFactura = (String) request.getAttribute("cuentaBancariaFactura");	
	String idCuentaUnica = (String) request.getAttribute("idCuentaUnica");		
	String numerocuentaBancariaUnica = (String) request.getAttribute("numerocuentaBancariaUnica");		

	String idFactura = "";
	Integer idInstitucion = new Integer(0);
	String parametro[] = new String[2];
	boolean formaPagoActualPorBanco = false;

	if (factura != null) {
		Integer idCuentaDeudor = (Integer) factura.getIdCuentaDeudor();		
		idInstitucion = factura.getIdInstitucion();
		idFactura = factura.getIdFactura();

		if (idCuentaDeudor != null) {
			parametro[0] = String.valueOf(factura.getIdPersonaDeudor());
		} else {
			parametro[0] = String.valueOf(factura.getIdPersona());
		}
		parametro[1] = String.valueOf(idInstitucion);
	}

	String descripcionFormaPagoActual = "";
	String radioMismaCuentaMarcado = "checked", radioPorCajaMarcado = "", radioPorBancoOtra = "", radioPorBancoUnica = "";
	if (estadoFactura != null) {
		if (estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA)) {
			descripcionFormaPagoActual = "facturacion.pagosFactura.Renegociar.literal.FormaPagoActualCaja";
			if (cuentaBancariaFactura == "") {
				radioMismaCuentaMarcado = "";
				if (idCuentaUnica == "") {
					radioPorBancoUnica = "checked";
					radioPorBancoOtra = "checked";
				} else {
					radioPorBancoUnica = "";
					radioPorBancoOtra = "";
				}
			}
			
		} else {
			if (estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_BANCO)) {
				if (factura.getIdFormaPago().intValue() == ClsConstants.TIPO_FORMAPAGO_FACTURA) {
					descripcionFormaPagoActual = "facturacion.pagosFactura.Renegociar.literal.FormaPagoActualPendiente";
					if (cuentaBancariaFactura == "") {
						radioPorBancoOtra = "checked";
						radioMismaCuentaMarcado = "";
					}
					
					if ((idCuentaUnica == "") || (idCuentaUnica == null)) {
						radioPorBancoUnica = "checked";
					}
				} else {
					descripcionFormaPagoActual = "facturacion.pagosFactura.Renegociar.literal.FormaPagoActualPendienteCaja";
					radioPorCajaMarcado = "checked";
				}
			}
			
			if (estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA)) {
				descripcionFormaPagoActual = "facturacion.pagosFactura.Renegociar.literal.FormaPagoActualDevuelta";
			}
		}
	}
	
	if (importePendiente != null) {
		importePendiente = new Double(UtilidadesNumero.redondea(importePendiente.doubleValue(), 2));
	}
%>


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<%=app%>/html/jsp/general/validacionSIGA.jsp"></script>

	<script type="text/javascript">

		// Asociada al boton Volver
		function accionCerrar(){ 
			window.top.close();
		}	
		
		function onload(){		
<%  
			if (mensaje!=null){
				String msg=UtilidadesString.escape(UtilidadesString.getMensajeIdioma(userBean.getLanguage(),mensaje));
				String estilo="notice";
				if(mensaje.contains("error")){
					estilo="error";
				} else if(mensaje.contains("success")||mensaje.contains("updated")){
					estilo="success";
				} 
%>
				alert(unescape("<%=msg %>"),"<%=estilo%>");		
<%
			}
%>	
		}		
	
		// Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {
			// Validamos los datos
			var i;
			for (i = 0; i < document.GestionarFacturaForm.datosPagosRenegociarNuevaFormaPago.length; i++) {
				if (document.GestionarFacturaForm.datosPagosRenegociarNuevaFormaPago[i].checked)
					break;
			}
			
			if (document.GestionarFacturaForm.radio3.checked && document.GestionarFacturaForm.datosPagosRenegociarIdCuenta.value == "") {
				var mensaje = '<siga:Idioma key="facturacion.pagosFactura.Renegociar.Obligatoria.Cuenta"/>';
				alert(mensaje);
				return 0;
			}				

<%
			if ((estadoFactura != null)
				&& (estadoFactura.intValue() == Integer.parseInt(ClsConstants.ESTADO_FACTURA_CAJA))) {
%>
				if (document.GestionarFacturaForm.datosPagosRenegociarNuevaFormaPago[i].value == "mismaCuenta") {
					var mensaje = '<siga:Idioma key="facturacion.pagosFactura.Renegociar.Error.MismaCuenta"/>';
					alert(mensaje);
					return 0;
				}
<%
			}
%>

			document.GestionarFacturaForm.modo.value = "insertarRenegociar";
			document.GestionarFacturaForm.target = "submitArea";	
			document.GestionarFacturaForm.submit();
		}				
	</script>	
</head>

<body onload="onload();">
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" height="32px">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="facturacion.pagosFactura.Renegociar.Titulo"/></td>
		</tr>
	</table>
	
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/FAC_PagosFactura.do" method="POST" >		
		<html:hidden property="modo" value=""/>
		<html:hidden property="idInstitucion" value="<%=String.valueOf(idInstitucion)%>"/>
		<html:hidden property="idFactura" value="<%=idFactura%>"/>
		<html:hidden property="datosPagosRenegociarEstadoFactura" value="<%=String.valueOf(estadoFactura)%>"/>
		<html:hidden property="datosPagosRenegociarImportePendiente" value="<%=String.valueOf(importePendiente)%>"/>
		<html:hidden property="idCuentaUnica" value="<%=idCuentaUnica%>"/>		
		
		<table class="tablaCentralCamposMedia" align="center">
			<tr>
				<td>
					<fieldset>
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.ImportePendiente"/>
								</td>
								<td  class="labelTextValue">									
									&nbsp;<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(importePendiente.doubleValue()))%>&nbsp;&euro;
								</td>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.FormaPago"/>
								</td>
								<td class="labelTextValue">									
							 		<siga:Idioma key="<%=descripcionFormaPagoActual%>"/>
<% 
									if (!radioPorCajaMarcado.equals("checked")) {
%>
							 			&nbsp; 
							 			<%=codigoEntidad%>
							 			<%=UtilidadesString.mostrarNumeroCuentaConAsteriscos(numeroCuenta)%>
<%
									}
%>						
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>

			<tr>
				<td>
					<siga:ConjCampos leyenda="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago">
						<table border="0" cellpadding="5" cellspacing="0">						
							<tr>
								<td>
									<input type="radio" id="radio1" name="datosPagosRenegociarNuevaFormaPago" value="mismaCuenta"
										<%=radioPorCajaMarcado.equals("checked") ? "disabled=disabled" : ""%>
										<%=radioPorBancoOtra.equals("checked") ? "disabled=disabled" : ""%>
										<%=radioMismaCuentaMarcado%>>
								</td>
								<td class="labelText" width="330px">
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.MismaCuenta" />
								</td>
								<td class="labelText" style="text-align:left;" width="250px">
									<%=UtilidadesString.mostrarDatoJSP(cuentaBancariaFactura)%>
								</td>							
							</tr>
							
							<tr>
								<td>
									<input type="radio" id="radio2" name="datosPagosRenegociarNuevaFormaPago" value="porBanco"
										<%=radioPorBancoUnica.equals("checked") ? "disabled=disabled" : ""%>>
								</td>
								<td class="labelText">
									<siga:Idioma key="facturacion.facturas.cuentabancaria.unica" />
								</td>								
								<td class="labelText" style="text-align:left;">
									<%=UtilidadesString.mostrarDatoJSP(numerocuentaBancariaUnica)%>
								</td>	
							</tr>
							
							<tr>
								<td>
									<input type="radio" id="radio3" name="datosPagosRenegociarNuevaFormaPago" value="porOtroBanco" <%=radioPorBancoOtra%>>
								</td>
								<td class="labelText">
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorBanco" />
								</td>									
								<td class="labelText" style="text-align: left;">
									<siga:ComboBD nombre="datosPagosRenegociarIdCuenta" tipo="cuentaCargo" clase="boxCombo" obligatorio="false" parametro="<%=parametro%>" />
								</td>
							</tr>
							
							<tr>
								<td>
									<input type="radio" id="radio4" name="datosPagosRenegociarNuevaFormaPago" value="porCaja" <%=radioPorCajaMarcado%>>
								</td>
								<td class="labelText">
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorCaja" />
								</td>
							</tr>							
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		
			<tr>
				<td>
					<fieldset>
						<table border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.Observaciones"/>
								</td>
								<td>
									<html:textarea property="datosPagosRenegociarObservaciones" 
										onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" 
										styleClass="box" value="" 
										style="overflow-y:auto; overflow-x:hidden; resize:none; width:530px; height:80px;"/>
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</table>		
	</html:form>
	<!-- FIN: CAMPOS -->

	<siga:ConjBotonesAccion botones='C,Y' modo='' modal="M" />
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>