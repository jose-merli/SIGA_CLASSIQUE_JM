<!DOCTYPE html>
<html>
<head>
<!-- consultaLEC.jsp -->
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
	<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
	<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
	<%@ taglib uri = "struts-html.tld" prefix="html"%>
	<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
	<%@ taglib uri="c.tld" prefix="c"%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
			<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.top.close();
		}
	</script>
<!-- FIN: SCRIPTS BOTONES -->

</head>

<body>
	<bean:define id="consultaLEC" name="consultaLEC" scope="request"/>
	
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="38">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="factSJCS.busquedaRetAplicadas.titulo.importesImplicados" /></td>
		</tr>
	</table>
	
	<siga:Table 
		name="tablaResultados" 
		border="2"
		columnNames="
			factSJCS.busquedaRetAplicadas.literal.pagoRelacionado,
			factSJCS.busquedaRetAplicadas.literal.anyomes,
			factSJCS.busquedaRetAplicadas.literal.importeAntRetener,
			factSJCS.busquedaRetAplicadas.literal.importeAntRetenido,
			factSJCS.busquedaRetAplicadas.literal.importeSMI,
			factSJCS.busquedaRetAplicadas.literal.importeMesRetener,
			factSJCS.busquedaRetAplicadas.literal.importeMesRetenido,
			factSJCS.busquedaRetAplicadas.literal.importeTotRetener,
			factSJCS.busquedaRetAplicadas.literal.importeTotRetenido,"
		columnSizes="36,6,8,8,8,8,8,8,8,2" 
		modal="M" >
		
	<c:forEach items="${consultaLEC}" var="registro" varStatus="status">    
	
		<siga:FilaConIconos 
			fila='${status.count}'
			botones="" 
			pintarEspacio="no"
			visibleConsulta="no"
			visibleEdicion = "no"
			visibleBorrado = "no"
			clase="listaNonEdit">
			
			
			
			
			<%-- fila <bean:write name='index'/> escribe la informacion de la linea de comunicacion--%>
			<td align="left"><c:out value="${registro['NOMBREPAGO']}"/></td>
			<td align="center"> <c:out value="${registro['ANIO']}"/> / <c:out value="${registro['MES']}"/></td>
			
		<c:choose>
			<c:when test="${registro['TIPORETENCION']=='L'}">
			<td align="right"><c:out value="${registro['IMPORTEANTAPLICARETENCION']}"/> &euro;</td>
			<td align="right"><c:out value="${registro['IMPORTEANTRETENIDO']}"/> &euro;</td>
			<td align="right"><c:out value="${registro['IMPORTESMI']}"/> &euro;</td>
			</c:when>
			<c:otherwise>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			</c:otherwise>
		</c:choose>
			
			<td align="right"><c:out value="${registro['IMPORTEAPLICARETENCION']}"/> &euro;</td>
			<td align="right"><c:out value="${registro['IMPORTERETENIDO']}"/> &euro;</td>
			
		<c:choose>
			<c:when test="${registro['TIPORETENCION']=='L'}">
			<td align="right"><c:out value="${registro['IMPORTETOTAPLICARETENCION']}"/> &euro;</td>
			<td align="right"><c:out value="${registro['IMPORTETOTRETENIDO']}"/> &euro;</td>
			</c:when>
			<c:otherwise>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			</c:otherwise>
		</c:choose>
		
			<td>&nbsp;</td>
		</siga:FilaConIconos>
		
	</c:forEach>
	
	</siga:Table>

	<siga:ConjBotonesAccion botones="C" modal="M"/>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>
	
</body>
</html>
