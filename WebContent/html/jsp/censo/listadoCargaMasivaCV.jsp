<!-- listadoCargaMasivaCV.jsp -->
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

	
	<table id='listadoArchivosCab' border='1' width='100%' cellspacing='0' cellpadding='0'>
		<tr class ='tableTitle'>
			<td align='center' width='30%'><b><siga:Idioma key="cargaMasivaDatosCurriculares.fechaCarga.literal"/></b></td>
			<td align='center' width='20%'><b><siga:Idioma key="cargaMasivaDatosCurriculares.usuario.literal"/></b></td>
			<td align='center' width='20%'><b><siga:Idioma key="cargaMasivaDatosCurriculares.nombreFichero.literal"/></b></td>
			<td align='center' width='15%'><b><siga:Idioma key="cargaMasivaDatosCurriculares.numRegistros.literal"/></b></td>
			<td align='center' width='10%'>&nbsp;</td>
		</tr>
	</table>

<div id='listadoArchivosDiv' style='height:595px; width: 100%; overflow-y: auto;overflow-x: hidden; '>

<table class="tablaCampos" id='listadoArchivos' border='1' align='center' width='100%' cellspacing='0' cellpadding='0'	>
	
		<c:choose>
		<c:when test="${empty listado}">
			<tr>
				<td width='30%'></td>
				<td width='20%'></td>
				<td width='20%'></td>
				<td width='15%'></td>
				<td width='10%'></td>
			</tr>
		
			<tr class="notFound">
		   		<td colspan="5" class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
		</c:when>
		<c:otherwise>
			<tr>
				<td width='30%'></td>
				<td width='20%'></td>
				<td width='20%'></td>
				<td width='15%'></td>
				<td width='10%'></td>
			</tr>
			<c:forEach items="${listado}" var="cargaMasivaCV" varStatus="status">
				<siga:FilaConIconos	fila='${status.count}'
	  				pintarEspacio="no"
	  				botones=""
	  				elementos="${cargaMasivaCV.elementosFila}"
		  			visibleConsulta="false"
		  			visibleEdicion="false"
		  			visibleBorrado="false"
	  				clase="listaNonEdit">
					<td align='left'>
						<input type="hidden" id="idInstitucion_${status.count}" value="${cargaMasivaCV.idInstitucion}"/>
						<input type="hidden" id="idCargaMasivaCV_${status.count}" value="${cargaMasivaCV.idCargaMasivaCV}"/>
						<input type="hidden" id="idFichero_${status.count}" value="${cargaMasivaCV.idFichero}"/>
						<input type="hidden" id="idFicheroLog_${status.count}" value="${cargaMasivaCV.idFicheroLog}"/>
						<c:out value="${cargaMasivaCV.fechaCarga}"/>
 					</td>
 					<td align='left'>
						<c:out value="${cargaMasivaCV.usuario}"/>&nbsp;
					</td>
					<td align='left'>
						<c:out value="${cargaMasivaCV.nombreFichero}"/>&nbsp;
					</td>
					
					<td align='left'>
						<c:out value="${cargaMasivaCV.numRegistros}"/>&nbsp;
					</td>
				</siga:FilaConIconos>
			</c:forEach>
		</c:otherwise>
	</c:choose>

</table>

</div>




