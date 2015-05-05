<!-- listadoCargaMasivaGF.jsp -->
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
			<c:forEach items="${listado}" var="cargaMasivaGF" varStatus="status">
				<siga:FilaConIconos	fila='${status.count}'
	  				pintarEspacio="no"
	  				botones=""
	  				elementos="${cargaMasivaGF.elementosFila}"
		  			visibleConsulta="false"
		  			visibleEdicion="false"
		  			visibleBorrado="false"
	  				clase="listaNonEdit">
					<td align='left'>
						<input type="hidden" id="idInstitucion_${status.count}" value="${cargaMasivaGF.idInstitucion}"/>
						<input type="hidden" id="idCargaMasivaCV_${status.count}" value="${cargaMasivaGF.idCargaMasivaCV}"/>
						<input type="hidden" id="idFichero_${status.count}" value="${cargaMasivaGF.idFichero}"/>
						<input type="hidden" id="idFicheroLog_${status.count}" value="${cargaMasivaGF.idFicheroLog}"/>
						<c:out value="${cargaMasivaGF.fechaCarga}"/>
 					</td>
 					<td align='left'>
						<c:out value="${cargaMasivaGF.usuario}"/>&nbsp;
					</td>
					<td align='left'>
						<c:out value="${cargaMasivaGF.nombreFichero}"/>&nbsp;
					</td>
					
					<td align='left'>
						<c:out value="${cargaMasivaGF.numRegistros}"/>&nbsp;
					</td>
				</siga:FilaConIconos>
			</c:forEach>
		</c:otherwise>
	</c:choose>

</siga:Table>






