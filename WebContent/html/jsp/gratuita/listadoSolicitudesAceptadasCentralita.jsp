
<!-- listadoSolitudesAceptadasCentralita.jsp -->

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


<table style="width:100%;BORDER-COLLAPSE: collapse; TABLE-LAYOUT: fixed; BORDER-SPACING: 0px; VISIBILITY: visible" id="listadoInicial" border="0">
<tr class="tableTitle" style="height:25px;">
			   	<th style="width:4%">
			   		<input type='checkbox' name='chkGeneral' onclick='marcarDesmarcarTodos(this);'/>
			   	</th>
			   	<th style="width:8%"><siga:Idioma key='gratuita.mantAsistencias.literal.estado'/></th>
			   	<th style="width:18%"><siga:Idioma key='gratuita.seleccionColegiadoJG.literal.colegiado'/></th>
			   	<th style="width:16%"><siga:Idioma key='gratuita.busquedaAsistencias.literal.guardia'/></th>
			   	<th style="width:14%"><siga:Idioma key='sjcs.solicitudaceptadacentralita.literal.fechaLlamada'/></th>
			   	<th style="width:18%"><siga:Idioma key='sjcs.solicitudaceptadacentralita.literal.centroDetencion'/></th>
			   	<th style="width:10%"><siga:Idioma key='gratuita.mantAsistencias.literal.estado'/></th>
			   	<th style="width:12%">&nbsp;</th>
			   	
			   	
			   	
		   	</tr>


	<div id='listadoArchivosDiv'>
		
		<c:choose>
			<c:when test="${empty solicitudesAceptadasCentralita}">
				<td align='center' colspan ="7" >
					<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
				<td>
			</c:when>
			<c:otherwise>
				
					<c:forEach items="${solicitudesAceptadasCentralita}" var="solicitudAceptadaCentralita" varStatus="status">
						<bean:define id="elementosFila" name="solicitudAceptadaCentralita" property="elementosFila" type="com.siga.tlds.FilaExtElement[]"/>							
						<siga:FilaConIconos	fila='${status.count}'
			  				botones="" 
			  				pintarEspacio="no"
			  				visibleConsulta="no"
			  				visibleEdicion = "no"
			  				visibleBorrado = "no"
			  				elementos="${elementosFila}"
			  				clase="listaNonEdit">
			  				
			  							  				
			  				
							<td align='center'>
								<input type="hidden" id="idSolicitudAceptada_${status.count}" value="${solicitudAceptadaCentralita.idSolicitudAceptada}">
								<input type="hidden" id="idInstitucion_${status.count}" value="${solicitudAceptadaCentralita.idInstitucion}">
								<input type="checkbox" value="${status.count}" name="chkSolicitud"  >
		 					</td>
		 					<td align='left'>
								
								<c:out value="${solicitudAceptadaCentralita.idSolicitudAceptada}"></c:out>
								
							</td>
							<td align='left'>
								
								<c:out value="${solicitudAceptadaCentralita.descripcionColegiado}"></c:out>
								
							</td>
							<td align='left'><c:out value="${solicitudAceptadaCentralita.nombreGuardia}"></c:out></td>
							<td align='center'><c:out value="${solicitudAceptadaCentralita.fechaLlamadaHoras}"></c:out></td>
							<td align='left'><c:out value="${solicitudAceptadaCentralita.nombreCentroDetencion}"></c:out></td>
			
							<td align='left'><c:out value="${solicitudAceptadaCentralita.descripcionEstado}"></c:out></td>
							
						</siga:FilaConIconos>
					</c:forEach>
			</c:otherwise>
		</c:choose>
	</div>	
		
		</table>
		

