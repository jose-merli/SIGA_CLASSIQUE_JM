<!DOCTYPE html>
<html>
<head>
<!-- listadoConfConjuntoGuardias -->

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

<%
	String dimensiones = "10,45,45";
	String titulosTabla = "gratuita.calendarios.programacion.orden,gratuita.calendarios.programacion.turno,gratuita.calendarios.programacion.guardia";
%>

<body>
	<bean:define id="accion" name="accion" type="java.lang.String" scope="request" />
	<bean:define id="error" name="error" scope="request" />
	<bean:define id="confConjuntoGuardiasForms" name="confConjuntoGuardiasForms" type="java.util.Collection" scope="request" />
	
	<c:if test="${accion=='actualizacion'}">
<%
		dimensiones = "10,10,40,40";
		titulosTabla = "<input type='checkbox' name='chkGeneral' id='chkGeneral' onclick='checkTodos(this);' />," + titulosTabla;
%>			
	</c:if>
	
	<siga:Table
		name="listadoConf"
		columnSizes="<%=dimensiones%>"
		columnNames="<%=titulosTabla%>">

		<c:choose>
			<c:when test="${empty confConjuntoGuardiasForms}">
				<tr class="notFound">
		 			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>	
			</c:when>
		
			<c:otherwise>
				<c:forEach items="${confConjuntoGuardiasForms}" var="confConjuntoGuardiasForm" varStatus="status">
					<siga:FilaConIconos fila='${status.count}' 
						botones=""
						pintarEspacio="no" 
						visibleConsulta="no" 
						visibleEdicion="no"
						visibleBorrado="no" 
						clase="listaNonEdit"
						id="filaConf_${status.count}">
											
						<c:if test="${accion=='actualizacion'}">
							<td align="center">
								<c:choose>
									<c:when test="${confConjuntoGuardiasForm.orden==null}">
									 	<input type="hidden" id="checkConfiguracionOld_${status.count}" value="false">
										<input type="checkbox" id="checkConfiguracion_${status.count}" name="chkConfiguracion" onclick="onclickCheckConfiguracion(${status.count});">
									</c:when>
									
									<c:otherwise>
										<input type="hidden" id="checkConfiguracionOld_${status.count}"	value="true">
										<input type="checkbox" id="checkConfiguracion_${status.count}" name="chkConfiguracion" onclick="onclickCheckConfiguracion(${status.count});" >
									</c:otherwise>
								</c:choose>
							</td>
						</c:if>
						
						<td align="center">
							<c:choose>
								<c:when test="${confConjuntoGuardiasForm.orden!=null}">
									<input type="text" class="boxConsulta" style="width:40px;" id="orden_${status.count}" value="${confConjuntoGuardiasForm.orden}" disabled='disabled' maxlength="5">
								</c:when>
								
								<c:otherwise>
									<input type="text" class="boxConsulta" style="width:40px;" id="orden_${status.count}" value="" disabled='disabled' maxlength="5">
								</c:otherwise>
							</c:choose>
						</td>
						<td align='left'><c:out value="${confConjuntoGuardiasForm.turno.nombre}"></c:out></td>
						<td align='left'>
							<c:out value="${confConjuntoGuardiasForm.guardia.nombreGuardia}"></c:out>
							
							<input type="hidden" id="idTurno_${status.count}" value="${confConjuntoGuardiasForm.turno.id}">
							<input type="hidden" id="idGuardia_${status.count}" value="${confConjuntoGuardiasForm.guardia.id}">
							<input type="hidden" id="ordenOld_${status.count}" value="${confConjuntoGuardiasForm.orden}">
						</td>
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