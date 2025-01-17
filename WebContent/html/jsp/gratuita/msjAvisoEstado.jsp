<!DOCTYPE html>
<html>
<head>
<!-- msjAvisoEstado.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>





<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
<script
	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"
	type="text/javascript"></script>

<script language="JavaScript" type="text/javascript">
		//Asociada al boton Cancelar
		function accionCancelar() {	
			window.top.close();	
		}
		
		function comprobarEstado() {	
			if(document.InscripcionTGForm.estadoPendientes &&document.InscripcionTGForm.estadoPendientes!=''){
				if(confirm(document.InscripcionTGForm.estadoPendientes.value)) {
					accionSiguiente();
				}
			}
			return true;
		}
		//Asociada al boton Siguiente
		function accionSiguiente() {	
			sub();
			document.InscripcionTGForm.submit();
		}

</script>

</head>

<body onload=comprobarEstado();>
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"
		property="path" scope="request" />
	<html:form action="${path}" method="POST">
		<html:hidden property="modo" />
		<html:hidden property="estadoPendientes" />

	</html:form>

</body>

</html>

