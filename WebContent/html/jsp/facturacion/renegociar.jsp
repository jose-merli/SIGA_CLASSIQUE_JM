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

<!-- JSP -->
<html>

<!-- HEAD -->
<head>

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
						<tr>
							<td class="labelText" colspan="2">
								<input type="radio" id="radio1" name="datosPagosRenegociarNuevaFormaPago" value="mismaCuenta" checked="checked">
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.MismaCuenta"/>
								<br>
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