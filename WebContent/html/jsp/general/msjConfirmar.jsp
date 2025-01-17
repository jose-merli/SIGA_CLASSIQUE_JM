<!DOCTYPE html>
<html>
<head>
<!-- msjConfirmar.jsp -->
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


<script src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>" type="text/javascript"></script>




	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

<script type="text/javascript">
		//Asociada al boton Cancelar
		function accionCancelar() 
		{	
			window.top.close();	
		}
		function onload() 
		{	
			

			msjConfirmacion = document.getElementById("msjConfimacion"); 
			
						
			if(msjConfirmacion && msjConfirmacion.value!=''){
				if (confirm(msjConfirmacion.value)){
					accionSiguiente();
				}
			}
			return true;
		}
		//Asociada al boton Siguiente
		function accionSiguiente() 
		{	sub();
			document.forms[0].submit();
			
		}

</script>

</head>

<body onload=onload();>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<bean:define id="msjConfimacion" name="msjConfimacion" scope="request"/>
<bean:define id="cerrarModal" name="cerrarModal" scope="request"/>

<input type="hidden" id="msjConfimacion" value="${msjConfimacion}">
<html:form action="${path}"  method="POST" >
	<html:hidden property="modo"/>
	<html:hidden property="parametros" />
</html:form>
<script type="text/javascript">
</script>

</body>

</html>

