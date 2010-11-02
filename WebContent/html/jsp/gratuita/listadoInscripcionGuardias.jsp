<!-- listadoInscripcionTurnos.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>


<html>

<!-- HEAD -->
<head>
<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>

<!--Step 2 -->
<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript"
	src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>


<!--Step 3 -->
  <!-- defaults for Autocomplete and displaytag -->
  <link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
  <link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />
  
</head>

<body>
<div>		
	<table id='tabInscripcionesCabeceras' border='1' width='100%' cellspacing='0' cellpadding='0'>
		<tr class = 'tableTitle'>
			<td align='center' width='4%'><input type='checkbox' name='chkGeneral' onclick='marcarDesmarcarTodos(this);'/>
			<td align='center' width='8%'><siga:Idioma key="gratuita.listaTurnosLetrados.literal.numeroletrado"/></td>
			<td align='center' width='13%'><siga:Idioma key="gratuita.listaTurnosLetrados.literal.nombreletrado"/></td>
			<td align='center' width='13%'><siga:Idioma key="gratuita.listaTurnosLetrados.literal.turno"/></td>
			<td align='center' id="columnaGuardia" width='13%'><siga:Idioma key="gratuita.listarTurnos.literal.guardias"/></td>
			<td align='center' width='8%'><siga:Idioma key="gratuita.gestionInscripciones.fecha.solicitud.alta"/></td>
			<td align='center' width='8%'><siga:Idioma key="gratuita.gestionInscripciones.fecha.estado.alta"/></td>
			<td align='center' width='8%'><siga:Idioma key="gratuita.gestionInscripciones.solicitud.baja"/></td>
			<td align='center' width='8%'><siga:Idioma key="gratuita.gestionInscripciones.estado.baja"/></td>
			<td align='center'  width='10%'><siga:Idioma key="gratuita.gestionInscripciones.estado"/></td>
			<td align='center' width='7%'>&nbsp;</td>
		</tr>
	</table>
</div>
	<table class="tablaCampos" id='inscripciones' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
		
		
		<logic:notEmpty name="InscripcionTGForm"	property="inscripcionesGuardia">
		<logic:iterate name="InscripcionTGForm" property="inscripcionesGuardia" id="inscripcionGuardia" indexId="index">
				<%index = index-1; %>
				<bean:define id="inscripcionForm" name="inscripcionGuardia" property="inscripcion" type="com.siga.gratuita.form.InscripcionTGForm"/>
				<bean:define id="elementosFila" name="inscripcionForm" property="elementosFila" type="com.siga.tlds.FilaExtElement[]"/>
			
  
				<input type="hidden" id="idInstitucion_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="idInstitucion" />">
				<input type="hidden" id="idTurno_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="idTurno" />">
				<input type="hidden" id="idGuardia_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="idGuardia" />">
				<input type="hidden" id="idPersona_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="idPersona" />">
				<input type="hidden" id="fechaSolicitud_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="fechaSuscripcion" />">
				<input type="hidden" id="observacionesSolicitud_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="observacionesSuscripcion" />">
				<input type="hidden" id="fechaValidacion_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="fechaValidacion" />">
				<input type="hidden" id="observacionesValidacion_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="observacionesValidacion" />">
				<input type="hidden" id="fechaSolicitudBaja_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="fechaSolicitudBaja" />">
				<input type="hidden" id="observacionesBaja_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="observacionesBaja" />">
				<input type="hidden" id="fechaBaja_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="fechaBaja" />">
				<input type="hidden" id="observacionesDenegacion_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="observacionesDenegacion" />">
				<input type="hidden" id="fechaDenegacion_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="fechaDenegacion" />">
				<input type="hidden" id="validarInscripciones_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="turno.validarInscripciones" />">
				<input type="hidden" id="tipoGuardias_<bean:write name='index'/>" value="<bean:write name="inscripcionGuardia" property="turno.guardias" />">
				
				<siga:FilaConIconos	fila='<%=String.valueOf(index.intValue()+1)%>'
	  				botones="" 
	  				pintarEspacio="no"
	  				visibleConsulta="no"
	  				visibleEdicion = "no"
	  				visibleBorrado = "no"
	  				 elementos="<%=elementosFila%>"
	  				clase="listaNonEdit">
				<td align='center' width='4%'>
					<c:choose>
					<c:when test="${inscripcionGuardia.turno.guardias=='0'}">
					<input type="checkbox" value="<%=String.valueOf(index.intValue()+1)%>" name="chkInscripcion" disabled="disabled" >
								</c:when>
					<c:otherwise>
						<input type="checkbox" value="<%=String.valueOf(index.intValue()+1)%>" name="chkInscripcion"  >
					</c:otherwise>
				</c:choose>
					
 						
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
				<td align='center' width='13%'><c:out value="${inscripcionForm.colegiadoNombre}"></c:out></td>
				<td align='center' width='13%'><c:out value="${inscripcionGuardia.turno.abreviatura}"></c:out> </td>
				<td align='center' width='13%'><c:out value="${inscripcionGuardia.guardia.nombre}"></c:out></td>
				
				
				<td align='center' width='8%'><c:out
					value="${inscripcionForm.fechaSolicitud}"></c:out></td>
				<td align='center' width='8%'><c:choose>
					<c:when test="${inscripcionForm.fechaValorAlta==''}">
									&nbsp;
								</c:when>
					<c:otherwise>
						<c:out value="${inscripcionForm.fechaValorAlta}"></c:out>
					</c:otherwise>
				</c:choose></td>

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

				<td width='10%'><c:out value="${inscripcionForm.estado}"></c:out>
				</td>

			
			
				
					
				

			</siga:FilaConIconos>
		</logic:iterate>
	</logic:notEmpty>
	<logic:empty name="InscripcionTGForm"	property="inscripcionesGuardia">
		<br>
   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
 		<br>
	</logic:empty>
</table>
<script>
marcarDesmarcarTodos(document.getElementById("chkGeneral"));
</script>
</body>

</html>