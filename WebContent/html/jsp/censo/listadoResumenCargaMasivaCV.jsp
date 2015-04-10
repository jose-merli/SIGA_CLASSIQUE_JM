<!-- listadoResumenCargaMasivaCV.jsp.jsp -->
<!DOCTYPE html>
<html>
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
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript">
	
	function accionVolver() {
			document.forms['CargaMasivaCVForm'].modo.value="volver";
			document.forms['CargaMasivaCVForm'].target = "mainWorkArea";
			document.forms['CargaMasivaCVForm'].submit();
	}
	
	
	</script>
	
</head>
<body>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<html:form action="${path}"   target="submitArea">
  	<html:hidden property="modo"/>
</html:form>


<table class="tablaTitulo" align="center">
		<tr>
		<td class="titulitos">
			Procesado de fichero para la carga masiva
		</td>
		</tr>
		</table>
<siga:Table 
	   	      name="tablaDatos"
	   		  border="1"
	   		  columnNames="colegiadoNumero,colegiadoNombre,tipoCVNombre,fechaInicio,fechaFin,subtipoCV1Nombre,subtipoCV2Nombre,fechaVerificacion,"
	   		  columnSizes="6,20,15,8,8,15,15,8,5"
	   		  
	   		      >
		<c:choose>
		<c:when test="${empty listado}">
		
			<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:forEach items="${listado}" var="datoCV" varStatus="status">
				<siga:FilaConIconos	fila='${status.count}'
	  				pintarEspacio="no"
	  				botones=""
		  			visibleConsulta="false"
		  			visibleEdicion="false"
		  			visibleBorrado="false"
	  				clase="listaNonEdit">
						
						<td align='left'><c:out value="${datoCV.colegiadoNumero}" /></td>
						<!--td align='left'><c:out value="${datoCV.personaNif}" /></td-->
						<td align='left'><c:out value="${datoCV.personaNombre}" /></td>
						<!-- td align='left'><c:out value="${datoCV.tipoCVCod}" /></td-->
						<td align='left'><c:out value="${datoCV.tipoCVNombre}" /></td>
						<td align='left'><c:out value="${datoCV.fechaInicio}" /></td>
						<td align='left'><c:out value="${datoCV.fechaFin}" /></td>
						<!-- td align='left'><c:out value="${datoCV.subtipoCV1Cod}" /></td-->
						<td align='left'><c:out value="${datoCV.subtipoCV1Nombre}" /></td>
						<!-- td align='left'><c:out value="${datoCV.subtipoCV2Cod}" /></td-->
						<td align='left'><c:out value="${datoCV.subtipoCV2Nombre}" /></td>
						<td align='left'><c:out value="${datoCV.creditos}" /></td>
						<td align='left'><c:out value="${datoCV.error}" /></td>
						<!--  td align='left'><c:out value="${datoCV.fechaVerificacion}" /></td-->
						<!-- td align='left'><c:out value="${datoCV.descripcion}" /></td-->


					</siga:FilaConIconos>
			</c:forEach>
		</c:otherwise>
	</c:choose>

</siga:Table>
<siga:ConjBotonesAccion botones="V,G" />
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 	style="display: none" />
</body>

</html>
