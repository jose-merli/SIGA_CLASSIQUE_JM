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
			<siga:Idioma key="facturacion.nuevo.fichero.abonos"/>
		</td>
	</tr>
</table>
<div id="camposRegistro" class="posicionModalMedia" align="center">
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<html:javascript formName="ficheroBancarioAbonosForm" staticJavascript="false" /> 
<html:form action="${path}" target="submitArea">
<html:hidden property="modo" value ="${ficheroBancarioAbonosForm.modo}"  />
<html:hidden property="listaSufijoProp"/>
	<div>
		<siga:ConjCampos leyenda="facturacion.propositos">
			<input type="hidden" name="sjcs" value="">
			<table class="tablaCampos" colspan=2>
				<tr>		
					<td class="labelText">
						<siga:Idioma key="factSJCS.abonos.configuracion.literal.proposito.SEPA"/>
					</td>
					<td>
						<bean:define id="listaPropositosSEPA" name="listaPropositosSEPA" scope="request"/>
						<bean:define id="idpropDefSEPA" name="idpropDefSEPA" scope="request"/>
						<html:select styleId="comboPropositosSEPA" property="idpropSEPA" value="${idpropDefSEPA}" styleClass="boxCombo" style="width:150px;" >
						<html:option value=""><c:out value=""/></html:option>
						<c:forEach items="${listaPropositosSEPA}" var="propSEPACmb">
							<html:option value="${propSEPACmb.idProposito}"><c:out value="${propSEPACmb.codigo} ${propSEPACmb.nombre}"/></html:option>
						</c:forEach>
						</html:select>	
					
					</td>
				</tr>
				<tr>
					<td class="labelText"> 
						<siga:Idioma key="factSJCS.abonos.configuracion.literal.proposito.otras"/>
					</td>
					<td>
						<bean:define id="listaPropositosOtros" name="listaPropositosOtros" scope="request"/>
						<bean:define id="idpropDefOtros" name="idpropDefOtros" scope="request"/>
						<html:select styleId="comboPropositosOtros" property="idpropOtros" value="${idpropDefOtros}" styleClass="boxCombo" style="width:150px;" >
						<html:option value=""><c:out value=""/></html:option>
						<c:forEach items="${listaPropositosOtros}" var="propOtrosCmb">
							<html:option value="${propOtrosCmb.idProposito}"><c:out value="${propOtrosCmb.codigo} ${propOtrosCmb.nombre}"/></html:option>
						</c:forEach>
						</html:select>	
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
	</div>
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
		
		document.ficheroBancarioAbonosForm.modo.value = "generarFichero";
		document.ficheroBancarioAbonosForm.submit();
		fin();
		
// 		var f = document.all.ficheroBancarioAbonosForm.name;	
<%-- 		window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=facturacion.ficheroBancarioAbonos.mensaje.generandoFicheros'; --%>
		
	}	
	
</script>
</body>
</html>