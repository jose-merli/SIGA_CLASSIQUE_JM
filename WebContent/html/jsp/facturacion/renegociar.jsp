<!DOCTYPE html>
<html>
<head>
<!-- renegociar.jsp -->

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

<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.beans.CenCuentasBancariasBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>

<%
	String app = request.getContextPath();
	UsrBean userBean = (UsrBean) request.getSession().getAttribute("USRBEAN");	

	FacFacturaBean beanFactura = (FacFacturaBean) request.getAttribute("beanFactura");
	CenCuentasBancariasBean beanCuentaBancaria = (CenCuentasBancariasBean) request.getAttribute("beanCuentaBancaria");
	String ultimaFechaPagosFactura = (String) request.getAttribute("ultimaFechaPagosFactura");
	Integer numeroFacturas = (Integer) request.getAttribute("numeroFacturas");
	Integer numeroPersonasFactura = (Integer) request.getAttribute("numeroPersonasFactura");	
	Integer numeroFacturasPorBanco = (Integer) request.getAttribute("numeroFacturasPorBanco");
	
	Integer numeroCuentasPersona = 0;
	if (numeroPersonasFactura == 1) {
		numeroCuentasPersona = (Integer) request.getAttribute("numeroCuentasPersona");				
	}
			
	String mensaje = (String) request.getAttribute("mensaje");	
	String fechaActual = UtilidadesBDAdm.getFechaBD("");	
	
	String parametro[] = new String[2];
	String descripcionFormaPagoActual = "";
	if (beanFactura != null) {
		if (beanFactura.getIdCuentaDeudor() != null) {
			parametro[0] = String.valueOf(beanFactura.getIdPersonaDeudor());
		} else {
			parametro[0] = String.valueOf(beanFactura.getIdPersona());
		}

		parametro[1] = String.valueOf(beanFactura.getIdInstitucion());
		
		if (beanFactura.getEstado() != null) {
			if (beanFactura.getEstado().equals(new Integer(ClsConstants.ESTADO_FACTURA_CAJA))) {
				descripcionFormaPagoActual = "facturacion.pagosFactura.Renegociar.literal.FormaPagoActualCaja";
				
			} else if (beanFactura.getEstado().equals(new Integer(ClsConstants.ESTADO_FACTURA_BANCO))) {
				if (beanFactura.getIdFormaPago().equals(new Integer(ClsConstants.TIPO_FORMAPAGO_FACTURA))) {
					descripcionFormaPagoActual = "facturacion.pagosFactura.Renegociar.literal.FormaPagoActualPendiente";
				} else {
					descripcionFormaPagoActual = "facturacion.pagosFactura.Renegociar.literal.FormaPagoActualPendienteCaja";
				}
				
			} else if (beanFactura.getEstado().equals(new Integer(ClsConstants.ESTADO_FACTURA_DEVUELTA))) {
				descripcionFormaPagoActual = "facturacion.pagosFactura.Renegociar.literal.FormaPagoActualDevuelta";
			}
		}			
	}
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<%=app%>/html/jsp/general/validacionSIGA.jsp"></script>
	
	<script language="JavaScript">
	
		function refrescarLocal() {
			jQuery("#idBotonesAccion").find('input:eq(0)').show();
		}

		// Asociada al boton Cerrar
		function accionCerrar() { 
			window.top.close();
			return 0;
		}	
		
		function onload(){		
<%  
			if (mensaje!=null){
				String msg = UtilidadesString.escape(UtilidadesString.getMensajeIdioma(userBean.getLanguage(),mensaje));
				String estilo = "notice";
				if (mensaje.contains("error")) {
					estilo="error";
				} else if (mensaje.contains("success")||mensaje.contains("updated")) {
					estilo="success";
				} 
%>
				alert(unescape("<%=msg%>"),"<%=estilo%>");		
<%
			}
%>	
			jQuery("#idBotonesAccion").find('input:eq(0)').hide();
		}
		
		// Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {
			if(!jQuery("input[name='datosPagosRenegociarNuevaFormaPago']:checked").val()){
				var mensaje = '<siga:Idioma key="messages.pys.pago.error"/>';
				alert(mensaje);
				return 0;
			}			
			
<%
			if (numeroFacturasPorBanco==0) {
%>
				if (jQuery("input[name='datosPagosRenegociarNuevaFormaPago']:checked").val() == "mismaCuenta") {
					var mensaje = '<siga:Idioma key="facturacion.pagosFactura.Renegociar.Error.MismaCuenta"/>';
					alert(mensaje);
					return 0;
				}
<%
			}
%>			

			if (document.GestionarFacturaForm.radio2.checked && document.GestionarFacturaForm.datosPagosRenegociarIdCuenta != null) {
				if (document.GestionarFacturaForm.datosPagosRenegociarIdCuenta.value == "") {
					var mensaje = '<siga:Idioma key="facturacion.pagosFactura.Renegociar.Obligatoria.Cuenta"/>';
					alert(mensaje);
					return 0;
				}
			}	
			
			if (document.GestionarFacturaForm.datosRenegociarFecha.value.length < 1) {
				var mensaje = "<siga:Idioma key="facturacion.pagosFactura.Caja.literal.Fecha"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return 0;
			}
			
			var ultimaFechaPagosFactura = "<%=ultimaFechaPagosFactura%>";
			if (compararFecha (document.GestionarFacturaForm.datosRenegociarFecha, ultimaFechaPagosFactura) > 1) {
				var mensaje = 'La fecha debe ser mayor o igual que: ' + ultimaFechaPagosFactura;
				alert(mensaje);
				return 0;
			}				
			
			jQuery("#idBotonesAccion").find('input:eq(0)').hide();
			document.GestionarFacturaForm.target = "submitArea";	
			document.GestionarFacturaForm.submit();
		}			
		
		// Asociada al boton Descargar Fichero
		function generarFichero() {
			if (!confirm('<siga:Idioma key="facturacion.renegociacionfacturacion.literal.confirmarDescargaFichero"/>')) {
				return 0;
			}

			document.GestionarFacturaForm.modo.value = "download";
	   		document.GestionarFacturaForm.submit();
		}	
	</script>	
</head>

<body onload="onload();">
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32px">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="facturacion.pagosFactura.Renegociar.Titulo"/></td>
		</tr>
	</table>
	
	<bean:define id="datosFacturas" name="datosFacturas"  scope="request"/>
	
	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/FAC_PagosFactura.do" method="POST">
		<html:hidden property = "modo" 	value = "renegociar"/>
		<html:hidden property="datosFacturas" value="${datosFacturas}"/>
		
		<table class="tablaCentralCamposMedia" align="center">
<%
			if (numeroFacturas == 1) {
%>		
				<tr>
					<td>
						<fieldset>
							<table border="0" cellpadding="5" cellspacing="0">
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.ImportePendiente"/>
									</td>
									<td  class="labelTextValue">									
										&nbsp;<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(beanFactura.getImpTotalPorPagar()))%>&nbsp;&euro;
									</td>
								</tr>
								
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.FormaPago"/>
									</td>
									<td class="labelTextValue">									
								 		<siga:Idioma key="<%=descripcionFormaPagoActual%>"/>
<%
											if (beanCuentaBancaria != null && beanCuentaBancaria.getIban() != null && !beanCuentaBancaria.getIban().equals("")) {
%>
								 			&nbsp; 
								 			<%=UtilidadesString.mostrarIBANConAsteriscos(beanCuentaBancaria.getIban())%>
<%
										}
%>						
									</td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>	
<%
			}
%>	
		
			<tr>
				<td>
					<siga:ConjCampos leyenda="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago">
						<table border="0" cellpadding="5" cellspacing="0">	
<%
							if (numeroPersonasFactura > 1) {
								if (numeroFacturasPorBanco > 0) {
%>								
									<tr>
										<td>
											<input type="radio" id="radio1" name="datosPagosRenegociarNuevaFormaPago" value="mismaCuenta" checked="checked" />
										</td>
										<td class="labelText">
											<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.MismaCuenta"/>
										</td>
									</tr>

									<tr>						
										<td>
											<input type="radio" id="radio2" name="datosPagosRenegociarNuevaFormaPago" value="porOtroBanco" />
										</td>
										<td class="labelText">
											<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorBanco"/>
										</td>									
									</tr>

									<tr>
										<td>
											<input type="radio" id="radio3" name="datosPagosRenegociarNuevaFormaPago" value="porCaja" />
										</td>
										<td class="labelText">
											<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorCaja"/>								
										</td>
									</tr>
<%
								} else { // numeroPersonasFactura > 1 && numeroFacturasPorBanco == 0
%>									

									<tr>
										<td>
											<input type="radio" id="radio1" name="datosPagosRenegociarNuevaFormaPago" value="mismaCuenta" checked="checked" disabled="disabled" />
										</td>
										<td class="labelText">
											<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.MismaCuenta"/>
										</td>
									</tr>

									<tr>						
										<td>
											<input type="radio" id="radio2" name="datosPagosRenegociarNuevaFormaPago" value="porOtroBanco" checked="checked" />
										</td>
										<td class="labelText">
											<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorBanco"/>
										</td>									
									</tr>

									<tr>
										<td>
											<input type="radio" id="radio3" name="datosPagosRenegociarNuevaFormaPago" value="porCaja" disabled="disabled" />
										</td>
										<td class="labelText">
											<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorCaja"/>								
										</td>
									</tr>															
<%
								}
								
							} else { // numeroPersonasFactura == 1
								if (numeroFacturasPorBanco > 0) {
									if (numeroCuentasPersona > 0) {									
%>								
										<tr>
											<td>
												<input type="radio" id="radio1" name="datosPagosRenegociarNuevaFormaPago" value="mismaCuenta" checked="checked"/>
											</td>
											<td class="labelText">
												<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.MismaCuenta"/>
											</td>
<%
											if (numeroFacturas == 1) {
%>										
											<td class="labelText">
												<%=UtilidadesString.mostrarIBANConAsteriscos(beanCuentaBancaria.getIban())%>
											</td>	
<%
											}
%>		
										</tr>
											
										<tr>
											<td>
												<input type="radio" id="radio2" name="datosPagosRenegociarNuevaFormaPago" value="porOtroBanco"/>
											</td>
											<td class="labelText">
												<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorBanco"/>
											</td>	
<%
											if (beanFactura!=null) {
%>											
												<td class="labelText" style="text-align: left;" >
													<siga:ComboBD nombre="datosPagosRenegociarIdCuenta" tipo="cuentaCargo" clase="boxCombo" obligatorio="false" parametro="<%=parametro%>" />
												</td>
<%
											}
%>
										</tr>
											
										<tr>
											<td>
												<input type="radio" id="radio3" name="datosPagosRenegociarNuevaFormaPago" value="porCaja">
											</td>
											<td class="labelText">
												<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorCaja"/>
											</td>
										</tr>							
<%
									} else { // numeroPersonasFactura == 1 && numeroFacturasPorBanco > 0 && numeroCuentasPersona == 0
%>									
										<tr>
											<td>
												<input type="radio" id="radio1" name="datosPagosRenegociarNuevaFormaPago" value="mismaCuenta" disabled="disabled" />
											</td>
											<td class="labelText">
												<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.MismaCuenta"/>
											</td>
										</tr>	
																		
										<tr>
											<td>
												<input type="radio" id="radio2" name="datosPagosRenegociarNuevaFormaPago" value="porOtroBanco" disabled="disabled" />
											</td>
											<td class="labelText">
												<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorBanco"/>
											</td>									
										</tr>
											
										<tr>
											<td>
												<input type="radio" id="radio3" name="datosPagosRenegociarNuevaFormaPago" value="porCaja" checked="checked" />
											</td>
											<td class="labelText">
												<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorCaja"/>
											</td>
										</tr>																
<%
									}			
									
								} else { // numeroPersonasFactura == 1 && numeroFacturasPorBanco == 0
%>																	
									<tr width="100%">
										<td>
											<input type="radio" id="radio1" name="datosPagosRenegociarNuevaFormaPago" value="mismaCuenta" disabled="disabled"/>
										</td>
										<td class="labelText" >
											<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.MismaCuenta"/>
										</td>
									</tr>
<%
									if (numeroCuentasPersona > 0) { // numeroPersonasFactura == 1 && numeroFacturasPorBanco == 0 && numeroCuentasPersona > 0
%>																		
										<tr>
											<td>
												<input type="radio" id="radio2" name="datosPagosRenegociarNuevaFormaPago" value="porOtroBanco" checked="checked"/>
											</td>
											<td class="labelText" >
												<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorBanco"  />
											</td>	
<%
											if (beanFactura!=null) {
%>												
												<td class="labelText" style="text-align: left;" >
													<siga:ComboBD nombre="datosPagosRenegociarIdCuenta" tipo="cuentaCargo" clase="boxCombo" obligatorio="false" parametro="<%=parametro%>" />
												</td>
<%
											}
%>	
										</tr>
<%
									} else { // numeroPersonasFactura == 1 && numeroFacturasPorBanco == 0 && numeroCuentasPersona == 0
%>										
										<tr>
											<td>
												<input type="radio" id="radio2" name="datosPagosRenegociarNuevaFormaPago" value="porOtroBanco" disabled="disabled" />
											</td>
											<td class="labelText">
												<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorBanco"/>
											</td>									
										</tr>															
<%
									}
%>
									<tr>
										<td>
											<input type="radio" id="radio3" name="datosPagosRenegociarNuevaFormaPago" value="porCaja" disabled="disabled" />
										</td>
										<td class="labelText">
											<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorCaja"/>
										</td>
									</tr>
<%
								}
							}
%>							
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
									<siga:Idioma key="facturacion.pagosFactura.Caja.literal.Fecha"/>&nbsp;(*)
								</td>
								<td>
									<siga:Fecha  nombreCampo="datosRenegociarFecha" valorInicial="<%=fechaActual%>" posicionX="50px" posicionY="10px"/>
								</td>
						   </tr>		
						
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.Observaciones"/>
								</td>
								<td>
									<html:textarea property="datosPagosRenegociarObservaciones" 
										onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" 
										styleClass="box" value=""
										style="overflow-y:auto; overflow-x:hidden; resize:none; width:340px; height:60px;"/>
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</table>		
	</html:form>
	<!-- FIN: CAMPOS -->
	
	<siga:ConjBotonesAccion botones='C,Y,GF' modo='' modal="M" />
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>