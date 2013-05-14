<!-- consultaAsistencia.jsp -->

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

<!-- IMPORTS -->
<%@page import="com.siga.Utilidades.UtilidadesString"%>
<%@page import="com.atos.utils.UsrBean"%>


<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>
</head>

<body>
<table class="tablaTitulo" cellspacing="0" heigth="38">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.volantesExpres.asistencia.detalle"/>
		</td>
	</tr>
	</table>



<!-- INICIO: CAMPOS DE BUSQUEDA-->
<bean:define id="path" name="org.apache.struts.action.mapping.instance"
	property="path" scope="request" />
<html:form action="${path}" method="POST" target="mainWorkArea">
<html:hidden property="modo" value="abrir"/>
	<table class="tablaCentralCampos">
		<tr>
			<td valign="top"><siga:ConjCampos
				leyenda="gratuita.mantAsistencias.literal.titulo">
				<table width="100%" border="0" style="table-layout: fixed">
					<tr>
						<td class="labelText" width="20%">
							<siga:Idioma key='gratuita.mantAsistencias.literal.anio' /> / 
							<siga:Idioma key='gratuita.mantAsistencias.literal.numero' /></td>
						<td class="labelTextValor" width="20%"><c:out value="${asistencia.anio}"></c:out>/ <c:out value="${asistencia.numero}"></c:out></td>
						<td class="labelText" style="width: 70px"><siga:Idioma
							key='gratuita.mantAsistencias.literal.turno' /></td>
						<td class="labelTextValor" style="width: 300px"><c:out value="${asistencia.turno.nombre}"></c:out></td>
					</tr>
						<td class="labelText" style="width: 70px"><siga:Idioma key='gratuita.mantAsistencias.literal.guardia' /></td>
						<td class="labelTextValor" style="width: 300px"><c:out value="${asistencia.guardia.nombre}"></c:out></td>
					<tr>
					
					</tr>
				</table>
			</siga:ConjCampos></td>
		</tr>
		<tr>
			<td>
			<table width="100%" border="0" style="table-layout: fixed">
				<tr>
					<td width="50%"><siga:ConjCampos
						leyenda="gratuita.mantAsistencias.literal.asistido">
						<table width="100%" border="0">
							<tr>
								<td class="labelText" width="20%"><siga:Idioma
									key='gratuita.mantAsistencias.literal.nif' /></td>
								<td class="labelTextValor" width="80%">
								<c:out value="${asistencia.asistido.nif}"></c:out>
								
								</td>
							</tr>
							<tr>
								<td class="labelText"><siga:Idioma
									key='gratuita.mantAsistencias.literal.nombre' /></td>
								<td class="labelTextValor">
								<c:out value="${asistencia.asistido.nombre}"></c:out>&nbsp;<c:out value="${asistencia.asistido.apellido1}"></c:out>&nbsp;<c:out value="${asistencia.asistido.apellido2}"></c:out></td>
							</tr>
						</table>
					</siga:ConjCampos></td>
					<td width="50%"><siga:ConjCampos
						leyenda="gratuita.mantAsistencias.literal.letrado">
						<table width="100%" border="0">
							<tr>
								<td class="labelText" width="20%"><siga:Idioma
									key='gratuita.mantAsistencias.literal.numero' /></td>
								<td class="labelTextValor" width="80%">
								<c:out value="${asistencia.personaColegiado.colegiado.NColegiado}"></c:out>
								
								
								</td>
							</tr>
							<tr>
								<td class="labelText"><siga:Idioma
									key='gratuita.mantAsistencias.literal.nombre' /></td>
								<td class="labelTextValor">
								<c:out value="${asistencia.personaColegiado.nombre}"></c:out>&nbsp;<c:out value="${asistencia.personaColegiado.apellido1}"></c:out>&nbsp;<c:out value="${asistencia.personaColegiado.apellido2}"></c:out></td>
								
							</tr>
						</table>
					</siga:ConjCampos></td>
				</tr>
			</table>

			</td>
		</tr>
		<tr>
			<td id="relaccion">
			<siga:ConjCampos
						leyenda="gratuita.mantAsistencias.literal.relacionado">
			<table width="100%">
				<tr>
					<td>
						<c:if test="${asistencia.ejg!=null}">
						
							<table width="100%" border="0" style="table-layout: fixed">
								<tr>
									<td class="labelText" width="90"><siga:Idioma
										key='gratuita.mantAsistencias.literal.ejg' /></td>
									<td class="labelText" width="70"><siga:Idioma
										key='gratuita.mantAsistencias.literal.tipo' /></td>
									<td class="labelTextValor" width="200"><c:out value="${asistencia.ejg.tipoEjg.descripcion}"></c:out></td>
									<td class="labelTextValue" width="280"><c:out value="${asistencia.ejg.anio}"></c:out>/<c:out value="${asistencia.ejg.numEJG}"></c:out> -
									<c:out value="${asistencia.ejg.solicitante.nombre}"></c:out>&nbsp;<c:out value="${asistencia.ejg.solicitante.apellido1}"></c:out>&nbsp;<c:out value="${asistencia.ejg.solicitante.apellido2}"></c:out>
									</td>
	
	
								</tr>
							</table>
							<script>
								document.getElementById("relaccion").id="ok";
							</script>
						</c:if>
						<c:if test="${asistencia.designa!=null}">
						<table width="100%" border="0" style="table-layout: fixed">
							<tr>
								<td class="labelText" width="90"><siga:Idioma
									key='gratuita.mantAsistencias.literal.designa' /></td>
								<td class="labelText" width="70"><siga:Idioma
									key='gratuita.mantAsistencias.literal.turno' /></td>
								<td class="labelTextValor" width="200"><c:out value="${asistencia.designa.turno.nombre}"></c:out></td>
								<td class="labelTextValue" width="280"><c:out value="${asistencia.designa.anio}"></c:out>/<c:out value="${asistencia.designa.codigo}"> </c:out> -
								<c:out value="${asistencia.designa.colegiadoDesignado.nombre}"></c:out>&nbsp;<c:out value="${asistencia.designa.colegiadoDesignado.apellido1}"></c:out>&nbsp;<c:out value="${asistencia.designa.colegiadoDesignado.apellido2}"></c:out>
								</td>
							</tr>
						</table>
						<script>
							if(document.getElementById("relaccion"))
								document.getElementById("relaccion").id="ok";
						</script>
						</c:if>

						
					
				</td>
				</tr>
				
			</table>
			</siga:ConjCampos>
			</td>
			<script>
				if(document.getElementById("relaccion"))
					document.getElementById("relaccion").style.display="none";
			</script>
		</tr>
		
	</table>


</html:form>
	<!-- INICIO: BOTONES REGISTRO -->
	<siga:ConjBotonesAccion botones="C" modal="M"/>
	<!-- FIN: BOTONES REGISTRO -->
</body>

<script>
				
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}		
					
</script>


</html>