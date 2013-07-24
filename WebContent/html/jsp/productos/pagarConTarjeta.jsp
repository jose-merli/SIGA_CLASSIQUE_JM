<!DOCTYPE html>
<html>
<head>
<!-- pagarConTarjeta.jsp -->
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
<%@ page import="com.siga.general.Firma"%>
<%@ page import="com.atos.utils.UsrBean"%>

<!-- JSP -->
<% String app=request.getContextPath(); %>

<%
	//---------------------------------------------------
	//TRATAMIENTO DE LA FIRMA
	//
	
	//Calculo el importe:
	String importe = "2332";
	
	//Calculo el número de la opereación=IDINSTITUCION+IDPERSONA
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String operacion = user.getLocation()+String.valueOf(user.getIdPersona());
	
	//Calculo la firma:
	String valorFirma = "";
	Firma firma = new Firma();
	firma.setClave("11439044");
	firma.setMerchantId("111950028");
	firma.setAcquirerBin("0000554052");
	firma.setTerminalId("00000003");
	firma.setOperacion(operacion);
	firma.setImporte(importe);
	firma.setMoneda("978");
	firma.setExponente("2");
	firma.setReferencia("");
	firma.setFirma(firma.calcularFirma());

	//URL de vuelta para OK y Error:
	String urlOK = "http://localhost:7001/SIGA/html/jsp/general/modalControlTpv.jsp?operacion=OK";
	String urlKO = "http://localhost:7001/SIGA/html/jsp/general/modalControlTpv.jsp?operacion=ERROR";
	//---------------------------------------------------
%>




<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		function campoRelleno (campo, minLength, maxLength, tipoDato) {		
		
			if (campo.value.length < minLength) {
				return false;
			}
			if (campo.value.length > maxLength) {
				return false;
			}
			
			if (tipoDato == "numero") {
			  if (isNaN(campo.value)) {
			   	return false;
			  }
			}
			return true;
		}

		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 
			var datos = new Array();
			datos[0] = 0;		// Boton cerrar
			datos[1] = "";
			window.top.returnValue=datos;
			window.top.close();
		}	
	
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
			//TPV
			document.tpv.submit();			
			//FIN de la prueba del TPV. Comento la validacion de la tarjeta y el cerrar para que lo haga la modalControlTPV.jsp
		
			/*
			// Validamos el numero tarjeta
			if (!campoRelleno(document.GestionSolicitudesForm.pagoTarjetaNumero1, 4, 4, "numero") || 
					!campoRelleno(document.GestionSolicitudesForm.pagoTarjetaNumero2, 4, 4, "numero") ||
					!campoRelleno(document.GestionSolicitudesForm.pagoTarjetaNumero3, 4, 4, "numero") ||
					!campoRelleno(document.GestionSolicitudesForm.pagoTarjetaNumero4, 4, 4, "numero"))	{
				alert ("<siga:Idioma key="pys.solicitudCompra.literal.nTarjeta"/> <siga:Idioma key="messages.campoObligatorio.error"/>");
				return;
			}

			// Validamos la fecha
			if (!campoRelleno(document.GestionSolicitudesForm.pagoTarjetaMes, 2, 2, "numero") ||
					!campoRelleno(document.GestionSolicitudesForm.pagoTarjetaAno, 4, 4, "numero")) {
				alert ("<siga:Idioma key="pys.solicitudCompra.literal.caducidad"/> <siga:Idioma key="messages.campoObligatorio.error"/>");
				return;
			}
			
		  var mes = parseInt(document.GestionSolicitudesForm.pagoTarjetaMes.value,10);
		  if ((mes < 1) || (mes > 12)) {
				alert ("<siga:Idioma key="pys.solicitudCompra.literal.caducidad"/> <siga:Idioma key="messages.campoNoCorrecto.error"/>");
				return;
 	  }

			
			var datos = new Array();
			datos[0] = 1;				// Boton aceptar
			datos[1] = document.GestionSolicitudesForm.pagoTarjetaNumero1.value + document.GestionSolicitudesForm.pagoTarjetaNumero2.value + document.GestionSolicitudesForm.pagoTarjetaNumero3.value + document.GestionSolicitudesForm.pagoTarjetaNumero4.value;
			datos[2] = document.GestionSolicitudesForm.pagoTarjetaAno.value + document.GestionSolicitudesForm.pagoTarjetaMes.value;
	
			window.top.returnValue = datos;
			window.top.close();
			*/
		}		
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="pys.desgloseCesta.literal.pagoTarjeta"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/PYS_GestionarSolicitudes.do" method="POST">
		<table class="tablaCentralCamposPeque" align="center">
			<tr>
				<td>
					<table class="tablaCampos" border="0">	
						<tr>
							<td class="labelText" colspan="2"><siga:Idioma key="pys.solicitudCompra.literal.tituloPagoTarjeta"/></td>
						</tr>
						<tr><td>&nbsp;</td></tr>
						<tr>
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.nTarjeta"/></td>
							<td>
								<input type="text" name="pagoTarjetaNumero1" maxlength="4" size="4" style="box"/>&nbsp;-
								<input type="text" name="pagoTarjetaNumero2" maxlength="4" size="4" style="box"/>&nbsp;-
								<input type="text" name="pagoTarjetaNumero3" maxlength="4" size="4" style="box"/>&nbsp;-
								<input type="text" name="pagoTarjetaNumero4" maxlength="4" size="4" style="box"/>
							</td>
						</tr>
						<tr>
							<td class="labelText"><siga:Idioma key="pys.solicitudCompra.literal.caducidad"/> (<siga:Idioma key="pys.solicitudCompra.literal.formatoCaducidad"/>)</td>
							<td>
								<input type="text" name="pagoTarjetaMes" maxlength="2" size="2" style="box"/>
								&nbsp;/
								<input type="text" name="pagoTarjetaAno" maxlength="4" size="4" style="box"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
	<!-- FIN: CAMPOS -->

	<siga:ConjBotonesAccion botones='C,Y' modo='' modal="P" />

</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
<!-- FORMULARIO PARA EL TPV -->
<form name="tpv" id="tpv" action="http://tpv.ceca.es:8000/cgi-bin/tpv" method="POST" enctype="application/x-www-form-urlencoded">
	<input type="hidden" name="MerchantID" value="<%=firma.getMerchantId()%>">
	<input type="hidden" name="AcquirerBIN" value="<%=firma.getAcquirerBin()%>">
	<input type="hidden" name="TerminalID" value="<%=firma.getTerminalId()%>">
	<!-- CAMPOS DEPENDIENTES DE LA OPERACION -->
	<input type="hidden" name="Num_operacion" value="<%=firma.getOperacion()%>">
	<input type="hidden" name="Importe" value="<%=firma.getImporte()%>">
	<!-- FIN CAMPOS DEPENDIENTES DE LA OPERACION -->
	<input type="hidden" name="TipoMoneda" value="<%=firma.getMoneda()%>">
	<input type="hidden" name="Exponente" value="<%=firma.getExponente()%>">
	<input type="hidden" name="URL_OK" value="<%=urlOK%>">
	<input type="hidden" name="URL_NOK" value="<%=urlKO%>">
	<input type="hidden" name="Firma" value="<%=firma.getFirma()%>">
	<input type="hidden" name="Idioma" value="1">
	<input type="hidden" name="Pago_soportado" value="SSL">
	<input type="hidden" name="Descripcion" value="Prueba de Compra">
</form>


</body>
</html>