<!--listadoProgrCalendarios.jsp -->

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
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>" />
	<link rel="stylesheet" href="<html:rewrite page="/html/js/themes/base/jquery.ui.all.css"/>" />
		
	<script type="text/javascript" src="<html:rewrite page="/html/js/jquery-1.7.1.js"/>" ></script>
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"
	type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
	
	<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>


	
</head>

<body  >
<html:hidden property="modo" value=""/>
<html:hidden property="idProgrCalendario" value=""/>
<input type="hidden" name="actionModal" />

<bean:define id="error" name="error" scope="request" />
<bean:define id="progrCalendariosForms" name="progrCalendariosForms" type="java.util.Collection" scope="request" />
<div>		
	<table id='listadoProgrCab' border='1' width='100%' cellspacing='0' cellpadding='0'>
		<tr class ='tableTitle'>
			<td align='center' width='15%'><siga:Idioma key="gratuita.calendarios.programacion.fechaProgramada"/></td>
			<td align='center' width='15%'><siga:Idioma key="gratuita.calendarios.programacion.estado"/></td>
			<td align='center' width='15%'><siga:Idioma key="gratuita.calendarios.programacion.fechaCalendario"/>&nbsp;<siga:Idioma key='general.literal.desde'/></td>
			<td align='center' width='15%'><siga:Idioma key='general.literal.hasta'/></td>
			<td align='center' width='27%'><siga:Idioma key="gratuita.calendarios.programacion.conjuntoGuardias"/></td>
			<td align='center' width='13%'></td>
		</tr>
	</table>
</div>
<div id='listadoProgrDiv' style='height:400;width:100%; overflow-y:auto'>

<table class="tablaCampos" id='listadoProgr' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
		<c:choose >
			
   			<c:when test="${empty progrCalendariosForms}">
   				<tr>
					<td width='15%'></td>
					<td width='15%'></td>
					<td width='15%'></td>
					<td width='15%'></td>
					<td width='27%'></td>
					<td width='13%'></td>
				</tr>
   				
				<tr class ='titulitos' id="noRecordFound">
					<td class="titulitos" style="text-align:center" colspan = "6">
						<siga:Idioma key="messages.noRecordFound"/>
	   		 		
	   		 		</td>
	 			</tr>
   			</c:when>
   			<c:otherwise>
		
				<tr>
					<td width='15%'></td>
					<td width='15%'></td>
					<td width='15%'></td>
					<td width='15%'></td>
					<td width='27%'></td>
					<td width='13%'></td>
				</tr>
				<c:forEach items="${progrCalendariosForms}" var="progrCalendariosForm" varStatus="status">                 

					
					<siga:FilaConIconos	fila='${status.count}'
		  				botones="${progrCalendariosForm.botones}" 
		  				elementos="${progrCalendariosForm.elementosFila}"
		  				visibleConsulta="false"
		  				>
	
		  				
		  				
		  			<input type="hidden" id="idConjuntoGuardia_${status.count}" value ="${progrCalendariosForm.idConjuntoGuardia}">
		  			<input type="hidden" id="idProgrCalendario_${status.count}" value ="${progrCalendariosForm.idProgrCalendario}">
		  			<input type="hidden" id="estado_${status.count}" value ="${progrCalendariosForm.estado}">
		  			<input type="hidden" id="fechaProgramacion_${status.count}" value ="${progrCalendariosForm.fechaProgramacion}">
		  			<input type="hidden" id="fechaCalInicio_${status.count}" value ="${progrCalendariosForm.fechaCalInicio}">
		  			<input type="hidden" id="fechaCalFin_${status.count}" value ="${progrCalendariosForm.fechaCalFin}">
		  				
		  			<td align="center">
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
		  			<td align="center">
		  				<c:out value="${progrCalendariosForm.fechaCalInicio}"/>
		  			</td>
		  			<td align="center">
		  				<c:out value="${progrCalendariosForm.fechaCalFin}"/>
		  			</td>
		  			<td align="center">
		  				<c:out value="${progrCalendariosForm.conjuntoGuardias.descripcion}"/>
		  			</td>
		  			
						
					</siga:FilaConIconos>
				</c:forEach>
				
				
			</c:otherwise>
	</c:choose>

</table>

</div>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
		
				style="display: none"></iframe>
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