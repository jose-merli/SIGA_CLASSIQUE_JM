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

<!-- INI ******* CAPA DE PRESENTACION ****** -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="facturacion.seleccionSerie.titulo"/>
		</td>
	</tr>
</table>
<div id="camposRegistro" class="posicionModalMedia" align="center">
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<html:javascript formName="CuentasBancariasForm" staticJavascript="false" /> 
<html:form action="${path}" target="submitArea">
	<html:hidden property="modo" value ="${CuentasBancariasForm.modo}"  />
	<html:hidden property="idInstitucion" value ="${CuentasBancariasForm.idInstitucion}"/>
	<html:hidden property="bancosCodigo" value ="${CuentasBancariasForm.bancosCodigo}"/>
    <input type="hidden" id="actionModal" name="actionModal" />	
    <siga:ConjCampos leyenda="Datos de la serie">
	<table  class="tablaCentralCamposMedia"  align="center">
	<c:choose>
  		<c:when test="${empty listaSeriesDisponibles}">
   			<tr class="notFound">
	   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>	 		
   		</c:when>
   			<c:otherwise>
				<tr>		
					<td class="labelText">
						<siga:Idioma key="facturacion.nuevaPrevisionFacturacion.literal.serieFacturacion"/>
					</td>
					<td>
						<bean:define id="listaSeriesDisponibles" name="listaSeriesDisponibles" scope="request"/>
						<html:select styleId="comboSeries" property="idSerieFacturacion" value="" styleClass="boxCombo" style="width:200px;" >
						<html:option value=""><c:out value=""/></html:option>
						<c:forEach items="${listaSeriesDisponibles}" var="seriesCmb">
							<html:option value="${seriesCmb.idseriefacturacion}"><c:out value="${seriesCmb.nombreabreviado}"/></html:option>
						</c:forEach>
						</html:select>	
					
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="facturacion.sufijos.literal.sufijo"/>
					</td>
					<td>
					<bean:define id="listaSufijos" name="listaSufijos" scope="request"/> 
					<html:select styleId="comboSufijos" property="idSufijoSerie" value="" styleClass="boxCombo" style="width:200px;">
					<html:option value=""><c:out value=""/></html:option>
					<c:forEach items="${listaSufijos}" var="sufijoSerieCmb">
						<html:option value="${sufijoSerieCmb.idSufijo}">
						<c:if	test="${sufijoSerieCmb.sufijo.trim().length()>0}">
							<c:out value="${sufijoSerieCmb.sufijo} ${sufijoSerieCmb.concepto}"/>
						</c:if>
						<c:if	test="${sufijoSerieCmb.sufijo.trim().length()==0}">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${sufijoSerieCmb.concepto}"/>
						</c:if>
						</html:option>
					</c:forEach>
					</html:select>	
					</td>
				</tr>	
			</c:otherwise>
		</c:choose>
	</table>
	</siga:ConjCampos>
	
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

		if(document.CuentasBancariasForm.idSerieFacturacion.value<1) {
			mensaje = "<siga:Idioma key='facturacion.message.error.cuenta.serie'/>";
			alert(mensaje);
			fin();
			return false;
		}
		
		
		if(document.CuentasBancariasForm.idSufijoSerie.value<1) {
			mensaje = "<siga:Idioma key='facturacion.sufijos.message.error.cuenta.serie.sufijo'/>";
			alert(mensaje);
			fin();
			return false;
		}
		
		document.CuentasBancariasForm.modo.value="guardarRelacionSerie";
		document.CuentasBancariasForm.submit();
		fin();
		
	}	
	
</script>
</body>
</html>