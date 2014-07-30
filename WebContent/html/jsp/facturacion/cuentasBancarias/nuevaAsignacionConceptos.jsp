<!DOCTYPE html>
<html>
<head>
<!--nuevaAsignacionConceptos.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type"
	content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="org.redabogacia.sigaservices.app.autogen.model.FacSeriefacturacion"%>
<%@ page import="java.util.List"%>

<!-- HEAD -->

<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
<script src="<html:rewrite page="/html/js/SIGA.js?v=${sessionScope.VERSIONJS}"/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
</head>

<body>

<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq"><siga:Idioma key="facturacion.previsionesFacturacion.literal.seriesFacturacion"/>
		</td>
	</tr>
</table>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<html:javascript formName="CuentasBancariasForm" staticJavascript="false" /> 
<html:form action="${path}" target="submitArea">
	<html:hidden property="modo" value ="${CuentasBancariasForm.modo}"  />
	<html:hidden property="idInstitucion" value ="${CuentasBancariasForm.idInstitucion}"/>
	<html:hidden property="bancosCodigo" value ="${CuentasBancariasForm.bancosCodigo}"/>
    <input type="hidden" id="actionModal" name="actionModal" />
	<table width="100%" border="0">
		<siga:Table 
			name="listado" 
			border="1" 
			columnNames=",Serie,Descripción" 
			columnSizes="10,30,60"
			 modal="P">
		<c:choose>
  				<c:when test="${empty listaSeriesDisponibles}">
   				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>	 		
   			</c:when>
   			<c:otherwise>
				<c:forEach items="${listaSeriesDisponibles}" var="listaSeriesDisponibles" varStatus="status">															
					<siga:FilaConIconos	fila='${status.count}'				    
			  			pintarEspacio="no"
			  			botones=""
			  			visibleBorrado="N"
			  			visibleEdicion="N"
			  			visibleConsulta="N"
			  			clase="listaNonEdit">
							<td> 
								<input type="radio" name="idSerieFacturacion" value="${listaSeriesDisponibles.idseriefacturacion}"> 
							</td>
							<td align='left'>
								<input type="hidden" name="idseriefacturacion_${status.count}" id="idseriefacturacion_${status.count}" value="${listaSeriesDisponibles.idseriefacturacion}"/>
								<c:out	value="${listaSeriesDisponibles.nombreabreviado}" />
							</td>
							<td align='left'>
								<c:out value="${listaSeriesDisponibles.descripcion}" />
							</td>
					</siga:FilaConIconos>
				</c:forEach>
			</c:otherwise>
		</c:choose>

	</siga:Table>
	</table>
</html:form>
		
	<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES ACCION -->
	<siga:ConjBotonesAccion botones="Y,C" modal="P" />
	<!-- FIN: BOTONES ACCION -->

<!-- FIN ******* CAPA DE PRESENTACION ****** -->
</div>			
	
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>
<script language="JavaScript">
	function accionCerrar() 
	{		
		top.cierraConParametros("NORMAL");
	}
	
	function accionGuardarCerrar(){
		sub();				
		document.CuentasBancariasForm.modo.value="guardarRelacionSerie";
		document.CuentasBancariasForm.submit();
		fin();
		
	}	
	
</script>
</body>
</html>