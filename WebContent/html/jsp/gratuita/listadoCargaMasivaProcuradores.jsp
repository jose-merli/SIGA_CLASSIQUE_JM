<!-- listadoCargaMasivaProcuradores.jsp -->
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


<siga:Table 
			   name="tablaDatos"
			   border="1"
			      columnNames="cargaMasivaDatosCurriculares.fechaCarga.literal,cargaMasivaDatosCurriculares.usuario.literal,
			      	cargaMasivaDatosCurriculares.nombreFichero.literal,cargaMasivaDatosCurriculares.numRegistros.literal,"
		   columnSizes="20,34,26,12,8">
	
		<c:choose>
		<c:when test="${empty listado}">
			<tr class="notFound">
			   	<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:forEach items="${listado}" var="cargaMasivaProcuradores" varStatus="status">
				<siga:FilaConIconos	fila='${status.count}'
	  				pintarEspacio="no"
	  				botones=""
	  				elementos="${cargaMasivaProcuradores.elementosFila}"
		  			visibleConsulta="false"
		  			visibleEdicion="false"
		  			visibleBorrado="false"
	  				clase="listaNonEdit">
					<td align='left'>
						<input type="hidden" id="idInstitucion_${status.count}" value="${cargaMasivaProcuradores.idInstitucion}"/>
						<input type="hidden" id="idCargaMasivaProcuradores_${status.count}" value="${cargaMasivaProcuradores.idCargaMasivaProcuradores}"/>
						<input type="hidden" id="idFichero_${status.count}" value="${cargaMasivaProcuradores.idFichero}"/>
						<input type="hidden" id="idFicheroLog_${status.count}" value="${cargaMasivaProcuradores.idFicheroLog}"/>
						<c:out value="${cargaMasivaProcuradores.fechaCarga}"/>
 					</td>
 					<td align='left'>
						<c:out value="${cargaMasivaProcuradores.usuario}"/>&nbsp;
					</td>
					<td align='left'>
						<c:out value="${cargaMasivaProcuradores.nombreFichero}"/>&nbsp;
					</td>
					
					<td align='left'>
						<c:out value="${cargaMasivaProcuradores.numRegistros}"/>&nbsp;
					</td>
				</siga:FilaConIconos>
			</c:forEach>
		</c:otherwise>
	</c:choose>

</siga:Table>






