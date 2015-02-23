<!-- listadoSubtiposCV.jsp -->
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
			<td align='center' width='30%'><b><bean:message key="censo.tiposDatosCurriculares.tipo.literal"/></b></td>
			<td align='center' width='30%'><b><bean:message key="censo.tiposDatosCurriculares.subtipo1.literal"/></b></td>
			<td align='center' width='25%'><b><bean:message key="censo.tiposDatosCurriculares.subtipo2.literal"/></b></td>
			<td align='center' width='10%'>&nbsp;</td>
		</tr>
	</table>

<div id='listadoArchivosDiv' style='height:595px; width: 100%; overflow-y: auto;overflow-x: hidden; '>

<table class="tablaCampos" id='listadoArchivos' border='1' align='center' width='100%' cellspacing='0' cellpadding='0'	>
	
		<c:choose>
		<c:when test="${empty tiposDatosCurriculares}">
			<tr>
					<td width='30%'></td>
					<td width='30%'></td>
					<td width='25%'></td>
					<td width='10%'></td>
				</tr>
		
			<tr class="notFound">
		   		<td colspan="4" class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
		</c:when>
		<c:otherwise>
			<tr>
				<td width='30%'></td>
				<td width='30%'></td>
				<td width='25%'></td>
				<td width='10%'></td>
			</tr>
			<c:forEach items="${tiposDatosCurriculares}" var="tiposDatosCurricular" varStatus="status">
				<siga:FilaConIconos	fila='${status.count}'
	  				pintarEspacio="no"
	  				botones="${tiposDatosCurricular.botones}" 
		  			visibleConsulta="false"
	  				clase="listaNonEdit">
					<td align='left'>
						<input type="hidden" id="idInstitucion_${status.count}" value="${tiposDatosCurricular.idInstitucion}"/>
						<input type="hidden" id="idTipoCV_${status.count}" value="${tiposDatosCurricular.idTipoCV}"/>
						<input type="hidden" id="descripcion_${status.count}" value="${tiposDatosCurricular.tipoDescripcion}"/>
						
						<input type="hidden" id="subTipo1IdInstitucion_${status.count}" value="${tiposDatosCurricular.subTipo1IdInstitucion}"/>
						<input type="hidden" id="subTipo1IdTipo_${status.count}" value="${tiposDatosCurricular.subTipo1IdTipo}"/>
						<input type="hidden" id="subTipo1CodigoExt_${status.count}" value="${tiposDatosCurricular.subTipo1CodigoExt}"/>
						<input type="hidden" id="subTipo1Descripcion_${status.count}" value="${tiposDatosCurricular.subTipo1Descripcion}"/>
						<input type="hidden" id="subTipo1IdRecursoDescripcion_${status.count}" value="${tiposDatosCurricular.subTipo1IdRecursoDescripcion}"/>
						
						<input type="hidden" id="subTipo2IdInstitucion_${status.count}" value="${tiposDatosCurricular.subTipo2IdInstitucion}"/>
						<input type="hidden" id="subTipo2IdTipo_${status.count}" value="${tiposDatosCurricular.subTipo2IdTipo}"/>
						<input type="hidden" id="subTipo2CodigoExt_${status.count}" value="${tiposDatosCurricular.subTipo2CodigoExt}"/>
						<input type="hidden" id="subTipo2Descripcion_${status.count}" value="${tiposDatosCurricular.subTipo2Descripcion}"/>
						<input type="hidden" id="subTipo2IdRecursoDescripcion_${status.count}" value="${tiposDatosCurricular.subTipo2IdRecursoDescripcion}"/>
						
						<c:out value="${tiposDatosCurricular.tipoDescripcion}"/>
 					</td>
 					<td align='left'>
						<c:out value="${tiposDatosCurricular.subTipo1Descripcion}"/>&nbsp;
					</td>
					<td align='left'>
						<c:out value="${tiposDatosCurricular.subTipo2Descripcion}"/>&nbsp;
					</td>
				</siga:FilaConIconos>
			</c:forEach>
		</c:otherwise>
	</c:choose>

</table>

</div>




