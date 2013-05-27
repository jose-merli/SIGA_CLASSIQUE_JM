<!-- incidenciasBajasTemporales.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>


<html>

<!-- HEAD -->
<head>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->

<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

<!-- Step 4 -->
 <!-- Importar el js propio de la pagina-->
<script src="<html:rewrite page='/html/js/censo/bajasTemporales.js'/>" type="text/javascript"></script>
</head>

<body onload="onLoad()">

<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<html:form  action="${path}"  name="BajasTemporalesForm" type="com.siga.censo.form.BajasTemporalesForm">
	<html:hidden property="modo" />
	<html:hidden property="datosSeleccionados" />
	<html:hidden property="fichaColegial" />
<table>
	
	<tr>
		<td class="labelText"><siga:Idioma
			key="censo.bajastemporales.filtro.fechaDesde" /></td>
		<td><html:text property="fechaDesde" size="10"
			readonly="true" styleClass="box" /></td>

		<td class="labelText"><siga:Idioma
			key="censo.bajastemporales.filtro.fechaHasta" /></td>
		<td><html:text property="fechaHasta" size="10"
			readonly="true" styleClass="box" /></td>

	</tr>
	
	<tr class='tableTitle'>
				<td align='center' colspan="4">
				<siga:Idioma
					key="censo.bajastemporales.incidencias.deBajaTemporal" />
				</td>
				
			</tr>
	<tr>
		<td colspan="4">
		<div style="height:140px;overflow:scroll ">
		<table class="tablaCampos" id='idPersonasdeBaja' border='1'
			align='center' width='100%' cellspacing='0' cellpadding='0'
			style='table-layout: fixed'>
			
			<tr class='tableTitle'>
				<td align='center' width='20%'><siga:Idioma
					key="censo.bajastemporales.colegiado.numero" /></td>
				<td align='center' width='80%'><siga:Idioma
					key="censo.bajastemporales.colegiado.nombre" /></td>
			</tr>
			<logic:notEmpty name="BajasTemporalesForm" property="personasDeBaja">
				<logic:iterate name="BajasTemporalesForm" property="personasDeBaja"
					id="personaDeBajaBean" indexId="index">
					<tr class="<%=((index+1)%2==0?"filaTablaPar":"filaTablaImpar")%>" style="padding:5px;">
						<td align='center' width='20%'><c:out
							value="${personaDeBajaBean.colegiado.NColegiado}" /></td>
						<td align='left' width='80%'><c:out
							value="${personaDeBajaBean.nombre}"/>&nbsp;<c:out
							value="${personaDeBajaBean.apellido1}"/>&nbsp;<c:out
							value="${personaDeBajaBean.apellido2}"/></td>

					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</table>
		</div>

		</td>
	</tr>
	
	<tr class='tableTitle'>
				<td align='center' colspan="4">
				<siga:Idioma
					key="censo.bajastemporales.incidencias.deGuardia" />
				</td>
				
			</tr>
	<tr>
		<td colspan="4">
			<div style="height:140px;overflow:scroll ">
		<table class="tablaCampos" id='idPersonasDeGuardia' border='1'
			align='center' width='100%' cellspacing='0' cellpadding='0'
			style='table-layout: fixed'>
			
			<tr class='tableTitle'>
				<td align='center' width='20%'><siga:Idioma
					key="censo.bajastemporales.colegiado.numero" /></td>
				<td align='center' width='80%'><siga:Idioma
					key="censo.bajastemporales.colegiado.nombre" /></td>
			</tr>
			<logic:notEmpty name="BajasTemporalesForm"
				property="personasDeGuardia">
				<logic:iterate name="BajasTemporalesForm"
					property="personasDeGuardia" id="personasDeGuardiaBean"
					indexId="index">
					<tr class="<%=((index+1)%2==0?"filaTablaPar":"filaTablaImpar")%>" style="padding:5px;">
						<td align='center' width='20%'><c:out
							value="${personasDeGuardiaBean.colegiado.NColegiado}" /></td>
						<td align='left' width='80%'><c:out
							value="${personasDeGuardiaBean.nombre}"/>&nbsp;<c:out
							value="${personasDeGuardiaBean.apellido1}"/>&nbsp;<c:out
							value="${personasDeGuardiaBean.apellido2}"/></td>

					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</table>
		</div>
		</td>
	</tr>
	<tr>
		<td colspan="4" class="labelText"><siga:Idioma
					key="censo.bajastemporales.incidencias.informacion" /></td>
	</tr>
</table>
</html:form>
<siga:ConjBotonesAccion botones="C,GX,Y"/>

</body>
<script>

function onLoad(){
	window.dialogHeight = "440px";
	window.dialogWidth = "700px";
}
function accionCerrar(){
	window.top.close();
}
function accionGuardarCerrar(){
	sub();
	document.BajasTemporalesForm.modo.value="insertarNuevaSolicitudDirect";
	document.BajasTemporalesForm.submit();


}
function accionGenerarExcels(){
	sub();
	document.BajasTemporalesForm.modo.value="generarIncidencias";
	document.BajasTemporalesForm.submit();
	fin();

}

</script>

</html>