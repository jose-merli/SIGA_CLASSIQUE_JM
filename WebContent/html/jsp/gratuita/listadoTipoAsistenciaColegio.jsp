<!DOCTYPE html>
<html>
<head>
<!-- listadoTipoAsistenciaColegio.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>



	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	
	<script type="text/javascript">
		function refrescarLocal() {
			parent.buscar();
		}
	</script>	
</head>

<body>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request" />
<bean:define id="listadoTiposAsistencia" name="listadoTiposAsistencia" scope="request"/>



	<siga:Table 		   
	   name="listadoTiposAsistencia"
	   border="1"
	   columnNames="administracion.catalogos.maestros.literal.tipo.asistencias,gratuita.confGuardia.asistencia.importe,gratuita.confGuardia.asistencia.importeMaximo,gratuita.turno.guardia.literal.tipoGuardia,"
	   columnSizes="45,8,8,29,10" >
		
		<c:choose>
   			<c:when test="${empty listadoTiposAsistencia}">
				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
   			</c:when>
   			
   			<c:otherwise>   
				<c:forEach items="${listadoTiposAsistencia}" var="tipoAsistencia" varStatus="status">
					<siga:FilaConIconos	fila='${status.count}'				    
		  				pintarEspacio="si"
		  				visibleConsulta="si"
		  				visibleEdicion = "si"
		  				visibleBorrado = "si"
		  				clase="listaNonEdit">
								 
						<td align='left'>
													<input type="hidden" name="oculto${status.count}_1" id="oculto${status.count}_1" value="${tipoAsistencia.idInstitucion}"/>
							<input type="hidden" name="oculto${status.count}_2" id="oculto${status.count}_2"  value="${tipoAsistencia.idTipoAsistenciaColegio}"/>

												
							<c:out value="${tipoAsistencia.descripcion}"/>&nbsp;
						</td>
						<td align="right"><c:out  value="${tipoAsistencia.importeNoEditable}"/>&nbsp;</td>
						<td align="right"><c:out value="${tipoAsistencia.importeMaximoNoEditable}"/>&nbsp;</td>
						<td><c:out value="${tipoAsistencia.descripcionTiposGuardia}"/>&nbsp;</td>
						
					</siga:FilaConIconos>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</siga:Table>
<html:form action="${path}"  method="POST" target="mainWorkArea">
	<html:hidden property = "modo"  styleId = "modo" />
	<html:hidden property = "descripcion"  styleId = "descripcion" />
	<html:hidden property = "tipoGuardia"  styleId = "tipoGuardia" />
	<html:hidden property = "visibleMovil"  styleId = "visibleMovil" />
</html:form>
	
	
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"/>
	<!-- FIN: SUBMIT AREA -->		
</body>	
</html>