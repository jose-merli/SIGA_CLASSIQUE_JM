<!-- listadoResumenCargaMasivaGF.jsp -->
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
			document.forms['CargaMasivaGFForm'].modo.value="volver";
			document.forms['CargaMasivaGFForm'].target = "mainWorkArea";
			document.forms['CargaMasivaGFForm'].submit();
	}
	function accionGuardar() {
		sub();
		document.forms['CargaMasivaGFForm'].modo.value="processExcelFile";
		document.forms['CargaMasivaGFForm'].target = "submitArea";
		document.forms['CargaMasivaGFForm'].submit();
	}
	function refrescarLocal() {
		accionVolver();
	}
	function accionDownload() {
		document.forms['CargaMasivaGFForm'].modo.value="downloadExcelError";
		document.forms['CargaMasivaGFForm'].target = "submitArea";
		document.forms['CargaMasivaGFForm'].submit();
	}
	
	
	</script>
	
</head>
<body>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<html:form action="${path}"   target="submitArea">
  	<html:hidden property="modo"/>
  	<html:hidden property="rutaFichero"/>
  	<html:hidden property="nombreFichero"/>
</html:form>

<table class="tablaTitulo" align="center">
	<tr>
	<td class="titulitos">
		<siga:Idioma key="cargaMasivaDatosCurriculares.ficheroProcesado.literal"/>&nbsp;<c:out value="${CargaMasivaGFForm.nombreFichero}" />
	</td>
	</tr>
</table>
		
<siga:Table 
	   	      name="tablaDatos"
	   		  border="1"
	   		  
	   		  columnNames="cargaMasivaDatosCurriculares.cliente.literal,administracion.grupos.asignarUsuariosGrupo.literal.grupo,"
	   		  columnSizes="50,44,6"
	   		  
	   		      >
		<c:choose>
		<c:when test="${empty listado}">
		<c:set var="botones" value="V" />
		
			<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:set var="botones" value="V,G" />
			<c:forEach items="${listado}" var="datoGF" varStatus="status">
				<siga:FilaConIconos	fila='${status.count}'
	  				pintarEspacio="no"
	  				botones=""
		  			visibleConsulta="false"
		  			visibleEdicion="false"
		  			visibleBorrado="false"
	  				clase="listaNonEdit">
						
						<td align='left'><c:out value="${datoGF.personaNombre}" /></td>
						<td align='left'><c:out value="${datoGF.nombreGrupo}" /></td>
						

						
						<td align='left'>
							<c:choose>
								<c:when test="${datoGF.error!=''}">
								<c:set var="botones" value="V,D" />
									Error
								</c:when>
								<c:otherwise>
									&nbsp;
								</c:otherwise>
							</c:choose>
						</td>

					</siga:FilaConIconos>
			</c:forEach>
		</c:otherwise>
	</c:choose>

</siga:Table>
<siga:ConjBotonesAccion botones="${botones}" />
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 	style="display: none" />
</body>

</html>