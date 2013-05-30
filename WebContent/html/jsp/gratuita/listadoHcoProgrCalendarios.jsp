<!-- listadoHcoProgrCalendarios -->

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
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
<script
	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"
	type="text/javascript"></script>

<script src="<html:rewrite page='/html/js/validacionStruts.js'/>"
	type="text/javascript"></script>



</head>

<body>
<bean:define id="error" name="error" scope="request" />
<bean:define id="hcoConfProgrCalendarioForms" name="hcoConfProgrCalendarioForms"
	type="java.util.Collection" scope="request" />



<div>




<table id='listadoHcoProgramacionCab' border='1' width='100%' cellspacing='0'
	cellpadding='0'>
	<tr class='tableTitle'>
		
				<td align='center' width='10%'><siga:Idioma key='gratuita.calendarios.programacion.orden'/></td>
				<td align='center' width='35%'><siga:Idioma key='gratuita.calendarios.programacion.turno'/></td>
				<td align='center' width='35%'><siga:Idioma key='gratuita.calendarios.programacion.guardia'/></td>
				<td align='center' width='10%'><siga:Idioma key='gratuita.calendarios.programacion.estado'/></td>
				<td align='center' width='10%'></td>
		



	</tr>
</table>
</div>
<div id='listadoHcoProgramacionDiv'
	style='height: 400; width: 100%; overflow-y: auto'>

<table class="tablaCampos" id='listadoHcoProgramacion' border='1' align='center'
	width='100%' cellspacing='0' cellpadding='0'
	style='table-layout: fixed'>
	<c:choose>

		<c:when test="${empty hcoConfProgrCalendarioForms}">
			<tr>
				
						<td width='10%'></td>
						<td width='35%'></td>
						<td width='35%'></td>
						<td width='10%'></td>
						<td width='10%'></td>
				


			</tr>

			<tr class='titulitos' id="noRecordFound">
				
					
						<td class="titulitos" style="text-align: center" colspan="5">
						<siga:Idioma key="messages.noRecordFound" /></td>
					


			</tr>
		</c:when>
		<c:otherwise>
			<tr>
					<td width='10%'></td>
					<td width='35%'></td>
					<td width='35%'></td>
					<td width='10%'></td>
					<td width='10%'></td>


			</tr>

			<c:forEach items="${hcoConfProgrCalendarioForms}"
				var="hcoConfProgrCalendarioForm" varStatus="status">
				<c:set var="botones" value="" />
				<c:if test="${hcoConfProgrCalendarioForm.estado=='2'||hcoConfProgrCalendarioForm.estado=='4'}">
					<c:set var="botones" value="B" />
				</c:if>
				
				<siga:FilaConIconos fila='${status.count}' botones="${botones}"
					elementos="${hcoConfProgrCalendarioForm.elementosFila}"
				
					pintarEspacio="no" visibleConsulta="no" visibleEdicion="no"
					visibleBorrado="no" clase="listaNonEdit"
					id="filaConf_${status.count}">
					
					<input type="hidden" id="idTurno_${status.count}"
						value="${hcoConfProgrCalendarioForm.idTurno}">
					<input type="hidden" id="idGuardia_${status.count}"
						value="${hcoConfProgrCalendarioForm.idGuardia}">
					<input type="hidden" id="idCalendarioGuardias_${status.count}"
						value="${hcoConfProgrCalendarioForm.idCalendarioGuardias}">
						
					<input type="hidden" id="idConjuntoGuardia_${status.count}"
						value="${hcoConfProgrCalendarioForm.idConjuntoGuardia}">
					
					<td align="center">
							<input type="text" class="box" style="width: 40;"
								id="orden_${status.count}"
								value="${hcoConfProgrCalendarioForm.orden}" disabled="disabled">

					</td>
					<td align='left'><c:out
						value="${hcoConfProgrCalendarioForm.turno.nombre}"></c:out></td>
					<td align='left'><c:out
						value="${hcoConfProgrCalendarioForm.guardia.nombreGuardia}"></c:out></td>
					<td align="center">
					
					<c:choose>
		  				<c:when test="${hcoConfProgrCalendarioForm.estado=='0'}">
		  				<siga:Idioma key="gratuita.calendarios.programacion.estado.programada"/>
		  				</c:when>
		  				<c:when test="${hcoConfProgrCalendarioForm.estado=='1'}">
		  				<siga:Idioma key="gratuita.calendarios.programacion.estado.procesando"/>
		  				</c:when>
		  				<c:when test="${hcoConfProgrCalendarioForm.estado=='2'}">
		  				<siga:Idioma key="gratuita.calendarios.programacion.estado.generadaErrores"/>
		  				</c:when>
		  				<c:when test="${hcoConfProgrCalendarioForm.estado=='3'}">
		  				<siga:Idioma key="gratuita.calendarios.programacion.estado.generada"/>
		  				</c:when>
		  				<c:when test="${hcoConfProgrCalendarioForm.estado=='4'}">
		  				<siga:Idioma key="gratuita.calendarios.programacion.estado.cancelado"/>
		  				</c:when>
		  				<c:when test="${hcoConfProgrCalendarioForm.estado=='5'}">
		  					<siga:Idioma key="gratuita.calendarios.programacion.estado.reprogramada"/>
		  				</c:when>
		  				</c:choose>
		  				
		  			</td>
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