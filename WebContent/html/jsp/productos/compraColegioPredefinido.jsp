<!-- compraCertificadoPredefinido.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%
	String idInstitucion = (String) request
			.getAttribute("idInstitucion");
	String idPersonaX = (String) request.getAttribute("idPersonaX");
	String idBoton = (String) request.getAttribute("idBoton");
	String paramInstitucion[] = { idInstitucion };
%>


<html>
<head>
<link id="default" rel="stylesheet" type="text/css"
	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">

<script src="<html:rewrite page="/html/js/SIGA.js"/>"
	type="text/javascript"></script>

<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
<script language="JavaScript">

		<!-- Funcion asociada a boton Finalizar -->
		function accionCerrar()
		{
			window.close();
		}

		<!-- Asociada al boton Aceptar -->
		function accionAceptar()
		{
			
			var aux3=document.forms[0].idProductoCertificado.value;
			if(aux3=="")
			{
				var mensaje = "<siga:Idioma key="certificados.mantenimiento.literal.productoCertificado"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}
			var a = new Array;
			a[0]=aux3;
			top.cierraConParametros(a);
		}
		</script>
<!-- FIN: SCRIPTS BOTONES -->
</head>

<body>

<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos"><siga:Idioma
			key="certificados.mantenimiento.literal.seleccionPlantillayCertificado" />
		</td>
	</tr>
</table>

<fieldset>
<table class="tablaCentralCamposPeque" align="center">
	<form name="x">
	<tr>
		<td class="labelText"><siga:Idioma
			key="certificados.mantenimiento.literal.productoCertificado" />&nbsp(*)
		</td>
		<td><siga:ComboBD nombre="idProductoCertificado"
			tipo="cmbCertificadosOrdinadios" clase="boxCombo"
			parametro="<%=paramInstitucion %>" obligatorio="true" /></td>
	</tr>
	
	
	</form>
</table>
</fieldset>

<siga:ConjBotonesAccion botones="A,C" modal="P" />

<iframe name="submitArea"
	src="<html:rewrite page="/html/jsp/general/blank.jsp"/>"
	style="display: none"></iframe>
</body>
</html>