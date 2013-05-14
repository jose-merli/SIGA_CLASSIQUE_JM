<!-- informeFacturacionMultiple.jsp -->

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


<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

<!-- Validaciones de los campos en cliente -->
<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

<!-- El nombre del formulario se obtiene del struts-config -->
<html:javascript formName="InformesFacturacionMultipleForm"	staticJavascript="false" />


<!-- Titulo y localizacion -->
<siga:Titulo titulo="factSJCS.informes.informeMultiple.cabecera" localizacion="factSJCS.informes.ruta" />


</head>
<body>


<!-- Campos -->
<siga:ConjCampos leyenda="factSJCS.informes.informeMultiple.cabecera">
<input type="hidden" id= "informeUnico" value="<%=informeUnico%>">
	<table class="tablaCampos" align="center">
		<tr>
			<td class="labelText" width="150"><siga:Idioma
				key="factSJCS.informes.informeMultiple.factInicial" />&nbsp;(*)</td>
			<td><siga:ComboBD nombre="idFacturacionInicio"
				tipo="cmb_FactInformes" parametro="<%=comboParams%>"
				clase="boxCombo" obligatorio="true"
				obligatorioSinTextoSeleccionar="true" /></td>
		</tr>
		<tr>
			<td class="labelText" width="150"><siga:Idioma
				key="factSJCS.informes.informeMultiple.factFinal" />&nbsp;(*)</td>
			<td><siga:ComboBD nombre="idFacturacionFin"
				tipo="cmb_FactInformes" parametro="<%=comboParams%>"
				clase="boxCombo" obligatorio="true"
				obligatorioSinTextoSeleccionar="true" /></td>
		</tr>
	</table>
</siga:ConjCampos>


<!-- Formularios -->
<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value="<%=usrbean.getLocation()%>"/>
	<html:hidden property="idTipoInforme" value="FJGM"/>
	<html:hidden property="enviar" value="0"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
	
</html:form>

<html:form action="/FCS_DatosGeneralesFacturacion.do" method="POST"	target="submitArea">
	<html:hidden property="accion" value="downloadMultiple" />
	<html:hidden property="idFacturacionIniDownload" value="" />
	<html:hidden property="idFacturacionFinDownload" value="" />
	<html:hidden property="idioma" value="" />
</html:form>

<!-- Formulario para la edicion del envio -->
<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
	<input type="hidden" name="modo" value="">
	<input type="hidden" name="tablaDatosDinamicosD" value="">

</form>

<!-- Botones -->
<siga:ConjBotonesAccion clase="botonesSeguido" botones="GX,GM" />


<!-- Scripts de botones -->
<script language="JavaScript">
	// Funcion asociada a boton Generar Informe
	function accionGenerarInforme() {
		idFactIni = document.getElementById("idFacturacionInicio").value;
		idFactFin = document.getElementById("idFacturacionFin").value;
		datos = "idFacturacionIni" + "==" + idFactIni + "##"
		+ "idFacturacionFin" + "==" + idFactFin;
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
		
		
	}

	// Funcion asociada a boton Generar Excels
	function accionGenerarExcels() {
		sub();
		var f = document.getElementById("DatosGeneralesFacturacionForm");
		idFactIni = document.getElementById("idFacturacionInicio").value;
		idFactFin = document.getElementById("idFacturacionFin").value;
		idio = document.getElementById("idioma").value;
		f.idFacturacionIniDownload.value = idFactIni;
		f.idFacturacionFinDownload.value = idFactFin;
		f.idioma.value = idio;
		f.submit();
	}
</script>


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>
</html>