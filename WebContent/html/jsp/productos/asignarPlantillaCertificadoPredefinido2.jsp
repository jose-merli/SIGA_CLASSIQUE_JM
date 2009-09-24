<!-- asignarPlantillaCertificado2.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.general.*"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");

	String idInstitucion=(String)request.getAttribute("idInstitucion");
	String idInstitucionX=(String)request.getAttribute("idInstitucionX");
	String idPersonaX=(String)request.getAttribute("idPersonaX");
	String idBoton=(String)request.getAttribute("idBoton");

	String paramInstitucion[] = {idInstitucion};
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">

		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

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
			//var aux=document.forms[0].idPlantilla.value;
			var aux2=document.forms[0].idInstitucionPresentador.value;
			var aux3=document.forms[0].idProductoCertificado.value;
			if(aux3=="")
			{
				var mensaje = "<siga:Idioma key="certificados.mantenimiento.literal.productoCertificado"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}
			/*
			if(aux=="")
			{
				var mensaje = "<siga:Idioma key="certificados.mantenimiento.literal.plantilla"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}
			*/
			if(aux2=="")
			{
				var mensaje = "<siga:Idioma key="pys.solicitudCompra.literal.presentador"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}

			var a = new Array;
			//a[0]=aux;
			a[0]=aux2;
			a[1]=aux3;

			top.cierraConParametros(a);
		}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body>

		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="certificados.mantenimiento.literal.seleccionPlantillayCertificado"/>
				</td>
			</tr>
		</table>

			<fieldset>
			<table class="tablaCentralCamposPeque" align="center">
				<form name="x">
				<tr>
								<td class="labelText">
									<siga:Idioma key="certificados.mantenimiento.literal.productoCertificado"/>&nbsp(*)
								</td>
								<td>
									<siga:ComboBD nombre="idProductoCertificado" tipo="cmbCertificadosOrdinadios" clase="boxCombo" parametro="<%=paramInstitucion %>" obligatorio="true"/>
								</td>
				</tr>
<!--				
				<tr>
								<td class="labelText">
									<siga:Idioma key="certificados.mantenimiento.literal.plantilla"/>&nbsp(*)
								</td>
								<td>
									<siga:ComboBD nombre="idPlantilla" tipo="cmbCerPlantillasCompuesto" clase="boxCombo" obligatorio="true" hijo="t"/>
								</td>
				</tr>
-->				
				<tr>
								<td class="labelText">
									<siga:Idioma key="pys.solicitudCompra.literal.presentador"/>&nbsp;(*)
								</td>
								<td>
									<siga:ComboBD nombre="idInstitucionPresentador" 
														tipo="cmbInstitucionesAbreviadas" 
														clase="boxCombo"
														readonly="false"
														obligatorio="true"
														/>									
								</td>
				</tr>
				</form>		
			</table>
			</fieldset>

			<siga:ConjBotonesAccion botones="A,C" modal="P"/>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>