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

<%@ page import="java.util.ArrayList"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>

<%
	String app=request.getContextPath();
	String idInstitucion = (String) request.getAttribute("idInstitucion");
	String idPersonaX = (String) request.getAttribute("idPersonaX");
	String idBoton = (String) request.getAttribute("idBoton");
	String paramInstitucion[] = { idInstitucion };
	
	String fechaSolicitud = UtilidadesBDAdm.getFechaBD("");
	
	ArrayList aMetodoSol = new ArrayList();
	aMetodoSol.add(1);
	
%>


<html>
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>	
	
	
	
	<!-- Para el calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script type="text/javascript">

		<!-- Funcion asociada a boton Finalizar -->
		function accionCerrar()	{
			window.top.close();
		}

		<!-- Asociada al boton Aceptar -->
		function accionAceptar() {
			var aux3=document.forms[0].idProductoCertificado.value;
			if(aux3=="") {
				var mensaje = "<siga:Idioma key="certificados.mantenimiento.literal.productoCertificado"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}
			var a = new Array;
			a[0]=aux3;
			a[1]=document.forms[0].fechaSolicitud.value;
			a[2]=document.forms[0].metodoSolicitud.value;
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
		<tr>	
		<td class="labelText">
			<siga:Idioma key="certificados.solicitudes.literal.fechaSolicitud"/>
		</td>				
		<td>

			<siga:Fecha nombreCampo="fechaSolicitud" valorInicial="<%=fechaSolicitud%>" posicionX="30" posicionY="10"></siga:Fecha>
		</td>
	</tr>
	<tr>
		<td class="labelText">
			<siga:Idioma key="certificados.solicitudes.literal.metodoSolicitud"/>
		</td>				
		<td>
			<siga:ComboBD nombre="metodoSolicitud" tipo="comboMetodoSolicitud" obligatorio="false" ElementoSel="<%=aMetodoSol%>" clase="boxCombo"/>
		</td>
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