<!-- informeFacturacionMultiple.jsp -->

<!-- Pantalla con los combos para seleccionar las facturaciones entre las que se genera el informe
	 VERSIONES:
	 jose.barrientos creado el  19-06-2009.
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoPagoForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	Properties src = (Properties) ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);

	//COMBO PAGOS CERRADOS:
	String comboParams[] = new String[1];
	comboParams[0] = usrbean.getLocation();
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="InformesFacturacionMultipleForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	


	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="factSJCS.informes.informeMultiple.cabecera" 
		localizacion="factSJCS.informes.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body >

	<siga:ConjCampos leyenda="factSJCS.informes.informeMultiple.cabecera">					
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
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
	<!-- FIN: CAMPOS DE BUSQUEDA-->
	</siga:ConjCampos>		





	<html:form action="/INF_InformesGenericos.do" method="POST" target="submitArea">
		<input type="hidden" name="actionModal" value="">
		<html:hidden property="idTipoInforme" value="FJGM" />
		<html:hidden property="datosInforme" value="" />
		<html:hidden property="seleccionados" value="" />
		<html:hidden property="idInforme" value="" />
	</html:form>


	<html:form action="/FCS_DatosGeneralesFacturacion.do" method="POST" target="submitArea">
		<html:hidden property="accion" value="downloadMultiple"/>
		<html:hidden property="idFacturacionIniDownload" value=""/>
		<html:hidden property="idFacturacionFinDownload" value=""/>
		<html:hidden property="idioma" value=""/>
	</html:form>


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
		<siga:ConjBotonesAccion clase="botonesSeguido" botones="GX,GM"  />
	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton Generar Informe -->
		function accionGenerarInforme() 
		{
			sub();		
			var f = document.getElementById("InformesGenericosForm");
			idFactIni = document.getElementById("idFacturacionInicio").value;
			idFactFin = document.getElementById("idFacturacionFin").value;
			idio = document.getElementById("idioma").value;
			f.datosInforme.value = "idFacturacionIni"+"=="+idFactIni+"##"+"idFacturacionFin"+"=="+idFactFin+"##"+"idioma"+"=="+idio;
			f.seleccionados.value="1";
			f.submit();
		}

		<!-- Funcion asociada a boton Generar Excels -->
		function accionGenerarExcels() 
		{
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
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>