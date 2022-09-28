
<!-- listadoIntercambiosCatalunya.jsp -->

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

<c:set var="colorEJG" value="" />
<bean:define id="path" name="org.apache.struts.action.mapping.instance"
	property="path" scope="request" />
<input type="hidden" id="mensajeSuccess" name="mensajeSuccess" class="inputNotSelect" value="${mensajeSuccess}"/>
<siga:Table name="listadoInicial" border="1"
	columnNames="censo.busquedaClientesAvanzada.literal.colegio,
	gratuita.mantActuacion.literal.fecha,
	gratuita.mantActuacion.literal.descripcion,
	gratuita.mantActuacion.literal.anio,
	gratuita.calendarioGuardias.literal.periodo,
	gratuita.mantAsistencias.literal.estado,"
	columnSizes="10,20,10,10,20,12">
					
	<c:choose>
		<c:when test="${empty listadoRegistros}">
			<tr class="notFound">
				<td class="titulitos"><siga:Idioma key="messages.noRecordFound" /></td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:forEach items="${listadoRegistros}"	var="intercambio" varStatus="status">
				<bean:define id="elementosFila" name="intercambio"
					property="elementosFila" type="com.siga.tlds.FilaExtElement[]" />
				<siga:FilaConIconos fila='${status.count}' botones=""
					pintarEspacio="no" visibleConsulta="no" visibleEdicion="no"
					visibleBorrado="no"  elementos="${elementosFila}"
					
					clase="listaNonEdit">
					<td align='left'>
						<input type="hidden" id="idInstitucion_${status.count}"
						value="${intercambio.idInstitucion}" class="inputNotSelect"/> 
						<input type="hidden" id="idIntercambio_${status.count}"
						value="${intercambio.idIntercambio}" class="inputNotSelect"/>
						<c:out	value="${intercambio.abreviaturaInstitucion}"/>
					</td>
					<td><c:out	value="${intercambio.fechaCreacion}"/></td>
					<td><c:out	value="${intercambio.descripcion}"/></td>
					<td align='center'><c:out value="${intercambio.anio}"/></td>
					<td align='left'><c:out	value="${intercambio.nombrePeriodo}"/></td>
					<c:choose> 
						<c:when test="${intercambio.idEstado==60}">
							<c:set var="colorEJG" value="color:red;" />
						</c:when>
						<c:when test="${intercambio.idEstado==95}">
							<c:set var="colorEJG" value="color:blue;" />
						</c:when>
						<c:otherwise>
						<c:set var="colorEJG" value="" />
						 </c:otherwise>
					</c:choose>
					<td align='left' style="${colorEJG}">
						<c:choose>
							<c:when test="${GestionEconomicaCatalunyaForm.idInstitucion=='3001' }">
								<c:out		value="${intercambio.descripcionEstadoConsell}"/>
							</c:when>
							<c:otherwise>
								<c:out		value="${intercambio.descripcionEstadoIca}"/>
							</c:otherwise>
						</c:choose>
						
					</td>
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
	divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
	distanciaPaginas=""
	action="${pageContext.request.contextPath}${path}.do?noReset=true&totalRegistros=${totalRegistros}&registrosPorPagina=${registrosPorPagina}" />

<script type="text/javascript">
	if(document.getElementById("mensajeSuccess") && document.getElementById("mensajeSuccess").value!=''){
		alert(document.getElementById("mensajeSuccess").value,'alert');
	}
	
	function paginar(pagina){
		var action = formPaginador.action;
		buscarIntercambios(pagina,action);
	
	}
	
</script>



