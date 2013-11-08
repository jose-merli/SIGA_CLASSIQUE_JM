<!DOCTYPE html>
<html>
<head>
<!--listadoProgrCalendarios.jsp -->

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
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
</head>

<body>
	<html:hidden property="modo" value=""/>
	<html:hidden property="idProgrCalendario" value=""/>
	<input type="hidden" name="actionModal" />

	<bean:define id="error" name="error" scope="request" />
	<bean:define id="progrCalendariosForms" name="progrCalendariosForms" type="java.util.Collection" scope="request" />
	
	<siga:Table
		name="listadoProgr"
		columnSizes="15,15,15,15,27,13"
		columnNames="gratuita.calendarios.programacion.fechaProgramada,
			gratuita.calendarios.programacion.estado,
			general.literal.desde,
			general.literal.hasta,
			gratuita.calendarios.programacion.conjuntoGuardias,">

		<c:choose >
   			<c:when test="${empty progrCalendariosForms}">   				
				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>	
   			</c:when>
   			
   			<c:otherwise>
				<c:forEach items="${progrCalendariosForms}" var="progrCalendariosForm" varStatus="status">                 					
					<siga:FilaConIconos	fila='${status.count}'
		  				botones="${progrCalendariosForm.botones}" 
		  				elementos="${progrCalendariosForm.elementosFila}"
		  				visibleConsulta="false">
		  				
		  				<td align="center">
		  					<input type="hidden" id="idConjuntoGuardia_${status.count}" value ="${progrCalendariosForm.idConjuntoGuardia}">
				  			<input type="hidden" id="idProgrCalendario_${status.count}" value ="${progrCalendariosForm.idProgrCalendario}">
				  			<input type="hidden" id="estado_${status.count}" value ="${progrCalendariosForm.estado}">
				  			<input type="hidden" id="fechaProgramacion_${status.count}" value ="${progrCalendariosForm.fechaProgramacion}">
				  			<input type="hidden" id="fechaCalInicio_${status.count}" value ="${progrCalendariosForm.fechaCalInicio}">
				  			<input type="hidden" id="fechaCalFin_${status.count}" value ="${progrCalendariosForm.fechaCalFin}">
				  			
		  					<c:out value="${progrCalendariosForm.fechaProgramacionConHoras}"/>
		  				</td>
		  				<td align="center">
			  				<c:choose>
				  				<c:when test="${progrCalendariosForm.estado=='0'}">
		  							<siga:Idioma key="gratuita.calendarios.programacion.estado.programada"/>
		  						</c:when>
		  						
		  						<c:when test="${progrCalendariosForm.estado=='1'}">
		  							<siga:Idioma key="gratuita.calendarios.programacion.estado.procesando"/>
		  						</c:when>
		  						
		  						<c:when test="${progrCalendariosForm.estado=='2'}">
		  							<siga:Idioma key="gratuita.calendarios.programacion.estado.generadaErrores"/>
		  						</c:when>
		  						
		  						<c:when test="${progrCalendariosForm.estado=='3'}">
		  							<siga:Idioma key="gratuita.calendarios.programacion.estado.generada"/>
		  						</c:when>
		  						
		  						<c:when test="${progrCalendariosForm.estado=='4'}">
		  							<siga:Idioma key="gratuita.calendarios.programacion.estado.cancelado"/>
		  						</c:when>
		  						
		  						<c:when test="${progrCalendariosForm.estado=='5'}">
		  							<siga:Idioma key="gratuita.calendarios.programacion.estado.reprogramada"/>
		  						</c:when>
		  					</c:choose>
		  				</td>
		  				<td align="center"><c:out value="${progrCalendariosForm.fechaCalInicio}"/></td>
		  				<td align="center"><c:out value="${progrCalendariosForm.fechaCalFin}"/></td>
		  				<td align="center"><c:out value="${progrCalendariosForm.conjuntoGuardias.descripcion}"/></td>
					</siga:FilaConIconos>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</siga:Table>

	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>

	<script language="JavaScript">

		function refrescarLocal() {
		}
	
		var messageError='${error}';
	
		if (messageError)
			alert(messageError);
	</script>
</body>
</html>