<!DOCTYPE html>
<html>
<head>
<!-- listadoTrabajosSJCSPendientes.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>




<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>

	
</head> 

<body  >
<siga:ConjCampos leyenda="Trabajos SJCS Pendientes">

	
		
	<siga:Table 
				name="listado" 
				border="1" 
				columnNames="gratuita.gestionInscripciones.trasbajosSJCSPendientes.incidencia,gratuita.gestionInscripciones.trasbajosSJCSPendientes.descripcion" 
				columnSizes="50,50">
		<c:choose>
   				<c:when test="${empty trabajosSJCSPendientes}">
   				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>	 		
	 			
   			</c:when>
   			<c:otherwise>
	   			
					<c:forEach items="${trabajosSJCSPendientes}" var="trabajoSJCSPendientes" varStatus="status">
						
						<siga:FilaConIconos	fila='${status.count}'				    
			  				pintarEspacio="no"
			  				botones=""
			  				visibleBorrado="N"
			  				visibleEdicion="N"
			  				visibleConsulta="N"
			  				clase="listaNonEdit">
							<td align='left'><c:out value="${trabajoSJCSPendientes.INCIDENCIA}"/></td>
							<td align='left'><c:out value="${trabajoSJCSPendientes.DESCRIPCION}"/></td>
					</siga:FilaConIconos>
				</c:forEach>
				
		</c:otherwise>
	</c:choose>
	</siga:Table>
	
	
</siga:ConjCampos>
</body>
</html>