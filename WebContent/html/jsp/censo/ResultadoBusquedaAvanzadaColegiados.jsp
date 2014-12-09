<!DOCTYPE html>
<html>
<head>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="displaytag.tld" 		prefix="display"%>
<%@ taglib uri="struts-html.tld" 		prefix="html"%>
<%@ taglib uri="struts-bean.tld" 		prefix="bean"%>
<%@ taglib uri="libreria_SIGA.tld"		prefix="siga"%>
<%@ taglib uri="struts-logic.tld" 		prefix="logic"%>
<%@ taglib uri="c.tld" 					prefix="c"%>
<%@ taglib uri="CheckboxDisplaytag.tld" prefix="cb"%>
<%@ taglib uri="javascript.tld" 		prefix="js"%>
<%@ taglib uri="css.tld" 				prefix="css"%>
<%@ taglib uri="displaytagScroll.tld" 	prefix="dts"%>

<%@ page import="com.atos.utils.ClsConstants"%>



<%@ page import="com.siga.comun.action.SessionForms"%>

<!-- ResultadoBusquedaAvanzadaColegiados.jsp -->

	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
	<meta http-equiv="Content-Type" 	content="text/html; charset=ISO-8859-1">
	<meta http-equiv="Expires" 			content="0">
	<meta http-equiv="Pragma" 			content="no-cache">
	<meta http-equiv="Cache-Control" 	content="no-cache">	
	<css:css 		relativePath="/html/jsp/general/" 		files="stylesheet2.jsp" />
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<css:css 		relativePath="/html/css/" 				files="ajaxtags.css" />
	<css:css 	   	relativePath="/html/css/themes/" 		files="default.css,mac_os_x.css" />	
	<js:javascript 	relativePath="/html/js/overlibmws/"		files="overlibmws.js" />
	<js:javascript 	relativePath="/html/jsp/censo/"			files="busquedaColegiados.js.jsp" />
	
	<siga:Titulo titulo="censo.busquedaClientesAvanzada.colegiados.titulo" localizacion="censo.busquedaClientes.localizacion" />
	<html:base />
</head>

<body onload="ajusteAltoDisplayTag('divWindowDataScroll'); selectRow('<bean:write name='BusquedaColegiadosForm' property='tableName' scope='request' />', '<bean:write name='BusquedaColegiadosForm' property='id' scope='request' />', '0');">
	<html:errors />

	<bean:define id="accessType" name="USRBEAN" property="accessType" scope="session"/>
	<bean:define id="modo" name="BusquedaColegiadosForm" property="modo" scope="request" />

<html:form action="/CEN_BusquedaColegiados.do" method="POST">	
	<html:hidden property="accion" value="abrirAvanzada" />
	<html:hidden property="id" value="" />
</html:form>

<div class="tablaTitulo titulitos" style="width:100%">
	<siga:Idioma key="censo.busquedaClientesAvanzada.literal.resultados" />
</div>

	<%@ include file="/html/jsp/censo/ResultadoBusquedaColegiados.jsp" %>
	<siga:ConjBotonesAccion botonera="${BusquedaColegiadosForm.botonesAccion}" />
</body>

</html>


<script language="Javascript">
	function accionVolver(){
		document.BusquedaColegiadosForm.accion.value="abrirAvanzada";
		document.BusquedaColegiadosForm.action = document.BusquedaColegiadosForm.action + "?" + "<%=SessionForms.getForm(request,"/CEN_BusquedaColegiados.do","buscarAvanzada")%>";
		document.BusquedaColegiadosForm.submit();	
	}
</script>


  

