<!DOCTYPE html>
<html>
<head>
<!-- listadoInscripcionTurnos.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-15">
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
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
</head>

<body>
	<siga:Table 		   
	   name="listadoInicial"
	   border="1"
	   columnNames="<input type='checkbox' name='chkGeneral' onclick='marcarDesmarcarTodos(this);'/>,gratuita.listaTurnosLetrados.literal.numeroletrado,gratuita.listaTurnosLetrados.literal.nombreletrado,gratuita.listaTurnosLetrados.literal.turno,gratuita.gestionInscripciones.fecha.solicitud.alta,gratuita.gestionInscripciones.fecha.estado.alta,gratuita.gestionInscripciones.solicitud.baja,gratuita.gestionInscripciones.estado.baja,gratuita.gestionInscripciones.estado,"
	   columnSizes="4,8,19,19,8,8,8,8,10,8">

		<logic:notEmpty name="InscripcionTGForm"	property="inscripcionesTurno">
			<logic:iterate name="InscripcionTGForm" property="inscripcionesTurno" id="inscripcionTurno" indexId="index">
				<%index = index-1; %>
				<bean:define id="inscripcionForm" name="inscripcionTurno" property="inscripcion" type="com.siga.gratuita.form.InscripcionTGForm"/>
				<bean:define id="elementosFila" name="inscripcionForm" property="elementosFila" type="com.siga.tlds.FilaExtElement[]"/>				
				
				<siga:FilaConIconos	fila='<%=String.valueOf(index.intValue()+1)%>' botones="" pintarEspacio="no" visibleConsulta="no" visibleEdicion = "no" visibleBorrado = "no" elementos="<%=elementosFila%>" clase="listaNonEdit">
					<td align='center' width='4%'>
						<input type="hidden" id="idInstitucion_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="idInstitucion" />">
						<input type="hidden" id="idTurno_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="idTurno" />">
						<input type="hidden" id="idPersona_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="idPersona" />">
						<input type="hidden" id="fechaSolicitud_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="fechaSolicitud" />">
						<input type="hidden" id="observacionesSolicitud_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="observacionesSolicitud" />">
						<input type="hidden" id="fechaValidacion_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="fechaValidacion" />">
						<input type="hidden" id="observacionesValidacion_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="observacionesValidacion" />">
						<input type="hidden" id="fechaSolicitudBaja_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="fechaSolicitudBaja" />">
						<input type="hidden" id="observacionesBaja_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="observacionesBaja" />">
						<input type="hidden" id="observacionesValBaja_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="observacionesValBaja" />">
						<input type="hidden" id="fechaBaja_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="fechaBaja" />">
						<input type="hidden" id="observacionesDenegacion_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="observacionesDenegacion" />">
						<input type="hidden" id="fechaDenegacion_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="fechaDenegacion" />">
						<input type="hidden" id="validarInscripciones_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="turno.validarInscripciones" />">
						<input type="hidden" id="tipoGuardias_<bean:write name='index'/>" value="<bean:write name="inscripcionTurno" property="turno.guardias" />">
						<input type="hidden" id='estado_<bean:write name="index"/>' value='<bean:write name="inscripcionTurno" property="estado" />'>
					
						<input type="checkbox" value="<%=String.valueOf(index.intValue()+1)%>" name="chkInscripcion" />
					</td>
					<td align='center' width='8%'>
						<c:choose>
							<c:when test="${inscripcionForm.colegiadoNumero==''}">
								&nbsp;
							</c:when>
							<c:otherwise>
								<c:out value="${inscripcionForm.colegiadoNumero}"></c:out>
							</c:otherwise>
						</c:choose>
					</td>
					<td align='center' width='19%'>
						<c:out value="${inscripcionForm.colegiadoNombre}"></c:out>
					</td>
					<td align='center' width='19%'>
						<c:out value="${inscripcionTurno.turno.abreviatura}"></c:out>
					</td>
					<td align='center' width='8%'>
						<c:out value="${inscripcionForm.fechaSolicitud}"></c:out>
					</td>
					<td align='center' width='8%'>
						<c:choose>
							<c:when test="${inscripcionForm.fechaValorAlta==''}">
								&nbsp;
							</c:when>
							<c:otherwise>
								<c:out value="${inscripcionForm.fechaValorAlta}"></c:out>
							</c:otherwise>
						</c:choose>
					</td>
					<td align='center' width='8%'><c:choose>
						<c:when test="${inscripcionForm.fechaSolicitudBaja==''}">
										&nbsp;
									</c:when>
						<c:otherwise>
							<c:out value="${inscripcionForm.fechaSolicitudBaja}"></c:out>
						</c:otherwise>
					</c:choose></td>	
					<td align='center' width='8%'>
						<c:choose>
							<c:when test="${inscripcionForm.fechaValorBaja==''}">
									&nbsp;
							</c:when>
						<c:otherwise>
							<c:out value="${inscripcionForm.fechaValorBaja}"></c:out>
						</c:otherwise>
					</c:choose></td>
	
					<td width='10%'>
						<c:out value="${inscripcionForm.estado}"></c:out>
					</td>	
				</siga:FilaConIconos>
			</logic:iterate>
		</logic:notEmpty>
		
		<logic:empty name="InscripcionTGForm"	property="inscripcionesTurno">
	 		<tr class="notFound">
				<td class="titulitos">
					<siga:Idioma key="messages.noRecordFound"/>
				</td>
			</tr>
		</logic:empty>
	</siga:Table>
</body>
</html>