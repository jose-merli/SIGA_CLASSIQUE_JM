<!-- consultaLEC.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<html>
<head>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

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

<body onLoad="ajusteAlto('resultado');">
<bean:define id="consultaLEC" name="consultaLEC" scope="request"/>


<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="38">
	<tr>
		<td id="titulo" class="titulitosDatos"><siga:Idioma
			key="factSJCS.busquedaRetAplicadas.titulo.importesImplicados" /></td>
	</tr>
</table>
<siga:Table 
	name="tablaResultados" 
	border="2"
	columnNames="factSJCS.busquedaRetAplicadas.literal.importeSMI,
		factSJCS.busquedaRetAplicadas.literal.colegiado ,
		factSJCS.busquedaRetAplicadas.literal.pagoRelacionado,
		factSJCS.busquedaRetAplicadas.literal.anyomes,
		factSJCS.busquedaRetAplicadas.literal.importeAntRetener,
		factSJCS.busquedaRetAplicadas.literal.importeAntRetenido,
		factSJCS.busquedaRetAplicadas.literal.importeMesRetener,
		factSJCS.busquedaRetAplicadas.literal.importeTotRetener,
		factSJCS.busquedaRetAplicadas.literal.importeTotRetenido,
		factSJCS.busquedaRetAplicadas.literal.importeMesRetenido"
	columnSizes="10,20,10,8,8,8,8,8,10,10" 
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
				<td align="right"><c:out value="${registro['IMPORTESMI']}"/>&nbsp;</td>
				<td align="left"><c:out value="${registro['COLEGIADO']}"/></td>
				<td align="left"><c:out value="${registro['NOMBREPAGO']}"/></td>
				<td align="center"> <c:out value="${registro['ANIO']}"/>/<c:out value="${registro['MES']}"/></td>
				<td align="right"><c:out value="${registro['IMPORTEANTAPLICARETENCION']}"/>&nbsp;</td>
				<td align="right"><c:out value="${registro['IMPORTEANTRETENIDO']}"/>&nbsp;</td>
				<td align="right"><c:out value="${registro['IMPORTEAPLICARETENCION']}"/>&nbsp;</td>
				<td align="right"> <c:out value="${registro['IMPORTETOTAPLICARETENCION']}"/>&nbsp;</td>
				<td align="right"><c:out value="${registro['IMPORTETOTRETENIDO']}"/>&nbsp;</td>
				<td align="right"><c:out value="${registro['IMPORTERETENIDO']}"/>&nbsp;</td>
			</siga:FilaConIconos>
			
		
</c:forEach>

</siga:Table>
<siga:ConjBotonesAccion botones="C" modal="M"/>

<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>
</body>
</html>

