<!-- facturaPagosRenegociar.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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


<!-- JSP -->
<% 
	String app=request.getContextPath(); 


	Double importePendiente = (Double)request.getAttribute("importePendiente");
	FacFacturaBean factura  = (FacFacturaBean)request.getAttribute("factura");
	Integer estadoFactura   = (Integer)request.getAttribute("estadoFactura");
	String numeroCuenta     = (String)request.getAttribute("numeroCuenta");
	String codigoEntidad     = (String)request.getAttribute("codigoEntidad");
	Integer idCuentaDeudor= (Integer)factura.getIdCuentaDeudor();

	String idFactura = "";
	Integer idInstitucion = new Integer(0);	
	String parametro[] = new String[2];
	boolean formaPagoActualPorBanco = false;

	if (factura != null) {
		idInstitucion = factura.getIdInstitucion();	
		idFactura = factura.getIdFactura();
		
		if (idCuentaDeudor!=null){
	 	  parametro[0] = String.valueOf(factura.getIdPersonaDeudor());
		}else{
		  parametro[0] = String.valueOf(factura.getIdPersona());
		}
	 	parametro[1] = String.valueOf(idInstitucion); 
	}
	
	String descripcionFormaPagoActual = "";
	String radioMismaCuentaMarcado = "checked", radioPorCajaMarcado = "";
	if (estadoFactura != null) {
		if (estadoFactura.intValue() == ConsPLFacturacion.PENDIENTE_COBRO){
			descripcionFormaPagoActual = "facturacion.pagosFactura.Renegociar.literal.FormaPagoActualCaja";
			radioPorCajaMarcado = "checked";
			radioMismaCuentaMarcado = "";
		}
		if (estadoFactura.intValue() == ConsPLFacturacion.RENEGOCIADA) {
			if (factura.getIdFormaPago().intValue()==ClsConstants.TIPO_FORMAPAGO_FACTURA) {
				descripcionFormaPagoActual = "facturacion.pagosFactura.Renegociar.literal.FormaPagoActualPendiente";
			} else {
				descripcionFormaPagoActual = "facturacion.pagosFactura.Renegociar.literal.FormaPagoActualPendienteCaja";
			}
		}
		if(estadoFactura.intValue() == ConsPLFacturacion.DEVUELTA) {
		  descripcionFormaPagoActual = "facturacion.pagosFactura.Renegociar.literal.FormaPagoActualDevuelta";
		}
	}
	importePendiente = new Double(UtilidadesNumero.redondea(importePendiente.doubleValue(),2));
%>


<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 
			window.close();
			return 0;
		}	
	
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {

			// Validamos los datos
			var i;
			for (i = 0; i < document.GestionarFacturaForm.datosPagosRenegociarNuevaFormaPago.length; i++) {
				if (document.GestionarFacturaForm.datosPagosRenegociarNuevaFormaPago[i].checked)
					break;
			}

			<% if ((estadoFactura != null) && (estadoFactura.intValue() == ConsPLFacturacion.PENDIENTE_COBRO)){ %>
				if (document.GestionarFacturaForm.datosPagosRenegociarNuevaFormaPago[i].value == "mismaCuenta") {
					var mensaje = "<siga:Idioma key="facturacion.pagosFactura.Renegociar.Error.MismaCuenta"/>";
					alert(mensaje);
					return 0;
				}
			<% }%>

			if (document.GestionarFacturaForm.datosPagosRenegociarNuevaFormaPago[i].value == "porBanco") {
				if(document.GestionarFacturaForm.datosPagosRenegociarIdCuenta.value == "") {
					var mensaje = "<siga:Idioma key="facturacion.pagosFactura.Renegociar.Error.CuentaNecesaria"/>";
					alert(mensaje);
					return 0;
				}
			}

			document.GestionarFacturaForm.modo.value = "insertarRenegociar";
			document.GestionarFacturaForm.submit();
			return 1;
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

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->


	
	
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/FAC_PagosFactura.do" method="POST" target="submitArea">
		
		<html:hidden property = "modo" 						 value = ""/>
		<html:hidden property = "idInstitucion" 	 value = "<%=String.valueOf(idInstitucion)%>"/>
		<html:hidden property = "idFactura"				 value = "<%=idFactura%>"/>
		<html:hidden property = "datosPagosRenegociarEstadoFactura"		 value = "<%=String.valueOf(estadoFactura)%>"/>
		<html:hidden property = "datosPagosRenegociarImportePendiente" value = "<%=String.valueOf(importePendiente)%>"/>
		
		<table class="tablaCentralCamposMedia" align="center">
			<tr>
				<td colspan="2">
					<br>
					<fieldset>
						<table class="tablaCampos" border="0">
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
								<td  class="labelTextValue">									
								 <siga:Idioma key="<%=descripcionFormaPagoActual%>"/>
								 &nbsp <%=codigoEntidad%><%=UtilidadesString.mostrarNumeroCuentaConAsteriscos(numeroCuenta)%>
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>

			<tr>
				<td colspan="2">
					<siga:ConjCampos leyenda="facturacion.pagosFactura.Renegociar.Titulo">
					<table class="tablaCampos" border="0">
						<tr>
							<td class="labelText"><siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago"/>&nbsp;&euro;&nbsp;(*)</td>
						</tr>
						<tr>
							<td class="labelText" colspan="2">
								<input type="radio" id="radio1" name="datosPagosRenegociarNuevaFormaPago" value="mismaCuenta" <%=radioMismaCuentaMarcado%> >
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.MismaCuenta"/>
								<br>
								<input type="radio" id="radio2" name="datosPagosRenegociarNuevaFormaPago" value="porCaja" <%=radioPorCajaMarcado%> >
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorCaja"/>
								<br>
								<input type="radio" id="radio3" name="datosPagosRenegociarNuevaFormaPago" value="porBanco">
								<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorBanco"/>
								&nbsp;
								<siga:ComboBD nombre="datosPagosRenegociarIdCuenta" tipo="cuentaCargo" clase="boxCombo" obligatorio="false" parametro="<%=parametro%>"/>
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
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>