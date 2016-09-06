
<!-- listadoSolitudesAceptadasCentralita.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type"
	content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>


<bean:define id="path" name="org.apache.struts.action.mapping.instance"
	property="path" scope="request" />
<input type="hidden" id="mensajeSuccess" name="mensajeSuccess" value="${mensajeSuccess}"/>
<siga:Table name="listadoInicial" border="1"
	columnNames="<input type='checkbox' name='chkGeneral' onclick='marcarDesmarcarTodos(this);'/>,
		    gratuita.busquedaAsistencias.literal.idAvisoCentralita,gratuita.seleccionColegiadoJG.literal.colegiado,gratuita.busquedaAsistencias.literal.guardia,
		    sjcs.solicitudaceptadacentralita.literal.fechaLlamada,sjcs.solicitudaceptadacentralita.literal.centroDetencion,gratuita.mantAsistencias.literal.estado,"
	columnSizes="4,8,18,16,14,18,10,12">
	<c:choose>
		<c:when test="${empty solicitudesAceptadasCentralita}">
			<tr class="notFound">
				<td class="titulitos"><siga:Idioma key="messages.noRecordFound" /></td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:forEach items="${solicitudesAceptadasCentralita}"
				var="solicitudAceptadaCentralita" varStatus="status">
				<bean:define id="elementosFila" name="solicitudAceptadaCentralita"
					property="elementosFila" type="com.siga.tlds.FilaExtElement[]" />
				<siga:FilaConIconos fila='${status.count}' botones=""
					pintarEspacio="no" visibleConsulta="no" visibleEdicion="no"
					visibleBorrado="no" elementos="${elementosFila}"
					clase="listaNonEdit">
					<td align='center'><input type="hidden"
						id="idSolicitudAceptada_${status.count}"
						value="${solicitudAceptadaCentralita.idSolicitudAceptada}">
						<input type="hidden" id="idInstitucion_${status.count}"
						value="${solicitudAceptadaCentralita.idInstitucion}"> 
						<input type="hidden"
						id="idLlamada_${status.count}"
						value="${solicitudAceptadaCentralita.idLlamada}">
						<input type="hidden"
						id="fechaLlamada_${status.count}"
						value="${solicitudAceptadaCentralita.fechaLlamadaHoras}">
						<input
						type="checkbox" value="${status.count}" name="chkSolicitud">
						<input type="hidden"
						id="numAviso_${status.count}"
						value="${solicitudAceptadaCentralita.numAvisoCV}">
					</td>
					<td align="right"><c:out
							value="${solicitudAceptadaCentralita.numAvisoCV}"></c:out></td>
					<td align='left'><c:out
							value="${solicitudAceptadaCentralita.descripcionColegiado}"></c:out>
					</td>
					<td align='left'><c:out value="${solicitudAceptadaCentralita.idGuardia}"/>&nbsp;<c:out
							value="${solicitudAceptadaCentralita.nombreGuardia}"></c:out></td>
					<td align='left'><c:out
							value="${solicitudAceptadaCentralita.fechaLlamadaHoras}"></c:out></td>
					<td align='left'><c:out value="${solicitudAceptadaCentralita.idCentroDetencion}"/>&nbsp;<c:out
							value="${solicitudAceptadaCentralita.nombreCentroDetencion}"></c:out></td>
					<td align='left'><c:out
							value="${solicitudAceptadaCentralita.descripcionEstado}"></c:out></td>
				</siga:FilaConIconos>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</siga:Table>
<siga:Paginador 
	totalRegistros="${totalRegistros}"
	registrosPorPagina="${registrosPorPagina}"
	paginaSeleccionada="${paginaSeleccionada}" idioma="${usrBean.language}"
	modo="getAjaxBusqueda"								
	clase="paginator" 
	divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:27px; left: 0px"
	distanciaPaginas=""
	action="${pageContext.request.contextPath}${path}.do?noReset=true&totalRegistros=${totalRegistros}&registrosPorPagina=${registrosPorPagina}" />

<script type="text/javascript">
	if(document.getElementById("mensajeSuccess") && document.getElementById("mensajeSuccess").value!=''){
		alert(document.getElementById("mensajeSuccess").value,'alert');
	}
	
	function paginar(pagina){
		var action = formPaginador.action;
		buscarSolicitudesAceptadas(pagina,action);
	
	}
</script>



