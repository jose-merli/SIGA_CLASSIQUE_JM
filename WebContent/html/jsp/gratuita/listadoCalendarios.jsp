<!--listadoCalendarios.jsp -->

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
<link id="default" rel="stylesheet" type="text/css"
	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<script src="<html:rewrite page='/html/js/SIGA.js'/>"
	type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"
	type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
	
	<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>


	
</head>

<body  >
<html:hidden property="modo" value=""/>
<input type="hidden" name="actionModal" />

<bean:define id="error" name="error" scope="request" />
<bean:define id="calendariosForms" name="calendariosForms" type="java.util.Collection" scope="request" />
<div>		
	<table id='listadoCalendariosCab' border='1' width='100%' cellspacing='0' cellpadding='0'>
		<tr class ='tableTitle'>
			
			<td align='center' width='20%'><siga:Idioma key='gratuita.calendarios.programacion.turno'/></td>
			<td align='center' width='20%'><siga:Idioma key='gratuita.calendarios.programacion.guardia'/></td>
			<td align='center' width='10%'><siga:Idioma key='gratuita.calendarios.programacion.fechaCalendario'/>&nbsp;<siga:Idioma key='general.literal.desde'/></td>
			<td align='center' width='10%'><siga:Idioma key='general.literal.hasta'/></td>
			<td align='center' width='15%'><siga:Idioma key='gratuita.calendarios.programacion.estado'/></td>
			<td align='center' width='12%'><siga:Idioma key='gratuita.calendarios.programacion.observaciones'/></td>
			<td align='center' width='13%'></td>
		</tr>
	</table>
</div>
<div id='listadoCalendariosDiv' style='height:400;width:100%; overflow-y:auto'>

<table class="tablaCampos" id='listadoCalendarios' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
		<c:choose >
			
   			<c:when test="${empty calendariosForms}">
   				<tr>
   					<td width='20%'></td>
   					<td width='20%'></td>
					<td width='10%'></td>
					<td width='10%'></td>
					<td width='15%'></td>
					<td width='12%'></td>
					<td width='13%'></td>
				</tr>
   				
				<tr class ='titulitos' id="noRecordFound">
					<td class="titulitos" style="text-align:center" colspan = "7">
						<siga:Idioma key="messages.noRecordFound"/>
	   		 		
	   		 		</td>
	 			</tr>
   			</c:when>
   			<c:otherwise>
		
				<tr>
					<td width='20%'></td>
   					<td width='20%'></td>
					<td width='10%'></td>
					<td width='10%'></td>
					<td width='15%'></td>
					<td width='12%'></td>
					<td width='13%'></td>
				</tr>
				<c:forEach items="${calendariosForms}" var="calendariosForm" varStatus="status">                 
					<input type="hidden" id="estado_${status.count}" value ="${calendariosForm.estado}">
		  			<input type="hidden" id="idTurno_${status.count}" value ="${calendariosForm.idTurno}">
		  			<input type="hidden" id="idGuardia_${status.count}" value ="${calendariosForm.idGuardia}">
		  			<input type="hidden" id="idCalendarioGuardias_${status.count}" value ="${calendariosForm.idCalendarioGuardias}">
					
					<siga:FilaConIconos	fila='${status.count}'
		  				botones="${calendariosForm.botones}" 
		  				elementos="${calendariosForm.elementosFila}"
		  				visibleBorrado="false"
		  				visibleEdicion="false"
		  				visibleConsulta="false"
		  				>
		  				
		  			<td align="center">
		  				<c:out value="${calendariosForm.turno.nombre}"/>
		  			</td>
		  			<td align="center">
		  				<c:out value="${calendariosForm.guardia.nombreGuardia}"/>
		  			</td>
		  			<td align="center">
		  				<c:out value="${calendariosForm.fechaInicioTxt}"/>
		  			</td>
		  			<td align="center">
		  				<c:out value="${calendariosForm.fechaFinTxt}"/>
		  			</td>
		  			<td align="center">
		  			
		  			<c:choose>
		  				<c:when test="${calendariosForm.estado=='0'}">
		  				<siga:Idioma key="gratuita.calendarios.programacion.estado.programada"/>
		  				</c:when>
		  				<c:when test="${calendariosForm.estado=='1'}">
		  				<siga:Idioma key="gratuita.calendarios.programacion.estado.procesando"/>
		  				</c:when>
		  				<c:when test="${calendariosForm.estado=='2'}">
		  				<siga:Idioma key="gratuita.calendarios.programacion.estado.generadaErrores"/>
		  				</c:when>
		  				<c:when test="${calendariosForm.estado=='3'}">
		  				<siga:Idioma key="gratuita.calendarios.programacion.estado.generada"/>
		  				</c:when>
		  				<c:when test="${calendariosForm.estado=='4'}">
		  				<siga:Idioma key="gratuita.calendarios.programacion.estado.cancelado"/>
		  				</c:when>
		  				<c:when test="${calendariosForm.estado=='5'}">
		  					<siga:Idioma key="gratuita.calendarios.programacion.estado.pendienteManual"/>
		  				</c:when>
		  				</c:choose>
		  			
		  				
		  				
		  			</td>
		  			
		  			<td align="center">
		  				<c:choose>
		  					<c:when test="${calendariosForm.observaciones!=''}">
		  						<c:out value="${calendariosForm.observaciones}"/>
		  					</c:when>
		  					<c:otherwise>&nbsp;</c:otherwise>
		  				</c:choose>
		  				
		  			</td>
		  			
						
					</siga:FilaConIconos>
				</c:forEach>
				
				
			</c:otherwise>
	</c:choose>

</table>

</div>
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
<script language="JavaScript">



function refrescarLocal()
{
	
}
var messageError='${error}';

if (messageError)
	alert(messageError);



</script>
</body>

</html>