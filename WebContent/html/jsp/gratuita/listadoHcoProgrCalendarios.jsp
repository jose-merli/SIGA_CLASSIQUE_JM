<!DOCTYPE html>
<html>
<head>
<!-- listadoHcoProgrCalendarios -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
</head>

<body>
	<bean:define id="error" name="error" scope="request" />
	<bean:define id="hcoConfProgrCalendarioForms" name="hcoConfProgrCalendarioForms" type="java.util.Collection" scope="request" />
	
	<siga:Table
		name="listadoHcoProgramacion"
		columnSizes="10,35,35,10,10"
		columnNames="gratuita.calendarios.programacion.orden,
			gratuita.calendarios.programacion.turno,
			gratuita.calendarios.programacion.guardia,
			gratuita.calendarios.programacion.estado,">
	
		<c:choose>
			<c:when test="${empty hcoConfProgrCalendarioForms}">
				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>	
			</c:when>
		
			<c:otherwise>
				<c:forEach items="${hcoConfProgrCalendarioForms}" var="hcoConfProgrCalendarioForm" varStatus="status">
					<c:set var="botones" value="" />
					<c:if test="${hcoConfProgrCalendarioForm.estado=='2'||hcoConfProgrCalendarioForm.estado=='4'}">
						<c:set var="botones" value="B" />
					</c:if>
			
					<siga:FilaConIconos fila='${status.count}' 
						botones="${botones}" 
						elementos="${hcoConfProgrCalendarioForm.elementosFila}"
						pintarEspacio="no" 
						visibleConsulta="no" 
						visibleEdicion="no"
						visibleBorrado="no" 
						clase="listaNonEdit"
						id="filaConf_${status.count}">												
				
						<td align="center">
							<input type="hidden" id="idTurno_${status.count}" value="${hcoConfProgrCalendarioForm.idTurno}">
							<input type="hidden" id="idGuardia_${status.count}" value="${hcoConfProgrCalendarioForm.idGuardia}">
							<input type="hidden" id="idCalendarioGuardias_${status.count}" value="${hcoConfProgrCalendarioForm.idCalendarioGuardias}">						
							<input type="hidden" id="idConjuntoGuardia_${status.count}" value="${hcoConfProgrCalendarioForm.idConjuntoGuardia}">
							
							<input type="text" class="boxConsulta" style="width:40px;" id="orden_${status.count}" value="${hcoConfProgrCalendarioForm.orden}" disabled="disabled">
						</td>
						<td align='left'><c:out value="${hcoConfProgrCalendarioForm.turno.nombre}"></c:out></td>
						<td align='left'><c:out value="${hcoConfProgrCalendarioForm.guardia.nombreGuardia}"></c:out></td>
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
	  					
	  					<c:if test="${botones=='' && (hcoConfProgrCalendarioForm.elementosFila==null || hcoConfProgrCalendarioForm.elementosFila[0]==null)}">
							<td>&nbsp;</td>
						</c:if>
					</siga:FilaConIconos>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</siga:Table>

	<script type="text/javascript">
		var messageError='${error}';
		
		if (messageError)
			alert(messageError);
	</script>
</body>
</html>