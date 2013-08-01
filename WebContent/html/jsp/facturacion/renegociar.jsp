<!DOCTYPE html PUBLIC "-//IETF//DTD HTML 2.0//EN">
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
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.beans.ConsPLFacturacion"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.CenCuentasBancariasBean"%>

<%
	String app = request.getContextPath();

	FacFacturaBean factura = (FacFacturaBean) request
			.getAttribute("factura");
	Integer estadoFactura = (Integer) request
			.getAttribute("estadoFactura");
	String pagoBanco = (String) request
			.getAttribute("pagoBanco");	

	Integer idCuentaDeudor = (Integer) factura.getIdCuentaDeudor();

	String radioPorBancoOtra = "";
	
	String idFactura = "";
	Integer idInstitucion = new Integer(0);
	String parametro[] = new String[2];
	boolean formaPagoActualPorBanco = false;

	if (factura != null) {
		idInstitucion = factura.getIdInstitucion();
		idFactura = factura.getIdFactura();

		if (idCuentaDeudor != null) {
			parametro[0] = String.valueOf(factura.getIdPersonaDeudor());
		} else {
			parametro[0] = String.valueOf(factura.getIdPersona());
		}

		parametro[1] = String.valueOf(idInstitucion);
	}
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script language="JavaScript">

		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 
			window.top.close();
			return 0;
		}	
	
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
			sub();
			document.GestionarFacturaForm.submit();
		}		
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
				<td id="titulo" class="titulitosDatos"><siga:Idioma key="facturacion.pagosFactura.Renegociar.Titulo"/></td>
		</tr>
	</table>
<bean:define id="datosFacturas" name="datosFacturas"  scope="request"/>
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/FAC_PagosFactura" method="POST" target="submitArea">
		<html:hidden property = "modo" 	value = "renegociar"/>
		<html:hidden property="datosFacturas" value="${datosFacturas}"/>
		
		<table class="tablaCentralCamposMedia" align="center">
			<tr>
				<td colspan="2">
					<siga:ConjCampos leyenda="facturacion.pagosFactura.Renegociar.Titulo">
					<table class="tablaCampos" border="0">
						<tr>
							<td class="labelText"><siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago"/>&nbsp;&euro;&nbsp;(*)</td>
						</tr>
							<c:if test="${pagoBanco=='0' }">
							<tr>
								<td><input type="radio" id="radio1"
									name="datosPagosRenegociarNuevaFormaPago" value="porOtroBanco" disabled="disabled"/>
									<siga:Idioma key="facturacion.facturas.cuentabancaria.otra"/>
								</td>									
								<td class="labelText" style="text-align: left;" ><siga:ComboBD readonly="true"
									nombre="datosPagosRenegociarIdCuenta" tipo="cuentaCargo"
									clase="boxCombo" obligatorio="false"  parametro="<%=parametro%>" /></td>

							</tr>
							</c:if>
							<c:if test="${pagoBanco=='1' }">
							<tr>
								<td><input type="radio" id="radio1"
									name="datosPagosRenegociarNuevaFormaPago" value="porOtroBanco" checked="checked" />
									<siga:Idioma key="facturacion.facturas.cuentabancaria.otra"/>
								</td>									
								<td class="labelText" style="text-align: left;" ><siga:ComboBD
									nombre="datosPagosRenegociarIdCuenta" tipo="cuentaCargo"
									clase="boxCombo" obligatorio="false" parametro="<%=parametro%>" /></td>

							</tr>							
							</c:if>
							<tr>
								<td>
								<input type="radio" id="radio2" name="datosPagosRenegociarNuevaFormaPago" value="porCaja" >
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorCaja"/>
								<br>
								
							</td>
						</tr>
					</table>
					</siga:ConjCampos>
				</td>
			</tr>
		
			<tr>
				<td>
				<br>
				<fieldset>
					<table class="tablaCampos" border="0">
						<tr>
							<td class="labelText"><siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.Observaciones"/></td>
							<td class="labelText"><html:textarea property="datosPagosRenegociarObservaciones" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" cols="80" rows="3" styleClass="box" value=""/></td>
						</tr>
					</table>
				</fieldset>
				</td>
			</tr>

		</table>
	</html:form>
	<!-- FIN: CAMPOS -->

		<siga:ConjBotonesAccion botones='C,Y' modo='' modal="M" />	

<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
<iframe name="submitArea" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>