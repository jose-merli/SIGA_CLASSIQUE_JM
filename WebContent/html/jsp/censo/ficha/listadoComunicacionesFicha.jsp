<!DOCTYPE html PUBLIC "-//IETF//DTD HTML 2.0//EN">

<!-- listadoComunicacionesFicha.jsp-->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">

<!-- TAGLIBS -->
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" 		prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri = "struts-logic.tld" 	prefix="logic"%>
<%@ taglib uri = "c.tld" 				prefix="c"%>

<html:form action="/ENV_Documentos.do" method="POST" target="submitArea">
	<html:hidden property="modo" value = "download"/>
	<html:hidden property="idInstitucion"/>
	<html:hidden property="idEnvio"/>
	<html:hidden property="idDocumento"/>
	<html:hidden property="tipo"/>			

</html:form>


<c:set var="columnNames" value="envios.definir.literal.identificador,envios.definir.literal.fechaenvio,envios.definir.literal.tipoenvio,envios.definir.literal.nombre,envios.plantillas.literal.asunto,envios.definirEnvios.documentos.cabecera,"/>
<c:set var="columnSizes" value="8,10,10,20,20,47," />
<siga:Table	
		name="comunicacionesSalida"
		border="1"
		columnNames="${columnNames}"
		columnSizes="${columnSizes}"
		fixedHeight="100%">

	<c:choose>
		
		<c:when test="${empty comunicaciones}">
			<tr class="notFound">
	   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>				
		</c:when>
		<c:otherwise>
				<c:forEach items="${comunicaciones}" var="comunicacion" varStatus="status">
							<siga:FilaConIconosExtExt fila='${status.count}' botones = "" pintarEspacio="no" visibleBorrado="false" visibleEdicion="false" visibleConsulta="false"   nombreTablaPadre="comunicaciones"  clase="listaNonEdit">
								<td>
									<c:out value="${comunicacion.idEnvio}"/>
								</td>
								<td align="center"><c:out value="${comunicacion.fecha}"/></td>
								<td><c:out value="${comunicacion.tipo}"/></td>
								<td ><siga:Idioma key="${comunicacion.nombre}" /></td>
								<td><siga:Idioma key="${comunicacion.asunto}" /></td>
								<td>
									<table>
									<c:forEach items="${comunicacion.documentos}" var="documento" >
										<c:choose>
											<c:when test="${status.count%2==0}">
												<tr id="fila_${status.count}" class="filaTablaPar" >
											</c:when>
											<c:otherwise>
												<tr id="fila_${status.count}" class="filaTablaImpar">
											</c:otherwise>
										</c:choose>
											<td style="border: none;" >	
												<img id="iconoboton_download1" hspace="0"
												src="/SIGA/html/imagenes/bdownload_off.gif" style="cursor:pointer;" 
												alt="Descargar" name="iconoFila" title="Descargar" border="0" 
												onClick="accionDescargaDocumento('${comunicacion.idEnvio}','${comunicacion.idInstitucion}','${documento.id}','${documento.tipo}')" 
												onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('download_1','','/SIGA/html/imagenes/bdownload_on.gif',1)">
											
											</td>
											<td style="border: none;">
												<c:out value="${documento.descripcion}"/>
											</td>
											
										</tr>
											
									</c:forEach>
									</table>
								
								</td>
							</siga:FilaConIconosExtExt>
				</c:forEach>
			
		</c:otherwise>
	</c:choose>
</siga:Table>	    
<siga:Paginador 
	totalRegistros="${totalRegistros}"
	registrosPorPagina="${registrosPorPagina}"
	paginaSeleccionada="${paginaSeleccionada}" idioma="${usrBean.language}"
	modo="listadoComunicacionesAjax"								
	clase="paginator" 
	divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:27px; left: 0px"
	distanciaPaginas=""
	action="${pageContext.request.contextPath}${path}.do?noReset=true&totalRegistros=${totalRegistros}&registrosPorPagina=${registrosPorPagina}" />
	
<script type="text/javascript">

	
	function paginar(pagina) {
		var parametros = formPaginador.action;
		var idInstitucion = document.ComunicacionesForm.idInstitucion.value;
		var idPersona = document.ComunicacionesForm.idPersona.value;

		var data = "idInstitucion=" + idInstitucion;
		if (idPersona != '')
			data += "&idPersona=" + idPersona;
		if (pagina)
			data += "&pagina=" + pagina;
		if (parametros) {
			data += '&' + parametros;
		}
		sub();
		var accion = document.ComunicacionesForm.action;
		jQuery.ajax({
			type : "POST",
			url : accion + "?modo=listadoComunicacionesAjax",
			data : data,
			success : function(response) {
				console.log(response);
				jQuery('#divListado').html(response);
				fin();
			},
			error : function(e) {
				fin();
				alert('Error: ' + e);
			}
		});

	}
	function accionDescargaDocumento(idEnvio,idInstitucion,idDocumento,tipo)
	{
		document.DocumentosForm.idEnvio.value = idEnvio;
		document.DocumentosForm.idInstitucion.value =idInstitucion;
		document.DocumentosForm.idDocumento.value = idDocumento;
		document.DocumentosForm.tipo.value = tipo;
		document.DocumentosForm.submit();
	}

	
</script>