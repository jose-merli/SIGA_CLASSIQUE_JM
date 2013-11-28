<!DOCTYPE html>
<html>
<head>
<!-- incidenciasBajasTemporales.jsp -->

	<!-- CABECERA JSP -->
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<%@ page pageEncoding="ISO-8859-1"%>
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
	<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
	
	<!-- TAGLIBS -->
	<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
	<%@ taglib uri="struts-bean.tld" prefix="bean"%>
	<%@ taglib uri="struts-html.tld" prefix="html"%>
	<%@ taglib uri="struts-logic.tld" prefix="logic"%>
	<%@ taglib uri="c.tld" prefix="c"%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>

	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Step 4 -->
 	<!-- Importar el js propio de la pagina-->
	<script src="<html:rewrite page='/html/js/censo/bajasTemporales.js?v=${sessionScope.VERSIONJS}'/>" type="text/javascript"></script>
</head>

<body>
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="censo.bajastemporales.incidencias.titulo" />
			</td>
		</tr>
	</table>
	
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>

	<html:form  action="${path}"  name="BajasTemporalesForm" type="com.siga.censo.form.BajasTemporalesForm">
		<html:hidden property="modo" />
		<html:hidden property="datosSeleccionados" />
		<html:hidden property="fichaColegial" />
		<table>	
			<tr>
				<td class="labelText"><siga:Idioma key="censo.bajastemporales.filtro.fechaDesde" /></td>
				<td><html:text property="fechaDesde" size="10" readonly="true" styleClass="boxConsulta" /></td>

				<td class="labelText"><siga:Idioma key="censo.bajastemporales.filtro.fechaHasta" /></td>
				<td><html:text property="fechaHasta" size="10" readonly="true" styleClass="boxConsulta" /></td>
			</tr>
	
			<tr class='tableTitle'>
				<td align='center' colspan="4"><siga:Idioma key="censo.bajastemporales.incidencias.deBajaTemporal" /></td>
			</tr>
			
			<tr>
				<td colspan="4">
					<siga:Table 
					   name="tablaPersonasBaja"
					   border="2"
					   columnNames="censo.bajastemporales.colegiado.numero,censo.bajastemporales.colegiado.nombre"
					   columnSizes="20,80"
					   fixedHeight="140">				
							
						<logic:notEmpty name="BajasTemporalesForm" property="personasDeBaja">
							<logic:iterate name="BajasTemporalesForm" property="personasDeBaja" id="personaDeBajaBean" indexId="index">
								<td align='center'><c:out value="${personaDeBajaBean.colegiado.NColegiado}" /></td>
								<td align='left'>
									<c:out value="${personaDeBajaBean.nombre}"/>
									&nbsp;<c:out value="${personaDeBajaBean.apellido1}"/>
									&nbsp;<c:out value="${personaDeBajaBean.apellido2}"/>
								</td>
							</logic:iterate>
						</logic:notEmpty>
					</siga:Table>
				</td>
			</tr>
	
			<tr class='tableTitle'>
				<td align='center' colspan="4"><siga:Idioma key="censo.bajastemporales.incidencias.deGuardia" /></td>				
			</tr>
			
			<tr>
				<td colspan="4">
					<siga:Table 
					   name="tablaPersonasGuardia"
					   border="2"
					   columnNames="censo.bajastemporales.colegiado.numero,censo.bajastemporales.colegiado.nombre"
					   columnSizes="20,80"
					   fixedHeight="140">	
							
						<logic:notEmpty name="BajasTemporalesForm" property="personasDeGuardia">
							<logic:iterate name="BajasTemporalesForm" property="personasDeGuardia" id="personasDeGuardiaBean" indexId="index">
								<td align='center'><c:out value="${personasDeGuardiaBean.colegiado.NColegiado}" /></td>
								<td align='left'>
									<c:out value="${personasDeGuardiaBean.nombre}"/>
									&nbsp;<c:out value="${personasDeGuardiaBean.apellido1}"/>
									&nbsp;<c:out value="${personasDeGuardiaBean.apellido2}"/>
								</td>
							</logic:iterate>
						</logic:notEmpty>
					</siga:Table>
				</td>
			</tr>
			
			<tr>
				<td colspan="4" class="labelText"><siga:Idioma key="censo.bajastemporales.incidencias.informacion" /></td>
			</tr>
		</table>
	</html:form>
	
	<siga:ConjBotonesAccion botones="C,GX,Y"/>
</body>

	<script>
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