<!-- informeFacturacionMultipleBeta.jsp -->

<!-- Pantalla con los combos para seleccionar las facturaciones entre las que se genera el informe
	 VERSIONES:
	 jose.barrientos creado el  19-06-2009.
-->

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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoPagoForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);

	//Combo Facturaciones
	String comboParams[] = new String[1];
	comboParams[0] = usrbean.getLocation();
	String informeUnico =(String) request.getAttribute("informeUnico");

%>


<html>
<head>

<link id="default" rel="stylesheet" type="text/css"	href="<%=app%>/html/jsp/general/stylesheet.jsp">
<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

<!-- Validaciones de los campos en cliente -->
<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

<!-- El nombre del formulario se obtiene del struts-config -->
<html:javascript formName="InformesFacturacionMultipleForm"	staticJavascript="false" />

<!-- Titulo y localizacion -->
<siga:Titulo titulo="menu.justiciaGratuita.informes.informeMultipleNuevo" localizacion="factSJCS.informes.ruta" />

</head>
<body>


<!-- Campos -->
<siga:ConjCampos leyenda="menu.justiciaGratuita.informes.informeMultipleNuevo">
<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">

	<table class="tablaCampos" align="center">
		<tr>
			<td class="labelText" width="150">
				<siga:Idioma key="gratuita.definirTurnosIndex.literal.grupoFacturacion"/>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td><siga:ComboBD estilo="true" obligatorio="true" nombre="grupoFacturacion" filasMostrar="1" ancho="700" accion="Hijo:idFacturacionInicio;Hijo:idFacturacionFin" seleccionMultiple="false" tipo="grupoFacturacionTodos" clase="boxCombo"  parametro="<%=comboParams%>"/>
			</td>
		</tr>
		<tr>
			<td class="labelText" width="150"><siga:Idioma
				key="factSJCS.informes.informeMultiple.factInicial" />&nbsp;(*)</td>
			<td><siga:ComboBD nombre="idFacturacionInicio"
				tipo="cmb_FactInformesTodos" parametro="<%=comboParams%>"
				clase="boxCombo" obligatorio="true"  hijo="t"  ancho="700"
				obligatorioSinTextoSeleccionar="true" /></td>
		</tr>
		<tr>
			<td class="labelText" width="150"><siga:Idioma
				key="factSJCS.informes.informeMultiple.factFinal" />&nbsp;(*)</td>
			<td><siga:ComboBD nombre="idFacturacionFin"
				tipo="cmb_FactInformesTodos" parametro="<%=comboParams%>"
				clase="boxCombo" obligatorio="true"  hijo="t"  ancho="700"
				obligatorioSinTextoSeleccionar="true" /></td>
		</tr>
	</table>
</siga:ConjCampos>


<!-- Formularios -->
<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value="<%=usrbean.getLocation()%>"/>
	<html:hidden property="idTipoInforme" value="FACJ2"/>
	<html:hidden property="enviar" value="0"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
		<input type='hidden' name='actionModal'>
	
	
</html:form>
<!-- Formulario para la edicion del envio -->
<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
	<input type="hidden" name="modo" value="">
	<input type="hidden" name="tablaDatosDinamicosD" value="">

</form>



<!-- Botones -->
<siga:ConjBotonesAccion clase="botonesSeguido" botones="GM" />


<!-- Scripts de botones -->
<script language="JavaScript">
	// Funcion asociada a boton Generar Informe
	function accionGenerarInforme() {
		idFactIni = document.getElementById("idFacturacionInicio").value;
		idFactFin = document.getElementById("idFacturacionFin").value;
		if (idFactIni != "") {
			grupoFact =  document.getElementById("grupoFacturacion").value;
			datos = "idFacturacionIni" + "==" + idFactIni + "##"
					+ "idFacturacionFin" + "==" + idFactFin+ "##"+ "grupoFacturacion" + "==" + grupoFact;
			document.InformesGenericosForm.datosInforme.value=datos;
			if(document.getElementById("informeUnico").value=='1'){
				sub();
				document.InformesGenericosForm.submit();
			}else{
				var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
				if (arrayResultado==undefined||arrayResultado[0]==undefined){
				   		
			   	}else {
			   		
				
				}
			}
		} else {
			alert("Seleccione una facturacion");
			fin();
			return false;
		} 
		
		
	}
</script>


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>
</html>