<!-- informeFichaCertificados.jsp-->
<!-- Pantalla con los combos para seleccionar la facturacion a generar -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.Properties"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	Properties src = (Properties) ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);

	//COMBO PAGOS CERRADOS:
	String comboParams[] = new String[2];
	comboParams[0] = usrbean.getLocation();
	comboParams[1] = ClsConstants.ESTADO_PAGO_EJECUTADO;
	String dato[] = {usrbean.getLocation()};
%>

<html>

<head>

<link id="default" rel="stylesheet" type="text/css"	href="<%=app%>/html/jsp/general/stylesheet.jsp">
<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
<html:javascript formName="mantenimientoInformesForm" staticJavascript="false" />
<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

<!-- Escribe el título y localización en la barra de título del frame principal -->
<siga:Titulo titulo="factSJCS.informes.certificados.cabecera" localizacion="factSJCS.informes.ruta" />

</head>

<body>

<siga:ConjCampos leyenda="factSJCS.informes.certificados.cabecera">

	<html:form action="/INF_FichaFacturacion.do" method="POST" target="submitArea">
		<html:hidden name="mantenimientoInformesForm" property="modo" value="" />

		<table class="tablaCentralCampos" align="center">
		
			<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.definirTurnosIndex.literal.grupoFacturacion"/>&nbsp;&nbsp;&nbsp;&nbsp;
					<siga:ComboBD estilo="true" obligatorio="true" nombre="grupoFacturacion" filasMostrar="1" ancho="700" accion="Hijo:idPago;Hijo:idPagoFinal" seleccionMultiple="false" tipo="grupoFacturacionTodos" clase="boxCombo"  parametro="<%=dato%>"/>
				</td>
			</tr>
		
			<tr>
				<td class="labelText" width="10%">
					<siga:Idioma key="informes.sjcs.pagos.literal.pago"/>&nbsp;(*)&nbsp;&nbsp;
					<siga:ComboBD estilo="true" nombre="idPago" tipo="cmb_CertificadosPagosTodos" ancho="700" parametro="<%=comboParams%>" clase="boxCombo" hijo="t" obligatorio="true" obligatorioSinTextoSeleccionar="true" />
				</td>
			</tr>
			
			<tr>
				<td class="labelText" width="10%">
					<siga:Idioma key="informes.sjcs.pagos.literal.pagoFin"/>&nbsp;&nbsp;&nbsp;(*)&nbsp;&nbsp;
					<siga:ComboBD estilo="true" nombre="idPagoFinal" tipo="cmb_CertificadosPagosTodos" ancho="700" parametro="<%=comboParams%>" clase="boxCombo" hijo="t" obligatorio="false" />
				</td>
			</tr>

<!--	<tr>-->
<!--	<td class="labelText">-->
<!--		<siga:Idioma key="factSJCS.datosPagos.literal.idioma"/>&nbsp;(*)-->
<!--	</td>				-->
<!--	<td>-->
<!--		<siga:ComboBD nombre="idioma" tipo="cmbIdioma" clase="boxCombo" obligatorio="false" />-->
<!--	</td>-->
<!--	</tr>-->
		</table>
	</html:form>

</siga:ConjCampos>

<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
<siga:ConjBotonesAccion clase="botonesSeguido" botones="GM" />

<!-- INICIO: SCRIPTS BOTONES -->
<script language="JavaScript">
	function accionGenerarInforme() 
	{	
		sub();	
		var f=document.getElementById("mantenimientoInformesForm");
		var fname = document.getElementById("mantenimientoInformesForm").name;
		if (f.idPago.value != "") {
	    	if (validateMantenimientoInformesForm(f)) {
				f.modo.value="generarCertificadoPago";
				// con pantalla de espera
				document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.factSJCS.procesandoInforme';
			} else {
				fin();
				return false;
			}

		} else {
			alert("Seleccione un pago");
			fin();
			return false;
		} 
	 
	}
</script>
<!-- FIN: SCRIPTS BOTONES -->

<!-- Obligatoria en todas las páginas-->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>

</body>

</html>