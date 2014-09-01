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

<!-- IMPORTS -->
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.beans.ConsPLFacturacion"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.CenCuentasBancariasBean"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>



<!-- JSP -->
<%
	String app = request.getContextPath();
	UsrBean userBean = (UsrBean) request.getSession().getAttribute("USRBEAN");	
	
	FacFacturaBean beanFactura = (FacFacturaBean) request.getAttribute("beanFactura");
	CenCuentasBancariasBean beanCuentaBancaria = (CenCuentasBancariasBean) request.getAttribute("beanCuentaBancaria");	
	String ultimaFecha = (String) request.getAttribute("ultimaFecha");
	Integer numeroFacturasPorBanco = (Integer) request.getAttribute("numeroFacturasPorBanco");	
	Integer numeroCuentasPersona = (Integer) request.getAttribute("numeroCuentasPersona");	
	
	String mensaje = (String) request.getAttribute("mensaje");	
	String fechaActual = UtilidadesBDAdm.getFechaBD("");	
	
	String idCuentaUnica = null;
	String idPersona = null;
	if (beanFactura.getIdCuentaDeudor() != null) {		
		idCuentaUnica = String.valueOf(beanFactura.getIdCuentaDeudor());
		idPersona = String.valueOf(beanFactura.getIdPersonaDeudor());
	} else {
		idCuentaUnica = String.valueOf(beanFactura.getIdCuenta());
		idPersona = String.valueOf(beanFactura.getIdPersona());
	}
	
	String parametro[] = new String[2];
	parametro[0] = idPersona;
	parametro[1] = String.valueOf(beanFactura.getIdInstitucion());
	
	String descripcionFormaPagoActual = "";
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
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<%=app%>/html/jsp/general/validacionSIGA.jsp"></script>

	<script type="text/javascript">

		// Asociada al boton Cerrar
		function accionCerrar(){ 
			window.top.close();
			return 0;
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
			
			if (document.GestionarFacturaForm.radio2.checked && document.GestionarFacturaForm.datosPagosRenegociarIdCuenta.value == "") {
				var mensaje = '<siga:Idioma key="facturacion.pagosFactura.Renegociar.Obligatoria.Cuenta"/>';
				alert(mensaje);
				return 0;
			}				

			if (document.GestionarFacturaForm.datosRenegociarFecha.value.length < 1) {
				var mensaje = "<siga:Idioma key="facturacion.pagosFactura.Caja.literal.Fecha"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return 0;
			}
			
			var ultimaFecha = "<%=ultimaFecha%>";
			if (compararFecha (document.GestionarFacturaForm.datosRenegociarFecha, ultimaFecha) > 1) {
				var mensaje = 'La fecha debe ser mayor o igual que: '+ultimaFecha;
				alert(mensaje);
				return 0;
			}			

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
		<html:hidden property="idInstitucion" value="<%=beanFactura.getIdInstitucion().toString()%>"/>
		<html:hidden property="idFactura" value="<%=beanFactura.getIdFactura()%>"/>
		<html:hidden property="datosPagosRenegociarEstadoFactura" value="<%=beanFactura.getEstado().toString()%>"/>
		<html:hidden property="datosPagosRenegociarImportePendiente" value="<%=beanFactura.getImpTotalPorPagar().toString()%>"/>
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

			<tr>
				<td>
					<siga:ConjCampos leyenda="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago">
						<table border="0" cellpadding="5" cellspacing="0">		
						
<%
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

										<td class="labelText">
											<%=UtilidadesString.mostrarIBANConAsteriscos(beanCuentaBancaria.getIban())%>
										</td>	
									</tr>
									
									<tr>
										<td>
											<input type="radio" id="radio2" name="datosPagosRenegociarNuevaFormaPago" value="porOtroBanco"/>
										</td>
										<td class="labelText">
											<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorBanco"/>
										</td>	

										<td class="labelText" style="text-align: left;" >
											<siga:ComboBD nombre="datosPagosRenegociarIdCuenta" tipo="cuentaCargo" clase="boxCombo" obligatorio="false" parametro="<%=parametro%>" />
										</td>
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
								} else { // numeroFacturasPorBanco > 0 && numeroCuentasPersona == 0
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
									
							} else { // numeroFacturasPorBanco == 0
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
								if (numeroCuentasPersona > 0) { // numeroFacturasPorBanco == 0 && numeroCuentasPersona > 0
%>																		
									<tr>
										<td>
											<input type="radio" id="radio2" name="datosPagosRenegociarNuevaFormaPago" value="porOtroBanco" checked="checked"/>
										</td>
										<td class="labelText" >
											<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorBanco"  />
										</td>	

										<td class="labelText" style="text-align: left;" >
											<siga:ComboBD nombre="datosPagosRenegociarIdCuenta" tipo="cuentaCargo" clase="boxCombo" obligatorio="false" parametro="<%=parametro%>" />
										</td>
									</tr>
<%
								} else { // numeroFacturasPorBanco == 0 && numeroCuentasPersona == 0
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
									<siga:Fecha nombreCampo="datosRenegociarFecha" valorInicial="<%=fechaActual%>" posicionX="50px" posicionY="10px"/>
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