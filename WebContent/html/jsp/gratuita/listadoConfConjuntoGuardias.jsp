<!-- listadoConfConjuntoGuardias -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type"
	content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

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
<script
	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"
	type="text/javascript"></script>

<script src="<html:rewrite page='/html/js/validacionStruts.js'/>"
	type="text/javascript"></script>



</head>

<body>
<bean:define id="accion" name="accion" type="java.lang.String"
	scope="request" />
<bean:define id="error" name="error" scope="request" />
<bean:define id="confConjuntoGuardiasForms" name="confConjuntoGuardiasForms"
	type="java.util.Collection" scope="request" />



<div>


<table id='listadoConfCab' border='1' width='100%' cellspacing='0'
	cellpadding='0'>
	<tr class='tableTitle'>
		<c:choose>
			<c:when test="${accion=='actualizacion'}">
				<td align='center' width='10%'><input type='checkbox'
					name='chkGeneral' id='chkGeneral' onclick="checkTodos(this);" /></td>
				<td align='center' width='10%'><siga:Idioma key='gratuita.calendarios.programacion.orden'/></td>
				<td align='center' width='40%'><siga:Idioma key='gratuita.calendarios.programacion.turno'/></td>
				<td align='center' width='40%'><siga:Idioma key='gratuita.calendarios.programacion.guardia'/></td>
			</c:when>
			<c:otherwise>
				<td align='center' width='10%'><siga:Idioma key='gratuita.calendarios.programacion.orden'/></td>
				<td align='center' width='45%'><siga:Idioma key='gratuita.calendarios.programacion.turno'/></td>
				<td align='center' width='45%'><siga:Idioma key='gratuita.calendarios.programacion.guardia'/></td>
			</c:otherwise>
		</c:choose>




	</tr>
</table>
</div>
<div id='listadoConfDiv'
	style='height: 400; width: 100%; overflow-y: auto'>

<table class="tablaCampos" id='listadoConf' border='1' align='center'
	width='100%' cellspacing='0' cellpadding='0'
	style='table-layout: fixed'>
	<c:choose>

		<c:when test="${empty confConjuntoGuardiasForms}">
			<tr>
				<c:choose>
					<c:when test="${accion=='actualizacion'}">
						<td width='10%'></td>
						<td width='10%'></td>
						<td width='40%'></td>
						<td width='40%'></td>
					</c:when>
					<c:otherwise>
						<td width='10%'></td>
						<td width='45%'></td>
						<td width='45%'></td>
					</c:otherwise>
				</c:choose>


			</tr>

			<tr class='titulitos' id="noRecordFound">
				<c:choose>
					<c:when test="${accion=='actualizacion'}">
						<td class="titulitos" style="text-align: center" colspan="4">
						<siga:Idioma key="messages.noRecordFound" /></td>
					</c:when>
					<c:otherwise>
						<td class="titulitos" style="text-align: center" colspan="3">
						<siga:Idioma key="messages.noRecordFound" /></td>
					</c:otherwise>
				</c:choose>


			</tr>
		</c:when>
		<c:otherwise>
			<tr>
				<c:choose>
					<c:when test="${accion=='actualizacion'}">
						<td width='10%'></td>
						<td width='10%'></td>
						<td width='40%'></td>
						<td width='40%'></td>
					</c:when>
					<c:otherwise>
						<td width='10%'></td>
						<td width='45%'></td>
						<td width='45%'></td>
					</c:otherwise>
				</c:choose>


			</tr>

			<c:forEach items="${confConjuntoGuardiasForms}"
				var="confConjuntoGuardiasForm" varStatus="status">
				<siga:FilaConIconos fila='${status.count}' botones=""
					pintarEspacio="no" visibleConsulta="no" visibleEdicion="no"
					visibleBorrado="NO" clase="listaNonEdit"
					id="filaConf_${status.count}">
					<input type="hidden" id="idTurno_${status.count}"
						value="${confConjuntoGuardiasForm.turno.id}">
					<input type="hidden" id="idGuardia_${status.count}"
						value="${confConjuntoGuardiasForm.guardia.id}">
					<input type="hidden" id="ordenOld_${status.count}"
						value="${confConjuntoGuardiasForm.orden}">
					<c:if test="${accion=='actualizacion'}">
						<td align="center"><c:choose>
							<c:when test="${confConjuntoGuardiasForm.orden==null}">
								 <input type="hidden" id="checkConfiguracionOld_${status.count}" value="false">
								<input type="checkbox" id="checkConfiguracion_${status.count}"
									name="chkConfiguracion" onclick="onclickCheckConfiguracion(${status.count});">
							</c:when>
							<c:otherwise>
							<input type="hidden" id="checkConfiguracionOld_${status.count}"	value="true">
								<input type="checkbox" id="checkConfiguracion_${status.count}"
									name="chkConfiguracion" onclick="onclickCheckConfiguracion(${status.count});" >
							</c:otherwise>
						</c:choose></td>
					</c:if>
					<td align="center"><c:choose>
						<c:when test="${confConjuntoGuardiasForm.orden!=null}">
							<input type="text" class="box" style="width: 40;"
								id="orden_${status.count}"
								value="${confConjuntoGuardiasForm.orden}" disabled='disabled' maxlength="5">
						</c:when>
						<c:otherwise>
							<input type="text" class="box" style="width: 40;"
								id="orden_${status.count}" value="" disabled='disabled' maxlength="5">
						</c:otherwise>

					</c:choose></td>
					<td align='left'><c:out
						value="${confConjuntoGuardiasForm.turno.nombre}"></c:out></td>
					<td align='left'><c:out
						value="${confConjuntoGuardiasForm.guardia.nombreGuardia}"></c:out></td>
				</siga:FilaConIconos>
			</c:forEach>


		</c:otherwise>
	</c:choose>

</table>

</div>

<script type="text/javascript">


	var messageError='${error}';
		
		if (messageError)
			alert(messageError);
		
	</script>
</body>

</html>